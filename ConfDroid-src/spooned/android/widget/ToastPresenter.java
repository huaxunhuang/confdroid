/**
 * Copyright (C) 2020 The Android Open Source Project
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
 * Class responsible for toast presentation inside app's process and in system UI.
 *
 * @unknown 
 */
public class ToastPresenter {
    private static final java.lang.String TAG = "ToastPresenter";

    private static final java.lang.String WINDOW_TITLE = "Toast";

    private static final long SHORT_DURATION_TIMEOUT = 4000;

    private static final long LONG_DURATION_TIMEOUT = 7000;

    @com.android.internal.annotations.VisibleForTesting
    public static final int TEXT_TOAST_LAYOUT = R.layout.transient_notification;

    /**
     * Returns the default text toast view for message {@code text}.
     */
    public static android.view.View getTextToastView(android.content.Context context, java.lang.CharSequence text) {
        android.view.View view = android.view.LayoutInflater.from(context).inflate(android.widget.ToastPresenter.TEXT_TOAST_LAYOUT, null);
        android.widget.TextView textView = view.findViewById(com.android.internal.R.id.message);
        textView.setText(text);
        return view;
    }

    private final android.content.Context mContext;

    private final android.content.res.Resources mResources;

    private final android.view.WindowManager mWindowManager;

    private final android.view.accessibility.AccessibilityManager mAccessibilityManager;

    private final android.app.INotificationManager mNotificationManager;

    private final java.lang.String mPackageName;

    private final android.view.WindowManager.LayoutParams mParams;

    @android.annotation.Nullable
    private android.view.View mView;

    @android.annotation.Nullable
    private android.os.IBinder mToken;

    public ToastPresenter(android.content.Context context, android.view.accessibility.IAccessibilityManager accessibilityManager, android.app.INotificationManager notificationManager, java.lang.String packageName) {
        mContext = context;
        mResources = context.getResources();
        mWindowManager = context.getSystemService(android.view.WindowManager.class);
        mNotificationManager = notificationManager;
        mPackageName = packageName;
        // We obtain AccessibilityManager manually via its constructor instead of using method
        // AccessibilityManager.getInstance() for 2 reasons:
        // 1. We want to be able to inject IAccessibilityManager in tests to verify behavior.
        // 2. getInstance() caches the instance for the process even if we pass a different
        // context to it. This is problematic for multi-user because callers can pass a context
        // created via Context.createContextAsUser().
        mAccessibilityManager = new android.view.accessibility.AccessibilityManager(context, accessibilityManager, context.getUserId());
        mParams = createLayoutParams();
    }

    public java.lang.String getPackageName() {
        return mPackageName;
    }

    public android.view.WindowManager.LayoutParams getLayoutParams() {
        return mParams;
    }

    /**
     * Returns the {@link View} being shown at the moment or {@code null} if no toast is being
     * displayed.
     */
    @android.annotation.Nullable
    public android.view.View getView() {
        return mView;
    }

    /**
     * Returns the {@link IBinder} token used to display the toast or {@code null} if there is no
     * toast being shown at the moment.
     */
    @android.annotation.Nullable
    public android.os.IBinder getToken() {
        return mToken;
    }

    /**
     * Creates {@link WindowManager.LayoutParams} with default values for toasts.
     */
    private android.view.WindowManager.LayoutParams createLayoutParams() {
        android.view.WindowManager.LayoutParams params = new android.view.WindowManager.LayoutParams();
        params.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = android.graphics.PixelFormat.TRANSLUCENT;
        params.windowAnimations = R.style.Animation_Toast;
        params.type = android.view.WindowManager.LayoutParams.TYPE_TOAST;
        params.setFitInsetsIgnoringVisibility(true);
        params.setTitle(android.widget.ToastPresenter.WINDOW_TITLE);
        params.flags = (android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE) | android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        setShowForAllUsersIfApplicable(params, mPackageName);
        return params;
    }

    /**
     * Customizes {@code params} according to other parameters, ready to be passed to {@link WindowManager#addView(View, ViewGroup.LayoutParams)}.
     */
    private void adjustLayoutParams(android.view.WindowManager.LayoutParams params, android.os.IBinder windowToken, int duration, int gravity, int xOffset, int yOffset, float horizontalMargin, float verticalMargin) {
        android.content.res.Configuration config = mResources.getConfiguration();
        int absGravity = android.view.Gravity.getAbsoluteGravity(gravity, config.getLayoutDirection());
        params.gravity = absGravity;
        if ((absGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) == android.view.Gravity.FILL_HORIZONTAL) {
            params.horizontalWeight = 1.0F;
        }
        if ((absGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK) == android.view.Gravity.FILL_VERTICAL) {
            params.verticalWeight = 1.0F;
        }
        params.x = xOffset;
        params.y = yOffset;
        params.horizontalMargin = horizontalMargin;
        params.verticalMargin = verticalMargin;
        params.packageName = mContext.getPackageName();
        params.hideTimeoutMilliseconds = (duration == android.widget.Toast.LENGTH_LONG) ? android.widget.ToastPresenter.LONG_DURATION_TIMEOUT : android.widget.ToastPresenter.SHORT_DURATION_TIMEOUT;
        params.token = windowToken;
    }

    /**
     * Sets {@link WindowManager.LayoutParams#SYSTEM_FLAG_SHOW_FOR_ALL_USERS} flag if {@code packageName} is a cross-user package.
     *
     * <p>Implementation note:
     *     This code is safe to be executed in SystemUI and the app's process:
     *         <li>SystemUI: It's running on a trusted domain so apps can't tamper with it. SystemUI
     *             has the permission INTERNAL_SYSTEM_WINDOW needed by the flag, so SystemUI can add
     *             the flag on behalf of those packages, which all contain INTERNAL_SYSTEM_WINDOW
     *             permission.
     *         <li>App: The flag being added is protected behind INTERNAL_SYSTEM_WINDOW permission
     *             and any app can already add that flag via getWindowParams() if it has that
     *             permission, so we are just doing this automatically for cross-user packages.
     */
    private void setShowForAllUsersIfApplicable(android.view.WindowManager.LayoutParams params, java.lang.String packageName) {
        if (isCrossUserPackage(packageName)) {
            params.privateFlags = android.view.WindowManager.LayoutParams.SYSTEM_FLAG_SHOW_FOR_ALL_USERS;
        }
    }

    private boolean isCrossUserPackage(java.lang.String packageName) {
        java.lang.String[] packages = mResources.getStringArray(R.array.config_toastCrossUserPackages);
        return com.android.internal.util.ArrayUtils.contains(packages, packageName);
    }

    /**
     * Shows the toast in {@code view} with the parameters passed and callback {@code callback}.
     */
    public void show(android.view.View view, android.os.IBinder token, android.os.IBinder windowToken, int duration, int gravity, int xOffset, int yOffset, float horizontalMargin, float verticalMargin, @android.annotation.Nullable
    android.app.ITransientNotificationCallback callback) {
        checkState(mView == null, "Only one toast at a time is allowed, call hide() first.");
        mView = view;
        mToken = token;
        adjustLayoutParams(mParams, windowToken, duration, gravity, xOffset, yOffset, horizontalMargin, verticalMargin);
        if (mView.getParent() != null) {
            mWindowManager.removeView(mView);
        }
        try {
            mWindowManager.addView(mView, mParams);
        } catch (android.view.WindowManager.BadTokenException e) {
            // Since the notification manager service cancels the token right after it notifies us
            // to cancel the toast there is an inherent race and we may attempt to add a window
            // after the token has been invalidated. Let us hedge against that.
            android.util.Log.w(android.widget.ToastPresenter.TAG, "Error while attempting to show toast from " + mPackageName, e);
            return;
        }
        trySendAccessibilityEvent(mView, mPackageName);
        if (callback != null) {
            try {
                callback.onToastShown();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.widget.ToastPresenter.TAG, ("Error calling back " + mPackageName) + " to notify onToastShow()", e);
            }
        }
    }

    /**
     * Hides toast that was shown using {@link #show(View, IBinder, IBinder, int,
     * int, int, int, float, float, ITransientNotificationCallback)}.
     *
     * <p>This method has to be called on the same thread on which {@link #show(View, IBinder,
     * IBinder, int, int, int, int, float, float, ITransientNotificationCallback)} was called.
     */
    public void hide(@android.annotation.Nullable
    android.app.ITransientNotificationCallback callback) {
        checkState(mView != null, "No toast to hide.");
        if (mView.getParent() != null) {
            mWindowManager.removeViewImmediate(mView);
        }
        try {
            mNotificationManager.finishToken(mPackageName, mToken);
        } catch (android.os.RemoteException e) {
            android.util.Log.w(android.widget.ToastPresenter.TAG, "Error finishing toast window token from package " + mPackageName, e);
        }
        if (callback != null) {
            try {
                callback.onToastHidden();
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.widget.ToastPresenter.TAG, ("Error calling back " + mPackageName) + " to notify onToastHide()", e);
            }
        }
        mView = null;
        mToken = null;
    }

    /**
     * Sends {@link AccessibilityEvent#TYPE_NOTIFICATION_STATE_CHANGED} event if accessibility is
     * enabled.
     */
    public void trySendAccessibilityEvent(android.view.View view, java.lang.String packageName) {
        if (!mAccessibilityManager.isEnabled()) {
            return;
        }
        android.view.accessibility.AccessibilityEvent event = android.view.accessibility.AccessibilityEvent.obtain(android.view.accessibility.AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED);
        event.setClassName(android.widget.Toast.class.getName());
        event.setPackageName(packageName);
        view.dispatchPopulateAccessibilityEvent(event);
        mAccessibilityManager.sendAccessibilityEvent(event);
    }
}

