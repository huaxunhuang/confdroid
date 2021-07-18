/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * A toast is a view containing a quick little message for the user.  The toast class
 * helps you create and show those.
 * {@more }
 *
 * <p>
 * When the view is shown to the user, appears as a floating view over the
 * application.  It will never receive focus.  The user will probably be in the
 * middle of typing something else.  The idea is to be as unobtrusive as
 * possible, while still showing the user the information you want them to see.
 * Two examples are the volume control, and the brief message saying that your
 * settings have been saved.
 * <p>
 * The easiest way to use this class is to call one of the static methods that constructs
 * everything you need and returns a new Toast object.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For information about creating Toast notifications, read the
 * <a href="{@docRoot }guide/topics/ui/notifiers/toasts.html">Toast Notifications</a> developer
 * guide.</p>
 * </div>
 */
public class Toast {
    static final java.lang.String TAG = "Toast";

    static final boolean localLOGV = false;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef(prefix = { "LENGTH_" }, value = { android.widget.Toast.LENGTH_SHORT, android.widget.Toast.LENGTH_LONG })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Duration {}

    /**
     * Show the view or text notification for a short period of time.  This time
     * could be user-definable.  This is the default.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = 0;

    /**
     * Show the view or text notification for a long period of time.  This time
     * could be user-definable.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 1;

    final android.content.Context mContext;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    final android.widget.Toast.TN mTN;

    @android.annotation.UnsupportedAppUsage
    int mDuration;

    android.view.View mNextView;

    /**
     * Construct an empty Toast object.  You must call {@link #setView} before you
     * can call {@link #show}.
     *
     * @param context
     * 		The context to use.  Usually your {@link android.app.Application}
     * 		or {@link android.app.Activity} object.
     */
    public Toast(android.content.Context context) {
        this(context, null);
    }

    /**
     * Constructs an empty Toast object.  If looper is null, Looper.myLooper() is used.
     *
     * @unknown 
     */
    public Toast(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.os.Looper looper) {
        mContext = context;
        mTN = new android.widget.Toast.TN(context.getPackageName(), looper);
        mTN.mY = context.getResources().getDimensionPixelSize(com.android.internal.R.dimen.toast_y_offset);
        mTN.mGravity = context.getResources().getInteger(com.android.internal.R.integer.config_toastDefaultGravity);
    }

    /**
     * Show the view for the specified duration.
     */
    public void show() {
        if (mNextView == null) {
            throw new java.lang.RuntimeException("setView must have been called");
        }
        android.app.INotificationManager service = android.widget.Toast.getService();
        java.lang.String pkg = mContext.getOpPackageName();
        android.widget.Toast.TN tn = mTN;
        tn.mNextView = mNextView;
        final int displayId = mContext.getDisplayId();
        try {
            service.enqueueToast(pkg, tn, mDuration, displayId);
        } catch (android.os.RemoteException e) {
            // Empty
        }
    }

    /**
     * Close the view if it's showing, or don't show it if it isn't showing yet.
     * You do not normally have to call this.  Normally view will disappear on its own
     * after the appropriate duration.
     */
    public void cancel() {
        mTN.cancel();
    }

    /**
     * Set the view to show.
     *
     * @see #getView
     */
    public void setView(android.view.View view) {
        mNextView = view;
    }

    /**
     * Return the view.
     *
     * @see #setView
     */
    public android.view.View getView() {
        return mNextView;
    }

    /**
     * Set how long to show the view for.
     *
     * @see #LENGTH_SHORT
     * @see #LENGTH_LONG
     */
    public void setDuration(@android.widget.Toast.Duration
    int duration) {
        mDuration = duration;
        mTN.mDuration = duration;
    }

    /**
     * Return the duration.
     *
     * @see #setDuration
     */
    @android.widget.Toast.Duration
    public int getDuration() {
        return mDuration;
    }

    /**
     * Set the margins of the view.
     *
     * @param horizontalMargin
     * 		The horizontal margin, in percentage of the
     * 		container width, between the container's edges and the
     * 		notification
     * @param verticalMargin
     * 		The vertical margin, in percentage of the
     * 		container height, between the container's edges and the
     * 		notification
     */
    public void setMargin(float horizontalMargin, float verticalMargin) {
        mTN.mHorizontalMargin = horizontalMargin;
        mTN.mVerticalMargin = verticalMargin;
    }

    /**
     * Return the horizontal margin.
     */
    public float getHorizontalMargin() {
        return mTN.mHorizontalMargin;
    }

    /**
     * Return the vertical margin.
     */
    public float getVerticalMargin() {
        return mTN.mVerticalMargin;
    }

    /**
     * Set the location at which the notification should appear on the screen.
     *
     * @see android.view.Gravity
     * @see #getGravity
     */
    public void setGravity(int gravity, int xOffset, int yOffset) {
        mTN.mGravity = gravity;
        mTN.mX = xOffset;
        mTN.mY = yOffset;
    }

    /**
     * Get the location at which the notification should appear on the screen.
     *
     * @see android.view.Gravity
     * @see #getGravity
     */
    public int getGravity() {
        return mTN.mGravity;
    }

    /**
     * Return the X offset in pixels to apply to the gravity's location.
     */
    public int getXOffset() {
        return mTN.mX;
    }

    /**
     * Return the Y offset in pixels to apply to the gravity's location.
     */
    public int getYOffset() {
        return mTN.mY;
    }

    /**
     * Gets the LayoutParams for the Toast window.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.view.WindowManager.LayoutParams getWindowParams() {
        return mTN.mParams;
    }

    /**
     * Make a standard toast that just contains a text view.
     *
     * @param context
     * 		The context to use.  Usually your {@link android.app.Application}
     * 		or {@link android.app.Activity} object.
     * @param text
     * 		The text to show.  Can be formatted text.
     * @param duration
     * 		How long to display the message.  Either {@link #LENGTH_SHORT} or
     * 		{@link #LENGTH_LONG}
     */
    public static android.widget.Toast makeText(android.content.Context context, java.lang.CharSequence text, @android.widget.Toast.Duration
    int duration) {
        return android.widget.Toast.makeText(context, null, text, duration);
    }

    /**
     * Make a standard toast to display using the specified looper.
     * If looper is null, Looper.myLooper() is used.
     *
     * @unknown 
     */
    public static android.widget.Toast makeText(@android.annotation.NonNull
    android.content.Context context, @android.annotation.Nullable
    android.os.Looper looper, @android.annotation.NonNull
    java.lang.CharSequence text, @android.widget.Toast.Duration
    int duration) {
        android.widget.Toast result = new android.widget.Toast(context, looper);
        android.view.LayoutInflater inflate = ((android.view.LayoutInflater) (context.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE)));
        android.view.View v = inflate.inflate(com.android.internal.R.layout.transient_notification, null);
        android.widget.TextView tv = ((android.widget.TextView) (v.findViewById(com.android.internal.R.id.message)));
        tv.setText(text);
        result.mNextView = v;
        result.mDuration = duration;
        return result;
    }

    /**
     * Make a standard toast that just contains a text view with the text from a resource.
     *
     * @param context
     * 		The context to use.  Usually your {@link android.app.Application}
     * 		or {@link android.app.Activity} object.
     * @param resId
     * 		The resource id of the string resource to use.  Can be formatted text.
     * @param duration
     * 		How long to display the message.  Either {@link #LENGTH_SHORT} or
     * 		{@link #LENGTH_LONG}
     * @throws Resources.NotFoundException
     * 		if the resource can't be found.
     */
    public static android.widget.Toast makeText(android.content.Context context, @android.annotation.StringRes
    int resId, @android.widget.Toast.Duration
    int duration) throws android.content.res.Resources.NotFoundException {
        return android.widget.Toast.makeText(context, context.getResources().getText(resId), duration);
    }

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     *
     * @param resId
     * 		The new text for the Toast.
     */
    public void setText(@android.annotation.StringRes
    int resId) {
        setText(mContext.getText(resId));
    }

    /**
     * Update the text in a Toast that was previously created using one of the makeText() methods.
     *
     * @param s
     * 		The new text for the Toast.
     */
    public void setText(java.lang.CharSequence s) {
        if (mNextView == null) {
            throw new java.lang.RuntimeException("This Toast was not created with Toast.makeText()");
        }
        android.widget.TextView tv = mNextView.findViewById(com.android.internal.R.id.message);
        if (tv == null) {
            throw new java.lang.RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    // =======================================================================================
    // All the gunk below is the interaction with the Notification Service, which handles
    // the proper ordering of these system-wide.
    // =======================================================================================
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private static android.app.INotificationManager sService;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
    private static android.app.INotificationManager getService() {
        if (android.widget.Toast.sService != null) {
            return android.widget.Toast.sService;
        }
        android.widget.Toast.sService = INotificationManager.Stub.asInterface(android.os.ServiceManager.getService("notification"));
        return android.widget.Toast.sService;
    }

    private static class TN extends android.app.ITransientNotification.Stub {
        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
        private final android.view.WindowManager.LayoutParams mParams = new android.view.WindowManager.LayoutParams();

        private static final int SHOW = 0;

        private static final int HIDE = 1;

        private static final int CANCEL = 2;

        final android.os.Handler mHandler;

        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
        int mGravity;

        int mX;

        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
        int mY;

        float mHorizontalMargin;

        float mVerticalMargin;

        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
        android.view.View mView;

        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P)
        android.view.View mNextView;

        int mDuration;

        android.view.WindowManager mWM;

        java.lang.String mPackageName;

        static final long SHORT_DURATION_TIMEOUT = 4000;

        static final long LONG_DURATION_TIMEOUT = 7000;

        TN(java.lang.String packageName, @android.annotation.Nullable
        android.os.Looper looper) {
            // XXX This should be changed to use a Dialog, with a Theme.Toast
            // defined that sets up the layout params appropriately.
            final android.view.WindowManager.LayoutParams params = mParams;
            params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            params.width = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            params.format = android.graphics.PixelFormat.TRANSLUCENT;
            params.windowAnimations = com.android.internal.R.style.Animation_Toast;
            params.type = android.view.WindowManager.LayoutParams.TYPE_TOAST;
            params.setTitle("Toast");
            params.flags = (android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE) | android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
            mPackageName = packageName;
            if (looper == null) {
                // Use Looper.myLooper() if looper is not specified.
                looper = android.os.Looper.myLooper();
                if (looper == null) {
                    throw new java.lang.RuntimeException("Can't toast on a thread that has not called Looper.prepare()");
                }
            }
            mHandler = new android.os.Handler(looper, null) {
                @java.lang.Override
                public void handleMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case android.widget.Toast.TN.SHOW :
                            {
                                android.os.IBinder token = ((android.os.IBinder) (msg.obj));
                                handleShow(token);
                                break;
                            }
                        case android.widget.Toast.TN.HIDE :
                            {
                                handleHide();
                                // Don't do this in handleHide() because it is also invoked by
                                // handleShow()
                                mNextView = null;
                                break;
                            }
                        case android.widget.Toast.TN.CANCEL :
                            {
                                handleHide();
                                // Don't do this in handleHide() because it is also invoked by
                                // handleShow()
                                mNextView = null;
                                try {
                                    android.widget.Toast.getService().cancelToast(mPackageName, android.widget.Toast.TN.this);
                                } catch (android.os.RemoteException e) {
                                }
                                break;
                            }
                    }
                }
            };
        }

        /**
         * schedule handleShow into the right thread
         */
        @java.lang.Override
        @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
        public void show(android.os.IBinder windowToken) {
            if (android.widget.Toast.localLOGV)
                android.util.Log.v(android.widget.Toast.TAG, "SHOW: " + this);

            mHandler.obtainMessage(android.widget.Toast.TN.SHOW, windowToken).sendToTarget();
        }

        /**
         * schedule handleHide into the right thread
         */
        @java.lang.Override
        public void hide() {
            if (android.widget.Toast.localLOGV)
                android.util.Log.v(android.widget.Toast.TAG, "HIDE: " + this);

            mHandler.obtainMessage(android.widget.Toast.TN.HIDE).sendToTarget();
        }

        public void cancel() {
            if (android.widget.Toast.localLOGV)
                android.util.Log.v(android.widget.Toast.TAG, "CANCEL: " + this);

            mHandler.obtainMessage(android.widget.Toast.TN.CANCEL).sendToTarget();
        }

        public void handleShow(android.os.IBinder windowToken) {
            if (android.widget.Toast.localLOGV)
                android.util.Log.v(android.widget.Toast.TAG, (((("HANDLE SHOW: " + this) + " mView=") + mView) + " mNextView=") + mNextView);

            // If a cancel/hide is pending - no need to show - at this point
            // the window token is already invalid and no need to do any work.
            if (mHandler.hasMessages(android.widget.Toast.TN.CANCEL) || mHandler.hasMessages(android.widget.Toast.TN.HIDE)) {
                return;
            }
            if (mView != mNextView) {
                // remove the old view if necessary
                handleHide();
                mView = mNextView;
                android.content.Context context = mView.getContext().getApplicationContext();
                java.lang.String packageName = mView.getContext().getOpPackageName();
                if (context == null) {
                    context = mView.getContext();
                }
                mWM = ((android.view.WindowManager) (context.getSystemService(android.content.Context.WINDOW_SERVICE)));
                // We can resolve the Gravity here by using the Locale for getting
                // the layout direction
                final android.content.res.Configuration config = mView.getContext().getResources().getConfiguration();
                final int gravity = android.view.Gravity.getAbsoluteGravity(mGravity, config.getLayoutDirection());
                mParams.gravity = gravity;
                if ((gravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) == android.view.Gravity.FILL_HORIZONTAL) {
                    mParams.horizontalWeight = 1.0F;
                }
                if ((gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == android.view.Gravity.FILL_VERTICAL) {
                    mParams.verticalWeight = 1.0F;
                }
                mParams.x = mX;
                mParams.y = mY;
                mParams.verticalMargin = mVerticalMargin;
                mParams.horizontalMargin = mHorizontalMargin;
                mParams.packageName = packageName;
                mParams.hideTimeoutMilliseconds = (mDuration == android.widget.Toast.LENGTH_LONG) ? android.widget.Toast.TN.LONG_DURATION_TIMEOUT : android.widget.Toast.TN.SHORT_DURATION_TIMEOUT;
                mParams.token = windowToken;
                if (mView.getParent() != null) {
                    if (android.widget.Toast.localLOGV)
                        android.util.Log.v(android.widget.Toast.TAG, (("REMOVE! " + mView) + " in ") + this);

                    mWM.removeView(mView);
                }
                if (android.widget.Toast.localLOGV)
                    android.util.Log.v(android.widget.Toast.TAG, (("ADD! " + mView) + " in ") + this);

                // Since the notification manager service cancels the token right
                // after it notifies us to cancel the toast there is an inherent
                // race and we may attempt to add a window after the token has been
                // invalidated. Let us hedge against that.
                try {
                    mWM.addView(mView, mParams);
                    trySendAccessibilityEvent();
                } catch (android.view.WindowManager.BadTokenException e) {
                    /* ignore */
                }
            }
        }

        private void trySendAccessibilityEvent() {
            android.view.accessibility.AccessibilityManager accessibilityManager = android.view.accessibility.AccessibilityManager.getInstance(mView.getContext());
            if (!accessibilityManager.isEnabled()) {
                return;
            }
            // treat toasts as notifications since they are used to
            // announce a transient piece of information to the user
            android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(android.view.accessibility.AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
            event.setClassName(getClass().getName());
            event.setPackageName(mView.getContext().getPackageName());
            mView.dispatchPopulateAccessibilityEvent(event);
            accessibilityManager.sendAccessibilityEvent(event);
        }

        @android.annotation.UnsupportedAppUsage
        public void handleHide() {
            if (android.widget.Toast.localLOGV)
                android.util.Log.v(android.widget.Toast.TAG, (("HANDLE HIDE: " + this) + " mView=") + mView);

            if (mView != null) {
                // note: checking parent() just to make sure the view has
                // been added...  i have seen cases where we get here when
                // the view isn't yet added, so let's try not to crash.
                if (mView.getParent() != null) {
                    if (android.widget.Toast.localLOGV)
                        android.util.Log.v(android.widget.Toast.TAG, (("REMOVE! " + mView) + " in ") + this);

                    mWM.removeViewImmediate(mView);
                }
                // Now that we've removed the view it's safe for the server to release
                // the resources.
                try {
                    android.widget.Toast.getService().finishToken(mPackageName, this);
                } catch (android.os.RemoteException e) {
                }
                mView = null;
            }
        }
    }
}

