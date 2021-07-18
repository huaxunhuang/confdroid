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
package android.support.v4.widget;


/**
 * DrawerLayout acts as a top-level container for window content that allows for
 * interactive "drawer" views to be pulled out from one or both vertical edges of the window.
 *
 * <p>Drawer positioning and layout is controlled using the <code>android:layout_gravity</code>
 * attribute on child views corresponding to which side of the view you want the drawer
 * to emerge from: left or right (or start/end on platform versions that support layout direction.)
 * Note that you can only have one drawer view for each vertical edge of the window. If your
 * layout configures more than one drawer view per vertical edge of the window, an exception will
 * be thrown at runtime.
 * </p>
 *
 * <p>To use a DrawerLayout, position your primary content view as the first child with
 * width and height of <code>match_parent</code> and no <code>layout_gravity></code>.
 * Add drawers as child views after the main content view and set the <code>layout_gravity</code>
 * appropriately. Drawers commonly use <code>match_parent</code> for height with a fixed width.</p>
 *
 * <p>{@link DrawerListener} can be used to monitor the state and motion of drawer views.
 * Avoid performing expensive operations such as layout during animation as it can cause
 * stuttering; try to perform expensive operations during the {@link #STATE_IDLE} state.
 * {@link SimpleDrawerListener} offers default/no-op implementations of each callback method.</p>
 *
 * <p>As per the <a href="{@docRoot }design/patterns/navigation-drawer.html">Android Design
 * guide</a>, any drawers positioned to the left/start should
 * always contain content for navigating around the application, whereas any drawers
 * positioned to the right/end should always contain actions to take on the current content.
 * This preserves the same navigation left, actions right structure present in the Action Bar
 * and elsewhere.</p>
 *
 * <p>For more information about how to use DrawerLayout, read <a
 * href="{@docRoot }training/implementing-navigation/nav-drawer.html">Creating a Navigation
 * Drawer</a>.</p>
 */
public class DrawerLayout extends android.view.ViewGroup implements android.support.v4.widget.DrawerLayoutImpl {
    private static final java.lang.String TAG = "DrawerLayout";

    @android.support.annotation.IntDef({ android.support.v4.widget.DrawerLayout.STATE_IDLE, android.support.v4.widget.DrawerLayout.STATE_DRAGGING, android.support.v4.widget.DrawerLayout.STATE_SETTLING })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface State {}

    /**
     * Indicates that any drawers are in an idle, settled state. No animation is in progress.
     */
    public static final int STATE_IDLE = android.support.v4.widget.ViewDragHelper.STATE_IDLE;

    /**
     * Indicates that a drawer is currently being dragged by the user.
     */
    public static final int STATE_DRAGGING = android.support.v4.widget.ViewDragHelper.STATE_DRAGGING;

    /**
     * Indicates that a drawer is in the process of settling to a final position.
     */
    public static final int STATE_SETTLING = android.support.v4.widget.ViewDragHelper.STATE_SETTLING;

    @android.support.annotation.IntDef({ android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED, android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED, android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_OPEN, android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface LockMode {}

    /**
     * The drawer is unlocked.
     */
    public static final int LOCK_MODE_UNLOCKED = 0;

    /**
     * The drawer is locked closed. The user may not open it, though
     * the app may open it programmatically.
     */
    public static final int LOCK_MODE_LOCKED_CLOSED = 1;

    /**
     * The drawer is locked open. The user may not close it, though the app
     * may close it programmatically.
     */
    public static final int LOCK_MODE_LOCKED_OPEN = 2;

    /**
     * The drawer's lock state is reset to default.
     */
    public static final int LOCK_MODE_UNDEFINED = 3;

    @android.support.annotation.IntDef({ android.view.Gravity.LEFT, android.view.Gravity.RIGHT, android.support.v4.view.GravityCompat.START, android.support.v4.view.GravityCompat.END })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    private @interface EdgeGravity {}

    private static final int MIN_DRAWER_MARGIN = 64;// dp


    private static final int DRAWER_ELEVATION = 10;// dp


    private static final int DEFAULT_SCRIM_COLOR = 0x99000000;

    /**
     * Length of time to delay before peeking the drawer.
     */
    private static final int PEEK_DELAY = 160;// ms


    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400;// dips per second


    /**
     * Experimental feature.
     */
    private static final boolean ALLOW_EDGE_LOCK = false;

    private static final boolean CHILDREN_DISALLOW_INTERCEPT = true;

    private static final float TOUCH_SLOP_SENSITIVITY = 1.0F;

    static final int[] LAYOUT_ATTRS = new int[]{ android.R.attr.layout_gravity };

    /**
     * Whether we can use NO_HIDE_DESCENDANTS accessibility importance.
     */
    static final boolean CAN_HIDE_DESCENDANTS = android.os.Build.VERSION.SDK_INT >= 19;

    /**
     * Whether the drawer shadow comes from setting elevation on the drawer.
     */
    private static final boolean SET_DRAWER_SHADOW_FROM_ELEVATION = android.os.Build.VERSION.SDK_INT >= 21;

    private final android.support.v4.widget.DrawerLayout.ChildAccessibilityDelegate mChildAccessibilityDelegate = new android.support.v4.widget.DrawerLayout.ChildAccessibilityDelegate();

    private float mDrawerElevation;

    private int mMinDrawerMargin;

    private int mScrimColor = android.support.v4.widget.DrawerLayout.DEFAULT_SCRIM_COLOR;

    private float mScrimOpacity;

    private android.graphics.Paint mScrimPaint = new android.graphics.Paint();

    private final android.support.v4.widget.ViewDragHelper mLeftDragger;

    private final android.support.v4.widget.ViewDragHelper mRightDragger;

    private final android.support.v4.widget.DrawerLayout.ViewDragCallback mLeftCallback;

    private final android.support.v4.widget.DrawerLayout.ViewDragCallback mRightCallback;

    private int mDrawerState;

    private boolean mInLayout;

    private boolean mFirstLayout = true;

    @android.support.v4.widget.DrawerLayout.LockMode
    private int mLockModeLeft = android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED;

    @android.support.v4.widget.DrawerLayout.LockMode
    private int mLockModeRight = android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED;

    @android.support.v4.widget.DrawerLayout.LockMode
    private int mLockModeStart = android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED;

    @android.support.v4.widget.DrawerLayout.LockMode
    private int mLockModeEnd = android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED;

    private boolean mDisallowInterceptRequested;

    private boolean mChildrenCanceledTouch;

    @android.support.annotation.Nullable
    private android.support.v4.widget.DrawerLayout.DrawerListener mListener;

    private java.util.List<android.support.v4.widget.DrawerLayout.DrawerListener> mListeners;

    private float mInitialMotionX;

    private float mInitialMotionY;

    private android.graphics.drawable.Drawable mStatusBarBackground;

    private android.graphics.drawable.Drawable mShadowLeftResolved;

    private android.graphics.drawable.Drawable mShadowRightResolved;

    private java.lang.CharSequence mTitleLeft;

    private java.lang.CharSequence mTitleRight;

    private java.lang.Object mLastInsets;

    private boolean mDrawStatusBarBackground;

    /**
     * Shadow drawables for different gravity
     */
    private android.graphics.drawable.Drawable mShadowStart = null;

    private android.graphics.drawable.Drawable mShadowEnd = null;

    private android.graphics.drawable.Drawable mShadowLeft = null;

    private android.graphics.drawable.Drawable mShadowRight = null;

    private final java.util.ArrayList<android.view.View> mNonDrawerViews;

    /**
     * Listener for monitoring events about drawers.
     */
    public interface DrawerListener {
        /**
         * Called when a drawer's position changes.
         *
         * @param drawerView
         * 		The child view that was moved
         * @param slideOffset
         * 		The new offset of this drawer within its range, from 0-1
         */
        void onDrawerSlide(android.view.View drawerView, float slideOffset);

        /**
         * Called when a drawer has settled in a completely open state.
         * The drawer is interactive at this point.
         *
         * @param drawerView
         * 		Drawer view that is now open
         */
        void onDrawerOpened(android.view.View drawerView);

        /**
         * Called when a drawer has settled in a completely closed state.
         *
         * @param drawerView
         * 		Drawer view that is now closed
         */
        void onDrawerClosed(android.view.View drawerView);

        /**
         * Called when the drawer motion state changes. The new state will
         * be one of {@link #STATE_IDLE}, {@link #STATE_DRAGGING} or {@link #STATE_SETTLING}.
         *
         * @param newState
         * 		The new drawer motion state
         */
        void onDrawerStateChanged(@android.support.v4.widget.DrawerLayout.State
        int newState);
    }

    /**
     * Stub/no-op implementations of all methods of {@link DrawerListener}.
     * Override this if you only care about a few of the available callback methods.
     */
    public static abstract class SimpleDrawerListener implements android.support.v4.widget.DrawerLayout.DrawerListener {
        @java.lang.Override
        public void onDrawerSlide(android.view.View drawerView, float slideOffset) {
        }

        @java.lang.Override
        public void onDrawerOpened(android.view.View drawerView) {
        }

        @java.lang.Override
        public void onDrawerClosed(android.view.View drawerView) {
        }

        @java.lang.Override
        public void onDrawerStateChanged(int newState) {
        }
    }

    interface DrawerLayoutCompatImpl {
        void configureApplyInsets(android.view.View drawerLayout);

        void dispatchChildInsets(android.view.View child, java.lang.Object insets, int drawerGravity);

        void applyMarginInsets(android.view.ViewGroup.MarginLayoutParams lp, java.lang.Object insets, int drawerGravity);

        int getTopInset(java.lang.Object lastInsets);

        android.graphics.drawable.Drawable getDefaultStatusBarBackground(android.content.Context context);
    }

    static class DrawerLayoutCompatImplBase implements android.support.v4.widget.DrawerLayout.DrawerLayoutCompatImpl {
        @java.lang.Override
        public void configureApplyInsets(android.view.View drawerLayout) {
            // This space for rent
        }

        @java.lang.Override
        public void dispatchChildInsets(android.view.View child, java.lang.Object insets, int drawerGravity) {
            // This space for rent
        }

        @java.lang.Override
        public void applyMarginInsets(android.view.ViewGroup.MarginLayoutParams lp, java.lang.Object insets, int drawerGravity) {
            // This space for rent
        }

        @java.lang.Override
        public int getTopInset(java.lang.Object insets) {
            return 0;
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getDefaultStatusBarBackground(android.content.Context context) {
            return null;
        }
    }

    static class DrawerLayoutCompatImplApi21 implements android.support.v4.widget.DrawerLayout.DrawerLayoutCompatImpl {
        @java.lang.Override
        public void configureApplyInsets(android.view.View drawerLayout) {
            android.support.v4.widget.DrawerLayoutCompatApi21.configureApplyInsets(drawerLayout);
        }

        @java.lang.Override
        public void dispatchChildInsets(android.view.View child, java.lang.Object insets, int drawerGravity) {
            android.support.v4.widget.DrawerLayoutCompatApi21.dispatchChildInsets(child, insets, drawerGravity);
        }

        @java.lang.Override
        public void applyMarginInsets(android.view.ViewGroup.MarginLayoutParams lp, java.lang.Object insets, int drawerGravity) {
            android.support.v4.widget.DrawerLayoutCompatApi21.applyMarginInsets(lp, insets, drawerGravity);
        }

        @java.lang.Override
        public int getTopInset(java.lang.Object insets) {
            return android.support.v4.widget.DrawerLayoutCompatApi21.getTopInset(insets);
        }

        @java.lang.Override
        public android.graphics.drawable.Drawable getDefaultStatusBarBackground(android.content.Context context) {
            return android.support.v4.widget.DrawerLayoutCompatApi21.getDefaultStatusBarBackground(context);
        }
    }

    static {
        final int version = android.os.Build.VERSION.SDK_INT;
        if (version >= 21) {
            IMPL = new android.support.v4.widget.DrawerLayout.DrawerLayoutCompatImplApi21();
        } else {
            IMPL = new android.support.v4.widget.DrawerLayout.DrawerLayoutCompatImplBase();
        }
    }

    static final android.support.v4.widget.DrawerLayout.DrawerLayoutCompatImpl IMPL;

    public DrawerLayout(android.content.Context context) {
        this(context, null);
    }

    public DrawerLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawerLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDescendantFocusability(android.view.ViewGroup.FOCUS_AFTER_DESCENDANTS);
        final float density = getResources().getDisplayMetrics().density;
        mMinDrawerMargin = ((int) ((android.support.v4.widget.DrawerLayout.MIN_DRAWER_MARGIN * density) + 0.5F));
        final float minVel = android.support.v4.widget.DrawerLayout.MIN_FLING_VELOCITY * density;
        mLeftCallback = new android.support.v4.widget.DrawerLayout.ViewDragCallback(android.view.Gravity.LEFT);
        mRightCallback = new android.support.v4.widget.DrawerLayout.ViewDragCallback(android.view.Gravity.RIGHT);
        mLeftDragger = android.support.v4.widget.ViewDragHelper.create(this, android.support.v4.widget.DrawerLayout.TOUCH_SLOP_SENSITIVITY, mLeftCallback);
        mLeftDragger.setEdgeTrackingEnabled(android.support.v4.widget.ViewDragHelper.EDGE_LEFT);
        mLeftDragger.setMinVelocity(minVel);
        mLeftCallback.setDragger(mLeftDragger);
        mRightDragger = android.support.v4.widget.ViewDragHelper.create(this, android.support.v4.widget.DrawerLayout.TOUCH_SLOP_SENSITIVITY, mRightCallback);
        mRightDragger.setEdgeTrackingEnabled(android.support.v4.widget.ViewDragHelper.EDGE_RIGHT);
        mRightDragger.setMinVelocity(minVel);
        mRightCallback.setDragger(mRightDragger);
        // So that we can catch the back button
        setFocusableInTouchMode(true);
        android.support.v4.view.ViewCompat.setImportantForAccessibility(this, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        android.support.v4.view.ViewCompat.setAccessibilityDelegate(this, new android.support.v4.widget.DrawerLayout.AccessibilityDelegate());
        android.support.v4.view.ViewGroupCompat.setMotionEventSplittingEnabled(this, false);
        if (android.support.v4.view.ViewCompat.getFitsSystemWindows(this)) {
            android.support.v4.widget.DrawerLayout.IMPL.configureApplyInsets(this);
            mStatusBarBackground = android.support.v4.widget.DrawerLayout.IMPL.getDefaultStatusBarBackground(context);
        }
        mDrawerElevation = android.support.v4.widget.DrawerLayout.DRAWER_ELEVATION * density;
        mNonDrawerViews = new java.util.ArrayList<android.view.View>();
    }

    /**
     * Sets the base elevation of the drawer(s) relative to the parent, in pixels. Note that the
     * elevation change is only supported in API 21 and above.
     *
     * @param elevation
     * 		The base depth position of the view, in pixels.
     */
    public void setDrawerElevation(float elevation) {
        mDrawerElevation = elevation;
        for (int i = 0; i < getChildCount(); i++) {
            android.view.View child = getChildAt(i);
            if (isDrawerView(child)) {
                android.support.v4.view.ViewCompat.setElevation(child, mDrawerElevation);
            }
        }
    }

    /**
     * The base elevation of the drawer(s) relative to the parent, in pixels. Note that the
     * elevation change is only supported in API 21 and above. For unsupported API levels, 0 will
     * be returned as the elevation.
     *
     * @return The base depth position of the view, in pixels.
     */
    public float getDrawerElevation() {
        if (android.support.v4.widget.DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return mDrawerElevation;
        }
        return 0.0F;
    }

    /**
     *
     *
     * @unknown Internal use only; called to apply window insets when configured
    with fitsSystemWindows="true"
     */
    @java.lang.Override
    public void setChildInsets(java.lang.Object insets, boolean draw) {
        mLastInsets = insets;
        mDrawStatusBarBackground = draw;
        setWillNotDraw((!draw) && (getBackground() == null));
        requestLayout();
    }

    /**
     * Set a simple drawable used for the left or right shadow. The drawable provided must have a
     * nonzero intrinsic width. For API 21 and above, an elevation will be set on the drawer
     * instead of the drawable provided.
     *
     * <p>Note that for better support for both left-to-right and right-to-left layout
     * directions, a drawable for RTL layout (in additional to the one in LTR layout) can be
     * defined with a resource qualifier "ldrtl" for API 17 and above with the gravity
     * {@link GravityCompat#START}. Alternatively, for API 23 and above, the drawable can
     * auto-mirrored such that the drawable will be mirrored in RTL layout.</p>
     *
     * @param shadowDrawable
     * 		Shadow drawable to use at the edge of a drawer
     * @param gravity
     * 		Which drawer the shadow should apply to
     */
    public void setDrawerShadow(android.graphics.drawable.Drawable shadowDrawable, @android.support.v4.widget.DrawerLayout.EdgeGravity
    int gravity) {
        /* TODO Someone someday might want to set more complex drawables here.
        They're probably nuts, but we might want to consider registering callbacks,
        setting states, etc. properly.
         */
        if (android.support.v4.widget.DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
            // No op. Drawer shadow will come from setting an elevation on the drawer.
            return;
        }
        if ((gravity & android.support.v4.view.GravityCompat.START) == android.support.v4.view.GravityCompat.START) {
            mShadowStart = shadowDrawable;
        } else
            if ((gravity & android.support.v4.view.GravityCompat.END) == android.support.v4.view.GravityCompat.END) {
                mShadowEnd = shadowDrawable;
            } else
                if ((gravity & android.view.Gravity.LEFT) == android.view.Gravity.LEFT) {
                    mShadowLeft = shadowDrawable;
                } else
                    if ((gravity & android.view.Gravity.RIGHT) == android.view.Gravity.RIGHT) {
                        mShadowRight = shadowDrawable;
                    } else {
                        return;
                    }



        resolveShadowDrawables();
        invalidate();
    }

    /**
     * Set a simple drawable used for the left or right shadow. The drawable provided must have a
     * nonzero intrinsic width. For API 21 and above, an elevation will be set on the drawer
     * instead of the drawable provided.
     *
     * <p>Note that for better support for both left-to-right and right-to-left layout
     * directions, a drawable for RTL layout (in additional to the one in LTR layout) can be
     * defined with a resource qualifier "ldrtl" for API 17 and above with the gravity
     * {@link GravityCompat#START}. Alternatively, for API 23 and above, the drawable can
     * auto-mirrored such that the drawable will be mirrored in RTL layout.</p>
     *
     * @param resId
     * 		Resource id of a shadow drawable to use at the edge of a drawer
     * @param gravity
     * 		Which drawer the shadow should apply to
     */
    public void setDrawerShadow(@android.support.annotation.DrawableRes
    int resId, @android.support.v4.widget.DrawerLayout.EdgeGravity
    int gravity) {
        setDrawerShadow(android.support.v4.content.ContextCompat.getDrawable(getContext(), resId), gravity);
    }

    /**
     * Set a color to use for the scrim that obscures primary content while a drawer is open.
     *
     * @param color
     * 		Color to use in 0xAARRGGBB format.
     */
    public void setScrimColor(@android.support.annotation.ColorInt
    int color) {
        mScrimColor = color;
        invalidate();
    }

    /**
     * Set a listener to be notified of drawer events. Note that this method is deprecated
     * and you should use {@link #addDrawerListener(DrawerListener)} to add a listener and
     * {@link #removeDrawerListener(DrawerListener)} to remove a registered listener.
     *
     * @param listener
     * 		Listener to notify when drawer events occur
     * @deprecated Use {@link #addDrawerListener(DrawerListener)}
     * @see DrawerListener
     * @see #addDrawerListener(DrawerListener)
     * @see #removeDrawerListener(DrawerListener)
     */
    @java.lang.Deprecated
    public void setDrawerListener(android.support.v4.widget.DrawerLayout.DrawerListener listener) {
        // The logic in this method emulates what we had before support for multiple
        // registered listeners.
        if (mListener != null) {
            removeDrawerListener(mListener);
        }
        if (listener != null) {
            addDrawerListener(listener);
        }
        // Update the deprecated field so that we can remove the passed listener the next
        // time we're called
        mListener = listener;
    }

    /**
     * Adds the specified listener to the list of listeners that will be notified of drawer events.
     *
     * @param listener
     * 		Listener to notify when drawer events occur.
     * @see #removeDrawerListener(DrawerListener)
     */
    public void addDrawerListener(@android.support.annotation.NonNull
    android.support.v4.widget.DrawerLayout.DrawerListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            mListeners = new java.util.ArrayList<android.support.v4.widget.DrawerLayout.DrawerListener>();
        }
        mListeners.add(listener);
    }

    /**
     * Removes the specified listener from the list of listeners that will be notified of drawer
     * events.
     *
     * @param listener
     * 		Listener to remove from being notified of drawer events
     * @see #addDrawerListener(DrawerListener)
     */
    public void removeDrawerListener(@android.support.annotation.NonNull
    android.support.v4.widget.DrawerLayout.DrawerListener listener) {
        if (listener == null) {
            return;
        }
        if (mListeners == null) {
            // This can happen if this method is called before the first call to addDrawerListener
            return;
        }
        mListeners.remove(listener);
    }

    /**
     * Enable or disable interaction with all drawers.
     *
     * <p>This allows the application to restrict the user's ability to open or close
     * any drawer within this layout. DrawerLayout will still respond to calls to
     * {@link #openDrawer(int)}, {@link #closeDrawer(int)} and friends if a drawer is locked.</p>
     *
     * <p>Locking drawers open or closed will implicitly open or close
     * any drawers as appropriate.</p>
     *
     * @param lockMode
     * 		The new lock mode for the given drawer. One of {@link #LOCK_MODE_UNLOCKED},
     * 		{@link #LOCK_MODE_LOCKED_CLOSED} or {@link #LOCK_MODE_LOCKED_OPEN}.
     */
    public void setDrawerLockMode(@android.support.v4.widget.DrawerLayout.LockMode
    int lockMode) {
        setDrawerLockMode(lockMode, android.view.Gravity.LEFT);
        setDrawerLockMode(lockMode, android.view.Gravity.RIGHT);
    }

    /**
     * Enable or disable interaction with the given drawer.
     *
     * <p>This allows the application to restrict the user's ability to open or close
     * the given drawer. DrawerLayout will still respond to calls to {@link #openDrawer(int)},
     * {@link #closeDrawer(int)} and friends if a drawer is locked.</p>
     *
     * <p>Locking a drawer open or closed will implicitly open or close
     * that drawer as appropriate.</p>
     *
     * @param lockMode
     * 		The new lock mode for the given drawer. One of {@link #LOCK_MODE_UNLOCKED},
     * 		{@link #LOCK_MODE_LOCKED_CLOSED} or {@link #LOCK_MODE_LOCKED_OPEN}.
     * @param edgeGravity
     * 		Gravity.LEFT, RIGHT, START or END.
     * 		Expresses which drawer to change the mode for.
     * @see #LOCK_MODE_UNLOCKED
     * @see #LOCK_MODE_LOCKED_CLOSED
     * @see #LOCK_MODE_LOCKED_OPEN
     */
    public void setDrawerLockMode(@android.support.v4.widget.DrawerLayout.LockMode
    int lockMode, @android.support.v4.widget.DrawerLayout.EdgeGravity
    int edgeGravity) {
        final int absGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(edgeGravity, android.support.v4.view.ViewCompat.getLayoutDirection(this));
        switch (edgeGravity) {
            case android.view.Gravity.LEFT :
                mLockModeLeft = lockMode;
                break;
            case android.view.Gravity.RIGHT :
                mLockModeRight = lockMode;
                break;
            case android.support.v4.view.GravityCompat.START :
                mLockModeStart = lockMode;
                break;
            case android.support.v4.view.GravityCompat.END :
                mLockModeEnd = lockMode;
                break;
        }
        if (lockMode != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED) {
            // Cancel interaction in progress
            final android.support.v4.widget.ViewDragHelper helper = (absGravity == android.view.Gravity.LEFT) ? mLeftDragger : mRightDragger;
            helper.cancel();
        }
        switch (lockMode) {
            case android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_OPEN :
                final android.view.View toOpen = findDrawerWithGravity(absGravity);
                if (toOpen != null) {
                    openDrawer(toOpen);
                }
                break;
            case android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED :
                final android.view.View toClose = findDrawerWithGravity(absGravity);
                if (toClose != null) {
                    closeDrawer(toClose);
                }
                break;
                // default: do nothing
        }
    }

    /**
     * Enable or disable interaction with the given drawer.
     *
     * <p>This allows the application to restrict the user's ability to open or close
     * the given drawer. DrawerLayout will still respond to calls to {@link #openDrawer(int)},
     * {@link #closeDrawer(int)} and friends if a drawer is locked.</p>
     *
     * <p>Locking a drawer open or closed will implicitly open or close
     * that drawer as appropriate.</p>
     *
     * @param lockMode
     * 		The new lock mode for the given drawer. One of {@link #LOCK_MODE_UNLOCKED},
     * 		{@link #LOCK_MODE_LOCKED_CLOSED} or {@link #LOCK_MODE_LOCKED_OPEN}.
     * @param drawerView
     * 		The drawer view to change the lock mode for
     * @see #LOCK_MODE_UNLOCKED
     * @see #LOCK_MODE_LOCKED_CLOSED
     * @see #LOCK_MODE_LOCKED_OPEN
     */
    public void setDrawerLockMode(@android.support.v4.widget.DrawerLayout.LockMode
    int lockMode, android.view.View drawerView) {
        if (!isDrawerView(drawerView)) {
            throw new java.lang.IllegalArgumentException((("View " + drawerView) + " is not a ") + "drawer with appropriate layout_gravity");
        }
        final int gravity = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams())).gravity;
        setDrawerLockMode(lockMode, gravity);
    }

    /**
     * Check the lock mode of the drawer with the given gravity.
     *
     * @param edgeGravity
     * 		Gravity of the drawer to check
     * @return one of {@link #LOCK_MODE_UNLOCKED}, {@link #LOCK_MODE_LOCKED_CLOSED} or
    {@link #LOCK_MODE_LOCKED_OPEN}.
     */
    @android.support.v4.widget.DrawerLayout.LockMode
    public int getDrawerLockMode(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int edgeGravity) {
        int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        switch (edgeGravity) {
            case android.view.Gravity.LEFT :
                if (mLockModeLeft != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return mLockModeLeft;
                }
                int leftLockMode = (layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) ? mLockModeStart : mLockModeEnd;
                if (leftLockMode != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return leftLockMode;
                }
                break;
            case android.view.Gravity.RIGHT :
                if (mLockModeRight != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return mLockModeRight;
                }
                int rightLockMode = (layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) ? mLockModeEnd : mLockModeStart;
                if (rightLockMode != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return rightLockMode;
                }
                break;
            case android.support.v4.view.GravityCompat.START :
                if (mLockModeStart != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return mLockModeStart;
                }
                int startLockMode = (layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) ? mLockModeLeft : mLockModeRight;
                if (startLockMode != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return startLockMode;
                }
                break;
            case android.support.v4.view.GravityCompat.END :
                if (mLockModeEnd != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return mLockModeEnd;
                }
                int endLockMode = (layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) ? mLockModeRight : mLockModeLeft;
                if (endLockMode != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
                    return endLockMode;
                }
                break;
        }
        return android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED;
    }

    /**
     * Check the lock mode of the given drawer view.
     *
     * @param drawerView
     * 		Drawer view to check lock mode
     * @return one of {@link #LOCK_MODE_UNLOCKED}, {@link #LOCK_MODE_LOCKED_CLOSED} or
    {@link #LOCK_MODE_LOCKED_OPEN}.
     */
    @android.support.v4.widget.DrawerLayout.LockMode
    public int getDrawerLockMode(android.view.View drawerView) {
        if (!isDrawerView(drawerView)) {
            throw new java.lang.IllegalArgumentException(("View " + drawerView) + " is not a drawer");
        }
        final int drawerGravity = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams())).gravity;
        return getDrawerLockMode(drawerGravity);
    }

    /**
     * Sets the title of the drawer with the given gravity.
     * <p>
     * When accessibility is turned on, this is the title that will be used to
     * identify the drawer to the active accessibility service.
     *
     * @param edgeGravity
     * 		Gravity.LEFT, RIGHT, START or END. Expresses which
     * 		drawer to set the title for.
     * @param title
     * 		The title for the drawer.
     */
    public void setDrawerTitle(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int edgeGravity, java.lang.CharSequence title) {
        final int absGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(edgeGravity, android.support.v4.view.ViewCompat.getLayoutDirection(this));
        if (absGravity == android.view.Gravity.LEFT) {
            mTitleLeft = title;
        } else
            if (absGravity == android.view.Gravity.RIGHT) {
                mTitleRight = title;
            }

    }

    /**
     * Returns the title of the drawer with the given gravity.
     *
     * @param edgeGravity
     * 		Gravity.LEFT, RIGHT, START or END. Expresses which
     * 		drawer to return the title for.
     * @return The title of the drawer, or null if none set.
     * @see #setDrawerTitle(int, CharSequence)
     */
    @android.support.annotation.Nullable
    public java.lang.CharSequence getDrawerTitle(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int edgeGravity) {
        final int absGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(edgeGravity, android.support.v4.view.ViewCompat.getLayoutDirection(this));
        if (absGravity == android.view.Gravity.LEFT) {
            return mTitleLeft;
        } else
            if (absGravity == android.view.Gravity.RIGHT) {
                return mTitleRight;
            }

        return null;
    }

    /**
     * Resolve the shared state of all drawers from the component ViewDragHelpers.
     * Should be called whenever a ViewDragHelper's state changes.
     */
    void updateDrawerState(int forGravity, @android.support.v4.widget.DrawerLayout.State
    int activeState, android.view.View activeDrawer) {
        final int leftState = mLeftDragger.getViewDragState();
        final int rightState = mRightDragger.getViewDragState();
        final int state;
        if ((leftState == android.support.v4.widget.DrawerLayout.STATE_DRAGGING) || (rightState == android.support.v4.widget.DrawerLayout.STATE_DRAGGING)) {
            state = android.support.v4.widget.DrawerLayout.STATE_DRAGGING;
        } else
            if ((leftState == android.support.v4.widget.DrawerLayout.STATE_SETTLING) || (rightState == android.support.v4.widget.DrawerLayout.STATE_SETTLING)) {
                state = android.support.v4.widget.DrawerLayout.STATE_SETTLING;
            } else {
                state = android.support.v4.widget.DrawerLayout.STATE_IDLE;
            }

        if ((activeDrawer != null) && (activeState == android.support.v4.widget.DrawerLayout.STATE_IDLE)) {
            final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (activeDrawer.getLayoutParams()));
            if (lp.onScreen == 0) {
                dispatchOnDrawerClosed(activeDrawer);
            } else
                if (lp.onScreen == 1) {
                    dispatchOnDrawerOpened(activeDrawer);
                }

        }
        if (state != mDrawerState) {
            mDrawerState = state;
            if (mListeners != null) {
                // Notify the listeners. Do that from the end of the list so that if a listener
                // removes itself as the result of being called, it won't mess up with our iteration
                int listenerCount = mListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) {
                    mListeners.get(i).onDrawerStateChanged(state);
                }
            }
        }
    }

    void dispatchOnDrawerClosed(android.view.View drawerView) {
        final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams()));
        if ((lp.openState & android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENED) == 1) {
            lp.openState = 0;
            if (mListeners != null) {
                // Notify the listeners. Do that from the end of the list so that if a listener
                // removes itself as the result of being called, it won't mess up with our iteration
                int listenerCount = mListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) {
                    mListeners.get(i).onDrawerClosed(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, false);
            // Only send WINDOW_STATE_CHANGE if the host has window focus. This
            // may change if support for multiple foreground windows (e.g. IME)
            // improves.
            if (hasWindowFocus()) {
                final android.view.View rootView = getRootView();
                if (rootView != null) {
                    rootView.sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                }
            }
        }
    }

    void dispatchOnDrawerOpened(android.view.View drawerView) {
        final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams()));
        if ((lp.openState & android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENED) == 0) {
            lp.openState = android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENED;
            if (mListeners != null) {
                // Notify the listeners. Do that from the end of the list so that if a listener
                // removes itself as the result of being called, it won't mess up with our iteration
                int listenerCount = mListeners.size();
                for (int i = listenerCount - 1; i >= 0; i--) {
                    mListeners.get(i).onDrawerOpened(drawerView);
                }
            }
            updateChildrenImportantForAccessibility(drawerView, true);
            // Only send WINDOW_STATE_CHANGE if the host has window focus.
            if (hasWindowFocus()) {
                sendAccessibilityEvent(android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
            }
        }
    }

    private void updateChildrenImportantForAccessibility(android.view.View drawerView, boolean isDrawerOpen) {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (((!isDrawerOpen) && (!isDrawerView(child))) || (isDrawerOpen && (child == drawerView))) {
                // Drawer is closed and this is a content view or this is an
                // open drawer view, so it should be visible.
                android.support.v4.view.ViewCompat.setImportantForAccessibility(child, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
            } else {
                android.support.v4.view.ViewCompat.setImportantForAccessibility(child, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
            }
        }
    }

    void dispatchOnDrawerSlide(android.view.View drawerView, float slideOffset) {
        if (mListeners != null) {
            // Notify the listeners. Do that from the end of the list so that if a listener
            // removes itself as the result of being called, it won't mess up with our iteration
            int listenerCount = mListeners.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                mListeners.get(i).onDrawerSlide(drawerView, slideOffset);
            }
        }
    }

    void setDrawerViewOffset(android.view.View drawerView, float slideOffset) {
        final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams()));
        if (slideOffset == lp.onScreen) {
            return;
        }
        lp.onScreen = slideOffset;
        dispatchOnDrawerSlide(drawerView, slideOffset);
    }

    float getDrawerViewOffset(android.view.View drawerView) {
        return ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams())).onScreen;
    }

    /**
     *
     *
     * @return the absolute gravity of the child drawerView, resolved according
    to the current layout direction
     */
    int getDrawerViewAbsoluteGravity(android.view.View drawerView) {
        final int gravity = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams())).gravity;
        return android.support.v4.view.GravityCompat.getAbsoluteGravity(gravity, android.support.v4.view.ViewCompat.getLayoutDirection(this));
    }

    boolean checkDrawerViewAbsoluteGravity(android.view.View drawerView, int checkFor) {
        final int absGravity = getDrawerViewAbsoluteGravity(drawerView);
        return (absGravity & checkFor) == checkFor;
    }

    android.view.View findOpenDrawer() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.v4.widget.DrawerLayout.LayoutParams childLp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (child.getLayoutParams()));
            if ((childLp.openState & android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENED) == 1) {
                return child;
            }
        }
        return null;
    }

    void moveDrawerToOffset(android.view.View drawerView, float slideOffset) {
        final float oldOffset = getDrawerViewOffset(drawerView);
        final int width = drawerView.getWidth();
        final int oldPos = ((int) (width * oldOffset));
        final int newPos = ((int) (width * slideOffset));
        final int dx = newPos - oldPos;
        drawerView.offsetLeftAndRight(checkDrawerViewAbsoluteGravity(drawerView, android.view.Gravity.LEFT) ? dx : -dx);
        setDrawerViewOffset(drawerView, slideOffset);
    }

    /**
     *
     *
     * @param gravity
     * 		the gravity of the child to return. If specified as a
     * 		relative value, it will be resolved according to the current
     * 		layout direction.
     * @return the drawer with the specified gravity
     */
    android.view.View findDrawerWithGravity(int gravity) {
        final int absHorizGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(gravity, android.support.v4.view.ViewCompat.getLayoutDirection(this)) & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            final int childAbsGravity = getDrawerViewAbsoluteGravity(child);
            if ((childAbsGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) == absHorizGravity) {
                return child;
            }
        }
        return null;
    }

    /**
     * Simple gravity to string - only supports LEFT and RIGHT for debugging output.
     *
     * @param gravity
     * 		Absolute gravity value
     * @return LEFT or RIGHT as appropriate, or a hex string
     */
    static java.lang.String gravityToString(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int gravity) {
        if ((gravity & android.view.Gravity.LEFT) == android.view.Gravity.LEFT) {
            return "LEFT";
        }
        if ((gravity & android.view.Gravity.RIGHT) == android.view.Gravity.RIGHT) {
            return "RIGHT";
        }
        return java.lang.Integer.toHexString(gravity);
    }

    @java.lang.Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mFirstLayout = true;
    }

    @java.lang.Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mFirstLayout = true;
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        if ((widthMode != android.view.View.MeasureSpec.EXACTLY) || (heightMode != android.view.View.MeasureSpec.EXACTLY)) {
            if (isInEditMode()) {
                // Don't crash the layout editor. Consume all of the space if specified
                // or pick a magic number from thin air otherwise.
                // TODO Better communication with tools of this bogus state.
                // It will crash on a real device.
                if (widthMode == android.view.View.MeasureSpec.AT_MOST) {
                    widthMode = android.view.View.MeasureSpec.EXACTLY;
                } else
                    if (widthMode == android.view.View.MeasureSpec.UNSPECIFIED) {
                        widthMode = android.view.View.MeasureSpec.EXACTLY;
                        widthSize = 300;
                    }

                if (heightMode == android.view.View.MeasureSpec.AT_MOST) {
                    heightMode = android.view.View.MeasureSpec.EXACTLY;
                } else
                    if (heightMode == android.view.View.MeasureSpec.UNSPECIFIED) {
                        heightMode = android.view.View.MeasureSpec.EXACTLY;
                        heightSize = 300;
                    }

            } else {
                throw new java.lang.IllegalArgumentException("DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
        }
        setMeasuredDimension(widthSize, heightSize);
        final boolean applyInsets = (mLastInsets != null) && android.support.v4.view.ViewCompat.getFitsSystemWindows(this);
        final int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        // Only one drawer is permitted along each vertical edge (left / right). These two booleans
        // are tracking the presence of the edge drawers.
        boolean hasDrawerOnLeftEdge = false;
        boolean hasDrawerOnRightEdge = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (child.getLayoutParams()));
            if (applyInsets) {
                final int cgrav = android.support.v4.view.GravityCompat.getAbsoluteGravity(lp.gravity, layoutDirection);
                if (android.support.v4.view.ViewCompat.getFitsSystemWindows(child)) {
                    android.support.v4.widget.DrawerLayout.IMPL.dispatchChildInsets(child, mLastInsets, cgrav);
                } else {
                    android.support.v4.widget.DrawerLayout.IMPL.applyMarginInsets(lp, mLastInsets, cgrav);
                }
            }
            if (isContentView(child)) {
                // Content views get measured at exactly the layout's size.
                final int contentWidthSpec = android.view.View.MeasureSpec.makeMeasureSpec((widthSize - lp.leftMargin) - lp.rightMargin, android.view.View.MeasureSpec.EXACTLY);
                final int contentHeightSpec = android.view.View.MeasureSpec.makeMeasureSpec((heightSize - lp.topMargin) - lp.bottomMargin, android.view.View.MeasureSpec.EXACTLY);
                child.measure(contentWidthSpec, contentHeightSpec);
            } else
                if (isDrawerView(child)) {
                    if (android.support.v4.widget.DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
                        if (android.support.v4.view.ViewCompat.getElevation(child) != mDrawerElevation) {
                            android.support.v4.view.ViewCompat.setElevation(child, mDrawerElevation);
                        }
                    }
                    @android.support.v4.widget.DrawerLayout.EdgeGravity
                    final int childGravity = getDrawerViewAbsoluteGravity(child) & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
                    // Note that the isDrawerView check guarantees that childGravity here is either
                    // LEFT or RIGHT
                    boolean isLeftEdgeDrawer = childGravity == android.view.Gravity.LEFT;
                    if ((isLeftEdgeDrawer && hasDrawerOnLeftEdge) || ((!isLeftEdgeDrawer) && hasDrawerOnRightEdge)) {
                        throw new java.lang.IllegalStateException((((("Child drawer has absolute gravity " + android.support.v4.widget.DrawerLayout.gravityToString(childGravity)) + " but this ") + android.support.v4.widget.DrawerLayout.TAG) + " already has a ") + "drawer view along that edge");
                    }
                    if (isLeftEdgeDrawer) {
                        hasDrawerOnLeftEdge = true;
                    } else {
                        hasDrawerOnRightEdge = true;
                    }
                    final int drawerWidthSpec = android.view.ViewGroup.getChildMeasureSpec(widthMeasureSpec, (mMinDrawerMargin + lp.leftMargin) + lp.rightMargin, lp.width);
                    final int drawerHeightSpec = android.view.ViewGroup.getChildMeasureSpec(heightMeasureSpec, lp.topMargin + lp.bottomMargin, lp.height);
                    child.measure(drawerWidthSpec, drawerHeightSpec);
                } else {
                    throw new java.lang.IllegalStateException((((("Child " + child) + " at index ") + i) + " does not have a valid layout_gravity - must be Gravity.LEFT, ") + "Gravity.RIGHT or Gravity.NO_GRAVITY");
                }

        }
    }

    private void resolveShadowDrawables() {
        if (android.support.v4.widget.DrawerLayout.SET_DRAWER_SHADOW_FROM_ELEVATION) {
            return;
        }
        mShadowLeftResolved = resolveLeftShadow();
        mShadowRightResolved = resolveRightShadow();
    }

    private android.graphics.drawable.Drawable resolveLeftShadow() {
        int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        // Prefer shadows defined with start/end gravity over left and right.
        if (layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) {
            if (mShadowStart != null) {
                // Correct drawable layout direction, if needed.
                mirror(mShadowStart, layoutDirection);
                return mShadowStart;
            }
        } else {
            if (mShadowEnd != null) {
                // Correct drawable layout direction, if needed.
                mirror(mShadowEnd, layoutDirection);
                return mShadowEnd;
            }
        }
        return mShadowLeft;
    }

    private android.graphics.drawable.Drawable resolveRightShadow() {
        int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        if (layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_LTR) {
            if (mShadowEnd != null) {
                // Correct drawable layout direction, if needed.
                mirror(mShadowEnd, layoutDirection);
                return mShadowEnd;
            }
        } else {
            if (mShadowStart != null) {
                // Correct drawable layout direction, if needed.
                mirror(mShadowStart, layoutDirection);
                return mShadowStart;
            }
        }
        return mShadowRight;
    }

    /**
     * Change the layout direction of the given drawable.
     * Return true if auto-mirror is supported and drawable's layout direction can be changed.
     * Otherwise, return false.
     */
    private boolean mirror(android.graphics.drawable.Drawable drawable, int layoutDirection) {
        if ((drawable == null) || (!android.support.v4.graphics.drawable.DrawableCompat.isAutoMirrored(drawable))) {
            return false;
        }
        android.support.v4.graphics.drawable.DrawableCompat.setLayoutDirection(drawable, layoutDirection);
        return true;
    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mInLayout = true;
        final int width = r - l;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (child.getVisibility() == android.view.View.GONE) {
                continue;
            }
            final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (child.getLayoutParams()));
            if (isContentView(child)) {
                child.layout(lp.leftMargin, lp.topMargin, lp.leftMargin + child.getMeasuredWidth(), lp.topMargin + child.getMeasuredHeight());
            } else {
                // Drawer, if it wasn't onMeasure would have thrown an exception.
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                int childLeft;
                final float newOffset;
                if (checkDrawerViewAbsoluteGravity(child, android.view.Gravity.LEFT)) {
                    childLeft = (-childWidth) + ((int) (childWidth * lp.onScreen));
                    newOffset = ((float) (childWidth + childLeft)) / childWidth;
                } else {
                    // Right; onMeasure checked for us.
                    childLeft = width - ((int) (childWidth * lp.onScreen));
                    newOffset = ((float) (width - childLeft)) / childWidth;
                }
                final boolean changeOffset = newOffset != lp.onScreen;
                final int vgrav = lp.gravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
                switch (vgrav) {
                    default :
                    case android.view.Gravity.TOP :
                        {
                            child.layout(childLeft, lp.topMargin, childLeft + childWidth, lp.topMargin + childHeight);
                            break;
                        }
                    case android.view.Gravity.BOTTOM :
                        {
                            final int height = b - t;
                            child.layout(childLeft, (height - lp.bottomMargin) - child.getMeasuredHeight(), childLeft + childWidth, height - lp.bottomMargin);
                            break;
                        }
                    case android.view.Gravity.CENTER_VERTICAL :
                        {
                            final int height = b - t;
                            int childTop = (height - childHeight) / 2;
                            // Offset for margins. If things don't fit right because of
                            // bad measurement before, oh well.
                            if (childTop < lp.topMargin) {
                                childTop = lp.topMargin;
                            } else
                                if ((childTop + childHeight) > (height - lp.bottomMargin)) {
                                    childTop = (height - lp.bottomMargin) - childHeight;
                                }

                            child.layout(childLeft, childTop, childLeft + childWidth, childTop + childHeight);
                            break;
                        }
                }
                if (changeOffset) {
                    setDrawerViewOffset(child, newOffset);
                }
                final int newVisibility = (lp.onScreen > 0) ? android.view.View.VISIBLE : android.view.View.INVISIBLE;
                if (child.getVisibility() != newVisibility) {
                    child.setVisibility(newVisibility);
                }
            }
        }
        mInLayout = false;
        mFirstLayout = false;
    }

    @java.lang.Override
    public void requestLayout() {
        if (!mInLayout) {
            super.requestLayout();
        }
    }

    @java.lang.Override
    public void computeScroll() {
        final int childCount = getChildCount();
        float scrimOpacity = 0;
        for (int i = 0; i < childCount; i++) {
            final float onscreen = ((android.support.v4.widget.DrawerLayout.LayoutParams) (getChildAt(i).getLayoutParams())).onScreen;
            scrimOpacity = java.lang.Math.max(scrimOpacity, onscreen);
        }
        mScrimOpacity = scrimOpacity;
        // "|" used on purpose; both need to run.
        if (mLeftDragger.continueSettling(true) | mRightDragger.continueSettling(true)) {
            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private static boolean hasOpaqueBackground(android.view.View v) {
        final android.graphics.drawable.Drawable bg = v.getBackground();
        if (bg != null) {
            return bg.getOpacity() == android.graphics.PixelFormat.OPAQUE;
        }
        return false;
    }

    /**
     * Set a drawable to draw in the insets area for the status bar.
     * Note that this will only be activated if this DrawerLayout fitsSystemWindows.
     *
     * @param bg
     * 		Background drawable to draw behind the status bar
     */
    public void setStatusBarBackground(android.graphics.drawable.Drawable bg) {
        mStatusBarBackground = bg;
        invalidate();
    }

    /**
     * Gets the drawable used to draw in the insets area for the status bar.
     *
     * @return The status bar background drawable, or null if none set
     */
    public android.graphics.drawable.Drawable getStatusBarBackgroundDrawable() {
        return mStatusBarBackground;
    }

    /**
     * Set a drawable to draw in the insets area for the status bar.
     * Note that this will only be activated if this DrawerLayout fitsSystemWindows.
     *
     * @param resId
     * 		Resource id of a background drawable to draw behind the status bar
     */
    public void setStatusBarBackground(int resId) {
        mStatusBarBackground = (resId != 0) ? android.support.v4.content.ContextCompat.getDrawable(getContext(), resId) : null;
        invalidate();
    }

    /**
     * Set a drawable to draw in the insets area for the status bar.
     * Note that this will only be activated if this DrawerLayout fitsSystemWindows.
     *
     * @param color
     * 		Color to use as a background drawable to draw behind the status bar
     * 		in 0xAARRGGBB format.
     */
    public void setStatusBarBackgroundColor(@android.support.annotation.ColorInt
    int color) {
        mStatusBarBackground = new android.graphics.drawable.ColorDrawable(color);
        invalidate();
    }

    public void onRtlPropertiesChanged(int layoutDirection) {
        resolveShadowDrawables();
    }

    @java.lang.Override
    public void onDraw(android.graphics.Canvas c) {
        super.onDraw(c);
        if (mDrawStatusBarBackground && (mStatusBarBackground != null)) {
            final int inset = android.support.v4.widget.DrawerLayout.IMPL.getTopInset(mLastInsets);
            if (inset > 0) {
                mStatusBarBackground.setBounds(0, 0, getWidth(), inset);
                mStatusBarBackground.draw(c);
            }
        }
    }

    @java.lang.Override
    protected boolean drawChild(android.graphics.Canvas canvas, android.view.View child, long drawingTime) {
        final int height = getHeight();
        final boolean drawingContent = isContentView(child);
        int clipLeft = 0;
        int clipRight = getWidth();
        final int restoreCount = canvas.save();
        if (drawingContent) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final android.view.View v = getChildAt(i);
                if (((((v == child) || (v.getVisibility() != android.view.View.VISIBLE)) || (!android.support.v4.widget.DrawerLayout.hasOpaqueBackground(v))) || (!isDrawerView(v))) || (v.getHeight() < height)) {
                    continue;
                }
                if (checkDrawerViewAbsoluteGravity(v, android.view.Gravity.LEFT)) {
                    final int vright = v.getRight();
                    if (vright > clipLeft)
                        clipLeft = vright;

                } else {
                    final int vleft = v.getLeft();
                    if (vleft < clipRight)
                        clipRight = vleft;

                }
            }
            canvas.clipRect(clipLeft, 0, clipRight, getHeight());
        }
        final boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreCount);
        if ((mScrimOpacity > 0) && drawingContent) {
            final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
            final int imag = ((int) (baseAlpha * mScrimOpacity));
            final int color = (imag << 24) | (mScrimColor & 0xffffff);
            mScrimPaint.setColor(color);
            canvas.drawRect(clipLeft, 0, clipRight, getHeight(), mScrimPaint);
        } else
            if ((mShadowLeftResolved != null) && checkDrawerViewAbsoluteGravity(child, android.view.Gravity.LEFT)) {
                final int shadowWidth = mShadowLeftResolved.getIntrinsicWidth();
                final int childRight = child.getRight();
                final int drawerPeekDistance = mLeftDragger.getEdgeSize();
                final float alpha = java.lang.Math.max(0, java.lang.Math.min(((float) (childRight)) / drawerPeekDistance, 1.0F));
                mShadowLeftResolved.setBounds(childRight, child.getTop(), childRight + shadowWidth, child.getBottom());
                mShadowLeftResolved.setAlpha(((int) (0xff * alpha)));
                mShadowLeftResolved.draw(canvas);
            } else
                if ((mShadowRightResolved != null) && checkDrawerViewAbsoluteGravity(child, android.view.Gravity.RIGHT)) {
                    final int shadowWidth = mShadowRightResolved.getIntrinsicWidth();
                    final int childLeft = child.getLeft();
                    final int showing = getWidth() - childLeft;
                    final int drawerPeekDistance = mRightDragger.getEdgeSize();
                    final float alpha = java.lang.Math.max(0, java.lang.Math.min(((float) (showing)) / drawerPeekDistance, 1.0F));
                    mShadowRightResolved.setBounds(childLeft - shadowWidth, child.getTop(), childLeft, child.getBottom());
                    mShadowRightResolved.setAlpha(((int) (0xff * alpha)));
                    mShadowRightResolved.draw(canvas);
                }


        return result;
    }

    boolean isContentView(android.view.View child) {
        return ((android.support.v4.widget.DrawerLayout.LayoutParams) (child.getLayoutParams())).gravity == android.view.Gravity.NO_GRAVITY;
    }

    boolean isDrawerView(android.view.View child) {
        final int gravity = ((android.support.v4.widget.DrawerLayout.LayoutParams) (child.getLayoutParams())).gravity;
        final int absGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(gravity, android.support.v4.view.ViewCompat.getLayoutDirection(child));
        if ((absGravity & android.view.Gravity.LEFT) != 0) {
            // This child is a left-edge drawer
            return true;
        }
        if ((absGravity & android.view.Gravity.RIGHT) != 0) {
            // This child is a right-edge drawer
            return true;
        }
        return false;
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        // "|" used deliberately here; both methods should be invoked.
        final boolean interceptForDrag = mLeftDragger.shouldInterceptTouchEvent(ev) | mRightDragger.shouldInterceptTouchEvent(ev);
        boolean interceptForTap = false;
        switch (action) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    mInitialMotionX = x;
                    mInitialMotionY = y;
                    if (mScrimOpacity > 0) {
                        final android.view.View child = mLeftDragger.findTopChildUnder(((int) (x)), ((int) (y)));
                        if ((child != null) && isContentView(child)) {
                            interceptForTap = true;
                        }
                    }
                    mDisallowInterceptRequested = false;
                    mChildrenCanceledTouch = false;
                    break;
                }
            case android.view.MotionEvent.ACTION_MOVE :
                {
                    // If we cross the touch slop, don't perform the delayed peek for an edge touch.
                    if (mLeftDragger.checkTouchSlop(android.support.v4.widget.ViewDragHelper.DIRECTION_ALL)) {
                        mLeftCallback.removeCallbacks();
                        mRightCallback.removeCallbacks();
                    }
                    break;
                }
            case android.view.MotionEvent.ACTION_CANCEL :
            case android.view.MotionEvent.ACTION_UP :
                {
                    closeDrawers(true);
                    mDisallowInterceptRequested = false;
                    mChildrenCanceledTouch = false;
                }
        }
        return ((interceptForDrag || interceptForTap) || hasPeekingDrawer()) || mChildrenCanceledTouch;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        mLeftDragger.processTouchEvent(ev);
        mRightDragger.processTouchEvent(ev);
        final int action = ev.getAction();
        boolean wantTouchEvents = true;
        switch (action & android.support.v4.view.MotionEventCompat.ACTION_MASK) {
            case android.view.MotionEvent.ACTION_DOWN :
                {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    mInitialMotionX = x;
                    mInitialMotionY = y;
                    mDisallowInterceptRequested = false;
                    mChildrenCanceledTouch = false;
                    break;
                }
            case android.view.MotionEvent.ACTION_UP :
                {
                    final float x = ev.getX();
                    final float y = ev.getY();
                    boolean peekingOnly = true;
                    final android.view.View touchedView = mLeftDragger.findTopChildUnder(((int) (x)), ((int) (y)));
                    if ((touchedView != null) && isContentView(touchedView)) {
                        final float dx = x - mInitialMotionX;
                        final float dy = y - mInitialMotionY;
                        final int slop = mLeftDragger.getTouchSlop();
                        if (((dx * dx) + (dy * dy)) < (slop * slop)) {
                            // Taps close a dimmed open drawer but only if it isn't locked open.
                            final android.view.View openDrawer = findOpenDrawer();
                            if (openDrawer != null) {
                                peekingOnly = getDrawerLockMode(openDrawer) == android.support.v4.widget.DrawerLayout.LOCK_MODE_LOCKED_OPEN;
                            }
                        }
                    }
                    closeDrawers(peekingOnly);
                    mDisallowInterceptRequested = false;
                    break;
                }
            case android.view.MotionEvent.ACTION_CANCEL :
                {
                    closeDrawers(true);
                    mDisallowInterceptRequested = false;
                    mChildrenCanceledTouch = false;
                    break;
                }
        }
        return wantTouchEvents;
    }

    @java.lang.Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        if (android.support.v4.widget.DrawerLayout.CHILDREN_DISALLOW_INTERCEPT || ((!mLeftDragger.isEdgeTouched(android.support.v4.widget.ViewDragHelper.EDGE_LEFT)) && (!mRightDragger.isEdgeTouched(android.support.v4.widget.ViewDragHelper.EDGE_RIGHT)))) {
            // If we have an edge touch we want to skip this and track it for later instead.
            super.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
        mDisallowInterceptRequested = disallowIntercept;
        if (disallowIntercept) {
            closeDrawers(true);
        }
    }

    /**
     * Close all currently open drawer views by animating them out of view.
     */
    public void closeDrawers() {
        closeDrawers(false);
    }

    void closeDrawers(boolean peekingOnly) {
        boolean needsInvalidate = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (child.getLayoutParams()));
            if ((!isDrawerView(child)) || (peekingOnly && (!lp.isPeeking))) {
                continue;
            }
            final int childWidth = child.getWidth();
            if (checkDrawerViewAbsoluteGravity(child, android.view.Gravity.LEFT)) {
                needsInvalidate |= mLeftDragger.smoothSlideViewTo(child, -childWidth, child.getTop());
            } else {
                needsInvalidate |= mRightDragger.smoothSlideViewTo(child, getWidth(), child.getTop());
            }
            lp.isPeeking = false;
        }
        mLeftCallback.removeCallbacks();
        mRightCallback.removeCallbacks();
        if (needsInvalidate) {
            invalidate();
        }
    }

    /**
     * Open the specified drawer view by animating it into view.
     *
     * @param drawerView
     * 		Drawer view to open
     */
    public void openDrawer(android.view.View drawerView) {
        openDrawer(drawerView, true);
    }

    /**
     * Open the specified drawer view.
     *
     * @param drawerView
     * 		Drawer view to open
     * @param animate
     * 		Whether opening of the drawer should be animated.
     */
    public void openDrawer(android.view.View drawerView, boolean animate) {
        if (!isDrawerView(drawerView)) {
            throw new java.lang.IllegalArgumentException(("View " + drawerView) + " is not a sliding drawer");
        }
        final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams()));
        if (mFirstLayout) {
            lp.onScreen = 1.0F;
            lp.openState = android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENED;
            updateChildrenImportantForAccessibility(drawerView, true);
        } else
            if (animate) {
                lp.openState |= android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENING;
                if (checkDrawerViewAbsoluteGravity(drawerView, android.view.Gravity.LEFT)) {
                    mLeftDragger.smoothSlideViewTo(drawerView, 0, drawerView.getTop());
                } else {
                    mRightDragger.smoothSlideViewTo(drawerView, getWidth() - drawerView.getWidth(), drawerView.getTop());
                }
            } else {
                moveDrawerToOffset(drawerView, 1.0F);
                updateDrawerState(lp.gravity, android.support.v4.widget.DrawerLayout.STATE_IDLE, drawerView);
                drawerView.setVisibility(android.view.View.VISIBLE);
            }

        invalidate();
    }

    /**
     * Open the specified drawer by animating it out of view.
     *
     * @param gravity
     * 		Gravity.LEFT to move the left drawer or Gravity.RIGHT for the right.
     * 		GravityCompat.START or GravityCompat.END may also be used.
     */
    public void openDrawer(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int gravity) {
        openDrawer(gravity, true);
    }

    /**
     * Open the specified drawer.
     *
     * @param gravity
     * 		Gravity.LEFT to move the left drawer or Gravity.RIGHT for the right.
     * 		GravityCompat.START or GravityCompat.END may also be used.
     * @param animate
     * 		Whether opening of the drawer should be animated.
     */
    public void openDrawer(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int gravity, boolean animate) {
        final android.view.View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            throw new java.lang.IllegalArgumentException("No drawer view found with gravity " + android.support.v4.widget.DrawerLayout.gravityToString(gravity));
        }
        openDrawer(drawerView, animate);
    }

    /**
     * Close the specified drawer view by animating it into view.
     *
     * @param drawerView
     * 		Drawer view to close
     */
    public void closeDrawer(android.view.View drawerView) {
        closeDrawer(drawerView, true);
    }

    /**
     * Close the specified drawer view.
     *
     * @param drawerView
     * 		Drawer view to close
     * @param animate
     * 		Whether closing of the drawer should be animated.
     */
    public void closeDrawer(android.view.View drawerView, boolean animate) {
        if (!isDrawerView(drawerView)) {
            throw new java.lang.IllegalArgumentException(("View " + drawerView) + " is not a sliding drawer");
        }
        final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawerView.getLayoutParams()));
        if (mFirstLayout) {
            lp.onScreen = 0.0F;
            lp.openState = 0;
        } else
            if (animate) {
                lp.openState |= android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_CLOSING;
                if (checkDrawerViewAbsoluteGravity(drawerView, android.view.Gravity.LEFT)) {
                    mLeftDragger.smoothSlideViewTo(drawerView, -drawerView.getWidth(), drawerView.getTop());
                } else {
                    mRightDragger.smoothSlideViewTo(drawerView, getWidth(), drawerView.getTop());
                }
            } else {
                moveDrawerToOffset(drawerView, 0.0F);
                updateDrawerState(lp.gravity, android.support.v4.widget.DrawerLayout.STATE_IDLE, drawerView);
                drawerView.setVisibility(android.view.View.INVISIBLE);
            }

        invalidate();
    }

    /**
     * Close the specified drawer by animating it out of view.
     *
     * @param gravity
     * 		Gravity.LEFT to move the left drawer or Gravity.RIGHT for the right.
     * 		GravityCompat.START or GravityCompat.END may also be used.
     */
    public void closeDrawer(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int gravity) {
        closeDrawer(gravity, true);
    }

    /**
     * Close the specified drawer.
     *
     * @param gravity
     * 		Gravity.LEFT to move the left drawer or Gravity.RIGHT for the right.
     * 		GravityCompat.START or GravityCompat.END may also be used.
     * @param animate
     * 		Whether closing of the drawer should be animated.
     */
    public void closeDrawer(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int gravity, boolean animate) {
        final android.view.View drawerView = findDrawerWithGravity(gravity);
        if (drawerView == null) {
            throw new java.lang.IllegalArgumentException("No drawer view found with gravity " + android.support.v4.widget.DrawerLayout.gravityToString(gravity));
        }
        closeDrawer(drawerView, animate);
    }

    /**
     * Check if the given drawer view is currently in an open state.
     * To be considered "open" the drawer must have settled into its fully
     * visible state. To check for partial visibility use
     * {@link #isDrawerVisible(android.view.View)}.
     *
     * @param drawer
     * 		Drawer view to check
     * @return true if the given drawer view is in an open state
     * @see #isDrawerVisible(android.view.View)
     */
    public boolean isDrawerOpen(android.view.View drawer) {
        if (!isDrawerView(drawer)) {
            throw new java.lang.IllegalArgumentException(("View " + drawer) + " is not a drawer");
        }
        android.support.v4.widget.DrawerLayout.LayoutParams drawerLp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawer.getLayoutParams()));
        return (drawerLp.openState & android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENED) == 1;
    }

    /**
     * Check if the given drawer view is currently in an open state.
     * To be considered "open" the drawer must have settled into its fully
     * visible state. If there is no drawer with the given gravity this method
     * will return false.
     *
     * @param drawerGravity
     * 		Gravity of the drawer to check
     * @return true if the given drawer view is in an open state
     */
    public boolean isDrawerOpen(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int drawerGravity) {
        final android.view.View drawerView = findDrawerWithGravity(drawerGravity);
        if (drawerView != null) {
            return isDrawerOpen(drawerView);
        }
        return false;
    }

    /**
     * Check if a given drawer view is currently visible on-screen. The drawer
     * may be only peeking onto the screen, fully extended, or anywhere inbetween.
     *
     * @param drawer
     * 		Drawer view to check
     * @return true if the given drawer is visible on-screen
     * @see #isDrawerOpen(android.view.View)
     */
    public boolean isDrawerVisible(android.view.View drawer) {
        if (!isDrawerView(drawer)) {
            throw new java.lang.IllegalArgumentException(("View " + drawer) + " is not a drawer");
        }
        return ((android.support.v4.widget.DrawerLayout.LayoutParams) (drawer.getLayoutParams())).onScreen > 0;
    }

    /**
     * Check if a given drawer view is currently visible on-screen. The drawer
     * may be only peeking onto the screen, fully extended, or anywhere in between.
     * If there is no drawer with the given gravity this method will return false.
     *
     * @param drawerGravity
     * 		Gravity of the drawer to check
     * @return true if the given drawer is visible on-screen
     */
    public boolean isDrawerVisible(@android.support.v4.widget.DrawerLayout.EdgeGravity
    int drawerGravity) {
        final android.view.View drawerView = findDrawerWithGravity(drawerGravity);
        if (drawerView != null) {
            return isDrawerVisible(drawerView);
        }
        return false;
    }

    private boolean hasPeekingDrawer() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (getChildAt(i).getLayoutParams()));
            if (lp.isPeeking) {
                return true;
            }
        }
        return false;
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new android.support.v4.widget.DrawerLayout.LayoutParams(android.support.v4.widget.DrawerLayout.LayoutParams.MATCH_PARENT, android.support.v4.widget.DrawerLayout.LayoutParams.MATCH_PARENT);
    }

    @java.lang.Override
    protected android.view.ViewGroup.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return p instanceof android.support.v4.widget.DrawerLayout.LayoutParams ? new android.support.v4.widget.DrawerLayout.LayoutParams(((android.support.v4.widget.DrawerLayout.LayoutParams) (p))) : p instanceof android.view.ViewGroup.MarginLayoutParams ? new android.support.v4.widget.DrawerLayout.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (p))) : new android.support.v4.widget.DrawerLayout.LayoutParams(p);
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof android.support.v4.widget.DrawerLayout.LayoutParams) && super.checkLayoutParams(p);
    }

    @java.lang.Override
    public android.view.ViewGroup.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.v4.widget.DrawerLayout.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    public void addFocusables(java.util.ArrayList<android.view.View> views, int direction, int focusableMode) {
        if (getDescendantFocusability() == android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS) {
            return;
        }
        // Only the views in the open drawers are focusables. Add normal child views when
        // no drawers are opened.
        final int childCount = getChildCount();
        boolean isDrawerOpen = false;
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (isDrawerView(child)) {
                if (isDrawerOpen(child)) {
                    isDrawerOpen = true;
                    child.addFocusables(views, direction, focusableMode);
                }
            } else {
                mNonDrawerViews.add(child);
            }
        }
        if (!isDrawerOpen) {
            final int nonDrawerViewsCount = mNonDrawerViews.size();
            for (int i = 0; i < nonDrawerViewsCount; ++i) {
                final android.view.View child = mNonDrawerViews.get(i);
                if (child.getVisibility() == android.view.View.VISIBLE) {
                    child.addFocusables(views, direction, focusableMode);
                }
            }
        }
        mNonDrawerViews.clear();
    }

    private boolean hasVisibleDrawer() {
        return findVisibleDrawer() != null;
    }

    android.view.View findVisibleDrawer() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (isDrawerView(child) && isDrawerVisible(child)) {
                return child;
            }
        }
        return null;
    }

    void cancelChildViewTouch() {
        // Cancel child touches
        if (!mChildrenCanceledTouch) {
            final long now = android.os.SystemClock.uptimeMillis();
            final android.view.MotionEvent cancelEvent = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_CANCEL, 0.0F, 0.0F, 0);
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).dispatchTouchEvent(cancelEvent);
            }
            cancelEvent.recycle();
            mChildrenCanceledTouch = true;
        }
    }

    @java.lang.Override
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if ((keyCode == android.view.KeyEvent.KEYCODE_BACK) && hasVisibleDrawer()) {
            event.startTracking();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @java.lang.Override
    public boolean onKeyUp(int keyCode, android.view.KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            final android.view.View visibleDrawer = findVisibleDrawer();
            if ((visibleDrawer != null) && (getDrawerLockMode(visibleDrawer) == android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED)) {
                closeDrawers();
            }
            return visibleDrawer != null;
        }
        return super.onKeyUp(keyCode, event);
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.v4.widget.DrawerLayout.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final android.support.v4.widget.DrawerLayout.SavedState ss = ((android.support.v4.widget.DrawerLayout.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        if (ss.openDrawerGravity != android.view.Gravity.NO_GRAVITY) {
            final android.view.View toOpen = findDrawerWithGravity(ss.openDrawerGravity);
            if (toOpen != null) {
                openDrawer(toOpen);
            }
        }
        if (ss.lockModeLeft != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
            setDrawerLockMode(ss.lockModeLeft, android.view.Gravity.LEFT);
        }
        if (ss.lockModeRight != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
            setDrawerLockMode(ss.lockModeRight, android.view.Gravity.RIGHT);
        }
        if (ss.lockModeStart != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
            setDrawerLockMode(ss.lockModeStart, android.support.v4.view.GravityCompat.START);
        }
        if (ss.lockModeEnd != android.support.v4.widget.DrawerLayout.LOCK_MODE_UNDEFINED) {
            setDrawerLockMode(ss.lockModeEnd, android.support.v4.view.GravityCompat.END);
        }
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.os.Parcelable superState = super.onSaveInstanceState();
        final android.support.v4.widget.DrawerLayout.SavedState ss = new android.support.v4.widget.DrawerLayout.SavedState(superState);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (child.getLayoutParams()));
            // Is the current child fully opened (that is, not closing)?
            boolean isOpenedAndNotClosing = lp.openState == android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENED;
            // Is the current child opening?
            boolean isClosedAndOpening = lp.openState == android.support.v4.widget.DrawerLayout.LayoutParams.FLAG_IS_OPENING;
            if (isOpenedAndNotClosing || isClosedAndOpening) {
                // If one of the conditions above holds, save the child's gravity
                // so that we open that child during state restore.
                ss.openDrawerGravity = lp.gravity;
                break;
            }
        }
        ss.lockModeLeft = mLockModeLeft;
        ss.lockModeRight = mLockModeRight;
        ss.lockModeStart = mLockModeStart;
        ss.lockModeEnd = mLockModeEnd;
        return ss;
    }

    @java.lang.Override
    public void addView(android.view.View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        final android.view.View openDrawer = findOpenDrawer();
        if ((openDrawer != null) || isDrawerView(child)) {
            // A drawer is already open or the new view is a drawer, so the
            // new view should start out hidden.
            android.support.v4.view.ViewCompat.setImportantForAccessibility(child, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS);
        } else {
            // Otherwise this is a content view and no drawer is open, so the
            // new view should start out visible.
            android.support.v4.view.ViewCompat.setImportantForAccessibility(child, android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }
        // We only need a delegate here if the framework doesn't understand
        // NO_HIDE_DESCENDANTS importance.
        if (!android.support.v4.widget.DrawerLayout.CAN_HIDE_DESCENDANTS) {
            android.support.v4.view.ViewCompat.setAccessibilityDelegate(child, mChildAccessibilityDelegate);
        }
    }

    static boolean includeChildForAccessibility(android.view.View child) {
        // If the child is not important for accessibility we make
        // sure this hides the entire subtree rooted at it as the
        // IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDATS is not
        // supported on older platforms but we want to hide the entire
        // content and not opened drawers if a drawer is opened.
        return (android.support.v4.view.ViewCompat.getImportantForAccessibility(child) != android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO_HIDE_DESCENDANTS) && (android.support.v4.view.ViewCompat.getImportantForAccessibility(child) != android.support.v4.view.ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
    }

    /**
     * State persisted across instances
     */
    protected static class SavedState extends android.support.v4.view.AbsSavedState {
        int openDrawerGravity = android.view.Gravity.NO_GRAVITY;

        @android.support.v4.widget.DrawerLayout.LockMode
        int lockModeLeft;

        @android.support.v4.widget.DrawerLayout.LockMode
        int lockModeRight;

        @android.support.v4.widget.DrawerLayout.LockMode
        int lockModeStart;

        @android.support.v4.widget.DrawerLayout.LockMode
        int lockModeEnd;

        public SavedState(android.os.Parcel in, java.lang.ClassLoader loader) {
            super(in, loader);
            openDrawerGravity = in.readInt();
            lockModeLeft = in.readInt();
            lockModeRight = in.readInt();
            lockModeStart = in.readInt();
            lockModeEnd = in.readInt();
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(openDrawerGravity);
            dest.writeInt(lockModeLeft);
            dest.writeInt(lockModeRight);
            dest.writeInt(lockModeStart);
            dest.writeInt(lockModeEnd);
        }

        public static final android.os.Parcelable.Creator<android.support.v4.widget.DrawerLayout.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.v4.widget.DrawerLayout.SavedState>() {
            @java.lang.Override
            public android.support.v4.widget.DrawerLayout.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.support.v4.widget.DrawerLayout.SavedState(in, loader);
            }

            @java.lang.Override
            public android.support.v4.widget.DrawerLayout.SavedState[] newArray(int size) {
                return new android.support.v4.widget.DrawerLayout.SavedState[size];
            }
        });
    }

    private class ViewDragCallback extends android.support.v4.widget.ViewDragHelper.Callback {
        private final int mAbsGravity;

        private android.support.v4.widget.ViewDragHelper mDragger;

        private final java.lang.Runnable mPeekRunnable = new java.lang.Runnable() {
            @java.lang.Override
            public void run() {
                peekDrawer();
            }
        };

        ViewDragCallback(int gravity) {
            mAbsGravity = gravity;
        }

        public void setDragger(android.support.v4.widget.ViewDragHelper dragger) {
            mDragger = dragger;
        }

        public void removeCallbacks() {
            android.support.v4.widget.DrawerLayout.this.removeCallbacks(mPeekRunnable);
        }

        @java.lang.Override
        public boolean tryCaptureView(android.view.View child, int pointerId) {
            // Only capture views where the gravity matches what we're looking for.
            // This lets us use two ViewDragHelpers, one for each side drawer.
            return (isDrawerView(child) && checkDrawerViewAbsoluteGravity(child, mAbsGravity)) && (getDrawerLockMode(child) == android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        @java.lang.Override
        public void onViewDragStateChanged(int state) {
            updateDrawerState(mAbsGravity, state, mDragger.getCapturedView());
        }

        @java.lang.Override
        public void onViewPositionChanged(android.view.View changedView, int left, int top, int dx, int dy) {
            float offset;
            final int childWidth = changedView.getWidth();
            // This reverses the positioning shown in onLayout.
            if (checkDrawerViewAbsoluteGravity(changedView, android.view.Gravity.LEFT)) {
                offset = ((float) (childWidth + left)) / childWidth;
            } else {
                final int width = getWidth();
                offset = ((float) (width - left)) / childWidth;
            }
            setDrawerViewOffset(changedView, offset);
            changedView.setVisibility(offset == 0 ? android.view.View.INVISIBLE : android.view.View.VISIBLE);
            invalidate();
        }

        @java.lang.Override
        public void onViewCaptured(android.view.View capturedChild, int activePointerId) {
            final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (capturedChild.getLayoutParams()));
            lp.isPeeking = false;
            closeOtherDrawer();
        }

        private void closeOtherDrawer() {
            final int otherGrav = (mAbsGravity == android.view.Gravity.LEFT) ? android.view.Gravity.RIGHT : android.view.Gravity.LEFT;
            final android.view.View toClose = findDrawerWithGravity(otherGrav);
            if (toClose != null) {
                closeDrawer(toClose);
            }
        }

        @java.lang.Override
        public void onViewReleased(android.view.View releasedChild, float xvel, float yvel) {
            // Offset is how open the drawer is, therefore left/right values
            // are reversed from one another.
            final float offset = getDrawerViewOffset(releasedChild);
            final int childWidth = releasedChild.getWidth();
            int left;
            if (checkDrawerViewAbsoluteGravity(releasedChild, android.view.Gravity.LEFT)) {
                left = ((xvel > 0) || ((xvel == 0) && (offset > 0.5F))) ? 0 : -childWidth;
            } else {
                final int width = getWidth();
                left = ((xvel < 0) || ((xvel == 0) && (offset > 0.5F))) ? width - childWidth : width;
            }
            mDragger.settleCapturedViewAt(left, releasedChild.getTop());
            invalidate();
        }

        @java.lang.Override
        public void onEdgeTouched(int edgeFlags, int pointerId) {
            postDelayed(mPeekRunnable, android.support.v4.widget.DrawerLayout.PEEK_DELAY);
        }

        void peekDrawer() {
            final android.view.View toCapture;
            final int childLeft;
            final int peekDistance = mDragger.getEdgeSize();
            final boolean leftEdge = mAbsGravity == android.view.Gravity.LEFT;
            if (leftEdge) {
                toCapture = findDrawerWithGravity(android.view.Gravity.LEFT);
                childLeft = (toCapture != null ? -toCapture.getWidth() : 0) + peekDistance;
            } else {
                toCapture = findDrawerWithGravity(android.view.Gravity.RIGHT);
                childLeft = getWidth() - peekDistance;
            }
            // Only peek if it would mean making the drawer more visible and the drawer isn't locked
            if (((toCapture != null) && ((leftEdge && (toCapture.getLeft() < childLeft)) || ((!leftEdge) && (toCapture.getLeft() > childLeft)))) && (getDrawerLockMode(toCapture) == android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED)) {
                final android.support.v4.widget.DrawerLayout.LayoutParams lp = ((android.support.v4.widget.DrawerLayout.LayoutParams) (toCapture.getLayoutParams()));
                mDragger.smoothSlideViewTo(toCapture, childLeft, toCapture.getTop());
                lp.isPeeking = true;
                invalidate();
                closeOtherDrawer();
                cancelChildViewTouch();
            }
        }

        @java.lang.Override
        public boolean onEdgeLock(int edgeFlags) {
            if (android.support.v4.widget.DrawerLayout.ALLOW_EDGE_LOCK) {
                final android.view.View drawer = findDrawerWithGravity(mAbsGravity);
                if ((drawer != null) && (!isDrawerOpen(drawer))) {
                    closeDrawer(drawer);
                }
                return true;
            }
            return false;
        }

        @java.lang.Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            final android.view.View toCapture;
            if ((edgeFlags & android.support.v4.widget.ViewDragHelper.EDGE_LEFT) == android.support.v4.widget.ViewDragHelper.EDGE_LEFT) {
                toCapture = findDrawerWithGravity(android.view.Gravity.LEFT);
            } else {
                toCapture = findDrawerWithGravity(android.view.Gravity.RIGHT);
            }
            if ((toCapture != null) && (getDrawerLockMode(toCapture) == android.support.v4.widget.DrawerLayout.LOCK_MODE_UNLOCKED)) {
                mDragger.captureChildView(toCapture, pointerId);
            }
        }

        @java.lang.Override
        public int getViewHorizontalDragRange(android.view.View child) {
            return isDrawerView(child) ? child.getWidth() : 0;
        }

        @java.lang.Override
        public int clampViewPositionHorizontal(android.view.View child, int left, int dx) {
            if (checkDrawerViewAbsoluteGravity(child, android.view.Gravity.LEFT)) {
                return java.lang.Math.max(-child.getWidth(), java.lang.Math.min(left, 0));
            } else {
                final int width = getWidth();
                return java.lang.Math.max(width - child.getWidth(), java.lang.Math.min(left, width));
            }
        }

        @java.lang.Override
        public int clampViewPositionVertical(android.view.View child, int top, int dy) {
            return child.getTop();
        }
    }

    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        private static final int FLAG_IS_OPENED = 0x1;

        private static final int FLAG_IS_OPENING = 0x2;

        private static final int FLAG_IS_CLOSING = 0x4;

        public int gravity = android.view.Gravity.NO_GRAVITY;

        float onScreen;

        boolean isPeeking;

        int openState;

        public LayoutParams(android.content.Context c, android.util.AttributeSet attrs) {
            super(c, attrs);
            final android.content.res.TypedArray a = c.obtainStyledAttributes(attrs, android.support.v4.widget.DrawerLayout.LAYOUT_ATTRS);
            this.gravity = a.getInt(0, android.view.Gravity.NO_GRAVITY);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, int gravity) {
            this(width, height);
            this.gravity = gravity;
        }

        public LayoutParams(android.support.v4.widget.DrawerLayout.LayoutParams source) {
            super(source);
            this.gravity = source.gravity;
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams source) {
            super(source);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams source) {
            super(source);
        }
    }

    class AccessibilityDelegate extends android.support.v4.view.AccessibilityDelegateCompat {
        private final android.graphics.Rect mTmpRect = new android.graphics.Rect();

        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View host, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            if (android.support.v4.widget.DrawerLayout.CAN_HIDE_DESCENDANTS) {
                super.onInitializeAccessibilityNodeInfo(host, info);
            } else {
                // Obtain a node for the host, then manually generate the list
                // of children to only include non-obscured views.
                final android.support.v4.view.accessibility.AccessibilityNodeInfoCompat superNode = android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.obtain(info);
                super.onInitializeAccessibilityNodeInfo(host, superNode);
                info.setSource(host);
                final android.view.ViewParent parent = android.support.v4.view.ViewCompat.getParentForAccessibility(host);
                if (parent instanceof android.view.View) {
                    info.setParent(((android.view.View) (parent)));
                }
                copyNodeInfoNoChildren(info, superNode);
                superNode.recycle();
                addChildrenForAccessibility(info, ((android.view.ViewGroup) (host)));
            }
            info.setClassName(android.support.v4.widget.DrawerLayout.class.getName());
            // This view reports itself as focusable so that it can intercept
            // the back button, but we should prevent this view from reporting
            // itself as focusable to accessibility services.
            info.setFocusable(false);
            info.setFocused(false);
            info.removeAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_FOCUS);
            info.removeAction(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLEAR_FOCUS);
        }

        @java.lang.Override
        public void onInitializeAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            super.onInitializeAccessibilityEvent(host, event);
            event.setClassName(android.support.v4.widget.DrawerLayout.class.getName());
        }

        @java.lang.Override
        public boolean dispatchPopulateAccessibilityEvent(android.view.View host, android.view.accessibility.AccessibilityEvent event) {
            // Special case to handle window state change events. As far as
            // accessibility services are concerned, state changes from
            // DrawerLayout invalidate the entire contents of the screen (like
            // an Activity or Dialog) and they should announce the title of the
            // new content.
            if (event.getEventType() == android.view.accessibility.AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                final java.util.List<java.lang.CharSequence> eventText = event.getText();
                final android.view.View visibleDrawer = findVisibleDrawer();
                if (visibleDrawer != null) {
                    final int edgeGravity = getDrawerViewAbsoluteGravity(visibleDrawer);
                    final java.lang.CharSequence title = getDrawerTitle(edgeGravity);
                    if (title != null) {
                        eventText.add(title);
                    }
                }
                return true;
            }
            return super.dispatchPopulateAccessibilityEvent(host, event);
        }

        @java.lang.Override
        public boolean onRequestSendAccessibilityEvent(android.view.ViewGroup host, android.view.View child, android.view.accessibility.AccessibilityEvent event) {
            if (android.support.v4.widget.DrawerLayout.CAN_HIDE_DESCENDANTS || android.support.v4.widget.DrawerLayout.includeChildForAccessibility(child)) {
                return super.onRequestSendAccessibilityEvent(host, child, event);
            }
            return false;
        }

        private void addChildrenForAccessibility(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info, android.view.ViewGroup v) {
            final int childCount = v.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final android.view.View child = v.getChildAt(i);
                if (android.support.v4.widget.DrawerLayout.includeChildForAccessibility(child)) {
                    info.addChild(child);
                }
            }
        }

        /**
         * This should really be in AccessibilityNodeInfoCompat, but there unfortunately
         * seem to be a few elements that are not easily cloneable using the underlying API.
         * Leave it private here as it's not general-purpose useful.
         */
        private void copyNodeInfoNoChildren(android.support.v4.view.accessibility.AccessibilityNodeInfoCompat dest, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat src) {
            final android.graphics.Rect rect = mTmpRect;
            src.getBoundsInParent(rect);
            dest.setBoundsInParent(rect);
            src.getBoundsInScreen(rect);
            dest.setBoundsInScreen(rect);
            dest.setVisibleToUser(src.isVisibleToUser());
            dest.setPackageName(src.getPackageName());
            dest.setClassName(src.getClassName());
            dest.setContentDescription(src.getContentDescription());
            dest.setEnabled(src.isEnabled());
            dest.setClickable(src.isClickable());
            dest.setFocusable(src.isFocusable());
            dest.setFocused(src.isFocused());
            dest.setAccessibilityFocused(src.isAccessibilityFocused());
            dest.setSelected(src.isSelected());
            dest.setLongClickable(src.isLongClickable());
            dest.addAction(src.getActions());
        }
    }

    final class ChildAccessibilityDelegate extends android.support.v4.view.AccessibilityDelegateCompat {
        @java.lang.Override
        public void onInitializeAccessibilityNodeInfo(android.view.View child, android.support.v4.view.accessibility.AccessibilityNodeInfoCompat info) {
            super.onInitializeAccessibilityNodeInfo(child, info);
            if (!android.support.v4.widget.DrawerLayout.includeChildForAccessibility(child)) {
                // If we are ignoring the sub-tree rooted at the child,
                // break the connection to the rest of the node tree.
                // For details refer to includeChildForAccessibility.
                info.setParent(null);
            }
        }
    }
}

