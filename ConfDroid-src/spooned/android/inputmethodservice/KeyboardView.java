/**
 * Copyright (C) 2008-2009 Google Inc.
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
 * A view that renders a virtual {@link Keyboard}. It handles rendering of keys and
 * detecting key presses and touch movements.
 *
 * @unknown ref android.R.styleable#KeyboardView_keyBackground
 * @unknown ref android.R.styleable#KeyboardView_keyPreviewLayout
 * @unknown ref android.R.styleable#KeyboardView_keyPreviewOffset
 * @unknown ref android.R.styleable#KeyboardView_labelTextSize
 * @unknown ref android.R.styleable#KeyboardView_keyTextSize
 * @unknown ref android.R.styleable#KeyboardView_keyTextColor
 * @unknown ref android.R.styleable#KeyboardView_verticalCorrection
 * @unknown ref android.R.styleable#KeyboardView_popupLayout
 */
public class KeyboardView extends android.view.View implements android.view.View.OnClickListener {
    /**
     * Listener for virtual keyboard events.
     */
    public interface OnKeyboardActionListener {
        /**
         * Called when the user presses a key. This is sent before the {@link #onKey} is called.
         * For keys that repeat, this is only called once.
         *
         * @param primaryCode
         * 		the unicode of the key being pressed. If the touch is not on a valid
         * 		key, the value will be zero.
         */
        void onPress(int primaryCode);

        /**
         * Called when the user releases a key. This is sent after the {@link #onKey} is called.
         * For keys that repeat, this is only called once.
         *
         * @param primaryCode
         * 		the code of the key that was released
         */
        void onRelease(int primaryCode);

        /**
         * Send a key press to the listener.
         *
         * @param primaryCode
         * 		this is the key that was pressed
         * @param keyCodes
         * 		the codes for all the possible alternative keys
         * 		with the primary code being the first. If the primary key code is
         * 		a single character such as an alphabet or number or symbol, the alternatives
         * 		will include other characters that may be on the same key or adjacent keys.
         * 		These codes are useful to correct for accidental presses of a key adjacent to
         * 		the intended key.
         */
        void onKey(int primaryCode, int[] keyCodes);

        /**
         * Sends a sequence of characters to the listener.
         *
         * @param text
         * 		the sequence of characters to be displayed.
         */
        void onText(java.lang.CharSequence text);

        /**
         * Called when the user quickly moves the finger from right to left.
         */
        void swipeLeft();

        /**
         * Called when the user quickly moves the finger from left to right.
         */
        void swipeRight();

        /**
         * Called when the user quickly moves the finger from up to down.
         */
        void swipeDown();

        /**
         * Called when the user quickly moves the finger from down to up.
         */
        void swipeUp();
    }

    private static final boolean DEBUG = false;

    private static final int NOT_A_KEY = -1;

    private static final int[] KEY_DELETE = new int[]{ android.inputmethodservice.Keyboard.KEYCODE_DELETE };

    private static final int[] LONG_PRESSABLE_STATE_SET = new int[]{ R.attr.state_long_pressable };

    private android.inputmethodservice.Keyboard mKeyboard;

    private int mCurrentKeyIndex = android.inputmethodservice.KeyboardView.NOT_A_KEY;

    private int mLabelTextSize;

    private int mKeyTextSize;

    private int mKeyTextColor;

    private float mShadowRadius;

    private int mShadowColor;

    private float mBackgroundDimAmount;

    private android.widget.TextView mPreviewText;

    private android.widget.PopupWindow mPreviewPopup;

    private int mPreviewTextSizeLarge;

    private int mPreviewOffset;

    private int mPreviewHeight;

    // Working variable
    private final int[] mCoordinates = new int[2];

    private android.widget.PopupWindow mPopupKeyboard;

    private android.view.View mMiniKeyboardContainer;

    private android.inputmethodservice.KeyboardView mMiniKeyboard;

    private boolean mMiniKeyboardOnScreen;

    private android.view.View mPopupParent;

    private int mMiniKeyboardOffsetX;

    private int mMiniKeyboardOffsetY;

    private java.util.Map<android.inputmethodservice.Keyboard.Key, android.view.View> mMiniKeyboardCache;

    private android.inputmethodservice.Keyboard.Key[] mKeys;

    /**
     * Listener for {@link OnKeyboardActionListener}.
     */
    private android.inputmethodservice.KeyboardView.OnKeyboardActionListener mKeyboardActionListener;

    private static final int MSG_SHOW_PREVIEW = 1;

    private static final int MSG_REMOVE_PREVIEW = 2;

    private static final int MSG_REPEAT = 3;

    private static final int MSG_LONGPRESS = 4;

    private static final int DELAY_BEFORE_PREVIEW = 0;

    private static final int DELAY_AFTER_PREVIEW = 70;

    private static final int DEBOUNCE_TIME = 70;

    private int mVerticalCorrection;

    private int mProximityThreshold;

    private boolean mPreviewCentered = false;

    private boolean mShowPreview = true;

    private boolean mShowTouchPoints = true;

    private int mPopupPreviewX;

    private int mPopupPreviewY;

    private int mLastX;

    private int mLastY;

    private int mStartX;

    private int mStartY;

    private boolean mProximityCorrectOn;

    private android.graphics.Paint mPaint;

    private android.graphics.Rect mPadding;

    private long mDownTime;

    private long mLastMoveTime;

    private int mLastKey;

    private int mLastCodeX;

    private int mLastCodeY;

    private int mCurrentKey = android.inputmethodservice.KeyboardView.NOT_A_KEY;

    private int mDownKey = android.inputmethodservice.KeyboardView.NOT_A_KEY;

    private long mLastKeyTime;

    private long mCurrentKeyTime;

    private int[] mKeyIndices = new int[12];

    private android.view.GestureDetector mGestureDetector;

    private int mPopupX;

    private int mPopupY;

    private int mRepeatKeyIndex = android.inputmethodservice.KeyboardView.NOT_A_KEY;

    private int mPopupLayout;

    private boolean mAbortKey;

    private android.inputmethodservice.Keyboard.Key mInvalidatedKey;

    private android.graphics.Rect mClipRegion = new android.graphics.Rect(0, 0, 0, 0);

    private boolean mPossiblePoly;

    private android.inputmethodservice.KeyboardView.SwipeTracker mSwipeTracker = new android.inputmethodservice.KeyboardView.SwipeTracker();

    private int mSwipeThreshold;

    private boolean mDisambiguateSwipe;

    // Variables for dealing with multiple pointers
    private int mOldPointerCount = 1;

    private float mOldPointerX;

    private float mOldPointerY;

    private android.graphics.drawable.Drawable mKeyBackground;

    private static final int REPEAT_INTERVAL = 50;// ~20 keys per second


    private static final int REPEAT_START_DELAY = 400;

    private static final int LONGPRESS_TIMEOUT = android.view.ViewConfiguration.getLongPressTimeout();

    private static int MAX_NEARBY_KEYS = 12;

    private int[] mDistances = new int[android.inputmethodservice.KeyboardView.MAX_NEARBY_KEYS];

    // For multi-tap
    private int mLastSentIndex;

    private int mTapCount;

    private long mLastTapTime;

    private boolean mInMultiTap;

    private static final int MULTITAP_INTERVAL = 800;// milliseconds


    private java.lang.StringBuilder mPreviewLabel = new java.lang.StringBuilder(1);

    /**
     * Whether the keyboard bitmap needs to be redrawn before it's blitted. *
     */
    private boolean mDrawPending;

    /**
     * The dirty region in the keyboard bitmap
     */
    private android.graphics.Rect mDirtyRect = new android.graphics.Rect();

    /**
     * The keyboard bitmap for faster updates
     */
    private android.graphics.Bitmap mBuffer;

    /**
     * Notes if the keyboard just changed, so that we could possibly reallocate the mBuffer.
     */
    private boolean mKeyboardChanged;

    /**
     * The canvas for the above mutable keyboard bitmap
     */
    private android.graphics.Canvas mCanvas;

    /**
     * The accessibility manager for accessibility support
     */
    private android.view.accessibility.AccessibilityManager mAccessibilityManager;

    /**
     * The audio manager for accessibility support
     */
    private android.media.AudioManager mAudioManager;

    /**
     * Whether the requirement of a headset to hear passwords if accessibility is enabled is announced.
     */
    private boolean mHeadsetRequiredToHearPasswordsAnnounced;

    android.os.Handler mHandler;

    public KeyboardView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.keyboardViewStyle);
    }

    public KeyboardView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public KeyboardView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.R.styleable.KeyboardView, defStyleAttr, defStyleRes);
        android.view.LayoutInflater inflate = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        int previewLayout = 0;
        int keyTextSize = 0;
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case com.android.internal.R.styleable.KeyboardView_keyBackground :
                    mKeyBackground = a.getDrawable(attr);
                    break;
                case com.android.internal.R.styleable.KeyboardView_verticalCorrection :
                    mVerticalCorrection = a.getDimensionPixelOffset(attr, 0);
                    break;
                case com.android.internal.R.styleable.KeyboardView_keyPreviewLayout :
                    previewLayout = a.getResourceId(attr, 0);
                    break;
                case com.android.internal.R.styleable.KeyboardView_keyPreviewOffset :
                    mPreviewOffset = a.getDimensionPixelOffset(attr, 0);
                    break;
                case com.android.internal.R.styleable.KeyboardView_keyPreviewHeight :
                    mPreviewHeight = a.getDimensionPixelSize(attr, 80);
                    break;
                case com.android.internal.R.styleable.KeyboardView_keyTextSize :
                    mKeyTextSize = a.getDimensionPixelSize(attr, 18);
                    break;
                case com.android.internal.R.styleable.KeyboardView_keyTextColor :
                    mKeyTextColor = a.getColor(attr, 0xff000000);
                    break;
                case com.android.internal.R.styleable.KeyboardView_labelTextSize :
                    mLabelTextSize = a.getDimensionPixelSize(attr, 14);
                    break;
                case com.android.internal.R.styleable.KeyboardView_popupLayout :
                    mPopupLayout = a.getResourceId(attr, 0);
                    break;
                case com.android.internal.R.styleable.KeyboardView_shadowColor :
                    mShadowColor = a.getColor(attr, 0);
                    break;
                case com.android.internal.R.styleable.KeyboardView_shadowRadius :
                    mShadowRadius = a.getFloat(attr, 0.0F);
                    break;
            }
        }
        a = mContext.obtainStyledAttributes(com.android.internal.R.styleable.Theme);
        mBackgroundDimAmount = a.getFloat(android.R.styleable.Theme_backgroundDimAmount, 0.5F);
        mPreviewPopup = new android.widget.PopupWindow(context);
        if (previewLayout != 0) {
            mPreviewText = ((android.widget.TextView) (inflate.inflate(previewLayout, null)));
            mPreviewTextSizeLarge = ((int) (mPreviewText.getTextSize()));
            mPreviewPopup.setContentView(mPreviewText);
            mPreviewPopup.setBackgroundDrawable(null);
        } else {
            mShowPreview = false;
        }
        mPreviewPopup.setTouchable(false);
        mPopupKeyboard = new android.widget.PopupWindow(context);
        mPopupKeyboard.setBackgroundDrawable(null);
        // mPopupKeyboard.setClippingEnabled(false);
        mPopupParent = this;
        // mPredicting = true;
        mPaint = new android.graphics.Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(keyTextSize);
        mPaint.setTextAlign(android.graphics.Paint.Align.CENTER);
        mPaint.setAlpha(255);
        mPadding = new android.graphics.Rect(0, 0, 0, 0);
        mMiniKeyboardCache = new java.util.HashMap<android.inputmethodservice.Keyboard.Key, android.view.View>();
        mKeyBackground.getPadding(mPadding);
        mSwipeThreshold = ((int) (500 * getResources().getDisplayMetrics().density));
        mDisambiguateSwipe = getResources().getBoolean(com.android.internal.R.bool.config_swipeDisambiguation);
        mAccessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(context);
        mAudioManager = ((android.media.AudioManager) (context.getSystemService(android.content.Context.AUDIO_SERVICE)));
        resetMultiTap();
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initGestureDetector();
        if (mHandler == null) {
            mHandler = new android.os.Handler() {
                @java.lang.Override
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case android.inputmethodservice.KeyboardView.MSG_SHOW_PREVIEW :
                            showKey(msg.arg1);
                            break;
                        case android.inputmethodservice.KeyboardView.MSG_REMOVE_PREVIEW :
                            mPreviewText.setVisibility(android.view.View.INVISIBLE);
                            break;
                        case android.inputmethodservice.KeyboardView.MSG_REPEAT :
                            if (repeatKey()) {
                                android.os.Message repeat = android.os.Message.obtain(this, android.inputmethodservice.KeyboardView.MSG_REPEAT);
                                sendMessageDelayed(repeat, android.inputmethodservice.KeyboardView.REPEAT_INTERVAL);
                            }
                            break;
                        case android.inputmethodservice.KeyboardView.MSG_LONGPRESS :
                            openPopupIfRequired(((android.view.MotionEvent) (msg.obj)));
                            break;
                    }
                }
            };
        }
    }

    private void initGestureDetector() {
        if (mGestureDetector == null) {
            mGestureDetector = new android.view.GestureDetector(getContext(), new android.view.GestureDetector.SimpleOnGestureListener() {
                @java.lang.Override
                public boolean onFling(android.view.MotionEvent me1, android.view.MotionEvent me2, float velocityX, float velocityY) {
                    if (mPossiblePoly)
                        return false;

                    final float absX = java.lang.Math.abs(velocityX);
                    final float absY = java.lang.Math.abs(velocityY);
                    float deltaX = me2.getX() - me1.getX();
                    float deltaY = me2.getY() - me1.getY();
                    int travelX = getWidth() / 2;// Half the keyboard width

                    int travelY = getHeight() / 2;// Half the keyboard height

                    mSwipeTracker.computeCurrentVelocity(1000);
                    final float endingVelocityX = mSwipeTracker.getXVelocity();
                    final float endingVelocityY = mSwipeTracker.getYVelocity();
                    boolean sendDownKey = false;
                    if (((velocityX > mSwipeThreshold) && (absY < absX)) && (deltaX > travelX)) {
                        if (mDisambiguateSwipe && (endingVelocityX < (velocityX / 4))) {
                            sendDownKey = true;
                        } else {
                            swipeRight();
                            return true;
                        }
                    } else
                        if (((velocityX < (-mSwipeThreshold)) && (absY < absX)) && (deltaX < (-travelX))) {
                            if (mDisambiguateSwipe && (endingVelocityX > (velocityX / 4))) {
                                sendDownKey = true;
                            } else {
                                swipeLeft();
                                return true;
                            }
                        } else
                            if (((velocityY < (-mSwipeThreshold)) && (absX < absY)) && (deltaY < (-travelY))) {
                                if (mDisambiguateSwipe && (endingVelocityY > (velocityY / 4))) {
                                    sendDownKey = true;
                                } else {
                                    swipeUp();
                                    return true;
                                }
                            } else
                                if (((velocityY > mSwipeThreshold) && (absX < (absY / 2))) && (deltaY > travelY)) {
                                    if (mDisambiguateSwipe && (endingVelocityY < (velocityY / 4))) {
                                        sendDownKey = true;
                                    } else {
                                        swipeDown();
                                        return true;
                                    }
                                }



                    if (sendDownKey) {
                        detectAndSendKey(mDownKey, mStartX, mStartY, me1.getEventTime());
                    }
                    return false;
                }
            });
            mGestureDetector.setIsLongpressEnabled(false);
        }
    }

    public void setOnKeyboardActionListener(android.inputmethodservice.KeyboardView.OnKeyboardActionListener listener) {
        mKeyboardActionListener = listener;
    }

    /**
     * Returns the {@link OnKeyboardActionListener} object.
     *
     * @return the listener attached to this keyboard
     */
    protected android.inputmethodservice.KeyboardView.OnKeyboardActionListener getOnKeyboardActionListener() {
        return mKeyboardActionListener;
    }

    /**
     * Attaches a keyboard to this view. The keyboard can be switched at any time and the
     * view will re-layout itself to accommodate the keyboard.
     *
     * @see Keyboard
     * @see #getKeyboard()
     * @param keyboard
     * 		the keyboard to display in this view
     */
    public void setKeyboard(android.inputmethodservice.Keyboard keyboard) {
        if (mKeyboard != null) {
            showPreview(android.inputmethodservice.KeyboardView.NOT_A_KEY);
        }
        // Remove any pending messages
        removeMessages();
        mKeyboard = keyboard;
        java.util.List<android.inputmethodservice.Keyboard.Key> keys = mKeyboard.getKeys();
        mKeys = keys.toArray(new android.inputmethodservice.Keyboard.Key[keys.size()]);
        requestLayout();
        // Hint to reallocate the buffer if the size changed
        mKeyboardChanged = true;
        invalidateAllKeys();
        computeProximityThreshold(keyboard);
        mMiniKeyboardCache.clear();// Not really necessary to do every time, but will free up views

        // Switching to a different keyboard should abort any pending keys so that the key up
        // doesn't get delivered to the old or new keyboard
        mAbortKey = true;// Until the next ACTION_DOWN

    }

    /**
     * Returns the current keyboard being displayed by this view.
     *
     * @return the currently attached keyboard
     * @see #setKeyboard(Keyboard)
     */
    public android.inputmethodservice.Keyboard getKeyboard() {
        return mKeyboard;
    }

    /**
     * Sets the state of the shift key of the keyboard, if any.
     *
     * @param shifted
     * 		whether or not to enable the state of the shift key
     * @return true if the shift key state changed, false if there was no change
     * @see KeyboardView#isShifted()
     */
    public boolean setShifted(boolean shifted) {
        if (mKeyboard != null) {
            if (mKeyboard.setShifted(shifted)) {
                // The whole keyboard probably needs to be redrawn
                invalidateAllKeys();
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the state of the shift key of the keyboard, if any.
     *
     * @return true if the shift is in a pressed state, false otherwise. If there is
    no shift key on the keyboard or there is no keyboard attached, it returns false.
     * @see KeyboardView#setShifted(boolean)
     */
    public boolean isShifted() {
        if (mKeyboard != null) {
            return mKeyboard.isShifted();
        }
        return false;
    }

    /**
     * Enables or disables the key feedback popup. This is a popup that shows a magnified
     * version of the depressed key. By default the preview is enabled.
     *
     * @param previewEnabled
     * 		whether or not to enable the key feedback popup
     * @see #isPreviewEnabled()
     */
    public void setPreviewEnabled(boolean previewEnabled) {
        mShowPreview = previewEnabled;
    }

    /**
     * Returns the enabled state of the key feedback popup.
     *
     * @return whether or not the key feedback popup is enabled
     * @see #setPreviewEnabled(boolean)
     */
    public boolean isPreviewEnabled() {
        return mShowPreview;
    }

    public void setVerticalCorrection(int verticalOffset) {
    }

    public void setPopupParent(android.view.View v) {
        mPopupParent = v;
    }

    public void setPopupOffset(int x, int y) {
        mMiniKeyboardOffsetX = x;
        mMiniKeyboardOffsetY = y;
        if (mPreviewPopup.isShowing()) {
            mPreviewPopup.dismiss();
        }
    }

    /**
     * When enabled, calls to {@link OnKeyboardActionListener#onKey} will include key
     * codes for adjacent keys.  When disabled, only the primary key code will be
     * reported.
     *
     * @param enabled
     * 		whether or not the proximity correction is enabled
     */
    public void setProximityCorrectionEnabled(boolean enabled) {
        mProximityCorrectOn = enabled;
    }

    /**
     * Returns true if proximity correction is enabled.
     */
    public boolean isProximityCorrectionEnabled() {
        return mProximityCorrectOn;
    }

    /**
     * Popup keyboard close button clicked.
     *
     * @unknown 
     */
    public void onClick(android.view.View v) {
        dismissPopupKeyboard();
    }

    private java.lang.CharSequence adjustCase(java.lang.CharSequence label) {
        if (((mKeyboard.isShifted() && (label != null)) && (label.length() < 3)) && java.lang.Character.isLowerCase(label.charAt(0))) {
            label = label.toString().toUpperCase();
        }
        return label;
    }

    @java.lang.Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Round up a little
        if (mKeyboard == null) {
            setMeasuredDimension(mPaddingLeft + mPaddingRight, mPaddingTop + mPaddingBottom);
        } else {
            int width = (mKeyboard.getMinWidth() + mPaddingLeft) + mPaddingRight;
            if (android.view.View.MeasureSpec.getSize(widthMeasureSpec) < (width + 10)) {
                width = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
            }
            setMeasuredDimension(width, (mKeyboard.getHeight() + mPaddingTop) + mPaddingBottom);
        }
    }

    /**
     * Compute the average distance between adjacent keys (horizontally and vertically)
     * and square it to get the proximity threshold. We use a square here and in computing
     * the touch distance from a key's center to avoid taking a square root.
     *
     * @param keyboard
     * 		
     */
    private void computeProximityThreshold(android.inputmethodservice.Keyboard keyboard) {
        if (keyboard == null)
            return;

        final android.inputmethodservice.Keyboard.Key[] keys = mKeys;
        if (keys == null)
            return;

        int length = keys.length;
        int dimensionSum = 0;
        for (int i = 0; i < length; i++) {
            android.inputmethodservice.Keyboard.Key key = keys[i];
            dimensionSum += java.lang.Math.min(key.width, key.height) + key.gap;
        }
        if ((dimensionSum < 0) || (length == 0))
            return;

        mProximityThreshold = ((int) ((dimensionSum * 1.4F) / length));
        mProximityThreshold *= mProximityThreshold;// Square it

    }

    @java.lang.Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mKeyboard != null) {
            mKeyboard.resize(w, h);
        }
        // Release the buffer, if any and it will be reallocated on the next draw
        mBuffer = null;
    }

    @java.lang.Override
    public void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if ((mDrawPending || (mBuffer == null)) || mKeyboardChanged) {
            onBufferDraw();
        }
        canvas.drawBitmap(mBuffer, 0, 0, null);
    }

    private void onBufferDraw() {
        if ((mBuffer == null) || mKeyboardChanged) {
            if ((mBuffer == null) || (mKeyboardChanged && ((mBuffer.getWidth() != getWidth()) || (mBuffer.getHeight() != getHeight())))) {
                // Make sure our bitmap is at least 1x1
                final int width = java.lang.Math.max(1, getWidth());
                final int height = java.lang.Math.max(1, getHeight());
                mBuffer = android.graphics.Bitmap.createBitmap(width, height, android.graphics.Bitmap.Config.ARGB_8888);
                mCanvas = new android.graphics.Canvas(mBuffer);
            }
            invalidateAllKeys();
            mKeyboardChanged = false;
        }
        final android.graphics.Canvas canvas = mCanvas;
        canvas.clipRect(mDirtyRect, android.graphics.Region.Op.REPLACE);
        if (mKeyboard == null)
            return;

        final android.graphics.Paint paint = mPaint;
        final android.graphics.drawable.Drawable keyBackground = mKeyBackground;
        final android.graphics.Rect clipRegion = mClipRegion;
        final android.graphics.Rect padding = mPadding;
        final int kbdPaddingLeft = mPaddingLeft;
        final int kbdPaddingTop = mPaddingTop;
        final android.inputmethodservice.Keyboard.Key[] keys = mKeys;
        final android.inputmethodservice.Keyboard.Key invalidKey = mInvalidatedKey;
        paint.setColor(mKeyTextColor);
        boolean drawSingleKey = false;
        if ((invalidKey != null) && canvas.getClipBounds(clipRegion)) {
            // Is clipRegion completely contained within the invalidated key?
            if ((((((invalidKey.x + kbdPaddingLeft) - 1) <= clipRegion.left) && (((invalidKey.y + kbdPaddingTop) - 1) <= clipRegion.top)) && ((((invalidKey.x + invalidKey.width) + kbdPaddingLeft) + 1) >= clipRegion.right)) && ((((invalidKey.y + invalidKey.height) + kbdPaddingTop) + 1) >= clipRegion.bottom)) {
                drawSingleKey = true;
            }
        }
        canvas.drawColor(0x0, android.graphics.PorterDuff.Mode.CLEAR);
        final int keyCount = keys.length;
        for (int i = 0; i < keyCount; i++) {
            final android.inputmethodservice.Keyboard.Key key = keys[i];
            if (drawSingleKey && (invalidKey != key)) {
                continue;
            }
            int[] drawableState = key.getCurrentDrawableState();
            keyBackground.setState(drawableState);
            // Switch the character to uppercase if shift is pressed
            java.lang.String label = (key.label == null) ? null : adjustCase(key.label).toString();
            final android.graphics.Rect bounds = keyBackground.getBounds();
            if ((key.width != bounds.right) || (key.height != bounds.bottom)) {
                keyBackground.setBounds(0, 0, key.width, key.height);
            }
            canvas.translate(key.x + kbdPaddingLeft, key.y + kbdPaddingTop);
            keyBackground.draw(canvas);
            if (label != null) {
                // For characters, use large font. For labels like "Done", use small font.
                if ((label.length() > 1) && (key.codes.length < 2)) {
                    paint.setTextSize(mLabelTextSize);
                    paint.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
                } else {
                    paint.setTextSize(mKeyTextSize);
                    paint.setTypeface(android.graphics.Typeface.DEFAULT);
                }
                // Draw a drop shadow for the text
                paint.setShadowLayer(mShadowRadius, 0, 0, mShadowColor);
                // Draw the text
                canvas.drawText(label, (((key.width - padding.left) - padding.right) / 2) + padding.left, ((((key.height - padding.top) - padding.bottom) / 2) + ((paint.getTextSize() - paint.descent()) / 2)) + padding.top, paint);
                // Turn off drop shadow
                paint.setShadowLayer(0, 0, 0, 0);
            } else
                if (key.icon != null) {
                    final int drawableX = ((((key.width - padding.left) - padding.right) - key.icon.getIntrinsicWidth()) / 2) + padding.left;
                    final int drawableY = ((((key.height - padding.top) - padding.bottom) - key.icon.getIntrinsicHeight()) / 2) + padding.top;
                    canvas.translate(drawableX, drawableY);
                    key.icon.setBounds(0, 0, key.icon.getIntrinsicWidth(), key.icon.getIntrinsicHeight());
                    key.icon.draw(canvas);
                    canvas.translate(-drawableX, -drawableY);
                }

            canvas.translate((-key.x) - kbdPaddingLeft, (-key.y) - kbdPaddingTop);
        }
        mInvalidatedKey = null;
        // Overlay a dark rectangle to dim the keyboard
        if (mMiniKeyboardOnScreen) {
            paint.setColor(((int) (mBackgroundDimAmount * 0xff)) << 24);
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }
        if (android.inputmethodservice.KeyboardView.DEBUG && mShowTouchPoints) {
            paint.setAlpha(128);
            paint.setColor(0xffff0000);
            canvas.drawCircle(mStartX, mStartY, 3, paint);
            canvas.drawLine(mStartX, mStartY, mLastX, mLastY, paint);
            paint.setColor(0xff0000ff);
            canvas.drawCircle(mLastX, mLastY, 3, paint);
            paint.setColor(0xff00ff00);
            canvas.drawCircle((mStartX + mLastX) / 2, (mStartY + mLastY) / 2, 2, paint);
        }
        mDrawPending = false;
        mDirtyRect.setEmpty();
    }

    private int getKeyIndices(int x, int y, int[] allKeys) {
        final android.inputmethodservice.Keyboard.Key[] keys = mKeys;
        int primaryIndex = android.inputmethodservice.KeyboardView.NOT_A_KEY;
        int closestKey = android.inputmethodservice.KeyboardView.NOT_A_KEY;
        int closestKeyDist = mProximityThreshold + 1;
        java.util.Arrays.fill(mDistances, java.lang.Integer.MAX_VALUE);
        int[] nearestKeyIndices = mKeyboard.getNearestKeys(x, y);
        final int keyCount = nearestKeyIndices.length;
        for (int i = 0; i < keyCount; i++) {
            final android.inputmethodservice.Keyboard.Key key = keys[nearestKeyIndices[i]];
            int dist = 0;
            boolean isInside = key.isInside(x, y);
            if (isInside) {
                primaryIndex = nearestKeyIndices[i];
            }
            if (((mProximityCorrectOn && ((dist = key.squaredDistanceFrom(x, y)) < mProximityThreshold)) || isInside) && (key.codes[0] > 32)) {
                // Find insertion point
                final int nCodes = key.codes.length;
                if (dist < closestKeyDist) {
                    closestKeyDist = dist;
                    closestKey = nearestKeyIndices[i];
                }
                if (allKeys == null)
                    continue;

                for (int j = 0; j < mDistances.length; j++) {
                    if (mDistances[j] > dist) {
                        // Make space for nCodes codes
                        java.lang.System.arraycopy(mDistances, j, mDistances, j + nCodes, (mDistances.length - j) - nCodes);
                        java.lang.System.arraycopy(allKeys, j, allKeys, j + nCodes, (allKeys.length - j) - nCodes);
                        for (int c = 0; c < nCodes; c++) {
                            allKeys[j + c] = key.codes[c];
                            mDistances[j + c] = dist;
                        }
                        break;
                    }
                }
            }
        }
        if (primaryIndex == android.inputmethodservice.KeyboardView.NOT_A_KEY) {
            primaryIndex = closestKey;
        }
        return primaryIndex;
    }

    private void detectAndSendKey(int index, int x, int y, long eventTime) {
        if ((index != android.inputmethodservice.KeyboardView.NOT_A_KEY) && (index < mKeys.length)) {
            final android.inputmethodservice.Keyboard.Key key = mKeys[index];
            if (key.text != null) {
                mKeyboardActionListener.onText(key.text);
                mKeyboardActionListener.onRelease(android.inputmethodservice.KeyboardView.NOT_A_KEY);
            } else {
                int code = key.codes[0];
                // TextEntryState.keyPressedAt(key, x, y);
                int[] codes = new int[android.inputmethodservice.KeyboardView.MAX_NEARBY_KEYS];
                java.util.Arrays.fill(codes, android.inputmethodservice.KeyboardView.NOT_A_KEY);
                getKeyIndices(x, y, codes);
                // Multi-tap
                if (mInMultiTap) {
                    if (mTapCount != (-1)) {
                        mKeyboardActionListener.onKey(android.inputmethodservice.Keyboard.KEYCODE_DELETE, android.inputmethodservice.KeyboardView.KEY_DELETE);
                    } else {
                        mTapCount = 0;
                    }
                    code = key.codes[mTapCount];
                }
                mKeyboardActionListener.onKey(code, codes);
                mKeyboardActionListener.onRelease(code);
            }
            mLastSentIndex = index;
            mLastTapTime = eventTime;
        }
    }

    /**
     * Handle multi-tap keys by producing the key label for the current multi-tap state.
     */
    private java.lang.CharSequence getPreviewText(android.inputmethodservice.Keyboard.Key key) {
        if (mInMultiTap) {
            // Multi-tap
            mPreviewLabel.setLength(0);
            mPreviewLabel.append(((char) (key.codes[mTapCount < 0 ? 0 : mTapCount])));
            return adjustCase(mPreviewLabel);
        } else {
            return adjustCase(key.label);
        }
    }

    private void showPreview(int keyIndex) {
        int oldKeyIndex = mCurrentKeyIndex;
        final android.widget.PopupWindow previewPopup = mPreviewPopup;
        mCurrentKeyIndex = keyIndex;
        // Release the old key and press the new key
        final android.inputmethodservice.Keyboard.Key[] keys = mKeys;
        if (oldKeyIndex != mCurrentKeyIndex) {
            if ((oldKeyIndex != android.inputmethodservice.KeyboardView.NOT_A_KEY) && (keys.length > oldKeyIndex)) {
                android.inputmethodservice.Keyboard.Key oldKey = keys[oldKeyIndex];
                oldKey.onReleased(mCurrentKeyIndex == android.inputmethodservice.KeyboardView.NOT_A_KEY);
                invalidateKey(oldKeyIndex);
                final int keyCode = oldKey.codes[0];
                sendAccessibilityEventForUnicodeCharacter(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_HOVER_EXIT, keyCode);
                // TODO: We need to implement AccessibilityNodeProvider for this view.
                sendAccessibilityEventForUnicodeCharacter(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED, keyCode);
            }
            if ((mCurrentKeyIndex != android.inputmethodservice.KeyboardView.NOT_A_KEY) && (keys.length > mCurrentKeyIndex)) {
                android.inputmethodservice.Keyboard.Key newKey = keys[mCurrentKeyIndex];
                newKey.onPressed();
                invalidateKey(mCurrentKeyIndex);
                final int keyCode = newKey.codes[0];
                sendAccessibilityEventForUnicodeCharacter(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_HOVER_ENTER, keyCode);
                // TODO: We need to implement AccessibilityNodeProvider for this view.
                sendAccessibilityEventForUnicodeCharacter(android.view.accessibility.AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED, keyCode);
            }
        }
        // If key changed and preview is on ...
        if ((oldKeyIndex != mCurrentKeyIndex) && mShowPreview) {
            mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_SHOW_PREVIEW);
            if (previewPopup.isShowing()) {
                if (keyIndex == android.inputmethodservice.KeyboardView.NOT_A_KEY) {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(android.inputmethodservice.KeyboardView.MSG_REMOVE_PREVIEW), android.inputmethodservice.KeyboardView.DELAY_AFTER_PREVIEW);
                }
            }
            if (keyIndex != android.inputmethodservice.KeyboardView.NOT_A_KEY) {
                if (previewPopup.isShowing() && (mPreviewText.getVisibility() == android.view.View.VISIBLE)) {
                    // Show right away, if it's already visible and finger is moving around
                    showKey(keyIndex);
                } else {
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(android.inputmethodservice.KeyboardView.MSG_SHOW_PREVIEW, keyIndex, 0), android.inputmethodservice.KeyboardView.DELAY_BEFORE_PREVIEW);
                }
            }
        }
    }

    private void showKey(final int keyIndex) {
        final android.widget.PopupWindow previewPopup = mPreviewPopup;
        final android.inputmethodservice.Keyboard.Key[] keys = mKeys;
        if ((keyIndex < 0) || (keyIndex >= mKeys.length))
            return;

        android.inputmethodservice.Keyboard.Key key = keys[keyIndex];
        if (key.icon != null) {
            mPreviewText.setCompoundDrawables(null, null, null, key.iconPreview != null ? key.iconPreview : key.icon);
            mPreviewText.setText(null);
        } else {
            mPreviewText.setCompoundDrawables(null, null, null, null);
            mPreviewText.setText(getPreviewText(key));
            if ((key.label.length() > 1) && (key.codes.length < 2)) {
                mPreviewText.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mKeyTextSize);
                mPreviewText.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
            } else {
                mPreviewText.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, mPreviewTextSizeLarge);
                mPreviewText.setTypeface(android.graphics.Typeface.DEFAULT);
            }
        }
        mPreviewText.measure(android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED), android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED));
        int popupWidth = java.lang.Math.max(mPreviewText.getMeasuredWidth(), (key.width + mPreviewText.getPaddingLeft()) + mPreviewText.getPaddingRight());
        final int popupHeight = mPreviewHeight;
        android.view.ViewGroup.LayoutParams lp = mPreviewText.getLayoutParams();
        if (lp != null) {
            lp.width = popupWidth;
            lp.height = popupHeight;
        }
        if (!mPreviewCentered) {
            mPopupPreviewX = (key.x - mPreviewText.getPaddingLeft()) + mPaddingLeft;
            mPopupPreviewY = (key.y - popupHeight) + mPreviewOffset;
        } else {
            // TODO: Fix this if centering is brought back
            mPopupPreviewX = 160 - (mPreviewText.getMeasuredWidth() / 2);
            mPopupPreviewY = -mPreviewText.getMeasuredHeight();
        }
        mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_REMOVE_PREVIEW);
        getLocationInWindow(mCoordinates);
        mCoordinates[0] += mMiniKeyboardOffsetX;// Offset may be zero

        mCoordinates[1] += mMiniKeyboardOffsetY;// Offset may be zero

        // Set the preview background state
        mPreviewText.getBackground().setState(key.popupResId != 0 ? android.inputmethodservice.KeyboardView.LONG_PRESSABLE_STATE_SET : android.view.View.EMPTY_STATE_SET);
        mPopupPreviewX += mCoordinates[0];
        mPopupPreviewY += mCoordinates[1];
        // If the popup cannot be shown above the key, put it on the side
        getLocationOnScreen(mCoordinates);
        if ((mPopupPreviewY + mCoordinates[1]) < 0) {
            // If the key you're pressing is on the left side of the keyboard, show the popup on
            // the right, offset by enough to see at least one key to the left/right.
            if ((key.x + key.width) <= (getWidth() / 2)) {
                mPopupPreviewX += ((int) (key.width * 2.5));
            } else {
                mPopupPreviewX -= ((int) (key.width * 2.5));
            }
            mPopupPreviewY += popupHeight;
        }
        if (previewPopup.isShowing()) {
            previewPopup.update(mPopupPreviewX, mPopupPreviewY, popupWidth, popupHeight);
        } else {
            previewPopup.setWidth(popupWidth);
            previewPopup.setHeight(popupHeight);
            previewPopup.showAtLocation(mPopupParent, android.view.Gravity.NO_GRAVITY, mPopupPreviewX, mPopupPreviewY);
        }
        mPreviewText.setVisibility(android.view.View.VISIBLE);
    }

    private void sendAccessibilityEventForUnicodeCharacter(int eventType, int code) {
        if (mAccessibilityManager.isEnabled()) {
            android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(eventType);
            onInitializeAccessibilityEvent(event);
            java.lang.String text = null;
            // This is very efficient since the properties are cached.
            final boolean speakPassword = android.provider.Settings.Secure.getIntForUser(mContext.getContentResolver(), android.provider.Settings.Secure.ACCESSIBILITY_SPEAK_PASSWORD, 0, android.os.UserHandle.USER_CURRENT_OR_SELF) != 0;
            // Add text only if password announcement is enabled or if headset is
            // used to avoid leaking passwords.
            if ((speakPassword || mAudioManager.isBluetoothA2dpOn()) || mAudioManager.isWiredHeadsetOn()) {
                switch (code) {
                    case android.inputmethodservice.Keyboard.KEYCODE_ALT :
                        text = mContext.getString(R.string.keyboardview_keycode_alt);
                        break;
                    case android.inputmethodservice.Keyboard.KEYCODE_CANCEL :
                        text = mContext.getString(R.string.keyboardview_keycode_cancel);
                        break;
                    case android.inputmethodservice.Keyboard.KEYCODE_DELETE :
                        text = mContext.getString(R.string.keyboardview_keycode_delete);
                        break;
                    case android.inputmethodservice.Keyboard.KEYCODE_DONE :
                        text = mContext.getString(R.string.keyboardview_keycode_done);
                        break;
                    case android.inputmethodservice.Keyboard.KEYCODE_MODE_CHANGE :
                        text = mContext.getString(R.string.keyboardview_keycode_mode_change);
                        break;
                    case android.inputmethodservice.Keyboard.KEYCODE_SHIFT :
                        text = mContext.getString(R.string.keyboardview_keycode_shift);
                        break;
                    case '\n' :
                        text = mContext.getString(R.string.keyboardview_keycode_enter);
                        break;
                    default :
                        text = java.lang.String.valueOf(((char) (code)));
                }
            } else
                if (!mHeadsetRequiredToHearPasswordsAnnounced) {
                    // We want the waring for required head set to be send with both the
                    // hover enter and hover exit event, so set the flag after the exit.
                    if (eventType == android.view.accessibility.AccessibilityEvent.TYPE_VIEW_HOVER_EXIT) {
                        mHeadsetRequiredToHearPasswordsAnnounced = true;
                    }
                    text = mContext.getString(R.string.keyboard_headset_required_to_hear_password);
                } else {
                    text = mContext.getString(R.string.keyboard_password_character_no_headset);
                }

            event.getText().add(text);
            mAccessibilityManager.sendAccessibilityEvent(event);
        }
    }

    /**
     * Requests a redraw of the entire keyboard. Calling {@link #invalidate} is not sufficient
     * because the keyboard renders the keys to an off-screen buffer and an invalidate() only
     * draws the cached buffer.
     *
     * @see #invalidateKey(int)
     */
    public void invalidateAllKeys() {
        mDirtyRect.union(0, 0, getWidth(), getHeight());
        mDrawPending = true;
        invalidate();
    }

    /**
     * Invalidates a key so that it will be redrawn on the next repaint. Use this method if only
     * one key is changing it's content. Any changes that affect the position or size of the key
     * may not be honored.
     *
     * @param keyIndex
     * 		the index of the key in the attached {@link Keyboard}.
     * @see #invalidateAllKeys
     */
    public void invalidateKey(int keyIndex) {
        if (mKeys == null)
            return;

        if ((keyIndex < 0) || (keyIndex >= mKeys.length)) {
            return;
        }
        final android.inputmethodservice.Keyboard.Key key = mKeys[keyIndex];
        mInvalidatedKey = key;
        mDirtyRect.union(key.x + mPaddingLeft, key.y + mPaddingTop, (key.x + key.width) + mPaddingLeft, (key.y + key.height) + mPaddingTop);
        onBufferDraw();
        invalidate(key.x + mPaddingLeft, key.y + mPaddingTop, (key.x + key.width) + mPaddingLeft, (key.y + key.height) + mPaddingTop);
    }

    private boolean openPopupIfRequired(android.view.MotionEvent me) {
        // Check if we have a popup layout specified first.
        if (mPopupLayout == 0) {
            return false;
        }
        if ((mCurrentKey < 0) || (mCurrentKey >= mKeys.length)) {
            return false;
        }
        android.inputmethodservice.Keyboard.Key popupKey = mKeys[mCurrentKey];
        boolean result = onLongPress(popupKey);
        if (result) {
            mAbortKey = true;
            showPreview(android.inputmethodservice.KeyboardView.NOT_A_KEY);
        }
        return result;
    }

    /**
     * Called when a key is long pressed. By default this will open any popup keyboard associated
     * with this key through the attributes popupLayout and popupCharacters.
     *
     * @param popupKey
     * 		the key that was long pressed
     * @return true if the long press is handled, false otherwise. Subclasses should call the
    method on the base class if the subclass doesn't wish to handle the call.
     */
    protected boolean onLongPress(android.inputmethodservice.Keyboard.Key popupKey) {
        int popupKeyboardId = popupKey.popupResId;
        if (popupKeyboardId != 0) {
            mMiniKeyboardContainer = mMiniKeyboardCache.get(popupKey);
            if (mMiniKeyboardContainer == null) {
                android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (getContext().getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
                mMiniKeyboardContainer = inflater.inflate(mPopupLayout, null);
                mMiniKeyboard = ((android.inputmethodservice.KeyboardView) (mMiniKeyboardContainer.findViewById(com.android.internal.R.id.keyboardView)));
                android.view.View closeButton = mMiniKeyboardContainer.findViewById(com.android.internal.R.id.closeButton);
                if (closeButton != null)
                    closeButton.setOnClickListener(this);

                mMiniKeyboard.setOnKeyboardActionListener(new android.inputmethodservice.KeyboardView.OnKeyboardActionListener() {
                    public void onKey(int primaryCode, int[] keyCodes) {
                        mKeyboardActionListener.onKey(primaryCode, keyCodes);
                        dismissPopupKeyboard();
                    }

                    public void onText(java.lang.CharSequence text) {
                        mKeyboardActionListener.onText(text);
                        dismissPopupKeyboard();
                    }

                    public void swipeLeft() {
                    }

                    public void swipeRight() {
                    }

                    public void swipeUp() {
                    }

                    public void swipeDown() {
                    }

                    public void onPress(int primaryCode) {
                        mKeyboardActionListener.onPress(primaryCode);
                    }

                    public void onRelease(int primaryCode) {
                        mKeyboardActionListener.onRelease(primaryCode);
                    }
                });
                // mInputView.setSuggest(mSuggest);
                android.inputmethodservice.Keyboard keyboard;
                if (popupKey.popupCharacters != null) {
                    keyboard = new android.inputmethodservice.Keyboard(getContext(), popupKeyboardId, popupKey.popupCharacters, -1, getPaddingLeft() + getPaddingRight());
                } else {
                    keyboard = new android.inputmethodservice.Keyboard(getContext(), popupKeyboardId);
                }
                mMiniKeyboard.setKeyboard(keyboard);
                mMiniKeyboard.setPopupParent(this);
                mMiniKeyboardContainer.measure(android.view.View.MeasureSpec.makeMeasureSpec(getWidth(), android.view.View.MeasureSpec.AT_MOST), android.view.View.MeasureSpec.makeMeasureSpec(getHeight(), android.view.View.MeasureSpec.AT_MOST));
                mMiniKeyboardCache.put(popupKey, mMiniKeyboardContainer);
            } else {
                mMiniKeyboard = ((android.inputmethodservice.KeyboardView) (mMiniKeyboardContainer.findViewById(com.android.internal.R.id.keyboardView)));
            }
            getLocationInWindow(mCoordinates);
            mPopupX = popupKey.x + mPaddingLeft;
            mPopupY = popupKey.y + mPaddingTop;
            mPopupX = (mPopupX + popupKey.width) - mMiniKeyboardContainer.getMeasuredWidth();
            mPopupY = mPopupY - mMiniKeyboardContainer.getMeasuredHeight();
            final int x = (mPopupX + mMiniKeyboardContainer.getPaddingRight()) + mCoordinates[0];
            final int y = (mPopupY + mMiniKeyboardContainer.getPaddingBottom()) + mCoordinates[1];
            mMiniKeyboard.setPopupOffset(x < 0 ? 0 : x, y);
            mMiniKeyboard.setShifted(isShifted());
            mPopupKeyboard.setContentView(mMiniKeyboardContainer);
            mPopupKeyboard.setWidth(mMiniKeyboardContainer.getMeasuredWidth());
            mPopupKeyboard.setHeight(mMiniKeyboardContainer.getMeasuredHeight());
            mPopupKeyboard.showAtLocation(this, android.view.Gravity.NO_GRAVITY, x, y);
            mMiniKeyboardOnScreen = true;
            // mMiniKeyboard.onTouchEvent(getTranslatedEvent(me));
            invalidateAllKeys();
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean onHoverEvent(android.view.MotionEvent event) {
        if (mAccessibilityManager.isTouchExplorationEnabled() && (event.getPointerCount() == 1)) {
            final int action = event.getAction();
            switch (action) {
                case android.view.MotionEvent.ACTION_HOVER_ENTER :
                    {
                        event.setAction(android.view.MotionEvent.ACTION_DOWN);
                    }
                    break;
                case android.view.MotionEvent.ACTION_HOVER_MOVE :
                    {
                        event.setAction(android.view.MotionEvent.ACTION_MOVE);
                    }
                    break;
                case android.view.MotionEvent.ACTION_HOVER_EXIT :
                    {
                        event.setAction(android.view.MotionEvent.ACTION_UP);
                    }
                    break;
            }
            return onTouchEvent(event);
        }
        return true;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent me) {
        // Convert multi-pointer up/down events to single up/down events to
        // deal with the typical multi-pointer behavior of two-thumb typing
        final int pointerCount = me.getPointerCount();
        final int action = me.getAction();
        boolean result = false;
        final long now = me.getEventTime();
        if (pointerCount != mOldPointerCount) {
            if (pointerCount == 1) {
                // Send a down event for the latest pointer
                android.view.MotionEvent down = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_DOWN, me.getX(), me.getY(), me.getMetaState());
                result = onModifiedTouchEvent(down, false);
                down.recycle();
                // If it's an up action, then deliver the up as well.
                if (action == android.view.MotionEvent.ACTION_UP) {
                    result = onModifiedTouchEvent(me, true);
                }
            } else {
                // Send an up event for the last pointer
                android.view.MotionEvent up = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_UP, mOldPointerX, mOldPointerY, me.getMetaState());
                result = onModifiedTouchEvent(up, true);
                up.recycle();
            }
        } else {
            if (pointerCount == 1) {
                result = onModifiedTouchEvent(me, false);
                mOldPointerX = me.getX();
                mOldPointerY = me.getY();
            } else {
                // Don't do anything when 2 pointers are down and moving.
                result = true;
            }
        }
        mOldPointerCount = pointerCount;
        return result;
    }

    private boolean onModifiedTouchEvent(android.view.MotionEvent me, boolean possiblePoly) {
        int touchX = ((int) (me.getX())) - mPaddingLeft;
        int touchY = ((int) (me.getY())) - mPaddingTop;
        if (touchY >= (-mVerticalCorrection))
            touchY += mVerticalCorrection;

        final int action = me.getAction();
        final long eventTime = me.getEventTime();
        int keyIndex = getKeyIndices(touchX, touchY, null);
        mPossiblePoly = possiblePoly;
        // Track the last few movements to look for spurious swipes.
        if (action == android.view.MotionEvent.ACTION_DOWN)
            mSwipeTracker.clear();

        mSwipeTracker.addMovement(me);
        // Ignore all motion events until a DOWN.
        if ((mAbortKey && (action != android.view.MotionEvent.ACTION_DOWN)) && (action != android.view.MotionEvent.ACTION_CANCEL)) {
            return true;
        }
        if (mGestureDetector.onTouchEvent(me)) {
            showPreview(android.inputmethodservice.KeyboardView.NOT_A_KEY);
            mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_REPEAT);
            mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_LONGPRESS);
            return true;
        }
        // Needs to be called after the gesture detector gets a turn, as it may have
        // displayed the mini keyboard
        if (mMiniKeyboardOnScreen && (action != android.view.MotionEvent.ACTION_CANCEL)) {
            return true;
        }
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
                mAbortKey = false;
                mStartX = touchX;
                mStartY = touchY;
                mLastCodeX = touchX;
                mLastCodeY = touchY;
                mLastKeyTime = 0;
                mCurrentKeyTime = 0;
                mLastKey = android.inputmethodservice.KeyboardView.NOT_A_KEY;
                mCurrentKey = keyIndex;
                mDownKey = keyIndex;
                mDownTime = me.getEventTime();
                mLastMoveTime = mDownTime;
                checkMultiTap(eventTime, keyIndex);
                mKeyboardActionListener.onPress(keyIndex != android.inputmethodservice.KeyboardView.NOT_A_KEY ? mKeys[keyIndex].codes[0] : 0);
                if ((mCurrentKey >= 0) && mKeys[mCurrentKey].repeatable) {
                    mRepeatKeyIndex = mCurrentKey;
                    android.os.Message msg = mHandler.obtainMessage(android.inputmethodservice.KeyboardView.MSG_REPEAT);
                    mHandler.sendMessageDelayed(msg, android.inputmethodservice.KeyboardView.REPEAT_START_DELAY);
                    repeatKey();
                    // Delivering the key could have caused an abort
                    if (mAbortKey) {
                        mRepeatKeyIndex = android.inputmethodservice.KeyboardView.NOT_A_KEY;
                        break;
                    }
                }
                if (mCurrentKey != android.inputmethodservice.KeyboardView.NOT_A_KEY) {
                    android.os.Message msg = mHandler.obtainMessage(android.inputmethodservice.KeyboardView.MSG_LONGPRESS, me);
                    mHandler.sendMessageDelayed(msg, android.inputmethodservice.KeyboardView.LONGPRESS_TIMEOUT);
                }
                showPreview(keyIndex);
                break;
            case android.view.MotionEvent.ACTION_MOVE :
                boolean continueLongPress = false;
                if (keyIndex != android.inputmethodservice.KeyboardView.NOT_A_KEY) {
                    if (mCurrentKey == android.inputmethodservice.KeyboardView.NOT_A_KEY) {
                        mCurrentKey = keyIndex;
                        mCurrentKeyTime = eventTime - mDownTime;
                    } else {
                        if (keyIndex == mCurrentKey) {
                            mCurrentKeyTime += eventTime - mLastMoveTime;
                            continueLongPress = true;
                        } else
                            if (mRepeatKeyIndex == android.inputmethodservice.KeyboardView.NOT_A_KEY) {
                                resetMultiTap();
                                mLastKey = mCurrentKey;
                                mLastCodeX = mLastX;
                                mLastCodeY = mLastY;
                                mLastKeyTime = (mCurrentKeyTime + eventTime) - mLastMoveTime;
                                mCurrentKey = keyIndex;
                                mCurrentKeyTime = 0;
                            }

                    }
                }
                if (!continueLongPress) {
                    // Cancel old longpress
                    mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_LONGPRESS);
                    // Start new longpress if key has changed
                    if (keyIndex != android.inputmethodservice.KeyboardView.NOT_A_KEY) {
                        android.os.Message msg = mHandler.obtainMessage(android.inputmethodservice.KeyboardView.MSG_LONGPRESS, me);
                        mHandler.sendMessageDelayed(msg, android.inputmethodservice.KeyboardView.LONGPRESS_TIMEOUT);
                    }
                }
                showPreview(mCurrentKey);
                mLastMoveTime = eventTime;
                break;
            case android.view.MotionEvent.ACTION_UP :
                removeMessages();
                if (keyIndex == mCurrentKey) {
                    mCurrentKeyTime += eventTime - mLastMoveTime;
                } else {
                    resetMultiTap();
                    mLastKey = mCurrentKey;
                    mLastKeyTime = (mCurrentKeyTime + eventTime) - mLastMoveTime;
                    mCurrentKey = keyIndex;
                    mCurrentKeyTime = 0;
                }
                if (((mCurrentKeyTime < mLastKeyTime) && (mCurrentKeyTime < android.inputmethodservice.KeyboardView.DEBOUNCE_TIME)) && (mLastKey != android.inputmethodservice.KeyboardView.NOT_A_KEY)) {
                    mCurrentKey = mLastKey;
                    touchX = mLastCodeX;
                    touchY = mLastCodeY;
                }
                showPreview(android.inputmethodservice.KeyboardView.NOT_A_KEY);
                java.util.Arrays.fill(mKeyIndices, android.inputmethodservice.KeyboardView.NOT_A_KEY);
                // If we're not on a repeating key (which sends on a DOWN event)
                if (((mRepeatKeyIndex == android.inputmethodservice.KeyboardView.NOT_A_KEY) && (!mMiniKeyboardOnScreen)) && (!mAbortKey)) {
                    detectAndSendKey(mCurrentKey, touchX, touchY, eventTime);
                }
                invalidateKey(keyIndex);
                mRepeatKeyIndex = android.inputmethodservice.KeyboardView.NOT_A_KEY;
                break;
            case android.view.MotionEvent.ACTION_CANCEL :
                removeMessages();
                dismissPopupKeyboard();
                mAbortKey = true;
                showPreview(android.inputmethodservice.KeyboardView.NOT_A_KEY);
                invalidateKey(mCurrentKey);
                break;
        }
        mLastX = touchX;
        mLastY = touchY;
        return true;
    }

    private boolean repeatKey() {
        android.inputmethodservice.Keyboard.Key key = mKeys[mRepeatKeyIndex];
        detectAndSendKey(mCurrentKey, key.x, key.y, mLastTapTime);
        return true;
    }

    protected void swipeRight() {
        mKeyboardActionListener.swipeRight();
    }

    protected void swipeLeft() {
        mKeyboardActionListener.swipeLeft();
    }

    protected void swipeUp() {
        mKeyboardActionListener.swipeUp();
    }

    protected void swipeDown() {
        mKeyboardActionListener.swipeDown();
    }

    public void closing() {
        if (mPreviewPopup.isShowing()) {
            mPreviewPopup.dismiss();
        }
        removeMessages();
        dismissPopupKeyboard();
        mBuffer = null;
        mCanvas = null;
        mMiniKeyboardCache.clear();
    }

    private void removeMessages() {
        if (mHandler != null) {
            mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_REPEAT);
            mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_LONGPRESS);
            mHandler.removeMessages(android.inputmethodservice.KeyboardView.MSG_SHOW_PREVIEW);
        }
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        closing();
    }

    private void dismissPopupKeyboard() {
        if (mPopupKeyboard.isShowing()) {
            mPopupKeyboard.dismiss();
            mMiniKeyboardOnScreen = false;
            invalidateAllKeys();
        }
    }

    public boolean handleBack() {
        if (mPopupKeyboard.isShowing()) {
            dismissPopupKeyboard();
            return true;
        }
        return false;
    }

    private void resetMultiTap() {
        mLastSentIndex = android.inputmethodservice.KeyboardView.NOT_A_KEY;
        mTapCount = 0;
        mLastTapTime = -1;
        mInMultiTap = false;
    }

    private void checkMultiTap(long eventTime, int keyIndex) {
        if (keyIndex == android.inputmethodservice.KeyboardView.NOT_A_KEY)
            return;

        android.inputmethodservice.Keyboard.Key key = mKeys[keyIndex];
        if (key.codes.length > 1) {
            mInMultiTap = true;
            if ((eventTime < (mLastTapTime + android.inputmethodservice.KeyboardView.MULTITAP_INTERVAL)) && (keyIndex == mLastSentIndex)) {
                mTapCount = (mTapCount + 1) % key.codes.length;
                return;
            } else {
                mTapCount = -1;
                return;
            }
        }
        if ((eventTime > (mLastTapTime + android.inputmethodservice.KeyboardView.MULTITAP_INTERVAL)) || (keyIndex != mLastSentIndex)) {
            resetMultiTap();
        }
    }

    private static class SwipeTracker {
        static final int NUM_PAST = 4;

        static final int LONGEST_PAST_TIME = 200;

        final float[] mPastX = new float[android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST];

        final float[] mPastY = new float[android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST];

        final long[] mPastTime = new long[android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST];

        float mYVelocity;

        float mXVelocity;

        public void clear() {
            mPastTime[0] = 0;
        }

        public void addMovement(android.view.MotionEvent ev) {
            long time = ev.getEventTime();
            final int N = ev.getHistorySize();
            for (int i = 0; i < N; i++) {
                addPoint(ev.getHistoricalX(i), ev.getHistoricalY(i), ev.getHistoricalEventTime(i));
            }
            addPoint(ev.getX(), ev.getY(), time);
        }

        private void addPoint(float x, float y, long time) {
            int drop = -1;
            int i;
            final long[] pastTime = mPastTime;
            for (i = 0; i < android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST; i++) {
                if (pastTime[i] == 0) {
                    break;
                } else
                    if (pastTime[i] < (time - android.inputmethodservice.KeyboardView.SwipeTracker.LONGEST_PAST_TIME)) {
                        drop = i;
                    }

            }
            if ((i == android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST) && (drop < 0)) {
                drop = 0;
            }
            if (drop == i)
                drop--;

            final float[] pastX = mPastX;
            final float[] pastY = mPastY;
            if (drop >= 0) {
                final int start = drop + 1;
                final int count = (android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST - drop) - 1;
                java.lang.System.arraycopy(pastX, start, pastX, 0, count);
                java.lang.System.arraycopy(pastY, start, pastY, 0, count);
                java.lang.System.arraycopy(pastTime, start, pastTime, 0, count);
                i -= drop + 1;
            }
            pastX[i] = x;
            pastY[i] = y;
            pastTime[i] = time;
            i++;
            if (i < android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST) {
                pastTime[i] = 0;
            }
        }

        public void computeCurrentVelocity(int units) {
            computeCurrentVelocity(units, java.lang.Float.MAX_VALUE);
        }

        public void computeCurrentVelocity(int units, float maxVelocity) {
            final float[] pastX = mPastX;
            final float[] pastY = mPastY;
            final long[] pastTime = mPastTime;
            final float oldestX = pastX[0];
            final float oldestY = pastY[0];
            final long oldestTime = pastTime[0];
            float accumX = 0;
            float accumY = 0;
            int N = 0;
            while (N < android.inputmethodservice.KeyboardView.SwipeTracker.NUM_PAST) {
                if (pastTime[N] == 0) {
                    break;
                }
                N++;
            } 
            for (int i = 1; i < N; i++) {
                final int dur = ((int) (pastTime[i] - oldestTime));
                if (dur == 0)
                    continue;

                float dist = pastX[i] - oldestX;
                float vel = (dist / dur) * units;// pixels/frame.

                if (accumX == 0)
                    accumX = vel;
                else
                    accumX = (accumX + vel) * 0.5F;

                dist = pastY[i] - oldestY;
                vel = (dist / dur) * units;// pixels/frame.

                if (accumY == 0)
                    accumY = vel;
                else
                    accumY = (accumY + vel) * 0.5F;

            }
            mXVelocity = (accumX < 0.0F) ? java.lang.Math.max(accumX, -maxVelocity) : java.lang.Math.min(accumX, maxVelocity);
            mYVelocity = (accumY < 0.0F) ? java.lang.Math.max(accumY, -maxVelocity) : java.lang.Math.min(accumY, maxVelocity);
        }

        public float getXVelocity() {
            return mXVelocity;
        }

        public float getYVelocity() {
            return mYVelocity;
        }
    }
}

