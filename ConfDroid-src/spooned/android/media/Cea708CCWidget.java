package android.media;


/**
 * Widget capable of rendering CEA-708 closed captions.
 *
 * @unknown 
 */
class Cea708CCWidget extends android.media.ClosedCaptionWidget implements android.media.Cea708CCParser.DisplayListener {
    private final android.media.Cea708CCWidget.CCHandler mCCHandler;

    public Cea708CCWidget(android.content.Context context) {
        this(context, null);
    }

    public Cea708CCWidget(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cea708CCWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public Cea708CCWidget(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mCCHandler = new android.media.Cea708CCWidget.CCHandler(((android.media.Cea708CCWidget.CCLayout) (mClosedCaptionLayout)));
    }

    @java.lang.Override
    public android.media.ClosedCaptionWidget.ClosedCaptionLayout createCaptionLayout(android.content.Context context) {
        return new android.media.Cea708CCWidget.CCLayout(context);
    }

    @java.lang.Override
    public void emitEvent(android.media.Cea708CCParser.CaptionEvent event) {
        mCCHandler.processCaptionEvent(event);
        setSize(getWidth(), getHeight());
        if (mListener != null) {
            mListener.onChanged(this);
        }
    }

    @java.lang.Override
    public void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        ((android.view.ViewGroup) (mClosedCaptionLayout)).draw(canvas);
    }

    /**
     *
     *
     * @unknown A layout that scales its children using the given percentage value.
     */
    static class ScaledLayout extends android.view.ViewGroup {
        private static final java.lang.String TAG = "ScaledLayout";

        private static final boolean DEBUG = false;

        private static final java.util.Comparator<android.graphics.Rect> mRectTopLeftSorter = new java.util.Comparator<android.graphics.Rect>() {
            @java.lang.Override
            public int compare(android.graphics.Rect lhs, android.graphics.Rect rhs) {
                if (lhs.top != rhs.top) {
                    return lhs.top - rhs.top;
                } else {
                    return lhs.left - rhs.left;
                }
            }
        };

        private android.graphics.Rect[] mRectArray;

        public ScaledLayout(android.content.Context context) {
            super(context);
        }

        /**
         *
         *
         * @unknown ScaledLayoutParams stores the four scale factors.
        <br>
        Vertical coordinate system:   (scaleStartRow * 100) % ~ (scaleEndRow * 100) %
        Horizontal coordinate system: (scaleStartCol * 100) % ~ (scaleEndCol * 100) %
        <br>
        In XML, for example,
        <pre>
        {@code <View
        app:layout_scaleStartRow="0.1"
        app:layout_scaleEndRow="0.5"
        app:layout_scaleStartCol="0.4"
        app:layout_scaleEndCol="1" />}
        </pre>
         */
        static class ScaledLayoutParams extends android.view.ViewGroup.LayoutParams {
            public static final float SCALE_UNSPECIFIED = -1;

            public float scaleStartRow;

            public float scaleEndRow;

            public float scaleStartCol;

            public float scaleEndCol;

            public ScaledLayoutParams(float scaleStartRow, float scaleEndRow, float scaleStartCol, float scaleEndCol) {
                super(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                this.scaleStartRow = scaleStartRow;
                this.scaleEndRow = scaleEndRow;
                this.scaleStartCol = scaleStartCol;
                this.scaleEndCol = scaleEndCol;
            }

            public ScaledLayoutParams(android.content.Context context, android.util.AttributeSet attrs) {
                super(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            }
        }

        @java.lang.Override
        public android.view.ViewGroup.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
            return new android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams(getContext(), attrs);
        }

        @java.lang.Override
        protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
            return p instanceof android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams;
        }

        @java.lang.Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int widthSpecSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
            int heightSpecSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
            int width = (widthSpecSize - getPaddingLeft()) - getPaddingRight();
            int height = (heightSpecSize - getPaddingTop()) - getPaddingBottom();
            if (android.media.Cea708CCWidget.ScaledLayout.DEBUG) {
                android.util.Log.d(android.media.Cea708CCWidget.ScaledLayout.TAG, java.lang.String.format("onMeasure width: %d, height: %d", width, height));
            }
            int count = getChildCount();
            mRectArray = new android.graphics.Rect[count];
            for (int i = 0; i < count; ++i) {
                android.view.View child = getChildAt(i);
                android.view.ViewGroup.LayoutParams params = child.getLayoutParams();
                float scaleStartRow;
                float scaleEndRow;
                float scaleStartCol;
                float scaleEndCol;
                if (!(params instanceof android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams)) {
                    throw new java.lang.RuntimeException("A child of ScaledLayout cannot have the UNSPECIFIED scale factors");
                }
                scaleStartRow = ((android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams) (params)).scaleStartRow;
                scaleEndRow = ((android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams) (params)).scaleEndRow;
                scaleStartCol = ((android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams) (params)).scaleStartCol;
                scaleEndCol = ((android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams) (params)).scaleEndCol;
                if ((scaleStartRow < 0) || (scaleStartRow > 1)) {
                    throw new java.lang.RuntimeException("A child of ScaledLayout should have a range of " + "scaleStartRow between 0 and 1");
                }
                if ((scaleEndRow < scaleStartRow) || (scaleStartRow > 1)) {
                    throw new java.lang.RuntimeException("A child of ScaledLayout should have a range of " + "scaleEndRow between scaleStartRow and 1");
                }
                if ((scaleEndCol < 0) || (scaleEndCol > 1)) {
                    throw new java.lang.RuntimeException("A child of ScaledLayout should have a range of " + "scaleStartCol between 0 and 1");
                }
                if ((scaleEndCol < scaleStartCol) || (scaleEndCol > 1)) {
                    throw new java.lang.RuntimeException("A child of ScaledLayout should have a range of " + "scaleEndCol between scaleStartCol and 1");
                }
                if (android.media.Cea708CCWidget.ScaledLayout.DEBUG) {
                    android.util.Log.d(android.media.Cea708CCWidget.ScaledLayout.TAG, java.lang.String.format("onMeasure child scaleStartRow: %f scaleEndRow: %f " + "scaleStartCol: %f scaleEndCol: %f", scaleStartRow, scaleEndRow, scaleStartCol, scaleEndCol));
                }
                mRectArray[i] = new android.graphics.Rect(((int) (scaleStartCol * width)), ((int) (scaleStartRow * height)), ((int) (scaleEndCol * width)), ((int) (scaleEndRow * height)));
                int childWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec(((int) (width * (scaleEndCol - scaleStartCol))), android.view.View.MeasureSpec.EXACTLY);
                int childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
                child.measure(childWidthSpec, childHeightSpec);
                // If the height of the measured child view is bigger than the height of the
                // calculated region by the given ScaleLayoutParams, the height of the region should
                // be increased to fit the size of the child view.
                if (child.getMeasuredHeight() > mRectArray[i].height()) {
                    int overflowedHeight = child.getMeasuredHeight() - mRectArray[i].height();
                    overflowedHeight = (overflowedHeight + 1) / 2;
                    mRectArray[i].bottom += overflowedHeight;
                    mRectArray[i].top -= overflowedHeight;
                    if (mRectArray[i].top < 0) {
                        mRectArray[i].bottom -= mRectArray[i].top;
                        mRectArray[i].top = 0;
                    }
                    if (mRectArray[i].bottom > height) {
                        mRectArray[i].top -= mRectArray[i].bottom - height;
                        mRectArray[i].bottom = height;
                    }
                }
                childHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec(((int) (height * (scaleEndRow - scaleStartRow))), android.view.View.MeasureSpec.EXACTLY);
                child.measure(childWidthSpec, childHeightSpec);
            }
            // Avoid overlapping rectangles.
            // Step 1. Sort rectangles by position (top-left).
            int visibleRectCount = 0;
            int[] visibleRectGroup = new int[count];
            android.graphics.Rect[] visibleRectArray = new android.graphics.Rect[count];
            for (int i = 0; i < count; ++i) {
                if (getChildAt(i).getVisibility() == android.view.View.VISIBLE) {
                    visibleRectGroup[visibleRectCount] = visibleRectCount;
                    visibleRectArray[visibleRectCount] = mRectArray[i];
                    ++visibleRectCount;
                }
            }
            java.util.Arrays.sort(visibleRectArray, 0, visibleRectCount, android.media.Cea708CCWidget.ScaledLayout.mRectTopLeftSorter);
            // Step 2. Move down if there are overlapping rectangles.
            for (int i = 0; i < (visibleRectCount - 1); ++i) {
                for (int j = i + 1; j < visibleRectCount; ++j) {
                    if (android.graphics.Rect.intersects(visibleRectArray[i], visibleRectArray[j])) {
                        visibleRectGroup[j] = visibleRectGroup[i];
                        visibleRectArray[j].set(visibleRectArray[j].left, visibleRectArray[i].bottom, visibleRectArray[j].right, visibleRectArray[i].bottom + visibleRectArray[j].height());
                    }
                }
            }
            // Step 3. Move up if there is any overflowed rectangle.
            for (int i = visibleRectCount - 1; i >= 0; --i) {
                if (visibleRectArray[i].bottom > height) {
                    int overflowedHeight = visibleRectArray[i].bottom - height;
                    for (int j = 0; j <= i; ++j) {
                        if (visibleRectGroup[i] == visibleRectGroup[j]) {
                            visibleRectArray[j].set(visibleRectArray[j].left, visibleRectArray[j].top - overflowedHeight, visibleRectArray[j].right, visibleRectArray[j].bottom - overflowedHeight);
                        }
                    }
                }
            }
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }

        @java.lang.Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int count = getChildCount();
            for (int i = 0; i < count; ++i) {
                android.view.View child = getChildAt(i);
                if (child.getVisibility() != android.view.View.GONE) {
                    int childLeft = paddingLeft + mRectArray[i].left;
                    int childTop = paddingTop + mRectArray[i].top;
                    int childBottom = paddingLeft + mRectArray[i].bottom;
                    int childRight = paddingTop + mRectArray[i].right;
                    if (android.media.Cea708CCWidget.ScaledLayout.DEBUG) {
                        android.util.Log.d(android.media.Cea708CCWidget.ScaledLayout.TAG, java.lang.String.format("child layout bottom: %d left: %d right: %d top: %d", childBottom, childLeft, childRight, childTop));
                    }
                    child.layout(childLeft, childTop, childRight, childBottom);
                }
            }
        }

        @java.lang.Override
        public void dispatchDraw(android.graphics.Canvas canvas) {
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int count = getChildCount();
            for (int i = 0; i < count; ++i) {
                android.view.View child = getChildAt(i);
                if (child.getVisibility() != android.view.View.GONE) {
                    if (i >= mRectArray.length) {
                        break;
                    }
                    int childLeft = paddingLeft + mRectArray[i].left;
                    int childTop = paddingTop + mRectArray[i].top;
                    final int saveCount = canvas.save();
                    canvas.translate(childLeft, childTop);
                    child.draw(canvas);
                    canvas.restoreToCount(saveCount);
                }
            }
        }
    }

    /**
     *
     *
     * @unknown Layout containing the safe title area that helps the closed captions look more prominent.

    <p>This is required by CEA-708B.
     */
    static class CCLayout extends android.media.Cea708CCWidget.ScaledLayout implements android.media.ClosedCaptionWidget.ClosedCaptionLayout {
        private static final float SAFE_TITLE_AREA_SCALE_START_X = 0.1F;

        private static final float SAFE_TITLE_AREA_SCALE_END_X = 0.9F;

        private static final float SAFE_TITLE_AREA_SCALE_START_Y = 0.1F;

        private static final float SAFE_TITLE_AREA_SCALE_END_Y = 0.9F;

        private final android.media.Cea708CCWidget.ScaledLayout mSafeTitleAreaLayout;

        public CCLayout(android.content.Context context) {
            super(context);
            mSafeTitleAreaLayout = new android.media.Cea708CCWidget.ScaledLayout(context);
            addView(mSafeTitleAreaLayout, new android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams(android.media.Cea708CCWidget.CCLayout.SAFE_TITLE_AREA_SCALE_START_X, android.media.Cea708CCWidget.CCLayout.SAFE_TITLE_AREA_SCALE_END_X, android.media.Cea708CCWidget.CCLayout.SAFE_TITLE_AREA_SCALE_START_Y, android.media.Cea708CCWidget.CCLayout.SAFE_TITLE_AREA_SCALE_END_Y));
        }

        public void addOrUpdateViewToSafeTitleArea(android.media.Cea708CCWidget.CCWindowLayout captionWindowLayout, android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams scaledLayoutParams) {
            int index = mSafeTitleAreaLayout.indexOfChild(captionWindowLayout);
            if (index < 0) {
                mSafeTitleAreaLayout.addView(captionWindowLayout, scaledLayoutParams);
                return;
            }
            mSafeTitleAreaLayout.updateViewLayout(captionWindowLayout, scaledLayoutParams);
        }

        public void removeViewFromSafeTitleArea(android.media.Cea708CCWidget.CCWindowLayout captionWindowLayout) {
            mSafeTitleAreaLayout.removeView(captionWindowLayout);
        }

        public void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle style) {
            final int count = mSafeTitleAreaLayout.getChildCount();
            for (int i = 0; i < count; ++i) {
                final android.media.Cea708CCWidget.CCWindowLayout windowLayout = ((android.media.Cea708CCWidget.CCWindowLayout) (mSafeTitleAreaLayout.getChildAt(i)));
                windowLayout.setCaptionStyle(style);
            }
        }

        public void setFontScale(float fontScale) {
            final int count = mSafeTitleAreaLayout.getChildCount();
            for (int i = 0; i < count; ++i) {
                final android.media.Cea708CCWidget.CCWindowLayout windowLayout = ((android.media.Cea708CCWidget.CCWindowLayout) (mSafeTitleAreaLayout.getChildAt(i)));
                windowLayout.setFontScale(fontScale);
            }
        }
    }

    /**
     *
     *
     * @unknown Renders the selected CC track.
     */
    static class CCHandler implements android.os.Handler.Callback {
        // TODO: Remaining works
        // CaptionTrackRenderer does not support the full spec of CEA-708. The remaining works are
        // described in the follows.
        // C0 Table: Backspace, FF, and HCR are not supported. The rule for P16 is not standardized
        // but it is handled as EUC-KR charset for Korea broadcasting.
        // C1 Table: All the styles of windows and pens except underline, italic, pen size, and pen
        // offset specified in CEA-708 are ignored and this follows system wide CC
        // preferences for look and feel. SetPenLocation is not implemented.
        // G2 Table: TSP, NBTSP and BLK are not supported.
        // Text/commands: Word wrapping, fonts, row and column locking are not supported.
        private static final java.lang.String TAG = "CCHandler";

        private static final boolean DEBUG = false;

        private static final int TENTHS_OF_SECOND_IN_MILLIS = 100;

        // According to CEA-708B, there can exist up to 8 caption windows.
        private static final int CAPTION_WINDOWS_MAX = 8;

        private static final int CAPTION_ALL_WINDOWS_BITMAP = 255;

        private static final int MSG_DELAY_CANCEL = 1;

        private static final int MSG_CAPTION_CLEAR = 2;

        private static final long CAPTION_CLEAR_INTERVAL_MS = 60000;

        private final android.media.Cea708CCWidget.CCLayout mCCLayout;

        private boolean mIsDelayed = false;

        private android.media.Cea708CCWidget.CCWindowLayout mCurrentWindowLayout;

        private final android.media.Cea708CCWidget.CCWindowLayout[] mCaptionWindowLayouts = new android.media.Cea708CCWidget.CCWindowLayout[android.media.Cea708CCWidget.CCHandler.CAPTION_WINDOWS_MAX];

        private final java.util.ArrayList<android.media.Cea708CCParser.CaptionEvent> mPendingCaptionEvents = new java.util.ArrayList<>();

        private final android.os.Handler mHandler;

        public CCHandler(android.media.Cea708CCWidget.CCLayout ccLayout) {
            mCCLayout = ccLayout;
            mHandler = new android.os.Handler(this);
        }

        @java.lang.Override
        public boolean handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.media.Cea708CCWidget.CCHandler.MSG_DELAY_CANCEL :
                    delayCancel();
                    return true;
                case android.media.Cea708CCWidget.CCHandler.MSG_CAPTION_CLEAR :
                    clearWindows(android.media.Cea708CCWidget.CCHandler.CAPTION_ALL_WINDOWS_BITMAP);
                    return true;
            }
            return false;
        }

        public void processCaptionEvent(android.media.Cea708CCParser.CaptionEvent event) {
            if (mIsDelayed) {
                mPendingCaptionEvents.add(event);
                return;
            }
            switch (event.type) {
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_BUFFER :
                    sendBufferToCurrentWindow(((java.lang.String) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_CONTROL :
                    sendControlToCurrentWindow(((char) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_CWX :
                    setCurrentWindowLayout(((int) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_CLW :
                    clearWindows(((int) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_DSW :
                    displayWindows(((int) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_HDW :
                    hideWindows(((int) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_TGW :
                    toggleWindows(((int) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_DLW :
                    deleteWindows(((int) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_DLY :
                    delay(((int) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_DLC :
                    delayCancel();
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_RST :
                    reset();
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_SPA :
                    setPenAttr(((android.media.Cea708CCParser.CaptionPenAttr) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_SPC :
                    setPenColor(((android.media.Cea708CCParser.CaptionPenColor) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_SPL :
                    setPenLocation(((android.media.Cea708CCParser.CaptionPenLocation) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_SWA :
                    setWindowAttr(((android.media.Cea708CCParser.CaptionWindowAttr) (event.obj)));
                    break;
                case android.media.Cea708CCParser.CAPTION_EMIT_TYPE_COMMAND_DFX :
                    defineWindow(((android.media.Cea708CCParser.CaptionWindow) (event.obj)));
                    break;
            }
        }

        // The window related caption commands
        private void setCurrentWindowLayout(int windowId) {
            if ((windowId < 0) || (windowId >= mCaptionWindowLayouts.length)) {
                return;
            }
            android.media.Cea708CCWidget.CCWindowLayout windowLayout = mCaptionWindowLayouts[windowId];
            if (windowLayout == null) {
                return;
            }
            if (android.media.Cea708CCWidget.CCHandler.DEBUG) {
                android.util.Log.d(android.media.Cea708CCWidget.CCHandler.TAG, "setCurrentWindowLayout to " + windowId);
            }
            mCurrentWindowLayout = windowLayout;
        }

        // Each bit of windowBitmap indicates a window.
        // If a bit is set, the window id is the same as the number of the trailing zeros of the
        // bit.
        private java.util.ArrayList<android.media.Cea708CCWidget.CCWindowLayout> getWindowsFromBitmap(int windowBitmap) {
            java.util.ArrayList<android.media.Cea708CCWidget.CCWindowLayout> windows = new java.util.ArrayList<>();
            for (int i = 0; i < android.media.Cea708CCWidget.CCHandler.CAPTION_WINDOWS_MAX; ++i) {
                if ((windowBitmap & (1 << i)) != 0) {
                    android.media.Cea708CCWidget.CCWindowLayout windowLayout = mCaptionWindowLayouts[i];
                    if (windowLayout != null) {
                        windows.add(windowLayout);
                    }
                }
            }
            return windows;
        }

        private void clearWindows(int windowBitmap) {
            if (windowBitmap == 0) {
                return;
            }
            for (android.media.Cea708CCWidget.CCWindowLayout windowLayout : getWindowsFromBitmap(windowBitmap)) {
                windowLayout.clear();
            }
        }

        private void displayWindows(int windowBitmap) {
            if (windowBitmap == 0) {
                return;
            }
            for (android.media.Cea708CCWidget.CCWindowLayout windowLayout : getWindowsFromBitmap(windowBitmap)) {
                windowLayout.show();
            }
        }

        private void hideWindows(int windowBitmap) {
            if (windowBitmap == 0) {
                return;
            }
            for (android.media.Cea708CCWidget.CCWindowLayout windowLayout : getWindowsFromBitmap(windowBitmap)) {
                windowLayout.hide();
            }
        }

        private void toggleWindows(int windowBitmap) {
            if (windowBitmap == 0) {
                return;
            }
            for (android.media.Cea708CCWidget.CCWindowLayout windowLayout : getWindowsFromBitmap(windowBitmap)) {
                if (windowLayout.isShown()) {
                    windowLayout.hide();
                } else {
                    windowLayout.show();
                }
            }
        }

        private void deleteWindows(int windowBitmap) {
            if (windowBitmap == 0) {
                return;
            }
            for (android.media.Cea708CCWidget.CCWindowLayout windowLayout : getWindowsFromBitmap(windowBitmap)) {
                windowLayout.removeFromCaptionView();
                mCaptionWindowLayouts[windowLayout.getCaptionWindowId()] = null;
            }
        }

        public void reset() {
            mCurrentWindowLayout = null;
            mIsDelayed = false;
            mPendingCaptionEvents.clear();
            for (int i = 0; i < android.media.Cea708CCWidget.CCHandler.CAPTION_WINDOWS_MAX; ++i) {
                if (mCaptionWindowLayouts[i] != null) {
                    mCaptionWindowLayouts[i].removeFromCaptionView();
                }
                mCaptionWindowLayouts[i] = null;
            }
            mCCLayout.setVisibility(android.view.View.INVISIBLE);
            mHandler.removeMessages(android.media.Cea708CCWidget.CCHandler.MSG_CAPTION_CLEAR);
        }

        private void setWindowAttr(android.media.Cea708CCParser.CaptionWindowAttr windowAttr) {
            if (mCurrentWindowLayout != null) {
                mCurrentWindowLayout.setWindowAttr(windowAttr);
            }
        }

        private void defineWindow(android.media.Cea708CCParser.CaptionWindow window) {
            if (window == null) {
                return;
            }
            int windowId = window.id;
            if ((windowId < 0) || (windowId >= mCaptionWindowLayouts.length)) {
                return;
            }
            android.media.Cea708CCWidget.CCWindowLayout windowLayout = mCaptionWindowLayouts[windowId];
            if (windowLayout == null) {
                windowLayout = new android.media.Cea708CCWidget.CCWindowLayout(mCCLayout.getContext());
            }
            windowLayout.initWindow(mCCLayout, window);
            mCurrentWindowLayout = mCaptionWindowLayouts[windowId] = windowLayout;
        }

        // The job related caption commands
        private void delay(int tenthsOfSeconds) {
            if ((tenthsOfSeconds < 0) || (tenthsOfSeconds > 255)) {
                return;
            }
            mIsDelayed = true;
            mHandler.sendMessageDelayed(mHandler.obtainMessage(android.media.Cea708CCWidget.CCHandler.MSG_DELAY_CANCEL), tenthsOfSeconds * android.media.Cea708CCWidget.CCHandler.TENTHS_OF_SECOND_IN_MILLIS);
        }

        private void delayCancel() {
            mIsDelayed = false;
            processPendingBuffer();
        }

        private void processPendingBuffer() {
            for (android.media.Cea708CCParser.CaptionEvent event : mPendingCaptionEvents) {
                processCaptionEvent(event);
            }
            mPendingCaptionEvents.clear();
        }

        // The implicit write caption commands
        private void sendControlToCurrentWindow(char control) {
            if (mCurrentWindowLayout != null) {
                mCurrentWindowLayout.sendControl(control);
            }
        }

        private void sendBufferToCurrentWindow(java.lang.String buffer) {
            if (mCurrentWindowLayout != null) {
                mCurrentWindowLayout.sendBuffer(buffer);
                mHandler.removeMessages(android.media.Cea708CCWidget.CCHandler.MSG_CAPTION_CLEAR);
                mHandler.sendMessageDelayed(mHandler.obtainMessage(android.media.Cea708CCWidget.CCHandler.MSG_CAPTION_CLEAR), android.media.Cea708CCWidget.CCHandler.CAPTION_CLEAR_INTERVAL_MS);
            }
        }

        // The pen related caption commands
        private void setPenAttr(android.media.Cea708CCParser.CaptionPenAttr attr) {
            if (mCurrentWindowLayout != null) {
                mCurrentWindowLayout.setPenAttr(attr);
            }
        }

        private void setPenColor(android.media.Cea708CCParser.CaptionPenColor color) {
            if (mCurrentWindowLayout != null) {
                mCurrentWindowLayout.setPenColor(color);
            }
        }

        private void setPenLocation(android.media.Cea708CCParser.CaptionPenLocation location) {
            if (mCurrentWindowLayout != null) {
                mCurrentWindowLayout.setPenLocation(location.row, location.column);
            }
        }
    }

    /**
     *
     *
     * @unknown Layout which renders a caption window of CEA-708B. It contains a {@link TextView} that takes
    care of displaying the actual CC text.
     */
    static class CCWindowLayout extends android.widget.RelativeLayout implements android.view.View.OnLayoutChangeListener {
        private static final java.lang.String TAG = "CCWindowLayout";

        private static final float PROPORTION_PEN_SIZE_SMALL = 0.75F;

        private static final float PROPORTION_PEN_SIZE_LARGE = 1.25F;

        // The following values indicates the maximum cell number of a window.
        private static final int ANCHOR_RELATIVE_POSITIONING_MAX = 99;

        private static final int ANCHOR_VERTICAL_MAX = 74;

        private static final int ANCHOR_HORIZONTAL_16_9_MAX = 209;

        private static final int MAX_COLUMN_COUNT_16_9 = 42;

        // The following values indicates a gravity of a window.
        private static final int ANCHOR_MODE_DIVIDER = 3;

        private static final int ANCHOR_HORIZONTAL_MODE_LEFT = 0;

        private static final int ANCHOR_HORIZONTAL_MODE_CENTER = 1;

        private static final int ANCHOR_HORIZONTAL_MODE_RIGHT = 2;

        private static final int ANCHOR_VERTICAL_MODE_TOP = 0;

        private static final int ANCHOR_VERTICAL_MODE_CENTER = 1;

        private static final int ANCHOR_VERTICAL_MODE_BOTTOM = 2;

        private android.media.Cea708CCWidget.CCLayout mCCLayout;

        private android.media.Cea708CCWidget.CCView mCCView;

        private android.view.accessibility.CaptioningManager.CaptionStyle mCaptionStyle;

        private int mRowLimit = 0;

        private final android.text.SpannableStringBuilder mBuilder = new android.text.SpannableStringBuilder();

        private final java.util.List<android.text.style.CharacterStyle> mCharacterStyles = new java.util.ArrayList<>();

        private int mCaptionWindowId;

        private int mRow = -1;

        private float mFontScale;

        private float mTextSize;

        private java.lang.String mWidestChar;

        private int mLastCaptionLayoutWidth;

        private int mLastCaptionLayoutHeight;

        public CCWindowLayout(android.content.Context context) {
            this(context, null);
        }

        public CCWindowLayout(android.content.Context context, android.util.AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CCWindowLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
            this(context, attrs, defStyleAttr, 0);
        }

        public CCWindowLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
            // Add a subtitle view to the layout.
            mCCView = new android.media.Cea708CCWidget.CCView(context);
            android.widget.RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(android.view.ViewGroup.LayoutParams.WRAP_CONTENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            addView(mCCView, params);
            // Set the system wide CC preferences to the subtitle view.
            android.view.accessibility.CaptioningManager captioningManager = ((android.view.accessibility.CaptioningManager) (context.getSystemService(android.content.Context.CAPTIONING_SERVICE)));
            mFontScale = captioningManager.getFontScale();
            setCaptionStyle(captioningManager.getUserStyle());
            setText("");
            updateWidestChar();
        }

        public void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle style) {
            mCaptionStyle = style;
            mCCView.setCaptionStyle(style);
        }

        public void setFontScale(float fontScale) {
            mFontScale = fontScale;
            updateTextSize();
        }

        public int getCaptionWindowId() {
            return mCaptionWindowId;
        }

        public void setCaptionWindowId(int captionWindowId) {
            mCaptionWindowId = captionWindowId;
        }

        public void clear() {
            clearText();
            hide();
        }

        public void show() {
            setVisibility(android.view.View.VISIBLE);
            requestLayout();
        }

        public void hide() {
            setVisibility(android.view.View.INVISIBLE);
            requestLayout();
        }

        public void setPenAttr(android.media.Cea708CCParser.CaptionPenAttr penAttr) {
            mCharacterStyles.clear();
            if (penAttr.italic) {
                mCharacterStyles.add(new android.text.style.StyleSpan(android.graphics.Typeface.ITALIC));
            }
            if (penAttr.underline) {
                mCharacterStyles.add(new android.text.style.UnderlineSpan());
            }
            switch (penAttr.penSize) {
                case android.media.Cea708CCParser.CaptionPenAttr.PEN_SIZE_SMALL :
                    mCharacterStyles.add(new android.text.style.RelativeSizeSpan(android.media.Cea708CCWidget.CCWindowLayout.PROPORTION_PEN_SIZE_SMALL));
                    break;
                case android.media.Cea708CCParser.CaptionPenAttr.PEN_SIZE_LARGE :
                    mCharacterStyles.add(new android.text.style.RelativeSizeSpan(android.media.Cea708CCWidget.CCWindowLayout.PROPORTION_PEN_SIZE_LARGE));
                    break;
            }
            switch (penAttr.penOffset) {
                case android.media.Cea708CCParser.CaptionPenAttr.OFFSET_SUBSCRIPT :
                    mCharacterStyles.add(new android.text.style.SubscriptSpan());
                    break;
                case android.media.Cea708CCParser.CaptionPenAttr.OFFSET_SUPERSCRIPT :
                    mCharacterStyles.add(new android.text.style.SuperscriptSpan());
                    break;
            }
        }

        public void setPenColor(android.media.Cea708CCParser.CaptionPenColor penColor) {
            // TODO: apply pen colors or skip this and use the style of system wide CC style as is.
        }

        public void setPenLocation(int row, int column) {
            // TODO: change the location of pen based on row and column both.
            if (mRow >= 0) {
                for (int r = mRow; r < row; ++r) {
                    appendText("\n");
                }
            }
            mRow = row;
        }

        public void setWindowAttr(android.media.Cea708CCParser.CaptionWindowAttr windowAttr) {
            // TODO: apply window attrs or skip this and use the style of system wide CC style as
            // is.
        }

        public void sendBuffer(java.lang.String buffer) {
            appendText(buffer);
        }

        public void sendControl(char control) {
            // TODO: there are a bunch of ASCII-style control codes.
        }

        /**
         * This method places the window on a given CaptionLayout along with the anchor of the
         * window.
         * <p>
         * According to CEA-708B, the anchor id indicates the gravity of the window as the follows.
         * For example, A value 7 of a anchor id says that a window is align with its parent bottom
         * and is located at the center horizontally of its parent.
         * </p>
         * <h4>Anchor id and the gravity of a window</h4>
         * <table>
         *     <tr>
         *         <th>GRAVITY</th>
         *         <th>LEFT</th>
         *         <th>CENTER_HORIZONTAL</th>
         *         <th>RIGHT</th>
         *     </tr>
         *     <tr>
         *         <th>TOP</th>
         *         <td>0</td>
         *         <td>1</td>
         *         <td>2</td>
         *     </tr>
         *     <tr>
         *         <th>CENTER_VERTICAL</th>
         *         <td>3</td>
         *         <td>4</td>
         *         <td>5</td>
         *     </tr>
         *     <tr>
         *         <th>BOTTOM</th>
         *         <td>6</td>
         *         <td>7</td>
         *         <td>8</td>
         *     </tr>
         * </table>
         * <p>
         * In order to handle the gravity of a window, there are two steps. First, set the size of
         * the window. Since the window will be positioned at ScaledLayout, the size factors are
         * determined in a ratio. Second, set the gravity of the window. CaptionWindowLayout is
         * inherited from RelativeLayout. Hence, we could set the gravity of its child view,
         * SubtitleView.
         * </p>
         * <p>
         * The gravity of the window is also related to its size. When it should be pushed to a one
         * of the end of the window, like LEFT, RIGHT, TOP or BOTTOM, the anchor point should be a
         * boundary of the window. When it should be pushed in the horizontal/vertical center of its
         * container, the horizontal/vertical center point of the window should be the same as the
         * anchor point.
         * </p>
         *
         * @param ccLayout
         * 		a given CaptionLayout, which contains a safe title area.
         * @param captionWindow
         * 		a given CaptionWindow, which stores the construction info of the
         * 		window.
         */
        public void initWindow(android.media.Cea708CCWidget.CCLayout ccLayout, android.media.Cea708CCParser.CaptionWindow captionWindow) {
            if (mCCLayout != ccLayout) {
                if (mCCLayout != null) {
                    mCCLayout.removeOnLayoutChangeListener(this);
                }
                mCCLayout = ccLayout;
                mCCLayout.addOnLayoutChangeListener(this);
                updateWidestChar();
            }
            // Both anchor vertical and horizontal indicates the position cell number of the window.
            float scaleRow = ((float) (captionWindow.anchorVertical)) / (captionWindow.relativePositioning ? android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_RELATIVE_POSITIONING_MAX : android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_VERTICAL_MAX);
            // Assumes it has a wide aspect ratio track.
            float scaleCol = ((float) (captionWindow.anchorHorizontal)) / (captionWindow.relativePositioning ? android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_RELATIVE_POSITIONING_MAX : android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_HORIZONTAL_16_9_MAX);
            // The range of scaleRow/Col need to be verified to be in [0, 1].
            // Otherwise a RuntimeException will be raised in ScaledLayout.
            if ((scaleRow < 0) || (scaleRow > 1)) {
                android.util.Log.i(android.media.Cea708CCWidget.CCWindowLayout.TAG, ("The vertical position of the anchor point should be at the range of 0 " + "and 1 but ") + scaleRow);
                scaleRow = java.lang.Math.max(0, java.lang.Math.min(scaleRow, 1));
            }
            if ((scaleCol < 0) || (scaleCol > 1)) {
                android.util.Log.i(android.media.Cea708CCWidget.CCWindowLayout.TAG, ("The horizontal position of the anchor point should be at the range of 0" + " and 1 but ") + scaleCol);
                scaleCol = java.lang.Math.max(0, java.lang.Math.min(scaleCol, 1));
            }
            int gravity = android.view.Gravity.CENTER;
            int horizontalMode = captionWindow.anchorId % android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_MODE_DIVIDER;
            int verticalMode = captionWindow.anchorId / android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_MODE_DIVIDER;
            float scaleStartRow = 0;
            float scaleEndRow = 1;
            float scaleStartCol = 0;
            float scaleEndCol = 1;
            switch (horizontalMode) {
                case android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_HORIZONTAL_MODE_LEFT :
                    gravity = android.view.Gravity.LEFT;
                    mCCView.setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL);
                    scaleStartCol = scaleCol;
                    break;
                case android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_HORIZONTAL_MODE_CENTER :
                    float gap = java.lang.Math.min(1 - scaleCol, scaleCol);
                    // Since all TV sets use left text alignment instead of center text alignment
                    // for this case, we follow the industry convention if possible.
                    int columnCount = captionWindow.columnCount + 1;
                    columnCount = java.lang.Math.min(getScreenColumnCount(), columnCount);
                    java.lang.StringBuilder widestTextBuilder = new java.lang.StringBuilder();
                    for (int i = 0; i < columnCount; ++i) {
                        widestTextBuilder.append(mWidestChar);
                    }
                    android.graphics.Paint paint = new android.graphics.Paint();
                    paint.setTypeface(mCaptionStyle.getTypeface());
                    paint.setTextSize(mTextSize);
                    float maxWindowWidth = paint.measureText(widestTextBuilder.toString());
                    float halfMaxWidthScale = (mCCLayout.getWidth() > 0) ? (maxWindowWidth / 2.0F) / (mCCLayout.getWidth() * 0.8F) : 0.0F;
                    if ((halfMaxWidthScale > 0.0F) && (halfMaxWidthScale < scaleCol)) {
                        // Calculate the expected max window size based on the column count of the
                        // caption window multiplied by average alphabets char width, then align the
                        // left side of the window with the left side of the expected max window.
                        gravity = android.view.Gravity.LEFT;
                        mCCView.setAlignment(android.text.Layout.Alignment.ALIGN_NORMAL);
                        scaleStartCol = scaleCol - halfMaxWidthScale;
                        scaleEndCol = 1.0F;
                    } else {
                        // The gap will be the minimum distance value of the distances from both
                        // horizontal end points to the anchor point.
                        // If scaleCol <= 0.5, the range of scaleCol is [0, the anchor point * 2].
                        // If scaleCol > 0.5, the range of scaleCol is
                        // [(1 - the anchor point) * 2, 1].
                        // The anchor point is located at the horizontal center of the window in
                        // both cases.
                        gravity = android.view.Gravity.CENTER_HORIZONTAL;
                        mCCView.setAlignment(android.text.Layout.Alignment.ALIGN_CENTER);
                        scaleStartCol = scaleCol - gap;
                        scaleEndCol = scaleCol + gap;
                    }
                    break;
                case android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_HORIZONTAL_MODE_RIGHT :
                    gravity = android.view.Gravity.RIGHT;
                    mCCView.setAlignment(android.text.Layout.Alignment.ALIGN_RIGHT);
                    scaleEndCol = scaleCol;
                    break;
            }
            switch (verticalMode) {
                case android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_VERTICAL_MODE_TOP :
                    gravity |= android.view.Gravity.TOP;
                    scaleStartRow = scaleRow;
                    break;
                case android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_VERTICAL_MODE_CENTER :
                    gravity |= android.view.Gravity.CENTER_VERTICAL;
                    // See the above comment.
                    float gap = java.lang.Math.min(1 - scaleRow, scaleRow);
                    scaleStartRow = scaleRow - gap;
                    scaleEndRow = scaleRow + gap;
                    break;
                case android.media.Cea708CCWidget.CCWindowLayout.ANCHOR_VERTICAL_MODE_BOTTOM :
                    gravity |= android.view.Gravity.BOTTOM;
                    scaleEndRow = scaleRow;
                    break;
            }
            mCCLayout.addOrUpdateViewToSafeTitleArea(this, new android.media.Cea708CCWidget.ScaledLayout.ScaledLayoutParams(scaleStartRow, scaleEndRow, scaleStartCol, scaleEndCol));
            setCaptionWindowId(captionWindow.id);
            setRowLimit(captionWindow.rowCount);
            setGravity(gravity);
            if (captionWindow.visible) {
                show();
            } else {
                hide();
            }
        }

        @java.lang.Override
        public void onLayoutChange(android.view.View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            int width = right - left;
            int height = bottom - top;
            if ((width != mLastCaptionLayoutWidth) || (height != mLastCaptionLayoutHeight)) {
                mLastCaptionLayoutWidth = width;
                mLastCaptionLayoutHeight = height;
                updateTextSize();
            }
        }

        private void updateWidestChar() {
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setTypeface(mCaptionStyle.getTypeface());
            java.nio.charset.Charset latin1 = java.nio.charset.Charset.forName("ISO-8859-1");
            float widestCharWidth = 0.0F;
            for (int i = 0; i < 256; ++i) {
                java.lang.String ch = new java.lang.String(new byte[]{ ((byte) (i)) }, latin1);
                float charWidth = paint.measureText(ch);
                if (widestCharWidth < charWidth) {
                    widestCharWidth = charWidth;
                    mWidestChar = ch;
                }
            }
            updateTextSize();
        }

        private void updateTextSize() {
            if (mCCLayout == null)
                return;

            // Calculate text size based on the max window size.
            java.lang.StringBuilder widestTextBuilder = new java.lang.StringBuilder();
            int screenColumnCount = getScreenColumnCount();
            for (int i = 0; i < screenColumnCount; ++i) {
                widestTextBuilder.append(mWidestChar);
            }
            java.lang.String widestText = widestTextBuilder.toString();
            android.graphics.Paint paint = new android.graphics.Paint();
            paint.setTypeface(mCaptionStyle.getTypeface());
            float startFontSize = 0.0F;
            float endFontSize = 255.0F;
            while (startFontSize < endFontSize) {
                float testTextSize = (startFontSize + endFontSize) / 2.0F;
                paint.setTextSize(testTextSize);
                float width = paint.measureText(widestText);
                if ((mCCLayout.getWidth() * 0.8F) > width) {
                    startFontSize = testTextSize + 0.01F;
                } else {
                    endFontSize = testTextSize - 0.01F;
                }
            } 
            mTextSize = endFontSize * mFontScale;
            mCCView.setTextSize(mTextSize);
        }

        private int getScreenColumnCount() {
            // Assume it has a wide aspect ratio track.
            return android.media.Cea708CCWidget.CCWindowLayout.MAX_COLUMN_COUNT_16_9;
        }

        public void removeFromCaptionView() {
            if (mCCLayout != null) {
                mCCLayout.removeViewFromSafeTitleArea(this);
                mCCLayout.removeOnLayoutChangeListener(this);
                mCCLayout = null;
            }
        }

        public void setText(java.lang.String text) {
            updateText(text, false);
        }

        public void appendText(java.lang.String text) {
            updateText(text, true);
        }

        public void clearText() {
            mBuilder.clear();
            setText("");
        }

        private void updateText(java.lang.String text, boolean appended) {
            if (!appended) {
                mBuilder.clear();
            }
            if ((text != null) && (text.length() > 0)) {
                int length = mBuilder.length();
                mBuilder.append(text);
                for (android.text.style.CharacterStyle characterStyle : mCharacterStyles) {
                    mBuilder.setSpan(characterStyle, length, mBuilder.length(), android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
            java.lang.String[] lines = android.text.TextUtils.split(mBuilder.toString(), "\n");
            // Truncate text not to exceed the row limit.
            // Plus one here since the range of the rows is [0, mRowLimit].
            java.lang.String truncatedText = android.text.TextUtils.join("\n", java.util.Arrays.copyOfRange(lines, java.lang.Math.max(0, lines.length - (mRowLimit + 1)), lines.length));
            mBuilder.delete(0, mBuilder.length() - truncatedText.length());
            // Trim the buffer first then set text to CCView.
            int start = 0;
            int last = mBuilder.length() - 1;
            int end = last;
            while ((start <= end) && (mBuilder.charAt(start) <= ' ')) {
                ++start;
            } 
            while ((end >= start) && (mBuilder.charAt(end) <= ' ')) {
                --end;
            } 
            if ((start == 0) && (end == last)) {
                mCCView.setText(mBuilder);
            } else {
                android.text.SpannableStringBuilder trim = new android.text.SpannableStringBuilder();
                trim.append(mBuilder);
                if (end < last) {
                    trim.delete(end + 1, last + 1);
                }
                if (start > 0) {
                    trim.delete(0, start);
                }
                mCCView.setText(trim);
            }
        }

        public void setRowLimit(int rowLimit) {
            if (rowLimit < 0) {
                throw new java.lang.IllegalArgumentException("A rowLimit should have a positive number");
            }
            mRowLimit = rowLimit;
        }
    }

    /**
     *
     *
     * @unknown 
     */
    static class CCView extends com.android.internal.widget.SubtitleView {
        private static final android.view.accessibility.CaptioningManager.CaptionStyle DEFAULT_CAPTION_STYLE = android.view.accessibility.CaptioningManager.CaptionStyle.DEFAULT;

        public CCView(android.content.Context context) {
            this(context, null);
        }

        public CCView(android.content.Context context, android.util.AttributeSet attrs) {
            this(context, attrs, 0);
        }

        public CCView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
            this(context, attrs, defStyleAttr, 0);
        }

        public CCView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }

        public void setCaptionStyle(android.view.accessibility.CaptioningManager.CaptionStyle style) {
            setForegroundColor(style.hasForegroundColor() ? style.foregroundColor : android.media.Cea708CCWidget.CCView.DEFAULT_CAPTION_STYLE.foregroundColor);
            setBackgroundColor(style.hasBackgroundColor() ? style.backgroundColor : android.media.Cea708CCWidget.CCView.DEFAULT_CAPTION_STYLE.backgroundColor);
            setEdgeType(style.hasEdgeType() ? style.edgeType : android.media.Cea708CCWidget.CCView.DEFAULT_CAPTION_STYLE.edgeType);
            setEdgeColor(style.hasEdgeColor() ? style.edgeColor : android.media.Cea708CCWidget.CCView.DEFAULT_CAPTION_STYLE.edgeColor);
            setTypeface(style.getTypeface());
        }
    }
}

