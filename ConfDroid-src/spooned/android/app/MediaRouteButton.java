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
package android.app;


public class MediaRouteButton extends android.view.View {
    private final android.media.MediaRouter mRouter;

    private final android.app.MediaRouteButton.MediaRouterCallback mCallback;

    private int mRouteTypes;

    private boolean mAttachedToWindow;

    private android.graphics.drawable.Drawable mRemoteIndicator;

    private boolean mRemoteActive;

    private boolean mCheatSheetEnabled;

    private boolean mIsConnecting;

    private int mMinWidth;

    private int mMinHeight;

    private android.view.View.OnClickListener mExtendedSettingsClickListener;

    // The checked state is used when connected to a remote route.
    private static final int[] CHECKED_STATE_SET = new int[]{ R.attr.state_checked };

    // The activated state is used while connecting to a remote route.
    private static final int[] ACTIVATED_STATE_SET = new int[]{ R.attr.state_activated };

    public MediaRouteButton(android.content.Context context) {
        this(context, null);
    }

    public MediaRouteButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, com.android.internal.R.attr.mediaRouteButtonStyle);
    }

    public MediaRouteButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public MediaRouteButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mRouter = ((android.media.MediaRouter) (context.getSystemService(android.content.Context.MEDIA_ROUTER_SERVICE)));
        mCallback = new android.app.MediaRouteButton.MediaRouterCallback();
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, com.android.internal.R.styleable.MediaRouteButton, defStyleAttr, defStyleRes);
        setRemoteIndicatorDrawable(a.getDrawable(com.android.internal.R.styleable.MediaRouteButton_externalRouteEnabledDrawable));
        mMinWidth = a.getDimensionPixelSize(com.android.internal.R.styleable.MediaRouteButton_minWidth, 0);
        mMinHeight = a.getDimensionPixelSize(com.android.internal.R.styleable.MediaRouteButton_minHeight, 0);
        final int routeTypes = a.getInteger(com.android.internal.R.styleable.MediaRouteButton_mediaRouteTypes, android.media.MediaRouter.ROUTE_TYPE_LIVE_AUDIO);
        a.recycle();
        setClickable(true);
        setLongClickable(true);
        setRouteTypes(routeTypes);
    }

    /**
     * Gets the media route types for filtering the routes that the user can
     * select using the media route chooser dialog.
     *
     * @return The route types.
     */
    public int getRouteTypes() {
        return mRouteTypes;
    }

    /**
     * Sets the types of routes that will be shown in the media route chooser dialog
     * launched by this button.
     *
     * @param types
     * 		The route types to match.
     */
    public void setRouteTypes(int types) {
        if (mRouteTypes != types) {
            if (mAttachedToWindow && (mRouteTypes != 0)) {
                mRouter.removeCallback(mCallback);
            }
            mRouteTypes = types;
            if (mAttachedToWindow && (types != 0)) {
                mRouter.addCallback(types, mCallback, android.media.MediaRouter.CALLBACK_FLAG_PASSIVE_DISCOVERY);
            }
            refreshRoute();
        }
    }

    public void setExtendedSettingsClickListener(android.view.View.OnClickListener listener) {
        mExtendedSettingsClickListener = listener;
    }

    /**
     * Show the route chooser or controller dialog.
     * <p>
     * If the default route is selected or if the currently selected route does
     * not match the {@link #getRouteTypes route types}, then shows the route chooser dialog.
     * Otherwise, shows the route controller dialog to offer the user
     * a choice to disconnect from the route or perform other control actions
     * such as setting the route's volume.
     * </p><p>
     * This will attach a {@link DialogFragment} to the containing Activity.
     * </p>
     */
    public void showDialog() {
        showDialogInternal();
    }

    boolean showDialogInternal() {
        if (!mAttachedToWindow) {
            return false;
        }
        android.app.DialogFragment f = com.android.internal.app.MediaRouteDialogPresenter.showDialogFragment(getActivity(), mRouteTypes, mExtendedSettingsClickListener);
        return f != null;
    }

    private android.app.Activity getActivity() {
        // Gross way of unwrapping the Activity so we can get the FragmentManager
        android.content.Context context = getContext();
        while (context instanceof android.content.ContextWrapper) {
            if (context instanceof android.app.Activity) {
                return ((android.app.Activity) (context));
            }
            context = ((android.content.ContextWrapper) (context)).getBaseContext();
        } 
        throw new java.lang.IllegalStateException("The MediaRouteButton's Context is not an Activity.");
    }

    /**
     * Sets whether to enable showing a toast with the content descriptor of the
     * button when the button is long pressed.
     */
    void setCheatSheetEnabled(boolean enable) {
        mCheatSheetEnabled = enable;
    }

    @java.lang.Override
    public boolean performClick() {
        // Send the appropriate accessibility events and call listeners
        boolean handled = super.performClick();
        if (!handled) {
            playSoundEffect(android.view.SoundEffectConstants.CLICK);
        }
        return showDialogInternal() || handled;
    }

    @java.lang.Override
    public boolean performLongClick() {
        if (super.performLongClick()) {
            return true;
        }
        if (!mCheatSheetEnabled) {
            return false;
        }
        final java.lang.CharSequence contentDesc = getContentDescription();
        if (android.text.TextUtils.isEmpty(contentDesc)) {
            // Don't show the cheat sheet if we have no description
            return false;
        }
        final int[] screenPos = new int[2];
        final android.graphics.Rect displayFrame = new android.graphics.Rect();
        getLocationOnScreen(screenPos);
        getWindowVisibleDisplayFrame(displayFrame);
        final android.content.Context context = getContext();
        final int width = getWidth();
        final int height = getHeight();
        final int midy = screenPos[1] + (height / 2);
        final int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        android.widget.Toast cheatSheet = android.widget.Toast.makeText(context, contentDesc, android.widget.Toast.LENGTH_SHORT);
        if (midy < displayFrame.height()) {
            // Show along the top; follow action buttons
            cheatSheet.setGravity(android.view.Gravity.TOP | android.view.Gravity.END, (screenWidth - screenPos[0]) - (width / 2), height);
        } else {
            // Show along the bottom center
            cheatSheet.setGravity(android.view.Gravity.BOTTOM | android.view.Gravity.CENTER_HORIZONTAL, 0, height);
        }
        cheatSheet.show();
        performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
        return true;
    }

    @java.lang.Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        // Technically we should be handling this more completely, but these
        // are implementation details here. Checked is used to express the connecting
        // drawable state and it's mutually exclusive with activated for the purposes
        // of state selection here.
        if (mIsConnecting) {
            android.view.View.mergeDrawableStates(drawableState, android.app.MediaRouteButton.CHECKED_STATE_SET);
        } else
            if (mRemoteActive) {
                android.view.View.mergeDrawableStates(drawableState, android.app.MediaRouteButton.ACTIVATED_STATE_SET);
            }

        return drawableState;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final android.graphics.drawable.Drawable remoteIndicator = mRemoteIndicator;
        if (((remoteIndicator != null) && remoteIndicator.isStateful()) && remoteIndicator.setState(getDrawableState())) {
            invalidateDrawable(remoteIndicator);
        }
    }

    private void setRemoteIndicatorDrawable(android.graphics.drawable.Drawable d) {
        if (mRemoteIndicator != null) {
            mRemoteIndicator.setCallback(null);
            unscheduleDrawable(mRemoteIndicator);
        }
        mRemoteIndicator = d;
        if (d != null) {
            d.setCallback(this);
            d.setState(getDrawableState());
            d.setVisible(getVisibility() == android.view.View.VISIBLE, false);
        }
        refreshDrawableState();
    }

    @java.lang.Override
    protected boolean verifyDrawable(@android.annotation.NonNull
    android.graphics.drawable.Drawable who) {
        return super.verifyDrawable(who) || (who == mRemoteIndicator);
    }

    @java.lang.Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (mRemoteIndicator != null) {
            mRemoteIndicator.jumpToCurrentState();
        }
    }

    @java.lang.Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (mRemoteIndicator != null) {
            mRemoteIndicator.setVisible(getVisibility() == android.view.View.VISIBLE, false);
        }
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        mAttachedToWindow = true;
        if (mRouteTypes != 0) {
            mRouter.addCallback(mRouteTypes, mCallback, android.media.MediaRouter.CALLBACK_FLAG_PASSIVE_DISCOVERY);
        }
        refreshRoute();
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        mAttachedToWindow = false;
        if (mRouteTypes != 0) {
            mRouter.removeCallback(mCallback);
        }
        super.onDetachedFromWindow();
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        final int width = java.lang.Math.max(mMinWidth, mRemoteIndicator != null ? (mRemoteIndicator.getIntrinsicWidth() + getPaddingLeft()) + getPaddingRight() : 0);
        final int height = java.lang.Math.max(mMinHeight, mRemoteIndicator != null ? (mRemoteIndicator.getIntrinsicHeight() + getPaddingTop()) + getPaddingBottom() : 0);
        int measuredWidth;
        switch (widthMode) {
            case android.view.View.MeasureSpec.EXACTLY :
                measuredWidth = widthSize;
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                measuredWidth = java.lang.Math.min(widthSize, width);
                break;
            default :
            case android.view.View.MeasureSpec.UNSPECIFIED :
                measuredWidth = width;
                break;
        }
        int measuredHeight;
        switch (heightMode) {
            case android.view.View.MeasureSpec.EXACTLY :
                measuredHeight = heightSize;
                break;
            case android.view.View.MeasureSpec.AT_MOST :
                measuredHeight = java.lang.Math.min(heightSize, height);
                break;
            default :
            case android.view.View.MeasureSpec.UNSPECIFIED :
                measuredHeight = height;
                break;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @java.lang.Override
    protected void onDraw(android.graphics.Canvas canvas) {
        super.onDraw(canvas);
        if (mRemoteIndicator == null)
            return;

        final int left = getPaddingLeft();
        final int right = getWidth() - getPaddingRight();
        final int top = getPaddingTop();
        final int bottom = getHeight() - getPaddingBottom();
        final int drawWidth = mRemoteIndicator.getIntrinsicWidth();
        final int drawHeight = mRemoteIndicator.getIntrinsicHeight();
        final int drawLeft = left + (((right - left) - drawWidth) / 2);
        final int drawTop = top + (((bottom - top) - drawHeight) / 2);
        mRemoteIndicator.setBounds(drawLeft, drawTop, drawLeft + drawWidth, drawTop + drawHeight);
        mRemoteIndicator.draw(canvas);
    }

    private void refreshRoute() {
        if (mAttachedToWindow) {
            final android.media.MediaRouter.RouteInfo route = mRouter.getSelectedRoute();
            final boolean isRemote = (!route.isDefault()) && route.matchesTypes(mRouteTypes);
            final boolean isConnecting = isRemote && route.isConnecting();
            boolean needsRefresh = false;
            if (mRemoteActive != isRemote) {
                mRemoteActive = isRemote;
                needsRefresh = true;
            }
            if (mIsConnecting != isConnecting) {
                mIsConnecting = isConnecting;
                needsRefresh = true;
            }
            if (needsRefresh) {
                refreshDrawableState();
            }
            setEnabled(mRouter.isRouteAvailable(mRouteTypes, android.media.MediaRouter.AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE));
        }
    }

    private final class MediaRouterCallback extends android.media.MediaRouter.SimpleCallback {
        @java.lang.Override
        public void onRouteAdded(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteRemoved(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteChanged(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteSelected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteUnselected(android.media.MediaRouter router, int type, android.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteGrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group, int index) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteUngrouped(android.media.MediaRouter router, android.media.MediaRouter.RouteInfo info, android.media.MediaRouter.RouteGroup group) {
            refreshRoute();
        }
    }
}

