/**
 * Copyright (C) 2007-2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package android.inputmethodservice;


/**
 * InputMethodService provides a standard implementation of an InputMethod,
 * which final implementations can derive from and customize.  See the
 * base class {@link AbstractInputMethodService} and the {@link InputMethod}
 * interface for more information on the basics of writing input methods.
 *
 * <p>In addition to the normal Service lifecycle methods, this class
 * introduces some new specific callbacks that most subclasses will want
 * to make use of:</p>
 * <ul>
 * <li> {@link #onInitializeInterface()} for user-interface initialization,
 * in particular to deal with configuration changes while the service is
 * running.
 * <li> {@link #onBindInput} to find out about switching to a new client.
 * <li> {@link #onStartInput} to deal with an input session starting with
 * the client.
 * <li> {@link #onCreateInputView()}, {@link #onCreateCandidatesView()},
 * and {@link #onCreateExtractTextView()} for non-demand generation of the UI.
 * <li> {@link #onStartInputView(EditorInfo, boolean)} to deal with input
 * starting within the input area of the IME.
 * </ul>
 *
 * <p>An input method has significant discretion in how it goes about its
 * work: the {@link android.inputmethodservice.InputMethodService} provides
 * a basic framework for standard UI elements (input view, candidates view,
 * and running in fullscreen mode), but it is up to a particular implementor
 * to decide how to use them.  For example, one input method could implement
 * an input area with a keyboard, another could allow the user to draw text,
 * while a third could have no input area (and thus not be visible to the
 * user) but instead listen to audio and perform text to speech conversion.</p>
 *
 * <p>In the implementation provided here, all of these elements are placed
 * together in a single window managed by the InputMethodService.  It will
 * execute callbacks as it needs information about them, and provides APIs for
 * programmatic control over them.  They layout of these elements is explicitly
 * defined:</p>
 *
 * <ul>
 * <li>The soft input view, if available, is placed at the bottom of the
 * screen.
 * <li>The candidates view, if currently shown, is placed above the soft
 * input view.
 * <li>If not running fullscreen, the application is moved or resized to be
 * above these views; if running fullscreen, the window will completely cover
 * the application and its top part will contain the extract text of what is
 * currently being edited by the application.
 * </ul>
 *
 *
 * <a name="SoftInputView"></a>
 * <h3>Soft Input View</h3>
 *
 * <p>Central to most input methods is the soft input view.  This is where most
 * user interaction occurs: pressing on soft keys, drawing characters, or
 * however else your input method wants to generate text.  Most implementations
 * will simply have their own view doing all of this work, and return a new
 * instance of it when {@link #onCreateInputView()} is called.  At that point,
 * as long as the input view is visible, you will see user interaction in
 * that view and can call back on the InputMethodService to interact with the
 * application as appropriate.</p>
 *
 * <p>There are some situations where you want to decide whether or not your
 * soft input view should be shown to the user.  This is done by implementing
 * the {@link #onEvaluateInputViewShown()} to return true or false based on
 * whether it should be shown in the current environment.  If any of your
 * state has changed that may impact this, call
 * {@link #updateInputViewShown()} to have it re-evaluated.  The default
 * implementation always shows the input view unless there is a hard
 * keyboard available, which is the appropriate behavior for most input
 * methods.</p>
 *
 *
 * <a name="CandidatesView"></a>
 * <h3>Candidates View</h3>
 *
 * <p>Often while the user is generating raw text, an input method wants to
 * provide them with a list of possible interpretations of that text that can
 * be selected for use.  This is accomplished with the candidates view, and
 * like the soft input view you implement {@link #onCreateCandidatesView()}
 * to instantiate your own view implementing your candidates UI.</p>
 *
 * <p>Management of the candidates view is a little different than the input
 * view, because the candidates view tends to be more transient, being shown
 * only when there are possible candidates for the current text being entered
 * by the user.  To control whether the candidates view is shown, you use
 * {@link #setCandidatesViewShown(boolean)}.  Note that because the candidate
 * view tends to be shown and hidden a lot, it does not impact the application
 * UI in the same way as the soft input view: it will never cause application
 * windows to resize, only cause them to be panned if needed for the user to
 * see the current focus.</p>
 *
 *
 * <a name="FullscreenMode"></a>
 * <h3>Fullscreen Mode</h3>
 *
 * <p>Sometimes your input method UI is too large to integrate with the
 * application UI, so you just want to take over the screen.  This is
 * accomplished by switching to full-screen mode, causing the input method
 * window to fill the entire screen and add its own "extracted text" editor
 * showing the user the text that is being typed.  Unlike the other UI elements,
 * there is a standard implementation for the extract editor that you should
 * not need to change.  The editor is placed at the top of the IME, above the
 * input and candidates views.</p>
 *
 * <p>Similar to the input view, you control whether the IME is running in
 * fullscreen mode by implementing {@link #onEvaluateFullscreenMode()}
 * to return true or false based on
 * whether it should be fullscreen in the current environment.  If any of your
 * state has changed that may impact this, call
 * {@link #updateFullscreenMode()} to have it re-evaluated.  The default
 * implementation selects fullscreen mode when the screen is in a landscape
 * orientation, which is appropriate behavior for most input methods that have
 * a significant input area.</p>
 *
 * <p>When in fullscreen mode, you have some special requirements because the
 * user can not see the application UI.  In particular, you should implement
 * {@link #onDisplayCompletions(CompletionInfo[])} to show completions
 * generated by your application, typically in your candidates view like you
 * would normally show candidates.
 *
 *
 * <a name="GeneratingText"></a>
 * <h3>Generating Text</h3>
 *
 * <p>The key part of an IME is of course generating text for the application.
 * This is done through calls to the
 * {@link android.view.inputmethod.InputConnection} interface to the
 * application, which can be retrieved from {@link #getCurrentInputConnection()}.
 * This interface allows you to generate raw key events or, if the target
 * supports it, directly edit in strings of candidates and committed text.</p>
 *
 * <p>Information about what the target is expected and supports can be found
 * through the {@link android.view.inputmethod.EditorInfo} class, which is
 * retrieved with {@link #getCurrentInputEditorInfo()} method.  The most
 * important part of this is {@link android.view.inputmethod.EditorInfo#inputType
 * EditorInfo.inputType}; in particular, if this is
 * {@link android.view.inputmethod.EditorInfo#TYPE_NULL EditorInfo.TYPE_NULL},
 * then the target does not support complex edits and you need to only deliver
 * raw key events to it.  An input method will also want to look at other
 * values here, to for example detect password mode, auto complete text views,
 * phone number entry, etc.</p>
 *
 * <p>When the user switches between input targets, you will receive calls to
 * {@link #onFinishInput()} and {@link #onStartInput(EditorInfo, boolean)}.
 * You can use these to reset and initialize your input state for the current
 * target.  For example, you will often want to clear any input state, and
 * update a soft keyboard to be appropriate for the new inputType.</p>
 *
 * @unknown ref android.R.styleable#InputMethodService_imeFullscreenBackground
 * @unknown ref android.R.styleable#InputMethodService_imeExtractEnterAnimation
 * @unknown ref android.R.styleable#InputMethodService_imeExtractExitAnimation
 */
public class InputMethodService extends android.inputmethodservice.AbstractInputMethodService {
    static final java.lang.String TAG = "InputMethodService";

    static final boolean DEBUG = false;

    /**
     * The back button will close the input window.
     */
    public static final int BACK_DISPOSITION_DEFAULT = 0;// based on window


    /**
     * This input method will not consume the back key.
     */
    public static final int BACK_DISPOSITION_WILL_NOT_DISMISS = 1;// back


    /**
     * This input method will consume the back key.
     */
    public static final int BACK_DISPOSITION_WILL_DISMISS = 2;// down


    /**
     *
     *
     * @unknown The IME is active.  It may or may not be visible.
     */
    public static final int IME_ACTIVE = 0x1;

    /**
     *
     *
     * @unknown The IME is visible.
     */
    public static final int IME_VISIBLE = 0x2;

    android.view.inputmethod.InputMethodManager mImm;

    int mTheme = 0;

    boolean mHardwareAccelerated = false;

    android.view.LayoutInflater mInflater;

    android.content.res.TypedArray mThemeAttrs;

    android.view.View mRootView;

    android.inputmethodservice.SoftInputWindow mWindow;

    boolean mInitialized;

    boolean mWindowCreated;

    boolean mWindowAdded;

    boolean mWindowVisible;

    boolean mWindowWasVisible;

    boolean mInShowWindow;

    android.view.ViewGroup mFullscreenArea;

    android.widget.FrameLayout mExtractFrame;

    android.widget.FrameLayout mCandidatesFrame;

    android.widget.FrameLayout mInputFrame;

    android.os.IBinder mToken;

    android.view.inputmethod.InputBinding mInputBinding;

    android.view.inputmethod.InputConnection mInputConnection;

    boolean mInputStarted;

    boolean mInputViewStarted;

    boolean mCandidatesViewStarted;

    android.view.inputmethod.InputConnection mStartedInputConnection;

    android.view.inputmethod.EditorInfo mInputEditorInfo;

    int mShowInputFlags;

    boolean mShowInputRequested;

    boolean mLastShowInputRequested;

    int mCandidatesVisibility;

    android.view.inputmethod.CompletionInfo[] mCurCompletions;

    boolean mFullscreenApplied;

    boolean mIsFullscreen;

    android.view.View mExtractView;

    boolean mExtractViewHidden;

    android.inputmethodservice.ExtractEditText mExtractEditText;

    android.view.ViewGroup mExtractAccessories;

    android.view.View mExtractAction;

    android.view.inputmethod.ExtractedText mExtractedText;

    int mExtractedToken;

    android.view.View mInputView;

    boolean mIsInputViewShown;

    int mStatusIcon;

    int mBackDisposition;

    /**
     * {@code true} when the previous IME had non-empty inset at the bottom of the screen and we
     * have not shown our own window yet.  In this situation, the previous inset continues to be
     * shown as an empty region until it is explicitly updated. Basically we can trigger the update
     * by calling 1) {@code mWindow.show()} or 2) {@link #clearInsetOfPreviousIme()}.
     */
    boolean mShouldClearInsetOfPreviousIme;

    final android.inputmethodservice.InputMethodService.Insets mTmpInsets = new android.inputmethodservice.InputMethodService.Insets();

    final int[] mTmpLocation = new int[2];

    final android.view.ViewTreeObserver.OnComputeInternalInsetsListener mInsetsComputer = new android.view.ViewTreeObserver.OnComputeInternalInsetsListener() {
        public void onComputeInternalInsets(android.view.ViewTreeObserver.InternalInsetsInfo info) {
            if (isExtractViewShown()) {
                // In true fullscreen mode, we just say the window isn't covering
                // any content so we don't impact whatever is behind.
                android.view.View decor = getWindow().getWindow().getDecorView();
                info.contentInsets.top = info.visibleInsets.top = decor.getHeight();
                info.touchableRegion.setEmpty();
                info.setTouchableInsets(android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_FRAME);
            } else {
                onComputeInsets(mTmpInsets);
                info.contentInsets.top = mTmpInsets.contentTopInsets;
                info.visibleInsets.top = mTmpInsets.visibleTopInsets;
                info.touchableRegion.set(mTmpInsets.touchableRegion);
                info.setTouchableInsets(mTmpInsets.touchableInsets);
            }
        }
    };

    final android.view.View.OnClickListener mActionClickListener = new android.view.View.OnClickListener() {
        public void onClick(android.view.View v) {
            final android.view.inputmethod.EditorInfo ei = getCurrentInputEditorInfo();
            final android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
            if ((ei != null) && (ic != null)) {
                if (ei.actionId != 0) {
                    ic.performEditorAction(ei.actionId);
                } else
                    if ((ei.imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION) != android.view.inputmethod.EditorInfo.IME_ACTION_NONE) {
                        ic.performEditorAction(ei.imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION);
                    }

            }
        }
    };

    /**
     * Concrete implementation of
     * {@link AbstractInputMethodService.AbstractInputMethodImpl} that provides
     * all of the standard behavior for an input method.
     */
    public class InputMethodImpl extends android.inputmethodservice.AbstractInputMethodService.AbstractInputMethodImpl {
        /**
         * Take care of attaching the given window token provided by the system.
         */
        public void attachToken(android.os.IBinder token) {
            if (mToken == null) {
                mToken = token;
                mWindow.setToken(token);
            }
        }

        /**
         * Handle a new input binding, calling
         * {@link InputMethodService#onBindInput InputMethodService.onBindInput()}
         * when done.
         */
        public void bindInput(android.view.inputmethod.InputBinding binding) {
            mInputBinding = binding;
            mInputConnection = binding.getConnection();
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, (("bindInput(): binding=" + binding) + " ic=") + mInputConnection);

            android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
            if (ic != null)
                ic.reportFullscreenMode(mIsFullscreen);

            initialize();
            onBindInput();
        }

        /**
         * Clear the current input binding.
         */
        public void unbindInput() {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, (("unbindInput(): binding=" + mInputBinding) + " ic=") + mInputConnection);

            onUnbindInput();
            mInputBinding = null;
            mInputConnection = null;
        }

        public void startInput(android.view.inputmethod.InputConnection ic, android.view.inputmethod.EditorInfo attribute) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "startInput(): editor=" + attribute);

            doStartInput(ic, attribute, false);
        }

        public void restartInput(android.view.inputmethod.InputConnection ic, android.view.inputmethod.EditorInfo attribute) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "restartInput(): editor=" + attribute);

            doStartInput(ic, attribute, true);
        }

        /**
         * Handle a request by the system to hide the soft input area.
         */
        public void hideSoftInput(int flags, android.os.ResultReceiver resultReceiver) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "hideSoftInput()");

            boolean wasVis = isInputViewShown();
            mShowInputFlags = 0;
            mShowInputRequested = false;
            doHideWindow();
            clearInsetOfPreviousIme();
            if (resultReceiver != null) {
                resultReceiver.send(wasVis != isInputViewShown() ? android.view.inputmethod.InputMethodManager.RESULT_HIDDEN : wasVis ? android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN : android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_HIDDEN, null);
            }
        }

        /**
         * Handle a request by the system to show the soft input area.
         */
        public void showSoftInput(int flags, android.os.ResultReceiver resultReceiver) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "showSoftInput()");

            boolean wasVis = isInputViewShown();
            if (dispatchOnShowInputRequested(flags, false)) {
                try {
                    showWindow(true);
                } catch (android.view.WindowManager.BadTokenException e) {
                    // We have ignored BadTokenException here since Jelly Bean MR-2 (API Level 18).
                    // We could ignore BadTokenException in InputMethodService#showWindow() instead,
                    // but it may break assumptions for those who override #showWindow() that we can
                    // detect errors in #showWindow() by checking BadTokenException.
                    // TODO: Investigate its feasibility.  Update JavaDoc of #showWindow() of
                    // whether it's OK to override #showWindow() or not.
                }
            }
            clearInsetOfPreviousIme();
            // If user uses hard keyboard, IME button should always be shown.
            boolean showing = isInputViewShown();
            mImm.setImeWindowStatus(mToken, android.inputmethodservice.InputMethodService.IME_ACTIVE | (showing ? android.inputmethodservice.InputMethodService.IME_VISIBLE : 0), mBackDisposition);
            if (resultReceiver != null) {
                resultReceiver.send(wasVis != isInputViewShown() ? android.view.inputmethod.InputMethodManager.RESULT_SHOWN : wasVis ? android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_SHOWN : android.view.inputmethod.InputMethodManager.RESULT_UNCHANGED_HIDDEN, null);
            }
        }

        public void changeInputMethodSubtype(android.view.inputmethod.InputMethodSubtype subtype) {
            onCurrentInputMethodSubtypeChanged(subtype);
        }
    }

    /**
     * Concrete implementation of
     * {@link AbstractInputMethodService.AbstractInputMethodSessionImpl} that provides
     * all of the standard behavior for an input method session.
     */
    public class InputMethodSessionImpl extends android.inputmethodservice.AbstractInputMethodService.AbstractInputMethodSessionImpl {
        public void finishInput() {
            if (!isEnabled()) {
                return;
            }
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "finishInput() in " + this);

            doFinishInput();
        }

        /**
         * Call {@link InputMethodService#onDisplayCompletions
         * InputMethodService.onDisplayCompletions()}.
         */
        public void displayCompletions(android.view.inputmethod.CompletionInfo[] completions) {
            if (!isEnabled()) {
                return;
            }
            mCurCompletions = completions;
            onDisplayCompletions(completions);
        }

        /**
         * Call {@link InputMethodService#onUpdateExtractedText
         * InputMethodService.onUpdateExtractedText()}.
         */
        public void updateExtractedText(int token, android.view.inputmethod.ExtractedText text) {
            if (!isEnabled()) {
                return;
            }
            onUpdateExtractedText(token, text);
        }

        /**
         * Call {@link InputMethodService#onUpdateSelection
         * InputMethodService.onUpdateSelection()}.
         */
        public void updateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
            if (!isEnabled()) {
                return;
            }
            android.inputmethodservice.InputMethodService.this.onUpdateSelection(oldSelStart, oldSelEnd, newSelStart, newSelEnd, candidatesStart, candidatesEnd);
        }

        @java.lang.Override
        public void viewClicked(boolean focusChanged) {
            if (!isEnabled()) {
                return;
            }
            android.inputmethodservice.InputMethodService.this.onViewClicked(focusChanged);
        }

        /**
         * Call {@link InputMethodService#onUpdateCursor
         * InputMethodService.onUpdateCursor()}.
         */
        public void updateCursor(android.graphics.Rect newCursor) {
            if (!isEnabled()) {
                return;
            }
            android.inputmethodservice.InputMethodService.this.onUpdateCursor(newCursor);
        }

        /**
         * Call {@link InputMethodService#onAppPrivateCommand
         * InputMethodService.onAppPrivateCommand()}.
         */
        public void appPrivateCommand(java.lang.String action, android.os.Bundle data) {
            if (!isEnabled()) {
                return;
            }
            android.inputmethodservice.InputMethodService.this.onAppPrivateCommand(action, data);
        }

        /**
         *
         */
        public void toggleSoftInput(int showFlags, int hideFlags) {
            android.inputmethodservice.InputMethodService.this.onToggleSoftInput(showFlags, hideFlags);
        }

        /**
         * Call {@link InputMethodService#onUpdateCursorAnchorInfo
         * InputMethodService.onUpdateCursorAnchorInfo()}.
         */
        public void updateCursorAnchorInfo(android.view.inputmethod.CursorAnchorInfo info) {
            if (!isEnabled()) {
                return;
            }
            android.inputmethodservice.InputMethodService.this.onUpdateCursorAnchorInfo(info);
        }
    }

    /**
     * Information about where interesting parts of the input method UI appear.
     */
    public static final class Insets {
        /**
         * This is the top part of the UI that is the main content.  It is
         * used to determine the basic space needed, to resize/pan the
         * application behind.  It is assumed that this inset does not
         * change very much, since any change will cause a full resize/pan
         * of the application behind.  This value is relative to the top edge
         * of the input method window.
         */
        public int contentTopInsets;

        /**
         * This is the top part of the UI that is visibly covering the
         * application behind it.  This provides finer-grained control over
         * visibility, allowing you to change it relatively frequently (such
         * as hiding or showing candidates) without disrupting the underlying
         * UI too much.  For example, this will never resize the application
         * UI, will only pan if needed to make the current focus visible, and
         * will not aggressively move the pan position when this changes unless
         * needed to make the focus visible.  This value is relative to the top edge
         * of the input method window.
         */
        public int visibleTopInsets;

        /**
         * This is the region of the UI that is touchable.  It is used when
         * {@link #touchableInsets} is set to {@link #TOUCHABLE_INSETS_REGION}.
         * The region should be specified relative to the origin of the window frame.
         */
        public final android.graphics.Region touchableRegion = new android.graphics.Region();

        /**
         * Option for {@link #touchableInsets}: the entire window frame
         * can be touched.
         */
        public static final int TOUCHABLE_INSETS_FRAME = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_FRAME;

        /**
         * Option for {@link #touchableInsets}: the area inside of
         * the content insets can be touched.
         */
        public static final int TOUCHABLE_INSETS_CONTENT = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_CONTENT;

        /**
         * Option for {@link #touchableInsets}: the area inside of
         * the visible insets can be touched.
         */
        public static final int TOUCHABLE_INSETS_VISIBLE = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_VISIBLE;

        /**
         * Option for {@link #touchableInsets}: the region specified by
         * {@link #touchableRegion} can be touched.
         */
        public static final int TOUCHABLE_INSETS_REGION = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_REGION;

        /**
         * Determine which area of the window is touchable by the user.  May
         * be one of: {@link #TOUCHABLE_INSETS_FRAME},
         * {@link #TOUCHABLE_INSETS_CONTENT}, {@link #TOUCHABLE_INSETS_VISIBLE},
         * or {@link #TOUCHABLE_INSETS_REGION}.
         */
        public int touchableInsets;
    }

    /**
     * A {@link ContentObserver} to monitor {@link Settings.Secure#SHOW_IME_WITH_HARD_KEYBOARD}.
     *
     * <p>Note that {@link Settings.Secure#SHOW_IME_WITH_HARD_KEYBOARD} is not a public API.
     * Basically this functionality still needs to be considered as implementation details.</p>
     */
    @android.annotation.MainThread
    private static final class SettingsObserver extends android.database.ContentObserver {
        @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
        @android.annotation.IntDef({ android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.UNKNOWN, android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.FALSE, android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.TRUE })
        private @interface ShowImeWithHardKeyboardType {
            int UNKNOWN = 0;

            int FALSE = 1;

            int TRUE = 2;
        }

        @android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType
        private int mShowImeWithHardKeyboard = android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.UNKNOWN;

        private final android.inputmethodservice.InputMethodService mService;

        private SettingsObserver(android.inputmethodservice.InputMethodService service) {
            super(new android.os.Handler(service.getMainLooper()));
            mService = service;
        }

        /**
         * A factory method that internally enforces two-phase initialization to make sure that the
         * object reference will not be escaped until the object is properly constructed.
         *
         * <p>NOTE: Currently {@link SettingsObserver} is accessed only from main thread.  Hence
         * this enforcement of two-phase initialization may be unnecessary at the moment.</p>
         *
         * @param service
         * 		{@link InputMethodService} that needs to receive the callback.
         * @return {@link SettingsObserver} that is already registered to
        {@link android.content.ContentResolver}. The caller must call
        {@link SettingsObserver#unregister()}.
         */
        public static android.inputmethodservice.InputMethodService.SettingsObserver createAndRegister(android.inputmethodservice.InputMethodService service) {
            final android.inputmethodservice.InputMethodService.SettingsObserver observer = new android.inputmethodservice.InputMethodService.SettingsObserver(service);
            // The observer is properly constructed. Let's start accepting the event.
            service.getContentResolver().registerContentObserver(android.provider.Settings.Secure.getUriFor(android.provider.Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD), false, observer);
            return observer;
        }

        void unregister() {
            mService.getContentResolver().unregisterContentObserver(this);
        }

        private boolean shouldShowImeWithHardKeyboard() {
            // Lazily initialize as needed.
            if (mShowImeWithHardKeyboard == android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.UNKNOWN) {
                mShowImeWithHardKeyboard = (android.provider.Settings.Secure.getInt(mService.getContentResolver(), android.provider.Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD, 0) != 0) ? android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.TRUE : android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.FALSE;
            }
            switch (mShowImeWithHardKeyboard) {
                case android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.TRUE :
                    return true;
                case android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.FALSE :
                    return false;
                default :
                    android.util.Log.e(android.inputmethodservice.InputMethodService.TAG, "Unexpected mShowImeWithHardKeyboard=" + mShowImeWithHardKeyboard);
                    return false;
            }
        }

        @java.lang.Override
        public void onChange(boolean selfChange, android.net.Uri uri) {
            final android.net.Uri showImeWithHardKeyboardUri = android.provider.Settings.Secure.getUriFor(android.provider.Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD);
            if (showImeWithHardKeyboardUri.equals(uri)) {
                mShowImeWithHardKeyboard = (android.provider.Settings.Secure.getInt(mService.getContentResolver(), android.provider.Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD, 0) != 0) ? android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.TRUE : android.inputmethodservice.InputMethodService.SettingsObserver.ShowImeWithHardKeyboardType.FALSE;
                // In Android M and prior, state change of
                // Settings.Secure.SHOW_IME_WITH_HARD_KEYBOARD has triggered
                // #onConfigurationChanged().  For compatibility reasons, we reset the internal
                // state as if configuration was changed.
                mService.resetStateForNewConfiguration();
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ("SettingsObserver{mShowImeWithHardKeyboard=" + mShowImeWithHardKeyboard) + "}";
        }
    }

    private android.inputmethodservice.InputMethodService.SettingsObserver mSettingsObserver;

    /**
     * You can call this to customize the theme used by your IME's window.
     * This theme should typically be one that derives from
     * {@link android.R.style#Theme_InputMethod}, which is the default theme
     * you will get.  This must be set before {@link #onCreate}, so you
     * will typically call it in your constructor with the resource ID
     * of your custom theme.
     */
    @java.lang.Override
    public void setTheme(int theme) {
        if (mWindow != null) {
            throw new java.lang.IllegalStateException("Must be called before onCreate()");
        }
        mTheme = theme;
    }

    /**
     * You can call this to try to enable hardware accelerated drawing for
     * your IME. This must be set before {@link #onCreate}, so you
     * will typically call it in your constructor.  It is not always possible
     * to use hardware accelerated drawing in an IME (for example on low-end
     * devices that do not have the resources to support this), so the call
     * returns true if it succeeds otherwise false if you will need to draw
     * in software.  You must be able to handle either case.
     *
     * @deprecated Starting in API 21, hardware acceleration is always enabled
    on capable devices.
     */
    public boolean enableHardwareAcceleration() {
        if (mWindow != null) {
            throw new java.lang.IllegalStateException("Must be called before onCreate()");
        }
        if (android.app.ActivityManager.isHighEndGfx()) {
            mHardwareAccelerated = true;
            return true;
        }
        return false;
    }

    @java.lang.Override
    public void onCreate() {
        mTheme = android.content.res.Resources.selectSystemTheme(mTheme, getApplicationInfo().targetSdkVersion, android.R.style.Theme_InputMethod, android.R.style.Theme_Holo_InputMethod, android.R.style.Theme_DeviceDefault_InputMethod, android.R.style.Theme_DeviceDefault_InputMethod);
        super.setTheme(mTheme);
        super.onCreate();
        mImm = ((android.view.inputmethod.InputMethodManager) (getSystemService(android.content.Context.INPUT_METHOD_SERVICE)));
        mSettingsObserver = android.inputmethodservice.InputMethodService.SettingsObserver.createAndRegister(this);
        // If the previous IME has occupied non-empty inset in the screen, we need to decide whether
        // we continue to use the same size of the inset or update it
        mShouldClearInsetOfPreviousIme = mImm.getInputMethodWindowVisibleHeight() > 0;
        mInflater = ((android.view.LayoutInflater) (getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        mWindow = new android.inputmethodservice.SoftInputWindow(this, "InputMethod", mTheme, null, null, mDispatcherState, android.view.WindowManager.LayoutParams.TYPE_INPUT_METHOD, android.view.Gravity.BOTTOM, false);
        if (mHardwareAccelerated) {
            mWindow.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
        }
        initViews();
        mWindow.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    /**
     * This is a hook that subclasses can use to perform initialization of
     * their interface.  It is called for you prior to any of your UI objects
     * being created, both after the service is first created and after a
     * configuration change happens.
     */
    public void onInitializeInterface() {
        // Intentionally empty
    }

    void initialize() {
        if (!mInitialized) {
            mInitialized = true;
            onInitializeInterface();
        }
    }

    void initViews() {
        mInitialized = false;
        mWindowCreated = false;
        mShowInputRequested = false;
        mShowInputFlags = 0;
        mThemeAttrs = obtainStyledAttributes(android.inputmethodservice.android.R.styleable);
        mRootView = mInflater.inflate(com.android.internal.R.layout.input_method, null);
        mRootView.setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mWindow.setContentView(mRootView);
        mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(mInsetsComputer);
        mRootView.getViewTreeObserver().addOnComputeInternalInsetsListener(mInsetsComputer);
        if (android.provider.Settings.Global.getInt(getContentResolver(), android.provider.Settings.Global.FANCY_IME_ANIMATIONS, 0) != 0) {
            mWindow.getWindow().setWindowAnimations(com.android.internal.R.style.Animation_InputMethodFancy);
        }
        mFullscreenArea = ((android.view.ViewGroup) (mRootView.findViewById(com.android.internal.R.id.fullscreenArea)));
        mExtractViewHidden = false;
        mExtractFrame = ((android.widget.FrameLayout) (mRootView.findViewById(android.R.id.extractArea)));
        mExtractView = null;
        mExtractEditText = null;
        mExtractAccessories = null;
        mExtractAction = null;
        mFullscreenApplied = false;
        mCandidatesFrame = ((android.widget.FrameLayout) (mRootView.findViewById(android.R.id.candidatesArea)));
        mInputFrame = ((android.widget.FrameLayout) (mRootView.findViewById(android.R.id.inputArea)));
        mInputView = null;
        mIsInputViewShown = false;
        mExtractFrame.setVisibility(android.view.View.GONE);
        mCandidatesVisibility = getCandidatesHiddenVisibility();
        mCandidatesFrame.setVisibility(mCandidatesVisibility);
        mInputFrame.setVisibility(android.view.View.GONE);
    }

    @java.lang.Override
    public void onDestroy() {
        super.onDestroy();
        mRootView.getViewTreeObserver().removeOnComputeInternalInsetsListener(mInsetsComputer);
        doFinishInput();
        if (mWindowAdded) {
            // Disable exit animation for the current IME window
            // to avoid the race condition between the exit and enter animations
            // when the current IME is being switched to another one.
            mWindow.getWindow().setWindowAnimations(0);
            mWindow.dismiss();
        }
        if (mSettingsObserver != null) {
            mSettingsObserver.unregister();
            mSettingsObserver = null;
        }
    }

    /**
     * Take care of handling configuration changes.  Subclasses of
     * InputMethodService generally don't need to deal directly with
     * this on their own; the standard implementation here takes care of
     * regenerating the input method UI as a result of the configuration
     * change, so you can rely on your {@link #onCreateInputView} and
     * other methods being called as appropriate due to a configuration change.
     *
     * <p>When a configuration change does happen,
     * {@link #onInitializeInterface()} is guaranteed to be called the next
     * time prior to any of the other input or UI creation callbacks.  The
     * following will be called immediately depending if appropriate for current
     * state: {@link #onStartInput} if input is active, and
     * {@link #onCreateInputView} and {@link #onStartInputView} and related
     * appropriate functions if the UI is displayed.
     */
    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resetStateForNewConfiguration();
    }

    private void resetStateForNewConfiguration() {
        boolean visible = mWindowVisible;
        int showFlags = mShowInputFlags;
        boolean showingInput = mShowInputRequested;
        android.view.inputmethod.CompletionInfo[] completions = mCurCompletions;
        initViews();
        mInputViewStarted = false;
        mCandidatesViewStarted = false;
        if (mInputStarted) {
            doStartInput(getCurrentInputConnection(), getCurrentInputEditorInfo(), true);
        }
        if (visible) {
            if (showingInput) {
                // If we were last showing the soft keyboard, try to do so again.
                if (dispatchOnShowInputRequested(showFlags, true)) {
                    showWindow(true);
                    if (completions != null) {
                        mCurCompletions = completions;
                        onDisplayCompletions(completions);
                    }
                } else {
                    doHideWindow();
                }
            } else
                if (mCandidatesVisibility == android.view.View.VISIBLE) {
                    // If the candidates are currently visible, make sure the
                    // window is shown for them.
                    showWindow(false);
                } else {
                    // Otherwise hide the window.
                    doHideWindow();
                }

            // If user uses hard keyboard, IME button should always be shown.
            boolean showing = onEvaluateInputViewShown();
            mImm.setImeWindowStatus(mToken, android.inputmethodservice.InputMethodService.IME_ACTIVE | (showing ? android.inputmethodservice.InputMethodService.IME_VISIBLE : 0), mBackDisposition);
        }
    }

    /**
     * Implement to return our standard {@link InputMethodImpl}.  Subclasses
     * can override to provide their own customized version.
     */
    @java.lang.Override
    public android.inputmethodservice.AbstractInputMethodService.AbstractInputMethodImpl onCreateInputMethodInterface() {
        return new android.inputmethodservice.InputMethodService.InputMethodImpl();
    }

    /**
     * Implement to return our standard {@link InputMethodSessionImpl}.  Subclasses
     * can override to provide their own customized version.
     */
    @java.lang.Override
    public android.inputmethodservice.AbstractInputMethodService.AbstractInputMethodSessionImpl onCreateInputMethodSessionInterface() {
        return new android.inputmethodservice.InputMethodService.InputMethodSessionImpl();
    }

    public android.view.LayoutInflater getLayoutInflater() {
        return mInflater;
    }

    public android.app.Dialog getWindow() {
        return mWindow;
    }

    public void setBackDisposition(int disposition) {
        mBackDisposition = disposition;
    }

    public int getBackDisposition() {
        return mBackDisposition;
    }

    /**
     * Return the maximum width, in pixels, available the input method.
     * Input methods are positioned at the bottom of the screen and, unless
     * running in fullscreen, will generally want to be as short as possible
     * so should compute their height based on their contents.  However, they
     * can stretch as much as needed horizontally.  The function returns to
     * you the maximum amount of space available horizontally, which you can
     * use if needed for UI placement.
     *
     * <p>In many cases this is not needed, you can just rely on the normal
     * view layout mechanisms to position your views within the full horizontal
     * space given to the input method.
     *
     * <p>Note that this value can change dynamically, in particular when the
     * screen orientation changes.
     */
    public int getMaxWidth() {
        android.view.WindowManager wm = ((android.view.WindowManager) (getSystemService(android.content.Context.WINDOW_SERVICE)));
        return wm.getDefaultDisplay().getWidth();
    }

    /**
     * Return the currently active InputBinding for the input method, or
     * null if there is none.
     */
    public android.view.inputmethod.InputBinding getCurrentInputBinding() {
        return mInputBinding;
    }

    /**
     * Retrieve the currently active InputConnection that is bound to
     * the input method, or null if there is none.
     */
    public android.view.inputmethod.InputConnection getCurrentInputConnection() {
        android.view.inputmethod.InputConnection ic = mStartedInputConnection;
        if (ic != null) {
            return ic;
        }
        return mInputConnection;
    }

    public boolean getCurrentInputStarted() {
        return mInputStarted;
    }

    public android.view.inputmethod.EditorInfo getCurrentInputEditorInfo() {
        return mInputEditorInfo;
    }

    /**
     * Re-evaluate whether the input method should be running in fullscreen
     * mode, and update its UI if this has changed since the last time it
     * was evaluated.  This will call {@link #onEvaluateFullscreenMode()} to
     * determine whether it should currently run in fullscreen mode.  You
     * can use {@link #isFullscreenMode()} to determine if the input method
     * is currently running in fullscreen mode.
     */
    public void updateFullscreenMode() {
        boolean isFullscreen = mShowInputRequested && onEvaluateFullscreenMode();
        boolean changed = mLastShowInputRequested != mShowInputRequested;
        if ((mIsFullscreen != isFullscreen) || (!mFullscreenApplied)) {
            changed = true;
            mIsFullscreen = isFullscreen;
            android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
            if (ic != null)
                ic.reportFullscreenMode(isFullscreen);

            mFullscreenApplied = true;
            initialize();
            android.widget.LinearLayout.LayoutParams lp = ((android.widget.LinearLayout.LayoutParams) (mFullscreenArea.getLayoutParams()));
            if (isFullscreen) {
                mFullscreenArea.setBackgroundDrawable(mThemeAttrs.getDrawable(com.android.internal.R.styleable.InputMethodService_imeFullscreenBackground));
                lp.height = 0;
                lp.weight = 1;
            } else {
                mFullscreenArea.setBackgroundDrawable(null);
                lp.height = android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;
                lp.weight = 0;
            }
            ((android.view.ViewGroup) (mFullscreenArea.getParent())).updateViewLayout(mFullscreenArea, lp);
            if (isFullscreen) {
                if (mExtractView == null) {
                    android.view.View v = onCreateExtractTextView();
                    if (v != null) {
                        setExtractView(v);
                    }
                }
                startExtractingText(false);
            }
            updateExtractFrameVisibility();
        }
        if (changed) {
            onConfigureWindow(mWindow.getWindow(), isFullscreen, !mShowInputRequested);
            mLastShowInputRequested = mShowInputRequested;
        }
    }

    /**
     * Update the given window's parameters for the given mode.  This is called
     * when the window is first displayed and each time the fullscreen or
     * candidates only mode changes.
     *
     * <p>The default implementation makes the layout for the window
     * MATCH_PARENT x MATCH_PARENT when in fullscreen mode, and
     * MATCH_PARENT x WRAP_CONTENT when in non-fullscreen mode.
     *
     * @param win
     * 		The input method's window.
     * @param isFullscreen
     * 		If true, the window is running in fullscreen mode
     * 		and intended to cover the entire application display.
     * @param isCandidatesOnly
     * 		If true, the window is only showing the
     * 		candidates view and none of the rest of its UI.  This is mutually
     * 		exclusive with fullscreen mode.
     */
    public void onConfigureWindow(android.view.Window win, boolean isFullscreen, boolean isCandidatesOnly) {
        final int currentHeight = mWindow.getWindow().getAttributes().height;
        final int newHeight = (isFullscreen) ? android.view.ViewGroup.LayoutParams.MATCH_PARENT : android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
        if (mIsInputViewShown && (currentHeight != newHeight)) {
            android.util.Log.w(android.inputmethodservice.InputMethodService.TAG, (("Window size has been changed. This may cause jankiness of resizing window: " + currentHeight) + " -> ") + newHeight);
        }
        mWindow.getWindow().setLayout(android.view.ViewGroup.LayoutParams.MATCH_PARENT, newHeight);
    }

    /**
     * Return whether the input method is <em>currently</em> running in
     * fullscreen mode.  This is the mode that was last determined and
     * applied by {@link #updateFullscreenMode()}.
     */
    public boolean isFullscreenMode() {
        return mIsFullscreen;
    }

    /**
     * Override this to control when the input method should run in
     * fullscreen mode.  The default implementation runs in fullsceen only
     * when the screen is in landscape mode.  If you change what
     * this returns, you will need to call {@link #updateFullscreenMode()}
     * yourself whenever the returned value may have changed to have it
     * re-evaluated and applied.
     */
    public boolean onEvaluateFullscreenMode() {
        android.content.res.Configuration config = getResources().getConfiguration();
        if (config.orientation != android.content.res.Configuration.ORIENTATION_LANDSCAPE) {
            return false;
        }
        if ((mInputEditorInfo != null) && ((mInputEditorInfo.imeOptions & android.view.inputmethod.EditorInfo.IME_FLAG_NO_FULLSCREEN) != 0)) {
            return false;
        }
        return true;
    }

    /**
     * Controls the visibility of the extracted text area.  This only applies
     * when the input method is in fullscreen mode, and thus showing extracted
     * text.  When false, the extracted text will not be shown, allowing some
     * of the application to be seen behind.  This is normally set for you
     * by {@link #onUpdateExtractingVisibility}.  This controls the visibility
     * of both the extracted text and candidate view; the latter since it is
     * not useful if there is no text to see.
     */
    public void setExtractViewShown(boolean shown) {
        if (mExtractViewHidden == shown) {
            mExtractViewHidden = !shown;
            updateExtractFrameVisibility();
        }
    }

    /**
     * Return whether the fullscreen extract view is shown.  This will only
     * return true if {@link #isFullscreenMode()} returns true, and in that
     * case its value depends on the last call to
     * {@link #setExtractViewShown(boolean)}.  This effectively lets you
     * determine if the application window is entirely covered (when this
     * returns true) or if some part of it may be shown (if this returns
     * false, though if {@link #isFullscreenMode()} returns true in that case
     * then it is probably only a sliver of the application).
     */
    public boolean isExtractViewShown() {
        return mIsFullscreen && (!mExtractViewHidden);
    }

    void updateExtractFrameVisibility() {
        final int vis;
        if (isFullscreenMode()) {
            vis = (mExtractViewHidden) ? android.view.View.INVISIBLE : android.view.View.VISIBLE;
            // "vis" should be applied for the extract frame as well in the fullscreen mode.
            mExtractFrame.setVisibility(vis);
        } else {
            vis = android.view.View.VISIBLE;
            mExtractFrame.setVisibility(android.view.View.GONE);
        }
        updateCandidatesVisibility(mCandidatesVisibility == android.view.View.VISIBLE);
        if (mWindowWasVisible && (mFullscreenArea.getVisibility() != vis)) {
            int animRes = mThemeAttrs.getResourceId(vis == android.view.View.VISIBLE ? com.android.internal.R.styleable.InputMethodService_imeExtractEnterAnimation : com.android.internal.R.styleable.InputMethodService_imeExtractExitAnimation, 0);
            if (animRes != 0) {
                mFullscreenArea.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, animRes));
            }
        }
        mFullscreenArea.setVisibility(vis);
    }

    /**
     * Compute the interesting insets into your UI.  The default implementation
     * uses the top of the candidates frame for the visible insets, and the
     * top of the input frame for the content insets.  The default touchable
     * insets are {@link Insets#TOUCHABLE_INSETS_VISIBLE}.
     *
     * <p>Note that this method is not called when
     * {@link #isExtractViewShown} returns true, since
     * in that case the application is left as-is behind the input method and
     * not impacted by anything in its UI.
     *
     * @param outInsets
     * 		Fill in with the current UI insets.
     */
    public void onComputeInsets(android.inputmethodservice.InputMethodService.Insets outInsets) {
        int[] loc = mTmpLocation;
        if (mInputFrame.getVisibility() == android.view.View.VISIBLE) {
            mInputFrame.getLocationInWindow(loc);
        } else {
            android.view.View decor = getWindow().getWindow().getDecorView();
            loc[1] = decor.getHeight();
        }
        if (isFullscreenMode()) {
            // In fullscreen mode, we never resize the underlying window.
            android.view.View decor = getWindow().getWindow().getDecorView();
            outInsets.contentTopInsets = decor.getHeight();
        } else {
            outInsets.contentTopInsets = loc[1];
        }
        if (mCandidatesFrame.getVisibility() == android.view.View.VISIBLE) {
            mCandidatesFrame.getLocationInWindow(loc);
        }
        outInsets.visibleTopInsets = loc[1];
        outInsets.touchableInsets = android.inputmethodservice.InputMethodService.Insets.TOUCHABLE_INSETS_VISIBLE;
        outInsets.touchableRegion.setEmpty();
    }

    /**
     * Re-evaluate whether the soft input area should currently be shown, and
     * update its UI if this has changed since the last time it
     * was evaluated.  This will call {@link #onEvaluateInputViewShown()} to
     * determine whether the input view should currently be shown.  You
     * can use {@link #isInputViewShown()} to determine if the input view
     * is currently shown.
     */
    public void updateInputViewShown() {
        boolean isShown = mShowInputRequested && onEvaluateInputViewShown();
        if ((mIsInputViewShown != isShown) && mWindowVisible) {
            mIsInputViewShown = isShown;
            mInputFrame.setVisibility(isShown ? android.view.View.VISIBLE : android.view.View.GONE);
            if (mInputView == null) {
                initialize();
                android.view.View v = onCreateInputView();
                if (v != null) {
                    setInputView(v);
                }
            }
        }
    }

    /**
     * Returns true if we have been asked to show our input view.
     */
    public boolean isShowInputRequested() {
        return mShowInputRequested;
    }

    /**
     * Return whether the soft input view is <em>currently</em> shown to the
     * user.  This is the state that was last determined and
     * applied by {@link #updateInputViewShown()}.
     */
    public boolean isInputViewShown() {
        return mIsInputViewShown && mWindowVisible;
    }

    /**
     * Override this to control when the soft input area should be shown to the user.  The default
     * implementation returns {@code false} when there is no hard keyboard or the keyboard is hidden
     * unless the user shows an intention to use software keyboard.  If you change what this
     * returns, you will need to call {@link #updateInputViewShown()} yourself whenever the returned
     * value may have changed to have it re-evaluated and applied.
     *
     * <p>When you override this method, it is recommended to call
     * {@code super.onEvaluateInputViewShown()} and return {@code true} when {@code true} is
     * returned.</p>
     */
    @android.annotation.CallSuper
    public boolean onEvaluateInputViewShown() {
        if (mSettingsObserver == null) {
            android.util.Log.w(android.inputmethodservice.InputMethodService.TAG, "onEvaluateInputViewShown: mSettingsObserver must not be null here.");
            return false;
        }
        if (mSettingsObserver.shouldShowImeWithHardKeyboard()) {
            return true;
        }
        android.content.res.Configuration config = getResources().getConfiguration();
        return (config.keyboard == android.content.res.Configuration.KEYBOARD_NOKEYS) || (config.hardKeyboardHidden == android.content.res.Configuration.HARDKEYBOARDHIDDEN_YES);
    }

    /**
     * Controls the visibility of the candidates display area.  By default
     * it is hidden.
     */
    public void setCandidatesViewShown(boolean shown) {
        updateCandidatesVisibility(shown);
        if ((!mShowInputRequested) && (mWindowVisible != shown)) {
            // If we are being asked to show the candidates view while the app
            // has not asked for the input view to be shown, then we need
            // to update whether the window is shown.
            if (shown) {
                showWindow(false);
            } else {
                doHideWindow();
            }
        }
    }

    void updateCandidatesVisibility(boolean shown) {
        int vis = (shown) ? android.view.View.VISIBLE : getCandidatesHiddenVisibility();
        if (mCandidatesVisibility != vis) {
            mCandidatesFrame.setVisibility(vis);
            mCandidatesVisibility = vis;
        }
    }

    /**
     * Returns the visibility mode (either {@link View#INVISIBLE View.INVISIBLE}
     * or {@link View#GONE View.GONE}) of the candidates view when it is not
     * shown.  The default implementation returns GONE when
     * {@link #isExtractViewShown} returns true,
     * otherwise VISIBLE.  Be careful if you change this to return GONE in
     * other situations -- if showing or hiding the candidates view causes
     * your window to resize, this can cause temporary drawing artifacts as
     * the resize takes place.
     */
    public int getCandidatesHiddenVisibility() {
        return isExtractViewShown() ? android.view.View.GONE : android.view.View.INVISIBLE;
    }

    public void showStatusIcon(@android.annotation.DrawableRes
    int iconResId) {
        mStatusIcon = iconResId;
        mImm.showStatusIcon(mToken, getPackageName(), iconResId);
    }

    public void hideStatusIcon() {
        mStatusIcon = 0;
        mImm.hideStatusIcon(mToken);
    }

    /**
     * Force switch to a new input method, as identified by <var>id</var>.  This
     * input method will be destroyed, and the requested one started on the
     * current input field.
     *
     * @param id
     * 		Unique identifier of the new input method ot start.
     */
    public void switchInputMethod(java.lang.String id) {
        mImm.setInputMethod(mToken, id);
    }

    public void setExtractView(android.view.View view) {
        mExtractFrame.removeAllViews();
        mExtractFrame.addView(view, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        mExtractView = view;
        if (view != null) {
            mExtractEditText = ((android.inputmethodservice.ExtractEditText) (view.findViewById(com.android.internal.R.id.inputExtractEditText)));
            mExtractEditText.setIME(this);
            mExtractAction = view.findViewById(com.android.internal.R.id.inputExtractAction);
            if (mExtractAction != null) {
                mExtractAccessories = ((android.view.ViewGroup) (view.findViewById(com.android.internal.R.id.inputExtractAccessories)));
            }
            startExtractingText(false);
        } else {
            mExtractEditText = null;
            mExtractAccessories = null;
            mExtractAction = null;
        }
    }

    /**
     * Replaces the current candidates view with a new one.  You only need to
     * call this when dynamically changing the view; normally, you should
     * implement {@link #onCreateCandidatesView()} and create your view when
     * first needed by the input method.
     */
    public void setCandidatesView(android.view.View view) {
        mCandidatesFrame.removeAllViews();
        mCandidatesFrame.addView(view, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    /**
     * Replaces the current input view with a new one.  You only need to
     * call this when dynamically changing the view; normally, you should
     * implement {@link #onCreateInputView()} and create your view when
     * first needed by the input method.
     */
    public void setInputView(android.view.View view) {
        mInputFrame.removeAllViews();
        mInputFrame.addView(view, new android.widget.FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        mInputView = view;
    }

    /**
     * Called by the framework to create the layout for showing extacted text.
     * Only called when in fullscreen mode.  The returned view hierarchy must
     * have an {@link ExtractEditText} whose ID is
     * {@link android.R.id#inputExtractEditText}.
     */
    public android.view.View onCreateExtractTextView() {
        return mInflater.inflate(com.android.internal.R.layout.input_method_extract_view, null);
    }

    /**
     * Create and return the view hierarchy used to show candidates.  This will
     * be called once, when the candidates are first displayed.  You can return
     * null to have no candidates view; the default implementation returns null.
     *
     * <p>To control when the candidates view is displayed, use
     * {@link #setCandidatesViewShown(boolean)}.
     * To change the candidates view after the first one is created by this
     * function, use {@link #setCandidatesView(View)}.
     */
    public android.view.View onCreateCandidatesView() {
        return null;
    }

    /**
     * Create and return the view hierarchy used for the input area (such as
     * a soft keyboard).  This will be called once, when the input area is
     * first displayed.  You can return null to have no input area; the default
     * implementation returns null.
     *
     * <p>To control when the input view is displayed, implement
     * {@link #onEvaluateInputViewShown()}.
     * To change the input view after the first one is created by this
     * function, use {@link #setInputView(View)}.
     */
    public android.view.View onCreateInputView() {
        return null;
    }

    /**
     * Called when the input view is being shown and input has started on
     * a new editor.  This will always be called after {@link #onStartInput},
     * allowing you to do your general setup there and just view-specific
     * setup here.  You are guaranteed that {@link #onCreateInputView()} will
     * have been called some time before this function is called.
     *
     * @param info
     * 		Description of the type of text being edited.
     * @param restarting
     * 		Set to true if we are restarting input on the
     * 		same text field as before.
     */
    public void onStartInputView(android.view.inputmethod.EditorInfo info, boolean restarting) {
        // Intentionally empty
    }

    /**
     * Called when the input view is being hidden from the user.  This will
     * be called either prior to hiding the window, or prior to switching to
     * another target for editing.
     *
     * <p>The default
     * implementation uses the InputConnection to clear any active composing
     * text; you can override this (not calling the base class implementation)
     * to perform whatever behavior you would like.
     *
     * @param finishingInput
     * 		If true, {@link #onFinishInput} will be
     * 		called immediately after.
     */
    public void onFinishInputView(boolean finishingInput) {
        if (!finishingInput) {
            android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * Called when only the candidates view has been shown for showing
     * processing as the user enters text through a hard keyboard.
     * This will always be called after {@link #onStartInput},
     * allowing you to do your general setup there and just view-specific
     * setup here.  You are guaranteed that {@link #onCreateCandidatesView()}
     * will have been called some time before this function is called.
     *
     * <p>Note that this will <em>not</em> be called when the input method
     * is running in full editing mode, and thus receiving
     * {@link #onStartInputView} to initiate that operation.  This is only
     * for the case when candidates are being shown while the input method
     * editor is hidden but wants to show its candidates UI as text is
     * entered through some other mechanism.
     *
     * @param info
     * 		Description of the type of text being edited.
     * @param restarting
     * 		Set to true if we are restarting input on the
     * 		same text field as before.
     */
    public void onStartCandidatesView(android.view.inputmethod.EditorInfo info, boolean restarting) {
        // Intentionally empty
    }

    /**
     * Called when the candidates view is being hidden from the user.  This will
     * be called either prior to hiding the window, or prior to switching to
     * another target for editing.
     *
     * <p>The default
     * implementation uses the InputConnection to clear any active composing
     * text; you can override this (not calling the base class implementation)
     * to perform whatever behavior you would like.
     *
     * @param finishingInput
     * 		If true, {@link #onFinishInput} will be
     * 		called immediately after.
     */
    public void onFinishCandidatesView(boolean finishingInput) {
        if (!finishingInput) {
            android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.finishComposingText();
            }
        }
    }

    /**
     * The system has decided that it may be time to show your input method.
     * This is called due to a corresponding call to your
     * {@link InputMethod#showSoftInput InputMethod.showSoftInput()}
     * method.  The default implementation uses
     * {@link #onEvaluateInputViewShown()}, {@link #onEvaluateFullscreenMode()},
     * and the current configuration to decide whether the input view should
     * be shown at this point.
     *
     * @param flags
     * 		Provides additional information about the show request,
     * 		as per {@link InputMethod#showSoftInput InputMethod.showSoftInput()}.
     * @param configChange
     * 		This is true if we are re-showing due to a
     * 		configuration change.
     * @return Returns true to indicate that the window should be shown.
     */
    public boolean onShowInputRequested(int flags, boolean configChange) {
        if (!onEvaluateInputViewShown()) {
            return false;
        }
        if ((flags & android.view.inputmethod.InputMethod.SHOW_EXPLICIT) == 0) {
            if ((!configChange) && onEvaluateFullscreenMode()) {
                // Don't show if this is not explicitly requested by the user and
                // the input method is fullscreen.  That would be too disruptive.
                // However, we skip this change for a config change, since if
                // the IME is already shown we do want to go into fullscreen
                // mode at this point.
                return false;
            }
            if ((!mSettingsObserver.shouldShowImeWithHardKeyboard()) && (getResources().getConfiguration().keyboard != android.content.res.Configuration.KEYBOARD_NOKEYS)) {
                // And if the device has a hard keyboard, even if it is
                // currently hidden, don't show the input method implicitly.
                // These kinds of devices don't need it that much.
                return false;
            }
        }
        return true;
    }

    /**
     * A utility method to call {{@link #onShowInputRequested(int, boolean)}} and update internal
     * states depending on its result.  Since {@link #onShowInputRequested(int, boolean)} is
     * exposed to IME authors as an overridable public method without {@code @CallSuper}, we have
     * to have this method to ensure that those internal states are always updated no matter how
     * {@link #onShowInputRequested(int, boolean)} is overridden by the IME author.
     *
     * @param flags
     * 		Provides additional information about the show request,
     * 		as per {@link InputMethod#showSoftInput InputMethod.showSoftInput()}.
     * @param configChange
     * 		This is true if we are re-showing due to a
     * 		configuration change.
     * @return Returns true to indicate that the window should be shown.
     * @see #onShowInputRequested(int, boolean)
     */
    private boolean dispatchOnShowInputRequested(int flags, boolean configChange) {
        final boolean result = onShowInputRequested(flags, configChange);
        if (result) {
            mShowInputFlags = flags;
        } else {
            mShowInputFlags = 0;
        }
        return result;
    }

    public void showWindow(boolean showInput) {
        if (android.inputmethodservice.InputMethodService.DEBUG)
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, (((((((((((("Showing window: showInput=" + showInput) + " mShowInputRequested=") + mShowInputRequested) + " mWindowAdded=") + mWindowAdded) + " mWindowCreated=") + mWindowCreated) + " mWindowVisible=") + mWindowVisible) + " mInputStarted=") + mInputStarted) + " mShowInputFlags=") + mShowInputFlags);

        if (mInShowWindow) {
            android.util.Log.w(android.inputmethodservice.InputMethodService.TAG, "Re-entrance in to showWindow");
            return;
        }
        try {
            mWindowWasVisible = mWindowVisible;
            mInShowWindow = true;
            showWindowInner(showInput);
        } catch (android.view.WindowManager.BadTokenException e) {
            // BadTokenException is a normal consequence in certain situations, e.g., swapping IMEs
            // while there is a DO_SHOW_SOFT_INPUT message in the IIMethodWrapper queue.
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "BadTokenException: IME is done.");

            mWindowVisible = false;
            mWindowAdded = false;
            // Rethrow the exception to preserve the existing behavior.  Some IMEs may have directly
            // called this method and relied on this exception for some clean-up tasks.
            // TODO: Give developers a clear guideline of whether it's OK to call this method or
            // InputMethodManager#showSoftInputFromInputMethod() should always be used instead.
            throw e;
        } finally {
            // TODO: Is it OK to set true when we get BadTokenException?
            mWindowWasVisible = true;
            mInShowWindow = false;
        }
    }

    void showWindowInner(boolean showInput) {
        boolean doShowInput = false;
        final int previousImeWindowStatus = (mWindowVisible ? android.inputmethodservice.InputMethodService.IME_ACTIVE : 0) | (isInputViewShown() ? android.inputmethodservice.InputMethodService.IME_VISIBLE : 0);
        mWindowVisible = true;
        if (((!mShowInputRequested) && mInputStarted) && showInput) {
            doShowInput = true;
            mShowInputRequested = true;
        }
        if (android.inputmethodservice.InputMethodService.DEBUG)
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "showWindow: updating UI");

        initialize();
        updateFullscreenMode();
        updateInputViewShown();
        if ((!mWindowAdded) || (!mWindowCreated)) {
            mWindowAdded = true;
            mWindowCreated = true;
            initialize();
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onCreateCandidatesView");

            android.view.View v = onCreateCandidatesView();
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "showWindow: candidates=" + v);

            if (v != null) {
                setCandidatesView(v);
            }
        }
        if (mShowInputRequested) {
            if (!mInputViewStarted) {
                if (android.inputmethodservice.InputMethodService.DEBUG)
                    android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onStartInputView");

                mInputViewStarted = true;
                onStartInputView(mInputEditorInfo, false);
            }
        } else
            if (!mCandidatesViewStarted) {
                if (android.inputmethodservice.InputMethodService.DEBUG)
                    android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onStartCandidatesView");

                mCandidatesViewStarted = true;
                onStartCandidatesView(mInputEditorInfo, false);
            }

        if (doShowInput) {
            startExtractingText(false);
        }
        final int nextImeWindowStatus = android.inputmethodservice.InputMethodService.IME_ACTIVE | (isInputViewShown() ? android.inputmethodservice.InputMethodService.IME_VISIBLE : 0);
        if (previousImeWindowStatus != nextImeWindowStatus) {
            mImm.setImeWindowStatus(mToken, nextImeWindowStatus, mBackDisposition);
        }
        if ((previousImeWindowStatus & android.inputmethodservice.InputMethodService.IME_ACTIVE) == 0) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "showWindow: showing!");

            onWindowShown();
            mWindow.show();
            // Put here rather than in onWindowShown() in case people forget to call
            // super.onWindowShown().
            mShouldClearInsetOfPreviousIme = false;
        }
    }

    private void finishViews() {
        if (mInputViewStarted) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onFinishInputView");

            onFinishInputView(false);
        } else
            if (mCandidatesViewStarted) {
                if (android.inputmethodservice.InputMethodService.DEBUG)
                    android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onFinishCandidatesView");

                onFinishCandidatesView(false);
            }

        mInputViewStarted = false;
        mCandidatesViewStarted = false;
    }

    private void doHideWindow() {
        mImm.setImeWindowStatus(mToken, 0, mBackDisposition);
        hideWindow();
    }

    public void hideWindow() {
        finishViews();
        if (mWindowVisible) {
            mWindow.hide();
            mWindowVisible = false;
            onWindowHidden();
            mWindowWasVisible = false;
        }
        updateFullscreenMode();
    }

    /**
     * Called when the input method window has been shown to the user, after
     * previously not being visible.  This is done after all of the UI setup
     * for the window has occurred (creating its views etc).
     */
    public void onWindowShown() {
        // Intentionally empty
    }

    /**
     * Called when the input method window has been hidden from the user,
     * after previously being visible.
     */
    public void onWindowHidden() {
        // Intentionally empty
    }

    /**
     * Reset the inset occupied the previous IME when and only when
     * {@link #mShouldClearInsetOfPreviousIme} is {@code true}.
     */
    private void clearInsetOfPreviousIme() {
        if (android.inputmethodservice.InputMethodService.DEBUG)
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, ("clearInsetOfPreviousIme() " + " mShouldClearInsetOfPreviousIme=") + mShouldClearInsetOfPreviousIme);

        if (!mShouldClearInsetOfPreviousIme)
            return;

        mImm.clearLastInputMethodWindowForTransition(mToken);
        mShouldClearInsetOfPreviousIme = false;
    }

    /**
     * Called when a new client has bound to the input method.  This
     * may be followed by a series of {@link #onStartInput(EditorInfo, boolean)}
     * and {@link #onFinishInput()} calls as the user navigates through its
     * UI.  Upon this call you know that {@link #getCurrentInputBinding}
     * and {@link #getCurrentInputConnection} return valid objects.
     */
    public void onBindInput() {
        // Intentionally empty
    }

    /**
     * Called when the previous bound client is no longer associated
     * with the input method.  After returning {@link #getCurrentInputBinding}
     * and {@link #getCurrentInputConnection} will no longer return
     * valid objects.
     */
    public void onUnbindInput() {
        // Intentionally empty
    }

    /**
     * Called to inform the input method that text input has started in an
     * editor.  You should use this callback to initialize the state of your
     * input to match the state of the editor given to it.
     *
     * @param attribute
     * 		The attributes of the editor that input is starting
     * 		in.
     * @param restarting
     * 		Set to true if input is restarting in the same
     * 		editor such as because the application has changed the text in
     * 		the editor.  Otherwise will be false, indicating this is a new
     * 		session with the editor.
     */
    public void onStartInput(android.view.inputmethod.EditorInfo attribute, boolean restarting) {
        // Intentionally empty
    }

    void doFinishInput() {
        if (mInputViewStarted) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onFinishInputView");

            onFinishInputView(true);
        } else
            if (mCandidatesViewStarted) {
                if (android.inputmethodservice.InputMethodService.DEBUG)
                    android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onFinishCandidatesView");

                onFinishCandidatesView(true);
            }

        mInputViewStarted = false;
        mCandidatesViewStarted = false;
        if (mInputStarted) {
            if (android.inputmethodservice.InputMethodService.DEBUG)
                android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onFinishInput");

            onFinishInput();
        }
        mInputStarted = false;
        mStartedInputConnection = null;
        mCurCompletions = null;
    }

    void doStartInput(android.view.inputmethod.InputConnection ic, android.view.inputmethod.EditorInfo attribute, boolean restarting) {
        if (!restarting) {
            doFinishInput();
        }
        mInputStarted = true;
        mStartedInputConnection = ic;
        mInputEditorInfo = attribute;
        initialize();
        if (android.inputmethodservice.InputMethodService.DEBUG)
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onStartInput");

        onStartInput(attribute, restarting);
        if (mWindowVisible) {
            if (mShowInputRequested) {
                if (android.inputmethodservice.InputMethodService.DEBUG)
                    android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onStartInputView");

                mInputViewStarted = true;
                onStartInputView(mInputEditorInfo, restarting);
                startExtractingText(true);
            } else
                if (mCandidatesVisibility == android.view.View.VISIBLE) {
                    if (android.inputmethodservice.InputMethodService.DEBUG)
                        android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "CALL: onStartCandidatesView");

                    mCandidatesViewStarted = true;
                    onStartCandidatesView(mInputEditorInfo, restarting);
                }

        }
    }

    /**
     * Called to inform the input method that text input has finished in
     * the last editor.  At this point there may be a call to
     * {@link #onStartInput(EditorInfo, boolean)} to perform input in a
     * new editor, or the input method may be left idle.  This method is
     * <em>not</em> called when input restarts in the same editor.
     *
     * <p>The default
     * implementation uses the InputConnection to clear any active composing
     * text; you can override this (not calling the base class implementation)
     * to perform whatever behavior you would like.
     */
    public void onFinishInput() {
        android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.finishComposingText();
        }
    }

    /**
     * Called when the application has reported auto-completion candidates that
     * it would like to have the input method displayed.  Typically these are
     * only used when an input method is running in full-screen mode, since
     * otherwise the user can see and interact with the pop-up window of
     * completions shown by the application.
     *
     * <p>The default implementation here does nothing.
     */
    public void onDisplayCompletions(android.view.inputmethod.CompletionInfo[] completions) {
        // Intentionally empty
    }

    /**
     * Called when the application has reported new extracted text to be shown
     * due to changes in its current text state.  The default implementation
     * here places the new text in the extract edit text, when the input
     * method is running in fullscreen mode.
     */
    public void onUpdateExtractedText(int token, android.view.inputmethod.ExtractedText text) {
        if (mExtractedToken != token) {
            return;
        }
        if (text != null) {
            if (mExtractEditText != null) {
                mExtractedText = text;
                mExtractEditText.setExtractedText(text);
            }
        }
    }

    /**
     * Called when the application has reported a new selection region of
     * the text.  This is called whether or not the input method has requested
     * extracted text updates, although if so it will not receive this call
     * if the extracted text has changed as well.
     *
     * <p>Be careful about changing the text in reaction to this call with
     * methods such as setComposingText, commitText or
     * deleteSurroundingText. If the cursor moves as a result, this method
     * will be called again, which may result in an infinite loop.
     *
     * <p>The default implementation takes care of updating the cursor in
     * the extract text, if it is being shown.
     */
    public void onUpdateSelection(int oldSelStart, int oldSelEnd, int newSelStart, int newSelEnd, int candidatesStart, int candidatesEnd) {
        final android.inputmethodservice.ExtractEditText eet = mExtractEditText;
        if (((eet != null) && isFullscreenMode()) && (mExtractedText != null)) {
            final int off = mExtractedText.startOffset;
            eet.startInternalChanges();
            newSelStart -= off;
            newSelEnd -= off;
            final int len = eet.getText().length();
            if (newSelStart < 0)
                newSelStart = 0;
            else
                if (newSelStart > len)
                    newSelStart = len;


            if (newSelEnd < 0)
                newSelEnd = 0;
            else
                if (newSelEnd > len)
                    newSelEnd = len;


            eet.setSelection(newSelStart, newSelEnd);
            eet.finishInternalChanges();
        }
    }

    /**
     * Called when the user tapped or clicked a text view.
     * IMEs can't rely on this method being called because this was not part of the original IME
     * protocol, so applications with custom text editing written before this method appeared will
     * not call to inform the IME of this interaction.
     *
     * @param focusChanged
     * 		true if the user changed the focused view by this click.
     */
    public void onViewClicked(boolean focusChanged) {
        // Intentionally empty
    }

    /**
     * Called when the application has reported a new location of its text
     * cursor.  This is only called if explicitly requested by the input method.
     * The default implementation does nothing.
     *
     * @deprecated Use {#link onUpdateCursorAnchorInfo(CursorAnchorInfo)} instead.
     */
    @java.lang.Deprecated
    public void onUpdateCursor(android.graphics.Rect newCursor) {
        // Intentionally empty
    }

    /**
     * Called when the application has reported a new location of its text insertion point and
     * characters in the composition string.  This is only called if explicitly requested by the
     * input method. The default implementation does nothing.
     *
     * @param cursorAnchorInfo
     * 		The positional information of the text insertion point and the
     * 		composition string.
     */
    public void onUpdateCursorAnchorInfo(android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo) {
        // Intentionally empty
    }

    /**
     * Close this input method's soft input area, removing it from the display.
     * The input method will continue running, but the user can no longer use
     * it to generate input by touching the screen.
     *
     * @param flags
     * 		Provides additional operating flags.  Currently may be
     * 		0 or have the {@link InputMethodManager#HIDE_IMPLICIT_ONLY
     * 		InputMethodManager.HIDE_IMPLICIT_ONLY} bit set.
     */
    public void requestHideSelf(int flags) {
        mImm.hideSoftInputFromInputMethod(mToken, flags);
    }

    /**
     * Show the input method. This is a call back to the
     * IMF to handle showing the input method.
     *
     * @param flags
     * 		Provides additional operating flags.  Currently may be
     * 		0 or have the {@link InputMethodManager#SHOW_FORCED
     * 		InputMethodManager.} bit set.
     */
    private void requestShowSelf(int flags) {
        mImm.showSoftInputFromInputMethod(mToken, flags);
    }

    private boolean handleBack(boolean doIt) {
        if (mShowInputRequested) {
            // If the soft input area is shown, back closes it and we
            // consume the back key.
            if (doIt)
                requestHideSelf(0);

            return true;
        } else
            if (mWindowVisible) {
                if (mCandidatesVisibility == android.view.View.VISIBLE) {
                    // If we are showing candidates even if no input area, then
                    // hide them.
                    if (doIt)
                        setCandidatesViewShown(false);

                } else {
                    // If we have the window visible for some other reason --
                    // most likely to show candidates -- then just get rid
                    // of it.  This really shouldn't happen, but just in case...
                    if (doIt)
                        doHideWindow();

                }
                return true;
            }

        return false;
    }

    /**
     *
     *
     * @return {#link ExtractEditText} if it is considered to be visible and active. Otherwise
    {@code null} is returned.
     */
    private android.inputmethodservice.ExtractEditText getExtractEditTextIfVisible() {
        if ((!isExtractViewShown()) || (!isInputViewShown())) {
            return null;
        }
        return mExtractEditText;
    }

    /**
     * Override this to intercept key down events before they are processed by the
     * application.  If you return true, the application will not
     * process the event itself.  If you return false, the normal application processing
     * will occur as if the IME had not seen the event at all.
     *
     * <p>The default implementation intercepts {@link KeyEvent#KEYCODE_BACK
     * KeyEvent.KEYCODE_BACK} if the IME is currently shown, to
     * possibly hide it when the key goes up (if not canceled or long pressed).  In
     * addition, in fullscreen mode only, it will consume DPAD movement
     * events to move the cursor in the extracted text view, not allowing
     * them to perform navigation in the underlying application.
     */
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (event.getKeyCode() == android.view.KeyEvent.KEYCODE_BACK) {
            final android.inputmethodservice.ExtractEditText eet = getExtractEditTextIfVisible();
            if ((eet != null) && eet.handleBackInTextActionModeIfNeeded(event)) {
                return true;
            }
            if (handleBack(false)) {
                event.startTracking();
                return true;
            }
            return false;
        }
        return doMovementKey(keyCode, event, android.inputmethodservice.InputMethodService.MOVEMENT_DOWN);
    }

    /**
     * Default implementation of {@link KeyEvent.Callback#onKeyLongPress(int, KeyEvent)
     * KeyEvent.Callback.onKeyLongPress()}: always returns false (doesn't handle
     * the event).
     */
    public boolean onKeyLongPress(int keyCode, android.view.KeyEvent event) {
        return false;
    }

    /**
     * Override this to intercept special key multiple events before they are
     * processed by the
     * application.  If you return true, the application will not itself
     * process the event.  If you return false, the normal application processing
     * will occur as if the IME had not seen the event at all.
     *
     * <p>The default implementation always returns false, except when
     * in fullscreen mode, where it will consume DPAD movement
     * events to move the cursor in the extracted text view, not allowing
     * them to perform navigation in the underlying application.
     */
    public boolean onKeyMultiple(int keyCode, int count, android.view.KeyEvent event) {
        return doMovementKey(keyCode, event, count);
    }

    /**
     * Override this to intercept key up events before they are processed by the
     * application.  If you return true, the application will not itself
     * process the event.  If you return false, the normal application processing
     * will occur as if the IME had not seen the event at all.
     *
     * <p>The default implementation intercepts {@link KeyEvent#KEYCODE_BACK
     * KeyEvent.KEYCODE_BACK} to hide the current IME UI if it is shown.  In
     * addition, in fullscreen mode only, it will consume DPAD movement
     * events to move the cursor in the extracted text view, not allowing
     * them to perform navigation in the underlying application.
     */
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (event.getKeyCode() == android.view.KeyEvent.KEYCODE_BACK) {
            final android.inputmethodservice.ExtractEditText eet = getExtractEditTextIfVisible();
            if ((eet != null) && eet.handleBackInTextActionModeIfNeeded(event)) {
                return true;
            }
            if (event.isTracking() && (!event.isCanceled())) {
                return handleBack(true);
            }
        }
        return doMovementKey(keyCode, event, android.inputmethodservice.InputMethodService.MOVEMENT_UP);
    }

    /**
     * Override this to intercept trackball motion events before they are
     * processed by the application.
     * If you return true, the application will not itself process the event.
     * If you return false, the normal application processing will occur as if
     * the IME had not seen the event at all.
     */
    @java.lang.Override
    public boolean onTrackballEvent(android.view.MotionEvent event) {
        if (android.inputmethodservice.InputMethodService.DEBUG)
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "onTrackballEvent: " + event);

        return false;
    }

    /**
     * Override this to intercept generic motion events before they are
     * processed by the application.
     * If you return true, the application will not itself process the event.
     * If you return false, the normal application processing will occur as if
     * the IME had not seen the event at all.
     */
    @java.lang.Override
    public boolean onGenericMotionEvent(android.view.MotionEvent event) {
        if (android.inputmethodservice.InputMethodService.DEBUG)
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "onGenericMotionEvent(): event " + event);

        return false;
    }

    public void onAppPrivateCommand(java.lang.String action, android.os.Bundle data) {
    }

    /**
     * Handle a request by the system to toggle the soft input area.
     */
    private void onToggleSoftInput(int showFlags, int hideFlags) {
        if (android.inputmethodservice.InputMethodService.DEBUG)
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "toggleSoftInput()");

        if (isInputViewShown()) {
            requestHideSelf(hideFlags);
        } else {
            requestShowSelf(showFlags);
        }
    }

    static final int MOVEMENT_DOWN = -1;

    static final int MOVEMENT_UP = -2;

    void reportExtractedMovement(int keyCode, int count) {
        int dx = 0;
        int dy = 0;
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                dx = -count;
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                dx = count;
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
                dy = -count;
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                dy = count;
                break;
        }
        onExtractedCursorMovement(dx, dy);
    }

    boolean doMovementKey(int keyCode, android.view.KeyEvent event, int count) {
        final android.inputmethodservice.ExtractEditText eet = getExtractEditTextIfVisible();
        if (eet != null) {
            // If we are in fullscreen mode, the cursor will move around
            // the extract edit text, but should NOT cause focus to move
            // to other fields.
            android.text.method.MovementMethod movement = eet.getMovementMethod();
            android.text.Layout layout = eet.getLayout();
            if ((movement != null) && (layout != null)) {
                // We want our own movement method to handle the key, so the
                // cursor will properly move in our own word wrapping.
                if (count == android.inputmethodservice.InputMethodService.MOVEMENT_DOWN) {
                    if (movement.onKeyDown(eet, eet.getText(), keyCode, event)) {
                        reportExtractedMovement(keyCode, 1);
                        return true;
                    }
                } else
                    if (count == android.inputmethodservice.InputMethodService.MOVEMENT_UP) {
                        if (movement.onKeyUp(eet, eet.getText(), keyCode, event)) {
                            return true;
                        }
                    } else {
                        if (movement.onKeyOther(eet, eet.getText(), event)) {
                            reportExtractedMovement(keyCode, count);
                        } else {
                            android.view.KeyEvent down = android.view.KeyEvent.changeAction(event, android.view.KeyEvent.ACTION_DOWN);
                            if (movement.onKeyDown(eet, eet.getText(), keyCode, down)) {
                                android.view.KeyEvent up = android.view.KeyEvent.changeAction(event, android.view.KeyEvent.ACTION_UP);
                                movement.onKeyUp(eet, eet.getText(), keyCode, up);
                                while ((--count) > 0) {
                                    movement.onKeyDown(eet, eet.getText(), keyCode, down);
                                    movement.onKeyUp(eet, eet.getText(), keyCode, up);
                                } 
                                reportExtractedMovement(keyCode, count);
                            }
                        }
                    }

            }
            // Regardless of whether the movement method handled the key,
            // we never allow DPAD navigation to the application.
            switch (keyCode) {
                case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
                case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                case android.view.KeyEvent.KEYCODE_DPAD_UP :
                case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
                    return true;
            }
        }
        return false;
    }

    /**
     * Send the given key event code (as defined by {@link KeyEvent}) to the
     * current input connection is a key down + key up event pair.  The sent
     * events have {@link KeyEvent#FLAG_SOFT_KEYBOARD KeyEvent.FLAG_SOFT_KEYBOARD}
     * set, so that the recipient can identify them as coming from a software
     * input method, and
     * {@link KeyEvent#FLAG_KEEP_TOUCH_MODE KeyEvent.FLAG_KEEP_TOUCH_MODE}, so
     * that they don't impact the current touch mode of the UI.
     *
     * <p>Note that it's discouraged to send such key events in normal operation;
     * this is mainly for use with {@link android.text.InputType#TYPE_NULL} type
     * text fields, or for non-rich input methods. A reasonably capable software
     * input method should use the
     * {@link android.view.inputmethod.InputConnection#commitText} family of methods
     * to send text to an application, rather than sending key events.</p>
     *
     * @param keyEventCode
     * 		The raw key code to send, as defined by
     * 		{@link KeyEvent}.
     */
    public void sendDownUpKeyEvents(int keyEventCode) {
        android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
        if (ic == null)
            return;

        long eventTime = android.os.SystemClock.uptimeMillis();
        ic.sendKeyEvent(new android.view.KeyEvent(eventTime, eventTime, android.view.KeyEvent.ACTION_DOWN, keyEventCode, 0, 0, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, android.view.KeyEvent.FLAG_SOFT_KEYBOARD | android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE));
        ic.sendKeyEvent(new android.view.KeyEvent(eventTime, android.os.SystemClock.uptimeMillis(), android.view.KeyEvent.ACTION_UP, keyEventCode, 0, 0, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, android.view.KeyEvent.FLAG_SOFT_KEYBOARD | android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE));
    }

    /**
     * Ask the input target to execute its default action via
     * {@link InputConnection#performEditorAction
     * InputConnection.performEditorAction()}.
     *
     * @param fromEnterKey
     * 		If true, this will be executed as if the user had
     * 		pressed an enter key on the keyboard, that is it will <em>not</em>
     * 		be done if the editor has set {@link EditorInfo#IME_FLAG_NO_ENTER_ACTION
     * 		EditorInfo.IME_FLAG_NO_ENTER_ACTION}.  If false, the action will be
     * 		sent regardless of how the editor has set that flag.
     * @return Returns a boolean indicating whether an action has been sent.
    If false, either the editor did not specify a default action or it
    does not want an action from the enter key.  If true, the action was
    sent (or there was no input connection at all).
     */
    public boolean sendDefaultEditorAction(boolean fromEnterKey) {
        android.view.inputmethod.EditorInfo ei = getCurrentInputEditorInfo();
        if (((ei != null) && ((!fromEnterKey) || ((ei.imeOptions & android.view.inputmethod.EditorInfo.IME_FLAG_NO_ENTER_ACTION) == 0))) && ((ei.imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION) != android.view.inputmethod.EditorInfo.IME_ACTION_NONE)) {
            // If the enter key was pressed, and the editor has a default
            // action associated with pressing enter, then send it that
            // explicit action instead of the key event.
            android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
            if (ic != null) {
                ic.performEditorAction(ei.imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION);
            }
            return true;
        }
        return false;
    }

    /**
     * Send the given UTF-16 character to the current input connection.  Most
     * characters will be delivered simply by calling
     * {@link InputConnection#commitText InputConnection.commitText()} with
     * the character; some, however, may be handled different.  In particular,
     * the enter character ('\n') will either be delivered as an action code
     * or a raw key event, as appropriate.  Consider this as a convenience
     * method for IMEs that do not have a full implementation of actions; a
     * fully complying IME will decide of the right action for each event and
     * will likely never call this method except maybe to handle events coming
     * from an actual hardware keyboard.
     *
     * @param charCode
     * 		The UTF-16 character code to send.
     */
    public void sendKeyChar(char charCode) {
        switch (charCode) {
            case '\n' :
                // Apps may be listening to an enter key to perform an action
                if (!sendDefaultEditorAction(true)) {
                    sendDownUpKeyEvents(android.view.KeyEvent.KEYCODE_ENTER);
                }
                break;
            default :
                // Make sure that digits go through any text watcher on the client side.
                if ((charCode >= '0') && (charCode <= '9')) {
                    sendDownUpKeyEvents((charCode - '0') + android.view.KeyEvent.KEYCODE_0);
                } else {
                    android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
                    if (ic != null) {
                        ic.commitText(java.lang.String.valueOf(charCode), 1);
                    }
                }
                break;
        }
    }

    /**
     * This is called when the user has moved the cursor in the extracted
     * text view, when running in fullsreen mode.  The default implementation
     * performs the corresponding selection change on the underlying text
     * editor.
     */
    public void onExtractedSelectionChanged(int start, int end) {
        android.view.inputmethod.InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setSelection(start, end);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void onExtractedDeleteText(int start, int end) {
        android.view.inputmethod.InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.finishComposingText();
            conn.setSelection(start, start);
            conn.deleteSurroundingText(0, end - start);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void onExtractedReplaceText(int start, int end, java.lang.CharSequence text) {
        android.view.inputmethod.InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            conn.setComposingRegion(start, end);
            conn.commitText(text, 1);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void onExtractedSetSpan(java.lang.Object span, int start, int end, int flags) {
        android.view.inputmethod.InputConnection conn = getCurrentInputConnection();
        if (conn != null) {
            if (!conn.setSelection(start, end))
                return;

            java.lang.CharSequence text = conn.getSelectedText(android.view.inputmethod.InputConnection.GET_TEXT_WITH_STYLES);
            if (text instanceof android.text.Spannable) {
                ((android.text.Spannable) (text)).setSpan(span, 0, text.length(), flags);
                conn.setComposingRegion(start, end);
                conn.commitText(text, 1);
            }
        }
    }

    /**
     * This is called when the user has clicked on the extracted text view,
     * when running in fullscreen mode.  The default implementation hides
     * the candidates view when this happens, but only if the extracted text
     * editor has a vertical scroll bar because its text doesn't fit.
     * Re-implement this to provide whatever behavior you want.
     */
    public void onExtractedTextClicked() {
        if (mExtractEditText == null) {
            return;
        }
        if (mExtractEditText.hasVerticalScrollBar()) {
            setCandidatesViewShown(false);
        }
    }

    /**
     * This is called when the user has performed a cursor movement in the
     * extracted text view, when it is running in fullscreen mode.  The default
     * implementation hides the candidates view when a vertical movement
     * happens, but only if the extracted text editor has a vertical scroll bar
     * because its text doesn't fit.
     * Re-implement this to provide whatever behavior you want.
     *
     * @param dx
     * 		The amount of cursor movement in the x dimension.
     * @param dy
     * 		The amount of cursor movement in the y dimension.
     */
    public void onExtractedCursorMovement(int dx, int dy) {
        if ((mExtractEditText == null) || (dy == 0)) {
            return;
        }
        if (mExtractEditText.hasVerticalScrollBar()) {
            setCandidatesViewShown(false);
        }
    }

    /**
     * This is called when the user has selected a context menu item from the
     * extracted text view, when running in fullscreen mode.  The default
     * implementation sends this action to the current InputConnection's
     * {@link InputConnection#performContextMenuAction(int)}, for it
     * to be processed in underlying "real" editor.  Re-implement this to
     * provide whatever behavior you want.
     */
    public boolean onExtractTextContextMenuItem(int id) {
        android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
        if (ic != null) {
            ic.performContextMenuAction(id);
        }
        return true;
    }

    /**
     * Return text that can be used as a button label for the given
     * {@link EditorInfo#imeOptions EditorInfo.imeOptions}.  Returns null
     * if there is no action requested.  Note that there is no guarantee that
     * the returned text will be relatively short, so you probably do not
     * want to use it as text on a soft keyboard key label.
     *
     * @param imeOptions
     * 		The value from @link EditorInfo#imeOptions EditorInfo.imeOptions}.
     * @return Returns a label to use, or null if there is no action.
     */
    public java.lang.CharSequence getTextForImeAction(int imeOptions) {
        switch (imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION) {
            case android.view.inputmethod.EditorInfo.IME_ACTION_NONE :
                return null;
            case android.view.inputmethod.EditorInfo.IME_ACTION_GO :
                return getText(com.android.internal.R.string.ime_action_go);
            case android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH :
                return getText(com.android.internal.R.string.ime_action_search);
            case android.view.inputmethod.EditorInfo.IME_ACTION_SEND :
                return getText(com.android.internal.R.string.ime_action_send);
            case android.view.inputmethod.EditorInfo.IME_ACTION_NEXT :
                return getText(com.android.internal.R.string.ime_action_next);
            case android.view.inputmethod.EditorInfo.IME_ACTION_DONE :
                return getText(com.android.internal.R.string.ime_action_done);
            case android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS :
                return getText(com.android.internal.R.string.ime_action_previous);
            default :
                return getText(com.android.internal.R.string.ime_action_default);
        }
    }

    /**
     * Return a drawable resource id that can be used as a button icon for the given
     * {@link EditorInfo#imeOptions EditorInfo.imeOptions}.
     *
     * @param imeOptions
     * 		The value from @link EditorInfo#imeOptions EditorInfo.imeOptions}.
     * @return Returns a drawable resource id to use.
     */
    @android.annotation.DrawableRes
    private int getIconForImeAction(int imeOptions) {
        switch (imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION) {
            case android.view.inputmethod.EditorInfo.IME_ACTION_GO :
                return com.android.internal.R.drawable.ic_input_extract_action_go;
            case android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH :
                return com.android.internal.R.drawable.ic_input_extract_action_search;
            case android.view.inputmethod.EditorInfo.IME_ACTION_SEND :
                return com.android.internal.R.drawable.ic_input_extract_action_send;
            case android.view.inputmethod.EditorInfo.IME_ACTION_NEXT :
                return com.android.internal.R.drawable.ic_input_extract_action_next;
            case android.view.inputmethod.EditorInfo.IME_ACTION_DONE :
                return com.android.internal.R.drawable.ic_input_extract_action_done;
            case android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS :
                return com.android.internal.R.drawable.ic_input_extract_action_previous;
            default :
                return com.android.internal.R.drawable.ic_input_extract_action_return;
        }
    }

    /**
     * Called when the fullscreen-mode extracting editor info has changed,
     * to determine whether the extracting (extract text and candidates) portion
     * of the UI should be shown.  The standard implementation hides or shows
     * the extract area depending on whether it makes sense for the
     * current editor.  In particular, a {@link InputType#TYPE_NULL}
     * input type or {@link EditorInfo#IME_FLAG_NO_EXTRACT_UI} flag will
     * turn off the extract area since there is no text to be shown.
     */
    public void onUpdateExtractingVisibility(android.view.inputmethod.EditorInfo ei) {
        if ((ei.inputType == android.text.InputType.TYPE_NULL) || ((ei.imeOptions & android.view.inputmethod.EditorInfo.IME_FLAG_NO_EXTRACT_UI) != 0)) {
            // No reason to show extract UI!
            setExtractViewShown(false);
            return;
        }
        setExtractViewShown(true);
    }

    /**
     * Called when the fullscreen-mode extracting editor info has changed,
     * to update the state of its UI such as the action buttons shown.
     * You do not need to deal with this if you are using the standard
     * full screen extract UI.  If replacing it, you will need to re-implement
     * this to put the appropriate action button in your own UI and handle it,
     * and perform any other changes.
     *
     * <p>The standard implementation turns on or off its accessory area
     * depending on whether there is an action button, and hides or shows
     * the entire extract area depending on whether it makes sense for the
     * current editor.  In particular, a {@link InputType#TYPE_NULL} or
     * {@link InputType#TYPE_TEXT_VARIATION_FILTER} input type will turn off the
     * extract area since there is no text to be shown.
     */
    public void onUpdateExtractingViews(android.view.inputmethod.EditorInfo ei) {
        if (!isExtractViewShown()) {
            return;
        }
        if (mExtractAccessories == null) {
            return;
        }
        final boolean hasAction = (ei.actionLabel != null) || ((((ei.imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION) != android.view.inputmethod.EditorInfo.IME_ACTION_NONE) && ((ei.imeOptions & android.view.inputmethod.EditorInfo.IME_FLAG_NO_ACCESSORY_ACTION) == 0)) && (ei.inputType != android.text.InputType.TYPE_NULL));
        if (hasAction) {
            mExtractAccessories.setVisibility(android.view.View.VISIBLE);
            if (mExtractAction != null) {
                if (mExtractAction instanceof android.widget.ImageButton) {
                    ((android.widget.ImageButton) (mExtractAction)).setImageResource(getIconForImeAction(ei.imeOptions));
                    if (ei.actionLabel != null) {
                        mExtractAction.setContentDescription(ei.actionLabel);
                    } else {
                        mExtractAction.setContentDescription(getTextForImeAction(ei.imeOptions));
                    }
                } else {
                    if (ei.actionLabel != null) {
                        ((android.widget.TextView) (mExtractAction)).setText(ei.actionLabel);
                    } else {
                        ((android.widget.TextView) (mExtractAction)).setText(getTextForImeAction(ei.imeOptions));
                    }
                }
                mExtractAction.setOnClickListener(mActionClickListener);
            }
        } else {
            mExtractAccessories.setVisibility(android.view.View.GONE);
            if (mExtractAction != null) {
                mExtractAction.setOnClickListener(null);
            }
        }
    }

    /**
     * This is called when, while currently displayed in extract mode, the
     * current input target changes.  The default implementation will
     * auto-hide the IME if the new target is not a full editor, since this
     * can be a confusing experience for the user.
     */
    public void onExtractingInputChanged(android.view.inputmethod.EditorInfo ei) {
        if (ei.inputType == android.text.InputType.TYPE_NULL) {
            requestHideSelf(android.view.inputmethod.InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    void startExtractingText(boolean inputChanged) {
        final android.inputmethodservice.ExtractEditText eet = mExtractEditText;
        if (((eet != null) && getCurrentInputStarted()) && isFullscreenMode()) {
            mExtractedToken++;
            android.view.inputmethod.ExtractedTextRequest req = new android.view.inputmethod.ExtractedTextRequest();
            req.token = mExtractedToken;
            req.flags = android.view.inputmethod.InputConnection.GET_TEXT_WITH_STYLES;
            req.hintMaxLines = 10;
            req.hintMaxChars = 10000;
            android.view.inputmethod.InputConnection ic = getCurrentInputConnection();
            mExtractedText = (ic == null) ? null : ic.getExtractedText(req, android.view.inputmethod.InputConnection.GET_EXTRACTED_TEXT_MONITOR);
            if ((mExtractedText == null) || (ic == null)) {
                android.util.Log.e(android.inputmethodservice.InputMethodService.TAG, (("Unexpected null in startExtractingText : mExtractedText = " + mExtractedText) + ", input connection = ") + ic);
            }
            final android.view.inputmethod.EditorInfo ei = getCurrentInputEditorInfo();
            try {
                eet.startInternalChanges();
                onUpdateExtractingVisibility(ei);
                onUpdateExtractingViews(ei);
                int inputType = ei.inputType;
                if ((inputType & android.view.inputmethod.EditorInfo.TYPE_MASK_CLASS) == android.view.inputmethod.EditorInfo.TYPE_CLASS_TEXT) {
                    if ((inputType & android.view.inputmethod.EditorInfo.TYPE_TEXT_FLAG_IME_MULTI_LINE) != 0) {
                        inputType |= android.view.inputmethod.EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE;
                    }
                }
                eet.setInputType(inputType);
                eet.setHint(ei.hintText);
                if (mExtractedText != null) {
                    eet.setEnabled(true);
                    eet.setExtractedText(mExtractedText);
                } else {
                    eet.setEnabled(false);
                    eet.setText("");
                }
            } finally {
                eet.finishInternalChanges();
            }
            if (inputChanged) {
                onExtractingInputChanged(ei);
            }
        }
    }

    // TODO: Handle the subtype change event
    /**
     * Called when the subtype was changed.
     *
     * @param newSubtype
     * 		the subtype which is being changed to.
     */
    protected void onCurrentInputMethodSubtypeChanged(android.view.inputmethod.InputMethodSubtype newSubtype) {
        if (android.inputmethodservice.InputMethodService.DEBUG) {
            int nameResId = newSubtype.getNameResId();
            java.lang.String mode = newSubtype.getMode();
            java.lang.String output = (((((("changeInputMethodSubtype:" + (nameResId == 0 ? "<none>" : getString(nameResId))) + ",") + mode) + ",") + newSubtype.getLocale()) + ",") + newSubtype.getExtraValue();
            android.util.Log.v(android.inputmethodservice.InputMethodService.TAG, "--- " + output);
        }
    }

    /**
     *
     *
     * @return The recommended height of the input method window.
    An IME author can get the last input method's height as the recommended height
    by calling this in
    {@link android.inputmethodservice.InputMethodService#onStartInputView(EditorInfo, boolean)}.
    If you don't need to use a predefined fixed height, you can avoid the window-resizing of IME
    switching by using this value as a visible inset height. It's efficient for the smooth
    transition between different IMEs. However, note that this may return 0 (or possibly
    unexpectedly low height). You should thus avoid relying on the return value of this method
    all the time. Please make sure to use a reasonable height for the IME.
     */
    public int getInputMethodWindowRecommendedHeight() {
        return mImm.getInputMethodWindowVisibleHeight();
    }

    /**
     * Allow the receiver of {@link InputContentInfo} to obtain a temporary read-only access
     * permission to the content.
     *
     * @param inputContentInfo
     * 		Content to be temporarily exposed from the input method to the
     * 		application.
     * 		This cannot be {@code null}.
     * @param inputConnection
     * 		{@link InputConnection} with which
     * 		{@link InputConnection#commitContent(InputContentInfo, Bundle)} will be called.
     * @unknown 
     */
    @java.lang.Override
    public final void exposeContent(@android.annotation.NonNull
    android.view.inputmethod.InputContentInfo inputContentInfo, @android.annotation.NonNull
    android.view.inputmethod.InputConnection inputConnection) {
        if (inputConnection == null) {
            return;
        }
        if (getCurrentInputConnection() != inputConnection) {
            return;
        }
        mImm.exposeContent(mToken, inputContentInfo, getCurrentInputEditorInfo());
    }

    /**
     * Performs a dump of the InputMethodService's internal state.  Override
     * to add your own information to the dump.
     */
    @java.lang.Override
    protected void dump(java.io.FileDescriptor fd, java.io.PrintWriter fout, java.lang.String[] args) {
        final android.util.Printer p = new android.util.PrintWriterPrinter(fout);
        p.println(("Input method service state for " + this) + ":");
        p.println((("  mWindowCreated=" + mWindowCreated) + " mWindowAdded=") + mWindowAdded);
        p.println((((("  mWindowVisible=" + mWindowVisible) + " mWindowWasVisible=") + mWindowWasVisible) + " mInShowWindow=") + mInShowWindow);
        p.println("  Configuration=" + getResources().getConfiguration());
        p.println("  mToken=" + mToken);
        p.println("  mInputBinding=" + mInputBinding);
        p.println("  mInputConnection=" + mInputConnection);
        p.println("  mStartedInputConnection=" + mStartedInputConnection);
        p.println((((("  mInputStarted=" + mInputStarted) + " mInputViewStarted=") + mInputViewStarted) + " mCandidatesViewStarted=") + mCandidatesViewStarted);
        if (mInputEditorInfo != null) {
            p.println("  mInputEditorInfo:");
            mInputEditorInfo.dump(p, "    ");
        } else {
            p.println("  mInputEditorInfo: null");
        }
        p.println((((("  mShowInputRequested=" + mShowInputRequested) + " mLastShowInputRequested=") + mLastShowInputRequested) + " mShowInputFlags=0x") + java.lang.Integer.toHexString(mShowInputFlags));
        p.println((((((("  mCandidatesVisibility=" + mCandidatesVisibility) + " mFullscreenApplied=") + mFullscreenApplied) + " mIsFullscreen=") + mIsFullscreen) + " mExtractViewHidden=") + mExtractViewHidden);
        if (mExtractedText != null) {
            p.println("  mExtractedText:");
            p.println(((("    text=" + mExtractedText.text.length()) + " chars") + " startOffset=") + mExtractedText.startOffset);
            p.println((((("    selectionStart=" + mExtractedText.selectionStart) + " selectionEnd=") + mExtractedText.selectionEnd) + " flags=0x") + java.lang.Integer.toHexString(mExtractedText.flags));
        } else {
            p.println("  mExtractedText: null");
        }
        p.println("  mExtractedToken=" + mExtractedToken);
        p.println((("  mIsInputViewShown=" + mIsInputViewShown) + " mStatusIcon=") + mStatusIcon);
        p.println("Last computed insets:");
        p.println((((((("  contentTopInsets=" + mTmpInsets.contentTopInsets) + " visibleTopInsets=") + mTmpInsets.visibleTopInsets) + " touchableInsets=") + mTmpInsets.touchableInsets) + " touchableRegion=") + mTmpInsets.touchableRegion);
        p.println(" mShouldClearInsetOfPreviousIme=" + mShouldClearInsetOfPreviousIme);
        p.println(" mSettingsObserver=" + mSettingsObserver);
    }
}

