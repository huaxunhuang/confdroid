/**
 * Copyright (C) 2017 The Android Open Source Project
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
 * Helper class for starting selection action mode
 * (synchronously without the TextClassifier, asynchronously with the TextClassifier).
 *
 * @unknown 
 */
@android.annotation.UiThread
@com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
public final class SelectionActionModeHelper {
    private static final java.lang.String LOG_TAG = "SelectActionModeHelper";

    private final android.widget.Editor mEditor;

    private final android.widget.TextView mTextView;

    private final android.widget.SelectionActionModeHelper.TextClassificationHelper mTextClassificationHelper;

    @android.annotation.Nullable
    private android.view.textclassifier.TextClassification mTextClassification;

    private android.os.AsyncTask mTextClassificationAsyncTask;

    private final android.widget.SelectionActionModeHelper.SelectionTracker mSelectionTracker;

    // TODO remove nullable marker once the switch gating the feature gets removed
    @android.annotation.Nullable
    private final android.widget.SmartSelectSprite mSmartSelectSprite;

    SelectionActionModeHelper(@android.annotation.NonNull
    android.widget.Editor editor) {
        mEditor = com.android.internal.util.Preconditions.checkNotNull(editor);
        mTextView = mEditor.getTextView();
        mTextClassificationHelper = new android.widget.SelectionActionModeHelper.TextClassificationHelper(mTextView.getContext(), mTextView::getTextClassifier, android.widget.SelectionActionModeHelper.getText(mTextView), 0, 1, mTextView.getTextLocales());
        mSelectionTracker = new android.widget.SelectionActionModeHelper.SelectionTracker(mTextView);
        if (getTextClassificationSettings().isSmartSelectionAnimationEnabled()) {
            mSmartSelectSprite = new android.widget.SmartSelectSprite(mTextView.getContext(), editor.getTextView().mHighlightColor, mTextView::invalidate);
        } else {
            mSmartSelectSprite = null;
        }
    }

    /**
     * Starts Selection ActionMode.
     */
    public void startSelectionActionModeAsync(boolean adjustSelection) {
        // Check if the smart selection should run for editable text.
        adjustSelection &= getTextClassificationSettings().isSmartSelectionEnabled();
        /* isLink */
        mSelectionTracker.onOriginalSelection(android.widget.SelectionActionModeHelper.getText(mTextView), mTextView.getSelectionStart(), mTextView.getSelectionEnd(), false);
        cancelAsyncTask();
        if (skipTextClassification()) {
            startSelectionActionMode(null);
        } else {
            resetTextClassificationHelper();
            mTextClassificationAsyncTask = new android.widget.SelectionActionModeHelper.TextClassificationAsyncTask(mTextView, mTextClassificationHelper.getTimeoutDuration(), adjustSelection ? mTextClassificationHelper::suggestSelection : mTextClassificationHelper::classifyText, mSmartSelectSprite != null ? this::startSelectionActionModeWithSmartSelectAnimation : this::startSelectionActionMode, mTextClassificationHelper::getOriginalSelection).execute();
        }
    }

    /**
     * Starts Link ActionMode.
     */
    public void startLinkActionModeAsync(int start, int end) {
        /* isLink */
        mSelectionTracker.onOriginalSelection(android.widget.SelectionActionModeHelper.getText(mTextView), start, end, true);
        cancelAsyncTask();
        if (skipTextClassification()) {
            startLinkActionMode(null);
        } else {
            resetTextClassificationHelper(start, end);
            mTextClassificationAsyncTask = new android.widget.SelectionActionModeHelper.TextClassificationAsyncTask(mTextView, mTextClassificationHelper.getTimeoutDuration(), mTextClassificationHelper::classifyText, this::startLinkActionMode, mTextClassificationHelper::getOriginalSelection).execute();
        }
    }

    public void invalidateActionModeAsync() {
        cancelAsyncTask();
        if (skipTextClassification()) {
            invalidateActionMode(null);
        } else {
            resetTextClassificationHelper();
            mTextClassificationAsyncTask = new android.widget.SelectionActionModeHelper.TextClassificationAsyncTask(mTextView, mTextClassificationHelper.getTimeoutDuration(), mTextClassificationHelper::classifyText, this::invalidateActionMode, mTextClassificationHelper::getOriginalSelection).execute();
        }
    }

    /**
     * Reports a selection action event.
     */
    public void onSelectionAction(int menuItemId, @android.annotation.Nullable
    java.lang.String actionLabel) {
        mSelectionTracker.onSelectionAction(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), android.widget.SelectionActionModeHelper.getActionType(menuItemId), actionLabel, mTextClassification);
    }

    public void onSelectionDrag() {
        /* actionLabel= */
        mSelectionTracker.onSelectionAction(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), android.view.textclassifier.SelectionEvent.ACTION_DRAG, null, mTextClassification);
    }

    public void onTextChanged(int start, int end) {
        mSelectionTracker.onTextChanged(start, end, mTextClassification);
    }

    public boolean resetSelection(int textIndex) {
        if (mSelectionTracker.resetSelection(textIndex, mEditor)) {
            invalidateActionModeAsync();
            return true;
        }
        return false;
    }

    @android.annotation.Nullable
    public android.view.textclassifier.TextClassification getTextClassification() {
        return mTextClassification;
    }

    public void onDestroyActionMode() {
        cancelSmartSelectAnimation();
        mSelectionTracker.onSelectionDestroyed();
        cancelAsyncTask();
    }

    public void onDraw(final android.graphics.Canvas canvas) {
        if (isDrawingHighlight() && (mSmartSelectSprite != null)) {
            mSmartSelectSprite.draw(canvas);
        }
    }

    public boolean isDrawingHighlight() {
        return (mSmartSelectSprite != null) && mSmartSelectSprite.isAnimationActive();
    }

    private android.view.textclassifier.TextClassificationConstants getTextClassificationSettings() {
        return android.view.textclassifier.TextClassificationManager.getSettings(mTextView.getContext());
    }

    private void cancelAsyncTask() {
        if (mTextClassificationAsyncTask != null) {
            mTextClassificationAsyncTask.cancel(true);
            mTextClassificationAsyncTask = null;
        }
        mTextClassification = null;
    }

    private boolean skipTextClassification() {
        // No need to make an async call for a no-op TextClassifier.
        final boolean noOpTextClassifier = mTextView.usesNoOpTextClassifier();
        // Do not call the TextClassifier if there is no selection.
        final boolean noSelection = mTextView.getSelectionEnd() == mTextView.getSelectionStart();
        // Do not call the TextClassifier if this is a password field.
        final boolean password = mTextView.hasPasswordTransformationMethod() || android.widget.TextView.isPasswordInputType(mTextView.getInputType());
        return (noOpTextClassifier || noSelection) || password;
    }

    private void startLinkActionMode(@android.annotation.Nullable
    android.widget.SelectionActionModeHelper.SelectionResult result) {
        startActionMode(android.widget.Editor.TextActionMode.TEXT_LINK, result);
    }

    private void startSelectionActionMode(@android.annotation.Nullable
    android.widget.SelectionActionModeHelper.SelectionResult result) {
        startActionMode(android.widget.Editor.TextActionMode.SELECTION, result);
    }

    private void startActionMode(@android.widget.Editor.TextActionMode
    int actionMode, @android.annotation.Nullable
    android.widget.SelectionActionModeHelper.SelectionResult result) {
        final java.lang.CharSequence text = android.widget.SelectionActionModeHelper.getText(mTextView);
        if (((result != null) && (text instanceof android.text.Spannable)) && (mTextView.isTextSelectable() || mTextView.isTextEditable())) {
            // Do not change the selection if TextClassifier should be dark launched.
            if (!getTextClassificationSettings().isModelDarkLaunchEnabled()) {
                android.text.Selection.setSelection(((android.text.Spannable) (text)), result.mStart, result.mEnd);
                mTextView.invalidate();
            }
            mTextClassification = result.mClassification;
        } else
            if ((result != null) && (actionMode == android.widget.Editor.TextActionMode.TEXT_LINK)) {
                mTextClassification = result.mClassification;
            } else {
                mTextClassification = null;
            }

        if (mEditor.startActionModeInternal(actionMode)) {
            final android.widget.Editor.SelectionModifierCursorController controller = mEditor.getSelectionController();
            if ((controller != null) && (mTextView.isTextSelectable() || mTextView.isTextEditable())) {
                controller.show();
            }
            if (result != null) {
                switch (actionMode) {
                    case android.widget.Editor.TextActionMode.SELECTION :
                        mSelectionTracker.onSmartSelection(result);
                        break;
                    case android.widget.Editor.TextActionMode.TEXT_LINK :
                        mSelectionTracker.onLinkSelected(result);
                        break;
                    default :
                        break;
                }
            }
        }
        mEditor.setRestartActionModeOnNextRefresh(false);
        mTextClassificationAsyncTask = null;
    }

    private void startSelectionActionModeWithSmartSelectAnimation(@android.annotation.Nullable
    android.widget.SelectionActionModeHelper.SelectionResult result) {
        final android.text.Layout layout = mTextView.getLayout();
        final java.lang.Runnable onAnimationEndCallback = () -> {
            final android.widget.SelectionActionModeHelper.SelectionResult startSelectionResult;
            if ((((result != null) && (result.mStart >= 0)) && (result.mEnd <= android.widget.SelectionActionModeHelper.getText(mTextView).length())) && (result.mStart <= result.mEnd)) {
                startSelectionResult = result;
            } else {
                startSelectionResult = null;
            }
            startSelectionActionMode(startSelectionResult);
        };
        // TODO do not trigger the animation if the change included only non-printable characters
        final boolean didSelectionChange = (result != null) && ((mTextView.getSelectionStart() != result.mStart) || (mTextView.getSelectionEnd() != result.mEnd));
        if (!didSelectionChange) {
            onAnimationEndCallback.run();
            return;
        }
        final java.util.List<android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout> selectionRectangles = convertSelectionToRectangles(layout, result.mStart, result.mEnd);
        final android.graphics.PointF touchPoint = new android.graphics.PointF(mEditor.getLastUpPositionX(), mEditor.getLastUpPositionY());
        final android.graphics.PointF animationStartPoint = android.widget.SelectionActionModeHelper.movePointInsideNearestRectangle(touchPoint, selectionRectangles, android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout::getRectangle);
        mSmartSelectSprite.startAnimation(animationStartPoint, selectionRectangles, onAnimationEndCallback);
    }

    private java.util.List<android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout> convertSelectionToRectangles(final android.text.Layout layout, final int start, final int end) {
        final java.util.List<android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout> result = new java.util.ArrayList<>();
        final android.text.Layout.SelectionRectangleConsumer consumer = ( left, top, right, bottom, textSelectionLayout) -> mergeRectangleIntoList(result, new android.graphics.RectF(left, top, right, bottom), SmartSelectSprite.RectangleWithTextSelectionLayout::getRectangle, ( r) -> new android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout(r, textSelectionLayout));
        layout.getSelection(start, end, consumer);
        result.sort(java.util.Comparator.comparing(android.widget.SmartSelectSprite.RectangleWithTextSelectionLayout::getRectangle, android.widget.SmartSelectSprite.RECTANGLE_COMPARATOR));
        return result;
    }

    // TODO: Move public pure functions out of this class and make it package-private.
    /**
     * Merges a {@link RectF} into an existing list of any objects which contain a rectangle.
     * While merging, this method makes sure that:
     *
     * <ol>
     * <li>No rectangle is redundant (contained within a bigger rectangle)</li>
     * <li>Rectangles of the same height and vertical position that intersect get merged</li>
     * </ol>
     *
     * @param list
     * 		the list of rectangles (or other rectangle containers) to merge the new
     * 		rectangle into
     * @param candidate
     * 		the {@link RectF} to merge into the list
     * @param extractor
     * 		a function that can extract a {@link RectF} from an element of the given
     * 		list
     * @param packer
     * 		a function that can wrap the resulting {@link RectF} into an element that
     * 		the list contains
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static <T> void mergeRectangleIntoList(final java.util.List<T> list, final android.graphics.RectF candidate, final java.util.function.Function<T, android.graphics.RectF> extractor, final java.util.function.Function<android.graphics.RectF, T> packer) {
        if (candidate.isEmpty()) {
            return;
        }
        final int elementCount = list.size();
        for (int index = 0; index < elementCount; ++index) {
            final android.graphics.RectF existingRectangle = extractor.apply(list.get(index));
            if (existingRectangle.contains(candidate)) {
                return;
            }
            if (candidate.contains(existingRectangle)) {
                existingRectangle.setEmpty();
                continue;
            }
            final boolean rectanglesContinueEachOther = (candidate.left == existingRectangle.right) || (candidate.right == existingRectangle.left);
            final boolean canMerge = ((candidate.top == existingRectangle.top) && (candidate.bottom == existingRectangle.bottom)) && (android.graphics.RectF.intersects(candidate, existingRectangle) || rectanglesContinueEachOther);
            if (canMerge) {
                candidate.union(existingRectangle);
                existingRectangle.setEmpty();
            }
        }
        for (int index = elementCount - 1; index >= 0; --index) {
            final android.graphics.RectF rectangle = extractor.apply(list.get(index));
            if (rectangle.isEmpty()) {
                list.remove(index);
            }
        }
        list.add(packer.apply(candidate));
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static <T> android.graphics.PointF movePointInsideNearestRectangle(final android.graphics.PointF point, final java.util.List<T> list, final java.util.function.Function<T, android.graphics.RectF> extractor) {
        float bestX = -1;
        float bestY = -1;
        double bestDistance = java.lang.Double.MAX_VALUE;
        final int elementCount = list.size();
        for (int index = 0; index < elementCount; ++index) {
            final android.graphics.RectF rectangle = extractor.apply(list.get(index));
            final float candidateY = rectangle.centerY();
            final float candidateX;
            if (point.x > rectangle.right) {
                candidateX = rectangle.right;
            } else
                if (point.x < rectangle.left) {
                    candidateX = rectangle.left;
                } else {
                    candidateX = point.x;
                }

            final double candidateDistance = java.lang.Math.pow(point.x - candidateX, 2) + java.lang.Math.pow(point.y - candidateY, 2);
            if (candidateDistance < bestDistance) {
                bestX = candidateX;
                bestY = candidateY;
                bestDistance = candidateDistance;
            }
        }
        return new android.graphics.PointF(bestX, bestY);
    }

    private void invalidateActionMode(@android.annotation.Nullable
    android.widget.SelectionActionModeHelper.SelectionResult result) {
        cancelSmartSelectAnimation();
        mTextClassification = (result != null) ? result.mClassification : null;
        final android.view.ActionMode actionMode = mEditor.getTextActionMode();
        if (actionMode != null) {
            actionMode.invalidate();
        }
        mSelectionTracker.onSelectionUpdated(mTextView.getSelectionStart(), mTextView.getSelectionEnd(), mTextClassification);
        mTextClassificationAsyncTask = null;
    }

    private void resetTextClassificationHelper(int selectionStart, int selectionEnd) {
        if ((selectionStart < 0) || (selectionEnd < 0)) {
            // Use selection indices
            selectionStart = mTextView.getSelectionStart();
            selectionEnd = mTextView.getSelectionEnd();
        }
        mTextClassificationHelper.init(mTextView::getTextClassifier, android.widget.SelectionActionModeHelper.getText(mTextView), selectionStart, selectionEnd, mTextView.getTextLocales());
    }

    private void resetTextClassificationHelper() {
        resetTextClassificationHelper(-1, -1);
    }

    private void cancelSmartSelectAnimation() {
        if (mSmartSelectSprite != null) {
            mSmartSelectSprite.cancelAnimation();
        }
    }

    /**
     * Tracks and logs smart selection changes.
     * It is important to trigger this object's methods at the appropriate event so that it tracks
     * smart selection events appropriately.
     */
    private static final class SelectionTracker {
        private final android.widget.TextView mTextView;

        private android.widget.SelectionActionModeHelper.SelectionMetricsLogger mLogger;

        private int mOriginalStart;

        private int mOriginalEnd;

        private int mSelectionStart;

        private int mSelectionEnd;

        private boolean mAllowReset;

        private final android.widget.SelectionActionModeHelper.SelectionTracker.LogAbandonRunnable mDelayedLogAbandon = new android.widget.SelectionActionModeHelper.SelectionTracker.LogAbandonRunnable();

        SelectionTracker(android.widget.TextView textView) {
            mTextView = com.android.internal.util.Preconditions.checkNotNull(textView);
            mLogger = new android.widget.SelectionActionModeHelper.SelectionMetricsLogger(textView);
        }

        /**
         * Called when the original selection happens, before smart selection is triggered.
         */
        public void onOriginalSelection(java.lang.CharSequence text, int selectionStart, int selectionEnd, boolean isLink) {
            // If we abandoned a selection and created a new one very shortly after, we may still
            // have a pending request to log ABANDON, which we flush here.
            mDelayedLogAbandon.flush();
            mOriginalStart = mSelectionStart = selectionStart;
            mOriginalEnd = mSelectionEnd = selectionEnd;
            mAllowReset = false;
            maybeInvalidateLogger();
            mLogger.logSelectionStarted(mTextView.getTextClassificationSession(), mTextView.getTextClassificationContext(), text, selectionStart, isLink ? android.view.textclassifier.SelectionEvent.INVOCATION_LINK : android.view.textclassifier.SelectionEvent.INVOCATION_MANUAL);
        }

        /**
         * Called when selection action mode is started and the results come from a classifier.
         */
        public void onSmartSelection(android.widget.SelectionActionModeHelper.SelectionResult result) {
            onClassifiedSelection(result);
            mLogger.logSelectionModified(result.mStart, result.mEnd, result.mClassification, result.mSelection);
        }

        /**
         * Called when link action mode is started and the classification comes from a classifier.
         */
        public void onLinkSelected(android.widget.SelectionActionModeHelper.SelectionResult result) {
            onClassifiedSelection(result);
            // TODO: log (b/70246800)
        }

        private void onClassifiedSelection(android.widget.SelectionActionModeHelper.SelectionResult result) {
            if (isSelectionStarted()) {
                mSelectionStart = result.mStart;
                mSelectionEnd = result.mEnd;
                mAllowReset = (mSelectionStart != mOriginalStart) || (mSelectionEnd != mOriginalEnd);
            }
        }

        /**
         * Called when selection bounds change.
         */
        public void onSelectionUpdated(int selectionStart, int selectionEnd, @android.annotation.Nullable
        android.view.textclassifier.TextClassification classification) {
            if (isSelectionStarted()) {
                mSelectionStart = selectionStart;
                mSelectionEnd = selectionEnd;
                mAllowReset = false;
                mLogger.logSelectionModified(selectionStart, selectionEnd, classification, null);
            }
        }

        /**
         * Called when the selection action mode is destroyed.
         */
        public void onSelectionDestroyed() {
            mAllowReset = false;
            // Wait a few ms to see if the selection was destroyed because of a text change event.
            /* ms */
            mDelayedLogAbandon.schedule(100);
        }

        /**
         * Called when an action is taken on a smart selection.
         */
        public void onSelectionAction(int selectionStart, int selectionEnd, @android.view.textclassifier.SelectionEvent.ActionType
        int action, @android.annotation.Nullable
        java.lang.String actionLabel, @android.annotation.Nullable
        android.view.textclassifier.TextClassification classification) {
            if (isSelectionStarted()) {
                mAllowReset = false;
                mLogger.logSelectionAction(selectionStart, selectionEnd, action, actionLabel, classification);
            }
        }

        /**
         * Returns true if the current smart selection should be reset to normal selection based on
         * information that has been recorded about the original selection and the smart selection.
         * The expected UX here is to allow the user to select a word inside of the smart selection
         * on a single tap.
         */
        public boolean resetSelection(int textIndex, android.widget.Editor editor) {
            final android.widget.TextView textView = editor.getTextView();
            if ((((isSelectionStarted() && mAllowReset) && (textIndex >= mSelectionStart)) && (textIndex <= mSelectionEnd)) && (android.widget.SelectionActionModeHelper.getText(textView) instanceof android.text.Spannable)) {
                mAllowReset = false;
                boolean selected = editor.selectCurrentWord();
                if (selected) {
                    mSelectionStart = editor.getTextView().getSelectionStart();
                    mSelectionEnd = editor.getTextView().getSelectionEnd();
                    /* actionLabel= */
                    /* classification= */
                    mLogger.logSelectionAction(textView.getSelectionStart(), textView.getSelectionEnd(), android.view.textclassifier.SelectionEvent.ACTION_RESET, null, null);
                }
                return selected;
            }
            return false;
        }

        public void onTextChanged(int start, int end, android.view.textclassifier.TextClassification classification) {
            if ((isSelectionStarted() && (start == mSelectionStart)) && (end == mSelectionEnd)) {
                /* actionLabel= */
                onSelectionAction(start, end, android.view.textclassifier.SelectionEvent.ACTION_OVERTYPE, null, classification);
            }
        }

        private void maybeInvalidateLogger() {
            if (mLogger.isEditTextLogger() != mTextView.isTextEditable()) {
                mLogger = new android.widget.SelectionActionModeHelper.SelectionMetricsLogger(mTextView);
            }
        }

        private boolean isSelectionStarted() {
            return ((mSelectionStart >= 0) && (mSelectionEnd >= 0)) && (mSelectionStart != mSelectionEnd);
        }

        /**
         * A helper for keeping track of pending abandon logging requests.
         */
        private final class LogAbandonRunnable implements java.lang.Runnable {
            private boolean mIsPending;

            /**
             * Schedules an abandon to be logged with the given delay. Flush if necessary.
             */
            void schedule(int delayMillis) {
                if (mIsPending) {
                    android.util.Log.e(android.widget.SelectionActionModeHelper.LOG_TAG, "Force flushing abandon due to new scheduling request");
                    flush();
                }
                mIsPending = true;
                mTextView.postDelayed(this, delayMillis);
            }

            /**
             * If there is a pending log request, execute it now.
             */
            void flush() {
                mTextView.removeCallbacks(this);
                run();
            }

            @java.lang.Override
            public void run() {
                if (mIsPending) {
                    /* actionLabel= */
                    /* classification= */
                    mLogger.logSelectionAction(mSelectionStart, mSelectionEnd, android.view.textclassifier.SelectionEvent.ACTION_ABANDON, null, null);
                    mSelectionStart = mSelectionEnd = -1;
                    mLogger.endTextClassificationSession();
                    mIsPending = false;
                }
            }
        }
    }

    // TODO: Write tests
    /**
     * Metrics logging helper.
     *
     * This logger logs selection by word indices. The initial (start) single word selection is
     * logged at [0, 1) -- end index is exclusive. Other word indices are logged relative to the
     * initial single word selection.
     * e.g. New York city, NY. Suppose the initial selection is "York" in
     * "New York city, NY", then "York" is at [0, 1), "New" is at [-1, 0], and "city" is at [1, 2).
     * "New York" is at [-1, 1).
     * Part selection of a word e.g. "or" is counted as selecting the
     * entire word i.e. equivalent to "York", and each special character is counted as a word, e.g.
     * "," is at [2, 3). Whitespaces are ignored.
     *
     * NOTE that the definition of a word is defined by the TextClassifier's Logger's token
     * iterator.
     */
    private static final class SelectionMetricsLogger {
        private static final java.lang.String LOG_TAG = "SelectionMetricsLogger";

        private static final java.util.regex.Pattern PATTERN_WHITESPACE = java.util.regex.Pattern.compile("\\s+");

        private final boolean mEditTextLogger;

        private final java.text.BreakIterator mTokenIterator;

        @android.annotation.Nullable
        private android.view.textclassifier.TextClassifier mClassificationSession;

        @android.annotation.Nullable
        private android.view.textclassifier.TextClassificationContext mClassificationContext;

        @android.annotation.Nullable
        private android.view.textclassifier.TextClassifierEvent mTranslateViewEvent;

        @android.annotation.Nullable
        private android.view.textclassifier.TextClassifierEvent mTranslateClickEvent;

        private int mStartIndex;

        private java.lang.String mText;

        SelectionMetricsLogger(android.widget.TextView textView) {
            com.android.internal.util.Preconditions.checkNotNull(textView);
            mEditTextLogger = textView.isTextEditable();
            mTokenIterator = android.view.textclassifier.SelectionSessionLogger.getTokenIterator(textView.getTextLocale());
        }

        public void logSelectionStarted(android.view.textclassifier.TextClassifier classificationSession, android.view.textclassifier.TextClassificationContext classificationContext, java.lang.CharSequence text, int index, @android.view.textclassifier.SelectionEvent.InvocationMethod
        int invocationMethod) {
            try {
                com.android.internal.util.Preconditions.checkNotNull(text);
                com.android.internal.util.Preconditions.checkArgumentInRange(index, 0, text.length(), "index");
                if ((mText == null) || (!mText.contentEquals(text))) {
                    mText = text.toString();
                }
                mTokenIterator.setText(mText);
                mStartIndex = index;
                mClassificationSession = classificationSession;
                mClassificationContext = classificationContext;
                if (hasActiveClassificationSession()) {
                    mClassificationSession.onSelectionEvent(android.view.textclassifier.SelectionEvent.createSelectionStartedEvent(invocationMethod, 0));
                }
            } catch (java.lang.Exception e) {
                // Avoid crashes due to logging.
                android.util.Log.e(android.widget.SelectionActionModeHelper.SelectionMetricsLogger.LOG_TAG, "" + e.getMessage(), e);
            }
        }

        public void logSelectionModified(int start, int end, @android.annotation.Nullable
        android.view.textclassifier.TextClassification classification, @android.annotation.Nullable
        android.view.textclassifier.TextSelection selection) {
            try {
                if (hasActiveClassificationSession()) {
                    com.android.internal.util.Preconditions.checkArgumentInRange(start, 0, mText.length(), "start");
                    com.android.internal.util.Preconditions.checkArgumentInRange(end, start, mText.length(), "end");
                    int[] wordIndices = getWordDelta(start, end);
                    if (selection != null) {
                        mClassificationSession.onSelectionEvent(android.view.textclassifier.SelectionEvent.createSelectionModifiedEvent(wordIndices[0], wordIndices[1], selection));
                    } else
                        if (classification != null) {
                            mClassificationSession.onSelectionEvent(android.view.textclassifier.SelectionEvent.createSelectionModifiedEvent(wordIndices[0], wordIndices[1], classification));
                        } else {
                            mClassificationSession.onSelectionEvent(android.view.textclassifier.SelectionEvent.createSelectionModifiedEvent(wordIndices[0], wordIndices[1]));
                        }

                    maybeGenerateTranslateViewEvent(classification);
                }
            } catch (java.lang.Exception e) {
                // Avoid crashes due to logging.
                android.util.Log.e(android.widget.SelectionActionModeHelper.SelectionMetricsLogger.LOG_TAG, "" + e.getMessage(), e);
            }
        }

        public void logSelectionAction(int start, int end, @android.view.textclassifier.SelectionEvent.ActionType
        int action, @android.annotation.Nullable
        java.lang.String actionLabel, @android.annotation.Nullable
        android.view.textclassifier.TextClassification classification) {
            try {
                if (hasActiveClassificationSession()) {
                    com.android.internal.util.Preconditions.checkArgumentInRange(start, 0, mText.length(), "start");
                    com.android.internal.util.Preconditions.checkArgumentInRange(end, start, mText.length(), "end");
                    int[] wordIndices = getWordDelta(start, end);
                    if (classification != null) {
                        mClassificationSession.onSelectionEvent(android.view.textclassifier.SelectionEvent.createSelectionActionEvent(wordIndices[0], wordIndices[1], action, classification));
                    } else {
                        mClassificationSession.onSelectionEvent(android.view.textclassifier.SelectionEvent.createSelectionActionEvent(wordIndices[0], wordIndices[1], action));
                    }
                    maybeGenerateTranslateClickEvent(classification, actionLabel);
                    if (android.view.textclassifier.SelectionEvent.isTerminal(action)) {
                        endTextClassificationSession();
                    }
                }
            } catch (java.lang.Exception e) {
                // Avoid crashes due to logging.
                android.util.Log.e(android.widget.SelectionActionModeHelper.SelectionMetricsLogger.LOG_TAG, "" + e.getMessage(), e);
            }
        }

        public boolean isEditTextLogger() {
            return mEditTextLogger;
        }

        public void endTextClassificationSession() {
            if (hasActiveClassificationSession()) {
                maybeReportTranslateEvents();
                mClassificationSession.destroy();
            }
        }

        private boolean hasActiveClassificationSession() {
            return (mClassificationSession != null) && (!mClassificationSession.isDestroyed());
        }

        private int[] getWordDelta(int start, int end) {
            int[] wordIndices = new int[2];
            if (start == mStartIndex) {
                wordIndices[0] = 0;
            } else
                if (start < mStartIndex) {
                    wordIndices[0] = -countWordsForward(start);
                } else {
                    // start > mStartIndex
                    wordIndices[0] = countWordsBackward(start);
                    // For the selection start index, avoid counting a partial word backwards.
                    if ((!mTokenIterator.isBoundary(start)) && (!isWhitespace(mTokenIterator.preceding(start), mTokenIterator.following(start)))) {
                        // We counted a partial word. Remove it.
                        wordIndices[0]--;
                    }
                }

            if (end == mStartIndex) {
                wordIndices[1] = 0;
            } else
                if (end < mStartIndex) {
                    wordIndices[1] = -countWordsForward(end);
                } else {
                    // end > mStartIndex
                    wordIndices[1] = countWordsBackward(end);
                }

            return wordIndices;
        }

        private int countWordsBackward(int from) {
            com.android.internal.util.Preconditions.checkArgument(from >= mStartIndex);
            int wordCount = 0;
            int offset = from;
            while (offset > mStartIndex) {
                int start = mTokenIterator.preceding(offset);
                if (!isWhitespace(start, offset)) {
                    wordCount++;
                }
                offset = start;
            } 
            return wordCount;
        }

        private int countWordsForward(int from) {
            com.android.internal.util.Preconditions.checkArgument(from <= mStartIndex);
            int wordCount = 0;
            int offset = from;
            while (offset < mStartIndex) {
                int end = mTokenIterator.following(offset);
                if (!isWhitespace(offset, end)) {
                    wordCount++;
                }
                offset = end;
            } 
            return wordCount;
        }

        private boolean isWhitespace(int start, int end) {
            return android.widget.SelectionActionModeHelper.SelectionMetricsLogger.PATTERN_WHITESPACE.matcher(mText.substring(start, end)).matches();
        }

        private void maybeGenerateTranslateViewEvent(@android.annotation.Nullable
        android.view.textclassifier.TextClassification classification) {
            if (classification != null) {
                final android.view.textclassifier.TextClassifierEvent event = /* actionLabel= */
                android.widget.SelectionActionModeHelper.SelectionMetricsLogger.generateTranslateEvent(android.view.textclassifier.TextClassifierEvent.TYPE_ACTIONS_SHOWN, classification, mClassificationContext, null);
                mTranslateViewEvent = (event != null) ? event : mTranslateViewEvent;
            }
        }

        private void maybeGenerateTranslateClickEvent(@android.annotation.Nullable
        android.view.textclassifier.TextClassification classification, java.lang.String actionLabel) {
            if (classification != null) {
                mTranslateClickEvent = android.widget.SelectionActionModeHelper.SelectionMetricsLogger.generateTranslateEvent(android.view.textclassifier.TextClassifierEvent.TYPE_SMART_ACTION, classification, mClassificationContext, actionLabel);
            }
        }

        private void maybeReportTranslateEvents() {
            // Translate view and click events should only be logged once per selection session.
            if (mTranslateViewEvent != null) {
                mClassificationSession.onTextClassifierEvent(mTranslateViewEvent);
                mTranslateViewEvent = null;
            }
            if (mTranslateClickEvent != null) {
                mClassificationSession.onTextClassifierEvent(mTranslateClickEvent);
                mTranslateClickEvent = null;
            }
        }

        @android.annotation.Nullable
        private static android.view.textclassifier.TextClassifierEvent generateTranslateEvent(int eventType, android.view.textclassifier.TextClassification classification, android.view.textclassifier.TextClassificationContext classificationContext, @android.annotation.Nullable
        java.lang.String actionLabel) {
            // The platform attempts to log "views" and "clicks" of the "Translate" action.
            // Views are logged if a user is presented with the translate action during a selection
            // session.
            // Clicks are logged if the user clicks on the translate action.
            // The index of the translate action is also logged to indicate whether it might have
            // been in the main panel or overflow panel of the selection toolbar.
            // NOTE that the "views" metric may be flawed if a TextView removes the translate menu
            // item via a custom action mode callback or does not show a selection menu item.
            final android.app.RemoteAction translateAction = android.view.textclassifier.ExtrasUtils.findTranslateAction(classification);
            if (translateAction == null) {
                // No translate action present. Nothing to log. Exit.
                return null;
            }
            if ((eventType == android.view.textclassifier.TextClassifierEvent.TYPE_SMART_ACTION) && (!translateAction.getTitle().toString().equals(actionLabel))) {
                // Clicked action is not a translate action. Nothing to log. Exit.
                // Note that we don't expect an actionLabel for "view" events.
                return null;
            }
            final android.os.Bundle foreignLanguageExtra = android.view.textclassifier.ExtrasUtils.getForeignLanguageExtra(classification);
            final java.lang.String language = android.view.textclassifier.ExtrasUtils.getEntityType(foreignLanguageExtra);
            final float score = android.view.textclassifier.ExtrasUtils.getScore(foreignLanguageExtra);
            final java.lang.String model = android.view.textclassifier.ExtrasUtils.getModelName(foreignLanguageExtra);
            return new android.view.textclassifier.TextClassifierEvent.LanguageDetectionEvent.Builder(eventType).setEventContext(classificationContext).setResultId(classification.getId()).setEntityTypes(language).setScores(score).setActionIndices(classification.getActions().indexOf(translateAction)).setModelName(model).build();
        }
    }

    /**
     * AsyncTask for running a query on a background thread and returning the result on the
     * UiThread. The AsyncTask times out after a specified time, returning a null result if the
     * query has not yet returned.
     */
    private static final class TextClassificationAsyncTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, android.widget.SelectionActionModeHelper.SelectionResult> {
        private final int mTimeOutDuration;

        private final java.util.function.Supplier<android.widget.SelectionActionModeHelper.SelectionResult> mSelectionResultSupplier;

        private final java.util.function.Consumer<android.widget.SelectionActionModeHelper.SelectionResult> mSelectionResultCallback;

        private final java.util.function.Supplier<android.widget.SelectionActionModeHelper.SelectionResult> mTimeOutResultSupplier;

        private final android.widget.TextView mTextView;

        private final java.lang.String mOriginalText;

        /**
         *
         *
         * @param textView
         * 		the TextView
         * @param timeOut
         * 		time in milliseconds to timeout the query if it has not completed
         * @param selectionResultSupplier
         * 		fetches the selection results. Runs on a background thread
         * @param selectionResultCallback
         * 		receives the selection results. Runs on the UiThread
         * @param timeOutResultSupplier
         * 		default result if the task times out
         */
        TextClassificationAsyncTask(@android.annotation.NonNull
        android.widget.TextView textView, int timeOut, @android.annotation.NonNull
        java.util.function.Supplier<android.widget.SelectionActionModeHelper.SelectionResult> selectionResultSupplier, @android.annotation.NonNull
        java.util.function.Consumer<android.widget.SelectionActionModeHelper.SelectionResult> selectionResultCallback, @android.annotation.NonNull
        java.util.function.Supplier<android.widget.SelectionActionModeHelper.SelectionResult> timeOutResultSupplier) {
            super(textView != null ? textView.getHandler() : null);
            mTextView = com.android.internal.util.Preconditions.checkNotNull(textView);
            mTimeOutDuration = timeOut;
            mSelectionResultSupplier = com.android.internal.util.Preconditions.checkNotNull(selectionResultSupplier);
            mSelectionResultCallback = com.android.internal.util.Preconditions.checkNotNull(selectionResultCallback);
            mTimeOutResultSupplier = com.android.internal.util.Preconditions.checkNotNull(timeOutResultSupplier);
            // Make a copy of the original text.
            mOriginalText = android.widget.SelectionActionModeHelper.getText(mTextView).toString();
        }

        @java.lang.Override
        @android.annotation.WorkerThread
        protected android.widget.SelectionActionModeHelper.SelectionResult doInBackground(java.lang.Void... params) {
            final java.lang.Runnable onTimeOut = this::onTimeOut;
            mTextView.postDelayed(onTimeOut, mTimeOutDuration);
            final android.widget.SelectionActionModeHelper.SelectionResult result = mSelectionResultSupplier.get();
            mTextView.removeCallbacks(onTimeOut);
            return result;
        }

        @java.lang.Override
        @android.annotation.UiThread
        protected void onPostExecute(android.widget.SelectionActionModeHelper.SelectionResult result) {
            result = (android.text.TextUtils.equals(mOriginalText, android.widget.SelectionActionModeHelper.getText(mTextView))) ? result : null;
            mSelectionResultCallback.accept(result);
        }

        private void onTimeOut() {
            if (getStatus() == Status.RUNNING) {
                onPostExecute(mTimeOutResultSupplier.get());
            }
            cancel(true);
        }
    }

    /**
     * Helper class for querying the TextClassifier.
     * It trims text so that only text necessary to provide context of the selected text is
     * sent to the TextClassifier.
     */
    private static final class TextClassificationHelper {
        private static final int TRIM_DELTA = 120;// characters


        private final android.content.Context mContext;

        private java.util.function.Supplier<android.view.textclassifier.TextClassifier> mTextClassifier;

        /**
         * The original TextView text. *
         */
        private java.lang.String mText;

        /**
         * Start index relative to mText.
         */
        private int mSelectionStart;

        /**
         * End index relative to mText.
         */
        private int mSelectionEnd;

        @android.annotation.Nullable
        private android.os.LocaleList mDefaultLocales;

        /**
         * Trimmed text starting from mTrimStart in mText.
         */
        private java.lang.CharSequence mTrimmedText;

        /**
         * Index indicating the start of mTrimmedText in mText.
         */
        private int mTrimStart;

        /**
         * Start index relative to mTrimmedText
         */
        private int mRelativeStart;

        /**
         * End index relative to mTrimmedText
         */
        private int mRelativeEnd;

        /**
         * Information about the last classified text to avoid re-running a query.
         */
        private java.lang.CharSequence mLastClassificationText;

        private int mLastClassificationSelectionStart;

        private int mLastClassificationSelectionEnd;

        private android.os.LocaleList mLastClassificationLocales;

        private android.widget.SelectionActionModeHelper.SelectionResult mLastClassificationResult;

        /**
         * Whether the TextClassifier has been initialized.
         */
        private boolean mHot;

        TextClassificationHelper(android.content.Context context, java.util.function.Supplier<android.view.textclassifier.TextClassifier> textClassifier, java.lang.CharSequence text, int selectionStart, int selectionEnd, android.os.LocaleList locales) {
            init(textClassifier, text, selectionStart, selectionEnd, locales);
            mContext = com.android.internal.util.Preconditions.checkNotNull(context);
        }

        @android.annotation.UiThread
        public void init(java.util.function.Supplier<android.view.textclassifier.TextClassifier> textClassifier, java.lang.CharSequence text, int selectionStart, int selectionEnd, android.os.LocaleList locales) {
            mTextClassifier = com.android.internal.util.Preconditions.checkNotNull(textClassifier);
            mText = com.android.internal.util.Preconditions.checkNotNull(text).toString();
            mLastClassificationText = null;// invalidate.

            com.android.internal.util.Preconditions.checkArgument(selectionEnd > selectionStart);
            mSelectionStart = selectionStart;
            mSelectionEnd = selectionEnd;
            mDefaultLocales = locales;
        }

        @android.annotation.WorkerThread
        public android.widget.SelectionActionModeHelper.SelectionResult classifyText() {
            mHot = true;
            return /* selection */
            performClassification(null);
        }

        @android.annotation.WorkerThread
        public android.widget.SelectionActionModeHelper.SelectionResult suggestSelection() {
            mHot = true;
            trimText();
            final android.view.textclassifier.TextSelection selection;
            if (mContext.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.P) {
                final android.view.textclassifier.TextSelection.Request request = new android.view.textclassifier.TextSelection.Request.Builder(mTrimmedText, mRelativeStart, mRelativeEnd).setDefaultLocales(mDefaultLocales).setDarkLaunchAllowed(true).build();
                selection = mTextClassifier.get().suggestSelection(request);
            } else {
                // Use old APIs.
                selection = mTextClassifier.get().suggestSelection(mTrimmedText, mRelativeStart, mRelativeEnd, mDefaultLocales);
            }
            // Do not classify new selection boundaries if TextClassifier should be dark launched.
            if (!isDarkLaunchEnabled()) {
                mSelectionStart = java.lang.Math.max(0, selection.getSelectionStartIndex() + mTrimStart);
                mSelectionEnd = java.lang.Math.min(mText.length(), selection.getSelectionEndIndex() + mTrimStart);
            }
            return performClassification(selection);
        }

        public android.widget.SelectionActionModeHelper.SelectionResult getOriginalSelection() {
            return new android.widget.SelectionActionModeHelper.SelectionResult(mSelectionStart, mSelectionEnd, null, null);
        }

        /**
         * Maximum time (in milliseconds) to wait for a textclassifier result before timing out.
         */
        // TODO: Consider making this a ViewConfiguration.
        public int getTimeoutDuration() {
            if (mHot) {
                return 200;
            } else {
                // Return a slightly larger number than usual when the TextClassifier is first
                // initialized. Initialization would usually take longer than subsequent calls to
                // the TextClassifier. The impact of this on the UI is that we do not show the
                // selection handles or toolbar until after this timeout.
                return 500;
            }
        }

        private boolean isDarkLaunchEnabled() {
            return android.view.textclassifier.TextClassificationManager.getSettings(mContext).isModelDarkLaunchEnabled();
        }

        private android.widget.SelectionActionModeHelper.SelectionResult performClassification(@android.annotation.Nullable
        android.view.textclassifier.TextSelection selection) {
            if ((((!java.util.Objects.equals(mText, mLastClassificationText)) || (mSelectionStart != mLastClassificationSelectionStart)) || (mSelectionEnd != mLastClassificationSelectionEnd)) || (!java.util.Objects.equals(mDefaultLocales, mLastClassificationLocales))) {
                mLastClassificationText = mText;
                mLastClassificationSelectionStart = mSelectionStart;
                mLastClassificationSelectionEnd = mSelectionEnd;
                mLastClassificationLocales = mDefaultLocales;
                trimText();
                final android.view.textclassifier.TextClassification classification;
                if (android.text.util.Linkify.containsUnsupportedCharacters(mText)) {
                    // Do not show smart actions for text containing unsupported characters.
                    android.widget.android.util.EventLog.writeEvent(0x534e4554, "116321860", -1, "");
                    classification = android.view.textclassifier.TextClassification.EMPTY;
                } else
                    if (mContext.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.P) {
                        final android.view.textclassifier.TextClassification.Request request = new android.view.textclassifier.TextClassification.Request.Builder(mTrimmedText, mRelativeStart, mRelativeEnd).setDefaultLocales(mDefaultLocales).build();
                        classification = mTextClassifier.get().classifyText(request);
                    } else {
                        // Use old APIs.
                        classification = mTextClassifier.get().classifyText(mTrimmedText, mRelativeStart, mRelativeEnd, mDefaultLocales);
                    }

                mLastClassificationResult = new android.widget.SelectionActionModeHelper.SelectionResult(mSelectionStart, mSelectionEnd, classification, selection);
            }
            return mLastClassificationResult;
        }

        private void trimText() {
            mTrimStart = java.lang.Math.max(0, mSelectionStart - android.widget.SelectionActionModeHelper.TextClassificationHelper.TRIM_DELTA);
            final int referenceEnd = java.lang.Math.min(mText.length(), mSelectionEnd + android.widget.SelectionActionModeHelper.TextClassificationHelper.TRIM_DELTA);
            mTrimmedText = mText.subSequence(mTrimStart, referenceEnd);
            mRelativeStart = mSelectionStart - mTrimStart;
            mRelativeEnd = mSelectionEnd - mTrimStart;
        }
    }

    /**
     * Selection result.
     */
    private static final class SelectionResult {
        private final int mStart;

        private final int mEnd;

        @android.annotation.Nullable
        private final android.view.textclassifier.TextClassification mClassification;

        @android.annotation.Nullable
        private final android.view.textclassifier.TextSelection mSelection;

        SelectionResult(int start, int end, @android.annotation.Nullable
        android.view.textclassifier.TextClassification classification, @android.annotation.Nullable
        android.view.textclassifier.TextSelection selection) {
            mStart = start;
            mEnd = end;
            mClassification = classification;
            mSelection = selection;
        }
    }

    @android.view.textclassifier.SelectionEvent.ActionType
    private static int getActionType(int menuItemId) {
        switch (menuItemId) {
            case android.widget.TextView.ID_SELECT_ALL :
                return android.view.textclassifier.SelectionEvent.ACTION_SELECT_ALL;
            case android.widget.TextView.ID_CUT :
                return android.view.textclassifier.SelectionEvent.ACTION_CUT;
            case android.widget.TextView.ID_COPY :
                return android.view.textclassifier.SelectionEvent.ACTION_COPY;
            case android.widget.TextView.ID_PASTE :
                // fall through
            case android.widget.TextView.ID_PASTE_AS_PLAIN_TEXT :
                return android.view.textclassifier.SelectionEvent.ACTION_PASTE;
            case android.widget.TextView.ID_SHARE :
                return android.view.textclassifier.SelectionEvent.ACTION_SHARE;
            case android.widget.TextView.ID_ASSIST :
                return android.view.textclassifier.SelectionEvent.ACTION_SMART_SHARE;
            default :
                return android.view.textclassifier.SelectionEvent.ACTION_OTHER;
        }
    }

    private static java.lang.CharSequence getText(android.widget.TextView textView) {
        // Extracts the textView's text.
        // TODO: Investigate why/when TextView.getText() is null.
        final java.lang.CharSequence text = textView.getText();
        if (text != null) {
            return text;
        }
        return "";
    }
}

