/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.view.autofill;


/**
 * Custom {@link PopupWindow} used to isolate its content from the autofilled app - the
 * UI is rendered in a framework process, but it's controlled by the app.
 *
 * TODO(b/34943932): use an app surface control solution.
 *
 * @unknown 
 */
public class AutofillPopupWindow extends android.widget.PopupWindow {
    private static final java.lang.String TAG = "AutofillPopupWindow";

    private final android.view.autofill.AutofillPopupWindow.WindowPresenter mWindowPresenter;

    private android.view.WindowManager.LayoutParams mWindowLayoutParams;

    private boolean mFullScreen;

    private final android.view.View.OnAttachStateChangeListener mOnAttachStateChangeListener = new android.view.View.OnAttachStateChangeListener() {
        @java.lang.Override
        public void onViewAttachedToWindow(android.view.View v) {
            /* ignore - handled by the super class */
        }

        @java.lang.Override
        public void onViewDetachedFromWindow(android.view.View v) {
            dismiss();
        }
    };

    /**
     * Creates a popup window with a presenter owning the window and responsible for
     * showing/hiding/updating the backing window. This can be useful of the window is
     * being shown by another process while the popup logic is in the process hosting
     * the anchor view.
     * <p>
     * Using this constructor means that the presenter completely owns the content of
     * the window and the following methods manipulating the window content shouldn't
     * be used: {@link #getEnterTransition()}, {@link #setEnterTransition(Transition)},
     * {@link #getExitTransition()}, {@link #setExitTransition(Transition)},
     * {@link #getContentView()}, {@link #setContentView(View)}, {@link #getBackground()},
     * {@link #setBackgroundDrawable(Drawable)}, {@link #getElevation()},
     * {@link #setElevation(float)}, ({@link #getAnimationStyle()},
     * {@link #setAnimationStyle(int)}, {@link #setTouchInterceptor(OnTouchListener)}.</p>
     */
    public AutofillPopupWindow(@android.annotation.NonNull
    android.view.autofill.IAutofillWindowPresenter presenter) {
        mWindowPresenter = new android.view.autofill.AutofillPopupWindow.WindowPresenter(presenter);
        setTouchModal(false);
        setOutsideTouchable(true);
        setInputMethodMode(android.widget.PopupWindow.INPUT_METHOD_NOT_NEEDED);
        setFocusable(true);
    }

    @java.lang.Override
    protected boolean hasContentView() {
        return true;
    }

    @java.lang.Override
    protected boolean hasDecorView() {
        return true;
    }

    @java.lang.Override
    protected android.view.WindowManager.LayoutParams getDecorViewLayoutParams() {
        return mWindowLayoutParams;
    }

    /**
     * The effective {@code update} method that should be called by its clients.
     */
    public void update(android.view.View anchor, int offsetX, int offsetY, int width, int height, android.graphics.Rect virtualBounds) {
        mFullScreen = width == android.view.WindowManager.LayoutParams.MATCH_PARENT;
        // For no fullscreen autofill window, we want to show the window as system controlled one
        // so it covers app windows, but it has to be an application type (so it's contained inside
        // the application area). Hence, we set it to the application type with the highest z-order,
        // which currently is TYPE_APPLICATION_ABOVE_SUB_PANEL.
        // For fullscreen mode, autofill window is at the bottom of screen, it should not be
        // clipped by app activity window. Fullscreen autofill window does not need to follow app
        // anchor view position.
        setWindowLayoutType(mFullScreen ? android.view.WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG : android.view.WindowManager.LayoutParams.TYPE_APPLICATION_ABOVE_SUB_PANEL);
        // If we are showing the popup for a virtual view we use a fake view which
        // delegates to the anchor but present itself with the same bounds as the
        // virtual view. This ensures that the location logic in popup works
        // symmetrically when the dropdown is below and above the anchor.
        final android.view.View actualAnchor;
        if (mFullScreen) {
            offsetX = 0;
            offsetY = 0;
            // If it is not fullscreen height, put window at bottom. Computes absolute position.
            // Note that we cannot easily change default gravity from Gravity.TOP to
            // Gravity.BOTTOM because PopupWindow base class does not expose computeGravity().
            final android.graphics.Point outPoint = new android.graphics.Point();
            anchor.getContext().getDisplay().getSize(outPoint);
            width = outPoint.x;
            if (height != android.view.WindowManager.LayoutParams.MATCH_PARENT) {
                offsetY = outPoint.y - height;
            }
            actualAnchor = anchor;
        } else
            if (virtualBounds != null) {
                final int[] mLocationOnScreen = new int[]{ virtualBounds.left, virtualBounds.top };
                actualAnchor = new android.view.View(anchor.getContext()) {
                    @java.lang.Override
                    public void getLocationOnScreen(int[] location) {
                        location[0] = mLocationOnScreen[0];
                        location[1] = mLocationOnScreen[1];
                    }

                    @java.lang.Override
                    public int getAccessibilityViewId() {
                        return anchor.getAccessibilityViewId();
                    }

                    @java.lang.Override
                    public android.view.ViewTreeObserver getViewTreeObserver() {
                        return anchor.getViewTreeObserver();
                    }

                    @java.lang.Override
                    public android.os.IBinder getApplicationWindowToken() {
                        return anchor.getApplicationWindowToken();
                    }

                    @java.lang.Override
                    public android.view.View getRootView() {
                        return anchor.getRootView();
                    }

                    @java.lang.Override
                    public int getLayoutDirection() {
                        return anchor.getLayoutDirection();
                    }

                    @java.lang.Override
                    public void getWindowDisplayFrame(android.graphics.Rect outRect) {
                        anchor.getWindowDisplayFrame(outRect);
                    }

                    @java.lang.Override
                    public void addOnAttachStateChangeListener(android.view.View.OnAttachStateChangeListener listener) {
                        anchor.addOnAttachStateChangeListener(listener);
                    }

                    @java.lang.Override
                    public void removeOnAttachStateChangeListener(android.view.View.OnAttachStateChangeListener listener) {
                        anchor.removeOnAttachStateChangeListener(listener);
                    }

                    @java.lang.Override
                    public boolean isAttachedToWindow() {
                        return anchor.isAttachedToWindow();
                    }

                    @java.lang.Override
                    public boolean requestRectangleOnScreen(android.graphics.Rect rectangle, boolean immediate) {
                        return anchor.requestRectangleOnScreen(rectangle, immediate);
                    }

                    @java.lang.Override
                    public android.os.IBinder getWindowToken() {
                        return anchor.getWindowToken();
                    }
                };
                actualAnchor.setLeftTopRightBottom(virtualBounds.left, virtualBounds.top, virtualBounds.right, virtualBounds.bottom);
                actualAnchor.setScrollX(anchor.getScrollX());
                actualAnchor.setScrollY(anchor.getScrollY());
                anchor.setOnScrollChangeListener(( v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                    mLocationOnScreen[0] = mLocationOnScreen[0] - (scrollX - oldScrollX);
                    mLocationOnScreen[1] = mLocationOnScreen[1] - (scrollY - oldScrollY);
                });
                actualAnchor.setWillNotDraw(true);
            } else {
                actualAnchor = anchor;
            }

        if (!mFullScreen) {
            // No fullscreen window animation is controlled by PopupWindow.
            setAnimationStyle(-1);
        } else
            if (height == android.view.WindowManager.LayoutParams.MATCH_PARENT) {
                // Complete fullscreen autofill window has no animation.
                setAnimationStyle(0);
            } else {
                // Slide half screen height autofill window from bottom.
                setAnimationStyle(com.android.internal.R.style.AutofillHalfScreenAnimation);
            }

        if (!isShowing()) {
            setWidth(width);
            setHeight(height);
            showAsDropDown(actualAnchor, offsetX, offsetY);
        } else {
            update(actualAnchor, offsetX, offsetY, width, height);
        }
    }

    @java.lang.Override
    protected void update(android.view.View anchor, android.view.WindowManager.LayoutParams params) {
        final int layoutDirection = (anchor != null) ? anchor.getLayoutDirection() : android.view.View.LAYOUT_DIRECTION_LOCALE;
        mWindowPresenter.show(params, getTransitionEpicenter(), isLayoutInsetDecor(), layoutDirection);
    }

    @java.lang.Override
    protected boolean findDropDownPosition(android.view.View anchor, android.view.WindowManager.LayoutParams outParams, int xOffset, int yOffset, int width, int height, int gravity, boolean allowScroll) {
        if (mFullScreen) {
            // In fullscreen mode, don't need consider the anchor view.
            outParams.x = xOffset;
            outParams.y = yOffset;
            outParams.width = width;
            outParams.height = height;
            outParams.gravity = gravity;
            return false;
        }
        return super.findDropDownPosition(anchor, outParams, xOffset, yOffset, width, height, gravity, allowScroll);
    }

    @java.lang.Override
    public void showAsDropDown(android.view.View anchor, int xoff, int yoff, int gravity) {
        if (android.view.autofill.Helper.sVerbose) {
            android.util.Log.v(android.view.autofill.AutofillPopupWindow.TAG, (((((("showAsDropDown(): anchor=" + anchor) + ", xoff=") + xoff) + ", yoff=") + yoff) + ", isShowing(): ") + isShowing());
        }
        if (isShowing()) {
            return;
        }
        setShowing(true);
        setDropDown(true);
        attachToAnchor(anchor, xoff, yoff, gravity);
        final android.view.WindowManager.LayoutParams p = mWindowLayoutParams = createPopupLayoutParams(anchor.getWindowToken());
        final boolean aboveAnchor = findDropDownPosition(anchor, p, xoff, yoff, p.width, p.height, gravity, getAllowScrollingAnchorParent());
        updateAboveAnchor(aboveAnchor);
        p.accessibilityIdOfAnchor = anchor.getAccessibilityViewId();
        p.packageName = anchor.getContext().getPackageName();
        mWindowPresenter.show(p, getTransitionEpicenter(), isLayoutInsetDecor(), anchor.getLayoutDirection());
    }

    @java.lang.Override
    protected void attachToAnchor(android.view.View anchor, int xoff, int yoff, int gravity) {
        super.attachToAnchor(anchor, xoff, yoff, gravity);
        anchor.addOnAttachStateChangeListener(mOnAttachStateChangeListener);
    }

    @java.lang.Override
    protected void detachFromAnchor() {
        final android.view.View anchor = getAnchor();
        if (anchor != null) {
            anchor.removeOnAttachStateChangeListener(mOnAttachStateChangeListener);
        }
        super.detachFromAnchor();
    }

    @java.lang.Override
    public void dismiss() {
        if ((!isShowing()) || isTransitioningToDismiss()) {
            return;
        }
        setShowing(false);
        setTransitioningToDismiss(true);
        mWindowPresenter.hide(getTransitionEpicenter());
        detachFromAnchor();
        if (getOnDismissListener() != null) {
            getOnDismissListener().onDismiss();
        }
    }

    @java.lang.Override
    public int getAnimationStyle() {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getBackground() {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public android.view.View getContentView() {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public float getElevation() {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public android.transition.Transition getEnterTransition() {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public android.transition.Transition getExitTransition() {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public void setBackgroundDrawable(android.graphics.drawable.Drawable background) {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public void setContentView(android.view.View contentView) {
        if (contentView != null) {
            throw new java.lang.IllegalStateException("You can't call this!");
        }
    }

    @java.lang.Override
    public void setElevation(float elevation) {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public void setEnterTransition(android.transition.Transition enterTransition) {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public void setExitTransition(android.transition.Transition exitTransition) {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    @java.lang.Override
    public void setTouchInterceptor(android.view.View.OnTouchListener l) {
        throw new java.lang.IllegalStateException("You can't call this!");
    }

    /**
     * Contract between the popup window and a presenter that is responsible for
     * showing/hiding/updating the actual window.
     *
     * <p>This can be useful if the anchor is in one process and the backing window is owned by
     * another process.
     */
    private class WindowPresenter {
        final android.view.autofill.IAutofillWindowPresenter mPresenter;

        WindowPresenter(android.view.autofill.IAutofillWindowPresenter presenter) {
            mPresenter = presenter;
        }

        /**
         * Shows the backing window.
         *
         * @param p
         * 		The window layout params.
         * @param transitionEpicenter
         * 		The transition epicenter if animating.
         * @param fitsSystemWindows
         * 		Whether the content view should account for system decorations.
         * @param layoutDirection
         * 		The content layout direction to be consistent with the anchor.
         */
        void show(android.view.WindowManager.LayoutParams p, android.graphics.Rect transitionEpicenter, boolean fitsSystemWindows, int layoutDirection) {
            try {
                mPresenter.show(p, transitionEpicenter, fitsSystemWindows, layoutDirection);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.view.autofill.AutofillPopupWindow.TAG, "Error showing fill window", e);
                e.rethrowFromSystemServer();
            }
        }

        /**
         * Hides the backing window.
         *
         * @param transitionEpicenter
         * 		The transition epicenter if animating.
         */
        void hide(android.graphics.Rect transitionEpicenter) {
            try {
                mPresenter.hide(transitionEpicenter);
            } catch (android.os.RemoteException e) {
                android.util.Log.w(android.view.autofill.AutofillPopupWindow.TAG, "Error hiding fill window", e);
                e.rethrowFromSystemServer();
            }
        }
    }
}

