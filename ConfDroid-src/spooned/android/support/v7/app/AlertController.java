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
package android.support.v7.app;


class AlertController {
    private final android.content.Context mContext;

    final android.support.v7.app.AppCompatDialog mDialog;

    private final android.view.Window mWindow;

    private java.lang.CharSequence mTitle;

    private java.lang.CharSequence mMessage;

    android.widget.ListView mListView;

    private android.view.View mView;

    private int mViewLayoutResId;

    private int mViewSpacingLeft;

    private int mViewSpacingTop;

    private int mViewSpacingRight;

    private int mViewSpacingBottom;

    private boolean mViewSpacingSpecified = false;

    android.widget.Button mButtonPositive;

    private java.lang.CharSequence mButtonPositiveText;

    android.os.Message mButtonPositiveMessage;

    android.widget.Button mButtonNegative;

    private java.lang.CharSequence mButtonNegativeText;

    android.os.Message mButtonNegativeMessage;

    android.widget.Button mButtonNeutral;

    private java.lang.CharSequence mButtonNeutralText;

    android.os.Message mButtonNeutralMessage;

    android.support.v4.widget.NestedScrollView mScrollView;

    private int mIconId = 0;

    private android.graphics.drawable.Drawable mIcon;

    private android.widget.ImageView mIconView;

    private android.widget.TextView mTitleView;

    private android.widget.TextView mMessageView;

    private android.view.View mCustomTitleView;

    android.widget.ListAdapter mAdapter;

    int mCheckedItem = -1;

    private int mAlertDialogLayout;

    private int mButtonPanelSideLayout;

    int mListLayout;

    int mMultiChoiceItemLayout;

    int mSingleChoiceItemLayout;

    int mListItemLayout;

    private int mButtonPanelLayoutHint = android.support.v7.app.AlertDialog.LAYOUT_HINT_NONE;

    android.os.Handler mHandler;

    private final android.view.View.OnClickListener mButtonHandler = new android.view.View.OnClickListener() {
        @java.lang.Override
        public void onClick(android.view.View v) {
            final android.os.Message m;
            if ((v == mButtonPositive) && (mButtonPositiveMessage != null)) {
                m = android.os.Message.obtain(mButtonPositiveMessage);
            } else
                if ((v == mButtonNegative) && (mButtonNegativeMessage != null)) {
                    m = android.os.Message.obtain(mButtonNegativeMessage);
                } else
                    if ((v == mButtonNeutral) && (mButtonNeutralMessage != null)) {
                        m = android.os.Message.obtain(mButtonNeutralMessage);
                    } else {
                        m = null;
                    }


            if (m != null) {
                m.sendToTarget();
            }
            // Post a message so we dismiss after the above handlers are executed
            mHandler.obtainMessage(android.support.v7.app.AlertController.ButtonHandler.MSG_DISMISS_DIALOG, mDialog).sendToTarget();
        }
    };

    private static final class ButtonHandler extends android.os.Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private java.lang.ref.WeakReference<android.content.DialogInterface> mDialog;

        public ButtonHandler(android.content.DialogInterface dialog) {
            mDialog = new java.lang.ref.WeakReference<>(dialog);
        }

        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.content.DialogInterface.BUTTON_POSITIVE :
                case android.content.DialogInterface.BUTTON_NEGATIVE :
                case android.content.DialogInterface.BUTTON_NEUTRAL :
                    ((android.content.DialogInterface.OnClickListener) (msg.obj)).onClick(mDialog.get(), msg.what);
                    break;
                case android.support.v7.app.AlertController.ButtonHandler.MSG_DISMISS_DIALOG :
                    ((android.content.DialogInterface) (msg.obj)).dismiss();
            }
        }
    }

    public AlertController(android.content.Context context, android.support.v7.app.AppCompatDialog di, android.view.Window window) {
        mContext = context;
        mDialog = di;
        mWindow = window;
        mHandler = new android.support.v7.app.AlertController.ButtonHandler(di);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(null, R.styleable.AlertDialog, R.attr.alertDialogStyle, 0);
        mAlertDialogLayout = a.getResourceId(R.styleable.AlertDialog_android_layout, 0);
        mButtonPanelSideLayout = a.getResourceId(R.styleable.AlertDialog_buttonPanelSideLayout, 0);
        mListLayout = a.getResourceId(R.styleable.AlertDialog_listLayout, 0);
        mMultiChoiceItemLayout = a.getResourceId(R.styleable.AlertDialog_multiChoiceItemLayout, 0);
        mSingleChoiceItemLayout = a.getResourceId(R.styleable.AlertDialog_singleChoiceItemLayout, 0);
        mListItemLayout = a.getResourceId(R.styleable.AlertDialog_listItemLayout, 0);
        a.recycle();
        /* We use a custom title so never request a window title */
        di.supportRequestWindowFeature(android.view.Window.FEATURE_NO_TITLE);
    }

    static boolean canTextInput(android.view.View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }
        if (!(v instanceof android.view.ViewGroup)) {
            return false;
        }
        android.view.ViewGroup vg = ((android.view.ViewGroup) (v));
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (android.support.v7.app.AlertController.canTextInput(v)) {
                return true;
            }
        } 
        return false;
    }

    public void installContent() {
        final int contentView = selectContentView();
        mDialog.setContentView(contentView);
        setupView();
    }

    private int selectContentView() {
        if (mButtonPanelSideLayout == 0) {
            return mAlertDialogLayout;
        }
        if (mButtonPanelLayoutHint == android.support.v7.app.AlertDialog.LAYOUT_HINT_SIDE) {
            return mButtonPanelSideLayout;
        }
        return mAlertDialogLayout;
    }

    public void setTitle(java.lang.CharSequence title) {
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    /**
     *
     *
     * @see AlertDialog.Builder#setCustomTitle(View)
     */
    public void setCustomTitle(android.view.View customTitleView) {
        mCustomTitleView = customTitleView;
    }

    public void setMessage(java.lang.CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    /**
     * Set the view resource to display in the dialog.
     */
    public void setView(int layoutResId) {
        mView = null;
        mViewLayoutResId = layoutResId;
        mViewSpacingSpecified = false;
    }

    /**
     * Set the view to display in the dialog.
     */
    public void setView(android.view.View view) {
        mView = view;
        mViewLayoutResId = 0;
        mViewSpacingSpecified = false;
    }

    /**
     * Set the view to display in the dialog along with the spacing around that view
     */
    public void setView(android.view.View view, int viewSpacingLeft, int viewSpacingTop, int viewSpacingRight, int viewSpacingBottom) {
        mView = view;
        mViewLayoutResId = 0;
        mViewSpacingSpecified = true;
        mViewSpacingLeft = viewSpacingLeft;
        mViewSpacingTop = viewSpacingTop;
        mViewSpacingRight = viewSpacingRight;
        mViewSpacingBottom = viewSpacingBottom;
    }

    /**
     * Sets a hint for the best button panel layout.
     */
    public void setButtonPanelLayoutHint(int layoutHint) {
        mButtonPanelLayoutHint = layoutHint;
    }

    /**
     * Sets a click listener or a message to be sent when the button is clicked.
     * You only need to pass one of {@code listener} or {@code msg}.
     *
     * @param whichButton
     * 		Which button, can be one of
     * 		{@link DialogInterface#BUTTON_POSITIVE},
     * 		{@link DialogInterface#BUTTON_NEGATIVE}, or
     * 		{@link DialogInterface#BUTTON_NEUTRAL}
     * @param text
     * 		The text to display in positive button.
     * @param listener
     * 		The {@link DialogInterface.OnClickListener} to use.
     * @param msg
     * 		The {@link Message} to be sent when clicked.
     */
    public void setButton(int whichButton, java.lang.CharSequence text, android.content.DialogInterface.OnClickListener listener, android.os.Message msg) {
        if ((msg == null) && (listener != null)) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }
        switch (whichButton) {
            case android.content.DialogInterface.BUTTON_POSITIVE :
                mButtonPositiveText = text;
                mButtonPositiveMessage = msg;
                break;
            case android.content.DialogInterface.BUTTON_NEGATIVE :
                mButtonNegativeText = text;
                mButtonNegativeMessage = msg;
                break;
            case android.content.DialogInterface.BUTTON_NEUTRAL :
                mButtonNeutralText = text;
                mButtonNeutralMessage = msg;
                break;
            default :
                throw new java.lang.IllegalArgumentException("Button does not exist");
        }
    }

    /**
     * Specifies the icon to display next to the alert title.
     *
     * @param resId
     * 		the resource identifier of the drawable to use as the icon,
     * 		or 0 for no icon
     */
    public void setIcon(int resId) {
        mIcon = null;
        mIconId = resId;
        if (mIconView != null) {
            if (resId != 0) {
                mIconView.setVisibility(android.view.View.VISIBLE);
                mIconView.setImageResource(mIconId);
            } else {
                mIconView.setVisibility(android.view.View.GONE);
            }
        }
    }

    /**
     * Specifies the icon to display next to the alert title.
     *
     * @param icon
     * 		the drawable to use as the icon or null for no icon
     */
    public void setIcon(android.graphics.drawable.Drawable icon) {
        mIcon = icon;
        mIconId = 0;
        if (mIconView != null) {
            if (icon != null) {
                mIconView.setVisibility(android.view.View.VISIBLE);
                mIconView.setImageDrawable(icon);
            } else {
                mIconView.setVisibility(android.view.View.GONE);
            }
        }
    }

    /**
     *
     *
     * @param attrId
     * 		the attributeId of the theme-specific drawable
     * 		to resolve the resourceId for.
     * @return resId the resourceId of the theme-specific drawable
     */
    public int getIconAttributeResId(int attrId) {
        android.util.TypedValue out = new android.util.TypedValue();
        mContext.getTheme().resolveAttribute(attrId, out, true);
        return out.resourceId;
    }

    public android.widget.ListView getListView() {
        return mListView;
    }

    public android.widget.Button getButton(int whichButton) {
        switch (whichButton) {
            case android.content.DialogInterface.BUTTON_POSITIVE :
                return mButtonPositive;
            case android.content.DialogInterface.BUTTON_NEGATIVE :
                return mButtonNegative;
            case android.content.DialogInterface.BUTTON_NEUTRAL :
                return mButtonNeutral;
            default :
                return null;
        }
    }

    @java.lang.SuppressWarnings({ "UnusedDeclaration" })
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        return (mScrollView != null) && mScrollView.executeKeyEvent(event);
    }

    @java.lang.SuppressWarnings({ "UnusedDeclaration" })
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        return (mScrollView != null) && mScrollView.executeKeyEvent(event);
    }

    /**
     * Resolves whether a custom or default panel should be used. Removes the
     * default panel if a custom panel should be used. If the resolved panel is
     * a view stub, inflates before returning.
     *
     * @param customPanel
     * 		the custom panel
     * @param defaultPanel
     * 		the default panel
     * @return the panel to use
     */
    @android.support.annotation.Nullable
    private android.view.ViewGroup resolvePanel(@android.support.annotation.Nullable
    android.view.View customPanel, @android.support.annotation.Nullable
    android.view.View defaultPanel) {
        if (customPanel == null) {
            // Inflate the default panel, if needed.
            if (defaultPanel instanceof android.view.ViewStub) {
                defaultPanel = ((android.view.ViewStub) (defaultPanel)).inflate();
            }
            return ((android.view.ViewGroup) (defaultPanel));
        }
        // Remove the default panel entirely.
        if (defaultPanel != null) {
            final android.view.ViewParent parent = defaultPanel.getParent();
            if (parent instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup) (parent)).removeView(defaultPanel);
            }
        }
        // Inflate the custom panel, if needed.
        if (customPanel instanceof android.view.ViewStub) {
            customPanel = ((android.view.ViewStub) (customPanel)).inflate();
        }
        return ((android.view.ViewGroup) (customPanel));
    }

    private void setupView() {
        final android.view.View parentPanel = mWindow.findViewById(R.id.parentPanel);
        final android.view.View defaultTopPanel = parentPanel.findViewById(R.id.topPanel);
        final android.view.View defaultContentPanel = parentPanel.findViewById(R.id.contentPanel);
        final android.view.View defaultButtonPanel = parentPanel.findViewById(R.id.buttonPanel);
        // Install custom content before setting up the title or buttons so
        // that we can handle panel overrides.
        final android.view.ViewGroup customPanel = ((android.view.ViewGroup) (parentPanel.findViewById(R.id.customPanel)));
        setupCustomContent(customPanel);
        final android.view.View customTopPanel = customPanel.findViewById(R.id.topPanel);
        final android.view.View customContentPanel = customPanel.findViewById(R.id.contentPanel);
        final android.view.View customButtonPanel = customPanel.findViewById(R.id.buttonPanel);
        // Resolve the correct panels and remove the defaults, if needed.
        final android.view.ViewGroup topPanel = resolvePanel(customTopPanel, defaultTopPanel);
        final android.view.ViewGroup contentPanel = resolvePanel(customContentPanel, defaultContentPanel);
        final android.view.ViewGroup buttonPanel = resolvePanel(customButtonPanel, defaultButtonPanel);
        setupContent(contentPanel);
        setupButtons(buttonPanel);
        setupTitle(topPanel);
        final boolean hasCustomPanel = (customPanel != null) && (customPanel.getVisibility() != android.view.View.GONE);
        final boolean hasTopPanel = (topPanel != null) && (topPanel.getVisibility() != android.view.View.GONE);
        final boolean hasButtonPanel = (buttonPanel != null) && (buttonPanel.getVisibility() != android.view.View.GONE);
        // Only display the text spacer if we don't have buttons.
        if (!hasButtonPanel) {
            if (contentPanel != null) {
                final android.view.View spacer = contentPanel.findViewById(R.id.textSpacerNoButtons);
                if (spacer != null) {
                    spacer.setVisibility(android.view.View.VISIBLE);
                }
            }
        }
        if (hasTopPanel) {
            // Only clip scrolling content to padding if we have a title.
            if (mScrollView != null) {
                mScrollView.setClipToPadding(true);
            }
        }
        // Update scroll indicators as needed.
        if (!hasCustomPanel) {
            final android.view.View content = (mListView != null) ? mListView : mScrollView;
            if (content != null) {
                final int indicators = (hasTopPanel ? android.support.v4.view.ViewCompat.SCROLL_INDICATOR_TOP : 0) | (hasButtonPanel ? android.support.v4.view.ViewCompat.SCROLL_INDICATOR_BOTTOM : 0);
                setScrollIndicators(contentPanel, content, indicators, android.support.v4.view.ViewCompat.SCROLL_INDICATOR_TOP | android.support.v4.view.ViewCompat.SCROLL_INDICATOR_BOTTOM);
            }
        }
        final android.widget.ListView listView = mListView;
        if ((listView != null) && (mAdapter != null)) {
            listView.setAdapter(mAdapter);
            final int checkedItem = mCheckedItem;
            if (checkedItem > (-1)) {
                listView.setItemChecked(checkedItem, true);
                listView.setSelection(checkedItem);
            }
        }
    }

    private void setScrollIndicators(android.view.ViewGroup contentPanel, android.view.View content, final int indicators, final int mask) {
        // Set up scroll indicators (if present).
        android.view.View indicatorUp = mWindow.findViewById(R.id.scrollIndicatorUp);
        android.view.View indicatorDown = mWindow.findViewById(R.id.scrollIndicatorDown);
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            // We're on Marshmallow so can rely on the View APIs
            android.support.v4.view.ViewCompat.setScrollIndicators(content, indicators, mask);
            // We can also remove the compat indicator views
            if (indicatorUp != null) {
                contentPanel.removeView(indicatorUp);
            }
            if (indicatorDown != null) {
                contentPanel.removeView(indicatorDown);
            }
        } else {
            // First, remove the indicator views if we're not set to use them
            if ((indicatorUp != null) && ((indicators & android.support.v4.view.ViewCompat.SCROLL_INDICATOR_TOP) == 0)) {
                contentPanel.removeView(indicatorUp);
                indicatorUp = null;
            }
            if ((indicatorDown != null) && ((indicators & android.support.v4.view.ViewCompat.SCROLL_INDICATOR_BOTTOM) == 0)) {
                contentPanel.removeView(indicatorDown);
                indicatorDown = null;
            }
            if ((indicatorUp != null) || (indicatorDown != null)) {
                final android.view.View top = indicatorUp;
                final android.view.View bottom = indicatorDown;
                if (mMessage != null) {
                    // We're just showing the ScrollView, set up listener.
                    mScrollView.setOnScrollChangeListener(new android.support.v4.widget.NestedScrollView.OnScrollChangeListener() {
                        @java.lang.Override
                        public void onScrollChange(android.support.v4.widget.NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                            android.support.v7.app.AlertController.manageScrollIndicators(v, top, bottom);
                        }
                    });
                    // Set up the indicators following layout.
                    mScrollView.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            android.support.v7.app.AlertController.manageScrollIndicators(mScrollView, top, bottom);
                        }
                    });
                } else
                    if (mListView != null) {
                        // We're just showing the AbsListView, set up listener.
                        mListView.setOnScrollListener(new android.widget.AbsListView.OnScrollListener() {
                            @java.lang.Override
                            public void onScrollStateChanged(android.widget.AbsListView view, int scrollState) {
                            }

                            @java.lang.Override
                            public void onScroll(android.widget.AbsListView v, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                                android.support.v7.app.AlertController.manageScrollIndicators(v, top, bottom);
                            }
                        });
                        // Set up the indicators following layout.
                        mListView.post(new java.lang.Runnable() {
                            @java.lang.Override
                            public void run() {
                                android.support.v7.app.AlertController.manageScrollIndicators(mListView, top, bottom);
                            }
                        });
                    } else {
                        // We don't have any content to scroll, remove the indicators.
                        if (top != null) {
                            contentPanel.removeView(top);
                        }
                        if (bottom != null) {
                            contentPanel.removeView(bottom);
                        }
                    }

            }
        }
    }

    private void setupCustomContent(android.view.ViewGroup customPanel) {
        final android.view.View customView;
        if (mView != null) {
            customView = mView;
        } else
            if (mViewLayoutResId != 0) {
                final android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mContext);
                customView = inflater.inflate(mViewLayoutResId, customPanel, false);
            } else {
                customView = null;
            }

        final boolean hasCustomView = customView != null;
        if ((!hasCustomView) || (!android.support.v7.app.AlertController.canTextInput(customView))) {
            mWindow.setFlags(android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM, android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        if (hasCustomView) {
            final android.widget.FrameLayout custom = ((android.widget.FrameLayout) (mWindow.findViewById(R.id.custom)));
            custom.addView(customView, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
            if (mViewSpacingSpecified) {
                custom.setPadding(mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
            }
            if (mListView != null) {
                ((android.widget.LinearLayout.LayoutParams) (customPanel.getLayoutParams())).weight = 0;
            }
        } else {
            customPanel.setVisibility(android.view.View.GONE);
        }
    }

    private void setupTitle(android.view.ViewGroup topPanel) {
        if (mCustomTitleView != null) {
            // Add the custom title view directly to the topPanel layout
            android.view.ViewGroup.LayoutParams lp = new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
            topPanel.addView(mCustomTitleView, 0, lp);
            // Hide the title template
            android.view.View titleTemplate = mWindow.findViewById(R.id.title_template);
            titleTemplate.setVisibility(android.view.View.GONE);
        } else {
            mIconView = ((android.widget.ImageView) (mWindow.findViewById(android.R.id.icon)));
            final boolean hasTextTitle = !android.text.TextUtils.isEmpty(mTitle);
            if (hasTextTitle) {
                // Display the title if a title is supplied, else hide it.
                mTitleView = ((android.widget.TextView) (mWindow.findViewById(R.id.alertTitle)));
                mTitleView.setText(mTitle);
                // Do this last so that if the user has supplied any icons we
                // use them instead of the default ones. If the user has
                // specified 0 then make it disappear.
                if (mIconId != 0) {
                    mIconView.setImageResource(mIconId);
                } else
                    if (mIcon != null) {
                        mIconView.setImageDrawable(mIcon);
                    } else {
                        // Apply the padding from the icon to ensure the title is
                        // aligned correctly.
                        mTitleView.setPadding(mIconView.getPaddingLeft(), mIconView.getPaddingTop(), mIconView.getPaddingRight(), mIconView.getPaddingBottom());
                        mIconView.setVisibility(android.view.View.GONE);
                    }

            } else {
                // Hide the title template
                final android.view.View titleTemplate = mWindow.findViewById(R.id.title_template);
                titleTemplate.setVisibility(android.view.View.GONE);
                mIconView.setVisibility(android.view.View.GONE);
                topPanel.setVisibility(android.view.View.GONE);
            }
        }
    }

    private void setupContent(android.view.ViewGroup contentPanel) {
        mScrollView = ((android.support.v4.widget.NestedScrollView) (mWindow.findViewById(R.id.scrollView)));
        mScrollView.setFocusable(false);
        mScrollView.setNestedScrollingEnabled(false);
        // Special case for users that only want to display a String
        mMessageView = ((android.widget.TextView) (contentPanel.findViewById(android.R.id.message)));
        if (mMessageView == null) {
            return;
        }
        if (mMessage != null) {
            mMessageView.setText(mMessage);
        } else {
            mMessageView.setVisibility(android.view.View.GONE);
            mScrollView.removeView(mMessageView);
            if (mListView != null) {
                final android.view.ViewGroup scrollParent = ((android.view.ViewGroup) (mScrollView.getParent()));
                final int childIndex = scrollParent.indexOfChild(mScrollView);
                scrollParent.removeViewAt(childIndex);
                scrollParent.addView(mListView, childIndex, new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
            } else {
                contentPanel.setVisibility(android.view.View.GONE);
            }
        }
    }

    static void manageScrollIndicators(android.view.View v, android.view.View upIndicator, android.view.View downIndicator) {
        if (upIndicator != null) {
            upIndicator.setVisibility(android.support.v4.view.ViewCompat.canScrollVertically(v, -1) ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        }
        if (downIndicator != null) {
            downIndicator.setVisibility(android.support.v4.view.ViewCompat.canScrollVertically(v, 1) ? android.view.View.VISIBLE : android.view.View.INVISIBLE);
        }
    }

    private void setupButtons(android.view.ViewGroup buttonPanel) {
        int BIT_BUTTON_POSITIVE = 1;
        int BIT_BUTTON_NEGATIVE = 2;
        int BIT_BUTTON_NEUTRAL = 4;
        int whichButtons = 0;
        mButtonPositive = ((android.widget.Button) (buttonPanel.findViewById(android.R.id.button1)));
        mButtonPositive.setOnClickListener(mButtonHandler);
        if (android.text.TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(android.view.View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(android.view.View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }
        mButtonNegative = ((android.widget.Button) (buttonPanel.findViewById(android.R.id.button2)));
        mButtonNegative.setOnClickListener(mButtonHandler);
        if (android.text.TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(android.view.View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(android.view.View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }
        mButtonNeutral = ((android.widget.Button) (buttonPanel.findViewById(android.R.id.button3)));
        mButtonNeutral.setOnClickListener(mButtonHandler);
        if (android.text.TextUtils.isEmpty(mButtonNeutralText)) {
            mButtonNeutral.setVisibility(android.view.View.GONE);
        } else {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setVisibility(android.view.View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
        }
        final boolean hasButtons = whichButtons != 0;
        if (!hasButtons) {
            buttonPanel.setVisibility(android.view.View.GONE);
        }
    }

    public static class AlertParams {
        public final android.content.Context mContext;

        public final android.view.LayoutInflater mInflater;

        public int mIconId = 0;

        public android.graphics.drawable.Drawable mIcon;

        public int mIconAttrId = 0;

        public java.lang.CharSequence mTitle;

        public android.view.View mCustomTitleView;

        public java.lang.CharSequence mMessage;

        public java.lang.CharSequence mPositiveButtonText;

        public android.content.DialogInterface.OnClickListener mPositiveButtonListener;

        public java.lang.CharSequence mNegativeButtonText;

        public android.content.DialogInterface.OnClickListener mNegativeButtonListener;

        public java.lang.CharSequence mNeutralButtonText;

        public android.content.DialogInterface.OnClickListener mNeutralButtonListener;

        public boolean mCancelable;

        public android.content.DialogInterface.OnCancelListener mOnCancelListener;

        public android.content.DialogInterface.OnDismissListener mOnDismissListener;

        public android.content.DialogInterface.OnKeyListener mOnKeyListener;

        public java.lang.CharSequence[] mItems;

        public android.widget.ListAdapter mAdapter;

        public android.content.DialogInterface.OnClickListener mOnClickListener;

        public int mViewLayoutResId;

        public android.view.View mView;

        public int mViewSpacingLeft;

        public int mViewSpacingTop;

        public int mViewSpacingRight;

        public int mViewSpacingBottom;

        public boolean mViewSpacingSpecified = false;

        public boolean[] mCheckedItems;

        public boolean mIsMultiChoice;

        public boolean mIsSingleChoice;

        public int mCheckedItem = -1;

        public android.content.DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;

        public android.database.Cursor mCursor;

        public java.lang.String mLabelColumn;

        public java.lang.String mIsCheckedColumn;

        public boolean mForceInverseBackground;

        public android.widget.AdapterView.OnItemSelectedListener mOnItemSelectedListener;

        public android.support.v7.app.AlertController.AlertParams.OnPrepareListViewListener mOnPrepareListViewListener;

        public boolean mRecycleOnMeasure = true;

        /**
         * Interface definition for a callback to be invoked before the ListView
         * will be bound to an adapter.
         */
        public interface OnPrepareListViewListener {
            /**
             * Called before the ListView is bound to an adapter.
             *
             * @param listView
             * 		The ListView that will be shown in the dialog.
             */
            void onPrepareListView(android.widget.ListView listView);
        }

        public AlertParams(android.content.Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        }

        public void apply(android.support.v7.app.AlertController dialog) {
            if (mCustomTitleView != null) {
                dialog.setCustomTitle(mCustomTitleView);
            } else {
                if (mTitle != null) {
                    dialog.setTitle(mTitle);
                }
                if (mIcon != null) {
                    dialog.setIcon(mIcon);
                }
                if (mIconId != 0) {
                    dialog.setIcon(mIconId);
                }
                if (mIconAttrId != 0) {
                    dialog.setIcon(dialog.getIconAttributeResId(mIconAttrId));
                }
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            if (mPositiveButtonText != null) {
                dialog.setButton(android.content.DialogInterface.BUTTON_POSITIVE, mPositiveButtonText, mPositiveButtonListener, null);
            }
            if (mNegativeButtonText != null) {
                dialog.setButton(android.content.DialogInterface.BUTTON_NEGATIVE, mNegativeButtonText, mNegativeButtonListener, null);
            }
            if (mNeutralButtonText != null) {
                dialog.setButton(android.content.DialogInterface.BUTTON_NEUTRAL, mNeutralButtonText, mNeutralButtonListener, null);
            }
            // For a list, the client can either supply an array of items or an
            // adapter or a cursor
            if (((mItems != null) || (mCursor != null)) || (mAdapter != null)) {
                createListView(dialog);
            }
            if (mView != null) {
                if (mViewSpacingSpecified) {
                    dialog.setView(mView, mViewSpacingLeft, mViewSpacingTop, mViewSpacingRight, mViewSpacingBottom);
                } else {
                    dialog.setView(mView);
                }
            } else
                if (mViewLayoutResId != 0) {
                    dialog.setView(mViewLayoutResId);
                }

            /* dialog.setCancelable(mCancelable);
            dialog.setOnCancelListener(mOnCancelListener);
            if (mOnKeyListener != null) {
            dialog.setOnKeyListener(mOnKeyListener);
            }
             */
        }

        private void createListView(final android.support.v7.app.AlertController dialog) {
            final android.widget.ListView listView = ((android.widget.ListView) (mInflater.inflate(dialog.mListLayout, null)));
            final android.widget.ListAdapter adapter;
            if (mIsMultiChoice) {
                if (mCursor == null) {
                    adapter = new android.widget.ArrayAdapter<java.lang.CharSequence>(mContext, dialog.mMultiChoiceItemLayout, android.R.id.text1, mItems) {
                        @java.lang.Override
                        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
                            android.view.View view = super.getView(position, convertView, parent);
                            if (mCheckedItems != null) {
                                boolean isItemChecked = mCheckedItems[position];
                                if (isItemChecked) {
                                    listView.setItemChecked(position, true);
                                }
                            }
                            return view;
                        }
                    };
                } else {
                    adapter = new android.widget.CursorAdapter(mContext, mCursor, false) {
                        private final int mLabelIndex;

                        private final int mIsCheckedIndex;

                        {
                            final android.database.Cursor cursor = getCursor();
                            mLabelIndex = cursor.getColumnIndexOrThrow(mLabelColumn);
                            mIsCheckedIndex = cursor.getColumnIndexOrThrow(mIsCheckedColumn);
                        }

                        @java.lang.Override
                        public void bindView(android.view.View view, android.content.Context context, android.database.Cursor cursor) {
                            android.widget.CheckedTextView text = ((android.widget.CheckedTextView) (view.findViewById(android.R.id.text1)));
                            text.setText(cursor.getString(mLabelIndex));
                            listView.setItemChecked(cursor.getPosition(), cursor.getInt(mIsCheckedIndex) == 1);
                        }

                        @java.lang.Override
                        public android.view.View newView(android.content.Context context, android.database.Cursor cursor, android.view.ViewGroup parent) {
                            return mInflater.inflate(dialog.mMultiChoiceItemLayout, parent, false);
                        }
                    };
                }
            } else {
                final int layout;
                if (mIsSingleChoice) {
                    layout = dialog.mSingleChoiceItemLayout;
                } else {
                    layout = dialog.mListItemLayout;
                }
                if (mCursor != null) {
                    adapter = new android.widget.SimpleCursorAdapter(mContext, layout, mCursor, new java.lang.String[]{ mLabelColumn }, new int[]{ android.R.id.text1 });
                } else
                    if (mAdapter != null) {
                        adapter = mAdapter;
                    } else {
                        adapter = new android.support.v7.app.AlertController.CheckedItemAdapter(mContext, layout, android.R.id.text1, mItems);
                    }

            }
            if (mOnPrepareListViewListener != null) {
                mOnPrepareListViewListener.onPrepareListView(listView);
            }
            /* Don't directly set the adapter on the ListView as we might
            want to add a footer to the ListView later.
             */
            dialog.mAdapter = adapter;
            dialog.mCheckedItem = mCheckedItem;
            if (mOnClickListener != null) {
                listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                    @java.lang.Override
                    public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                        mOnClickListener.onClick(dialog.mDialog, position);
                        if (!mIsSingleChoice) {
                            dialog.mDialog.dismiss();
                        }
                    }
                });
            } else
                if (mOnCheckboxClickListener != null) {
                    listView.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
                        @java.lang.Override
                        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View v, int position, long id) {
                            if (mCheckedItems != null) {
                                mCheckedItems[position] = listView.isItemChecked(position);
                            }
                            mOnCheckboxClickListener.onClick(dialog.mDialog, position, listView.isItemChecked(position));
                        }
                    });
                }

            // Attach a given OnItemSelectedListener to the ListView
            if (mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(mOnItemSelectedListener);
            }
            if (mIsSingleChoice) {
                listView.setChoiceMode(android.widget.ListView.CHOICE_MODE_SINGLE);
            } else
                if (mIsMultiChoice) {
                    listView.setChoiceMode(android.widget.ListView.CHOICE_MODE_MULTIPLE);
                }

            dialog.mListView = listView;
        }
    }

    private static class CheckedItemAdapter extends android.widget.ArrayAdapter<java.lang.CharSequence> {
        public CheckedItemAdapter(android.content.Context context, int resource, int textViewResourceId, java.lang.CharSequence[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @java.lang.Override
        public boolean hasStableIds() {
            return true;
        }

        @java.lang.Override
        public long getItemId(int position) {
            return position;
        }
    }
}

