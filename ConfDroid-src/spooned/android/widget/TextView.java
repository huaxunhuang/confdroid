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


import android.widget.TextView;

import static android.widget.TextView.LINES;
import static java.lang.Integer.MAX_VALUE;


/**
 * A user interface element that displays text to the user.
 * To provide user-editable text, see {@link EditText}.
 * <p>
 * The following code sample shows a typical use, with an XML layout
 * and code to modify the contents of the text view:
 * </p>
 *
 * <pre>
 * &lt;LinearLayout
 * xmlns:android="http://schemas.android.com/apk/res/android"
 * android:layout_width="match_parent"
 * android:layout_height="match_parent"&gt;
 *    &lt;TextView
 *        android:id="@+id/text_view_id"
 *        android:layout_height="wrap_content"
 *        android:layout_width="wrap_content"
 *        android:text="@string/hello" /&gt;
 * &lt;/LinearLayout&gt;
 * </pre>
 * <p>
 * This code sample demonstrates how to modify the contents of the text view
 * defined in the previous XML layout:
 * </p>
 * <pre>
 * public class MainActivity extends Activity {
 *
 *    protected void onCreate(Bundle savedInstanceState) {
 *         super.onCreate(savedInstanceState);
 *         setContentView(R.layout.activity_main);
 *         final TextView helloTextView = (TextView) findViewById(R.id.text_view_id);
 *         helloTextView.setText(R.string.user_greeting);
 *     }
 * }
 * </pre>
 * <p>
 * To customize the appearance of TextView, see <a href="https://developer.android.com/guide/topics/ui/themes.html">Styles and Themes</a>.
 * </p>
 * <p>
 * <b>XML attributes</b>
 * <p>
 * See {@link android.R.styleable#TextView TextView Attributes},
 * {@link android.R.styleable#View View Attributes}
 *
 * @unknown ref android.R.styleable#TextView_text
 * @unknown ref android.R.styleable#TextView_bufferType
 * @unknown ref android.R.styleable#TextView_hint
 * @unknown ref android.R.styleable#TextView_textColor
 * @unknown ref android.R.styleable#TextView_textColorHighlight
 * @unknown ref android.R.styleable#TextView_textColorHint
 * @unknown ref android.R.styleable#TextView_textAppearance
 * @unknown ref android.R.styleable#TextView_textColorLink
 * @unknown ref android.R.styleable#TextView_textFontWeight
 * @unknown ref android.R.styleable#TextView_textSize
 * @unknown ref android.R.styleable#TextView_textScaleX
 * @unknown ref android.R.styleable#TextView_fontFamily
 * @unknown ref android.R.styleable#TextView_typeface
 * @unknown ref android.R.styleable#TextView_textStyle
 * @unknown ref android.R.styleable#TextView_cursorVisible
 * @unknown ref android.R.styleable#TextView_maxLines
 * @unknown ref android.R.styleable#TextView_maxHeight
 * @unknown ref android.R.styleable#TextView_lines
 * @unknown ref android.R.styleable#TextView_height
 * @unknown ref android.R.styleable#TextView_minLines
 * @unknown ref android.R.styleable#TextView_minHeight
 * @unknown ref android.R.styleable#TextView_maxEms
 * @unknown ref android.R.styleable#TextView_maxWidth
 * @unknown ref android.R.styleable#TextView_ems
 * @unknown ref android.R.styleable#TextView_width
 * @unknown ref android.R.styleable#TextView_minEms
 * @unknown ref android.R.styleable#TextView_minWidth
 * @unknown ref android.R.styleable#TextView_gravity
 * @unknown ref android.R.styleable#TextView_scrollHorizontally
 * @unknown ref android.R.styleable#TextView_password
 * @unknown ref android.R.styleable#TextView_singleLine
 * @unknown ref android.R.styleable#TextView_selectAllOnFocus
 * @unknown ref android.R.styleable#TextView_includeFontPadding
 * @unknown ref android.R.styleable#TextView_maxLength
 * @unknown ref android.R.styleable#TextView_shadowColor
 * @unknown ref android.R.styleable#TextView_shadowDx
 * @unknown ref android.R.styleable#TextView_shadowDy
 * @unknown ref android.R.styleable#TextView_shadowRadius
 * @unknown ref android.R.styleable#TextView_autoLink
 * @unknown ref android.R.styleable#TextView_linksClickable
 * @unknown ref android.R.styleable#TextView_numeric
 * @unknown ref android.R.styleable#TextView_digits
 * @unknown ref android.R.styleable#TextView_phoneNumber
 * @unknown ref android.R.styleable#TextView_inputMethod
 * @unknown ref android.R.styleable#TextView_capitalize
 * @unknown ref android.R.styleable#TextView_autoText
 * @unknown ref android.R.styleable#TextView_editable
 * @unknown ref android.R.styleable#TextView_freezesText
 * @unknown ref android.R.styleable#TextView_ellipsize
 * @unknown ref android.R.styleable#TextView_drawableTop
 * @unknown ref android.R.styleable#TextView_drawableBottom
 * @unknown ref android.R.styleable#TextView_drawableRight
 * @unknown ref android.R.styleable#TextView_drawableLeft
 * @unknown ref android.R.styleable#TextView_drawableStart
 * @unknown ref android.R.styleable#TextView_drawableEnd
 * @unknown ref android.R.styleable#TextView_drawablePadding
 * @unknown ref android.R.styleable#TextView_drawableTint
 * @unknown ref android.R.styleable#TextView_drawableTintMode
 * @unknown ref android.R.styleable#TextView_lineSpacingExtra
 * @unknown ref android.R.styleable#TextView_lineSpacingMultiplier
 * @unknown ref android.R.styleable#TextView_justificationMode
 * @unknown ref android.R.styleable#TextView_marqueeRepeatLimit
 * @unknown ref android.R.styleable#TextView_inputType
 * @unknown ref android.R.styleable#TextView_imeOptions
 * @unknown ref android.R.styleable#TextView_privateImeOptions
 * @unknown ref android.R.styleable#TextView_imeActionLabel
 * @unknown ref android.R.styleable#TextView_imeActionId
 * @unknown ref android.R.styleable#TextView_editorExtras
 * @unknown ref android.R.styleable#TextView_elegantTextHeight
 * @unknown ref android.R.styleable#TextView_fallbackLineSpacing
 * @unknown ref android.R.styleable#TextView_letterSpacing
 * @unknown ref android.R.styleable#TextView_fontFeatureSettings
 * @unknown ref android.R.styleable#TextView_fontVariationSettings
 * @unknown ref android.R.styleable#TextView_breakStrategy
 * @unknown ref android.R.styleable#TextView_hyphenationFrequency
 * @unknown ref android.R.styleable#TextView_autoSizeTextType
 * @unknown ref android.R.styleable#TextView_autoSizeMinTextSize
 * @unknown ref android.R.styleable#TextView_autoSizeMaxTextSize
 * @unknown ref android.R.styleable#TextView_autoSizeStepGranularity
 * @unknown ref android.R.styleable#TextView_autoSizePresetSizes
 * @unknown ref android.R.styleable#TextView_textCursorDrawable
 * @unknown ref android.R.styleable#TextView_textSelectHandle
 * @unknown ref android.R.styleable#TextView_textSelectHandleLeft
 * @unknown ref android.R.styleable#TextView_textSelectHandleRight
 * @unknown ref android.R.styleable#TextView_allowUndo
 * @unknown ref android.R.styleable#TextView_enabled
 */
@android.widget.RemoteViews.RemoteView
public class TextView extends android.view.View implements android.view.ViewTreeObserver.OnPreDrawListener {
    static final java.lang.String LOG_TAG = "TextView";

    static final boolean DEBUG_EXTRACT = false;

    private static final float[] TEMP_POSITION = new float[2];

    // Enum for the "typeface" XML parameter.
    // TODO: How can we get this from the XML instead of hardcoding it here?
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ TextView.DEFAULT_TYPEFACE, TextView.SANS, TextView.SERIF, TextView.MONOSPACE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface XMLTypefaceAttr {}

    private static final int DEFAULT_TYPEFACE = -1;

    private static final int SANS = 1;

    private static final int SERIF = 2;

    private static final int MONOSPACE = 3;

    // Enum for the "ellipsize" XML parameter.
    private static final int ELLIPSIZE_NOT_SET = -1;

    private static final int ELLIPSIZE_NONE = 0;

    private static final int ELLIPSIZE_START = 1;

    private static final int ELLIPSIZE_MIDDLE = 2;

    private static final int ELLIPSIZE_END = 3;

    private static final int ELLIPSIZE_MARQUEE = 4;

    // Bitfield for the "numeric" XML parameter.
    // TODO: How can we get this from the XML instead of hardcoding it here?
    private static final int SIGNED = 2;

    private static final int DECIMAL = 4;

    /**
     * Draw marquee text with fading edges as usual
     */
    private static final int MARQUEE_FADE_NORMAL = 0;

    /**
     * Draw marquee text as ellipsize end while inactive instead of with the fade.
     * (Useful for devices where the fade can be expensive if overdone)
     */
    private static final int MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS = 1;

    /**
     * Draw marquee text with fading edges because it is currently active/animating.
     */
    private static final int MARQUEE_FADE_SWITCH_SHOW_FADE = 2;

    @android.annotation.UnsupportedAppUsage
    private static final int LINES = 1;

    private static final int EMS = LINES;

    private static final int PIXELS = 2;

    private static final android.graphics.RectF TEMP_RECTF = new android.graphics.RectF();

    /**
     *
     *
     * @unknown 
     */
    static final int VERY_WIDE = 1024 * 1024;// XXX should be much larger


    private static final int ANIMATED_SCROLL_GAP = 250;

    private static final android.text.InputFilter[] NO_FILTERS = new android.text.InputFilter[0];

    private static final android.text.Spanned EMPTY_SPANNED = new android.text.SpannedString("");

    private static final int CHANGE_WATCHER_PRIORITY = 100;

    // New state used to change background based on whether this TextView is multiline.
    private static final int[] MULTILINE_STATE_SET = new int[]{ R.attr.state_multiline };

    // Accessibility action to share selected text.
    private static final int ACCESSIBILITY_ACTION_SHARE = 0x10000000;

    /**
     *
     *
     * @unknown 
     */
    // Accessibility action start id for "process text" actions.
    static final int ACCESSIBILITY_ACTION_PROCESS_TEXT_START_ID = 0x10000100;

    /**
     *
     *
     * @unknown 
     */
    static final int PROCESS_TEXT_REQUEST_CODE = 100;

    /**
     * Return code of {@link #doKeyDown}.
     */
    private static final int KEY_EVENT_NOT_HANDLED = 0;

    private static final int KEY_EVENT_HANDLED = -1;

    private static final int KEY_DOWN_HANDLED_BY_KEY_LISTENER = 1;

    private static final int KEY_DOWN_HANDLED_BY_MOVEMENT_METHOD = 2;

    private static final int FLOATING_TOOLBAR_SELECT_ALL_REFRESH_DELAY = 500;

    // System wide time for last cut, copy or text changed action.
    static long sLastCutCopyOrTextChangedTime;

    private android.content.res.ColorStateList mTextColor;

    private android.content.res.ColorStateList mHintTextColor;

    private android.content.res.ColorStateList mLinkTextColor;

    /**
     * {@link #setTextColor(int)} or {@link #getCurrentTextColor()} should be used instead.
     */
    @android.view.ViewDebug.ExportedProperty(category = "text")
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P)
    private int mCurTextColor;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 115609023)
    private int mCurHintTextColor;

    private boolean mFreezesText;

    @android.annotation.UnsupportedAppUsage
    private Editable.Factory mEditableFactory = Editable.Factory.getInstance();

    @android.annotation.UnsupportedAppUsage
    private Spannable.Factory mSpannableFactory = Spannable.Factory.getInstance();

    @android.annotation.UnsupportedAppUsage
    private float mShadowRadius;

    @android.annotation.UnsupportedAppUsage
    private float mShadowDx;

    @android.annotation.UnsupportedAppUsage
    private float mShadowDy;

    private int mShadowColor;

    private boolean mPreDrawRegistered;

    private boolean mPreDrawListenerDetached;

    private android.view.textclassifier.TextClassifier mTextClassifier;

    private android.view.textclassifier.TextClassifier mTextClassificationSession;

    private android.view.textclassifier.TextClassificationContext mTextClassificationContext;

    // A flag to prevent repeated movements from escaping the enclosing text view. The idea here is
    // that if a user is holding down a movement key to traverse text, we shouldn't also traverse
    // the view hierarchy. On the other hand, if the user is using the movement key to traverse
    // views (i.e. the first movement was to traverse out of this view, or this view was traversed
    // into by the user holding the movement key down) then we shouldn't prevent the focus from
    // changing.
    private boolean mPreventDefaultMovement;

    private android.text.TextUtils.TruncateAt mEllipsize;

    static class Drawables {
        static final int LEFT = 0;

        static final int TOP = 1;

        static final int RIGHT = 2;

        static final int BOTTOM = 3;

        static final int DRAWABLE_NONE = -1;

        static final int DRAWABLE_RIGHT = 0;

        static final int DRAWABLE_LEFT = 1;

        final android.graphics.Rect mCompoundRect = new android.graphics.Rect();

        final android.graphics.drawable.Drawable[] mShowing = new android.graphics.drawable.Drawable[4];

        android.content.res.ColorStateList mTintList;

        android.graphics.BlendMode mBlendMode;

        boolean mHasTint;

        boolean mHasTintMode;

        android.graphics.drawable.Drawable mDrawableStart;

        android.graphics.drawable.Drawable mDrawableEnd;

        android.graphics.drawable.Drawable mDrawableError;

        android.graphics.drawable.Drawable mDrawableTemp;

        android.graphics.drawable.Drawable mDrawableLeftInitial;

        android.graphics.drawable.Drawable mDrawableRightInitial;

        boolean mIsRtlCompatibilityMode;

        boolean mOverride;

        int mDrawableSizeTop;

        int mDrawableSizeBottom;

        int mDrawableSizeLeft;

        int mDrawableSizeRight;

        int mDrawableSizeStart;

        int mDrawableSizeEnd;

        int mDrawableSizeError;

        int mDrawableSizeTemp;

        int mDrawableWidthTop;

        int mDrawableWidthBottom;

        int mDrawableHeightLeft;

        int mDrawableHeightRight;

        int mDrawableHeightStart;

        int mDrawableHeightEnd;

        int mDrawableHeightError;

        int mDrawableHeightTemp;

        int mDrawablePadding;

        int mDrawableSaved = TextView.Drawables.DRAWABLE_NONE;

        public Drawables(android.content.Context context) {
            final int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
            mIsRtlCompatibilityMode = (targetSdkVersion < android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) || (!context.getApplicationInfo().hasRtlSupport());
            mOverride = false;
        }

        /**
         *
         *
         * @return {@code true} if this object contains metadata that needs to
        be retained, {@code false} otherwise
         */
        public boolean hasMetadata() {
            return ((mDrawablePadding != 0) || mHasTintMode) || mHasTint;
        }

        /**
         * Updates the list of displayed drawables to account for the current
         * layout direction.
         *
         * @param layoutDirection
         * 		the current layout direction
         * @return {@code true} if the displayed drawables changed
         */
        public boolean resolveWithLayoutDirection(int layoutDirection) {
            final android.graphics.drawable.Drawable previousLeft = mShowing[TextView.Drawables.LEFT];
            final android.graphics.drawable.Drawable previousRight = mShowing[TextView.Drawables.RIGHT];
            // First reset "left" and "right" drawables to their initial values
            mShowing[TextView.Drawables.LEFT] = mDrawableLeftInitial;
            mShowing[TextView.Drawables.RIGHT] = mDrawableRightInitial;
            if (mIsRtlCompatibilityMode) {
                // Use "start" drawable as "left" drawable if the "left" drawable was not defined
                if ((mDrawableStart != null) && (mShowing[TextView.Drawables.LEFT] == null)) {
                    mShowing[TextView.Drawables.LEFT] = mDrawableStart;
                    mDrawableSizeLeft = mDrawableSizeStart;
                    mDrawableHeightLeft = mDrawableHeightStart;
                }
                // Use "end" drawable as "right" drawable if the "right" drawable was not defined
                if ((mDrawableEnd != null) && (mShowing[TextView.Drawables.RIGHT] == null)) {
                    mShowing[TextView.Drawables.RIGHT] = mDrawableEnd;
                    mDrawableSizeRight = mDrawableSizeEnd;
                    mDrawableHeightRight = mDrawableHeightEnd;
                }
            } else {
                // JB-MR1+ normal case: "start" / "end" drawables are overriding "left" / "right"
                // drawable if and only if they have been defined
                switch (layoutDirection) {
                    case android.view.View.LAYOUT_DIRECTION_RTL :
                        if (mOverride) {
                            mShowing[TextView.Drawables.RIGHT] = mDrawableStart;
                            mDrawableSizeRight = mDrawableSizeStart;
                            mDrawableHeightRight = mDrawableHeightStart;
                            mShowing[TextView.Drawables.LEFT] = mDrawableEnd;
                            mDrawableSizeLeft = mDrawableSizeEnd;
                            mDrawableHeightLeft = mDrawableHeightEnd;
                        }
                        break;
                    case android.view.View.LAYOUT_DIRECTION_LTR :
                    default :
                        if (mOverride) {
                            mShowing[TextView.Drawables.LEFT] = mDrawableStart;
                            mDrawableSizeLeft = mDrawableSizeStart;
                            mDrawableHeightLeft = mDrawableHeightStart;
                            mShowing[TextView.Drawables.RIGHT] = mDrawableEnd;
                            mDrawableSizeRight = mDrawableSizeEnd;
                            mDrawableHeightRight = mDrawableHeightEnd;
                        }
                        break;
                }
            }
            applyErrorDrawableIfNeeded(layoutDirection);
            return (mShowing[TextView.Drawables.LEFT] != previousLeft) || (mShowing[TextView.Drawables.RIGHT] != previousRight);
        }

        public void setErrorDrawable(android.graphics.drawable.Drawable dr, TextView tv) {
            if ((mDrawableError != dr) && (mDrawableError != null)) {
                mDrawableError.setCallback(null);
            }
            mDrawableError = dr;
            if (mDrawableError != null) {
                final android.graphics.Rect compoundRect = mCompoundRect;
                final int[] state = tv.getDrawableState();
                mDrawableError.setState(state);
                mDrawableError.copyBounds(compoundRect);
                mDrawableError.setCallback(tv);
                mDrawableSizeError = compoundRect.width();
                mDrawableHeightError = compoundRect.height();
            } else {
                mDrawableSizeError = mDrawableHeightError = 0;
            }
        }

        private void applyErrorDrawableIfNeeded(int layoutDirection) {
            // first restore the initial state if needed
            switch (mDrawableSaved) {
                case TextView.Drawables.DRAWABLE_LEFT :
                    mShowing[TextView.Drawables.LEFT] = mDrawableTemp;
                    mDrawableSizeLeft = mDrawableSizeTemp;
                    mDrawableHeightLeft = mDrawableHeightTemp;
                    break;
                case TextView.Drawables.DRAWABLE_RIGHT :
                    mShowing[TextView.Drawables.RIGHT] = mDrawableTemp;
                    mDrawableSizeRight = mDrawableSizeTemp;
                    mDrawableHeightRight = mDrawableHeightTemp;
                    break;
                case TextView.Drawables.DRAWABLE_NONE :
                default :
            }
            // then, if needed, assign the Error drawable to the correct location
            if (mDrawableError != null) {
                switch (layoutDirection) {
                    case android.view.View.LAYOUT_DIRECTION_RTL :
                        mDrawableSaved = TextView.Drawables.DRAWABLE_LEFT;
                        mDrawableTemp = mShowing[TextView.Drawables.LEFT];
                        mDrawableSizeTemp = mDrawableSizeLeft;
                        mDrawableHeightTemp = mDrawableHeightLeft;
                        mShowing[TextView.Drawables.LEFT] = mDrawableError;
                        mDrawableSizeLeft = mDrawableSizeError;
                        mDrawableHeightLeft = mDrawableHeightError;
                        break;
                    case android.view.View.LAYOUT_DIRECTION_LTR :
                    default :
                        mDrawableSaved = TextView.Drawables.DRAWABLE_RIGHT;
                        mDrawableTemp = mShowing[TextView.Drawables.RIGHT];
                        mDrawableSizeTemp = mDrawableSizeRight;
                        mDrawableHeightTemp = mDrawableHeightRight;
                        mShowing[TextView.Drawables.RIGHT] = mDrawableError;
                        mDrawableSizeRight = mDrawableSizeError;
                        mDrawableHeightRight = mDrawableHeightError;
                        break;
                }
            }
        }
    }

    @android.annotation.UnsupportedAppUsage
    TextView.Drawables mDrawables;

    @android.annotation.UnsupportedAppUsage
    private TextView.CharWrapper mCharWrapper;

    @android.annotation.UnsupportedAppUsage(trackingBug = 124050217)
    private TextView.Marquee mMarquee;

    @android.annotation.UnsupportedAppUsage
    private boolean mRestartMarquee;

    private int mMarqueeRepeatLimit = 3;

    private int mLastLayoutDirection = -1;

    /**
     * On some devices the fading edges add a performance penalty if used
     * extensively in the same layout. This mode indicates how the marquee
     * is currently being shown, if applicable. (mEllipsize will == MARQUEE)
     */
    @android.annotation.UnsupportedAppUsage
    private int mMarqueeFadeMode = TextView.MARQUEE_FADE_NORMAL;

    /**
     * When mMarqueeFadeMode is not MARQUEE_FADE_NORMAL, this stores
     * the layout that should be used when the mode switches.
     */
    @android.annotation.UnsupportedAppUsage
    private android.text.Layout mSavedMarqueeModeLayout;

    // Do not update following mText/mSpannable/mPrecomputed except for setTextInternal()
    @android.view.ViewDebug.ExportedProperty(category = "text")
    @android.annotation.UnsupportedAppUsage
    @android.annotation.Nullable
    private java.lang.CharSequence mText;

    @android.annotation.Nullable
    private android.text.Spannable mSpannable;

    @android.annotation.Nullable
    private android.text.PrecomputedText mPrecomputed;

    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence mTransformed;

    @android.annotation.UnsupportedAppUsage
    private TextView.BufferType mBufferType = TextView.BufferType.NORMAL;

    private java.lang.CharSequence mHint;

    @android.annotation.UnsupportedAppUsage
    private android.text.Layout mHintLayout;

    private android.text.method.MovementMethod mMovement;

    private android.text.method.TransformationMethod mTransformation;

    @android.annotation.UnsupportedAppUsage
    private boolean mAllowTransformationLengthChange;

    @android.annotation.UnsupportedAppUsage
    private TextView.ChangeWatcher mChangeWatcher;

    @android.annotation.UnsupportedAppUsage(trackingBug = 123769451)
    private java.util.ArrayList<android.text.TextWatcher> mListeners;

    // display attributes
    @android.annotation.UnsupportedAppUsage
    private final android.text.TextPaint mTextPaint;

    @android.annotation.UnsupportedAppUsage
    private boolean mUserSetTextScaleX;

    @android.annotation.UnsupportedAppUsage
    private android.text.Layout mLayout;

    private boolean mLocalesChanged = false;

    // True if setKeyListener() has been explicitly called
    private boolean mListenerChanged = false;

    // True if internationalized input should be used for numbers and date and time.
    private final boolean mUseInternationalizedInput;

    // True if fallback fonts that end up getting used should be allowed to affect line spacing.
    /* package */
    boolean mUseFallbackLineSpacing;

    @android.view.ViewDebug.ExportedProperty(category = "text")
    @android.annotation.UnsupportedAppUsage
    private int mGravity = android.view.Gravity.TOP | android.view.Gravity.START;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P)
    private boolean mHorizontallyScrolling;

    private int mAutoLinkMask;

    private boolean mLinksClickable = true;

    @android.annotation.UnsupportedAppUsage
    private float mSpacingMult = 1.0F;

    @android.annotation.UnsupportedAppUsage
    private float mSpacingAdd = 0.0F;

    private int mBreakStrategy;

    private int mHyphenationFrequency;

    private int mJustificationMode;

    @android.annotation.UnsupportedAppUsage
    private int mMaximum = MAX_VALUE;

    @android.annotation.UnsupportedAppUsage
    private int mMaxMode = LINES;

    @android.annotation.UnsupportedAppUsage
    private int mMinimum = 0;

    @android.annotation.UnsupportedAppUsage
    private int mMinMode = LINES;

    @android.annotation.UnsupportedAppUsage
    private int mOldMaximum = mMaximum;

    @android.annotation.UnsupportedAppUsage
    private int mOldMaxMode = mMaxMode;

    @android.annotation.UnsupportedAppUsage
    private int mMaxWidth = MAX_VALUE;

    @android.annotation.UnsupportedAppUsage
    private int mMaxWidthMode = TextView.PIXELS;

    @android.annotation.UnsupportedAppUsage
    private int mMinWidth = 0;

    @android.annotation.UnsupportedAppUsage
    private int mMinWidthMode = TextView.PIXELS;

    @android.annotation.UnsupportedAppUsage
    private boolean mSingleLine;

    @android.annotation.UnsupportedAppUsage
    private int mDesiredHeightAtMeasure = -1;

    @android.annotation.UnsupportedAppUsage
    private boolean mIncludePad = true;

    private int mDeferScroll = -1;

    // tmp primitives, so we don't alloc them on each draw
    private android.graphics.Rect mTempRect;

    private long mLastScroll;

    private android.widget.Scroller mScroller;

    private android.text.TextPaint mTempTextPaint;

    @android.annotation.UnsupportedAppUsage
    private BoringLayout.Metrics mBoring;

    @android.annotation.UnsupportedAppUsage
    private BoringLayout.Metrics mHintBoring;

    @android.annotation.UnsupportedAppUsage
    private android.text.BoringLayout mSavedLayout;

    @android.annotation.UnsupportedAppUsage
    private android.text.BoringLayout mSavedHintLayout;

    @android.annotation.UnsupportedAppUsage
    private android.text.TextDirectionHeuristic mTextDir;

    private android.text.InputFilter[] mFilters = TextView.NO_FILTERS;

    /**
     * {@link UserHandle} that represents the logical owner of the text. {@code null} when it is
     * the same as {@link Process#myUserHandle()}.
     *
     * <p>Most of applications should not worry about this. Some privileged apps that host UI for
     * other apps may need to set this so that the system can use right user's resources and
     * services such as input methods and spell checkers.</p>
     *
     * @see #setTextOperationUser(UserHandle)
     */
    @android.annotation.Nullable
    private android.os.UserHandle mTextOperationUser;

    private volatile java.util.Locale mCurrentSpellCheckerLocaleCache;

    // It is possible to have a selection even when mEditor is null (programmatically set, like when
    // a link is pressed). These highlight-related fields do not go in mEditor.
    @android.annotation.UnsupportedAppUsage
    int mHighlightColor = 0x6633b5e5;

    private android.graphics.Path mHighlightPath;

    @android.annotation.UnsupportedAppUsage
    private final android.graphics.Paint mHighlightPaint;

    @android.annotation.UnsupportedAppUsage
    private boolean mHighlightPathBogus = true;

    // Although these fields are specific to editable text, they are not added to Editor because
    // they are defined by the TextView's style and are theme-dependent.
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P)
    int mCursorDrawableRes;

    private android.graphics.drawable.Drawable mCursorDrawable;

    // Note: this might be stale if setTextSelectHandleLeft is used. We could simplify the code
    // by removing it, but we would break apps targeting <= P that use it by reflection.
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P)
    int mTextSelectHandleLeftRes;

    private android.graphics.drawable.Drawable mTextSelectHandleLeft;

    // Note: this might be stale if setTextSelectHandleRight is used. We could simplify the code
    // by removing it, but we would break apps targeting <= P that use it by reflection.
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P)
    int mTextSelectHandleRightRes;

    private android.graphics.drawable.Drawable mTextSelectHandleRight;

    // Note: this might be stale if setTextSelectHandle is used. We could simplify the code
    // by removing it, but we would break apps targeting <= P that use it by reflection.
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P)
    int mTextSelectHandleRes;

    private android.graphics.drawable.Drawable mTextSelectHandle;

    int mTextEditSuggestionItemLayout;

    int mTextEditSuggestionContainerLayout;

    int mTextEditSuggestionHighlightStyle;

    /**
     * {@link EditText} specific data, created on demand when one of the Editor fields is used.
     * See {@link #createEditorIfNeeded()}.
     */
    @android.annotation.UnsupportedAppUsage
    private android.widget.Editor mEditor;

    private static final int DEVICE_PROVISIONED_UNKNOWN = 0;

    private static final int DEVICE_PROVISIONED_NO = 1;

    private static final int DEVICE_PROVISIONED_YES = 2;

    /**
     * Some special options such as sharing selected text should only be shown if the device
     * is provisioned. Only check the provisioned state once for a given view instance.
     */
    private int mDeviceProvisionedState = TextView.DEVICE_PROVISIONED_UNKNOWN;

    /**
     * The TextView does not auto-size text (default).
     */
    public static final int AUTO_SIZE_TEXT_TYPE_NONE = 0;

    /**
     * The TextView scales text size both horizontally and vertically to fit within the
     * container.
     */
    public static final int AUTO_SIZE_TEXT_TYPE_UNIFORM = 1;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "AUTO_SIZE_TEXT_TYPE_" }, value = { TextView.AUTO_SIZE_TEXT_TYPE_NONE, TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface AutoSizeTextType {}

    // Default minimum size for auto-sizing text in scaled pixels.
    private static final int DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP = 12;

    // Default maximum size for auto-sizing text in scaled pixels.
    private static final int DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP = 112;

    // Default value for the step size in pixels.
    private static final int DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX = 1;

    // Use this to specify that any of the auto-size configuration int values have not been set.
    private static final float UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE = -1.0F;

    // Auto-size text type.
    private int mAutoSizeTextType = TextView.AUTO_SIZE_TEXT_TYPE_NONE;

    // Specify if auto-size text is needed.
    private boolean mNeedsAutoSizeText = false;

    // Step size for auto-sizing in pixels.
    private float mAutoSizeStepGranularityInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;

    // Minimum text size for auto-sizing in pixels.
    private float mAutoSizeMinTextSizeInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;

    // Maximum text size for auto-sizing in pixels.
    private float mAutoSizeMaxTextSizeInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;

    // Contains a (specified or computed) distinct sorted set of text sizes in pixels to pick from
    // when auto-sizing text.
    private int[] mAutoSizeTextSizesInPx = libcore.util.EmptyArray.INT;

    // Specifies whether auto-size should use the provided auto size steps set or if it should
    // build the steps set using mAutoSizeMinTextSizeInPx, mAutoSizeMaxTextSizeInPx and
    // mAutoSizeStepGranularityInPx.
    private boolean mHasPresetAutoSizeValues = false;

    // Autofill-related attributes
    // 
    // Indicates whether the text was set statically or dynamically, so it can be used to
    // sanitize autofill requests.
    private boolean mTextSetFromXmlOrResourceId = false;

    // Resource id used to set the text.
    @android.annotation.StringRes
    private int mTextId = android.content.res.Resources.ID_NULL;

    // 
    // End of autofill-related attributes
    /**
     * Kick-start the font cache for the zygote process (to pay the cost of
     * initializing freetype for our default font only once).
     *
     * @unknown 
     */
    public static void preloadFontCache() {
        android.graphics.Paint p = new android.graphics.Paint();
        p.setAntiAlias(true);
        // Ensure that the Typeface is loaded here.
        // Typically, Typeface is preloaded by zygote but not on all devices, e.g. Android Auto.
        // So, sets Typeface.DEFAULT explicitly here for ensuring that the Typeface is loaded here
        // since Paint.measureText can not be called without Typeface static initializer.
        p.setTypeface(android.graphics.Typeface.DEFAULT);
        // We don't care about the result, just the side-effect of measuring.
        p.measureText("H");
    }

    /**
     * Interface definition for a callback to be invoked when an action is
     * performed on the editor.
     */
    public interface OnEditorActionListener {
        /**
         * Called when an action is being performed.
         *
         * @param v
         * 		The view that was clicked.
         * @param actionId
         * 		Identifier of the action.  This will be either the
         * 		identifier you supplied, or {@link EditorInfo#IME_NULL
         * 		EditorInfo.IME_NULL} if being called due to the enter key
         * 		being pressed.
         * @param event
         * 		If triggered by an enter key, this is the event;
         * 		otherwise, this is null.
         * @return Return true if you have consumed the action, else false.
         */
        boolean onEditorAction(TextView v, int actionId, android.view.KeyEvent event);
    }

    public TextView(android.content.Context context) {
        this(context, null);
    }

    public TextView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.textViewStyle);
    }

    public TextView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @java.lang.SuppressWarnings("deprecation")
    public TextView(android.content.Context context, @android.annotation.Nullable
    android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        // TextView is important by default, unless app developer overrode attribute.
        if (getImportantForAutofill() == android.view.View.IMPORTANT_FOR_AUTOFILL_AUTO) {
            setImportantForAutofill(android.view.View.IMPORTANT_FOR_AUTOFILL_YES);
        }
        setTextInternal("");
        final android.content.res.Resources res = getResources();
        final android.content.res.CompatibilityInfo compat = res.getCompatibilityInfo();
        mTextPaint = new android.text.TextPaint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = res.getDisplayMetrics().density;
        mTextPaint.setCompatibilityScaling(compat.applicationScale);
        mHighlightPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
        mHighlightPaint.setCompatibilityScaling(compat.applicationScale);
        mMovement = getDefaultMovementMethod();
        mTransformation = null;
        final TextView.TextAppearanceAttributes attributes = new TextView.TextAppearanceAttributes();
        attributes.mTextColor = android.content.res.ColorStateList.valueOf(0xff000000);
        attributes.mTextSize = 15;
        mBreakStrategy = android.text.Layout.BREAK_STRATEGY_SIMPLE;
        mHyphenationFrequency = android.text.Layout.HYPHENATION_FREQUENCY_NONE;
        mJustificationMode = android.text.Layout.JUSTIFICATION_MODE_NONE;
        final android.content.res.Resources.Theme theme = context.getTheme();
        /* Look the appearance up without checking first if it exists because
        almost every TextView has one and it greatly simplifies the logic
        to be able to parse the appearance first and then let specific tags
        for this View override it.
         */
        android.content.res.TypedArray a = theme.obtainStyledAttributes(attrs, android.widget.com.android.internal.R.styleable.TextViewAppearance, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, android.widget.com.android.internal.R.styleable.TextViewAppearance, attrs, a, defStyleAttr, defStyleRes);
        android.content.res.TypedArray appearance = null;
        int ap = a.getResourceId(android.widget.com.android.internal.R.styleable.TextViewAppearance_textAppearance, -1);
        a.recycle();
        if (ap != (-1)) {
            appearance = theme.obtainStyledAttributes(ap, android.widget.com.android.internal.R.styleable.TextAppearance);
            saveAttributeDataForStyleable(context, android.widget.com.android.internal.R.styleable.TextAppearance, null, appearance, 0, ap);
        }
        if (appearance != null) {
            /* styleArray */
            readTextAppearance(context, appearance, attributes, false);
            attributes.mFontFamilyExplicit = false;
            appearance.recycle();
        }
        boolean editable = getDefaultEditable();
        java.lang.CharSequence inputMethod = null;
        int numeric = 0;
        java.lang.CharSequence digits = null;
        boolean phone = false;
        boolean autotext = false;
        int autocap = -1;
        int buffertype = 0;
        boolean selectallonfocus = false;
        android.graphics.drawable.Drawable drawableLeft = null;
        android.graphics.drawable.Drawable drawableTop = null;
        android.graphics.drawable.Drawable drawableRight = null;
        android.graphics.drawable.Drawable drawableBottom = null;
        android.graphics.drawable.Drawable drawableStart = null;
        android.graphics.drawable.Drawable drawableEnd = null;
        android.content.res.ColorStateList drawableTint = null;
        android.graphics.BlendMode drawableTintMode = null;
        int drawablePadding = 0;
        int ellipsize = TextView.ELLIPSIZE_NOT_SET;
        boolean singleLine = false;
        int maxlength = -1;
        java.lang.CharSequence text = "";
        java.lang.CharSequence hint = null;
        boolean password = false;
        float autoSizeMinTextSizeInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        float autoSizeMaxTextSizeInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        float autoSizeStepGranularityInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        int inputType = TYPE_NULL;
        a = theme.obtainStyledAttributes(attrs, android.widget.com.android.internal.R.styleable.TextView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, android.widget.com.android.internal.R.styleable.TextView, attrs, a, defStyleAttr, defStyleRes);
        int firstBaselineToTopHeight = -1;
        int lastBaselineToBottomHeight = -1;
        int lineHeight = -1;
        /* styleArray */
        readTextAppearance(context, a, attributes, true);
        int n = a.getIndexCount();
        // Must set id in a temporary variable because it will be reset by setText()
        boolean textIsSetFromXml = false;
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case android.widget.com.android.internal.R.styleable.TextView_editable :
                    editable = a.getBoolean(attr, editable);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_inputMethod :
                    inputMethod = a.getText(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_numeric :
                    numeric = a.getInt(attr, numeric);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_digits :
                    digits = a.getText(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_phoneNumber :
                    phone = a.getBoolean(attr, phone);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_autoText :
                    autotext = a.getBoolean(attr, autotext);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_capitalize :
                    autocap = a.getInt(attr, autocap);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_bufferType :
                    buffertype = a.getInt(attr, buffertype);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_selectAllOnFocus :
                    selectallonfocus = a.getBoolean(attr, selectallonfocus);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_autoLink :
                    mAutoLinkMask = a.getInt(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_linksClickable :
                    mLinksClickable = a.getBoolean(attr, true);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableLeft :
                    drawableLeft = a.getDrawable(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableTop :
                    drawableTop = a.getDrawable(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableRight :
                    drawableRight = a.getDrawable(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableBottom :
                    drawableBottom = a.getDrawable(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableStart :
                    drawableStart = a.getDrawable(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableEnd :
                    drawableEnd = a.getDrawable(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableTint :
                    drawableTint = a.getColorStateList(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawableTintMode :
                    drawableTintMode = android.graphics.drawable.Drawable.parseBlendMode(a.getInt(attr, -1), drawableTintMode);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_drawablePadding :
                    drawablePadding = a.getDimensionPixelSize(attr, drawablePadding);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_maxLines :
                    setMaxLines(a.getInt(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_maxHeight :
                    setMaxHeight(a.getDimensionPixelSize(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_lines :
                    setLines(a.getInt(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_height :
                    setHeight(a.getDimensionPixelSize(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_minLines :
                    setMinLines(a.getInt(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_minHeight :
                    setMinHeight(a.getDimensionPixelSize(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_maxEms :
                    setMaxEms(a.getInt(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_maxWidth :
                    setMaxWidth(a.getDimensionPixelSize(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_ems :
                    setEms(a.getInt(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_width :
                    setWidth(a.getDimensionPixelSize(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_minEms :
                    setMinEms(a.getInt(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_minWidth :
                    setMinWidth(a.getDimensionPixelSize(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_gravity :
                    setGravity(a.getInt(attr, -1));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_hint :
                    hint = a.getText(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_text :
                    textIsSetFromXml = true;
                    mTextId = a.getResourceId(attr, android.content.res.Resources.ID_NULL);
                    text = a.getText(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_scrollHorizontally :
                    if (a.getBoolean(attr, false)) {
                        setHorizontallyScrolling(true);
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_singleLine :
                    singleLine = a.getBoolean(attr, singleLine);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_ellipsize :
                    ellipsize = a.getInt(attr, ellipsize);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_marqueeRepeatLimit :
                    setMarqueeRepeatLimit(a.getInt(attr, mMarqueeRepeatLimit));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_includeFontPadding :
                    if (!a.getBoolean(attr, true)) {
                        setIncludeFontPadding(false);
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_cursorVisible :
                    if (!a.getBoolean(attr, true)) {
                        setCursorVisible(false);
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_maxLength :
                    maxlength = a.getInt(attr, -1);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textScaleX :
                    setTextScaleX(a.getFloat(attr, 1.0F));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_freezesText :
                    mFreezesText = a.getBoolean(attr, false);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_enabled :
                    setEnabled(a.getBoolean(attr, isEnabled()));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_password :
                    password = a.getBoolean(attr, password);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_lineSpacingExtra :
                    mSpacingAdd = a.getDimensionPixelSize(attr, ((int) (mSpacingAdd)));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_lineSpacingMultiplier :
                    mSpacingMult = a.getFloat(attr, mSpacingMult);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_inputType :
                    inputType = a.getInt(attr, TYPE_NULL);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_allowUndo :
                    createEditorIfNeeded();
                    mEditor.mAllowUndo = a.getBoolean(attr, true);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_imeOptions :
                    createEditorIfNeeded();
                    mEditor.createInputContentTypeIfNeeded();
                    mEditor.mInputContentType.imeOptions = a.getInt(attr, mEditor.mInputContentType.imeOptions);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_imeActionLabel :
                    createEditorIfNeeded();
                    mEditor.createInputContentTypeIfNeeded();
                    mEditor.mInputContentType.imeActionLabel = a.getText(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_imeActionId :
                    createEditorIfNeeded();
                    mEditor.createInputContentTypeIfNeeded();
                    mEditor.mInputContentType.imeActionId = a.getInt(attr, mEditor.mInputContentType.imeActionId);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_privateImeOptions :
                    setPrivateImeOptions(a.getString(attr));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_editorExtras :
                    try {
                        setInputExtras(a.getResourceId(attr, 0));
                    } catch (org.xmlpull.v1.XmlPullParserException e) {
                        android.util.Log.w(TextView.LOG_TAG, "Failure reading input extras", e);
                    } catch (java.io.IOException e) {
                        android.util.Log.w(TextView.LOG_TAG, "Failure reading input extras", e);
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textCursorDrawable :
                    mCursorDrawableRes = a.getResourceId(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textSelectHandleLeft :
                    mTextSelectHandleLeftRes = a.getResourceId(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textSelectHandleRight :
                    mTextSelectHandleRightRes = a.getResourceId(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textSelectHandle :
                    mTextSelectHandleRes = a.getResourceId(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textEditSuggestionItemLayout :
                    mTextEditSuggestionItemLayout = a.getResourceId(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textEditSuggestionContainerLayout :
                    mTextEditSuggestionContainerLayout = a.getResourceId(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textEditSuggestionHighlightStyle :
                    mTextEditSuggestionHighlightStyle = a.getResourceId(attr, 0);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_textIsSelectable :
                    setTextIsSelectable(a.getBoolean(attr, false));
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_breakStrategy :
                    mBreakStrategy = a.getInt(attr, Layout.BREAK_STRATEGY_SIMPLE);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_hyphenationFrequency :
                    mHyphenationFrequency = a.getInt(attr, Layout.HYPHENATION_FREQUENCY_NONE);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_autoSizeTextType :
                    mAutoSizeTextType = a.getInt(attr, TextView.AUTO_SIZE_TEXT_TYPE_NONE);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_autoSizeStepGranularity :
                    autoSizeStepGranularityInPx = a.getDimension(attr, TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_autoSizeMinTextSize :
                    autoSizeMinTextSizeInPx = a.getDimension(attr, TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_autoSizeMaxTextSize :
                    autoSizeMaxTextSizeInPx = a.getDimension(attr, TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_autoSizePresetSizes :
                    final int autoSizeStepSizeArrayResId = a.getResourceId(attr, 0);
                    if (autoSizeStepSizeArrayResId > 0) {
                        final android.content.res.TypedArray autoSizePresetTextSizes = a.getResources().obtainTypedArray(autoSizeStepSizeArrayResId);
                        setupAutoSizeUniformPresetSizes(autoSizePresetTextSizes);
                        autoSizePresetTextSizes.recycle();
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_justificationMode :
                    mJustificationMode = a.getInt(attr, Layout.JUSTIFICATION_MODE_NONE);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_firstBaselineToTopHeight :
                    firstBaselineToTopHeight = a.getDimensionPixelSize(attr, -1);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_lastBaselineToBottomHeight :
                    lastBaselineToBottomHeight = a.getDimensionPixelSize(attr, -1);
                    break;
                case android.widget.com.android.internal.R.styleable.TextView_lineHeight :
                    lineHeight = a.getDimensionPixelSize(attr, -1);
                    break;
            }
        }
        a.recycle();
        TextView.BufferType bufferType = TextView.BufferType.EDITABLE;
        final int variation = inputType & (TYPE_MASK_CLASS | TYPE_MASK_VARIATION);
        final boolean passwordInputType = variation == (TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD);
        final boolean webPasswordInputType = variation == (TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_WEB_PASSWORD);
        final boolean numberPasswordInputType = variation == (TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD);
        final int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        mUseInternationalizedInput = targetSdkVersion >= android.os.Build.VERSION_CODES.O;
        mUseFallbackLineSpacing = targetSdkVersion >= android.os.Build.VERSION_CODES.P;
        if (inputMethod != null) {
            java.lang.Class<?> c;
            try {
                c = java.lang.Class.forName(inputMethod.toString());
            } catch (java.lang.ClassNotFoundException ex) {
                throw new java.lang.RuntimeException(ex);
            }
            try {
                createEditorIfNeeded();
                mEditor.mKeyListener = ((android.text.method.KeyListener) (c.newInstance()));
            } catch (java.lang.InstantiationException ex) {
                throw new java.lang.RuntimeException(ex);
            } catch (java.lang.IllegalAccessException ex) {
                throw new java.lang.RuntimeException(ex);
            }
            try {
                mEditor.mInputType = (inputType != TYPE_NULL) ? inputType : mEditor.mKeyListener.getInputType();
            } catch (java.lang.IncompatibleClassChangeError e) {
                mEditor.mInputType = TYPE_CLASS_TEXT;
            }
        } else
            if (digits != null) {
                createEditorIfNeeded();
                mEditor.mKeyListener = android.text.method.DigitsKeyListener.getInstance(digits.toString());
                // If no input type was specified, we will default to generic
                // text, since we can't tell the IME about the set of digits
                // that was selected.
                mEditor.mInputType = (inputType != TYPE_NULL) ? inputType : TYPE_CLASS_TEXT;
            } else
                if (inputType != TYPE_NULL) {
                    setInputType(inputType, true);
                    // If set, the input type overrides what was set using the deprecated singleLine flag.
                    singleLine = !TextView.isMultilineInputType(inputType);
                } else
                    if (phone) {
                        createEditorIfNeeded();
                        mEditor.mKeyListener = android.text.method.DialerKeyListener.getInstance();
                        mEditor.mInputType = inputType = TYPE_CLASS_PHONE;
                    } else
                        if (numeric != 0) {
                            createEditorIfNeeded();
                            mEditor.mKeyListener = // locale
                            android.text.method.DigitsKeyListener.getInstance(null, (numeric & TextView.SIGNED) != 0, (numeric & TextView.DECIMAL) != 0);
                            inputType = mEditor.mKeyListener.getInputType();
                            mEditor.mInputType = inputType;
                        } else
                            if (autotext || (autocap != (-1))) {
                                android.text.method.TextKeyListener.Capitalize cap;
                                inputType = TYPE_CLASS_TEXT;
                                switch (autocap) {
                                    case 1 :
                                        cap = TextKeyListener.Capitalize.SENTENCES;
                                        inputType |= TYPE_TEXT_FLAG_CAP_SENTENCES;
                                        break;
                                    case 2 :
                                        cap = TextKeyListener.Capitalize.WORDS;
                                        inputType |= TYPE_TEXT_FLAG_CAP_WORDS;
                                        break;
                                    case 3 :
                                        cap = TextKeyListener.Capitalize.CHARACTERS;
                                        inputType |= TYPE_TEXT_FLAG_CAP_CHARACTERS;
                                        break;
                                    default :
                                        cap = TextKeyListener.Capitalize.NONE;
                                        break;
                                }
                                createEditorIfNeeded();
                                mEditor.mKeyListener = android.text.method.TextKeyListener.getInstance(autotext, cap);
                                mEditor.mInputType = inputType;
                            } else
                                if (editable) {
                                    createEditorIfNeeded();
                                    mEditor.mKeyListener = android.text.method.TextKeyListener.getInstance();
                                    mEditor.mInputType = TYPE_CLASS_TEXT;
                                } else
                                    if (isTextSelectable()) {
                                        // Prevent text changes from keyboard.
                                        if (mEditor != null) {
                                            mEditor.mKeyListener = null;
                                            mEditor.mInputType = TYPE_NULL;
                                        }
                                        bufferType = TextView.BufferType.SPANNABLE;
                                        // So that selection can be changed using arrow keys and touch is handled.
                                        setMovementMethod(android.text.method.ArrowKeyMovementMethod.getInstance());
                                    } else {
                                        if (mEditor != null)
                                            mEditor.mKeyListener = null;

                                        switch (buffertype) {
                                            case 0 :
                                                bufferType = TextView.BufferType.NORMAL;
                                                break;
                                            case 1 :
                                                bufferType = TextView.BufferType.SPANNABLE;
                                                break;
                                            case 2 :
                                                bufferType = TextView.BufferType.EDITABLE;
                                                break;
                                        }
                                    }







        if (mEditor != null) {
            mEditor.adjustInputType(password, passwordInputType, webPasswordInputType, numberPasswordInputType);
        }
        if (selectallonfocus) {
            createEditorIfNeeded();
            mEditor.mSelectAllOnFocus = true;
            if (bufferType == TextView.BufferType.NORMAL) {
                bufferType = TextView.BufferType.SPANNABLE;
            }
        }
        // Set up the tint (if needed) before setting the drawables so that it
        // gets applied correctly.
        if ((drawableTint != null) || (drawableTintMode != null)) {
            if (mDrawables == null) {
                mDrawables = new TextView.Drawables(context);
            }
            if (drawableTint != null) {
                mDrawables.mTintList = drawableTint;
                mDrawables.mHasTint = true;
            }
            if (drawableTintMode != null) {
                mDrawables.mBlendMode = drawableTintMode;
                mDrawables.mHasTintMode = true;
            }
        }
        // This call will save the initial left/right drawables
        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom);
        setRelativeDrawablesIfNeeded(drawableStart, drawableEnd);
        setCompoundDrawablePadding(drawablePadding);
        // Same as setSingleLine(), but make sure the transformation method and the maximum number
        // of lines of height are unchanged for multi-line TextViews.
        setInputTypeSingleLine(singleLine);
        applySingleLine(singleLine, singleLine, singleLine);
        if ((singleLine && (getKeyListener() == null)) && (ellipsize == TextView.ELLIPSIZE_NOT_SET)) {
            ellipsize = TextView.ELLIPSIZE_END;
        }
        switch (ellipsize) {
            case TextView.ELLIPSIZE_START :
                setEllipsize(TextUtils.TruncateAt.START);
                break;
            case TextView.ELLIPSIZE_MIDDLE :
                setEllipsize(TextUtils.TruncateAt.MIDDLE);
                break;
            case TextView.ELLIPSIZE_END :
                setEllipsize(TextUtils.TruncateAt.END);
                break;
            case TextView.ELLIPSIZE_MARQUEE :
                if (android.view.ViewConfiguration.get(context).isFadingMarqueeEnabled()) {
                    setHorizontalFadingEdgeEnabled(true);
                    mMarqueeFadeMode = TextView.MARQUEE_FADE_NORMAL;
                } else {
                    setHorizontalFadingEdgeEnabled(false);
                    mMarqueeFadeMode = TextView.MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS;
                }
                setEllipsize(TextUtils.TruncateAt.MARQUEE);
                break;
        }
        final boolean isPassword = ((password || passwordInputType) || webPasswordInputType) || numberPasswordInputType;
        final boolean isMonospaceEnforced = isPassword || ((mEditor != null) && ((mEditor.mInputType & (TYPE_MASK_CLASS | TYPE_MASK_VARIATION)) == (TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD)));
        if (isMonospaceEnforced) {
            attributes.mTypefaceIndex = TextView.MONOSPACE;
        }
        applyTextAppearance(attributes);
        if (isPassword) {
            setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
        }
        if (maxlength >= 0) {
            setFilters(new android.text.InputFilter[]{ new android.text.InputFilter.LengthFilter(maxlength) });
        } else {
            setFilters(TextView.NO_FILTERS);
        }
        setText(text, bufferType);
        if (mText == null) {
            mText = "";
        }
        if (mTransformed == null) {
            mTransformed = "";
        }
        if (textIsSetFromXml) {
            mTextSetFromXmlOrResourceId = true;
        }
        if (hint != null)
            setHint(hint);

        /* Views are not normally clickable unless specified to be.
        However, TextViews that have input or movement methods *are*
        clickable by default. By setting clickable here, we implicitly set focusable as well
        if not overridden by the developer.
         */
        a = context.obtainStyledAttributes(attrs, android.view.com.android.internal.R.styleable.View, defStyleAttr, defStyleRes);
        boolean canInputOrMove = (mMovement != null) || (getKeyListener() != null);
        boolean clickable = canInputOrMove || isClickable();
        boolean longClickable = canInputOrMove || isLongClickable();
        int focusable = getFocusable();
        n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case android.widget.com.android.internal.R.styleable.View_focusable :
                    android.util.TypedValue val = new android.util.TypedValue();
                    if (a.getValue(attr, val)) {
                        focusable = (val.type == android.util.TypedValue.TYPE_INT_BOOLEAN) ? val.data == 0 ? android.view.View.NOT_FOCUSABLE : android.view.View.FOCUSABLE : val.data;
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.View_clickable :
                    clickable = a.getBoolean(attr, clickable);
                    break;
                case android.widget.com.android.internal.R.styleable.View_longClickable :
                    longClickable = a.getBoolean(attr, longClickable);
                    break;
            }
        }
        a.recycle();
        // Some apps were relying on the undefined behavior of focusable winning over
        // focusableInTouchMode != focusable in TextViews if both were specified in XML (usually
        // when starting with EditText and setting only focusable=false). To keep those apps from
        // breaking, re-apply the focusable attribute here.
        if (focusable != getFocusable()) {
            setFocusable(focusable);
        }
        setClickable(clickable);
        setLongClickable(longClickable);
        if (mEditor != null)
            mEditor.prepareCursorControllers();

        // If not explicitly specified this view is important for accessibility.
        if (getImportantForAccessibility() == android.view.View.IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
            setImportantForAccessibility(android.view.View.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        if (supportsAutoSizeText()) {
            if (mAutoSizeTextType == TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM) {
                // If uniform auto-size has been specified but preset values have not been set then
                // replace the auto-size configuration values that have not been specified with the
                // defaults.
                if (!mHasPresetAutoSizeValues) {
                    final android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    if (autoSizeMinTextSizeInPx == TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                        autoSizeMinTextSizeInPx = android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TextView.DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP, displayMetrics);
                    }
                    if (autoSizeMaxTextSizeInPx == TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                        autoSizeMaxTextSizeInPx = android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TextView.DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP, displayMetrics);
                    }
                    if (autoSizeStepGranularityInPx == TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE) {
                        autoSizeStepGranularityInPx = TextView.DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX;
                    }
                    validateAndSetAutoSizeTextTypeUniformConfiguration(autoSizeMinTextSizeInPx, autoSizeMaxTextSizeInPx, autoSizeStepGranularityInPx);
                }
                setupAutoSizeText();
            }
        } else {
            mAutoSizeTextType = TextView.AUTO_SIZE_TEXT_TYPE_NONE;
        }
        if (firstBaselineToTopHeight >= 0) {
            setFirstBaselineToTopHeight(firstBaselineToTopHeight);
        }
        if (lastBaselineToBottomHeight >= 0) {
            setLastBaselineToBottomHeight(lastBaselineToBottomHeight);
        }
        if (lineHeight >= 0) {
            setLineHeight(lineHeight);
        }
    }

    // Update mText and mPrecomputed
    private void setTextInternal(@android.annotation.Nullable
    java.lang.CharSequence text) {
        mText = text;
        mSpannable = (text instanceof android.text.Spannable) ? ((android.text.Spannable) (text)) : null;
        mPrecomputed = (text instanceof android.text.PrecomputedText) ? ((android.text.PrecomputedText) (text)) : null;
    }

    /**
     * Specify whether this widget should automatically scale the text to try to perfectly fit
     * within the layout bounds by using the default auto-size configuration.
     *
     * @param autoSizeTextType
     * 		the type of auto-size. Must be one of
     * 		{@link TextView#AUTO_SIZE_TEXT_TYPE_NONE} or
     * 		{@link TextView#AUTO_SIZE_TEXT_TYPE_UNIFORM}
     * @throws IllegalArgumentException
     * 		if <code>autoSizeTextType</code> is none of the types above.
     * @unknown ref android.R.styleable#TextView_autoSizeTextType
     * @see #getAutoSizeTextType()
     */
    public void setAutoSizeTextTypeWithDefaults(@TextView.AutoSizeTextType
    int autoSizeTextType) {
        if (supportsAutoSizeText()) {
            switch (autoSizeTextType) {
                case TextView.AUTO_SIZE_TEXT_TYPE_NONE :
                    clearAutoSizeConfiguration();
                    break;
                case TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM :
                    final android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    final float autoSizeMinTextSizeInPx = android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TextView.DEFAULT_AUTO_SIZE_MIN_TEXT_SIZE_IN_SP, displayMetrics);
                    final float autoSizeMaxTextSizeInPx = android.util.TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, TextView.DEFAULT_AUTO_SIZE_MAX_TEXT_SIZE_IN_SP, displayMetrics);
                    validateAndSetAutoSizeTextTypeUniformConfiguration(autoSizeMinTextSizeInPx, autoSizeMaxTextSizeInPx, TextView.DEFAULT_AUTO_SIZE_GRANULARITY_IN_PX);
                    if (setupAutoSizeText()) {
                        autoSizeText();
                        invalidate();
                    }
                    break;
                default :
                    throw new java.lang.IllegalArgumentException("Unknown auto-size text type: " + autoSizeTextType);
            }
        }
    }

    /**
     * Specify whether this widget should automatically scale the text to try to perfectly fit
     * within the layout bounds. If all the configuration params are valid the type of auto-size is
     * set to {@link #AUTO_SIZE_TEXT_TYPE_UNIFORM}.
     *
     * @param autoSizeMinTextSize
     * 		the minimum text size available for auto-size
     * @param autoSizeMaxTextSize
     * 		the maximum text size available for auto-size
     * @param autoSizeStepGranularity
     * 		the auto-size step granularity. It is used in conjunction with
     * 		the minimum and maximum text size in order to build the set of
     * 		text sizes the system uses to choose from when auto-sizing
     * @param unit
     * 		the desired dimension unit for all sizes above. See {@link TypedValue} for the
     * 		possible dimension units
     * @throws IllegalArgumentException
     * 		if any of the configuration params are invalid.
     * @unknown ref android.R.styleable#TextView_autoSizeTextType
     * @unknown ref android.R.styleable#TextView_autoSizeMinTextSize
     * @unknown ref android.R.styleable#TextView_autoSizeMaxTextSize
     * @unknown ref android.R.styleable#TextView_autoSizeStepGranularity
     * @see #setAutoSizeTextTypeWithDefaults(int)
     * @see #setAutoSizeTextTypeUniformWithPresetSizes(int[], int)
     * @see #getAutoSizeMinTextSize()
     * @see #getAutoSizeMaxTextSize()
     * @see #getAutoSizeStepGranularity()
     * @see #getAutoSizeTextAvailableSizes()
     */
    public void setAutoSizeTextTypeUniformWithConfiguration(int autoSizeMinTextSize, int autoSizeMaxTextSize, int autoSizeStepGranularity, int unit) {
        if (supportsAutoSizeText()) {
            final android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            final float autoSizeMinTextSizeInPx = android.util.TypedValue.applyDimension(unit, autoSizeMinTextSize, displayMetrics);
            final float autoSizeMaxTextSizeInPx = android.util.TypedValue.applyDimension(unit, autoSizeMaxTextSize, displayMetrics);
            final float autoSizeStepGranularityInPx = android.util.TypedValue.applyDimension(unit, autoSizeStepGranularity, displayMetrics);
            validateAndSetAutoSizeTextTypeUniformConfiguration(autoSizeMinTextSizeInPx, autoSizeMaxTextSizeInPx, autoSizeStepGranularityInPx);
            if (setupAutoSizeText()) {
                autoSizeText();
                invalidate();
            }
        }
    }

    /**
     * Specify whether this widget should automatically scale the text to try to perfectly fit
     * within the layout bounds. If at least one value from the <code>presetSizes</code> is valid
     * then the type of auto-size is set to {@link #AUTO_SIZE_TEXT_TYPE_UNIFORM}.
     *
     * @param presetSizes
     * 		an {@code int} array of sizes in pixels
     * @param unit
     * 		the desired dimension unit for the preset sizes above. See {@link TypedValue} for
     * 		the possible dimension units
     * @throws IllegalArgumentException
     * 		if all of the <code>presetSizes</code> are invalid.
     * @unknown ref android.R.styleable#TextView_autoSizeTextType
     * @unknown ref android.R.styleable#TextView_autoSizePresetSizes
     * @see #setAutoSizeTextTypeWithDefaults(int)
     * @see #setAutoSizeTextTypeUniformWithConfiguration(int, int, int, int)
     * @see #getAutoSizeMinTextSize()
     * @see #getAutoSizeMaxTextSize()
     * @see #getAutoSizeTextAvailableSizes()
     */
    public void setAutoSizeTextTypeUniformWithPresetSizes(@android.annotation.NonNull
    int[] presetSizes, int unit) {
        if (supportsAutoSizeText()) {
            final int presetSizesLength = presetSizes.length;
            if (presetSizesLength > 0) {
                int[] presetSizesInPx = new int[presetSizesLength];
                if (unit == android.util.TypedValue.COMPLEX_UNIT_PX) {
                    presetSizesInPx = java.util.Arrays.copyOf(presetSizes, presetSizesLength);
                } else {
                    final android.util.DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                    // Convert all to sizes to pixels.
                    for (int i = 0; i < presetSizesLength; i++) {
                        presetSizesInPx[i] = java.lang.Math.round(android.util.TypedValue.applyDimension(unit, presetSizes[i], displayMetrics));
                    }
                }
                mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(presetSizesInPx);
                if (!setupAutoSizeUniformPresetSizesConfiguration()) {
                    throw new java.lang.IllegalArgumentException("None of the preset sizes is valid: " + java.util.Arrays.toString(presetSizes));
                }
            } else {
                mHasPresetAutoSizeValues = false;
            }
            if (setupAutoSizeText()) {
                autoSizeText();
                invalidate();
            }
        }
    }

    /**
     * Returns the type of auto-size set for this widget.
     *
     * @return an {@code int} corresponding to one of the auto-size types:
    {@link TextView#AUTO_SIZE_TEXT_TYPE_NONE} or
    {@link TextView#AUTO_SIZE_TEXT_TYPE_UNIFORM}
     * @unknown ref android.R.styleable#TextView_autoSizeTextType
     * @see #setAutoSizeTextTypeWithDefaults(int)
     * @see #setAutoSizeTextTypeUniformWithConfiguration(int, int, int, int)
     * @see #setAutoSizeTextTypeUniformWithPresetSizes(int[], int)
     */
    @android.view.inspector.InspectableProperty(enumMapping = { @android.view.inspector.InspectableProperty.EnumEntry(name = "none", value = TextView.AUTO_SIZE_TEXT_TYPE_NONE), @android.view.inspector.InspectableProperty.EnumEntry(name = "uniform", value = TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM) })
    @TextView.AutoSizeTextType
    public int getAutoSizeTextType() {
        return mAutoSizeTextType;
    }

    /**
     *
     *
     * @return the current auto-size step granularity in pixels.
     * @unknown ref android.R.styleable#TextView_autoSizeStepGranularity
     * @see #setAutoSizeTextTypeUniformWithConfiguration(int, int, int, int)
     */
    @android.view.inspector.InspectableProperty
    public int getAutoSizeStepGranularity() {
        return java.lang.Math.round(mAutoSizeStepGranularityInPx);
    }

    /**
     *
     *
     * @return the current auto-size minimum text size in pixels (the default is 12sp). Note that
    if auto-size has not been configured this function returns {@code -1}.
     * @unknown ref android.R.styleable#TextView_autoSizeMinTextSize
     * @see #setAutoSizeTextTypeUniformWithConfiguration(int, int, int, int)
     * @see #setAutoSizeTextTypeUniformWithPresetSizes(int[], int)
     */
    @android.view.inspector.InspectableProperty
    public int getAutoSizeMinTextSize() {
        return java.lang.Math.round(mAutoSizeMinTextSizeInPx);
    }

    /**
     *
     *
     * @return the current auto-size maximum text size in pixels (the default is 112sp). Note that
    if auto-size has not been configured this function returns {@code -1}.
     * @unknown ref android.R.styleable#TextView_autoSizeMaxTextSize
     * @see #setAutoSizeTextTypeUniformWithConfiguration(int, int, int, int)
     * @see #setAutoSizeTextTypeUniformWithPresetSizes(int[], int)
     */
    @android.view.inspector.InspectableProperty
    public int getAutoSizeMaxTextSize() {
        return java.lang.Math.round(mAutoSizeMaxTextSizeInPx);
    }

    /**
     *
     *
     * @return the current auto-size {@code int} sizes array (in pixels).
     * @see #setAutoSizeTextTypeUniformWithConfiguration(int, int, int, int)
     * @see #setAutoSizeTextTypeUniformWithPresetSizes(int[], int)
     */
    public int[] getAutoSizeTextAvailableSizes() {
        return mAutoSizeTextSizesInPx;
    }

    private void setupAutoSizeUniformPresetSizes(android.content.res.TypedArray textSizes) {
        final int textSizesLength = textSizes.length();
        final int[] parsedSizes = new int[textSizesLength];
        if (textSizesLength > 0) {
            for (int i = 0; i < textSizesLength; i++) {
                parsedSizes[i] = textSizes.getDimensionPixelSize(i, -1);
            }
            mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(parsedSizes);
            setupAutoSizeUniformPresetSizesConfiguration();
        }
    }

    private boolean setupAutoSizeUniformPresetSizesConfiguration() {
        final int sizesLength = mAutoSizeTextSizesInPx.length;
        mHasPresetAutoSizeValues = sizesLength > 0;
        if (mHasPresetAutoSizeValues) {
            mAutoSizeTextType = TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;
            mAutoSizeMinTextSizeInPx = mAutoSizeTextSizesInPx[0];
            mAutoSizeMaxTextSizeInPx = mAutoSizeTextSizesInPx[sizesLength - 1];
            mAutoSizeStepGranularityInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        }
        return mHasPresetAutoSizeValues;
    }

    /**
     * If all params are valid then save the auto-size configuration.
     *
     * @throws IllegalArgumentException
     * 		if any of the params are invalid
     */
    private void validateAndSetAutoSizeTextTypeUniformConfiguration(float autoSizeMinTextSizeInPx, float autoSizeMaxTextSizeInPx, float autoSizeStepGranularityInPx) {
        // First validate.
        if (autoSizeMinTextSizeInPx <= 0) {
            throw new java.lang.IllegalArgumentException(("Minimum auto-size text size (" + autoSizeMinTextSizeInPx) + "px) is less or equal to (0px)");
        }
        if (autoSizeMaxTextSizeInPx <= autoSizeMinTextSizeInPx) {
            throw new java.lang.IllegalArgumentException((((("Maximum auto-size text size (" + autoSizeMaxTextSizeInPx) + "px) is less or equal to minimum auto-size ") + "text size (") + autoSizeMinTextSizeInPx) + "px)");
        }
        if (autoSizeStepGranularityInPx <= 0) {
            throw new java.lang.IllegalArgumentException(("The auto-size step granularity (" + autoSizeStepGranularityInPx) + "px) is less or equal to (0px)");
        }
        // All good, persist the configuration.
        mAutoSizeTextType = TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM;
        mAutoSizeMinTextSizeInPx = autoSizeMinTextSizeInPx;
        mAutoSizeMaxTextSizeInPx = autoSizeMaxTextSizeInPx;
        mAutoSizeStepGranularityInPx = autoSizeStepGranularityInPx;
        mHasPresetAutoSizeValues = false;
    }

    private void clearAutoSizeConfiguration() {
        mAutoSizeTextType = TextView.AUTO_SIZE_TEXT_TYPE_NONE;
        mAutoSizeMinTextSizeInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        mAutoSizeMaxTextSizeInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        mAutoSizeStepGranularityInPx = TextView.UNSET_AUTO_SIZE_UNIFORM_CONFIGURATION_VALUE;
        mAutoSizeTextSizesInPx = libcore.util.EmptyArray.INT;
        mNeedsAutoSizeText = false;
    }

    // Returns distinct sorted positive values.
    private int[] cleanupAutoSizePresetSizes(int[] presetValues) {
        final int presetValuesLength = presetValues.length;
        if (presetValuesLength == 0) {
            return presetValues;
        }
        java.util.Arrays.sort(presetValues);
        final android.util.IntArray uniqueValidSizes = new android.util.IntArray();
        for (int i = 0; i < presetValuesLength; i++) {
            final int currentPresetValue = presetValues[i];
            if ((currentPresetValue > 0) && (uniqueValidSizes.binarySearch(currentPresetValue) < 0)) {
                uniqueValidSizes.add(currentPresetValue);
            }
        }
        return presetValuesLength == uniqueValidSizes.size() ? presetValues : uniqueValidSizes.toArray();
    }

    private boolean setupAutoSizeText() {
        if (supportsAutoSizeText() && (mAutoSizeTextType == TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM)) {
            // Calculate the sizes set based on minimum size, maximum size and step size if we do
            // not have a predefined set of sizes or if the current sizes array is empty.
            if ((!mHasPresetAutoSizeValues) || (mAutoSizeTextSizesInPx.length == 0)) {
                final int autoSizeValuesLength = ((int) (java.lang.Math.floor((mAutoSizeMaxTextSizeInPx - mAutoSizeMinTextSizeInPx) / mAutoSizeStepGranularityInPx))) + 1;
                final int[] autoSizeTextSizesInPx = new int[autoSizeValuesLength];
                for (int i = 0; i < autoSizeValuesLength; i++) {
                    autoSizeTextSizesInPx[i] = java.lang.Math.round(mAutoSizeMinTextSizeInPx + (i * mAutoSizeStepGranularityInPx));
                }
                mAutoSizeTextSizesInPx = cleanupAutoSizePresetSizes(autoSizeTextSizesInPx);
            }
            mNeedsAutoSizeText = true;
        } else {
            mNeedsAutoSizeText = false;
        }
        return mNeedsAutoSizeText;
    }

    private int[] parseDimensionArray(android.content.res.TypedArray dimens) {
        if (dimens == null) {
            return null;
        }
        int[] result = new int[dimens.length()];
        for (int i = 0; i < result.length; i++) {
            result[i] = dimens.getDimensionPixelSize(i, 0);
        }
        return result;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
        if (requestCode == TextView.PROCESS_TEXT_REQUEST_CODE) {
            if ((resultCode == android.app.Activity.RESULT_OK) && (data != null)) {
                java.lang.CharSequence result = data.getCharSequenceExtra(android.content.Intent.EXTRA_PROCESS_TEXT);
                if (result != null) {
                    if (isTextEditable()) {
                        replaceSelectionWithText(result);
                        if (mEditor != null) {
                            mEditor.refreshTextActionMode();
                        }
                    } else {
                        if (result.length() > 0) {
                            android.widget.Toast.makeText(getContext(), java.lang.String.valueOf(result), android.widget.Toast.LENGTH_LONG).show();
                        }
                    }
                }
            } else
                if (mSpannable != null) {
                    // Reset the selection.
                    android.text.Selection.setSelection(mSpannable, getSelectionEnd());
                }

        }
    }

    /**
     * Sets the Typeface taking into account the given attributes.
     *
     * @param typeface
     * 		a typeface
     * @param familyName
     * 		family name string, e.g. "serif"
     * @param typefaceIndex
     * 		an index of the typeface enum, e.g. SANS, SERIF.
     * @param style
     * 		a typeface style
     * @param weight
     * 		a weight value for the Typeface or -1 if not specified.
     */
    private void setTypefaceFromAttrs(@android.annotation.Nullable
    android.graphics.Typeface typeface, @android.annotation.Nullable
    java.lang.String familyName, @TextView.XMLTypefaceAttr
    int typefaceIndex, @android.graphics.Typeface.Style
    int style, @android.annotation.IntRange(from = -1, to = android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX)
    int weight) {
        if ((typeface == null) && (familyName != null)) {
            // Lookup normal Typeface from system font map.
            final android.graphics.Typeface normalTypeface = android.graphics.Typeface.create(familyName, android.graphics.Typeface.NORMAL);
            resolveStyleAndSetTypeface(normalTypeface, style, weight);
        } else
            if (typeface != null) {
                resolveStyleAndSetTypeface(typeface, style, weight);
            } else {
                // both typeface and familyName is null.
                switch (typefaceIndex) {
                    case TextView.SANS :
                        resolveStyleAndSetTypeface(android.graphics.Typeface.SANS_SERIF, style, weight);
                        break;
                    case TextView.SERIF :
                        resolveStyleAndSetTypeface(android.graphics.Typeface.SERIF, style, weight);
                        break;
                    case TextView.MONOSPACE :
                        resolveStyleAndSetTypeface(android.graphics.Typeface.MONOSPACE, style, weight);
                        break;
                    case TextView.DEFAULT_TYPEFACE :
                    default :
                        resolveStyleAndSetTypeface(null, style, weight);
                        break;
                }
            }

    }

    private void resolveStyleAndSetTypeface(@android.annotation.NonNull
    android.graphics.Typeface typeface, @android.graphics.Typeface.Style
    int style, @android.annotation.IntRange(from = -1, to = android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX)
    int weight) {
        if (weight >= 0) {
            weight = java.lang.Math.min(android.graphics.fonts.FontStyle.FONT_WEIGHT_MAX, weight);
            final boolean italic = (style & android.graphics.Typeface.ITALIC) != 0;
            setTypeface(android.graphics.Typeface.create(typeface, weight, italic));
        } else {
            setTypeface(typeface, style);
        }
    }

    private void setRelativeDrawablesIfNeeded(android.graphics.drawable.Drawable start, android.graphics.drawable.Drawable end) {
        boolean hasRelativeDrawables = (start != null) || (end != null);
        if (hasRelativeDrawables) {
            TextView.Drawables dr = mDrawables;
            if (dr == null) {
                mDrawables = dr = new TextView.Drawables(getContext());
            }
            mDrawables.mOverride = true;
            final android.graphics.Rect compoundRect = dr.mCompoundRect;
            int[] state = getDrawableState();
            if (start != null) {
                start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableStart = start;
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableSizeStart = dr.mDrawableHeightStart = 0;
            }
            if (end != null) {
                end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableEnd = end;
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableSizeEnd = dr.mDrawableHeightEnd = 0;
            }
            resetResolvedDrawables();
            resolveDrawables();
            applyCompoundDrawableTint();
        }
    }

    @android.view.RemotableViewMethod
    @java.lang.Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled()) {
            return;
        }
        if (!enabled) {
            // Hide the soft input if the currently active TextView is disabled
            android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            if ((imm != null) && imm.isActive(this)) {
                imm.hideSoftInputFromWindow(getWindowToken(), 0);
            }
        }
        super.setEnabled(enabled);
        if (enabled) {
            // Make sure IME is updated with current editor info.
            android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            if (imm != null)
                imm.restartInput(this);

        }
        // Will change text color
        if (mEditor != null) {
            mEditor.invalidateTextDisplayList();
            mEditor.prepareCursorControllers();
            // start or stop the cursor blinking as appropriate
            mEditor.makeBlink();
        }
    }

    /**
     * Sets the typeface and style in which the text should be displayed,
     * and turns on the fake bold and italic bits in the Paint if the
     * Typeface that you provided does not have all the bits in the
     * style that you specified.
     *
     * @unknown ref android.R.styleable#TextView_typeface
     * @unknown ref android.R.styleable#TextView_textStyle
     */
    public void setTypeface(@android.annotation.Nullable
    android.graphics.Typeface tf, @android.graphics.Typeface.Style
    int style) {
        if (style > 0) {
            if (tf == null) {
                tf = android.graphics.Typeface.defaultFromStyle(style);
            } else {
                tf = android.graphics.Typeface.create(tf, style);
            }
            setTypeface(tf);
            // now compute what (if any) algorithmic styling is needed
            int typefaceStyle = (tf != null) ? tf.getStyle() : 0;
            int need = style & (~typefaceStyle);
            mTextPaint.setFakeBoldText((need & android.graphics.Typeface.BOLD) != 0);
            mTextPaint.setTextSkewX((need & android.graphics.Typeface.ITALIC) != 0 ? -0.25F : 0);
        } else {
            mTextPaint.setFakeBoldText(false);
            mTextPaint.setTextSkewX(0);
            setTypeface(tf);
        }
    }

    /**
     * Subclasses override this to specify that they have a KeyListener
     * by default even if not specifically called for in the XML options.
     */
    protected boolean getDefaultEditable() {
        return false;
    }

    /**
     * Subclasses override this to specify a default movement method.
     */
    protected android.text.method.MovementMethod getDefaultMovementMethod() {
        return null;
    }

    /**
     * Return the text that TextView is displaying. If {@link #setText(CharSequence)} was called
     * with an argument of {@link android.widget.TextView.BufferType#SPANNABLE BufferType.SPANNABLE}
     * or {@link android.widget.TextView.BufferType#EDITABLE BufferType.EDITABLE}, you can cast
     * the return value from this method to Spannable or Editable, respectively.
     *
     * <p>The content of the return value should not be modified. If you want a modifiable one, you
     * should make your own copy first.</p>
     *
     * @return The text displayed by the text view.
     * @unknown ref android.R.styleable#TextView_text
     */
    @android.view.ViewDebug.CapturedViewProperty
    @android.view.inspector.InspectableProperty
    public java.lang.CharSequence getText() {
        return mText;
    }

    /**
     * Returns the length, in characters, of the text managed by this TextView
     *
     * @return The length of the text managed by the TextView in characters.
     */
    public int length() {
        return mText.length();
    }

    /**
     * Return the text that TextView is displaying as an Editable object. If the text is not
     * editable, null is returned.
     *
     * @see #getText
     */
    public android.text.Editable getEditableText() {
        return mText instanceof android.text.Editable ? ((android.text.Editable) (mText)) : null;
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public java.lang.CharSequence getTransformed() {
        return mTransformed;
    }

    /**
     * Gets the vertical distance between lines of text, in pixels.
     * Note that markup within the text can cause individual lines
     * to be taller or shorter than this height, and the layout may
     * contain additional first-or last-line padding.
     *
     * @return The height of one standard line in pixels.
     */
    @android.view.inspector.InspectableProperty
    public int getLineHeight() {
        return com.android.internal.util.FastMath.round((mTextPaint.getFontMetricsInt(null) * mSpacingMult) + mSpacingAdd);
    }

    /**
     * Gets the {@link android.text.Layout} that is currently being used to display the text.
     * This value can be null if the text or width has recently changed.
     *
     * @return The Layout that is currently being used to display the text.
     */
    public final android.text.Layout getLayout() {
        return mLayout;
    }

    /**
     *
     *
     * @return the {@link android.text.Layout} that is currently being used to
    display the hint text. This can be null.
     */
    @android.annotation.UnsupportedAppUsage
    final android.text.Layout getHintLayout() {
        return mHintLayout;
    }

    /**
     * Retrieve the {@link android.content.UndoManager} that is currently associated
     * with this TextView.  By default there is no associated UndoManager, so null
     * is returned.  One can be associated with the TextView through
     * {@link #setUndoManager(android.content.UndoManager, String)}
     *
     * @unknown 
     */
    public final android.content.UndoManager getUndoManager() {
        // TODO: Consider supporting a global undo manager.
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public final android.widget.Editor getEditorForTesting() {
        return mEditor;
    }

    /**
     * Associate an {@link android.content.UndoManager} with this TextView.  Once
     * done, all edit operations on the TextView will result in appropriate
     * {@link android.content.UndoOperation} objects pushed on the given UndoManager's
     * stack.
     *
     * @param undoManager
     * 		The {@link android.content.UndoManager} to associate with
     * 		this TextView, or null to clear any existing association.
     * @param tag
     * 		String tag identifying this particular TextView owner in the
     * 		UndoManager.  This is used to keep the correct association with the
     * 		{@link android.content.UndoOwner} of any operations inside of the UndoManager.
     * @unknown 
     */
    public final void setUndoManager(android.content.UndoManager undoManager, java.lang.String tag) {
        // TODO: Consider supporting a global undo manager. An implementation will need to:
        // * createEditorIfNeeded()
        // * Promote to BufferType.EDITABLE if needed.
        // * Update the UndoManager and UndoOwner.
        // Likewise it will need to be able to restore the default UndoManager.
        throw new java.lang.UnsupportedOperationException("not implemented");
    }

    /**
     * Gets the current {@link KeyListener} for the TextView.
     * This will frequently be null for non-EditText TextViews.
     *
     * @return the current key listener for this TextView.
     * @unknown ref android.R.styleable#TextView_numeric
     * @unknown ref android.R.styleable#TextView_digits
     * @unknown ref android.R.styleable#TextView_phoneNumber
     * @unknown ref android.R.styleable#TextView_inputMethod
     * @unknown ref android.R.styleable#TextView_capitalize
     * @unknown ref android.R.styleable#TextView_autoText
     */
    public final android.text.method.KeyListener getKeyListener() {
        return mEditor == null ? null : mEditor.mKeyListener;
    }

    /**
     * Sets the key listener to be used with this TextView.  This can be null
     * to disallow user input.  Note that this method has significant and
     * subtle interactions with soft keyboards and other input method:
     * see {@link KeyListener#getInputType() KeyListener.getInputType()}
     * for important details.  Calling this method will replace the current
     * content type of the text view with the content type returned by the
     * key listener.
     * <p>
     * Be warned that if you want a TextView with a key listener or movement
     * method not to be focusable, or if you want a TextView without a
     * key listener or movement method to be focusable, you must call
     * {@link #setFocusable} again after calling this to get the focusability
     * back the way you want it.
     *
     * @unknown ref android.R.styleable#TextView_numeric
     * @unknown ref android.R.styleable#TextView_digits
     * @unknown ref android.R.styleable#TextView_phoneNumber
     * @unknown ref android.R.styleable#TextView_inputMethod
     * @unknown ref android.R.styleable#TextView_capitalize
     * @unknown ref android.R.styleable#TextView_autoText
     */
    public void setKeyListener(android.text.method.KeyListener input) {
        mListenerChanged = true;
        setKeyListenerOnly(input);
        fixFocusableAndClickableSettings();
        if (input != null) {
            createEditorIfNeeded();
            setInputTypeFromEditor();
        } else {
            if (mEditor != null)
                mEditor.mInputType = TYPE_NULL;

        }
        android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
        if (imm != null)
            imm.restartInput(this);

    }

    private void setInputTypeFromEditor() {
        try {
            mEditor.mInputType = mEditor.mKeyListener.getInputType();
        } catch (java.lang.IncompatibleClassChangeError e) {
            mEditor.mInputType = TYPE_CLASS_TEXT;
        }
        // Change inputType, without affecting transformation.
        // No need to applySingleLine since mSingleLine is unchanged.
        setInputTypeSingleLine(mSingleLine);
    }

    private void setKeyListenerOnly(android.text.method.KeyListener input) {
        if ((mEditor == null) && (input == null))
            return;
        // null is the default value

        createEditorIfNeeded();
        if (mEditor.mKeyListener != input) {
            mEditor.mKeyListener = input;
            if ((input != null) && (!(mText instanceof android.text.Editable))) {
                setText(mText);
            }
            setFilters(((android.text.Editable) (mText)), mFilters);
        }
    }

    /**
     * Gets the {@link android.text.method.MovementMethod} being used for this TextView,
     * which provides positioning, scrolling, and text selection functionality.
     * This will frequently be null for non-EditText TextViews.
     *
     * @return the movement method being used for this TextView.
     * @see android.text.method.MovementMethod
     */
    public final android.text.method.MovementMethod getMovementMethod() {
        return mMovement;
    }

    /**
     * Sets the {@link android.text.method.MovementMethod} for handling arrow key movement
     * for this TextView. This can be null to disallow using the arrow keys to move the
     * cursor or scroll the view.
     * <p>
     * Be warned that if you want a TextView with a key listener or movement
     * method not to be focusable, or if you want a TextView without a
     * key listener or movement method to be focusable, you must call
     * {@link #setFocusable} again after calling this to get the focusability
     * back the way you want it.
     */
    public final void setMovementMethod(android.text.method.MovementMethod movement) {
        if (mMovement != movement) {
            mMovement = movement;
            if ((movement != null) && (mSpannable == null)) {
                setText(mText);
            }
            fixFocusableAndClickableSettings();
            // SelectionModifierCursorController depends on textCanBeSelected, which depends on
            // mMovement
            if (mEditor != null)
                mEditor.prepareCursorControllers();

        }
    }

    private void fixFocusableAndClickableSettings() {
        if ((mMovement != null) || ((mEditor != null) && (mEditor.mKeyListener != null))) {
            setFocusable(android.view.View.FOCUSABLE);
            setClickable(true);
            setLongClickable(true);
        } else {
            setFocusable(android.view.View.FOCUSABLE_AUTO);
            setClickable(false);
            setLongClickable(false);
        }
    }

    /**
     * Gets the current {@link android.text.method.TransformationMethod} for the TextView.
     * This is frequently null, except for single-line and password fields.
     *
     * @return the current transformation method for this TextView.
     * @unknown ref android.R.styleable#TextView_password
     * @unknown ref android.R.styleable#TextView_singleLine
     */
    public final android.text.method.TransformationMethod getTransformationMethod() {
        return mTransformation;
    }

    /**
     * Sets the transformation that is applied to the text that this
     * TextView is displaying.
     *
     * @unknown ref android.R.styleable#TextView_password
     * @unknown ref android.R.styleable#TextView_singleLine
     */
    public final void setTransformationMethod(android.text.method.TransformationMethod method) {
        if (method == mTransformation) {
            // Avoid the setText() below if the transformation is
            // the same.
            return;
        }
        if (mTransformation != null) {
            if (mSpannable != null) {
                mSpannable.removeSpan(mTransformation);
            }
        }
        mTransformation = method;
        if (method instanceof android.text.method.TransformationMethod2) {
            android.text.method.TransformationMethod2 method2 = ((android.text.method.TransformationMethod2) (method));
            mAllowTransformationLengthChange = (!isTextSelectable()) && (!(mText instanceof android.text.Editable));
            method2.setLengthChangesAllowed(mAllowTransformationLengthChange);
        } else {
            mAllowTransformationLengthChange = false;
        }
        setText(mText);
        if (hasPasswordTransformationMethod()) {
            notifyViewAccessibilityStateChangedIfNeeded(android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);
        }
        // PasswordTransformationMethod always have LTR text direction heuristics returned by
        // getTextDirectionHeuristic, needs reset
        mTextDir = getTextDirectionHeuristic();
    }

    /**
     * Returns the top padding of the view, plus space for the top
     * Drawable if any.
     */
    public int getCompoundPaddingTop() {
        final TextView.Drawables dr = mDrawables;
        if ((dr == null) || (dr.mShowing[TextView.Drawables.TOP] == null)) {
            return mPaddingTop;
        } else {
            return (mPaddingTop + dr.mDrawablePadding) + dr.mDrawableSizeTop;
        }
    }

    /**
     * Returns the bottom padding of the view, plus space for the bottom
     * Drawable if any.
     */
    public int getCompoundPaddingBottom() {
        final TextView.Drawables dr = mDrawables;
        if ((dr == null) || (dr.mShowing[TextView.Drawables.BOTTOM] == null)) {
            return mPaddingBottom;
        } else {
            return (mPaddingBottom + dr.mDrawablePadding) + dr.mDrawableSizeBottom;
        }
    }

    /**
     * Returns the left padding of the view, plus space for the left
     * Drawable if any.
     */
    public int getCompoundPaddingLeft() {
        final TextView.Drawables dr = mDrawables;
        if ((dr == null) || (dr.mShowing[TextView.Drawables.LEFT] == null)) {
            return mPaddingLeft;
        } else {
            return (mPaddingLeft + dr.mDrawablePadding) + dr.mDrawableSizeLeft;
        }
    }

    /**
     * Returns the right padding of the view, plus space for the right
     * Drawable if any.
     */
    public int getCompoundPaddingRight() {
        final TextView.Drawables dr = mDrawables;
        if ((dr == null) || (dr.mShowing[TextView.Drawables.RIGHT] == null)) {
            return mPaddingRight;
        } else {
            return (mPaddingRight + dr.mDrawablePadding) + dr.mDrawableSizeRight;
        }
    }

    /**
     * Returns the start padding of the view, plus space for the start
     * Drawable if any.
     */
    public int getCompoundPaddingStart() {
        resolveDrawables();
        switch (getLayoutDirection()) {
            default :
            case android.view.View.LAYOUT_DIRECTION_LTR :
                return getCompoundPaddingLeft();
            case android.view.View.LAYOUT_DIRECTION_RTL :
                return getCompoundPaddingRight();
        }
    }

    /**
     * Returns the end padding of the view, plus space for the end
     * Drawable if any.
     */
    public int getCompoundPaddingEnd() {
        resolveDrawables();
        switch (getLayoutDirection()) {
            default :
            case android.view.View.LAYOUT_DIRECTION_LTR :
                return getCompoundPaddingRight();
            case android.view.View.LAYOUT_DIRECTION_RTL :
                return getCompoundPaddingLeft();
        }
    }

    /**
     * Returns the extended top padding of the view, including both the
     * top Drawable if any and any extra space to keep more than maxLines
     * of text from showing.  It is only valid to call this after measuring.
     */
    public int getExtendedPaddingTop() {
        if (mMaxMode != LINES) {
            return getCompoundPaddingTop();
        }
        if (mLayout == null) {
            assumeLayout();
        }
        if (mLayout.getLineCount() <= mMaximum) {
            return getCompoundPaddingTop();
        }
        int top = getCompoundPaddingTop();
        int bottom = getCompoundPaddingBottom();
        int viewht = (getHeight() - top) - bottom;
        int layoutht = mLayout.getLineTop(mMaximum);
        if (layoutht >= viewht) {
            return top;
        }
        final int gravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        if (gravity == android.view.Gravity.TOP) {
            return top;
        } else
            if (gravity == android.view.Gravity.BOTTOM) {
                return (top + viewht) - layoutht;
            } else {
                // (gravity == Gravity.CENTER_VERTICAL)
                return top + ((viewht - layoutht) / 2);
            }

    }

    /**
     * Returns the extended bottom padding of the view, including both the
     * bottom Drawable if any and any extra space to keep more than maxLines
     * of text from showing.  It is only valid to call this after measuring.
     */
    public int getExtendedPaddingBottom() {
        if (mMaxMode != LINES) {
            return getCompoundPaddingBottom();
        }
        if (mLayout == null) {
            assumeLayout();
        }
        if (mLayout.getLineCount() <= mMaximum) {
            return getCompoundPaddingBottom();
        }
        int top = getCompoundPaddingTop();
        int bottom = getCompoundPaddingBottom();
        int viewht = (getHeight() - top) - bottom;
        int layoutht = mLayout.getLineTop(mMaximum);
        if (layoutht >= viewht) {
            return bottom;
        }
        final int gravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        if (gravity == android.view.Gravity.TOP) {
            return (bottom + viewht) - layoutht;
        } else
            if (gravity == android.view.Gravity.BOTTOM) {
                return bottom;
            } else {
                // (gravity == Gravity.CENTER_VERTICAL)
                return bottom + ((viewht - layoutht) / 2);
            }

    }

    /**
     * Returns the total left padding of the view, including the left
     * Drawable if any.
     */
    public int getTotalPaddingLeft() {
        return getCompoundPaddingLeft();
    }

    /**
     * Returns the total right padding of the view, including the right
     * Drawable if any.
     */
    public int getTotalPaddingRight() {
        return getCompoundPaddingRight();
    }

    /**
     * Returns the total start padding of the view, including the start
     * Drawable if any.
     */
    public int getTotalPaddingStart() {
        return getCompoundPaddingStart();
    }

    /**
     * Returns the total end padding of the view, including the end
     * Drawable if any.
     */
    public int getTotalPaddingEnd() {
        return getCompoundPaddingEnd();
    }

    /**
     * Returns the total top padding of the view, including the top
     * Drawable if any, the extra space to keep more than maxLines
     * from showing, and the vertical offset for gravity, if any.
     */
    public int getTotalPaddingTop() {
        return getExtendedPaddingTop() + getVerticalOffset(true);
    }

    /**
     * Returns the total bottom padding of the view, including the bottom
     * Drawable if any, the extra space to keep more than maxLines
     * from showing, and the vertical offset for gravity, if any.
     */
    public int getTotalPaddingBottom() {
        return getExtendedPaddingBottom() + getBottomVerticalOffset(true);
    }

    /**
     * Sets the Drawables (if any) to appear to the left of, above, to the
     * right of, and below the text. Use {@code null} if you do not want a
     * Drawable there. The Drawables must already have had
     * {@link Drawable#setBounds} called.
     * <p>
     * Calling this method will overwrite any Drawables previously set using
     * {@link #setCompoundDrawablesRelative} or related methods.
     *
     * @unknown ref android.R.styleable#TextView_drawableLeft
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableRight
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    public void setCompoundDrawables(@android.annotation.Nullable
    android.graphics.drawable.Drawable left, @android.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.annotation.Nullable
    android.graphics.drawable.Drawable right, @android.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        TextView.Drawables dr = mDrawables;
        // We're switching to absolute, discard relative.
        if (dr != null) {
            if (dr.mDrawableStart != null)
                dr.mDrawableStart.setCallback(null);

            dr.mDrawableStart = null;
            if (dr.mDrawableEnd != null)
                dr.mDrawableEnd.setCallback(null);

            dr.mDrawableEnd = null;
            dr.mDrawableSizeStart = dr.mDrawableHeightStart = 0;
            dr.mDrawableSizeEnd = dr.mDrawableHeightEnd = 0;
        }
        final boolean drawables = (((left != null) || (top != null)) || (right != null)) || (bottom != null);
        if (!drawables) {
            // Clearing drawables...  can we free the data structure?
            if (dr != null) {
                if (!dr.hasMetadata()) {
                    mDrawables = null;
                } else {
                    // We need to retain the last set padding, so just clear
                    // out all of the fields in the existing structure.
                    for (int i = dr.mShowing.length - 1; i >= 0; i--) {
                        if (dr.mShowing[i] != null) {
                            dr.mShowing[i].setCallback(null);
                        }
                        dr.mShowing[i] = null;
                    }
                    dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
                    dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
                    dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
                    dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
                }
            }
        } else {
            if (dr == null) {
                mDrawables = dr = new TextView.Drawables(getContext());
            }
            mDrawables.mOverride = false;
            if ((dr.mShowing[TextView.Drawables.LEFT] != left) && (dr.mShowing[TextView.Drawables.LEFT] != null)) {
                dr.mShowing[TextView.Drawables.LEFT].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.LEFT] = left;
            if ((dr.mShowing[TextView.Drawables.TOP] != top) && (dr.mShowing[TextView.Drawables.TOP] != null)) {
                dr.mShowing[TextView.Drawables.TOP].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.TOP] = top;
            if ((dr.mShowing[TextView.Drawables.RIGHT] != right) && (dr.mShowing[TextView.Drawables.RIGHT] != null)) {
                dr.mShowing[TextView.Drawables.RIGHT].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.RIGHT] = right;
            if ((dr.mShowing[TextView.Drawables.BOTTOM] != bottom) && (dr.mShowing[TextView.Drawables.BOTTOM] != null)) {
                dr.mShowing[TextView.Drawables.BOTTOM].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.BOTTOM] = bottom;
            final android.graphics.Rect compoundRect = dr.mCompoundRect;
            int[] state;
            state = getDrawableState();
            if (left != null) {
                left.setState(state);
                left.copyBounds(compoundRect);
                left.setCallback(this);
                dr.mDrawableSizeLeft = compoundRect.width();
                dr.mDrawableHeightLeft = compoundRect.height();
            } else {
                dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
            }
            if (right != null) {
                right.setState(state);
                right.copyBounds(compoundRect);
                right.setCallback(this);
                dr.mDrawableSizeRight = compoundRect.width();
                dr.mDrawableHeightRight = compoundRect.height();
            } else {
                dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
            }
        }
        // Save initial left/right drawables
        if (dr != null) {
            dr.mDrawableLeftInitial = left;
            dr.mDrawableRightInitial = right;
        }
        resetResolvedDrawables();
        resolveDrawables();
        applyCompoundDrawableTint();
        invalidate();
        requestLayout();
    }

    /**
     * Sets the Drawables (if any) to appear to the left of, above, to the
     * right of, and below the text. Use 0 if you do not want a Drawable there.
     * The Drawables' bounds will be set to their intrinsic bounds.
     * <p>
     * Calling this method will overwrite any Drawables previously set using
     * {@link #setCompoundDrawablesRelative} or related methods.
     *
     * @param left
     * 		Resource identifier of the left Drawable.
     * @param top
     * 		Resource identifier of the top Drawable.
     * @param right
     * 		Resource identifier of the right Drawable.
     * @param bottom
     * 		Resource identifier of the bottom Drawable.
     * @unknown ref android.R.styleable#TextView_drawableLeft
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableRight
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    @android.view.RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(@android.annotation.DrawableRes
    int left, @android.annotation.DrawableRes
    int top, @android.annotation.DrawableRes
    int right, @android.annotation.DrawableRes
    int bottom) {
        final android.content.Context context = getContext();
        setCompoundDrawablesWithIntrinsicBounds(left != 0 ? context.getDrawable(left) : null, top != 0 ? context.getDrawable(top) : null, right != 0 ? context.getDrawable(right) : null, bottom != 0 ? context.getDrawable(bottom) : null);
    }

    /**
     * Sets the Drawables (if any) to appear to the left of, above, to the
     * right of, and below the text. Use {@code null} if you do not want a
     * Drawable there. The Drawables' bounds will be set to their intrinsic
     * bounds.
     * <p>
     * Calling this method will overwrite any Drawables previously set using
     * {@link #setCompoundDrawablesRelative} or related methods.
     *
     * @unknown ref android.R.styleable#TextView_drawableLeft
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableRight
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    @android.view.RemotableViewMethod
    public void setCompoundDrawablesWithIntrinsicBounds(@android.annotation.Nullable
    android.graphics.drawable.Drawable left, @android.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.annotation.Nullable
    android.graphics.drawable.Drawable right, @android.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, left.getIntrinsicWidth(), left.getIntrinsicHeight());
        }
        if (right != null) {
            right.setBounds(0, 0, right.getIntrinsicWidth(), right.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    /**
     * Sets the Drawables (if any) to appear to the start of, above, to the end
     * of, and below the text. Use {@code null} if you do not want a Drawable
     * there. The Drawables must already have had {@link Drawable#setBounds}
     * called.
     * <p>
     * Calling this method will overwrite any Drawables previously set using
     * {@link #setCompoundDrawables} or related methods.
     *
     * @unknown ref android.R.styleable#TextView_drawableStart
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableEnd
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    @android.view.RemotableViewMethod
    public void setCompoundDrawablesRelative(@android.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        TextView.Drawables dr = mDrawables;
        // We're switching to relative, discard absolute.
        if (dr != null) {
            if (dr.mShowing[TextView.Drawables.LEFT] != null) {
                dr.mShowing[TextView.Drawables.LEFT].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.LEFT] = dr.mDrawableLeftInitial = null;
            if (dr.mShowing[TextView.Drawables.RIGHT] != null) {
                dr.mShowing[TextView.Drawables.RIGHT].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.RIGHT] = dr.mDrawableRightInitial = null;
            dr.mDrawableSizeLeft = dr.mDrawableHeightLeft = 0;
            dr.mDrawableSizeRight = dr.mDrawableHeightRight = 0;
        }
        final boolean drawables = (((start != null) || (top != null)) || (end != null)) || (bottom != null);
        if (!drawables) {
            // Clearing drawables...  can we free the data structure?
            if (dr != null) {
                if (!dr.hasMetadata()) {
                    mDrawables = null;
                } else {
                    // We need to retain the last set padding, so just clear
                    // out all of the fields in the existing structure.
                    if (dr.mDrawableStart != null)
                        dr.mDrawableStart.setCallback(null);

                    dr.mDrawableStart = null;
                    if (dr.mShowing[TextView.Drawables.TOP] != null) {
                        dr.mShowing[TextView.Drawables.TOP].setCallback(null);
                    }
                    dr.mShowing[TextView.Drawables.TOP] = null;
                    if (dr.mDrawableEnd != null) {
                        dr.mDrawableEnd.setCallback(null);
                    }
                    dr.mDrawableEnd = null;
                    if (dr.mShowing[TextView.Drawables.BOTTOM] != null) {
                        dr.mShowing[TextView.Drawables.BOTTOM].setCallback(null);
                    }
                    dr.mShowing[TextView.Drawables.BOTTOM] = null;
                    dr.mDrawableSizeStart = dr.mDrawableHeightStart = 0;
                    dr.mDrawableSizeEnd = dr.mDrawableHeightEnd = 0;
                    dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
                    dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
                }
            }
        } else {
            if (dr == null) {
                mDrawables = dr = new TextView.Drawables(getContext());
            }
            mDrawables.mOverride = true;
            if ((dr.mDrawableStart != start) && (dr.mDrawableStart != null)) {
                dr.mDrawableStart.setCallback(null);
            }
            dr.mDrawableStart = start;
            if ((dr.mShowing[TextView.Drawables.TOP] != top) && (dr.mShowing[TextView.Drawables.TOP] != null)) {
                dr.mShowing[TextView.Drawables.TOP].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.TOP] = top;
            if ((dr.mDrawableEnd != end) && (dr.mDrawableEnd != null)) {
                dr.mDrawableEnd.setCallback(null);
            }
            dr.mDrawableEnd = end;
            if ((dr.mShowing[TextView.Drawables.BOTTOM] != bottom) && (dr.mShowing[TextView.Drawables.BOTTOM] != null)) {
                dr.mShowing[TextView.Drawables.BOTTOM].setCallback(null);
            }
            dr.mShowing[TextView.Drawables.BOTTOM] = bottom;
            final android.graphics.Rect compoundRect = dr.mCompoundRect;
            int[] state;
            state = getDrawableState();
            if (start != null) {
                start.setState(state);
                start.copyBounds(compoundRect);
                start.setCallback(this);
                dr.mDrawableSizeStart = compoundRect.width();
                dr.mDrawableHeightStart = compoundRect.height();
            } else {
                dr.mDrawableSizeStart = dr.mDrawableHeightStart = 0;
            }
            if (end != null) {
                end.setState(state);
                end.copyBounds(compoundRect);
                end.setCallback(this);
                dr.mDrawableSizeEnd = compoundRect.width();
                dr.mDrawableHeightEnd = compoundRect.height();
            } else {
                dr.mDrawableSizeEnd = dr.mDrawableHeightEnd = 0;
            }
            if (top != null) {
                top.setState(state);
                top.copyBounds(compoundRect);
                top.setCallback(this);
                dr.mDrawableSizeTop = compoundRect.height();
                dr.mDrawableWidthTop = compoundRect.width();
            } else {
                dr.mDrawableSizeTop = dr.mDrawableWidthTop = 0;
            }
            if (bottom != null) {
                bottom.setState(state);
                bottom.copyBounds(compoundRect);
                bottom.setCallback(this);
                dr.mDrawableSizeBottom = compoundRect.height();
                dr.mDrawableWidthBottom = compoundRect.width();
            } else {
                dr.mDrawableSizeBottom = dr.mDrawableWidthBottom = 0;
            }
        }
        resetResolvedDrawables();
        resolveDrawables();
        invalidate();
        requestLayout();
    }

    /**
     * Sets the Drawables (if any) to appear to the start of, above, to the end
     * of, and below the text. Use 0 if you do not want a Drawable there. The
     * Drawables' bounds will be set to their intrinsic bounds.
     * <p>
     * Calling this method will overwrite any Drawables previously set using
     * {@link #setCompoundDrawables} or related methods.
     *
     * @param start
     * 		Resource identifier of the start Drawable.
     * @param top
     * 		Resource identifier of the top Drawable.
     * @param end
     * 		Resource identifier of the end Drawable.
     * @param bottom
     * 		Resource identifier of the bottom Drawable.
     * @unknown ref android.R.styleable#TextView_drawableStart
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableEnd
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    @android.view.RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.annotation.DrawableRes
    int start, @android.annotation.DrawableRes
    int top, @android.annotation.DrawableRes
    int end, @android.annotation.DrawableRes
    int bottom) {
        final android.content.Context context = getContext();
        setCompoundDrawablesRelativeWithIntrinsicBounds(start != 0 ? context.getDrawable(start) : null, top != 0 ? context.getDrawable(top) : null, end != 0 ? context.getDrawable(end) : null, bottom != 0 ? context.getDrawable(bottom) : null);
    }

    /**
     * Sets the Drawables (if any) to appear to the start of, above, to the end
     * of, and below the text. Use {@code null} if you do not want a Drawable
     * there. The Drawables' bounds will be set to their intrinsic bounds.
     * <p>
     * Calling this method will overwrite any Drawables previously set using
     * {@link #setCompoundDrawables} or related methods.
     *
     * @unknown ref android.R.styleable#TextView_drawableStart
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableEnd
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    @android.view.RemotableViewMethod
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@android.annotation.Nullable
    android.graphics.drawable.Drawable start, @android.annotation.Nullable
    android.graphics.drawable.Drawable top, @android.annotation.Nullable
    android.graphics.drawable.Drawable end, @android.annotation.Nullable
    android.graphics.drawable.Drawable bottom) {
        if (start != null) {
            start.setBounds(0, 0, start.getIntrinsicWidth(), start.getIntrinsicHeight());
        }
        if (end != null) {
            end.setBounds(0, 0, end.getIntrinsicWidth(), end.getIntrinsicHeight());
        }
        if (top != null) {
            top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, bottom.getIntrinsicWidth(), bottom.getIntrinsicHeight());
        }
        setCompoundDrawablesRelative(start, top, end, bottom);
    }

    /**
     * Returns drawables for the left, top, right, and bottom borders.
     *
     * @unknown ref android.R.styleable#TextView_drawableLeft
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableRight
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    @android.annotation.NonNull
    public android.graphics.drawable.Drawable[] getCompoundDrawables() {
        final TextView.Drawables dr = mDrawables;
        if (dr != null) {
            return dr.mShowing.clone();
        } else {
            return new android.graphics.drawable.Drawable[]{ null, null, null, null };
        }
    }

    /**
     * Returns drawables for the start, top, end, and bottom borders.
     *
     * @unknown ref android.R.styleable#TextView_drawableStart
     * @unknown ref android.R.styleable#TextView_drawableTop
     * @unknown ref android.R.styleable#TextView_drawableEnd
     * @unknown ref android.R.styleable#TextView_drawableBottom
     */
    @android.annotation.NonNull
    public android.graphics.drawable.Drawable[] getCompoundDrawablesRelative() {
        final TextView.Drawables dr = mDrawables;
        if (dr != null) {
            return new android.graphics.drawable.Drawable[]{ dr.mDrawableStart, dr.mShowing[TextView.Drawables.TOP], dr.mDrawableEnd, dr.mShowing[TextView.Drawables.BOTTOM] };
        } else {
            return new android.graphics.drawable.Drawable[]{ null, null, null, null };
        }
    }

    /**
     * Sets the size of the padding between the compound drawables and
     * the text.
     *
     * @unknown ref android.R.styleable#TextView_drawablePadding
     */
    @android.view.RemotableViewMethod
    public void setCompoundDrawablePadding(int pad) {
        TextView.Drawables dr = mDrawables;
        if (pad == 0) {
            if (dr != null) {
                dr.mDrawablePadding = pad;
            }
        } else {
            if (dr == null) {
                mDrawables = dr = new TextView.Drawables(getContext());
            }
            dr.mDrawablePadding = pad;
        }
        invalidate();
        requestLayout();
    }

    /**
     * Returns the padding between the compound drawables and the text.
     *
     * @unknown ref android.R.styleable#TextView_drawablePadding
     */
    @android.view.inspector.InspectableProperty(name = "drawablePadding")
    public int getCompoundDrawablePadding() {
        final TextView.Drawables dr = mDrawables;
        return dr != null ? dr.mDrawablePadding : 0;
    }

    /**
     * Applies a tint to the compound drawables. Does not modify the
     * current tint mode, which is {@link PorterDuff.Mode#SRC_IN} by default.
     * <p>
     * Subsequent calls to
     * {@link #setCompoundDrawables(Drawable, Drawable, Drawable, Drawable)}
     * and related methods will automatically mutate the drawables and apply
     * the specified tint and tint mode using
     * {@link Drawable#setTintList(ColorStateList)}.
     *
     * @param tint
     * 		the tint to apply, may be {@code null} to clear tint
     * @unknown ref android.R.styleable#TextView_drawableTint
     * @see #getCompoundDrawableTintList()
     * @see Drawable#setTintList(ColorStateList)
     */
    public void setCompoundDrawableTintList(@android.annotation.Nullable
    android.content.res.ColorStateList tint) {
        if (mDrawables == null) {
            mDrawables = new TextView.Drawables(getContext());
        }
        mDrawables.mTintList = tint;
        mDrawables.mHasTint = true;
        applyCompoundDrawableTint();
    }

    /**
     *
     *
     * @return the tint applied to the compound drawables
     * @unknown ref android.R.styleable#TextView_drawableTint
     * @see #setCompoundDrawableTintList(ColorStateList)
     */
    @android.view.inspector.InspectableProperty(name = "drawableTint")
    public android.content.res.ColorStateList getCompoundDrawableTintList() {
        return mDrawables != null ? mDrawables.mTintList : null;
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setCompoundDrawableTintList(ColorStateList)} to the compound
     * drawables. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param tintMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#TextView_drawableTintMode
     * @see #setCompoundDrawableTintList(ColorStateList)
     * @see Drawable#setTintMode(PorterDuff.Mode)
     */
    public void setCompoundDrawableTintMode(@android.annotation.Nullable
    android.graphics.PorterDuff.Mode tintMode) {
        setCompoundDrawableTintBlendMode(tintMode != null ? android.graphics.BlendMode.fromValue(tintMode.nativeInt) : null);
    }

    /**
     * Specifies the blending mode used to apply the tint specified by
     * {@link #setCompoundDrawableTintList(ColorStateList)} to the compound
     * drawables. The default mode is {@link PorterDuff.Mode#SRC_IN}.
     *
     * @param blendMode
     * 		the blending mode used to apply the tint, may be
     * 		{@code null} to clear tint
     * @unknown ref android.R.styleable#TextView_drawableTintMode
     * @see #setCompoundDrawableTintList(ColorStateList)
     * @see Drawable#setTintBlendMode(BlendMode)
     */
    public void setCompoundDrawableTintBlendMode(@android.annotation.Nullable
    android.graphics.BlendMode blendMode) {
        if (mDrawables == null) {
            mDrawables = new TextView.Drawables(getContext());
        }
        mDrawables.mBlendMode = blendMode;
        mDrawables.mHasTintMode = true;
        applyCompoundDrawableTint();
    }

    /**
     * Returns the blending mode used to apply the tint to the compound
     * drawables, if specified.
     *
     * @return the blending mode used to apply the tint to the compound
    drawables
     * @unknown ref android.R.styleable#TextView_drawableTintMode
     * @see #setCompoundDrawableTintMode(PorterDuff.Mode)
     */
    @android.view.inspector.InspectableProperty(name = "drawableTintMode")
    public android.graphics.PorterDuff.Mode getCompoundDrawableTintMode() {
        android.graphics.BlendMode mode = getCompoundDrawableTintBlendMode();
        return mode != null ? android.graphics.BlendMode.blendModeToPorterDuffMode(mode) : null;
    }

    /**
     * Returns the blending mode used to apply the tint to the compound
     * drawables, if specified.
     *
     * @return the blending mode used to apply the tint to the compound
    drawables
     * @unknown ref android.R.styleable#TextView_drawableTintMode
     * @see #setCompoundDrawableTintBlendMode(BlendMode)
     */
    @android.view.inspector.InspectableProperty(name = "drawableBlendMode", attributeId = com.android.internal.R.styleable.TextView_drawableTintMode)
    @android.annotation.Nullable
    public android.graphics.BlendMode getCompoundDrawableTintBlendMode() {
        return mDrawables != null ? mDrawables.mBlendMode : null;
    }

    private void applyCompoundDrawableTint() {
        if (mDrawables == null) {
            return;
        }
        if (mDrawables.mHasTint || mDrawables.mHasTintMode) {
            final android.content.res.ColorStateList tintList = mDrawables.mTintList;
            final android.graphics.BlendMode blendMode = mDrawables.mBlendMode;
            final boolean hasTint = mDrawables.mHasTint;
            final boolean hasTintMode = mDrawables.mHasTintMode;
            final int[] state = getDrawableState();
            for (android.graphics.drawable.Drawable dr : mDrawables.mShowing) {
                if (dr == null) {
                    continue;
                }
                if (dr == mDrawables.mDrawableError) {
                    // From a developer's perspective, the error drawable isn't
                    // a compound drawable. Don't apply the generic compound
                    // drawable tint to it.
                    continue;
                }
                dr.mutate();
                if (hasTint) {
                    dr.setTintList(tintList);
                }
                if (hasTintMode) {
                    dr.setTintBlendMode(blendMode);
                }
                // The drawable (or one of its children) may not have been
                // stateful before applying the tint, so let's try again.
                if (dr.isStateful()) {
                    dr.setState(state);
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     * @see #setFirstBaselineToTopHeight(int)
     * @see #setLastBaselineToBottomHeight(int)
     */
    @java.lang.Override
    public void setPadding(int left, int top, int right, int bottom) {
        if ((((left != mPaddingLeft) || (right != mPaddingRight)) || (top != mPaddingTop)) || (bottom != mPaddingBottom)) {
            nullLayouts();
        }
        // the super call will requestLayout()
        super.setPadding(left, top, right, bottom);
        invalidate();
    }

    /**
     *
     *
     * @unknown 
     * @see #setFirstBaselineToTopHeight(int)
     * @see #setLastBaselineToBottomHeight(int)
     */
    @java.lang.Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        if ((((start != getPaddingStart()) || (end != getPaddingEnd())) || (top != mPaddingTop)) || (bottom != mPaddingBottom)) {
            nullLayouts();
        }
        // the super call will requestLayout()
        super.setPaddingRelative(start, top, end, bottom);
        invalidate();
    }

    /**
     * Updates the top padding of the TextView so that {@code firstBaselineToTopHeight} is
     * the distance between the top of the TextView and first line's baseline.
     * <p>
     * <img src="{@docRoot }reference/android/images/text/widget/first_last_baseline.png" />
     * <figcaption>First and last baseline metrics for a TextView.</figcaption>
     *
     * <strong>Note</strong> that if {@code FontMetrics.top} or {@code FontMetrics.ascent} was
     * already greater than {@code firstBaselineToTopHeight}, the top padding is not updated.
     * Moreover since this function sets the top padding, if the height of the TextView is less than
     * the sum of top padding, line height and bottom padding, top of the line will be pushed
     * down and bottom will be clipped.
     *
     * @param firstBaselineToTopHeight
     * 		distance between first baseline to top of the container
     * 		in pixels
     * @see #getFirstBaselineToTopHeight()
     * @see #setLastBaselineToBottomHeight(int)
     * @see #setPadding(int, int, int, int)
     * @see #setPaddingRelative(int, int, int, int)
     * @unknown ref android.R.styleable#TextView_firstBaselineToTopHeight
     */
    public void setFirstBaselineToTopHeight(@android.annotation.Px
    @android.annotation.IntRange(from = 0)
    int firstBaselineToTopHeight) {
        com.android.internal.util.Preconditions.checkArgumentNonnegative(firstBaselineToTopHeight);
        final android.graphics.Paint.FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        final int fontMetricsTop;
        if (getIncludeFontPadding()) {
            fontMetricsTop = fontMetrics.top;
        } else {
            fontMetricsTop = fontMetrics.ascent;
        }
        // TODO: Decide if we want to ignore density ratio (i.e. when the user changes font size
        // in settings). At the moment, we don't.
        if (firstBaselineToTopHeight > java.lang.Math.abs(fontMetricsTop)) {
            final int paddingTop = firstBaselineToTopHeight - (-fontMetricsTop);
            setPadding(getPaddingLeft(), paddingTop, getPaddingRight(), getPaddingBottom());
        }
    }

    /**
     * Updates the bottom padding of the TextView so that {@code lastBaselineToBottomHeight} is
     * the distance between the bottom of the TextView and the last line's baseline.
     * <p>
     * <img src="{@docRoot }reference/android/images/text/widget/first_last_baseline.png" />
     * <figcaption>First and last baseline metrics for a TextView.</figcaption>
     *
     * <strong>Note</strong> that if {@code FontMetrics.bottom} or {@code FontMetrics.descent} was
     * already greater than {@code lastBaselineToBottomHeight}, the bottom padding is not updated.
     * Moreover since this function sets the bottom padding, if the height of the TextView is less
     * than the sum of top padding, line height and bottom padding, bottom of the text will be
     * clipped.
     *
     * @param lastBaselineToBottomHeight
     * 		distance between last baseline to bottom of the container
     * 		in pixels
     * @see #getLastBaselineToBottomHeight()
     * @see #setFirstBaselineToTopHeight(int)
     * @see #setPadding(int, int, int, int)
     * @see #setPaddingRelative(int, int, int, int)
     * @unknown ref android.R.styleable#TextView_lastBaselineToBottomHeight
     */
    public void setLastBaselineToBottomHeight(@android.annotation.Px
    @android.annotation.IntRange(from = 0)
    int lastBaselineToBottomHeight) {
        com.android.internal.util.Preconditions.checkArgumentNonnegative(lastBaselineToBottomHeight);
        final android.graphics.Paint.FontMetricsInt fontMetrics = getPaint().getFontMetricsInt();
        final int fontMetricsBottom;
        if (getIncludeFontPadding()) {
            fontMetricsBottom = fontMetrics.bottom;
        } else {
            fontMetricsBottom = fontMetrics.descent;
        }
        // TODO: Decide if we want to ignore density ratio (i.e. when the user changes font size
        // in settings). At the moment, we don't.
        if (lastBaselineToBottomHeight > java.lang.Math.abs(fontMetricsBottom)) {
            final int paddingBottom = lastBaselineToBottomHeight - fontMetricsBottom;
            setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), paddingBottom);
        }
    }

    /**
     * Returns the distance between the first text baseline and the top of this TextView.
     *
     * @see #setFirstBaselineToTopHeight(int)
     * @unknown ref android.R.styleable#TextView_firstBaselineToTopHeight
     */
    @android.view.inspector.InspectableProperty
    public int getFirstBaselineToTopHeight() {
        return getPaddingTop() - getPaint().getFontMetricsInt().top;
    }

    /**
     * Returns the distance between the last text baseline and the bottom of this TextView.
     *
     * @see #setLastBaselineToBottomHeight(int)
     * @unknown ref android.R.styleable#TextView_lastBaselineToBottomHeight
     */
    @android.view.inspector.InspectableProperty
    public int getLastBaselineToBottomHeight() {
        return getPaddingBottom() + getPaint().getFontMetricsInt().bottom;
    }

    /**
     * Gets the autolink mask of the text.
     *
     * See {@link Linkify#ALL} and peers for possible values.
     *
     * @unknown ref android.R.styleable#TextView_autoLink
     */
    @android.view.inspector.InspectableProperty(name = "autoLink", flagMapping = { @android.view.inspector.InspectableProperty.FlagEntry(name = "web", target = android.text.util.Linkify.WEB_URLS), @android.view.inspector.InspectableProperty.FlagEntry(name = "email", target = android.text.util.Linkify.EMAIL_ADDRESSES), @android.view.inspector.InspectableProperty.FlagEntry(name = "phone", target = android.text.util.Linkify.PHONE_NUMBERS), @android.view.inspector.InspectableProperty.FlagEntry(name = "map", target = android.text.util.Linkify.MAP_ADDRESSES) })
    public final int getAutoLinkMask() {
        return mAutoLinkMask;
    }

    /**
     * Sets the Drawable corresponding to the selection handle used for
     * positioning the cursor within text. The Drawable defaults to the value
     * of the textSelectHandle attribute.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @see #setTextSelectHandle(int)
     * @unknown ref android.R.styleable#TextView_textSelectHandle
     */
    @android.view.RemotableViewMethod
    public void setTextSelectHandle(@android.annotation.NonNull
    android.graphics.drawable.Drawable textSelectHandle) {
        com.android.internal.util.Preconditions.checkNotNull(textSelectHandle, "The text select handle should not be null.");
        mTextSelectHandle = textSelectHandle;
        mTextSelectHandleRes = 0;
        if (mEditor != null) {
            /* overwrite */
            mEditor.loadHandleDrawables(true);
        }
    }

    /**
     * Sets the Drawable corresponding to the selection handle used for
     * positioning the cursor within text. The Drawable defaults to the value
     * of the textSelectHandle attribute.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @see #setTextSelectHandle(Drawable)
     * @unknown ref android.R.styleable#TextView_textSelectHandle
     */
    @android.view.RemotableViewMethod
    public void setTextSelectHandle(@android.annotation.DrawableRes
    int textSelectHandle) {
        com.android.internal.util.Preconditions.checkArgument(textSelectHandle != 0, "The text select handle should be a valid drawable resource id.");
        setTextSelectHandle(mContext.getDrawable(textSelectHandle));
    }

    /**
     * Returns the Drawable corresponding to the selection handle used
     * for positioning the cursor within text.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @return the text select handle drawable
     * @see #setTextSelectHandle(Drawable)
     * @see #setTextSelectHandle(int)
     * @unknown ref android.R.styleable#TextView_textSelectHandle
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getTextSelectHandle() {
        if ((mTextSelectHandle == null) && (mTextSelectHandleRes != 0)) {
            mTextSelectHandle = mContext.getDrawable(mTextSelectHandleRes);
        }
        return mTextSelectHandle;
    }

    /**
     * Sets the Drawable corresponding to the left handle used
     * for selecting text. The Drawable defaults to the value of the
     * textSelectHandleLeft attribute.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @see #setTextSelectHandleLeft(int)
     * @unknown ref android.R.styleable#TextView_textSelectHandleLeft
     */
    @android.view.RemotableViewMethod
    public void setTextSelectHandleLeft(@android.annotation.NonNull
    android.graphics.drawable.Drawable textSelectHandleLeft) {
        com.android.internal.util.Preconditions.checkNotNull(textSelectHandleLeft, "The left text select handle should not be null.");
        mTextSelectHandleLeft = textSelectHandleLeft;
        mTextSelectHandleLeftRes = 0;
        if (mEditor != null) {
            /* overwrite */
            mEditor.loadHandleDrawables(true);
        }
    }

    /**
     * Sets the Drawable corresponding to the left handle used
     * for selecting text. The Drawable defaults to the value of the
     * textSelectHandleLeft attribute.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @see #setTextSelectHandleLeft(Drawable)
     * @unknown ref android.R.styleable#TextView_textSelectHandleLeft
     */
    @android.view.RemotableViewMethod
    public void setTextSelectHandleLeft(@android.annotation.DrawableRes
    int textSelectHandleLeft) {
        com.android.internal.util.Preconditions.checkArgument(textSelectHandleLeft != 0, "The text select left handle should be a valid drawable resource id.");
        setTextSelectHandleLeft(mContext.getDrawable(textSelectHandleLeft));
    }

    /**
     * Returns the Drawable corresponding to the left handle used
     * for selecting text.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @return the left text selection handle drawable
     * @see #setTextSelectHandleLeft(Drawable)
     * @see #setTextSelectHandleLeft(int)
     * @unknown ref android.R.styleable#TextView_textSelectHandleLeft
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getTextSelectHandleLeft() {
        if ((mTextSelectHandleLeft == null) && (mTextSelectHandleLeftRes != 0)) {
            mTextSelectHandleLeft = mContext.getDrawable(mTextSelectHandleLeftRes);
        }
        return mTextSelectHandleLeft;
    }

    /**
     * Sets the Drawable corresponding to the right handle used
     * for selecting text. The Drawable defaults to the value of the
     * textSelectHandleRight attribute.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @see #setTextSelectHandleRight(int)
     * @unknown ref android.R.styleable#TextView_textSelectHandleRight
     */
    @android.view.RemotableViewMethod
    public void setTextSelectHandleRight(@android.annotation.NonNull
    android.graphics.drawable.Drawable textSelectHandleRight) {
        com.android.internal.util.Preconditions.checkNotNull(textSelectHandleRight, "The right text select handle should not be null.");
        mTextSelectHandleRight = textSelectHandleRight;
        mTextSelectHandleRightRes = 0;
        if (mEditor != null) {
            /* overwrite */
            mEditor.loadHandleDrawables(true);
        }
    }

    /**
     * Sets the Drawable corresponding to the right handle used
     * for selecting text. The Drawable defaults to the value of the
     * textSelectHandleRight attribute.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @see #setTextSelectHandleRight(Drawable)
     * @unknown ref android.R.styleable#TextView_textSelectHandleRight
     */
    @android.view.RemotableViewMethod
    public void setTextSelectHandleRight(@android.annotation.DrawableRes
    int textSelectHandleRight) {
        com.android.internal.util.Preconditions.checkArgument(textSelectHandleRight != 0, "The text select right handle should be a valid drawable resource id.");
        setTextSelectHandleRight(mContext.getDrawable(textSelectHandleRight));
    }

    /**
     * Returns the Drawable corresponding to the right handle used
     * for selecting text.
     * Note that any change applied to the handle Drawable will not be visible
     * until the handle is hidden and then drawn again.
     *
     * @return the right text selection handle drawable
     * @see #setTextSelectHandleRight(Drawable)
     * @see #setTextSelectHandleRight(int)
     * @unknown ref android.R.styleable#TextView_textSelectHandleRight
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getTextSelectHandleRight() {
        if ((mTextSelectHandleRight == null) && (mTextSelectHandleRightRes != 0)) {
            mTextSelectHandleRight = mContext.getDrawable(mTextSelectHandleRightRes);
        }
        return mTextSelectHandleRight;
    }

    /**
     * Sets the Drawable corresponding to the text cursor. The Drawable defaults to the
     * value of the textCursorDrawable attribute.
     * Note that any change applied to the cursor Drawable will not be visible
     * until the cursor is hidden and then drawn again.
     *
     * @see #setTextCursorDrawable(int)
     * @unknown ref android.R.styleable#TextView_textCursorDrawable
     */
    public void setTextCursorDrawable(@android.annotation.Nullable
    android.graphics.drawable.Drawable textCursorDrawable) {
        mCursorDrawable = textCursorDrawable;
        mCursorDrawableRes = 0;
        if (mEditor != null) {
            mEditor.loadCursorDrawable();
        }
    }

    /**
     * Sets the Drawable corresponding to the text cursor. The Drawable defaults to the
     * value of the textCursorDrawable attribute.
     * Note that any change applied to the cursor Drawable will not be visible
     * until the cursor is hidden and then drawn again.
     *
     * @see #setTextCursorDrawable(Drawable)
     * @unknown ref android.R.styleable#TextView_textCursorDrawable
     */
    public void setTextCursorDrawable(@android.annotation.DrawableRes
    int textCursorDrawable) {
        setTextCursorDrawable(textCursorDrawable != 0 ? mContext.getDrawable(textCursorDrawable) : null);
    }

    /**
     * Returns the Drawable corresponding to the text cursor.
     * Note that any change applied to the cursor Drawable will not be visible
     * until the cursor is hidden and then drawn again.
     *
     * @return the text cursor drawable
     * @see #setTextCursorDrawable(Drawable)
     * @see #setTextCursorDrawable(int)
     * @unknown ref android.R.styleable#TextView_textCursorDrawable
     */
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable getTextCursorDrawable() {
        if ((mCursorDrawable == null) && (mCursorDrawableRes != 0)) {
            mCursorDrawable = mContext.getDrawable(mCursorDrawableRes);
        }
        return mCursorDrawable;
    }

    /**
     * Sets the text appearance from the specified style resource.
     * <p>
     * Use a framework-defined {@code TextAppearance} style like
     * {@link android.R.style#TextAppearance_Material_Body1 @android:style/TextAppearance.Material.Body1}
     * or see {@link android.R.styleable#TextAppearance TextAppearance} for the
     * set of attributes that can be used in a custom style.
     *
     * @param resId
     * 		the resource identifier of the style to apply
     * @unknown ref android.R.styleable#TextView_textAppearance
     */
    @java.lang.SuppressWarnings("deprecation")
    public void setTextAppearance(@android.annotation.StyleRes
    int resId) {
        setTextAppearance(mContext, resId);
    }

    /**
     * Sets the text color, size, style, hint color, and highlight color
     * from the specified TextAppearance resource.
     *
     * @deprecated Use {@link #setTextAppearance(int)} instead.
     */
    @java.lang.Deprecated
    public void setTextAppearance(android.content.Context context, @android.annotation.StyleRes
    int resId) {
        final android.content.res.TypedArray ta = context.obtainStyledAttributes(resId, R.styleable.TextAppearance);
        final TextView.TextAppearanceAttributes attributes = new TextView.TextAppearanceAttributes();
        /* styleArray */
        readTextAppearance(context, ta, attributes, false);
        ta.recycle();
        applyTextAppearance(attributes);
    }

    /**
     * Set of attributes that can be defined in a Text Appearance. This is used to simplify the code
     * that reads these attributes in the constructor and in {@link #setTextAppearance}.
     */
    private static class TextAppearanceAttributes {
        int mTextColorHighlight = 0;

        android.content.res.ColorStateList mTextColor = null;

        android.content.res.ColorStateList mTextColorHint = null;

        android.content.res.ColorStateList mTextColorLink = null;

        int mTextSize = -1;

        android.os.LocaleList mTextLocales = null;

        java.lang.String mFontFamily = null;

        android.graphics.Typeface mFontTypeface = null;

        boolean mFontFamilyExplicit = false;

        int mTypefaceIndex = -1;

        int mTextStyle = 0;

        int mFontWeight = -1;

        boolean mAllCaps = false;

        int mShadowColor = 0;

        float mShadowDx = 0;

        float mShadowDy = 0;

        float mShadowRadius = 0;

        boolean mHasElegant = false;

        boolean mElegant = false;

        boolean mHasFallbackLineSpacing = false;

        boolean mFallbackLineSpacing = false;

        boolean mHasLetterSpacing = false;

        float mLetterSpacing = 0;

        java.lang.String mFontFeatureSettings = null;

        java.lang.String mFontVariationSettings = null;

        @java.lang.Override
        public java.lang.String toString() {
            return ((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((((("TextAppearanceAttributes {\n" + "    mTextColorHighlight:") + mTextColorHighlight) + "\n") + "    mTextColor:") + mTextColor) + "\n") + "    mTextColorHint:") + mTextColorHint) + "\n") + "    mTextColorLink:") + mTextColorLink) + "\n") + "    mTextSize:") + mTextSize) + "\n") + "    mTextLocales:") + mTextLocales) + "\n") + "    mFontFamily:") + mFontFamily) + "\n") + "    mFontTypeface:") + mFontTypeface) + "\n") + "    mFontFamilyExplicit:") + mFontFamilyExplicit) + "\n") + "    mTypefaceIndex:") + mTypefaceIndex) + "\n") + "    mTextStyle:") + mTextStyle) + "\n") + "    mFontWeight:") + mFontWeight) + "\n") + "    mAllCaps:") + mAllCaps) + "\n") + "    mShadowColor:") + mShadowColor) + "\n") + "    mShadowDx:") + mShadowDx) + "\n") + "    mShadowDy:") + mShadowDy) + "\n") + "    mShadowRadius:") + mShadowRadius) + "\n") + "    mHasElegant:") + mHasElegant) + "\n") + "    mElegant:") + mElegant) + "\n") + "    mHasFallbackLineSpacing:") + mHasFallbackLineSpacing) + "\n") + "    mFallbackLineSpacing:") + mFallbackLineSpacing) + "\n") + "    mHasLetterSpacing:") + mHasLetterSpacing) + "\n") + "    mLetterSpacing:") + mLetterSpacing) + "\n") + "    mFontFeatureSettings:") + mFontFeatureSettings) + "\n") + "    mFontVariationSettings:") + mFontVariationSettings) + "\n") + "}";
        }
    }

    // Maps styleable attributes that exist both in TextView style and TextAppearance.
    private static final android.util.SparseIntArray sAppearanceValues = new android.util.SparseIntArray();

    static {
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textColorHighlight, android.widget.com.android.internal.R.styleable.TextAppearance_textColorHighlight);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textColor, android.widget.com.android.internal.R.styleable.TextAppearance_textColor);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textColorHint, android.widget.com.android.internal.R.styleable.TextAppearance_textColorHint);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textColorLink, android.widget.com.android.internal.R.styleable.TextAppearance_textColorLink);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textSize, android.widget.com.android.internal.R.styleable.TextAppearance_textSize);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textLocale, android.widget.com.android.internal.R.styleable.TextAppearance_textLocale);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_typeface, android.widget.com.android.internal.R.styleable.TextAppearance_typeface);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_fontFamily, android.widget.com.android.internal.R.styleable.TextAppearance_fontFamily);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textStyle, android.widget.com.android.internal.R.styleable.TextAppearance_textStyle);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textFontWeight, android.widget.com.android.internal.R.styleable.TextAppearance_textFontWeight);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_textAllCaps, android.widget.com.android.internal.R.styleable.TextAppearance_textAllCaps);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_shadowColor, android.widget.com.android.internal.R.styleable.TextAppearance_shadowColor);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_shadowDx, android.widget.com.android.internal.R.styleable.TextAppearance_shadowDx);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_shadowDy, android.widget.com.android.internal.R.styleable.TextAppearance_shadowDy);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_shadowRadius, android.widget.com.android.internal.R.styleable.TextAppearance_shadowRadius);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_elegantTextHeight, android.widget.com.android.internal.R.styleable.TextAppearance_elegantTextHeight);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_fallbackLineSpacing, android.widget.com.android.internal.R.styleable.TextAppearance_fallbackLineSpacing);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_letterSpacing, android.widget.com.android.internal.R.styleable.TextAppearance_letterSpacing);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_fontFeatureSettings, android.widget.com.android.internal.R.styleable.TextAppearance_fontFeatureSettings);
        TextView.sAppearanceValues.put(android.widget.com.android.internal.R.styleable.TextView_fontVariationSettings, android.widget.com.android.internal.R.styleable.TextAppearance_fontVariationSettings);
    }

    /**
     * Read the Text Appearance attributes from a given TypedArray and set its values to the given
     * set. If the TypedArray contains a value that was already set in the given attributes, that
     * will be overridden.
     *
     * @param context
     * 		The Context to be used
     * @param appearance
     * 		The TypedArray to read properties from
     * @param attributes
     * 		the TextAppearanceAttributes to fill in
     * @param styleArray
     * 		Whether the given TypedArray is a style or a TextAppearance. This defines
     * 		what attribute indexes will be used to read the properties.
     */
    private void readTextAppearance(android.content.Context context, android.content.res.TypedArray appearance, TextView.TextAppearanceAttributes attributes, boolean styleArray) {
        final int n = appearance.getIndexCount();
        for (int i = 0; i < n; i++) {
            final int attr = appearance.getIndex(i);
            int index = attr;
            // Translate style array index ids to TextAppearance ids.
            if (styleArray) {
                index = TextView.sAppearanceValues.get(attr, -1);
                if (index == (-1)) {
                    // This value is not part of a Text Appearance and should be ignored.
                    continue;
                }
            }
            switch (index) {
                case android.widget.com.android.internal.R.styleable.TextAppearance_textColorHighlight :
                    attributes.mTextColorHighlight = appearance.getColor(attr, attributes.mTextColorHighlight);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textColor :
                    attributes.mTextColor = appearance.getColorStateList(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textColorHint :
                    attributes.mTextColorHint = appearance.getColorStateList(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textColorLink :
                    attributes.mTextColorLink = appearance.getColorStateList(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textSize :
                    attributes.mTextSize = appearance.getDimensionPixelSize(attr, attributes.mTextSize);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textLocale :
                    final java.lang.String localeString = appearance.getString(attr);
                    if (localeString != null) {
                        final android.os.LocaleList localeList = android.os.LocaleList.forLanguageTags(localeString);
                        if (!localeList.isEmpty()) {
                            attributes.mTextLocales = localeList;
                        }
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_typeface :
                    attributes.mTypefaceIndex = appearance.getInt(attr, attributes.mTypefaceIndex);
                    if ((attributes.mTypefaceIndex != (-1)) && (!attributes.mFontFamilyExplicit)) {
                        attributes.mFontFamily = null;
                    }
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_fontFamily :
                    if ((!context.isRestricted()) && context.canLoadUnsafeResources()) {
                        try {
                            attributes.mFontTypeface = appearance.getFont(attr);
                        } catch (java.lang.UnsupportedOperationException | android.content.res.Resources.NotFoundException e) {
                            // Expected if it is not a font resource.
                        }
                    }
                    if (attributes.mFontTypeface == null) {
                        attributes.mFontFamily = appearance.getString(attr);
                    }
                    attributes.mFontFamilyExplicit = true;
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textStyle :
                    attributes.mTextStyle = appearance.getInt(attr, attributes.mTextStyle);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textFontWeight :
                    attributes.mFontWeight = appearance.getInt(attr, attributes.mFontWeight);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_textAllCaps :
                    attributes.mAllCaps = appearance.getBoolean(attr, attributes.mAllCaps);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_shadowColor :
                    attributes.mShadowColor = appearance.getInt(attr, attributes.mShadowColor);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_shadowDx :
                    attributes.mShadowDx = appearance.getFloat(attr, attributes.mShadowDx);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_shadowDy :
                    attributes.mShadowDy = appearance.getFloat(attr, attributes.mShadowDy);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_shadowRadius :
                    attributes.mShadowRadius = appearance.getFloat(attr, attributes.mShadowRadius);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_elegantTextHeight :
                    attributes.mHasElegant = true;
                    attributes.mElegant = appearance.getBoolean(attr, attributes.mElegant);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_fallbackLineSpacing :
                    attributes.mHasFallbackLineSpacing = true;
                    attributes.mFallbackLineSpacing = appearance.getBoolean(attr, attributes.mFallbackLineSpacing);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_letterSpacing :
                    attributes.mHasLetterSpacing = true;
                    attributes.mLetterSpacing = appearance.getFloat(attr, attributes.mLetterSpacing);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_fontFeatureSettings :
                    attributes.mFontFeatureSettings = appearance.getString(attr);
                    break;
                case android.widget.com.android.internal.R.styleable.TextAppearance_fontVariationSettings :
                    attributes.mFontVariationSettings = appearance.getString(attr);
                    break;
                default :
            }
        }
    }

    private void applyTextAppearance(TextView.TextAppearanceAttributes attributes) {
        if (attributes.mTextColor != null) {
            setTextColor(attributes.mTextColor);
        }
        if (attributes.mTextColorHint != null) {
            setHintTextColor(attributes.mTextColorHint);
        }
        if (attributes.mTextColorLink != null) {
            setLinkTextColor(attributes.mTextColorLink);
        }
        if (attributes.mTextColorHighlight != 0) {
            setHighlightColor(attributes.mTextColorHighlight);
        }
        if (attributes.mTextSize != (-1)) {
            /* shouldRequestLayout */
            setRawTextSize(attributes.mTextSize, true);
        }
        if (attributes.mTextLocales != null) {
            setTextLocales(attributes.mTextLocales);
        }
        if ((attributes.mTypefaceIndex != (-1)) && (!attributes.mFontFamilyExplicit)) {
            attributes.mFontFamily = null;
        }
        setTypefaceFromAttrs(attributes.mFontTypeface, attributes.mFontFamily, attributes.mTypefaceIndex, attributes.mTextStyle, attributes.mFontWeight);
        if (attributes.mShadowColor != 0) {
            setShadowLayer(attributes.mShadowRadius, attributes.mShadowDx, attributes.mShadowDy, attributes.mShadowColor);
        }
        if (attributes.mAllCaps) {
            setTransformationMethod(new android.text.method.AllCapsTransformationMethod(getContext()));
        }
        if (attributes.mHasElegant) {
            setElegantTextHeight(attributes.mElegant);
        }
        if (attributes.mHasFallbackLineSpacing) {
            setFallbackLineSpacing(attributes.mFallbackLineSpacing);
        }
        if (attributes.mHasLetterSpacing) {
            setLetterSpacing(attributes.mLetterSpacing);
        }
        if (attributes.mFontFeatureSettings != null) {
            setFontFeatureSettings(attributes.mFontFeatureSettings);
        }
        if (attributes.mFontVariationSettings != null) {
            setFontVariationSettings(attributes.mFontVariationSettings);
        }
    }

    /**
     * Get the default primary {@link Locale} of the text in this TextView. This will always be
     * the first member of {@link #getTextLocales()}.
     *
     * @return the default primary {@link Locale} of the text in this TextView.
     */
    @android.annotation.NonNull
    public java.util.Locale getTextLocale() {
        return mTextPaint.getTextLocale();
    }

    /**
     * Get the default {@link LocaleList} of the text in this TextView.
     *
     * @return the default {@link LocaleList} of the text in this TextView.
     */
    @android.annotation.NonNull
    @android.annotation.Size(min = 1)
    public android.os.LocaleList getTextLocales() {
        return mTextPaint.getTextLocales();
    }

    private void changeListenerLocaleTo(@android.annotation.Nullable
    java.util.Locale locale) {
        if (mListenerChanged) {
            // If a listener has been explicitly set, don't change it. We may break something.
            return;
        }
        // The following null check is not absolutely necessary since all calling points of
        // changeListenerLocaleTo() guarantee a non-null mEditor at the moment. But this is left
        // here in case others would want to call this method in the future.
        if (mEditor != null) {
            android.text.method.KeyListener listener = mEditor.mKeyListener;
            if (listener instanceof android.text.method.DigitsKeyListener) {
                listener = android.text.method.DigitsKeyListener.getInstance(locale, ((android.text.method.DigitsKeyListener) (listener)));
            } else
                if (listener instanceof android.text.method.DateKeyListener) {
                    listener = android.text.method.DateKeyListener.getInstance(locale);
                } else
                    if (listener instanceof android.text.method.TimeKeyListener) {
                        listener = android.text.method.TimeKeyListener.getInstance(locale);
                    } else
                        if (listener instanceof android.text.method.DateTimeKeyListener) {
                            listener = android.text.method.DateTimeKeyListener.getInstance(locale);
                        } else {
                            return;
                        }



            final boolean wasPasswordType = TextView.isPasswordInputType(mEditor.mInputType);
            setKeyListenerOnly(listener);
            setInputTypeFromEditor();
            if (wasPasswordType) {
                final int newInputClass = mEditor.mInputType & TYPE_MASK_CLASS;
                if (newInputClass == TYPE_CLASS_TEXT) {
                    mEditor.mInputType |= TYPE_TEXT_VARIATION_PASSWORD;
                } else
                    if (newInputClass == TYPE_CLASS_NUMBER) {
                        mEditor.mInputType |= TYPE_NUMBER_VARIATION_PASSWORD;
                    }

            }
        }
    }

    /**
     * Set the default {@link Locale} of the text in this TextView to a one-member
     * {@link LocaleList} containing just the given Locale.
     *
     * @param locale
     * 		the {@link Locale} for drawing text, must not be null.
     * @see #setTextLocales
     */
    public void setTextLocale(@android.annotation.NonNull
    java.util.Locale locale) {
        mLocalesChanged = true;
        mTextPaint.setTextLocale(locale);
        if (mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /**
     * Set the default {@link LocaleList} of the text in this TextView to the given value.
     *
     * This value is used to choose appropriate typefaces for ambiguous characters (typically used
     * for CJK locales to disambiguate Hanzi/Kanji/Hanja characters). It also affects
     * other aspects of text display, including line breaking.
     *
     * @param locales
     * 		the {@link LocaleList} for drawing text, must not be null or empty.
     * @see Paint#setTextLocales
     */
    public void setTextLocales(@android.annotation.NonNull
    @android.annotation.Size(min = 1)
    android.os.LocaleList locales) {
        mLocalesChanged = true;
        mTextPaint.setTextLocales(locales);
        if (mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    @java.lang.Override
    protected void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!mLocalesChanged) {
            mTextPaint.setTextLocales(android.os.LocaleList.getDefault());
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     *
     *
     * @return the size (in pixels) of the default text size in this TextView.
     */
    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.ExportedProperty(category = "text")
    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    /**
     *
     *
     * @return the size (in scaled pixels) of the default text size in this TextView.
     * @unknown 
     */
    @android.view.ViewDebug.ExportedProperty(category = "text")
    public float getScaledTextSize() {
        return mTextPaint.getTextSize() / mTextPaint.density;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.view.ViewDebug.ExportedProperty(category = "text", mapping = { @android.view.ViewDebug.IntToString(from = android.graphics.Typeface.NORMAL, to = "NORMAL"), @android.view.ViewDebug.IntToString(from = android.graphics.Typeface.BOLD, to = "BOLD"), @android.view.ViewDebug.IntToString(from = android.graphics.Typeface.ITALIC, to = "ITALIC"), @android.view.ViewDebug.IntToString(from = android.graphics.Typeface.BOLD_ITALIC, to = "BOLD_ITALIC") })
    public int getTypefaceStyle() {
        android.graphics.Typeface typeface = mTextPaint.getTypeface();
        return typeface != null ? typeface.getStyle() : android.graphics.Typeface.NORMAL;
    }

    /**
     * Set the default text size to the given value, interpreted as "scaled
     * pixel" units.  This size is adjusted based on the current density and
     * user font size preference.
     *
     * <p>Note: if this TextView has the auto-size feature enabled than this function is no-op.
     *
     * @param size
     * 		The scaled pixel size.
     * @unknown ref android.R.styleable#TextView_textSize
     */
    @android.view.RemotableViewMethod
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the default text size to a given unit and value. See {@link TypedValue} for the possible dimension units.
     *
     * <p>Note: if this TextView has the auto-size feature enabled than this function is no-op.
     *
     * @param unit
     * 		The desired dimension unit.
     * @param size
     * 		The desired size in the given units.
     * @unknown ref android.R.styleable#TextView_textSize
     */
    public void setTextSize(int unit, float size) {
        if (!isAutoSizeEnabled()) {
            /* shouldRequestLayout */
            setTextSizeInternal(unit, size, true);
        }
    }

    private void setTextSizeInternal(int unit, float size, boolean shouldRequestLayout) {
        android.content.Context c = getContext();
        android.content.res.Resources r;
        if (c == null) {
            r = android.content.res.Resources.getSystem();
        } else {
            r = c.getResources();
        }
        setRawTextSize(android.util.TypedValue.applyDimension(unit, size, r.getDisplayMetrics()), shouldRequestLayout);
    }

    @android.annotation.UnsupportedAppUsage
    private void setRawTextSize(float size, boolean shouldRequestLayout) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);
            if (shouldRequestLayout && (mLayout != null)) {
                // Do not auto-size right after setting the text size.
                mNeedsAutoSizeText = false;
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Gets the extent by which text should be stretched horizontally.
     * This will usually be 1.0.
     *
     * @return The horizontal scale factor.
     */
    @android.view.inspector.InspectableProperty
    public float getTextScaleX() {
        return mTextPaint.getTextScaleX();
    }

    /**
     * Sets the horizontal scale factor for text. The default value
     * is 1.0. Values greater than 1.0 stretch the text wider.
     * Values less than 1.0 make the text narrower. By default, this value is 1.0.
     *
     * @param size
     * 		The horizontal scale factor.
     * @unknown ref android.R.styleable#TextView_textScaleX
     */
    @android.view.RemotableViewMethod
    public void setTextScaleX(float size) {
        if (size != mTextPaint.getTextScaleX()) {
            mUserSetTextScaleX = true;
            mTextPaint.setTextScaleX(size);
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Sets the typeface and style in which the text should be displayed.
     * Note that not all Typeface families actually have bold and italic
     * variants, so you may need to use
     * {@link #setTypeface(Typeface, int)} to get the appearance
     * that you actually want.
     *
     * @see #getTypeface()
     * @unknown ref android.R.styleable#TextView_fontFamily
     * @unknown ref android.R.styleable#TextView_typeface
     * @unknown ref android.R.styleable#TextView_textStyle
     */
    public void setTypeface(@android.annotation.Nullable
    android.graphics.Typeface tf) {
        if (mTextPaint.getTypeface() != tf) {
            mTextPaint.setTypeface(tf);
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Gets the current {@link Typeface} that is used to style the text.
     *
     * @return The current Typeface.
     * @see #setTypeface(Typeface)
     * @unknown ref android.R.styleable#TextView_fontFamily
     * @unknown ref android.R.styleable#TextView_typeface
     * @unknown ref android.R.styleable#TextView_textStyle
     */
    @android.view.inspector.InspectableProperty
    public android.graphics.Typeface getTypeface() {
        return mTextPaint.getTypeface();
    }

    /**
     * Set the TextView's elegant height metrics flag. This setting selects font
     * variants that have not been compacted to fit Latin-based vertical
     * metrics, and also increases top and bottom bounds to provide more space.
     *
     * @param elegant
     * 		set the paint's elegant metrics flag.
     * @see #isElegantTextHeight()
     * @see Paint#isElegantTextHeight()
     * @unknown ref android.R.styleable#TextView_elegantTextHeight
     */
    public void setElegantTextHeight(boolean elegant) {
        if (elegant != mTextPaint.isElegantTextHeight()) {
            mTextPaint.setElegantTextHeight(elegant);
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Set whether to respect the ascent and descent of the fallback fonts that are used in
     * displaying the text (which is needed to avoid text from consecutive lines running into
     * each other). If set, fallback fonts that end up getting used can increase the ascent
     * and descent of the lines that they are used on.
     * <p/>
     * It is required to be true if text could be in languages like Burmese or Tibetan where text
     * is typically much taller or deeper than Latin text.
     *
     * @param enabled
     * 		whether to expand linespacing based on fallback fonts, {@code true} by default
     * @see StaticLayout.Builder#setUseLineSpacingFromFallbacks(boolean)
     * @unknown ref android.R.styleable#TextView_fallbackLineSpacing
     */
    public void setFallbackLineSpacing(boolean enabled) {
        if (mUseFallbackLineSpacing != enabled) {
            mUseFallbackLineSpacing = enabled;
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     *
     *
     * @return whether fallback line spacing is enabled, {@code true} by default
     * @see #setFallbackLineSpacing(boolean)
     * @unknown ref android.R.styleable#TextView_fallbackLineSpacing
     */
    @android.view.inspector.InspectableProperty
    public boolean isFallbackLineSpacing() {
        return mUseFallbackLineSpacing;
    }

    /**
     * Get the value of the TextView's elegant height metrics flag. This setting selects font
     * variants that have not been compacted to fit Latin-based vertical
     * metrics, and also increases top and bottom bounds to provide more space.
     *
     * @return {@code true} if the elegant height metrics flag is set.
     * @see #setElegantTextHeight(boolean)
     * @see Paint#setElegantTextHeight(boolean)
     */
    @android.view.inspector.InspectableProperty
    public boolean isElegantTextHeight() {
        return mTextPaint.isElegantTextHeight();
    }

    /**
     * Gets the text letter-space value, which determines the spacing between characters.
     * The value returned is in ems. Normally, this value is 0.0.
     *
     * @return The text letter-space value in ems.
     * @see #setLetterSpacing(float)
     * @see Paint#setLetterSpacing
     */
    @android.view.inspector.InspectableProperty
    public float getLetterSpacing() {
        return mTextPaint.getLetterSpacing();
    }

    /**
     * Sets text letter-spacing in em units.  Typical values
     * for slight expansion will be around 0.05.  Negative values tighten text.
     *
     * @see #getLetterSpacing()
     * @see Paint#getLetterSpacing
     * @param letterSpacing
     * 		A text letter-space value in ems.
     * @unknown ref android.R.styleable#TextView_letterSpacing
     */
    @android.view.RemotableViewMethod
    public void setLetterSpacing(float letterSpacing) {
        if (letterSpacing != mTextPaint.getLetterSpacing()) {
            mTextPaint.setLetterSpacing(letterSpacing);
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Returns the font feature settings. The format is the same as the CSS
     * font-feature-settings attribute:
     * <a href="https://www.w3.org/TR/css-fonts-3/#font-feature-settings-prop">
     *     https://www.w3.org/TR/css-fonts-3/#font-feature-settings-prop</a>
     *
     * @return the currently set font feature settings.  Default is null.
     * @see #setFontFeatureSettings(String)
     * @see Paint#setFontFeatureSettings(String) Paint.setFontFeatureSettings(String)
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.Nullable
    public java.lang.String getFontFeatureSettings() {
        return mTextPaint.getFontFeatureSettings();
    }

    /**
     * Returns the font variation settings.
     *
     * @return the currently set font variation settings.  Returns null if no variation is
    specified.
     * @see #setFontVariationSettings(String)
     * @see Paint#setFontVariationSettings(String) Paint.setFontVariationSettings(String)
     */
    @android.annotation.Nullable
    public java.lang.String getFontVariationSettings() {
        return mTextPaint.getFontVariationSettings();
    }

    /**
     * Sets the break strategy for breaking paragraphs into lines. The default value for
     * TextView is {@link Layout#BREAK_STRATEGY_HIGH_QUALITY}, and the default value for
     * EditText is {@link Layout#BREAK_STRATEGY_SIMPLE}, the latter to avoid the
     * text "dancing" when being edited.
     * <p/>
     * Enabling hyphenation with either using {@link Layout#HYPHENATION_FREQUENCY_NORMAL} or
     * {@link Layout#HYPHENATION_FREQUENCY_FULL} while line breaking is set to one of
     * {@link Layout#BREAK_STRATEGY_BALANCED}, {@link Layout#BREAK_STRATEGY_HIGH_QUALITY}
     * improves the structure of text layout however has performance impact and requires more time
     * to do the text layout.
     *
     * @unknown ref android.R.styleable#TextView_breakStrategy
     * @see #getBreakStrategy()
     * @see #setHyphenationFrequency(int)
     */
    public void setBreakStrategy(@android.text.Layout.BreakStrategy
    int breakStrategy) {
        mBreakStrategy = breakStrategy;
        if (mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /**
     * Gets the current strategy for breaking paragraphs into lines.
     *
     * @return the current strategy for breaking paragraphs into lines.
     * @unknown ref android.R.styleable#TextView_breakStrategy
     * @see #setBreakStrategy(int)
     */
    @android.view.inspector.InspectableProperty(enumMapping = { @android.view.inspector.InspectableProperty.EnumEntry(name = "simple", value = android.text.Layout.BREAK_STRATEGY_SIMPLE), @android.view.inspector.InspectableProperty.EnumEntry(name = "high_quality", value = android.text.Layout.BREAK_STRATEGY_HIGH_QUALITY), @android.view.inspector.InspectableProperty.EnumEntry(name = "balanced", value = android.text.Layout.BREAK_STRATEGY_BALANCED) })
    @android.text.Layout.BreakStrategy
    public int getBreakStrategy() {
        return mBreakStrategy;
    }

    /**
     * Sets the frequency of automatic hyphenation to use when determining word breaks.
     * The default value for both TextView and {@link EditText} is
     * {@link Layout#HYPHENATION_FREQUENCY_NONE}. Note that the default hyphenation frequency value
     * is set from the theme.
     * <p/>
     * Enabling hyphenation with either using {@link Layout#HYPHENATION_FREQUENCY_NORMAL} or
     * {@link Layout#HYPHENATION_FREQUENCY_FULL} while line breaking is set to one of
     * {@link Layout#BREAK_STRATEGY_BALANCED}, {@link Layout#BREAK_STRATEGY_HIGH_QUALITY}
     * improves the structure of text layout however has performance impact and requires more time
     * to do the text layout.
     * <p/>
     * Note: Before Android Q, in the theme hyphenation frequency is set to
     * {@link Layout#HYPHENATION_FREQUENCY_NORMAL}. The default value is changed into
     * {@link Layout#HYPHENATION_FREQUENCY_NONE} on Q.
     *
     * @param hyphenationFrequency
     * 		the hyphenation frequency to use, one of
     * 		{@link Layout#HYPHENATION_FREQUENCY_NONE},
     * 		{@link Layout#HYPHENATION_FREQUENCY_NORMAL},
     * 		{@link Layout#HYPHENATION_FREQUENCY_FULL}
     * @unknown ref android.R.styleable#TextView_hyphenationFrequency
     * @see #getHyphenationFrequency()
     * @see #getBreakStrategy()
     */
    public void setHyphenationFrequency(@android.text.Layout.HyphenationFrequency
    int hyphenationFrequency) {
        mHyphenationFrequency = hyphenationFrequency;
        if (mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /**
     * Gets the current frequency of automatic hyphenation to be used when determining word breaks.
     *
     * @return the current frequency of automatic hyphenation to be used when determining word
    breaks.
     * @unknown ref android.R.styleable#TextView_hyphenationFrequency
     * @see #setHyphenationFrequency(int)
     */
    @android.view.inspector.InspectableProperty(enumMapping = { @android.view.inspector.InspectableProperty.EnumEntry(name = "none", value = android.text.Layout.HYPHENATION_FREQUENCY_NONE), @android.view.inspector.InspectableProperty.EnumEntry(name = "normal", value = android.text.Layout.HYPHENATION_FREQUENCY_NORMAL), @android.view.inspector.InspectableProperty.EnumEntry(name = "full", value = android.text.Layout.HYPHENATION_FREQUENCY_FULL) })
    @android.text.Layout.HyphenationFrequency
    public int getHyphenationFrequency() {
        return mHyphenationFrequency;
    }

    /**
     * Gets the parameters for text layout precomputation, for use with {@link PrecomputedText}.
     *
     * @return a current {@link PrecomputedText.Params}
     * @see PrecomputedText
     */
    @android.annotation.NonNull
    public PrecomputedText.Params getTextMetricsParams() {
        return new android.text.PrecomputedText.Params(new android.text.TextPaint(mTextPaint), getTextDirectionHeuristic(), mBreakStrategy, mHyphenationFrequency);
    }

    /**
     * Apply the text layout parameter.
     *
     * Update the TextView parameters to be compatible with {@link PrecomputedText.Params}.
     *
     * @see PrecomputedText
     */
    public void setTextMetricsParams(@android.annotation.NonNull
    android.text.PrecomputedText.Params params) {
        mTextPaint.set(params.getTextPaint());
        mUserSetTextScaleX = true;
        mTextDir = params.getTextDirection();
        mBreakStrategy = params.getBreakStrategy();
        mHyphenationFrequency = params.getHyphenationFrequency();
        if (mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /**
     * Set justification mode. The default value is {@link Layout#JUSTIFICATION_MODE_NONE}. If the
     * last line is too short for justification, the last line will be displayed with the
     * alignment set by {@link android.view.View#setTextAlignment}.
     *
     * @see #getJustificationMode()
     */
    @android.text.Layout.JustificationMode
    public void setJustificationMode(@android.text.Layout.JustificationMode
    int justificationMode) {
        mJustificationMode = justificationMode;
        if (mLayout != null) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    /**
     *
     *
     * @return true if currently paragraph justification mode.
     * @see #setJustificationMode(int)
     */
    @android.view.inspector.InspectableProperty(enumMapping = { @android.view.inspector.InspectableProperty.EnumEntry(name = "none", value = android.text.Layout.JUSTIFICATION_MODE_NONE), @android.view.inspector.InspectableProperty.EnumEntry(name = "inter_word", value = android.text.Layout.JUSTIFICATION_MODE_INTER_WORD) })
    @android.text.Layout.JustificationMode
    public int getJustificationMode() {
        return mJustificationMode;
    }

    /**
     * Sets font feature settings. The format is the same as the CSS
     * font-feature-settings attribute:
     * <a href="https://www.w3.org/TR/css-fonts-3/#font-feature-settings-prop">
     *     https://www.w3.org/TR/css-fonts-3/#font-feature-settings-prop</a>
     *
     * @param fontFeatureSettings
     * 		font feature settings represented as CSS compatible string
     * @see #getFontFeatureSettings()
     * @see Paint#getFontFeatureSettings() Paint.getFontFeatureSettings()
     * @unknown ref android.R.styleable#TextView_fontFeatureSettings
     */
    @android.view.RemotableViewMethod
    public void setFontFeatureSettings(@android.annotation.Nullable
    java.lang.String fontFeatureSettings) {
        if (fontFeatureSettings != mTextPaint.getFontFeatureSettings()) {
            mTextPaint.setFontFeatureSettings(fontFeatureSettings);
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Sets TrueType or OpenType font variation settings. The settings string is constructed from
     * multiple pairs of axis tag and style values. The axis tag must contain four ASCII characters
     * and must be wrapped with single quotes (U+0027) or double quotes (U+0022). Axis strings that
     * are longer or shorter than four characters, or contain characters outside of U+0020..U+007E
     * are invalid. If a specified axis name is not defined in the font, the settings will be
     * ignored.
     *
     * <p>
     * Examples,
     * <ul>
     * <li>Set font width to 150.
     * <pre>
     * <code>
     *   TextView textView = (TextView) findViewById(R.id.textView);
     *   textView.setFontVariationSettings("'wdth' 150");
     * </code>
     * </pre>
     * </li>
     *
     * <li>Set the font slant to 20 degrees and ask for italic style.
     * <pre>
     * <code>
     *   TextView textView = (TextView) findViewById(R.id.textView);
     *   textView.setFontVariationSettings("'slnt' 20, 'ital' 1");
     * </code>
     * </pre>
     * </p>
     * </li>
     * </ul>
     *
     * @param fontVariationSettings
     * 		font variation settings. You can pass null or empty string as
     * 		no variation settings.
     * @return true if the given settings is effective to at least one font file underlying this
    TextView. This function also returns true for empty settings string. Otherwise
    returns false.
     * @throws IllegalArgumentException
     * 		If given string is not a valid font variation settings
     * 		format.
     * @see #getFontVariationSettings()
     * @see FontVariationAxis
     * @unknown ref android.R.styleable#TextView_fontVariationSettings
     */
    public boolean setFontVariationSettings(@android.annotation.Nullable
    java.lang.String fontVariationSettings) {
        final java.lang.String existingSettings = mTextPaint.getFontVariationSettings();
        if ((fontVariationSettings == existingSettings) || ((fontVariationSettings != null) && fontVariationSettings.equals(existingSettings))) {
            return true;
        }
        boolean effective = mTextPaint.setFontVariationSettings(fontVariationSettings);
        if (effective && (mLayout != null)) {
            nullLayouts();
            requestLayout();
            invalidate();
        }
        return effective;
    }

    /**
     * Sets the text color for all the states (normal, selected,
     * focused) to be this color.
     *
     * @param color
     * 		A color value in the form 0xAARRGGBB.
     * 		Do not pass a resource ID. To get a color value from a resource ID, call
     * 		{@link android.support.v4.content.ContextCompat#getColor(Context, int) getColor}.
     * @see #setTextColor(ColorStateList)
     * @see #getTextColors()
     * @unknown ref android.R.styleable#TextView_textColor
     */
    @android.view.RemotableViewMethod
    public void setTextColor(@android.annotation.ColorInt
    int color) {
        mTextColor = android.content.res.ColorStateList.valueOf(color);
        updateTextColors();
    }

    /**
     * Sets the text color.
     *
     * @see #setTextColor(int)
     * @see #getTextColors()
     * @see #setHintTextColor(ColorStateList)
     * @see #setLinkTextColor(ColorStateList)
     * @unknown ref android.R.styleable#TextView_textColor
     */
    @android.view.RemotableViewMethod
    public void setTextColor(android.content.res.ColorStateList colors) {
        if (colors == null) {
            throw new java.lang.NullPointerException();
        }
        mTextColor = colors;
        updateTextColors();
    }

    /**
     * Gets the text colors for the different states (normal, selected, focused) of the TextView.
     *
     * @see #setTextColor(ColorStateList)
     * @see #setTextColor(int)
     * @unknown ref android.R.styleable#TextView_textColor
     */
    @android.view.inspector.InspectableProperty(name = "textColor")
    public final android.content.res.ColorStateList getTextColors() {
        return mTextColor;
    }

    /**
     * Return the current color selected for normal text.
     *
     * @return Returns the current text color.
     */
    @android.annotation.ColorInt
    public final int getCurrentTextColor() {
        return mCurTextColor;
    }

    /**
     * Sets the color used to display the selection highlight.
     *
     * @unknown ref android.R.styleable#TextView_textColorHighlight
     */
    @android.view.RemotableViewMethod
    public void setHighlightColor(@android.annotation.ColorInt
    int color) {
        if (mHighlightColor != color) {
            mHighlightColor = color;
            invalidate();
        }
    }

    /**
     *
     *
     * @return the color used to display the selection highlight
     * @see #setHighlightColor(int)
     * @unknown ref android.R.styleable#TextView_textColorHighlight
     */
    @android.view.inspector.InspectableProperty(name = "textColorHighlight")
    @android.annotation.ColorInt
    public int getHighlightColor() {
        return mHighlightColor;
    }

    /**
     * Sets whether the soft input method will be made visible when this
     * TextView gets focused. The default is true.
     */
    @android.view.RemotableViewMethod
    public final void setShowSoftInputOnFocus(boolean show) {
        createEditorIfNeeded();
        mEditor.mShowSoftInputOnFocus = show;
    }

    /**
     * Returns whether the soft input method will be made visible when this
     * TextView gets focused. The default is true.
     */
    public final boolean getShowSoftInputOnFocus() {
        // When there is no Editor, return default true value
        return (mEditor == null) || mEditor.mShowSoftInputOnFocus;
    }

    /**
     * Gives the text a shadow of the specified blur radius and color, the specified
     * distance from its drawn position.
     * <p>
     * The text shadow produced does not interact with the properties on view
     * that are responsible for real time shadows,
     * {@link View#getElevation() elevation} and
     * {@link View#getTranslationZ() translationZ}.
     *
     * @see Paint#setShadowLayer(float, float, float, int)
     * @unknown ref android.R.styleable#TextView_shadowColor
     * @unknown ref android.R.styleable#TextView_shadowDx
     * @unknown ref android.R.styleable#TextView_shadowDy
     * @unknown ref android.R.styleable#TextView_shadowRadius
     */
    public void setShadowLayer(float radius, float dx, float dy, int color) {
        mTextPaint.setShadowLayer(radius, dx, dy, color);
        mShadowRadius = radius;
        mShadowDx = dx;
        mShadowDy = dy;
        mShadowColor = color;
        // Will change text clip region
        if (mEditor != null) {
            mEditor.invalidateTextDisplayList();
            mEditor.invalidateHandlesAndActionMode();
        }
        invalidate();
    }

    /**
     * Gets the radius of the shadow layer.
     *
     * @return the radius of the shadow layer. If 0, the shadow layer is not visible
     * @see #setShadowLayer(float, float, float, int)
     * @unknown ref android.R.styleable#TextView_shadowRadius
     */
    @android.view.inspector.InspectableProperty
    public float getShadowRadius() {
        return mShadowRadius;
    }

    /**
     *
     *
     * @return the horizontal offset of the shadow layer
     * @see #setShadowLayer(float, float, float, int)
     * @unknown ref android.R.styleable#TextView_shadowDx
     */
    @android.view.inspector.InspectableProperty
    public float getShadowDx() {
        return mShadowDx;
    }

    /**
     * Gets the vertical offset of the shadow layer.
     *
     * @return The vertical offset of the shadow layer.
     * @see #setShadowLayer(float, float, float, int)
     * @unknown ref android.R.styleable#TextView_shadowDy
     */
    @android.view.inspector.InspectableProperty
    public float getShadowDy() {
        return mShadowDy;
    }

    /**
     * Gets the color of the shadow layer.
     *
     * @return the color of the shadow layer
     * @see #setShadowLayer(float, float, float, int)
     * @unknown ref android.R.styleable#TextView_shadowColor
     */
    @android.view.inspector.InspectableProperty
    @android.annotation.ColorInt
    public int getShadowColor() {
        return mShadowColor;
    }

    /**
     * Gets the {@link TextPaint} used for the text.
     * Use this only to consult the Paint's properties and not to change them.
     *
     * @return The base paint used for the text.
     */
    public android.text.TextPaint getPaint() {
        return mTextPaint;
    }

    /**
     * Sets the autolink mask of the text.  See {@link android.text.util.Linkify#ALL Linkify.ALL} and peers for
     * possible values.
     *
     * <p class="note"><b>Note:</b>
     * {@link android.text.util.Linkify#MAP_ADDRESSES Linkify.MAP_ADDRESSES}
     * is deprecated and should be avoided; see its documentation.
     *
     * @unknown ref android.R.styleable#TextView_autoLink
     */
    @android.view.RemotableViewMethod
    public final void setAutoLinkMask(int mask) {
        mAutoLinkMask = mask;
    }

    /**
     * Sets whether the movement method will automatically be set to
     * {@link LinkMovementMethod} if {@link #setAutoLinkMask} has been
     * set to nonzero and links are detected in {@link #setText}.
     * The default is true.
     *
     * @unknown ref android.R.styleable#TextView_linksClickable
     */
    @android.view.RemotableViewMethod
    public final void setLinksClickable(boolean whether) {
        mLinksClickable = whether;
    }

    /**
     * Returns whether the movement method will automatically be set to
     * {@link LinkMovementMethod} if {@link #setAutoLinkMask} has been
     * set to nonzero and links are detected in {@link #setText}.
     * The default is true.
     *
     * @unknown ref android.R.styleable#TextView_linksClickable
     */
    @android.view.inspector.InspectableProperty
    public final boolean getLinksClickable() {
        return mLinksClickable;
    }

    /**
     * Returns the list of {@link android.text.style.URLSpan URLSpans} attached to the text
     * (by {@link Linkify} or otherwise) if any.  You can call
     * {@link URLSpan#getURL} on them to find where they link to
     * or use {@link Spanned#getSpanStart} and {@link Spanned#getSpanEnd}
     * to find the region of the text they are attached to.
     */
    public android.text.style.URLSpan[] getUrls() {
        if (mText instanceof android.text.Spanned) {
            return ((android.text.Spanned) (mText)).getSpans(0, mText.length(), android.text.style.URLSpan.class);
        } else {
            return new android.text.style.URLSpan[0];
        }
    }

    /**
     * Sets the color of the hint text for all the states (disabled, focussed, selected...) of this
     * TextView.
     *
     * @see #setHintTextColor(ColorStateList)
     * @see #getHintTextColors()
     * @see #setTextColor(int)
     * @unknown ref android.R.styleable#TextView_textColorHint
     */
    @android.view.RemotableViewMethod
    public final void setHintTextColor(@android.annotation.ColorInt
    int color) {
        mHintTextColor = android.content.res.ColorStateList.valueOf(color);
        updateTextColors();
    }

    /**
     * Sets the color of the hint text.
     *
     * @see #getHintTextColors()
     * @see #setHintTextColor(int)
     * @see #setTextColor(ColorStateList)
     * @see #setLinkTextColor(ColorStateList)
     * @unknown ref android.R.styleable#TextView_textColorHint
     */
    public final void setHintTextColor(android.content.res.ColorStateList colors) {
        mHintTextColor = colors;
        updateTextColors();
    }

    /**
     *
     *
     * @return the color of the hint text, for the different states of this TextView.
     * @see #setHintTextColor(ColorStateList)
     * @see #setHintTextColor(int)
     * @see #setTextColor(ColorStateList)
     * @see #setLinkTextColor(ColorStateList)
     * @unknown ref android.R.styleable#TextView_textColorHint
     */
    @android.view.inspector.InspectableProperty(name = "textColorHint")
    public final android.content.res.ColorStateList getHintTextColors() {
        return mHintTextColor;
    }

    /**
     * <p>Return the current color selected to paint the hint text.</p>
     *
     * @return Returns the current hint text color.
     */
    @android.annotation.ColorInt
    public final int getCurrentHintTextColor() {
        return mHintTextColor != null ? mCurHintTextColor : mCurTextColor;
    }

    /**
     * Sets the color of links in the text.
     *
     * @see #setLinkTextColor(ColorStateList)
     * @see #getLinkTextColors()
     * @unknown ref android.R.styleable#TextView_textColorLink
     */
    @android.view.RemotableViewMethod
    public final void setLinkTextColor(@android.annotation.ColorInt
    int color) {
        mLinkTextColor = android.content.res.ColorStateList.valueOf(color);
        updateTextColors();
    }

    /**
     * Sets the color of links in the text.
     *
     * @see #setLinkTextColor(int)
     * @see #getLinkTextColors()
     * @see #setTextColor(ColorStateList)
     * @see #setHintTextColor(ColorStateList)
     * @unknown ref android.R.styleable#TextView_textColorLink
     */
    public final void setLinkTextColor(android.content.res.ColorStateList colors) {
        mLinkTextColor = colors;
        updateTextColors();
    }

    /**
     *
     *
     * @return the list of colors used to paint the links in the text, for the different states of
    this TextView
     * @see #setLinkTextColor(ColorStateList)
     * @see #setLinkTextColor(int)
     * @unknown ref android.R.styleable#TextView_textColorLink
     */
    @android.view.inspector.InspectableProperty(name = "textColorLink")
    public final android.content.res.ColorStateList getLinkTextColors() {
        return mLinkTextColor;
    }

    /**
     * Sets the horizontal alignment of the text and the
     * vertical gravity that will be used when there is extra space
     * in the TextView beyond what is required for the text itself.
     *
     * @see android.view.Gravity
     * @unknown ref android.R.styleable#TextView_gravity
     */
    public void setGravity(int gravity) {
        if ((gravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) == 0) {
            gravity |= android.view.Gravity.START;
        }
        if ((gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == 0) {
            gravity |= android.view.Gravity.TOP;
        }
        boolean newLayout = false;
        if ((gravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) != (mGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK)) {
            newLayout = true;
        }
        if (gravity != mGravity) {
            invalidate();
        }
        mGravity = gravity;
        if ((mLayout != null) && newLayout) {
            // XXX this is heavy-handed because no actual content changes.
            int want = mLayout.getWidth();
            int hintWant = (mHintLayout == null) ? 0 : mHintLayout.getWidth();
            makeNewLayout(want, hintWant, TextView.UNKNOWN_BORING, TextView.UNKNOWN_BORING, ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), true);
        }
    }

    /**
     * Returns the horizontal and vertical alignment of this TextView.
     *
     * @see android.view.Gravity
     * @unknown ref android.R.styleable#TextView_gravity
     */
    @android.view.inspector.InspectableProperty(valueType = android.view.inspector.InspectableProperty.ValueType.GRAVITY)
    public int getGravity() {
        return mGravity;
    }

    /**
     * Gets the flags on the Paint being used to display the text.
     *
     * @return The flags on the Paint being used to display the text.
     * @see Paint#getFlags
     */
    public int getPaintFlags() {
        return mTextPaint.getFlags();
    }

    /**
     * Sets flags on the Paint being used to display the text and
     * reflows the text if they are different from the old flags.
     *
     * @see Paint#setFlags
     */
    @android.view.RemotableViewMethod
    public void setPaintFlags(int flags) {
        if (mTextPaint.getFlags() != flags) {
            mTextPaint.setFlags(flags);
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Sets whether the text should be allowed to be wider than the
     * View is.  If false, it will be wrapped to the width of the View.
     *
     * @unknown ref android.R.styleable#TextView_scrollHorizontally
     */
    public void setHorizontallyScrolling(boolean whether) {
        if (mHorizontallyScrolling != whether) {
            mHorizontallyScrolling = whether;
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Returns whether the text is allowed to be wider than the View.
     * If false, the text will be wrapped to the width of the View.
     *
     * @unknown ref android.R.styleable#TextView_scrollHorizontally
     * @see #setHorizontallyScrolling(boolean)
     */
    @android.view.inspector.InspectableProperty(name = "scrollHorizontally")
    public final boolean isHorizontallyScrollable() {
        return mHorizontallyScrolling;
    }

    /**
     * Returns whether the text is allowed to be wider than the View.
     * If false, the text will be wrapped to the width of the View.
     *
     * @unknown ref android.R.styleable#TextView_scrollHorizontally
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P)
    public boolean getHorizontallyScrolling() {
        return mHorizontallyScrolling;
    }

    /**
     * Sets the height of the TextView to be at least {@code minLines} tall.
     * <p>
     * This value is used for height calculation if LayoutParams does not force TextView to have an
     * exact height. Setting this value overrides other previous minimum height configurations such
     * as {@link #setMinHeight(int)} or {@link #setHeight(int)}. {@link #setSingleLine()} will set
     * this value to 1.
     *
     * @param minLines
     * 		the minimum height of TextView in terms of number of lines
     * @see #getMinLines()
     * @see #setLines(int)
     * @unknown ref android.R.styleable#TextView_minLines
     */
    @android.view.RemotableViewMethod
    public void setMinLines(int minLines) {
        mMinimum = minLines;
        mMinMode = LINES;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the minimum height of TextView in terms of number of lines or -1 if the minimum
     * height was set using {@link #setMinHeight(int)} or {@link #setHeight(int)}.
     *
     * @return the minimum height of TextView in terms of number of lines or -1 if the minimum
    height is not defined in lines
     * @see #setMinLines(int)
     * @see #setLines(int)
     * @unknown ref android.R.styleable#TextView_minLines
     */
    @android.view.inspector.InspectableProperty
    public int getMinLines() {
        return mMinMode == LINES ? mMinimum : -1;
    }

    /**
     * Sets the height of the TextView to be at least {@code minPixels} tall.
     * <p>
     * This value is used for height calculation if LayoutParams does not force TextView to have an
     * exact height. Setting this value overrides previous minimum height configurations such as
     * {@link #setMinLines(int)} or {@link #setLines(int)}.
     * <p>
     * The value given here is different than {@link #setMinimumHeight(int)}. Between
     * {@code minHeight} and the value set in {@link #setMinimumHeight(int)}, the greater one is
     * used to decide the final height.
     *
     * @param minPixels
     * 		the minimum height of TextView in terms of pixels
     * @see #getMinHeight()
     * @see #setHeight(int)
     * @unknown ref android.R.styleable#TextView_minHeight
     */
    @android.view.RemotableViewMethod
    public void setMinHeight(int minPixels) {
        mMinimum = minPixels;
        mMinMode = TextView.PIXELS;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the minimum height of TextView in terms of pixels or -1 if the minimum height was
     * set using {@link #setMinLines(int)} or {@link #setLines(int)}.
     *
     * @return the minimum height of TextView in terms of pixels or -1 if the minimum height is not
    defined in pixels
     * @see #setMinHeight(int)
     * @see #setHeight(int)
     * @unknown ref android.R.styleable#TextView_minHeight
     */
    public int getMinHeight() {
        return mMinMode == TextView.PIXELS ? mMinimum : -1;
    }

    /**
     * Sets the height of the TextView to be at most {@code maxLines} tall.
     * <p>
     * This value is used for height calculation if LayoutParams does not force TextView to have an
     * exact height. Setting this value overrides previous maximum height configurations such as
     * {@link #setMaxHeight(int)} or {@link #setLines(int)}.
     *
     * @param maxLines
     * 		the maximum height of TextView in terms of number of lines
     * @see #getMaxLines()
     * @see #setLines(int)
     * @unknown ref android.R.styleable#TextView_maxLines
     */
    @android.view.RemotableViewMethod
    public void setMaxLines(int maxLines) {
        mMaximum = maxLines;
        mMaxMode = LINES;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the maximum height of TextView in terms of number of lines or -1 if the
     * maximum height was set using {@link #setMaxHeight(int)} or {@link #setHeight(int)}.
     *
     * @return the maximum height of TextView in terms of number of lines. -1 if the maximum height
    is not defined in lines.
     * @see #setMaxLines(int)
     * @see #setLines(int)
     * @unknown ref android.R.styleable#TextView_maxLines
     */
    @android.view.inspector.InspectableProperty
    public int getMaxLines() {
        return mMaxMode == LINES ? mMaximum : -1;
    }

    /**
     * Sets the height of the TextView to be at most {@code maxPixels} tall.
     * <p>
     * This value is used for height calculation if LayoutParams does not force TextView to have an
     * exact height. Setting this value overrides previous maximum height configurations such as
     * {@link #setMaxLines(int)} or {@link #setLines(int)}.
     *
     * @param maxPixels
     * 		the maximum height of TextView in terms of pixels
     * @see #getMaxHeight()
     * @see #setHeight(int)
     * @unknown ref android.R.styleable#TextView_maxHeight
     */
    @android.view.RemotableViewMethod
    public void setMaxHeight(int maxPixels) {
        mMaximum = maxPixels;
        mMaxMode = TextView.PIXELS;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the maximum height of TextView in terms of pixels or -1 if the maximum height was
     * set using {@link #setMaxLines(int)} or {@link #setLines(int)}.
     *
     * @return the maximum height of TextView in terms of pixels or -1 if the maximum height
    is not defined in pixels
     * @see #setMaxHeight(int)
     * @see #setHeight(int)
     * @unknown ref android.R.styleable#TextView_maxHeight
     */
    @android.view.inspector.InspectableProperty
    public int getMaxHeight() {
        return mMaxMode == TextView.PIXELS ? mMaximum : -1;
    }

    /**
     * Sets the height of the TextView to be exactly {@code lines} tall.
     * <p>
     * This value is used for height calculation if LayoutParams does not force TextView to have an
     * exact height. Setting this value overrides previous minimum/maximum height configurations
     * such as {@link #setMinLines(int)} or {@link #setMaxLines(int)}. {@link #setSingleLine()} will
     * set this value to 1.
     *
     * @param lines
     * 		the exact height of the TextView in terms of lines
     * @see #setHeight(int)
     * @unknown ref android.R.styleable#TextView_lines
     */
    @android.view.RemotableViewMethod
    public void setLines(int lines) {
        mMaximum = mMinimum = lines;
        mMaxMode = mMinMode = LINES;
        requestLayout();
        invalidate();
    }

    /**
     * Sets the height of the TextView to be exactly <code>pixels</code> tall.
     * <p>
     * This value is used for height calculation if LayoutParams does not force TextView to have an
     * exact height. Setting this value overrides previous minimum/maximum height configurations
     * such as {@link #setMinHeight(int)} or {@link #setMaxHeight(int)}.
     *
     * @param pixels
     * 		the exact height of the TextView in terms of pixels
     * @see #setLines(int)
     * @unknown ref android.R.styleable#TextView_height
     */
    @android.view.RemotableViewMethod
    public void setHeight(int pixels) {
        mMaximum = mMinimum = pixels;
        mMaxMode = mMinMode = TextView.PIXELS;
        requestLayout();
        invalidate();
    }

    /**
     * Sets the width of the TextView to be at least {@code minEms} wide.
     * <p>
     * This value is used for width calculation if LayoutParams does not force TextView to have an
     * exact width. Setting this value overrides previous minimum width configurations such as
     * {@link #setMinWidth(int)} or {@link #setWidth(int)}.
     *
     * @param minEms
     * 		the minimum width of TextView in terms of ems
     * @see #getMinEms()
     * @see #setEms(int)
     * @unknown ref android.R.styleable#TextView_minEms
     */
    @android.view.RemotableViewMethod
    public void setMinEms(int minEms) {
        mMinWidth = minEms;
        mMinWidthMode = TextView.EMS;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the minimum width of TextView in terms of ems or -1 if the minimum width was set
     * using {@link #setMinWidth(int)} or {@link #setWidth(int)}.
     *
     * @return the minimum width of TextView in terms of ems. -1 if the minimum width is not
    defined in ems
     * @see #setMinEms(int)
     * @see #setEms(int)
     * @unknown ref android.R.styleable#TextView_minEms
     */
    @android.view.inspector.InspectableProperty
    public int getMinEms() {
        return mMinWidthMode == TextView.EMS ? mMinWidth : -1;
    }

    /**
     * Sets the width of the TextView to be at least {@code minPixels} wide.
     * <p>
     * This value is used for width calculation if LayoutParams does not force TextView to have an
     * exact width. Setting this value overrides previous minimum width configurations such as
     * {@link #setMinEms(int)} or {@link #setEms(int)}.
     * <p>
     * The value given here is different than {@link #setMinimumWidth(int)}. Between
     * {@code minWidth} and the value set in {@link #setMinimumWidth(int)}, the greater one is used
     * to decide the final width.
     *
     * @param minPixels
     * 		the minimum width of TextView in terms of pixels
     * @see #getMinWidth()
     * @see #setWidth(int)
     * @unknown ref android.R.styleable#TextView_minWidth
     */
    @android.view.RemotableViewMethod
    public void setMinWidth(int minPixels) {
        mMinWidth = minPixels;
        mMinWidthMode = TextView.PIXELS;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the minimum width of TextView in terms of pixels or -1 if the minimum width was set
     * using {@link #setMinEms(int)} or {@link #setEms(int)}.
     *
     * @return the minimum width of TextView in terms of pixels or -1 if the minimum width is not
    defined in pixels
     * @see #setMinWidth(int)
     * @see #setWidth(int)
     * @unknown ref android.R.styleable#TextView_minWidth
     */
    @android.view.inspector.InspectableProperty
    public int getMinWidth() {
        return mMinWidthMode == TextView.PIXELS ? mMinWidth : -1;
    }

    /**
     * Sets the width of the TextView to be at most {@code maxEms} wide.
     * <p>
     * This value is used for width calculation if LayoutParams does not force TextView to have an
     * exact width. Setting this value overrides previous maximum width configurations such as
     * {@link #setMaxWidth(int)} or {@link #setWidth(int)}.
     *
     * @param maxEms
     * 		the maximum width of TextView in terms of ems
     * @see #getMaxEms()
     * @see #setEms(int)
     * @unknown ref android.R.styleable#TextView_maxEms
     */
    @android.view.RemotableViewMethod
    public void setMaxEms(int maxEms) {
        mMaxWidth = maxEms;
        mMaxWidthMode = TextView.EMS;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the maximum width of TextView in terms of ems or -1 if the maximum width was set
     * using {@link #setMaxWidth(int)} or {@link #setWidth(int)}.
     *
     * @return the maximum width of TextView in terms of ems or -1 if the maximum width is not
    defined in ems
     * @see #setMaxEms(int)
     * @see #setEms(int)
     * @unknown ref android.R.styleable#TextView_maxEms
     */
    @android.view.inspector.InspectableProperty
    public int getMaxEms() {
        return mMaxWidthMode == TextView.EMS ? mMaxWidth : -1;
    }

    /**
     * Sets the width of the TextView to be at most {@code maxPixels} wide.
     * <p>
     * This value is used for width calculation if LayoutParams does not force TextView to have an
     * exact width. Setting this value overrides previous maximum width configurations such as
     * {@link #setMaxEms(int)} or {@link #setEms(int)}.
     *
     * @param maxPixels
     * 		the maximum width of TextView in terms of pixels
     * @see #getMaxWidth()
     * @see #setWidth(int)
     * @unknown ref android.R.styleable#TextView_maxWidth
     */
    @android.view.RemotableViewMethod
    public void setMaxWidth(int maxPixels) {
        mMaxWidth = maxPixels;
        mMaxWidthMode = TextView.PIXELS;
        requestLayout();
        invalidate();
    }

    /**
     * Returns the maximum width of TextView in terms of pixels or -1 if the maximum width was set
     * using {@link #setMaxEms(int)} or {@link #setEms(int)}.
     *
     * @return the maximum width of TextView in terms of pixels. -1 if the maximum width is not
    defined in pixels
     * @see #setMaxWidth(int)
     * @see #setWidth(int)
     * @unknown ref android.R.styleable#TextView_maxWidth
     */
    @android.view.inspector.InspectableProperty
    public int getMaxWidth() {
        return mMaxWidthMode == TextView.PIXELS ? mMaxWidth : -1;
    }

    /**
     * Sets the width of the TextView to be exactly {@code ems} wide.
     *
     * This value is used for width calculation if LayoutParams does not force TextView to have an
     * exact width. Setting this value overrides previous minimum/maximum configurations such as
     * {@link #setMinEms(int)} or {@link #setMaxEms(int)}.
     *
     * @param ems
     * 		the exact width of the TextView in terms of ems
     * @see #setWidth(int)
     * @unknown ref android.R.styleable#TextView_ems
     */
    @android.view.RemotableViewMethod
    public void setEms(int ems) {
        mMaxWidth = mMinWidth = ems;
        mMaxWidthMode = mMinWidthMode = TextView.EMS;
        requestLayout();
        invalidate();
    }

    /**
     * Sets the width of the TextView to be exactly {@code pixels} wide.
     * <p>
     * This value is used for width calculation if LayoutParams does not force TextView to have an
     * exact width. Setting this value overrides previous minimum/maximum width configurations
     * such as {@link #setMinWidth(int)} or {@link #setMaxWidth(int)}.
     *
     * @param pixels
     * 		the exact width of the TextView in terms of pixels
     * @see #setEms(int)
     * @unknown ref android.R.styleable#TextView_width
     */
    @android.view.RemotableViewMethod
    public void setWidth(int pixels) {
        mMaxWidth = mMinWidth = pixels;
        mMaxWidthMode = mMinWidthMode = TextView.PIXELS;
        requestLayout();
        invalidate();
    }

    /**
     * Sets line spacing for this TextView.  Each line other than the last line will have its height
     * multiplied by {@code mult} and have {@code add} added to it.
     *
     * @param add
     * 		The value in pixels that should be added to each line other than the last line.
     * 		This will be applied after the multiplier
     * @param mult
     * 		The value by which each line height other than the last line will be multiplied
     * 		by
     * @unknown ref android.R.styleable#TextView_lineSpacingExtra
     * @unknown ref android.R.styleable#TextView_lineSpacingMultiplier
     */
    public void setLineSpacing(float add, float mult) {
        if ((mSpacingAdd != add) || (mSpacingMult != mult)) {
            mSpacingAdd = add;
            mSpacingMult = mult;
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Gets the line spacing multiplier
     *
     * @return the value by which each line's height is multiplied to get its actual height.
     * @see #setLineSpacing(float, float)
     * @see #getLineSpacingExtra()
     * @unknown ref android.R.styleable#TextView_lineSpacingMultiplier
     */
    @android.view.inspector.InspectableProperty
    public float getLineSpacingMultiplier() {
        return mSpacingMult;
    }

    /**
     * Gets the line spacing extra space
     *
     * @return the extra space that is added to the height of each lines of this TextView.
     * @see #setLineSpacing(float, float)
     * @see #getLineSpacingMultiplier()
     * @unknown ref android.R.styleable#TextView_lineSpacingExtra
     */
    @android.view.inspector.InspectableProperty
    public float getLineSpacingExtra() {
        return mSpacingAdd;
    }

    /**
     * Sets an explicit line height for this TextView. This is equivalent to the vertical distance
     * between subsequent baselines in the TextView.
     *
     * @param lineHeight
     * 		the line height in pixels
     * @see #setLineSpacing(float, float)
     * @see #getLineSpacingExtra()
     * @unknown ref android.R.styleable#TextView_lineHeight
     */
    public void setLineHeight(@android.annotation.Px
    @android.annotation.IntRange(from = 0)
    int lineHeight) {
        com.android.internal.util.Preconditions.checkArgumentNonnegative(lineHeight);
        final int fontHeight = getPaint().getFontMetricsInt(null);
        // Make sure we don't setLineSpacing if it's not needed to avoid unnecessary redraw.
        if (lineHeight != fontHeight) {
            // Set lineSpacingExtra by the difference of lineSpacing with lineHeight
            setLineSpacing(lineHeight - fontHeight, 1.0F);
        }
    }

    /**
     * Convenience method to append the specified text to the TextView's
     * display buffer, upgrading it to {@link android.widget.TextView.BufferType#EDITABLE}
     * if it was not already editable.
     *
     * @param text
     * 		text to be appended to the already displayed text
     */
    public final void append(java.lang.CharSequence text) {
        append(text, 0, text.length());
    }

    /**
     * Convenience method to append the specified text slice to the TextView's
     * display buffer, upgrading it to {@link android.widget.TextView.BufferType#EDITABLE}
     * if it was not already editable.
     *
     * @param text
     * 		text to be appended to the already displayed text
     * @param start
     * 		the index of the first character in the {@code text}
     * @param end
     * 		the index of the character following the last character in the {@code text}
     * @see Appendable#append(CharSequence, int, int)
     */
    public void append(java.lang.CharSequence text, int start, int end) {
        if (!(mText instanceof android.text.Editable)) {
            setText(mText, TextView.BufferType.EDITABLE);
        }
        append(text, start, end);
        if (mAutoLinkMask != 0) {
            boolean linksWereAdded = android.text.util.Linkify.addLinks(mSpannable, mAutoLinkMask);
            // Do not change the movement method for text that support text selection as it
            // would prevent an arbitrary cursor displacement.
            if ((linksWereAdded && mLinksClickable) && (!textCanBeSelected())) {
                setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
            }
        }
    }

    private void updateTextColors() {
        boolean inval = false;
        final int[] drawableState = getDrawableState();
        int color = mTextColor.getColorForState(drawableState, 0);
        if (color != mCurTextColor) {
            mCurTextColor = color;
            inval = true;
        }
        if (mLinkTextColor != null) {
            color = mLinkTextColor.getColorForState(drawableState, 0);
            if (color != mTextPaint.linkColor) {
                mTextPaint.linkColor = color;
                inval = true;
            }
        }
        if (mHintTextColor != null) {
            color = mHintTextColor.getColorForState(drawableState, 0);
            if (color != mCurHintTextColor) {
                mCurHintTextColor = color;
                if (mText.length() == 0) {
                    inval = true;
                }
            }
        }
        if (inval) {
            // Text needs to be redrawn with the new color
            if (mEditor != null)
                mEditor.invalidateTextDisplayList();

            invalidate();
        }
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if ((((mTextColor != null) && mTextColor.isStateful()) || ((mHintTextColor != null) && mHintTextColor.isStateful())) || ((mLinkTextColor != null) && mLinkTextColor.isStateful())) {
            updateTextColors();
        }
        if (mDrawables != null) {
            final int[] state = getDrawableState();
            for (android.graphics.drawable.Drawable dr : mDrawables.mShowing) {
                if (((dr != null) && dr.isStateful()) && dr.setState(state)) {
                    invalidateDrawable(dr);
                }
            }
        }
    }

    @java.lang.Override
    public void drawableHotspotChanged(float x, float y) {
        super.drawableHotspotChanged(x, y);
        if (mDrawables != null) {
            for (android.graphics.drawable.Drawable dr : mDrawables.mShowing) {
                if (dr != null) {
                    dr.setHotspot(x, y);
                }
            }
        }
    }

    @java.lang.Override
    public android.os.Parcelable onSaveInstanceState() {
        android.os.Parcelable superState = super.onSaveInstanceState();
        // Save state if we are forced to
        final boolean freezesText = getFreezesText();
        boolean hasSelection = false;
        int start = -1;
        int end = -1;
        if (mText != null) {
            start = getSelectionStart();
            end = getSelectionEnd();
            if ((start >= 0) || (end >= 0)) {
                // Or save state if there is a selection
                hasSelection = true;
            }
        }
        if (freezesText || hasSelection) {
            TextView.SavedState ss = new TextView.SavedState(superState);
            if (freezesText) {
                if (mText instanceof android.text.Spanned) {
                    final android.text.Spannable sp = new android.text.SpannableStringBuilder(mText);
                    if (mEditor != null) {
                        removeMisspelledSpans(sp);
                        sp.removeSpan(mEditor.mSuggestionRangeSpan);
                    }
                    ss.text = sp;
                } else {
                    ss.text = mText.toString();
                }
            }
            if (hasSelection) {
                // XXX Should also save the current scroll position!
                ss.selStart = start;
                ss.selEnd = end;
            }
            if ((isFocused() && (start >= 0)) && (end >= 0)) {
                ss.frozenWithFocus = true;
            }
            ss.error = getError();
            if (mEditor != null) {
                ss.editorState = mEditor.saveInstanceState();
            }
            return ss;
        }
        return superState;
    }

    void removeMisspelledSpans(android.text.Spannable spannable) {
        android.text.style.SuggestionSpan[] suggestionSpans = spannable.getSpans(0, spannable.length(), android.text.style.SuggestionSpan.class);
        for (int i = 0; i < suggestionSpans.length; i++) {
            int flags = suggestionSpans[i].getFlags();
            if (((flags & android.text.style.SuggestionSpan.FLAG_EASY_CORRECT) != 0) && ((flags & android.text.style.SuggestionSpan.FLAG_MISSPELLED) != 0)) {
                spannable.removeSpan(suggestionSpans[i]);
            }
        }
    }

    @java.lang.Override
    public void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof TextView.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        TextView.SavedState ss = ((TextView.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        // XXX restore buffer type too, as well as lots of other stuff
        if (ss.text != null) {
            setText(ss.text);
        }
        if ((ss.selStart >= 0) && (ss.selEnd >= 0)) {
            if (mSpannable != null) {
                int len = mText.length();
                if ((ss.selStart > len) || (ss.selEnd > len)) {
                    java.lang.String restored = "";
                    if (ss.text != null) {
                        restored = "(restored) ";
                    }
                    android.util.Log.e(TextView.LOG_TAG, (((((("Saved cursor position " + ss.selStart) + "/") + ss.selEnd) + " out of range for ") + restored) + "text ") + mText);
                } else {
                    android.text.Selection.setSelection(mSpannable, ss.selStart, ss.selEnd);
                    if (ss.frozenWithFocus) {
                        createEditorIfNeeded();
                        mEditor.mFrozenWithFocus = true;
                    }
                }
            }
        }
        if (ss.error != null) {
            final java.lang.CharSequence error = ss.error;
            // Display the error later, after the first layout pass
            post(new java.lang.Runnable() {
                public void run() {
                    if ((mEditor == null) || (!mEditor.mErrorWasChanged)) {
                        setError(error);
                    }
                }
            });
        }
        if (ss.editorState != null) {
            createEditorIfNeeded();
            mEditor.restoreInstanceState(ss.editorState);
        }
    }

    /**
     * Control whether this text view saves its entire text contents when
     * freezing to an icicle, in addition to dynamic state such as cursor
     * position.  By default this is false, not saving the text.  Set to true
     * if the text in the text view is not being saved somewhere else in
     * persistent storage (such as in a content provider) so that if the
     * view is later thawed the user will not lose their data. For
     * {@link android.widget.EditText} it is always enabled, regardless of
     * the value of the attribute.
     *
     * @param freezesText
     * 		Controls whether a frozen icicle should include the
     * 		entire text data: true to include it, false to not.
     * @unknown ref android.R.styleable#TextView_freezesText
     */
    @android.view.RemotableViewMethod
    public void setFreezesText(boolean freezesText) {
        mFreezesText = freezesText;
    }

    /**
     * Return whether this text view is including its entire text contents
     * in frozen icicles. For {@link android.widget.EditText} it always returns true.
     *
     * @return Returns true if text is included, false if it isn't.
     * @see #setFreezesText
     */
    @android.view.inspector.InspectableProperty
    public boolean getFreezesText() {
        return mFreezesText;
    }

    // /////////////////////////////////////////////////////////////////////////
    /**
     * Sets the Factory used to create new {@link Editable Editables}.
     *
     * @param factory
     * 		{@link android.text.Editable.Factory Editable.Factory} to be used
     * @see android.text.Editable.Factory
     * @see android.widget.TextView.BufferType#EDITABLE
     */
    public final void setEditableFactory(android.text.Editable.Factory factory) {
        mEditableFactory = factory;
        setText(mText);
    }

    /**
     * Sets the Factory used to create new {@link Spannable Spannables}.
     *
     * @param factory
     * 		{@link android.text.Spannable.Factory Spannable.Factory} to be used
     * @see android.text.Spannable.Factory
     * @see android.widget.TextView.BufferType#SPANNABLE
     */
    public final void setSpannableFactory(android.text.Spannable.Factory factory) {
        mSpannableFactory = factory;
        setText(mText);
    }

    /**
     * Sets the text to be displayed. TextView <em>does not</em> accept
     * HTML-like formatting, which you can do with text strings in XML resource files.
     * To style your strings, attach android.text.style.* objects to a
     * {@link android.text.SpannableString}, or see the
     * <a href="{@docRoot }guide/topics/resources/available-resources.html#stringresources">
     * Available Resource Types</a> documentation for an example of setting
     * formatted text in the XML resource file.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * If the passed text is a {@link PrecomputedText} but the parameters used to create the
     * PrecomputedText mismatches with this TextView, IllegalArgumentException is thrown. To ensure
     * the parameters match, you can call {@link TextView#setTextMetricsParams} before calling this.
     *
     * @param text
     * 		text to be displayed
     * @unknown ref android.R.styleable#TextView_text
     * @throws IllegalArgumentException
     * 		if the passed text is a {@link PrecomputedText} but the
     * 		parameters used to create the PrecomputedText mismatches
     * 		with this TextView.
     */
    @android.view.RemotableViewMethod
    public final void setText(java.lang.CharSequence text) {
        setText(text, mBufferType);
    }

    /**
     * Sets the text to be displayed but retains the cursor position. Same as
     * {@link #setText(CharSequence)} except that the cursor position (if any) is retained in the
     * new text.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * @param text
     * 		text to be displayed
     * @see #setText(CharSequence)
     */
    @android.view.RemotableViewMethod
    public final void setTextKeepState(java.lang.CharSequence text) {
        setTextKeepState(text, mBufferType);
    }

    /**
     * Sets the text to be displayed and the {@link android.widget.TextView.BufferType}.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * Subclasses overriding this method should ensure that the following post condition holds,
     * in order to guarantee the safety of the view's measurement and layout operations:
     * regardless of the input, after calling #setText both {@code mText} and {@code mTransformed}
     * will be different from {@code null}.
     *
     * @param text
     * 		text to be displayed
     * @param type
     * 		a {@link android.widget.TextView.BufferType} which defines whether the text is
     * 		stored as a static text, styleable/spannable text, or editable text
     * @see #setText(CharSequence)
     * @see android.widget.TextView.BufferType
     * @see #setSpannableFactory(Spannable.Factory)
     * @see #setEditableFactory(Editable.Factory)
     * @unknown ref android.R.styleable#TextView_text
     * @unknown ref android.R.styleable#TextView_bufferType
     */
    public void setText(java.lang.CharSequence text, TextView.BufferType type) {
        setText(text, type, true, 0);
        if (mCharWrapper != null) {
            mCharWrapper.mChars = null;
        }
    }

    @android.annotation.UnsupportedAppUsage
    private void setText(java.lang.CharSequence text, TextView.BufferType type, boolean notifyBefore, int oldlen) {
        mTextSetFromXmlOrResourceId = false;
        if (text == null) {
            text = "";
        }
        // If suggestions are not enabled, remove the suggestion spans from the text
        if (!isSuggestionsEnabled()) {
            text = removeSuggestionSpans(text);
        }
        if (!mUserSetTextScaleX)
            mTextPaint.setTextScaleX(1.0F);

        if ((text instanceof android.text.Spanned) && (((android.text.Spanned) (text)).getSpanStart(TextUtils.TruncateAt.MARQUEE) >= 0)) {
            if (android.view.ViewConfiguration.get(mContext).isFadingMarqueeEnabled()) {
                setHorizontalFadingEdgeEnabled(true);
                mMarqueeFadeMode = TextView.MARQUEE_FADE_NORMAL;
            } else {
                setHorizontalFadingEdgeEnabled(false);
                mMarqueeFadeMode = TextView.MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS;
            }
            setEllipsize(TextUtils.TruncateAt.MARQUEE);
        }
        int n = mFilters.length;
        for (int i = 0; i < n; i++) {
            java.lang.CharSequence out = mFilters[i].filter(text, 0, text.length(), TextView.EMPTY_SPANNED, 0, 0);
            if (out != null) {
                text = out;
            }
        }
        if (notifyBefore) {
            if (mText != null) {
                oldlen = mText.length();
                sendBeforeTextChanged(mText, 0, oldlen, text.length());
            } else {
                sendBeforeTextChanged("", 0, 0, text.length());
            }
        }
        boolean needEditableForNotification = false;
        if ((mListeners != null) && (mListeners.size() != 0)) {
            needEditableForNotification = true;
        }
        android.text.PrecomputedText precomputed = (text instanceof android.text.PrecomputedText) ? ((android.text.PrecomputedText) (text)) : null;
        if (((type == TextView.BufferType.EDITABLE) || (getKeyListener() != null)) || needEditableForNotification) {
            createEditorIfNeeded();
            mEditor.forgetUndoRedo();
            android.text.Editable t = mEditableFactory.newEditable(text);
            text = t;
            setFilters(t, mFilters);
            android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            if (imm != null)
                imm.restartInput(this);

        } else
            if (precomputed != null) {
                if (mTextDir == null) {
                    mTextDir = getTextDirectionHeuristic();
                }
                @android.text.PrecomputedText.Params.CheckResultUsableResult
                final int checkResult = precomputed.getParams().checkResultUsable(getPaint(), mTextDir, mBreakStrategy, mHyphenationFrequency);
                switch (checkResult) {
                    case PrecomputedText.Params.UNUSABLE :
                        throw new java.lang.IllegalArgumentException(((("PrecomputedText's Parameters don't match the parameters of this TextView." + (("Consider using setTextMetricsParams(precomputedText.getParams()) " + "to override the settings of this TextView: ") + "PrecomputedText: ")) + precomputed.getParams()) + "TextView: ") + getTextMetricsParams());
                    case PrecomputedText.Params.NEED_RECOMPUTE :
                        precomputed = android.text.PrecomputedText.create(precomputed, getTextMetricsParams());
                        break;
                    case PrecomputedText.Params.USABLE :
                        // pass through
                }
            } else
                if ((type == TextView.BufferType.SPANNABLE) || (mMovement != null)) {
                    text = mSpannableFactory.newSpannable(text);
                } else
                    if (!(text instanceof TextView.CharWrapper)) {
                        text = android.text.TextUtils.stringOrSpannedString(text);
                    }



        if (mAutoLinkMask != 0) {
            android.text.Spannable s2;
            if ((type == TextView.BufferType.EDITABLE) || (text instanceof android.text.Spannable)) {
                s2 = ((android.text.Spannable) (text));
            } else {
                s2 = mSpannableFactory.newSpannable(text);
            }
            if (android.text.util.Linkify.addLinks(s2, mAutoLinkMask)) {
                text = s2;
                type = (type == TextView.BufferType.EDITABLE) ? TextView.BufferType.EDITABLE : TextView.BufferType.SPANNABLE;
                /* We must go ahead and set the text before changing the
                movement method, because setMovementMethod() may call
                setText() again to try to upgrade the buffer type.
                 */
                setTextInternal(text);
                // Do not change the movement method for text that support text selection as it
                // would prevent an arbitrary cursor displacement.
                if (mLinksClickable && (!textCanBeSelected())) {
                    setMovementMethod(android.text.method.LinkMovementMethod.getInstance());
                }
            }
        }
        mBufferType = type;
        setTextInternal(text);
        if (mTransformation == null) {
            mTransformed = text;
        } else {
            mTransformed = mTransformation.getTransformation(text, this);
        }
        if (mTransformed == null) {
            // Should not happen if the transformation method follows the non-null postcondition.
            mTransformed = "";
        }
        final int textLength = text.length();
        if ((text instanceof android.text.Spannable) && (!mAllowTransformationLengthChange)) {
            android.text.Spannable sp = ((android.text.Spannable) (text));
            // Remove any ChangeWatchers that might have come from other TextViews.
            final TextView.ChangeWatcher[] watchers = sp.getSpans(0, sp.length(), TextView.ChangeWatcher.class);
            final int count = watchers.length;
            for (int i = 0; i < count; i++) {
                sp.removeSpan(watchers[i]);
            }
            if (mChangeWatcher == null)
                mChangeWatcher = new TextView.ChangeWatcher();

            sp.setSpan(mChangeWatcher, 0, textLength, android.text.Spanned.SPAN_INCLUSIVE_INCLUSIVE | (TextView.CHANGE_WATCHER_PRIORITY << android.text.Spanned.SPAN_PRIORITY_SHIFT));
            if (mEditor != null)
                mEditor.addSpanWatchers(sp);

            if (mTransformation != null) {
                sp.setSpan(mTransformation, 0, textLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            }
            if (mMovement != null) {
                mMovement.initialize(this, ((android.text.Spannable) (text)));
                /* Initializing the movement method will have set the
                selection, so reset mSelectionMoved to keep that from
                interfering with the normal on-focus selection-setting.
                 */
                if (mEditor != null)
                    mEditor.mSelectionMoved = false;

            }
        }
        if (mLayout != null) {
            checkForRelayout();
        }
        sendOnTextChanged(text, 0, oldlen, textLength);
        onTextChanged(text, 0, oldlen, textLength);
        notifyViewAccessibilityStateChangedIfNeeded(android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_TEXT);
        if (needEditableForNotification) {
            sendAfterTextChanged(((android.text.Editable) (text)));
        } else {
            notifyListeningManagersAfterTextChanged();
        }
        // SelectionModifierCursorController depends on textCanBeSelected, which depends on text
        if (mEditor != null)
            mEditor.prepareCursorControllers();

    }

    /**
     * Sets the TextView to display the specified slice of the specified
     * char array. You must promise that you will not change the contents
     * of the array except for right before another call to setText(),
     * since the TextView has no way to know that the text
     * has changed and that it needs to invalidate and re-layout.
     *
     * @param text
     * 		char array to be displayed
     * @param start
     * 		start index in the char array
     * @param len
     * 		length of char count after {@code start}
     */
    public final void setText(char[] text, int start, int len) {
        int oldlen = 0;
        if (((start < 0) || (len < 0)) || ((start + len) > text.length)) {
            throw new java.lang.IndexOutOfBoundsException((start + ", ") + len);
        }
        /* We must do the before-notification here ourselves because if
        the old text is a CharWrapper we destroy it before calling
        into the normal path.
         */
        if (mText != null) {
            oldlen = mText.length();
            sendBeforeTextChanged(mText, 0, oldlen, len);
        } else {
            sendBeforeTextChanged("", 0, 0, len);
        }
        if (mCharWrapper == null) {
            mCharWrapper = new TextView.CharWrapper(text, start, len);
        } else {
            mCharWrapper.set(text, start, len);
        }
        setText(mCharWrapper, mBufferType, false, oldlen);
    }

    /**
     * Sets the text to be displayed and the {@link android.widget.TextView.BufferType} but retains
     * the cursor position. Same as
     * {@link #setText(CharSequence, android.widget.TextView.BufferType)} except that the cursor
     * position (if any) is retained in the new text.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * @param text
     * 		text to be displayed
     * @param type
     * 		a {@link android.widget.TextView.BufferType} which defines whether the text is
     * 		stored as a static text, styleable/spannable text, or editable text
     * @see #setText(CharSequence, android.widget.TextView.BufferType)
     */
    public final void setTextKeepState(java.lang.CharSequence text, TextView.BufferType type) {
        int start = getSelectionStart();
        int end = getSelectionEnd();
        int len = text.length();
        setText(text, type);
        if ((start >= 0) || (end >= 0)) {
            if (mSpannable != null) {
                android.text.Selection.setSelection(mSpannable, java.lang.Math.max(0, java.lang.Math.min(start, len)), java.lang.Math.max(0, java.lang.Math.min(end, len)));
            }
        }
    }

    /**
     * Sets the text to be displayed using a string resource identifier.
     *
     * @param resid
     * 		the resource identifier of the string resource to be displayed
     * @see #setText(CharSequence)
     * @unknown ref android.R.styleable#TextView_text
     */
    @android.view.RemotableViewMethod
    public final void setText(@android.annotation.StringRes
    int resid) {
        setText(getContext().getResources().getText(resid));
        mTextSetFromXmlOrResourceId = true;
        mTextId = resid;
    }

    /**
     * Sets the text to be displayed using a string resource identifier and the
     * {@link android.widget.TextView.BufferType}.
     * <p/>
     * When required, TextView will use {@link android.text.Spannable.Factory} to create final or
     * intermediate {@link Spannable Spannables}. Likewise it will use
     * {@link android.text.Editable.Factory} to create final or intermediate
     * {@link Editable Editables}.
     *
     * @param resid
     * 		the resource identifier of the string resource to be displayed
     * @param type
     * 		a {@link android.widget.TextView.BufferType} which defines whether the text is
     * 		stored as a static text, styleable/spannable text, or editable text
     * @see #setText(int)
     * @see #setText(CharSequence)
     * @see android.widget.TextView.BufferType
     * @see #setSpannableFactory(Spannable.Factory)
     * @see #setEditableFactory(Editable.Factory)
     * @unknown ref android.R.styleable#TextView_text
     * @unknown ref android.R.styleable#TextView_bufferType
     */
    public final void setText(@android.annotation.StringRes
    int resid, TextView.BufferType type) {
        setText(getContext().getResources().getText(resid), type);
        mTextSetFromXmlOrResourceId = true;
        mTextId = resid;
    }

    /**
     * Sets the text to be displayed when the text of the TextView is empty.
     * Null means to use the normal empty text. The hint does not currently
     * participate in determining the size of the view.
     *
     * @unknown ref android.R.styleable#TextView_hint
     */
    @android.view.RemotableViewMethod
    public final void setHint(java.lang.CharSequence hint) {
        setHintInternal(hint);
        if ((mEditor != null) && isInputMethodTarget()) {
            mEditor.reportExtractedText();
        }
    }

    private void setHintInternal(java.lang.CharSequence hint) {
        mHint = android.text.TextUtils.stringOrSpannedString(hint);
        if (mLayout != null) {
            checkForRelayout();
        }
        if (mText.length() == 0) {
            invalidate();
        }
        // Invalidate display list if hint is currently used
        if (((mEditor != null) && (mText.length() == 0)) && (mHint != null)) {
            mEditor.invalidateTextDisplayList();
        }
    }

    /**
     * Sets the text to be displayed when the text of the TextView is empty,
     * from a resource.
     *
     * @unknown ref android.R.styleable#TextView_hint
     */
    @android.view.RemotableViewMethod
    public final void setHint(@android.annotation.StringRes
    int resid) {
        setHint(getContext().getResources().getText(resid));
    }

    /**
     * Returns the hint that is displayed when the text of the TextView
     * is empty.
     *
     * @unknown ref android.R.styleable#TextView_hint
     */
    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.CapturedViewProperty
    public java.lang.CharSequence getHint() {
        return mHint;
    }

    /**
     * Returns if the text is constrained to a single horizontally scrolling line ignoring new
     * line characters instead of letting it wrap onto multiple lines.
     *
     * @unknown ref android.R.styleable#TextView_singleLine
     */
    @android.view.inspector.InspectableProperty
    public boolean isSingleLine() {
        return mSingleLine;
    }

    private static boolean isMultilineInputType(int type) {
        return (type & (TYPE_MASK_CLASS | TYPE_TEXT_FLAG_MULTI_LINE)) == (TYPE_CLASS_TEXT | TYPE_TEXT_FLAG_MULTI_LINE);
    }

    /**
     * Removes the suggestion spans.
     */
    java.lang.CharSequence removeSuggestionSpans(java.lang.CharSequence text) {
        if (text instanceof android.text.Spanned) {
            android.text.Spannable spannable;
            if (text instanceof android.text.Spannable) {
                spannable = ((android.text.Spannable) (text));
            } else {
                spannable = mSpannableFactory.newSpannable(text);
            }
            android.text.style.SuggestionSpan[] spans = spannable.getSpans(0, text.length(), android.text.style.SuggestionSpan.class);
            if (spans.length == 0) {
                return text;
            } else {
                text = spannable;
            }
            for (int i = 0; i < spans.length; i++) {
                spannable.removeSpan(spans[i]);
            }
        }
        return text;
    }

    /**
     * Set the type of the content with a constant as defined for {@link EditorInfo#inputType}. This
     * will take care of changing the key listener, by calling {@link #setKeyListener(KeyListener)},
     * to match the given content type.  If the given content type is {@link EditorInfo#TYPE_NULL}
     * then a soft keyboard will not be displayed for this text view.
     *
     * Note that the maximum number of displayed lines (see {@link #setMaxLines(int)}) will be
     * modified if you change the {@link EditorInfo#TYPE_TEXT_FLAG_MULTI_LINE} flag of the input
     * type.
     *
     * @see #getInputType()
     * @see #setRawInputType(int)
     * @see android.text.InputType
     * @unknown ref android.R.styleable#TextView_inputType
     */
    public void setInputType(int type) {
        final boolean wasPassword = TextView.isPasswordInputType(getInputType());
        final boolean wasVisiblePassword = TextView.isVisiblePasswordInputType(getInputType());
        setInputType(type, false);
        final boolean isPassword = TextView.isPasswordInputType(type);
        final boolean isVisiblePassword = TextView.isVisiblePasswordInputType(type);
        boolean forceUpdate = false;
        if (isPassword) {
            setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
            /* fontTypeface */
            /* fontFamily */
            /* weight, not specifeid */
            setTypefaceFromAttrs(null, null, TextView.MONOSPACE, android.graphics.Typeface.NORMAL, -1);
        } else
            if (isVisiblePassword) {
                if (mTransformation == android.text.method.PasswordTransformationMethod.getInstance()) {
                    forceUpdate = true;
                }
                /* fontTypeface */
                /* fontFamily */
                /* weight, not specified */
                setTypefaceFromAttrs(null, null, TextView.MONOSPACE, android.graphics.Typeface.NORMAL, -1);
            } else
                if (wasPassword || wasVisiblePassword) {
                    // not in password mode, clean up typeface and transformation
                    /* fontTypeface */
                    /* fontFamily */
                    /* typeface index */
                    /* weight, not specified */
                    setTypefaceFromAttrs(null, null, TextView.DEFAULT_TYPEFACE, android.graphics.Typeface.NORMAL, -1);
                    if (mTransformation == android.text.method.PasswordTransformationMethod.getInstance()) {
                        forceUpdate = true;
                    }
                }


        boolean singleLine = !TextView.isMultilineInputType(type);
        // We need to update the single line mode if it has changed or we
        // were previously in password mode.
        if ((mSingleLine != singleLine) || forceUpdate) {
            // Change single line mode, but only change the transformation if
            // we are not in password mode.
            applySingleLine(singleLine, !isPassword, true);
        }
        if (!isSuggestionsEnabled()) {
            setTextInternal(removeSuggestionSpans(mText));
        }
        android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
        if (imm != null)
            imm.restartInput(this);

    }

    /**
     * It would be better to rely on the input type for everything. A password inputType should have
     * a password transformation. We should hence use isPasswordInputType instead of this method.
     *
     * We should:
     * - Call setInputType in setKeyListener instead of changing the input type directly (which
     * would install the correct transformation).
     * - Refuse the installation of a non-password transformation in setTransformation if the input
     * type is password.
     *
     * However, this is like this for legacy reasons and we cannot break existing apps. This method
     * is useful since it matches what the user can see (obfuscated text or not).
     *
     * @return true if the current transformation method is of the password type.
     */
    boolean hasPasswordTransformationMethod() {
        return mTransformation instanceof android.text.method.PasswordTransformationMethod;
    }

    static boolean isPasswordInputType(int inputType) {
        final int variation = inputType & (TYPE_MASK_CLASS | TYPE_MASK_VARIATION);
        return ((variation == (TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_PASSWORD)) || (variation == (TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_WEB_PASSWORD))) || (variation == (TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_PASSWORD));
    }

    private static boolean isVisiblePasswordInputType(int inputType) {
        final int variation = inputType & (TYPE_MASK_CLASS | TYPE_MASK_VARIATION);
        return variation == (TYPE_CLASS_TEXT | TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }

    /**
     * Directly change the content type integer of the text view, without
     * modifying any other state.
     *
     * @see #setInputType(int)
     * @see android.text.InputType
     * @unknown ref android.R.styleable#TextView_inputType
     */
    public void setRawInputType(int type) {
        if ((type == android.text.InputType.TYPE_NULL) && (mEditor == null))
            return;
        // TYPE_NULL is the default value

        createEditorIfNeeded();
        mEditor.mInputType = type;
    }

    /**
     *
     *
     * @return {@code null} if the key listener should use pre-O (locale-independent). Otherwise
    a {@code Locale} object that can be used to customize key various listeners.
     * @see DateKeyListener#getInstance(Locale)
     * @see DateTimeKeyListener#getInstance(Locale)
     * @see DigitsKeyListener#getInstance(Locale)
     * @see TimeKeyListener#getInstance(Locale)
     */
    @android.annotation.Nullable
    private java.util.Locale getCustomLocaleForKeyListenerOrNull() {
        if (!mUseInternationalizedInput) {
            // If the application does not target O, stick to the previous behavior.
            return null;
        }
        final android.os.LocaleList locales = getImeHintLocales();
        if (locales == null) {
            // If the application does not explicitly specify IME hint locale, also stick to the
            // previous behavior.
            return null;
        }
        return locales.get(0);
    }

    @android.annotation.UnsupportedAppUsage
    private void setInputType(int type, boolean direct) {
        final int cls = type & TYPE_MASK_CLASS;
        android.text.method.KeyListener input;
        if (cls == TYPE_CLASS_TEXT) {
            boolean autotext = (type & TYPE_TEXT_FLAG_AUTO_CORRECT) != 0;
            android.text.method.TextKeyListener.Capitalize cap;
            if ((type & TYPE_TEXT_FLAG_CAP_CHARACTERS) != 0) {
                cap = TextKeyListener.Capitalize.CHARACTERS;
            } else
                if ((type & TYPE_TEXT_FLAG_CAP_WORDS) != 0) {
                    cap = TextKeyListener.Capitalize.WORDS;
                } else
                    if ((type & TYPE_TEXT_FLAG_CAP_SENTENCES) != 0) {
                        cap = TextKeyListener.Capitalize.SENTENCES;
                    } else {
                        cap = TextKeyListener.Capitalize.NONE;
                    }


            input = android.text.method.TextKeyListener.getInstance(autotext, cap);
        } else
            if (cls == TYPE_CLASS_NUMBER) {
                final java.util.Locale locale = getCustomLocaleForKeyListenerOrNull();
                input = android.text.method.DigitsKeyListener.getInstance(locale, (type & TYPE_NUMBER_FLAG_SIGNED) != 0, (type & TYPE_NUMBER_FLAG_DECIMAL) != 0);
                if (locale != null) {
                    // Override type, if necessary for i18n.
                    int newType = input.getInputType();
                    final int newClass = newType & TYPE_MASK_CLASS;
                    if (newClass != TYPE_CLASS_NUMBER) {
                        // The class is different from the original class. So we need to override
                        // 'type'. But we want to keep the password flag if it's there.
                        if ((type & TYPE_NUMBER_VARIATION_PASSWORD) != 0) {
                            newType |= TYPE_TEXT_VARIATION_PASSWORD;
                        }
                        type = newType;
                    }
                }
            } else
                if (cls == TYPE_CLASS_DATETIME) {
                    final java.util.Locale locale = getCustomLocaleForKeyListenerOrNull();
                    switch (type & TYPE_MASK_VARIATION) {
                        case TYPE_DATETIME_VARIATION_DATE :
                            input = android.text.method.DateKeyListener.getInstance(locale);
                            break;
                        case TYPE_DATETIME_VARIATION_TIME :
                            input = android.text.method.TimeKeyListener.getInstance(locale);
                            break;
                        default :
                            input = android.text.method.DateTimeKeyListener.getInstance(locale);
                            break;
                    }
                    if (mUseInternationalizedInput) {
                        type = input.getInputType();// Override type, if necessary for i18n.

                    }
                } else
                    if (cls == TYPE_CLASS_PHONE) {
                        input = android.text.method.DialerKeyListener.getInstance();
                    } else {
                        input = android.text.method.TextKeyListener.getInstance();
                    }



        setRawInputType(type);
        mListenerChanged = false;
        if (direct) {
            createEditorIfNeeded();
            mEditor.mKeyListener = input;
        } else {
            setKeyListenerOnly(input);
        }
    }

    /**
     * Get the type of the editable content.
     *
     * @see #setInputType(int)
     * @see android.text.InputType
     */
    @android.view.inspector.InspectableProperty(flagMapping = { @android.view.inspector.InspectableProperty.FlagEntry(name = "none", mask = 0xffffffff, target = android.text.InputType.TYPE_NULL), @android.view.inspector.InspectableProperty.FlagEntry(name = "text", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_NORMAL), @android.view.inspector.InspectableProperty.FlagEntry(name = "textUri", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_URI), @android.view.inspector.InspectableProperty.FlagEntry(name = "textEmailAddress", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS), @android.view.inspector.InspectableProperty.FlagEntry(name = "textEmailSubject", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_SUBJECT), @android.view.inspector.InspectableProperty.FlagEntry(name = "textShortMessage", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_SHORT_MESSAGE), @android.view.inspector.InspectableProperty.FlagEntry(name = "textLongMessage", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE), @android.view.inspector.InspectableProperty.FlagEntry(name = "textPersonName", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PERSON_NAME), @android.view.inspector.InspectableProperty.FlagEntry(name = "textPostalAddress", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS), @android.view.inspector.InspectableProperty.FlagEntry(name = "textPassword", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD), @android.view.inspector.InspectableProperty.FlagEntry(name = "textVisiblePassword", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD), @android.view.inspector.InspectableProperty.FlagEntry(name = "textWebEditText", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_WEB_EDIT_TEXT), @android.view.inspector.InspectableProperty.FlagEntry(name = "textFilter", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_FILTER), @android.view.inspector.InspectableProperty.FlagEntry(name = "textPhonetic", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PHONETIC), @android.view.inspector.InspectableProperty.FlagEntry(name = "textWebEmailAddress", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_WEB_EMAIL_ADDRESS), @android.view.inspector.InspectableProperty.FlagEntry(name = "textWebPassword", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD), @android.view.inspector.InspectableProperty.FlagEntry(name = "number", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL), @android.view.inspector.InspectableProperty.FlagEntry(name = "numberPassword", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD), @android.view.inspector.InspectableProperty.FlagEntry(name = "phone", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_PHONE), @android.view.inspector.InspectableProperty.FlagEntry(name = "datetime", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_DATETIME | android.text.InputType.TYPE_DATETIME_VARIATION_NORMAL), @android.view.inspector.InspectableProperty.FlagEntry(name = "date", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_DATETIME | android.text.InputType.TYPE_DATETIME_VARIATION_DATE), @android.view.inspector.InspectableProperty.FlagEntry(name = "time", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_VARIATION, target = android.text.InputType.TYPE_CLASS_DATETIME | android.text.InputType.TYPE_DATETIME_VARIATION_TIME), @android.view.inspector.InspectableProperty.FlagEntry(name = "textCapCharacters", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS), @android.view.inspector.InspectableProperty.FlagEntry(name = "textCapWords", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_CAP_WORDS), @android.view.inspector.InspectableProperty.FlagEntry(name = "textCapSentences", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_CAP_SENTENCES), @android.view.inspector.InspectableProperty.FlagEntry(name = "textAutoCorrect", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_AUTO_CORRECT), @android.view.inspector.InspectableProperty.FlagEntry(name = "textAutoComplete", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_AUTO_COMPLETE), @android.view.inspector.InspectableProperty.FlagEntry(name = "textMultiLine", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE), @android.view.inspector.InspectableProperty.FlagEntry(name = "textImeMultiLine", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE), @android.view.inspector.InspectableProperty.FlagEntry(name = "textNoSuggestions", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS), @android.view.inspector.InspectableProperty.FlagEntry(name = "numberSigned", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_SIGNED), @android.view.inspector.InspectableProperty.FlagEntry(name = "numberDecimal", mask = android.text.InputType.TYPE_MASK_CLASS | android.text.InputType.TYPE_MASK_FLAGS, target = android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL) })
    public int getInputType() {
        return mEditor == null ? TYPE_NULL : mEditor.mInputType;
    }

    /**
     * Change the editor type integer associated with the text view, which
     * is reported to an Input Method Editor (IME) with {@link EditorInfo#imeOptions}
     * when it has focus.
     *
     * @see #getImeOptions
     * @see android.view.inputmethod.EditorInfo
     * @unknown ref android.R.styleable#TextView_imeOptions
     */
    public void setImeOptions(int imeOptions) {
        createEditorIfNeeded();
        mEditor.createInputContentTypeIfNeeded();
        mEditor.mInputContentType.imeOptions = imeOptions;
    }

    /**
     * Get the type of the Input Method Editor (IME).
     *
     * @return the type of the IME
     * @see #setImeOptions(int)
     * @see EditorInfo
     */
    @android.view.inspector.InspectableProperty(flagMapping = { @android.view.inspector.InspectableProperty.FlagEntry(name = "normal", mask = 0xffffffff, target = android.view.inputmethod.EditorInfo.IME_NULL), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionUnspecified", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_UNSPECIFIED), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionNone", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_NONE), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionGo", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_GO), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionSearch", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionSend", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_SEND), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionNext", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_NEXT), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionDone", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_DONE), @android.view.inspector.InspectableProperty.FlagEntry(name = "actionPrevious", mask = android.view.inputmethod.EditorInfo.IME_MASK_ACTION, target = android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagForceAscii", target = android.view.inputmethod.EditorInfo.IME_FLAG_FORCE_ASCII), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagNavigateNext", target = android.view.inputmethod.EditorInfo.IME_FLAG_NAVIGATE_NEXT), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagNavigatePrevious", target = android.view.inputmethod.EditorInfo.IME_FLAG_NAVIGATE_PREVIOUS), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagNoAccessoryAction", target = android.view.inputmethod.EditorInfo.IME_FLAG_NO_ACCESSORY_ACTION), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagNoEnterAction", target = android.view.inputmethod.EditorInfo.IME_FLAG_NO_ENTER_ACTION), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagNoExtractUi", target = android.view.inputmethod.EditorInfo.IME_FLAG_NO_EXTRACT_UI), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagNoFullscreen", target = android.view.inputmethod.EditorInfo.IME_FLAG_NO_FULLSCREEN), @android.view.inspector.InspectableProperty.FlagEntry(name = "flagNoPersonalizedLearning", target = android.view.inputmethod.EditorInfo.IME_FLAG_NO_PERSONALIZED_LEARNING) })
    public int getImeOptions() {
        return (mEditor != null) && (mEditor.mInputContentType != null) ? mEditor.mInputContentType.imeOptions : android.view.inputmethod.EditorInfo.IME_NULL;
    }

    /**
     * Change the custom IME action associated with the text view, which
     * will be reported to an IME with {@link EditorInfo#actionLabel}
     * and {@link EditorInfo#actionId} when it has focus.
     *
     * @see #getImeActionLabel
     * @see #getImeActionId
     * @see android.view.inputmethod.EditorInfo
     * @unknown ref android.R.styleable#TextView_imeActionLabel
     * @unknown ref android.R.styleable#TextView_imeActionId
     */
    public void setImeActionLabel(java.lang.CharSequence label, int actionId) {
        createEditorIfNeeded();
        mEditor.createInputContentTypeIfNeeded();
        mEditor.mInputContentType.imeActionLabel = label;
        mEditor.mInputContentType.imeActionId = actionId;
    }

    /**
     * Get the IME action label previous set with {@link #setImeActionLabel}.
     *
     * @see #setImeActionLabel
     * @see android.view.inputmethod.EditorInfo
     */
    @android.view.inspector.InspectableProperty
    public java.lang.CharSequence getImeActionLabel() {
        return (mEditor != null) && (mEditor.mInputContentType != null) ? mEditor.mInputContentType.imeActionLabel : null;
    }

    /**
     * Get the IME action ID previous set with {@link #setImeActionLabel}.
     *
     * @see #setImeActionLabel
     * @see android.view.inputmethod.EditorInfo
     */
    @android.view.inspector.InspectableProperty
    public int getImeActionId() {
        return (mEditor != null) && (mEditor.mInputContentType != null) ? mEditor.mInputContentType.imeActionId : 0;
    }

    /**
     * Set a special listener to be called when an action is performed
     * on the text view.  This will be called when the enter key is pressed,
     * or when an action supplied to the IME is selected by the user.  Setting
     * this means that the normal hard key event will not insert a newline
     * into the text view, even if it is multi-line; holding down the ALT
     * modifier will, however, allow the user to insert a newline character.
     */
    public void setOnEditorActionListener(TextView.OnEditorActionListener l) {
        createEditorIfNeeded();
        mEditor.createInputContentTypeIfNeeded();
        mEditor.mInputContentType.onEditorActionListener = l;
    }

    /**
     * Called when an attached input method calls
     * {@link InputConnection#performEditorAction(int)
     * InputConnection.performEditorAction()}
     * for this text view.  The default implementation will call your action
     * listener supplied to {@link #setOnEditorActionListener}, or perform
     * a standard operation for {@link EditorInfo#IME_ACTION_NEXT
     * EditorInfo.IME_ACTION_NEXT}, {@link EditorInfo#IME_ACTION_PREVIOUS
     * EditorInfo.IME_ACTION_PREVIOUS}, or {@link EditorInfo#IME_ACTION_DONE
     * EditorInfo.IME_ACTION_DONE}.
     *
     * <p>For backwards compatibility, if no IME options have been set and the
     * text view would not normally advance focus on enter, then
     * the NEXT and DONE actions received here will be turned into an enter
     * key down/up pair to go through the normal key handling.
     *
     * @param actionCode
     * 		The code of the action being performed.
     * @see #setOnEditorActionListener
     */
    public void onEditorAction(int actionCode) {
        final android.widget.Editor.InputContentType ict = (mEditor == null) ? null : mEditor.mInputContentType;
        if (ict != null) {
            if (ict.onEditorActionListener != null) {
                if (ict.onEditorActionListener.onEditorAction(this, actionCode, null)) {
                    return;
                }
            }
            // This is the handling for some default action.
            // Note that for backwards compatibility we don't do this
            // default handling if explicit ime options have not been given,
            // instead turning this into the normal enter key codes that an
            // app may be expecting.
            if (actionCode == android.view.inputmethod.EditorInfo.IME_ACTION_NEXT) {
                android.view.View v = focusSearch(android.view.View.FOCUS_FORWARD);
                if (v != null) {
                    if (!v.requestFocus(android.view.View.FOCUS_FORWARD)) {
                        throw new java.lang.IllegalStateException("focus search returned a view " + "that wasn't able to take focus!");
                    }
                }
                return;
            } else
                if (actionCode == android.view.inputmethod.EditorInfo.IME_ACTION_PREVIOUS) {
                    android.view.View v = focusSearch(android.view.View.FOCUS_BACKWARD);
                    if (v != null) {
                        if (!v.requestFocus(android.view.View.FOCUS_BACKWARD)) {
                            throw new java.lang.IllegalStateException("focus search returned a view " + "that wasn't able to take focus!");
                        }
                    }
                    return;
                } else
                    if (actionCode == android.view.inputmethod.EditorInfo.IME_ACTION_DONE) {
                        android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
                        if ((imm != null) && imm.isActive(this)) {
                            imm.hideSoftInputFromWindow(getWindowToken(), 0);
                        }
                        return;
                    }


        }
        android.view.ViewRootImpl viewRootImpl = getViewRootImpl();
        if (viewRootImpl != null) {
            long eventTime = android.os.SystemClock.uptimeMillis();
            viewRootImpl.dispatchKeyFromIme(new android.view.KeyEvent(eventTime, eventTime, android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_ENTER, 0, 0, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, (android.view.KeyEvent.FLAG_SOFT_KEYBOARD | android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE) | android.view.KeyEvent.FLAG_EDITOR_ACTION));
            viewRootImpl.dispatchKeyFromIme(new android.view.KeyEvent(android.os.SystemClock.uptimeMillis(), eventTime, android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_ENTER, 0, 0, android.view.KeyCharacterMap.VIRTUAL_KEYBOARD, 0, (android.view.KeyEvent.FLAG_SOFT_KEYBOARD | android.view.KeyEvent.FLAG_KEEP_TOUCH_MODE) | android.view.KeyEvent.FLAG_EDITOR_ACTION));
        }
    }

    /**
     * Set the private content type of the text, which is the
     * {@link EditorInfo#privateImeOptions EditorInfo.privateImeOptions}
     * field that will be filled in when creating an input connection.
     *
     * @see #getPrivateImeOptions()
     * @see EditorInfo#privateImeOptions
     * @unknown ref android.R.styleable#TextView_privateImeOptions
     */
    public void setPrivateImeOptions(java.lang.String type) {
        createEditorIfNeeded();
        mEditor.createInputContentTypeIfNeeded();
        mEditor.mInputContentType.privateImeOptions = type;
    }

    /**
     * Get the private type of the content.
     *
     * @see #setPrivateImeOptions(String)
     * @see EditorInfo#privateImeOptions
     */
    @android.view.inspector.InspectableProperty
    public java.lang.String getPrivateImeOptions() {
        return (mEditor != null) && (mEditor.mInputContentType != null) ? mEditor.mInputContentType.privateImeOptions : null;
    }

    /**
     * Set the extra input data of the text, which is the
     * {@link EditorInfo#extras TextBoxAttribute.extras}
     * Bundle that will be filled in when creating an input connection.  The
     * given integer is the resource identifier of an XML resource holding an
     * {@link android.R.styleable#InputExtras &lt;input-extras&gt;} XML tree.
     *
     * @see #getInputExtras(boolean)
     * @see EditorInfo#extras
     * @unknown ref android.R.styleable#TextView_editorExtras
     */
    public void setInputExtras(@android.annotation.XmlRes
    int xmlResId) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        createEditorIfNeeded();
        android.content.res.XmlResourceParser parser = getResources().getXml(xmlResId);
        mEditor.createInputContentTypeIfNeeded();
        mEditor.mInputContentType.extras = new android.os.Bundle();
        getResources().parseBundleExtras(parser, mEditor.mInputContentType.extras);
    }

    /**
     * Retrieve the input extras currently associated with the text view, which
     * can be viewed as well as modified.
     *
     * @param create
     * 		If true, the extras will be created if they don't already
     * 		exist.  Otherwise, null will be returned if none have been created.
     * @see #setInputExtras(int)
     * @see EditorInfo#extras
     * @unknown ref android.R.styleable#TextView_editorExtras
     */
    public android.os.Bundle getInputExtras(boolean create) {
        if ((mEditor == null) && (!create))
            return null;

        createEditorIfNeeded();
        if (mEditor.mInputContentType == null) {
            if (!create)
                return null;

            mEditor.createInputContentTypeIfNeeded();
        }
        if (mEditor.mInputContentType.extras == null) {
            if (!create)
                return null;

            mEditor.mInputContentType.extras = new android.os.Bundle();
        }
        return mEditor.mInputContentType.extras;
    }

    /**
     * Change "hint" locales associated with the text view, which will be reported to an IME with
     * {@link EditorInfo#hintLocales} when it has focus.
     *
     * Starting with Android O, this also causes internationalized listeners to be created (or
     * change locale) based on the first locale in the input locale list.
     *
     * <p><strong>Note:</strong> If you want new "hint" to take effect immediately you need to
     * call {@link InputMethodManager#restartInput(View)}.</p>
     *
     * @param hintLocales
     * 		List of the languages that the user is supposed to switch to no matter
     * 		what input method subtype is currently used. Set {@code null} to clear the current "hint".
     * @see #getImeHintLocales()
     * @see android.view.inputmethod.EditorInfo#hintLocales
     */
    public void setImeHintLocales(@android.annotation.Nullable
    android.os.LocaleList hintLocales) {
        createEditorIfNeeded();
        mEditor.createInputContentTypeIfNeeded();
        mEditor.mInputContentType.imeHintLocales = hintLocales;
        if (mUseInternationalizedInput) {
            changeListenerLocaleTo(hintLocales == null ? null : hintLocales.get(0));
        }
    }

    /**
     *
     *
     * @return The current languages list "hint". {@code null} when no "hint" is available.
     * @see #setImeHintLocales(LocaleList)
     * @see android.view.inputmethod.EditorInfo#hintLocales
     */
    @android.annotation.Nullable
    public android.os.LocaleList getImeHintLocales() {
        if (mEditor == null) {
            return null;
        }
        if (mEditor.mInputContentType == null) {
            return null;
        }
        return mEditor.mInputContentType.imeHintLocales;
    }

    /**
     * Returns the error message that was set to be displayed with
     * {@link #setError}, or <code>null</code> if no error was set
     * or if it the error was cleared by the widget after user input.
     */
    public java.lang.CharSequence getError() {
        return mEditor == null ? null : mEditor.mError;
    }

    /**
     * Sets the right-hand compound drawable of the TextView to the "error"
     * icon and sets an error message that will be displayed in a popup when
     * the TextView has focus.  The icon and error message will be reset to
     * null when any key events cause changes to the TextView's text.  If the
     * <code>error</code> is <code>null</code>, the error message and icon
     * will be cleared.
     */
    @android.view.RemotableViewMethod
    public void setError(java.lang.CharSequence error) {
        if (error == null) {
            setError(null, null);
        } else {
            android.graphics.drawable.Drawable dr = getContext().getDrawable(com.android.internal.R.drawable.indicator_input_error);
            dr.setBounds(0, 0, dr.getIntrinsicWidth(), dr.getIntrinsicHeight());
            setError(error, dr);
        }
    }

    /**
     * Sets the right-hand compound drawable of the TextView to the specified
     * icon and sets an error message that will be displayed in a popup when
     * the TextView has focus.  The icon and error message will be reset to
     * null when any key events cause changes to the TextView's text.  The
     * drawable must already have had {@link Drawable#setBounds} set on it.
     * If the <code>error</code> is <code>null</code>, the error message will
     * be cleared (and you should provide a <code>null</code> icon as well).
     */
    public void setError(java.lang.CharSequence error, android.graphics.drawable.Drawable icon) {
        createEditorIfNeeded();
        mEditor.setError(error, icon);
        notifyViewAccessibilityStateChangedIfNeeded(android.view.accessibility.AccessibilityEvent.CONTENT_CHANGE_TYPE_UNDEFINED);
    }

    @java.lang.Override
    protected boolean setFrame(int l, int t, int r, int b) {
        boolean result = super.setFrame(l, t, r, b);
        if (mEditor != null)
            mEditor.setFrame();

        restartMarqueeIfNeeded();
        return result;
    }

    private void restartMarqueeIfNeeded() {
        if (mRestartMarquee && (mEllipsize == android.text.TextUtils.TruncateAt.MARQUEE)) {
            mRestartMarquee = false;
            startMarquee();
        }
    }

    /**
     * Sets the list of input filters that will be used if the buffer is
     * Editable. Has no effect otherwise.
     *
     * @unknown ref android.R.styleable#TextView_maxLength
     */
    public void setFilters(android.text.InputFilter[] filters) {
        if (filters == null) {
            throw new java.lang.IllegalArgumentException();
        }
        mFilters = filters;
        if (mText instanceof android.text.Editable) {
            setFilters(((android.text.Editable) (mText)), filters);
        }
    }

    /**
     * Sets the list of input filters on the specified Editable,
     * and includes mInput in the list if it is an InputFilter.
     */
    private void setFilters(android.text.Editable e, android.text.InputFilter[] filters) {
        if (mEditor != null) {
            final boolean undoFilter = mEditor.mUndoInputFilter != null;
            final boolean keyFilter = mEditor.mKeyListener instanceof android.text.InputFilter;
            int num = 0;
            if (undoFilter)
                num++;

            if (keyFilter)
                num++;

            if (num > 0) {
                android.text.InputFilter[] nf = new android.text.InputFilter[filters.length + num];
                java.lang.System.arraycopy(filters, 0, nf, 0, filters.length);
                num = 0;
                if (undoFilter) {
                    nf[filters.length] = mEditor.mUndoInputFilter;
                    num++;
                }
                if (keyFilter) {
                    nf[filters.length + num] = ((android.text.InputFilter) (mEditor.mKeyListener));
                }
                e.setFilters(nf);
                return;
            }
        }
        e.setFilters(filters);
    }

    /**
     * Returns the current list of input filters.
     *
     * @unknown ref android.R.styleable#TextView_maxLength
     */
    public android.text.InputFilter[] getFilters() {
        return mFilters;
    }

    // ///////////////////////////////////////////////////////////////////////
    private int getBoxHeight(android.text.Layout l) {
        android.graphics.Insets opticalInsets = (android.view.View.isLayoutModeOptical(mParent)) ? getOpticalInsets() : android.graphics.Insets.NONE;
        int padding = (l == mHintLayout) ? getCompoundPaddingTop() + getCompoundPaddingBottom() : getExtendedPaddingTop() + getExtendedPaddingBottom();
        return ((getMeasuredHeight() - padding) + opticalInsets.top) + opticalInsets.bottom;
    }

    @android.annotation.UnsupportedAppUsage
    int getVerticalOffset(boolean forceNormal) {
        int voffset = 0;
        final int gravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        android.text.Layout l = mLayout;
        if (((!forceNormal) && (mText.length() == 0)) && (mHintLayout != null)) {
            l = mHintLayout;
        }
        if (gravity != android.view.Gravity.TOP) {
            int boxht = getBoxHeight(l);
            int textht = l.getHeight();
            if (textht < boxht) {
                if (gravity == android.view.Gravity.BOTTOM) {
                    voffset = boxht - textht;
                } else {
                    // (gravity == Gravity.CENTER_VERTICAL)
                    voffset = (boxht - textht) >> 1;
                }
            }
        }
        return voffset;
    }

    private int getBottomVerticalOffset(boolean forceNormal) {
        int voffset = 0;
        final int gravity = mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        android.text.Layout l = mLayout;
        if (((!forceNormal) && (mText.length() == 0)) && (mHintLayout != null)) {
            l = mHintLayout;
        }
        if (gravity != android.view.Gravity.BOTTOM) {
            int boxht = getBoxHeight(l);
            int textht = l.getHeight();
            if (textht < boxht) {
                if (gravity == android.view.Gravity.TOP) {
                    voffset = boxht - textht;
                } else {
                    // (gravity == Gravity.CENTER_VERTICAL)
                    voffset = (boxht - textht) >> 1;
                }
            }
        }
        return voffset;
    }

    void invalidateCursorPath() {
        if (mHighlightPathBogus) {
            invalidateCursor();
        } else {
            final int horizontalPadding = getCompoundPaddingLeft();
            final int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
            if (mEditor.mDrawableForCursor == null) {
                synchronized(TextView.TEMP_RECTF) {
                    /* The reason for this concern about the thickness of the
                    cursor and doing the floor/ceil on the coordinates is that
                    some EditTexts (notably textfields in the Browser) have
                    anti-aliased text where not all the characters are
                    necessarily at integer-multiple locations.  This should
                    make sure the entire cursor gets invalidated instead of
                    sometimes missing half a pixel.
                     */
                    float thick = ((float) (java.lang.Math.ceil(mTextPaint.getStrokeWidth())));
                    if (thick < 1.0F) {
                        thick = 1.0F;
                    }
                    thick /= 2.0F;
                    // mHighlightPath is guaranteed to be non null at that point.
                    mHighlightPath.computeBounds(TextView.TEMP_RECTF, false);
                    invalidate(((int) (java.lang.Math.floor((horizontalPadding + TextView.TEMP_RECTF.left) - thick))), ((int) (java.lang.Math.floor((verticalPadding + TextView.TEMP_RECTF.top) - thick))), ((int) (java.lang.Math.ceil((horizontalPadding + TextView.TEMP_RECTF.right) + thick))), ((int) (java.lang.Math.ceil((verticalPadding + TextView.TEMP_RECTF.bottom) + thick))));
                }
            } else {
                final android.graphics.Rect bounds = mEditor.mDrawableForCursor.getBounds();
                invalidate(bounds.left + horizontalPadding, bounds.top + verticalPadding, bounds.right + horizontalPadding, bounds.bottom + verticalPadding);
            }
        }
    }

    void invalidateCursor() {
        int where = getSelectionEnd();
        invalidateCursor(where, where, where);
    }

    private void invalidateCursor(int a, int b, int c) {
        if (((a >= 0) || (b >= 0)) || (c >= 0)) {
            int start = java.lang.Math.min(java.lang.Math.min(a, b), c);
            int end = java.lang.Math.max(java.lang.Math.max(a, b), c);
            /* Also invalidates blinking cursor */
            invalidateRegion(start, end, true);
        }
    }

    /**
     * Invalidates the region of text enclosed between the start and end text offsets.
     */
    void invalidateRegion(int start, int end, boolean invalidateCursor) {
        if (mLayout == null) {
            invalidate();
        } else {
            int lineStart = mLayout.getLineForOffset(start);
            int top = mLayout.getLineTop(lineStart);
            // This is ridiculous, but the descent from the line above
            // can hang down into the line we really want to redraw,
            // so we have to invalidate part of the line above to make
            // sure everything that needs to be redrawn really is.
            // (But not the whole line above, because that would cause
            // the same problem with the descenders on the line above it!)
            if (lineStart > 0) {
                top -= mLayout.getLineDescent(lineStart - 1);
            }
            int lineEnd;
            if (start == end) {
                lineEnd = lineStart;
            } else {
                lineEnd = mLayout.getLineForOffset(end);
            }
            int bottom = mLayout.getLineBottom(lineEnd);
            // mEditor can be null in case selection is set programmatically.
            if ((invalidateCursor && (mEditor != null)) && (mEditor.mDrawableForCursor != null)) {
                final android.graphics.Rect bounds = mEditor.mDrawableForCursor.getBounds();
                top = java.lang.Math.min(top, bounds.top);
                bottom = java.lang.Math.max(bottom, bounds.bottom);
            }
            final int compoundPaddingLeft = getCompoundPaddingLeft();
            final int verticalPadding = getExtendedPaddingTop() + getVerticalOffset(true);
            int left;
            int right;
            if ((lineStart == lineEnd) && (!invalidateCursor)) {
                left = ((int) (mLayout.getPrimaryHorizontal(start)));
                right = ((int) (mLayout.getPrimaryHorizontal(end) + 1.0));
                left += compoundPaddingLeft;
                right += compoundPaddingLeft;
            } else {
                // Rectangle bounding box when the region spans several lines
                left = compoundPaddingLeft;
                right = getWidth() - getCompoundPaddingRight();
            }
            invalidate(mScrollX + left, verticalPadding + top, mScrollX + right, verticalPadding + bottom);
        }
    }

    private void registerForPreDraw() {
        if (!mPreDrawRegistered) {
            getViewTreeObserver().addOnPreDrawListener(this);
            mPreDrawRegistered = true;
        }
    }

    private void unregisterForPreDraw() {
        getViewTreeObserver().removeOnPreDrawListener(this);
        mPreDrawRegistered = false;
        mPreDrawListenerDetached = false;
    }

    /**
     * {@inheritDoc }
     */
    @java.lang.Override
    public boolean onPreDraw() {
        if (mLayout == null) {
            assumeLayout();
        }
        if (mMovement != null) {
            /* This code also provides auto-scrolling when a cursor is moved using a
            CursorController (insertion point or selection limits).
            For selection, ensure start or end is visible depending on controller's state.
             */
            int curs = getSelectionEnd();
            // Do not create the controller if it is not already created.
            if (((mEditor != null) && (mEditor.mSelectionModifierCursorController != null)) && mEditor.mSelectionModifierCursorController.isSelectionStartDragged()) {
                curs = getSelectionStart();
            }
            /* TODO: This should really only keep the end in view if
            it already was before the text changed.  I'm not sure
            of a good way to tell from here if it was.
             */
            if ((curs < 0) && ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == android.view.Gravity.BOTTOM)) {
                curs = mText.length();
            }
            if (curs >= 0) {
                bringPointIntoView(curs);
            }
        } else {
            bringTextIntoView();
        }
        // This has to be checked here since:
        // - onFocusChanged cannot start it when focus is given to a view with selected text (after
        // a screen rotation) since layout is not yet initialized at that point.
        if ((mEditor != null) && mEditor.mCreatedWithASelection) {
            mEditor.refreshTextActionMode();
            mEditor.mCreatedWithASelection = false;
        }
        unregisterForPreDraw();
        return true;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mEditor != null)
            mEditor.onAttachedToWindow();

        if (mPreDrawListenerDetached) {
            getViewTreeObserver().addOnPreDrawListener(this);
            mPreDrawListenerDetached = false;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void onDetachedFromWindowInternal() {
        if (mPreDrawRegistered) {
            getViewTreeObserver().removeOnPreDrawListener(this);
            mPreDrawListenerDetached = true;
        }
        resetResolvedDrawables();
        if (mEditor != null)
            mEditor.onDetachedFromWindow();

        super.onDetachedFromWindowInternal();
    }

    @java.lang.Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        if (mEditor != null)
            mEditor.onScreenStateChanged(screenState);

    }

    @java.lang.Override
    protected boolean isPaddingOffsetRequired() {
        return (mShadowRadius != 0) || (mDrawables != null);
    }

    @java.lang.Override
    protected int getLeftPaddingOffset() {
        return (getCompoundPaddingLeft() - mPaddingLeft) + ((int) (java.lang.Math.min(0, mShadowDx - mShadowRadius)));
    }

    @java.lang.Override
    protected int getTopPaddingOffset() {
        return ((int) (java.lang.Math.min(0, mShadowDy - mShadowRadius)));
    }

    @java.lang.Override
    protected int getBottomPaddingOffset() {
        return ((int) (java.lang.Math.max(0, mShadowDy + mShadowRadius)));
    }

    @java.lang.Override
    protected int getRightPaddingOffset() {
        return (-(getCompoundPaddingRight() - mPaddingRight)) + ((int) (java.lang.Math.max(0, mShadowDx + mShadowRadius)));
    }

    @java.lang.Override
    protected boolean verifyDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        final boolean verified = super.verifyDrawable(who);
        if ((!verified) && (mDrawables != null)) {
            for (android.graphics.drawable.Drawable dr : mDrawables.mShowing) {
                if (who == dr) {
                    return true;
                }
            }
        }
        return verified;
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mDrawables != null) {
            for (android.graphics.drawable.Drawable dr : mDrawables.mShowing) {
                if (dr != null) {
                    dr.jumpToCurrentState();
                }
            }
        }
    }

    @java.lang.Override
    public void invalidateDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable drawable) {
        boolean handled = false;
        if (verifyDrawable(drawable)) {
            final android.graphics.Rect dirty = drawable.getBounds();
            int scrollX = mScrollX;
            int scrollY = mScrollY;
            // IMPORTANT: The coordinates below are based on the coordinates computed
            // for each compound drawable in onDraw(). Make sure to update each section
            // accordingly.
            final TextView.Drawables drawables = mDrawables;
            if (drawables != null) {
                if (drawable == drawables.mShowing[TextView.Drawables.LEFT]) {
                    final int compoundPaddingTop = getCompoundPaddingTop();
                    final int compoundPaddingBottom = getCompoundPaddingBottom();
                    final int vspace = ((mBottom - mTop) - compoundPaddingBottom) - compoundPaddingTop;
                    scrollX += mPaddingLeft;
                    scrollY += compoundPaddingTop + ((vspace - drawables.mDrawableHeightLeft) / 2);
                    handled = true;
                } else
                    if (drawable == drawables.mShowing[TextView.Drawables.RIGHT]) {
                        final int compoundPaddingTop = getCompoundPaddingTop();
                        final int compoundPaddingBottom = getCompoundPaddingBottom();
                        final int vspace = ((mBottom - mTop) - compoundPaddingBottom) - compoundPaddingTop;
                        scrollX += ((mRight - mLeft) - mPaddingRight) - drawables.mDrawableSizeRight;
                        scrollY += compoundPaddingTop + ((vspace - drawables.mDrawableHeightRight) / 2);
                        handled = true;
                    } else
                        if (drawable == drawables.mShowing[TextView.Drawables.TOP]) {
                            final int compoundPaddingLeft = getCompoundPaddingLeft();
                            final int compoundPaddingRight = getCompoundPaddingRight();
                            final int hspace = ((mRight - mLeft) - compoundPaddingRight) - compoundPaddingLeft;
                            scrollX += compoundPaddingLeft + ((hspace - drawables.mDrawableWidthTop) / 2);
                            scrollY += mPaddingTop;
                            handled = true;
                        } else
                            if (drawable == drawables.mShowing[TextView.Drawables.BOTTOM]) {
                                final int compoundPaddingLeft = getCompoundPaddingLeft();
                                final int compoundPaddingRight = getCompoundPaddingRight();
                                final int hspace = ((mRight - mLeft) - compoundPaddingRight) - compoundPaddingLeft;
                                scrollX += compoundPaddingLeft + ((hspace - drawables.mDrawableWidthBottom) / 2);
                                scrollY += ((mBottom - mTop) - mPaddingBottom) - drawables.mDrawableSizeBottom;
                                handled = true;
                            }



            }
            if (handled) {
                invalidate(dirty.left + scrollX, dirty.top + scrollY, dirty.right + scrollX, dirty.bottom + scrollY);
            }
        }
        if (!handled) {
            super.invalidateDrawable(drawable);
        }
    }

    @java.lang.Override
    public boolean hasOverlappingRendering() {
        // horizontal fading edge causes SaveLayerAlpha, which doesn't support alpha modulation
        return (((((getBackground() != null) && (getBackground().getCurrent() != null)) || (mSpannable != null)) || hasSelection()) || isHorizontalFadingEdgeEnabled()) || (mShadowColor != 0);
    }

    /**
     * Returns the state of the {@code textIsSelectable} flag (See
     * {@link #setTextIsSelectable setTextIsSelectable()}). Although you have to set this flag
     * to allow users to select and copy text in a non-editable TextView, the content of an
     * {@link EditText} can always be selected, independently of the value of this flag.
     * <p>
     *
     * @return True if the text displayed in this TextView can be selected by the user.
     * @unknown ref android.R.styleable#TextView_textIsSelectable
     */
    @android.view.inspector.InspectableProperty(name = "textIsSelectable")
    public boolean isTextSelectable() {
        return mEditor == null ? false : mEditor.mTextIsSelectable;
    }

    /**
     * Sets whether the content of this view is selectable by the user. The default is
     * {@code false}, meaning that the content is not selectable.
     * <p>
     * When you use a TextView to display a useful piece of information to the user (such as a
     * contact's address), make it selectable, so that the user can select and copy its
     * content. You can also use set the XML attribute
     * {@link android.R.styleable#TextView_textIsSelectable} to "true".
     * <p>
     * When you call this method to set the value of {@code textIsSelectable}, it sets
     * the flags {@code focusable}, {@code focusableInTouchMode}, {@code clickable},
     * and {@code longClickable} to the same value. These flags correspond to the attributes
     * {@link android.R.styleable#View_focusable android:focusable},
     * {@link android.R.styleable#View_focusableInTouchMode android:focusableInTouchMode},
     * {@link android.R.styleable#View_clickable android:clickable}, and
     * {@link android.R.styleable#View_longClickable android:longClickable}. To restore any of these
     * flags to a state you had set previously, call one or more of the following methods:
     * {@link #setFocusable(boolean) setFocusable()},
     * {@link #setFocusableInTouchMode(boolean) setFocusableInTouchMode()},
     * {@link #setClickable(boolean) setClickable()} or
     * {@link #setLongClickable(boolean) setLongClickable()}.
     *
     * @param selectable
     * 		Whether the content of this TextView should be selectable.
     */
    public void setTextIsSelectable(boolean selectable) {
        if ((!selectable) && (mEditor == null))
            return;
        // false is default value with no edit data

        createEditorIfNeeded();
        if (mEditor.mTextIsSelectable == selectable)
            return;

        mEditor.mTextIsSelectable = selectable;
        setFocusableInTouchMode(selectable);
        setFocusable(android.view.View.FOCUSABLE_AUTO);
        setClickable(selectable);
        setLongClickable(selectable);
        // mInputType should already be EditorInfo.TYPE_NULL and mInput should be null
        setMovementMethod(selectable ? android.text.method.ArrowKeyMovementMethod.getInstance() : null);
        setText(mText, selectable ? TextView.BufferType.SPANNABLE : TextView.BufferType.NORMAL);
        // Called by setText above, but safer in case of future code changes
        mEditor.prepareCursorControllers();
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState;
        if (mSingleLine) {
            drawableState = super.onCreateDrawableState(extraSpace);
        } else {
            drawableState = super.onCreateDrawableState(extraSpace + 1);
            android.view.View.mergeDrawableStates(drawableState, TextView.MULTILINE_STATE_SET);
        }
        if (isTextSelectable()) {
            // Disable pressed state, which was introduced when TextView was made clickable.
            // Prevents text color change.
            // setClickable(false) would have a similar effect, but it also disables focus changes
            // and long press actions, which are both needed by text selection.
            final int length = drawableState.length;
            for (int i = 0; i < length; i++) {
                if (drawableState[i] == R.attr.state_pressed) {
                    final int[] nonPressedState = new int[length - 1];
                    java.lang.System.arraycopy(drawableState, 0, nonPressedState, 0, i);
                    java.lang.System.arraycopy(drawableState, i + 1, nonPressedState, i, (length - i) - 1);
                    return nonPressedState;
                }
            }
        }
        return drawableState;
    }

    @android.annotation.UnsupportedAppUsage
    private android.graphics.Path getUpdatedHighlightPath() {
        android.graphics.Path highlight = null;
        android.graphics.Paint highlightPaint = mHighlightPaint;
        final int selStart = getSelectionStart();
        final int selEnd = getSelectionEnd();
        if (((mMovement != null) && (isFocused() || isPressed())) && (selStart >= 0)) {
            if (selStart == selEnd) {
                if ((mEditor != null) && mEditor.shouldRenderCursor()) {
                    if (mHighlightPathBogus) {
                        if (mHighlightPath == null)
                            mHighlightPath = new android.graphics.Path();

                        mHighlightPath.reset();
                        mLayout.getCursorPath(selStart, mHighlightPath, mText);
                        mEditor.updateCursorPosition();
                        mHighlightPathBogus = false;
                    }
                    // XXX should pass to skin instead of drawing directly
                    highlightPaint.setColor(mCurTextColor);
                    highlightPaint.setStyle(android.graphics.Paint.Style.STROKE);
                    highlight = mHighlightPath;
                }
            } else {
                if (mHighlightPathBogus) {
                    if (mHighlightPath == null)
                        mHighlightPath = new android.graphics.Path();

                    mHighlightPath.reset();
                    mLayout.getSelectionPath(selStart, selEnd, mHighlightPath);
                    mHighlightPathBogus = false;
                }
                // XXX should pass to skin instead of drawing directly
                highlightPaint.setColor(mHighlightColor);
                highlightPaint.setStyle(android.graphics.Paint.Style.FILL);
                highlight = mHighlightPath;
            }
        }
        return highlight;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getHorizontalOffsetForDrawables() {
        return 0;
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        restartMarqueeIfNeeded();
        // Draw the background for this view
        super.onDraw(canvas);
        final int compoundPaddingLeft = getCompoundPaddingLeft();
        final int compoundPaddingTop = getCompoundPaddingTop();
        final int compoundPaddingRight = getCompoundPaddingRight();
        final int compoundPaddingBottom = getCompoundPaddingBottom();
        final int scrollX = mScrollX;
        final int scrollY = mScrollY;
        final int right = mRight;
        final int left = mLeft;
        final int bottom = mBottom;
        final int top = mTop;
        final boolean isLayoutRtl = isLayoutRtl();
        final int offset = getHorizontalOffsetForDrawables();
        final int leftOffset = (isLayoutRtl) ? 0 : offset;
        final int rightOffset = (isLayoutRtl) ? offset : 0;
        final TextView.Drawables dr = mDrawables;
        if (dr != null) {
            /* Compound, not extended, because the icon is not clipped
            if the text height is smaller.
             */
            int vspace = ((bottom - top) - compoundPaddingBottom) - compoundPaddingTop;
            int hspace = ((right - left) - compoundPaddingRight) - compoundPaddingLeft;
            // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
            // Make sure to update invalidateDrawable() when changing this code.
            if (dr.mShowing[TextView.Drawables.LEFT] != null) {
                canvas.save();
                canvas.translate((scrollX + mPaddingLeft) + leftOffset, (scrollY + compoundPaddingTop) + ((vspace - dr.mDrawableHeightLeft) / 2));
                dr.mShowing[TextView.Drawables.LEFT].draw(canvas);
                canvas.restore();
            }
            // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
            // Make sure to update invalidateDrawable() when changing this code.
            if (dr.mShowing[TextView.Drawables.RIGHT] != null) {
                canvas.save();
                canvas.translate(((((scrollX + right) - left) - mPaddingRight) - dr.mDrawableSizeRight) - rightOffset, (scrollY + compoundPaddingTop) + ((vspace - dr.mDrawableHeightRight) / 2));
                dr.mShowing[TextView.Drawables.RIGHT].draw(canvas);
                canvas.restore();
            }
            // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
            // Make sure to update invalidateDrawable() when changing this code.
            if (dr.mShowing[TextView.Drawables.TOP] != null) {
                canvas.save();
                canvas.translate((scrollX + compoundPaddingLeft) + ((hspace - dr.mDrawableWidthTop) / 2), scrollY + mPaddingTop);
                dr.mShowing[TextView.Drawables.TOP].draw(canvas);
                canvas.restore();
            }
            // IMPORTANT: The coordinates computed are also used in invalidateDrawable()
            // Make sure to update invalidateDrawable() when changing this code.
            if (dr.mShowing[TextView.Drawables.BOTTOM] != null) {
                canvas.save();
                canvas.translate((scrollX + compoundPaddingLeft) + ((hspace - dr.mDrawableWidthBottom) / 2), (((scrollY + bottom) - top) - mPaddingBottom) - dr.mDrawableSizeBottom);
                dr.mShowing[TextView.Drawables.BOTTOM].draw(canvas);
                canvas.restore();
            }
        }
        int color = mCurTextColor;
        if (mLayout == null) {
            assumeLayout();
        }
        android.text.Layout layout = mLayout;
        if ((mHint != null) && (mText.length() == 0)) {
            if (mHintTextColor != null) {
                color = mCurHintTextColor;
            }
            layout = mHintLayout;
        }
        mTextPaint.setColor(color);
        mTextPaint.drawableState = getDrawableState();
        canvas.save();
        /* Would be faster if we didn't have to do this. Can we chop the
        (displayable) text so that we don't need to do this ever?
         */
        int extendedPaddingTop = getExtendedPaddingTop();
        int extendedPaddingBottom = getExtendedPaddingBottom();
        final int vspace = ((mBottom - mTop) - compoundPaddingBottom) - compoundPaddingTop;
        final int maxScrollY = mLayout.getHeight() - vspace;
        float clipLeft = compoundPaddingLeft + scrollX;
        float clipTop = (scrollY == 0) ? 0 : extendedPaddingTop + scrollY;
        float clipRight = ((right - left) - getCompoundPaddingRight()) + scrollX;
        float clipBottom = ((bottom - top) + scrollY) - (scrollY == maxScrollY ? 0 : extendedPaddingBottom);
        if (mShadowRadius != 0) {
            clipLeft += java.lang.Math.min(0, mShadowDx - mShadowRadius);
            clipRight += java.lang.Math.max(0, mShadowDx + mShadowRadius);
            clipTop += java.lang.Math.min(0, mShadowDy - mShadowRadius);
            clipBottom += java.lang.Math.max(0, mShadowDy + mShadowRadius);
        }
        canvas.clipRect(clipLeft, clipTop, clipRight, clipBottom);
        int voffsetText = 0;
        int voffsetCursor = 0;
        // translate in by our padding
        /* shortcircuit calling getVerticaOffset() */
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != android.view.Gravity.TOP) {
            voffsetText = getVerticalOffset(false);
            voffsetCursor = getVerticalOffset(true);
        }
        canvas.translate(compoundPaddingLeft, extendedPaddingTop + voffsetText);
        final int layoutDirection = getLayoutDirection();
        final int absoluteGravity = android.view.Gravity.getAbsoluteGravity(mGravity, layoutDirection);
        if (isMarqueeFadeEnabled()) {
            if ((((!mSingleLine) && (getLineCount() == 1)) && canMarquee()) && ((absoluteGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) != android.view.Gravity.LEFT)) {
                final int width = mRight - mLeft;
                final int padding = getCompoundPaddingLeft() + getCompoundPaddingRight();
                final float dx = mLayout.getLineRight(0) - (width - padding);
                canvas.translate(layout.getParagraphDirection(0) * dx, 0.0F);
            }
            if ((mMarquee != null) && mMarquee.isRunning()) {
                final float dx = -mMarquee.getScroll();
                canvas.translate(layout.getParagraphDirection(0) * dx, 0.0F);
            }
        }
        final int cursorOffsetVertical = voffsetCursor - voffsetText;
        android.graphics.Path highlight = getUpdatedHighlightPath();
        if (mEditor != null) {
            mEditor.onDraw(canvas, layout, highlight, mHighlightPaint, cursorOffsetVertical);
        } else {
            layout.draw(canvas, highlight, mHighlightPaint, cursorOffsetVertical);
        }
        if ((mMarquee != null) && mMarquee.shouldDrawGhost()) {
            final float dx = mMarquee.getGhostOffset();
            canvas.translate(layout.getParagraphDirection(0) * dx, 0.0F);
            layout.draw(canvas, highlight, mHighlightPaint, cursorOffsetVertical);
        }
        canvas.restore();
    }

    @java.lang.Override
    public void getFocusedRect(android.graphics.Rect r) {
        if (mLayout == null) {
            super.getFocusedRect(r);
            return;
        }
        int selEnd = getSelectionEnd();
        if (selEnd < 0) {
            super.getFocusedRect(r);
            return;
        }
        int selStart = getSelectionStart();
        if ((selStart < 0) || (selStart >= selEnd)) {
            int line = mLayout.getLineForOffset(selEnd);
            r.top = mLayout.getLineTop(line);
            r.bottom = mLayout.getLineBottom(line);
            r.left = ((int) (mLayout.getPrimaryHorizontal(selEnd))) - 2;
            r.right = r.left + 4;
        } else {
            int lineStart = mLayout.getLineForOffset(selStart);
            int lineEnd = mLayout.getLineForOffset(selEnd);
            r.top = mLayout.getLineTop(lineStart);
            r.bottom = mLayout.getLineBottom(lineEnd);
            if (lineStart == lineEnd) {
                r.left = ((int) (mLayout.getPrimaryHorizontal(selStart)));
                r.right = ((int) (mLayout.getPrimaryHorizontal(selEnd)));
            } else {
                // Selection extends across multiple lines -- make the focused
                // rect cover the entire width.
                if (mHighlightPathBogus) {
                    if (mHighlightPath == null)
                        mHighlightPath = new android.graphics.Path();

                    mHighlightPath.reset();
                    mLayout.getSelectionPath(selStart, selEnd, mHighlightPath);
                    mHighlightPathBogus = false;
                }
                synchronized(TextView.TEMP_RECTF) {
                    mHighlightPath.computeBounds(TextView.TEMP_RECTF, true);
                    r.left = ((int) (TextView.TEMP_RECTF.left)) - 1;
                    r.right = ((int) (TextView.TEMP_RECTF.right)) + 1;
                }
            }
        }
        // Adjust for padding and gravity.
        int paddingLeft = getCompoundPaddingLeft();
        int paddingTop = getExtendedPaddingTop();
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != android.view.Gravity.TOP) {
            paddingTop += getVerticalOffset(false);
        }
        r.offset(paddingLeft, paddingTop);
        int paddingBottom = getExtendedPaddingBottom();
        r.bottom += paddingBottom;
    }

    /**
     * Return the number of lines of text, or 0 if the internal Layout has not
     * been built.
     */
    public int getLineCount() {
        return mLayout != null ? mLayout.getLineCount() : 0;
    }

    /**
     * Return the baseline for the specified line (0...getLineCount() - 1)
     * If bounds is not null, return the top, left, right, bottom extents
     * of the specified line in it. If the internal Layout has not been built,
     * return 0 and set bounds to (0, 0, 0, 0)
     *
     * @param line
     * 		which line to examine (0..getLineCount() - 1)
     * @param bounds
     * 		Optional. If not null, it returns the extent of the line
     * @return the Y-coordinate of the baseline
     */
    public int getLineBounds(int line, android.graphics.Rect bounds) {
        if (mLayout == null) {
            if (bounds != null) {
                bounds.set(0, 0, 0, 0);
            }
            return 0;
        } else {
            int baseline = mLayout.getLineBounds(line, bounds);
            int voffset = getExtendedPaddingTop();
            if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != android.view.Gravity.TOP) {
                voffset += getVerticalOffset(true);
            }
            if (bounds != null) {
                bounds.offset(getCompoundPaddingLeft(), voffset);
            }
            return baseline + voffset;
        }
    }

    @java.lang.Override
    public int getBaseline() {
        if (mLayout == null) {
            return super.getBaseline();
        }
        return getBaselineOffset() + mLayout.getLineBaseline(0);
    }

    int getBaselineOffset() {
        int voffset = 0;
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != android.view.Gravity.TOP) {
            voffset = getVerticalOffset(true);
        }
        if (android.view.View.isLayoutModeOptical(mParent)) {
            voffset -= getOpticalInsets().top;
        }
        return getExtendedPaddingTop() + voffset;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected int getFadeTop(boolean offsetRequired) {
        if (mLayout == null)
            return 0;

        int voffset = 0;
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != android.view.Gravity.TOP) {
            voffset = getVerticalOffset(true);
        }
        if (offsetRequired)
            voffset += getTopPaddingOffset();

        return getExtendedPaddingTop() + voffset;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected int getFadeHeight(boolean offsetRequired) {
        return mLayout != null ? mLayout.getHeight() : 0;
    }

    @java.lang.Override
    public android.view.PointerIcon onResolvePointerIcon(android.view.MotionEvent event, int pointerIndex) {
        if ((mSpannable != null) && mLinksClickable) {
            final float x = event.getX(pointerIndex);
            final float y = event.getY(pointerIndex);
            final int offset = getOffsetForPosition(x, y);
            final android.text.style.ClickableSpan[] clickables = mSpannable.getSpans(offset, offset, android.text.style.ClickableSpan.class);
            if (clickables.length > 0) {
                return android.view.PointerIcon.getSystemIcon(mContext, android.view.PointerIcon.TYPE_HAND);
            }
        }
        if (isTextSelectable() || isTextEditable()) {
            return android.view.PointerIcon.getSystemIcon(mContext, android.view.PointerIcon.TYPE_TEXT);
        }
        return super.onResolvePointerIcon(event, pointerIndex);
    }

    @java.lang.Override
    public boolean onKeyPreIme(int keyCode, android.view.KeyEvent event) {
        // Note: If the IME is in fullscreen mode and IMS#mExtractEditText is in text action mode,
        // InputMethodService#onKeyDown and InputMethodService#onKeyUp are responsible to call
        // InputMethodService#mExtractEditText.maybeHandleBackInTextActionMode(event).
        if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) && handleBackInTextActionModeIfNeeded(event)) {
            return true;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean handleBackInTextActionModeIfNeeded(android.view.KeyEvent event) {
        // Do nothing unless mEditor is in text action mode.
        if ((mEditor == null) || (mEditor.getTextActionMode() == null)) {
            return false;
        }
        if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getRepeatCount() == 0)) {
            android.view.KeyEvent.DispatcherState state = getKeyDispatcherState();
            if (state != null) {
                state.startTracking(event, this);
            }
            return true;
        } else
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                android.view.KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.handleUpEvent(event);
                }
                if (event.isTracking() && (!event.isCanceled())) {
                    stopTextActionMode();
                    return true;
                }
            }

        return false;
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        final int which = doKeyDown(keyCode, event, null);
        if (which == TextView.KEY_EVENT_NOT_HANDLED) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }

    @java.lang.Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, android.view.KeyEvent event) {
        android.view.KeyEvent down = android.view.KeyEvent.changeAction(event, android.view.KeyEvent.ACTION_DOWN);
        final int which = doKeyDown(keyCode, down, event);
        if (which == TextView.KEY_EVENT_NOT_HANDLED) {
            // Go through default dispatching.
            return super.onKeyMultiple(keyCode, repeatCount, event);
        }
        if (which == TextView.KEY_EVENT_HANDLED) {
            // Consumed the whole thing.
            return true;
        }
        repeatCount--;
        // We are going to dispatch the remaining events to either the input
        // or movement method.  To do this, we will just send a repeated stream
        // of down and up events until we have done the complete repeatCount.
        // It would be nice if those interfaces had an onKeyMultiple() method,
        // but adding that is a more complicated change.
        android.view.KeyEvent up = android.view.KeyEvent.changeAction(event, android.view.KeyEvent.ACTION_UP);
        if (which == TextView.KEY_DOWN_HANDLED_BY_KEY_LISTENER) {
            // mEditor and mEditor.mInput are not null from doKeyDown
            mEditor.mKeyListener.onKeyUp(this, ((android.text.Editable) (mText)), keyCode, up);
            while ((--repeatCount) > 0) {
                mEditor.mKeyListener.onKeyDown(this, ((android.text.Editable) (mText)), keyCode, down);
                mEditor.mKeyListener.onKeyUp(this, ((android.text.Editable) (mText)), keyCode, up);
            } 
            hideErrorIfUnchanged();
        } else
            if (which == TextView.KEY_DOWN_HANDLED_BY_MOVEMENT_METHOD) {
                // mMovement is not null from doKeyDown
                mMovement.onKeyUp(this, mSpannable, keyCode, up);
                while ((--repeatCount) > 0) {
                    mMovement.onKeyDown(this, mSpannable, keyCode, down);
                    mMovement.onKeyUp(this, mSpannable, keyCode, up);
                } 
            }

        return true;
    }

    /**
     * Returns true if pressing ENTER in this field advances focus instead
     * of inserting the character.  This is true mostly in single-line fields,
     * but also in mail addresses and subjects which will display on multiple
     * lines but where it doesn't make sense to insert newlines.
     */
    private boolean shouldAdvanceFocusOnEnter() {
        if (getKeyListener() == null) {
            return false;
        }
        if (mSingleLine) {
            return true;
        }
        if ((mEditor != null) && ((mEditor.mInputType & TYPE_MASK_CLASS) == TYPE_CLASS_TEXT)) {
            int variation = mEditor.mInputType & TYPE_MASK_VARIATION;
            if ((variation == TYPE_TEXT_VARIATION_EMAIL_ADDRESS) || (variation == TYPE_TEXT_VARIATION_EMAIL_SUBJECT)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if pressing TAB in this field advances focus instead
     * of inserting the character.  Insert tabs only in multi-line editors.
     */
    private boolean shouldAdvanceFocusOnTab() {
        if ((((getKeyListener() != null) && (!mSingleLine)) && (mEditor != null)) && ((mEditor.mInputType & TYPE_MASK_CLASS) == TYPE_CLASS_TEXT)) {
            int variation = mEditor.mInputType & TYPE_MASK_VARIATION;
            if ((variation == TYPE_TEXT_FLAG_IME_MULTI_LINE) || (variation == TYPE_TEXT_FLAG_MULTI_LINE)) {
                return false;
            }
        }
        return true;
    }

    private boolean isDirectionalNavigationKey(int keyCode) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
                return true;
        }
        return false;
    }

    private int doKeyDown(int keyCode, android.view.KeyEvent event, android.view.KeyEvent otherEvent) {
        if (!isEnabled()) {
            return TextView.KEY_EVENT_NOT_HANDLED;
        }
        // If this is the initial keydown, we don't want to prevent a movement away from this view.
        // While this shouldn't be necessary because any time we're preventing default movement we
        // should be restricting the focus to remain within this view, thus we'll also receive
        // the key up event, occasionally key up events will get dropped and we don't want to
        // prevent the user from traversing out of this on the next key down.
        if ((event.getRepeatCount() == 0) && (!android.view.KeyEvent.isModifierKey(keyCode))) {
            mPreventDefaultMovement = false;
        }
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_ENTER :
                if (event.hasNoModifiers()) {
                    // When mInputContentType is set, we know that we are
                    // running in a "modern" cupcake environment, so don't need
                    // to worry about the application trying to capture
                    // enter key events.
                    if ((mEditor != null) && (mEditor.mInputContentType != null)) {
                        // If there is an action listener, given them a
                        // chance to consume the event.
                        if ((mEditor.mInputContentType.onEditorActionListener != null) && mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, android.view.inputmethod.EditorInfo.IME_NULL, event)) {
                            mEditor.mInputContentType.enterDown = true;
                            // We are consuming the enter key for them.
                            return TextView.KEY_EVENT_HANDLED;
                        }
                    }
                    // If our editor should move focus when enter is pressed, or
                    // this is a generated event from an IME action button, then
                    // don't let it be inserted into the text.
                    if (((event.getFlags() & android.view.KeyEvent.FLAG_EDITOR_ACTION) != 0) || shouldAdvanceFocusOnEnter()) {
                        if (hasOnClickListeners()) {
                            return TextView.KEY_EVENT_NOT_HANDLED;
                        }
                        return TextView.KEY_EVENT_HANDLED;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                if (event.hasNoModifiers()) {
                    if (shouldAdvanceFocusOnEnter()) {
                        return TextView.KEY_EVENT_NOT_HANDLED;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_TAB :
                if (event.hasNoModifiers() || event.hasModifiers(android.view.KeyEvent.META_SHIFT_ON)) {
                    if (shouldAdvanceFocusOnTab()) {
                        return TextView.KEY_EVENT_NOT_HANDLED;
                    }
                }
                break;
                // Has to be done on key down (and not on key up) to correctly be intercepted.
            case android.view.KeyEvent.KEYCODE_BACK :
                if ((mEditor != null) && (mEditor.getTextActionMode() != null)) {
                    stopTextActionMode();
                    return TextView.KEY_EVENT_HANDLED;
                }
                break;
            case android.view.KeyEvent.KEYCODE_CUT :
                if (event.hasNoModifiers() && canCut()) {
                    if (onTextContextMenuItem(TextView.ID_CUT)) {
                        return TextView.KEY_EVENT_HANDLED;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_COPY :
                if (event.hasNoModifiers() && canCopy()) {
                    if (onTextContextMenuItem(TextView.ID_COPY)) {
                        return TextView.KEY_EVENT_HANDLED;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_PASTE :
                if (event.hasNoModifiers() && canPaste()) {
                    if (onTextContextMenuItem(TextView.ID_PASTE)) {
                        return TextView.KEY_EVENT_HANDLED;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_FORWARD_DEL :
                if (event.hasModifiers(android.view.KeyEvent.META_SHIFT_ON) && canCut()) {
                    if (onTextContextMenuItem(TextView.ID_CUT)) {
                        return TextView.KEY_EVENT_HANDLED;
                    }
                }
                break;
            case android.view.KeyEvent.KEYCODE_INSERT :
                if (event.hasModifiers(android.view.KeyEvent.META_CTRL_ON) && canCopy()) {
                    if (onTextContextMenuItem(TextView.ID_COPY)) {
                        return TextView.KEY_EVENT_HANDLED;
                    }
                } else
                    if (event.hasModifiers(android.view.KeyEvent.META_SHIFT_ON) && canPaste()) {
                        if (onTextContextMenuItem(TextView.ID_PASTE)) {
                            return TextView.KEY_EVENT_HANDLED;
                        }
                    }

                break;
        }
        if ((mEditor != null) && (mEditor.mKeyListener != null)) {
            boolean doDown = true;
            if (otherEvent != null) {
                try {
                    beginBatchEdit();
                    final boolean handled = mEditor.mKeyListener.onKeyOther(this, ((android.text.Editable) (mText)), otherEvent);
                    hideErrorIfUnchanged();
                    doDown = false;
                    if (handled) {
                        return TextView.KEY_EVENT_HANDLED;
                    }
                } catch (java.lang.AbstractMethodError e) {
                    // onKeyOther was added after 1.0, so if it isn't
                    // implemented we need to try to dispatch as a regular down.
                } finally {
                    endBatchEdit();
                }
            }
            if (doDown) {
                beginBatchEdit();
                final boolean handled = mEditor.mKeyListener.onKeyDown(this, ((android.text.Editable) (mText)), keyCode, event);
                endBatchEdit();
                hideErrorIfUnchanged();
                if (handled)
                    return TextView.KEY_DOWN_HANDLED_BY_KEY_LISTENER;

            }
        }
        // bug 650865: sometimes we get a key event before a layout.
        // don't try to move around if we don't know the layout.
        if ((mMovement != null) && (mLayout != null)) {
            boolean doDown = true;
            if (otherEvent != null) {
                try {
                    boolean handled = mMovement.onKeyOther(this, mSpannable, otherEvent);
                    doDown = false;
                    if (handled) {
                        return TextView.KEY_EVENT_HANDLED;
                    }
                } catch (java.lang.AbstractMethodError e) {
                    // onKeyOther was added after 1.0, so if it isn't
                    // implemented we need to try to dispatch as a regular down.
                }
            }
            if (doDown) {
                if (mMovement.onKeyDown(this, mSpannable, keyCode, event)) {
                    if ((event.getRepeatCount() == 0) && (!android.view.KeyEvent.isModifierKey(keyCode))) {
                        mPreventDefaultMovement = true;
                    }
                    return TextView.KEY_DOWN_HANDLED_BY_MOVEMENT_METHOD;
                }
            }
            // Consume arrows from keyboard devices to prevent focus leaving the editor.
            // DPAD/JOY devices (Gamepads, TV remotes) often lack a TAB key so allow those
            // to move focus with arrows.
            if ((event.getSource() == android.view.InputDevice.SOURCE_KEYBOARD) && isDirectionalNavigationKey(keyCode)) {
                return TextView.KEY_EVENT_HANDLED;
            }
        }
        return mPreventDefaultMovement && (!android.view.KeyEvent.isModifierKey(keyCode)) ? TextView.KEY_EVENT_HANDLED : TextView.KEY_EVENT_NOT_HANDLED;
    }

    /**
     * Resets the mErrorWasChanged flag, so that future calls to {@link #setError(CharSequence)}
     * can be recorded.
     *
     * @unknown 
     */
    public void resetErrorChangedFlag() {
        /* Keep track of what the error was before doing the input
        so that if an input filter changed the error, we leave
        that error showing.  Otherwise, we take down whatever
        error was showing when the user types something.
         */
        if (mEditor != null)
            mEditor.mErrorWasChanged = false;

    }

    /**
     *
     *
     * @unknown 
     */
    public void hideErrorIfUnchanged() {
        if (((mEditor != null) && (mEditor.mError != null)) && (!mEditor.mErrorWasChanged)) {
            setError(null, null);
        }
    }

    @java.lang.Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (!isEnabled()) {
            return super.onKeyUp(keyCode, event);
        }
        if (!android.view.KeyEvent.isModifierKey(keyCode)) {
            mPreventDefaultMovement = false;
        }
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
                if (event.hasNoModifiers()) {
                    /* If there is a click listener, just call through to
                    super, which will invoke it.

                    If there isn't a click listener, try to show the soft
                    input method.  (It will also
                    call performClick(), but that won't do anything in
                    this case.)
                     */
                    if (!hasOnClickListeners()) {
                        if ((((mMovement != null) && (mText instanceof android.text.Editable)) && (mLayout != null)) && onCheckIsTextEditor()) {
                            android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
                            viewClicked(imm);
                            if ((imm != null) && getShowSoftInputOnFocus()) {
                                imm.showSoftInput(this, 0);
                            }
                        }
                    }
                }
                return super.onKeyUp(keyCode, event);
            case android.view.KeyEvent.KEYCODE_ENTER :
                if (event.hasNoModifiers()) {
                    if ((((mEditor != null) && (mEditor.mInputContentType != null)) && (mEditor.mInputContentType.onEditorActionListener != null)) && mEditor.mInputContentType.enterDown) {
                        mEditor.mInputContentType.enterDown = false;
                        if (mEditor.mInputContentType.onEditorActionListener.onEditorAction(this, android.view.inputmethod.EditorInfo.IME_NULL, event)) {
                            return true;
                        }
                    }
                    if (((event.getFlags() & android.view.KeyEvent.FLAG_EDITOR_ACTION) != 0) || shouldAdvanceFocusOnEnter()) {
                        /* If there is a click listener, just call through to
                        super, which will invoke it.

                        If there isn't a click listener, try to advance focus,
                        but still call through to super, which will reset the
                        pressed state and longpress state.  (It will also
                        call performClick(), but that won't do anything in
                        this case.)
                         */
                        if (!hasOnClickListeners()) {
                            android.view.View v = focusSearch(android.view.View.FOCUS_DOWN);
                            if (v != null) {
                                if (!v.requestFocus(android.view.View.FOCUS_DOWN)) {
                                    throw new java.lang.IllegalStateException("focus search returned a view " + "that wasn't able to take focus!");
                                }
                                /* Return true because we handled the key; super
                                will return false because there was no click
                                listener.
                                 */
                                super.onKeyUp(keyCode, event);
                                return true;
                            } else
                                if ((event.getFlags() & android.view.KeyEvent.FLAG_EDITOR_ACTION) != 0) {
                                    // No target for next focus, but make sure the IME
                                    // if this came from it.
                                    android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
                                    if ((imm != null) && imm.isActive(this)) {
                                        imm.hideSoftInputFromWindow(getWindowToken(), 0);
                                    }
                                }

                        }
                    }
                    return super.onKeyUp(keyCode, event);
                }
                break;
        }
        if ((mEditor != null) && (mEditor.mKeyListener != null)) {
            if (mEditor.mKeyListener.onKeyUp(this, ((android.text.Editable) (mText)), keyCode, event)) {
                return true;
            }
        }
        if ((mMovement != null) && (mLayout != null)) {
            if (mMovement.onKeyUp(this, mSpannable, keyCode, event)) {
                return true;
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @java.lang.Override
    public boolean onCheckIsTextEditor() {
        return (mEditor != null) && (mEditor.mInputType != TYPE_NULL);
    }

    @java.lang.Override
    public android.view.inputmethod.InputConnection onCreateInputConnection(android.view.inputmethod.EditorInfo outAttrs) {
        if (onCheckIsTextEditor() && isEnabled()) {
            mEditor.createInputMethodStateIfNeeded();
            outAttrs.inputType = getInputType();
            if (mEditor.mInputContentType != null) {
                outAttrs.imeOptions = mEditor.mInputContentType.imeOptions;
                outAttrs.privateImeOptions = mEditor.mInputContentType.privateImeOptions;
                outAttrs.actionLabel = mEditor.mInputContentType.imeActionLabel;
                outAttrs.actionId = mEditor.mInputContentType.imeActionId;
                outAttrs.extras = mEditor.mInputContentType.extras;
                outAttrs.hintLocales = mEditor.mInputContentType.imeHintLocales;
            } else {
                outAttrs.imeOptions = android.view.inputmethod.EditorInfo.IME_NULL;
                outAttrs.hintLocales = null;
            }
            if (focusSearch(android.view.View.FOCUS_DOWN) != null) {
                outAttrs.imeOptions |= android.view.inputmethod.EditorInfo.IME_FLAG_NAVIGATE_NEXT;
            }
            if (focusSearch(android.view.View.FOCUS_UP) != null) {
                outAttrs.imeOptions |= android.view.inputmethod.EditorInfo.IME_FLAG_NAVIGATE_PREVIOUS;
            }
            if ((outAttrs.imeOptions & android.view.inputmethod.EditorInfo.IME_MASK_ACTION) == android.view.inputmethod.EditorInfo.IME_ACTION_UNSPECIFIED) {
                if ((outAttrs.imeOptions & android.view.inputmethod.EditorInfo.IME_FLAG_NAVIGATE_NEXT) != 0) {
                    // An action has not been set, but the enter key will move to
                    // the next focus, so set the action to that.
                    outAttrs.imeOptions |= android.view.inputmethod.EditorInfo.IME_ACTION_NEXT;
                } else {
                    // An action has not been set, and there is no focus to move
                    // to, so let's just supply a "done" action.
                    outAttrs.imeOptions |= android.view.inputmethod.EditorInfo.IME_ACTION_DONE;
                }
                if (!shouldAdvanceFocusOnEnter()) {
                    outAttrs.imeOptions |= android.view.inputmethod.EditorInfo.IME_FLAG_NO_ENTER_ACTION;
                }
            }
            if (TextView.isMultilineInputType(outAttrs.inputType)) {
                // Multi-line text editors should always show an enter key.
                outAttrs.imeOptions |= android.view.inputmethod.EditorInfo.IME_FLAG_NO_ENTER_ACTION;
            }
            outAttrs.hintText = mHint;
            outAttrs.targetInputMethodUser = mTextOperationUser;
            if (mText instanceof android.text.Editable) {
                android.view.inputmethod.InputConnection ic = new com.android.internal.widget.EditableInputConnection(this);
                outAttrs.initialSelStart = getSelectionStart();
                outAttrs.initialSelEnd = getSelectionEnd();
                outAttrs.initialCapsMode = ic.getCursorCapsMode(getInputType());
                return ic;
            }
        }
        return null;
    }

    /**
     * If this TextView contains editable content, extract a portion of it
     * based on the information in <var>request</var> in to <var>outText</var>.
     *
     * @return Returns true if the text was successfully extracted, else false.
     */
    public boolean extractText(android.view.inputmethod.ExtractedTextRequest request, android.view.inputmethod.ExtractedText outText) {
        createEditorIfNeeded();
        return mEditor.extractText(request, outText);
    }

    /**
     * This is used to remove all style-impacting spans from text before new
     * extracted text is being replaced into it, so that we don't have any
     * lingering spans applied during the replace.
     */
    static void removeParcelableSpans(android.text.Spannable spannable, int start, int end) {
        java.lang.Object[] spans = spannable.getSpans(start, end, android.text.ParcelableSpan.class);
        int i = spans.length;
        while (i > 0) {
            i--;
            spannable.removeSpan(spans[i]);
        } 
    }

    /**
     * Apply to this text view the given extracted text, as previously
     * returned by {@link #extractText(ExtractedTextRequest, ExtractedText)}.
     */
    public void setExtractedText(android.view.inputmethod.ExtractedText text) {
        android.text.Editable content = getEditableText();
        if (text.text != null) {
            if (content == null) {
                setText(text.text, TextView.BufferType.EDITABLE);
            } else {
                int start = 0;
                int end = content.length();
                if (text.partialStartOffset >= 0) {
                    final int N = content.length();
                    start = text.partialStartOffset;
                    if (start > N)
                        start = N;

                    end = text.partialEndOffset;
                    if (end > N)
                        end = N;

                }
                TextView.removeParcelableSpans(content, start, end);
                if (android.text.TextUtils.equals(content.subSequence(start, end), text.text)) {
                    if (text.text instanceof android.text.Spanned) {
                        // OK to copy spans only.
                        android.text.TextUtils.copySpansFrom(((android.text.Spanned) (text.text)), 0, end - start, java.lang.Object.class, content, start);
                    }
                } else {
                    content.replace(start, end, text.text);
                }
            }
        }
        // Now set the selection position...  make sure it is in range, to
        // avoid crashes.  If this is a partial update, it is possible that
        // the underlying text may have changed, causing us problems here.
        // Also we just don't want to trust clients to do the right thing.
        android.text.Spannable sp = ((android.text.Spannable) (getText()));
        final int N = sp.length();
        int start = text.selectionStart;
        if (start < 0) {
            start = 0;
        } else
            if (start > N) {
                start = N;
            }

        int end = text.selectionEnd;
        if (end < 0) {
            end = 0;
        } else
            if (end > N) {
                end = N;
            }

        android.text.Selection.setSelection(sp, start, end);
        // Finally, update the selection mode.
        if ((text.flags & android.view.inputmethod.ExtractedText.FLAG_SELECTING) != 0) {
            android.text.method.MetaKeyKeyListener.startSelecting(this, sp);
        } else {
            android.text.method.MetaKeyKeyListener.stopSelecting(this, sp);
        }
        setHintInternal(text.hint);
    }

    /**
     *
     *
     * @unknown 
     */
    public void setExtracting(android.view.inputmethod.ExtractedTextRequest req) {
        if (mEditor.mInputMethodState != null) {
            mEditor.mInputMethodState.mExtractedTextRequest = req;
        }
        // This would stop a possible selection mode, but no such mode is started in case
        // extracted mode will start. Some text is selected though, and will trigger an action mode
        // in the extracted view.
        mEditor.hideCursorAndSpanControllers();
        stopTextActionMode();
        if (mEditor.mSelectionModifierCursorController != null) {
            mEditor.mSelectionModifierCursorController.resetTouchOffsets();
        }
    }

    /**
     * Called by the framework in response to a text completion from
     * the current input method, provided by it calling
     * {@link InputConnection#commitCompletion
     * InputConnection.commitCompletion()}.  The default implementation does
     * nothing; text views that are supporting auto-completion should override
     * this to do their desired behavior.
     *
     * @param text
     * 		The auto complete text the user has selected.
     */
    public void onCommitCompletion(android.view.inputmethod.CompletionInfo text) {
        // intentionally empty
    }

    /**
     * Called by the framework in response to a text auto-correction (such as fixing a typo using a
     * dictionary) from the current input method, provided by it calling
     * {@link InputConnection#commitCorrection(CorrectionInfo) InputConnection.commitCorrection()}.
     * The default implementation flashes the background of the corrected word to provide
     * feedback to the user.
     *
     * @param info
     * 		The auto correct info about the text that was corrected.
     */
    public void onCommitCorrection(android.view.inputmethod.CorrectionInfo info) {
        if (mEditor != null)
            mEditor.onCommitCorrection(info);

    }

    public void beginBatchEdit() {
        if (mEditor != null)
            mEditor.beginBatchEdit();

    }

    public void endBatchEdit() {
        if (mEditor != null)
            mEditor.endBatchEdit();

    }

    /**
     * Called by the framework in response to a request to begin a batch
     * of edit operations through a call to link {@link #beginBatchEdit()}.
     */
    public void onBeginBatchEdit() {
        // intentionally empty
    }

    /**
     * Called by the framework in response to a request to end a batch
     * of edit operations through a call to link {@link #endBatchEdit}.
     */
    public void onEndBatchEdit() {
        // intentionally empty
    }

    /**
     * Called by the framework in response to a private command from the
     * current method, provided by it calling
     * {@link InputConnection#performPrivateCommand
     * InputConnection.performPrivateCommand()}.
     *
     * @param action
     * 		The action name of the command.
     * @param data
     * 		Any additional data for the command.  This may be null.
     * @return Return true if you handled the command, else false.
     */
    public boolean onPrivateIMECommand(java.lang.String action, android.os.Bundle data) {
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.UnsupportedAppUsage
    public void nullLayouts() {
        if ((mLayout instanceof android.text.BoringLayout) && (mSavedLayout == null)) {
            mSavedLayout = ((android.text.BoringLayout) (mLayout));
        }
        if ((mHintLayout instanceof android.text.BoringLayout) && (mSavedHintLayout == null)) {
            mSavedHintLayout = ((android.text.BoringLayout) (mHintLayout));
        }
        mSavedMarqueeModeLayout = mLayout = mHintLayout = null;
        mBoring = mHintBoring = null;
        // Since it depends on the value of mLayout
        if (mEditor != null)
            mEditor.prepareCursorControllers();

    }

    /**
     * Make a new Layout based on the already-measured size of the view,
     * on the assumption that it was measured correctly at some point.
     */
    @android.annotation.UnsupportedAppUsage
    private void assumeLayout() {
        int width = ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        if (width < 1) {
            width = 0;
        }
        int physicalWidth = width;
        if (mHorizontallyScrolling) {
            width = TextView.VERY_WIDE;
        }
        makeNewLayout(width, physicalWidth, TextView.UNKNOWN_BORING, TextView.UNKNOWN_BORING, physicalWidth, false);
    }

    @android.annotation.UnsupportedAppUsage
    private Layout.Alignment getLayoutAlignment() {
        android.text.Layout.Alignment alignment;
        switch (getTextAlignment()) {
            case android.view.View.TEXT_ALIGNMENT_GRAVITY :
                switch (mGravity & android.view.Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK) {
                    case android.view.Gravity.START :
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                    case android.view.Gravity.END :
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                        break;
                    case android.view.Gravity.LEFT :
                        alignment = Layout.Alignment.ALIGN_LEFT;
                        break;
                    case android.view.Gravity.RIGHT :
                        alignment = Layout.Alignment.ALIGN_RIGHT;
                        break;
                    case android.view.Gravity.CENTER_HORIZONTAL :
                        alignment = Layout.Alignment.ALIGN_CENTER;
                        break;
                    default :
                        alignment = Layout.Alignment.ALIGN_NORMAL;
                        break;
                }
                break;
            case android.view.View.TEXT_ALIGNMENT_TEXT_START :
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
            case android.view.View.TEXT_ALIGNMENT_TEXT_END :
                alignment = Layout.Alignment.ALIGN_OPPOSITE;
                break;
            case android.view.View.TEXT_ALIGNMENT_CENTER :
                alignment = Layout.Alignment.ALIGN_CENTER;
                break;
            case android.view.View.TEXT_ALIGNMENT_VIEW_START :
                alignment = (getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL) ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
                break;
            case android.view.View.TEXT_ALIGNMENT_VIEW_END :
                alignment = (getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL) ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
                break;
            case android.view.View.TEXT_ALIGNMENT_INHERIT :
                // This should never happen as we have already resolved the text alignment
                // but better safe than sorry so we just fall through
            default :
                alignment = Layout.Alignment.ALIGN_NORMAL;
                break;
        }
        return alignment;
    }

    /**
     * The width passed in is now the desired layout width,
     * not the full view width with padding.
     * {@hide }
     */
    @com.android.internal.annotations.VisibleForTesting
    @android.annotation.UnsupportedAppUsage
    public void makeNewLayout(int wantWidth, int hintWidth, android.text.BoringLayout.Metrics boring, android.text.BoringLayout.Metrics hintBoring, int ellipsisWidth, boolean bringIntoView) {
        stopMarquee();
        // Update "old" cached values
        mOldMaximum = mMaximum;
        mOldMaxMode = mMaxMode;
        mHighlightPathBogus = true;
        if (wantWidth < 0) {
            wantWidth = 0;
        }
        if (hintWidth < 0) {
            hintWidth = 0;
        }
        android.text.Layout.Alignment alignment = getLayoutAlignment();
        final boolean testDirChange = (mSingleLine && (mLayout != null)) && ((alignment == Layout.Alignment.ALIGN_NORMAL) || (alignment == Layout.Alignment.ALIGN_OPPOSITE));
        int oldDir = 0;
        if (testDirChange)
            oldDir = mLayout.getParagraphDirection(0);

        boolean shouldEllipsize = (mEllipsize != null) && (getKeyListener() == null);
        final boolean switchEllipsize = (mEllipsize == android.text.TextUtils.TruncateAt.MARQUEE) && (mMarqueeFadeMode != TextView.MARQUEE_FADE_NORMAL);
        android.text.TextUtils.TruncateAt effectiveEllipsize = mEllipsize;
        if ((mEllipsize == android.text.TextUtils.TruncateAt.MARQUEE) && (mMarqueeFadeMode == TextView.MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS)) {
            effectiveEllipsize = android.text.TextUtils.TruncateAt.END_SMALL;
        }
        if (mTextDir == null) {
            mTextDir = getTextDirectionHeuristic();
        }
        mLayout = makeSingleLayout(wantWidth, boring, ellipsisWidth, alignment, shouldEllipsize, effectiveEllipsize, effectiveEllipsize == mEllipsize);
        if (switchEllipsize) {
            android.text.TextUtils.TruncateAt oppositeEllipsize = (effectiveEllipsize == android.text.TextUtils.TruncateAt.MARQUEE) ? android.text.TextUtils.TruncateAt.END : android.text.TextUtils.TruncateAt.MARQUEE;
            mSavedMarqueeModeLayout = makeSingleLayout(wantWidth, boring, ellipsisWidth, alignment, shouldEllipsize, oppositeEllipsize, effectiveEllipsize != mEllipsize);
        }
        shouldEllipsize = mEllipsize != null;
        mHintLayout = null;
        if (mHint != null) {
            if (shouldEllipsize)
                hintWidth = wantWidth;

            if (hintBoring == TextView.UNKNOWN_BORING) {
                hintBoring = android.text.BoringLayout.isBoring(mHint, mTextPaint, mTextDir, mHintBoring);
                if (hintBoring != null) {
                    mHintBoring = hintBoring;
                }
            }
            if (hintBoring != null) {
                if ((hintBoring.width <= hintWidth) && ((!shouldEllipsize) || (hintBoring.width <= ellipsisWidth))) {
                    if (mSavedHintLayout != null) {
                        mHintLayout = mSavedHintLayout.replaceOrMake(mHint, mTextPaint, hintWidth, alignment, mSpacingMult, mSpacingAdd, hintBoring, mIncludePad);
                    } else {
                        mHintLayout = android.text.BoringLayout.make(mHint, mTextPaint, hintWidth, alignment, mSpacingMult, mSpacingAdd, hintBoring, mIncludePad);
                    }
                    mSavedHintLayout = ((android.text.BoringLayout) (mHintLayout));
                } else
                    if (shouldEllipsize && (hintBoring.width <= hintWidth)) {
                        if (mSavedHintLayout != null) {
                            mHintLayout = mSavedHintLayout.replaceOrMake(mHint, mTextPaint, hintWidth, alignment, mSpacingMult, mSpacingAdd, hintBoring, mIncludePad, mEllipsize, ellipsisWidth);
                        } else {
                            mHintLayout = android.text.BoringLayout.make(mHint, mTextPaint, hintWidth, alignment, mSpacingMult, mSpacingAdd, hintBoring, mIncludePad, mEllipsize, ellipsisWidth);
                        }
                    }

            }
            // TODO: code duplication with makeSingleLayout()
            if (mHintLayout == null) {
                android.text.StaticLayout.Builder builder = setMaxLines(mMaxMode == LINES ? mMaximum : MAX_VALUE);
                if (shouldEllipsize) {
                    builder.setEllipsize(mEllipsize).setEllipsizedWidth(ellipsisWidth);
                }
                mHintLayout = builder.build();
            }
        }
        if (bringIntoView || (testDirChange && (oldDir != mLayout.getParagraphDirection(0)))) {
            registerForPreDraw();
        }
        if (mEllipsize == android.text.TextUtils.TruncateAt.MARQUEE) {
            if (!compressText(ellipsisWidth)) {
                final int height = mLayoutParams.height;
                // If the size of the view does not depend on the size of the text, try to
                // start the marquee immediately
                if ((height != android.view.ViewGroup.LayoutParams.WRAP_CONTENT) && (height != android.view.ViewGroup.LayoutParams.MATCH_PARENT)) {
                    startMarquee();
                } else {
                    // Defer the start of the marquee until we know our width (see setFrame())
                    mRestartMarquee = true;
                }
            }
        }
        // CursorControllers need a non-null mLayout
        if (mEditor != null)
            mEditor.prepareCursorControllers();

    }

    /**
     * Returns true if DynamicLayout is required
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public boolean useDynamicLayout() {
        return isTextSelectable() || ((mSpannable != null) && (mPrecomputed == null));
    }

    /**
     *
     *
     * @unknown 
     */
    protected android.text.Layout makeSingleLayout(int wantWidth, android.text.BoringLayout.Metrics boring, int ellipsisWidth, android.text.Layout.Alignment alignment, boolean shouldEllipsize, android.text.TextUtils.TruncateAt effectiveEllipsize, boolean useSaved) {
        android.text.Layout result = null;
        if (useDynamicLayout()) {
            final android.text.DynamicLayout.Builder builder = setJustificationMode(mJustificationMode).setEllipsize(getKeyListener() == null ? effectiveEllipsize : null).setEllipsizedWidth(ellipsisWidth);
            result = builder.build();
        } else {
            if (boring == TextView.UNKNOWN_BORING) {
                boring = android.text.BoringLayout.isBoring(mTransformed, mTextPaint, mTextDir, mBoring);
                if (boring != null) {
                    mBoring = boring;
                }
            }
            if (boring != null) {
                if ((boring.width <= wantWidth) && ((effectiveEllipsize == null) || (boring.width <= ellipsisWidth))) {
                    if (useSaved && (mSavedLayout != null)) {
                        result = mSavedLayout.replaceOrMake(mTransformed, mTextPaint, wantWidth, alignment, mSpacingMult, mSpacingAdd, boring, mIncludePad);
                    } else {
                        result = android.text.BoringLayout.make(mTransformed, mTextPaint, wantWidth, alignment, mSpacingMult, mSpacingAdd, boring, mIncludePad);
                    }
                    if (useSaved) {
                        mSavedLayout = ((android.text.BoringLayout) (result));
                    }
                } else
                    if (shouldEllipsize && (boring.width <= wantWidth)) {
                        if (useSaved && (mSavedLayout != null)) {
                            result = mSavedLayout.replaceOrMake(mTransformed, mTextPaint, wantWidth, alignment, mSpacingMult, mSpacingAdd, boring, mIncludePad, effectiveEllipsize, ellipsisWidth);
                        } else {
                            result = android.text.BoringLayout.make(mTransformed, mTextPaint, wantWidth, alignment, mSpacingMult, mSpacingAdd, boring, mIncludePad, effectiveEllipsize, ellipsisWidth);
                        }
                    }

            }
        }
        if (result == null) {
            android.text.StaticLayout.Builder builder = setMaxLines(mMaxMode == LINES ? mMaximum : MAX_VALUE);
            if (shouldEllipsize) {
                builder.setEllipsize(effectiveEllipsize).setEllipsizedWidth(ellipsisWidth);
            }
            result = builder.build();
        }
        return result;
    }

    @android.annotation.UnsupportedAppUsage
    private boolean compressText(float width) {
        if (isHardwareAccelerated())
            return false;

        // Only compress the text if it hasn't been compressed by the previous pass
        if (((((width > 0.0F) && (mLayout != null)) && (getLineCount() == 1)) && (!mUserSetTextScaleX)) && (mTextPaint.getTextScaleX() == 1.0F)) {
            final float textWidth = mLayout.getLineWidth(0);
            final float overflow = ((textWidth + 1.0F) - width) / width;
            if ((overflow > 0.0F) && (overflow <= TextView.Marquee.MARQUEE_DELTA_MAX)) {
                mTextPaint.setTextScaleX((1.0F - overflow) - 0.005F);
                post(new java.lang.Runnable() {
                    public void run() {
                        requestLayout();
                    }
                });
                return true;
            }
        }
        return false;
    }

    private static int desired(android.text.Layout layout) {
        int n = layout.getLineCount();
        java.lang.CharSequence text = layout.getText();
        float max = 0;
        // if any line was wrapped, we can't use it.
        // but it's ok for the last line not to have a newline
        for (int i = 0; i < (n - 1); i++) {
            if (text.charAt(layout.getLineEnd(i) - 1) != '\n') {
                return -1;
            }
        }
        for (int i = 0; i < n; i++) {
            max = java.lang.Math.max(max, layout.getLineWidth(i));
        }
        return ((int) (java.lang.Math.ceil(max)));
    }

    /**
     * Set whether the TextView includes extra top and bottom padding to make
     * room for accents that go above the normal ascent and descent.
     * The default is true.
     *
     * @see #getIncludeFontPadding()
     * @unknown ref android.R.styleable#TextView_includeFontPadding
     */
    public void setIncludeFontPadding(boolean includepad) {
        if (mIncludePad != includepad) {
            mIncludePad = includepad;
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Gets whether the TextView includes extra top and bottom padding to make
     * room for accents that go above the normal ascent and descent.
     *
     * @see #setIncludeFontPadding(boolean)
     * @unknown ref android.R.styleable#TextView_includeFontPadding
     */
    @android.view.inspector.InspectableProperty
    public boolean getIncludeFontPadding() {
        return mIncludePad;
    }

    /**
     *
     *
     * @unknown 
     */
    @com.android.internal.annotations.VisibleForTesting
    public static final BoringLayout.Metrics UNKNOWN_BORING = new android.text.BoringLayout.Metrics();

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        int width;
        int height;
        android.text.BoringLayout.Metrics boring = TextView.UNKNOWN_BORING;
        android.text.BoringLayout.Metrics hintBoring = TextView.UNKNOWN_BORING;
        if (mTextDir == null) {
            mTextDir = getTextDirectionHeuristic();
        }
        int des = -1;
        boolean fromexisting = false;
        final float widthLimit = (widthMode == android.view.View.MeasureSpec.AT_MOST) ? ((float) (widthSize)) : java.lang.Float.MAX_VALUE;
        if (widthMode == android.view.View.MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            width = widthSize;
        } else {
            if ((mLayout != null) && (mEllipsize == null)) {
                des = TextView.desired(mLayout);
            }
            if (des < 0) {
                boring = android.text.BoringLayout.isBoring(mTransformed, mTextPaint, mTextDir, mBoring);
                if (boring != null) {
                    mBoring = boring;
                }
            } else {
                fromexisting = true;
            }
            if ((boring == null) || (boring == TextView.UNKNOWN_BORING)) {
                if (des < 0) {
                    des = ((int) (java.lang.Math.ceil(android.text.Layout.getDesiredWidthWithLimit(mTransformed, 0, mTransformed.length(), mTextPaint, mTextDir, widthLimit))));
                }
                width = des;
            } else {
                width = boring.width;
            }
            final TextView.Drawables dr = mDrawables;
            if (dr != null) {
                width = java.lang.Math.max(width, dr.mDrawableWidthTop);
                width = java.lang.Math.max(width, dr.mDrawableWidthBottom);
            }
            if (mHint != null) {
                int hintDes = -1;
                int hintWidth;
                if ((mHintLayout != null) && (mEllipsize == null)) {
                    hintDes = TextView.desired(mHintLayout);
                }
                if (hintDes < 0) {
                    hintBoring = android.text.BoringLayout.isBoring(mHint, mTextPaint, mTextDir, mHintBoring);
                    if (hintBoring != null) {
                        mHintBoring = hintBoring;
                    }
                }
                if ((hintBoring == null) || (hintBoring == TextView.UNKNOWN_BORING)) {
                    if (hintDes < 0) {
                        hintDes = ((int) (java.lang.Math.ceil(android.text.Layout.getDesiredWidthWithLimit(mHint, 0, mHint.length(), mTextPaint, mTextDir, widthLimit))));
                    }
                    hintWidth = hintDes;
                } else {
                    hintWidth = hintBoring.width;
                }
                if (hintWidth > width) {
                    width = hintWidth;
                }
            }
            width += getCompoundPaddingLeft() + getCompoundPaddingRight();
            if (mMaxWidthMode == TextView.EMS) {
                width = java.lang.Math.min(width, mMaxWidth * getLineHeight());
            } else {
                width = java.lang.Math.min(width, mMaxWidth);
            }
            if (mMinWidthMode == TextView.EMS) {
                width = java.lang.Math.max(width, mMinWidth * getLineHeight());
            } else {
                width = java.lang.Math.max(width, mMinWidth);
            }
            // Check against our minimum width
            width = java.lang.Math.max(width, getSuggestedMinimumWidth());
            if (widthMode == android.view.View.MeasureSpec.AT_MOST) {
                width = java.lang.Math.min(widthSize, width);
            }
        }
        int want = (width - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int unpaddedWidth = want;
        if (mHorizontallyScrolling)
            want = TextView.VERY_WIDE;

        int hintWant = want;
        int hintWidth = (mHintLayout == null) ? hintWant : mHintLayout.getWidth();
        if (mLayout == null) {
            makeNewLayout(want, hintWant, boring, hintBoring, (width - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
        } else {
            final boolean layoutChanged = ((mLayout.getWidth() != want) || (hintWidth != hintWant)) || (mLayout.getEllipsizedWidth() != ((width - getCompoundPaddingLeft()) - getCompoundPaddingRight()));
            final boolean widthChanged = (((mHint == null) && (mEllipsize == null)) && (want > mLayout.getWidth())) && ((mLayout instanceof android.text.BoringLayout) || ((fromexisting && (des >= 0)) && (des <= want)));
            final boolean maximumChanged = (mMaxMode != mOldMaxMode) || (mMaximum != mOldMaximum);
            if (layoutChanged || maximumChanged) {
                if ((!maximumChanged) && widthChanged) {
                    mLayout.increaseWidthTo(want);
                } else {
                    makeNewLayout(want, hintWant, boring, hintBoring, (width - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
                }
            } else {
                // Nothing has changed
            }
        }
        if (heightMode == android.view.View.MeasureSpec.EXACTLY) {
            // Parent has told us how big to be. So be it.
            height = heightSize;
            mDesiredHeightAtMeasure = -1;
        } else {
            int desired = getDesiredHeight();
            height = desired;
            mDesiredHeightAtMeasure = desired;
            if (heightMode == android.view.View.MeasureSpec.AT_MOST) {
                height = java.lang.Math.min(desired, heightSize);
            }
        }
        int unpaddedHeight = (height - getCompoundPaddingTop()) - getCompoundPaddingBottom();
        if ((mMaxMode == LINES) && (mLayout.getLineCount() > mMaximum)) {
            unpaddedHeight = java.lang.Math.min(unpaddedHeight, mLayout.getLineTop(mMaximum));
        }
        /* We didn't let makeNewLayout() register to bring the cursor into view,
        so do it here if there is any possibility that it is needed.
         */
        if (((mMovement != null) || (mLayout.getWidth() > unpaddedWidth)) || (mLayout.getHeight() > unpaddedHeight)) {
            registerForPreDraw();
        } else {
            scrollTo(0, 0);
        }
        setMeasuredDimension(width, height);
    }

    /**
     * Automatically computes and sets the text size.
     */
    private void autoSizeText() {
        if (!isAutoSizeEnabled()) {
            return;
        }
        if (mNeedsAutoSizeText) {
            if ((getMeasuredWidth() <= 0) || (getMeasuredHeight() <= 0)) {
                return;
            }
            final int availableWidth = (mHorizontallyScrolling) ? TextView.VERY_WIDE : (getMeasuredWidth() - getTotalPaddingLeft()) - getTotalPaddingRight();
            final int availableHeight = (getMeasuredHeight() - getExtendedPaddingBottom()) - getExtendedPaddingTop();
            if ((availableWidth <= 0) || (availableHeight <= 0)) {
                return;
            }
            synchronized(TextView.TEMP_RECTF) {
                TextView.TEMP_RECTF.setEmpty();
                TextView.TEMP_RECTF.right = availableWidth;
                TextView.TEMP_RECTF.bottom = availableHeight;
                final float optimalTextSize = findLargestTextSizeWhichFits(TextView.TEMP_RECTF);
                if (optimalTextSize != getTextSize()) {
                    /* shouldRequestLayout */
                    setTextSizeInternal(TypedValue.COMPLEX_UNIT_PX, optimalTextSize, false);
                    /* hintWidth */
                    /* bringIntoView */
                    makeNewLayout(availableWidth, 0, TextView.UNKNOWN_BORING, TextView.UNKNOWN_BORING, ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
                }
            }
        }
        // Always try to auto-size if enabled. Functions that do not want to trigger auto-sizing
        // after the next layout pass should set this to false.
        mNeedsAutoSizeText = true;
    }

    /**
     * Performs a binary search to find the largest text size that will still fit within the size
     * available to this view.
     */
    private int findLargestTextSizeWhichFits(android.graphics.RectF availableSpace) {
        final int sizesCount = mAutoSizeTextSizesInPx.length;
        if (sizesCount == 0) {
            throw new java.lang.IllegalStateException("No available text sizes to choose from.");
        }
        int bestSizeIndex = 0;
        int lowIndex = bestSizeIndex + 1;
        int highIndex = sizesCount - 1;
        int sizeToTryIndex;
        while (lowIndex <= highIndex) {
            sizeToTryIndex = (lowIndex + highIndex) / 2;
            if (suggestedSizeFitsInSpace(mAutoSizeTextSizesInPx[sizeToTryIndex], availableSpace)) {
                bestSizeIndex = lowIndex;
                lowIndex = sizeToTryIndex + 1;
            } else {
                highIndex = sizeToTryIndex - 1;
                bestSizeIndex = highIndex;
            }
        } 
        return mAutoSizeTextSizesInPx[bestSizeIndex];
    }

    private boolean suggestedSizeFitsInSpace(int suggestedSizeInPx, android.graphics.RectF availableSpace) {
        final java.lang.CharSequence text = (mTransformed != null) ? mTransformed : getText();
        final int maxLines = getMaxLines();
        if (mTempTextPaint == null) {
            mTempTextPaint = new android.text.TextPaint();
        } else {
            mTempTextPaint.reset();
        }
        mTempTextPaint.set(getPaint());
        mTempTextPaint.setTextSize(suggestedSizeInPx);
        final android.text.StaticLayout.Builder layoutBuilder = StaticLayout.Builder.obtain(text, 0, text.length(), mTempTextPaint, java.lang.Math.round(availableSpace.right));
        setJustificationMode(getJustificationMode()).setMaxLines(mMaxMode == LINES ? mMaximum : MAX_VALUE).setTextDirection(getTextDirectionHeuristic());
        final android.text.StaticLayout layout = layoutBuilder.build();
        // Lines overflow.
        if ((maxLines != (-1)) && (layout.getLineCount() > maxLines)) {
            return false;
        }
        // Height overflow.
        if (layout.getHeight() > availableSpace.bottom) {
            return false;
        }
        return true;
    }

    private int getDesiredHeight() {
        return java.lang.Math.max(getDesiredHeight(mLayout, true), getDesiredHeight(mHintLayout, mEllipsize != null));
    }

    private int getDesiredHeight(android.text.Layout layout, boolean cap) {
        if (layout == null) {
            return 0;
        }
        /* Don't cap the hint to a certain number of lines.
        (Do cap it, though, if we have a maximum pixel height.)
         */
        int desired = layout.getHeight(cap);
        final TextView.Drawables dr = mDrawables;
        if (dr != null) {
            desired = java.lang.Math.max(desired, dr.mDrawableHeightLeft);
            desired = java.lang.Math.max(desired, dr.mDrawableHeightRight);
        }
        int linecount = layout.getLineCount();
        final int padding = getCompoundPaddingTop() + getCompoundPaddingBottom();
        desired += padding;
        if (mMaxMode != LINES) {
            desired = java.lang.Math.min(desired, mMaximum);
        } else
            if ((cap && (linecount > mMaximum)) && ((layout instanceof android.text.DynamicLayout) || (layout instanceof android.text.BoringLayout))) {
                desired = layout.getLineTop(mMaximum);
                if (dr != null) {
                    desired = java.lang.Math.max(desired, dr.mDrawableHeightLeft);
                    desired = java.lang.Math.max(desired, dr.mDrawableHeightRight);
                }
                desired += padding;
                linecount = mMaximum;
            }

        if (mMinMode == LINES) {
            if (linecount < mMinimum) {
                desired += getLineHeight() * (mMinimum - linecount);
            }
        } else {
            desired = java.lang.Math.max(desired, mMinimum);
        }
        // Check against our minimum height
        desired = java.lang.Math.max(desired, getSuggestedMinimumHeight());
        return desired;
    }

    /**
     * Check whether a change to the existing text layout requires a
     * new view layout.
     */
    private void checkForResize() {
        boolean sizeChanged = false;
        if (mLayout != null) {
            // Check if our width changed
            if (mLayoutParams.width == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
                sizeChanged = true;
                invalidate();
            }
            // Check if our height changed
            if (mLayoutParams.height == android.view.ViewGroup.LayoutParams.WRAP_CONTENT) {
                int desiredHeight = getDesiredHeight();
                if (desiredHeight != this.getHeight()) {
                    sizeChanged = true;
                }
            } else
                if (mLayoutParams.height == android.view.ViewGroup.LayoutParams.MATCH_PARENT) {
                    if (mDesiredHeightAtMeasure >= 0) {
                        int desiredHeight = getDesiredHeight();
                        if (desiredHeight != mDesiredHeightAtMeasure) {
                            sizeChanged = true;
                        }
                    }
                }

        }
        if (sizeChanged) {
            requestLayout();
            // caller will have already invalidated
        }
    }

    /**
     * Check whether entirely new text requires a new view layout
     * or merely a new text layout.
     */
    @android.annotation.UnsupportedAppUsage
    private void checkForRelayout() {
        // If we have a fixed width, we can just swap in a new text layout
        // if the text height stays the same or if the view height is fixed.
        if ((((mLayoutParams.width != android.view.ViewGroup.LayoutParams.WRAP_CONTENT) || ((mMaxWidthMode == mMinWidthMode) && (mMaxWidth == mMinWidth))) && ((mHint == null) || (mHintLayout != null))) && ((((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight()) > 0)) {
            // Static width, so try making a new text layout.
            int oldht = mLayout.getHeight();
            int want = mLayout.getWidth();
            int hintWant = (mHintLayout == null) ? 0 : mHintLayout.getWidth();
            /* No need to bring the text into view, since the size is not
            changing (unless we do the requestLayout(), in which case it
            will happen at measure).
             */
            makeNewLayout(want, hintWant, TextView.UNKNOWN_BORING, TextView.UNKNOWN_BORING, ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), false);
            if (mEllipsize != android.text.TextUtils.TruncateAt.MARQUEE) {
                // In a fixed-height view, so use our new text layout.
                if ((mLayoutParams.height != android.view.ViewGroup.LayoutParams.WRAP_CONTENT) && (mLayoutParams.height != android.view.ViewGroup.LayoutParams.MATCH_PARENT)) {
                    autoSizeText();
                    invalidate();
                    return;
                }
                // Dynamic height, but height has stayed the same,
                // so use our new text layout.
                if ((mLayout.getHeight() == oldht) && ((mHintLayout == null) || (mHintLayout.getHeight() == oldht))) {
                    autoSizeText();
                    invalidate();
                    return;
                }
            }
            // We lose: the height has changed and we have a dynamic height.
            // Request a new view layout using our new text layout.
            requestLayout();
            invalidate();
        } else {
            // Dynamic width, so we have no choice but to request a new
            // view layout with a new text layout.
            nullLayouts();
            requestLayout();
            invalidate();
        }
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mDeferScroll >= 0) {
            int curs = mDeferScroll;
            mDeferScroll = -1;
            bringPointIntoView(java.lang.Math.min(curs, mText.length()));
        }
        // Call auto-size after the width and height have been calculated.
        autoSizeText();
    }

    private boolean isShowingHint() {
        return android.text.TextUtils.isEmpty(mText) && (!android.text.TextUtils.isEmpty(mHint));
    }

    /**
     * Returns true if anything changed.
     */
    @android.annotation.UnsupportedAppUsage
    private boolean bringTextIntoView() {
        android.text.Layout layout = (isShowingHint()) ? mHintLayout : mLayout;
        int line = 0;
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == android.view.Gravity.BOTTOM) {
            line = layout.getLineCount() - 1;
        }
        android.text.Layout.Alignment a = layout.getParagraphAlignment(line);
        int dir = layout.getParagraphDirection(line);
        int hspace = ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((mBottom - mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        int ht = layout.getHeight();
        int scrollx;
        int scrolly;
        // Convert to left, center, or right alignment.
        if (a == Layout.Alignment.ALIGN_NORMAL) {
            a = (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) ? Layout.Alignment.ALIGN_LEFT : Layout.Alignment.ALIGN_RIGHT;
        } else
            if (a == Layout.Alignment.ALIGN_OPPOSITE) {
                a = (dir == android.text.Layout.DIR_LEFT_TO_RIGHT) ? Layout.Alignment.ALIGN_RIGHT : Layout.Alignment.ALIGN_LEFT;
            }

        if (a == Layout.Alignment.ALIGN_CENTER) {
            /* Keep centered if possible, or, if it is too wide to fit,
            keep leading edge in view.
             */
            int left = ((int) (java.lang.Math.floor(layout.getLineLeft(line))));
            int right = ((int) (java.lang.Math.ceil(layout.getLineRight(line))));
            if ((right - left) < hspace) {
                scrollx = ((right + left) / 2) - (hspace / 2);
            } else {
                if (dir < 0) {
                    scrollx = right - hspace;
                } else {
                    scrollx = left;
                }
            }
        } else
            if (a == Layout.Alignment.ALIGN_RIGHT) {
                int right = ((int) (java.lang.Math.ceil(layout.getLineRight(line))));
                scrollx = right - hspace;
            } else {
                // a == Layout.Alignment.ALIGN_LEFT (will also be the default)
                scrollx = ((int) (java.lang.Math.floor(layout.getLineLeft(line))));
            }

        if (ht < vspace) {
            scrolly = 0;
        } else {
            if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == android.view.Gravity.BOTTOM) {
                scrolly = ht - vspace;
            } else {
                scrolly = 0;
            }
        }
        if ((scrollx != mScrollX) || (scrolly != mScrollY)) {
            scrollTo(scrollx, scrolly);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Move the point, specified by the offset, into the view if it is needed.
     * This has to be called after layout. Returns true if anything changed.
     */
    public boolean bringPointIntoView(int offset) {
        if (isLayoutRequested()) {
            mDeferScroll = offset;
            return false;
        }
        boolean changed = false;
        android.text.Layout layout = (isShowingHint()) ? mHintLayout : mLayout;
        if (layout == null)
            return changed;

        int line = layout.getLineForOffset(offset);
        int grav;
        switch (layout.getParagraphAlignment(line)) {
            case ALIGN_LEFT :
                grav = 1;
                break;
            case ALIGN_RIGHT :
                grav = -1;
                break;
            case ALIGN_NORMAL :
                grav = layout.getParagraphDirection(line);
                break;
            case ALIGN_OPPOSITE :
                grav = -layout.getParagraphDirection(line);
                break;
            case ALIGN_CENTER :
            default :
                grav = 0;
                break;
        }
        // We only want to clamp the cursor to fit within the layout width
        // in left-to-right modes, because in a right to left alignment,
        // we want to scroll to keep the line-right on the screen, as other
        // lines are likely to have text flush with the right margin, which
        // we want to keep visible.
        // A better long-term solution would probably be to measure both
        // the full line and a blank-trimmed version, and, for example, use
        // the latter measurement for centering and right alignment, but for
        // the time being we only implement the cursor clamping in left to
        // right where it is most likely to be annoying.
        final boolean clamped = grav > 0;
        // FIXME: Is it okay to truncate this, or should we round?
        final int x = ((int) (layout.getPrimaryHorizontal(offset, clamped)));
        final int top = layout.getLineTop(line);
        final int bottom = layout.getLineTop(line + 1);
        int left = ((int) (java.lang.Math.floor(layout.getLineLeft(line))));
        int right = ((int) (java.lang.Math.ceil(layout.getLineRight(line))));
        int ht = layout.getHeight();
        int hspace = ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        int vspace = ((mBottom - mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        if (((!mHorizontallyScrolling) && ((right - left) > hspace)) && (right > x)) {
            // If cursor has been clamped, make sure we don't scroll.
            right = java.lang.Math.max(x, left + hspace);
        }
        int hslack = (bottom - top) / 2;
        int vslack = hslack;
        if (vslack > (vspace / 4)) {
            vslack = vspace / 4;
        }
        if (hslack > (hspace / 4)) {
            hslack = hspace / 4;
        }
        int hs = mScrollX;
        int vs = mScrollY;
        if ((top - vs) < vslack) {
            vs = top - vslack;
        }
        if ((bottom - vs) > (vspace - vslack)) {
            vs = bottom - (vspace - vslack);
        }
        if ((ht - vs) < vspace) {
            vs = ht - vspace;
        }
        if ((0 - vs) > 0) {
            vs = 0;
        }
        if (grav != 0) {
            if ((x - hs) < hslack) {
                hs = x - hslack;
            }
            if ((x - hs) > (hspace - hslack)) {
                hs = x - (hspace - hslack);
            }
        }
        if (grav < 0) {
            if ((left - hs) > 0) {
                hs = left;
            }
            if ((right - hs) < hspace) {
                hs = right - hspace;
            }
        } else
            if (grav > 0) {
                if ((right - hs) < hspace) {
                    hs = right - hspace;
                }
                if ((left - hs) > 0) {
                    hs = left;
                }
            } else {
                if ((right - left) <= hspace) {
                    /* If the entire text fits, center it exactly. */
                    hs = left - ((hspace - (right - left)) / 2);
                } else
                    if (x > (right - hslack)) {
                        /* If we are near the right edge, keep the right edge
                        at the edge of the view.
                         */
                        hs = right - hspace;
                    } else
                        if (x < (left + hslack)) {
                            /* If we are near the left edge, keep the left edge
                            at the edge of the view.
                             */
                            hs = left;
                        } else
                            if (left > hs) {
                                /* Is there whitespace visible at the left?  Fix it if so. */
                                hs = left;
                            } else
                                if (right < (hs + hspace)) {
                                    /* Is there whitespace visible at the right?  Fix it if so. */
                                    hs = right - hspace;
                                } else {
                                    /* Otherwise, float as needed. */
                                    if ((x - hs) < hslack) {
                                        hs = x - hslack;
                                    }
                                    if ((x - hs) > (hspace - hslack)) {
                                        hs = x - (hspace - hslack);
                                    }
                                }




            }

        if ((hs != mScrollX) || (vs != mScrollY)) {
            if (mScroller == null) {
                scrollTo(hs, vs);
            } else {
                long duration = android.view.animation.AnimationUtils.currentAnimationTimeMillis() - mLastScroll;
                int dx = hs - mScrollX;
                int dy = vs - mScrollY;
                if (duration > TextView.ANIMATED_SCROLL_GAP) {
                    mScroller.startScroll(mScrollX, mScrollY, dx, dy);
                    awakenScrollBars(mScroller.getDuration());
                    invalidate();
                } else {
                    if (!mScroller.isFinished()) {
                        mScroller.abortAnimation();
                    }
                    scrollBy(dx, dy);
                }
                mLastScroll = android.view.animation.AnimationUtils.currentAnimationTimeMillis();
            }
            changed = true;
        }
        if (isFocused()) {
            // This offsets because getInterestingRect() is in terms of viewport coordinates, but
            // requestRectangleOnScreen() is in terms of content coordinates.
            // The offsets here are to ensure the rectangle we are using is
            // within our view bounds, in case the cursor is on the far left
            // or right.  If it isn't withing the bounds, then this request
            // will be ignored.
            if (mTempRect == null)
                mTempRect = new android.graphics.Rect();

            mTempRect.set(x - 2, top, x + 2, bottom);
            getInterestingRect(mTempRect, line);
            mTempRect.offset(mScrollX, mScrollY);
            if (requestRectangleOnScreen(mTempRect)) {
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Move the cursor, if needed, so that it is at an offset that is visible
     * to the user.  This will not move the cursor if it represents more than
     * one character (a selection range).  This will only work if the
     * TextView contains spannable text; otherwise it will do nothing.
     *
     * @return True if the cursor was actually moved, false otherwise.
     */
    public boolean moveCursorToVisibleOffset() {
        if (!(mText instanceof android.text.Spannable)) {
            return false;
        }
        int start = getSelectionStart();
        int end = getSelectionEnd();
        if (start != end) {
            return false;
        }
        // First: make sure the line is visible on screen:
        int line = mLayout.getLineForOffset(start);
        final int top = mLayout.getLineTop(line);
        final int bottom = mLayout.getLineTop(line + 1);
        final int vspace = ((mBottom - mTop) - getExtendedPaddingTop()) - getExtendedPaddingBottom();
        int vslack = (bottom - top) / 2;
        if (vslack > (vspace / 4)) {
            vslack = vspace / 4;
        }
        final int vs = mScrollY;
        if (top < (vs + vslack)) {
            line = mLayout.getLineForVertical((vs + vslack) + (bottom - top));
        } else
            if (bottom > ((vspace + vs) - vslack)) {
                line = mLayout.getLineForVertical(((vspace + vs) - vslack) - (bottom - top));
            }

        // Next: make sure the character is visible on screen:
        final int hspace = ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        final int hs = mScrollX;
        final int leftChar = mLayout.getOffsetForHorizontal(line, hs);
        final int rightChar = mLayout.getOffsetForHorizontal(line, hspace + hs);
        // line might contain bidirectional text
        final int lowChar = (leftChar < rightChar) ? leftChar : rightChar;
        final int highChar = (leftChar > rightChar) ? leftChar : rightChar;
        int newStart = start;
        if (newStart < lowChar) {
            newStart = lowChar;
        } else
            if (newStart > highChar) {
                newStart = highChar;
            }

        if (newStart != start) {
            android.text.Selection.setSelection(mSpannable, newStart);
            return true;
        }
        return false;
    }

    @java.lang.Override
    public void computeScroll() {
        if (mScroller != null) {
            if (mScroller.computeScrollOffset()) {
                mScrollX = mScroller.getCurrX();
                mScrollY = mScroller.getCurrY();
                invalidateParentCaches();
                postInvalidate();// So we draw again

            }
        }
    }

    private void getInterestingRect(android.graphics.Rect r, int line) {
        convertFromViewportToContentCoordinates(r);
        // Rectangle can can be expanded on first and last line to take
        // padding into account.
        // TODO Take left/right padding into account too?
        if (line == 0)
            r.top -= getExtendedPaddingTop();

        if (line == (mLayout.getLineCount() - 1))
            r.bottom += getExtendedPaddingBottom();

    }

    private void convertFromViewportToContentCoordinates(android.graphics.Rect r) {
        final int horizontalOffset = viewportToContentHorizontalOffset();
        r.left += horizontalOffset;
        r.right += horizontalOffset;
        final int verticalOffset = viewportToContentVerticalOffset();
        r.top += verticalOffset;
        r.bottom += verticalOffset;
    }

    int viewportToContentHorizontalOffset() {
        return getCompoundPaddingLeft() - mScrollX;
    }

    @android.annotation.UnsupportedAppUsage
    int viewportToContentVerticalOffset() {
        int offset = getExtendedPaddingTop() - mScrollY;
        if ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) != android.view.Gravity.TOP) {
            offset += getVerticalOffset(false);
        }
        return offset;
    }

    @java.lang.Override
    public void debug(int depth) {
        super.debug(depth);
        java.lang.String output = android.view.View.debugIndent(depth);
        output += ((((((((((("frame={" + mLeft) + ", ") + mTop) + ", ") + mRight) + ", ") + mBottom) + "} scroll={") + mScrollX) + ", ") + mScrollY) + "} ";
        if (mText != null) {
            output += ("mText=\"" + mText) + "\" ";
            if (mLayout != null) {
                output += (("mLayout width=" + mLayout.getWidth()) + " height=") + mLayout.getHeight();
            }
        } else {
            output += "mText=NULL";
        }
        android.util.Log.d(android.view.View.VIEW_LOG_TAG, output);
    }

    /**
     * Convenience for {@link Selection#getSelectionStart}.
     */
    @android.view.ViewDebug.ExportedProperty(category = "text")
    public int getSelectionStart() {
        return android.text.Selection.getSelectionStart(getText());
    }

    /**
     * Convenience for {@link Selection#getSelectionEnd}.
     */
    @android.view.ViewDebug.ExportedProperty(category = "text")
    public int getSelectionEnd() {
        return android.text.Selection.getSelectionEnd(getText());
    }

    /**
     * Return true iff there is a selection of nonzero length inside this text view.
     */
    public boolean hasSelection() {
        final int selectionStart = getSelectionStart();
        final int selectionEnd = getSelectionEnd();
        return ((selectionStart >= 0) && (selectionEnd > 0)) && (selectionStart != selectionEnd);
    }

    java.lang.String getSelectedText() {
        if (!hasSelection()) {
            return null;
        }
        final int start = getSelectionStart();
        final int end = getSelectionEnd();
        return java.lang.String.valueOf(start > end ? mText.subSequence(end, start) : mText.subSequence(start, end));
    }

    /**
     * Sets the properties of this field (lines, horizontally scrolling,
     * transformation method) to be for a single-line input.
     *
     * @unknown ref android.R.styleable#TextView_singleLine
     */
    public void setSingleLine() {
        setSingleLine(true);
    }

    /**
     * Sets the properties of this field to transform input to ALL CAPS
     * display. This may use a "small caps" formatting if available.
     * This setting will be ignored if this field is editable or selectable.
     *
     * This call replaces the current transformation method. Disabling this
     * will not necessarily restore the previous behavior from before this
     * was enabled.
     *
     * @see #setTransformationMethod(TransformationMethod)
     * @unknown ref android.R.styleable#TextView_textAllCaps
     */
    public void setAllCaps(boolean allCaps) {
        if (allCaps) {
            setTransformationMethod(new android.text.method.AllCapsTransformationMethod(getContext()));
        } else {
            setTransformationMethod(null);
        }
    }

    /**
     * Checks whether the transformation method applied to this TextView is set to ALL CAPS.
     *
     * @return Whether the current transformation method is for ALL CAPS.
     * @see #setAllCaps(boolean)
     * @see #setTransformationMethod(TransformationMethod)
     */
    @android.view.inspector.InspectableProperty(name = "textAllCaps")
    public boolean isAllCaps() {
        final android.text.method.TransformationMethod method = getTransformationMethod();
        return (method != null) && (method instanceof android.text.method.AllCapsTransformationMethod);
    }

    /**
     * If true, sets the properties of this field (number of lines, horizontally scrolling,
     * transformation method) to be for a single-line input; if false, restores these to the default
     * conditions.
     *
     * Note that the default conditions are not necessarily those that were in effect prior this
     * method, and you may want to reset these properties to your custom values.
     *
     * @unknown ref android.R.styleable#TextView_singleLine
     */
    @android.view.RemotableViewMethod
    public void setSingleLine(boolean singleLine) {
        // Could be used, but may break backward compatibility.
        // if (mSingleLine == singleLine) return;
        setInputTypeSingleLine(singleLine);
        applySingleLine(singleLine, true, true);
    }

    /**
     * Adds or remove the EditorInfo.TYPE_TEXT_FLAG_MULTI_LINE on the mInputType.
     *
     * @param singleLine
     * 		
     */
    private void setInputTypeSingleLine(boolean singleLine) {
        if ((mEditor != null) && ((mEditor.mInputType & TYPE_MASK_CLASS) == TYPE_CLASS_TEXT)) {
            if (singleLine) {
                mEditor.mInputType &= ~TYPE_TEXT_FLAG_MULTI_LINE;
            } else {
                mEditor.mInputType |= TYPE_TEXT_FLAG_MULTI_LINE;
            }
        }
    }

    private void applySingleLine(boolean singleLine, boolean applyTransformation, boolean changeMaxLines) {
        mSingleLine = singleLine;
        if (singleLine) {
            setLines(1);
            setHorizontallyScrolling(true);
            if (applyTransformation) {
                setTransformationMethod(android.text.method.SingleLineTransformationMethod.getInstance());
            }
        } else {
            if (changeMaxLines) {
                setMaxLines(MAX_VALUE);
            }
            setHorizontallyScrolling(false);
            if (applyTransformation) {
                setTransformationMethod(null);
            }
        }
    }

    /**
     * Causes words in the text that are longer than the view's width
     * to be ellipsized instead of broken in the middle.  You may also
     * want to {@link #setSingleLine} or {@link #setHorizontallyScrolling}
     * to constrain the text to a single line.  Use <code>null</code>
     * to turn off ellipsizing.
     *
     * If {@link #setMaxLines} has been used to set two or more lines,
     * only {@link android.text.TextUtils.TruncateAt#END} and
     * {@link android.text.TextUtils.TruncateAt#MARQUEE} are supported
     * (other ellipsizing types will not do anything).
     *
     * @unknown ref android.R.styleable#TextView_ellipsize
     */
    public void setEllipsize(android.text.TextUtils.TruncateAt where) {
        // TruncateAt is an enum. != comparison is ok between these singleton objects.
        if (mEllipsize != where) {
            mEllipsize = where;
            if (mLayout != null) {
                nullLayouts();
                requestLayout();
                invalidate();
            }
        }
    }

    /**
     * Sets how many times to repeat the marquee animation. Only applied if the
     * TextView has marquee enabled. Set to -1 to repeat indefinitely.
     *
     * @see #getMarqueeRepeatLimit()
     * @unknown ref android.R.styleable#TextView_marqueeRepeatLimit
     */
    public void setMarqueeRepeatLimit(int marqueeLimit) {
        mMarqueeRepeatLimit = marqueeLimit;
    }

    /**
     * Gets the number of times the marquee animation is repeated. Only meaningful if the
     * TextView has marquee enabled.
     *
     * @return the number of times the marquee animation is repeated. -1 if the animation
    repeats indefinitely
     * @see #setMarqueeRepeatLimit(int)
     * @unknown ref android.R.styleable#TextView_marqueeRepeatLimit
     */
    @android.view.inspector.InspectableProperty
    public int getMarqueeRepeatLimit() {
        return mMarqueeRepeatLimit;
    }

    /**
     * Returns where, if anywhere, words that are longer than the view
     * is wide should be ellipsized.
     */
    @android.view.inspector.InspectableProperty
    @android.view.ViewDebug.ExportedProperty
    public android.text.TextUtils.TruncateAt getEllipsize() {
        return mEllipsize;
    }

    /**
     * Set the TextView so that when it takes focus, all the text is
     * selected.
     *
     * @unknown ref android.R.styleable#TextView_selectAllOnFocus
     */
    @android.view.RemotableViewMethod
    public void setSelectAllOnFocus(boolean selectAllOnFocus) {
        createEditorIfNeeded();
        mEditor.mSelectAllOnFocus = selectAllOnFocus;
        if (selectAllOnFocus && (!(mText instanceof android.text.Spannable))) {
            setText(mText, TextView.BufferType.SPANNABLE);
        }
    }

    /**
     * Set whether the cursor is visible. The default is true. Note that this property only
     * makes sense for editable TextView.
     *
     * @see #isCursorVisible()
     * @unknown ref android.R.styleable#TextView_cursorVisible
     */
    @android.view.RemotableViewMethod
    public void setCursorVisible(boolean visible) {
        if (visible && (mEditor == null))
            return;
        // visible is the default value with no edit data

        createEditorIfNeeded();
        if (mEditor.mCursorVisible != visible) {
            mEditor.mCursorVisible = visible;
            invalidate();
            mEditor.makeBlink();
            // InsertionPointCursorController depends on mCursorVisible
            mEditor.prepareCursorControllers();
        }
    }

    /**
     *
     *
     * @return whether or not the cursor is visible (assuming this TextView is editable)
     * @see #setCursorVisible(boolean)
     * @unknown ref android.R.styleable#TextView_cursorVisible
     */
    @android.view.inspector.InspectableProperty
    public boolean isCursorVisible() {
        // true is the default value
        return mEditor == null ? true : mEditor.mCursorVisible;
    }

    private boolean canMarquee() {
        int width = ((mRight - mLeft) - getCompoundPaddingLeft()) - getCompoundPaddingRight();
        return (width > 0) && ((mLayout.getLineWidth(0) > width) || (((mMarqueeFadeMode != TextView.MARQUEE_FADE_NORMAL) && (mSavedMarqueeModeLayout != null)) && (mSavedMarqueeModeLayout.getLineWidth(0) > width)));
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 115609023)
    private void startMarquee() {
        // Do not ellipsize EditText
        if (getKeyListener() != null)
            return;

        if (compressText((getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight())) {
            return;
        }
        if (((((mMarquee == null) || mMarquee.isStopped()) && (isFocused() || isSelected())) && (getLineCount() == 1)) && canMarquee()) {
            if (mMarqueeFadeMode == TextView.MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS) {
                mMarqueeFadeMode = TextView.MARQUEE_FADE_SWITCH_SHOW_FADE;
                final android.text.Layout tmp = mLayout;
                mLayout = mSavedMarqueeModeLayout;
                mSavedMarqueeModeLayout = tmp;
                setHorizontalFadingEdgeEnabled(true);
                requestLayout();
                invalidate();
            }
            if (mMarquee == null)
                mMarquee = new TextView.Marquee(this);

            mMarquee.start(mMarqueeRepeatLimit);
        }
    }

    private void stopMarquee() {
        if ((mMarquee != null) && (!mMarquee.isStopped())) {
            mMarquee.stop();
        }
        if (mMarqueeFadeMode == TextView.MARQUEE_FADE_SWITCH_SHOW_FADE) {
            mMarqueeFadeMode = TextView.MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS;
            final android.text.Layout tmp = mSavedMarqueeModeLayout;
            mSavedMarqueeModeLayout = mLayout;
            mLayout = tmp;
            setHorizontalFadingEdgeEnabled(false);
            requestLayout();
            invalidate();
        }
    }

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = android.os.Build.VERSION_CODES.P, trackingBug = 115609023)
    private void startStopMarquee(boolean start) {
        if (mEllipsize == android.text.TextUtils.TruncateAt.MARQUEE) {
            if (start) {
                startMarquee();
            } else {
                stopMarquee();
            }
        }
    }

    /**
     * This method is called when the text is changed, in case any subclasses
     * would like to know.
     *
     * Within <code>text</code>, the <code>lengthAfter</code> characters
     * beginning at <code>start</code> have just replaced old text that had
     * length <code>lengthBefore</code>. It is an error to attempt to make
     * changes to <code>text</code> from this callback.
     *
     * @param text
     * 		The text the TextView is displaying
     * @param start
     * 		The offset of the start of the range of the text that was
     * 		modified
     * @param lengthBefore
     * 		The length of the former text that has been replaced
     * @param lengthAfter
     * 		The length of the replacement modified text
     */
    protected void onTextChanged(java.lang.CharSequence text, int start, int lengthBefore, int lengthAfter) {
        // intentionally empty, template pattern method can be overridden by subclasses
    }

    /**
     * This method is called when the selection has changed, in case any
     * subclasses would like to know.
     *
     * @param selStart
     * 		The new selection start location.
     * @param selEnd
     * 		The new selection end location.
     */
    protected void onSelectionChanged(int selStart, int selEnd) {
        sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED);
    }

    /**
     * Adds a TextWatcher to the list of those whose methods are called
     * whenever this TextView's text changes.
     * <p>
     * In 1.0, the {@link TextWatcher#afterTextChanged} method was erroneously
     * not called after {@link #setText} calls.  Now, doing {@link #setText}
     * if there are any text changed listeners forces the buffer type to
     * Editable if it would not otherwise be and does call this method.
     */
    public void addTextChangedListener(android.text.TextWatcher watcher) {
        if (mListeners == null) {
            mListeners = new java.util.ArrayList<android.text.TextWatcher>();
        }
        mListeners.add(watcher);
    }

    /**
     * Removes the specified TextWatcher from the list of those whose
     * methods are called
     * whenever this TextView's text changes.
     */
    public void removeTextChangedListener(android.text.TextWatcher watcher) {
        if (mListeners != null) {
            int i = mListeners.indexOf(watcher);
            if (i >= 0) {
                mListeners.remove(i);
            }
        }
    }

    private void sendBeforeTextChanged(java.lang.CharSequence text, int start, int before, int after) {
        if (mListeners != null) {
            final java.util.ArrayList<android.text.TextWatcher> list = mListeners;
            final int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).beforeTextChanged(text, start, before, after);
            }
        }
        // The spans that are inside or intersect the modified region no longer make sense
        removeIntersectingNonAdjacentSpans(start, start + before, android.text.style.SpellCheckSpan.class);
        removeIntersectingNonAdjacentSpans(start, start + before, android.text.style.SuggestionSpan.class);
    }

    // Removes all spans that are inside or actually overlap the start..end range
    private <T> void removeIntersectingNonAdjacentSpans(int start, int end, java.lang.Class<T> type) {
        if (!(mText instanceof android.text.Editable))
            return;

        android.text.Editable text = ((android.text.Editable) (mText));
        T[] spans = text.getSpans(start, end, type);
        final int length = spans.length;
        for (int i = 0; i < length; i++) {
            final int spanStart = text.getSpanStart(spans[i]);
            final int spanEnd = text.getSpanEnd(spans[i]);
            if ((spanEnd == start) || (spanStart == end))
                break;

            text.removeSpan(spans[i]);
        }
    }

    void removeAdjacentSuggestionSpans(final int pos) {
        if (!(mText instanceof android.text.Editable))
            return;

        final android.text.Editable text = ((android.text.Editable) (mText));
        final android.text.style.SuggestionSpan[] spans = text.getSpans(pos, pos, android.text.style.SuggestionSpan.class);
        final int length = spans.length;
        for (int i = 0; i < length; i++) {
            final int spanStart = text.getSpanStart(spans[i]);
            final int spanEnd = text.getSpanEnd(spans[i]);
            if ((spanEnd == pos) || (spanStart == pos)) {
                if (android.widget.SpellChecker.haveWordBoundariesChanged(text, pos, pos, spanStart, spanEnd)) {
                    text.removeSpan(spans[i]);
                }
            }
        }
    }

    /**
     * Not private so it can be called from an inner class without going
     * through a thunk.
     */
    void sendOnTextChanged(java.lang.CharSequence text, int start, int before, int after) {
        if (mListeners != null) {
            final java.util.ArrayList<android.text.TextWatcher> list = mListeners;
            final int count = list.size();
            for (int i = 0; i < count; i++) {
                onTextChanged(text, start, before, after);
            }
        }
        if (mEditor != null)
            mEditor.sendOnTextChanged(start, before, after);

    }

    /**
     * Not private so it can be called from an inner class without going
     * through a thunk.
     */
    void sendAfterTextChanged(android.text.Editable text) {
        if (mListeners != null) {
            final java.util.ArrayList<android.text.TextWatcher> list = mListeners;
            final int count = list.size();
            for (int i = 0; i < count; i++) {
                list.get(i).afterTextChanged(text);
            }
        }
        notifyListeningManagersAfterTextChanged();
        hideErrorIfUnchanged();
    }

    /**
     * Notify managers (such as {@link AutofillManager}) that are interested in text changes.
     */
    private void notifyListeningManagersAfterTextChanged() {
        // Autofill
        if (isAutofillable()) {
            // It is important to not check whether the view is important for autofill
            // since the user can trigger autofill manually on not important views.
            final android.view.autofill.AutofillManager afm = mContext.getSystemService(android.view.autofill.AutofillManager.class);
            if (afm != null) {
                if (android.view.autofill.Helper.sVerbose) {
                    android.util.Log.v(TextView.LOG_TAG, "notifyAutoFillManagerAfterTextChanged");
                }
                afm.notifyValueChanged(this);
            }
        }
    }

    private boolean isAutofillable() {
        // It is important to not check whether the view is important for autofill
        // since the user can trigger autofill manually on not important views.
        return getAutofillType() != android.view.View.AUTOFILL_TYPE_NONE;
    }

    void updateAfterEdit() {
        invalidate();
        int curs = getSelectionStart();
        if ((curs >= 0) || ((mGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == android.view.Gravity.BOTTOM)) {
            registerForPreDraw();
        }
        checkForResize();
        if (curs >= 0) {
            mHighlightPathBogus = true;
            if (mEditor != null)
                mEditor.makeBlink();

            bringPointIntoView(curs);
        }
    }

    /**
     * Not private so it can be called from an inner class without going
     * through a thunk.
     */
    void handleTextChanged(java.lang.CharSequence buffer, int start, int before, int after) {
        TextView.sLastCutCopyOrTextChangedTime = 0;
        final android.widget.Editor.InputMethodState ims = (mEditor == null) ? null : mEditor.mInputMethodState;
        if ((ims == null) || (ims.mBatchEditNesting == 0)) {
            updateAfterEdit();
        }
        if (ims != null) {
            ims.mContentChanged = true;
            if (ims.mChangedStart < 0) {
                ims.mChangedStart = start;
                ims.mChangedEnd = start + before;
            } else {
                ims.mChangedStart = java.lang.Math.min(ims.mChangedStart, start);
                ims.mChangedEnd = java.lang.Math.max(ims.mChangedEnd, (start + before) - ims.mChangedDelta);
            }
            ims.mChangedDelta += after - before;
        }
        resetErrorChangedFlag();
        sendOnTextChanged(buffer, start, before, after);
        onTextChanged(buffer, start, before, after);
    }

    /**
     * Not private so it can be called from an inner class without going
     * through a thunk.
     */
    void spanChange(android.text.Spanned buf, java.lang.Object what, int oldStart, int newStart, int oldEnd, int newEnd) {
        // XXX Make the start and end move together if this ends up
        // spending too much time invalidating.
        boolean selChanged = false;
        int newSelStart = -1;
        int newSelEnd = -1;
        final android.widget.Editor.InputMethodState ims = (mEditor == null) ? null : mEditor.mInputMethodState;
        if (what == android.text.Selection.SELECTION_END) {
            selChanged = true;
            newSelEnd = newStart;
            if ((oldStart >= 0) || (newStart >= 0)) {
                invalidateCursor(android.text.Selection.getSelectionStart(buf), oldStart, newStart);
                checkForResize();
                registerForPreDraw();
                if (mEditor != null)
                    mEditor.makeBlink();

            }
        }
        if (what == android.text.Selection.SELECTION_START) {
            selChanged = true;
            newSelStart = newStart;
            if ((oldStart >= 0) || (newStart >= 0)) {
                int end = android.text.Selection.getSelectionEnd(buf);
                invalidateCursor(end, oldStart, newStart);
            }
        }
        if (selChanged) {
            mHighlightPathBogus = true;
            if ((mEditor != null) && (!isFocused()))
                mEditor.mSelectionMoved = true;

            if ((buf.getSpanFlags(what) & android.text.Spanned.SPAN_INTERMEDIATE) == 0) {
                if (newSelStart < 0) {
                    newSelStart = android.text.Selection.getSelectionStart(buf);
                }
                if (newSelEnd < 0) {
                    newSelEnd = android.text.Selection.getSelectionEnd(buf);
                }
                if (mEditor != null) {
                    mEditor.refreshTextActionMode();
                    if (((!hasSelection()) && (mEditor.getTextActionMode() == null)) && hasTransientState()) {
                        // User generated selection has been removed.
                        setHasTransientState(false);
                    }
                }
                onSelectionChanged(newSelStart, newSelEnd);
            }
        }
        if (((what instanceof android.text.style.UpdateAppearance) || (what instanceof android.text.style.ParagraphStyle)) || (what instanceof android.text.style.CharacterStyle)) {
            if ((ims == null) || (ims.mBatchEditNesting == 0)) {
                invalidate();
                mHighlightPathBogus = true;
                checkForResize();
            } else {
                ims.mContentChanged = true;
            }
            if (mEditor != null) {
                if (oldStart >= 0)
                    mEditor.invalidateTextDisplayList(mLayout, oldStart, oldEnd);

                if (newStart >= 0)
                    mEditor.invalidateTextDisplayList(mLayout, newStart, newEnd);

                mEditor.invalidateHandlesAndActionMode();
            }
        }
        if (android.text.method.MetaKeyKeyListener.isMetaTracker(buf, what)) {
            mHighlightPathBogus = true;
            if ((ims != null) && android.text.method.MetaKeyKeyListener.isSelectingMetaTracker(buf, what)) {
                ims.mSelectionModeChanged = true;
            }
            if (android.text.Selection.getSelectionStart(buf) >= 0) {
                if ((ims == null) || (ims.mBatchEditNesting == 0)) {
                    invalidateCursor();
                } else {
                    ims.mCursorChanged = true;
                }
            }
        }
        if (what instanceof android.text.ParcelableSpan) {
            // If this is a span that can be sent to a remote process,
            // the current extract editor would be interested in it.
            if ((ims != null) && (ims.mExtractedTextRequest != null)) {
                if (ims.mBatchEditNesting != 0) {
                    if (oldStart >= 0) {
                        if (ims.mChangedStart > oldStart) {
                            ims.mChangedStart = oldStart;
                        }
                        if (ims.mChangedStart > oldEnd) {
                            ims.mChangedStart = oldEnd;
                        }
                    }
                    if (newStart >= 0) {
                        if (ims.mChangedStart > newStart) {
                            ims.mChangedStart = newStart;
                        }
                        if (ims.mChangedStart > newEnd) {
                            ims.mChangedStart = newEnd;
                        }
                    }
                } else {
                    if (TextView.DEBUG_EXTRACT) {
                        android.util.Log.v(TextView.LOG_TAG, (((((((("Span change outside of batch: " + oldStart) + "-") + oldEnd) + ",") + newStart) + "-") + newEnd) + " ") + what);
                    }
                    ims.mContentChanged = true;
                }
            }
        }
        if ((((mEditor != null) && (mEditor.mSpellChecker != null)) && (newStart < 0)) && (what instanceof android.text.style.SpellCheckSpan)) {
            mEditor.mSpellChecker.onSpellCheckSpanRemoved(((android.text.style.SpellCheckSpan) (what)));
        }
    }

    @java.lang.Override
    protected void onFocusChanged(boolean focused, int direction, android.graphics.Rect previouslyFocusedRect) {
        if (isTemporarilyDetached()) {
            // If we are temporarily in the detach state, then do nothing.
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
            return;
        }
        if (mEditor != null)
            mEditor.onFocusChanged(focused, direction);

        if (focused) {
            if (mSpannable != null) {
                android.text.method.MetaKeyKeyListener.resetMetaState(mSpannable);
            }
        }
        startStopMarquee(focused);
        if (mTransformation != null) {
            mTransformation.onFocusChanged(this, mText, focused, direction, previouslyFocusedRect);
        }
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @java.lang.Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mEditor != null)
            mEditor.onWindowFocusChanged(hasWindowFocus);

        startStopMarquee(hasWindowFocus);
    }

    @java.lang.Override
    protected void onVisibilityChanged(android.view.View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if ((mEditor != null) && (visibility != android.view.View.VISIBLE)) {
            mEditor.hideCursorAndSpanControllers();
            stopTextActionMode();
        }
    }

    /**
     * Use {@link BaseInputConnection#removeComposingSpans
     * BaseInputConnection.removeComposingSpans()} to remove any IME composing
     * state from this text view.
     */
    public void clearComposingText() {
        if (mText instanceof android.text.Spannable) {
            android.view.inputmethod.BaseInputConnection.removeComposingSpans(mSpannable);
        }
    }

    @java.lang.Override
    public void setSelected(boolean selected) {
        boolean wasSelected = isSelected();
        super.setSelected(selected);
        if ((selected != wasSelected) && (mEllipsize == android.text.TextUtils.TruncateAt.MARQUEE)) {
            if (selected) {
                startMarquee();
            } else {
                stopMarquee();
            }
        }
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        final int action = event.getActionMasked();
        if (mEditor != null) {
            mEditor.onTouchEvent(event);
            if ((mEditor.mSelectionModifierCursorController != null) && mEditor.mSelectionModifierCursorController.isDragAcceleratorActive()) {
                return true;
            }
        }
        final boolean superResult = super.onTouchEvent(event);
        /* Don't handle the release after a long press, because it will move the selection away from
        whatever the menu action was trying to affect. If the long press should have triggered an
        insertion action mode, we can now actually show it.
         */
        if (((mEditor != null) && mEditor.mDiscardNextActionUp) && (action == android.view.MotionEvent.ACTION_UP)) {
            mEditor.mDiscardNextActionUp = false;
            if (mEditor.mIsInsertionActionModeStartPending) {
                mEditor.startInsertionActionMode();
                mEditor.mIsInsertionActionModeStartPending = false;
            }
            return superResult;
        }
        final boolean touchIsFinished = ((action == android.view.MotionEvent.ACTION_UP) && ((mEditor == null) || (!mEditor.mIgnoreActionUpEvent))) && isFocused();
        if (((((mMovement != null) || onCheckIsTextEditor()) && isEnabled()) && (mText instanceof android.text.Spannable)) && (mLayout != null)) {
            boolean handled = false;
            if (mMovement != null) {
                handled |= mMovement.onTouchEvent(this, mSpannable, event);
            }
            final boolean textIsSelectable = isTextSelectable();
            if (((touchIsFinished && mLinksClickable) && (mAutoLinkMask != 0)) && textIsSelectable) {
                // The LinkMovementMethod which should handle taps on links has not been installed
                // on non editable text that support text selection.
                // We reproduce its behavior here to open links for these.
                android.text.style.ClickableSpan[] links = mSpannable.getSpans(getSelectionStart(), getSelectionEnd(), android.text.style.ClickableSpan.class);
                if (links.length > 0) {
                    links[0].onClick(this);
                    handled = true;
                }
            }
            if (touchIsFinished && (isTextEditable() || textIsSelectable)) {
                // Show the IME, except when selecting in read-only text.
                final android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
                viewClicked(imm);
                if ((isTextEditable() && mEditor.mShowSoftInputOnFocus) && (imm != null)) {
                    imm.showSoftInput(this, 0);
                }
                // The above condition ensures that the mEditor is not null
                mEditor.onTouchUpEvent(event);
                handled = true;
            }
            if (handled) {
                return true;
            }
        }
        return superResult;
    }

    @java.lang.Override
    public boolean onGenericMotionEvent(android.view.MotionEvent event) {
        if (((mMovement != null) && (mText instanceof android.text.Spannable)) && (mLayout != null)) {
            try {
                if (mMovement.onGenericMotionEvent(this, mSpannable, event)) {
                    return true;
                }
            } catch (java.lang.AbstractMethodError ex) {
                // onGenericMotionEvent was added to the MovementMethod interface in API 12.
                // Ignore its absence in case third party applications implemented the
                // interface directly.
            }
        }
        return super.onGenericMotionEvent(event);
    }

    @java.lang.Override
    protected void onCreateContextMenu(android.view.ContextMenu menu) {
        if (mEditor != null) {
            mEditor.onCreateContextMenu(menu);
        }
    }

    @java.lang.Override
    public boolean showContextMenu() {
        if (mEditor != null) {
            mEditor.setContextMenuAnchor(java.lang.Float.NaN, java.lang.Float.NaN);
        }
        return super.showContextMenu();
    }

    @java.lang.Override
    public boolean showContextMenu(float x, float y) {
        if (mEditor != null) {
            mEditor.setContextMenuAnchor(x, y);
        }
        return super.showContextMenu(x, y);
    }

    /**
     *
     *
     * @return True iff this TextView contains a text that can be edited, or if this is
    a selectable TextView.
     */
    @android.annotation.UnsupportedAppUsage
    boolean isTextEditable() {
        return ((mText instanceof android.text.Editable) && onCheckIsTextEditor()) && isEnabled();
    }

    /**
     * Returns true, only while processing a touch gesture, if the initial
     * touch down event caused focus to move to the text view and as a result
     * its selection changed.  Only valid while processing the touch gesture
     * of interest, in an editable text view.
     */
    public boolean didTouchFocusSelect() {
        return (mEditor != null) && mEditor.mTouchFocusSelected;
    }

    @java.lang.Override
    public void cancelLongPress() {
        super.cancelLongPress();
        if (mEditor != null)
            mEditor.mIgnoreActionUpEvent = true;

    }

    @java.lang.Override
    public boolean onTrackballEvent(android.view.MotionEvent event) {
        if (((mMovement != null) && (mSpannable != null)) && (mLayout != null)) {
            if (mMovement.onTrackballEvent(this, mSpannable, event)) {
                return true;
            }
        }
        return super.onTrackballEvent(event);
    }

    /**
     * Sets the Scroller used for producing a scrolling animation
     *
     * @param s
     * 		A Scroller instance
     */
    public void setScroller(android.widget.Scroller s) {
        mScroller = s;
    }

    @java.lang.Override
    protected float getLeftFadingEdgeStrength() {
        if ((isMarqueeFadeEnabled() && (mMarquee != null)) && (!mMarquee.isStopped())) {
            final TextView.Marquee marquee = mMarquee;
            if (marquee.shouldDrawLeftFade()) {
                return getHorizontalFadingEdgeStrength(marquee.getScroll(), 0.0F);
            } else {
                return 0.0F;
            }
        } else
            if (getLineCount() == 1) {
                final float lineLeft = getLayout().getLineLeft(0);
                if (lineLeft > mScrollX)
                    return 0.0F;

                return getHorizontalFadingEdgeStrength(mScrollX, lineLeft);
            }

        return super.getLeftFadingEdgeStrength();
    }

    @java.lang.Override
    protected float getRightFadingEdgeStrength() {
        if ((isMarqueeFadeEnabled() && (mMarquee != null)) && (!mMarquee.isStopped())) {
            final TextView.Marquee marquee = mMarquee;
            return getHorizontalFadingEdgeStrength(marquee.getMaxFadeScroll(), marquee.getScroll());
        } else
            if (getLineCount() == 1) {
                final float rightEdge = mScrollX + ((getWidth() - getCompoundPaddingLeft()) - getCompoundPaddingRight());
                final float lineRight = getLayout().getLineRight(0);
                if (lineRight < rightEdge)
                    return 0.0F;

                return getHorizontalFadingEdgeStrength(rightEdge, lineRight);
            }

        return super.getRightFadingEdgeStrength();
    }

    /**
     * Calculates the fading edge strength as the ratio of the distance between two
     * horizontal positions to {@link View#getHorizontalFadingEdgeLength()}. Uses the absolute
     * value for the distance calculation.
     *
     * @param position1
     * 		A horizontal position.
     * @param position2
     * 		A horizontal position.
     * @return Fading edge strength between [0.0f, 1.0f].
     */
    @android.annotation.FloatRange(from = 0.0, to = 1.0)
    private float getHorizontalFadingEdgeStrength(float position1, float position2) {
        final int horizontalFadingEdgeLength = getHorizontalFadingEdgeLength();
        if (horizontalFadingEdgeLength == 0)
            return 0.0F;

        final float diff = java.lang.Math.abs(position1 - position2);
        if (diff > horizontalFadingEdgeLength)
            return 1.0F;

        return diff / horizontalFadingEdgeLength;
    }

    private boolean isMarqueeFadeEnabled() {
        return (mEllipsize == android.text.TextUtils.TruncateAt.MARQUEE) && (mMarqueeFadeMode != TextView.MARQUEE_FADE_SWITCH_SHOW_ELLIPSIS);
    }

    @java.lang.Override
    protected int computeHorizontalScrollRange() {
        if (mLayout != null) {
            return mSingleLine && ((mGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) == android.view.Gravity.LEFT) ? ((int) (mLayout.getLineWidth(0))) : mLayout.getWidth();
        }
        return super.computeHorizontalScrollRange();
    }

    @java.lang.Override
    protected int computeVerticalScrollRange() {
        if (mLayout != null) {
            return mLayout.getHeight();
        }
        return super.computeVerticalScrollRange();
    }

    @java.lang.Override
    protected int computeVerticalScrollExtent() {
        return (getHeight() - getCompoundPaddingTop()) - getCompoundPaddingBottom();
    }

    @java.lang.Override
    public void findViewsWithText(java.util.ArrayList<android.view.View> outViews, java.lang.CharSequence searched, int flags) {
        super.findViewsWithText(outViews, searched, flags);
        if ((((!outViews.contains(this)) && ((flags & android.view.View.FIND_VIEWS_WITH_TEXT) != 0)) && (!android.text.TextUtils.isEmpty(searched))) && (!android.text.TextUtils.isEmpty(mText))) {
            java.lang.String searchedLowerCase = searched.toString().toLowerCase();
            java.lang.String textLowerCase = mText.toString().toLowerCase();
            if (textLowerCase.contains(searchedLowerCase)) {
                outViews.add(this);
            }
        }
    }

    /**
     * Type of the text buffer that defines the characteristics of the text such as static,
     * styleable, or editable.
     */
    public enum BufferType {

        NORMAL,
        SPANNABLE,
        EDITABLE;}

    /**
     * Returns the TextView_textColor attribute from the TypedArray, if set, or
     * the TextAppearance_textColor from the TextView_textAppearance attribute,
     * if TextView_textColor was not set directly.
     *
     * @unknown 
     */
    public static android.content.res.ColorStateList getTextColors(android.content.Context context, android.content.res.TypedArray attrs) {
        if (attrs == null) {
            // Preserve behavior prior to removal of this API.
            throw new java.lang.NullPointerException();
        }
        // It's not safe to use this method from apps. The parameter 'attrs'
        // must have been obtained using the TextView filter array which is not
        // available to the SDK. As such, we grab a default TypedArray with the
        // right filter instead here.
        final android.content.res.TypedArray a = context.obtainStyledAttributes(R.styleable.TextView);
        android.content.res.ColorStateList colors = a.getColorStateList(R.styleable.TextView_textColor);
        if (colors == null) {
            final int ap = a.getResourceId(R.styleable.TextView_textAppearance, 0);
            if (ap != 0) {
                final android.content.res.TypedArray appearance = context.obtainStyledAttributes(ap, R.styleable.TextAppearance);
                colors = appearance.getColorStateList(R.styleable.TextAppearance_textColor);
                appearance.recycle();
            }
        }
        a.recycle();
        return colors;
    }

    /**
     * Returns the default color from the TextView_textColor attribute from the
     * AttributeSet, if set, or the default color from the
     * TextAppearance_textColor from the TextView_textAppearance attribute, if
     * TextView_textColor was not set directly.
     *
     * @unknown 
     */
    public static int getTextColor(android.content.Context context, android.content.res.TypedArray attrs, int def) {
        final android.content.res.ColorStateList colors = TextView.getTextColors(context, attrs);
        if (colors == null) {
            return def;
        } else {
            return colors.getDefaultColor();
        }
    }

    @java.lang.Override
    public boolean onKeyShortcut(int keyCode, android.view.KeyEvent event) {
        if (event.hasModifiers(android.view.KeyEvent.META_CTRL_ON)) {
            // Handle Ctrl-only shortcuts.
            switch (keyCode) {
                case android.view.KeyEvent.KEYCODE_A :
                    if (canSelectText()) {
                        return onTextContextMenuItem(TextView.ID_SELECT_ALL);
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_Z :
                    if (canUndo()) {
                        return onTextContextMenuItem(TextView.ID_UNDO);
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_X :
                    if (canCut()) {
                        return onTextContextMenuItem(TextView.ID_CUT);
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_C :
                    if (canCopy()) {
                        return onTextContextMenuItem(TextView.ID_COPY);
                    }
                    break;
                case android.view.KeyEvent.KEYCODE_V :
                    if (canPaste()) {
                        return onTextContextMenuItem(TextView.ID_PASTE);
                    }
                    break;
            }
        } else
            if (event.hasModifiers(android.view.KeyEvent.META_CTRL_ON | android.view.KeyEvent.META_SHIFT_ON)) {
                // Handle Ctrl-Shift shortcuts.
                switch (keyCode) {
                    case android.view.KeyEvent.KEYCODE_Z :
                        if (canRedo()) {
                            return onTextContextMenuItem(TextView.ID_REDO);
                        }
                        break;
                    case android.view.KeyEvent.KEYCODE_V :
                        if (canPaste()) {
                            return onTextContextMenuItem(TextView.ID_PASTE_AS_PLAIN_TEXT);
                        }
                }
            }

        return super.onKeyShortcut(keyCode, event);
    }

    /**
     * Unlike {@link #textCanBeSelected()}, this method is based on the <i>current</i> state of the
     * TextView. {@link #textCanBeSelected()} has to be true (this is one of the conditions to have
     * a selection controller (see {@link Editor#prepareCursorControllers()}), but this is not
     * sufficient.
     */
    boolean canSelectText() {
        return ((mText.length() != 0) && (mEditor != null)) && mEditor.hasSelectionController();
    }

    /**
     * Test based on the <i>intrinsic</i> charateristics of the TextView.
     * The text must be spannable and the movement method must allow for arbitary selection.
     *
     * See also {@link #canSelectText()}.
     */
    boolean textCanBeSelected() {
        // prepareCursorController() relies on this method.
        // If you change this condition, make sure prepareCursorController is called anywhere
        // the value of this condition might be changed.
        if ((mMovement == null) || (!mMovement.canSelectArbitrarily()))
            return false;

        return isTextEditable() || ((isTextSelectable() && (mText instanceof android.text.Spannable)) && isEnabled());
    }

    @android.annotation.UnsupportedAppUsage
    private java.util.Locale getTextServicesLocale(boolean allowNullLocale) {
        // Start fetching the text services locale asynchronously.
        updateTextServicesLocaleAsync();
        // If !allowNullLocale and there is no cached text services locale, just return the default
        // locale.
        return (mCurrentSpellCheckerLocaleCache == null) && (!allowNullLocale) ? java.util.Locale.getDefault() : mCurrentSpellCheckerLocaleCache;
    }

    /**
     * Associate {@link UserHandle} who is considered to be the logical owner of the text shown in
     * this {@link TextView}.
     *
     * <p>Most of applications should not worry about this.  Some privileged apps that host UI for
     * other apps may need to set this so that the system can user right user's resources and
     * services such as input methods and spell checkers.</p>
     *
     * @param user
     * 		{@link UserHandle} who is considered to be the owner of the text shown in this
     * 		{@link TextView}. {@code null} to reset {@link #mTextOperationUser}.
     * @unknown 
     */
    @android.annotation.RequiresPermission(android.Manifest.permission.INTERACT_ACROSS_USERS_FULL)
    public final void setTextOperationUser(@android.annotation.Nullable
    android.os.UserHandle user) {
        if (java.util.Objects.equals(mTextOperationUser, user)) {
            return;
        }
        if ((user != null) && (!java.lang.Process.myUserHandle().equals(user))) {
            // Just for preventing people from accidentally using this hidden API without
            // the required permission.  The same permission is also checked in the system server.
            if (getContext().checkSelfPermission(android.Manifest.permission.INTERACT_ACROSS_USERS_FULL) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                throw new java.lang.SecurityException(((("INTERACT_ACROSS_USERS_FULL is required." + " userId=") + user.getIdentifier()) + " callingUserId") + android.os.UserHandle.myUserId());
            }
        }
        mTextOperationUser = user;
        // Invalidate some resources
        mCurrentSpellCheckerLocaleCache = null;
        if (mEditor != null) {
            mEditor.onTextOperationUserChanged();
        }
    }

    @android.annotation.Nullable
    final android.view.textservice.TextServicesManager getTextServicesManagerForUser() {
        return getServiceManagerForUser("android", android.view.textservice.TextServicesManager.class);
    }

    @android.annotation.Nullable
    final android.content.ClipboardManager getClipboardManagerForUser() {
        return getServiceManagerForUser(getContext().getPackageName(), android.content.ClipboardManager.class);
    }

    @android.annotation.Nullable
    final <T> T getServiceManagerForUser(java.lang.String packageName, java.lang.Class<T> managerClazz) {
        if (mTextOperationUser == null) {
            return getContext().getSystemService(managerClazz);
        }
        try {
            android.content.Context context = /* flags */
            getContext().createPackageContextAsUser(packageName, 0, mTextOperationUser);
            return context.getSystemService(managerClazz);
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * Starts {@link Activity} as a text-operation user if it is specified with
     * {@link #setTextOperationUser(UserHandle)}.
     *
     * <p>Otherwise, just starts {@link Activity} with {@link Context#startActivity(Intent)}.</p>
     *
     * @param intent
     * 		The description of the activity to start.
     */
    void startActivityAsTextOperationUserIfNecessary(@android.annotation.NonNull
    android.content.Intent intent) {
        if (mTextOperationUser != null) {
            getContext().startActivityAsUser(intent, mTextOperationUser);
        } else {
            getContext().startActivity(intent);
        }
    }

    /**
     * This is a temporary method. Future versions may support multi-locale text.
     * Caveat: This method may not return the latest text services locale, but this should be
     * acceptable and it's more important to make this method asynchronous.
     *
     * @return The locale that should be used for a word iterator
    in this TextView, based on the current spell checker settings,
    the current IME's locale, or the system default locale.
    Please note that a word iterator in this TextView is different from another word iterator
    used by SpellChecker.java of TextView. This method should be used for the former.
     * @unknown 
     */
    // TODO: Support multi-locale
    // TODO: Update the text services locale immediately after the keyboard locale is switched
    // by catching intent of keyboard switch event
    public java.util.Locale getTextServicesLocale() {
        return /* allowNullLocale */
        getTextServicesLocale(false);
    }

    /**
     *
     *
     * @return {@code true} if this TextView is specialized for showing and interacting with the
    extracted text in a full-screen input method.
     * @unknown 
     */
    public boolean isInExtractedMode() {
        return false;
    }

    /**
     *
     *
     * @return {@code true} if this widget supports auto-sizing text and has been configured to
    auto-size.
     */
    private boolean isAutoSizeEnabled() {
        return supportsAutoSizeText() && (mAutoSizeTextType != TextView.AUTO_SIZE_TEXT_TYPE_NONE);
    }

    /**
     *
     *
     * @return {@code true} if this TextView supports auto-sizing text to fit within its container.
     * @unknown 
     */
    protected boolean supportsAutoSizeText() {
        return true;
    }

    /**
     * This is a temporary method. Future versions may support multi-locale text.
     * Caveat: This method may not return the latest spell checker locale, but this should be
     * acceptable and it's more important to make this method asynchronous.
     *
     * @return The locale that should be used for a spell checker in this TextView,
    based on the current spell checker settings, the current IME's locale, or the system default
    locale.
     * @unknown 
     */
    public java.util.Locale getSpellCheckerLocale() {
        return /* allowNullLocale */
        getTextServicesLocale(true);
    }

    private void updateTextServicesLocaleAsync() {
        // AsyncTask.execute() uses a serial executor which means we don't have
        // to lock around updateTextServicesLocaleLocked() to prevent it from
        // being executed n times in parallel.
        android.os.AsyncTask.execute(new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                updateTextServicesLocaleLocked();
            }
        });
    }

    @android.annotation.UnsupportedAppUsage
    private void updateTextServicesLocaleLocked() {
        final android.view.textservice.TextServicesManager textServicesManager = getTextServicesManagerForUser();
        if (textServicesManager == null) {
            return;
        }
        final android.view.textservice.SpellCheckerSubtype subtype = textServicesManager.getCurrentSpellCheckerSubtype(true);
        final java.util.Locale locale;
        if (subtype != null) {
            locale = subtype.getLocaleObject();
        } else {
            locale = null;
        }
        mCurrentSpellCheckerLocaleCache = locale;
    }

    void onLocaleChanged() {
        mEditor.onLocaleChanged();
    }

    /**
     * This method is used by the ArrowKeyMovementMethod to jump from one word to the other.
     * Made available to achieve a consistent behavior.
     *
     * @unknown 
     */
    public android.text.method.WordIterator getWordIterator() {
        if (mEditor != null) {
            return mEditor.getWordIterator();
        } else {
            return null;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onPopulateAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onPopulateAccessibilityEventInternal(event);
        final java.lang.CharSequence text = getTextForAccessibility();
        if (!android.text.TextUtils.isEmpty(text)) {
            event.getText().add(text);
        }
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return TextView.class.getName();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void onProvideStructure(@android.annotation.NonNull
    android.view.ViewStructure structure, @android.view.View.ViewStructureType
    int viewFor, int flags) {
        super.onProvideStructure(structure, viewFor, flags);
        final boolean isPassword = hasPasswordTransformationMethod() || TextView.isPasswordInputType(getInputType());
        if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL) {
            if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL) {
                structure.setDataIsSensitive(!mTextSetFromXmlOrResourceId);
            }
            if (mTextId != android.content.res.Resources.ID_NULL) {
                try {
                    structure.setTextIdEntry(getResources().getResourceEntryName(mTextId));
                } catch (android.content.res.Resources.NotFoundException e) {
                    if (android.view.autofill.Helper.sVerbose) {
                        android.util.Log.v(TextView.LOG_TAG, (("onProvideAutofillStructure(): cannot set name for text id " + mTextId) + ": ") + e.getMessage());
                    }
                }
            }
        }
        if ((!isPassword) || (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL)) {
            if (mLayout == null) {
                assumeLayout();
            }
            android.text.Layout layout = mLayout;
            final int lineCount = layout.getLineCount();
            if (lineCount <= 1) {
                // Simple case: this is a single line.
                final java.lang.CharSequence text = getText();
                if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL) {
                    structure.setText(text);
                } else {
                    structure.setText(text, getSelectionStart(), getSelectionEnd());
                }
            } else {
                // Complex case: multi-line, could be scrolled or within a scroll container
                // so some lines are not visible.
                final int[] tmpCords = new int[2];
                getLocationInWindow(tmpCords);
                final int topWindowLocation = tmpCords[1];
                android.view.View root = this;
                android.view.ViewParent viewParent = getParent();
                while (viewParent instanceof android.view.View) {
                    root = ((android.view.View) (viewParent));
                    viewParent = root.getParent();
                } 
                final int windowHeight = root.getHeight();
                final int topLine;
                final int bottomLine;
                if (topWindowLocation >= 0) {
                    // The top of the view is fully within its window; start text at line 0.
                    topLine = getLineAtCoordinateUnclamped(0);
                    bottomLine = getLineAtCoordinateUnclamped(windowHeight - 1);
                } else {
                    // The top of hte window has scrolled off the top of the window; figure out
                    // the starting line for this.
                    topLine = getLineAtCoordinateUnclamped(-topWindowLocation);
                    bottomLine = getLineAtCoordinateUnclamped((windowHeight - 1) - topWindowLocation);
                }
                // We want to return some contextual lines above/below the lines that are
                // actually visible.
                int expandedTopLine = topLine - ((bottomLine - topLine) / 2);
                if (expandedTopLine < 0) {
                    expandedTopLine = 0;
                }
                int expandedBottomLine = bottomLine + ((bottomLine - topLine) / 2);
                if (expandedBottomLine >= lineCount) {
                    expandedBottomLine = lineCount - 1;
                }
                // Convert lines into character offsets.
                int expandedTopChar = layout.getLineStart(expandedTopLine);
                int expandedBottomChar = layout.getLineEnd(expandedBottomLine);
                // Take into account selection -- if there is a selection, we need to expand
                // the text we are returning to include that selection.
                final int selStart = getSelectionStart();
                final int selEnd = getSelectionEnd();
                if (selStart < selEnd) {
                    if (selStart < expandedTopChar) {
                        expandedTopChar = selStart;
                    }
                    if (selEnd > expandedBottomChar) {
                        expandedBottomChar = selEnd;
                    }
                }
                // Get the text and trim it to the range we are reporting.
                java.lang.CharSequence text = getText();
                if ((expandedTopChar > 0) || (expandedBottomChar < text.length())) {
                    text = text.subSequence(expandedTopChar, expandedBottomChar);
                }
                if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL) {
                    structure.setText(text);
                } else {
                    structure.setText(text, selStart - expandedTopChar, selEnd - expandedTopChar);
                    final int[] lineOffsets = new int[(bottomLine - topLine) + 1];
                    final int[] lineBaselines = new int[(bottomLine - topLine) + 1];
                    final int baselineOffset = getBaselineOffset();
                    for (int i = topLine; i <= bottomLine; i++) {
                        lineOffsets[i - topLine] = layout.getLineStart(i);
                        lineBaselines[i - topLine] = layout.getLineBaseline(i) + baselineOffset;
                    }
                    structure.setTextLines(lineOffsets, lineBaselines);
                }
            }
            if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_ASSIST) {
                // Extract style information that applies to the TextView as a whole.
                int style = 0;
                int typefaceStyle = getTypefaceStyle();
                if ((typefaceStyle & android.graphics.Typeface.BOLD) != 0) {
                    style |= AssistStructure.ViewNode.TEXT_STYLE_BOLD;
                }
                if ((typefaceStyle & android.graphics.Typeface.ITALIC) != 0) {
                    style |= AssistStructure.ViewNode.TEXT_STYLE_ITALIC;
                }
                // Global styles can also be set via TextView.setPaintFlags().
                int paintFlags = mTextPaint.getFlags();
                if ((paintFlags & android.graphics.Paint.FAKE_BOLD_TEXT_FLAG) != 0) {
                    style |= AssistStructure.ViewNode.TEXT_STYLE_BOLD;
                }
                if ((paintFlags & android.graphics.Paint.UNDERLINE_TEXT_FLAG) != 0) {
                    style |= AssistStructure.ViewNode.TEXT_STYLE_UNDERLINE;
                }
                if ((paintFlags & android.graphics.Paint.STRIKE_THRU_TEXT_FLAG) != 0) {
                    style |= AssistStructure.ViewNode.TEXT_STYLE_STRIKE_THRU;
                }
                // TextView does not have its own text background color. A background is either part
                // of the View (and can be any drawable) or a BackgroundColorSpan inside the text.
                /* bgColor */
                structure.setTextStyle(getTextSize(), getCurrentTextColor(), AssistStructure.ViewNode.TEXT_COLOR_UNDEFINED, style);
            }
            if (viewFor == android.view.View.VIEW_STRUCTURE_FOR_AUTOFILL) {
                structure.setMinTextEms(getMinEms());
                structure.setMaxTextEms(getMaxEms());
                int maxLength = -1;
                for (android.text.InputFilter filter : getFilters()) {
                    if (filter instanceof android.text.InputFilter.LengthFilter) {
                        maxLength = ((android.text.InputFilter.LengthFilter) (filter)).getMax();
                        break;
                    }
                }
                structure.setMaxTextLength(maxLength);
            }
        }
        structure.setHint(getHint());
        structure.setInputType(getInputType());
    }

    boolean canRequestAutofill() {
        if (!isAutofillable()) {
            return false;
        }
        final android.view.autofill.AutofillManager afm = mContext.getSystemService(android.view.autofill.AutofillManager.class);
        if (afm != null) {
            return afm.isEnabled();
        }
        return false;
    }

    private void requestAutofill() {
        final android.view.autofill.AutofillManager afm = mContext.getSystemService(android.view.autofill.AutofillManager.class);
        if (afm != null) {
            afm.requestAutofill(this);
        }
    }

    @java.lang.Override
    public void autofill(android.view.autofill.AutofillValue value) {
        if ((!value.isText()) || (!isTextEditable())) {
            android.util.Log.w(TextView.LOG_TAG, (value + " could not be autofilled into ") + this);
            return;
        }
        final java.lang.CharSequence autofilledValue = value.getTextValue();
        // First autofill it...
        setText(autofilledValue, mBufferType, true, 0);
        // ...then move cursor to the end.
        final java.lang.CharSequence text = getText();
        if (text instanceof android.text.Spannable) {
            android.text.Selection.setSelection(((android.text.Spannable) (text)), text.length());
        }
    }

    @java.lang.Override
    @android.view.View.AutofillType
    public int getAutofillType() {
        return isTextEditable() ? android.view.View.AUTOFILL_TYPE_TEXT : android.view.View.AUTOFILL_TYPE_NONE;
    }

    /**
     * Gets the {@link TextView}'s current text for AutoFill. The value is trimmed to 100K
     * {@code char}s if longer.
     *
     * @return current text, {@code null} if the text is not editable
     * @see View#getAutofillValue()
     */
    @java.lang.Override
    @android.annotation.Nullable
    public android.view.autofill.AutofillValue getAutofillValue() {
        if (isTextEditable()) {
            final java.lang.CharSequence text = android.text.TextUtils.trimToParcelableSize(getText());
            return android.view.autofill.AutofillValue.forText(text);
        }
        return null;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityEventInternal(android.view.accessibility.AccessibilityEvent event) {
        super.onInitializeAccessibilityEventInternal(event);
        final boolean isPassword = hasPasswordTransformationMethod();
        event.setPassword(isPassword);
        if (event.getEventType() == android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_SELECTION_CHANGED) {
            event.setFromIndex(android.text.Selection.getSelectionStart(mText));
            event.setToIndex(android.text.Selection.getSelectionEnd(mText));
            event.setItemCount(mText.length());
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onInitializeAccessibilityNodeInfoInternal(android.view.accessibility.AccessibilityNodeInfo info) {
        super.onInitializeAccessibilityNodeInfoInternal(info);
        final boolean isPassword = hasPasswordTransformationMethod();
        info.setPassword(isPassword);
        info.setText(getTextForAccessibility());
        info.setHintText(mHint);
        info.setShowingHintText(isShowingHint());
        if (mBufferType == TextView.BufferType.EDITABLE) {
            info.setEditable(true);
            if (isEnabled()) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_TEXT);
            }
        }
        if (mEditor != null) {
            info.setInputType(mEditor.mInputType);
            if (mEditor.mError != null) {
                info.setContentInvalid(true);
                info.setError(mEditor.mError);
            }
        }
        if (!android.text.TextUtils.isEmpty(mText)) {
            info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY);
            info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY);
            info.setMovementGranularities((((android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_CHARACTER | android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_WORD) | android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE) | android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PARAGRAPH) | android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE);
            info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_SELECTION);
            info.setAvailableExtraData(java.util.Arrays.asList(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY));
        }
        if (isFocused()) {
            if (canCopy()) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_COPY);
            }
            if (canPaste()) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_PASTE);
            }
            if (canCut()) {
                info.addAction(android.view.accessibility.AccessibilityNodeInfo.ACTION_CUT);
            }
            if (canShare()) {
                info.addAction(new android.view.accessibility.AccessibilityNodeInfo.AccessibilityAction(TextView.ACCESSIBILITY_ACTION_SHARE, getResources().getString(com.android.internal.R.string.share)));
            }
            if (canProcessText()) {
                // also implies mEditor is not null.
                mEditor.mProcessTextIntentActionsHandler.onInitializeAccessibilityNodeInfo(info);
            }
        }
        // Check for known input filter types.
        final int numFilters = mFilters.length;
        for (int i = 0; i < numFilters; i++) {
            final android.text.InputFilter filter = mFilters[i];
            if (filter instanceof android.text.InputFilter.LengthFilter) {
                info.setMaxTextLength(((android.text.InputFilter.LengthFilter) (filter)).getMax());
            }
        }
        if (!isSingleLine()) {
            info.setMultiLine(true);
        }
    }

    @java.lang.Override
    public void addExtraDataToAccessibilityNodeInfo(android.view.accessibility.AccessibilityNodeInfo info, java.lang.String extraDataKey, android.os.Bundle arguments) {
        // The only extra data we support requires arguments.
        if (arguments == null) {
            return;
        }
        if (extraDataKey.equals(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_KEY)) {
            int positionInfoStartIndex = arguments.getInt(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_START_INDEX, -1);
            int positionInfoLength = arguments.getInt(android.view.accessibility.AccessibilityNodeInfo.EXTRA_DATA_TEXT_CHARACTER_LOCATION_ARG_LENGTH, -1);
            if (((positionInfoLength <= 0) || (positionInfoStartIndex < 0)) || (positionInfoStartIndex >= mText.length())) {
                android.util.Log.e(TextView.LOG_TAG, "Invalid arguments for accessibility character locations");
                return;
            }
            android.graphics.RectF[] boundingRects = new android.graphics.RectF[positionInfoLength];
            final android.view.inputmethod.CursorAnchorInfo.Builder builder = new android.view.inputmethod.CursorAnchorInfo.Builder();
            populateCharacterBounds(builder, positionInfoStartIndex, positionInfoStartIndex + positionInfoLength, viewportToContentHorizontalOffset(), viewportToContentVerticalOffset());
            android.view.inputmethod.CursorAnchorInfo cursorAnchorInfo = builder.setMatrix(null).build();
            for (int i = 0; i < positionInfoLength; i++) {
                int flags = cursorAnchorInfo.getCharacterBoundsFlags(positionInfoStartIndex + i);
                if ((flags & android.view.inputmethod.CursorAnchorInfo.FLAG_HAS_VISIBLE_REGION) == android.view.inputmethod.CursorAnchorInfo.FLAG_HAS_VISIBLE_REGION) {
                    android.graphics.RectF bounds = cursorAnchorInfo.getCharacterBounds(positionInfoStartIndex + i);
                    if (bounds != null) {
                        mapRectFromViewToScreenCoords(bounds, true);
                        boundingRects[i] = bounds;
                    }
                }
            }
            info.getExtras().putParcelableArray(extraDataKey, boundingRects);
        }
    }

    /**
     * Populate requested character bounds in a {@link CursorAnchorInfo.Builder}
     *
     * @param builder
     * 		The builder to populate
     * @param startIndex
     * 		The starting character index to populate
     * @param endIndex
     * 		The ending character index to populate
     * @param viewportToContentHorizontalOffset
     * 		The horizontal offset from the viewport to the
     * 		content
     * @param viewportToContentVerticalOffset
     * 		The vertical offset from the viewport to the content
     * @unknown 
     */
    public void populateCharacterBounds(android.view.inputmethod.CursorAnchorInfo.Builder builder, int startIndex, int endIndex, float viewportToContentHorizontalOffset, float viewportToContentVerticalOffset) {
        final int minLine = mLayout.getLineForOffset(startIndex);
        final int maxLine = mLayout.getLineForOffset(endIndex - 1);
        for (int line = minLine; line <= maxLine; ++line) {
            final int lineStart = mLayout.getLineStart(line);
            final int lineEnd = mLayout.getLineEnd(line);
            final int offsetStart = java.lang.Math.max(lineStart, startIndex);
            final int offsetEnd = java.lang.Math.min(lineEnd, endIndex);
            final boolean ltrLine = mLayout.getParagraphDirection(line) == android.text.Layout.DIR_LEFT_TO_RIGHT;
            final float[] widths = new float[offsetEnd - offsetStart];
            mLayout.getPaint().getTextWidths(mTransformed, offsetStart, offsetEnd, widths);
            final float top = mLayout.getLineTop(line);
            final float bottom = mLayout.getLineBottom(line);
            for (int offset = offsetStart; offset < offsetEnd; ++offset) {
                final float charWidth = widths[offset - offsetStart];
                final boolean isRtl = mLayout.isRtlCharAt(offset);
                final float primary = mLayout.getPrimaryHorizontal(offset);
                final float secondary = mLayout.getSecondaryHorizontal(offset);
                // TODO: This doesn't work perfectly for text with custom styles and
                // TAB chars.
                final float left;
                final float right;
                if (ltrLine) {
                    if (isRtl) {
                        left = secondary - charWidth;
                        right = secondary;
                    } else {
                        left = primary;
                        right = primary + charWidth;
                    }
                } else {
                    if (!isRtl) {
                        left = secondary;
                        right = secondary + charWidth;
                    } else {
                        left = primary - charWidth;
                        right = primary;
                    }
                }
                // TODO: Check top-right and bottom-left as well.
                final float localLeft = left + viewportToContentHorizontalOffset;
                final float localRight = right + viewportToContentHorizontalOffset;
                final float localTop = top + viewportToContentVerticalOffset;
                final float localBottom = bottom + viewportToContentVerticalOffset;
                final boolean isTopLeftVisible = isPositionVisible(localLeft, localTop);
                final boolean isBottomRightVisible = isPositionVisible(localRight, localBottom);
                int characterBoundsFlags = 0;
                if (isTopLeftVisible || isBottomRightVisible) {
                    characterBoundsFlags |= android.view.inputmethod.CursorAnchorInfo.FLAG_HAS_VISIBLE_REGION;
                }
                if ((!isTopLeftVisible) || (!isBottomRightVisible)) {
                    characterBoundsFlags |= android.view.inputmethod.CursorAnchorInfo.FLAG_HAS_INVISIBLE_REGION;
                }
                if (isRtl) {
                    characterBoundsFlags |= android.view.inputmethod.CursorAnchorInfo.FLAG_IS_RTL;
                }
                // Here offset is the index in Java chars.
                builder.addCharacterBounds(offset, localLeft, localTop, localRight, localBottom, characterBoundsFlags);
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isPositionVisible(final float positionX, final float positionY) {
        synchronized(TextView.TEMP_POSITION) {
            final float[] position = TextView.TEMP_POSITION;
            position[0] = positionX;
            position[1] = positionY;
            android.view.View view = this;
            while (view != null) {
                if (view != this) {
                    // Local scroll is already taken into account in positionX/Y
                    position[0] -= view.getScrollX();
                    position[1] -= view.getScrollY();
                }
                if ((((position[0] < 0) || (position[1] < 0)) || (position[0] > view.getWidth())) || (position[1] > view.getHeight())) {
                    return false;
                }
                if (!view.getMatrix().isIdentity()) {
                    view.getMatrix().mapPoints(position);
                }
                position[0] += view.getLeft();
                position[1] += view.getTop();
                final android.view.ViewParent parent = view.getParent();
                if (parent instanceof android.view.View) {
                    view = ((android.view.View) (parent));
                } else {
                    // We've reached the ViewRoot, stop iterating
                    view = null;
                }
            } 
        }
        // We've been able to walk up the view hierarchy and the position was never clipped
        return true;
    }

    /**
     * Performs an accessibility action after it has been offered to the
     * delegate.
     *
     * @unknown 
     */
    @java.lang.Override
    public boolean performAccessibilityActionInternal(int action, android.os.Bundle arguments) {
        if ((mEditor != null) && mEditor.mProcessTextIntentActionsHandler.performAccessibilityAction(action)) {
            return true;
        }
        switch (action) {
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_CLICK :
                {
                    return performAccessibilityActionClick(arguments);
                }
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_COPY :
                {
                    if (isFocused() && canCopy()) {
                        if (onTextContextMenuItem(TextView.ID_COPY)) {
                            return true;
                        }
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_PASTE :
                {
                    if (isFocused() && canPaste()) {
                        if (onTextContextMenuItem(TextView.ID_PASTE)) {
                            return true;
                        }
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_CUT :
                {
                    if (isFocused() && canCut()) {
                        if (onTextContextMenuItem(TextView.ID_CUT)) {
                            return true;
                        }
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_SELECTION :
                {
                    ensureIterableTextForAccessibilitySelectable();
                    java.lang.CharSequence text = getIterableTextForAccessibility();
                    if (text == null) {
                        return false;
                    }
                    final int start = (arguments != null) ? arguments.getInt(android.view.accessibility.AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_START_INT, -1) : -1;
                    final int end = (arguments != null) ? arguments.getInt(android.view.accessibility.AccessibilityNodeInfo.ACTION_ARGUMENT_SELECTION_END_INT, -1) : -1;
                    if ((getSelectionStart() != start) || (getSelectionEnd() != end)) {
                        // No arguments clears the selection.
                        if ((start == end) && (end == (-1))) {
                            android.text.Selection.removeSelection(((android.text.Spannable) (text)));
                            return true;
                        }
                        if (((start >= 0) && (start <= end)) && (end <= text.length())) {
                            android.text.Selection.setSelection(((android.text.Spannable) (text)), start, end);
                            // Make sure selection mode is engaged.
                            if (mEditor != null) {
                                mEditor.startSelectionActionModeAsync(false);
                            }
                            return true;
                        }
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_NEXT_AT_MOVEMENT_GRANULARITY :
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_PREVIOUS_AT_MOVEMENT_GRANULARITY :
                {
                    ensureIterableTextForAccessibilitySelectable();
                    return super.performAccessibilityActionInternal(action, arguments);
                }
            case TextView.ACCESSIBILITY_ACTION_SHARE :
                {
                    if (isFocused() && canShare()) {
                        if (onTextContextMenuItem(TextView.ID_SHARE)) {
                            return true;
                        }
                    }
                }
                return false;
            case android.view.accessibility.AccessibilityNodeInfo.ACTION_SET_TEXT :
                {
                    if ((!isEnabled()) || (mBufferType != TextView.BufferType.EDITABLE)) {
                        return false;
                    }
                    java.lang.CharSequence text = (arguments != null) ? arguments.getCharSequence(android.view.accessibility.AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE) : null;
                    setText(text);
                    if (mText != null) {
                        int updatedTextLength = mText.length();
                        if (updatedTextLength > 0) {
                            android.text.Selection.setSelection(mSpannable, updatedTextLength);
                        }
                    }
                }
                return true;
            default :
                {
                    return super.performAccessibilityActionInternal(action, arguments);
                }
        }
    }

    private boolean performAccessibilityActionClick(android.os.Bundle arguments) {
        boolean handled = false;
        if (!isEnabled()) {
            return false;
        }
        if (isClickable() || isLongClickable()) {
            // Simulate View.onTouchEvent for an ACTION_UP event
            if (isFocusable() && (!isFocused())) {
                requestFocus();
            }
            performClick();
            handled = true;
        }
        // Show the IME, except when selecting in read-only text.
        if ((((((mMovement != null) || onCheckIsTextEditor()) && hasSpannableText()) && (mLayout != null)) && (isTextEditable() || isTextSelectable())) && isFocused()) {
            final android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
            viewClicked(imm);
            if (((!isTextSelectable()) && mEditor.mShowSoftInputOnFocus) && (imm != null)) {
                handled |= imm.showSoftInput(this, 0);
            }
        }
        return handled;
    }

    private boolean hasSpannableText() {
        return (mText != null) && (mText instanceof android.text.Spannable);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void sendAccessibilityEventInternal(int eventType) {
        if ((eventType == android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED) && (mEditor != null)) {
            mEditor.mProcessTextIntentActionsHandler.initializeAccessibilityActions();
        }
        super.sendAccessibilityEventInternal(eventType);
    }

    @java.lang.Override
    public void sendAccessibilityEventUnchecked(android.view.accessibility.AccessibilityEvent event) {
        // Do not send scroll events since first they are not interesting for
        // accessibility and second such events a generated too frequently.
        // For details see the implementation of bringTextIntoView().
        if (event.getEventType() == android.view.accessibility.AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            return;
        }
        super.sendAccessibilityEventUnchecked(event);
    }

    /**
     * Returns the text that should be exposed to accessibility services.
     * <p>
     * This approximates what is displayed visually. If the user has specified
     * that accessibility services should speak passwords, this method will
     * bypass any password transformation method and return unobscured text.
     *
     * @return the text that should be exposed to accessibility services, may
    be {@code null} if no text is set
     */
    @android.annotation.Nullable
    @android.annotation.UnsupportedAppUsage
    private java.lang.CharSequence getTextForAccessibility() {
        // If the text is empty, we must be showing the hint text.
        if (android.text.TextUtils.isEmpty(mText)) {
            return mHint;
        }
        // Otherwise, return whatever text is being displayed.
        return android.text.TextUtils.trimToParcelableSize(mTransformed);
    }

    void sendAccessibilityEventTypeViewTextChanged(java.lang.CharSequence beforeText, int fromIndex, int removedCount, int addedCount) {
        android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        event.setFromIndex(fromIndex);
        event.setRemovedCount(removedCount);
        event.setAddedCount(addedCount);
        event.setBeforeText(beforeText);
        sendAccessibilityEventUnchecked(event);
    }

    private android.view.inputmethod.InputMethodManager getInputMethodManager() {
        return getContext().getSystemService(android.view.inputmethod.InputMethodManager.class);
    }

    /**
     * Returns whether this text view is a current input method target.  The
     * default implementation just checks with {@link InputMethodManager}.
     *
     * @return True if the TextView is a current input method target; false otherwise.
     */
    public boolean isInputMethodTarget() {
        android.view.inputmethod.InputMethodManager imm = getInputMethodManager();
        return (imm != null) && imm.isActive(this);
    }

    static final int ID_SELECT_ALL = android.R.id.selectAll;

    static final int ID_UNDO = android.R.id.undo;

    static final int ID_REDO = android.R.id.redo;

    static final int ID_CUT = android.R.id.cut;

    static final int ID_COPY = android.R.id.copy;

    static final int ID_PASTE = android.R.id.paste;

    static final int ID_SHARE = android.R.id.shareText;

    static final int ID_PASTE_AS_PLAIN_TEXT = android.R.id.pasteAsPlainText;

    static final int ID_REPLACE = android.R.id.replaceText;

    static final int ID_ASSIST = android.R.id.textAssist;

    static final int ID_AUTOFILL = android.R.id.autofill;

    /**
     * Called when a context menu option for the text view is selected.  Currently
     * this will be one of {@link android.R.id#selectAll}, {@link android.R.id#cut},
     * {@link android.R.id#copy}, {@link android.R.id#paste} or {@link android.R.id#shareText}.
     *
     * @return true if the context menu item action was performed.
     */
    public boolean onTextContextMenuItem(int id) {
        int min = 0;
        int max = mText.length();
        if (isFocused()) {
            final int selStart = getSelectionStart();
            final int selEnd = getSelectionEnd();
            min = java.lang.Math.max(0, java.lang.Math.min(selStart, selEnd));
            max = java.lang.Math.max(0, java.lang.Math.max(selStart, selEnd));
        }
        switch (id) {
            case TextView.ID_SELECT_ALL :
                final boolean hadSelection = hasSelection();
                selectAllText();
                if ((mEditor != null) && hadSelection) {
                    mEditor.invalidateActionModeAsync();
                }
                return true;
            case TextView.ID_UNDO :
                if (mEditor != null) {
                    mEditor.undo();
                }
                return true;// Returns true even if nothing was undone.

            case TextView.ID_REDO :
                if (mEditor != null) {
                    mEditor.redo();
                }
                return true;// Returns true even if nothing was undone.

            case TextView.ID_PASTE :
                /* withFormatting */
                paste(min, max, true);
                return true;
            case TextView.ID_PASTE_AS_PLAIN_TEXT :
                /* withFormatting */
                paste(min, max, false);
                return true;
            case TextView.ID_CUT :
                final android.content.ClipData cutData = android.content.ClipData.newPlainText(null, getTransformedText(min, max));
                if (setPrimaryClip(cutData)) {
                    deleteText_internal(min, max);
                } else {
                    android.widget.Toast.makeText(getContext(), com.android.internal.R.string.failed_to_copy_to_clipboard, android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            case TextView.ID_COPY :
                // For link action mode in a non-selectable/non-focusable TextView,
                // make sure that we set the appropriate min/max.
                final int selStart = getSelectionStart();
                final int selEnd = getSelectionEnd();
                min = java.lang.Math.max(0, java.lang.Math.min(selStart, selEnd));
                max = java.lang.Math.max(0, java.lang.Math.max(selStart, selEnd));
                final android.content.ClipData copyData = android.content.ClipData.newPlainText(null, getTransformedText(min, max));
                if (setPrimaryClip(copyData)) {
                    stopTextActionMode();
                } else {
                    android.widget.Toast.makeText(getContext(), com.android.internal.R.string.failed_to_copy_to_clipboard, android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            case TextView.ID_REPLACE :
                if (mEditor != null) {
                    mEditor.replace();
                }
                return true;
            case TextView.ID_SHARE :
                shareSelectedText();
                return true;
            case TextView.ID_AUTOFILL :
                requestAutofill();
                stopTextActionMode();
                return true;
        }
        return false;
    }

    @android.annotation.UnsupportedAppUsage
    java.lang.CharSequence getTransformedText(int start, int end) {
        return removeSuggestionSpans(mTransformed.subSequence(start, end));
    }

    @java.lang.Override
    public boolean performLongClick() {
        boolean handled = false;
        boolean performedHapticFeedback = false;
        if (mEditor != null) {
            mEditor.mIsBeingLongClicked = true;
        }
        if (super.performLongClick()) {
            handled = true;
            performedHapticFeedback = true;
        }
        if (mEditor != null) {
            handled |= mEditor.performLongClick(handled);
            mEditor.mIsBeingLongClicked = false;
        }
        if (handled) {
            if (!performedHapticFeedback) {
                performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
            }
            if (mEditor != null)
                mEditor.mDiscardNextActionUp = true;

        } else {
            com.android.internal.logging.MetricsLogger.action(mContext, MetricsEvent.TEXT_LONGPRESS, android.widget.TextViewMetrics.SUBTYPE_LONG_PRESS_OTHER);
        }
        return handled;
    }

    @java.lang.Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert) {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (mEditor != null) {
            mEditor.onScrollChanged();
        }
    }

    /**
     * Return whether or not suggestions are enabled on this TextView. The suggestions are generated
     * by the IME or by the spell checker as the user types. This is done by adding
     * {@link SuggestionSpan}s to the text.
     *
     * When suggestions are enabled (default), this list of suggestions will be displayed when the
     * user asks for them on these parts of the text. This value depends on the inputType of this
     * TextView.
     *
     * The class of the input type must be {@link InputType#TYPE_CLASS_TEXT}.
     *
     * In addition, the type variation must be one of
     * {@link InputType#TYPE_TEXT_VARIATION_NORMAL},
     * {@link InputType#TYPE_TEXT_VARIATION_EMAIL_SUBJECT},
     * {@link InputType#TYPE_TEXT_VARIATION_LONG_MESSAGE},
     * {@link InputType#TYPE_TEXT_VARIATION_SHORT_MESSAGE} or
     * {@link InputType#TYPE_TEXT_VARIATION_WEB_EDIT_TEXT}.
     *
     * And finally, the {@link InputType#TYPE_TEXT_FLAG_NO_SUGGESTIONS} flag must <i>not</i> be set.
     *
     * @return true if the suggestions popup window is enabled, based on the inputType.
     */
    public boolean isSuggestionsEnabled() {
        if (mEditor == null)
            return false;

        if ((mEditor.mInputType & android.text.InputType.TYPE_MASK_CLASS) != android.text.InputType.TYPE_CLASS_TEXT) {
            return false;
        }
        if ((mEditor.mInputType & android.text.InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS) > 0)
            return false;

        final int variation = mEditor.mInputType & TYPE_MASK_VARIATION;
        return ((((variation == TYPE_TEXT_VARIATION_NORMAL) || (variation == TYPE_TEXT_VARIATION_EMAIL_SUBJECT)) || (variation == TYPE_TEXT_VARIATION_LONG_MESSAGE)) || (variation == TYPE_TEXT_VARIATION_SHORT_MESSAGE)) || (variation == TYPE_TEXT_VARIATION_WEB_EDIT_TEXT);
    }

    /**
     * If provided, this ActionMode.Callback will be used to create the ActionMode when text
     * selection is initiated in this View.
     *
     * <p>The standard implementation populates the menu with a subset of Select All, Cut, Copy,
     * Paste, Replace and Share actions, depending on what this View supports.
     *
     * <p>A custom implementation can add new entries in the default menu in its
     * {@link android.view.ActionMode.Callback#onPrepareActionMode(ActionMode, android.view.Menu)}
     * method. The default actions can also be removed from the menu using
     * {@link android.view.Menu#removeItem(int)} and passing {@link android.R.id#selectAll},
     * {@link android.R.id#cut}, {@link android.R.id#copy}, {@link android.R.id#paste},
     * {@link android.R.id#replaceText} or {@link android.R.id#shareText} ids as parameters.
     *
     * <p>Returning false from
     * {@link android.view.ActionMode.Callback#onCreateActionMode(ActionMode, android.view.Menu)}
     * will prevent the action mode from being started.
     *
     * <p>Action click events should be handled by the custom implementation of
     * {@link android.view.ActionMode.Callback#onActionItemClicked(ActionMode,
     * android.view.MenuItem)}.
     *
     * <p>Note that text selection mode is not started when a TextView receives focus and the
     * {@link android.R.attr#selectAllOnFocus} flag has been set. The content is highlighted in
     * that case, to allow for quick replacement.
     */
    public void setCustomSelectionActionModeCallback(android.view.ActionMode.Callback actionModeCallback) {
        createEditorIfNeeded();
        mEditor.mCustomSelectionActionModeCallback = actionModeCallback;
    }

    /**
     * Retrieves the value set in {@link #setCustomSelectionActionModeCallback}. Default is null.
     *
     * @return The current custom selection callback.
     */
    public android.view.ActionMode.Callback getCustomSelectionActionModeCallback() {
        return mEditor == null ? null : mEditor.mCustomSelectionActionModeCallback;
    }

    /**
     * If provided, this ActionMode.Callback will be used to create the ActionMode when text
     * insertion is initiated in this View.
     * The standard implementation populates the menu with a subset of Select All,
     * Paste and Replace actions, depending on what this View supports.
     *
     * <p>A custom implementation can add new entries in the default menu in its
     * {@link android.view.ActionMode.Callback#onPrepareActionMode(android.view.ActionMode,
     * android.view.Menu)} method. The default actions can also be removed from the menu using
     * {@link android.view.Menu#removeItem(int)} and passing {@link android.R.id#selectAll},
     * {@link android.R.id#paste} or {@link android.R.id#replaceText} ids as parameters.</p>
     *
     * <p>Returning false from
     * {@link android.view.ActionMode.Callback#onCreateActionMode(android.view.ActionMode,
     * android.view.Menu)} will prevent the action mode from being started.</p>
     *
     * <p>Action click events should be handled by the custom implementation of
     * {@link android.view.ActionMode.Callback#onActionItemClicked(android.view.ActionMode,
     * android.view.MenuItem)}.</p>
     *
     * <p>Note that text insertion mode is not started when a TextView receives focus and the
     * {@link android.R.attr#selectAllOnFocus} flag has been set.</p>
     */
    public void setCustomInsertionActionModeCallback(android.view.ActionMode.Callback actionModeCallback) {
        createEditorIfNeeded();
        mEditor.mCustomInsertionActionModeCallback = actionModeCallback;
    }

    /**
     * Retrieves the value set in {@link #setCustomInsertionActionModeCallback}. Default is null.
     *
     * @return The current custom insertion callback.
     */
    public android.view.ActionMode.Callback getCustomInsertionActionModeCallback() {
        return mEditor == null ? null : mEditor.mCustomInsertionActionModeCallback;
    }

    /**
     * Sets the {@link TextClassifier} for this TextView.
     */
    public void setTextClassifier(@android.annotation.Nullable
    android.view.textclassifier.TextClassifier textClassifier) {
        mTextClassifier = textClassifier;
    }

    /**
     * Returns the {@link TextClassifier} used by this TextView.
     * If no TextClassifier has been set, this TextView uses the default set by the
     * {@link TextClassificationManager}.
     */
    @android.annotation.NonNull
    public android.view.textclassifier.TextClassifier getTextClassifier() {
        if (mTextClassifier == null) {
            final android.view.textclassifier.TextClassificationManager tcm = mContext.getSystemService(android.view.textclassifier.TextClassificationManager.class);
            if (tcm != null) {
                return tcm.getTextClassifier();
            }
            return android.view.textclassifier.TextClassifier.NO_OP;
        }
        return mTextClassifier;
    }

    /**
     * Returns a session-aware text classifier.
     * This method creates one if none already exists or the current one is destroyed.
     */
    @android.annotation.NonNull
    android.view.textclassifier.TextClassifier getTextClassificationSession() {
        if ((mTextClassificationSession == null) || mTextClassificationSession.isDestroyed()) {
            final android.view.textclassifier.TextClassificationManager tcm = mContext.getSystemService(android.view.textclassifier.TextClassificationManager.class);
            if (tcm != null) {
                final java.lang.String widgetType;
                if (isTextEditable()) {
                    widgetType = android.view.textclassifier.TextClassifier.WIDGET_TYPE_EDITTEXT;
                } else
                    if (isTextSelectable()) {
                        widgetType = android.view.textclassifier.TextClassifier.WIDGET_TYPE_TEXTVIEW;
                    } else {
                        widgetType = android.view.textclassifier.TextClassifier.WIDGET_TYPE_UNSELECTABLE_TEXTVIEW;
                    }

                mTextClassificationContext = new android.view.textclassifier.TextClassificationContext.Builder(mContext.getPackageName(), widgetType).build();
                if (mTextClassifier != null) {
                    mTextClassificationSession = tcm.createTextClassificationSession(mTextClassificationContext, mTextClassifier);
                } else {
                    mTextClassificationSession = tcm.createTextClassificationSession(mTextClassificationContext);
                }
            } else {
                mTextClassificationSession = android.view.textclassifier.TextClassifier.NO_OP;
            }
        }
        return mTextClassificationSession;
    }

    /**
     * Returns the {@link TextClassificationContext} for the current TextClassifier session.
     *
     * @see #getTextClassificationSession()
     */
    @android.annotation.Nullable
    android.view.textclassifier.TextClassificationContext getTextClassificationContext() {
        return mTextClassificationContext;
    }

    /**
     * Returns true if this TextView uses a no-op TextClassifier.
     */
    boolean usesNoOpTextClassifier() {
        return getTextClassifier() == android.view.textclassifier.TextClassifier.NO_OP;
    }

    /**
     * Starts an ActionMode for the specified TextLinkSpan.
     *
     * @return Whether or not we're attempting to start the action mode.
     * @unknown 
     */
    public boolean requestActionMode(@android.annotation.NonNull
    android.view.textclassifier.TextLinks.TextLinkSpan clickedSpan) {
        com.android.internal.util.Preconditions.checkNotNull(clickedSpan);
        if (!(mText instanceof android.text.Spanned)) {
            return false;
        }
        final int start = ((android.text.Spanned) (mText)).getSpanStart(clickedSpan);
        final int end = ((android.text.Spanned) (mText)).getSpanEnd(clickedSpan);
        if (((start < 0) || (end > mText.length())) || (start >= end)) {
            return false;
        }
        createEditorIfNeeded();
        mEditor.startLinkActionModeAsync(start, end);
        return true;
    }

    /**
     * Handles a click on the specified TextLinkSpan.
     *
     * @return Whether or not the click is being handled.
     * @unknown 
     */
    public boolean handleClick(@android.annotation.NonNull
    android.view.textclassifier.TextLinks.TextLinkSpan clickedSpan) {
        com.android.internal.util.Preconditions.checkNotNull(clickedSpan);
        if (mText instanceof android.text.Spanned) {
            final android.text.Spanned spanned = ((android.text.Spanned) (mText));
            final int start = spanned.getSpanStart(clickedSpan);
            final int end = spanned.getSpanEnd(clickedSpan);
            if (((start >= 0) && (end <= mText.length())) && (start < end)) {
                final android.view.textclassifier.TextClassification.Request request = new android.view.textclassifier.TextClassification.Request.Builder(mText, start, end).setDefaultLocales(getTextLocales()).build();
                final java.util.function.Supplier<android.view.textclassifier.TextClassification> supplier = () -> getTextClassifier().classifyText(request);
                final java.util.function.Consumer<android.view.textclassifier.TextClassification> consumer = ( classification) -> {
                    if (classification != null) {
                        if (!classification.getActions().isEmpty()) {
                            try {
                                classification.getActions().get(0).getActionIntent().send();
                            } catch (android.app.PendingIntent e) {
                                android.util.Log.e(TextView.LOG_TAG, "Error sending PendingIntent", e);
                            }
                        } else {
                            android.util.Log.d(TextView.LOG_TAG, "No link action to perform");
                        }
                    } else {
                        // classification == null
                        android.util.Log.d(TextView.LOG_TAG, "Timeout while classifying text");
                    }
                };
                java.util.concurrent.CompletableFuture.supplyAsync(supplier).completeOnTimeout(null, 1, java.util.concurrent.TimeUnit.SECONDS).thenAccept(consumer);
                return true;
            }
        }
        return false;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected void stopTextActionMode() {
        if (mEditor != null) {
            mEditor.stopTextActionMode();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void hideFloatingToolbar(int durationMs) {
        if (mEditor != null) {
            mEditor.hideFloatingToolbar(durationMs);
        }
    }

    boolean canUndo() {
        return (mEditor != null) && mEditor.canUndo();
    }

    boolean canRedo() {
        return (mEditor != null) && mEditor.canRedo();
    }

    boolean canCut() {
        if (hasPasswordTransformationMethod()) {
            return false;
        }
        if (((((mText.length() > 0) && hasSelection()) && (mText instanceof android.text.Editable)) && (mEditor != null)) && (mEditor.mKeyListener != null)) {
            return true;
        }
        return false;
    }

    boolean canCopy() {
        if (hasPasswordTransformationMethod()) {
            return false;
        }
        if (((mText.length() > 0) && hasSelection()) && (mEditor != null)) {
            return true;
        }
        return false;
    }

    boolean canShare() {
        if ((!getContext().canStartActivityForResult()) || (!isDeviceProvisioned())) {
            return false;
        }
        return canCopy();
    }

    boolean isDeviceProvisioned() {
        if (mDeviceProvisionedState == TextView.DEVICE_PROVISIONED_UNKNOWN) {
            mDeviceProvisionedState = (Settings.Global.getInt(mContext.getContentResolver(), Settings.Global.DEVICE_PROVISIONED, 0) != 0) ? TextView.DEVICE_PROVISIONED_YES : TextView.DEVICE_PROVISIONED_NO;
        }
        return mDeviceProvisionedState == TextView.DEVICE_PROVISIONED_YES;
    }

    @android.annotation.UnsupportedAppUsage
    boolean canPaste() {
        return (((((mText instanceof android.text.Editable) && (mEditor != null)) && (mEditor.mKeyListener != null)) && (getSelectionStart() >= 0)) && (getSelectionEnd() >= 0)) && getClipboardManagerForUser().hasPrimaryClip();
    }

    boolean canPasteAsPlainText() {
        if (!canPaste()) {
            return false;
        }
        final android.content.ClipData clipData = getClipboardManagerForUser().getPrimaryClip();
        final android.content.ClipDescription description = clipData.getDescription();
        final boolean isPlainType = description.hasMimeType(android.content.ClipDescription.MIMETYPE_TEXT_PLAIN);
        final java.lang.CharSequence text = clipData.getItemAt(0).getText();
        if (isPlainType && (text instanceof android.text.Spanned)) {
            android.text.Spanned spanned = ((android.text.Spanned) (text));
            if (android.text.TextUtils.hasStyleSpan(spanned)) {
                return true;
            }
        }
        return description.hasMimeType(android.content.ClipDescription.MIMETYPE_TEXT_HTML);
    }

    boolean canProcessText() {
        if (getId() == android.view.View.NO_ID) {
            return false;
        }
        return canShare();
    }

    boolean canSelectAllText() {
        return (canSelectText() && (!hasPasswordTransformationMethod())) && (!((getSelectionStart() == 0) && (getSelectionEnd() == mText.length())));
    }

    boolean selectAllText() {
        if (mEditor != null) {
            // Hide the toolbar before changing the selection to avoid flickering.
            hideFloatingToolbar(TextView.FLOATING_TOOLBAR_SELECT_ALL_REFRESH_DELAY);
        }
        final int length = mText.length();
        android.text.Selection.setSelection(mSpannable, 0, length);
        return length > 0;
    }

    void replaceSelectionWithText(java.lang.CharSequence text) {
        ((android.text.Editable) (mText)).replace(getSelectionStart(), getSelectionEnd(), text);
    }

    /**
     * Paste clipboard content between min and max positions.
     */
    private void paste(int min, int max, boolean withFormatting) {
        android.content.ClipboardManager clipboard = getClipboardManagerForUser();
        android.content.ClipData clip = clipboard.getPrimaryClip();
        if (clip != null) {
            boolean didFirst = false;
            for (int i = 0; i < clip.getItemCount(); i++) {
                final java.lang.CharSequence paste;
                if (withFormatting) {
                    paste = clip.getItemAt(i).coerceToStyledText(getContext());
                } else {
                    // Get an item as text and remove all spans by toString().
                    final java.lang.CharSequence text = clip.getItemAt(i).coerceToText(getContext());
                    paste = (text instanceof android.text.Spanned) ? text.toString() : text;
                }
                if (paste != null) {
                    if (!didFirst) {
                        android.text.Selection.setSelection(mSpannable, max);
                        ((android.text.Editable) (mText)).replace(min, max, paste);
                        didFirst = true;
                    } else {
                        ((android.text.Editable) (mText)).insert(getSelectionEnd(), "\n");
                        ((android.text.Editable) (mText)).insert(getSelectionEnd(), paste);
                    }
                }
            }
            TextView.sLastCutCopyOrTextChangedTime = 0;
        }
    }

    private void shareSelectedText() {
        java.lang.String selectedText = getSelectedText();
        if ((selectedText != null) && (!selectedText.isEmpty())) {
            android.content.Intent sharingIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.removeExtra(android.content.Intent.EXTRA_TEXT);
            selectedText = android.text.TextUtils.trimToParcelableSize(selectedText);
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, selectedText);
            getContext().startActivity(android.content.Intent.createChooser(sharingIntent, null));
            android.text.Selection.setSelection(mSpannable, getSelectionEnd());
        }
    }

    @android.annotation.CheckResult
    private boolean setPrimaryClip(android.content.ClipData clip) {
        android.content.ClipboardManager clipboard = getClipboardManagerForUser();
        try {
            clipboard.setPrimaryClip(clip);
        } catch (java.lang.Throwable t) {
            return false;
        }
        TextView.sLastCutCopyOrTextChangedTime = android.os.SystemClock.uptimeMillis();
        return true;
    }

    /**
     * Get the character offset closest to the specified absolute position. A typical use case is to
     * pass the result of {@link MotionEvent#getX()} and {@link MotionEvent#getY()} to this method.
     *
     * @param x
     * 		The horizontal absolute position of a point on screen
     * @param y
     * 		The vertical absolute position of a point on screen
     * @return the character offset for the character whose position is closest to the specified
    position. Returns -1 if there is no layout.
     */
    public int getOffsetForPosition(float x, float y) {
        if (getLayout() == null)
            return -1;

        final int line = getLineAtCoordinate(y);
        final int offset = getOffsetAtCoordinate(line, x);
        return offset;
    }

    float convertToLocalHorizontalCoordinate(float x) {
        x -= getTotalPaddingLeft();
        // Clamp the position to inside of the view.
        x = java.lang.Math.max(0.0F, x);
        x = java.lang.Math.min((getWidth() - getTotalPaddingRight()) - 1, x);
        x += getScrollX();
        return x;
    }

    @android.annotation.UnsupportedAppUsage
    int getLineAtCoordinate(float y) {
        y -= getTotalPaddingTop();
        // Clamp the position to inside of the view.
        y = java.lang.Math.max(0.0F, y);
        y = java.lang.Math.min((getHeight() - getTotalPaddingBottom()) - 1, y);
        y += getScrollY();
        return getLayout().getLineForVertical(((int) (y)));
    }

    int getLineAtCoordinateUnclamped(float y) {
        y -= getTotalPaddingTop();
        y += getScrollY();
        return getLayout().getLineForVertical(((int) (y)));
    }

    int getOffsetAtCoordinate(int line, float x) {
        x = convertToLocalHorizontalCoordinate(x);
        return getLayout().getOffsetForHorizontal(line, x);
    }

    @java.lang.Override
    public boolean onDragEvent(android.view.DragEvent event) {
        switch (event.getAction()) {
            case android.view.DragEvent.ACTION_DRAG_STARTED :
                return (mEditor != null) && mEditor.hasInsertionController();
            case android.view.DragEvent.ACTION_DRAG_ENTERED :
                this.requestFocus();
                return true;
            case android.view.DragEvent.ACTION_DRAG_LOCATION :
                if (mText instanceof android.text.Spannable) {
                    final int offset = getOffsetForPosition(event.getX(), event.getY());
                    android.text.Selection.setSelection(mSpannable, offset);
                }
                return true;
            case android.view.DragEvent.ACTION_DROP :
                if (mEditor != null)
                    mEditor.onDrop(event);

                return true;
            case android.view.DragEvent.ACTION_DRAG_ENDED :
            case android.view.DragEvent.ACTION_DRAG_EXITED :
            default :
                return true;
        }
    }

    boolean isInBatchEditMode() {
        if (mEditor == null)
            return false;

        final android.widget.Editor.InputMethodState ims = mEditor.mInputMethodState;
        if (ims != null) {
            return ims.mBatchEditNesting > 0;
        }
        return mEditor.mInBatchEditControllers;
    }

    @java.lang.Override
    public void onRtlPropertiesChanged(int layoutDirection) {
        super.onRtlPropertiesChanged(layoutDirection);
        final android.text.TextDirectionHeuristic newTextDir = getTextDirectionHeuristic();
        if (mTextDir != newTextDir) {
            mTextDir = newTextDir;
            if (mLayout != null) {
                checkForRelayout();
            }
        }
    }

    /**
     * Returns resolved {@link TextDirectionHeuristic} that will be used for text layout.
     * The {@link TextDirectionHeuristic} that is used by TextView is only available after
     * {@link #getTextDirection()} and {@link #getLayoutDirection()} is resolved. Therefore the
     * return value may not be the same as the one TextView uses if the View's layout direction is
     * not resolved or detached from parent root view.
     */
    @android.annotation.NonNull
    public android.text.TextDirectionHeuristic getTextDirectionHeuristic() {
        if (hasPasswordTransformationMethod()) {
            // passwords fields should be LTR
            return android.text.TextDirectionHeuristics.LTR;
        }
        if ((mEditor != null) && ((mEditor.mInputType & TYPE_MASK_CLASS) == TYPE_CLASS_PHONE)) {
            // Phone numbers must be in the direction of the locale's digits. Most locales have LTR
            // digits, but some locales, such as those written in the Adlam or N'Ko scripts, have
            // RTL digits.
            final android.icu.text.DecimalFormatSymbols symbols = android.icu.text.DecimalFormatSymbols.getInstance(getTextLocale());
            final java.lang.String zero = symbols.getDigitStrings()[0];
            // In case the zero digit is multi-codepoint, just use the first codepoint to determine
            // direction.
            final int firstCodepoint = zero.codePointAt(0);
            final byte digitDirection = java.lang.Character.getDirectionality(firstCodepoint);
            if ((digitDirection == java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT) || (digitDirection == java.lang.Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC)) {
                return android.text.TextDirectionHeuristics.RTL;
            } else {
                return android.text.TextDirectionHeuristics.LTR;
            }
        }
        // Always need to resolve layout direction first
        final boolean defaultIsRtl = getLayoutDirection() == android.view.View.LAYOUT_DIRECTION_RTL;
        // Now, we can select the heuristic
        switch (getTextDirection()) {
            default :
            case android.view.View.TEXT_DIRECTION_FIRST_STRONG :
                return defaultIsRtl ? android.text.TextDirectionHeuristics.FIRSTSTRONG_RTL : android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR;
            case android.view.View.TEXT_DIRECTION_ANY_RTL :
                return android.text.TextDirectionHeuristics.ANYRTL_LTR;
            case android.view.View.TEXT_DIRECTION_LTR :
                return android.text.TextDirectionHeuristics.LTR;
            case android.view.View.TEXT_DIRECTION_RTL :
                return android.text.TextDirectionHeuristics.RTL;
            case android.view.View.TEXT_DIRECTION_LOCALE :
                return android.text.TextDirectionHeuristics.LOCALE;
            case android.view.View.TEXT_DIRECTION_FIRST_STRONG_LTR :
                return android.text.TextDirectionHeuristics.FIRSTSTRONG_LTR;
            case android.view.View.TEXT_DIRECTION_FIRST_STRONG_RTL :
                return android.text.TextDirectionHeuristics.FIRSTSTRONG_RTL;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void onResolveDrawables(int layoutDirection) {
        // No need to resolve twice
        if (mLastLayoutDirection == layoutDirection) {
            return;
        }
        mLastLayoutDirection = layoutDirection;
        // Resolve drawables
        if (mDrawables != null) {
            if (mDrawables.resolveWithLayoutDirection(layoutDirection)) {
                prepareDrawableForDisplay(mDrawables.mShowing[TextView.Drawables.LEFT]);
                prepareDrawableForDisplay(mDrawables.mShowing[TextView.Drawables.RIGHT]);
                applyCompoundDrawableTint();
            }
        }
    }

    /**
     * Prepares a drawable for display by propagating layout direction and
     * drawable state.
     *
     * @param dr
     * 		the drawable to prepare
     */
    private void prepareDrawableForDisplay(@android.annotation.Nullable
    android.graphics.drawable.Drawable dr) {
        if (dr == null) {
            return;
        }
        dr.setLayoutDirection(getLayoutDirection());
        if (dr.isStateful()) {
            dr.setState(getDrawableState());
            dr.jumpToCurrentState();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    protected void resetResolvedDrawables() {
        super.resetResolvedDrawables();
        mLastLayoutDirection = -1;
    }

    /**
     *
     *
     * @unknown 
     */
    protected void viewClicked(android.view.inputmethod.InputMethodManager imm) {
        if (imm != null) {
            imm.viewClicked(this);
        }
    }

    /**
     * Deletes the range of text [start, end[.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    protected void deleteText_internal(int start, int end) {
        ((android.text.Editable) (mText)).delete(start, end);
    }

    /**
     * Replaces the range of text [start, end[ by replacement text
     *
     * @unknown 
     */
    protected void replaceText_internal(int start, int end, java.lang.CharSequence text) {
        ((android.text.Editable) (mText)).replace(start, end, text);
    }

    /**
     * Sets a span on the specified range of text
     *
     * @unknown 
     */
    protected void setSpan_internal(java.lang.Object span, int start, int end, int flags) {
        ((android.text.Editable) (mText)).setSpan(span, start, end, flags);
    }

    /**
     * Moves the cursor to the specified offset position in text
     *
     * @unknown 
     */
    protected void setCursorPosition_internal(int start, int end) {
        android.text.Selection.setSelection(((android.text.Editable) (mText)), start, end);
    }

    /**
     * An Editor should be created as soon as any of the editable-specific fields (grouped
     * inside the Editor object) is assigned to a non-default value.
     * This method will create the Editor if needed.
     *
     * A standard TextView (as well as buttons, checkboxes...) should not qualify and hence will
     * have a null Editor, unlike an EditText. Inconsistent in-between states will have an
     * Editor for backward compatibility, as soon as one of these fields is assigned.
     *
     * Also note that for performance reasons, the mEditor is created when needed, but not
     * reset when no more edit-specific fields are needed.
     */
    @android.annotation.UnsupportedAppUsage
    private void createEditorIfNeeded() {
        if (mEditor == null) {
            mEditor = new android.widget.Editor(this);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    @android.annotation.UnsupportedAppUsage
    public java.lang.CharSequence getIterableTextForAccessibility() {
        return mText;
    }

    private void ensureIterableTextForAccessibilitySelectable() {
        if (!(mText instanceof android.text.Spannable)) {
            setText(mText, TextView.BufferType.SPANNABLE);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public android.view.AccessibilityIterators.TextSegmentIterator getIteratorForGranularity(int granularity) {
        switch (granularity) {
            case android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_LINE :
                {
                    android.text.Spannable text = ((android.text.Spannable) (getIterableTextForAccessibility()));
                    if ((!android.text.TextUtils.isEmpty(text)) && (getLayout() != null)) {
                        android.widget.AccessibilityIterators.LineTextSegmentIterator iterator = android.widget.AccessibilityIterators.LineTextSegmentIterator.getInstance();
                        iterator.initialize(text, getLayout());
                        return iterator;
                    }
                }
                break;
            case android.view.accessibility.AccessibilityNodeInfo.MOVEMENT_GRANULARITY_PAGE :
                {
                    android.text.Spannable text = ((android.text.Spannable) (getIterableTextForAccessibility()));
                    if ((!android.text.TextUtils.isEmpty(text)) && (getLayout() != null)) {
                        android.widget.AccessibilityIterators.PageTextSegmentIterator iterator = android.widget.AccessibilityIterators.PageTextSegmentIterator.getInstance();
                        iterator.initialize(this);
                        return iterator;
                    }
                }
                break;
        }
        return super.getIteratorForGranularity(granularity);
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getAccessibilitySelectionStart() {
        return getSelectionStart();
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isAccessibilitySelectionExtendable() {
        return true;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public int getAccessibilitySelectionEnd() {
        return getSelectionEnd();
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public void setAccessibilitySelection(int start, int end) {
        if ((getAccessibilitySelectionStart() == start) && (getAccessibilitySelectionEnd() == end)) {
            return;
        }
        java.lang.CharSequence text = getIterableTextForAccessibility();
        if ((java.lang.Math.min(start, end) >= 0) && (java.lang.Math.max(start, end) <= text.length())) {
            android.text.Selection.setSelection(((android.text.Spannable) (text)), start, end);
        } else {
            android.text.Selection.removeSelection(((android.text.Spannable) (text)));
        }
        // Hide all selection controllers used for adjusting selection
        // since we are doing so explicitlty by other means and these
        // controllers interact with how selection behaves.
        if (mEditor != null) {
            mEditor.hideCursorAndSpanControllers();
            mEditor.stopTextActionMode();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    protected void encodeProperties(@android.annotation.NonNull
    android.view.ViewHierarchyEncoder stream) {
        super.encodeProperties(stream);
        android.text.TextUtils.TruncateAt ellipsize = getEllipsize();
        stream.addProperty("text:ellipsize", ellipsize == null ? null : ellipsize.name());
        stream.addProperty("text:textSize", getTextSize());
        stream.addProperty("text:scaledTextSize", getScaledTextSize());
        stream.addProperty("text:typefaceStyle", getTypefaceStyle());
        stream.addProperty("text:selectionStart", getSelectionStart());
        stream.addProperty("text:selectionEnd", getSelectionEnd());
        stream.addProperty("text:curTextColor", mCurTextColor);
        stream.addProperty("text:text", mText == null ? null : mText.toString());
        stream.addProperty("text:gravity", mGravity);
    }

    /**
     * User interface state that is stored by TextView for implementing
     * {@link View#onSaveInstanceState}.
     */
    public static class SavedState extends android.view.View.BaseSavedState {
        int selStart = -1;

        int selEnd = -1;

        @android.annotation.UnsupportedAppUsage
        java.lang.CharSequence text;

        boolean frozenWithFocus;

        java.lang.CharSequence error;

        android.os.ParcelableParcel editorState;// Optional state from Editor.


        SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(selStart);
            out.writeInt(selEnd);
            out.writeInt(frozenWithFocus ? 1 : 0);
            android.text.TextUtils.writeToParcel(text, out, flags);
            if (error == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                android.text.TextUtils.writeToParcel(error, out, flags);
            }
            if (editorState == null) {
                out.writeInt(0);
            } else {
                out.writeInt(1);
                editorState.writeToParcel(out, flags);
            }
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.String str = (((("TextView.SavedState{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " start=") + selStart) + " end=") + selEnd;
            if (text != null) {
                str += " text=" + text;
            }
            return str + "}";
        }

        @java.lang.SuppressWarnings("hiding")
        @android.annotation.NonNull
        public static final android.os.Parcelable.Creator<TextView.SavedState> CREATOR = new android.os.Parcelable.Creator<TextView.SavedState>() {
            public android.widget.SavedState createFromParcel(android.os.Parcel in) {
                return new android.widget.SavedState(in);
            }

            public android.widget.SavedState[] newArray(int size) {
                return new android.widget.SavedState[size];
            }
        };

        private SavedState(android.os.Parcel in) {
            super(in);
            selStart = in.readInt();
            selEnd = in.readInt();
            frozenWithFocus = in.readInt() != 0;
            text = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            if (in.readInt() != 0) {
                error = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
            }
            if (in.readInt() != 0) {
                editorState = ParcelableParcel.CREATOR.createFromParcel(in);
            }
        }
    }

    private static class CharWrapper implements android.text.GetChars , android.text.GraphicsOperations , java.lang.CharSequence {
        private char[] mChars;

        private int mStart;

        private int mLength;

        public CharWrapper(char[] chars, int start, int len) {
            mChars = chars;
            mStart = start;
            mLength = len;
        }

        /* package */
        void set(char[] chars, int start, int len) {
            mChars = chars;
            mStart = start;
            mLength = len;
        }

        public int length() {
            return mLength;
        }

        public char charAt(int off) {
            return mChars[off + mStart];
        }

        @java.lang.Override
        public java.lang.String toString() {
            return new java.lang.String(mChars, mStart, mLength);
        }

        public java.lang.CharSequence subSequence(int start, int end) {
            if ((((start < 0) || (end < 0)) || (start > mLength)) || (end > mLength)) {
                throw new java.lang.IndexOutOfBoundsException((start + ", ") + end);
            }
            return new java.lang.String(mChars, start + mStart, end - start);
        }

        public void getChars(int start, int end, char[] buf, int off) {
            if ((((start < 0) || (end < 0)) || (start > mLength)) || (end > mLength)) {
                throw new java.lang.IndexOutOfBoundsException((start + ", ") + end);
            }
            java.lang.System.arraycopy(mChars, start + mStart, buf, off, end - start);
        }

        @java.lang.Override
        public void drawText(android.graphics.BaseCanvas c, int start, int end, float x, float y, android.graphics.Paint p) {
            c.drawText(mChars, start + mStart, end - start, x, y, p);
        }

        @java.lang.Override
        public void drawTextRun(android.graphics.BaseCanvas c, int start, int end, int contextStart, int contextEnd, float x, float y, boolean isRtl, android.graphics.Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            c.drawTextRun(mChars, start + mStart, count, contextStart + mStart, contextCount, x, y, isRtl, p);
        }

        public float measureText(int start, int end, android.graphics.Paint p) {
            return p.measureText(mChars, start + mStart, end - start);
        }

        public int getTextWidths(int start, int end, float[] widths, android.graphics.Paint p) {
            return p.getTextWidths(mChars, start + mStart, end - start, widths);
        }

        public float getTextRunAdvances(int start, int end, int contextStart, int contextEnd, boolean isRtl, float[] advances, int advancesIndex, android.graphics.Paint p) {
            int count = end - start;
            int contextCount = contextEnd - contextStart;
            return p.getTextRunAdvances(mChars, start + mStart, count, contextStart + mStart, contextCount, isRtl, advances, advancesIndex);
        }

        public int getTextRunCursor(int contextStart, int contextEnd, boolean isRtl, int offset, int cursorOpt, android.graphics.Paint p) {
            int contextCount = contextEnd - contextStart;
            return p.getTextRunCursor(mChars, contextStart + mStart, contextCount, isRtl, offset + mStart, cursorOpt);
        }
    }

    private static final class Marquee {
        // TODO: Add an option to configure this
        private static final float MARQUEE_DELTA_MAX = 0.07F;

        private static final int MARQUEE_DELAY = 1200;

        private static final int MARQUEE_DP_PER_SECOND = 30;

        private static final byte MARQUEE_STOPPED = 0x0;

        private static final byte MARQUEE_STARTING = 0x1;

        private static final byte MARQUEE_RUNNING = 0x2;

        private final java.lang.ref.WeakReference<TextView> mView;

        private final android.view.Choreographer mChoreographer;

        private byte mStatus = TextView.Marquee.MARQUEE_STOPPED;

        private final float mPixelsPerMs;

        private float mMaxScroll;

        private float mMaxFadeScroll;

        private float mGhostStart;

        private float mGhostOffset;

        private float mFadeStop;

        private int mRepeatLimit;

        private float mScroll;

        private long mLastAnimationMs;

        Marquee(TextView v) {
            final float density = v.getContext().getResources().getDisplayMetrics().density;
            mPixelsPerMs = (TextView.Marquee.MARQUEE_DP_PER_SECOND * density) / 1000.0F;
            mView = new java.lang.ref.WeakReference<TextView>(v);
            mChoreographer = android.view.Choreographer.getInstance();
        }

        private android.view.Choreographer.FrameCallback mTickCallback = new android.view.Choreographer.FrameCallback() {
            @java.lang.Override
            public void doFrame(long frameTimeNanos) {
                tick();
            }
        };

        private android.view.Choreographer.FrameCallback mStartCallback = new android.view.Choreographer.FrameCallback() {
            @java.lang.Override
            public void doFrame(long frameTimeNanos) {
                mStatus = TextView.Marquee.MARQUEE_RUNNING;
                mLastAnimationMs = mChoreographer.getFrameTime();
                tick();
            }
        };

        private android.view.Choreographer.FrameCallback mRestartCallback = new android.view.Choreographer.FrameCallback() {
            @java.lang.Override
            public void doFrame(long frameTimeNanos) {
                if (mStatus == TextView.Marquee.MARQUEE_RUNNING) {
                    if (mRepeatLimit >= 0) {
                        mRepeatLimit--;
                    }
                    start(mRepeatLimit);
                }
            }
        };

        void tick() {
            if (mStatus != TextView.Marquee.MARQUEE_RUNNING) {
                return;
            }
            mChoreographer.removeFrameCallback(mTickCallback);
            final TextView textView = mView.get();
            if ((textView != null) && (textView.isFocused() || textView.isSelected())) {
                long currentMs = mChoreographer.getFrameTime();
                long deltaMs = currentMs - mLastAnimationMs;
                mLastAnimationMs = currentMs;
                float deltaPx = deltaMs * mPixelsPerMs;
                mScroll += deltaPx;
                if (mScroll > mMaxScroll) {
                    mScroll = mMaxScroll;
                    mChoreographer.postFrameCallbackDelayed(mRestartCallback, TextView.Marquee.MARQUEE_DELAY);
                } else {
                    mChoreographer.postFrameCallback(mTickCallback);
                }
                textView.invalidate();
            }
        }

        void stop() {
            mStatus = TextView.Marquee.MARQUEE_STOPPED;
            mChoreographer.removeFrameCallback(mStartCallback);
            mChoreographer.removeFrameCallback(mRestartCallback);
            mChoreographer.removeFrameCallback(mTickCallback);
            resetScroll();
        }

        private void resetScroll() {
            mScroll = 0.0F;
            final TextView textView = mView.get();
            if (textView != null)
                textView.invalidate();

        }

        void start(int repeatLimit) {
            if (repeatLimit == 0) {
                stop();
                return;
            }
            mRepeatLimit = repeatLimit;
            final TextView textView = mView.get();
            if ((textView != null) && (textView.mLayout != null)) {
                mStatus = TextView.Marquee.MARQUEE_STARTING;
                mScroll = 0.0F;
                final int textWidth = (textView.getWidth() - textView.getCompoundPaddingLeft()) - textView.getCompoundPaddingRight();
                final float lineWidth = textView.mLayout.getLineWidth(0);
                final float gap = textWidth / 3.0F;
                mGhostStart = (lineWidth - textWidth) + gap;
                mMaxScroll = mGhostStart + textWidth;
                mGhostOffset = lineWidth + gap;
                mFadeStop = lineWidth + (textWidth / 6.0F);
                mMaxFadeScroll = (mGhostStart + lineWidth) + lineWidth;
                textView.invalidate();
                mChoreographer.postFrameCallback(mStartCallback);
            }
        }

        float getGhostOffset() {
            return mGhostOffset;
        }

        float getScroll() {
            return mScroll;
        }

        float getMaxFadeScroll() {
            return mMaxFadeScroll;
        }

        boolean shouldDrawLeftFade() {
            return mScroll <= mFadeStop;
        }

        boolean shouldDrawGhost() {
            return (mStatus == TextView.Marquee.MARQUEE_RUNNING) && (mScroll > mGhostStart);
        }

        boolean isRunning() {
            return mStatus == TextView.Marquee.MARQUEE_RUNNING;
        }

        boolean isStopped() {
            return mStatus == TextView.Marquee.MARQUEE_STOPPED;
        }
    }

    private class ChangeWatcher implements android.text.SpanWatcher , android.text.TextWatcher {
        private java.lang.CharSequence mBeforeText;

        public void beforeTextChanged(java.lang.CharSequence buffer, int start, int before, int after) {
            if (TextView.DEBUG_EXTRACT) {
                android.util.Log.v(TextView.LOG_TAG, (((((("beforeTextChanged start=" + start) + " before=") + before) + " after=") + after) + ": ") + buffer);
            }
            if (android.view.accessibility.AccessibilityManager.getInstance(mContext).isEnabled() && (mTransformed != null)) {
                mBeforeText = mTransformed.toString();
            }
            TextView.this.sendBeforeTextChanged(buffer, start, before, after);
        }

        public void onTextChanged(java.lang.CharSequence buffer, int start, int before, int after) {
            if (TextView.DEBUG_EXTRACT) {
                android.util.Log.v(TextView.LOG_TAG, (((((("onTextChanged start=" + start) + " before=") + before) + " after=") + after) + ": ") + buffer);
            }
            TextView.this.handleTextChanged(buffer, start, before, after);
            if (android.view.accessibility.AccessibilityManager.getInstance(mContext).isEnabled() && (isFocused() || (isSelected() && isShown()))) {
                sendAccessibilityEventTypeViewTextChanged(mBeforeText, start, before, after);
                mBeforeText = null;
            }
        }

        public void afterTextChanged(android.text.Editable buffer) {
            if (TextView.DEBUG_EXTRACT) {
                android.util.Log.v(TextView.LOG_TAG, "afterTextChanged: " + buffer);
            }
            TextView.this.sendAfterTextChanged(buffer);
            if (android.text.method.MetaKeyKeyListener.getMetaState(buffer, MetaKeyKeyListener.META_SELECTING) != 0) {
                android.text.method.MetaKeyKeyListener.stopSelecting(TextView.this, buffer);
            }
        }

        public void onSpanChanged(android.text.Spannable buf, java.lang.Object what, int s, int e, int st, int en) {
            if (TextView.DEBUG_EXTRACT) {
                android.util.Log.v(TextView.LOG_TAG, (((((((((("onSpanChanged s=" + s) + " e=") + e) + " st=") + st) + " en=") + en) + " what=") + what) + ": ") + buf);
            }
            TextView.this.spanChange(buf, what, s, st, e, en);
        }

        public void onSpanAdded(android.text.Spannable buf, java.lang.Object what, int s, int e) {
            if (TextView.DEBUG_EXTRACT) {
                android.util.Log.v(TextView.LOG_TAG, (((((("onSpanAdded s=" + s) + " e=") + e) + " what=") + what) + ": ") + buf);
            }
            TextView.this.spanChange(buf, what, -1, s, -1, e);
        }

        public void onSpanRemoved(android.text.Spannable buf, java.lang.Object what, int s, int e) {
            if (TextView.DEBUG_EXTRACT) {
                android.util.Log.v(TextView.LOG_TAG, (((((("onSpanRemoved s=" + s) + " e=") + e) + " what=") + what) + ": ") + buf);
            }
            TextView.this.spanChange(buf, what, s, -1, e, -1);
        }
    }
}

