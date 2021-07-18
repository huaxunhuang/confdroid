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
package android.view;


/**
 * A view tree observer is used to register listeners that can be notified of global
 * changes in the view tree. Such global events include, but are not limited to,
 * layout of the whole tree, beginning of the drawing pass, touch mode change....
 *
 * A ViewTreeObserver should never be instantiated by applications as it is provided
 * by the views hierarchy. Refer to {@link android.view.View#getViewTreeObserver()}
 * for more information.
 */
public final class ViewTreeObserver {
    // Recursive listeners use CopyOnWriteArrayList
    private java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnWindowFocusChangeListener> mOnWindowFocusListeners;

    private java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnWindowAttachListener> mOnWindowAttachListeners;

    private java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnGlobalFocusChangeListener> mOnGlobalFocusListeners;

    @android.annotation.UnsupportedAppUsage
    private java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnTouchModeChangeListener> mOnTouchModeChangeListeners;

    private java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnEnterAnimationCompleteListener> mOnEnterAnimationCompleteListeners;

    // Non-recursive listeners use CopyOnWriteArray
    // Any listener invoked from ViewRootImpl.performTraversals() should not be recursive
    @android.annotation.UnsupportedAppUsage
    private android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnGlobalLayoutListener> mOnGlobalLayoutListeners;

    @android.annotation.UnsupportedAppUsage
    private android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnComputeInternalInsetsListener> mOnComputeInternalInsetsListeners;

    @android.annotation.UnsupportedAppUsage
    private android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnScrollChangedListener> mOnScrollChangedListeners;

    private android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnPreDrawListener> mOnPreDrawListeners;

    private android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnWindowShownListener> mOnWindowShownListeners;

    private android.view.ViewTreeObserver.CopyOnWriteArray<java.util.function.Consumer<java.util.List<android.graphics.Rect>>> mGestureExclusionListeners;

    // These listeners cannot be mutated during dispatch
    private boolean mInDispatchOnDraw;

    private java.util.ArrayList<android.view.ViewTreeObserver.OnDrawListener> mOnDrawListeners;

    private static boolean sIllegalOnDrawModificationIsFatal;

    // These listeners are one-shot
    private java.util.ArrayList<java.lang.Runnable> mOnFrameCommitListeners;

    /**
     * Remains false until #dispatchOnWindowShown() is called. If a listener registers after
     * that the listener will be immediately called.
     */
    private boolean mWindowShown;

    private boolean mAlive = true;

    /**
     * Interface definition for a callback to be invoked when the view hierarchy is
     * attached to and detached from its window.
     */
    public interface OnWindowAttachListener {
        /**
         * Callback method to be invoked when the view hierarchy is attached to a window
         */
        public void onWindowAttached();

        /**
         * Callback method to be invoked when the view hierarchy is detached from a window
         */
        public void onWindowDetached();
    }

    /**
     * Interface definition for a callback to be invoked when the view hierarchy's window
     * focus state changes.
     */
    public interface OnWindowFocusChangeListener {
        /**
         * Callback method to be invoked when the window focus changes in the view tree.
         *
         * @param hasFocus
         * 		Set to true if the window is gaining focus, false if it is
         * 		losing focus.
         */
        public void onWindowFocusChanged(boolean hasFocus);
    }

    /**
     * Interface definition for a callback to be invoked when the focus state within
     * the view tree changes.
     */
    public interface OnGlobalFocusChangeListener {
        /**
         * Callback method to be invoked when the focus changes in the view tree. When
         * the view tree transitions from touch mode to non-touch mode, oldFocus is null.
         * When the view tree transitions from non-touch mode to touch mode, newFocus is
         * null. When focus changes in non-touch mode (without transition from or to
         * touch mode) either oldFocus or newFocus can be null.
         *
         * @param oldFocus
         * 		The previously focused view, if any.
         * @param newFocus
         * 		The newly focused View, if any.
         */
        public void onGlobalFocusChanged(android.view.View oldFocus, android.view.View newFocus);
    }

    /**
     * Interface definition for a callback to be invoked when the global layout state
     * or the visibility of views within the view tree changes.
     */
    public interface OnGlobalLayoutListener {
        /**
         * Callback method to be invoked when the global layout state or the visibility of views
         * within the view tree changes
         */
        public void onGlobalLayout();
    }

    /**
     * Interface definition for a callback to be invoked when the view tree is about to be drawn.
     */
    public interface OnPreDrawListener {
        /**
         * Callback method to be invoked when the view tree is about to be drawn. At this point, all
         * views in the tree have been measured and given a frame. Clients can use this to adjust
         * their scroll bounds or even to request a new layout before drawing occurs.
         *
         * @return Return true to proceed with the current drawing pass, or false to cancel.
         * @see android.view.View#onMeasure
         * @see android.view.View#onLayout
         * @see android.view.View#onDraw
         */
        public boolean onPreDraw();
    }

    /**
     * Interface definition for a callback to be invoked when the view tree is about to be drawn.
     */
    public interface OnDrawListener {
        /**
         * <p>Callback method to be invoked when the view tree is about to be drawn. At this point,
         * views cannot be modified in any way.</p>
         *
         * <p>Unlike with {@link OnPreDrawListener}, this method cannot be used to cancel the
         * current drawing pass.</p>
         *
         * <p>An {@link OnDrawListener} listener <strong>cannot be added or removed</strong>
         * from this method.</p>
         *
         * @see android.view.View#onMeasure
         * @see android.view.View#onLayout
         * @see android.view.View#onDraw
         */
        public void onDraw();
    }

    /**
     * Interface definition for a callback to be invoked when the touch mode changes.
     */
    public interface OnTouchModeChangeListener {
        /**
         * Callback method to be invoked when the touch mode changes.
         *
         * @param isInTouchMode
         * 		True if the view hierarchy is now in touch mode, false  otherwise.
         */
        public void onTouchModeChanged(boolean isInTouchMode);
    }

    /**
     * Interface definition for a callback to be invoked when
     * something in the view tree has been scrolled.
     */
    public interface OnScrollChangedListener {
        /**
         * Callback method to be invoked when something in the view tree
         * has been scrolled.
         */
        public void onScrollChanged();
    }

    /**
     * Interface definition for a callback noting when a system window has been displayed.
     * This is only used for non-Activity windows. Activity windows can use
     * Activity.onEnterAnimationComplete() to get the same signal.
     *
     * @unknown 
     */
    public interface OnWindowShownListener {
        /**
         * Callback method to be invoked when a non-activity window is fully shown.
         */
        void onWindowShown();
    }

    /**
     * Parameters used with OnComputeInternalInsetsListener.
     *
     * We are not yet ready to commit to this API and support it, so
     *
     * @unknown 
     */
    public static final class InternalInsetsInfo {
        /**
         * Offsets from the frame of the window at which the content of
         * windows behind it should be placed.
         */
        @android.annotation.UnsupportedAppUsage
        public final android.graphics.Rect contentInsets = new android.graphics.Rect();

        /**
         * Offsets from the frame of the window at which windows behind it
         * are visible.
         */
        @android.annotation.UnsupportedAppUsage
        public final android.graphics.Rect visibleInsets = new android.graphics.Rect();

        /**
         * Touchable region defined relative to the origin of the frame of the window.
         * Only used when {@link #setTouchableInsets(int)} is called with
         * the option {@link #TOUCHABLE_INSETS_REGION}.
         */
        @android.annotation.UnsupportedAppUsage
        public final android.graphics.Region touchableRegion = new android.graphics.Region();

        /**
         * Option for {@link #setTouchableInsets(int)}: the entire window frame
         * can be touched.
         */
        public static final int TOUCHABLE_INSETS_FRAME = 0;

        /**
         * Option for {@link #setTouchableInsets(int)}: the area inside of
         * the content insets can be touched.
         */
        public static final int TOUCHABLE_INSETS_CONTENT = 1;

        /**
         * Option for {@link #setTouchableInsets(int)}: the area inside of
         * the visible insets can be touched.
         */
        public static final int TOUCHABLE_INSETS_VISIBLE = 2;

        /**
         * Option for {@link #setTouchableInsets(int)}: the area inside of
         * the provided touchable region in {@link #touchableRegion} can be touched.
         */
        @android.annotation.UnsupportedAppUsage
        public static final int TOUCHABLE_INSETS_REGION = 3;

        /**
         * Set which parts of the window can be touched: either
         * {@link #TOUCHABLE_INSETS_FRAME}, {@link #TOUCHABLE_INSETS_CONTENT},
         * {@link #TOUCHABLE_INSETS_VISIBLE}, or {@link #TOUCHABLE_INSETS_REGION}.
         */
        @android.annotation.UnsupportedAppUsage
        public void setTouchableInsets(int val) {
            mTouchableInsets = val;
        }

        @android.annotation.UnsupportedAppUsage
        int mTouchableInsets;

        void reset() {
            contentInsets.setEmpty();
            visibleInsets.setEmpty();
            touchableRegion.setEmpty();
            mTouchableInsets = android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_FRAME;
        }

        boolean isEmpty() {
            return ((contentInsets.isEmpty() && visibleInsets.isEmpty()) && touchableRegion.isEmpty()) && (mTouchableInsets == android.view.ViewTreeObserver.InternalInsetsInfo.TOUCHABLE_INSETS_FRAME);
        }

        @java.lang.Override
        public int hashCode() {
            int result = contentInsets.hashCode();
            result = (31 * result) + visibleInsets.hashCode();
            result = (31 * result) + touchableRegion.hashCode();
            result = (31 * result) + mTouchableInsets;
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (this == o)
                return true;

            if ((o == null) || (getClass() != o.getClass()))
                return false;

            android.view.ViewTreeObserver.InternalInsetsInfo other = ((android.view.ViewTreeObserver.InternalInsetsInfo) (o));
            return (((mTouchableInsets == other.mTouchableInsets) && contentInsets.equals(other.contentInsets)) && visibleInsets.equals(other.visibleInsets)) && touchableRegion.equals(other.touchableRegion);
        }

        @android.annotation.UnsupportedAppUsage
        void set(android.view.ViewTreeObserver.InternalInsetsInfo other) {
            contentInsets.set(other.contentInsets);
            visibleInsets.set(other.visibleInsets);
            touchableRegion.set(other.touchableRegion);
            mTouchableInsets = other.mTouchableInsets;
        }
    }

    /**
     * Interface definition for a callback to be invoked when layout has
     * completed and the client can compute its interior insets.
     *
     * We are not yet ready to commit to this API and support it, so
     *
     * @unknown 
     */
    public interface OnComputeInternalInsetsListener {
        /**
         * Callback method to be invoked when layout has completed and the
         * client can compute its interior insets.
         *
         * @param inoutInfo
         * 		Should be filled in by the implementation with
         * 		the information about the insets of the window.  This is called
         * 		with whatever values the previous OnComputeInternalInsetsListener
         * 		returned, if there are multiple such listeners in the window.
         */
        public void onComputeInternalInsets(android.view.ViewTreeObserver.InternalInsetsInfo inoutInfo);
    }

    /**
     *
     *
     * @unknown 
     */
    public interface OnEnterAnimationCompleteListener {
        public void onEnterAnimationComplete();
    }

    /**
     * Creates a new ViewTreeObserver. This constructor should not be called
     */
    ViewTreeObserver(android.content.Context context) {
        android.view.ViewTreeObserver.sIllegalOnDrawModificationIsFatal = context.getApplicationInfo().targetSdkVersion >= Build.VERSION_CODES.O;
    }

    /**
     * Merges all the listeners registered on the specified observer with the listeners
     * registered on this object. After this method is invoked, the specified observer
     * will return false in {@link #isAlive()} and should not be used anymore.
     *
     * @param observer
     * 		The ViewTreeObserver whose listeners must be added to this observer
     */
    void merge(android.view.ViewTreeObserver observer) {
        if (observer.mOnWindowAttachListeners != null) {
            if (mOnWindowAttachListeners != null) {
                mOnWindowAttachListeners.addAll(observer.mOnWindowAttachListeners);
            } else {
                mOnWindowAttachListeners = observer.mOnWindowAttachListeners;
            }
        }
        if (observer.mOnWindowFocusListeners != null) {
            if (mOnWindowFocusListeners != null) {
                mOnWindowFocusListeners.addAll(observer.mOnWindowFocusListeners);
            } else {
                mOnWindowFocusListeners = observer.mOnWindowFocusListeners;
            }
        }
        if (observer.mOnGlobalFocusListeners != null) {
            if (mOnGlobalFocusListeners != null) {
                mOnGlobalFocusListeners.addAll(observer.mOnGlobalFocusListeners);
            } else {
                mOnGlobalFocusListeners = observer.mOnGlobalFocusListeners;
            }
        }
        if (observer.mOnGlobalLayoutListeners != null) {
            if (mOnGlobalLayoutListeners != null) {
                mOnGlobalLayoutListeners.addAll(observer.mOnGlobalLayoutListeners);
            } else {
                mOnGlobalLayoutListeners = observer.mOnGlobalLayoutListeners;
            }
        }
        if (observer.mOnPreDrawListeners != null) {
            if (mOnPreDrawListeners != null) {
                mOnPreDrawListeners.addAll(observer.mOnPreDrawListeners);
            } else {
                mOnPreDrawListeners = observer.mOnPreDrawListeners;
            }
        }
        if (observer.mOnDrawListeners != null) {
            if (mOnDrawListeners != null) {
                mOnDrawListeners.addAll(observer.mOnDrawListeners);
            } else {
                mOnDrawListeners = observer.mOnDrawListeners;
            }
        }
        if (observer.mOnFrameCommitListeners != null) {
            if (mOnFrameCommitListeners != null) {
                mOnFrameCommitListeners.addAll(observer.captureFrameCommitCallbacks());
            } else {
                mOnFrameCommitListeners = observer.captureFrameCommitCallbacks();
            }
        }
        if (observer.mOnTouchModeChangeListeners != null) {
            if (mOnTouchModeChangeListeners != null) {
                mOnTouchModeChangeListeners.addAll(observer.mOnTouchModeChangeListeners);
            } else {
                mOnTouchModeChangeListeners = observer.mOnTouchModeChangeListeners;
            }
        }
        if (observer.mOnComputeInternalInsetsListeners != null) {
            if (mOnComputeInternalInsetsListeners != null) {
                mOnComputeInternalInsetsListeners.addAll(observer.mOnComputeInternalInsetsListeners);
            } else {
                mOnComputeInternalInsetsListeners = observer.mOnComputeInternalInsetsListeners;
            }
        }
        if (observer.mOnScrollChangedListeners != null) {
            if (mOnScrollChangedListeners != null) {
                mOnScrollChangedListeners.addAll(observer.mOnScrollChangedListeners);
            } else {
                mOnScrollChangedListeners = observer.mOnScrollChangedListeners;
            }
        }
        if (observer.mOnWindowShownListeners != null) {
            if (mOnWindowShownListeners != null) {
                mOnWindowShownListeners.addAll(observer.mOnWindowShownListeners);
            } else {
                mOnWindowShownListeners = observer.mOnWindowShownListeners;
            }
        }
        if (observer.mGestureExclusionListeners != null) {
            if (mGestureExclusionListeners != null) {
                mGestureExclusionListeners.addAll(observer.mGestureExclusionListeners);
            } else {
                mGestureExclusionListeners = observer.mGestureExclusionListeners;
            }
        }
        observer.kill();
    }

    /**
     * Register a callback to be invoked when the view hierarchy is attached to a window.
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnWindowAttachListener(android.view.ViewTreeObserver.OnWindowAttachListener listener) {
        checkIsAlive();
        if (mOnWindowAttachListeners == null) {
            mOnWindowAttachListeners = new java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnWindowAttachListener>();
        }
        mOnWindowAttachListeners.add(listener);
    }

    /**
     * Remove a previously installed window attach callback.
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnWindowAttachListener(android.view.ViewTreeObserver.OnWindowAttachListener)
     */
    public void removeOnWindowAttachListener(android.view.ViewTreeObserver.OnWindowAttachListener victim) {
        checkIsAlive();
        if (mOnWindowAttachListeners == null) {
            return;
        }
        mOnWindowAttachListeners.remove(victim);
    }

    /**
     * Register a callback to be invoked when the window focus state within the view tree changes.
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnWindowFocusChangeListener(android.view.ViewTreeObserver.OnWindowFocusChangeListener listener) {
        checkIsAlive();
        if (mOnWindowFocusListeners == null) {
            mOnWindowFocusListeners = new java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnWindowFocusChangeListener>();
        }
        mOnWindowFocusListeners.add(listener);
    }

    /**
     * Remove a previously installed window focus change callback.
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnWindowFocusChangeListener(android.view.ViewTreeObserver.OnWindowFocusChangeListener)
     */
    public void removeOnWindowFocusChangeListener(android.view.ViewTreeObserver.OnWindowFocusChangeListener victim) {
        checkIsAlive();
        if (mOnWindowFocusListeners == null) {
            return;
        }
        mOnWindowFocusListeners.remove(victim);
    }

    /**
     * Register a callback to be invoked when the focus state within the view tree changes.
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnGlobalFocusChangeListener(android.view.ViewTreeObserver.OnGlobalFocusChangeListener listener) {
        checkIsAlive();
        if (mOnGlobalFocusListeners == null) {
            mOnGlobalFocusListeners = new java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnGlobalFocusChangeListener>();
        }
        mOnGlobalFocusListeners.add(listener);
    }

    /**
     * Remove a previously installed focus change callback.
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnGlobalFocusChangeListener(OnGlobalFocusChangeListener)
     */
    public void removeOnGlobalFocusChangeListener(android.view.ViewTreeObserver.OnGlobalFocusChangeListener victim) {
        checkIsAlive();
        if (mOnGlobalFocusListeners == null) {
            return;
        }
        mOnGlobalFocusListeners.remove(victim);
    }

    /**
     * Register a callback to be invoked when the global layout state or the visibility of views
     * within the view tree changes
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnGlobalLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener listener) {
        checkIsAlive();
        if (mOnGlobalLayoutListeners == null) {
            mOnGlobalLayoutListeners = new android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnGlobalLayoutListener>();
        }
        mOnGlobalLayoutListeners.add(listener);
    }

    /**
     * Remove a previously installed global layout callback
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @deprecated Use #removeOnGlobalLayoutListener instead
     * @see #addOnGlobalLayoutListener(OnGlobalLayoutListener)
     */
    @java.lang.Deprecated
    public void removeGlobalOnLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener victim) {
        removeOnGlobalLayoutListener(victim);
    }

    /**
     * Remove a previously installed global layout callback
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnGlobalLayoutListener(OnGlobalLayoutListener)
     */
    public void removeOnGlobalLayoutListener(android.view.ViewTreeObserver.OnGlobalLayoutListener victim) {
        checkIsAlive();
        if (mOnGlobalLayoutListeners == null) {
            return;
        }
        mOnGlobalLayoutListeners.remove(victim);
    }

    /**
     * Register a callback to be invoked when the view tree is about to be drawn
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnPreDrawListener(android.view.ViewTreeObserver.OnPreDrawListener listener) {
        checkIsAlive();
        if (mOnPreDrawListeners == null) {
            mOnPreDrawListeners = new android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnPreDrawListener>();
        }
        mOnPreDrawListeners.add(listener);
    }

    /**
     * Remove a previously installed pre-draw callback
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnPreDrawListener(OnPreDrawListener)
     */
    public void removeOnPreDrawListener(android.view.ViewTreeObserver.OnPreDrawListener victim) {
        checkIsAlive();
        if (mOnPreDrawListeners == null) {
            return;
        }
        mOnPreDrawListeners.remove(victim);
    }

    /**
     * Register a callback to be invoked when the view tree window has been shown
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @unknown 
     */
    public void addOnWindowShownListener(android.view.ViewTreeObserver.OnWindowShownListener listener) {
        checkIsAlive();
        if (mOnWindowShownListeners == null) {
            mOnWindowShownListeners = new android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnWindowShownListener>();
        }
        mOnWindowShownListeners.add(listener);
        if (mWindowShown) {
            listener.onWindowShown();
        }
    }

    /**
     * Remove a previously installed window shown callback
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnWindowShownListener(OnWindowShownListener)
     * @unknown 
     */
    public void removeOnWindowShownListener(android.view.ViewTreeObserver.OnWindowShownListener victim) {
        checkIsAlive();
        if (mOnWindowShownListeners == null) {
            return;
        }
        mOnWindowShownListeners.remove(victim);
    }

    /**
     * <p>Register a callback to be invoked when the view tree is about to be drawn.</p>
     * <p><strong>Note:</strong> this method <strong>cannot</strong> be invoked from
     * {@link android.view.ViewTreeObserver.OnDrawListener#onDraw()}.</p>
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnDrawListener(android.view.ViewTreeObserver.OnDrawListener listener) {
        checkIsAlive();
        if (mOnDrawListeners == null) {
            mOnDrawListeners = new java.util.ArrayList<android.view.ViewTreeObserver.OnDrawListener>();
        }
        if (mInDispatchOnDraw) {
            java.lang.IllegalStateException ex = new java.lang.IllegalStateException("Cannot call addOnDrawListener inside of onDraw");
            if (android.view.ViewTreeObserver.sIllegalOnDrawModificationIsFatal) {
                throw ex;
            } else {
                android.util.Log.e("ViewTreeObserver", ex.getMessage(), ex);
            }
        }
        mOnDrawListeners.add(listener);
    }

    /**
     * <p>Remove a previously installed pre-draw callback.</p>
     * <p><strong>Note:</strong> this method <strong>cannot</strong> be invoked from
     * {@link android.view.ViewTreeObserver.OnDrawListener#onDraw()}.</p>
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnDrawListener(OnDrawListener)
     */
    public void removeOnDrawListener(android.view.ViewTreeObserver.OnDrawListener victim) {
        checkIsAlive();
        if (mOnDrawListeners == null) {
            return;
        }
        if (mInDispatchOnDraw) {
            java.lang.IllegalStateException ex = new java.lang.IllegalStateException("Cannot call removeOnDrawListener inside of onDraw");
            if (android.view.ViewTreeObserver.sIllegalOnDrawModificationIsFatal) {
                throw ex;
            } else {
                android.util.Log.e("ViewTreeObserver", ex.getMessage(), ex);
            }
        }
        mOnDrawListeners.remove(victim);
    }

    /**
     * Adds a frame commit callback. This callback will be invoked when the current rendering
     * content has been rendered into a frame and submitted to the swap chain. The frame may
     * not currently be visible on the display when this is invoked, but it has been submitted.
     * This callback is useful in combination with {@link PixelCopy} to capture the current
     * rendered content of the UI reliably.
     *
     * Note: Only works with hardware rendering. Does nothing otherwise.
     *
     * @param callback
     * 		The callback to invoke when the frame is committed.
     */
    public void registerFrameCommitCallback(@android.annotation.NonNull
    java.lang.Runnable callback) {
        checkIsAlive();
        if (mOnFrameCommitListeners == null) {
            mOnFrameCommitListeners = new java.util.ArrayList<>();
        }
        mOnFrameCommitListeners.add(callback);
    }

    @android.annotation.Nullable
    java.util.ArrayList<java.lang.Runnable> captureFrameCommitCallbacks() {
        java.util.ArrayList<java.lang.Runnable> ret = mOnFrameCommitListeners;
        mOnFrameCommitListeners = null;
        return ret;
    }

    /**
     * Attempts to remove the given callback from the list of pending frame complete callbacks.
     *
     * @param callback
     * 		The callback to remove
     * @return Whether or not the callback was removed. If this returns true the callback will
    not be invoked. If false is returned then the callback was either never added
    or may already be pending execution and was unable to be removed
     */
    public boolean unregisterFrameCommitCallback(@android.annotation.NonNull
    java.lang.Runnable callback) {
        checkIsAlive();
        if (mOnFrameCommitListeners == null) {
            return false;
        }
        return mOnFrameCommitListeners.remove(callback);
    }

    /**
     * Register a callback to be invoked when a view has been scrolled.
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnScrollChangedListener(android.view.ViewTreeObserver.OnScrollChangedListener listener) {
        checkIsAlive();
        if (mOnScrollChangedListeners == null) {
            mOnScrollChangedListeners = new android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnScrollChangedListener>();
        }
        mOnScrollChangedListeners.add(listener);
    }

    /**
     * Remove a previously installed scroll-changed callback
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnScrollChangedListener(OnScrollChangedListener)
     */
    public void removeOnScrollChangedListener(android.view.ViewTreeObserver.OnScrollChangedListener victim) {
        checkIsAlive();
        if (mOnScrollChangedListeners == null) {
            return;
        }
        mOnScrollChangedListeners.remove(victim);
    }

    /**
     * Register a callback to be invoked when the invoked when the touch mode changes.
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     */
    public void addOnTouchModeChangeListener(android.view.ViewTreeObserver.OnTouchModeChangeListener listener) {
        checkIsAlive();
        if (mOnTouchModeChangeListeners == null) {
            mOnTouchModeChangeListeners = new java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnTouchModeChangeListener>();
        }
        mOnTouchModeChangeListeners.add(listener);
    }

    /**
     * Remove a previously installed touch mode change callback
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnTouchModeChangeListener(OnTouchModeChangeListener)
     */
    public void removeOnTouchModeChangeListener(android.view.ViewTreeObserver.OnTouchModeChangeListener victim) {
        checkIsAlive();
        if (mOnTouchModeChangeListeners == null) {
            return;
        }
        mOnTouchModeChangeListeners.remove(victim);
    }

    /**
     * Register a callback to be invoked when the invoked when it is time to
     * compute the window's internal insets.
     *
     * @param listener
     * 		The callback to add
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * 		
     * 		We are not yet ready to commit to this API and support it, so
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void addOnComputeInternalInsetsListener(android.view.ViewTreeObserver.OnComputeInternalInsetsListener listener) {
        checkIsAlive();
        if (mOnComputeInternalInsetsListeners == null) {
            mOnComputeInternalInsetsListeners = new android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnComputeInternalInsetsListener>();
        }
        mOnComputeInternalInsetsListeners.add(listener);
    }

    /**
     * Remove a previously installed internal insets computation callback
     *
     * @param victim
     * 		The callback to remove
     * @throws IllegalStateException
     * 		If {@link #isAlive()} returns false
     * @see #addOnComputeInternalInsetsListener(OnComputeInternalInsetsListener)

    We are not yet ready to commit to this API and support it, so
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void removeOnComputeInternalInsetsListener(android.view.ViewTreeObserver.OnComputeInternalInsetsListener victim) {
        checkIsAlive();
        if (mOnComputeInternalInsetsListeners == null) {
            return;
        }
        mOnComputeInternalInsetsListeners.remove(victim);
    }

    /**
     *
     *
     * @unknown 
     */
    public void addOnEnterAnimationCompleteListener(android.view.ViewTreeObserver.OnEnterAnimationCompleteListener listener) {
        checkIsAlive();
        if (mOnEnterAnimationCompleteListeners == null) {
            mOnEnterAnimationCompleteListeners = new java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnEnterAnimationCompleteListener>();
        }
        mOnEnterAnimationCompleteListeners.add(listener);
    }

    /**
     *
     *
     * @unknown 
     */
    public void removeOnEnterAnimationCompleteListener(android.view.ViewTreeObserver.OnEnterAnimationCompleteListener listener) {
        checkIsAlive();
        if (mOnEnterAnimationCompleteListeners == null) {
            return;
        }
        mOnEnterAnimationCompleteListeners.remove(listener);
    }

    /**
     * Add a listener to be notified when the tree's <em>transformed</em> gesture exclusion rects
     * change. This could be the result of an animation or other layout change, or a view calling
     * {@link View#setSystemGestureExclusionRects(List)}.
     *
     * @param listener
     * 		listener to add
     * @see View#setSystemGestureExclusionRects(List)
     */
    public void addOnSystemGestureExclusionRectsChangedListener(@android.annotation.NonNull
    java.util.function.Consumer<java.util.List<android.graphics.Rect>> listener) {
        checkIsAlive();
        if (mGestureExclusionListeners == null) {
            mGestureExclusionListeners = new android.view.ViewTreeObserver.CopyOnWriteArray<>();
        }
        mGestureExclusionListeners.add(listener);
    }

    /**
     * Unsubscribe the given listener from gesture exclusion rect changes.
     *
     * @see #addOnSystemGestureExclusionRectsChangedListener(Consumer)
     * @see View#setSystemGestureExclusionRects(List)
     */
    public void removeOnSystemGestureExclusionRectsChangedListener(@android.annotation.NonNull
    java.util.function.Consumer<java.util.List<android.graphics.Rect>> listener) {
        checkIsAlive();
        if (mGestureExclusionListeners == null) {
            return;
        }
        mGestureExclusionListeners.remove(listener);
    }

    private void checkIsAlive() {
        if (!mAlive) {
            throw new java.lang.IllegalStateException("This ViewTreeObserver is not alive, call " + "getViewTreeObserver() again");
        }
    }

    /**
     * Indicates whether this ViewTreeObserver is alive. When an observer is not alive,
     * any call to a method (except this one) will throw an exception.
     *
     * If an application keeps a long-lived reference to this ViewTreeObserver, it should
     * always check for the result of this method before calling any other method.
     *
     * @return True if this object is alive and be used, false otherwise.
     */
    public boolean isAlive() {
        return mAlive;
    }

    /**
     * Marks this ViewTreeObserver as not alive. After invoking this method, invoking
     * any other method but {@link #isAlive()} and {@link #kill()} will throw an Exception.
     *
     * @unknown 
     */
    private void kill() {
        mAlive = false;
    }

    /**
     * Notifies registered listeners that window has been attached/detached.
     */
    final void dispatchOnWindowAttachedChange(boolean attached) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to
        // perform the dispatching. The iterator is a safe guard against listeners that
        // could mutate the list by calling the various add/remove methods. This prevents
        // the array from being modified while we iterate it.
        final java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnWindowAttachListener> listeners = mOnWindowAttachListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            for (android.view.ViewTreeObserver.OnWindowAttachListener listener : listeners) {
                if (attached)
                    listener.onWindowAttached();
                else
                    listener.onWindowDetached();

            }
        }
    }

    /**
     * Notifies registered listeners that window focus has changed.
     */
    final void dispatchOnWindowFocusChange(boolean hasFocus) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to
        // perform the dispatching. The iterator is a safe guard against listeners that
        // could mutate the list by calling the various add/remove methods. This prevents
        // the array from being modified while we iterate it.
        final java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnWindowFocusChangeListener> listeners = mOnWindowFocusListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            for (android.view.ViewTreeObserver.OnWindowFocusChangeListener listener : listeners) {
                listener.onWindowFocusChanged(hasFocus);
            }
        }
    }

    /**
     * Notifies registered listeners that focus has changed.
     */
    @android.annotation.UnsupportedAppUsage
    final void dispatchOnGlobalFocusChange(android.view.View oldFocus, android.view.View newFocus) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to
        // perform the dispatching. The iterator is a safe guard against listeners that
        // could mutate the list by calling the various add/remove methods. This prevents
        // the array from being modified while we iterate it.
        final java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnGlobalFocusChangeListener> listeners = mOnGlobalFocusListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            for (android.view.ViewTreeObserver.OnGlobalFocusChangeListener listener : listeners) {
                listener.onGlobalFocusChanged(oldFocus, newFocus);
            }
        }
    }

    /**
     * Notifies registered listeners that a global layout happened. This can be called
     * manually if you are forcing a layout on a View or a hierarchy of Views that are
     * not attached to a Window or in the GONE state.
     */
    public final void dispatchOnGlobalLayout() {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to
        // perform the dispatching. The iterator is a safe guard against listeners that
        // could mutate the list by calling the various add/remove methods. This prevents
        // the array from being modified while we iterate it.
        final android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnGlobalLayoutListener> listeners = mOnGlobalLayoutListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            android.view.ViewTreeObserver.CopyOnWriteArray.Access<android.view.ViewTreeObserver.OnGlobalLayoutListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onGlobalLayout();
                }
            } finally {
                listeners.end();
            }
        }
    }

    /**
     * Returns whether there are listeners for on pre-draw events.
     */
    final boolean hasOnPreDrawListeners() {
        return (mOnPreDrawListeners != null) && (mOnPreDrawListeners.size() > 0);
    }

    /**
     * Notifies registered listeners that the drawing pass is about to start. If a
     * listener returns true, then the drawing pass is canceled and rescheduled. This can
     * be called manually if you are forcing the drawing on a View or a hierarchy of Views
     * that are not attached to a Window or in the GONE state.
     *
     * @return True if the current draw should be canceled and resceduled, false otherwise.
     */
    @java.lang.SuppressWarnings("unchecked")
    public final boolean dispatchOnPreDraw() {
        boolean cancelDraw = false;
        final android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnPreDrawListener> listeners = mOnPreDrawListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            android.view.ViewTreeObserver.CopyOnWriteArray.Access<android.view.ViewTreeObserver.OnPreDrawListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    cancelDraw |= !access.get(i).onPreDraw();
                }
            } finally {
                listeners.end();
            }
        }
        return cancelDraw;
    }

    /**
     * Notifies registered listeners that the window is now shown
     *
     * @unknown 
     */
    @java.lang.SuppressWarnings("unchecked")
    public final void dispatchOnWindowShown() {
        mWindowShown = true;
        final android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnWindowShownListener> listeners = mOnWindowShownListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            android.view.ViewTreeObserver.CopyOnWriteArray.Access<android.view.ViewTreeObserver.OnWindowShownListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onWindowShown();
                }
            } finally {
                listeners.end();
            }
        }
    }

    /**
     * Notifies registered listeners that the drawing pass is about to start.
     */
    public final void dispatchOnDraw() {
        if (mOnDrawListeners != null) {
            mInDispatchOnDraw = true;
            final java.util.ArrayList<android.view.ViewTreeObserver.OnDrawListener> listeners = mOnDrawListeners;
            int numListeners = listeners.size();
            for (int i = 0; i < numListeners; ++i) {
                listeners.get(i).onDraw();
            }
            mInDispatchOnDraw = false;
        }
    }

    /**
     * Notifies registered listeners that the touch mode has changed.
     *
     * @param inTouchMode
     * 		True if the touch mode is now enabled, false otherwise.
     */
    @android.annotation.UnsupportedAppUsage
    final void dispatchOnTouchModeChanged(boolean inTouchMode) {
        final java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnTouchModeChangeListener> listeners = mOnTouchModeChangeListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            for (android.view.ViewTreeObserver.OnTouchModeChangeListener listener : listeners) {
                listener.onTouchModeChanged(inTouchMode);
            }
        }
    }

    /**
     * Notifies registered listeners that something has scrolled.
     */
    @android.annotation.UnsupportedAppUsage
    final void dispatchOnScrollChanged() {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to
        // perform the dispatching. The iterator is a safe guard against listeners that
        // could mutate the list by calling the various add/remove methods. This prevents
        // the array from being modified while we iterate it.
        final android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnScrollChangedListener> listeners = mOnScrollChangedListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            android.view.ViewTreeObserver.CopyOnWriteArray.Access<android.view.ViewTreeObserver.OnScrollChangedListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onScrollChanged();
                }
            } finally {
                listeners.end();
            }
        }
    }

    /**
     * Returns whether there are listeners for computing internal insets.
     */
    @android.annotation.UnsupportedAppUsage
    final boolean hasComputeInternalInsetsListeners() {
        final android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnComputeInternalInsetsListener> listeners = mOnComputeInternalInsetsListeners;
        return (listeners != null) && (listeners.size() > 0);
    }

    /**
     * Calls all listeners to compute the current insets.
     */
    @android.annotation.UnsupportedAppUsage
    final void dispatchOnComputeInternalInsets(android.view.ViewTreeObserver.InternalInsetsInfo inoutInfo) {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to
        // perform the dispatching. The iterator is a safe guard against listeners that
        // could mutate the list by calling the various add/remove methods. This prevents
        // the array from being modified while we iterate it.
        final android.view.ViewTreeObserver.CopyOnWriteArray<android.view.ViewTreeObserver.OnComputeInternalInsetsListener> listeners = mOnComputeInternalInsetsListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            android.view.ViewTreeObserver.CopyOnWriteArray.Access<android.view.ViewTreeObserver.OnComputeInternalInsetsListener> access = listeners.start();
            try {
                int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).onComputeInternalInsets(inoutInfo);
                }
            } finally {
                listeners.end();
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public final void dispatchOnEnterAnimationComplete() {
        // NOTE: because of the use of CopyOnWriteArrayList, we *must* use an iterator to
        // perform the dispatching. The iterator is a safe guard against listeners that
        // could mutate the list by calling the various add/remove methods. This prevents
        // the array from being modified while we iterate it.
        final java.util.concurrent.CopyOnWriteArrayList<android.view.ViewTreeObserver.OnEnterAnimationCompleteListener> listeners = mOnEnterAnimationCompleteListeners;
        if ((listeners != null) && (!listeners.isEmpty())) {
            for (android.view.ViewTreeObserver.OnEnterAnimationCompleteListener listener : listeners) {
                listener.onEnterAnimationComplete();
            }
        }
    }

    void dispatchOnSystemGestureExclusionRectsChanged(@android.annotation.NonNull
    java.util.List<android.graphics.Rect> rects) {
        final android.view.ViewTreeObserver.CopyOnWriteArray<java.util.function.Consumer<java.util.List<android.graphics.Rect>>> listeners = mGestureExclusionListeners;
        if ((listeners != null) && (listeners.size() > 0)) {
            android.view.ViewTreeObserver.CopyOnWriteArray.Access<java.util.function.Consumer<java.util.List<android.graphics.Rect>>> access = listeners.start();
            try {
                final int count = access.size();
                for (int i = 0; i < count; i++) {
                    access.get(i).accept(rects);
                }
            } finally {
                listeners.end();
            }
        }
    }

    /**
     * Copy on write array. This array is not thread safe, and only one loop can
     * iterate over this array at any given time. This class avoids allocations
     * until a concurrent modification happens.
     *
     * Usage:
     *
     * CopyOnWriteArray.Access<MyData> access = array.start();
     * try {
     *     for (int i = 0; i < access.size(); i++) {
     *         MyData d = access.get(i);
     *     }
     * } finally {
     *     access.end();
     * }
     */
    static class CopyOnWriteArray<T> {
        private java.util.ArrayList<T> mData = new java.util.ArrayList<T>();

        private java.util.ArrayList<T> mDataCopy;

        private final android.view.ViewTreeObserver.CopyOnWriteArray.Access<T> mAccess = new android.view.ViewTreeObserver.CopyOnWriteArray.Access<T>();

        private boolean mStart;

        static class Access<T> {
            private java.util.ArrayList<T> mData;

            private int mSize;

            T get(int index) {
                return mData.get(index);
            }

            int size() {
                return mSize;
            }
        }

        CopyOnWriteArray() {
        }

        private java.util.ArrayList<T> getArray() {
            if (mStart) {
                if (mDataCopy == null)
                    mDataCopy = new java.util.ArrayList<T>(mData);

                return mDataCopy;
            }
            return mData;
        }

        android.view.ViewTreeObserver.CopyOnWriteArray.Access<T> start() {
            if (mStart)
                throw new java.lang.IllegalStateException("Iteration already started");

            mStart = true;
            mDataCopy = null;
            mAccess.mData = mData;
            mAccess.mSize = mData.size();
            return mAccess;
        }

        void end() {
            if (!mStart)
                throw new java.lang.IllegalStateException("Iteration not started");

            mStart = false;
            if (mDataCopy != null) {
                mData = mDataCopy;
                mAccess.mData.clear();
                mAccess.mSize = 0;
            }
            mDataCopy = null;
        }

        int size() {
            return getArray().size();
        }

        void add(T item) {
            getArray().add(item);
        }

        void addAll(android.view.ViewTreeObserver.CopyOnWriteArray<T> array) {
            getArray().addAll(array.mData);
        }

        void remove(T item) {
            getArray().remove(item);
        }

        void clear() {
            getArray().clear();
        }
    }
}

