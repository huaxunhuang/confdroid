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
 * The media route button allows the user to select routes and to control the
 * currently selected route.
 * <p>
 * The application must specify the kinds of routes that the user should be allowed
 * to select by specifying a {@link MediaRouteSelector selector} with the
 * {@link #setRouteSelector} method.
 * </p><p>
 * When the default route is selected or when the currently selected route does not
 * match the {@link #getRouteSelector() selector}, the button will appear in
 * an inactive state indicating that the application is not connected to a
 * route of the kind that it wants to use.  Clicking on the button opens
 * a {@link MediaRouteChooserDialog} to allow the user to select a route.
 * If no non-default routes match the selector and it is not possible for an active
 * scan to discover any matching routes, then the button is disabled and cannot
 * be clicked.
 * </p><p>
 * When a non-default route is selected that matches the selector, the button will
 * appear in an active state indicating that the application is connected
 * to a route of the kind that it wants to use.  The button may also appear
 * in an intermediary connecting state if the route is in the process of connecting
 * to the destination but has not yet completed doing so.  In either case, clicking
 * on the button opens a {@link MediaRouteControllerDialog} to allow the user
 * to control or disconnect from the current route.
 * </p>
 *
 * <h3>Prerequisites</h3>
 * <p>
 * To use the media route button, the activity must be a subclass of
 * {@link FragmentActivity} from the <code>android.support.v4</code>
 * support library.  Refer to support library documentation for details.
 * </p>
 *
 * @see MediaRouteActionProvider
 * @see #setRouteSelector
 */
public class MediaRouteButton extends android.view.View {
    private static final java.lang.String TAG = "MediaRouteButton";

    private static final java.lang.String CHOOSER_FRAGMENT_TAG = "android.support.v7.mediarouter:MediaRouteChooserDialogFragment";

    private static final java.lang.String CONTROLLER_FRAGMENT_TAG = "android.support.v7.mediarouter:MediaRouteControllerDialogFragment";

    private final android.support.v7.media.MediaRouter mRouter;

    private final android.support.v7.app.MediaRouteButton.MediaRouterCallback mCallback;

    private android.support.v7.media.MediaRouteSelector mSelector = android.support.v7.media.MediaRouteSelector.EMPTY;

    private android.support.v7.app.MediaRouteDialogFactory mDialogFactory = android.support.v7.app.MediaRouteDialogFactory.getDefault();

    private boolean mAttachedToWindow;

    private android.graphics.drawable.Drawable mRemoteIndicator;

    private boolean mRemoteActive;

    private boolean mCheatSheetEnabled;

    private boolean mIsConnecting;

    private int mMinWidth;

    private int mMinHeight;

    // The checked state is used when connected to a remote route.
    private static final int[] CHECKED_STATE_SET = new int[]{ android.R.attr.state_checked };

    // The checkable state is used while connecting to a remote route.
    private static final int[] CHECKABLE_STATE_SET = new int[]{ android.R.attr.state_checkable };

    public MediaRouteButton(android.content.Context context) {
        this(context, null);
    }

    public MediaRouteButton(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, R.attr.mediaRouteButtonStyle);
    }

    public MediaRouteButton(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(android.support.v7.app.MediaRouterThemeHelper.createThemedContext(context, defStyleAttr), attrs, defStyleAttr);
        context = getContext();
        mRouter = android.support.v7.media.MediaRouter.getInstance(context);
        mCallback = new android.support.v7.app.MediaRouteButton.MediaRouterCallback();
        android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MediaRouteButton, defStyleAttr, 0);
        setRemoteIndicatorDrawable(a.getDrawable(R.styleable.MediaRouteButton_externalRouteEnabledDrawable));
        mMinWidth = a.getDimensionPixelSize(R.styleable.MediaRouteButton_android_minWidth, 0);
        mMinHeight = a.getDimensionPixelSize(R.styleable.MediaRouteButton_android_minHeight, 0);
        a.recycle();
        updateContentDescription();
        setClickable(true);
        setLongClickable(true);
    }

    /**
     * Gets the media route selector for filtering the routes that the user can
     * select using the media route chooser dialog.
     *
     * @return The selector, never null.
     */
    @android.support.annotation.NonNull
    public android.support.v7.media.MediaRouteSelector getRouteSelector() {
        return mSelector;
    }

    /**
     * Sets the media route selector for filtering the routes that the user can
     * select using the media route chooser dialog.
     *
     * @param selector
     * 		The selector, must not be null.
     */
    public void setRouteSelector(android.support.v7.media.MediaRouteSelector selector) {
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("selector must not be null");
        }
        if (!mSelector.equals(selector)) {
            if (mAttachedToWindow) {
                if (!mSelector.isEmpty()) {
                    mRouter.removeCallback(mCallback);
                }
                if (!selector.isEmpty()) {
                    mRouter.addCallback(selector, mCallback);
                }
            }
            mSelector = selector;
            refreshRoute();
        }
    }

    /**
     * Gets the media route dialog factory to use when showing the route chooser
     * or controller dialog.
     *
     * @return The dialog factory, never null.
     */
    @android.support.annotation.NonNull
    public android.support.v7.app.MediaRouteDialogFactory getDialogFactory() {
        return mDialogFactory;
    }

    /**
     * Sets the media route dialog factory to use when showing the route chooser
     * or controller dialog.
     *
     * @param factory
     * 		The dialog factory, must not be null.
     */
    public void setDialogFactory(@android.support.annotation.NonNull
    android.support.v7.app.MediaRouteDialogFactory factory) {
        if (factory == null) {
            throw new java.lang.IllegalArgumentException("factory must not be null");
        }
        mDialogFactory = factory;
    }

    /**
     * Show the route chooser or controller dialog.
     * <p>
     * If the default route is selected or if the currently selected route does
     * not match the {@link #getRouteSelector selector}, then shows the route chooser dialog.
     * Otherwise, shows the route controller dialog to offer the user
     * a choice to disconnect from the route or perform other control actions
     * such as setting the route's volume.
     * </p><p>
     * The application can customize the dialogs by calling {@link #setDialogFactory}
     * to provide a customized dialog factory.
     * </p>
     *
     * @return True if the dialog was actually shown.
     * @throws IllegalStateException
     * 		if the activity is not a subclass of
     * 		{@link FragmentActivity}.
     */
    public boolean showDialog() {
        if (!mAttachedToWindow) {
            return false;
        }
        final android.support.v4.app.FragmentManager fm = getFragmentManager();
        if (fm == null) {
            throw new java.lang.IllegalStateException("The activity must be a subclass of FragmentActivity");
        }
        android.support.v7.media.MediaRouter.RouteInfo route = mRouter.getSelectedRoute();
        if (route.isDefaultOrBluetooth() || (!route.matchesSelector(mSelector))) {
            if (fm.findFragmentByTag(android.support.v7.app.MediaRouteButton.CHOOSER_FRAGMENT_TAG) != null) {
                android.util.Log.w(android.support.v7.app.MediaRouteButton.TAG, "showDialog(): Route chooser dialog already showing!");
                return false;
            }
            android.support.v7.app.MediaRouteChooserDialogFragment f = mDialogFactory.onCreateChooserDialogFragment();
            f.setRouteSelector(mSelector);
            f.show(fm, android.support.v7.app.MediaRouteButton.CHOOSER_FRAGMENT_TAG);
        } else {
            if (fm.findFragmentByTag(android.support.v7.app.MediaRouteButton.CONTROLLER_FRAGMENT_TAG) != null) {
                android.util.Log.w(android.support.v7.app.MediaRouteButton.TAG, "showDialog(): Route controller dialog already showing!");
                return false;
            }
            android.support.v7.app.MediaRouteControllerDialogFragment f = mDialogFactory.onCreateControllerDialogFragment();
            f.show(fm, android.support.v7.app.MediaRouteButton.CONTROLLER_FRAGMENT_TAG);
        }
        return true;
    }

    private android.support.v4.app.FragmentManager getFragmentManager() {
        android.app.Activity activity = getActivity();
        if (activity instanceof android.support.v4.app.FragmentActivity) {
            return ((android.support.v4.app.FragmentActivity) (activity)).getSupportFragmentManager();
        }
        return null;
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
        return null;
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
        return showDialog() || handled;
    }

    @java.lang.Override
    public boolean performLongClick() {
        if (super.performLongClick()) {
            return true;
        }
        if (!mCheatSheetEnabled) {
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
        android.widget.Toast cheatSheet = android.widget.Toast.makeText(context, R.string.mr_button_content_description, android.widget.Toast.LENGTH_SHORT);
        if (midy < displayFrame.height()) {
            // Show along the top; follow action buttons
            cheatSheet.setGravity(android.view.Gravity.TOP | android.support.v4.view.GravityCompat.END, (screenWidth - screenPos[0]) - (width / 2), height);
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
        // are implementation details here. Checkable is used to express the connecting
        // drawable state and it's mutually exclusive with check for the purposes
        // of state selection here.
        if (mIsConnecting) {
            android.view.View.mergeDrawableStates(drawableState, android.support.v7.app.MediaRouteButton.CHECKABLE_STATE_SET);
        } else
            if (mRemoteActive) {
                android.view.View.mergeDrawableStates(drawableState, android.support.v7.app.MediaRouteButton.CHECKED_STATE_SET);
            }

        return drawableState;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (mRemoteIndicator != null) {
            int[] myDrawableState = getDrawableState();
            mRemoteIndicator.setState(myDrawableState);
            invalidate();
        }
    }

    /**
     * Sets a drawable to use as the remote route indicator.
     */
    public void setRemoteIndicatorDrawable(android.graphics.drawable.Drawable d) {
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
    protected boolean verifyDrawable(android.graphics.drawable.Drawable who) {
        return super.verifyDrawable(who) || (who == mRemoteIndicator);
    }

    // @Override defined in v11
    public void jumpDrawablesToCurrentState() {
        // We can't call super to handle the background so we do it ourselves.
        // super.jumpDrawablesToCurrentState();
        if (getBackground() != null) {
            android.support.v4.graphics.drawable.DrawableCompat.jumpToCurrentState(getBackground());
        }
        // Handle our own remote indicator.
        if (mRemoteIndicator != null) {
            android.support.v4.graphics.drawable.DrawableCompat.jumpToCurrentState(mRemoteIndicator);
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
        if (!mSelector.isEmpty()) {
            mRouter.addCallback(mSelector, mCallback);
        }
        refreshRoute();
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        mAttachedToWindow = false;
        if (!mSelector.isEmpty()) {
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
        if (mRemoteIndicator != null) {
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
    }

    void refreshRoute() {
        if (mAttachedToWindow) {
            final android.support.v7.media.MediaRouter.RouteInfo route = mRouter.getSelectedRoute();
            final boolean isRemote = (!route.isDefaultOrBluetooth()) && route.matchesSelector(mSelector);
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
                updateContentDescription();
                refreshDrawableState();
                if (mRemoteIndicator.getCurrent() instanceof android.graphics.drawable.AnimationDrawable) {
                    android.graphics.drawable.AnimationDrawable curDrawable = ((android.graphics.drawable.AnimationDrawable) (mRemoteIndicator.getCurrent()));
                    if (!curDrawable.isRunning()) {
                        curDrawable.start();
                    }
                }
            }
            setEnabled(mRouter.isRouteAvailable(mSelector, android.support.v7.media.MediaRouter.AVAILABILITY_FLAG_IGNORE_DEFAULT_ROUTE));
        }
    }

    private void updateContentDescription() {
        int resId;
        if (mIsConnecting) {
            resId = R.string.mr_cast_button_connecting;
        } else
            if (mRemoteActive) {
                resId = R.string.mr_cast_button_connected;
            } else {
                resId = R.string.mr_cast_button_disconnected;
            }

        setContentDescription(getContext().getString(resId));
    }

    private final class MediaRouterCallback extends android.support.v7.media.MediaRouter.Callback {
        MediaRouterCallback() {
        }

        @java.lang.Override
        public void onRouteAdded(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteRemoved(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteSelected(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onRouteUnselected(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.RouteInfo info) {
            refreshRoute();
        }

        @java.lang.Override
        public void onProviderAdded(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
            refreshRoute();
        }

        @java.lang.Override
        public void onProviderRemoved(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
            refreshRoute();
        }

        @java.lang.Override
        public void onProviderChanged(android.support.v7.media.MediaRouter router, android.support.v7.media.MediaRouter.ProviderInfo provider) {
            refreshRoute();
        }
    }
}

