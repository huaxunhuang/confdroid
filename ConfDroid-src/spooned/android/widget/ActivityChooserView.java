/**
 * Copyright (C) 2011 The Android Open Source Project
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
 * This class is a view for choosing an activity for handling a given {@link Intent}.
 * <p>
 * The view is composed of two adjacent buttons:
 * <ul>
 * <li>
 * The left button is an immediate action and allows one click activity choosing.
 * Tapping this button immediately executes the intent without requiring any further
 * user input. Long press on this button shows a popup for changing the default
 * activity.
 * </li>
 * <li>
 * The right button is an overflow action and provides an optimized menu
 * of additional activities. Tapping this button shows a popup anchored to this
 * view, listing the most frequently used activities. This list is initially
 * limited to a small number of items in frequency used order. The last item,
 * "Show all..." serves as an affordance to display all available activities.
 * </li>
 * </ul>
 * </p>
 *
 * @unknown 
 */
public class ActivityChooserView extends android.view.ViewGroup implements android.widget.ActivityChooserModel.ActivityChooserModelClient {
    private static final java.lang.String LOG_TAG = "ActivityChooserView";

    /**
     * An adapter for displaying the activities in an {@link AdapterView}.
     */
    private final android.widget.ActivityChooserView.ActivityChooserViewAdapter mAdapter;

    /**
     * Implementation of various interfaces to avoid publishing them in the APIs.
     */
    private final android.widget.ActivityChooserView.Callbacks mCallbacks;

    /**
     * The content of this view.
     */
    private final android.widget.LinearLayout mActivityChooserContent;

    /**
     * Stores the background drawable to allow hiding and latter showing.
     */
    private final android.graphics.drawable.Drawable mActivityChooserContentBackground;

    /**
     * The expand activities action button;
     */
    private final android.widget.FrameLayout mExpandActivityOverflowButton;

    /**
     * The image for the expand activities action button;
     */
    private final android.widget.ImageView mExpandActivityOverflowButtonImage;

    /**
     * The default activities action button;
     */
    private final android.widget.FrameLayout mDefaultActivityButton;

    /**
     * The image for the default activities action button;
     */
    private final android.widget.ImageView mDefaultActivityButtonImage;

    /**
     * The maximal width of the list popup.
     */
    private final int mListPopupMaxWidth;

    /**
     * The ActionProvider hosting this view, if applicable.
     */
    android.view.ActionProvider mProvider;

    /**
     * Observer for the model data.
     */
    private final android.database.DataSetObserver mModelDataSetOberver = new android.database.DataSetObserver() {
        @java.lang.Override
        public void onChanged() {
            onChanged();
            mAdapter.notifyDataSetChanged();
        }

        @java.lang.Override
        public void onInvalidated() {
            onInvalidated();
            mAdapter.notifyDataSetInvalidated();
        }
    };

    private final android.view.ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
        @java.lang.Override
        public void onGlobalLayout() {
            if (isShowingPopup()) {
                if (!isShown()) {
                    getListPopupWindow().dismiss();
                } else {
                    getListPopupWindow().show();
                    if (mProvider != null) {
                        mProvider.subUiVisibilityChanged(true);
                    }
                }
            }
        }
    };

    /**
     * Popup window for showing the activity overflow list.
     */
    private android.widget.ListPopupWindow mListPopupWindow;

    /**
     * Listener for the dismissal of the popup/alert.
     */
    private android.widget.PopupWindow.OnDismissListener mOnDismissListener;

    /**
     * Flag whether a default activity currently being selected.
     */
    private boolean mIsSelectingDefaultActivity;

    /**
     * The count of activities in the popup.
     */
    private int mInitialActivityCount = android.widget.ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_DEFAULT;

    /**
     * Flag whether this view is attached to a window.
     */
    private boolean mIsAttachedToWindow;

    /**
     * String resource for formatting content description of the default target.
     */
    private int mDefaultActionButtonContentDescription;

    /**
     * Create a new instance.
     *
     * @param context
     * 		The application environment.
     */
    public ActivityChooserView(android.content.Context context) {
        this(context, null);
    }

    /**
     * Create a new instance.
     *
     * @param context
     * 		The application environment.
     * @param attrs
     * 		A collection of attributes.
     */
    public ActivityChooserView(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Create a new instance.
     *
     * @param context
     * 		The application environment.
     * @param attrs
     * 		A collection of attributes.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     */
    public ActivityChooserView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    /**
     * Create a new instance.
     *
     * @param context
     * 		The application environment.
     * @param attrs
     * 		A collection of attributes.
     * @param defStyleAttr
     * 		An attribute in the current theme that contains a
     * 		reference to a style resource that supplies default values for
     * 		the view. Can be 0 to not look for defaults.
     * @param defStyleRes
     * 		A resource identifier of a style resource that
     * 		supplies default values for the view, used only if
     * 		defStyleAttr is 0 or can not be found in the theme. Can be 0
     * 		to not look for defaults.
     */
    public ActivityChooserView(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        android.content.res.TypedArray attributesArray = context.obtainStyledAttributes(attrs, R.styleable.ActivityChooserView, defStyleAttr, defStyleRes);
        saveAttributeDataForStyleable(context, R.styleable.ActivityChooserView, attrs, attributesArray, defStyleAttr, defStyleRes);
        mInitialActivityCount = attributesArray.getInt(R.styleable.ActivityChooserView_initialActivityCount, android.widget.ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_DEFAULT);
        android.graphics.drawable.Drawable expandActivityOverflowButtonDrawable = attributesArray.getDrawable(R.styleable.ActivityChooserView_expandActivityOverflowButtonDrawable);
        attributesArray.recycle();
        android.view.LayoutInflater inflater = android.view.LayoutInflater.from(mContext);
        inflater.inflate(R.layout.activity_chooser_view, this, true);
        mCallbacks = new android.widget.ActivityChooserView.Callbacks();
        mActivityChooserContent = ((android.widget.LinearLayout) (findViewById(R.id.activity_chooser_view_content)));
        mActivityChooserContentBackground = mActivityChooserContent.getBackground();
        mDefaultActivityButton = ((android.widget.FrameLayout) (findViewById(R.id.default_activity_button)));
        mDefaultActivityButton.setOnClickListener(mCallbacks);
        mDefaultActivityButton.setOnLongClickListener(mCallbacks);
        mDefaultActivityButtonImage = mDefaultActivityButton.findViewById(R.id.image);
        final android.widget.FrameLayout expandButton = ((android.widget.FrameLayout) (findViewById(R.id.expand_activities_button)));
        expandButton.setOnClickListener(mCallbacks);
        expandButton.setAccessibilityDelegate(new android.view.View.AccessibilityDelegate() {
            @java.lang.Override
            public void onInitializeAccessibilityNodeInfo(android.view.View host, android.view.accessibility.AccessibilityNodeInfo info) {
                super.onInitializeAccessibilityNodeInfo(host, info);
                info.setCanOpenPopup(true);
            }
        });
        expandButton.setOnTouchListener(new android.widget.ForwardingListener(expandButton) {
            @java.lang.Override
            public com.android.internal.view.menu.ShowableListMenu getPopup() {
                return getListPopupWindow();
            }

            @java.lang.Override
            protected boolean onForwardingStarted() {
                showPopup();
                return true;
            }

            @java.lang.Override
            protected boolean onForwardingStopped() {
                dismissPopup();
                return true;
            }
        });
        mExpandActivityOverflowButton = expandButton;
        mExpandActivityOverflowButtonImage = expandButton.findViewById(R.id.image);
        mExpandActivityOverflowButtonImage.setImageDrawable(expandActivityOverflowButtonDrawable);
        mAdapter = new android.widget.ActivityChooserView.ActivityChooserViewAdapter();
        mAdapter.registerDataSetObserver(new android.database.DataSetObserver() {
            @java.lang.Override
            public void onChanged() {
                onChanged();
                updateAppearance();
            }
        });
        android.content.res.Resources resources = context.getResources();
        mListPopupMaxWidth = java.lang.Math.max(resources.getDisplayMetrics().widthPixels / 2, resources.getDimensionPixelSize(com.android.internal.R.dimen.config_prefDialogWidth));
    }

    /**
     * {@inheritDoc }
     */
    public void setActivityChooserModel(android.widget.ActivityChooserModel dataModel) {
        mAdapter.setDataModel(dataModel);
        if (isShowingPopup()) {
            dismissPopup();
            showPopup();
        }
    }

    /**
     * Sets the background for the button that expands the activity
     * overflow list.
     *
     * <strong>Note:</strong> Clients would like to set this drawable
     * as a clue about the action the chosen activity will perform. For
     * example, if a share activity is to be chosen the drawable should
     * give a clue that sharing is to be performed.
     *
     * @param drawable
     * 		The drawable.
     */
    @android.annotation.UnsupportedAppUsage
    public void setExpandActivityOverflowButtonDrawable(android.graphics.drawable.Drawable drawable) {
        mExpandActivityOverflowButtonImage.setImageDrawable(drawable);
    }

    /**
     * Sets the content description for the button that expands the activity
     * overflow list.
     *
     * description as a clue about the action performed by the button.
     * For example, if a share activity is to be chosen the content
     * description should be something like "Share with".
     *
     * @param resourceId
     * 		The content description resource id.
     */
    public void setExpandActivityOverflowButtonContentDescription(@android.annotation.StringRes
    int resourceId) {
        java.lang.CharSequence contentDescription = mContext.getString(resourceId);
        mExpandActivityOverflowButtonImage.setContentDescription(contentDescription);
    }

    /**
     * Set the provider hosting this view, if applicable.
     *
     * @unknown Internal use only
     */
    public void setProvider(android.view.ActionProvider provider) {
        mProvider = provider;
    }

    /**
     * Shows the popup window with activities.
     *
     * @return True if the popup was shown, false if already showing.
     */
    public boolean showPopup() {
        if (isShowingPopup() || (!mIsAttachedToWindow)) {
            return false;
        }
        mIsSelectingDefaultActivity = false;
        showPopupUnchecked(mInitialActivityCount);
        return true;
    }

    /**
     * Shows the popup no matter if it was already showing.
     *
     * @param maxActivityCount
     * 		The max number of activities to display.
     */
    private void showPopupUnchecked(int maxActivityCount) {
        if (mAdapter.getDataModel() == null) {
            throw new java.lang.IllegalStateException("No data model. Did you call #setDataModel?");
        }
        getViewTreeObserver().addOnGlobalLayoutListener(mOnGlobalLayoutListener);
        final boolean defaultActivityButtonShown = mDefaultActivityButton.getVisibility() == android.view.View.VISIBLE;
        final int activityCount = mAdapter.getActivityCount();
        final int maxActivityCountOffset = (defaultActivityButtonShown) ? 1 : 0;
        if ((maxActivityCount != android.widget.ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED) && (activityCount > (maxActivityCount + maxActivityCountOffset))) {
            mAdapter.setShowFooterView(true);
            mAdapter.setMaxActivityCount(maxActivityCount - 1);
        } else {
            mAdapter.setShowFooterView(false);
            mAdapter.setMaxActivityCount(maxActivityCount);
        }
        android.widget.ListPopupWindow popupWindow = getListPopupWindow();
        if (!popupWindow.isShowing()) {
            if (mIsSelectingDefaultActivity || (!defaultActivityButtonShown)) {
                mAdapter.setShowDefaultActivity(true, defaultActivityButtonShown);
            } else {
                mAdapter.setShowDefaultActivity(false, false);
            }
            final int contentWidth = java.lang.Math.min(mAdapter.measureContentWidth(), mListPopupMaxWidth);
            popupWindow.setContentWidth(contentWidth);
            popupWindow.show();
            if (mProvider != null) {
                mProvider.subUiVisibilityChanged(true);
            }
            popupWindow.getListView().setContentDescription(mContext.getString(R.string.activitychooserview_choose_application));
            popupWindow.getListView().setSelector(new android.graphics.drawable.ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
    }

    /**
     * Dismisses the popup window with activities.
     *
     * @return True if dismissed, false if already dismissed.
     */
    public boolean dismissPopup() {
        if (isShowingPopup()) {
            getListPopupWindow().dismiss();
            android.view.ViewTreeObserver viewTreeObserver = getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
            }
        }
        return true;
    }

    /**
     * Gets whether the popup window with activities is shown.
     *
     * @return True if the popup is shown.
     */
    public boolean isShowingPopup() {
        return getListPopupWindow().isShowing();
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        android.widget.ActivityChooserModel dataModel = mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.registerObserver(mModelDataSetOberver);
        }
        mIsAttachedToWindow = true;
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        android.widget.ActivityChooserModel dataModel = mAdapter.getDataModel();
        if (dataModel != null) {
            dataModel.unregisterObserver(mModelDataSetOberver);
        }
        android.view.ViewTreeObserver viewTreeObserver = getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener);
        }
        if (isShowingPopup()) {
            dismissPopup();
        }
        mIsAttachedToWindow = false;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        android.view.View child = mActivityChooserContent;
        // If the default action is not visible we want to be as tall as the
        // ActionBar so if this widget is used in the latter it will look as
        // a normal action button.
        if (mDefaultActivityButton.getVisibility() != android.view.View.VISIBLE) {
            heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(android.view.View.MeasureSpec.getSize(heightMeasureSpec), android.view.View.MeasureSpec.EXACTLY);
        }
        measureChild(child, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(child.getMeasuredWidth(), child.getMeasuredHeight());
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mActivityChooserContent.layout(0, 0, right - left, bottom - top);
        if (!isShowingPopup()) {
            dismissPopup();
        }
    }

    public android.widget.ActivityChooserModel getDataModel() {
        return mAdapter.getDataModel();
    }

    /**
     * Sets a listener to receive a callback when the popup is dismissed.
     *
     * @param listener
     * 		The listener to be notified.
     */
    public void setOnDismissListener(android.widget.PopupWindow.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    /**
     * Sets the initial count of items shown in the activities popup
     * i.e. the items before the popup is expanded. This is an upper
     * bound since it is not guaranteed that such number of intent
     * handlers exist.
     *
     * @param itemCount
     * 		The initial popup item count.
     */
    public void setInitialActivityCount(int itemCount) {
        mInitialActivityCount = itemCount;
    }

    /**
     * Sets a content description of the default action button. This
     * resource should be a string taking one formatting argument and
     * will be used for formatting the content description of the button
     * dynamically as the default target changes. For example, a resource
     * pointing to the string "share with %1$s" will result in a content
     * description "share with Bluetooth" for the Bluetooth activity.
     *
     * @param resourceId
     * 		The resource id.
     */
    public void setDefaultActionButtonContentDescription(@android.annotation.StringRes
    int resourceId) {
        mDefaultActionButtonContentDescription = resourceId;
    }

    /**
     * Gets the list popup window which is lazily initialized.
     *
     * @return The popup.
     */
    private android.widget.ListPopupWindow getListPopupWindow() {
        if (mListPopupWindow == null) {
            mListPopupWindow = new android.widget.ListPopupWindow(getContext());
            mListPopupWindow.setAdapter(mAdapter);
            mListPopupWindow.setAnchorView(this);
            mListPopupWindow.setModal(true);
            mListPopupWindow.setOnItemClickListener(mCallbacks);
            mListPopupWindow.setOnDismissListener(mCallbacks);
        }
        return mListPopupWindow;
    }

    /**
     * Updates the buttons state.
     */
    private void updateAppearance() {
        // Expand overflow button.
        if (mAdapter.getCount() > 0) {
            mExpandActivityOverflowButton.setEnabled(true);
        } else {
            mExpandActivityOverflowButton.setEnabled(false);
        }
        // Default activity button.
        final int activityCount = mAdapter.getActivityCount();
        final int historySize = mAdapter.getHistorySize();
        if ((activityCount == 1) || ((activityCount > 1) && (historySize > 0))) {
            mDefaultActivityButton.setVisibility(android.view.View.VISIBLE);
            android.content.pm.ResolveInfo activity = mAdapter.getDefaultActivity();
            android.content.pm.PackageManager packageManager = mContext.getPackageManager();
            mDefaultActivityButtonImage.setImageDrawable(activity.loadIcon(packageManager));
            if (mDefaultActionButtonContentDescription != 0) {
                java.lang.CharSequence label = activity.loadLabel(packageManager);
                java.lang.String contentDescription = mContext.getString(mDefaultActionButtonContentDescription, label);
                mDefaultActivityButton.setContentDescription(contentDescription);
            }
        } else {
            mDefaultActivityButton.setVisibility(android.view.View.GONE);
        }
        // Activity chooser content.
        if (mDefaultActivityButton.getVisibility() == android.view.View.VISIBLE) {
            mActivityChooserContent.setBackground(mActivityChooserContentBackground);
        } else {
            mActivityChooserContent.setBackground(null);
        }
    }

    /**
     * Interface implementation to avoid publishing them in the APIs.
     */
    private class Callbacks implements android.view.View.OnClickListener , android.view.View.OnLongClickListener , android.widget.AdapterView.OnItemClickListener , android.widget.PopupWindow.OnDismissListener {
        // AdapterView#OnItemClickListener
        public void onItemClick(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
            android.widget.ActivityChooserView.ActivityChooserViewAdapter adapter = ((android.widget.ActivityChooserView.ActivityChooserViewAdapter) (parent.getAdapter()));
            final int itemViewType = adapter.getItemViewType(position);
            switch (itemViewType) {
                case android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_FOOTER :
                    {
                        showPopupUnchecked(android.widget.ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED);
                    }
                    break;
                case android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_ACTIVITY :
                    {
                        dismissPopup();
                        if (mIsSelectingDefaultActivity) {
                            // The item at position zero is the default already.
                            if (position > 0) {
                                mAdapter.getDataModel().setDefaultActivity(position);
                            }
                        } else {
                            // If the default target is not shown in the list, the first
                            // item in the model is default action => adjust index
                            position = (mAdapter.getShowDefaultActivity()) ? position : position + 1;
                            android.content.Intent launchIntent = mAdapter.getDataModel().chooseActivity(position);
                            if (launchIntent != null) {
                                launchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                android.content.pm.ResolveInfo resolveInfo = mAdapter.getDataModel().getActivity(position);
                                startActivity(launchIntent, resolveInfo);
                            }
                        }
                    }
                    break;
                default :
                    throw new java.lang.IllegalArgumentException();
            }
        }

        // View.OnClickListener
        public void onClick(android.view.View view) {
            if (view == mDefaultActivityButton) {
                dismissPopup();
                android.content.pm.ResolveInfo defaultActivity = mAdapter.getDefaultActivity();
                final int index = mAdapter.getDataModel().getActivityIndex(defaultActivity);
                android.content.Intent launchIntent = mAdapter.getDataModel().chooseActivity(index);
                if (launchIntent != null) {
                    launchIntent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                    startActivity(launchIntent, defaultActivity);
                }
            } else
                if (view == mExpandActivityOverflowButton) {
                    mIsSelectingDefaultActivity = false;
                    showPopupUnchecked(mInitialActivityCount);
                } else {
                    throw new java.lang.IllegalArgumentException();
                }

        }

        // OnLongClickListener#onLongClick
        @java.lang.Override
        public boolean onLongClick(android.view.View view) {
            if (view == mDefaultActivityButton) {
                if (mAdapter.getCount() > 0) {
                    mIsSelectingDefaultActivity = true;
                    showPopupUnchecked(mInitialActivityCount);
                }
            } else {
                throw new java.lang.IllegalArgumentException();
            }
            return true;
        }

        // PopUpWindow.OnDismissListener#onDismiss
        public void onDismiss() {
            notifyOnDismissListener();
            if (mProvider != null) {
                mProvider.subUiVisibilityChanged(false);
            }
        }

        private void notifyOnDismissListener() {
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss();
            }
        }

        private void startActivity(android.content.Intent intent, android.content.pm.ResolveInfo resolveInfo) {
            try {
                mContext.startActivity(intent);
            } catch (java.lang.RuntimeException re) {
                java.lang.CharSequence appLabel = resolveInfo.loadLabel(mContext.getPackageManager());
                java.lang.String message = mContext.getString(R.string.activitychooserview_choose_application_error, appLabel);
                android.util.Log.e(android.widget.ActivityChooserView.LOG_TAG, message);
                android.widget.Toast.makeText(mContext, message, android.widget.Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Adapter for backing the list of activities shown in the popup.
     */
    private class ActivityChooserViewAdapter extends android.widget.BaseAdapter {
        public static final int MAX_ACTIVITY_COUNT_UNLIMITED = java.lang.Integer.MAX_VALUE;

        public static final int MAX_ACTIVITY_COUNT_DEFAULT = 4;

        private static final int ITEM_VIEW_TYPE_ACTIVITY = 0;

        private static final int ITEM_VIEW_TYPE_FOOTER = 1;

        private static final int ITEM_VIEW_TYPE_COUNT = 3;

        private android.widget.ActivityChooserModel mDataModel;

        private int mMaxActivityCount = android.widget.ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_DEFAULT;

        private boolean mShowDefaultActivity;

        private boolean mHighlightDefaultActivity;

        private boolean mShowFooterView;

        public void setDataModel(android.widget.ActivityChooserModel dataModel) {
            android.widget.ActivityChooserModel oldDataModel = mAdapter.getDataModel();
            if ((oldDataModel != null) && isShown()) {
                oldDataModel.unregisterObserver(mModelDataSetOberver);
            }
            mDataModel = dataModel;
            if ((dataModel != null) && isShown()) {
                dataModel.registerObserver(mModelDataSetOberver);
            }
            notifyDataSetChanged();
        }

        @java.lang.Override
        public int getItemViewType(int position) {
            if (mShowFooterView && (position == (getCount() - 1))) {
                return android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_FOOTER;
            } else {
                return android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_ACTIVITY;
            }
        }

        @java.lang.Override
        public int getViewTypeCount() {
            return android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_COUNT;
        }

        public int getCount() {
            int count = 0;
            int activityCount = mDataModel.getActivityCount();
            if ((!mShowDefaultActivity) && (mDataModel.getDefaultActivity() != null)) {
                activityCount--;
            }
            count = java.lang.Math.min(activityCount, mMaxActivityCount);
            if (mShowFooterView) {
                count++;
            }
            return count;
        }

        public java.lang.Object getItem(int position) {
            final int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_FOOTER :
                    return null;
                case android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_ACTIVITY :
                    if ((!mShowDefaultActivity) && (mDataModel.getDefaultActivity() != null)) {
                        position++;
                    }
                    return mDataModel.getActivity(position);
                default :
                    throw new java.lang.IllegalArgumentException();
            }
        }

        public long getItemId(int position) {
            return position;
        }

        public android.view.View getView(int position, android.view.View convertView, android.view.ViewGroup parent) {
            final int itemViewType = getItemViewType(position);
            switch (itemViewType) {
                case android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_FOOTER :
                    if ((convertView == null) || (convertView.getId() != android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_FOOTER)) {
                        convertView = android.view.LayoutInflater.from(getContext()).inflate(R.layout.activity_chooser_view_list_item, parent, false);
                        convertView.setId(android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_FOOTER);
                        android.widget.TextView titleView = convertView.findViewById(R.id.title);
                        titleView.setText(mContext.getString(R.string.activity_chooser_view_see_all));
                    }
                    return convertView;
                case android.widget.ActivityChooserView.ActivityChooserViewAdapter.ITEM_VIEW_TYPE_ACTIVITY :
                    if ((convertView == null) || (convertView.getId() != R.id.list_item)) {
                        convertView = android.view.LayoutInflater.from(getContext()).inflate(R.layout.activity_chooser_view_list_item, parent, false);
                    }
                    android.content.pm.PackageManager packageManager = mContext.getPackageManager();
                    // Set the icon
                    android.widget.ImageView iconView = convertView.findViewById(R.id.icon);
                    android.content.pm.ResolveInfo activity = ((android.content.pm.ResolveInfo) (getItem(position)));
                    iconView.setImageDrawable(activity.loadIcon(packageManager));
                    // Set the title.
                    android.widget.TextView titleView = convertView.findViewById(R.id.title);
                    titleView.setText(activity.loadLabel(packageManager));
                    // Highlight the default.
                    if ((mShowDefaultActivity && (position == 0)) && mHighlightDefaultActivity) {
                        convertView.setActivated(true);
                    } else {
                        convertView.setActivated(false);
                    }
                    return convertView;
                default :
                    throw new java.lang.IllegalArgumentException();
            }
        }

        public int measureContentWidth() {
            // The user may have specified some of the target not to be shown but we
            // want to measure all of them since after expansion they should fit.
            final int oldMaxActivityCount = mMaxActivityCount;
            mMaxActivityCount = android.widget.ActivityChooserView.ActivityChooserViewAdapter.MAX_ACTIVITY_COUNT_UNLIMITED;
            int contentWidth = 0;
            android.view.View itemView = null;
            final int widthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
            final int heightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, android.view.View.MeasureSpec.UNSPECIFIED);
            final int count = getCount();
            for (int i = 0; i < count; i++) {
                itemView = getView(i, itemView, null);
                itemView.measure(widthMeasureSpec, heightMeasureSpec);
                contentWidth = java.lang.Math.max(contentWidth, itemView.getMeasuredWidth());
            }
            mMaxActivityCount = oldMaxActivityCount;
            return contentWidth;
        }

        public void setMaxActivityCount(int maxActivityCount) {
            if (mMaxActivityCount != maxActivityCount) {
                mMaxActivityCount = maxActivityCount;
                notifyDataSetChanged();
            }
        }

        public android.content.pm.ResolveInfo getDefaultActivity() {
            return mDataModel.getDefaultActivity();
        }

        public void setShowFooterView(boolean showFooterView) {
            if (mShowFooterView != showFooterView) {
                mShowFooterView = showFooterView;
                notifyDataSetChanged();
            }
        }

        public int getActivityCount() {
            return mDataModel.getActivityCount();
        }

        public int getHistorySize() {
            return mDataModel.getHistorySize();
        }

        public android.widget.ActivityChooserModel getDataModel() {
            return mDataModel;
        }

        public void setShowDefaultActivity(boolean showDefaultActivity, boolean highlightDefaultActivity) {
            if ((mShowDefaultActivity != showDefaultActivity) || (mHighlightDefaultActivity != highlightDefaultActivity)) {
                mShowDefaultActivity = showDefaultActivity;
                mHighlightDefaultActivity = highlightDefaultActivity;
                notifyDataSetChanged();
            }
        }

        public boolean getShowDefaultActivity() {
            return mShowDefaultActivity;
        }
    }
}

