/**
 * Copyright (C) 2013 The Android Open Source Project
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


/**
 * This class implements the route controller dialog for {@link MediaRouter}.
 * <p>
 * This dialog allows the user to control or disconnect from the currently selected route.
 * </p>
 *
 * @see MediaRouteButton
 * @see MediaRouteActionProvider
 */
public class MediaRouteControllerDialog extends android.support.v7.app.AlertDialog {
    // Tags should be less than 24 characters long (see docs for android.util.Log.isLoggable())
    static final java.lang.String TAG = "MediaRouteCtrlDialog";

    static final boolean DEBUG = android.util.Log.isLoggable(android.support.v7.app.MediaRouteControllerDialog.TAG, android.util.Log.DEBUG);

    // Time to wait before updating the volume when the user lets go of the seek bar
    // to allow the route provider time to propagate the change and publish a new
    // route descriptor.
    static final int VOLUME_UPDATE_DELAY_MILLIS = 500;

    static final int CONNECTION_TIMEOUT_MILLIS = ((int) (java.util.concurrent.TimeUnit.SECONDS.toMillis(30L)));

    private static final int BUTTON_NEUTRAL_RES_ID = android.R.id.button3;

    static final int BUTTON_DISCONNECT_RES_ID = android.R.id.button2;

    static final int BUTTON_STOP_RES_ID = android.R.id.button1;

    final android.support.v7.media.MediaRouter mRouter;

    private final android.support.v7.app.MediaRouteControllerDialog.MediaRouterCallback mCallback;

    final android.support.v7.media.MediaRouter.RouteInfo mRoute;

    android.content.Context mContext;

    private boolean mCreated;

    private boolean mAttachedToWindow;

    private int mDialogContentWidth;

    private android.view.View mCustomControlView;

    private android.widget.Button mDisconnectButton;

    private android.widget.Button mStopCastingButton;

    private android.widget.ImageButton mPlayPauseButton;

    private android.widget.ImageButton mCloseButton;

    private android.support.v7.app.MediaRouteExpandCollapseButton mGroupExpandCollapseButton;

    private android.widget.FrameLayout mExpandableAreaLayout;

    private android.widget.LinearLayout mDialogAreaLayout;

    android.widget.FrameLayout mDefaultControlLayout;

    private android.widget.FrameLayout mCustomControlLayout;

    private android.widget.ImageView mArtView;

    private android.widget.TextView mTitleView;

    private android.widget.TextView mSubtitleView;

    private android.widget.TextView mRouteNameTextView;

    private boolean mVolumeControlEnabled = true;

    // Layout for media controllers including play/pause button and the main volume slider.
    private android.widget.LinearLayout mMediaMainControlLayout;

    private android.widget.RelativeLayout mPlaybackControlLayout;

    private android.widget.LinearLayout mVolumeControlLayout;

    private android.view.View mDividerView;

    android.support.v7.app.OverlayListView mVolumeGroupList;

    android.support.v7.app.MediaRouteControllerDialog.VolumeGroupAdapter mVolumeGroupAdapter;

    private java.util.List<android.support.v7.media.MediaRouter.RouteInfo> mGroupMemberRoutes;

    java.util.Set<android.support.v7.media.MediaRouter.RouteInfo> mGroupMemberRoutesAdded;

    private java.util.Set<android.support.v7.media.MediaRouter.RouteInfo> mGroupMemberRoutesRemoved;

    java.util.Set<android.support.v7.media.MediaRouter.RouteInfo> mGroupMemberRoutesAnimatingWithBitmap;

    android.widget.SeekBar mVolumeSlider;

    android.support.v7.app.MediaRouteControllerDialog.VolumeChangeListener mVolumeChangeListener;

    android.support.v7.media.MediaRouter.RouteInfo mRouteInVolumeSliderTouched;

    private int mVolumeGroupListItemIconSize;

    private int mVolumeGroupListItemHeight;

    private int mVolumeGroupListMaxHeight;

    private final int mVolumeGroupListPaddingTop;

    java.util.Map<android.support.v7.media.MediaRouter.RouteInfo, android.widget.SeekBar> mVolumeSliderMap;

    android.support.v4.media.session.MediaControllerCompat mMediaController;

    android.support.v7.app.MediaRouteControllerDialog.MediaControllerCallback mControllerCallback;

    android.support.v4.media.session.PlaybackStateCompat mState;

    android.support.v4.media.MediaDescriptionCompat mDescription;

    android.support.v7.app.MediaRouteControllerDialog.FetchArtTask mFetchArtTask;

    android.graphics.Bitmap mArtIconBitmap;

    android.net.Uri mArtIconUri;

    boolean mArtIconIsLoaded;

    android.graphics.Bitmap mArtIconLoadedBitmap;

    int mArtIconBackgroundColor;

    boolean mHasPendingUpdate;

    boolean mPendingUpdateAnimationNeeded;

    boolean mIsGroupExpanded;

    boolean mIsGroupListAnimating;

    boolean mIsGroupListAnimationPending;

    int mGroupListAnimationDurationMs;

    private int mGroupListFadeInDurationMs;

    private int mGroupListFadeOutDurationMs;

    private android.view.animation.Interpolator mInterpolator;

    private android.view.animation.Interpolator mLinearOutSlowInInterpolator;

    private android.view.animation.Interpolator mFastOutSlowInInterpolator;

    private android.view.animation.Interpolator mAccelerateDecelerateInterpolator;

    final android.view.accessibility.AccessibilityManager mAccessibilityManager;

    java.lang.Runnable mGroupListFadeInAnimation = new java.lang.Runnable() {
        @java.lang.Override
        public void run() {
            startGroupListFadeInAnimation();
        }
    };

    public MediaRouteControllerDialog(android.content.Context context) {
        this(context, 0);
    }

    public MediaRouteControllerDialog(android.content.Context context, int theme) {
        super(android.support.v7.app.MediaRouterThemeHelper.createThemedContext(context, android.support.v7.app.MediaRouterThemeHelper.getAlertDialogResolvedTheme(context, theme)), theme);
        mContext = getContext();
        mControllerCallback = new android.support.v7.app.MediaRouteControllerDialog.MediaControllerCallback();
        mRouter = android.support.v7.media.MediaRouter.getInstance(mContext);
        mCallback = new android.support.v7.app.MediaRouteControllerDialog.MediaRouterCallback();
        mRoute = mRouter.getSelectedRoute();
        setMediaSession(mRouter.getMediaSessionToken());
        mVolumeGroupListPaddingTop = mContext.getResources().getDimensionPixelSize(R.dimen.mr_controller_volume_group_list_padding_top);
        mAccessibilityManager = ((android.view.accessibility.AccessibilityManager) (mContext.getSystemService(android.content.Context.ACCESSIBILITY_SERVICE)));
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            mLinearOutSlowInInterpolator = android.view.animation.AnimationUtils.loadInterpolator(context, R.interpolator.mr_linear_out_slow_in);
            mFastOutSlowInInterpolator = android.view.animation.AnimationUtils.loadInterpolator(context, R.interpolator.mr_fast_out_slow_in);
        }
        mAccelerateDecelerateInterpolator = new android.view.animation.AccelerateDecelerateInterpolator();
    }

    /**
     * Gets the route that this dialog is controlling.
     */
    public android.support.v7.media.MediaRouter.RouteInfo getRoute() {
        return mRoute;
    }

    private android.support.v7.media.MediaRouter.RouteGroup getGroup() {
        if (mRoute instanceof android.support.v7.media.MediaRouter.RouteGroup) {
            return ((android.support.v7.media.MediaRouter.RouteGroup) (mRoute));
        }
        return null;
    }

    /**
     * Provides the subclass an opportunity to create a view that will replace the default media
     * controls for the currently playing content.
     *
     * @param savedInstanceState
     * 		The dialog's saved instance state.
     * @return The media control view, or null if none.
     */
    public android.view.View onCreateMediaControlView(android.os.Bundle savedInstanceState) {
        return null;
    }

    /**
     * Gets the media control view that was created by {@link #onCreateMediaControlView(Bundle)}.
     *
     * @return The media control view, or null if none.
     */
    public android.view.View getMediaControlView() {
        return mCustomControlView;
    }

    /**
     * Sets whether to enable the volume slider and volume control using the volume keys
     * when the route supports it.
     * <p>
     * The default value is true.
     * </p>
     */
    public void setVolumeControlEnabled(boolean enable) {
        if (mVolumeControlEnabled != enable) {
            mVolumeControlEnabled = enable;
            if (mCreated) {
                update(false);
            }
        }
    }

    /**
     * Returns whether to enable the volume slider and volume control using the volume keys
     * when the route supports it.
     */
    public boolean isVolumeControlEnabled() {
        return mVolumeControlEnabled;
    }

    /**
     * Set the session to use for metadata and transport controls. The dialog
     * will listen to changes on this session and update the UI automatically in
     * response to changes.
     *
     * @param sessionToken
     * 		The token for the session to use.
     */
    private void setMediaSession(android.support.v4.media.session.MediaSessionCompat.Token sessionToken) {
        if (mMediaController != null) {
            mMediaController.unregisterCallback(mControllerCallback);
            mMediaController = null;
        }
        if (sessionToken == null) {
            return;
        }
        if (!mAttachedToWindow) {
            return;
        }
        try {
            mMediaController = new android.support.v4.media.session.MediaControllerCompat(mContext, sessionToken);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.support.v7.app.MediaRouteControllerDialog.TAG, "Error creating media controller in setMediaSession.", e);
        }
        if (mMediaController != null) {
            mMediaController.registerCallback(mControllerCallback);
        }
        android.support.v4.media.MediaMetadataCompat metadata = (mMediaController == null) ? null : mMediaController.getMetadata();
        mDescription = (metadata == null) ? null : metadata.getDescription();
        mState = (mMediaController == null) ? null : mMediaController.getPlaybackState();
        updateArtIconIfNeeded();
        update(false);
    }

    /**
     * Gets the session to use for metadata and transport controls.
     *
     * @return The token for the session to use or null if none.
     */
    public android.support.v4.media.session.MediaSessionCompat.Token getMediaSession() {
        return mMediaController == null ? null : mMediaController.getSessionToken();
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setContentView(R.layout.mr_controller_material_dialog_b);
        // Remove the neutral button.
        findViewById(android.support.v7.app.MediaRouteControllerDialog.BUTTON_NEUTRAL_RES_ID).setVisibility(android.view.View.GONE);
        android.support.v7.app.MediaRouteControllerDialog.ClickListener listener = new android.support.v7.app.MediaRouteControllerDialog.ClickListener();
        mExpandableAreaLayout = ((android.widget.FrameLayout) (findViewById(R.id.mr_expandable_area)));
        mExpandableAreaLayout.setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                dismiss();
            }
        });
        mDialogAreaLayout = ((android.widget.LinearLayout) (findViewById(R.id.mr_dialog_area)));
        mDialogAreaLayout.setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                // Eat unhandled touch events.
            }
        });
        int color = android.support.v7.app.MediaRouterThemeHelper.getButtonTextColor(mContext);
        mDisconnectButton = ((android.widget.Button) (findViewById(android.support.v7.app.MediaRouteControllerDialog.BUTTON_DISCONNECT_RES_ID)));
        mDisconnectButton.setText(R.string.mr_controller_disconnect);
        mDisconnectButton.setTextColor(color);
        mDisconnectButton.setOnClickListener(listener);
        mStopCastingButton = ((android.widget.Button) (findViewById(android.support.v7.app.MediaRouteControllerDialog.BUTTON_STOP_RES_ID)));
        mStopCastingButton.setText(R.string.mr_controller_stop);
        mStopCastingButton.setTextColor(color);
        mStopCastingButton.setOnClickListener(listener);
        mRouteNameTextView = ((android.widget.TextView) (findViewById(R.id.mr_name)));
        mCloseButton = ((android.widget.ImageButton) (findViewById(R.id.mr_close)));
        mCloseButton.setOnClickListener(listener);
        mCustomControlLayout = ((android.widget.FrameLayout) (findViewById(R.id.mr_custom_control)));
        mDefaultControlLayout = ((android.widget.FrameLayout) (findViewById(R.id.mr_default_control)));
        // Start the session activity when a content item (album art, title or subtitle) is clicked.
        android.view.View.OnClickListener onClickListener = new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                if (mMediaController != null) {
                    android.app.PendingIntent pi = mMediaController.getSessionActivity();
                    if (pi != null) {
                        try {
                            pi.send();
                            dismiss();
                        } catch (android.app.PendingIntent.CanceledException e) {
                            android.util.Log.e(android.support.v7.app.MediaRouteControllerDialog.TAG, pi + " was not sent, it had been canceled.");
                        }
                    }
                }
            }
        };
        mArtView = ((android.widget.ImageView) (findViewById(R.id.mr_art)));
        mArtView.setOnClickListener(onClickListener);
        findViewById(R.id.mr_control_title_container).setOnClickListener(onClickListener);
        mMediaMainControlLayout = ((android.widget.LinearLayout) (findViewById(R.id.mr_media_main_control)));
        mDividerView = findViewById(R.id.mr_control_divider);
        mPlaybackControlLayout = ((android.widget.RelativeLayout) (findViewById(R.id.mr_playback_control)));
        mTitleView = ((android.widget.TextView) (findViewById(R.id.mr_control_title)));
        mSubtitleView = ((android.widget.TextView) (findViewById(R.id.mr_control_subtitle)));
        mPlayPauseButton = ((android.widget.ImageButton) (findViewById(R.id.mr_control_play_pause)));
        mPlayPauseButton.setOnClickListener(listener);
        mVolumeControlLayout = ((android.widget.LinearLayout) (findViewById(R.id.mr_volume_control)));
        mVolumeControlLayout.setVisibility(android.view.View.GONE);
        mVolumeSlider = ((android.widget.SeekBar) (findViewById(R.id.mr_volume_slider)));
        mVolumeSlider.setTag(mRoute);
        mVolumeChangeListener = new android.support.v7.app.MediaRouteControllerDialog.VolumeChangeListener();
        mVolumeSlider.setOnSeekBarChangeListener(mVolumeChangeListener);
        mVolumeGroupList = ((android.support.v7.app.OverlayListView) (findViewById(R.id.mr_volume_group_list)));
        mGroupMemberRoutes = new java.util.ArrayList<android.support.v7.media.MediaRouter.RouteInfo>();
        mVolumeGroupAdapter = new android.support.v7.app.MediaRouteControllerDialog.VolumeGroupAdapter(mContext, mGroupMemberRoutes);
        mVolumeGroupList.setAdapter(mVolumeGroupAdapter);
        mGroupMemberRoutesAnimatingWithBitmap = new java.util.HashSet<>();
        android.support.v7.app.MediaRouterThemeHelper.setMediaControlsBackgroundColor(mContext, mMediaMainControlLayout, mVolumeGroupList, getGroup() != null);
        android.support.v7.app.MediaRouterThemeHelper.setVolumeSliderColor(mContext, ((android.support.v7.app.MediaRouteVolumeSlider) (mVolumeSlider)), mMediaMainControlLayout);
        mVolumeSliderMap = new java.util.HashMap<>();
        mVolumeSliderMap.put(mRoute, mVolumeSlider);
        mGroupExpandCollapseButton = ((android.support.v7.app.MediaRouteExpandCollapseButton) (findViewById(R.id.mr_group_expand_collapse)));
        mGroupExpandCollapseButton.setOnClickListener(new android.view.View.OnClickListener() {
            @java.lang.Override
            public void onClick(android.view.View v) {
                mIsGroupExpanded = !mIsGroupExpanded;
                if (mIsGroupExpanded) {
                    mVolumeGroupList.setVisibility(android.view.View.VISIBLE);
                }
                loadInterpolator();
                updateLayoutHeight(true);
            }
        });
        loadInterpolator();
        mGroupListAnimationDurationMs = mContext.getResources().getInteger(R.integer.mr_controller_volume_group_list_animation_duration_ms);
        mGroupListFadeInDurationMs = mContext.getResources().getInteger(R.integer.mr_controller_volume_group_list_fade_in_duration_ms);
        mGroupListFadeOutDurationMs = mContext.getResources().getInteger(R.integer.mr_controller_volume_group_list_fade_out_duration_ms);
        mCustomControlView = onCreateMediaControlView(savedInstanceState);
        if (mCustomControlView != null) {
            mCustomControlLayout.addView(mCustomControlView);
            mCustomControlLayout.setVisibility(android.view.View.VISIBLE);
        }
        mCreated = true;
        updateLayout();
    }

    /**
     * Sets the width of the dialog. Also called when configuration changes.
     */
    void updateLayout() {
        int width = android.support.v7.app.MediaRouteDialogHelper.getDialogWidth(mContext);
        getWindow().setLayout(width, android.view.ViewGroup.LayoutParams.WRAP_CONTENT);
        android.view.View decorView = getWindow().getDecorView();
        mDialogContentWidth = (width - decorView.getPaddingLeft()) - decorView.getPaddingRight();
        android.content.res.Resources res = mContext.getResources();
        mVolumeGroupListItemIconSize = res.getDimensionPixelSize(R.dimen.mr_controller_volume_group_list_item_icon_size);
        mVolumeGroupListItemHeight = res.getDimensionPixelSize(R.dimen.mr_controller_volume_group_list_item_height);
        mVolumeGroupListMaxHeight = res.getDimensionPixelSize(R.dimen.mr_controller_volume_group_list_max_height);
        // Fetch art icons again for layout changes to resize it accordingly
        mArtIconBitmap = null;
        mArtIconUri = null;
        updateArtIconIfNeeded();
        update(false);
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        mRouter.addCallback(android.support.v7.media.MediaRouteSelector.EMPTY, mCallback, android.support.v7.media.MediaRouter.CALLBACK_FLAG_UNFILTERED_EVENTS);
        setMediaSession(mRouter.getMediaSessionToken());
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        mRouter.removeCallback(mCallback);
        setMediaSession(null);
        mAttachedToWindow = false;
        super.onDetachedFromWindow();
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_UP)) {
            mRoute.requestUpdateVolume(keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN ? -1 : 1);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @java.lang.Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_VOLUME_DOWN) || (keyCode == android.view.KeyEvent.KEYCODE_VOLUME_UP)) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    void update(boolean animate) {
        // Defer dialog updates if a user is adjusting a volume in the list
        if (mRouteInVolumeSliderTouched != null) {
            mHasPendingUpdate = true;
            mPendingUpdateAnimationNeeded |= animate;
            return;
        }
        mHasPendingUpdate = false;
        mPendingUpdateAnimationNeeded = false;
        if ((!mRoute.isSelected()) || mRoute.isDefaultOrBluetooth()) {
            dismiss();
            return;
        }
        if (!mCreated) {
            return;
        }
        mRouteNameTextView.setText(mRoute.getName());
        mDisconnectButton.setVisibility(mRoute.canDisconnect() ? android.view.View.VISIBLE : android.view.View.GONE);
        if ((mCustomControlView == null) && mArtIconIsLoaded) {
            mArtView.setImageBitmap(mArtIconLoadedBitmap);
            mArtView.setBackgroundColor(mArtIconBackgroundColor);
            clearLoadedBitmap();
        }
        updateVolumeControlLayout();
        updatePlaybackControlLayout();
        updateLayoutHeight(animate);
    }

    private boolean canShowPlaybackControlLayout() {
        return (mCustomControlView == null) && ((mDescription != null) || (mState != null));
    }

    /**
     * Returns the height of main media controller which includes playback control and master
     * volume control.
     */
    private int getMainControllerHeight(boolean showPlaybackControl) {
        int height = 0;
        if (showPlaybackControl || (mVolumeControlLayout.getVisibility() == android.view.View.VISIBLE)) {
            height += mMediaMainControlLayout.getPaddingTop() + mMediaMainControlLayout.getPaddingBottom();
            if (showPlaybackControl) {
                height += mPlaybackControlLayout.getMeasuredHeight();
            }
            if (mVolumeControlLayout.getVisibility() == android.view.View.VISIBLE) {
                height += mVolumeControlLayout.getMeasuredHeight();
            }
            if (showPlaybackControl && (mVolumeControlLayout.getVisibility() == android.view.View.VISIBLE)) {
                height += mDividerView.getMeasuredHeight();
            }
        }
        return height;
    }

    private void updateMediaControlVisibility(boolean canShowPlaybackControlLayout) {
        // TODO: Update the top and bottom padding of the control layout according to the display
        // height.
        mDividerView.setVisibility((mVolumeControlLayout.getVisibility() == android.view.View.VISIBLE) && canShowPlaybackControlLayout ? android.view.View.VISIBLE : android.view.View.GONE);
        mMediaMainControlLayout.setVisibility((mVolumeControlLayout.getVisibility() == android.view.View.GONE) && (!canShowPlaybackControlLayout) ? android.view.View.GONE : android.view.View.VISIBLE);
    }

    void updateLayoutHeight(final boolean animate) {
        // We need to defer the update until the first layout has occurred, as we don't yet know the
        // overall visible display size in which the window this view is attached to has been
        // positioned in.
        mDefaultControlLayout.requestLayout();
        android.view.ViewTreeObserver observer = mDefaultControlLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
            @java.lang.Override
            public void onGlobalLayout() {
                mDefaultControlLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (mIsGroupListAnimating) {
                    mIsGroupListAnimationPending = true;
                } else {
                    updateLayoutHeightInternal(animate);
                }
            }
        });
    }

    /**
     * Updates the height of views and hide artwork or metadata if space is limited.
     */
    void updateLayoutHeightInternal(boolean animate) {
        // Measure the size of widgets and get the height of main components.
        int oldHeight = android.support.v7.app.MediaRouteControllerDialog.getLayoutHeight(mMediaMainControlLayout);
        android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(mMediaMainControlLayout, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        updateMediaControlVisibility(canShowPlaybackControlLayout());
        android.view.View decorView = getWindow().getDecorView();
        decorView.measure(android.view.View.MeasureSpec.makeMeasureSpec(getWindow().getAttributes().width, android.view.View.MeasureSpec.EXACTLY), android.view.View.MeasureSpec.UNSPECIFIED);
        android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(mMediaMainControlLayout, oldHeight);
        int artViewHeight = 0;
        if ((mCustomControlView == null) && (mArtView.getDrawable() instanceof android.graphics.drawable.BitmapDrawable)) {
            android.graphics.Bitmap art = ((android.graphics.drawable.BitmapDrawable) (mArtView.getDrawable())).getBitmap();
            if (art != null) {
                artViewHeight = getDesiredArtHeight(art.getWidth(), art.getHeight());
                mArtView.setScaleType(art.getWidth() >= art.getHeight() ? android.widget.ImageView.ScaleType.FIT_XY : android.widget.ImageView.ScaleType.FIT_CENTER);
            }
        }
        int mainControllerHeight = getMainControllerHeight(canShowPlaybackControlLayout());
        int volumeGroupListCount = mGroupMemberRoutes.size();
        // Scale down volume group list items in landscape mode.
        int expandedGroupListHeight = (getGroup() == null) ? 0 : mVolumeGroupListItemHeight * getGroup().getRoutes().size();
        if (volumeGroupListCount > 0) {
            expandedGroupListHeight += mVolumeGroupListPaddingTop;
        }
        expandedGroupListHeight = java.lang.Math.min(expandedGroupListHeight, mVolumeGroupListMaxHeight);
        int visibleGroupListHeight = (mIsGroupExpanded) ? expandedGroupListHeight : 0;
        int desiredControlLayoutHeight = java.lang.Math.max(artViewHeight, visibleGroupListHeight) + mainControllerHeight;
        android.graphics.Rect visibleRect = new android.graphics.Rect();
        decorView.getWindowVisibleDisplayFrame(visibleRect);
        // Height of non-control views in decor view.
        // This includes title bar, button bar, and dialog's vertical padding which should be
        // always shown.
        int nonControlViewHeight = mDialogAreaLayout.getMeasuredHeight() - mDefaultControlLayout.getMeasuredHeight();
        // Maximum allowed height for controls to fit screen.
        int maximumControlViewHeight = visibleRect.height() - nonControlViewHeight;
        // Show artwork if it fits the screen.
        if (((mCustomControlView == null) && (artViewHeight > 0)) && (desiredControlLayoutHeight <= maximumControlViewHeight)) {
            mArtView.setVisibility(android.view.View.VISIBLE);
            android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(mArtView, artViewHeight);
        } else {
            if ((android.support.v7.app.MediaRouteControllerDialog.getLayoutHeight(mVolumeGroupList) + mMediaMainControlLayout.getMeasuredHeight()) >= mDefaultControlLayout.getMeasuredHeight()) {
                mArtView.setVisibility(android.view.View.GONE);
            }
            artViewHeight = 0;
            desiredControlLayoutHeight = visibleGroupListHeight + mainControllerHeight;
        }
        // Show the playback control if it fits the screen.
        if (canShowPlaybackControlLayout() && (desiredControlLayoutHeight <= maximumControlViewHeight)) {
            mPlaybackControlLayout.setVisibility(android.view.View.VISIBLE);
        } else {
            mPlaybackControlLayout.setVisibility(android.view.View.GONE);
        }
        updateMediaControlVisibility(mPlaybackControlLayout.getVisibility() == android.view.View.VISIBLE);
        mainControllerHeight = getMainControllerHeight(mPlaybackControlLayout.getVisibility() == android.view.View.VISIBLE);
        desiredControlLayoutHeight = java.lang.Math.max(artViewHeight, visibleGroupListHeight) + mainControllerHeight;
        // Limit the volume group list height to fit the screen.
        if (desiredControlLayoutHeight > maximumControlViewHeight) {
            visibleGroupListHeight -= desiredControlLayoutHeight - maximumControlViewHeight;
            desiredControlLayoutHeight = maximumControlViewHeight;
        }
        // Update the layouts with the computed heights.
        mMediaMainControlLayout.clearAnimation();
        mVolumeGroupList.clearAnimation();
        mDefaultControlLayout.clearAnimation();
        if (animate) {
            animateLayoutHeight(mMediaMainControlLayout, mainControllerHeight);
            animateLayoutHeight(mVolumeGroupList, visibleGroupListHeight);
            animateLayoutHeight(mDefaultControlLayout, desiredControlLayoutHeight);
        } else {
            android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(mMediaMainControlLayout, mainControllerHeight);
            android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(mVolumeGroupList, visibleGroupListHeight);
            android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(mDefaultControlLayout, desiredControlLayoutHeight);
        }
        // Maximize the window size with a transparent layout in advance for smooth animation.
        android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(mExpandableAreaLayout, visibleRect.height());
        rebuildVolumeGroupList(animate);
    }

    void updateVolumeGroupItemHeight(android.view.View item) {
        android.widget.LinearLayout container = ((android.widget.LinearLayout) (item.findViewById(R.id.volume_item_container)));
        android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(container, mVolumeGroupListItemHeight);
        android.view.View icon = item.findViewById(R.id.mr_volume_item_icon);
        android.view.ViewGroup.LayoutParams lp = icon.getLayoutParams();
        lp.width = mVolumeGroupListItemIconSize;
        lp.height = mVolumeGroupListItemIconSize;
        icon.setLayoutParams(lp);
    }

    private void animateLayoutHeight(final android.view.View view, int targetHeight) {
        final int startValue = android.support.v7.app.MediaRouteControllerDialog.getLayoutHeight(view);
        final int endValue = targetHeight;
        android.view.animation.Animation anim = new android.view.animation.Animation() {
            @java.lang.Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                int height = startValue - ((int) ((startValue - endValue) * interpolatedTime));
                android.support.v7.app.MediaRouteControllerDialog.setLayoutHeight(view, height);
            }
        };
        anim.setDuration(mGroupListAnimationDurationMs);
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            anim.setInterpolator(mInterpolator);
        }
        view.startAnimation(anim);
    }

    void loadInterpolator() {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            mInterpolator = (mIsGroupExpanded) ? mLinearOutSlowInInterpolator : mFastOutSlowInInterpolator;
        } else {
            mInterpolator = mAccelerateDecelerateInterpolator;
        }
    }

    private void updateVolumeControlLayout() {
        if (isVolumeControlAvailable(mRoute)) {
            if (mVolumeControlLayout.getVisibility() == android.view.View.GONE) {
                mVolumeControlLayout.setVisibility(android.view.View.VISIBLE);
                mVolumeSlider.setMax(mRoute.getVolumeMax());
                mVolumeSlider.setProgress(mRoute.getVolume());
                mGroupExpandCollapseButton.setVisibility(getGroup() == null ? android.view.View.GONE : android.view.View.VISIBLE);
            }
        } else {
            mVolumeControlLayout.setVisibility(android.view.View.GONE);
        }
    }

    private void rebuildVolumeGroupList(boolean animate) {
        java.util.List<android.support.v7.media.MediaRouter.RouteInfo> routes = (getGroup() == null) ? null : getGroup().getRoutes();
        if (routes == null) {
            mGroupMemberRoutes.clear();
            mVolumeGroupAdapter.notifyDataSetChanged();
        } else
            if (android.support.v7.app.MediaRouteDialogHelper.listUnorderedEquals(mGroupMemberRoutes, routes)) {
                mVolumeGroupAdapter.notifyDataSetChanged();
            } else {
                java.util.HashMap<android.support.v7.media.MediaRouter.RouteInfo, android.graphics.Rect> previousRouteBoundMap = (animate) ? android.support.v7.app.MediaRouteDialogHelper.getItemBoundMap(mVolumeGroupList, mVolumeGroupAdapter) : null;
                java.util.HashMap<android.support.v7.media.MediaRouter.RouteInfo, android.graphics.drawable.BitmapDrawable> previousRouteBitmapMap = (animate) ? android.support.v7.app.MediaRouteDialogHelper.getItemBitmapMap(mContext, mVolumeGroupList, mVolumeGroupAdapter) : null;
                mGroupMemberRoutesAdded = android.support.v7.app.MediaRouteDialogHelper.getItemsAdded(mGroupMemberRoutes, routes);
                mGroupMemberRoutesRemoved = android.support.v7.app.MediaRouteDialogHelper.getItemsRemoved(mGroupMemberRoutes, routes);
                mGroupMemberRoutes.addAll(0, mGroupMemberRoutesAdded);
                mGroupMemberRoutes.removeAll(mGroupMemberRoutesRemoved);
                mVolumeGroupAdapter.notifyDataSetChanged();
                if ((animate && mIsGroupExpanded) && ((mGroupMemberRoutesAdded.size() + mGroupMemberRoutesRemoved.size()) > 0)) {
                    animateGroupListItems(previousRouteBoundMap, previousRouteBitmapMap);
                } else {
                    mGroupMemberRoutesAdded = null;
                    mGroupMemberRoutesRemoved = null;
                }
            }

    }

    private void animateGroupListItems(final java.util.Map<android.support.v7.media.MediaRouter.RouteInfo, android.graphics.Rect> previousRouteBoundMap, final java.util.Map<android.support.v7.media.MediaRouter.RouteInfo, android.graphics.drawable.BitmapDrawable> previousRouteBitmapMap) {
        mVolumeGroupList.setEnabled(false);
        mVolumeGroupList.requestLayout();
        mIsGroupListAnimating = true;
        android.view.ViewTreeObserver observer = mVolumeGroupList.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
            @java.lang.Override
            public void onGlobalLayout() {
                mVolumeGroupList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                animateGroupListItemsInternal(previousRouteBoundMap, previousRouteBitmapMap);
            }
        });
    }

    void animateGroupListItemsInternal(java.util.Map<android.support.v7.media.MediaRouter.RouteInfo, android.graphics.Rect> previousRouteBoundMap, java.util.Map<android.support.v7.media.MediaRouter.RouteInfo, android.graphics.drawable.BitmapDrawable> previousRouteBitmapMap) {
        if ((mGroupMemberRoutesAdded == null) || (mGroupMemberRoutesRemoved == null)) {
            return;
        }
        int groupSizeDelta = mGroupMemberRoutesAdded.size() - mGroupMemberRoutesRemoved.size();
        boolean listenerRegistered = false;
        android.view.animation.Animation.AnimationListener listener = new android.view.animation.Animation.AnimationListener() {
            @java.lang.Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                mVolumeGroupList.startAnimationAll();
                mVolumeGroupList.postDelayed(mGroupListFadeInAnimation, mGroupListAnimationDurationMs);
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
            }

            @java.lang.Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }
        };
        // Animate visible items from previous positions to current positions except routes added
        // just before. Added routes will remain hidden until translate animation finishes.
        int first = mVolumeGroupList.getFirstVisiblePosition();
        for (int i = 0; i < mVolumeGroupList.getChildCount(); ++i) {
            android.view.View view = mVolumeGroupList.getChildAt(i);
            int position = first + i;
            android.support.v7.media.MediaRouter.RouteInfo route = mVolumeGroupAdapter.getItem(position);
            android.graphics.Rect previousBounds = previousRouteBoundMap.get(route);
            int currentTop = view.getTop();
            int previousTop = (previousBounds != null) ? previousBounds.top : currentTop + (mVolumeGroupListItemHeight * groupSizeDelta);
            android.view.animation.AnimationSet animSet = new android.view.animation.AnimationSet(true);
            if ((mGroupMemberRoutesAdded != null) && mGroupMemberRoutesAdded.contains(route)) {
                previousTop = currentTop;
                android.view.animation.Animation alphaAnim = new android.view.animation.AlphaAnimation(0.0F, 0.0F);
                alphaAnim.setDuration(mGroupListFadeInDurationMs);
                animSet.addAnimation(alphaAnim);
            }
            android.view.animation.Animation translationAnim = new android.view.animation.TranslateAnimation(0, 0, previousTop - currentTop, 0);
            translationAnim.setDuration(mGroupListAnimationDurationMs);
            animSet.addAnimation(translationAnim);
            animSet.setFillAfter(true);
            animSet.setFillEnabled(true);
            animSet.setInterpolator(mInterpolator);
            if (!listenerRegistered) {
                listenerRegistered = true;
                animSet.setAnimationListener(listener);
            }
            view.clearAnimation();
            view.startAnimation(animSet);
            previousRouteBoundMap.remove(route);
            previousRouteBitmapMap.remove(route);
        }
        // If a member route doesn't exist any longer, it can be either removed or moved out of the
        // ListView layout boundary. In this case, use the previously captured bitmaps for
        // animation.
        for (java.util.Map.Entry<android.support.v7.media.MediaRouter.RouteInfo, android.graphics.drawable.BitmapDrawable> item : previousRouteBitmapMap.entrySet()) {
            final android.support.v7.media.MediaRouter.RouteInfo route = item.getKey();
            final android.graphics.drawable.BitmapDrawable bitmap = item.getValue();
            final android.graphics.Rect bounds = previousRouteBoundMap.get(route);
            android.support.v7.app.OverlayListView.OverlayObject object = null;
            if (mGroupMemberRoutesRemoved.contains(route)) {
                object = new android.support.v7.app.OverlayListView.OverlayObject(bitmap, bounds).setAlphaAnimation(1.0F, 0.0F).setDuration(mGroupListFadeOutDurationMs).setInterpolator(mInterpolator);
            } else {
                int deltaY = groupSizeDelta * mVolumeGroupListItemHeight;
                object = new android.support.v7.app.OverlayListView.OverlayObject(bitmap, bounds).setTranslateYAnimation(deltaY).setDuration(mGroupListAnimationDurationMs).setInterpolator(mInterpolator).setAnimationEndListener(new android.support.v7.app.OverlayListView.OverlayObject.OnAnimationEndListener() {
                    @java.lang.Override
                    public void onAnimationEnd() {
                        mGroupMemberRoutesAnimatingWithBitmap.remove(route);
                        mVolumeGroupAdapter.notifyDataSetChanged();
                    }
                });
                mGroupMemberRoutesAnimatingWithBitmap.add(route);
            }
            mVolumeGroupList.addOverlayObject(object);
        }
    }

    void startGroupListFadeInAnimation() {
        clearGroupListAnimation(true);
        mVolumeGroupList.requestLayout();
        android.view.ViewTreeObserver observer = mVolumeGroupList.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new android.view.ViewTreeObserver.OnGlobalLayoutListener() {
            @java.lang.Override
            public void onGlobalLayout() {
                mVolumeGroupList.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                startGroupListFadeInAnimationInternal();
            }
        });
    }

    void startGroupListFadeInAnimationInternal() {
        if ((mGroupMemberRoutesAdded != null) && (mGroupMemberRoutesAdded.size() != 0)) {
            fadeInAddedRoutes();
        } else {
            finishAnimation(true);
        }
    }

    void finishAnimation(boolean animate) {
        mGroupMemberRoutesAdded = null;
        mGroupMemberRoutesRemoved = null;
        mIsGroupListAnimating = false;
        if (mIsGroupListAnimationPending) {
            mIsGroupListAnimationPending = false;
            updateLayoutHeight(animate);
        }
        mVolumeGroupList.setEnabled(true);
    }

    private void fadeInAddedRoutes() {
        android.view.animation.Animation.AnimationListener listener = new android.view.animation.Animation.AnimationListener() {
            @java.lang.Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @java.lang.Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                finishAnimation(true);
            }

            @java.lang.Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
            }
        };
        boolean listenerRegistered = false;
        int first = mVolumeGroupList.getFirstVisiblePosition();
        for (int i = 0; i < mVolumeGroupList.getChildCount(); ++i) {
            android.view.View view = mVolumeGroupList.getChildAt(i);
            int position = first + i;
            android.support.v7.media.MediaRouter.RouteInfo route = mVolumeGroupAdapter.getItem(position);
            if (mGroupMemberRoutesAdded.contains(route)) {
                android.view.animation.Animation alphaAnim = new android.view.animation.AlphaAnimation(0.0F, 1.0F);
                alphaAnim.setDuration(mGroupListFadeInDurationMs);
                alphaAnim.setFillEnabled(true);
                alphaAnim.setFillAfter(true);
                if (!listenerRegistered) {
                    listenerRegistered = true;
                    alphaAnim.setAnimationListener(listener);
                }
                view.clearAnimation();
                view.startAnimation(alphaAnim);
            }
        }
    }

    void clearGroupListAnimation(boolean exceptAddedRoutes) {
        int first = mVolumeGroupList.getFirstVisiblePosition();
        for (int i = 0; i < mVolumeGroupList.getChildCount(); ++i) {
            android.view.View view = mVolumeGroupList.getChildAt(i);
            int position = first + i;
            android.support.v7.media.MediaRouter.RouteInfo route = mVolumeGroupAdapter.getItem(position);
            if ((exceptAddedRoutes && (mGroupMemberRoutesAdded != null)) && mGroupMemberRoutesAdded.contains(route)) {
                continue;
            }
            android.widget.LinearLayout container = ((android.widget.LinearLayout) (view.findViewById(R.id.volume_item_container)));
            container.setVisibility(android.view.View.VISIBLE);
            android.view.animation.AnimationSet animSet = new android.view.animation.AnimationSet(true);
            android.view.animation.Animation alphaAnim = new android.view.animation.AlphaAnimation(1.0F, 1.0F);
            alphaAnim.setDuration(0);
            animSet.addAnimation(alphaAnim);
            android.view.animation.Animation translationAnim = new android.view.animation.TranslateAnimation(0, 0, 0, 0);
            translationAnim.setDuration(0);
            animSet.setFillAfter(true);
            animSet.setFillEnabled(true);
            view.clearAnimation();
            view.startAnimation(animSet);
        }
        mVolumeGroupList.stopAnimationAll();
        if (!exceptAddedRoutes) {
            finishAnimation(false);
        }
    }

    private void updatePlaybackControlLayout() {
        if (canShowPlaybackControlLayout()) {
            java.lang.CharSequence title = (mDescription == null) ? null : mDescription.getTitle();
            boolean hasTitle = !android.text.TextUtils.isEmpty(title);
            java.lang.CharSequence subtitle = (mDescription == null) ? null : mDescription.getSubtitle();
            boolean hasSubtitle = !android.text.TextUtils.isEmpty(subtitle);
            boolean showTitle = false;
            boolean showSubtitle = false;
            if (mRoute.getPresentationDisplayId() != android.support.v7.media.MediaRouter.RouteInfo.PRESENTATION_DISPLAY_ID_NONE) {
                // The user is currently casting screen.
                mTitleView.setText(R.string.mr_controller_casting_screen);
                showTitle = true;
            } else
                if ((mState == null) || (mState.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_NONE)) {
                    // Show "No media selected" as we don't yet know the playback state.
                    mTitleView.setText(R.string.mr_controller_no_media_selected);
                    showTitle = true;
                } else
                    if ((!hasTitle) && (!hasSubtitle)) {
                        mTitleView.setText(R.string.mr_controller_no_info_available);
                        showTitle = true;
                    } else {
                        if (hasTitle) {
                            mTitleView.setText(title);
                            showTitle = true;
                        }
                        if (hasSubtitle) {
                            mSubtitleView.setText(subtitle);
                            showSubtitle = true;
                        }
                    }


            mTitleView.setVisibility(showTitle ? android.view.View.VISIBLE : android.view.View.GONE);
            mSubtitleView.setVisibility(showSubtitle ? android.view.View.VISIBLE : android.view.View.GONE);
            if (mState != null) {
                boolean isPlaying = (mState.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_BUFFERING) || (mState.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING);
                boolean supportsPlay = (mState.getActions() & (android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY | android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE)) != 0;
                boolean supportsPause = (mState.getActions() & (android.support.v4.media.session.PlaybackStateCompat.ACTION_PAUSE | android.support.v4.media.session.PlaybackStateCompat.ACTION_PLAY_PAUSE)) != 0;
                if (isPlaying && supportsPause) {
                    mPlayPauseButton.setVisibility(android.view.View.VISIBLE);
                    mPlayPauseButton.setImageResource(android.support.v7.app.MediaRouterThemeHelper.getThemeResource(mContext, R.attr.mediaRoutePauseDrawable));
                    mPlayPauseButton.setContentDescription(mContext.getResources().getText(R.string.mr_controller_pause));
                } else
                    if ((!isPlaying) && supportsPlay) {
                        mPlayPauseButton.setVisibility(android.view.View.VISIBLE);
                        mPlayPauseButton.setImageResource(android.support.v7.app.MediaRouterThemeHelper.getThemeResource(mContext, R.attr.mediaRoutePlayDrawable));
                        mPlayPauseButton.setContentDescription(mContext.getResources().getText(R.string.mr_controller_play));
                    } else {
                        mPlayPauseButton.setVisibility(android.view.View.GONE);
                    }

            }
        }
    }

    boolean isVolumeControlAvailable(android.support.v7.media.MediaRouter.RouteInfo route) {
        return mVolumeControlEnabled && (route.getVolumeHandling() == android.support.v7.media.MediaRouter.RouteInfo.PLAYBACK_VOLUME_VARIABLE);
    }

    private static int getLayoutHeight(android.view.View view) {
        return view.getLayoutParams().height;
    }

    static void setLayoutHeight(android.view.View view, int height) {
        android.view.ViewGroup.LayoutParams lp = view.getLayoutParams();
        lp.height = height;
        view.setLayoutParams(lp);
    }

    private static boolean uriEquals(android.net.Uri uri1, android.net.Uri uri2) {
        if ((uri1 != null) && uri1.equals(uri2)) {
            return true;
        } else
            if ((uri1 == null) && (uri2 == null)) {
                return true;
            }

        return false;
    }

    /**
     * Returns desired art height to fit into controller dialog.
     */
    int getDesiredArtHeight(int originalWidth, int originalHeight) {
        if (originalWidth >= originalHeight) {
            // For landscape art, fit width to dialog width.
            return ((int) (((((float) (mDialogContentWidth)) * originalHeight) / originalWidth) + 0.5F));
        }
        // For portrait art, fit height to 16:9 ratio case's height.
        return ((int) (((((float) (mDialogContentWidth)) * 9) / 16) + 0.5F));
    }

    void updateArtIconIfNeeded() {
        if ((mCustomControlView != null) || (!isIconChanged())) {
            return;
        }
        if (mFetchArtTask != null) {
            mFetchArtTask.cancel(true);
        }
        mFetchArtTask = new android.support.v7.app.MediaRouteControllerDialog.FetchArtTask();
        mFetchArtTask.execute();
    }

    /**
     * Clear the bitmap loaded by FetchArtTask. Will be called after the loaded bitmaps are applied
     * to artwork, or no longer valid.
     */
    void clearLoadedBitmap() {
        mArtIconIsLoaded = false;
        mArtIconLoadedBitmap = null;
        mArtIconBackgroundColor = 0;
    }

    /**
     * Returns whether a new art image is different from an original art image. Compares
     * Bitmap objects first, and then compares URIs only if bitmap is unchanged with
     * a null value.
     */
    private boolean isIconChanged() {
        android.graphics.Bitmap newBitmap = (mDescription == null) ? null : mDescription.getIconBitmap();
        android.net.Uri newUri = (mDescription == null) ? null : mDescription.getIconUri();
        android.graphics.Bitmap oldBitmap = (mFetchArtTask == null) ? mArtIconBitmap : mFetchArtTask.getIconBitmap();
        android.net.Uri oldUri = (mFetchArtTask == null) ? mArtIconUri : mFetchArtTask.getIconUri();
        if (oldBitmap != newBitmap) {
            return true;
        } else
            if ((oldBitmap == null) && (!android.support.v7.app.MediaRouteControllerDialog.uriEquals(oldUri, newUri))) {
                return true;
            }

        return false;
    }

    private final class MediaRouterCallback extends android.support.v7.media.MediaRouter.Callback {
        MediaRouterCallback() {
        }

        @java.lang.Override
        public void onRouteUnselected(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
            update(false);
        }

        @java.lang.Override
        public void onRouteChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
            update(true);
        }

        @java.lang.Override
        public void onRouteVolumeChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo route) {
            android.widget.SeekBar volumeSlider = mVolumeSliderMap.get(route);
            int volume = route.getVolume();
            if (android.support.v7.app.MediaRouteControllerDialog.DEBUG) {
                android.util.Log.d(android.support.v7.app.MediaRouteControllerDialog.TAG, "onRouteVolumeChanged(), route.getVolume:" + volume);
            }
            if ((volumeSlider != null) && (mRouteInVolumeSliderTouched != route)) {
                volumeSlider.setProgress(volume);
            }
        }
    }

    private final class MediaControllerCallback extends android.support.v4.media.session.MediaControllerCompat.Callback {
        MediaControllerCallback() {
        }

        @java.lang.Override
        public void onSessionDestroyed() {
            if (mMediaController != null) {
                mMediaController.unregisterCallback(mControllerCallback);
                mMediaController = null;
            }
        }

        @java.lang.Override
        public void onPlaybackStateChanged(android.support.v4.media.session.PlaybackStateCompat state) {
            mState = state;
            update(false);
        }

        @java.lang.Override
        public void onMetadataChanged(android.support.v4.media.MediaMetadataCompat metadata) {
            mDescription = (metadata == null) ? null : metadata.getDescription();
            updateArtIconIfNeeded();
            update(false);
        }
    }

    private final class ClickListener implements android.view.View.OnClickListener {
        ClickListener() {
        }

        @java.lang.Override
        public void onClick(android.view.View v) {
            int id = v.getId();
            if ((id == android.support.v7.app.MediaRouteControllerDialog.BUTTON_STOP_RES_ID) || (id == android.support.v7.app.MediaRouteControllerDialog.BUTTON_DISCONNECT_RES_ID)) {
                if (mRoute.isSelected()) {
                    mRouter.unselect(id == android.support.v7.app.MediaRouteControllerDialog.BUTTON_STOP_RES_ID ? android.support.v7.media.MediaRouter.UNSELECT_REASON_STOPPED : android.support.v7.media.MediaRouter.UNSELECT_REASON_DISCONNECTED);
                }
                dismiss();
            } else
                if (id == R.id.mr_control_play_pause) {
                    if ((mMediaController != null) && (mState != null)) {
                        boolean isPlaying = mState.getState() == android.support.v4.media.session.PlaybackStateCompat.STATE_PLAYING;
                        if (isPlaying) {
                            mMediaController.getTransportControls().pause();
                        } else {
                            mMediaController.getTransportControls().play();
                        }
                        // Announce the action for accessibility.
                        if ((mAccessibilityManager != null) && mAccessibilityManager.isEnabled()) {
                            android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(android.support.v4.view.accessibility.AccessibilityEventCompat.TYPE_ANNOUNCEMENT);
                            event.setPackageName(mContext.getPackageName());
                            event.setClassName(getClass().getName());
                            int resId = (isPlaying) ? R.string.mr_controller_pause : R.string.mr_controller_play;
                            event.getText().add(mContext.getString(resId));
                            mAccessibilityManager.sendAccessibilityEvent(event);
                        }
                    }
                } else
                    if (id == R.id.mr_close) {
                        dismiss();
                    }


        }
    }

    private class VolumeChangeListener implements android.widget.SeekBar.OnSeekBarChangeListener {
        private final java.lang.Runnable mStopTrackingTouch = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                if (mRouteInVolumeSliderTouched != null) {
                    mRouteInVolumeSliderTouched = null;
                    if (mHasPendingUpdate) {
                        update(mPendingUpdateAnimationNeeded);
                    }
                }
            }
        };

        VolumeChangeListener() {
        }

        @java.lang.Override
        public void onStartTrackingTouch(android.widget.SeekBar seekBar) {
            if (mRouteInVolumeSliderTouched != null) {
                mVolumeSlider.removeCallbacks(mStopTrackingTouch);
            }
            mRouteInVolumeSliderTouched = ((android.support.v7.media.MediaRouter.RouteInfo) (seekBar.getTag()));
        }

        @java.lang.Override
        public void onStopTrackingTouch(android.widget.SeekBar seekBar) {
            // Defer resetting mVolumeSliderTouched to allow the media route provider
            // a little time to settle into its new state and publish the final
            // volume update.
            mVolumeSlider.postDelayed(mStopTrackingTouch, android.support.v7.app.MediaRouteControllerDialog.VOLUME_UPDATE_DELAY_MILLIS);
        }

        @java.lang.Override
        public void onProgressChanged(android.widget.SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                android.support.v7.media.MediaRouter.RouteInfo route = ((android.support.v7.media.MediaRouter.RouteInfo) (seekBar.getTag()));
                if (android.support.v7.app.MediaRouteControllerDialog.DEBUG) {
                    android.util.Log.d(android.support.v7.app.MediaRouteControllerDialog.TAG, (("onProgressChanged(): calling " + "MediaRouter.RouteInfo.requestSetVolume(") + progress) + ")");
                }
                route.requestSetVolume(progress);
            }
        }
    }

    private class VolumeGroupAdapter extends android.widget.ArrayAdapter<android.support.v7.media.MediaRouter.RouteInfo> {
        final float mDisabledAlpha;

        public VolumeGroupAdapter(android.content.Context context, java.util.List<android.support.v7.media.MediaRouter.RouteInfo> objects) {
            super(context, 0, objects);
            mDisabledAlpha = android.support.v7.app.MediaRouterThemeHelper.getDisabledAlpha(context);
        }

        @java.lang.Override
        public android.view.View getView(final int position, android.view.View convertView, android.view.ViewGroup parent) {
            android.view.View v = convertView;
            if (v == null) {
                v = android.view.LayoutInflater.from(android.support.v7.app.MediaRouteControllerDialog.this.mContext).inflate(R.layout.mr_controller_volume_item, parent, false);
            } else {
                updateVolumeGroupItemHeight(v);
            }
            android.support.v7.media.MediaRouter.RouteInfo route = getItem(position);
            if (route != null) {
                boolean isEnabled = route.isEnabled();
                android.widget.TextView routeName = ((android.widget.TextView) (v.findViewById(R.id.mr_name)));
                routeName.setEnabled(isEnabled);
                routeName.setText(route.getName());
                android.support.v7.app.MediaRouteVolumeSlider volumeSlider = ((android.support.v7.app.MediaRouteVolumeSlider) (v.findViewById(R.id.mr_volume_slider)));
                android.support.v7.app.MediaRouterThemeHelper.setVolumeSliderColor(android.support.v7.app.MediaRouteControllerDialog.this.mContext, volumeSlider, mVolumeGroupList);
                volumeSlider.setTag(route);
                mVolumeSliderMap.put(route, volumeSlider);
                volumeSlider.setHideThumb(!isEnabled);
                volumeSlider.setEnabled(isEnabled);
                if (isEnabled) {
                    if (isVolumeControlAvailable(route)) {
                        volumeSlider.setMax(route.getVolumeMax());
                        volumeSlider.setProgress(route.getVolume());
                        volumeSlider.setOnSeekBarChangeListener(mVolumeChangeListener);
                    } else {
                        volumeSlider.setMax(100);
                        volumeSlider.setProgress(100);
                        volumeSlider.setEnabled(false);
                    }
                }
                android.widget.ImageView volumeItemIcon = ((android.widget.ImageView) (v.findViewById(R.id.mr_volume_item_icon)));
                volumeItemIcon.setAlpha(isEnabled ? 0xff : ((int) (0xff * mDisabledAlpha)));
                // If overlay bitmap exists, real view should remain hidden until
                // the animation ends.
                android.widget.LinearLayout container = ((android.widget.LinearLayout) (v.findViewById(R.id.volume_item_container)));
                container.setVisibility(mGroupMemberRoutesAnimatingWithBitmap.contains(route) ? android.view.View.INVISIBLE : android.view.View.VISIBLE);
                // Routes which are being added will be invisible until animation ends.
                if ((mGroupMemberRoutesAdded != null) && mGroupMemberRoutesAdded.contains(route)) {
                    android.view.animation.Animation alphaAnim = new android.view.animation.AlphaAnimation(0.0F, 0.0F);
                    alphaAnim.setDuration(0);
                    alphaAnim.setFillEnabled(true);
                    alphaAnim.setFillAfter(true);
                    v.clearAnimation();
                    v.startAnimation(alphaAnim);
                }
            }
            return v;
        }
    }

    private class FetchArtTask extends android.os.AsyncTask<java.lang.Void, java.lang.Void, android.graphics.Bitmap> {
        // Show animation only when fetching takes a long time.
        private static final long SHOW_ANIM_TIME_THRESHOLD_MILLIS = 120L;

        private final android.graphics.Bitmap mIconBitmap;

        private final android.net.Uri mIconUri;

        private int mBackgroundColor;

        private long mStartTimeMillis;

        FetchArtTask() {
            mIconBitmap = (mDescription == null) ? null : mDescription.getIconBitmap();
            mIconUri = (mDescription == null) ? null : mDescription.getIconUri();
        }

        public android.graphics.Bitmap getIconBitmap() {
            return mIconBitmap;
        }

        public android.net.Uri getIconUri() {
            return mIconUri;
        }

        @java.lang.Override
        protected void onPreExecute() {
            mStartTimeMillis = android.os.SystemClock.uptimeMillis();
            clearLoadedBitmap();
        }

        @java.lang.Override
        protected android.graphics.Bitmap doInBackground(java.lang.Void... arg) {
            android.graphics.Bitmap art = null;
            if (mIconBitmap != null) {
                art = mIconBitmap;
            } else
                if (mIconUri != null) {
                    java.io.InputStream stream = null;
                    try {
                        if ((stream = openInputStreamByScheme(mIconUri)) == null) {
                            android.util.Log.w(android.support.v7.app.MediaRouteControllerDialog.TAG, "Unable to open: " + mIconUri);
                            return null;
                        }
                        // Query art size.
                        android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
                        options.inJustDecodeBounds = true;
                        android.graphics.BitmapFactory.decodeStream(stream, null, options);
                        if ((options.outWidth == 0) || (options.outHeight == 0)) {
                            return null;
                        }
                        // Rewind the stream in order to restart art decoding.
                        try {
                            stream.reset();
                        } catch (java.io.IOException e) {
                            // Failed to rewind the stream, try to reopen it.
                            stream.close();
                            if ((stream = openInputStreamByScheme(mIconUri)) == null) {
                                android.util.Log.w(android.support.v7.app.MediaRouteControllerDialog.TAG, "Unable to open: " + mIconUri);
                                return null;
                            }
                        }
                        // Calculate required size to decode the art and possibly resize it.
                        options.inJustDecodeBounds = false;
                        int reqHeight = getDesiredArtHeight(options.outWidth, options.outHeight);
                        int ratio = options.outHeight / reqHeight;
                        options.inSampleSize = java.lang.Math.max(1, java.lang.Integer.highestOneBit(ratio));
                        if (isCancelled()) {
                            return null;
                        }
                        art = android.graphics.BitmapFactory.decodeStream(stream, null, options);
                    } catch (java.io.IOException e) {
                        android.util.Log.w(android.support.v7.app.MediaRouteControllerDialog.TAG, "Unable to open: " + mIconUri, e);
                    } finally {
                        if (stream != null) {
                            try {
                                stream.close();
                            } catch (java.io.IOException e) {
                            }
                        }
                    }
                }

            if ((art != null) && (art.getWidth() < art.getHeight())) {
                // Portrait art requires dominant color as background color.
                android.support.v7.graphics.Palette palette = new android.support.v7.graphics.Palette.Builder(art).maximumColorCount(1).generate();
                mBackgroundColor = (palette.getSwatches().isEmpty()) ? 0 : palette.getSwatches().get(0).getRgb();
            }
            return art;
        }

        @java.lang.Override
        protected void onPostExecute(android.graphics.Bitmap art) {
            mFetchArtTask = null;
            if ((mArtIconBitmap != mIconBitmap) || (mArtIconUri != mIconUri)) {
                mArtIconBitmap = mIconBitmap;
                mArtIconLoadedBitmap = art;
                mArtIconUri = mIconUri;
                mArtIconBackgroundColor = mBackgroundColor;
                mArtIconIsLoaded = true;
                long elapsedTimeMillis = android.os.SystemClock.uptimeMillis() - mStartTimeMillis;
                // Loaded bitmap will be applied on the next update
                update(elapsedTimeMillis > android.support.v7.app.MediaRouteControllerDialog.FetchArtTask.SHOW_ANIM_TIME_THRESHOLD_MILLIS);
            }
        }

        private java.io.InputStream openInputStreamByScheme(android.net.Uri uri) throws java.io.IOException {
            java.lang.String scheme = uri.getScheme().toLowerCase();
            java.io.InputStream stream = null;
            if ((android.content.ContentResolver.SCHEME_ANDROID_RESOURCE.equals(scheme) || android.content.ContentResolver.SCHEME_CONTENT.equals(scheme)) || android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
                stream = mContext.getContentResolver().openInputStream(uri);
            } else {
                java.net.URL url = new java.net.URL(uri.toString());
                java.net.URLConnection conn = url.openConnection();
                conn.setConnectTimeout(android.support.v7.app.MediaRouteControllerDialog.CONNECTION_TIMEOUT_MILLIS);
                conn.setReadTimeout(android.support.v7.app.MediaRouteControllerDialog.CONNECTION_TIMEOUT_MILLIS);
                stream = conn.getInputStream();
            }
            return stream == null ? null : new java.io.BufferedInputStream(stream);
        }
    }
}

