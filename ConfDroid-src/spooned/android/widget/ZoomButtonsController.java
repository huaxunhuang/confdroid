/**
 * Copyright (C) 2008 The Android Open Source Project
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


/* Implementation notes:
- The zoom controls are displayed in their own window.
  (Easier for the client and better performance)
- This window is never touchable, and by default is not focusable.
  Its rect is quite big (fills horizontally) but has empty space between the
  edges and center.  Touches there should be given to the owner.  Instead of
  having the window touchable and dispatching these empty touch events to the
  owner, we set the window to not touchable and steal events from owner
  via onTouchListener.
- To make the buttons clickable, it attaches an OnTouchListener to the owner
  view and does the hit detection locally (attaches when visible, detaches when invisible).
- When it is focusable, it forwards uninteresting events to the owner view's
  view hierarchy.
 */
/**
 * The {@link ZoomButtonsController} handles showing and hiding the zoom
 * controls and positioning it relative to an owner view. It also gives the
 * client access to the zoom controls container, allowing for additional
 * accessory buttons to be shown in the zoom controls window.
 * <p>
 * Typically, clients should call {@link #setVisible(boolean) setVisible(true)}
 * on a touch down or move (no need to call {@link #setVisible(boolean)
 * setVisible(false)} since it will time out on its own). Also, whenever the
 * owner cannot be zoomed further, the client should update
 * {@link #setZoomInEnabled(boolean)} and {@link #setZoomOutEnabled(boolean)}.
 * <p>
 * If you are using this with a custom View, please call
 * {@link #setVisible(boolean) setVisible(false)} from
 * {@link View#onDetachedFromWindow} and from {@link View#onVisibilityChanged}
 * when <code>visibility != View.VISIBLE</code>.
 *
 * @deprecated This functionality and UI is better handled with custom views and layouts
rather than a dedicated zoom-control widget
 */
@java.lang.Deprecated
public class ZoomButtonsController implements android.view.View.OnTouchListener {
    private static final java.lang.String TAG = "ZoomButtonsController";

    private static final int ZOOM_CONTROLS_TIMEOUT = ((int) (android.view.ViewConfiguration.getZoomControlsTimeout()));

    private static final int ZOOM_CONTROLS_TOUCH_PADDING = 20;

    private int mTouchPaddingScaledSq;

    private final android.content.Context mContext;

    private final android.view.WindowManager mWindowManager;

    private boolean mAutoDismissControls = true;

    /**
     * The view that is being zoomed by this zoom controller.
     */
    private final android.view.View mOwnerView;

    /**
     * The location of the owner view on the screen. This is recalculated
     * each time the zoom controller is shown.
     */
    private final int[] mOwnerViewRawLocation = new int[2];

    /**
     * The container that is added as a window.
     */
    private final android.widget.FrameLayout mContainer;

    private android.view.WindowManager.LayoutParams mContainerLayoutParams;

    private final int[] mContainerRawLocation = new int[2];

    private android.widget.ZoomControls mControls;

    /**
     * The view (or null) that should receive touch events. This will get set if
     * the touch down hits the container. It will be reset on the touch up.
     */
    private android.view.View mTouchTargetView;

    /**
     * The {@link #mTouchTargetView}'s location in window, set on touch down.
     */
    private final int[] mTouchTargetWindowLocation = new int[2];

    /**
     * If the zoom controller is dismissed but the user is still in a touch
     * interaction, we set this to true. This will ignore all touch events until
     * up/cancel, and then set the owner's touch listener to null.
     * <p>
     * Otherwise, the owner view would get mismatched events (i.e., touch move
     * even though it never got the touch down.)
     */
    private boolean mReleaseTouchListenerOnUp;

    /**
     * Whether the container has been added to the window manager.
     */
    private boolean mIsVisible;

    private final android.graphics.Rect mTempRect = new android.graphics.Rect();

    private final int[] mTempIntArray = new int[2];

    private android.widget.ZoomButtonsController.OnZoomListener mCallback;

    /**
     * When showing the zoom, we add the view as a new window. However, there is
     * logic that needs to know the size of the zoom which is determined after
     * it's laid out. Therefore, we must post this logic onto the UI thread so
     * it will be exceuted AFTER the layout. This is the logic.
     */
    private java.lang.Runnable mPostedVisibleInitializer;

    private final android.content.IntentFilter mConfigurationChangedFilter = new android.content.IntentFilter(android.content.Intent.ACTION_CONFIGURATION_CHANGED);

    /**
     * Needed to reposition the zoom controls after configuration changes.
     */
    private final android.content.BroadcastReceiver mConfigurationChangedReceiver = new android.content.BroadcastReceiver() {
        @java.lang.Override
        public void onReceive(android.content.Context context, android.content.Intent intent) {
            if (!mIsVisible)
                return;

            mHandler.removeMessages(android.widget.ZoomButtonsController.MSG_POST_CONFIGURATION_CHANGED);
            mHandler.sendEmptyMessage(android.widget.ZoomButtonsController.MSG_POST_CONFIGURATION_CHANGED);
        }
    };

    /**
     * When configuration changes, this is called after the UI thread is idle.
     */
    private static final int MSG_POST_CONFIGURATION_CHANGED = 2;

    /**
     * Used to delay the zoom controller dismissal.
     */
    private static final int MSG_DISMISS_ZOOM_CONTROLS = 3;

    /**
     * If setVisible(true) is called and the owner view's window token is null,
     * we delay the setVisible(true) call until it is not null.
     */
    private static final int MSG_POST_SET_VISIBLE = 4;

    private final android.os.Handler mHandler = new android.os.Handler() {
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case android.widget.ZoomButtonsController.MSG_POST_CONFIGURATION_CHANGED :
                    onPostConfigurationChanged();
                    break;
                case android.widget.ZoomButtonsController.MSG_DISMISS_ZOOM_CONTROLS :
                    setVisible(false);
                    break;
                case android.widget.ZoomButtonsController.MSG_POST_SET_VISIBLE :
                    if (mOwnerView.getWindowToken() == null) {
                        // Doh, it is still null, just ignore the set visible call
                        android.util.Log.e(android.widget.ZoomButtonsController.TAG, "Cannot make the zoom controller visible if the owner view is " + "not attached to a window.");
                    } else {
                        setVisible(true);
                    }
                    break;
            }
        }
    };

    /**
     * Constructor for the {@link ZoomButtonsController}.
     *
     * @param ownerView
     * 		The view that is being zoomed by the zoom controls. The
     * 		zoom controls will be displayed aligned with this view.
     */
    public ZoomButtonsController(android.view.View ownerView) {
        mContext = ownerView.getContext();
        mWindowManager = ((android.view.WindowManager) (mContext.getSystemService(android.content.Context.WINDOW_SERVICE)));
        mOwnerView = ownerView;
        mTouchPaddingScaledSq = ((int) (android.widget.ZoomButtonsController.ZOOM_CONTROLS_TOUCH_PADDING * mContext.getResources().getDisplayMetrics().density));
        mTouchPaddingScaledSq *= mTouchPaddingScaledSq;
        mContainer = createContainer();
    }

    /**
     * Whether to enable the zoom in control.
     *
     * @param enabled
     * 		Whether to enable the zoom in control.
     */
    public void setZoomInEnabled(boolean enabled) {
        mControls.setIsZoomInEnabled(enabled);
    }

    /**
     * Whether to enable the zoom out control.
     *
     * @param enabled
     * 		Whether to enable the zoom out control.
     */
    public void setZoomOutEnabled(boolean enabled) {
        mControls.setIsZoomOutEnabled(enabled);
    }

    /**
     * Sets the delay between zoom callbacks as the user holds a zoom button.
     *
     * @param speed
     * 		The delay in milliseconds between zoom callbacks.
     */
    public void setZoomSpeed(long speed) {
        mControls.setZoomSpeed(speed);
    }

    private android.widget.FrameLayout createContainer() {
        android.view.WindowManager.LayoutParams lp = new android.view.WindowManager.LayoutParams(android.view.WindowManager.LayoutParams.WRAP_CONTENT, android.view.WindowManager.LayoutParams.WRAP_CONTENT);
        // Controls are positioned BOTTOM | CENTER with respect to the owner view.
        lp.gravity = android.view.Gravity.TOP | android.view.Gravity.START;
        lp.flags = ((android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE) | android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) | android.view.WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        lp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        lp.width = android.view.WindowManager.LayoutParams.MATCH_PARENT;
        lp.type = android.view.WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
        lp.format = android.graphics.PixelFormat.TRANSLUCENT;
        lp.windowAnimations = com.android.internal.R.style.Animation_ZoomButtons;
        mContainerLayoutParams = lp;
        android.widget.FrameLayout container = new android.widget.ZoomButtonsController.Container(mContext);
        container.setLayoutParams(lp);
        container.setMeasureAllChildren(true);
        android.view.LayoutInflater inflater = ((android.view.LayoutInflater) (mContext.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        inflater.inflate(com.android.internal.R.layout.zoom_container, container);
        mControls = container.findViewById(com.android.internal.R.id.zoomControls);
        mControls.setOnZoomInClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                dismissControlsDelayed(android.widget.ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
                if (mCallback != null)
                    mCallback.onZoom(true);

            }
        });
        mControls.setOnZoomOutClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                dismissControlsDelayed(android.widget.ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
                if (mCallback != null)
                    mCallback.onZoom(false);

            }
        });
        return container;
    }

    /**
     * Sets the {@link OnZoomListener} listener that receives callbacks to zoom.
     *
     * @param listener
     * 		The listener that will be told to zoom.
     */
    public void setOnZoomListener(android.widget.ZoomButtonsController.OnZoomListener listener) {
        mCallback = listener;
    }

    /**
     * Sets whether the zoom controls should be focusable. If the controls are
     * focusable, then trackball and arrow key interactions are possible.
     * Otherwise, only touch interactions are possible.
     *
     * @param focusable
     * 		Whether the zoom controls should be focusable.
     */
    public void setFocusable(boolean focusable) {
        int oldFlags = mContainerLayoutParams.flags;
        if (focusable) {
            mContainerLayoutParams.flags &= ~android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        } else {
            mContainerLayoutParams.flags |= android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        if ((mContainerLayoutParams.flags != oldFlags) && mIsVisible) {
            mWindowManager.updateViewLayout(mContainer, mContainerLayoutParams);
        }
    }

    /**
     * Whether the zoom controls will be automatically dismissed after showing.
     *
     * @return Whether the zoom controls will be auto dismissed after showing.
     */
    public boolean isAutoDismissed() {
        return mAutoDismissControls;
    }

    /**
     * Sets whether the zoom controls will be automatically dismissed after
     * showing.
     */
    public void setAutoDismissed(boolean autoDismiss) {
        if (mAutoDismissControls == autoDismiss)
            return;

        mAutoDismissControls = autoDismiss;
    }

    /**
     * Whether the zoom controls are visible to the user.
     *
     * @return Whether the zoom controls are visible to the user.
     */
    public boolean isVisible() {
        return mIsVisible;
    }

    /**
     * Sets whether the zoom controls should be visible to the user.
     *
     * @param visible
     * 		Whether the zoom controls should be visible to the user.
     */
    public void setVisible(boolean visible) {
        if (visible) {
            if (mOwnerView.getWindowToken() == null) {
                /* We need a window token to show ourselves, maybe the owner's
                window hasn't been created yet but it will have been by the
                time the looper is idle, so post the setVisible(true) call.
                 */
                if (!mHandler.hasMessages(android.widget.ZoomButtonsController.MSG_POST_SET_VISIBLE)) {
                    mHandler.sendEmptyMessage(android.widget.ZoomButtonsController.MSG_POST_SET_VISIBLE);
                }
                return;
            }
            dismissControlsDelayed(android.widget.ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
        }
        if (mIsVisible == visible) {
            return;
        }
        mIsVisible = visible;
        if (visible) {
            if (mContainerLayoutParams.token == null) {
                mContainerLayoutParams.token = mOwnerView.getWindowToken();
            }
            mWindowManager.addView(mContainer, mContainerLayoutParams);
            if (mPostedVisibleInitializer == null) {
                mPostedVisibleInitializer = new java.lang.Runnable() {
                    public void run() {
                        refreshPositioningVariables();
                        if (mCallback != null) {
                            mCallback.onVisibilityChanged(true);
                        }
                    }
                };
            }
            mHandler.post(mPostedVisibleInitializer);
            // Handle configuration changes when visible
            mContext.registerReceiver(mConfigurationChangedReceiver, mConfigurationChangedFilter);
            // Steal touches events from the owner
            mOwnerView.setOnTouchListener(this);
            mReleaseTouchListenerOnUp = false;
        } else {
            // Don't want to steal any more touches
            if (mTouchTargetView != null) {
                // We are still stealing the touch events for this touch
                // sequence, so release the touch listener later
                mReleaseTouchListenerOnUp = true;
            } else {
                mOwnerView.setOnTouchListener(null);
            }
            // No longer care about configuration changes
            mContext.unregisterReceiver(mConfigurationChangedReceiver);
            mWindowManager.removeViewImmediate(mContainer);
            mHandler.removeCallbacks(mPostedVisibleInitializer);
            if (mCallback != null) {
                mCallback.onVisibilityChanged(false);
            }
        }
    }

    /**
     * Gets the container that is the parent of the zoom controls.
     * <p>
     * The client can add other views to this container to link them with the
     * zoom controls.
     *
     * @return The container of the zoom controls. It will be a layout that
    respects the gravity of a child's layout parameters.
     */
    public android.view.ViewGroup getContainer() {
        return mContainer;
    }

    /**
     * Gets the view for the zoom controls.
     *
     * @return The zoom controls view.
     */
    public android.view.View getZoomControls() {
        return mControls;
    }

    private void dismissControlsDelayed(int delay) {
        if (mAutoDismissControls) {
            mHandler.removeMessages(android.widget.ZoomButtonsController.MSG_DISMISS_ZOOM_CONTROLS);
            mHandler.sendEmptyMessageDelayed(android.widget.ZoomButtonsController.MSG_DISMISS_ZOOM_CONTROLS, delay);
        }
    }

    private void refreshPositioningVariables() {
        // if the mOwnerView is detached from window then skip.
        if (mOwnerView.getWindowToken() == null)
            return;

        // Position the zoom controls on the bottom of the owner view.
        int ownerHeight = mOwnerView.getHeight();
        int ownerWidth = mOwnerView.getWidth();
        // The gap between the top of the owner and the top of the container
        int containerOwnerYOffset = ownerHeight - mContainer.getHeight();
        // Calculate the owner view's bounds
        mOwnerView.getLocationOnScreen(mOwnerViewRawLocation);
        mContainerRawLocation[0] = mOwnerViewRawLocation[0];
        mContainerRawLocation[1] = mOwnerViewRawLocation[1] + containerOwnerYOffset;
        int[] ownerViewWindowLoc = mTempIntArray;
        mOwnerView.getLocationInWindow(ownerViewWindowLoc);
        // lp.x and lp.y should be relative to the owner's window top-left
        mContainerLayoutParams.x = ownerViewWindowLoc[0];
        mContainerLayoutParams.width = ownerWidth;
        mContainerLayoutParams.y = ownerViewWindowLoc[1] + containerOwnerYOffset;
        if (mIsVisible) {
            mWindowManager.updateViewLayout(mContainer, mContainerLayoutParams);
        }
    }

    /* This will only be called when the container has focus. */
    private boolean onContainerKey(android.view.KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (isInterestingKey(keyCode)) {
            if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
                if ((event.getAction() == android.view.KeyEvent.ACTION_DOWN) && (event.getRepeatCount() == 0)) {
                    if (mOwnerView != null) {
                        android.view.KeyEvent.DispatcherState ds = mOwnerView.getKeyDispatcherState();
                        if (ds != null) {
                            ds.startTracking(event, this);
                        }
                    }
                    return true;
                } else
                    if (((event.getAction() == android.view.KeyEvent.ACTION_UP) && event.isTracking()) && (!event.isCanceled())) {
                        setVisible(false);
                        return true;
                    }

            } else {
                dismissControlsDelayed(android.widget.ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
            }
            // Let the container handle the key
            return false;
        } else {
            android.view.ViewRootImpl viewRoot = mOwnerView.getViewRootImpl();
            if (viewRoot != null) {
                viewRoot.dispatchInputEvent(event);
            }
            // We gave the key to the owner, don't let the container handle this key
            return true;
        }
    }

    private boolean isInterestingKey(int keyCode) {
        switch (keyCode) {
            case android.view.KeyEvent.KEYCODE_DPAD_CENTER :
            case android.view.KeyEvent.KEYCODE_DPAD_UP :
            case android.view.KeyEvent.KEYCODE_DPAD_DOWN :
            case android.view.KeyEvent.KEYCODE_DPAD_LEFT :
            case android.view.KeyEvent.KEYCODE_DPAD_RIGHT :
            case android.view.KeyEvent.KEYCODE_ENTER :
            case android.view.KeyEvent.KEYCODE_BACK :
                return true;
            default :
                return false;
        }
    }

    /**
     *
     *
     * @unknown The ZoomButtonsController implements the OnTouchListener, but this
    does not need to be shown in its public API.
     */
    public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
        int action = event.getAction();
        if (event.getPointerCount() > 1) {
            // ZoomButtonsController doesn't handle mutitouch. Give up control.
            return false;
        }
        if (mReleaseTouchListenerOnUp) {
            // The controls were dismissed but we need to throw away all events until the up
            if ((action == android.view.MotionEvent.ACTION_UP) || (action == android.view.MotionEvent.ACTION_CANCEL)) {
                mOwnerView.setOnTouchListener(null);
                setTouchTargetView(null);
                mReleaseTouchListenerOnUp = false;
            }
            // Eat this event
            return true;
        }
        dismissControlsDelayed(android.widget.ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
        android.view.View targetView = mTouchTargetView;
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
                targetView = findViewForTouch(((int) (event.getRawX())), ((int) (event.getRawY())));
                setTouchTargetView(targetView);
                break;
            case android.view.MotionEvent.ACTION_UP :
            case android.view.MotionEvent.ACTION_CANCEL :
                setTouchTargetView(null);
                break;
        }
        if (targetView != null) {
            // The upperleft corner of the target view in raw coordinates
            int targetViewRawX = mContainerRawLocation[0] + mTouchTargetWindowLocation[0];
            int targetViewRawY = mContainerRawLocation[1] + mTouchTargetWindowLocation[1];
            android.view.MotionEvent containerEvent = android.view.MotionEvent.obtain(event);
            // Convert the motion event into the target view's coordinates (from
            // owner view's coordinates)
            containerEvent.offsetLocation(mOwnerViewRawLocation[0] - targetViewRawX, mOwnerViewRawLocation[1] - targetViewRawY);
            /* Disallow negative coordinates (which can occur due to
            ZOOM_CONTROLS_TOUCH_PADDING)
             */
            // These are floats because we need to potentially offset away this exact amount
            float containerX = containerEvent.getX();
            float containerY = containerEvent.getY();
            if ((containerX < 0) && (containerX > (-android.widget.ZoomButtonsController.ZOOM_CONTROLS_TOUCH_PADDING))) {
                containerEvent.offsetLocation(-containerX, 0);
            }
            if ((containerY < 0) && (containerY > (-android.widget.ZoomButtonsController.ZOOM_CONTROLS_TOUCH_PADDING))) {
                containerEvent.offsetLocation(0, -containerY);
            }
            boolean retValue = targetView.dispatchTouchEvent(containerEvent);
            containerEvent.recycle();
            return retValue;
        } else {
            return false;
        }
    }

    private void setTouchTargetView(android.view.View view) {
        mTouchTargetView = view;
        if (view != null) {
            view.getLocationInWindow(mTouchTargetWindowLocation);
        }
    }

    /**
     * Returns the View that should receive a touch at the given coordinates.
     *
     * @param rawX
     * 		The raw X.
     * @param rawY
     * 		The raw Y.
     * @return The view that should receive the touches, or null if there is not one.
     */
    private android.view.View findViewForTouch(int rawX, int rawY) {
        // Reverse order so the child drawn on top gets first dibs.
        int containerCoordsX = rawX - mContainerRawLocation[0];
        int containerCoordsY = rawY - mContainerRawLocation[1];
        android.graphics.Rect frame = mTempRect;
        android.view.View closestChild = null;
        int closestChildDistanceSq = java.lang.Integer.MAX_VALUE;
        for (int i = mContainer.getChildCount() - 1; i >= 0; i--) {
            android.view.View child = mContainer.getChildAt(i);
            if (child.getVisibility() != android.view.View.VISIBLE) {
                continue;
            }
            child.getHitRect(frame);
            if (frame.contains(containerCoordsX, containerCoordsY)) {
                return child;
            }
            int distanceX;
            if ((containerCoordsX >= frame.left) && (containerCoordsX <= frame.right)) {
                distanceX = 0;
            } else {
                distanceX = java.lang.Math.min(java.lang.Math.abs(frame.left - containerCoordsX), java.lang.Math.abs(containerCoordsX - frame.right));
            }
            int distanceY;
            if ((containerCoordsY >= frame.top) && (containerCoordsY <= frame.bottom)) {
                distanceY = 0;
            } else {
                distanceY = java.lang.Math.min(java.lang.Math.abs(frame.top - containerCoordsY), java.lang.Math.abs(containerCoordsY - frame.bottom));
            }
            int distanceSq = (distanceX * distanceX) + (distanceY * distanceY);
            if ((distanceSq < mTouchPaddingScaledSq) && (distanceSq < closestChildDistanceSq)) {
                closestChild = child;
                closestChildDistanceSq = distanceSq;
            }
        }
        return closestChild;
    }

    private void onPostConfigurationChanged() {
        dismissControlsDelayed(android.widget.ZoomButtonsController.ZOOM_CONTROLS_TIMEOUT);
        refreshPositioningVariables();
    }

    /**
     * Interface that will be called when the user performs an interaction that
     * triggers some action, for example zooming.
     */
    public interface OnZoomListener {
        /**
         * Called when the zoom controls' visibility changes.
         *
         * @param visible
         * 		Whether the zoom controls are visible.
         */
        void onVisibilityChanged(boolean visible);

        /**
         * Called when the owner view needs to be zoomed.
         *
         * @param zoomIn
         * 		The direction of the zoom: true to zoom in, false to zoom out.
         */
        void onZoom(boolean zoomIn);
    }

    private class Container extends android.widget.FrameLayout {
        public Container(android.content.Context context) {
            super(context);
        }

        /* Need to override this to intercept the key events. Otherwise, we
        would attach a key listener to the container but its superclass
        ViewGroup gives it to the focused View instead of calling the key
        listener, and so we wouldn't get the events.
         */
        @java.lang.Override
        public boolean dispatchKeyEvent(android.view.KeyEvent event) {
            return onContainerKey(event) ? true : super.dispatchKeyEvent(event);
        }
    }
}

