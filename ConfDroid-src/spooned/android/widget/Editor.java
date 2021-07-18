/**
 * Copyright (C) 2012 The Android Open Source Project
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
 * Helper class used by TextView to handle editable text views.
 *
 * @unknown 
 */
public class Editor {
    private static final java.lang.String TAG = "Editor";

    private static final boolean DEBUG_UNDO = false;

    // Specifies whether to use or not the magnifier when pressing the insertion or selection
    // handles.
    private static final boolean FLAG_USE_MAGNIFIER = true;

    static final int BLINK = 500;

    private static final int DRAG_SHADOW_MAX_TEXT_LENGTH = 20;

    private static final float LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS = 0.5F;

    private static final int UNSET_X_VALUE = -1;

    private static final int UNSET_LINE = -1;

    // Tag used when the Editor maintains its own separate UndoManager.
    private static final java.lang.String UNDO_OWNER_TAG = "Editor";

    // Ordering constants used to place the Action Mode or context menu items in their menu.
    private static final int MENU_ITEM_ORDER_ASSIST = 0;

    private static final int MENU_ITEM_ORDER_UNDO = 2;

    private static final int MENU_ITEM_ORDER_REDO = 3;

    private static final int MENU_ITEM_ORDER_CUT = 4;

    private static final int MENU_ITEM_ORDER_COPY = 5;

    private static final int MENU_ITEM_ORDER_PASTE = 6;

    private static final int MENU_ITEM_ORDER_SHARE = 7;

    private static final int MENU_ITEM_ORDER_SELECT_ALL = 8;

    private static final int MENU_ITEM_ORDER_REPLACE = 9;

    private static final int MENU_ITEM_ORDER_AUTOFILL = 10;

    private static final int MENU_ITEM_ORDER_PASTE_AS_PLAIN_TEXT = 11;

    private static final int MENU_ITEM_ORDER_SECONDARY_ASSIST_ACTIONS_START = 50;

    private static final int MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START = 100;

    @android.annotation.IntDef({ android.widget.Editor.MagnifierHandleTrigger.SELECTION_START, android.widget.Editor.MagnifierHandleTrigger.SELECTION_END, android.widget.Editor.MagnifierHandleTrigger.INSERTION })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface MagnifierHandleTrigger {
        int INSERTION = 0;

        int SELECTION_START = 1;

        int SELECTION_END = 2;
    }

    @android.annotation.IntDef({ android.widget.Editor.TextActionMode.SELECTION, android.widget.Editor.TextActionMode.INSERTION, android.widget.Editor.TextActionMode.TEXT_LINK })
    @interface TextActionMode {
        int SELECTION = 0;

        int INSERTION = 1;

        int TEXT_LINK = 2;
    }

    // Each Editor manages its own undo stack.
    private final android.content.UndoManager mUndoManager = new android.content.UndoManager();

    private android.content.UndoOwner mUndoOwner = mUndoManager.getOwner(android.widget.Editor.UNDO_OWNER_TAG, this);

    final android.widget.Editor.UndoInputFilter mUndoInputFilter = new android.widget.Editor.UndoInputFilter(this);

    boolean mAllowUndo = true;

    private final com.android.internal.logging.MetricsLogger mMetricsLogger = new com.android.internal.logging.MetricsLogger();

    // Cursor Controllers.
    private android.widget.Editor.InsertionPointCursorController mInsertionPointCursorController;

    android.widget.Editor.SelectionModifierCursorController mSelectionModifierCursorController;

    // Action mode used when text is selected or when actions on an insertion cursor are triggered.
    private android.view.ActionMode mTextActionMode;

    @android.annotation.UnsupportedAppUsage
    private boolean mInsertionControllerEnabled;

    @android.annotation.UnsupportedAppUsage
    private boolean mSelectionControllerEnabled;

    private final boolean mHapticTextHandleEnabled;

    private final android.widget.Editor.MagnifierMotionAnimator mMagnifierAnimator;

    private final java.lang.Runnable mUpdateMagnifierRunnable = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            mMagnifierAnimator.update();
        }
    };

    // Update the magnifier contents whenever anything in the view hierarchy is updated.
    // Note: this only captures UI thread-visible changes, so it's a known issue that an animating
    // VectorDrawable or Ripple animation will not trigger capture, since they're owned by
    // RenderThread.
    private final android.view.ViewTreeObserver.OnDrawListener mMagnifierOnDrawListener = new android.view.ViewTreeObserver.OnDrawListener() {
        @java.lang.Override
        public void onDraw() {
            if (mMagnifierAnimator != null) {
                // Posting the method will ensure that updating the magnifier contents will
                // happen right after the rendering of the current frame.
                mTextView.post(mUpdateMagnifierRunnable);
            }
        }
    };

    // Used to highlight a word when it is corrected by the IME
    private android.widget.Editor.CorrectionHighlighter mCorrectionHighlighter;

    android.widget.Editor.InputContentType mInputContentType;

    android.widget.Editor.InputMethodState mInputMethodState;

    private static class TextRenderNode {
        // Render node has 3 recording states:
        // 1. Recorded operations are valid.
        // #needsRecord() returns false, but needsToBeShifted is false.
        // 2. Recorded operations are not valid, but just the position needed to be updated.
        // #needsRecord() returns false, but needsToBeShifted is true.
        // 3. Recorded operations are not valid. Need to record operations. #needsRecord() returns
        // true.
        android.graphics.RenderNode renderNode;

        boolean isDirty;

        // Becomes true when recorded operations can be reused, but the position has to be updated.
        boolean needsToBeShifted;

        public TextRenderNode(java.lang.String name) {
            renderNode = android.graphics.RenderNode.create(name, null);
            isDirty = true;
            needsToBeShifted = true;
        }

        boolean needsRecord() {
            return isDirty || (!renderNode.hasDisplayList());
        }
    }

    private android.widget.Editor.TextRenderNode[] mTextRenderNodes;

    boolean mFrozenWithFocus;

    boolean mSelectionMoved;

    boolean mTouchFocusSelected;

    android.text.method.KeyListener mKeyListener;

    int mInputType = TYPE_NULL;

    boolean mDiscardNextActionUp;

    boolean mIgnoreActionUpEvent;

    /**
     * To set a custom cursor, you should use {@link TextView#setTextCursorDrawable(Drawable)}
     * or {@link TextView#setTextCursorDrawable(int)}.
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private long mShowCursor;

    private boolean mRenderCursorRegardlessTiming;

    private android.widget.Editor.Blink mBlink;

    boolean mCursorVisible = true;

    boolean mSelectAllOnFocus;

    boolean mTextIsSelectable;

    java.lang.CharSequence mError;

    boolean mErrorWasChanged;

    private android.widget.Editor.ErrorPopup mErrorPopup;

    /**
     * This flag is set if the TextView tries to display an error before it
     * is attached to the window (so its position is still unknown).
     * It causes the error to be shown later, when onAttachedToWindow()
     * is called.
     */
    private boolean mShowErrorAfterAttach;

    boolean mInBatchEditControllers;

    @android.annotation.UnsupportedAppUsage
    boolean mShowSoftInputOnFocus = true;

    private boolean mPreserveSelection;

    private boolean mRestartActionModeOnNextRefresh;

    private boolean mRequestingLinkActionMode;

    private android.widget.SelectionActionModeHelper mSelectionActionModeHelper;

    boolean mIsBeingLongClicked;

    private android.widget.Editor.SuggestionsPopupWindow mSuggestionsPopupWindow;

    android.text.style.SuggestionRangeSpan mSuggestionRangeSpan;

    private java.lang.Runnable mShowSuggestionRunnable;

    android.graphics.drawable.Drawable mDrawableForCursor = null;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    android.graphics.drawable.Drawable mSelectHandleLeft;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    android.graphics.drawable.Drawable mSelectHandleRight;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    android.graphics.drawable.Drawable mSelectHandleCenter;

    // Global listener that detects changes in the global position of the TextView
    private android.widget.Editor.PositionListener mPositionListener;

    private float mLastDownPositionX;

    private float mLastDownPositionY;

    private float mLastUpPositionX;

    private float mLastUpPositionY;

    private float mContextMenuAnchorX;

    private float mContextMenuAnchorY;

    android.view.ActionMode.Callback mCustomSelectionActionModeCallback;

    android.view.ActionMode.Callback mCustomInsertionActionModeCallback;

    // Set when this TextView gained focus with some text selected. Will start selection mode.
    @android.annotation.UnsupportedAppUsage
    boolean mCreatedWithASelection;

    // Indicates the current tap state (first tap, double tap, or triple click).
    private int mTapState = android.widget.Editor.TAP_STATE_INITIAL;

    private long mLastTouchUpTime = 0;

    private static final int TAP_STATE_INITIAL = 0;

    private static final int TAP_STATE_FIRST_TAP = 1;

    private static final int TAP_STATE_DOUBLE_TAP = 2;

    // Only for mouse input.
    private static final int TAP_STATE_TRIPLE_CLICK = 3;

    // The button state as of the last time #onTouchEvent is called.
    private int mLastButtonState;

    private java.lang.Runnable mInsertionActionModeRunnable;

    // The span controller helps monitoring the changes to which the Editor needs to react:
    // - EasyEditSpans, for which we have some UI to display on attach and on hide
    // - SelectionSpans, for which we need to call updateSelection if an IME is attached
    private android.widget.Editor.SpanController mSpanController;

    private android.text.method.WordIterator mWordIterator;

    android.widget.SpellChecker mSpellChecker;

    // This word iterator is set with text and used to determine word boundaries
    // when a user is selecting text.
    private android.text.method.WordIterator mWordIteratorWithText;

    // Indicate that the text in the word iterator needs to be updated.
    private boolean mUpdateWordIteratorText;

    private android.graphics.Rect mTempRect;

    private final android.widget.TextView mTextView;

    final android.widget.Editor.ProcessTextIntentActionsHandler mProcessTextIntentActionsHandler;

    private final android.widget.Editor.CursorAnchorInfoNotifier mCursorAnchorInfoNotifier = new android.widget.Editor.CursorAnchorInfoNotifier();

    private final java.lang.Runnable mShowFloatingToolbar = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            if (mTextActionMode != null) {
                mTextActionMode.hide(0);// hide off.

            }
        }
    };

    boolean mIsInsertionActionModeStartPending = false;

    private final android.widget.Editor.SuggestionHelper mSuggestionHelper = new android.widget.Editor.SuggestionHelper();

    Editor(android.widget.TextView textView) {
        mTextView = textView;
        // Synchronize the filter list, which places the undo input filter at the end.
        mTextView.setFilters(mTextView.getFilters());
        mProcessTextIntentActionsHandler = new android.widget.Editor.ProcessTextIntentActionsHandler(this);
        mHapticTextHandleEnabled = mTextView.getContext().getResources().getBoolean(com.android.internal.R.bool.config_enableHapticTextHandle);
        if (android.widget.Editor.FLAG_USE_MAGNIFIER) {
            final android.widget.Magnifier magnifier = android.widget.Magnifier.createBuilderWithOldMagnifierDefaults(mTextView).build();
            mMagnifierAnimator = new android.widget.Editor.MagnifierMotionAnimator(magnifier);
        }
    }

    android.os.ParcelableParcel saveInstanceState() {
        android.os.ParcelableParcel state = new android.os.ParcelableParcel(getClass().getClassLoader());
        android.os.Parcel parcel = state.getParcel();
        mUndoManager.saveInstanceState(parcel);
        mUndoInputFilter.saveInstanceState(parcel);
        return state;
    }

    void restoreInstanceState(android.os.ParcelableParcel state) {
        android.os.Parcel parcel = state.getParcel();
        mUndoManager.restoreInstanceState(parcel, state.getClassLoader());
        mUndoInputFilter.restoreInstanceState(parcel);
        // Re-associate this object as the owner of undo state.
        mUndoOwner = mUndoManager.getOwner(android.widget.Editor.UNDO_OWNER_TAG, this);
    }

    /**
     * Forgets all undo and redo operations for this Editor.
     */
    void forgetUndoRedo() {
        android.content.UndoOwner[] owners = new android.content.UndoOwner[]{ mUndoOwner };
        /* all */
        mUndoManager.forgetUndos(owners, -1);
        /* all */
        mUndoManager.forgetRedos(owners, -1);
    }

    boolean canUndo() {
        android.content.UndoOwner[] owners = new android.content.UndoOwner[]{ mUndoOwner };
        return mAllowUndo && (mUndoManager.countUndos(owners) > 0);
    }

    boolean canRedo() {
        android.content.UndoOwner[] owners = new android.content.UndoOwner[]{ mUndoOwner };
        return mAllowUndo && (mUndoManager.countRedos(owners) > 0);
    }

    void undo() {
        if (!mAllowUndo) {
            return;
        }
        android.content.UndoOwner[] owners = new android.content.UndoOwner[]{ mUndoOwner };
        mUndoManager.undo(owners, 1);// Undo 1 action.

    }

    void redo() {
        if (!mAllowUndo) {
            return;
        }
        android.content.UndoOwner[] owners = new android.content.UndoOwner[]{ mUndoOwner };
        mUndoManager.redo(owners, 1);// Redo 1 action.

    }

    void replace() {
        if (mSuggestionsPopupWindow == null) {
            mSuggestionsPopupWindow = new android.widget.Editor.SuggestionsPopupWindow();
        }
        hideCursorAndSpanControllers();
        mSuggestionsPopupWindow.show();
        int middle = (mTextView.getSelectionStart() + mTextView.getSelectionEnd()) / 2;
        android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), middle);
    }

    void onAttachedToWindow() {
        if (mShowErrorAfterAttach) {
            showError();
            mShowErrorAfterAttach = false;
        }
        final android.view.ViewTreeObserver observer = mTextView.getViewTreeObserver();
        if (observer.isAlive()) {
            // No need to create the controller.
            // The get method will add the listener on controller creation.
            if (mInsertionPointCursorController != null) {
                observer.addOnTouchModeChangeListener(mInsertionPointCursorController);
            }
            if (mSelectionModifierCursorController != null) {
                mSelectionModifierCursorController.resetTouchOffsets();
                observer.addOnTouchModeChangeListener(mSelectionModifierCursorController);
            }
            if (android.widget.Editor.FLAG_USE_MAGNIFIER) {
                observer.addOnDrawListener(mMagnifierOnDrawListener);
            }
        }
        /* create the spell checker if needed */
        updateSpellCheckSpans(0, mTextView.getText().length(), true);
        if (mTextView.hasSelection()) {
            refreshTextActionMode();
        }
        getPositionListener().addSubscriber(mCursorAnchorInfoNotifier, true);
        resumeBlink();
    }

    void onDetachedFromWindow() {
        getPositionListener().removeSubscriber(mCursorAnchorInfoNotifier);
        if (mError != null) {
            hideError();
        }
        suspendBlink();
        if (mInsertionPointCursorController != null) {
            mInsertionPointCursorController.onDetached();
        }
        if (mSelectionModifierCursorController != null) {
            mSelectionModifierCursorController.onDetached();
        }
        if (mShowSuggestionRunnable != null) {
            mTextView.removeCallbacks(mShowSuggestionRunnable);
        }
        // Cancel the single tap delayed runnable.
        if (mInsertionActionModeRunnable != null) {
            mTextView.removeCallbacks(mInsertionActionModeRunnable);
        }
        mTextView.removeCallbacks(mShowFloatingToolbar);
        discardTextDisplayLists();
        if (mSpellChecker != null) {
            mSpellChecker.closeSession();
            // Forces the creation of a new SpellChecker next time this window is created.
            // Will handle the cases where the settings has been changed in the meantime.
            mSpellChecker = null;
        }
        if (android.widget.Editor.FLAG_USE_MAGNIFIER) {
            final android.view.ViewTreeObserver observer = mTextView.getViewTreeObserver();
            if (observer.isAlive()) {
                observer.removeOnDrawListener(mMagnifierOnDrawListener);
            }
        }
        hideCursorAndSpanControllers();
        stopTextActionModeWithPreservingSelection();
    }

    private void discardTextDisplayLists() {
        if (mTextRenderNodes != null) {
            for (int i = 0; i < mTextRenderNodes.length; i++) {
                android.graphics.RenderNode displayList = (mTextRenderNodes[i] != null) ? mTextRenderNodes[i].renderNode : null;
                if ((displayList != null) && displayList.hasDisplayList()) {
                    displayList.discardDisplayList();
                }
            }
        }
    }

    private void showError() {
        if (mTextView.getWindowToken() == null) {
            mShowErrorAfterAttach = true;
            return;
        }
        if (mErrorPopup == null) {
            android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mTextView.getContext());
            final android.widget.TextView err = ((android.widget.TextView) (inflater.inflate(com.android.internal.R.layout.textview_hint, null)));
            final float scale = mTextView.getResources().getDisplayMetrics().density;
            mErrorPopup = new android.widget.Editor.ErrorPopup(err, ((int) ((200 * scale) + 0.5F)), ((int) ((50 * scale) + 0.5F)));
            mErrorPopup.setFocusable(false);
            // The user is entering text, so the input method is needed.  We
            // don't want the popup to be displayed on top of it.
            mErrorPopup.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NEEDED);
        }
        android.widget.TextView tv = ((android.widget.TextView) (mErrorPopup.getContentView()));
        chooseSize(mErrorPopup, mError, tv);
        tv.setText(mError);
        mErrorPopup.showAsDropDown(mTextView, getErrorX(), getErrorY(), android.view.Gravity.TOP | android.view.Gravity.LEFT);
        mErrorPopup.fixDirection(mErrorPopup.isAboveAnchor());
    }

    public void setError(java.lang.CharSequence error, android.graphics.drawable.Drawable icon) {
        mError = android.text.TextUtils.stringOrSpannedString(error);
        mErrorWasChanged = true;
        if (mError == null) {
            setErrorIcon(null);
            if (mErrorPopup != null) {
                if (mErrorPopup.isShowing()) {
                    mErrorPopup.dismiss();
                }
                mErrorPopup = null;
            }
            mShowErrorAfterAttach = false;
        } else {
            setErrorIcon(icon);
            if (mTextView.isFocused()) {
                showError();
            }
        }
    }

    private void setErrorIcon(android.graphics.drawable.Drawable icon) {
        android.widget.TextView.Drawables dr = mTextView.mDrawables;
        if (dr == null) {
            mTextView.mDrawables = dr = new android.widget.TextView.Drawables(mTextView.getContext());
        }
        dr.setErrorDrawable(icon, mTextView);
        mTextView.resetResolvedDrawables();
        mTextView.invalidate();
        mTextView.requestLayout();
    }

    private void hideError() {
        if (mErrorPopup != null) {
            if (mErrorPopup.isShowing()) {
                mErrorPopup.dismiss();
            }
        }
        mShowErrorAfterAttach = false;
    }

    /**
     * Returns the X offset to make the pointy top of the error point
     * at the middle of the error icon.
     */
    private int getErrorX() {
        /* The "25" is the distance between the point and the right edge
        of the background
         */
        final float scale = mTextView.getResources().getDisplayMetrics().density;
        final android.widget.TextView.Drawables dr = mTextView.mDrawables;
        final int layoutDirection = mTextView.getLayoutDirection();
        int errorX;
        int offset;
        switch (layoutDirection) {
            default :
            case android.view.View.LAYOUT_DIRECTION_LTR :
                offset = ((-(dr != null ? dr.mDrawableSizeRight : 0)) / 2) + ((int) ((25 * scale) + 0.5F));
                errorX = ((mTextView.getWidth() - mErrorPopup.getWidth()) - mTextView.getPaddingRight()) + offset;
                break;
            case android.view.View.LAYOUT_DIRECTION_RTL :
                offset = ((dr != null ? dr.mDrawableSizeLeft : 0) / 2) - ((int) ((25 * scale) + 0.5F));
                errorX = mTextView.getPaddingLeft() + offset;
                break;
        }
        return errorX;
    }

    /**
     * Returns the Y offset to make the pointy top of the error point
     * at the bottom of the error icon.
     */
    private int getErrorY() {
        /* Compound, not extended, because the icon is not clipped
        if the text height is smaller.
         */
        final int compoundPaddingTop = mTextView.getCompoundPaddingTop();
        int vspace = ((mTextView.getBottom() - mTextView.getTop()) - mTextView.getCompoundPaddingBottom()) - compoundPaddingTop;
        final android.widget.TextView.Drawables dr = mTextView.mDrawables;
        final int layoutDirection = mTextView.getLayoutDirection();
        int height;
        switch (layoutDirection) {
            default :
            case android.view.View.LAYOUT_DIRECTION_LTR :
                height = (dr != null) ? dr.mDrawableHeightRight : 0;
                break;
            case android.view.View.LAYOUT_DIRECTION_RTL :
                height = (dr != null) ? dr.mDrawableHeightLeft : 0;
                break;
        }
        int icontop = compoundPaddingTop + ((vspace - height) / 2);
        /* The "2" is the distance between the point and the top edge
        of the background.
         */
        final float scale = mTextView.getResources().getDisplayMetrics().density;
        return ((icontop + height) - mTextView.getHeight()) - ((int) ((2 * scale) + 0.5F));
    }

    void createInputContentTypeIfNeeded() {
        if (mInputContentType == null) {
            mInputContentType = new android.widget.Editor.InputContentType();
        }
    }

    void createInputMethodStateIfNeeded() {
        if (mInputMethodState == null) {
            mInputMethodState = new android.widget.Editor.InputMethodState();
        }
    }

    private boolean isCursorVisible() {
        // The default value is true, even when there is no associated Editor
        return mCursorVisible && mTextView.isTextEditable();
    }

    boolean shouldRenderCursor() {
        if (!isCursorVisible()) {
            return false;
        }
        if (mRenderCursorRegardlessTiming) {
            return true;
        }
        final long showCursorDelta = android.os.SystemClock.uptimeMillis() - mShowCursor;
        return (showCursorDelta % (2 * android.widget.Editor.BLINK)) < android.widget.Editor.BLINK;
    }

    void prepareCursorControllers() {
        boolean windowSupportsHandles = false;
        android.view.ViewGroup.LayoutParams params = mTextView.getRootView().getLayoutParams();
        if (params instanceof android.view.WindowManager.LayoutParams) {
            android.view.WindowManager.LayoutParams windowParams = ((android.view.WindowManager.LayoutParams) (params));
            windowSupportsHandles = (windowParams.type < android.view.WindowManager.LayoutParams.FIRST_SUB_WINDOW) || (windowParams.type > android.view.WindowManager.LayoutParams.LAST_SUB_WINDOW);
        }
        boolean enabled = windowSupportsHandles && (mTextView.getLayout() != null);
        mInsertionControllerEnabled = enabled && isCursorVisible();
        mSelectionControllerEnabled = enabled && mTextView.textCanBeSelected();
        if (!mInsertionControllerEnabled) {
            hideInsertionPointCursorController();
            if (mInsertionPointCursorController != null) {
                mInsertionPointCursorController.onDetached();
                mInsertionPointCursorController = null;
            }
        }
        if (!mSelectionControllerEnabled) {
            stopTextActionMode();
            if (mSelectionModifierCursorController != null) {
                mSelectionModifierCursorController.onDetached();
                mSelectionModifierCursorController = null;
            }
        }
    }

    void hideInsertionPointCursorController() {
        if (mInsertionPointCursorController != null) {
            mInsertionPointCursorController.hide();
        }
    }

    /**
     * Hides the insertion and span controllers.
     */
    void hideCursorAndSpanControllers() {
        hideCursorControllers();
        hideSpanControllers();
    }

    private void hideSpanControllers() {
        if (mSpanController != null) {
            mSpanController.hide();
        }
    }

    private void hideCursorControllers() {
        // When mTextView is not ExtractEditText, we need to distinguish two kinds of focus-lost.
        // One is the true focus lost where suggestions pop-up (if any) should be dismissed, and the
        // other is an side effect of showing the suggestions pop-up itself. We use isShowingUp()
        // to distinguish one from the other.
        if ((mSuggestionsPopupWindow != null) && (mTextView.isInExtractedMode() || (!mSuggestionsPopupWindow.isShowingUp()))) {
            // Should be done before hide insertion point controller since it triggers a show of it
            mSuggestionsPopupWindow.hide();
        }
        hideInsertionPointCursorController();
    }

    /**
     * Create new SpellCheckSpans on the modified region.
     */
    private void updateSpellCheckSpans(int start, int end, boolean createSpellChecker) {
        // Remove spans whose adjacent characters are text not punctuation
        mTextView.removeAdjacentSuggestionSpans(start);
        mTextView.removeAdjacentSuggestionSpans(end);
        if ((mTextView.isTextEditable() && mTextView.isSuggestionsEnabled()) && (!mTextView.isInExtractedMode())) {
            if ((mSpellChecker == null) && createSpellChecker) {
                mSpellChecker = new android.widget.SpellChecker(mTextView);
            }
            if (mSpellChecker != null) {
                mSpellChecker.spellCheck(start, end);
            }
        }
    }

    void onScreenStateChanged(int screenState) {
        switch (screenState) {
            case android.view.View.SCREEN_STATE_ON :
                resumeBlink();
                break;
            case android.view.View.SCREEN_STATE_OFF :
                suspendBlink();
                break;
        }
    }

    private void suspendBlink() {
        if (mBlink != null) {
            mBlink.cancel();
        }
    }

    private void resumeBlink() {
        if (mBlink != null) {
            mBlink.uncancel();
            makeBlink();
        }
    }

    void adjustInputType(boolean password, boolean passwordInputType, boolean webPasswordInputType, boolean numberPasswordInputType) {
        // mInputType has been set from inputType, possibly modified by mInputMethod.
        // Specialize mInputType to [web]password if we have a text class and the original input
        // type was a password.
        if ((mInputType & TYPE_MASK_CLASS) == TYPE_CLASS_TEXT) {
            if (password || passwordInputType) {
                mInputType = (mInputType & (~TYPE_MASK_VARIATION)) | TYPE_TEXT_VARIATION_PASSWORD;
            }
            if (webPasswordInputType) {
                mInputType = (mInputType & (~TYPE_MASK_VARIATION)) | TYPE_TEXT_VARIATION_WEB_PASSWORD;
            }
        } else
            if ((mInputType & TYPE_MASK_CLASS) == TYPE_CLASS_NUMBER) {
                if (numberPasswordInputType) {
                    mInputType = (mInputType & (~TYPE_MASK_VARIATION)) | TYPE_NUMBER_VARIATION_PASSWORD;
                }
            }

    }

    private void chooseSize(@android.annotation.NonNull
    android.widget.PopupWindow pop, @android.annotation.NonNull
    java.lang.CharSequence text, @android.annotation.NonNull
    android.widget.TextView tv) {
        final int wid = tv.getPaddingLeft() + tv.getPaddingRight();
        final int ht = tv.getPaddingTop() + tv.getPaddingBottom();
        final int defaultWidthInPixels = mTextView.getResources().getDimensionPixelSize(com.android.internal.R.dimen.textview_error_popup_default_width);
        final android.text.StaticLayout l = StaticLayout.Builder.obtain(text, 0, text.length(), tv.getPaint(), defaultWidthInPixels).setUseLineSpacingFromFallbacks(tv.mUseFallbackLineSpacing).build();
        float max = 0;
        for (int i = 0; i < l.getLineCount(); i++) {
            max = java.lang.Math.max(max, l.getLineWidth(i));
        }
        /* Now set the popup size to be big enough for the text plus the border capped
        to DEFAULT_MAX_POPUP_WIDTH
         */
        pop.setWidth(wid + ((int) (java.lang.Math.ceil(max))));
        pop.setHeight(ht + l.getHeight());
    }

    void setFrame() {
        if (mErrorPopup != null) {
            android.widget.TextView tv = ((android.widget.TextView) (mErrorPopup.getContentView()));
            chooseSize(mErrorPopup, mError, tv);
            mErrorPopup.update(mTextView, getErrorX(), getErrorY(), mErrorPopup.getWidth(), mErrorPopup.getHeight());
        }
    }

    private int getWordStart(int offset) {
        // FIXME - For this and similar methods we're not doing anything to check if there's
        // a LocaleSpan in the text, this may be something we should try handling or checking for.
        int retOffset = getWordIteratorWithText().prevBoundary(offset);
        if (getWordIteratorWithText().isOnPunctuation(retOffset)) {
            // On punctuation boundary or within group of punctuation, find punctuation start.
            retOffset = getWordIteratorWithText().getPunctuationBeginning(offset);
        } else {
            // Not on a punctuation boundary, find the word start.
            retOffset = getWordIteratorWithText().getPrevWordBeginningOnTwoWordsBoundary(offset);
        }
        if (retOffset == java.text.BreakIterator.DONE) {
            return offset;
        }
        return retOffset;
    }

    private int getWordEnd(int offset) {
        int retOffset = getWordIteratorWithText().nextBoundary(offset);
        if (getWordIteratorWithText().isAfterPunctuation(retOffset)) {
            // On punctuation boundary or within group of punctuation, find punctuation end.
            retOffset = getWordIteratorWithText().getPunctuationEnd(offset);
        } else {
            // Not on a punctuation boundary, find the word end.
            retOffset = getWordIteratorWithText().getNextWordEndOnTwoWordBoundary(offset);
        }
        if (retOffset == java.text.BreakIterator.DONE) {
            return offset;
        }
        return retOffset;
    }

    private boolean needsToSelectAllToSelectWordOrParagraph() {
        if (mTextView.hasPasswordTransformationMethod()) {
            // Always select all on a password field.
            // Cut/copy menu entries are not available for passwords, but being able to select all
            // is however useful to delete or paste to replace the entire content.
            return true;
        }
        int inputType = mTextView.getInputType();
        int klass = inputType & android.text.InputType.TYPE_MASK_CLASS;
        int variation = inputType & android.text.InputType.TYPE_MASK_VARIATION;
        // Specific text field types: select the entire text for these
        if (((((((klass == android.text.InputType.TYPE_CLASS_NUMBER) || (klass == android.text.InputType.TYPE_CLASS_PHONE)) || (klass == android.text.InputType.TYPE_CLASS_DATETIME)) || (variation == android.text.InputType.TYPE_TEXT_VARIATION_URI)) || (variation == android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)) || (variation == android.text.InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS)) || (variation == android.text.InputType.TYPE_TEXT_VARIATION_FILTER)) {
            return true;
        }
        return false;
    }

    /**
     * Adjusts selection to the word under last touch offset. Return true if the operation was
     * successfully performed.
     */
    boolean selectCurrentWord() {
        if (!mTextView.canSelectText()) {
            return false;
        }
        if (needsToSelectAllToSelectWordOrParagraph()) {
            return mTextView.selectAllText();
        }
        long lastTouchOffsets = getLastTouchOffsets();
        final int minOffset = android.text.TextUtils.unpackRangeStartFromLong(lastTouchOffsets);
        final int maxOffset = android.text.TextUtils.unpackRangeEndFromLong(lastTouchOffsets);
        // Safety check in case standard touch event handling has been bypassed
        if ((minOffset < 0) || (minOffset > mTextView.getText().length()))
            return false;

        if ((maxOffset < 0) || (maxOffset > mTextView.getText().length()))
            return false;

        int selectionStart;
        int selectionEnd;
        // If a URLSpan (web address, email, phone...) is found at that position, select it.
        android.text.style.URLSpan[] urlSpans = ((android.text.Spanned) (mTextView.getText())).getSpans(minOffset, maxOffset, android.text.style.URLSpan.class);
        if (urlSpans.length >= 1) {
            android.text.style.URLSpan urlSpan = urlSpans[0];
            selectionStart = ((android.text.Spanned) (mTextView.getText())).getSpanStart(urlSpan);
            selectionEnd = ((android.text.Spanned) (mTextView.getText())).getSpanEnd(urlSpan);
        } else {
            // FIXME - We should check if there's a LocaleSpan in the text, this may be
            // something we should try handling or checking for.
            final android.text.method.WordIterator wordIterator = getWordIterator();
            wordIterator.setCharSequence(mTextView.getText(), minOffset, maxOffset);
            selectionStart = wordIterator.getBeginning(minOffset);
            selectionEnd = wordIterator.getEnd(maxOffset);
            if (((selectionStart == java.text.BreakIterator.DONE) || (selectionEnd == java.text.BreakIterator.DONE)) || (selectionStart == selectionEnd)) {
                // Possible when the word iterator does not properly handle the text's language
                long range = getCharClusterRange(minOffset);
                selectionStart = android.text.TextUtils.unpackRangeStartFromLong(range);
                selectionEnd = android.text.TextUtils.unpackRangeEndFromLong(range);
            }
        }
        android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), selectionStart, selectionEnd);
        return selectionEnd > selectionStart;
    }

    /**
     * Adjusts selection to the paragraph under last touch offset. Return true if the operation was
     * successfully performed.
     */
    private boolean selectCurrentParagraph() {
        if (!mTextView.canSelectText()) {
            return false;
        }
        if (needsToSelectAllToSelectWordOrParagraph()) {
            return mTextView.selectAllText();
        }
        long lastTouchOffsets = getLastTouchOffsets();
        final int minLastTouchOffset = android.text.TextUtils.unpackRangeStartFromLong(lastTouchOffsets);
        final int maxLastTouchOffset = android.text.TextUtils.unpackRangeEndFromLong(lastTouchOffsets);
        final long paragraphsRange = getParagraphsRange(minLastTouchOffset, maxLastTouchOffset);
        final int start = android.text.TextUtils.unpackRangeStartFromLong(paragraphsRange);
        final int end = android.text.TextUtils.unpackRangeEndFromLong(paragraphsRange);
        if (start < end) {
            android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), start, end);
            return true;
        }
        return false;
    }

    /**
     * Get the minimum range of paragraphs that contains startOffset and endOffset.
     */
    private long getParagraphsRange(int startOffset, int endOffset) {
        final android.text.Layout layout = mTextView.getLayout();
        if (layout == null) {
            return android.text.TextUtils.packRangeInLong(-1, -1);
        }
        final java.lang.CharSequence text = mTextView.getText();
        int minLine = layout.getLineForOffset(startOffset);
        // Search paragraph start.
        while (minLine > 0) {
            final int prevLineEndOffset = layout.getLineEnd(minLine - 1);
            if (text.charAt(prevLineEndOffset - 1) == '\n') {
                break;
            }
            minLine--;
        } 
        int maxLine = layout.getLineForOffset(endOffset);
        // Search paragraph end.
        while (maxLine < (layout.getLineCount() - 1)) {
            final int lineEndOffset = layout.getLineEnd(maxLine);
            if (text.charAt(lineEndOffset - 1) == '\n') {
                break;
            }
            maxLine++;
        } 
        return android.text.TextUtils.packRangeInLong(layout.getLineStart(minLine), layout.getLineEnd(maxLine));
    }

    void onLocaleChanged() {
        // Will be re-created on demand in getWordIterator and getWordIteratorWithText with the
        // proper new locale
        mWordIterator = null;
        mWordIteratorWithText = null;
    }

    public android.text.method.WordIterator getWordIterator() {
        if (mWordIterator == null) {
            mWordIterator = new android.text.method.WordIterator(mTextView.getTextServicesLocale());
        }
        return mWordIterator;
    }

    private android.text.method.WordIterator getWordIteratorWithText() {
        if (mWordIteratorWithText == null) {
            mWordIteratorWithText = new android.text.method.WordIterator(mTextView.getTextServicesLocale());
            mUpdateWordIteratorText = true;
        }
        if (mUpdateWordIteratorText) {
            // FIXME - Shouldn't copy all of the text as only the area of the text relevant
            // to the user's selection is needed. A possible solution would be to
            // copy some number N of characters near the selection and then when the
            // user approaches N then we'd do another copy of the next N characters.
            java.lang.CharSequence text = mTextView.getText();
            mWordIteratorWithText.setCharSequence(text, 0, text.length());
            mUpdateWordIteratorText = false;
        }
        return mWordIteratorWithText;
    }

    private int getNextCursorOffset(int offset, boolean findAfterGivenOffset) {
        final android.text.Layout layout = mTextView.getLayout();
        if (layout == null)
            return offset;

        return findAfterGivenOffset == layout.isRtlCharAt(offset) ? layout.getOffsetToLeftOf(offset) : layout.getOffsetToRightOf(offset);
    }

    private long getCharClusterRange(int offset) {
        final int textLength = mTextView.getText().length();
        if (offset < textLength) {
            final int clusterEndOffset = getNextCursorOffset(offset, true);
            return android.text.TextUtils.packRangeInLong(getNextCursorOffset(clusterEndOffset, false), clusterEndOffset);
        }
        if ((offset - 1) >= 0) {
            final int clusterStartOffset = getNextCursorOffset(offset, false);
            return android.text.TextUtils.packRangeInLong(clusterStartOffset, getNextCursorOffset(clusterStartOffset, true));
        }
        return android.text.TextUtils.packRangeInLong(offset, offset);
    }

    private boolean touchPositionIsInSelection() {
        int selectionStart = mTextView.getSelectionStart();
        int selectionEnd = mTextView.getSelectionEnd();
        if (selectionStart == selectionEnd) {
            return false;
        }
        if (selectionStart > selectionEnd) {
            int tmp = selectionStart;
            selectionStart = selectionEnd;
            selectionEnd = tmp;
            android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), selectionStart, selectionEnd);
        }
        android.widget.Editor.SelectionModifierCursorController selectionController = getSelectionController();
        int minOffset = selectionController.getMinTouchOffset();
        int maxOffset = selectionController.getMaxTouchOffset();
        return (minOffset >= selectionStart) && (maxOffset < selectionEnd);
    }

    private android.widget.Editor.PositionListener getPositionListener() {
        if (mPositionListener == null) {
            mPositionListener = new android.widget.Editor.PositionListener();
        }
        return mPositionListener;
    }

    private interface TextViewPositionListener {
        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled);
    }

    private boolean isOffsetVisible(int offset) {
        android.text.Layout layout = mTextView.getLayout();
        if (layout == null)
            return false;

        final int line = layout.getLineForOffset(offset);
        final int lineBottom = layout.getLineBottom(line);
        final int primaryHorizontal = ((int) (layout.getPrimaryHorizontal(offset)));
        return mTextView.isPositionVisible(primaryHorizontal + mTextView.viewportToContentHorizontalOffset(), lineBottom + mTextView.viewportToContentVerticalOffset());
    }

    /**
     * Returns true if the screen coordinates position (x,y) corresponds to a character displayed
     * in the view. Returns false when the position is in the empty space of left/right of text.
     */
    private boolean isPositionOnText(float x, float y) {
        android.text.Layout layout = mTextView.getLayout();
        if (layout == null)
            return false;

        final int line = mTextView.getLineAtCoordinate(y);
        x = mTextView.convertToLocalHorizontalCoordinate(x);
        if (x < layout.getLineLeft(line))
            return false;

        if (x > layout.getLineRight(line))
            return false;

        return true;
    }

    private void startDragAndDrop() {
        getSelectionActionModeHelper().onSelectionDrag();
        // TODO: Fix drag and drop in full screen extracted mode.
        if (mTextView.isInExtractedMode()) {
            return;
        }
        final int start = mTextView.getSelectionStart();
        final int end = mTextView.getSelectionEnd();
        java.lang.CharSequence selectedText = mTextView.getTransformedText(start, end);
        android.content.ClipData data = android.content.ClipData.newPlainText(null, selectedText);
        android.widget.Editor.DragLocalState localState = new android.widget.Editor.DragLocalState(mTextView, start, end);
        mTextView.startDragAndDrop(data, getTextThumbnailBuilder(start, end), localState, android.view.View.DRAG_FLAG_GLOBAL);
        stopTextActionMode();
        if (hasSelectionController()) {
            getSelectionController().resetTouchOffsets();
        }
    }

    public boolean performLongClick(boolean handled) {
        // Long press in empty space moves cursor and starts the insertion action mode.
        if (((!handled) && (!isPositionOnText(mLastDownPositionX, mLastDownPositionY))) && mInsertionControllerEnabled) {
            final int offset = mTextView.getOffsetForPosition(mLastDownPositionX, mLastDownPositionY);
            android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), offset);
            getInsertionController().show();
            mIsInsertionActionModeStartPending = true;
            handled = true;
            com.android.internal.logging.MetricsLogger.action(mTextView.getContext(), MetricsEvent.TEXT_LONGPRESS, android.widget.TextViewMetrics.SUBTYPE_LONG_PRESS_OTHER);
        }
        if ((!handled) && (mTextActionMode != null)) {
            if (touchPositionIsInSelection()) {
                startDragAndDrop();
                com.android.internal.logging.MetricsLogger.action(mTextView.getContext(), MetricsEvent.TEXT_LONGPRESS, android.widget.TextViewMetrics.SUBTYPE_LONG_PRESS_DRAG_AND_DROP);
            } else {
                stopTextActionMode();
                selectCurrentWordAndStartDrag();
                com.android.internal.logging.MetricsLogger.action(mTextView.getContext(), MetricsEvent.TEXT_LONGPRESS, android.widget.TextViewMetrics.SUBTYPE_LONG_PRESS_SELECTION);
            }
            handled = true;
        }
        // Start a new selection
        if (!handled) {
            handled = selectCurrentWordAndStartDrag();
            if (handled) {
                com.android.internal.logging.MetricsLogger.action(mTextView.getContext(), MetricsEvent.TEXT_LONGPRESS, android.widget.TextViewMetrics.SUBTYPE_LONG_PRESS_SELECTION);
            }
        }
        return handled;
    }

    float getLastUpPositionX() {
        return mLastUpPositionX;
    }

    float getLastUpPositionY() {
        return mLastUpPositionY;
    }

    private long getLastTouchOffsets() {
        android.widget.Editor.SelectionModifierCursorController selectionController = getSelectionController();
        final int minOffset = selectionController.getMinTouchOffset();
        final int maxOffset = selectionController.getMaxTouchOffset();
        return android.text.TextUtils.packRangeInLong(minOffset, maxOffset);
    }

    void onFocusChanged(boolean focused, int direction) {
        mShowCursor = android.os.SystemClock.uptimeMillis();
        ensureEndedBatchEdit();
        if (focused) {
            int selStart = mTextView.getSelectionStart();
            int selEnd = mTextView.getSelectionEnd();
            // SelectAllOnFocus fields are highlighted and not selected. Do not start text selection
            // mode for these, unless there was a specific selection already started.
            final boolean isFocusHighlighted = (mSelectAllOnFocus && (selStart == 0)) && (selEnd == mTextView.getText().length());
            mCreatedWithASelection = (mFrozenWithFocus && mTextView.hasSelection()) && (!isFocusHighlighted);
            if ((!mFrozenWithFocus) || ((selStart < 0) || (selEnd < 0))) {
                // If a tap was used to give focus to that view, move cursor at tap position.
                // Has to be done before onTakeFocus, which can be overloaded.
                final int lastTapPosition = getLastTapPosition();
                if (lastTapPosition >= 0) {
                    android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), lastTapPosition);
                }
                // Note this may have to be moved out of the Editor class
                android.text.method.MovementMethod mMovement = mTextView.getMovementMethod();
                if (mMovement != null) {
                    mMovement.onTakeFocus(mTextView, ((android.text.Spannable) (mTextView.getText())), direction);
                }
                // The DecorView does not have focus when the 'Done' ExtractEditText button is
                // pressed. Since it is the ViewAncestor's mView, it requests focus before
                // ExtractEditText clears focus, which gives focus to the ExtractEditText.
                // This special case ensure that we keep current selection in that case.
                // It would be better to know why the DecorView does not have focus at that time.
                if (((mTextView.isInExtractedMode() || mSelectionMoved) && (selStart >= 0)) && (selEnd >= 0)) {
                    /* Someone intentionally set the selection, so let them
                    do whatever it is that they wanted to do instead of
                    the default on-focus behavior.  We reset the selection
                    here instead of just skipping the onTakeFocus() call
                    because some movement methods do something other than
                    just setting the selection in theirs and we still
                    need to go through that path.
                     */
                    android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), selStart, selEnd);
                }
                if (mSelectAllOnFocus) {
                    mTextView.selectAllText();
                }
                mTouchFocusSelected = true;
            }
            mFrozenWithFocus = false;
            mSelectionMoved = false;
            if (mError != null) {
                showError();
            }
            makeBlink();
        } else {
            if (mError != null) {
                hideError();
            }
            // Don't leave us in the middle of a batch edit.
            mTextView.onEndBatchEdit();
            if (mTextView.isInExtractedMode()) {
                hideCursorAndSpanControllers();
                stopTextActionModeWithPreservingSelection();
            } else {
                hideCursorAndSpanControllers();
                if (mTextView.isTemporarilyDetached()) {
                    stopTextActionModeWithPreservingSelection();
                } else {
                    stopTextActionMode();
                }
                downgradeEasyCorrectionSpans();
            }
            // No need to create the controller
            if (mSelectionModifierCursorController != null) {
                mSelectionModifierCursorController.resetTouchOffsets();
            }
            ensureNoSelectionIfNonSelectable();
        }
    }

    private void ensureNoSelectionIfNonSelectable() {
        // This could be the case if a TextLink has been tapped.
        if ((!mTextView.textCanBeSelected()) && mTextView.hasSelection()) {
            android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), mTextView.length(), mTextView.length());
        }
    }

    /**
     * Downgrades to simple suggestions all the easy correction spans that are not a spell check
     * span.
     */
    private void downgradeEasyCorrectionSpans() {
        java.lang.CharSequence text = mTextView.getText();
        if (text instanceof android.text.Spannable) {
            android.text.Spannable spannable = ((android.text.Spannable) (text));
            android.text.style.SuggestionSpan[] suggestionSpans = spannable.getSpans(0, spannable.length(), android.text.style.SuggestionSpan.class);
            for (int i = 0; i < suggestionSpans.length; i++) {
                int flags = suggestionSpans[i].getFlags();
                if (((flags & android.text.style.SuggestionSpan.FLAG_EASY_CORRECT) != 0) && ((flags & android.text.style.SuggestionSpan.FLAG_MISSPELLED) == 0)) {
                    flags &= ~android.text.style.SuggestionSpan.FLAG_EASY_CORRECT;
                    suggestionSpans[i].setFlags(flags);
                }
            }
        }
    }

    void sendOnTextChanged(int start, int before, int after) {
        getSelectionActionModeHelper().onTextChanged(start, start + before);
        updateSpellCheckSpans(start, start + after, false);
        // Flip flag to indicate the word iterator needs to have the text reset.
        mUpdateWordIteratorText = true;
        // Hide the controllers as soon as text is modified (typing, procedural...)
        // We do not hide the span controllers, since they can be added when a new text is
        // inserted into the text view (voice IME).
        hideCursorControllers();
        // Reset drag accelerator.
        if (mSelectionModifierCursorController != null) {
            mSelectionModifierCursorController.resetTouchOffsets();
        }
        stopTextActionMode();
    }

    private int getLastTapPosition() {
        // No need to create the controller at that point, no last tap position saved
        if (mSelectionModifierCursorController != null) {
            int lastTapPosition = mSelectionModifierCursorController.getMinTouchOffset();
            if (lastTapPosition >= 0) {
                // Safety check, should not be possible.
                if (lastTapPosition > mTextView.getText().length()) {
                    lastTapPosition = mTextView.getText().length();
                }
                return lastTapPosition;
            }
        }
        return -1;
    }

    void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            if (mBlink != null) {
                mBlink.uncancel();
                makeBlink();
            }
            if (mTextView.hasSelection() && (!extractedTextModeWillBeStarted())) {
                refreshTextActionMode();
            }
        } else {
            if (mBlink != null) {
                mBlink.cancel();
            }
            if (mInputContentType != null) {
                mInputContentType.enterDown = false;
            }
            // Order matters! Must be done before onParentLostFocus to rely on isShowingUp
            hideCursorAndSpanControllers();
            stopTextActionModeWithPreservingSelection();
            if (mSuggestionsPopupWindow != null) {
                mSuggestionsPopupWindow.onParentLostFocus();
            }
            // Don't leave us in the middle of a batch edit. Same as in onFocusChanged
            ensureEndedBatchEdit();
            ensureNoSelectionIfNonSelectable();
        }
    }

    private void updateTapState(android.view.MotionEvent event) {
        final int action = event.getActionMasked();
        if (action == android.view.MotionEvent.ACTION_DOWN) {
            final boolean isMouse = event.isFromSource(android.view.InputDevice.SOURCE_MOUSE);
            // Detect double tap and triple click.
            if (((mTapState == android.widget.Editor.TAP_STATE_FIRST_TAP) || ((mTapState == android.widget.Editor.TAP_STATE_DOUBLE_TAP) && isMouse)) && ((android.os.SystemClock.uptimeMillis() - mLastTouchUpTime) <= android.view.ViewConfiguration.getDoubleTapTimeout())) {
                if (mTapState == android.widget.Editor.TAP_STATE_FIRST_TAP) {
                    mTapState = android.widget.Editor.TAP_STATE_DOUBLE_TAP;
                } else {
                    mTapState = android.widget.Editor.TAP_STATE_TRIPLE_CLICK;
                }
            } else {
                mTapState = android.widget.Editor.TAP_STATE_FIRST_TAP;
            }
        }
        if (action == android.view.MotionEvent.ACTION_UP) {
            mLastTouchUpTime = android.os.SystemClock.uptimeMillis();
        }
    }

    private boolean shouldFilterOutTouchEvent(android.view.MotionEvent event) {
        if (!event.isFromSource(android.view.InputDevice.SOURCE_MOUSE)) {
            return false;
        }
        final boolean primaryButtonStateChanged = ((mLastButtonState ^ event.getButtonState()) & android.view.MotionEvent.BUTTON_PRIMARY) != 0;
        final int action = event.getActionMasked();
        if (((action == android.view.MotionEvent.ACTION_DOWN) || (action == android.view.MotionEvent.ACTION_UP)) && (!primaryButtonStateChanged)) {
            return true;
        }
        if ((action == android.view.MotionEvent.ACTION_MOVE) && (!event.isButtonPressed(android.view.MotionEvent.BUTTON_PRIMARY))) {
            return true;
        }
        return false;
    }

    void onTouchEvent(android.view.MotionEvent event) {
        final boolean filterOutEvent = shouldFilterOutTouchEvent(event);
        mLastButtonState = event.getButtonState();
        if (filterOutEvent) {
            if (event.getActionMasked() == android.view.MotionEvent.ACTION_UP) {
                mDiscardNextActionUp = true;
            }
            return;
        }
        updateTapState(event);
        updateFloatingToolbarVisibility(event);
        if (hasSelectionController()) {
            getSelectionController().onTouchEvent(event);
        }
        if (mShowSuggestionRunnable != null) {
            mTextView.removeCallbacks(mShowSuggestionRunnable);
            mShowSuggestionRunnable = null;
        }
        if (event.getActionMasked() == android.view.MotionEvent.ACTION_UP) {
            mLastUpPositionX = event.getX();
            mLastUpPositionY = event.getY();
        }
        if (event.getActionMasked() == android.view.MotionEvent.ACTION_DOWN) {
            mLastDownPositionX = event.getX();
            mLastDownPositionY = event.getY();
            // Reset this state; it will be re-set if super.onTouchEvent
            // causes focus to move to the view.
            mTouchFocusSelected = false;
            mIgnoreActionUpEvent = false;
        }
    }

    private void updateFloatingToolbarVisibility(android.view.MotionEvent event) {
        if (mTextActionMode != null) {
            switch (event.getActionMasked()) {
                case android.view.MotionEvent.ACTION_MOVE :
                    hideFloatingToolbar(android.view.ActionMode.DEFAULT_HIDE_DURATION);
                    break;
                case android.view.MotionEvent.ACTION_UP :
                    // fall through
                case android.view.MotionEvent.ACTION_CANCEL :
                    showFloatingToolbar();
            }
        }
    }

    void hideFloatingToolbar(int duration) {
        if (mTextActionMode != null) {
            mTextView.removeCallbacks(mShowFloatingToolbar);
            mTextActionMode.hide(duration);
        }
    }

    private void showFloatingToolbar() {
        if (mTextActionMode != null) {
            // Delay "show" so it doesn't interfere with click confirmations
            // or double-clicks that could "dismiss" the floating toolbar.
            int delay = android.view.ViewConfiguration.getDoubleTapTimeout();
            mTextView.postDelayed(mShowFloatingToolbar, delay);
            // This classifies the text and most likely returns before the toolbar is actually
            // shown. If not, it will update the toolbar with the result when classification
            // returns. We would rather not wait for a long running classification process.
            invalidateActionModeAsync();
        }
    }

    private android.view.inputmethod.InputMethodManager getInputMethodManager() {
        return mTextView.getContext().getSystemService(android.view.inputmethod.InputMethodManager.class);
    }

    public void beginBatchEdit() {
        mInBatchEditControllers = true;
        final android.widget.Editor.InputMethodState ims = mInputMethodState;
        if (ims != null) {
            int nesting = ++ims.mBatchEditNesting;
            if (nesting == 1) {
                ims.mCursorChanged = false;
                ims.mChangedDelta = 0;
                if (ims.mContentChanged) {
                    // We already have a pending change from somewhere else,
                    // so turn this into a full update.
                    ims.mChangedStart = 0;
                    ims.mChangedEnd = mTextView.getText().length();
                } else {
                    ims.mChangedStart = android.widget.Editor.EXTRACT_UNKNOWN;
                    ims.mChangedEnd = android.widget.Editor.EXTRACT_UNKNOWN;
                    ims.mContentChanged = false;
                }
                mUndoInputFilter.beginBatchEdit();
                mTextView.onBeginBatchEdit();
            }
        }
    }

    public void endBatchEdit() {
        mInBatchEditControllers = false;
        final android.widget.Editor.InputMethodState ims = mInputMethodState;
        if (ims != null) {
            int nesting = --ims.mBatchEditNesting;
            if (nesting == 0) {
                finishBatchEdit(ims);
            }
        }
    }

    void ensureEndedBatchEdit() {
        final android.widget.Editor.InputMethodState ims = mInputMethodState;
        if ((ims != null) && (ims.mBatchEditNesting != 0)) {
            ims.mBatchEditNesting = 0;
            finishBatchEdit(ims);
        }
    }

    void finishBatchEdit(final android.widget.Editor.InputMethodState ims) {
        mTextView.onEndBatchEdit();
        mUndoInputFilter.endBatchEdit();
        if (ims.mContentChanged || ims.mSelectionModeChanged) {
            mTextView.updateAfterEdit();
            reportExtractedText();
        } else
            if (ims.mCursorChanged) {
                // Cheesy way to get us to report the current cursor location.
                mTextView.invalidateCursor();
            }

        // sendUpdateSelection knows to avoid sending if the selection did
        // not actually change.
        sendUpdateSelection();
        // Show drag handles if they were blocked by batch edit mode.
        if (mTextActionMode != null) {
            final android.widget.Editor.CursorController cursorController = (mTextView.hasSelection()) ? getSelectionController() : getInsertionController();
            if (((cursorController != null) && (!cursorController.isActive())) && (!cursorController.isCursorBeingModified())) {
                cursorController.show();
            }
        }
    }

    static final int EXTRACT_NOTHING = -2;

    static final int EXTRACT_UNKNOWN = -1;

    boolean extractText(android.view.inputmethod.ExtractedTextRequest request, android.view.inputmethod.ExtractedText outText) {
        return extractTextInternal(request, android.widget.Editor.EXTRACT_UNKNOWN, android.widget.Editor.EXTRACT_UNKNOWN, android.widget.Editor.EXTRACT_UNKNOWN, outText);
    }

    private boolean extractTextInternal(@android.annotation.Nullable
    android.view.inputmethod.ExtractedTextRequest request, int partialStartOffset, int partialEndOffset, int delta, @android.annotation.Nullable
    android.view.inputmethod.ExtractedText outText) {
        if ((request == null) || (outText == null)) {
            return false;
        }
        final java.lang.CharSequence content = mTextView.getText();
        if (content == null) {
            return false;
        }
        if (partialStartOffset != android.widget.Editor.EXTRACT_NOTHING) {
            final int N = content.length();
            if (partialStartOffset < 0) {
                outText.partialStartOffset = outText.partialEndOffset = -1;
                partialStartOffset = 0;
                partialEndOffset = N;
            } else {
                // Now use the delta to determine the actual amount of text
                // we need.
                partialEndOffset += delta;
                // Adjust offsets to ensure we contain full spans.
                if (content instanceof android.text.Spanned) {
                    android.text.Spanned spanned = ((android.text.Spanned) (content));
                    java.lang.Object[] spans = spanned.getSpans(partialStartOffset, partialEndOffset, android.text.ParcelableSpan.class);
                    int i = spans.length;
                    while (i > 0) {
                        i--;
                        int j = spanned.getSpanStart(spans[i]);
                        if (j < partialStartOffset)
                            partialStartOffset = j;

                        j = spanned.getSpanEnd(spans[i]);
                        if (j > partialEndOffset)
                            partialEndOffset = j;

                    } 
                }
                outText.partialStartOffset = partialStartOffset;
                outText.partialEndOffset = partialEndOffset - delta;
                if (partialStartOffset > N) {
                    partialStartOffset = N;
                } else
                    if (partialStartOffset < 0) {
                        partialStartOffset = 0;
                    }

                if (partialEndOffset > N) {
                    partialEndOffset = N;
                } else
                    if (partialEndOffset < 0) {
                        partialEndOffset = 0;
                    }

            }
            if ((request.flags & android.view.inputmethod.InputConnection.GET_TEXT_WITH_STYLES) != 0) {
                outText.text = content.subSequence(partialStartOffset, partialEndOffset);
            } else {
                outText.text = android.text.TextUtils.substring(content, partialStartOffset, partialEndOffset);
            }
        } else {
            outText.partialStartOffset = 0;
            outText.partialEndOffset = 0;
            outText.text = "";
        }
        outText.flags = 0;
        if (android.text.method.MetaKeyKeyListener.getMetaState(content, MetaKeyKeyListener.META_SELECTING) != 0) {
            outText.flags |= android.view.inputmethod.ExtractedText.FLAG_SELECTING;
        }
        if (mTextView.isSingleLine()) {
            outText.flags |= android.view.inputmethod.ExtractedText.FLAG_SINGLE_LINE;
        }
        outText.startOffset = 0;
        outText.selectionStart = mTextView.getSelectionStart();
        outText.selectionEnd = mTextView.getSelectionEnd();
        outText.hint = mTextView.getHint();
        return true;
    }

    boolean reportExtractedText() {
        final android.widget.Editor.InputMethodState ims = mInputMethodState;
        if (ims == null) {
            return false;
        }
        final boolean wasContentChanged = ims.mContentChanged;
        if ((!wasContentChanged) && (!ims.mSelectionModeChanged)) {
            return false;
        }
        ims.mContentChanged = false;
        ims.mSelectionModeChanged = false;
        final android.view.inputmethod.ExtractedTextRequest req = ims.mExtractedTextRequest;
        if (req == null) {
            return false;
        }
        final android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
        if (imm == null) {
            return false;
        }
        if (android.widget.TextView.DEBUG_EXTRACT) {
            android.util.Log.v(android.widget.TextView.LOG_TAG, (((("Retrieving extracted start=" + ims.mChangedStart) + " end=") + ims.mChangedEnd) + " delta=") + ims.mChangedDelta);
        }
        if ((ims.mChangedStart < 0) && (!wasContentChanged)) {
            ims.mChangedStart = android.widget.Editor.EXTRACT_NOTHING;
        }
        if (extractTextInternal(req, ims.mChangedStart, ims.mChangedEnd, ims.mChangedDelta, ims.mExtractedText)) {
            if (android.widget.TextView.DEBUG_EXTRACT) {
                android.util.Log.v(android.widget.TextView.LOG_TAG, (((("Reporting extracted start=" + ims.mExtractedText.partialStartOffset) + " end=") + ims.mExtractedText.partialEndOffset) + ": ") + ims.mExtractedText.text);
            }
            imm.updateExtractedText(mTextView, req.token, ims.mExtractedText);
            ims.mChangedStart = android.widget.Editor.EXTRACT_UNKNOWN;
            ims.mChangedEnd = android.widget.Editor.EXTRACT_UNKNOWN;
            ims.mChangedDelta = 0;
            ims.mContentChanged = false;
            return true;
        }
        return false;
    }

    private void sendUpdateSelection() {
        if ((null != mInputMethodState) && (mInputMethodState.mBatchEditNesting <= 0)) {
            final android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            if (null != imm) {
                final int selectionStart = mTextView.getSelectionStart();
                final int selectionEnd = mTextView.getSelectionEnd();
                int candStart = -1;
                int candEnd = -1;
                if (mTextView.getText() instanceof android.text.Spannable) {
                    final android.text.Spannable sp = ((android.text.Spannable) (mTextView.getText()));
                    candStart = com.android.internal.widget.EditableInputConnection.getComposingSpanStart(sp);
                    candEnd = com.android.internal.widget.EditableInputConnection.getComposingSpanEnd(sp);
                }
                // InputMethodManager#updateSelection skips sending the message if
                // none of the parameters have changed since the last time we called it.
                imm.updateSelection(mTextView, selectionStart, selectionEnd, candStart, candEnd);
            }
        }
    }

    void onDraw(android.graphics.Canvas canvas, android.text.Layout layout, android.graphics.Path highlight, android.graphics.Paint highlightPaint, int cursorOffsetVertical) {
        final int selectionStart = mTextView.getSelectionStart();
        final int selectionEnd = mTextView.getSelectionEnd();
        final android.widget.Editor.InputMethodState ims = mInputMethodState;
        if ((ims != null) && (ims.mBatchEditNesting == 0)) {
            android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            if (imm != null) {
                if (imm.isActive(mTextView)) {
                    if (ims.mContentChanged || ims.mSelectionModeChanged) {
                        // We are in extract mode and the content has changed
                        // in some way... just report complete new text to the
                        // input method.
                        reportExtractedText();
                    }
                }
            }
        }
        if (mCorrectionHighlighter != null) {
            mCorrectionHighlighter.draw(canvas, cursorOffsetVertical);
        }
        if (((highlight != null) && (selectionStart == selectionEnd)) && (mDrawableForCursor != null)) {
            drawCursor(canvas, cursorOffsetVertical);
            // Rely on the drawable entirely, do not draw the cursor line.
            // Has to be done after the IMM related code above which relies on the highlight.
            highlight = null;
        }
        if (mSelectionActionModeHelper != null) {
            mSelectionActionModeHelper.onDraw(canvas);
            if (mSelectionActionModeHelper.isDrawingHighlight()) {
                highlight = null;
            }
        }
        if (mTextView.canHaveDisplayList() && canvas.isHardwareAccelerated()) {
            drawHardwareAccelerated(canvas, layout, highlight, highlightPaint, cursorOffsetVertical);
        } else {
            layout.draw(canvas, highlight, highlightPaint, cursorOffsetVertical);
        }
    }

    private void drawHardwareAccelerated(android.graphics.Canvas canvas, android.text.Layout layout, android.graphics.Path highlight, android.graphics.Paint highlightPaint, int cursorOffsetVertical) {
        final long lineRange = layout.getLineRangeForDraw(canvas);
        int firstLine = android.text.TextUtils.unpackRangeStartFromLong(lineRange);
        int lastLine = android.text.TextUtils.unpackRangeEndFromLong(lineRange);
        if (lastLine < 0)
            return;

        layout.drawBackground(canvas, highlight, highlightPaint, cursorOffsetVertical, firstLine, lastLine);
        if (layout instanceof android.text.DynamicLayout) {
            if (mTextRenderNodes == null) {
                mTextRenderNodes = com.android.internal.util.ArrayUtils.emptyArray(android.widget.Editor.TextRenderNode.class);
            }
            android.text.DynamicLayout dynamicLayout = ((android.text.DynamicLayout) (layout));
            int[] blockEndLines = dynamicLayout.getBlockEndLines();
            int[] blockIndices = dynamicLayout.getBlockIndices();
            final int numberOfBlocks = dynamicLayout.getNumberOfBlocks();
            final int indexFirstChangedBlock = dynamicLayout.getIndexFirstChangedBlock();
            final android.util.ArraySet<java.lang.Integer> blockSet = dynamicLayout.getBlocksAlwaysNeedToBeRedrawn();
            if (blockSet != null) {
                for (int i = 0; i < blockSet.size(); i++) {
                    final int blockIndex = dynamicLayout.getBlockIndex(blockSet.valueAt(i));
                    if ((blockIndex != android.text.DynamicLayout.INVALID_BLOCK_INDEX) && (mTextRenderNodes[blockIndex] != null)) {
                        mTextRenderNodes[blockIndex].needsToBeShifted = true;
                    }
                }
            }
            int startBlock = java.util.Arrays.binarySearch(blockEndLines, 0, numberOfBlocks, firstLine);
            if (startBlock < 0) {
                startBlock = -(startBlock + 1);
            }
            startBlock = java.lang.Math.min(indexFirstChangedBlock, startBlock);
            int startIndexToFindAvailableRenderNode = 0;
            int lastIndex = numberOfBlocks;
            for (int i = startBlock; i < numberOfBlocks; i++) {
                final int blockIndex = blockIndices[i];
                if (((i >= indexFirstChangedBlock) && (blockIndex != android.text.DynamicLayout.INVALID_BLOCK_INDEX)) && (mTextRenderNodes[blockIndex] != null)) {
                    mTextRenderNodes[blockIndex].needsToBeShifted = true;
                }
                if (blockEndLines[i] < firstLine) {
                    // Blocks in [indexFirstChangedBlock, firstLine) are not redrawn here. They will
                    // be redrawn after they get scrolled into drawing range.
                    continue;
                }
                startIndexToFindAvailableRenderNode = drawHardwareAcceleratedInner(canvas, layout, highlight, highlightPaint, cursorOffsetVertical, blockEndLines, blockIndices, i, numberOfBlocks, startIndexToFindAvailableRenderNode);
                if (blockEndLines[i] >= lastLine) {
                    lastIndex = java.lang.Math.max(indexFirstChangedBlock, i + 1);
                    break;
                }
            }
            if (blockSet != null) {
                for (int i = 0; i < blockSet.size(); i++) {
                    final int block = blockSet.valueAt(i);
                    final int blockIndex = dynamicLayout.getBlockIndex(block);
                    if (((blockIndex == android.text.DynamicLayout.INVALID_BLOCK_INDEX) || (mTextRenderNodes[blockIndex] == null)) || mTextRenderNodes[blockIndex].needsToBeShifted) {
                        startIndexToFindAvailableRenderNode = drawHardwareAcceleratedInner(canvas, layout, highlight, highlightPaint, cursorOffsetVertical, blockEndLines, blockIndices, block, numberOfBlocks, startIndexToFindAvailableRenderNode);
                    }
                }
            }
            dynamicLayout.setIndexFirstChangedBlock(lastIndex);
        } else {
            // Boring layout is used for empty and hint text
            layout.drawText(canvas, firstLine, lastLine);
        }
    }

    private int drawHardwareAcceleratedInner(android.graphics.Canvas canvas, android.text.Layout layout, android.graphics.Path highlight, android.graphics.Paint highlightPaint, int cursorOffsetVertical, int[] blockEndLines, int[] blockIndices, int blockInfoIndex, int numberOfBlocks, int startIndexToFindAvailableRenderNode) {
        final int blockEndLine = blockEndLines[blockInfoIndex];
        int blockIndex = blockIndices[blockInfoIndex];
        final boolean blockIsInvalid = blockIndex == android.text.DynamicLayout.INVALID_BLOCK_INDEX;
        if (blockIsInvalid) {
            blockIndex = getAvailableDisplayListIndex(blockIndices, numberOfBlocks, startIndexToFindAvailableRenderNode);
            // Note how dynamic layout's internal block indices get updated from Editor
            blockIndices[blockInfoIndex] = blockIndex;
            if (mTextRenderNodes[blockIndex] != null) {
                mTextRenderNodes[blockIndex].isDirty = true;
            }
            startIndexToFindAvailableRenderNode = blockIndex + 1;
        }
        if (mTextRenderNodes[blockIndex] == null) {
            mTextRenderNodes[blockIndex] = new android.widget.Editor.TextRenderNode("Text " + blockIndex);
        }
        final boolean blockDisplayListIsInvalid = mTextRenderNodes[blockIndex].needsRecord();
        android.graphics.RenderNode blockDisplayList = mTextRenderNodes[blockIndex].renderNode;
        if (mTextRenderNodes[blockIndex].needsToBeShifted || blockDisplayListIsInvalid) {
            final int blockBeginLine = (blockInfoIndex == 0) ? 0 : blockEndLines[blockInfoIndex - 1] + 1;
            final int top = layout.getLineTop(blockBeginLine);
            final int bottom = layout.getLineBottom(blockEndLine);
            int left = 0;
            int right = mTextView.getWidth();
            if (mTextView.getHorizontallyScrolling()) {
                float min = java.lang.Float.MAX_VALUE;
                float max = java.lang.Float.MIN_VALUE;
                for (int line = blockBeginLine; line <= blockEndLine; line++) {
                    min = java.lang.Math.min(min, layout.getLineLeft(line));
                    max = java.lang.Math.max(max, layout.getLineRight(line));
                }
                left = ((int) (min));
                right = ((int) (max + 0.5F));
            }
            // Rebuild display list if it is invalid
            if (blockDisplayListIsInvalid) {
                final android.graphics.RecordingCanvas recordingCanvas = blockDisplayList.beginRecording(right - left, bottom - top);
                try {
                    // drawText is always relative to TextView's origin, this translation
                    // brings this range of text back to the top left corner of the viewport
                    recordingCanvas.translate(-left, -top);
                    layout.drawText(recordingCanvas, blockBeginLine, blockEndLine);
                    mTextRenderNodes[blockIndex].isDirty = false;
                    // No need to untranslate, previous context is popped after
                    // drawDisplayList
                } finally {
                    blockDisplayList.endRecording();
                    // Same as drawDisplayList below, handled by our TextView's parent
                    blockDisplayList.setClipToBounds(false);
                }
            }
            // Valid display list only needs to update its drawing location.
            blockDisplayList.setLeftTopRightBottom(left, top, right, bottom);
            mTextRenderNodes[blockIndex].needsToBeShifted = false;
        }
        ((android.graphics.RecordingCanvas) (canvas)).drawRenderNode(blockDisplayList);
        return startIndexToFindAvailableRenderNode;
    }

    private int getAvailableDisplayListIndex(int[] blockIndices, int numberOfBlocks, int searchStartIndex) {
        int length = mTextRenderNodes.length;
        for (int i = searchStartIndex; i < length; i++) {
            boolean blockIndexFound = false;
            for (int j = 0; j < numberOfBlocks; j++) {
                if (blockIndices[j] == i) {
                    blockIndexFound = true;
                    break;
                }
            }
            if (blockIndexFound)
                continue;

            return i;
        }
        // No available index found, the pool has to grow
        mTextRenderNodes = com.android.internal.util.GrowingArrayUtils.append(mTextRenderNodes, length, null);
        return length;
    }

    private void drawCursor(android.graphics.Canvas canvas, int cursorOffsetVertical) {
        final boolean translate = cursorOffsetVertical != 0;
        if (translate)
            canvas.translate(0, cursorOffsetVertical);

        if (mDrawableForCursor != null) {
            mDrawableForCursor.draw(canvas);
        }
        if (translate)
            canvas.translate(0, -cursorOffsetVertical);

    }

    void invalidateHandlesAndActionMode() {
        if (mSelectionModifierCursorController != null) {
            mSelectionModifierCursorController.invalidateHandles();
        }
        if (mInsertionPointCursorController != null) {
            mInsertionPointCursorController.invalidateHandle();
        }
        if (mTextActionMode != null) {
            invalidateActionMode();
        }
    }

    /**
     * Invalidates all the sub-display lists that overlap the specified character range
     */
    void invalidateTextDisplayList(android.text.Layout layout, int start, int end) {
        if ((mTextRenderNodes != null) && (layout instanceof android.text.DynamicLayout)) {
            final int firstLine = layout.getLineForOffset(start);
            final int lastLine = layout.getLineForOffset(end);
            android.text.DynamicLayout dynamicLayout = ((android.text.DynamicLayout) (layout));
            int[] blockEndLines = dynamicLayout.getBlockEndLines();
            int[] blockIndices = dynamicLayout.getBlockIndices();
            final int numberOfBlocks = dynamicLayout.getNumberOfBlocks();
            int i = 0;
            // Skip the blocks before firstLine
            while (i < numberOfBlocks) {
                if (blockEndLines[i] >= firstLine)
                    break;

                i++;
            } 
            // Invalidate all subsequent blocks until lastLine is passed
            while (i < numberOfBlocks) {
                final int blockIndex = blockIndices[i];
                if (blockIndex != android.text.DynamicLayout.INVALID_BLOCK_INDEX) {
                    mTextRenderNodes[blockIndex].isDirty = true;
                }
                if (blockEndLines[i] >= lastLine)
                    break;

                i++;
            } 
        }
    }

    @android.annotation.UnsupportedAppUsage
    void invalidateTextDisplayList() {
        if (mTextRenderNodes != null) {
            for (int i = 0; i < mTextRenderNodes.length; i++) {
                if (mTextRenderNodes[i] != null)
                    mTextRenderNodes[i].isDirty = true;

            }
        }
    }

    void updateCursorPosition() {
        loadCursorDrawable();
        if (mDrawableForCursor == null) {
            return;
        }
        final android.text.Layout layout = mTextView.getLayout();
        final int offset = mTextView.getSelectionStart();
        final int line = layout.getLineForOffset(offset);
        final int top = layout.getLineTop(line);
        final int bottom = layout.getLineBottomWithoutSpacing(line);
        final boolean clamped = layout.shouldClampCursor(line);
        updateCursorPosition(top, bottom, layout.getPrimaryHorizontal(offset, clamped));
    }

    void refreshTextActionMode() {
        if (extractedTextModeWillBeStarted()) {
            mRestartActionModeOnNextRefresh = false;
            return;
        }
        final boolean hasSelection = mTextView.hasSelection();
        final android.widget.Editor.SelectionModifierCursorController selectionController = getSelectionController();
        final android.widget.Editor.InsertionPointCursorController insertionController = getInsertionController();
        if (((selectionController != null) && selectionController.isCursorBeingModified()) || ((insertionController != null) && insertionController.isCursorBeingModified())) {
            // ActionMode should be managed by the currently active cursor controller.
            mRestartActionModeOnNextRefresh = false;
            return;
        }
        if (hasSelection) {
            hideInsertionPointCursorController();
            if (mTextActionMode == null) {
                if (mRestartActionModeOnNextRefresh) {
                    // To avoid distraction, newly start action mode only when selection action
                    // mode is being restarted.
                    startSelectionActionModeAsync(false);
                }
            } else
                if ((selectionController == null) || (!selectionController.isActive())) {
                    // Insertion action mode is active. Avoid dismissing the selection.
                    stopTextActionModeWithPreservingSelection();
                    startSelectionActionModeAsync(false);
                } else {
                    mTextActionMode.invalidateContentRect();
                }

        } else {
            // Insertion action mode is started only when insertion controller is explicitly
            // activated.
            if ((insertionController == null) || (!insertionController.isActive())) {
                stopTextActionMode();
            } else
                if (mTextActionMode != null) {
                    mTextActionMode.invalidateContentRect();
                }

        }
        mRestartActionModeOnNextRefresh = false;
    }

    /**
     * Start an Insertion action mode.
     */
    void startInsertionActionMode() {
        if (mInsertionActionModeRunnable != null) {
            mTextView.removeCallbacks(mInsertionActionModeRunnable);
        }
        if (extractedTextModeWillBeStarted()) {
            return;
        }
        stopTextActionMode();
        android.view.ActionMode.Callback actionModeCallback = new android.widget.Editor.TextActionModeCallback(android.widget.Editor.TextActionMode.INSERTION);
        mTextActionMode = mTextView.startActionMode(actionModeCallback, android.view.ActionMode.TYPE_FLOATING);
        if ((mTextActionMode != null) && (getInsertionController() != null)) {
            getInsertionController().show();
        }
    }

    @android.annotation.NonNull
    android.widget.TextView getTextView() {
        return mTextView;
    }

    @android.annotation.Nullable
    android.view.ActionMode getTextActionMode() {
        return mTextActionMode;
    }

    void setRestartActionModeOnNextRefresh(boolean value) {
        mRestartActionModeOnNextRefresh = value;
    }

    /**
     * Asynchronously starts a selection action mode using the TextClassifier.
     */
    void startSelectionActionModeAsync(boolean adjustSelection) {
        getSelectionActionModeHelper().startSelectionActionModeAsync(adjustSelection);
    }

    void startLinkActionModeAsync(int start, int end) {
        if (!(mTextView.getText() instanceof android.text.Spannable)) {
            return;
        }
        stopTextActionMode();
        mRequestingLinkActionMode = true;
        getSelectionActionModeHelper().startLinkActionModeAsync(start, end);
    }

    /**
     * Asynchronously invalidates an action mode using the TextClassifier.
     */
    void invalidateActionModeAsync() {
        getSelectionActionModeHelper().invalidateActionModeAsync();
    }

    /**
     * Synchronously invalidates an action mode without the TextClassifier.
     */
    private void invalidateActionMode() {
        if (mTextActionMode != null) {
            mTextActionMode.invalidate();
        }
    }

    private android.widget.SelectionActionModeHelper getSelectionActionModeHelper() {
        if (mSelectionActionModeHelper == null) {
            mSelectionActionModeHelper = new android.widget.SelectionActionModeHelper(this);
        }
        return mSelectionActionModeHelper;
    }

    /**
     * If the TextView allows text selection, selects the current word when no existing selection
     * was available and starts a drag.
     *
     * @return true if the drag was started.
     */
    private boolean selectCurrentWordAndStartDrag() {
        if (mInsertionActionModeRunnable != null) {
            mTextView.removeCallbacks(mInsertionActionModeRunnable);
        }
        if (extractedTextModeWillBeStarted()) {
            return false;
        }
        if (!checkField()) {
            return false;
        }
        if ((!mTextView.hasSelection()) && (!selectCurrentWord())) {
            // No selection and cannot select a word.
            return false;
        }
        stopTextActionModeWithPreservingSelection();
        getSelectionController().enterDrag(android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_WORD);
        return true;
    }

    /**
     * Checks whether a selection can be performed on the current TextView.
     *
     * @return true if a selection can be performed
     */
    boolean checkField() {
        if ((!mTextView.canSelectText()) || (!mTextView.requestFocus())) {
            android.util.Log.w(android.widget.TextView.LOG_TAG, "TextView does not support text selection. Selection cancelled.");
            return false;
        }
        return true;
    }

    boolean startActionModeInternal(@android.widget.Editor.TextActionMode
    int actionMode) {
        if (extractedTextModeWillBeStarted()) {
            return false;
        }
        if (mTextActionMode != null) {
            // Text action mode is already started
            invalidateActionMode();
            return false;
        }
        if ((actionMode != android.widget.Editor.TextActionMode.TEXT_LINK) && ((!checkField()) || (!mTextView.hasSelection()))) {
            return false;
        }
        android.view.ActionMode.Callback actionModeCallback = new android.widget.Editor.TextActionModeCallback(actionMode);
        mTextActionMode = mTextView.startActionMode(actionModeCallback, android.view.ActionMode.TYPE_FLOATING);
        final boolean selectableText = mTextView.isTextEditable() || mTextView.isTextSelectable();
        if (((actionMode == android.widget.Editor.TextActionMode.TEXT_LINK) && (!selectableText)) && (mTextActionMode instanceof com.android.internal.view.FloatingActionMode)) {
            // Make the toolbar outside-touchable so that it can be dismissed when the user clicks
            // outside of it.
            ((com.android.internal.view.FloatingActionMode) (mTextActionMode)).setOutsideTouchable(true, () -> stopTextActionMode());
        }
        final boolean selectionStarted = mTextActionMode != null;
        if (((selectionStarted && mTextView.isTextEditable()) && (!mTextView.isTextSelectable())) && mShowSoftInputOnFocus) {
            // Show the IME to be able to replace text, except when selecting non editable text.
            final android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            if (imm != null) {
                imm.showSoftInput(mTextView, 0, null);
            }
        }
        return selectionStarted;
    }

    private boolean extractedTextModeWillBeStarted() {
        if (!mTextView.isInExtractedMode()) {
            final android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            return (imm != null) && imm.isFullscreenMode();
        }
        return false;
    }

    /**
     *
     *
     * @return <code>true</code> if it's reasonable to offer to show suggestions depending on
    the current cursor position or selection range. This method is consistent with the
    method to show suggestions {@link SuggestionsPopupWindow#updateSuggestions}.
     */
    private boolean shouldOfferToShowSuggestions() {
        java.lang.CharSequence text = mTextView.getText();
        if (!(text instanceof android.text.Spannable))
            return false;

        final android.text.Spannable spannable = ((android.text.Spannable) (text));
        final int selectionStart = mTextView.getSelectionStart();
        final int selectionEnd = mTextView.getSelectionEnd();
        final android.text.style.SuggestionSpan[] suggestionSpans = spannable.getSpans(selectionStart, selectionEnd, android.text.style.SuggestionSpan.class);
        if (suggestionSpans.length == 0) {
            return false;
        }
        if (selectionStart == selectionEnd) {
            // Spans overlap the cursor.
            for (int i = 0; i < suggestionSpans.length; i++) {
                if (suggestionSpans[i].getSuggestions().length > 0) {
                    return true;
                }
            }
            return false;
        }
        int minSpanStart = mTextView.getText().length();
        int maxSpanEnd = 0;
        int unionOfSpansCoveringSelectionStartStart = mTextView.getText().length();
        int unionOfSpansCoveringSelectionStartEnd = 0;
        boolean hasValidSuggestions = false;
        for (int i = 0; i < suggestionSpans.length; i++) {
            final int spanStart = spannable.getSpanStart(suggestionSpans[i]);
            final int spanEnd = spannable.getSpanEnd(suggestionSpans[i]);
            minSpanStart = java.lang.Math.min(minSpanStart, spanStart);
            maxSpanEnd = java.lang.Math.max(maxSpanEnd, spanEnd);
            if ((selectionStart < spanStart) || (selectionStart > spanEnd)) {
                // The span doesn't cover the current selection start point.
                continue;
            }
            hasValidSuggestions = hasValidSuggestions || (suggestionSpans[i].getSuggestions().length > 0);
            unionOfSpansCoveringSelectionStartStart = java.lang.Math.min(unionOfSpansCoveringSelectionStartStart, spanStart);
            unionOfSpansCoveringSelectionStartEnd = java.lang.Math.max(unionOfSpansCoveringSelectionStartEnd, spanEnd);
        }
        if (!hasValidSuggestions) {
            return false;
        }
        if (unionOfSpansCoveringSelectionStartStart >= unionOfSpansCoveringSelectionStartEnd) {
            // No spans cover the selection start point.
            return false;
        }
        if ((minSpanStart < unionOfSpansCoveringSelectionStartStart) || (maxSpanEnd > unionOfSpansCoveringSelectionStartEnd)) {
            // There is a span that is not covered by the union. In this case, we soouldn't offer
            // to show suggestions as it's confusing.
            return false;
        }
        return true;
    }

    /**
     *
     *
     * @return <code>true</code> if the cursor is inside an {@link SuggestionSpan} with
    {@link SuggestionSpan#FLAG_EASY_CORRECT} set.
     */
    private boolean isCursorInsideEasyCorrectionSpan() {
        android.text.Spannable spannable = ((android.text.Spannable) (mTextView.getText()));
        android.text.style.SuggestionSpan[] suggestionSpans = spannable.getSpans(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), android.text.style.SuggestionSpan.class);
        for (int i = 0; i < suggestionSpans.length; i++) {
            if ((suggestionSpans[i].getFlags() & android.text.style.SuggestionSpan.FLAG_EASY_CORRECT) != 0) {
                return true;
            }
        }
        return false;
    }

    void onTouchUpEvent(android.view.MotionEvent event) {
        if (getSelectionActionModeHelper().resetSelection(getTextView().getOffsetForPosition(event.getX(), event.getY()))) {
            return;
        }
        boolean selectAllGotFocus = mSelectAllOnFocus && mTextView.didTouchFocusSelect();
        hideCursorAndSpanControllers();
        stopTextActionMode();
        java.lang.CharSequence text = mTextView.getText();
        if ((!selectAllGotFocus) && (text.length() > 0)) {
            // Move cursor
            final int offset = mTextView.getOffsetForPosition(event.getX(), event.getY());
            final boolean shouldInsertCursor = !mRequestingLinkActionMode;
            if (shouldInsertCursor) {
                android.text.Selection.setSelection(((android.text.Spannable) (text)), offset);
                if (mSpellChecker != null) {
                    // When the cursor moves, the word that was typed may need spell check
                    mSpellChecker.onSelectionChanged();
                }
            }
            if (!extractedTextModeWillBeStarted()) {
                if (isCursorInsideEasyCorrectionSpan()) {
                    // Cancel the single tap delayed runnable.
                    if (mInsertionActionModeRunnable != null) {
                        mTextView.removeCallbacks(mInsertionActionModeRunnable);
                    }
                    mShowSuggestionRunnable = this::replace;
                    // removeCallbacks is performed on every touch
                    mTextView.postDelayed(mShowSuggestionRunnable, android.view.ViewConfiguration.getDoubleTapTimeout());
                } else
                    if (hasInsertionController()) {
                        if (shouldInsertCursor) {
                            getInsertionController().show();
                        } else {
                            getInsertionController().hide();
                        }
                    }

            }
        }
    }

    /**
     * Called when {@link TextView#mTextOperationUser} has changed.
     *
     * <p>Any user-specific resources need to be refreshed here.</p>
     */
    final void onTextOperationUserChanged() {
        if (mSpellChecker != null) {
            mSpellChecker.resetSession();
        }
    }

    protected void stopTextActionMode() {
        if (mTextActionMode != null) {
            // This will hide the mSelectionModifierCursorController
            mTextActionMode.finish();
        }
    }

    private void stopTextActionModeWithPreservingSelection() {
        if (mTextActionMode != null) {
            mRestartActionModeOnNextRefresh = true;
        }
        mPreserveSelection = true;
        stopTextActionMode();
        mPreserveSelection = false;
    }

    /**
     *
     *
     * @return True if this view supports insertion handles.
     */
    boolean hasInsertionController() {
        return mInsertionControllerEnabled;
    }

    /**
     *
     *
     * @return True if this view supports selection handles.
     */
    boolean hasSelectionController() {
        return mSelectionControllerEnabled;
    }

    private android.widget.Editor.InsertionPointCursorController getInsertionController() {
        if (!mInsertionControllerEnabled) {
            return null;
        }
        if (mInsertionPointCursorController == null) {
            mInsertionPointCursorController = new android.widget.Editor.InsertionPointCursorController();
            final android.view.ViewTreeObserver observer = mTextView.getViewTreeObserver();
            observer.addOnTouchModeChangeListener(mInsertionPointCursorController);
        }
        return mInsertionPointCursorController;
    }

    @android.annotation.Nullable
    android.widget.Editor.SelectionModifierCursorController getSelectionController() {
        if (!mSelectionControllerEnabled) {
            return null;
        }
        if (mSelectionModifierCursorController == null) {
            mSelectionModifierCursorController = new android.widget.Editor.SelectionModifierCursorController();
            final android.view.ViewTreeObserver observer = mTextView.getViewTreeObserver();
            observer.addOnTouchModeChangeListener(mSelectionModifierCursorController);
        }
        return mSelectionModifierCursorController;
    }

    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getCursorDrawable() {
        return mDrawableForCursor;
    }

    private void updateCursorPosition(int top, int bottom, float horizontal) {
        loadCursorDrawable();
        final int left = clampHorizontalPosition(mDrawableForCursor, horizontal);
        final int width = mDrawableForCursor.getIntrinsicWidth();
        mDrawableForCursor.setBounds(left, top - mTempRect.top, left + width, bottom + mTempRect.bottom);
    }

    /**
     * Return clamped position for the drawable. If the drawable is within the boundaries of the
     * view, then it is offset with the left padding of the cursor drawable. If the drawable is at
     * the beginning or the end of the text then its drawable edge is aligned with left or right of
     * the view boundary. If the drawable is null, horizontal parameter is aligned to left or right
     * of the view.
     *
     * @param drawable
     * 		Drawable. Can be null.
     * @param horizontal
     * 		Horizontal position for the drawable.
     * @return The clamped horizontal position for the drawable.
     */
    private int clampHorizontalPosition(@android.annotation.Nullable
    final android.graphics.drawable.Drawable drawable, float horizontal) {
        horizontal = java.lang.Math.max(0.5F, horizontal - 0.5F);
        if (mTempRect == null)
            mTempRect = new android.graphics.Rect();

        int drawableWidth = 0;
        if (drawable != null) {
            drawable.getPadding(mTempRect);
            drawableWidth = drawable.getIntrinsicWidth();
        } else {
            mTempRect.setEmpty();
        }
        int scrollX = mTextView.getScrollX();
        float horizontalDiff = horizontal - scrollX;
        int viewClippedWidth = (mTextView.getWidth() - mTextView.getCompoundPaddingLeft()) - mTextView.getCompoundPaddingRight();
        final int left;
        if (horizontalDiff >= (viewClippedWidth - 1.0F)) {
            // at the rightmost position
            left = (viewClippedWidth + scrollX) - (drawableWidth - mTempRect.right);
        } else
            if ((java.lang.Math.abs(horizontalDiff) <= 1.0F) || ((android.text.TextUtils.isEmpty(mTextView.getText()) && ((android.widget.TextView.VERY_WIDE - scrollX) <= (viewClippedWidth + 1.0F))) && (horizontal <= 1.0F))) {
                // at the leftmost position
                left = scrollX - mTempRect.left;
            } else {
                left = ((int) (horizontal)) - mTempRect.left;
            }

        return left;
    }

    /**
     * Called by the framework in response to a text auto-correction (such as fixing a typo using a
     * a dictionary) from the current input method, provided by it calling
     * {@link InputConnection#commitCorrection} InputConnection.commitCorrection()}. The default
     * implementation flashes the background of the corrected word to provide feedback to the user.
     *
     * @param info
     * 		The auto correct info about the text that was corrected.
     */
    public void onCommitCorrection(android.view.inputmethod.CorrectionInfo info) {
        if (mCorrectionHighlighter == null) {
            mCorrectionHighlighter = new android.widget.Editor.CorrectionHighlighter();
        } else {
            mCorrectionHighlighter.invalidate(false);
        }
        mCorrectionHighlighter.highlight(info);
        mUndoInputFilter.freezeLastEdit();
    }

    void onScrollChanged() {
        if (mPositionListener != null) {
            mPositionListener.onScrollChanged();
        }
        if (mTextActionMode != null) {
            mTextActionMode.invalidateContentRect();
        }
    }

    /**
     *
     *
     * @return True when the TextView isFocused and has a valid zero-length selection (cursor).
     */
    private boolean shouldBlink() {
        if ((!isCursorVisible()) || (!mTextView.isFocused()))
            return false;

        final int start = mTextView.getSelectionStart();
        if (start < 0)
            return false;

        final int end = mTextView.getSelectionEnd();
        if (end < 0)
            return false;

        return start == end;
    }

    void makeBlink() {
        if (shouldBlink()) {
            mShowCursor = android.os.SystemClock.uptimeMillis();
            if (mBlink == null)
                mBlink = new android.widget.Editor.Blink();

            mTextView.removeCallbacks(mBlink);
            mTextView.postDelayed(mBlink, android.widget.Editor.BLINK);
        } else {
            if (mBlink != null)
                mTextView.removeCallbacks(mBlink);

        }
    }

    private class Blink implements java.lang.Runnable {
        private boolean mCancelled;

        public void run() {
            if (mCancelled) {
                return;
            }
            mTextView.removeCallbacks(this);
            if (shouldBlink()) {
                if (mTextView.getLayout() != null) {
                    mTextView.invalidateCursorPath();
                }
                mTextView.postDelayed(this, android.widget.Editor.BLINK);
            }
        }

        void cancel() {
            if (!mCancelled) {
                mTextView.removeCallbacks(this);
                mCancelled = true;
            }
        }

        void uncancel() {
            mCancelled = false;
        }
    }

    private android.view.View.DragShadowBuilder getTextThumbnailBuilder(int start, int end) {
        android.widget.TextView shadowView = ((android.widget.TextView) (android.view.View.inflate(mTextView.getContext(), com.android.internal.R.layout.text_drag_thumbnail, null)));
        if (shadowView == null) {
            throw new java.lang.IllegalArgumentException("Unable to inflate text drag thumbnail");
        }
        if ((end - start) > android.widget.Editor.DRAG_SHADOW_MAX_TEXT_LENGTH) {
            final long range = getCharClusterRange(start + android.widget.Editor.DRAG_SHADOW_MAX_TEXT_LENGTH);
            end = android.text.TextUtils.unpackRangeEndFromLong(range);
        }
        final java.lang.CharSequence text = mTextView.getTransformedText(start, end);
        shadowView.setText(text);
        shadowView.setTextColor(mTextView.getTextColors());
        shadowView.setTextAppearance(R.styleable.Theme_textAppearanceLarge);
        shadowView.setGravity(android.view.Gravity.CENTER);
        shadowView.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        final int size = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
        shadowView.measure(size, size);
        shadowView.layout(0, 0, shadowView.getMeasuredWidth(), shadowView.getMeasuredHeight());
        shadowView.invalidate();
        return new android.view.View.DragShadowBuilder(shadowView);
    }

    private static class DragLocalState {
        public android.widget.TextView sourceTextView;

        public int start;

        public int end;

        public DragLocalState(android.widget.TextView sourceTextView, int start, int end) {
            this.sourceTextView = sourceTextView;
            this.start = start;
            this.end = end;
        }
    }

    void onDrop(android.view.DragEvent event) {
        android.text.SpannableStringBuilder content = new android.text.SpannableStringBuilder();
        final android.view.DragAndDropPermissions permissions = android.view.DragAndDropPermissions.obtain(event);
        if (permissions != null) {
            permissions.takeTransient();
        }
        try {
            android.content.ClipData clipData = event.getClipData();
            final int itemCount = clipData.getItemCount();
            for (int i = 0; i < itemCount; i++) {
                android.content.ClipData.Item item = clipData.getItemAt(i);
                content.append(item.coerceToStyledText(mTextView.getContext()));
            }
        } finally {
            if (permissions != null) {
                permissions.release();
            }
        }
        mTextView.beginBatchEdit();
        mUndoInputFilter.freezeLastEdit();
        try {
            final int offset = mTextView.getOffsetForPosition(event.getX(), event.getY());
            java.lang.Object localState = event.getLocalState();
            android.widget.Editor.DragLocalState dragLocalState = null;
            if (localState instanceof android.widget.Editor.DragLocalState) {
                dragLocalState = ((android.widget.Editor.DragLocalState) (localState));
            }
            boolean dragDropIntoItself = (dragLocalState != null) && (dragLocalState.sourceTextView == mTextView);
            if (dragDropIntoItself) {
                if ((offset >= dragLocalState.start) && (offset < dragLocalState.end)) {
                    // A drop inside the original selection discards the drop.
                    return;
                }
            }
            final int originalLength = mTextView.getText().length();
            int min = offset;
            int max = offset;
            android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), max);
            mTextView.replaceText_internal(min, max, content);
            if (dragDropIntoItself) {
                int dragSourceStart = dragLocalState.start;
                int dragSourceEnd = dragLocalState.end;
                if (max <= dragSourceStart) {
                    // Inserting text before selection has shifted positions
                    final int shift = mTextView.getText().length() - originalLength;
                    dragSourceStart += shift;
                    dragSourceEnd += shift;
                }
                // Delete original selection
                mTextView.deleteText_internal(dragSourceStart, dragSourceEnd);
                // Make sure we do not leave two adjacent spaces.
                final int prevCharIdx = java.lang.Math.max(0, dragSourceStart - 1);
                final int nextCharIdx = java.lang.Math.min(mTextView.getText().length(), dragSourceStart + 1);
                if (nextCharIdx > (prevCharIdx + 1)) {
                    java.lang.CharSequence t = mTextView.getTransformedText(prevCharIdx, nextCharIdx);
                    if (java.lang.Character.isSpaceChar(t.charAt(0)) && java.lang.Character.isSpaceChar(t.charAt(1))) {
                        mTextView.deleteText_internal(prevCharIdx, prevCharIdx + 1);
                    }
                }
            }
        } finally {
            mTextView.endBatchEdit();
            mUndoInputFilter.freezeLastEdit();
        }
    }

    public void addSpanWatchers(android.text.Spannable text) {
        final int textLength = text.length();
        if (mKeyListener != null) {
            text.setSpan(mKeyListener, 0, textLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        if (mSpanController == null) {
            mSpanController = new android.widget.Editor.SpanController();
        }
        text.setSpan(mSpanController, 0, textLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
    }

    void setContextMenuAnchor(float x, float y) {
        mContextMenuAnchorX = x;
        mContextMenuAnchorY = y;
    }

    void onCreateContextMenu(android.view.ContextMenu menu) {
        if ((mIsBeingLongClicked || java.lang.Float.isNaN(mContextMenuAnchorX)) || java.lang.Float.isNaN(mContextMenuAnchorY)) {
            return;
        }
        final int offset = mTextView.getOffsetForPosition(mContextMenuAnchorX, mContextMenuAnchorY);
        if (offset == (-1)) {
            return;
        }
        stopTextActionModeWithPreservingSelection();
        if (mTextView.canSelectText()) {
            final boolean isOnSelection = (mTextView.hasSelection() && (offset >= mTextView.getSelectionStart())) && (offset <= mTextView.getSelectionEnd());
            if (!isOnSelection) {
                // Right clicked position is not on the selection. Remove the selection and move the
                // cursor to the right clicked position.
                android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), offset);
                stopTextActionMode();
            }
        }
        if (shouldOfferToShowSuggestions()) {
            final android.widget.Editor.SuggestionInfo[] suggestionInfoArray = new android.widget.Editor.SuggestionInfo[android.text.style.SuggestionSpan.SUGGESTIONS_MAX_SIZE];
            for (int i = 0; i < suggestionInfoArray.length; i++) {
                suggestionInfoArray[i] = new android.widget.Editor.SuggestionInfo();
            }
            final android.view.SubMenu subMenu = menu.addSubMenu(android.view.Menu.NONE, android.view.Menu.NONE, android.widget.Editor.MENU_ITEM_ORDER_REPLACE, com.android.internal.R.string.replace);
            final int numItems = mSuggestionHelper.getSuggestionInfo(suggestionInfoArray, null);
            for (int i = 0; i < numItems; i++) {
                final android.widget.Editor.SuggestionInfo info = suggestionInfoArray[i];
                subMenu.add(android.view.Menu.NONE, android.view.Menu.NONE, i, info.mText).setOnMenuItemClickListener(new android.view.MenuItem.OnMenuItemClickListener() {
                    @java.lang.Override
                    public boolean onMenuItemClick(android.view.MenuItem item) {
                        replaceWithSuggestion(info);
                        return true;
                    }
                });
            }
        }
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_UNDO, android.widget.Editor.MENU_ITEM_ORDER_UNDO, com.android.internal.R.string.undo).setAlphabeticShortcut('z').setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canUndo());
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_REDO, android.widget.Editor.MENU_ITEM_ORDER_REDO, com.android.internal.R.string.redo).setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canRedo());
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_CUT, android.widget.Editor.MENU_ITEM_ORDER_CUT, com.android.internal.R.string.cut).setAlphabeticShortcut('x').setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canCut());
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_COPY, android.widget.Editor.MENU_ITEM_ORDER_COPY, com.android.internal.R.string.copy).setAlphabeticShortcut('c').setOnMenuItemClickListener(mOnContextMenuItemClickListener).setEnabled(mTextView.canCopy());
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_PASTE, android.widget.Editor.MENU_ITEM_ORDER_PASTE, com.android.internal.R.string.paste).setAlphabeticShortcut('v').setEnabled(mTextView.canPaste()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_PASTE_AS_PLAIN_TEXT, android.widget.Editor.MENU_ITEM_ORDER_PASTE_AS_PLAIN_TEXT, com.android.internal.R.string.paste_as_plain_text).setEnabled(mTextView.canPasteAsPlainText()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_SHARE, android.widget.Editor.MENU_ITEM_ORDER_SHARE, com.android.internal.R.string.share).setEnabled(mTextView.canShare()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_SELECT_ALL, android.widget.Editor.MENU_ITEM_ORDER_SELECT_ALL, com.android.internal.R.string.selectAll).setAlphabeticShortcut('a').setEnabled(mTextView.canSelectAllText()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
        menu.add(android.view.Menu.NONE, android.widget.TextView.ID_AUTOFILL, android.widget.Editor.MENU_ITEM_ORDER_AUTOFILL, android.R.string.autofill).setEnabled(mTextView.canRequestAutofill()).setOnMenuItemClickListener(mOnContextMenuItemClickListener);
        mPreserveSelection = true;
    }

    @android.annotation.Nullable
    private android.text.style.SuggestionSpan findEquivalentSuggestionSpan(@android.annotation.NonNull
    android.widget.Editor.SuggestionSpanInfo suggestionSpanInfo) {
        final android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
        if (editable.getSpanStart(suggestionSpanInfo.mSuggestionSpan) >= 0) {
            // Exactly same span is found.
            return suggestionSpanInfo.mSuggestionSpan;
        }
        // Suggestion span couldn't be found. Try to find a suggestion span that has the same
        // contents.
        final android.text.style.SuggestionSpan[] suggestionSpans = editable.getSpans(suggestionSpanInfo.mSpanStart, suggestionSpanInfo.mSpanEnd, android.text.style.SuggestionSpan.class);
        for (final android.text.style.SuggestionSpan suggestionSpan : suggestionSpans) {
            final int start = editable.getSpanStart(suggestionSpan);
            if (start != suggestionSpanInfo.mSpanStart) {
                continue;
            }
            final int end = editable.getSpanEnd(suggestionSpan);
            if (end != suggestionSpanInfo.mSpanEnd) {
                continue;
            }
            if (suggestionSpan.equals(suggestionSpanInfo.mSuggestionSpan)) {
                return suggestionSpan;
            }
        }
        return null;
    }

    private void replaceWithSuggestion(@android.annotation.NonNull
    final android.widget.Editor.SuggestionInfo suggestionInfo) {
        final android.text.style.SuggestionSpan targetSuggestionSpan = findEquivalentSuggestionSpan(suggestionInfo.mSuggestionSpanInfo);
        if (targetSuggestionSpan == null) {
            // Span has been removed
            return;
        }
        final android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
        final int spanStart = editable.getSpanStart(targetSuggestionSpan);
        final int spanEnd = editable.getSpanEnd(targetSuggestionSpan);
        if ((spanStart < 0) || (spanEnd <= spanStart)) {
            // Span has been removed
            return;
        }
        final java.lang.String originalText = android.text.TextUtils.substring(editable, spanStart, spanEnd);
        // SuggestionSpans are removed by replace: save them before
        android.text.style.SuggestionSpan[] suggestionSpans = editable.getSpans(spanStart, spanEnd, android.text.style.SuggestionSpan.class);
        final int length = suggestionSpans.length;
        int[] suggestionSpansStarts = new int[length];
        int[] suggestionSpansEnds = new int[length];
        int[] suggestionSpansFlags = new int[length];
        for (int i = 0; i < length; i++) {
            final android.text.style.SuggestionSpan suggestionSpan = suggestionSpans[i];
            suggestionSpansStarts[i] = editable.getSpanStart(suggestionSpan);
            suggestionSpansEnds[i] = editable.getSpanEnd(suggestionSpan);
            suggestionSpansFlags[i] = editable.getSpanFlags(suggestionSpan);
            // Remove potential misspelled flags
            int suggestionSpanFlags = suggestionSpan.getFlags();
            if ((suggestionSpanFlags & android.text.style.SuggestionSpan.FLAG_MISSPELLED) != 0) {
                suggestionSpanFlags &= ~android.text.style.SuggestionSpan.FLAG_MISSPELLED;
                suggestionSpanFlags &= ~android.text.style.SuggestionSpan.FLAG_EASY_CORRECT;
                suggestionSpan.setFlags(suggestionSpanFlags);
            }
        }
        // Swap text content between actual text and Suggestion span
        final int suggestionStart = suggestionInfo.mSuggestionStart;
        final int suggestionEnd = suggestionInfo.mSuggestionEnd;
        final java.lang.String suggestion = suggestionInfo.mText.subSequence(suggestionStart, suggestionEnd).toString();
        mTextView.replaceText_internal(spanStart, spanEnd, suggestion);
        java.lang.String[] suggestions = targetSuggestionSpan.getSuggestions();
        suggestions[suggestionInfo.mSuggestionIndex] = originalText;
        // Restore previous SuggestionSpans
        final int lengthDelta = suggestion.length() - (spanEnd - spanStart);
        for (int i = 0; i < length; i++) {
            // Only spans that include the modified region make sense after replacement
            // Spans partially included in the replaced region are removed, there is no
            // way to assign them a valid range after replacement
            if ((suggestionSpansStarts[i] <= spanStart) && (suggestionSpansEnds[i] >= spanEnd)) {
                mTextView.setSpan_internal(suggestionSpans[i], suggestionSpansStarts[i], suggestionSpansEnds[i] + lengthDelta, suggestionSpansFlags[i]);
            }
        }
        // Move cursor at the end of the replaced word
        final int newCursorPosition = spanEnd + lengthDelta;
        mTextView.setCursorPosition_internal(newCursorPosition, newCursorPosition);
    }

    private final android.view.MenuItem.OnMenuItemClickListener mOnContextMenuItemClickListener = new android.view.MenuItem.OnMenuItemClickListener() {
        @java.lang.Override
        public boolean onMenuItemClick(android.view.MenuItem item) {
            if (mProcessTextIntentActionsHandler.performMenuItemAction(item)) {
                return true;
            }
            return mTextView.onTextContextMenuItem(item.getItemId());
        }
    };

    /**
     * Controls the {@link EasyEditSpan} monitoring when it is added, and when the related
     * pop-up should be displayed.
     * Also monitors {@link Selection} to call back to the attached input method.
     */
    private class SpanController implements android.text.SpanWatcher {
        private static final int DISPLAY_TIMEOUT_MS = 3000;// 3 secs


        private android.widget.Editor.EasyEditPopupWindow mPopupWindow;

        private java.lang.Runnable mHidePopup;

        // This function is pure but inner classes can't have static functions
        private boolean isNonIntermediateSelectionSpan(final android.text.Spannable text, final java.lang.Object span) {
            return ((android.text.Selection.SELECTION_START == span) || (android.text.Selection.SELECTION_END == span)) && ((text.getSpanFlags(span) & android.text.Spanned.SPAN_INTERMEDIATE) == 0);
        }

        @java.lang.Override
        public void onSpanAdded(android.text.Spannable text, java.lang.Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                sendUpdateSelection();
            } else
                if (span instanceof android.text.style.EasyEditSpan) {
                    if (mPopupWindow == null) {
                        mPopupWindow = new android.widget.Editor.EasyEditPopupWindow();
                        mHidePopup = new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                hide();
                            }
                        };
                    }
                    // Make sure there is only at most one EasyEditSpan in the text
                    if (mPopupWindow.mEasyEditSpan != null) {
                        mPopupWindow.mEasyEditSpan.setDeleteEnabled(false);
                    }
                    mPopupWindow.setEasyEditSpan(((android.text.style.EasyEditSpan) (span)));
                    mPopupWindow.setOnDeleteListener(new android.widget.Editor.EasyEditDeleteListener() {
                        @java.lang.Override
                        public void onDeleteClick(android.text.style.EasyEditSpan span) {
                            android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
                            int start = editable.getSpanStart(span);
                            int end = editable.getSpanEnd(span);
                            if ((start >= 0) && (end >= 0)) {
                                sendEasySpanNotification(EasyEditSpan.TEXT_DELETED, span);
                                mTextView.deleteText_internal(start, end);
                            }
                            editable.removeSpan(span);
                        }
                    });
                    if (mTextView.getWindowVisibility() != android.view.View.VISIBLE) {
                        // The window is not visible yet, ignore the text change.
                        return;
                    }
                    if (mTextView.getLayout() == null) {
                        // The view has not been laid out yet, ignore the text change
                        return;
                    }
                    if (extractedTextModeWillBeStarted()) {
                        // The input is in extract mode. Do not handle the easy edit in
                        // the original TextView, as the ExtractEditText will do
                        return;
                    }
                    mPopupWindow.show();
                    mTextView.removeCallbacks(mHidePopup);
                    mTextView.postDelayed(mHidePopup, android.widget.Editor.SpanController.DISPLAY_TIMEOUT_MS);
                }

        }

        @java.lang.Override
        public void onSpanRemoved(android.text.Spannable text, java.lang.Object span, int start, int end) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                sendUpdateSelection();
            } else
                if ((mPopupWindow != null) && (span == mPopupWindow.mEasyEditSpan)) {
                    hide();
                }

        }

        @java.lang.Override
        public void onSpanChanged(android.text.Spannable text, java.lang.Object span, int previousStart, int previousEnd, int newStart, int newEnd) {
            if (isNonIntermediateSelectionSpan(text, span)) {
                sendUpdateSelection();
            } else
                if ((mPopupWindow != null) && (span instanceof android.text.style.EasyEditSpan)) {
                    android.text.style.EasyEditSpan easyEditSpan = ((android.text.style.EasyEditSpan) (span));
                    sendEasySpanNotification(EasyEditSpan.TEXT_MODIFIED, easyEditSpan);
                    text.removeSpan(easyEditSpan);
                }

        }

        public void hide() {
            if (mPopupWindow != null) {
                mPopupWindow.hide();
                mTextView.removeCallbacks(mHidePopup);
            }
        }

        private void sendEasySpanNotification(int textChangedType, android.text.style.EasyEditSpan span) {
            try {
                android.app.PendingIntent pendingIntent = span.getPendingIntent();
                if (pendingIntent != null) {
                    android.content.Intent intent = new android.content.Intent();
                    intent.putExtra(EasyEditSpan.EXTRA_TEXT_CHANGED_TYPE, textChangedType);
                    pendingIntent.send(mTextView.getContext(), 0, intent);
                }
            } catch (android.app.PendingIntent.CanceledException e) {
                // This should not happen, as we should try to send the intent only once.
                android.util.Log.w(android.widget.Editor.TAG, "PendingIntent for notification cannot be sent", e);
            }
        }
    }

    /**
     * Listens for the delete event triggered by {@link EasyEditPopupWindow}.
     */
    private interface EasyEditDeleteListener {
        /**
         * Clicks the delete pop-up.
         */
        void onDeleteClick(android.text.style.EasyEditSpan span);
    }

    /**
     * Displays the actions associated to an {@link EasyEditSpan}. The pop-up is controlled
     * by {@link SpanController}.
     */
    private class EasyEditPopupWindow extends android.widget.Editor.PinnedPopupWindow implements android.view.View.OnClickListener {
        private static final int POPUP_TEXT_LAYOUT = com.android.internal.R.layout.text_edit_action_popup_text;

        private android.widget.TextView mDeleteTextView;

        private android.text.style.EasyEditSpan mEasyEditSpan;

        private android.widget.Editor.EasyEditDeleteListener mOnDeleteListener;

        @java.lang.Override
        protected void createPopupWindow() {
            mPopupWindow = new android.widget.PopupWindow(mTextView.getContext(), null, com.android.internal.R.attr.textSelectHandleWindowStyle);
            mPopupWindow.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED);
            mPopupWindow.setClippingEnabled(true);
        }

        @java.lang.Override
        protected void initContentView() {
            android.widget.LinearLayout linearLayout = new android.widget.LinearLayout(mTextView.getContext());
            linearLayout.setOrientation(android.widget.LinearLayout.HORIZONTAL);
            mContentView = linearLayout;
            mContentView.setBackgroundResource(com.android.internal.R.drawable.text_edit_side_paste_window);
            android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (mTextView.getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            android.view.ViewGroup.LayoutParams wrapContent = new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            mDeleteTextView = ((android.widget.TextView) (inflater.inflate(android.widget.Editor.EasyEditPopupWindow.POPUP_TEXT_LAYOUT, null)));
            mDeleteTextView.setLayoutParams(wrapContent);
            mDeleteTextView.setText(com.android.internal.R.string.delete);
            mDeleteTextView.setOnClickListener(this);
            mContentView.addView(mDeleteTextView);
        }

        public void setEasyEditSpan(android.text.style.EasyEditSpan easyEditSpan) {
            mEasyEditSpan = easyEditSpan;
        }

        private void setOnDeleteListener(android.widget.Editor.EasyEditDeleteListener listener) {
            mOnDeleteListener = listener;
        }

        @java.lang.Override
        public void onClick(android.view.View view) {
            if ((((view == mDeleteTextView) && (mEasyEditSpan != null)) && mEasyEditSpan.isDeleteEnabled()) && (mOnDeleteListener != null)) {
                mOnDeleteListener.onDeleteClick(mEasyEditSpan);
            }
        }

        @java.lang.Override
        public void hide() {
            if (mEasyEditSpan != null) {
                mEasyEditSpan.setDeleteEnabled(false);
            }
            mOnDeleteListener = null;
            super.hide();
        }

        @java.lang.Override
        protected int getTextOffset() {
            // Place the pop-up at the end of the span
            android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
            return editable.getSpanEnd(mEasyEditSpan);
        }

        @java.lang.Override
        protected int getVerticalLocalPosition(int line) {
            final android.text.Layout layout = mTextView.getLayout();
            return layout.getLineBottomWithoutSpacing(line);
        }

        @java.lang.Override
        protected int clipVertically(int positionY) {
            // As we display the pop-up below the span, no vertical clipping is required.
            return positionY;
        }
    }

    private class PositionListener implements android.view.ViewTreeObserver.OnPreDrawListener {
        // 3 handles
        // 3 ActionPopup [replace, suggestion, easyedit] (suggestionsPopup first hides the others)
        // 1 CursorAnchorInfoNotifier
        private static final int MAXIMUM_NUMBER_OF_LISTENERS = 7;

        private android.widget.Editor.TextViewPositionListener[] mPositionListeners = new android.widget.Editor.TextViewPositionListener[android.widget.Editor.PositionListener.MAXIMUM_NUMBER_OF_LISTENERS];

        private boolean[] mCanMove = new boolean[android.widget.Editor.PositionListener.MAXIMUM_NUMBER_OF_LISTENERS];

        private boolean mPositionHasChanged = true;

        private int mPositionX;

        // Absolute position of the TextView with respect to its parent window
        private int mPositionY;

        private int mPositionXOnScreen;

        private int mPositionYOnScreen;

        private int mNumberOfListeners;

        private boolean mScrollHasChanged;

        final int[] mTempCoords = new int[2];

        public void addSubscriber(android.widget.Editor.TextViewPositionListener positionListener, boolean canMove) {
            if (mNumberOfListeners == 0) {
                updatePosition();
                android.view.ViewTreeObserver vto = mTextView.getViewTreeObserver();
                vto.addOnPreDrawListener(this);
            }
            int emptySlotIndex = -1;
            for (int i = 0; i < android.widget.Editor.PositionListener.MAXIMUM_NUMBER_OF_LISTENERS; i++) {
                android.widget.Editor.TextViewPositionListener listener = mPositionListeners[i];
                if (listener == positionListener) {
                    return;
                } else
                    if ((emptySlotIndex < 0) && (listener == null)) {
                        emptySlotIndex = i;
                    }

            }
            mPositionListeners[emptySlotIndex] = positionListener;
            mCanMove[emptySlotIndex] = canMove;
            mNumberOfListeners++;
        }

        public void removeSubscriber(android.widget.Editor.TextViewPositionListener positionListener) {
            for (int i = 0; i < android.widget.Editor.PositionListener.MAXIMUM_NUMBER_OF_LISTENERS; i++) {
                if (mPositionListeners[i] == positionListener) {
                    mPositionListeners[i] = null;
                    mNumberOfListeners--;
                    break;
                }
            }
            if (mNumberOfListeners == 0) {
                android.view.ViewTreeObserver vto = mTextView.getViewTreeObserver();
                vto.removeOnPreDrawListener(this);
            }
        }

        public int getPositionX() {
            return mPositionX;
        }

        public int getPositionY() {
            return mPositionY;
        }

        public int getPositionXOnScreen() {
            return mPositionXOnScreen;
        }

        public int getPositionYOnScreen() {
            return mPositionYOnScreen;
        }

        @java.lang.Override
        public boolean onPreDraw() {
            updatePosition();
            for (int i = 0; i < android.widget.Editor.PositionListener.MAXIMUM_NUMBER_OF_LISTENERS; i++) {
                if ((mPositionHasChanged || mScrollHasChanged) || mCanMove[i]) {
                    android.widget.Editor.TextViewPositionListener positionListener = mPositionListeners[i];
                    if (positionListener != null) {
                        positionListener.updatePosition(mPositionX, mPositionY, mPositionHasChanged, mScrollHasChanged);
                    }
                }
            }
            mScrollHasChanged = false;
            return true;
        }

        private void updatePosition() {
            mTextView.getLocationInWindow(mTempCoords);
            mPositionHasChanged = (mTempCoords[0] != mPositionX) || (mTempCoords[1] != mPositionY);
            mPositionX = mTempCoords[0];
            mPositionY = mTempCoords[1];
            mTextView.getLocationOnScreen(mTempCoords);
            mPositionXOnScreen = mTempCoords[0];
            mPositionYOnScreen = mTempCoords[1];
        }

        public void onScrollChanged() {
            mScrollHasChanged = true;
        }
    }

    private abstract class PinnedPopupWindow implements android.widget.Editor.TextViewPositionListener {
        protected android.widget.PopupWindow mPopupWindow;

        protected android.view.ViewGroup mContentView;

        int mPositionX;

        int mPositionY;

        int mClippingLimitLeft;

        int mClippingLimitRight;

        protected abstract void createPopupWindow();

        protected abstract void initContentView();

        protected abstract int getTextOffset();

        protected abstract int getVerticalLocalPosition(int line);

        protected abstract int clipVertically(int positionY);

        protected void setUp() {
        }

        public PinnedPopupWindow() {
            // Due to calling subclass methods in base constructor, subclass constructor is not
            // called before subclass methods, e.g. createPopupWindow or initContentView. To give
            // a chance to initialize subclasses, call setUp() method here.
            // TODO: It is good to extract non trivial initialization code from constructor.
            setUp();
            createPopupWindow();
            mPopupWindow.setWindowLayoutType(android.view.WindowManager.LayoutParams.TYPE_APPLICATION_ABOVE_SUB_PANEL);
            mPopupWindow.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            mPopupWindow.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            initContentView();
            android.view.ViewGroup.LayoutParams wrapContent = new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            mContentView.setLayoutParams(wrapContent);
            mPopupWindow.setContentView(mContentView);
        }

        public void show() {
            /* offset is fixed */
            getPositionListener().addSubscriber(this, false);
            computeLocalPosition();
            final android.widget.Editor.PositionListener positionListener = getPositionListener();
            updatePosition(positionListener.getPositionX(), positionListener.getPositionY());
        }

        protected void measureContent() {
            final android.util.DisplayMetrics displayMetrics = mTextView.getResources().getDisplayMetrics();
            mContentView.measure(android.view.View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, android.view.View.MeasureSpec.AT_MOST), android.view.View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, android.view.View.MeasureSpec.AT_MOST));
        }

        /* The popup window will be horizontally centered on the getTextOffset() and vertically
        positioned according to viewportToContentHorizontalOffset.

        This method assumes that mContentView has properly been measured from its content.
         */
        private void computeLocalPosition() {
            measureContent();
            final int width = mContentView.getMeasuredWidth();
            final int offset = getTextOffset();
            mPositionX = ((int) (mTextView.getLayout().getPrimaryHorizontal(offset) - (width / 2.0F)));
            mPositionX += mTextView.viewportToContentHorizontalOffset();
            final int line = mTextView.getLayout().getLineForOffset(offset);
            mPositionY = getVerticalLocalPosition(line);
            mPositionY += mTextView.viewportToContentVerticalOffset();
        }

        private void updatePosition(int parentPositionX, int parentPositionY) {
            int positionX = parentPositionX + mPositionX;
            int positionY = parentPositionY + mPositionY;
            positionY = clipVertically(positionY);
            // Horizontal clipping
            final android.util.DisplayMetrics displayMetrics = mTextView.getResources().getDisplayMetrics();
            final int width = mContentView.getMeasuredWidth();
            positionX = java.lang.Math.min((displayMetrics.widthPixels - width) + mClippingLimitRight, positionX);
            positionX = java.lang.Math.max(-mClippingLimitLeft, positionX);
            if (isShowing()) {
                mPopupWindow.update(positionX, positionY, -1, -1);
            } else {
                mPopupWindow.showAtLocation(mTextView, android.view.Gravity.NO_GRAVITY, positionX, positionY);
            }
        }

        public void hide() {
            if (!isShowing()) {
                return;
            }
            mPopupWindow.dismiss();
            getPositionListener().removeSubscriber(this);
        }

        @java.lang.Override
        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            // Either parentPositionChanged or parentScrolled is true, check if still visible
            if (isShowing() && isOffsetVisible(getTextOffset())) {
                if (parentScrolled)
                    computeLocalPosition();

                updatePosition(parentPositionX, parentPositionY);
            } else {
                hide();
            }
        }

        public boolean isShowing() {
            return mPopupWindow.isShowing();
        }
    }

    private static final class SuggestionInfo {
        int mSuggestionStart;

        // Range of actual suggestion within mText
        int mSuggestionEnd;

        // The SuggestionSpan that this TextView represents
        final android.widget.Editor.SuggestionSpanInfo mSuggestionSpanInfo = new android.widget.Editor.SuggestionSpanInfo();

        // The index of this suggestion inside suggestionSpan
        int mSuggestionIndex;

        final android.text.SpannableStringBuilder mText = new android.text.SpannableStringBuilder();

        void clear() {
            mSuggestionSpanInfo.clear();
            mText.clear();
        }

        // Utility method to set attributes about a SuggestionSpan.
        void setSpanInfo(android.text.style.SuggestionSpan span, int spanStart, int spanEnd) {
            mSuggestionSpanInfo.mSuggestionSpan = span;
            mSuggestionSpanInfo.mSpanStart = spanStart;
            mSuggestionSpanInfo.mSpanEnd = spanEnd;
        }
    }

    private static final class SuggestionSpanInfo {
        // The SuggestionSpan;
        @android.annotation.Nullable
        android.text.style.SuggestionSpan mSuggestionSpan;

        // The SuggestionSpan start position
        int mSpanStart;

        // The SuggestionSpan end position
        int mSpanEnd;

        void clear() {
            mSuggestionSpan = null;
        }
    }

    private class SuggestionHelper {
        private final java.util.Comparator<android.text.style.SuggestionSpan> mSuggestionSpanComparator = new android.widget.Editor.SuggestionHelper.SuggestionSpanComparator();

        private final java.util.HashMap<android.text.style.SuggestionSpan, java.lang.Integer> mSpansLengths = new java.util.HashMap<android.text.style.SuggestionSpan, java.lang.Integer>();

        private class SuggestionSpanComparator implements java.util.Comparator<android.text.style.SuggestionSpan> {
            public int compare(android.text.style.SuggestionSpan span1, android.text.style.SuggestionSpan span2) {
                final int flag1 = span1.getFlags();
                final int flag2 = span2.getFlags();
                if (flag1 != flag2) {
                    // The order here should match what is used in updateDrawState
                    final boolean easy1 = (flag1 & android.text.style.SuggestionSpan.FLAG_EASY_CORRECT) != 0;
                    final boolean easy2 = (flag2 & android.text.style.SuggestionSpan.FLAG_EASY_CORRECT) != 0;
                    final boolean misspelled1 = (flag1 & android.text.style.SuggestionSpan.FLAG_MISSPELLED) != 0;
                    final boolean misspelled2 = (flag2 & android.text.style.SuggestionSpan.FLAG_MISSPELLED) != 0;
                    if (easy1 && (!misspelled1))
                        return -1;

                    if (easy2 && (!misspelled2))
                        return 1;

                    if (misspelled1)
                        return -1;

                    if (misspelled2)
                        return 1;

                }
                return mSpansLengths.get(span1).intValue() - mSpansLengths.get(span2).intValue();
            }
        }

        /**
         * Returns the suggestion spans that cover the current cursor position. The suggestion
         * spans are sorted according to the length of text that they are attached to.
         */
        private android.text.style.SuggestionSpan[] getSortedSuggestionSpans() {
            int pos = mTextView.getSelectionStart();
            android.text.Spannable spannable = ((android.text.Spannable) (mTextView.getText()));
            android.text.style.SuggestionSpan[] suggestionSpans = spannable.getSpans(pos, pos, android.text.style.SuggestionSpan.class);
            mSpansLengths.clear();
            for (android.text.style.SuggestionSpan suggestionSpan : suggestionSpans) {
                int start = spannable.getSpanStart(suggestionSpan);
                int end = spannable.getSpanEnd(suggestionSpan);
                mSpansLengths.put(suggestionSpan, java.lang.Integer.valueOf(end - start));
            }
            // The suggestions are sorted according to their types (easy correction first, then
            // misspelled) and to the length of the text that they cover (shorter first).
            java.util.Arrays.sort(suggestionSpans, mSuggestionSpanComparator);
            mSpansLengths.clear();
            return suggestionSpans;
        }

        /**
         * Gets the SuggestionInfo list that contains suggestion information at the current cursor
         * position.
         *
         * @param suggestionInfos
         * 		SuggestionInfo array the results will be set.
         * @param misspelledSpanInfo
         * 		a struct the misspelled SuggestionSpan info will be set.
         * @return the number of suggestions actually fetched.
         */
        public int getSuggestionInfo(android.widget.Editor.SuggestionInfo[] suggestionInfos, @android.annotation.Nullable
        android.widget.Editor.SuggestionSpanInfo misspelledSpanInfo) {
            final android.text.Spannable spannable = ((android.text.Spannable) (mTextView.getText()));
            final android.text.style.SuggestionSpan[] suggestionSpans = getSortedSuggestionSpans();
            final int nbSpans = suggestionSpans.length;
            if (nbSpans == 0)
                return 0;

            int numberOfSuggestions = 0;
            for (final android.text.style.SuggestionSpan suggestionSpan : suggestionSpans) {
                final int spanStart = spannable.getSpanStart(suggestionSpan);
                final int spanEnd = spannable.getSpanEnd(suggestionSpan);
                if ((misspelledSpanInfo != null) && ((suggestionSpan.getFlags() & android.text.style.SuggestionSpan.FLAG_MISSPELLED) != 0)) {
                    misspelledSpanInfo.mSuggestionSpan = suggestionSpan;
                    misspelledSpanInfo.mSpanStart = spanStart;
                    misspelledSpanInfo.mSpanEnd = spanEnd;
                }
                final java.lang.String[] suggestions = suggestionSpan.getSuggestions();
                final int nbSuggestions = suggestions.length;
                suggestionLoop : for (int suggestionIndex = 0; suggestionIndex < nbSuggestions; suggestionIndex++) {
                    final java.lang.String suggestion = suggestions[suggestionIndex];
                    for (int i = 0; i < numberOfSuggestions; i++) {
                        final android.widget.Editor.SuggestionInfo otherSuggestionInfo = suggestionInfos[i];
                        if (otherSuggestionInfo.mText.toString().equals(suggestion)) {
                            final int otherSpanStart = otherSuggestionInfo.mSuggestionSpanInfo.mSpanStart;
                            final int otherSpanEnd = otherSuggestionInfo.mSuggestionSpanInfo.mSpanEnd;
                            if ((spanStart == otherSpanStart) && (spanEnd == otherSpanEnd)) {
                                continue suggestionLoop;
                            }
                        }
                    }
                    android.widget.Editor.SuggestionInfo suggestionInfo = suggestionInfos[numberOfSuggestions];
                    suggestionInfo.setSpanInfo(suggestionSpan, spanStart, spanEnd);
                    suggestionInfo.mSuggestionIndex = suggestionIndex;
                    suggestionInfo.mSuggestionStart = 0;
                    suggestionInfo.mSuggestionEnd = suggestion.length();
                    suggestionInfo.mText.replace(0, suggestionInfo.mText.length(), suggestion);
                    numberOfSuggestions++;
                    if (numberOfSuggestions >= suggestionInfos.length) {
                        return numberOfSuggestions;
                    }
                }
            }
            return numberOfSuggestions;
        }
    }

    private final class SuggestionsPopupWindow extends android.widget.Editor.PinnedPopupWindow implements android.widget.AdapterView.OnItemClickListener {
        private static final int MAX_NUMBER_SUGGESTIONS = android.text.style.SuggestionSpan.SUGGESTIONS_MAX_SIZE;

        // Key of intent extras for inserting new word into user dictionary.
        private static final java.lang.String USER_DICTIONARY_EXTRA_WORD = "word";

        private static final java.lang.String USER_DICTIONARY_EXTRA_LOCALE = "locale";

        private android.widget.Editor.SuggestionInfo[] mSuggestionInfos;

        private int mNumberOfSuggestions;

        private boolean mCursorWasVisibleBeforeSuggestions;

        private boolean mIsShowingUp = false;

        private android.widget.Editor.SuggestionsPopupWindow.SuggestionAdapter mSuggestionsAdapter;

        private android.text.style.TextAppearanceSpan mHighlightSpan;// TODO: Make mHighlightSpan final.


        private android.widget.TextView mAddToDictionaryButton;

        private android.widget.TextView mDeleteButton;

        private android.widget.ListView mSuggestionListView;

        private final android.widget.Editor.SuggestionSpanInfo mMisspelledSpanInfo = new android.widget.Editor.SuggestionSpanInfo();

        private int mContainerMarginWidth;

        private int mContainerMarginTop;

        private android.widget.LinearLayout mContainerView;

        private android.content.Context mContext;// TODO: Make mContext final.


        private class CustomPopupWindow extends android.widget.PopupWindow {
            @java.lang.Override
            public void dismiss() {
                if (!isShowing()) {
                    return;
                }
                super.dismiss();
                getPositionListener().removeSubscriber(android.widget.Editor.SuggestionsPopupWindow.this);
                // Safe cast since show() checks that mTextView.getText() is an Editable
                ((android.text.Spannable) (mTextView.getText())).removeSpan(mSuggestionRangeSpan);
                mTextView.setCursorVisible(mCursorWasVisibleBeforeSuggestions);
                if (hasInsertionController() && (!extractedTextModeWillBeStarted())) {
                    getInsertionController().show();
                }
            }
        }

        public SuggestionsPopupWindow() {
            mCursorWasVisibleBeforeSuggestions = mCursorVisible;
        }

        @java.lang.Override
        protected void setUp() {
            mContext = applyDefaultTheme(mTextView.getContext());
            mHighlightSpan = new android.text.style.TextAppearanceSpan(mContext, mTextView.mTextEditSuggestionHighlightStyle);
        }

        private android.content.Context applyDefaultTheme(android.content.Context originalContext) {
            android.content.res.TypedArray a = originalContext.obtainStyledAttributes(new int[]{ com.android.internal.R.attr.isLightTheme });
            boolean isLightTheme = a.getBoolean(0, true);
            int themeId = (isLightTheme) ? R.style.ThemeOverlay_Material_Light : R.style.ThemeOverlay_Material_Dark;
            a.recycle();
            return new android.view.ContextThemeWrapper(originalContext, themeId);
        }

        @java.lang.Override
        protected void createPopupWindow() {
            mPopupWindow = new android.widget.Editor.SuggestionsPopupWindow.CustomPopupWindow();
            mPopupWindow.setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED);
            mPopupWindow.setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
            mPopupWindow.setFocusable(true);
            mPopupWindow.setClippingEnabled(false);
        }

        @java.lang.Override
        protected void initContentView() {
            final android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
            mContentView = ((android.view.ViewGroup) (inflater.inflate(mTextView.mTextEditSuggestionContainerLayout, null)));
            mContainerView = ((android.widget.LinearLayout) (mContentView.findViewById(com.android.internal.R.id.suggestionWindowContainer)));
            android.view.ViewGroup.MarginLayoutParams lp = ((android.view.ViewGroup.MarginLayoutParams) (mContainerView.getLayoutParams()));
            mContainerMarginWidth = lp.leftMargin + lp.rightMargin;
            mContainerMarginTop = lp.topMargin;
            mClippingLimitLeft = lp.leftMargin;
            mClippingLimitRight = lp.rightMargin;
            mSuggestionListView = ((android.widget.ListView) (mContentView.findViewById(com.android.internal.R.id.suggestionContainer)));
            mSuggestionsAdapter = new android.widget.Editor.SuggestionsPopupWindow.SuggestionAdapter();
            mSuggestionListView.setAdapter(mSuggestionsAdapter);
            mSuggestionListView.setOnItemClickListener(this);
            // Inflate the suggestion items once and for all.
            mSuggestionInfos = new android.widget.Editor.SuggestionInfo[android.widget.Editor.SuggestionsPopupWindow.MAX_NUMBER_SUGGESTIONS];
            for (int i = 0; i < mSuggestionInfos.length; i++) {
                mSuggestionInfos[i] = new android.widget.Editor.SuggestionInfo();
            }
            mAddToDictionaryButton = ((android.widget.TextView) (mContentView.findViewById(com.android.internal.R.id.addToDictionaryButton)));
            mAddToDictionaryButton.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View v) {
                    final android.text.style.SuggestionSpan misspelledSpan = findEquivalentSuggestionSpan(mMisspelledSpanInfo);
                    if (misspelledSpan == null) {
                        // Span has been removed.
                        return;
                    }
                    final android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
                    final int spanStart = editable.getSpanStart(misspelledSpan);
                    final int spanEnd = editable.getSpanEnd(misspelledSpan);
                    if ((spanStart < 0) || (spanEnd <= spanStart)) {
                        return;
                    }
                    final java.lang.String originalText = android.text.TextUtils.substring(editable, spanStart, spanEnd);
                    final android.content.Intent intent = new android.content.Intent(android.provider.Settings.ACTION_USER_DICTIONARY_INSERT);
                    intent.putExtra(android.widget.Editor.SuggestionsPopupWindow.USER_DICTIONARY_EXTRA_WORD, originalText);
                    intent.putExtra(android.widget.Editor.SuggestionsPopupWindow.USER_DICTIONARY_EXTRA_LOCALE, mTextView.getTextServicesLocale().toString());
                    intent.setFlags(intent.getFlags() | android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                    mTextView.startActivityAsTextOperationUserIfNecessary(intent);
                    // There is no way to know if the word was indeed added. Re-check.
                    // TODO The ExtractEditText should remove the span in the original text instead
                    editable.removeSpan(mMisspelledSpanInfo.mSuggestionSpan);
                    android.text.Selection.setSelection(editable, spanEnd);
                    updateSpellCheckSpans(spanStart, spanEnd, false);
                    hideWithCleanUp();
                }
            });
            mDeleteButton = ((android.widget.TextView) (mContentView.findViewById(com.android.internal.R.id.deleteButton)));
            mDeleteButton.setOnClickListener(new android.view.View.OnClickListener() {
                public void onClick(android.view.View v) {
                    final android.text.Editable editable = ((android.text.Editable) (mTextView.getText()));
                    final int spanUnionStart = editable.getSpanStart(mSuggestionRangeSpan);
                    int spanUnionEnd = editable.getSpanEnd(mSuggestionRangeSpan);
                    if ((spanUnionStart >= 0) && (spanUnionEnd > spanUnionStart)) {
                        // Do not leave two adjacent spaces after deletion, or one at beginning of
                        // text
                        if (((spanUnionEnd < editable.length()) && java.lang.Character.isSpaceChar(editable.charAt(spanUnionEnd))) && ((spanUnionStart == 0) || java.lang.Character.isSpaceChar(editable.charAt(spanUnionStart - 1)))) {
                            spanUnionEnd = spanUnionEnd + 1;
                        }
                        mTextView.deleteText_internal(spanUnionStart, spanUnionEnd);
                    }
                    hideWithCleanUp();
                }
            });
        }

        public boolean isShowingUp() {
            return mIsShowingUp;
        }

        public void onParentLostFocus() {
            mIsShowingUp = false;
        }

        private class SuggestionAdapter extends android.widget.BaseAdapter {
            private android.view.LayoutInflater mInflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));

            @java.lang.Override
            public int getCount() {
                return mNumberOfSuggestions;
            }

            @java.lang.Override
            public java.lang.Object getItem(int position) {
                return mSuggestionInfos[position];
            }

            @java.lang.Override
            public long getItemId(int position) {
                return position;
            }

            @java.lang.Override
            public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                android.widget.TextView textView = ((android.widget.TextView) (convertView));
                if (textView == null) {
                    textView = ((android.widget.TextView) (mInflater.inflate(mTextView.mTextEditSuggestionItemLayout, parent, false)));
                }
                final android.widget.Editor.SuggestionInfo suggestionInfo = mSuggestionInfos[position];
                textView.setText(suggestionInfo.mText);
                return textView;
            }
        }

        @java.lang.Override
        public void show() {
            if (!(mTextView.getText() instanceof android.text.Editable))
                return;

            if (extractedTextModeWillBeStarted()) {
                return;
            }
            if (updateSuggestions()) {
                mCursorWasVisibleBeforeSuggestions = mCursorVisible;
                mTextView.setCursorVisible(false);
                mIsShowingUp = true;
                super.show();
            }
            mSuggestionListView.setVisibility(mNumberOfSuggestions == 0 ? android.view.View.GONE : android.view.View.VISIBLE);
        }

        @java.lang.Override
        protected void measureContent() {
            final android.util.DisplayMetrics displayMetrics = mTextView.getResources().getDisplayMetrics();
            final int horizontalMeasure = android.view.View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, android.view.View.MeasureSpec.AT_MOST);
            final int verticalMeasure = android.view.View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, android.view.View.MeasureSpec.AT_MOST);
            int width = 0;
            android.view.View view = null;
            for (int i = 0; i < mNumberOfSuggestions; i++) {
                view = mSuggestionsAdapter.getView(i, view, mContentView);
                view.getLayoutParams().width = android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
                view.measure(horizontalMeasure, verticalMeasure);
                width = java.lang.Math.max(width, view.getMeasuredWidth());
            }
            if (mAddToDictionaryButton.getVisibility() != android.view.View.GONE) {
                mAddToDictionaryButton.measure(horizontalMeasure, verticalMeasure);
                width = java.lang.Math.max(width, mAddToDictionaryButton.getMeasuredWidth());
            }
            mDeleteButton.measure(horizontalMeasure, verticalMeasure);
            width = java.lang.Math.max(width, mDeleteButton.getMeasuredWidth());
            width += (mContainerView.getPaddingLeft() + mContainerView.getPaddingRight()) + mContainerMarginWidth;
            // Enforce the width based on actual text widths
            mContentView.measure(android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY), verticalMeasure);
            android.graphics.drawable.Drawable popupBackground = mPopupWindow.getBackground();
            if (popupBackground != null) {
                if (mTempRect == null)
                    mTempRect = new android.graphics.Rect();

                popupBackground.getPadding(mTempRect);
                width += mTempRect.left + mTempRect.right;
            }
            mPopupWindow.setWidth(width);
        }

        @java.lang.Override
        protected int getTextOffset() {
            return (mTextView.getSelectionStart() + mTextView.getSelectionStart()) / 2;
        }

        @java.lang.Override
        protected int getVerticalLocalPosition(int line) {
            final android.text.Layout layout = mTextView.getLayout();
            return layout.getLineBottomWithoutSpacing(line) - mContainerMarginTop;
        }

        @java.lang.Override
        protected int clipVertically(int positionY) {
            final int height = mContentView.getMeasuredHeight();
            final android.util.DisplayMetrics displayMetrics = mTextView.getResources().getDisplayMetrics();
            return java.lang.Math.min(positionY, displayMetrics.heightPixels - height);
        }

        private void hideWithCleanUp() {
            for (final android.widget.Editor.SuggestionInfo info : mSuggestionInfos) {
                info.clear();
            }
            mMisspelledSpanInfo.clear();
            hide();
        }

        private boolean updateSuggestions() {
            android.text.Spannable spannable = ((android.text.Spannable) (mTextView.getText()));
            mNumberOfSuggestions = mSuggestionHelper.getSuggestionInfo(mSuggestionInfos, mMisspelledSpanInfo);
            if ((mNumberOfSuggestions == 0) && (mMisspelledSpanInfo.mSuggestionSpan == null)) {
                return false;
            }
            int spanUnionStart = mTextView.getText().length();
            int spanUnionEnd = 0;
            for (int i = 0; i < mNumberOfSuggestions; i++) {
                final android.widget.Editor.SuggestionSpanInfo spanInfo = mSuggestionInfos[i].mSuggestionSpanInfo;
                spanUnionStart = java.lang.Math.min(spanUnionStart, spanInfo.mSpanStart);
                spanUnionEnd = java.lang.Math.max(spanUnionEnd, spanInfo.mSpanEnd);
            }
            if (mMisspelledSpanInfo.mSuggestionSpan != null) {
                spanUnionStart = java.lang.Math.min(spanUnionStart, mMisspelledSpanInfo.mSpanStart);
                spanUnionEnd = java.lang.Math.max(spanUnionEnd, mMisspelledSpanInfo.mSpanEnd);
            }
            for (int i = 0; i < mNumberOfSuggestions; i++) {
                highlightTextDifferences(mSuggestionInfos[i], spanUnionStart, spanUnionEnd);
            }
            // Make "Add to dictionary" item visible if there is a span with the misspelled flag
            int addToDictionaryButtonVisibility = android.view.View.GONE;
            if (mMisspelledSpanInfo.mSuggestionSpan != null) {
                if ((mMisspelledSpanInfo.mSpanStart >= 0) && (mMisspelledSpanInfo.mSpanEnd > mMisspelledSpanInfo.mSpanStart)) {
                    addToDictionaryButtonVisibility = android.view.View.VISIBLE;
                }
            }
            mAddToDictionaryButton.setVisibility(addToDictionaryButtonVisibility);
            if (mSuggestionRangeSpan == null)
                mSuggestionRangeSpan = new android.text.style.SuggestionRangeSpan();

            final int underlineColor;
            if (mNumberOfSuggestions != 0) {
                underlineColor = mSuggestionInfos[0].mSuggestionSpanInfo.mSuggestionSpan.getUnderlineColor();
            } else {
                underlineColor = mMisspelledSpanInfo.mSuggestionSpan.getUnderlineColor();
            }
            if (underlineColor == 0) {
                // Fallback on the default highlight color when the first span does not provide one
                mSuggestionRangeSpan.setBackgroundColor(mTextView.mHighlightColor);
            } else {
                final float BACKGROUND_TRANSPARENCY = 0.4F;
                final int newAlpha = ((int) (android.graphics.Color.alpha(underlineColor) * BACKGROUND_TRANSPARENCY));
                mSuggestionRangeSpan.setBackgroundColor((underlineColor & 0xffffff) + (newAlpha << 24));
            }
            spannable.setSpan(mSuggestionRangeSpan, spanUnionStart, spanUnionEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mSuggestionsAdapter.notifyDataSetChanged();
            return true;
        }

        private void highlightTextDifferences(android.widget.Editor.SuggestionInfo suggestionInfo, int unionStart, int unionEnd) {
            final android.text.Spannable text = ((android.text.Spannable) (mTextView.getText()));
            final int spanStart = suggestionInfo.mSuggestionSpanInfo.mSpanStart;
            final int spanEnd = suggestionInfo.mSuggestionSpanInfo.mSpanEnd;
            // Adjust the start/end of the suggestion span
            suggestionInfo.mSuggestionStart = spanStart - unionStart;
            suggestionInfo.mSuggestionEnd = suggestionInfo.mSuggestionStart + suggestionInfo.mText.length();
            suggestionInfo.mText.setSpan(mHighlightSpan, 0, suggestionInfo.mText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // Add the text before and after the span.
            final java.lang.String textAsString = text.toString();
            suggestionInfo.mText.insert(0, textAsString.substring(unionStart, spanStart));
            suggestionInfo.mText.append(textAsString.substring(spanEnd, unionEnd));
        }

        @java.lang.Override
        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            android.widget.Editor.SuggestionInfo suggestionInfo = mSuggestionInfos[position];
            replaceWithSuggestion(suggestionInfo);
            hideWithCleanUp();
        }
    }

    /**
     * An ActionMode Callback class that is used to provide actions while in text insertion or
     * selection mode.
     *
     * The default callback provides a subset of Select All, Cut, Copy, Paste, Share and Replace
     * actions, depending on which of these this TextView supports and the current selection.
     */
    private class TextActionModeCallback extends android.view.ActionMode.Callback2 {
        private final android.graphics.Path mSelectionPath = new android.graphics.Path();

        private final android.graphics.RectF mSelectionBounds = new android.graphics.RectF();

        private final boolean mHasSelection;

        private final int mHandleHeight;

        private final java.util.Map<android.view.MenuItem, android.view.View.OnClickListener> mAssistClickHandlers = new java.util.HashMap<>();

        TextActionModeCallback(@android.widget.Editor.TextActionMode
        int mode) {
            mHasSelection = (mode == android.widget.Editor.TextActionMode.SELECTION) || (mTextIsSelectable && (mode == android.widget.Editor.TextActionMode.TEXT_LINK));
            if (mHasSelection) {
                android.widget.Editor.SelectionModifierCursorController selectionController = getSelectionController();
                if (selectionController.mStartHandle == null) {
                    // As these are for initializing selectionController, hide() must be called.
                    /* overwrite */
                    loadHandleDrawables(false);
                    selectionController.initHandles();
                    selectionController.hide();
                }
                mHandleHeight = java.lang.Math.max(mSelectHandleLeft.getMinimumHeight(), mSelectHandleRight.getMinimumHeight());
            } else {
                android.widget.Editor.InsertionPointCursorController insertionController = getInsertionController();
                if (insertionController != null) {
                    insertionController.getHandle();
                    mHandleHeight = mSelectHandleCenter.getMinimumHeight();
                } else {
                    mHandleHeight = 0;
                }
            }
        }

        @java.lang.Override
        public boolean onCreateActionMode(android.view.ActionMode mode, android.view.Menu menu) {
            mAssistClickHandlers.clear();
            mode.setTitle(null);
            mode.setSubtitle(null);
            mode.setTitleOptionalHint(true);
            populateMenuWithItems(menu);
            android.view.ActionMode.Callback customCallback = getCustomCallback();
            if (customCallback != null) {
                if (!customCallback.onCreateActionMode(mode, menu)) {
                    // The custom mode can choose to cancel the action mode, dismiss selection.
                    android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), mTextView.getSelectionEnd());
                    return false;
                }
            }
            if (mTextView.canProcessText()) {
                mProcessTextIntentActionsHandler.onInitializeMenu(menu);
            }
            if (mHasSelection && (!mTextView.hasTransientState())) {
                mTextView.setHasTransientState(true);
            }
            return true;
        }

        private android.view.ActionMode.Callback getCustomCallback() {
            return mHasSelection ? mCustomSelectionActionModeCallback : mCustomInsertionActionModeCallback;
        }

        private void populateMenuWithItems(android.view.Menu menu) {
            if (mTextView.canCut()) {
                menu.add(android.view.Menu.NONE, android.widget.TextView.ID_CUT, android.widget.Editor.MENU_ITEM_ORDER_CUT, com.android.internal.R.string.cut).setAlphabeticShortcut('x').setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            if (mTextView.canCopy()) {
                menu.add(android.view.Menu.NONE, android.widget.TextView.ID_COPY, android.widget.Editor.MENU_ITEM_ORDER_COPY, com.android.internal.R.string.copy).setAlphabeticShortcut('c').setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            if (mTextView.canPaste()) {
                menu.add(android.view.Menu.NONE, android.widget.TextView.ID_PASTE, android.widget.Editor.MENU_ITEM_ORDER_PASTE, com.android.internal.R.string.paste).setAlphabeticShortcut('v').setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
            }
            if (mTextView.canShare()) {
                menu.add(android.view.Menu.NONE, android.widget.TextView.ID_SHARE, android.widget.Editor.MENU_ITEM_ORDER_SHARE, com.android.internal.R.string.share).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
            if (mTextView.canRequestAutofill()) {
                final java.lang.String selected = mTextView.getSelectedText();
                if ((selected == null) || selected.isEmpty()) {
                    menu.add(android.view.Menu.NONE, android.widget.TextView.ID_AUTOFILL, android.widget.Editor.MENU_ITEM_ORDER_AUTOFILL, com.android.internal.R.string.autofill).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_NEVER);
                }
            }
            if (mTextView.canPasteAsPlainText()) {
                menu.add(android.view.Menu.NONE, android.widget.TextView.ID_PASTE_AS_PLAIN_TEXT, android.widget.Editor.MENU_ITEM_ORDER_PASTE_AS_PLAIN_TEXT, com.android.internal.R.string.paste_as_plain_text).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            }
            updateSelectAllItem(menu);
            updateReplaceItem(menu);
            updateAssistMenuItems(menu);
        }

        @java.lang.Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, android.view.Menu menu) {
            updateSelectAllItem(menu);
            updateReplaceItem(menu);
            updateAssistMenuItems(menu);
            android.view.ActionMode.Callback customCallback = getCustomCallback();
            if (customCallback != null) {
                return customCallback.onPrepareActionMode(mode, menu);
            }
            return true;
        }

        private void updateSelectAllItem(android.view.Menu menu) {
            boolean canSelectAll = mTextView.canSelectAllText();
            boolean selectAllItemExists = menu.findItem(android.widget.TextView.ID_SELECT_ALL) != null;
            if (canSelectAll && (!selectAllItemExists)) {
                menu.add(android.view.Menu.NONE, android.widget.TextView.ID_SELECT_ALL, android.widget.Editor.MENU_ITEM_ORDER_SELECT_ALL, com.android.internal.R.string.selectAll).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            } else
                if ((!canSelectAll) && selectAllItemExists) {
                    menu.removeItem(android.widget.TextView.ID_SELECT_ALL);
                }

        }

        private void updateReplaceItem(android.view.Menu menu) {
            boolean canReplace = mTextView.isSuggestionsEnabled() && shouldOfferToShowSuggestions();
            boolean replaceItemExists = menu.findItem(android.widget.TextView.ID_REPLACE) != null;
            if (canReplace && (!replaceItemExists)) {
                menu.add(android.view.Menu.NONE, android.widget.TextView.ID_REPLACE, android.widget.Editor.MENU_ITEM_ORDER_REPLACE, com.android.internal.R.string.replace).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_IF_ROOM);
            } else
                if ((!canReplace) && replaceItemExists) {
                    menu.removeItem(android.widget.TextView.ID_REPLACE);
                }

        }

        private void updateAssistMenuItems(android.view.Menu menu) {
            clearAssistMenuItems(menu);
            if (!shouldEnableAssistMenuItems()) {
                return;
            }
            final android.view.textclassifier.TextClassification textClassification = getSelectionActionModeHelper().getTextClassification();
            if (textClassification == null) {
                return;
            }
            if (!textClassification.getActions().isEmpty()) {
                // Primary assist action (Always shown).
                final android.view.MenuItem item = addAssistMenuItem(menu, textClassification.getActions().get(0), android.widget.TextView.ID_ASSIST, android.widget.Editor.MENU_ITEM_ORDER_ASSIST, android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
                item.setIntent(textClassification.getIntent());
            } else
                if (hasLegacyAssistItem(textClassification)) {
                    // Legacy primary assist action (Always shown).
                    final android.view.MenuItem item = menu.add(android.widget.TextView.ID_ASSIST, android.widget.TextView.ID_ASSIST, android.widget.Editor.MENU_ITEM_ORDER_ASSIST, textClassification.getLabel()).setIcon(textClassification.getIcon()).setIntent(textClassification.getIntent());
                    item.setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_ALWAYS);
                    mAssistClickHandlers.put(item, android.view.textclassifier.TextClassification.createIntentOnClickListener(android.view.textclassifier.TextClassification.createPendingIntent(mTextView.getContext(), textClassification.getIntent(), createAssistMenuItemPendingIntentRequestCode())));
                }

            final int count = textClassification.getActions().size();
            for (int i = 1; i < count; i++) {
                // Secondary assist action (Never shown).
                addAssistMenuItem(menu, textClassification.getActions().get(i), android.view.Menu.NONE, (android.widget.Editor.MENU_ITEM_ORDER_SECONDARY_ASSIST_ACTIONS_START + i) - 1, android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            }
        }

        private android.view.MenuItem addAssistMenuItem(android.view.Menu menu, android.app.RemoteAction action, int itemId, int order, int showAsAction) {
            final android.view.MenuItem item = menu.add(android.widget.TextView.ID_ASSIST, itemId, order, action.getTitle()).setContentDescription(action.getContentDescription());
            if (action.shouldShowIcon()) {
                item.setIcon(action.getIcon().loadDrawable(mTextView.getContext()));
            }
            item.setShowAsAction(showAsAction);
            mAssistClickHandlers.put(item, android.view.textclassifier.TextClassification.createIntentOnClickListener(action.getActionIntent()));
            return item;
        }

        private void clearAssistMenuItems(android.view.Menu menu) {
            int i = 0;
            while (i < menu.size()) {
                final android.view.MenuItem menuItem = menu.getItem(i);
                if (menuItem.getGroupId() == android.widget.TextView.ID_ASSIST) {
                    menu.removeItem(menuItem.getItemId());
                    continue;
                }
                i++;
            } 
        }

        private boolean hasLegacyAssistItem(android.view.textclassifier.TextClassification classification) {
            // Check whether we have the UI data and and action.
            return ((classification.getIcon() != null) || (!android.text.TextUtils.isEmpty(classification.getLabel()))) && ((classification.getIntent() != null) || (classification.getOnClickListener() != null));
        }

        private boolean onAssistMenuItemClicked(android.view.MenuItem assistMenuItem) {
            com.android.internal.util.Preconditions.checkArgument(assistMenuItem.getGroupId() == android.widget.TextView.ID_ASSIST);
            final android.view.textclassifier.TextClassification textClassification = getSelectionActionModeHelper().getTextClassification();
            if ((!shouldEnableAssistMenuItems()) || (textClassification == null)) {
                // No textClassification result to handle the click. Eat the click.
                return true;
            }
            android.view.View.OnClickListener onClickListener = mAssistClickHandlers.get(assistMenuItem);
            if (onClickListener == null) {
                final android.content.Intent intent = assistMenuItem.getIntent();
                if (intent != null) {
                    onClickListener = android.view.textclassifier.TextClassification.createIntentOnClickListener(android.view.textclassifier.TextClassification.createPendingIntent(mTextView.getContext(), intent, createAssistMenuItemPendingIntentRequestCode()));
                }
            }
            if (onClickListener != null) {
                onClickListener.onClick(mTextView);
                stopTextActionMode();
            }
            // We tried our best.
            return true;
        }

        private int createAssistMenuItemPendingIntentRequestCode() {
            return mTextView.hasSelection() ? mTextView.getText().subSequence(mTextView.getSelectionStart(), mTextView.getSelectionEnd()).hashCode() : 0;
        }

        private boolean shouldEnableAssistMenuItems() {
            return mTextView.isDeviceProvisioned() && android.view.textclassifier.TextClassificationManager.getSettings(mTextView.getContext()).isSmartTextShareEnabled();
        }

        @java.lang.Override
        public boolean onActionItemClicked(android.view.ActionMode mode, android.view.MenuItem item) {
            getSelectionActionModeHelper().onSelectionAction(item.getItemId(), item.getTitle().toString());
            if (mProcessTextIntentActionsHandler.performMenuItemAction(item)) {
                return true;
            }
            android.view.ActionMode.Callback customCallback = getCustomCallback();
            if ((customCallback != null) && customCallback.onActionItemClicked(mode, item)) {
                return true;
            }
            if ((item.getGroupId() == android.widget.TextView.ID_ASSIST) && onAssistMenuItemClicked(item)) {
                return true;
            }
            return mTextView.onTextContextMenuItem(item.getItemId());
        }

        @java.lang.Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            // Clear mTextActionMode not to recursively destroy action mode by clearing selection.
            getSelectionActionModeHelper().onDestroyActionMode();
            mTextActionMode = null;
            android.view.ActionMode.Callback customCallback = getCustomCallback();
            if (customCallback != null) {
                customCallback.onDestroyActionMode(mode);
            }
            if (!mPreserveSelection) {
                /* Leave current selection when we tentatively destroy action mode for the
                selection. If we're detaching from a window, we'll bring back the selection
                mode when (if) we get reattached.
                 */
                android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), mTextView.getSelectionEnd());
            }
            if (mSelectionModifierCursorController != null) {
                mSelectionModifierCursorController.hide();
            }
            mAssistClickHandlers.clear();
            mRequestingLinkActionMode = false;
        }

        @java.lang.Override
        public void onGetContentRect(android.view.ActionMode mode, android.view.View view, android.graphics.Rect outRect) {
            if ((!view.equals(mTextView)) || (mTextView.getLayout() == null)) {
                super.onGetContentRect(mode, view, outRect);
                return;
            }
            if (mTextView.getSelectionStart() != mTextView.getSelectionEnd()) {
                // We have a selection.
                mSelectionPath.reset();
                mTextView.getLayout().getSelectionPath(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), mSelectionPath);
                mSelectionPath.computeBounds(mSelectionBounds, true);
                mSelectionBounds.bottom += mHandleHeight;
            } else {
                // We have a cursor.
                android.text.Layout layout = mTextView.getLayout();
                int line = layout.getLineForOffset(mTextView.getSelectionStart());
                float primaryHorizontal = clampHorizontalPosition(null, layout.getPrimaryHorizontal(mTextView.getSelectionStart()));
                mSelectionBounds.set(primaryHorizontal, layout.getLineTop(line), primaryHorizontal, layout.getLineBottom(line) + mHandleHeight);
            }
            // Take TextView's padding and scroll into account.
            int textHorizontalOffset = mTextView.viewportToContentHorizontalOffset();
            int textVerticalOffset = mTextView.viewportToContentVerticalOffset();
            outRect.set(((int) (java.lang.Math.floor(mSelectionBounds.left + textHorizontalOffset))), ((int) (java.lang.Math.floor(mSelectionBounds.top + textVerticalOffset))), ((int) (java.lang.Math.ceil(mSelectionBounds.right + textHorizontalOffset))), ((int) (java.lang.Math.ceil(mSelectionBounds.bottom + textVerticalOffset))));
        }
    }

    /**
     * A listener to call {@link InputMethodManager#updateCursorAnchorInfo(View, CursorAnchorInfo)}
     * while the input method is requesting the cursor/anchor position. Does nothing as long as
     * {@link InputMethodManager#isWatchingCursor(View)} returns false.
     */
    private final class CursorAnchorInfoNotifier implements android.widget.Editor.TextViewPositionListener {
        final android.view.inputmethod.CursorAnchorInfo.Builder mSelectionInfoBuilder = new android.view.inputmethod.CursorAnchorInfo.Builder();

        final int[] mTmpIntOffset = new int[2];

        final android.graphics.Matrix mViewToScreenMatrix = new android.graphics.Matrix();

        @java.lang.Override
        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            final android.widget.Editor.InputMethodState ims = mInputMethodState;
            if ((ims == null) || (ims.mBatchEditNesting > 0)) {
                return;
            }
            final android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            if (null == imm) {
                return;
            }
            if (!imm.isActive(mTextView)) {
                return;
            }
            // Skip if the IME has not requested the cursor/anchor position.
            if (!imm.isCursorAnchorInfoEnabled()) {
                return;
            }
            android.text.Layout layout = mTextView.getLayout();
            if (layout == null) {
                return;
            }
            final android.view.inputmethod.CursorAnchorInfo.Builder builder = mSelectionInfoBuilder;
            builder.reset();
            final int selectionStart = mTextView.getSelectionStart();
            builder.setSelectionRange(selectionStart, mTextView.getSelectionEnd());
            // Construct transformation matrix from view local coordinates to screen coordinates.
            mViewToScreenMatrix.set(mTextView.getMatrix());
            mTextView.getLocationOnScreen(mTmpIntOffset);
            mViewToScreenMatrix.postTranslate(mTmpIntOffset[0], mTmpIntOffset[1]);
            builder.setMatrix(mViewToScreenMatrix);
            final float viewportToContentHorizontalOffset = mTextView.viewportToContentHorizontalOffset();
            final float viewportToContentVerticalOffset = mTextView.viewportToContentVerticalOffset();
            final java.lang.CharSequence text = mTextView.getText();
            if (text instanceof android.text.Spannable) {
                final android.text.Spannable sp = ((android.text.Spannable) (text));
                int composingTextStart = com.android.internal.widget.EditableInputConnection.getComposingSpanStart(sp);
                int composingTextEnd = com.android.internal.widget.EditableInputConnection.getComposingSpanEnd(sp);
                if (composingTextEnd < composingTextStart) {
                    final int temp = composingTextEnd;
                    composingTextEnd = composingTextStart;
                    composingTextStart = temp;
                }
                final boolean hasComposingText = (0 <= composingTextStart) && (composingTextStart < composingTextEnd);
                if (hasComposingText) {
                    final java.lang.CharSequence composingText = text.subSequence(composingTextStart, composingTextEnd);
                    builder.setComposingText(composingTextStart, composingText);
                    mTextView.populateCharacterBounds(builder, composingTextStart, composingTextEnd, viewportToContentHorizontalOffset, viewportToContentVerticalOffset);
                }
            }
            // Treat selectionStart as the insertion point.
            if (0 <= selectionStart) {
                final int offset = selectionStart;
                final int line = layout.getLineForOffset(offset);
                final float insertionMarkerX = layout.getPrimaryHorizontal(offset) + viewportToContentHorizontalOffset;
                final float insertionMarkerTop = layout.getLineTop(line) + viewportToContentVerticalOffset;
                final float insertionMarkerBaseline = layout.getLineBaseline(line) + viewportToContentVerticalOffset;
                final float insertionMarkerBottom = layout.getLineBottomWithoutSpacing(line) + viewportToContentVerticalOffset;
                final boolean isTopVisible = mTextView.isPositionVisible(insertionMarkerX, insertionMarkerTop);
                final boolean isBottomVisible = mTextView.isPositionVisible(insertionMarkerX, insertionMarkerBottom);
                int insertionMarkerFlags = 0;
                if (isTopVisible || isBottomVisible) {
                    insertionMarkerFlags |= android.view.inputmethod.CursorAnchorInfo.FLAG_HAS_VISIBLE_REGION;
                }
                if ((!isTopVisible) || (!isBottomVisible)) {
                    insertionMarkerFlags |= android.view.inputmethod.CursorAnchorInfo.FLAG_HAS_INVISIBLE_REGION;
                }
                if (layout.isRtlCharAt(offset)) {
                    insertionMarkerFlags |= android.view.inputmethod.CursorAnchorInfo.FLAG_IS_RTL;
                }
                builder.setInsertionMarkerLocation(insertionMarkerX, insertionMarkerTop, insertionMarkerBaseline, insertionMarkerBottom, insertionMarkerFlags);
            }
            imm.updateCursorAnchorInfo(mTextView, builder.build());
        }
    }

    private static class MagnifierMotionAnimator {
        /* miliseconds */
        private static final long DURATION = 100;

        // The magnifier being animated.
        private final android.widget.Magnifier mMagnifier;

        // A value animator used to animate the magnifier.
        private final android.animation.ValueAnimator mAnimator;

        // Whether the magnifier is currently visible.
        private boolean mMagnifierIsShowing;

        // The coordinates of the magnifier when the currently running animation started.
        private float mAnimationStartX;

        private float mAnimationStartY;

        // The coordinates of the magnifier in the latest animation frame.
        private float mAnimationCurrentX;

        private float mAnimationCurrentY;

        // The latest coordinates the motion animator was asked to #show() the magnifier at.
        private float mLastX;

        private float mLastY;

        private MagnifierMotionAnimator(final android.widget.Magnifier magnifier) {
            mMagnifier = magnifier;
            // Prepare the animator used to run the motion animation.
            mAnimator = android.animation.ValueAnimator.ofFloat(0, 1);
            mAnimator.setDuration(android.widget.Editor.MagnifierMotionAnimator.DURATION);
            mAnimator.setInterpolator(new android.view.animation.LinearInterpolator());
            mAnimator.addUpdateListener(( animation) -> {
                // Interpolate to find the current position of the magnifier.
                mAnimationCurrentX = mAnimationStartX + ((mLastX - mAnimationStartX) * animation.getAnimatedFraction());
                mAnimationCurrentY = mAnimationStartY + ((mLastY - mAnimationStartY) * animation.getAnimatedFraction());
                mMagnifier.show(mAnimationCurrentX, mAnimationCurrentY);
            });
        }

        /**
         * Shows the magnifier at a new position.
         * If the y coordinate is different from the previous y coordinate
         * (probably corresponding to a line jump in the text), a short
         * animation is added to the jump.
         */
        private void show(final float x, final float y) {
            final boolean startNewAnimation = mMagnifierIsShowing && (y != mLastY);
            if (startNewAnimation) {
                if (mAnimator.isRunning()) {
                    mAnimator.cancel();
                    mAnimationStartX = mAnimationCurrentX;
                    mAnimationStartY = mAnimationCurrentY;
                } else {
                    mAnimationStartX = mLastX;
                    mAnimationStartY = mLastY;
                }
                mAnimator.start();
            } else {
                if (!mAnimator.isRunning()) {
                    mMagnifier.show(x, y);
                }
            }
            mLastX = x;
            mLastY = y;
            mMagnifierIsShowing = true;
        }

        /**
         * Updates the content of the magnifier.
         */
        private void update() {
            mMagnifier.update();
        }

        /**
         * Dismisses the magnifier, or does nothing if it is already dismissed.
         */
        private void dismiss() {
            mMagnifier.dismiss();
            mAnimator.cancel();
            mMagnifierIsShowing = false;
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    public abstract class HandleView extends android.view.View implements android.widget.Editor.TextViewPositionListener {
        protected android.graphics.drawable.Drawable mDrawable;

        protected android.graphics.drawable.Drawable mDrawableLtr;

        protected android.graphics.drawable.Drawable mDrawableRtl;

        private final android.widget.PopupWindow mContainer;

        private int mPositionX;

        // Position with respect to the parent TextView
        private int mPositionY;

        private boolean mIsDragging;

        private float mTouchToWindowOffsetX;

        // Offset from touch position to mPosition
        private float mTouchToWindowOffsetY;

        protected int mHotspotX;

        protected int mHorizontalGravity;

        // Offsets the hotspot point up, so that cursor is not hidden by the finger when moving up
        private float mTouchOffsetY;

        // Where the touch position should be on the handle to ensure a maximum cursor visibility
        private float mIdealVerticalOffset;

        private int mLastParentX;

        // Parent's (TextView) previous position in window
        private int mLastParentY;

        private int mLastParentXOnScreen;

        // Parent's (TextView) previous position on screen
        private int mLastParentYOnScreen;

        // Previous text character offset
        protected int mPreviousOffset = -1;

        // Previous text character offset
        private boolean mPositionHasChanged = true;

        // Minimum touch target size for handles
        private int mMinSize;

        // Indicates the line of text that the handle is on.
        protected int mPrevLine = android.widget.Editor.UNSET_LINE;

        // Indicates the line of text that the user was touching. This can differ from mPrevLine
        // when selecting text when the handles jump to the end / start of words which may be on
        // a different line.
        protected int mPreviousLineTouched = android.widget.Editor.UNSET_LINE;

        // The raw x coordinate of the motion down event which started the current dragging session.
        // Only used and stored when magnifier is used.
        private float mCurrentDragInitialTouchRawX = android.widget.Editor.UNSET_X_VALUE;

        // The scale transform applied by containers to the TextView. Only used and computed
        // when magnifier is used.
        private float mTextViewScaleX;

        private float mTextViewScaleY;

        private HandleView(android.graphics.drawable.Drawable drawableLtr, android.graphics.drawable.Drawable drawableRtl, final int id) {
            super(mTextView.getContext());
            setId(id);
            mContainer = new android.widget.PopupWindow(mTextView.getContext(), null, com.android.internal.R.attr.textSelectHandleWindowStyle);
            mContainer.setSplitTouchEnabled(true);
            mContainer.setClippingEnabled(false);
            mContainer.setWindowLayoutType(android.view.WindowManager.LayoutParams.TYPE_APPLICATION_SUB_PANEL);
            mContainer.setWidth(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            mContainer.setHeight(android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            mContainer.setContentView(this);
            setDrawables(drawableLtr, drawableRtl);
            mMinSize = mTextView.getContext().getResources().getDimensionPixelSize(com.android.internal.R.dimen.text_handle_min_size);
            final int handleHeight = getPreferredHeight();
            mTouchOffsetY = (-0.3F) * handleHeight;
            mIdealVerticalOffset = 0.7F * handleHeight;
        }

        public float getIdealVerticalOffset() {
            return mIdealVerticalOffset;
        }

        void setDrawables(final android.graphics.drawable.Drawable drawableLtr, final android.graphics.drawable.Drawable drawableRtl) {
            mDrawableLtr = drawableLtr;
            mDrawableRtl = drawableRtl;
            /* updateDrawableWhenDragging */
            updateDrawable(true);
        }

        protected void updateDrawable(final boolean updateDrawableWhenDragging) {
            if ((!updateDrawableWhenDragging) && mIsDragging) {
                return;
            }
            final android.text.Layout layout = mTextView.getLayout();
            if (layout == null) {
                return;
            }
            final int offset = getCurrentCursorOffset();
            final boolean isRtlCharAtOffset = isAtRtlRun(layout, offset);
            final android.graphics.drawable.Drawable oldDrawable = mDrawable;
            mDrawable = (isRtlCharAtOffset) ? mDrawableRtl : mDrawableLtr;
            mHotspotX = getHotspotX(mDrawable, isRtlCharAtOffset);
            mHorizontalGravity = getHorizontalGravity(isRtlCharAtOffset);
            if ((oldDrawable != mDrawable) && isShowing()) {
                // Update popup window position.
                mPositionX = ((getCursorHorizontalPosition(layout, offset) - mHotspotX) - getHorizontalOffset()) + getCursorOffset();
                mPositionX += mTextView.viewportToContentHorizontalOffset();
                mPositionHasChanged = true;
                updatePosition(mLastParentX, mLastParentY, false, false);
                postInvalidate();
            }
        }

        protected abstract int getHotspotX(android.graphics.drawable.Drawable drawable, boolean isRtlRun);

        protected abstract int getHorizontalGravity(boolean isRtlRun);

        // Touch-up filter: number of previous positions remembered
        private static final int HISTORY_SIZE = 5;

        private static final int TOUCH_UP_FILTER_DELAY_AFTER = 150;

        private static final int TOUCH_UP_FILTER_DELAY_BEFORE = 350;

        private final long[] mPreviousOffsetsTimes = new long[android.widget.Editor.HandleView.HISTORY_SIZE];

        private final int[] mPreviousOffsets = new int[android.widget.Editor.HandleView.HISTORY_SIZE];

        private int mPreviousOffsetIndex = 0;

        private int mNumberPreviousOffsets = 0;

        private void startTouchUpFilter(int offset) {
            mNumberPreviousOffsets = 0;
            addPositionToTouchUpFilter(offset);
        }

        private void addPositionToTouchUpFilter(int offset) {
            mPreviousOffsetIndex = (mPreviousOffsetIndex + 1) % android.widget.Editor.HandleView.HISTORY_SIZE;
            mPreviousOffsets[mPreviousOffsetIndex] = offset;
            mPreviousOffsetsTimes[mPreviousOffsetIndex] = android.os.SystemClock.uptimeMillis();
            mNumberPreviousOffsets++;
        }

        private void filterOnTouchUp(boolean fromTouchScreen) {
            final long now = android.os.SystemClock.uptimeMillis();
            int i = 0;
            int index = mPreviousOffsetIndex;
            final int iMax = java.lang.Math.min(mNumberPreviousOffsets, android.widget.Editor.HandleView.HISTORY_SIZE);
            while ((i < iMax) && ((now - mPreviousOffsetsTimes[index]) < android.widget.Editor.HandleView.TOUCH_UP_FILTER_DELAY_AFTER)) {
                i++;
                index = ((mPreviousOffsetIndex - i) + android.widget.Editor.HandleView.HISTORY_SIZE) % android.widget.Editor.HandleView.HISTORY_SIZE;
            } 
            if (((i > 0) && (i < iMax)) && ((now - mPreviousOffsetsTimes[index]) > android.widget.Editor.HandleView.TOUCH_UP_FILTER_DELAY_BEFORE)) {
                positionAtCursorOffset(mPreviousOffsets[index], false, fromTouchScreen);
            }
        }

        public boolean offsetHasBeenChanged() {
            return mNumberPreviousOffsets > 1;
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            setMeasuredDimension(getPreferredWidth(), getPreferredHeight());
        }

        @java.lang.Override
        public void invalidate() {
            super.invalidate();
            if (isShowing()) {
                positionAtCursorOffset(getCurrentCursorOffset(), true, false);
            }
        }

        private int getPreferredWidth() {
            return java.lang.Math.max(mDrawable.getIntrinsicWidth(), mMinSize);
        }

        private int getPreferredHeight() {
            return java.lang.Math.max(mDrawable.getIntrinsicHeight(), mMinSize);
        }

        public void show() {
            if (isShowing())
                return;

            /* local position may change */
            getPositionListener().addSubscriber(this, true);
            // Make sure the offset is always considered new, even when focusing at same position
            mPreviousOffset = -1;
            positionAtCursorOffset(getCurrentCursorOffset(), false, false);
        }

        protected void dismiss() {
            mIsDragging = false;
            mContainer.dismiss();
            onDetached();
        }

        public void hide() {
            dismiss();
            getPositionListener().removeSubscriber(this);
        }

        public boolean isShowing() {
            return mContainer.isShowing();
        }

        private boolean shouldShow() {
            // A dragging handle should always be shown.
            if (mIsDragging) {
                return true;
            }
            if (mTextView.isInBatchEditMode()) {
                return false;
            }
            return mTextView.isPositionVisible((mPositionX + mHotspotX) + getHorizontalOffset(), mPositionY);
        }

        private void setVisible(final boolean visible) {
            mContainer.getContentView().setVisibility(visible ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        }

        public abstract int getCurrentCursorOffset();

        protected abstract void updateSelection(int offset);

        protected abstract void updatePosition(float x, float y, boolean fromTouchScreen);

        @android.widget.Editor.MagnifierHandleTrigger
        protected abstract int getMagnifierHandleTrigger();

        protected boolean isAtRtlRun(@android.annotation.NonNull
        android.text.Layout layout, int offset) {
            return layout.isRtlCharAt(offset);
        }

        @com.android.internal.annotations.VisibleForTesting
        public float getHorizontal(@android.annotation.NonNull
        android.text.Layout layout, int offset) {
            return layout.getPrimaryHorizontal(offset);
        }

        protected int getOffsetAtCoordinate(@android.annotation.NonNull
        android.text.Layout layout, int line, float x) {
            return mTextView.getOffsetAtCoordinate(line, x);
        }

        /**
         *
         *
         * @param offset
         * 		Cursor offset. Must be in [-1, length].
         * @param forceUpdatePosition
         * 		whether to force update the position.  This should be true
         * 		when If the parent has been scrolled, for example.
         * @param fromTouchScreen
         * 		{@code true} if the cursor is moved with motion events from the
         * 		touch screen.
         */
        protected void positionAtCursorOffset(int offset, boolean forceUpdatePosition, boolean fromTouchScreen) {
            // A HandleView relies on the layout, which may be nulled by external methods
            android.text.Layout layout = mTextView.getLayout();
            if (layout == null) {
                // Will update controllers' state, hiding them and stopping selection mode if needed
                prepareCursorControllers();
                return;
            }
            layout = mTextView.getLayout();
            boolean offsetChanged = offset != mPreviousOffset;
            if (offsetChanged || forceUpdatePosition) {
                if (offsetChanged) {
                    updateSelection(offset);
                    if (fromTouchScreen && mHapticTextHandleEnabled) {
                        mTextView.performHapticFeedback(android.view.HapticFeedbackConstants.TEXT_HANDLE_MOVE);
                    }
                    addPositionToTouchUpFilter(offset);
                }
                final int line = layout.getLineForOffset(offset);
                mPrevLine = line;
                mPositionX = ((getCursorHorizontalPosition(layout, offset) - mHotspotX) - getHorizontalOffset()) + getCursorOffset();
                mPositionY = layout.getLineBottomWithoutSpacing(line);
                // Take TextView's padding and scroll into account.
                mPositionX += mTextView.viewportToContentHorizontalOffset();
                mPositionY += mTextView.viewportToContentVerticalOffset();
                mPreviousOffset = offset;
                mPositionHasChanged = true;
            }
        }

        /**
         * Return the clamped horizontal position for the cursor.
         *
         * @param layout
         * 		Text layout.
         * @param offset
         * 		Character offset for the cursor.
         * @return The clamped horizontal position for the cursor.
         */
        int getCursorHorizontalPosition(android.text.Layout layout, int offset) {
            return ((int) (getHorizontal(layout, offset) - 0.5F));
        }

        @java.lang.Override
        public void updatePosition(int parentPositionX, int parentPositionY, boolean parentPositionChanged, boolean parentScrolled) {
            positionAtCursorOffset(getCurrentCursorOffset(), parentScrolled, false);
            if (parentPositionChanged || mPositionHasChanged) {
                if (mIsDragging) {
                    // Update touchToWindow offset in case of parent scrolling while dragging
                    if ((parentPositionX != mLastParentX) || (parentPositionY != mLastParentY)) {
                        mTouchToWindowOffsetX += parentPositionX - mLastParentX;
                        mTouchToWindowOffsetY += parentPositionY - mLastParentY;
                        mLastParentX = parentPositionX;
                        mLastParentY = parentPositionY;
                    }
                    onHandleMoved();
                }
                if (shouldShow()) {
                    // Transform to the window coordinates to follow the view tranformation.
                    final int[] pts = new int[]{ (mPositionX + mHotspotX) + getHorizontalOffset(), mPositionY };
                    mTextView.transformFromViewToWindowSpace(pts);
                    pts[0] -= mHotspotX + getHorizontalOffset();
                    if (isShowing()) {
                        mContainer.update(pts[0], pts[1], -1, -1);
                    } else {
                        mContainer.showAtLocation(mTextView, android.view.Gravity.NO_GRAVITY, pts[0], pts[1]);
                    }
                } else {
                    if (isShowing()) {
                        dismiss();
                    }
                }
                mPositionHasChanged = false;
            }
        }

        @java.lang.Override
        protected void onDraw(android.graphics.Canvas c) {
            final int drawWidth = mDrawable.getIntrinsicWidth();
            final int left = getHorizontalOffset();
            mDrawable.setBounds(left, 0, left + drawWidth, mDrawable.getIntrinsicHeight());
            mDrawable.draw(c);
        }

        private int getHorizontalOffset() {
            final int width = getPreferredWidth();
            final int drawWidth = mDrawable.getIntrinsicWidth();
            final int left;
            switch (mHorizontalGravity) {
                case android.view.Gravity.LEFT :
                    left = 0;
                    break;
                default :
                case android.view.Gravity.CENTER :
                    left = (width - drawWidth) / 2;
                    break;
                case android.view.Gravity.RIGHT :
                    left = width - drawWidth;
                    break;
            }
            return left;
        }

        protected int getCursorOffset() {
            return 0;
        }

        private boolean tooLargeTextForMagnifier() {
            final float magnifierContentHeight = java.lang.Math.round(mMagnifierAnimator.mMagnifier.getHeight() / mMagnifierAnimator.mMagnifier.getZoom());
            final android.graphics.Paint.FontMetrics fontMetrics = mTextView.getPaint().getFontMetrics();
            final float glyphHeight = fontMetrics.descent - fontMetrics.ascent;
            return (glyphHeight * mTextViewScaleY) > magnifierContentHeight;
        }

        /**
         * Traverses the hierarchy above the text view, and computes the total scale applied
         * to it. If a rotation is encountered, the method returns {@code false}, indicating
         * that the magnifier should not be shown anyways. It would be nice to keep these two
         * pieces of logic separate (the rotation check and the total scale calculation),
         * but for efficiency we can do them in a single go.
         *
         * @return whether the text view is rotated
         */
        private boolean checkForTransforms() {
            if (mMagnifierAnimator.mMagnifierIsShowing) {
                // Do not check again when the magnifier is currently showing.
                return true;
            }
            if (((mTextView.getRotation() != 0.0F) || (mTextView.getRotationX() != 0.0F)) || (mTextView.getRotationY() != 0.0F)) {
                return false;
            }
            mTextViewScaleX = mTextView.getScaleX();
            mTextViewScaleY = mTextView.getScaleY();
            android.view.ViewParent viewParent = mTextView.getParent();
            while (viewParent != null) {
                if (viewParent instanceof android.view.View) {
                    final android.view.View view = ((android.view.View) (viewParent));
                    if (((view.getRotation() != 0.0F) || (view.getRotationX() != 0.0F)) || (view.getRotationY() != 0.0F)) {
                        return false;
                    }
                    mTextViewScaleX *= view.getScaleX();
                    mTextViewScaleY *= view.getScaleY();
                }
                viewParent = viewParent.getParent();
            } 
            return true;
        }

        /**
         * Computes the position where the magnifier should be shown, relative to
         * {@code mTextView}, and writes them to {@code showPosInView}. Also decides
         * whether the magnifier should be shown or dismissed after this touch event.
         *
         * @return Whether the magnifier should be shown at the computed coordinates or dismissed.
         */
        private boolean obtainMagnifierShowCoordinates(@android.annotation.NonNull
        final android.view.MotionEvent event, final android.graphics.PointF showPosInView) {
            final int trigger = getMagnifierHandleTrigger();
            final int offset;
            final int otherHandleOffset;
            switch (trigger) {
                case android.widget.Editor.MagnifierHandleTrigger.INSERTION :
                    offset = mTextView.getSelectionStart();
                    otherHandleOffset = -1;
                    break;
                case android.widget.Editor.MagnifierHandleTrigger.SELECTION_START :
                    offset = mTextView.getSelectionStart();
                    otherHandleOffset = mTextView.getSelectionEnd();
                    break;
                case android.widget.Editor.MagnifierHandleTrigger.SELECTION_END :
                    offset = mTextView.getSelectionEnd();
                    otherHandleOffset = mTextView.getSelectionStart();
                    break;
                default :
                    offset = -1;
                    otherHandleOffset = -1;
                    break;
            }
            if (offset == (-1)) {
                return false;
            }
            if (event.getActionMasked() == android.view.MotionEvent.ACTION_DOWN) {
                mCurrentDragInitialTouchRawX = event.getRawX();
            } else
                if (event.getActionMasked() == android.view.MotionEvent.ACTION_UP) {
                    mCurrentDragInitialTouchRawX = android.widget.Editor.UNSET_X_VALUE;
                }

            final android.text.Layout layout = mTextView.getLayout();
            final int lineNumber = layout.getLineForOffset(offset);
            // Compute whether the selection handles are currently on the same line, and,
            // in this particular case, whether the selected text is right to left.
            final boolean sameLineSelection = (otherHandleOffset != (-1)) && (lineNumber == layout.getLineForOffset(otherHandleOffset));
            final boolean rtl = sameLineSelection && ((offset < otherHandleOffset) != (getHorizontal(mTextView.getLayout(), offset) < getHorizontal(mTextView.getLayout(), otherHandleOffset)));
            // Horizontally move the magnifier smoothly, clamp inside the current line / selection.
            final int[] textViewLocationOnScreen = new int[2];
            mTextView.getLocationOnScreen(textViewLocationOnScreen);
            final float touchXInView = event.getRawX() - textViewLocationOnScreen[0];
            float leftBound = mTextView.getTotalPaddingLeft() - mTextView.getScrollX();
            float rightBound = mTextView.getTotalPaddingLeft() - mTextView.getScrollX();
            if (sameLineSelection && ((trigger == android.widget.Editor.MagnifierHandleTrigger.SELECTION_END) ^ rtl)) {
                leftBound += getHorizontal(mTextView.getLayout(), otherHandleOffset);
            } else {
                leftBound += mTextView.getLayout().getLineLeft(lineNumber);
            }
            if (sameLineSelection && ((trigger == android.widget.Editor.MagnifierHandleTrigger.SELECTION_START) ^ rtl)) {
                rightBound += getHorizontal(mTextView.getLayout(), otherHandleOffset);
            } else {
                rightBound += mTextView.getLayout().getLineRight(lineNumber);
            }
            leftBound *= mTextViewScaleX;
            rightBound *= mTextViewScaleX;
            final float contentWidth = java.lang.Math.round(mMagnifierAnimator.mMagnifier.getWidth() / mMagnifierAnimator.mMagnifier.getZoom());
            if ((touchXInView < (leftBound - (contentWidth / 2))) || (touchXInView > (rightBound + (contentWidth / 2)))) {
                // The touch is too far from the current line / selection, so hide the magnifier.
                return false;
            }
            final float scaledTouchXInView;
            if (mTextViewScaleX == 1.0F) {
                // In the common case, do not use mCurrentDragInitialTouchRawX to compute this
                // coordinate, although the formula on the else branch should be equivalent.
                // Since the formula relies on mCurrentDragInitialTouchRawX being set on
                // MotionEvent.ACTION_DOWN, this makes us more defensive against cases when
                // the sequence of events might not look as expected: for example, a sequence of
                // ACTION_MOVE not preceded by ACTION_DOWN.
                scaledTouchXInView = touchXInView;
            } else {
                scaledTouchXInView = (((event.getRawX() - mCurrentDragInitialTouchRawX) * mTextViewScaleX) + mCurrentDragInitialTouchRawX) - textViewLocationOnScreen[0];
            }
            showPosInView.x = java.lang.Math.max(leftBound, java.lang.Math.min(rightBound, scaledTouchXInView));
            // Vertically snap to middle of current line.
            showPosInView.y = ((((mTextView.getLayout().getLineTop(lineNumber) + mTextView.getLayout().getLineBottom(lineNumber)) / 2.0F) + mTextView.getTotalPaddingTop()) - mTextView.getScrollY()) * mTextViewScaleY;
            return true;
        }

        private boolean handleOverlapsMagnifier(@android.annotation.NonNull
        final android.widget.Editor.HandleView handle, @android.annotation.NonNull
        final android.graphics.Rect magnifierRect) {
            final android.widget.PopupWindow window = handle.mContainer;
            if (!window.hasDecorView()) {
                return false;
            }
            final android.graphics.Rect handleRect = new android.graphics.Rect(window.getDecorViewLayoutParams().x, window.getDecorViewLayoutParams().y, window.getDecorViewLayoutParams().x + window.getContentView().getWidth(), window.getDecorViewLayoutParams().y + window.getContentView().getHeight());
            return android.graphics.Rect.intersects(handleRect, magnifierRect);
        }

        @android.annotation.Nullable
        private android.widget.Editor.HandleView getOtherSelectionHandle() {
            final android.widget.Editor.SelectionModifierCursorController controller = getSelectionController();
            if ((controller == null) || (!controller.isActive())) {
                return null;
            }
            return controller.mStartHandle != this ? controller.mStartHandle : controller.mEndHandle;
        }

        private void updateHandlesVisibility() {
            final android.graphics.Point magnifierTopLeft = mMagnifierAnimator.mMagnifier.getPosition();
            if (magnifierTopLeft == null) {
                return;
            }
            final android.graphics.Rect magnifierRect = new android.graphics.Rect(magnifierTopLeft.x, magnifierTopLeft.y, magnifierTopLeft.x + mMagnifierAnimator.mMagnifier.getWidth(), magnifierTopLeft.y + mMagnifierAnimator.mMagnifier.getHeight());
            setVisible(!handleOverlapsMagnifier(this, magnifierRect));
            final android.widget.Editor.HandleView otherHandle = getOtherSelectionHandle();
            if (otherHandle != null) {
                otherHandle.setVisible(!handleOverlapsMagnifier(otherHandle, magnifierRect));
            }
        }

        protected final void updateMagnifier(@android.annotation.NonNull
        final android.view.MotionEvent event) {
            if (mMagnifierAnimator == null) {
                return;
            }
            final android.graphics.PointF showPosInView = new android.graphics.PointF();
            final boolean shouldShow = (checkForTransforms()/* check not rotated and compute scale */
             && (!tooLargeTextForMagnifier())) && obtainMagnifierShowCoordinates(event, showPosInView);
            if (shouldShow) {
                // Make the cursor visible and stop blinking.
                mRenderCursorRegardlessTiming = true;
                mTextView.invalidateCursorPath();
                suspendBlink();
                mMagnifierAnimator.show(showPosInView.x, showPosInView.y);
                updateHandlesVisibility();
            } else {
                dismissMagnifier();
            }
        }

        protected final void dismissMagnifier() {
            if (mMagnifierAnimator != null) {
                mMagnifierAnimator.dismiss();
                mRenderCursorRegardlessTiming = false;
                resumeBlink();
                setVisible(true);
                final android.widget.Editor.HandleView otherHandle = getOtherSelectionHandle();
                if (otherHandle != null) {
                    otherHandle.setVisible(true);
                }
            }
        }

        @java.lang.Override
        public boolean onTouchEvent(android.view.MotionEvent ev) {
            updateFloatingToolbarVisibility(ev);
            switch (ev.getActionMasked()) {
                case android.view.MotionEvent.ACTION_DOWN :
                    {
                        startTouchUpFilter(getCurrentCursorOffset());
                        final android.widget.Editor.PositionListener positionListener = getPositionListener();
                        mLastParentX = positionListener.getPositionX();
                        mLastParentY = positionListener.getPositionY();
                        mLastParentXOnScreen = positionListener.getPositionXOnScreen();
                        mLastParentYOnScreen = positionListener.getPositionYOnScreen();
                        final float xInWindow = (ev.getRawX() - mLastParentXOnScreen) + mLastParentX;
                        final float yInWindow = (ev.getRawY() - mLastParentYOnScreen) + mLastParentY;
                        mTouchToWindowOffsetX = xInWindow - mPositionX;
                        mTouchToWindowOffsetY = yInWindow - mPositionY;
                        mIsDragging = true;
                        mPreviousLineTouched = android.widget.Editor.UNSET_LINE;
                        break;
                    }
                case android.view.MotionEvent.ACTION_MOVE :
                    {
                        final float xInWindow = (ev.getRawX() - mLastParentXOnScreen) + mLastParentX;
                        final float yInWindow = (ev.getRawY() - mLastParentYOnScreen) + mLastParentY;
                        // Vertical hysteresis: vertical down movement tends to snap to ideal offset
                        final float previousVerticalOffset = mTouchToWindowOffsetY - mLastParentY;
                        final float currentVerticalOffset = (yInWindow - mPositionY) - mLastParentY;
                        float newVerticalOffset;
                        if (previousVerticalOffset < mIdealVerticalOffset) {
                            newVerticalOffset = java.lang.Math.min(currentVerticalOffset, mIdealVerticalOffset);
                            newVerticalOffset = java.lang.Math.max(newVerticalOffset, previousVerticalOffset);
                        } else {
                            newVerticalOffset = java.lang.Math.max(currentVerticalOffset, mIdealVerticalOffset);
                            newVerticalOffset = java.lang.Math.min(newVerticalOffset, previousVerticalOffset);
                        }
                        mTouchToWindowOffsetY = newVerticalOffset + mLastParentY;
                        final float newPosX = ((xInWindow - mTouchToWindowOffsetX) + mHotspotX) + getHorizontalOffset();
                        final float newPosY = (yInWindow - mTouchToWindowOffsetY) + mTouchOffsetY;
                        updatePosition(newPosX, newPosY, ev.isFromSource(android.view.InputDevice.SOURCE_TOUCHSCREEN));
                        break;
                    }
                case android.view.MotionEvent.ACTION_UP :
                    filterOnTouchUp(ev.isFromSource(android.view.InputDevice.SOURCE_TOUCHSCREEN));
                    // Fall through.
                case android.view.MotionEvent.ACTION_CANCEL :
                    mIsDragging = false;
                    /* updateDrawableWhenDragging */
                    updateDrawable(false);
                    break;
            }
            return true;
        }

        public boolean isDragging() {
            return mIsDragging;
        }

        void onHandleMoved() {
        }

        public void onDetached() {
        }

        @java.lang.Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            setSystemGestureExclusionRects(java.util.Collections.singletonList(new android.graphics.Rect(0, 0, w, h)));
        }
    }

    private class InsertionHandleView extends android.widget.Editor.HandleView {
        private static final int DELAY_BEFORE_HANDLE_FADES_OUT = 4000;

        private static final int RECENT_CUT_COPY_DURATION = 15 * 1000;// seconds


        private float mDownPositionX;

        // Used to detect taps on the insertion handle, which will affect the insertion action mode
        private float mDownPositionY;

        private java.lang.Runnable mHider;

        public InsertionHandleView(android.graphics.drawable.Drawable drawable) {
            super(drawable, drawable, com.android.internal.R.id.insertion_handle);
        }

        @java.lang.Override
        public void show() {
            super.show();
            final long durationSinceCutOrCopy = android.os.SystemClock.uptimeMillis() - android.widget.TextView.sLastCutCopyOrTextChangedTime;
            // Cancel the single tap delayed runnable.
            if ((mInsertionActionModeRunnable != null) && (((mTapState == android.widget.Editor.TAP_STATE_DOUBLE_TAP) || (mTapState == android.widget.Editor.TAP_STATE_TRIPLE_CLICK)) || isCursorInsideEasyCorrectionSpan())) {
                mTextView.removeCallbacks(mInsertionActionModeRunnable);
            }
            // Prepare and schedule the single tap runnable to run exactly after the double tap
            // timeout has passed.
            if ((((mTapState != android.widget.Editor.TAP_STATE_DOUBLE_TAP) && (mTapState != android.widget.Editor.TAP_STATE_TRIPLE_CLICK)) && (!isCursorInsideEasyCorrectionSpan())) && (durationSinceCutOrCopy < android.widget.Editor.InsertionHandleView.RECENT_CUT_COPY_DURATION)) {
                if (mTextActionMode == null) {
                    if (mInsertionActionModeRunnable == null) {
                        mInsertionActionModeRunnable = new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                startInsertionActionMode();
                            }
                        };
                    }
                    mTextView.postDelayed(mInsertionActionModeRunnable, android.view.ViewConfiguration.getDoubleTapTimeout() + 1);
                }
            }
            hideAfterDelay();
        }

        private void hideAfterDelay() {
            if (mHider == null) {
                mHider = new java.lang.Runnable() {
                    public void run() {
                        hide();
                    }
                };
            } else {
                removeHiderCallback();
            }
            mTextView.postDelayed(mHider, android.widget.Editor.InsertionHandleView.DELAY_BEFORE_HANDLE_FADES_OUT);
        }

        private void removeHiderCallback() {
            if (mHider != null) {
                mTextView.removeCallbacks(mHider);
            }
        }

        @java.lang.Override
        protected int getHotspotX(android.graphics.drawable.Drawable drawable, boolean isRtlRun) {
            return drawable.getIntrinsicWidth() / 2;
        }

        @java.lang.Override
        protected int getHorizontalGravity(boolean isRtlRun) {
            return android.view.Gravity.CENTER_HORIZONTAL;
        }

        @java.lang.Override
        protected int getCursorOffset() {
            int offset = super.getCursorOffset();
            if (mDrawableForCursor != null) {
                mDrawableForCursor.getPadding(mTempRect);
                offset += ((mDrawableForCursor.getIntrinsicWidth() - mTempRect.left) - mTempRect.right) / 2;
            }
            return offset;
        }

        @java.lang.Override
        int getCursorHorizontalPosition(android.text.Layout layout, int offset) {
            if (mDrawableForCursor != null) {
                final float horizontal = getHorizontal(layout, offset);
                return clampHorizontalPosition(mDrawableForCursor, horizontal) + mTempRect.left;
            }
            return super.getCursorHorizontalPosition(layout, offset);
        }

        @java.lang.Override
        public boolean onTouchEvent(android.view.MotionEvent ev) {
            final boolean result = super.onTouchEvent(ev);
            switch (ev.getActionMasked()) {
                case android.view.MotionEvent.ACTION_DOWN :
                    mDownPositionX = ev.getRawX();
                    mDownPositionY = ev.getRawY();
                    updateMagnifier(ev);
                    break;
                case android.view.MotionEvent.ACTION_MOVE :
                    updateMagnifier(ev);
                    break;
                case android.view.MotionEvent.ACTION_UP :
                    if (!offsetHasBeenChanged()) {
                        final float deltaX = mDownPositionX - ev.getRawX();
                        final float deltaY = mDownPositionY - ev.getRawY();
                        final float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        final android.view.ViewConfiguration viewConfiguration = android.view.ViewConfiguration.get(mTextView.getContext());
                        final int touchSlop = viewConfiguration.getScaledTouchSlop();
                        if (distanceSquared < (touchSlop * touchSlop)) {
                            // Tapping on the handle toggles the insertion action mode.
                            if (mTextActionMode != null) {
                                stopTextActionMode();
                            } else {
                                startInsertionActionMode();
                            }
                        }
                    } else {
                        if (mTextActionMode != null) {
                            mTextActionMode.invalidateContentRect();
                        }
                    }
                    // Fall through.
                case android.view.MotionEvent.ACTION_CANCEL :
                    hideAfterDelay();
                    dismissMagnifier();
                    break;
                default :
                    break;
            }
            return result;
        }

        @java.lang.Override
        public int getCurrentCursorOffset() {
            return mTextView.getSelectionStart();
        }

        @java.lang.Override
        public void updateSelection(int offset) {
            android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), offset);
        }

        @java.lang.Override
        protected void updatePosition(float x, float y, boolean fromTouchScreen) {
            android.text.Layout layout = mTextView.getLayout();
            int offset;
            if (layout != null) {
                if (mPreviousLineTouched == android.widget.Editor.UNSET_LINE) {
                    mPreviousLineTouched = mTextView.getLineAtCoordinate(y);
                }
                int currLine = getCurrentLineAdjustedForSlop(layout, mPreviousLineTouched, y);
                offset = getOffsetAtCoordinate(layout, currLine, x);
                mPreviousLineTouched = currLine;
            } else {
                offset = -1;
            }
            positionAtCursorOffset(offset, false, fromTouchScreen);
            if (mTextActionMode != null) {
                invalidateActionMode();
            }
        }

        @java.lang.Override
        void onHandleMoved() {
            super.onHandleMoved();
            removeHiderCallback();
        }

        @java.lang.Override
        public void onDetached() {
            super.onDetached();
            removeHiderCallback();
        }

        @java.lang.Override
        @android.widget.Editor.MagnifierHandleTrigger
        protected int getMagnifierHandleTrigger() {
            return android.widget.Editor.MagnifierHandleTrigger.INSERTION;
        }
    }

    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef(prefix = { "HANDLE_TYPE_" }, value = { android.widget.Editor.HANDLE_TYPE_SELECTION_START, android.widget.Editor.HANDLE_TYPE_SELECTION_END })
    public @interface HandleType {}

    public static final int HANDLE_TYPE_SELECTION_START = 0;

    public static final int HANDLE_TYPE_SELECTION_END = 1;

    /**
     * For selection handles
     */
    @com.android.internal.annotations.VisibleForTesting
    public final class SelectionHandleView extends android.widget.Editor.HandleView {
        // Indicates the handle type, selection start (HANDLE_TYPE_SELECTION_START) or selection
        // end (HANDLE_TYPE_SELECTION_END).
        @android.widget.Editor.HandleType
        private final int mHandleType;

        // Indicates whether the cursor is making adjustments within a word.
        private boolean mInWord = false;

        // Difference between touch position and word boundary position.
        private float mTouchWordDelta;

        // X value of the previous updatePosition call.
        private float mPrevX;

        // Indicates if the handle has moved a boundary between LTR and RTL text.
        private boolean mLanguageDirectionChanged = false;

        // Distance from edge of horizontally scrolling text view
        // to use to switch to character mode.
        private final float mTextViewEdgeSlop;

        // Used to save text view location.
        private final int[] mTextViewLocation = new int[2];

        public SelectionHandleView(android.graphics.drawable.Drawable drawableLtr, android.graphics.drawable.Drawable drawableRtl, int id, @android.widget.Editor.HandleType
        int handleType) {
            super(drawableLtr, drawableRtl, id);
            mHandleType = handleType;
            android.view.ViewConfiguration viewConfiguration = android.view.ViewConfiguration.get(mTextView.getContext());
            mTextViewEdgeSlop = viewConfiguration.getScaledTouchSlop() * 4;
        }

        private boolean isStartHandle() {
            return mHandleType == android.widget.Editor.HANDLE_TYPE_SELECTION_START;
        }

        @java.lang.Override
        protected int getHotspotX(android.graphics.drawable.Drawable drawable, boolean isRtlRun) {
            if (isRtlRun == isStartHandle()) {
                return drawable.getIntrinsicWidth() / 4;
            } else {
                return (drawable.getIntrinsicWidth() * 3) / 4;
            }
        }

        @java.lang.Override
        protected int getHorizontalGravity(boolean isRtlRun) {
            return isRtlRun == isStartHandle() ? android.view.Gravity.LEFT : android.view.Gravity.RIGHT;
        }

        @java.lang.Override
        public int getCurrentCursorOffset() {
            return isStartHandle() ? mTextView.getSelectionStart() : mTextView.getSelectionEnd();
        }

        @java.lang.Override
        protected void updateSelection(int offset) {
            if (isStartHandle()) {
                android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), offset, mTextView.getSelectionEnd());
            } else {
                android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), mTextView.getSelectionStart(), offset);
            }
            /* updateDrawableWhenDragging */
            updateDrawable(false);
            if (mTextActionMode != null) {
                invalidateActionMode();
            }
        }

        @java.lang.Override
        protected void updatePosition(float x, float y, boolean fromTouchScreen) {
            final android.text.Layout layout = mTextView.getLayout();
            if (layout == null) {
                // HandleView will deal appropriately in positionAtCursorOffset when
                // layout is null.
                positionAndAdjustForCrossingHandles(mTextView.getOffsetForPosition(x, y), fromTouchScreen);
                return;
            }
            if (mPreviousLineTouched == android.widget.Editor.UNSET_LINE) {
                mPreviousLineTouched = mTextView.getLineAtCoordinate(y);
            }
            boolean positionCursor = false;
            final int anotherHandleOffset = (isStartHandle()) ? mTextView.getSelectionEnd() : mTextView.getSelectionStart();
            int currLine = getCurrentLineAdjustedForSlop(layout, mPreviousLineTouched, y);
            int initialOffset = getOffsetAtCoordinate(layout, currLine, x);
            if ((isStartHandle() && (initialOffset >= anotherHandleOffset)) || ((!isStartHandle()) && (initialOffset <= anotherHandleOffset))) {
                // Handles have crossed, bound it to the first selected line and
                // adjust by word / char as normal.
                currLine = layout.getLineForOffset(anotherHandleOffset);
                initialOffset = getOffsetAtCoordinate(layout, currLine, x);
            }
            int offset = initialOffset;
            final int wordEnd = getWordEnd(offset);
            final int wordStart = getWordStart(offset);
            if (mPrevX == android.widget.Editor.UNSET_X_VALUE) {
                mPrevX = x;
            }
            final int currentOffset = getCurrentCursorOffset();
            final boolean rtlAtCurrentOffset = isAtRtlRun(layout, currentOffset);
            final boolean atRtl = isAtRtlRun(layout, offset);
            final boolean isLvlBoundary = layout.isLevelBoundary(offset);
            // We can't determine if the user is expanding or shrinking the selection if they're
            // on a bi-di boundary, so until they've moved past the boundary we'll just place
            // the cursor at the current position.
            if ((isLvlBoundary || (rtlAtCurrentOffset && (!atRtl))) || ((!rtlAtCurrentOffset) && atRtl)) {
                // We're on a boundary or this is the first direction change -- just update
                // to the current position.
                mLanguageDirectionChanged = true;
                mTouchWordDelta = 0.0F;
                positionAndAdjustForCrossingHandles(offset, fromTouchScreen);
                return;
            } else
                if (mLanguageDirectionChanged && (!isLvlBoundary)) {
                    // We've just moved past the boundary so update the position. After this we can
                    // figure out if the user is expanding or shrinking to go by word or character.
                    positionAndAdjustForCrossingHandles(offset, fromTouchScreen);
                    mTouchWordDelta = 0.0F;
                    mLanguageDirectionChanged = false;
                    return;
                }

            boolean isExpanding;
            final float xDiff = x - mPrevX;
            if (isStartHandle()) {
                isExpanding = currLine < mPreviousLineTouched;
            } else {
                isExpanding = currLine > mPreviousLineTouched;
            }
            if (atRtl == isStartHandle()) {
                isExpanding |= xDiff > 0;
            } else {
                isExpanding |= xDiff < 0;
            }
            if (mTextView.getHorizontallyScrolling()) {
                if ((positionNearEdgeOfScrollingView(x, atRtl) && ((isStartHandle() && (mTextView.getScrollX() != 0)) || ((!isStartHandle()) && mTextView.canScrollHorizontally(atRtl ? -1 : 1)))) && ((isExpanding && ((isStartHandle() && (offset < currentOffset)) || ((!isStartHandle()) && (offset > currentOffset)))) || (!isExpanding))) {
                    // If we're expanding ensure that the offset is actually expanding compared to
                    // the current offset, if the handle snapped to the word, the finger position
                    // may be out of sync and we don't want the selection to jump back.
                    mTouchWordDelta = 0.0F;
                    final int nextOffset = (atRtl == isStartHandle()) ? layout.getOffsetToRightOf(mPreviousOffset) : layout.getOffsetToLeftOf(mPreviousOffset);
                    positionAndAdjustForCrossingHandles(nextOffset, fromTouchScreen);
                    return;
                }
            }
            if (isExpanding) {
                // User is increasing the selection.
                int wordBoundary = (isStartHandle()) ? wordStart : wordEnd;
                final boolean snapToWord = ((!mInWord) || (isStartHandle() ? currLine < mPrevLine : currLine > mPrevLine)) && (atRtl == isAtRtlRun(layout, wordBoundary));
                if (snapToWord) {
                    // Sometimes words can be broken across lines (Chinese, hyphenation).
                    // We still snap to the word boundary but we only use the letters on the
                    // current line to determine if the user is far enough into the word to snap.
                    if (layout.getLineForOffset(wordBoundary) != currLine) {
                        wordBoundary = (isStartHandle()) ? layout.getLineStart(currLine) : layout.getLineEnd(currLine);
                    }
                    final int offsetThresholdToSnap = (isStartHandle()) ? wordEnd - ((wordEnd - wordBoundary) / 2) : wordStart + ((wordBoundary - wordStart) / 2);
                    if (isStartHandle() && ((offset <= offsetThresholdToSnap) || (currLine < mPrevLine))) {
                        // User is far enough into the word or on a different line so we expand by
                        // word.
                        offset = wordStart;
                    } else
                        if ((!isStartHandle()) && ((offset >= offsetThresholdToSnap) || (currLine > mPrevLine))) {
                            // User is far enough into the word or on a different line so we expand by
                            // word.
                            offset = wordEnd;
                        } else {
                            offset = mPreviousOffset;
                        }

                }
                if ((isStartHandle() && (offset < initialOffset)) || ((!isStartHandle()) && (offset > initialOffset))) {
                    final float adjustedX = getHorizontal(layout, offset);
                    mTouchWordDelta = mTextView.convertToLocalHorizontalCoordinate(x) - adjustedX;
                } else {
                    mTouchWordDelta = 0.0F;
                }
                positionCursor = true;
            } else {
                final int adjustedOffset = getOffsetAtCoordinate(layout, currLine, x - mTouchWordDelta);
                final boolean shrinking = (isStartHandle()) ? (adjustedOffset > mPreviousOffset) || (currLine > mPrevLine) : (adjustedOffset < mPreviousOffset) || (currLine < mPrevLine);
                if (shrinking) {
                    // User is shrinking the selection.
                    if (currLine != mPrevLine) {
                        // We're on a different line, so we'll snap to word boundaries.
                        offset = (isStartHandle()) ? wordStart : wordEnd;
                        if ((isStartHandle() && (offset < initialOffset)) || ((!isStartHandle()) && (offset > initialOffset))) {
                            final float adjustedX = getHorizontal(layout, offset);
                            mTouchWordDelta = mTextView.convertToLocalHorizontalCoordinate(x) - adjustedX;
                        } else {
                            mTouchWordDelta = 0.0F;
                        }
                    } else {
                        offset = adjustedOffset;
                    }
                    positionCursor = true;
                } else
                    if ((isStartHandle() && (adjustedOffset < mPreviousOffset)) || ((!isStartHandle()) && (adjustedOffset > mPreviousOffset))) {
                        // Handle has jumped to the word boundary, and the user is moving
                        // their finger towards the handle, the delta should be updated.
                        mTouchWordDelta = mTextView.convertToLocalHorizontalCoordinate(x) - getHorizontal(layout, mPreviousOffset);
                    }

            }
            if (positionCursor) {
                mPreviousLineTouched = currLine;
                positionAndAdjustForCrossingHandles(offset, fromTouchScreen);
            }
            mPrevX = x;
        }

        @java.lang.Override
        protected void positionAtCursorOffset(int offset, boolean forceUpdatePosition, boolean fromTouchScreen) {
            super.positionAtCursorOffset(offset, forceUpdatePosition, fromTouchScreen);
            mInWord = (offset != (-1)) && (!getWordIteratorWithText().isBoundary(offset));
        }

        @java.lang.Override
        public boolean onTouchEvent(android.view.MotionEvent event) {
            boolean superResult = super.onTouchEvent(event);
            switch (event.getActionMasked()) {
                case android.view.MotionEvent.ACTION_DOWN :
                    // Reset the touch word offset and x value when the user
                    // re-engages the handle.
                    mTouchWordDelta = 0.0F;
                    mPrevX = android.widget.Editor.UNSET_X_VALUE;
                    updateMagnifier(event);
                    break;
                case android.view.MotionEvent.ACTION_MOVE :
                    updateMagnifier(event);
                    break;
                case android.view.MotionEvent.ACTION_UP :
                case android.view.MotionEvent.ACTION_CANCEL :
                    dismissMagnifier();
                    break;
            }
            return superResult;
        }

        private void positionAndAdjustForCrossingHandles(int offset, boolean fromTouchScreen) {
            final int anotherHandleOffset = (isStartHandle()) ? mTextView.getSelectionEnd() : mTextView.getSelectionStart();
            if ((isStartHandle() && (offset >= anotherHandleOffset)) || ((!isStartHandle()) && (offset <= anotherHandleOffset))) {
                mTouchWordDelta = 0.0F;
                final android.text.Layout layout = mTextView.getLayout();
                if ((layout != null) && (offset != anotherHandleOffset)) {
                    final float horiz = getHorizontal(layout, offset);
                    final float anotherHandleHoriz = getHorizontal(layout, anotherHandleOffset, !isStartHandle());
                    final float currentHoriz = getHorizontal(layout, mPreviousOffset);
                    if (((currentHoriz < anotherHandleHoriz) && (horiz < anotherHandleHoriz)) || ((currentHoriz > anotherHandleHoriz) && (horiz > anotherHandleHoriz))) {
                        // This handle passes another one as it crossed a direction boundary.
                        // Don't minimize the selection, but keep the handle at the run boundary.
                        final int currentOffset = getCurrentCursorOffset();
                        final int offsetToGetRunRange = (isStartHandle()) ? currentOffset : java.lang.Math.max(currentOffset - 1, 0);
                        final long range = layout.getRunRange(offsetToGetRunRange);
                        if (isStartHandle()) {
                            offset = android.text.TextUtils.unpackRangeStartFromLong(range);
                        } else {
                            offset = android.text.TextUtils.unpackRangeEndFromLong(range);
                        }
                        positionAtCursorOffset(offset, false, fromTouchScreen);
                        return;
                    }
                }
                // Handles can not cross and selection is at least one character.
                offset = getNextCursorOffset(anotherHandleOffset, !isStartHandle());
            }
            positionAtCursorOffset(offset, false, fromTouchScreen);
        }

        private boolean positionNearEdgeOfScrollingView(float x, boolean atRtl) {
            mTextView.getLocationOnScreen(mTextViewLocation);
            boolean nearEdge;
            if (atRtl == isStartHandle()) {
                int rightEdge = (mTextViewLocation[0] + mTextView.getWidth()) - mTextView.getPaddingRight();
                nearEdge = x > (rightEdge - mTextViewEdgeSlop);
            } else {
                int leftEdge = mTextViewLocation[0] + mTextView.getPaddingLeft();
                nearEdge = x < (leftEdge + mTextViewEdgeSlop);
            }
            return nearEdge;
        }

        @java.lang.Override
        protected boolean isAtRtlRun(@android.annotation.NonNull
        android.text.Layout layout, int offset) {
            final int offsetToCheck = (isStartHandle()) ? offset : java.lang.Math.max(offset - 1, 0);
            return layout.isRtlCharAt(offsetToCheck);
        }

        @java.lang.Override
        public float getHorizontal(@android.annotation.NonNull
        android.text.Layout layout, int offset) {
            return getHorizontal(layout, offset, isStartHandle());
        }

        private float getHorizontal(@android.annotation.NonNull
        android.text.Layout layout, int offset, boolean startHandle) {
            final int line = layout.getLineForOffset(offset);
            final int offsetToCheck = (startHandle) ? offset : java.lang.Math.max(offset - 1, 0);
            final boolean isRtlChar = layout.isRtlCharAt(offsetToCheck);
            final boolean isRtlParagraph = layout.getParagraphDirection(line) == (-1);
            return isRtlChar == isRtlParagraph ? layout.getPrimaryHorizontal(offset) : layout.getSecondaryHorizontal(offset);
        }

        @java.lang.Override
        protected int getOffsetAtCoordinate(@android.annotation.NonNull
        android.text.Layout layout, int line, float x) {
            final float localX = mTextView.convertToLocalHorizontalCoordinate(x);
            final int primaryOffset = layout.getOffsetForHorizontal(line, localX, true);
            if (!layout.isLevelBoundary(primaryOffset)) {
                return primaryOffset;
            }
            final int secondaryOffset = layout.getOffsetForHorizontal(line, localX, false);
            final int currentOffset = getCurrentCursorOffset();
            final int primaryDiff = java.lang.Math.abs(primaryOffset - currentOffset);
            final int secondaryDiff = java.lang.Math.abs(secondaryOffset - currentOffset);
            if (primaryDiff < secondaryDiff) {
                return primaryOffset;
            } else
                if (primaryDiff > secondaryDiff) {
                    return secondaryOffset;
                } else {
                    final int offsetToCheck = (isStartHandle()) ? currentOffset : java.lang.Math.max(currentOffset - 1, 0);
                    final boolean isRtlChar = layout.isRtlCharAt(offsetToCheck);
                    final boolean isRtlParagraph = layout.getParagraphDirection(line) == (-1);
                    return isRtlChar == isRtlParagraph ? primaryOffset : secondaryOffset;
                }

        }

        @android.widget.Editor.MagnifierHandleTrigger
        protected int getMagnifierHandleTrigger() {
            return isStartHandle() ? android.widget.Editor.MagnifierHandleTrigger.SELECTION_START : android.widget.Editor.MagnifierHandleTrigger.SELECTION_END;
        }
    }

    private int getCurrentLineAdjustedForSlop(android.text.Layout layout, int prevLine, float y) {
        final int trueLine = mTextView.getLineAtCoordinate(y);
        if ((((layout == null) || (prevLine > layout.getLineCount())) || (layout.getLineCount() <= 0)) || (prevLine < 0)) {
            // Invalid parameters, just return whatever line is at y.
            return trueLine;
        }
        if (java.lang.Math.abs(trueLine - prevLine) >= 2) {
            // Only stick to lines if we're within a line of the previous selection.
            return trueLine;
        }
        final float verticalOffset = mTextView.viewportToContentVerticalOffset();
        final int lineCount = layout.getLineCount();
        final float slop = mTextView.getLineHeight() * android.widget.Editor.LINE_SLOP_MULTIPLIER_FOR_HANDLEVIEWS;
        final float firstLineTop = layout.getLineTop(0) + verticalOffset;
        final float prevLineTop = layout.getLineTop(prevLine) + verticalOffset;
        final float yTopBound = java.lang.Math.max(prevLineTop - slop, firstLineTop + slop);
        final float lastLineBottom = layout.getLineBottom(lineCount - 1) + verticalOffset;
        final float prevLineBottom = layout.getLineBottom(prevLine) + verticalOffset;
        final float yBottomBound = java.lang.Math.min(prevLineBottom + slop, lastLineBottom - slop);
        // Determine if we've moved lines based on y position and previous line.
        int currLine;
        if (y <= yTopBound) {
            currLine = java.lang.Math.max(prevLine - 1, 0);
        } else
            if (y >= yBottomBound) {
                currLine = java.lang.Math.min(prevLine + 1, lineCount - 1);
            } else {
                currLine = prevLine;
            }

        return currLine;
    }

    /**
     * A CursorController instance can be used to control a cursor in the text.
     */
    private interface CursorController extends android.view.ViewTreeObserver.OnTouchModeChangeListener {
        /**
         * Makes the cursor controller visible on screen.
         * See also {@link #hide()}.
         */
        public void show();

        /**
         * Hide the cursor controller from screen.
         * See also {@link #show()}.
         */
        public void hide();

        /**
         * Called when the view is detached from window. Perform house keeping task, such as
         * stopping Runnable thread that would otherwise keep a reference on the context, thus
         * preventing the activity from being recycled.
         */
        public void onDetached();

        public boolean isCursorBeingModified();

        public boolean isActive();
    }

    void loadCursorDrawable() {
        if (mDrawableForCursor == null) {
            mDrawableForCursor = mTextView.getTextCursorDrawable();
        }
    }

    private class InsertionPointCursorController implements android.widget.Editor.CursorController {
        private android.widget.Editor.InsertionHandleView mHandle;

        public void show() {
            getHandle().show();
            if (mSelectionModifierCursorController != null) {
                mSelectionModifierCursorController.hide();
            }
        }

        public void hide() {
            if (mHandle != null) {
                mHandle.hide();
            }
        }

        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        private android.widget.Editor.InsertionHandleView getHandle() {
            if (mHandle == null) {
                /* overwrite */
                loadHandleDrawables(false);
                mHandle = new android.widget.Editor.InsertionHandleView(mSelectHandleCenter);
            }
            return mHandle;
        }

        private void reloadHandleDrawable() {
            if (mHandle == null) {
                // No need to reload, the potentially new drawable will
                // be used when the handle is created.
                return;
            }
            mHandle.setDrawables(mSelectHandleCenter, mSelectHandleCenter);
        }

        @java.lang.Override
        public void onDetached() {
            final android.view.ViewTreeObserver observer = mTextView.getViewTreeObserver();
            observer.removeOnTouchModeChangeListener(this);
            if (mHandle != null)
                mHandle.onDetached();

        }

        @java.lang.Override
        public boolean isCursorBeingModified() {
            return (mHandle != null) && mHandle.isDragging();
        }

        @java.lang.Override
        public boolean isActive() {
            return (mHandle != null) && mHandle.isShowing();
        }

        public void invalidateHandle() {
            if (mHandle != null) {
                mHandle.invalidate();
            }
        }
    }

    class SelectionModifierCursorController implements android.widget.Editor.CursorController {
        // The cursor controller handles, lazily created when shown.
        private android.widget.Editor.SelectionHandleView mStartHandle;

        private android.widget.Editor.SelectionHandleView mEndHandle;

        private int mMinTouchOffset;

        // The offsets of that last touch down event. Remembered to start selection there.
        private int mMaxTouchOffset;

        private float mDownPositionX;

        private float mDownPositionY;

        private boolean mGestureStayedInTapRegion;

        // Where the user first starts the drag motion.
        private int mStartOffset = -1;

        private boolean mHaventMovedEnoughToStartDrag;

        // The line that a selection happened most recently with the drag accelerator.
        private int mLineSelectionIsOn = -1;

        // Whether the drag accelerator has selected past the initial line.
        private boolean mSwitchedLines = false;

        // Indicates the drag accelerator mode that the user is currently using.
        private int mDragAcceleratorMode = android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_INACTIVE;

        // Drag accelerator is inactive.
        private static final int DRAG_ACCELERATOR_MODE_INACTIVE = 0;

        // Character based selection by dragging. Only for mouse.
        private static final int DRAG_ACCELERATOR_MODE_CHARACTER = 1;

        // Word based selection by dragging. Enabled after long pressing or double tapping.
        private static final int DRAG_ACCELERATOR_MODE_WORD = 2;

        // Paragraph based selection by dragging. Enabled after mouse triple click.
        private static final int DRAG_ACCELERATOR_MODE_PARAGRAPH = 3;

        SelectionModifierCursorController() {
            resetTouchOffsets();
        }

        public void show() {
            if (mTextView.isInBatchEditMode()) {
                return;
            }
            /* overwrite */
            loadHandleDrawables(false);
            initHandles();
        }

        private void initHandles() {
            // Lazy object creation has to be done before updatePosition() is called.
            if (mStartHandle == null) {
                mStartHandle = new android.widget.Editor.SelectionHandleView(mSelectHandleLeft, mSelectHandleRight, com.android.internal.R.id.selection_start_handle, android.widget.Editor.HANDLE_TYPE_SELECTION_START);
            }
            if (mEndHandle == null) {
                mEndHandle = new android.widget.Editor.SelectionHandleView(mSelectHandleRight, mSelectHandleLeft, com.android.internal.R.id.selection_end_handle, android.widget.Editor.HANDLE_TYPE_SELECTION_END);
            }
            mStartHandle.show();
            mEndHandle.show();
            hideInsertionPointCursorController();
        }

        private void reloadHandleDrawables() {
            if (mStartHandle == null) {
                // No need to reload, the potentially new drawables will
                // be used when the handles are created.
                return;
            }
            mStartHandle.setDrawables(mSelectHandleLeft, mSelectHandleRight);
            mEndHandle.setDrawables(mSelectHandleRight, mSelectHandleLeft);
        }

        public void hide() {
            if (mStartHandle != null)
                mStartHandle.hide();

            if (mEndHandle != null)
                mEndHandle.hide();

        }

        public void enterDrag(int dragAcceleratorMode) {
            // Just need to init the handles / hide insertion cursor.
            show();
            mDragAcceleratorMode = dragAcceleratorMode;
            // Start location of selection.
            mStartOffset = mTextView.getOffsetForPosition(mLastDownPositionX, mLastDownPositionY);
            mLineSelectionIsOn = mTextView.getLineAtCoordinate(mLastDownPositionY);
            // Don't show the handles until user has lifted finger.
            hide();
            // This stops scrolling parents from intercepting the touch event, allowing
            // the user to continue dragging across the screen to select text; TextView will
            // scroll as necessary.
            mTextView.getParent().requestDisallowInterceptTouchEvent(true);
            mTextView.cancelLongPress();
        }

        public void onTouchEvent(android.view.MotionEvent event) {
            // This is done even when the View does not have focus, so that long presses can start
            // selection and tap can move cursor from this tap position.
            final float eventX = event.getX();
            final float eventY = event.getY();
            final boolean isMouse = event.isFromSource(android.view.InputDevice.SOURCE_MOUSE);
            switch (event.getActionMasked()) {
                case android.view.MotionEvent.ACTION_DOWN :
                    if (extractedTextModeWillBeStarted()) {
                        // Prevent duplicating the selection handles until the mode starts.
                        hide();
                    } else {
                        // Remember finger down position, to be able to start selection from there.
                        mMinTouchOffset = mMaxTouchOffset = mTextView.getOffsetForPosition(eventX, eventY);
                        // Double tap detection
                        if (mGestureStayedInTapRegion) {
                            if ((mTapState == android.widget.Editor.TAP_STATE_DOUBLE_TAP) || (mTapState == android.widget.Editor.TAP_STATE_TRIPLE_CLICK)) {
                                final float deltaX = eventX - mDownPositionX;
                                final float deltaY = eventY - mDownPositionY;
                                final float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                                android.view.ViewConfiguration viewConfiguration = android.view.ViewConfiguration.get(mTextView.getContext());
                                int doubleTapSlop = viewConfiguration.getScaledDoubleTapSlop();
                                boolean stayedInArea = distanceSquared < (doubleTapSlop * doubleTapSlop);
                                if (stayedInArea && (isMouse || isPositionOnText(eventX, eventY))) {
                                    if (mTapState == android.widget.Editor.TAP_STATE_DOUBLE_TAP) {
                                        selectCurrentWordAndStartDrag();
                                    } else
                                        if (mTapState == android.widget.Editor.TAP_STATE_TRIPLE_CLICK) {
                                            selectCurrentParagraphAndStartDrag();
                                        }

                                    mDiscardNextActionUp = true;
                                }
                            }
                        }
                        mDownPositionX = eventX;
                        mDownPositionY = eventY;
                        mGestureStayedInTapRegion = true;
                        mHaventMovedEnoughToStartDrag = true;
                    }
                    break;
                case android.view.MotionEvent.ACTION_POINTER_DOWN :
                case android.view.MotionEvent.ACTION_POINTER_UP :
                    // Handle multi-point gestures. Keep min and max offset positions.
                    // Only activated for devices that correctly handle multi-touch.
                    if (mTextView.getContext().getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH_DISTINCT)) {
                        updateMinAndMaxOffsets(event);
                    }
                    break;
                case android.view.MotionEvent.ACTION_MOVE :
                    final android.view.ViewConfiguration viewConfig = android.view.ViewConfiguration.get(mTextView.getContext());
                    final int touchSlop = viewConfig.getScaledTouchSlop();
                    if (mGestureStayedInTapRegion || mHaventMovedEnoughToStartDrag) {
                        final float deltaX = eventX - mDownPositionX;
                        final float deltaY = eventY - mDownPositionY;
                        final float distanceSquared = (deltaX * deltaX) + (deltaY * deltaY);
                        if (mGestureStayedInTapRegion) {
                            int doubleTapTouchSlop = viewConfig.getScaledDoubleTapTouchSlop();
                            mGestureStayedInTapRegion = distanceSquared <= (doubleTapTouchSlop * doubleTapTouchSlop);
                        }
                        if (mHaventMovedEnoughToStartDrag) {
                            // We don't start dragging until the user has moved enough.
                            mHaventMovedEnoughToStartDrag = distanceSquared <= (touchSlop * touchSlop);
                        }
                    }
                    if (isMouse && (!isDragAcceleratorActive())) {
                        final int offset = mTextView.getOffsetForPosition(eventX, eventY);
                        if (((mTextView.hasSelection() && ((!mHaventMovedEnoughToStartDrag) || (mStartOffset != offset))) && (offset >= mTextView.getSelectionStart())) && (offset <= mTextView.getSelectionEnd())) {
                            startDragAndDrop();
                            break;
                        }
                        if (mStartOffset != offset) {
                            // Start character based drag accelerator.
                            stopTextActionMode();
                            enterDrag(android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_CHARACTER);
                            mDiscardNextActionUp = true;
                            mHaventMovedEnoughToStartDrag = false;
                        }
                    }
                    if ((mStartHandle != null) && mStartHandle.isShowing()) {
                        // Don't do the drag if the handles are showing already.
                        break;
                    }
                    updateSelection(event);
                    break;
                case android.view.MotionEvent.ACTION_UP :
                    if (!isDragAcceleratorActive()) {
                        break;
                    }
                    updateSelection(event);
                    // No longer dragging to select text, let the parent intercept events.
                    mTextView.getParent().requestDisallowInterceptTouchEvent(false);
                    // No longer the first dragging motion, reset.
                    resetDragAcceleratorState();
                    if (mTextView.hasSelection()) {
                        // Drag selection should not be adjusted by the text classifier.
                        startSelectionActionModeAsync(mHaventMovedEnoughToStartDrag);
                    }
                    break;
            }
        }

        private void updateSelection(android.view.MotionEvent event) {
            if (mTextView.getLayout() != null) {
                switch (mDragAcceleratorMode) {
                    case android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_CHARACTER :
                        updateCharacterBasedSelection(event);
                        break;
                    case android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_WORD :
                        updateWordBasedSelection(event);
                        break;
                    case android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_PARAGRAPH :
                        updateParagraphBasedSelection(event);
                        break;
                }
            }
        }

        /**
         * If the TextView allows text selection, selects the current paragraph and starts a drag.
         *
         * @return true if the drag was started.
         */
        private boolean selectCurrentParagraphAndStartDrag() {
            if (mInsertionActionModeRunnable != null) {
                mTextView.removeCallbacks(mInsertionActionModeRunnable);
            }
            stopTextActionMode();
            if (!selectCurrentParagraph()) {
                return false;
            }
            enterDrag(android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_PARAGRAPH);
            return true;
        }

        private void updateCharacterBasedSelection(android.view.MotionEvent event) {
            final int offset = mTextView.getOffsetForPosition(event.getX(), event.getY());
            updateSelectionInternal(mStartOffset, offset, event.isFromSource(android.view.InputDevice.SOURCE_TOUCHSCREEN));
        }

        private void updateWordBasedSelection(android.view.MotionEvent event) {
            if (mHaventMovedEnoughToStartDrag) {
                return;
            }
            final boolean isMouse = event.isFromSource(android.view.InputDevice.SOURCE_MOUSE);
            final android.view.ViewConfiguration viewConfig = android.view.ViewConfiguration.get(mTextView.getContext());
            final float eventX = event.getX();
            final float eventY = event.getY();
            final int currLine;
            if (isMouse) {
                // No need to offset the y coordinate for mouse input.
                currLine = mTextView.getLineAtCoordinate(eventY);
            } else {
                float y = eventY;
                if (mSwitchedLines) {
                    // Offset the finger by the same vertical offset as the handles.
                    // This improves visibility of the content being selected by
                    // shifting the finger below the content, this is applied once
                    // the user has switched lines.
                    final int touchSlop = viewConfig.getScaledTouchSlop();
                    final float fingerOffset = (mStartHandle != null) ? mStartHandle.getIdealVerticalOffset() : touchSlop;
                    y = eventY - fingerOffset;
                }
                currLine = getCurrentLineAdjustedForSlop(mTextView.getLayout(), mLineSelectionIsOn, y);
                if ((!mSwitchedLines) && (currLine != mLineSelectionIsOn)) {
                    // Break early here, we want to offset the finger position from
                    // the selection highlight, once the user moved their finger
                    // to a different line we should apply the offset and *not* switch
                    // lines until recomputing the position with the finger offset.
                    mSwitchedLines = true;
                    return;
                }
            }
            int startOffset;
            int offset = mTextView.getOffsetAtCoordinate(currLine, eventX);
            // Snap to word boundaries.
            if (mStartOffset < offset) {
                // Expanding with end handle.
                offset = getWordEnd(offset);
                startOffset = getWordStart(mStartOffset);
            } else {
                // Expanding with start handle.
                offset = getWordStart(offset);
                startOffset = getWordEnd(mStartOffset);
                if (startOffset == offset) {
                    offset = getNextCursorOffset(offset, false);
                }
            }
            mLineSelectionIsOn = currLine;
            updateSelectionInternal(startOffset, offset, event.isFromSource(android.view.InputDevice.SOURCE_TOUCHSCREEN));
        }

        private void updateParagraphBasedSelection(android.view.MotionEvent event) {
            final int offset = mTextView.getOffsetForPosition(event.getX(), event.getY());
            final int start = java.lang.Math.min(offset, mStartOffset);
            final int end = java.lang.Math.max(offset, mStartOffset);
            final long paragraphsRange = getParagraphsRange(start, end);
            final int selectionStart = android.text.TextUtils.unpackRangeStartFromLong(paragraphsRange);
            final int selectionEnd = android.text.TextUtils.unpackRangeEndFromLong(paragraphsRange);
            updateSelectionInternal(selectionStart, selectionEnd, event.isFromSource(android.view.InputDevice.SOURCE_TOUCHSCREEN));
        }

        private void updateSelectionInternal(int selectionStart, int selectionEnd, boolean fromTouchScreen) {
            final boolean performHapticFeedback = (fromTouchScreen && mHapticTextHandleEnabled) && ((mTextView.getSelectionStart() != selectionStart) || (mTextView.getSelectionEnd() != selectionEnd));
            android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), selectionStart, selectionEnd);
            if (performHapticFeedback) {
                mTextView.performHapticFeedback(android.view.HapticFeedbackConstants.TEXT_HANDLE_MOVE);
            }
        }

        /**
         *
         *
         * @param event
         * 		
         */
        private void updateMinAndMaxOffsets(android.view.MotionEvent event) {
            int pointerCount = event.getPointerCount();
            for (int index = 0; index < pointerCount; index++) {
                int offset = mTextView.getOffsetForPosition(event.getX(index), event.getY(index));
                if (offset < mMinTouchOffset)
                    mMinTouchOffset = offset;

                if (offset > mMaxTouchOffset)
                    mMaxTouchOffset = offset;

            }
        }

        public int getMinTouchOffset() {
            return mMinTouchOffset;
        }

        public int getMaxTouchOffset() {
            return mMaxTouchOffset;
        }

        public void resetTouchOffsets() {
            mMinTouchOffset = mMaxTouchOffset = -1;
            resetDragAcceleratorState();
        }

        private void resetDragAcceleratorState() {
            mStartOffset = -1;
            mDragAcceleratorMode = android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_INACTIVE;
            mSwitchedLines = false;
            final int selectionStart = mTextView.getSelectionStart();
            final int selectionEnd = mTextView.getSelectionEnd();
            if ((selectionStart < 0) || (selectionEnd < 0)) {
                android.text.Selection.removeSelection(((android.text.Spannable) (mTextView.getText())));
            } else
                if (selectionStart > selectionEnd) {
                    android.text.Selection.setSelection(((android.text.Spannable) (mTextView.getText())), selectionEnd, selectionStart);
                }

        }

        /**
         *
         *
         * @return true iff this controller is currently used to move the selection start.
         */
        public boolean isSelectionStartDragged() {
            return (mStartHandle != null) && mStartHandle.isDragging();
        }

        @java.lang.Override
        public boolean isCursorBeingModified() {
            return (isDragAcceleratorActive() || isSelectionStartDragged()) || ((mEndHandle != null) && mEndHandle.isDragging());
        }

        /**
         *
         *
         * @return true if the user is selecting text using the drag accelerator.
         */
        public boolean isDragAcceleratorActive() {
            return mDragAcceleratorMode != android.widget.Editor.SelectionModifierCursorController.DRAG_ACCELERATOR_MODE_INACTIVE;
        }

        public void onTouchModeChanged(boolean isInTouchMode) {
            if (!isInTouchMode) {
                hide();
            }
        }

        @java.lang.Override
        public void onDetached() {
            final android.view.ViewTreeObserver observer = mTextView.getViewTreeObserver();
            observer.removeOnTouchModeChangeListener(this);
            if (mStartHandle != null)
                mStartHandle.onDetached();

            if (mEndHandle != null)
                mEndHandle.onDetached();

        }

        @java.lang.Override
        public boolean isActive() {
            return (mStartHandle != null) && mStartHandle.isShowing();
        }

        public void invalidateHandles() {
            if (mStartHandle != null) {
                mStartHandle.invalidate();
            }
            if (mEndHandle != null) {
                mEndHandle.invalidate();
            }
        }
    }

    /**
     * Loads the insertion and selection handle Drawables from TextView. If the handle
     * drawables are already loaded, do not overwrite them unless the method parameter
     * is set to true. This logic is required to avoid overwriting Drawables assigned
     * to mSelectHandle[Center/Left/Right] by developers using reflection, unless they
     * explicitly call the setters in TextView.
     *
     * @param overwrite
     * 		whether to overwrite already existing nonnull Drawables
     */
    void loadHandleDrawables(final boolean overwrite) {
        if ((mSelectHandleCenter == null) || overwrite) {
            mSelectHandleCenter = mTextView.getTextSelectHandle();
            if (hasInsertionController()) {
                getInsertionController().reloadHandleDrawable();
            }
        }
        if (((mSelectHandleLeft == null) || (mSelectHandleRight == null)) || overwrite) {
            mSelectHandleLeft = mTextView.getTextSelectHandleLeft();
            mSelectHandleRight = mTextView.getTextSelectHandleRight();
            if (hasSelectionController()) {
                getSelectionController().reloadHandleDrawables();
            }
        }
    }

    private class CorrectionHighlighter {
        private final android.graphics.Path mPath = new android.graphics.Path();

        private final android.graphics.Paint mPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);

        private int mStart;

        private int mEnd;

        private long mFadingStartTime;

        private android.graphics.RectF mTempRectF;

        private static final int FADE_OUT_DURATION = 400;

        public CorrectionHighlighter() {
            mPaint.setCompatibilityScaling(mTextView.getResources().getCompatibilityInfo().applicationScale);
            mPaint.setStyle(android.graphics.Paint.Style.FILL);
        }

        public void highlight(android.view.inputmethod.CorrectionInfo info) {
            mStart = info.getOffset();
            mEnd = mStart + info.getNewText().length();
            mFadingStartTime = android.os.SystemClock.uptimeMillis();
            if ((mStart < 0) || (mEnd < 0)) {
                stopAnimation();
            }
        }

        public void draw(android.graphics.Canvas canvas, int cursorOffsetVertical) {
            if (updatePath() && updatePaint()) {
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0, cursorOffsetVertical);
                }
                canvas.drawPath(mPath, mPaint);
                if (cursorOffsetVertical != 0) {
                    canvas.translate(0, -cursorOffsetVertical);
                }
                invalidate(true);// TODO invalidate cursor region only

            } else {
                stopAnimation();
                invalidate(false);// TODO invalidate cursor region only

            }
        }

        private boolean updatePaint() {
            final long duration = android.os.SystemClock.uptimeMillis() - mFadingStartTime;
            if (duration > android.widget.Editor.CorrectionHighlighter.FADE_OUT_DURATION)
                return false;

            final float coef = 1.0F - (((float) (duration)) / android.widget.Editor.CorrectionHighlighter.FADE_OUT_DURATION);
            final int highlightColorAlpha = android.graphics.Color.alpha(mTextView.mHighlightColor);
            final int color = (mTextView.mHighlightColor & 0xffffff) + (((int) (highlightColorAlpha * coef)) << 24);
            mPaint.setColor(color);
            return true;
        }

        private boolean updatePath() {
            final android.text.Layout layout = mTextView.getLayout();
            if (layout == null)
                return false;

            // Update in case text is edited while the animation is run
            final int length = mTextView.getText().length();
            int start = java.lang.Math.min(length, mStart);
            int end = java.lang.Math.min(length, mEnd);
            mPath.reset();
            layout.getSelectionPath(start, end, mPath);
            return true;
        }

        private void invalidate(boolean delayed) {
            if (mTextView.getLayout() == null)
                return;

            if (mTempRectF == null)
                mTempRectF = new android.graphics.RectF();

            mPath.computeBounds(mTempRectF, false);
            int left = mTextView.getCompoundPaddingLeft();
            int top = mTextView.getExtendedPaddingTop() + mTextView.getVerticalOffset(true);
            if (delayed) {
                mTextView.postInvalidateOnAnimation(left + ((int) (mTempRectF.left)), top + ((int) (mTempRectF.top)), left + ((int) (mTempRectF.right)), top + ((int) (mTempRectF.bottom)));
            } else {
                mTextView.postInvalidate(((int) (mTempRectF.left)), ((int) (mTempRectF.top)), ((int) (mTempRectF.right)), ((int) (mTempRectF.bottom)));
            }
        }

        private void stopAnimation() {
            android.widget.Editor.this.mCorrectionHighlighter = null;
        }
    }

    private static class ErrorPopup extends android.widget.PopupWindow {
        private boolean mAbove = false;

        private final android.widget.TextView mView;

        private int mPopupInlineErrorBackgroundId = 0;

        private int mPopupInlineErrorAboveBackgroundId = 0;

        ErrorPopup(android.widget.TextView v, int width, int height) {
            super(v, width, height);
            mView = v;
            // Make sure the TextView has a background set as it will be used the first time it is
            // shown and positioned. Initialized with below background, which should have
            // dimensions identical to the above version for this to work (and is more likely).
            mPopupInlineErrorBackgroundId = getResourceId(mPopupInlineErrorBackgroundId, com.android.internal.R.styleable.Theme_errorMessageBackground);
            mView.setBackgroundResource(mPopupInlineErrorBackgroundId);
        }

        void fixDirection(boolean above) {
            mAbove = above;
            if (above) {
                mPopupInlineErrorAboveBackgroundId = getResourceId(mPopupInlineErrorAboveBackgroundId, com.android.internal.R.styleable.Theme_errorMessageAboveBackground);
            } else {
                mPopupInlineErrorBackgroundId = getResourceId(mPopupInlineErrorBackgroundId, com.android.internal.R.styleable.Theme_errorMessageBackground);
            }
            mView.setBackgroundResource(above ? mPopupInlineErrorAboveBackgroundId : mPopupInlineErrorBackgroundId);
        }

        private int getResourceId(int currentId, int index) {
            if (currentId == 0) {
                android.content.res.TypedArray styledAttributes = mView.getContext().obtainStyledAttributes(R.styleable.Theme);
                currentId = styledAttributes.getResourceId(index, 0);
                styledAttributes.recycle();
            }
            return currentId;
        }

        @java.lang.Override
        public void update(int x, int y, int w, int h, boolean force) {
            super.update(x, y, w, h, force);
            boolean above = isAboveAnchor();
            if (above != mAbove) {
                fixDirection(above);
            }
        }
    }

    static class InputContentType {
        int imeOptions = android.view.inputmethod.EditorInfo.IME_NULL;

        @android.annotation.UnsupportedAppUsage
        java.lang.String privateImeOptions;

        java.lang.CharSequence imeActionLabel;

        int imeActionId;

        android.os.Bundle extras;

        android.widget.TextView.OnEditorActionListener onEditorActionListener;

        boolean enterDown;

        android.os.LocaleList imeHintLocales;
    }

    static class InputMethodState {
        android.view.inputmethod.ExtractedTextRequest mExtractedTextRequest;

        final android.view.inputmethod.ExtractedText mExtractedText = new android.view.inputmethod.ExtractedText();

        int mBatchEditNesting;

        boolean mCursorChanged;

        boolean mSelectionModeChanged;

        boolean mContentChanged;

        int mChangedStart;

        int mChangedEnd;

        int mChangedDelta;
    }

    /**
     *
     *
     * @return True iff (start, end) is a valid range within the text.
     */
    private static boolean isValidRange(java.lang.CharSequence text, int start, int end) {
        return ((0 <= start) && (start <= end)) && (end <= text.length());
    }

    /**
     * An InputFilter that monitors text input to maintain undo history. It does not modify the
     * text being typed (and hence always returns null from the filter() method).
     *
     * TODO: Make this span aware.
     */
    public static class UndoInputFilter implements android.text.InputFilter {
        private final android.widget.Editor mEditor;

        // Whether the current filter pass is directly caused by an end-user text edit.
        private boolean mIsUserEdit;

        // Whether the text field is handling an IME composition. Must be parceled in case the user
        // rotates the screen during composition.
        private boolean mHasComposition;

        // Whether the user is expanding or shortening the text
        private boolean mExpanding;

        // Whether the previous edit operation was in the current batch edit.
        private boolean mPreviousOperationWasInSameBatchEdit;

        public UndoInputFilter(android.widget.Editor editor) {
            mEditor = editor;
        }

        public void saveInstanceState(android.os.Parcel parcel) {
            parcel.writeInt(mIsUserEdit ? 1 : 0);
            parcel.writeInt(mHasComposition ? 1 : 0);
            parcel.writeInt(mExpanding ? 1 : 0);
            parcel.writeInt(mPreviousOperationWasInSameBatchEdit ? 1 : 0);
        }

        public void restoreInstanceState(android.os.Parcel parcel) {
            mIsUserEdit = parcel.readInt() != 0;
            mHasComposition = parcel.readInt() != 0;
            mExpanding = parcel.readInt() != 0;
            mPreviousOperationWasInSameBatchEdit = parcel.readInt() != 0;
        }

        /**
         * Signals that a user-triggered edit is starting.
         */
        public void beginBatchEdit() {
            if (android.widget.Editor.DEBUG_UNDO)
                android.util.Log.d(android.widget.Editor.TAG, "beginBatchEdit");

            mIsUserEdit = true;
        }

        public void endBatchEdit() {
            if (android.widget.Editor.DEBUG_UNDO)
                android.util.Log.d(android.widget.Editor.TAG, "endBatchEdit");

            mIsUserEdit = false;
            mPreviousOperationWasInSameBatchEdit = false;
        }

        @java.lang.Override
        public java.lang.CharSequence filter(java.lang.CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend) {
            if (android.widget.Editor.DEBUG_UNDO) {
                android.util.Log.d(android.widget.Editor.TAG, (((((((((((("filter: source=" + source) + " (") + start) + "-") + end) + ") ") + "dest=") + dest) + " (") + dstart) + "-") + dend) + ")");
            }
            // Check to see if this edit should be tracked for undo.
            if (!canUndoEdit(source, start, end, dest, dstart, dend)) {
                return null;
            }
            final boolean hadComposition = mHasComposition;
            mHasComposition = android.widget.Editor.UndoInputFilter.isComposition(source);
            final boolean wasExpanding = mExpanding;
            boolean shouldCreateSeparateState = false;
            if ((end - start) != (dend - dstart)) {
                mExpanding = (end - start) > (dend - dstart);
                if (hadComposition && (mExpanding != wasExpanding)) {
                    shouldCreateSeparateState = true;
                }
            }
            // Handle edit.
            handleEdit(source, start, end, dest, dstart, dend, shouldCreateSeparateState);
            return null;
        }

        void freezeLastEdit() {
            mEditor.mUndoManager.beginUpdate("Edit text");
            android.widget.Editor.EditOperation lastEdit = getLastEdit();
            if (lastEdit != null) {
                lastEdit.mFrozen = true;
            }
            mEditor.mUndoManager.endUpdate();
        }

        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef(prefix = { "MERGE_EDIT_MODE_" }, value = { android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_FORCE_MERGE, android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_NEVER_MERGE, android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_NORMAL })
        private @interface MergeMode {}

        private static final int MERGE_EDIT_MODE_FORCE_MERGE = 0;

        private static final int MERGE_EDIT_MODE_NEVER_MERGE = 1;

        /**
         * Use {@link EditOperation#mergeWith} to merge
         */
        private static final int MERGE_EDIT_MODE_NORMAL = 2;

        private void handleEdit(java.lang.CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend, boolean shouldCreateSeparateState) {
            // An application may install a TextWatcher to provide additional modifications after
            // the initial input filters run (e.g. a credit card formatter that adds spaces to a
            // string). This results in multiple filter() calls for what the user considers to be
            // a single operation. Always undo the whole set of changes in one step.
            @android.widget.Editor.UndoInputFilter.MergeMode
            final int mergeMode;
            if (isInTextWatcher() || mPreviousOperationWasInSameBatchEdit) {
                mergeMode = android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_FORCE_MERGE;
            } else
                if (shouldCreateSeparateState) {
                    mergeMode = android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_NEVER_MERGE;
                } else {
                    mergeMode = android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_NORMAL;
                }

            // Build a new operation with all the information from this edit.
            java.lang.String newText = android.text.TextUtils.substring(source, start, end);
            java.lang.String oldText = android.text.TextUtils.substring(dest, dstart, dend);
            android.widget.Editor.EditOperation edit = new android.widget.Editor.EditOperation(mEditor, oldText, dstart, newText, mHasComposition);
            if (mHasComposition && android.text.TextUtils.equals(edit.mNewText, edit.mOldText)) {
                return;
            }
            recordEdit(edit, mergeMode);
        }

        private android.widget.Editor.EditOperation getLastEdit() {
            final android.content.UndoManager um = mEditor.mUndoManager;
            return um.getLastOperation(android.widget.Editor.EditOperation.class, mEditor.mUndoOwner, android.content.UndoManager.MERGE_MODE_UNIQUE);
        }

        /**
         * Fetches the last undo operation and checks to see if a new edit should be merged into it.
         * If forceMerge is true then the new edit is always merged.
         */
        private void recordEdit(android.widget.Editor.EditOperation edit, @android.widget.Editor.UndoInputFilter.MergeMode
        int mergeMode) {
            // Fetch the last edit operation and attempt to merge in the new edit.
            final android.content.UndoManager um = mEditor.mUndoManager;
            um.beginUpdate("Edit text");
            android.widget.Editor.EditOperation lastEdit = getLastEdit();
            if (lastEdit == null) {
                // Add this as the first edit.
                if (android.widget.Editor.DEBUG_UNDO)
                    android.util.Log.d(android.widget.Editor.TAG, "filter: adding first op " + edit);

                um.addOperation(edit, android.content.UndoManager.MERGE_MODE_NONE);
            } else
                if (mergeMode == android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_FORCE_MERGE) {
                    // Forced merges take priority because they could be the result of a non-user-edit
                    // change and this case should not create a new undo operation.
                    if (android.widget.Editor.DEBUG_UNDO)
                        android.util.Log.d(android.widget.Editor.TAG, "filter: force merge " + edit);

                    lastEdit.forceMergeWith(edit);
                } else
                    if (!mIsUserEdit) {
                        // An application directly modified the Editable outside of a text edit. Treat this
                        // as a new change and don't attempt to merge.
                        if (android.widget.Editor.DEBUG_UNDO)
                            android.util.Log.d(android.widget.Editor.TAG, "non-user edit, new op " + edit);

                        um.commitState(mEditor.mUndoOwner);
                        um.addOperation(edit, android.content.UndoManager.MERGE_MODE_NONE);
                    } else
                        if ((mergeMode == android.widget.Editor.UndoInputFilter.MERGE_EDIT_MODE_NORMAL) && lastEdit.mergeWith(edit)) {
                            // Merge succeeded, nothing else to do.
                            if (android.widget.Editor.DEBUG_UNDO)
                                android.util.Log.d(android.widget.Editor.TAG, "filter: merge succeeded, created " + lastEdit);

                        } else {
                            // Could not merge with the last edit, so commit the last edit and add this edit.
                            if (android.widget.Editor.DEBUG_UNDO)
                                android.util.Log.d(android.widget.Editor.TAG, "filter: merge failed, adding " + edit);

                            um.commitState(mEditor.mUndoOwner);
                            um.addOperation(edit, android.content.UndoManager.MERGE_MODE_NONE);
                        }



            mPreviousOperationWasInSameBatchEdit = mIsUserEdit;
            um.endUpdate();
        }

        private boolean canUndoEdit(java.lang.CharSequence source, int start, int end, android.text.Spanned dest, int dstart, int dend) {
            if (!mEditor.mAllowUndo) {
                if (android.widget.Editor.DEBUG_UNDO)
                    android.util.Log.d(android.widget.Editor.TAG, "filter: undo is disabled");

                return false;
            }
            if (mEditor.mUndoManager.isInUndo()) {
                if (android.widget.Editor.DEBUG_UNDO)
                    android.util.Log.d(android.widget.Editor.TAG, "filter: skipping, currently performing undo/redo");

                return false;
            }
            // Text filters run before input operations are applied. However, some input operations
            // are invalid and will throw exceptions when applied. This is common in tests. Don't
            // attempt to undo invalid operations.
            if ((!android.widget.Editor.isValidRange(source, start, end)) || (!android.widget.Editor.isValidRange(dest, dstart, dend))) {
                if (android.widget.Editor.DEBUG_UNDO)
                    android.util.Log.d(android.widget.Editor.TAG, "filter: invalid op");

                return false;
            }
            // Earlier filters can rewrite input to be a no-op, for example due to a length limit
            // on an input field. Skip no-op changes.
            if ((start == end) && (dstart == dend)) {
                if (android.widget.Editor.DEBUG_UNDO)
                    android.util.Log.d(android.widget.Editor.TAG, "filter: skipping no-op");

                return false;
            }
            return true;
        }

        private static boolean isComposition(java.lang.CharSequence source) {
            if (!(source instanceof android.text.Spannable)) {
                return false;
            }
            // This is a composition edit if the source has a non-zero-length composing span.
            android.text.Spannable text = ((android.text.Spannable) (source));
            int composeBegin = com.android.internal.widget.EditableInputConnection.getComposingSpanStart(text);
            int composeEnd = com.android.internal.widget.EditableInputConnection.getComposingSpanEnd(text);
            return composeBegin < composeEnd;
        }

        private boolean isInTextWatcher() {
            java.lang.CharSequence text = mEditor.mTextView.getText();
            return (text instanceof android.text.SpannableStringBuilder) && (((android.text.SpannableStringBuilder) (text)).getTextWatcherDepth() > 0);
        }
    }

    /**
     * An operation to undo a single "edit" to a text view.
     */
    public static class EditOperation extends android.content.UndoOperation<android.widget.Editor> {
        private static final int TYPE_INSERT = 0;

        private static final int TYPE_DELETE = 1;

        private static final int TYPE_REPLACE = 2;

        private int mType;

        private java.lang.String mOldText;

        private java.lang.String mNewText;

        private int mStart;

        private int mOldCursorPos;

        private int mNewCursorPos;

        private boolean mFrozen;

        private boolean mIsComposition;

        /**
         * Constructs an edit operation from a text input operation on editor that replaces the
         * oldText starting at dstart with newText.
         */
        public EditOperation(android.widget.Editor editor, java.lang.String oldText, int dstart, java.lang.String newText, boolean isComposition) {
            super(editor.mUndoOwner);
            mOldText = oldText;
            mNewText = newText;
            // Determine the type of the edit.
            if ((mNewText.length() > 0) && (mOldText.length() == 0)) {
                mType = android.widget.Editor.EditOperation.TYPE_INSERT;
            } else
                if ((mNewText.length() == 0) && (mOldText.length() > 0)) {
                    mType = android.widget.Editor.EditOperation.TYPE_DELETE;
                } else {
                    mType = android.widget.Editor.EditOperation.TYPE_REPLACE;
                }

            mStart = dstart;
            // Store cursor data.
            mOldCursorPos = editor.mTextView.getSelectionStart();
            mNewCursorPos = dstart + mNewText.length();
            mIsComposition = isComposition;
        }

        public EditOperation(android.os.Parcel src, java.lang.ClassLoader loader) {
            super(src, loader);
            mType = src.readInt();
            mOldText = src.readString();
            mNewText = src.readString();
            mStart = src.readInt();
            mOldCursorPos = src.readInt();
            mNewCursorPos = src.readInt();
            mFrozen = src.readInt() == 1;
            mIsComposition = src.readInt() == 1;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(mType);
            dest.writeString(mOldText);
            dest.writeString(mNewText);
            dest.writeInt(mStart);
            dest.writeInt(mOldCursorPos);
            dest.writeInt(mNewCursorPos);
            dest.writeInt(mFrozen ? 1 : 0);
            dest.writeInt(mIsComposition ? 1 : 0);
        }

        private int getNewTextEnd() {
            return mStart + mNewText.length();
        }

        private int getOldTextEnd() {
            return mStart + mOldText.length();
        }

        @java.lang.Override
        public void commit() {
        }

        @java.lang.Override
        public void undo() {
            if (android.widget.Editor.DEBUG_UNDO)
                android.util.Log.d(android.widget.Editor.TAG, "undo");

            // Remove the new text and insert the old.
            android.widget.Editor editor = getOwnerData();
            android.text.Editable text = ((android.text.Editable) (editor.mTextView.getText()));
            android.widget.Editor.EditOperation.modifyText(text, mStart, getNewTextEnd(), mOldText, mStart, mOldCursorPos);
        }

        @java.lang.Override
        public void redo() {
            if (android.widget.Editor.DEBUG_UNDO)
                android.util.Log.d(android.widget.Editor.TAG, "redo");

            // Remove the old text and insert the new.
            android.widget.Editor editor = getOwnerData();
            android.text.Editable text = ((android.text.Editable) (editor.mTextView.getText()));
            android.widget.Editor.EditOperation.modifyText(text, mStart, getOldTextEnd(), mNewText, mStart, mNewCursorPos);
        }

        /**
         * Attempts to merge this existing operation with a new edit.
         *
         * @param edit
         * 		The new edit operation.
         * @return If the merge succeeded, returns true. Otherwise returns false and leaves this
        object unchanged.
         */
        private boolean mergeWith(android.widget.Editor.EditOperation edit) {
            if (android.widget.Editor.DEBUG_UNDO) {
                android.util.Log.d(android.widget.Editor.TAG, "mergeWith old " + this);
                android.util.Log.d(android.widget.Editor.TAG, "mergeWith new " + edit);
            }
            if (mFrozen) {
                return false;
            }
            switch (mType) {
                case android.widget.Editor.EditOperation.TYPE_INSERT :
                    return mergeInsertWith(edit);
                case android.widget.Editor.EditOperation.TYPE_DELETE :
                    return mergeDeleteWith(edit);
                case android.widget.Editor.EditOperation.TYPE_REPLACE :
                    return mergeReplaceWith(edit);
                default :
                    return false;
            }
        }

        private boolean mergeInsertWith(android.widget.Editor.EditOperation edit) {
            if (edit.mType == android.widget.Editor.EditOperation.TYPE_INSERT) {
                // Merge insertions that are contiguous even when it's frozen.
                if (getNewTextEnd() != edit.mStart) {
                    return false;
                }
                mNewText += edit.mNewText;
                mNewCursorPos = edit.mNewCursorPos;
                mFrozen = edit.mFrozen;
                mIsComposition = edit.mIsComposition;
                return true;
            }
            if (((mIsComposition && (edit.mType == android.widget.Editor.EditOperation.TYPE_REPLACE)) && (mStart <= edit.mStart)) && (getNewTextEnd() >= edit.getOldTextEnd())) {
                // Merge insertion with replace as they can be single insertion.
                mNewText = (mNewText.substring(0, edit.mStart - mStart) + edit.mNewText) + mNewText.substring(edit.getOldTextEnd() - mStart, mNewText.length());
                mNewCursorPos = edit.mNewCursorPos;
                mIsComposition = edit.mIsComposition;
                return true;
            }
            return false;
        }

        // TODO: Support forward delete.
        private boolean mergeDeleteWith(android.widget.Editor.EditOperation edit) {
            // Only merge continuous deletes.
            if (edit.mType != android.widget.Editor.EditOperation.TYPE_DELETE) {
                return false;
            }
            // Only merge deletions that are contiguous.
            if (mStart != edit.getOldTextEnd()) {
                return false;
            }
            mStart = edit.mStart;
            mOldText = edit.mOldText + mOldText;
            mNewCursorPos = edit.mNewCursorPos;
            mIsComposition = edit.mIsComposition;
            return true;
        }

        private boolean mergeReplaceWith(android.widget.Editor.EditOperation edit) {
            if ((edit.mType == android.widget.Editor.EditOperation.TYPE_INSERT) && (getNewTextEnd() == edit.mStart)) {
                // Merge with adjacent insert.
                mNewText += edit.mNewText;
                mNewCursorPos = edit.mNewCursorPos;
                return true;
            }
            if (!mIsComposition) {
                return false;
            }
            if (((edit.mType == android.widget.Editor.EditOperation.TYPE_DELETE) && (mStart <= edit.mStart)) && (getNewTextEnd() >= edit.getOldTextEnd())) {
                // Merge with delete as they can be single operation.
                mNewText = mNewText.substring(0, edit.mStart - mStart) + mNewText.substring(edit.getOldTextEnd() - mStart, mNewText.length());
                if (mNewText.isEmpty()) {
                    mType = android.widget.Editor.EditOperation.TYPE_DELETE;
                }
                mNewCursorPos = edit.mNewCursorPos;
                mIsComposition = edit.mIsComposition;
                return true;
            }
            if (((edit.mType == android.widget.Editor.EditOperation.TYPE_REPLACE) && (mStart == edit.mStart)) && android.text.TextUtils.equals(mNewText, edit.mOldText)) {
                // Merge with the replace that replaces the same region.
                mNewText = edit.mNewText;
                mNewCursorPos = edit.mNewCursorPos;
                mIsComposition = edit.mIsComposition;
                return true;
            }
            return false;
        }

        /**
         * Forcibly creates a single merged edit operation by simulating the entire text
         * contents being replaced.
         */
        public void forceMergeWith(android.widget.Editor.EditOperation edit) {
            if (android.widget.Editor.DEBUG_UNDO)
                android.util.Log.d(android.widget.Editor.TAG, "forceMerge");

            if (mergeWith(edit)) {
                return;
            }
            android.widget.Editor editor = getOwnerData();
            // Copy the text of the current field.
            // NOTE: Using StringBuilder instead of SpannableStringBuilder would be somewhat faster,
            // but would require two parallel implementations of modifyText() because Editable and
            // StringBuilder do not share an interface for replace/delete/insert.
            android.text.Editable editable = ((android.text.Editable) (editor.mTextView.getText()));
            android.text.Editable originalText = new android.text.SpannableStringBuilder(editable.toString());
            // Roll back the last operation.
            android.widget.Editor.EditOperation.modifyText(originalText, mStart, getNewTextEnd(), mOldText, mStart, mOldCursorPos);
            // Clone the text again and apply the new operation.
            android.text.Editable finalText = new android.text.SpannableStringBuilder(editable.toString());
            android.widget.Editor.EditOperation.modifyText(finalText, edit.mStart, edit.getOldTextEnd(), edit.mNewText, edit.mStart, edit.mNewCursorPos);
            // Convert this operation into a replace operation.
            mType = android.widget.Editor.EditOperation.TYPE_REPLACE;
            mNewText = finalText.toString();
            mOldText = originalText.toString();
            mStart = 0;
            mNewCursorPos = edit.mNewCursorPos;
            mIsComposition = edit.mIsComposition;
            // mOldCursorPos is unchanged.
        }

        private static void modifyText(android.text.Editable text, int deleteFrom, int deleteTo, java.lang.CharSequence newText, int newTextInsertAt, int newCursorPos) {
            // Apply the edit if it is still valid.
            if (android.widget.Editor.isValidRange(text, deleteFrom, deleteTo) && (newTextInsertAt <= (text.length() - (deleteTo - deleteFrom)))) {
                if (deleteFrom != deleteTo) {
                    text.delete(deleteFrom, deleteTo);
                }
                if (newText.length() != 0) {
                    text.insert(newTextInsertAt, newText);
                }
            }
            // Restore the cursor position. If there wasn't an old cursor (newCursorPos == -1) then
            // don't explicitly set it and rely on SpannableStringBuilder to position it.
            // TODO: Select all the text that was undone.
            if ((0 <= newCursorPos) && (newCursorPos <= text.length())) {
                android.text.Selection.setSelection(text, newCursorPos);
            }
        }

        private java.lang.String getTypeString() {
            switch (mType) {
                case android.widget.Editor.EditOperation.TYPE_INSERT :
                    return "insert";
                case android.widget.Editor.EditOperation.TYPE_DELETE :
                    return "delete";
                case android.widget.Editor.EditOperation.TYPE_REPLACE :
                    return "replace";
                default :
                    return "";
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((((((((((((((((("[mType=" + getTypeString()) + ", ") + "mOldText=") + mOldText) + ", ") + "mNewText=") + mNewText) + ", ") + "mStart=") + mStart) + ", ") + "mOldCursorPos=") + mOldCursorPos) + ", ") + "mNewCursorPos=") + mNewCursorPos) + ", ") + "mFrozen=") + mFrozen) + ", ") + "mIsComposition=") + mIsComposition) + "]";
        }

        public static final android.os.Parcelable.ClassLoaderCreator<android.widget.Editor.EditOperation> CREATOR = new android.os.Parcelable.ClassLoaderCreator<android.widget.Editor.EditOperation>() {
            @java.lang.Override
            public android.widget.EditOperation createFromParcel(android.os.Parcel in) {
                return new android.widget.EditOperation(in, null);
            }

            @java.lang.Override
            public android.widget.EditOperation createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.widget.EditOperation(in, loader);
            }

            @java.lang.Override
            public android.widget.EditOperation[] newArray(int size) {
                return new android.widget.EditOperation[size];
            }
        };
    }

    /**
     * A helper for enabling and handling "PROCESS_TEXT" menu actions.
     * These allow external applications to plug into currently selected text.
     */
    static final class ProcessTextIntentActionsHandler {
        private final android.widget.Editor mEditor;

        private final android.widget.TextView mTextView;

        private final android.content.Context mContext;

        private final android.content.pm.PackageManager mPackageManager;

        private final java.lang.String mPackageName;

        private final android.util.SparseArray<android.content.Intent> mAccessibilityIntents = new android.util.SparseArray();

        private final android.util.SparseArray<android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction> mAccessibilityActions = new android.util.SparseArray();

        private final java.util.List<android.content.pm.ResolveInfo> mSupportedActivities = new java.util.ArrayList<>();

        private ProcessTextIntentActionsHandler(android.widget.Editor editor) {
            mEditor = com.android.internal.util.Preconditions.checkNotNull(editor);
            mTextView = com.android.internal.util.Preconditions.checkNotNull(mEditor.mTextView);
            mContext = com.android.internal.util.Preconditions.checkNotNull(mTextView.getContext());
            mPackageManager = com.android.internal.util.Preconditions.checkNotNull(mContext.getPackageManager());
            mPackageName = com.android.internal.util.Preconditions.checkNotNull(mContext.getPackageName());
        }

        /**
         * Adds "PROCESS_TEXT" menu items to the specified menu.
         */
        public void onInitializeMenu(android.view.Menu menu) {
            loadSupportedActivities();
            final int size = mSupportedActivities.size();
            for (int i = 0; i < size; i++) {
                final android.content.pm.ResolveInfo resolveInfo = mSupportedActivities.get(i);
                menu.add(android.view.Menu.NONE, android.view.Menu.NONE, android.widget.Editor.MENU_ITEM_ORDER_PROCESS_TEXT_INTENT_ACTIONS_START + i, getLabel(resolveInfo)).setIntent(createProcessTextIntentForResolveInfo(resolveInfo)).setShowAsAction(android.view.MenuItem.SHOW_AS_ACTION_NEVER);
            }
        }

        /**
         * Performs a "PROCESS_TEXT" action if there is one associated with the specified
         * menu item.
         *
         * @return True if the action was performed, false otherwise.
         */
        public boolean performMenuItemAction(android.view.MenuItem item) {
            return fireIntent(item.getIntent());
        }

        /**
         * Initializes and caches "PROCESS_TEXT" accessibility actions.
         */
        public void initializeAccessibilityActions() {
            mAccessibilityIntents.clear();
            mAccessibilityActions.clear();
            int i = 0;
            loadSupportedActivities();
            for (android.content.pm.ResolveInfo resolveInfo : mSupportedActivities) {
                int actionId = android.widget.TextView.ACCESSIBILITY_ACTION_PROCESS_TEXT_START_ID + (i++);
                mAccessibilityActions.put(actionId, new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(actionId, getLabel(resolveInfo)));
                mAccessibilityIntents.put(actionId, createProcessTextIntentForResolveInfo(resolveInfo));
            }
        }

        /**
         * Adds "PROCESS_TEXT" accessibility actions to the specified accessibility node info.
         * NOTE: This needs a prior call to {@link #initializeAccessibilityActions()} to make the
         * latest accessibility actions available for this call.
         */
        public void onInitializeAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo nodeInfo) {
            for (int i = 0; i < mAccessibilityActions.size(); i++) {
                nodeInfo.addAction(mAccessibilityActions.valueAt(i));
            }
        }

        /**
         * Performs a "PROCESS_TEXT" action if there is one associated with the specified
         * accessibility action id.
         *
         * @return True if the action was performed, false otherwise.
         */
        public boolean performAccessibilityAction(int actionId) {
            return fireIntent(mAccessibilityIntents.get(actionId));
        }

        private boolean fireIntent(android.content.Intent intent) {
            if ((intent != null) && android.content.Intent.ACTION_PROCESS_TEXT.equals(intent.getAction())) {
                java.lang.String selectedText = mTextView.getSelectedText();
                selectedText = android.text.TextUtils.trimToParcelableSize(selectedText);
                intent.putExtra(android.content.Intent.EXTRA_PROCESS_TEXT, selectedText);
                mEditor.mPreserveSelection = true;
                mTextView.startActivityForResult(intent, android.widget.TextView.PROCESS_TEXT_REQUEST_CODE);
                return true;
            }
            return false;
        }

        private void loadSupportedActivities() {
            mSupportedActivities.clear();
            if (!mContext.canStartActivityForResult()) {
                return;
            }
            android.content.pm.PackageManager packageManager = mTextView.getContext().getPackageManager();
            java.util.List<android.content.pm.ResolveInfo> unfiltered = packageManager.queryIntentActivities(createProcessTextIntent(), 0);
            for (android.content.pm.ResolveInfo info : unfiltered) {
                if (isSupportedActivity(info)) {
                    mSupportedActivities.add(info);
                }
            }
        }

        private boolean isSupportedActivity(android.content.pm.ResolveInfo info) {
            return mPackageName.equals(info.activityInfo.packageName) || (info.activityInfo.exported && ((info.activityInfo.permission == null) || (mContext.checkSelfPermission(info.activityInfo.permission) == android.content.pm.PackageManager.PERMISSION_GRANTED)));
        }

        private android.content.Intent createProcessTextIntentForResolveInfo(android.content.pm.ResolveInfo info) {
            return createProcessTextIntent().putExtra(android.content.Intent.EXTRA_PROCESS_TEXT_READONLY, !mTextView.isTextEditable()).setClassName(info.activityInfo.packageName, info.activityInfo.name);
        }

        private android.content.Intent createProcessTextIntent() {
            return new android.content.Intent().setAction(android.content.Intent.ACTION_PROCESS_TEXT).setType("text/plain");
        }

        private java.lang.CharSequence getLabel(android.content.pm.ResolveInfo resolveInfo) {
            return resolveInfo.loadLabel(mPackageManager);
        }
    }
}

