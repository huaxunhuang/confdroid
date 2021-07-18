/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.design.widget;


/**
 * CoordinatorLayout is a super-powered {@link android.widget.FrameLayout FrameLayout}.
 *
 * <p>CoordinatorLayout is intended for two primary use cases:</p>
 * <ol>
 *     <li>As a top-level application decor or chrome layout</li>
 *     <li>As a container for a specific interaction with one or more child views</li>
 * </ol>
 *
 * <p>By specifying {@link CoordinatorLayout.Behavior Behaviors} for child views of a
 * CoordinatorLayout you can provide many different interactions within a single parent and those
 * views can also interact with one another. View classes can specify a default behavior when
 * used as a child of a CoordinatorLayout using the
 * {@link CoordinatorLayout.DefaultBehavior DefaultBehavior} annotation.</p>
 *
 * <p>Behaviors may be used to implement a variety of interactions and additional layout
 * modifications ranging from sliding drawers and panels to swipe-dismissable elements and buttons
 * that stick to other elements as they move and animate.</p>
 *
 * <p>Children of a CoordinatorLayout may have an
 * {@link CoordinatorLayout.LayoutParams#setAnchorId(int) anchor}. This view id must correspond
 * to an arbitrary descendant of the CoordinatorLayout, but it may not be the anchored child itself
 * or a descendant of the anchored child. This can be used to place floating views relative to
 * other arbitrary content panes.</p>
 *
 * <p>Children can specify {@link CoordinatorLayout.LayoutParams#insetEdge} to describe how the
 * view insets the CoordinatorLayout. Any child views which are set to dodge the same inset edges by
 * {@link CoordinatorLayout.LayoutParams#dodgeInsetEdges} will be moved appropriately so that the
 * views do not overlap.</p>
 */
public class CoordinatorLayout extends android.view.ViewGroup implements android.support.v4.view.NestedScrollingParent {
    static final java.lang.String TAG = "CoordinatorLayout";

    static final java.lang.String WIDGET_PACKAGE_NAME;

    static {
        final java.lang.Package pkg = android.support.design.widget.CoordinatorLayout.class.getPackage();
        WIDGET_PACKAGE_NAME = (pkg != null) ? pkg.getName() : null;
    }

    private static final int TYPE_ON_INTERCEPT = 0;

    private static final int TYPE_ON_TOUCH = 1;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            TOP_SORTED_CHILDREN_COMPARATOR = new android.support.design.widget.CoordinatorLayout.ViewElevationComparator();
        } else {
            TOP_SORTED_CHILDREN_COMPARATOR = null;
        }
    }

    static final java.lang.Class<?>[] CONSTRUCTOR_PARAMS = new java.lang.Class<?>[]{ android.content.Context.class, android.util.AttributeSet.class };

    static final java.lang.ThreadLocal<java.util.Map<java.lang.String, java.lang.reflect.Constructor<android.support.design.widget.CoordinatorLayout.Behavior>>> sConstructors = new java.lang.ThreadLocal<>();

    static final int EVENT_PRE_DRAW = 0;

    static final int EVENT_NESTED_SCROLL = 1;

    static final int EVENT_VIEW_REMOVED = 2;

    /**
     *
     *
     * @unknown 
     */
    @android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.support.annotation.IntDef({ android.support.design.widget.CoordinatorLayout.EVENT_PRE_DRAW, android.support.design.widget.CoordinatorLayout.EVENT_NESTED_SCROLL, android.support.design.widget.CoordinatorLayout.EVENT_VIEW_REMOVED })
    public @interface DispatchChangeEvent {}

    static final java.util.Comparator<android.view.View> TOP_SORTED_CHILDREN_COMPARATOR;

    private final java.util.List<android.view.View> mDependencySortedChildren = new java.util.ArrayList<>();

    private final android.support.design.widget.DirectedAcyclicGraph<android.view.View> mChildDag = new android.support.design.widget.DirectedAcyclicGraph<>();

    private final java.util.List<android.view.View> mTempList1 = new java.util.ArrayList<>();

    private final java.util.List<android.view.View> mTempDependenciesList = new java.util.ArrayList<>();

    private final android.graphics.Rect mTempRect1 = new android.graphics.Rect();

    private final android.graphics.Rect mTempRect2 = new android.graphics.Rect();

    private final android.graphics.Rect mTempRect3 = new android.graphics.Rect();

    private final android.graphics.Rect mTempRect4 = new android.graphics.Rect();

    private final android.graphics.Rect mTempRect5 = new android.graphics.Rect();

    private final int[] mTempIntPair = new int[2];

    private android.graphics.Paint mScrimPaint;

    private boolean mDisallowInterceptReset;

    private boolean mIsAttachedToWindow;

    private int[] mKeylines;

    private android.view.View mBehaviorTouchView;

    private android.view.View mNestedScrollingDirectChild;

    private android.view.View mNestedScrollingTarget;

    private android.support.design.widget.CoordinatorLayout.OnPreDrawListener mOnPreDrawListener;

    private boolean mNeedsPreDrawListener;

    private android.support.v4.view.WindowInsetsCompat mLastInsets;

    private boolean mDrawStatusBarBackground;

    private android.graphics.drawable.Drawable mStatusBarBackground;

    android.view.ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

    private android.support.v4.view.OnApplyWindowInsetsListener mApplyWindowInsetsListener;

    private final android.support.v4.view.NestedScrollingParentHelper mNestedScrollingParentHelper = new android.support.v4.view.NestedScrollingParentHelper(this);

    public CoordinatorLayout(android.content.Context context) {
        this(context, null);
    }

    public CoordinatorLayout(android.content.Context context, android.util.AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoordinatorLayout(android.content.Context context, android.util.AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        android.support.design.widget.ThemeUtils.checkAppCompatTheme(context);
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoordinatorLayout, defStyleAttr, R.style.Widget_Design_CoordinatorLayout);
        final int keylineArrayRes = a.getResourceId(R.styleable.CoordinatorLayout_keylines, 0);
        if (keylineArrayRes != 0) {
            final android.content.res.Resources res = context.getResources();
            mKeylines = res.getIntArray(keylineArrayRes);
            final float density = res.getDisplayMetrics().density;
            final int count = mKeylines.length;
            for (int i = 0; i < count; i++) {
                mKeylines[i] *= density;
            }
        }
        mStatusBarBackground = a.getDrawable(R.styleable.CoordinatorLayout_statusBarBackground);
        a.recycle();
        setupForInsets();
        super.setOnHierarchyChangeListener(new android.support.design.widget.CoordinatorLayout.HierarchyChangeListener());
    }

    @java.lang.Override
    public void setOnHierarchyChangeListener(android.view.ViewGroup.OnHierarchyChangeListener onHierarchyChangeListener) {
        mOnHierarchyChangeListener = onHierarchyChangeListener;
    }

    @java.lang.Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        resetTouchBehaviors();
        if (mNeedsPreDrawListener) {
            if (mOnPreDrawListener == null) {
                mOnPreDrawListener = new android.support.design.widget.CoordinatorLayout.OnPreDrawListener();
            }
            final android.view.ViewTreeObserver vto = getViewTreeObserver();
            vto.addOnPreDrawListener(mOnPreDrawListener);
        }
        if ((mLastInsets == null) && android.support.v4.view.ViewCompat.getFitsSystemWindows(this)) {
            // We're set to fitSystemWindows but we haven't had any insets yet...
            // We should request a new dispatch of window insets
            android.support.v4.view.ViewCompat.requestApplyInsets(this);
        }
        mIsAttachedToWindow = true;
    }

    @java.lang.Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        resetTouchBehaviors();
        if (mNeedsPreDrawListener && (mOnPreDrawListener != null)) {
            final android.view.ViewTreeObserver vto = getViewTreeObserver();
            vto.removeOnPreDrawListener(mOnPreDrawListener);
        }
        if (mNestedScrollingTarget != null) {
            onStopNestedScroll(mNestedScrollingTarget);
        }
        mIsAttachedToWindow = false;
    }

    /**
     * Set a drawable to draw in the insets area for the status bar.
     * Note that this will only be activated if this DrawerLayout fitsSystemWindows.
     *
     * @param bg
     * 		Background drawable to draw behind the status bar
     */
    public void setStatusBarBackground(@android.support.annotation.Nullable
    final android.graphics.drawable.Drawable bg) {
        if (mStatusBarBackground != bg) {
            if (mStatusBarBackground != null) {
                mStatusBarBackground.setCallback(null);
            }
            mStatusBarBackground = (bg != null) ? bg.mutate() : null;
            if (mStatusBarBackground != null) {
                if (mStatusBarBackground.isStateful()) {
                    mStatusBarBackground.setState(getDrawableState());
                }
                android.support.v4.graphics.drawable.DrawableCompat.setLayoutDirection(mStatusBarBackground, android.support.v4.view.ViewCompat.getLayoutDirection(this));
                mStatusBarBackground.setVisible(getVisibility() == android.view.View.VISIBLE, false);
                mStatusBarBackground.setCallback(this);
            }
            android.support.v4.view.ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    /**
     * Gets the drawable used to draw in the insets area for the status bar.
     *
     * @return The status bar background drawable, or null if none set
     */
    @android.support.annotation.Nullable
    public android.graphics.drawable.Drawable getStatusBarBackground() {
        return mStatusBarBackground;
    }

    @java.lang.Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final int[] state = getDrawableState();
        boolean changed = false;
        android.graphics.drawable.Drawable d = mStatusBarBackground;
        if ((d != null) && d.isStateful()) {
            changed |= d.setState(state);
        }
        if (changed) {
            invalidate();
        }
    }

    @java.lang.Override
    protected boolean verifyDrawable(android.graphics.drawable.Drawable who) {
        return super.verifyDrawable(who) || (who == mStatusBarBackground);
    }

    @java.lang.Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        final boolean visible = visibility == android.view.View.VISIBLE;
        if ((mStatusBarBackground != null) && (mStatusBarBackground.isVisible() != visible)) {
            mStatusBarBackground.setVisible(visible, false);
        }
    }

    /**
     * Set a drawable to draw in the insets area for the status bar.
     * Note that this will only be activated if this DrawerLayout fitsSystemWindows.
     *
     * @param resId
     * 		Resource id of a background drawable to draw behind the status bar
     */
    public void setStatusBarBackgroundResource(@android.support.annotation.DrawableRes
    int resId) {
        setStatusBarBackground(resId != 0 ? android.support.v4.content.ContextCompat.getDrawable(getContext(), resId) : null);
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
        setStatusBarBackground(new android.graphics.drawable.ColorDrawable(color));
    }

    final android.support.v4.view.WindowInsetsCompat setWindowInsets(android.support.v4.view.WindowInsetsCompat insets) {
        if (!android.support.design.widget.ViewUtils.objectEquals(mLastInsets, insets)) {
            mLastInsets = insets;
            mDrawStatusBarBackground = (insets != null) && (insets.getSystemWindowInsetTop() > 0);
            setWillNotDraw((!mDrawStatusBarBackground) && (getBackground() == null));
            // Now dispatch to the Behaviors
            insets = dispatchApplyWindowInsetsToBehaviors(insets);
            requestLayout();
        }
        return insets;
    }

    final android.support.v4.view.WindowInsetsCompat getLastWindowInsets() {
        return mLastInsets;
    }

    /**
     * Reset all Behavior-related tracking records either to clean up or in preparation
     * for a new event stream. This should be called when attached or detached from a window,
     * in response to an UP or CANCEL event, when intercept is request-disallowed
     * and similar cases where an event stream in progress will be aborted.
     */
    private void resetTouchBehaviors() {
        if (mBehaviorTouchView != null) {
            final android.support.design.widget.CoordinatorLayout.Behavior b = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (mBehaviorTouchView.getLayoutParams())).getBehavior();
            if (b != null) {
                final long now = android.os.SystemClock.uptimeMillis();
                final android.view.MotionEvent cancelEvent = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_CANCEL, 0.0F, 0.0F, 0);
                b.onTouchEvent(this, mBehaviorTouchView, cancelEvent);
                cancelEvent.recycle();
            }
            mBehaviorTouchView = null;
        }
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            lp.resetTouchBehaviorTracking();
        }
        mDisallowInterceptReset = false;
    }

    /**
     * Populate a list with the current child views, sorted such that the topmost views
     * in z-order are at the front of the list. Useful for hit testing and event dispatch.
     */
    private void getTopSortedChildren(java.util.List<android.view.View> out) {
        out.clear();
        final boolean useCustomOrder = isChildrenDrawingOrderEnabled();
        final int childCount = getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final int childIndex = (useCustomOrder) ? getChildDrawingOrder(childCount, i) : i;
            final android.view.View child = getChildAt(childIndex);
            out.add(child);
        }
        if (android.support.design.widget.CoordinatorLayout.TOP_SORTED_CHILDREN_COMPARATOR != null) {
            java.util.Collections.sort(out, android.support.design.widget.CoordinatorLayout.TOP_SORTED_CHILDREN_COMPARATOR);
        }
    }

    private boolean performIntercept(android.view.MotionEvent ev, final int type) {
        boolean intercepted = false;
        boolean newBlock = false;
        android.view.MotionEvent cancelEvent = null;
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        final java.util.List<android.view.View> topmostChildList = mTempList1;
        getTopSortedChildren(topmostChildList);
        // Let topmost child views inspect first
        final int childCount = topmostChildList.size();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = topmostChildList.get(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            final android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
            if ((intercepted || newBlock) && (action != android.view.MotionEvent.ACTION_DOWN)) {
                // Cancel all behaviors beneath the one that intercepted.
                // If the event is "down" then we don't have anything to cancel yet.
                if (b != null) {
                    if (cancelEvent == null) {
                        final long now = android.os.SystemClock.uptimeMillis();
                        cancelEvent = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_CANCEL, 0.0F, 0.0F, 0);
                    }
                    switch (type) {
                        case android.support.design.widget.CoordinatorLayout.TYPE_ON_INTERCEPT :
                            b.onInterceptTouchEvent(this, child, cancelEvent);
                            break;
                        case android.support.design.widget.CoordinatorLayout.TYPE_ON_TOUCH :
                            b.onTouchEvent(this, child, cancelEvent);
                            break;
                    }
                }
                continue;
            }
            if ((!intercepted) && (b != null)) {
                switch (type) {
                    case android.support.design.widget.CoordinatorLayout.TYPE_ON_INTERCEPT :
                        intercepted = b.onInterceptTouchEvent(this, child, ev);
                        break;
                    case android.support.design.widget.CoordinatorLayout.TYPE_ON_TOUCH :
                        intercepted = b.onTouchEvent(this, child, ev);
                        break;
                }
                if (intercepted) {
                    mBehaviorTouchView = child;
                }
            }
            // Don't keep going if we're not allowing interaction below this.
            // Setting newBlock will make sure we cancel the rest of the behaviors.
            final boolean wasBlocking = lp.didBlockInteraction();
            final boolean isBlocking = lp.isBlockingInteractionBelow(this, child);
            newBlock = isBlocking && (!wasBlocking);
            if (isBlocking && (!newBlock)) {
                // Stop here since we don't have anything more to cancel - we already did
                // when the behavior first started blocking things below this point.
                break;
            }
        }
        topmostChildList.clear();
        return intercepted;
    }

    @java.lang.Override
    public boolean onInterceptTouchEvent(android.view.MotionEvent ev) {
        android.view.MotionEvent cancelEvent = null;
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        // Make sure we reset in case we had missed a previous important event.
        if (action == android.view.MotionEvent.ACTION_DOWN) {
            resetTouchBehaviors();
        }
        final boolean intercepted = performIntercept(ev, android.support.design.widget.CoordinatorLayout.TYPE_ON_INTERCEPT);
        if (cancelEvent != null) {
            cancelEvent.recycle();
        }
        if ((action == android.view.MotionEvent.ACTION_UP) || (action == android.view.MotionEvent.ACTION_CANCEL)) {
            resetTouchBehaviors();
        }
        return intercepted;
    }

    @java.lang.Override
    public boolean onTouchEvent(android.view.MotionEvent ev) {
        boolean handled = false;
        boolean cancelSuper = false;
        android.view.MotionEvent cancelEvent = null;
        final int action = android.support.v4.view.MotionEventCompat.getActionMasked(ev);
        if ((mBehaviorTouchView != null) || (cancelSuper = performIntercept(ev, android.support.design.widget.CoordinatorLayout.TYPE_ON_TOUCH))) {
            // Safe since performIntercept guarantees that
            // mBehaviorTouchView != null if it returns true
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (mBehaviorTouchView.getLayoutParams()));
            final android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
            if (b != null) {
                handled = b.onTouchEvent(this, mBehaviorTouchView, ev);
            }
        }
        // Keep the super implementation correct
        if (mBehaviorTouchView == null) {
            handled |= super.onTouchEvent(ev);
        } else
            if (cancelSuper) {
                if (cancelEvent == null) {
                    final long now = android.os.SystemClock.uptimeMillis();
                    cancelEvent = android.view.MotionEvent.obtain(now, now, android.view.MotionEvent.ACTION_CANCEL, 0.0F, 0.0F, 0);
                }
                super.onTouchEvent(cancelEvent);
            }

        if ((!handled) && (action == android.view.MotionEvent.ACTION_DOWN)) {
        }
        if (cancelEvent != null) {
            cancelEvent.recycle();
        }
        if ((action == android.view.MotionEvent.ACTION_UP) || (action == android.view.MotionEvent.ACTION_CANCEL)) {
            resetTouchBehaviors();
        }
        return handled;
    }

    @java.lang.Override
    public void requestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        super.requestDisallowInterceptTouchEvent(disallowIntercept);
        if (disallowIntercept && (!mDisallowInterceptReset)) {
            resetTouchBehaviors();
            mDisallowInterceptReset = true;
        }
    }

    private int getKeyline(int index) {
        if (mKeylines == null) {
            android.util.Log.e(android.support.design.widget.CoordinatorLayout.TAG, (("No keylines defined for " + this) + " - attempted index lookup ") + index);
            return 0;
        }
        if ((index < 0) || (index >= mKeylines.length)) {
            android.util.Log.e(android.support.design.widget.CoordinatorLayout.TAG, (("Keyline index " + index) + " out of range for ") + this);
            return 0;
        }
        return mKeylines[index];
    }

    static android.support.design.widget.CoordinatorLayout.Behavior parseBehavior(android.content.Context context, android.util.AttributeSet attrs, java.lang.String name) {
        if (android.text.TextUtils.isEmpty(name)) {
            return null;
        }
        final java.lang.String fullName;
        if (name.startsWith(".")) {
            // Relative to the app package. Prepend the app package name.
            fullName = context.getPackageName() + name;
        } else
            if (name.indexOf('.') >= 0) {
                // Fully qualified package name.
                fullName = name;
            } else {
                // Assume stock behavior in this package (if we have one)
                fullName = (!android.text.TextUtils.isEmpty(android.support.design.widget.CoordinatorLayout.WIDGET_PACKAGE_NAME)) ? (android.support.design.widget.CoordinatorLayout.WIDGET_PACKAGE_NAME + '.') + name : name;
            }

        try {
            java.util.Map<java.lang.String, java.lang.reflect.Constructor<android.support.design.widget.CoordinatorLayout.Behavior>> constructors = android.support.design.widget.CoordinatorLayout.sConstructors.get();
            if (constructors == null) {
                constructors = new java.util.HashMap<>();
                android.support.design.widget.CoordinatorLayout.sConstructors.set(constructors);
            }
            java.lang.reflect.Constructor<android.support.design.widget.CoordinatorLayout.Behavior> c = constructors.get(fullName);
            if (c == null) {
                final java.lang.Class<android.support.design.widget.CoordinatorLayout.Behavior> clazz = ((java.lang.Class<android.support.design.widget.CoordinatorLayout.Behavior>) (java.lang.Class.forName(fullName, true, context.getClassLoader())));
                c = clazz.getConstructor(android.support.design.widget.CoordinatorLayout.CONSTRUCTOR_PARAMS);
                c.setAccessible(true);
                constructors.put(fullName, c);
            }
            return c.newInstance(context, attrs);
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException("Could not inflate Behavior subclass " + fullName, e);
        }
    }

    android.support.design.widget.CoordinatorLayout.LayoutParams getResolvedLayoutParams(android.view.View child) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams result = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        if (!result.mBehaviorResolved) {
            java.lang.Class<?> childClass = child.getClass();
            android.support.design.widget.CoordinatorLayout.DefaultBehavior defaultBehavior = null;
            while ((childClass != null) && ((defaultBehavior = childClass.getAnnotation(android.support.design.widget.CoordinatorLayout.DefaultBehavior.class)) == null)) {
                childClass = childClass.getSuperclass();
            } 
            if (defaultBehavior != null) {
                try {
                    result.setBehavior(defaultBehavior.value().newInstance());
                } catch (java.lang.Exception e) {
                    android.util.Log.e(android.support.design.widget.CoordinatorLayout.TAG, ("Default behavior class " + defaultBehavior.value().getName()) + " could not be instantiated. Did you forget a default constructor?", e);
                }
            }
            result.mBehaviorResolved = true;
        }
        return result;
    }

    private void prepareChildren() {
        mDependencySortedChildren.clear();
        mChildDag.clear();
        for (int i = 0, count = getChildCount(); i < count; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = getResolvedLayoutParams(view);
            lp.findAnchorView(this, view);
            mChildDag.addNode(view);
            // Now iterate again over the other children, adding any dependencies to the graph
            for (int j = 0; j < count; j++) {
                if (j == i) {
                    continue;
                }
                final android.view.View other = getChildAt(j);
                final android.support.design.widget.CoordinatorLayout.LayoutParams otherLp = getResolvedLayoutParams(other);
                if (otherLp.dependsOn(this, other, view)) {
                    if (!mChildDag.contains(other)) {
                        // Make sure that the other node is added
                        mChildDag.addNode(other);
                    }
                    // Now add the dependency to the graph
                    mChildDag.addEdge(view, other);
                }
            }
        }
        // Finally add the sorted graph list to our list
        mDependencySortedChildren.addAll(mChildDag.getSortedList());
        // We also need to reverse the result since we want the start of the list to contain
        // Views which have no dependencies, then dependent views after that
        java.util.Collections.reverse(mDependencySortedChildren);
    }

    /**
     * Retrieve the transformed bounding rect of an arbitrary descendant view.
     * This does not need to be a direct child.
     *
     * @param descendant
     * 		descendant view to reference
     * @param out
     * 		rect to set to the bounds of the descendant view
     */
    void getDescendantRect(android.view.View descendant, android.graphics.Rect out) {
        android.support.design.widget.ViewGroupUtils.getDescendantRect(this, descendant, out);
    }

    @java.lang.Override
    protected int getSuggestedMinimumWidth() {
        return java.lang.Math.max(super.getSuggestedMinimumWidth(), getPaddingLeft() + getPaddingRight());
    }

    @java.lang.Override
    protected int getSuggestedMinimumHeight() {
        return java.lang.Math.max(super.getSuggestedMinimumHeight(), getPaddingTop() + getPaddingBottom());
    }

    /**
     * Called to measure each individual child view unless a
     * {@link CoordinatorLayout.Behavior Behavior} is present. The Behavior may choose to delegate
     * child measurement to this method.
     *
     * @param child
     * 		the child to measure
     * @param parentWidthMeasureSpec
     * 		the width requirements for this view
     * @param widthUsed
     * 		extra space that has been used up by the parent
     * 		horizontally (possibly by other children of the parent)
     * @param parentHeightMeasureSpec
     * 		the height requirements for this view
     * @param heightUsed
     * 		extra space that has been used up by the parent
     * 		vertically (possibly by other children of the parent)
     */
    public void onMeasureChild(android.view.View child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        measureChildWithMargins(child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }

    @java.lang.Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        prepareChildren();
        ensurePreDrawListener();
        final int paddingLeft = getPaddingLeft();
        final int paddingTop = getPaddingTop();
        final int paddingRight = getPaddingRight();
        final int paddingBottom = getPaddingBottom();
        final int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        final boolean isRtl = layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL;
        final int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = android.view.View.MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = android.view.View.MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = android.view.View.MeasureSpec.getSize(heightMeasureSpec);
        final int widthPadding = paddingLeft + paddingRight;
        final int heightPadding = paddingTop + paddingBottom;
        int widthUsed = getSuggestedMinimumWidth();
        int heightUsed = getSuggestedMinimumHeight();
        int childState = 0;
        final boolean applyInsets = (mLastInsets != null) && android.support.v4.view.ViewCompat.getFitsSystemWindows(this);
        final int childCount = mDependencySortedChildren.size();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = mDependencySortedChildren.get(i);
            if (child.getVisibility() == android.view.View.GONE) {
                // If the child is GONE, skip...
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            int keylineWidthUsed = 0;
            if ((lp.keyline >= 0) && (widthMode != android.view.View.MeasureSpec.UNSPECIFIED)) {
                final int keylinePos = getKeyline(lp.keyline);
                final int keylineGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(android.support.design.widget.CoordinatorLayout.resolveKeylineGravity(lp.gravity), layoutDirection) & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
                if (((keylineGravity == android.view.Gravity.LEFT) && (!isRtl)) || ((keylineGravity == android.view.Gravity.RIGHT) && isRtl)) {
                    keylineWidthUsed = java.lang.Math.max(0, (widthSize - paddingRight) - keylinePos);
                } else
                    if (((keylineGravity == android.view.Gravity.RIGHT) && (!isRtl)) || ((keylineGravity == android.view.Gravity.LEFT) && isRtl)) {
                        keylineWidthUsed = java.lang.Math.max(0, keylinePos - paddingLeft);
                    }

            }
            int childWidthMeasureSpec = widthMeasureSpec;
            int childHeightMeasureSpec = heightMeasureSpec;
            if (applyInsets && (!android.support.v4.view.ViewCompat.getFitsSystemWindows(child))) {
                // We're set to handle insets but this child isn't, so we will measure the
                // child as if there are no insets
                final int horizInsets = mLastInsets.getSystemWindowInsetLeft() + mLastInsets.getSystemWindowInsetRight();
                final int vertInsets = mLastInsets.getSystemWindowInsetTop() + mLastInsets.getSystemWindowInsetBottom();
                childWidthMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(widthSize - horizInsets, widthMode);
                childHeightMeasureSpec = android.view.View.MeasureSpec.makeMeasureSpec(heightSize - vertInsets, heightMode);
            }
            final android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
            if ((b == null) || (!b.onMeasureChild(this, child, childWidthMeasureSpec, keylineWidthUsed, childHeightMeasureSpec, 0))) {
                onMeasureChild(child, childWidthMeasureSpec, keylineWidthUsed, childHeightMeasureSpec, 0);
            }
            widthUsed = java.lang.Math.max(widthUsed, ((widthPadding + child.getMeasuredWidth()) + lp.leftMargin) + lp.rightMargin);
            heightUsed = java.lang.Math.max(heightUsed, ((heightPadding + child.getMeasuredHeight()) + lp.topMargin) + lp.bottomMargin);
            childState = android.support.v4.view.ViewCompat.combineMeasuredStates(childState, android.support.v4.view.ViewCompat.getMeasuredState(child));
        }
        final int width = android.support.v4.view.ViewCompat.resolveSizeAndState(widthUsed, widthMeasureSpec, childState & android.support.v4.view.ViewCompat.MEASURED_STATE_MASK);
        final int height = android.support.v4.view.ViewCompat.resolveSizeAndState(heightUsed, heightMeasureSpec, childState << android.support.v4.view.ViewCompat.MEASURED_HEIGHT_STATE_SHIFT);
        setMeasuredDimension(width, height);
    }

    private android.support.v4.view.WindowInsetsCompat dispatchApplyWindowInsetsToBehaviors(android.support.v4.view.WindowInsetsCompat insets) {
        if (insets.isConsumed()) {
            return insets;
        }
        for (int i = 0, z = getChildCount(); i < z; i++) {
            final android.view.View child = getChildAt(i);
            if (android.support.v4.view.ViewCompat.getFitsSystemWindows(child)) {
                final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
                final android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
                if (b != null) {
                    // If the view has a behavior, let it try first
                    insets = b.onApplyWindowInsets(this, child, insets);
                    if (insets.isConsumed()) {
                        // If it consumed the insets, break
                        break;
                    }
                }
            }
        }
        return insets;
    }

    /**
     * Called to lay out each individual child view unless a
     * {@link CoordinatorLayout.Behavior Behavior} is present. The Behavior may choose to
     * delegate child measurement to this method.
     *
     * @param child
     * 		child view to lay out
     * @param layoutDirection
     * 		the resolved layout direction for the CoordinatorLayout, such as
     * 		{@link ViewCompat#LAYOUT_DIRECTION_LTR} or
     * 		{@link ViewCompat#LAYOUT_DIRECTION_RTL}.
     */
    public void onLayoutChild(android.view.View child, int layoutDirection) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        if (lp.checkAnchorChanged()) {
            throw new java.lang.IllegalStateException("An anchor may not be changed after CoordinatorLayout" + " measurement begins before layout is complete.");
        }
        if (lp.mAnchorView != null) {
            layoutChildWithAnchor(child, lp.mAnchorView, layoutDirection);
        } else
            if (lp.keyline >= 0) {
                layoutChildWithKeyline(child, lp.keyline, layoutDirection);
            } else {
                layoutChild(child, layoutDirection);
            }

    }

    @java.lang.Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        final int childCount = mDependencySortedChildren.size();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = mDependencySortedChildren.get(i);
            if (child.getVisibility() == android.view.View.GONE) {
                // If the child is GONE, skip...
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            final android.support.design.widget.CoordinatorLayout.Behavior behavior = lp.getBehavior();
            if ((behavior == null) || (!behavior.onLayoutChild(this, child, layoutDirection))) {
                onLayoutChild(child, layoutDirection);
            }
        }
    }

    @java.lang.Override
    public void onDraw(android.graphics.Canvas c) {
        super.onDraw(c);
        if (mDrawStatusBarBackground && (mStatusBarBackground != null)) {
            final int inset = (mLastInsets != null) ? mLastInsets.getSystemWindowInsetTop() : 0;
            if (inset > 0) {
                mStatusBarBackground.setBounds(0, 0, getWidth(), inset);
                mStatusBarBackground.draw(c);
            }
        }
    }

    @java.lang.Override
    public void setFitsSystemWindows(boolean fitSystemWindows) {
        super.setFitsSystemWindows(fitSystemWindows);
        setupForInsets();
    }

    /**
     * Mark the last known child position rect for the given child view.
     * This will be used when checking if a child view's position has changed between frames.
     * The rect used here should be one returned by
     * {@link #getChildRect(android.view.View, boolean, android.graphics.Rect)}, with translation
     * disabled.
     *
     * @param child
     * 		child view to set for
     * @param r
     * 		rect to set
     */
    void recordLastChildRect(android.view.View child, android.graphics.Rect r) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        lp.setLastChildRect(r);
    }

    /**
     * Get the last known child rect recorded by
     * {@link #recordLastChildRect(android.view.View, android.graphics.Rect)}.
     *
     * @param child
     * 		child view to retrieve from
     * @param out
     * 		rect to set to the outpur values
     */
    void getLastChildRect(android.view.View child, android.graphics.Rect out) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        out.set(lp.getLastChildRect());
    }

    /**
     * Get the position rect for the given child. If the child has currently requested layout
     * or has a visibility of GONE.
     *
     * @param child
     * 		child view to check
     * @param transform
     * 		true to include transformation in the output rect, false to
     * 		only account for the base position
     * @param out
     * 		rect to set to the output values
     */
    void getChildRect(android.view.View child, boolean transform, android.graphics.Rect out) {
        if (child.isLayoutRequested() || (child.getVisibility() == android.view.View.GONE)) {
            out.setEmpty();
            return;
        }
        if (transform) {
            getDescendantRect(child, out);
        } else {
            out.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        }
    }

    private void getDesiredAnchoredChildRectWithoutConstraints(android.view.View child, int layoutDirection, android.graphics.Rect anchorRect, android.graphics.Rect out, android.support.design.widget.CoordinatorLayout.LayoutParams lp, int childWidth, int childHeight) {
        final int absGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(android.support.design.widget.CoordinatorLayout.resolveAnchoredChildGravity(lp.gravity), layoutDirection);
        final int absAnchorGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(android.support.design.widget.CoordinatorLayout.resolveGravity(lp.anchorGravity), layoutDirection);
        final int hgrav = absGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
        final int vgrav = absGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        final int anchorHgrav = absAnchorGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
        final int anchorVgrav = absAnchorGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        int left;
        int top;
        // Align to the anchor. This puts us in an assumed right/bottom child view gravity.
        // If this is not the case we will subtract out the appropriate portion of
        // the child size below.
        switch (anchorHgrav) {
            default :
            case android.view.Gravity.LEFT :
                left = anchorRect.left;
                break;
            case android.view.Gravity.RIGHT :
                left = anchorRect.right;
                break;
            case android.view.Gravity.CENTER_HORIZONTAL :
                left = anchorRect.left + (anchorRect.width() / 2);
                break;
        }
        switch (anchorVgrav) {
            default :
            case android.view.Gravity.TOP :
                top = anchorRect.top;
                break;
            case android.view.Gravity.BOTTOM :
                top = anchorRect.bottom;
                break;
            case android.view.Gravity.CENTER_VERTICAL :
                top = anchorRect.top + (anchorRect.height() / 2);
                break;
        }
        // Offset by the child view's gravity itself. The above assumed right/bottom gravity.
        switch (hgrav) {
            default :
            case android.view.Gravity.LEFT :
                left -= childWidth;
                break;
            case android.view.Gravity.RIGHT :
                // Do nothing, we're already in position.
                break;
            case android.view.Gravity.CENTER_HORIZONTAL :
                left -= childWidth / 2;
                break;
        }
        switch (vgrav) {
            default :
            case android.view.Gravity.TOP :
                top -= childHeight;
                break;
            case android.view.Gravity.BOTTOM :
                // Do nothing, we're already in position.
                break;
            case android.view.Gravity.CENTER_VERTICAL :
                top -= childHeight / 2;
                break;
        }
        out.set(left, top, left + childWidth, top + childHeight);
    }

    private void constrainChildRect(android.support.design.widget.CoordinatorLayout.LayoutParams lp, android.graphics.Rect out, int childWidth, int childHeight) {
        final int width = getWidth();
        final int height = getHeight();
        // Obey margins and padding
        int left = java.lang.Math.max(getPaddingLeft() + lp.leftMargin, java.lang.Math.min(out.left, ((width - getPaddingRight()) - childWidth) - lp.rightMargin));
        int top = java.lang.Math.max(getPaddingTop() + lp.topMargin, java.lang.Math.min(out.top, ((height - getPaddingBottom()) - childHeight) - lp.bottomMargin));
        out.set(left, top, left + childWidth, top + childHeight);
    }

    /**
     * Calculate the desired child rect relative to an anchor rect, respecting both
     * gravity and anchorGravity.
     *
     * @param child
     * 		child view to calculate a rect for
     * @param layoutDirection
     * 		the desired layout direction for the CoordinatorLayout
     * @param anchorRect
     * 		rect in CoordinatorLayout coordinates of the anchor view area
     * @param out
     * 		rect to set to the output values
     */
    void getDesiredAnchoredChildRect(android.view.View child, int layoutDirection, android.graphics.Rect anchorRect, android.graphics.Rect out) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        final int childWidth = child.getMeasuredWidth();
        final int childHeight = child.getMeasuredHeight();
        getDesiredAnchoredChildRectWithoutConstraints(child, layoutDirection, anchorRect, out, lp, childWidth, childHeight);
        constrainChildRect(lp, out, childWidth, childHeight);
    }

    /**
     * CORE ASSUMPTION: anchor has been laid out by the time this is called for a given child view.
     *
     * @param child
     * 		child to lay out
     * @param anchor
     * 		view to anchor child relative to; already laid out.
     * @param layoutDirection
     * 		ViewCompat constant for layout direction
     */
    private void layoutChildWithAnchor(android.view.View child, android.view.View anchor, int layoutDirection) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        final android.graphics.Rect anchorRect = mTempRect1;
        final android.graphics.Rect childRect = mTempRect2;
        getDescendantRect(anchor, anchorRect);
        getDesiredAnchoredChildRect(child, layoutDirection, anchorRect, childRect);
        child.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
    }

    /**
     * Lay out a child view with respect to a keyline.
     *
     * <p>The keyline represents a horizontal offset from the unpadded starting edge of
     * the CoordinatorLayout. The child's gravity will affect how it is positioned with
     * respect to the keyline.</p>
     *
     * @param child
     * 		child to lay out
     * @param keyline
     * 		offset from the starting edge in pixels of the keyline to align with
     * @param layoutDirection
     * 		ViewCompat constant for layout direction
     */
    private void layoutChildWithKeyline(android.view.View child, int keyline, int layoutDirection) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        final int absGravity = android.support.v4.view.GravityCompat.getAbsoluteGravity(android.support.design.widget.CoordinatorLayout.resolveKeylineGravity(lp.gravity), layoutDirection);
        final int hgrav = absGravity & android.view.Gravity.HORIZONTAL_GRAVITY_MASK;
        final int vgrav = absGravity & android.view.Gravity.VERTICAL_GRAVITY_MASK;
        final int width = getWidth();
        final int height = getHeight();
        final int childWidth = child.getMeasuredWidth();
        final int childHeight = child.getMeasuredHeight();
        if (layoutDirection == android.support.v4.view.ViewCompat.LAYOUT_DIRECTION_RTL) {
            keyline = width - keyline;
        }
        int left = getKeyline(keyline) - childWidth;
        int top = 0;
        switch (hgrav) {
            default :
            case android.view.Gravity.LEFT :
                // Nothing to do.
                break;
            case android.view.Gravity.RIGHT :
                left += childWidth;
                break;
            case android.view.Gravity.CENTER_HORIZONTAL :
                left += childWidth / 2;
                break;
        }
        switch (vgrav) {
            default :
            case android.view.Gravity.TOP :
                // Do nothing, we're already in position.
                break;
            case android.view.Gravity.BOTTOM :
                top += childHeight;
                break;
            case android.view.Gravity.CENTER_VERTICAL :
                top += childHeight / 2;
                break;
        }
        // Obey margins and padding
        left = java.lang.Math.max(getPaddingLeft() + lp.leftMargin, java.lang.Math.min(left, ((width - getPaddingRight()) - childWidth) - lp.rightMargin));
        top = java.lang.Math.max(getPaddingTop() + lp.topMargin, java.lang.Math.min(top, ((height - getPaddingBottom()) - childHeight) - lp.bottomMargin));
        child.layout(left, top, left + childWidth, top + childHeight);
    }

    /**
     * Lay out a child view with no special handling. This will position the child as
     * if it were within a FrameLayout or similar simple frame.
     *
     * @param child
     * 		child view to lay out
     * @param layoutDirection
     * 		ViewCompat constant for the desired layout direction
     */
    private void layoutChild(android.view.View child, int layoutDirection) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        final android.graphics.Rect parent = mTempRect1;
        parent.set(getPaddingLeft() + lp.leftMargin, getPaddingTop() + lp.topMargin, (getWidth() - getPaddingRight()) - lp.rightMargin, (getHeight() - getPaddingBottom()) - lp.bottomMargin);
        if (((mLastInsets != null) && android.support.v4.view.ViewCompat.getFitsSystemWindows(this)) && (!android.support.v4.view.ViewCompat.getFitsSystemWindows(child))) {
            // If we're set to handle insets but this child isn't, then it has been measured as
            // if there are no insets. We need to lay it out to match.
            parent.left += mLastInsets.getSystemWindowInsetLeft();
            parent.top += mLastInsets.getSystemWindowInsetTop();
            parent.right -= mLastInsets.getSystemWindowInsetRight();
            parent.bottom -= mLastInsets.getSystemWindowInsetBottom();
        }
        final android.graphics.Rect out = mTempRect2;
        android.support.v4.view.GravityCompat.apply(android.support.design.widget.CoordinatorLayout.resolveGravity(lp.gravity), child.getMeasuredWidth(), child.getMeasuredHeight(), parent, out, layoutDirection);
        child.layout(out.left, out.top, out.right, out.bottom);
    }

    /**
     * Return the given gravity value or the default if the passed value is NO_GRAVITY.
     * This should be used for children that are not anchored to another view or a keyline.
     */
    private static int resolveGravity(int gravity) {
        return gravity == android.view.Gravity.NO_GRAVITY ? android.support.v4.view.GravityCompat.START | android.view.Gravity.TOP : gravity;
    }

    /**
     * Return the given gravity value or the default if the passed value is NO_GRAVITY.
     * This should be used for children that are positioned relative to a keyline.
     */
    private static int resolveKeylineGravity(int gravity) {
        return gravity == android.view.Gravity.NO_GRAVITY ? android.support.v4.view.GravityCompat.END | android.view.Gravity.TOP : gravity;
    }

    /**
     * Return the given gravity value or the default if the passed value is NO_GRAVITY.
     * This should be used for children that are anchored to another view.
     */
    private static int resolveAnchoredChildGravity(int gravity) {
        return gravity == android.view.Gravity.NO_GRAVITY ? android.view.Gravity.CENTER : gravity;
    }

    @java.lang.Override
    protected boolean drawChild(android.graphics.Canvas canvas, android.view.View child, long drawingTime) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        if (lp.mBehavior != null) {
            final float scrimAlpha = lp.mBehavior.getScrimOpacity(this, child);
            if (scrimAlpha > 0.0F) {
                if (mScrimPaint == null) {
                    mScrimPaint = new android.graphics.Paint();
                }
                mScrimPaint.setColor(lp.mBehavior.getScrimColor(this, child));
                mScrimPaint.setAlpha(android.support.design.widget.MathUtils.constrain(java.lang.Math.round(255 * scrimAlpha), 0, 255));
                final int saved = canvas.save();
                if (child.isOpaque()) {
                    // If the child is opaque, there is no need to draw behind it so we'll inverse
                    // clip the canvas
                    canvas.clipRect(child.getLeft(), child.getTop(), child.getRight(), child.getBottom(), android.graphics.Region.Op.DIFFERENCE);
                }
                // Now draw the rectangle for the scrim
                canvas.drawRect(getPaddingLeft(), getPaddingTop(), getWidth() - getPaddingRight(), getHeight() - getPaddingBottom(), mScrimPaint);
                canvas.restoreToCount(saved);
            }
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    /**
     * Dispatch any dependent view changes to the relevant {@link Behavior} instances.
     *
     * Usually run as part of the pre-draw step when at least one child view has a reported
     * dependency on another view. This allows CoordinatorLayout to account for layout
     * changes and animations that occur outside of the normal layout pass.
     *
     * It can also be ran as part of the nested scrolling dispatch to ensure that any offsetting
     * is completed within the correct coordinate window.
     *
     * The offsetting behavior implemented here does not store the computed offset in
     * the LayoutParams; instead it expects that the layout process will always reconstruct
     * the proper positioning.
     *
     * @param type
     * 		the type of event which has caused this call
     */
    final void onChildViewsChanged(@android.support.design.widget.CoordinatorLayout.DispatchChangeEvent
    final int type) {
        final int layoutDirection = android.support.v4.view.ViewCompat.getLayoutDirection(this);
        final int childCount = mDependencySortedChildren.size();
        final android.graphics.Rect inset = mTempRect4;
        inset.setEmpty();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = mDependencySortedChildren.get(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            // Check child views before for anchor
            for (int j = 0; j < i; j++) {
                final android.view.View checkChild = mDependencySortedChildren.get(j);
                if (lp.mAnchorDirectChild == checkChild) {
                    offsetChildToAnchor(child, layoutDirection);
                }
            }
            // Get the current draw rect of the view
            final android.graphics.Rect drawRect = mTempRect1;
            getChildRect(child, true, drawRect);
            // Accumulate inset sizes
            if ((lp.insetEdge != android.view.Gravity.NO_GRAVITY) && (!drawRect.isEmpty())) {
                final int absInsetEdge = android.support.v4.view.GravityCompat.getAbsoluteGravity(lp.insetEdge, layoutDirection);
                switch (absInsetEdge & android.view.Gravity.VERTICAL_GRAVITY_MASK) {
                    case android.view.Gravity.TOP :
                        inset.top = java.lang.Math.max(inset.top, drawRect.bottom);
                        break;
                    case android.view.Gravity.BOTTOM :
                        inset.bottom = java.lang.Math.max(inset.bottom, getHeight() - drawRect.top);
                        break;
                }
                switch (absInsetEdge & android.view.Gravity.HORIZONTAL_GRAVITY_MASK) {
                    case android.view.Gravity.LEFT :
                        inset.left = java.lang.Math.max(inset.left, drawRect.right);
                        break;
                    case android.view.Gravity.RIGHT :
                        inset.right = java.lang.Math.max(inset.right, getWidth() - drawRect.left);
                        break;
                }
            }
            // Dodge inset edges if necessary
            if ((lp.dodgeInsetEdges != android.view.Gravity.NO_GRAVITY) && (child.getVisibility() == android.view.View.VISIBLE)) {
                offsetChildByInset(child, inset, layoutDirection);
            }
            if (type == android.support.design.widget.CoordinatorLayout.EVENT_PRE_DRAW) {
                // Did it change? if not continue
                final android.graphics.Rect lastDrawRect = mTempRect2;
                getLastChildRect(child, lastDrawRect);
                if (lastDrawRect.equals(drawRect)) {
                    continue;
                }
                recordLastChildRect(child, drawRect);
            }
            // Update any behavior-dependent views for the change
            for (int j = i + 1; j < childCount; j++) {
                final android.view.View checkChild = mDependencySortedChildren.get(j);
                final android.support.design.widget.CoordinatorLayout.LayoutParams checkLp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (checkChild.getLayoutParams()));
                final android.support.design.widget.CoordinatorLayout.Behavior b = checkLp.getBehavior();
                if ((b != null) && b.layoutDependsOn(this, checkChild, child)) {
                    if ((type == android.support.design.widget.CoordinatorLayout.EVENT_PRE_DRAW) && checkLp.getChangedAfterNestedScroll()) {
                        // If this is from a pre-draw and we have already been changed
                        // from a nested scroll, skip the dispatch and reset the flag
                        checkLp.resetChangedAfterNestedScroll();
                        continue;
                    }
                    final boolean handled;
                    switch (type) {
                        case android.support.design.widget.CoordinatorLayout.EVENT_VIEW_REMOVED :
                            // EVENT_VIEW_REMOVED means that we need to dispatch
                            // onDependentViewRemoved() instead
                            b.onDependentViewRemoved(this, checkChild, child);
                            handled = true;
                            break;
                        default :
                            // Otherwise we dispatch onDependentViewChanged()
                            handled = b.onDependentViewChanged(this, checkChild, child);
                            break;
                    }
                    if (type == android.support.design.widget.CoordinatorLayout.EVENT_NESTED_SCROLL) {
                        // If this is from a nested scroll, set the flag so that we may skip
                        // any resulting onPreDraw dispatch (if needed)
                        checkLp.setChangedAfterNestedScroll(handled);
                    }
                }
            }
        }
    }

    private void offsetChildByInset(final android.view.View child, final android.graphics.Rect inset, final int layoutDirection) {
        if (!android.support.v4.view.ViewCompat.isLaidOut(child)) {
            // The view has not been laid out yet, so we can't obtain its bounds.
            return;
        }
        final android.graphics.Rect bounds = mTempRect5;
        bounds.set(child.getLeft(), child.getTop(), child.getRight(), child.getBottom());
        if (bounds.isEmpty()) {
            // Bounds are empty so there is nothing to dodge against, skip...
            return;
        }
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        final android.support.design.widget.CoordinatorLayout.Behavior behavior = lp.getBehavior();
        final android.graphics.Rect dodgeRect = mTempRect3;
        dodgeRect.setEmpty();
        if ((behavior != null) && behavior.getInsetDodgeRect(this, child, dodgeRect)) {
            // Make sure that the rect is within the view's bounds
            if (!bounds.contains(dodgeRect)) {
                throw new java.lang.IllegalArgumentException(((("Rect should be within the child's bounds." + " Rect:") + dodgeRect.toShortString()) + " | Bounds:") + bounds.toShortString());
            }
        } else {
            dodgeRect.set(bounds);
        }
        if (dodgeRect.isEmpty()) {
            // Rect is empty so there is nothing to dodge against, skip...
            return;
        }
        final int absDodgeInsetEdges = android.support.v4.view.GravityCompat.getAbsoluteGravity(lp.dodgeInsetEdges, layoutDirection);
        boolean offsetY = false;
        if ((absDodgeInsetEdges & android.view.Gravity.TOP) == android.view.Gravity.TOP) {
            int distance = (dodgeRect.top - lp.topMargin) - lp.mInsetOffsetY;
            if (distance < inset.top) {
                setInsetOffsetY(child, inset.top - distance);
                offsetY = true;
            }
        }
        if ((absDodgeInsetEdges & android.view.Gravity.BOTTOM) == android.view.Gravity.BOTTOM) {
            int distance = ((getHeight() - dodgeRect.bottom) - lp.bottomMargin) + lp.mInsetOffsetY;
            if (distance < inset.bottom) {
                setInsetOffsetY(child, distance - inset.bottom);
                offsetY = true;
            }
        }
        if (!offsetY) {
            setInsetOffsetY(child, 0);
        }
        boolean offsetX = false;
        if ((absDodgeInsetEdges & android.view.Gravity.LEFT) == android.view.Gravity.LEFT) {
            int distance = (dodgeRect.left - lp.leftMargin) - lp.mInsetOffsetX;
            if (distance < inset.left) {
                setInsetOffsetX(child, inset.left - distance);
                offsetX = true;
            }
        }
        if ((absDodgeInsetEdges & android.view.Gravity.RIGHT) == android.view.Gravity.RIGHT) {
            int distance = ((getWidth() - dodgeRect.right) - lp.rightMargin) + lp.mInsetOffsetX;
            if (distance < inset.right) {
                setInsetOffsetX(child, distance - inset.right);
                offsetX = true;
            }
        }
        if (!offsetX) {
            setInsetOffsetX(child, 0);
        }
    }

    private void setInsetOffsetX(android.view.View child, int offsetX) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        if (lp.mInsetOffsetX != offsetX) {
            final int dx = offsetX - lp.mInsetOffsetX;
            android.support.v4.view.ViewCompat.offsetLeftAndRight(child, dx);
            lp.mInsetOffsetX = offsetX;
        }
    }

    private void setInsetOffsetY(android.view.View child, int offsetY) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        if (lp.mInsetOffsetY != offsetY) {
            final int dy = offsetY - lp.mInsetOffsetY;
            android.support.v4.view.ViewCompat.offsetTopAndBottom(child, dy);
            lp.mInsetOffsetY = offsetY;
        }
    }

    /**
     * Allows the caller to manually dispatch
     * {@link Behavior#onDependentViewChanged(CoordinatorLayout, View, View)} to the associated
     * {@link Behavior} instances of views which depend on the provided {@link View}.
     *
     * <p>You should not normally need to call this method as the it will be automatically done
     * when the view has changed.
     *
     * @param view
     * 		the View to find dependents of to dispatch the call.
     */
    public void dispatchDependentViewsChanged(android.view.View view) {
        final java.util.List<android.view.View> dependents = mChildDag.getIncomingEdges(view);
        if ((dependents != null) && (!dependents.isEmpty())) {
            for (int i = 0; i < dependents.size(); i++) {
                final android.view.View child = dependents.get(i);
                android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
                android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
                if (b != null) {
                    b.onDependentViewChanged(this, child, view);
                }
            }
        }
    }

    /**
     * Returns the list of views which the provided view depends on. Do not store this list as its
     * contents may not be valid beyond the caller.
     *
     * @param child
     * 		the view to find dependencies for.
     * @return the list of views which {@code child} depends on.
     */
    @android.support.annotation.NonNull
    public java.util.List<android.view.View> getDependencies(@android.support.annotation.NonNull
    android.view.View child) {
        final java.util.List<android.view.View> dependencies = mChildDag.getOutgoingEdges(child);
        mTempDependenciesList.clear();
        if (dependencies != null) {
            mTempDependenciesList.addAll(dependencies);
        }
        return mTempDependenciesList;
    }

    /**
     * Returns the list of views which depend on the provided view. Do not store this list as its
     * contents may not be valid beyond the caller.
     *
     * @param child
     * 		the view to find dependents of.
     * @return the list of views which depend on {@code child}.
     */
    @android.support.annotation.NonNull
    public java.util.List<android.view.View> getDependents(@android.support.annotation.NonNull
    android.view.View child) {
        final java.util.List<android.view.View> edges = mChildDag.getIncomingEdges(child);
        mTempDependenciesList.clear();
        if (edges != null) {
            mTempDependenciesList.addAll(edges);
        }
        return mTempDependenciesList;
    }

    @android.support.annotation.VisibleForTesting
    final java.util.List<android.view.View> getDependencySortedChildren() {
        prepareChildren();
        return java.util.Collections.unmodifiableList(mDependencySortedChildren);
    }

    /**
     * Add or remove the pre-draw listener as necessary.
     */
    void ensurePreDrawListener() {
        boolean hasDependencies = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View child = getChildAt(i);
            if (hasDependencies(child)) {
                hasDependencies = true;
                break;
            }
        }
        if (hasDependencies != mNeedsPreDrawListener) {
            if (hasDependencies) {
                addPreDrawListener();
            } else {
                removePreDrawListener();
            }
        }
    }

    /**
     * Check if the given child has any layout dependencies on other child views.
     */
    private boolean hasDependencies(android.view.View child) {
        return mChildDag.hasOutgoingEdges(child);
    }

    /**
     * Add the pre-draw listener if we're attached to a window and mark that we currently
     * need it when attached.
     */
    void addPreDrawListener() {
        if (mIsAttachedToWindow) {
            // Add the listener
            if (mOnPreDrawListener == null) {
                mOnPreDrawListener = new android.support.design.widget.CoordinatorLayout.OnPreDrawListener();
            }
            final android.view.ViewTreeObserver vto = getViewTreeObserver();
            vto.addOnPreDrawListener(mOnPreDrawListener);
        }
        // Record that we need the listener regardless of whether or not we're attached.
        // We'll add the real listener when we become attached.
        mNeedsPreDrawListener = true;
    }

    /**
     * Remove the pre-draw listener if we're attached to a window and mark that we currently
     * do not need it when attached.
     */
    void removePreDrawListener() {
        if (mIsAttachedToWindow) {
            if (mOnPreDrawListener != null) {
                final android.view.ViewTreeObserver vto = getViewTreeObserver();
                vto.removeOnPreDrawListener(mOnPreDrawListener);
            }
        }
        mNeedsPreDrawListener = false;
    }

    /**
     * Adjust the child left, top, right, bottom rect to the correct anchor view position,
     * respecting gravity and anchor gravity.
     *
     * Note that child translation properties are ignored in this process, allowing children
     * to be animated away from their anchor. However, if the anchor view is animated,
     * the child will be offset to match the anchor's translated position.
     */
    void offsetChildToAnchor(android.view.View child, int layoutDirection) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        if (lp.mAnchorView != null) {
            final android.graphics.Rect anchorRect = mTempRect1;
            final android.graphics.Rect childRect = mTempRect2;
            final android.graphics.Rect desiredChildRect = mTempRect3;
            getDescendantRect(lp.mAnchorView, anchorRect);
            getChildRect(child, false, childRect);
            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            getDesiredAnchoredChildRectWithoutConstraints(child, layoutDirection, anchorRect, desiredChildRect, lp, childWidth, childHeight);
            boolean changed = (desiredChildRect.left != childRect.left) || (desiredChildRect.top != childRect.top);
            constrainChildRect(lp, desiredChildRect, childWidth, childHeight);
            final int dx = desiredChildRect.left - childRect.left;
            final int dy = desiredChildRect.top - childRect.top;
            if (dx != 0) {
                android.support.v4.view.ViewCompat.offsetLeftAndRight(child, dx);
            }
            if (dy != 0) {
                android.support.v4.view.ViewCompat.offsetTopAndBottom(child, dy);
            }
            if (changed) {
                // If we have needed to move, make sure to notify the child's Behavior
                final android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
                if (b != null) {
                    b.onDependentViewChanged(this, child, lp.mAnchorView);
                }
            }
        }
    }

    /**
     * Check if a given point in the CoordinatorLayout's coordinates are within the view bounds
     * of the given direct child view.
     *
     * @param child
     * 		child view to test
     * @param x
     * 		X coordinate to test, in the CoordinatorLayout's coordinate system
     * @param y
     * 		Y coordinate to test, in the CoordinatorLayout's coordinate system
     * @return true if the point is within the child view's bounds, false otherwise
     */
    public boolean isPointInChildBounds(android.view.View child, int x, int y) {
        final android.graphics.Rect r = mTempRect1;
        getDescendantRect(child, r);
        return r.contains(x, y);
    }

    /**
     * Check whether two views overlap each other. The views need to be descendants of this
     * {@link CoordinatorLayout} in the view hierarchy.
     *
     * @param first
     * 		first child view to test
     * @param second
     * 		second child view to test
     * @return true if both views are visible and overlap each other
     */
    public boolean doViewsOverlap(android.view.View first, android.view.View second) {
        if ((first.getVisibility() == android.view.View.VISIBLE) && (second.getVisibility() == android.view.View.VISIBLE)) {
            final android.graphics.Rect firstRect = mTempRect1;
            getChildRect(first, first.getParent() != this, firstRect);
            final android.graphics.Rect secondRect = mTempRect2;
            getChildRect(second, second.getParent() != this, secondRect);
            return !((((firstRect.left > secondRect.right) || (firstRect.top > secondRect.bottom)) || (firstRect.right < secondRect.left)) || (firstRect.bottom < secondRect.top));
        }
        return false;
    }

    @java.lang.Override
    public android.support.design.widget.CoordinatorLayout.LayoutParams generateLayoutParams(android.util.AttributeSet attrs) {
        return new android.support.design.widget.CoordinatorLayout.LayoutParams(getContext(), attrs);
    }

    @java.lang.Override
    protected android.support.design.widget.CoordinatorLayout.LayoutParams generateLayoutParams(android.view.ViewGroup.LayoutParams p) {
        if (p instanceof android.support.design.widget.CoordinatorLayout.LayoutParams) {
            return new android.support.design.widget.CoordinatorLayout.LayoutParams(((android.support.design.widget.CoordinatorLayout.LayoutParams) (p)));
        } else
            if (p instanceof android.view.ViewGroup.MarginLayoutParams) {
                return new android.support.design.widget.CoordinatorLayout.LayoutParams(((android.view.ViewGroup.MarginLayoutParams) (p)));
            }

        return new android.support.design.widget.CoordinatorLayout.LayoutParams(p);
    }

    @java.lang.Override
    protected android.support.design.widget.CoordinatorLayout.LayoutParams generateDefaultLayoutParams() {
        return new android.support.design.widget.CoordinatorLayout.LayoutParams(android.support.design.widget.CoordinatorLayout.LayoutParams.WRAP_CONTENT, android.support.design.widget.CoordinatorLayout.LayoutParams.WRAP_CONTENT);
    }

    @java.lang.Override
    protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
        return (p instanceof android.support.design.widget.CoordinatorLayout.LayoutParams) && super.checkLayoutParams(p);
    }

    @java.lang.Override
    public boolean onStartNestedScroll(android.view.View child, android.view.View target, int nestedScrollAxes) {
        boolean handled = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (view.getLayoutParams()));
            final android.support.design.widget.CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
                final boolean accepted = viewBehavior.onStartNestedScroll(this, view, child, target, nestedScrollAxes);
                handled |= accepted;
                lp.acceptNestedScroll(accepted);
            } else {
                lp.acceptNestedScroll(false);
            }
        }
        return handled;
    }

    @java.lang.Override
    public void onNestedScrollAccepted(android.view.View child, android.view.View target, int nestedScrollAxes) {
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        mNestedScrollingDirectChild = child;
        mNestedScrollingTarget = target;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (view.getLayoutParams()));
            if (!lp.isNestedScrollAccepted()) {
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
                viewBehavior.onNestedScrollAccepted(this, view, child, target, nestedScrollAxes);
            }
        }
    }

    @java.lang.Override
    public void onStopNestedScroll(android.view.View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (view.getLayoutParams()));
            if (!lp.isNestedScrollAccepted()) {
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
                viewBehavior.onStopNestedScroll(this, view, target);
            }
            lp.resetNestedScroll();
            lp.resetChangedAfterNestedScroll();
        }
        mNestedScrollingDirectChild = null;
        mNestedScrollingTarget = null;
    }

    @java.lang.Override
    public void onNestedScroll(android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        final int childCount = getChildCount();
        boolean accepted = false;
        for (int i = 0; i < childCount; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (view.getLayoutParams()));
            if (!lp.isNestedScrollAccepted()) {
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
                viewBehavior.onNestedScroll(this, view, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);
                accepted = true;
            }
        }
        if (accepted) {
            onChildViewsChanged(android.support.design.widget.CoordinatorLayout.EVENT_NESTED_SCROLL);
        }
    }

    @java.lang.Override
    public void onNestedPreScroll(android.view.View target, int dx, int dy, int[] consumed) {
        int xConsumed = 0;
        int yConsumed = 0;
        boolean accepted = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (view.getLayoutParams()));
            if (!lp.isNestedScrollAccepted()) {
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
                mTempIntPair[0] = mTempIntPair[1] = 0;
                viewBehavior.onNestedPreScroll(this, view, target, dx, dy, mTempIntPair);
                xConsumed = (dx > 0) ? java.lang.Math.max(xConsumed, mTempIntPair[0]) : java.lang.Math.min(xConsumed, mTempIntPair[0]);
                yConsumed = (dy > 0) ? java.lang.Math.max(yConsumed, mTempIntPair[1]) : java.lang.Math.min(yConsumed, mTempIntPair[1]);
                accepted = true;
            }
        }
        consumed[0] = xConsumed;
        consumed[1] = yConsumed;
        if (accepted) {
            onChildViewsChanged(android.support.design.widget.CoordinatorLayout.EVENT_NESTED_SCROLL);
        }
    }

    @java.lang.Override
    public boolean onNestedFling(android.view.View target, float velocityX, float velocityY, boolean consumed) {
        boolean handled = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (view.getLayoutParams()));
            if (!lp.isNestedScrollAccepted()) {
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
                handled |= viewBehavior.onNestedFling(this, view, target, velocityX, velocityY, consumed);
            }
        }
        if (handled) {
            onChildViewsChanged(android.support.design.widget.CoordinatorLayout.EVENT_NESTED_SCROLL);
        }
        return handled;
    }

    @java.lang.Override
    public boolean onNestedPreFling(android.view.View target, float velocityX, float velocityY) {
        boolean handled = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final android.view.View view = getChildAt(i);
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (view.getLayoutParams()));
            if (!lp.isNestedScrollAccepted()) {
                continue;
            }
            final android.support.design.widget.CoordinatorLayout.Behavior viewBehavior = lp.getBehavior();
            if (viewBehavior != null) {
                handled |= viewBehavior.onNestedPreFling(this, view, target, velocityX, velocityY);
            }
        }
        return handled;
    }

    @java.lang.Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }

    class OnPreDrawListener implements android.view.ViewTreeObserver.OnPreDrawListener {
        @java.lang.Override
        public boolean onPreDraw() {
            onChildViewsChanged(android.support.design.widget.CoordinatorLayout.EVENT_PRE_DRAW);
            return true;
        }
    }

    /**
     * Sorts child views with higher Z values to the beginning of a collection.
     */
    static class ViewElevationComparator implements java.util.Comparator<android.view.View> {
        @java.lang.Override
        public int compare(android.view.View lhs, android.view.View rhs) {
            final float lz = android.support.v4.view.ViewCompat.getZ(lhs);
            final float rz = android.support.v4.view.ViewCompat.getZ(rhs);
            if (lz > rz) {
                return -1;
            } else
                if (lz < rz) {
                    return 1;
                }

            return 0;
        }
    }

    /**
     * Defines the default {@link Behavior} of a {@link View} class.
     *
     * <p>When writing a custom view, use this annotation to define the default behavior
     * when used as a direct child of an {@link CoordinatorLayout}. The default behavior
     * can be overridden using {@link LayoutParams#setBehavior}.</p>
     *
     * <p>Example: <code>@DefaultBehavior(MyBehavior.class)</code></p>
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface DefaultBehavior {
        java.lang.Class<? extends android.support.design.widget.CoordinatorLayout.Behavior> value();
    }

    /**
     * Interaction behavior plugin for child views of {@link CoordinatorLayout}.
     *
     * <p>A Behavior implements one or more interactions that a user can take on a child view.
     * These interactions may include drags, swipes, flings, or any other gestures.</p>
     *
     * @param <V>
     * 		The View type that this Behavior operates on
     */
    public static abstract class Behavior<V extends android.view.View> {
        /**
         * Default constructor for instantiating Behaviors.
         */
        public Behavior() {
        }

        /**
         * Default constructor for inflating Behaviors from layout. The Behavior will have
         * the opportunity to parse specially defined layout parameters. These parameters will
         * appear on the child view tag.
         *
         * @param context
         * 		
         * @param attrs
         * 		
         */
        public Behavior(android.content.Context context, android.util.AttributeSet attrs) {
        }

        /**
         * Called when the Behavior has been attached to a LayoutParams instance.
         *
         * <p>This will be called after the LayoutParams has been instantiated and can be
         * modified.</p>
         *
         * @param params
         * 		the LayoutParams instance that this Behavior has been attached to
         */
        public void onAttachedToLayoutParams(@android.support.annotation.NonNull
        android.support.design.widget.CoordinatorLayout.LayoutParams params) {
        }

        /**
         * Called when the Behavior has been detached from its holding LayoutParams instance.
         *
         * <p>This will only be called if the Behavior has been explicitly removed from the
         * LayoutParams instance via {@link LayoutParams#setBehavior(Behavior)}. It will not be
         * called if the associated view is removed from the CoordinatorLayout or similar.</p>
         */
        public void onDetachedFromLayoutParams() {
        }

        /**
         * Respond to CoordinatorLayout touch events before they are dispatched to child views.
         *
         * <p>Behaviors can use this to monitor inbound touch events until one decides to
         * intercept the rest of the event stream to take an action on its associated child view.
         * This method will return false until it detects the proper intercept conditions, then
         * return true once those conditions have occurred.</p>
         *
         * <p>Once a Behavior intercepts touch events, the rest of the event stream will
         * be sent to the {@link #onTouchEvent} method.</p>
         *
         * <p>The default implementation of this method always returns false.</p>
         *
         * @param parent
         * 		the parent view currently receiving this touch event
         * @param child
         * 		the child view associated with this Behavior
         * @param ev
         * 		the MotionEvent describing the touch event being processed
         * @return true if this Behavior would like to intercept and take over the event stream.
        The default always returns false.
         */
        public boolean onInterceptTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent ev) {
            return false;
        }

        /**
         * Respond to CoordinatorLayout touch events after this Behavior has started
         * {@link #onInterceptTouchEvent intercepting} them.
         *
         * <p>Behaviors may intercept touch events in order to help the CoordinatorLayout
         * manipulate its child views. For example, a Behavior may allow a user to drag a
         * UI pane open or closed. This method should perform actual mutations of view
         * layout state.</p>
         *
         * @param parent
         * 		the parent view currently receiving this touch event
         * @param child
         * 		the child view associated with this Behavior
         * @param ev
         * 		the MotionEvent describing the touch event being processed
         * @return true if this Behavior handled this touch event and would like to continue
        receiving events in this stream. The default always returns false.
         */
        public boolean onTouchEvent(android.support.design.widget.CoordinatorLayout parent, V child, android.view.MotionEvent ev) {
            return false;
        }

        /**
         * Supply a scrim color that will be painted behind the associated child view.
         *
         * <p>A scrim may be used to indicate that the other elements beneath it are not currently
         * interactive or actionable, drawing user focus and attention to the views above the scrim.
         * </p>
         *
         * <p>The default implementation returns {@link Color#BLACK}.</p>
         *
         * @param parent
         * 		the parent view of the given child
         * @param child
         * 		the child view above the scrim
         * @return the desired scrim color in 0xAARRGGBB format. The default return value is
        {@link Color#BLACK}.
         * @see #getScrimOpacity(CoordinatorLayout, android.view.View)
         */
        @android.support.annotation.ColorInt
        public int getScrimColor(android.support.design.widget.CoordinatorLayout parent, V child) {
            return android.graphics.Color.BLACK;
        }

        /**
         * Determine the current opacity of the scrim behind a given child view
         *
         * <p>A scrim may be used to indicate that the other elements beneath it are not currently
         * interactive or actionable, drawing user focus and attention to the views above the scrim.
         * </p>
         *
         * <p>The default implementation returns 0.0f.</p>
         *
         * @param parent
         * 		the parent view of the given child
         * @param child
         * 		the child view above the scrim
         * @return the desired scrim opacity from 0.0f to 1.0f. The default return value is 0.0f.
         */
        @android.support.annotation.FloatRange(from = 0, to = 1)
        public float getScrimOpacity(android.support.design.widget.CoordinatorLayout parent, V child) {
            return 0.0F;
        }

        /**
         * Determine whether interaction with views behind the given child in the child order
         * should be blocked.
         *
         * <p>The default implementation returns true if
         * {@link #getScrimOpacity(CoordinatorLayout, android.view.View)} would return > 0.0f.</p>
         *
         * @param parent
         * 		the parent view of the given child
         * @param child
         * 		the child view to test
         * @return true if {@link #getScrimOpacity(CoordinatorLayout, android.view.View)} would
        return > 0.0f.
         */
        public boolean blocksInteractionBelow(android.support.design.widget.CoordinatorLayout parent, V child) {
            return getScrimOpacity(parent, child) > 0.0F;
        }

        /**
         * Determine whether the supplied child view has another specific sibling view as a
         * layout dependency.
         *
         * <p>This method will be called at least once in response to a layout request. If it
         * returns true for a given child and dependency view pair, the parent CoordinatorLayout
         * will:</p>
         * <ol>
         *     <li>Always lay out this child after the dependent child is laid out, regardless
         *     of child order.</li>
         *     <li>Call {@link #onDependentViewChanged} when the dependency view's layout or
         *     position changes.</li>
         * </ol>
         *
         * @param parent
         * 		the parent view of the given child
         * @param child
         * 		the child view to test
         * @param dependency
         * 		the proposed dependency of child
         * @return true if child's layout depends on the proposed dependency's layout,
        false otherwise
         * @see #onDependentViewChanged(CoordinatorLayout, android.view.View, android.view.View)
         */
        public boolean layoutDependsOn(android.support.design.widget.CoordinatorLayout parent, V child, android.view.View dependency) {
            return false;
        }

        /**
         * Respond to a change in a child's dependent view
         *
         * <p>This method is called whenever a dependent view changes in size or position outside
         * of the standard layout flow. A Behavior may use this method to appropriately update
         * the child view in response.</p>
         *
         * <p>A view's dependency is determined by
         * {@link #layoutDependsOn(CoordinatorLayout, android.view.View, android.view.View)} or
         * if {@code child} has set another view as it's anchor.</p>
         *
         * <p>Note that if a Behavior changes the layout of a child via this method, it should
         * also be able to reconstruct the correct position in
         * {@link #onLayoutChild(CoordinatorLayout, android.view.View, int) onLayoutChild}.
         * <code>onDependentViewChanged</code> will not be called during normal layout since
         * the layout of each child view will always happen in dependency order.</p>
         *
         * <p>If the Behavior changes the child view's size or position, it should return true.
         * The default implementation returns false.</p>
         *
         * @param parent
         * 		the parent view of the given child
         * @param child
         * 		the child view to manipulate
         * @param dependency
         * 		the dependent view that changed
         * @return true if the Behavior changed the child view's size or position, false otherwise
         */
        public boolean onDependentViewChanged(android.support.design.widget.CoordinatorLayout parent, V child, android.view.View dependency) {
            return false;
        }

        /**
         * Respond to a child's dependent view being removed.
         *
         * <p>This method is called after a dependent view has been removed from the parent.
         * A Behavior may use this method to appropriately update the child view in response.</p>
         *
         * <p>A view's dependency is determined by
         * {@link #layoutDependsOn(CoordinatorLayout, android.view.View, android.view.View)} or
         * if {@code child} has set another view as it's anchor.</p>
         *
         * @param parent
         * 		the parent view of the given child
         * @param child
         * 		the child view to manipulate
         * @param dependency
         * 		the dependent view that has been removed
         */
        public void onDependentViewRemoved(android.support.design.widget.CoordinatorLayout parent, V child, android.view.View dependency) {
        }

        /**
         *
         *
         * @deprecated this method is not called anymore. You can safely remove all usages
        and implementations. This method will be removed in a future release.
         */
        @java.lang.Deprecated
        public boolean isDirty(android.support.design.widget.CoordinatorLayout parent, V child) {
            return false;
        }

        /**
         * Called when the parent CoordinatorLayout is about to measure the given child view.
         *
         * <p>This method can be used to perform custom or modified measurement of a child view
         * in place of the default child measurement behavior. The Behavior's implementation
         * can delegate to the standard CoordinatorLayout measurement behavior by calling
         * {@link CoordinatorLayout#onMeasureChild(android.view.View, int, int, int, int)
         * parent.onMeasureChild}.</p>
         *
         * @param parent
         * 		the parent CoordinatorLayout
         * @param child
         * 		the child to measure
         * @param parentWidthMeasureSpec
         * 		the width requirements for this view
         * @param widthUsed
         * 		extra space that has been used up by the parent
         * 		horizontally (possibly by other children of the parent)
         * @param parentHeightMeasureSpec
         * 		the height requirements for this view
         * @param heightUsed
         * 		extra space that has been used up by the parent
         * 		vertically (possibly by other children of the parent)
         * @return true if the Behavior measured the child view, false if the CoordinatorLayout
        should perform its default measurement
         */
        public boolean onMeasureChild(android.support.design.widget.CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
            return false;
        }

        /**
         * Called when the parent CoordinatorLayout is about the lay out the given child view.
         *
         * <p>This method can be used to perform custom or modified layout of a child view
         * in place of the default child layout behavior. The Behavior's implementation can
         * delegate to the standard CoordinatorLayout measurement behavior by calling
         * {@link CoordinatorLayout#onLayoutChild(android.view.View, int)
         * parent.onLayoutChild}.</p>
         *
         * <p>If a Behavior implements
         * {@link #onDependentViewChanged(CoordinatorLayout, android.view.View, android.view.View)}
         * to change the position of a view in response to a dependent view changing, it
         * should also implement <code>onLayoutChild</code> in such a way that respects those
         * dependent views. <code>onLayoutChild</code> will always be called for a dependent view
         * <em>after</em> its dependency has been laid out.</p>
         *
         * @param parent
         * 		the parent CoordinatorLayout
         * @param child
         * 		child view to lay out
         * @param layoutDirection
         * 		the resolved layout direction for the CoordinatorLayout, such as
         * 		{@link ViewCompat#LAYOUT_DIRECTION_LTR} or
         * 		{@link ViewCompat#LAYOUT_DIRECTION_RTL}.
         * @return true if the Behavior performed layout of the child view, false to request
        default layout behavior
         */
        public boolean onLayoutChild(android.support.design.widget.CoordinatorLayout parent, V child, int layoutDirection) {
            return false;
        }

        // Utility methods for accessing child-specific, behavior-modifiable properties.
        /**
         * Associate a Behavior-specific tag object with the given child view.
         * This object will be stored with the child view's LayoutParams.
         *
         * @param child
         * 		child view to set tag with
         * @param tag
         * 		tag object to set
         */
        public static void setTag(android.view.View child, java.lang.Object tag) {
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            lp.mBehaviorTag = tag;
        }

        /**
         * Get the behavior-specific tag object with the given child view.
         * This object is stored with the child view's LayoutParams.
         *
         * @param child
         * 		child view to get tag with
         * @return the previously stored tag object
         */
        public static java.lang.Object getTag(android.view.View child) {
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            return lp.mBehaviorTag;
        }

        /**
         * Called when a descendant of the CoordinatorLayout attempts to initiate a nested scroll.
         *
         * <p>Any Behavior associated with any direct child of the CoordinatorLayout may respond
         * to this event and return true to indicate that the CoordinatorLayout should act as
         * a nested scrolling parent for this scroll. Only Behaviors that return true from
         * this method will receive subsequent nested scroll events.</p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param directTargetChild
         * 		the child view of the CoordinatorLayout that either is or
         * 		contains the target of the nested scroll operation
         * @param target
         * 		the descendant view of the CoordinatorLayout initiating the nested scroll
         * @param nestedScrollAxes
         * 		the axes that this nested scroll applies to. See
         * 		{@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
         * 		{@link ViewCompat#SCROLL_AXIS_VERTICAL}
         * @return true if the Behavior wishes to accept this nested scroll
         * @see NestedScrollingParent#onStartNestedScroll(View, View, int)
         */
        public boolean onStartNestedScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View directTargetChild, android.view.View target, int nestedScrollAxes) {
            return false;
        }

        /**
         * Called when a nested scroll has been accepted by the CoordinatorLayout.
         *
         * <p>Any Behavior associated with any direct child of the CoordinatorLayout may elect
         * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
         * that returned true will receive subsequent nested scroll events for that nested scroll.
         * </p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param directTargetChild
         * 		the child view of the CoordinatorLayout that either is or
         * 		contains the target of the nested scroll operation
         * @param target
         * 		the descendant view of the CoordinatorLayout initiating the nested scroll
         * @param nestedScrollAxes
         * 		the axes that this nested scroll applies to. See
         * 		{@link ViewCompat#SCROLL_AXIS_HORIZONTAL},
         * 		{@link ViewCompat#SCROLL_AXIS_VERTICAL}
         * @see NestedScrollingParent#onNestedScrollAccepted(View, View, int)
         */
        public void onNestedScrollAccepted(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View directTargetChild, android.view.View target, int nestedScrollAxes) {
            // Do nothing
        }

        /**
         * Called when a nested scroll has ended.
         *
         * <p>Any Behavior associated with any direct child of the CoordinatorLayout may elect
         * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
         * that returned true will receive subsequent nested scroll events for that nested scroll.
         * </p>
         *
         * <p><code>onStopNestedScroll</code> marks the end of a single nested scroll event
         * sequence. This is a good place to clean up any state related to the nested scroll.
         * </p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param target
         * 		the descendant view of the CoordinatorLayout that initiated
         * 		the nested scroll
         * @see NestedScrollingParent#onStopNestedScroll(View)
         */
        public void onStopNestedScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target) {
            // Do nothing
        }

        /**
         * Called when a nested scroll in progress has updated and the target has scrolled or
         * attempted to scroll.
         *
         * <p>Any Behavior associated with the direct child of the CoordinatorLayout may elect
         * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
         * that returned true will receive subsequent nested scroll events for that nested scroll.
         * </p>
         *
         * <p><code>onNestedScroll</code> is called each time the nested scroll is updated by the
         * nested scrolling child, with both consumed and unconsumed components of the scroll
         * supplied in pixels. <em>Each Behavior responding to the nested scroll will receive the
         * same values.</em>
         * </p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param target
         * 		the descendant view of the CoordinatorLayout performing the nested scroll
         * @param dxConsumed
         * 		horizontal pixels consumed by the target's own scrolling operation
         * @param dyConsumed
         * 		vertical pixels consumed by the target's own scrolling operation
         * @param dxUnconsumed
         * 		horizontal pixels not consumed by the target's own scrolling
         * 		operation, but requested by the user
         * @param dyUnconsumed
         * 		vertical pixels not consumed by the target's own scrolling operation,
         * 		but requested by the user
         * @see NestedScrollingParent#onNestedScroll(View, int, int, int, int)
         */
        public void onNestedScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
            // Do nothing
        }

        /**
         * Called when a nested scroll in progress is about to update, before the target has
         * consumed any of the scrolled distance.
         *
         * <p>Any Behavior associated with the direct child of the CoordinatorLayout may elect
         * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
         * that returned true will receive subsequent nested scroll events for that nested scroll.
         * </p>
         *
         * <p><code>onNestedPreScroll</code> is called each time the nested scroll is updated
         * by the nested scrolling child, before the nested scrolling child has consumed the scroll
         * distance itself. <em>Each Behavior responding to the nested scroll will receive the
         * same values.</em> The CoordinatorLayout will report as consumed the maximum number
         * of pixels in either direction that any Behavior responding to the nested scroll reported
         * as consumed.</p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param target
         * 		the descendant view of the CoordinatorLayout performing the nested scroll
         * @param dx
         * 		the raw horizontal number of pixels that the user attempted to scroll
         * @param dy
         * 		the raw vertical number of pixels that the user attempted to scroll
         * @param consumed
         * 		out parameter. consumed[0] should be set to the distance of dx that
         * 		was consumed, consumed[1] should be set to the distance of dy that
         * 		was consumed
         * @see NestedScrollingParent#onNestedPreScroll(View, int, int, int[])
         */
        public void onNestedPreScroll(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target, int dx, int dy, int[] consumed) {
            // Do nothing
        }

        /**
         * Called when a nested scrolling child is starting a fling or an action that would
         * be a fling.
         *
         * <p>Any Behavior associated with the direct child of the CoordinatorLayout may elect
         * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
         * that returned true will receive subsequent nested scroll events for that nested scroll.
         * </p>
         *
         * <p><code>onNestedFling</code> is called when the current nested scrolling child view
         * detects the proper conditions for a fling. It reports if the child itself consumed
         * the fling. If it did not, the child is expected to show some sort of overscroll
         * indication. This method should return true if it consumes the fling, so that a child
         * that did not itself take an action in response can choose not to show an overfling
         * indication.</p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param target
         * 		the descendant view of the CoordinatorLayout performing the nested scroll
         * @param velocityX
         * 		horizontal velocity of the attempted fling
         * @param velocityY
         * 		vertical velocity of the attempted fling
         * @param consumed
         * 		true if the nested child view consumed the fling
         * @return true if the Behavior consumed the fling
         * @see NestedScrollingParent#onNestedFling(View, float, float, boolean)
         */
        public boolean onNestedFling(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target, float velocityX, float velocityY, boolean consumed) {
            return false;
        }

        /**
         * Called when a nested scrolling child is about to start a fling.
         *
         * <p>Any Behavior associated with the direct child of the CoordinatorLayout may elect
         * to accept the nested scroll as part of {@link #onStartNestedScroll}. Each Behavior
         * that returned true will receive subsequent nested scroll events for that nested scroll.
         * </p>
         *
         * <p><code>onNestedPreFling</code> is called when the current nested scrolling child view
         * detects the proper conditions for a fling, but it has not acted on it yet. A
         * Behavior can return true to indicate that it consumed the fling. If at least one
         * Behavior returns true, the fling should not be acted upon by the child.</p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param target
         * 		the descendant view of the CoordinatorLayout performing the nested scroll
         * @param velocityX
         * 		horizontal velocity of the attempted fling
         * @param velocityY
         * 		vertical velocity of the attempted fling
         * @return true if the Behavior consumed the fling
         * @see NestedScrollingParent#onNestedPreFling(View, float, float)
         */
        public boolean onNestedPreFling(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.view.View target, float velocityX, float velocityY) {
            return false;
        }

        /**
         * Called when the window insets have changed.
         *
         * <p>Any Behavior associated with the direct child of the CoordinatorLayout may elect
         * to handle the window inset change on behalf of it's associated view.
         * </p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param insets
         * 		the new window insets.
         * @return The insets supplied, minus any insets that were consumed
         */
        @android.support.annotation.NonNull
        public android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.support.v4.view.WindowInsetsCompat insets) {
            return insets;
        }

        /**
         * Called when a child of the view associated with this behavior wants a particular
         * rectangle to be positioned onto the screen.
         *
         * <p>The contract for this method is the same as
         * {@link ViewParent#requestChildRectangleOnScreen(View, Rect, boolean)}.</p>
         *
         * @param coordinatorLayout
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is
         * 		associated with
         * @param rectangle
         * 		The rectangle which the child wishes to be on the screen
         * 		in the child's coordinates
         * @param immediate
         * 		true to forbid animated or delayed scrolling, false otherwise
         * @return true if the Behavior handled the request
         * @see ViewParent#requestChildRectangleOnScreen(View, Rect, boolean)
         */
        public boolean onRequestChildRectangleOnScreen(android.support.design.widget.CoordinatorLayout coordinatorLayout, V child, android.graphics.Rect rectangle, boolean immediate) {
            return false;
        }

        /**
         * Hook allowing a behavior to re-apply a representation of its internal state that had
         * previously been generated by {@link #onSaveInstanceState}. This function will never
         * be called with a null state.
         *
         * @param parent
         * 		the parent CoordinatorLayout
         * @param child
         * 		child view to restore from
         * @param state
         * 		The frozen state that had previously been returned by
         * 		{@link #onSaveInstanceState}.
         * @see #onSaveInstanceState()
         */
        public void onRestoreInstanceState(android.support.design.widget.CoordinatorLayout parent, V child, android.os.Parcelable state) {
            // no-op
        }

        /**
         * Hook allowing a behavior to generate a representation of its internal state
         * that can later be used to create a new instance with that same state.
         * This state should only contain information that is not persistent or can
         * not be reconstructed later.
         *
         * <p>Behavior state is only saved when both the parent {@link CoordinatorLayout} and
         * a view using this behavior have valid IDs set.</p>
         *
         * @param parent
         * 		the parent CoordinatorLayout
         * @param child
         * 		child view to restore from
         * @return Returns a Parcelable object containing the behavior's current dynamic
        state.
         * @see #onRestoreInstanceState(android.os.Parcelable)
         * @see View#onSaveInstanceState()
         */
        public android.os.Parcelable onSaveInstanceState(android.support.design.widget.CoordinatorLayout parent, V child) {
            return android.view.View.BaseSavedState.EMPTY_STATE;
        }

        /**
         * Called when a view is set to dodge view insets.
         *
         * <p>This method allows a behavior to update the rectangle that should be dodged.
         * The rectangle should be in the parent's coordinate system and within the child's
         * bounds. If not, a {@link IllegalArgumentException} is thrown.</p>
         *
         * @param parent
         * 		the CoordinatorLayout parent of the view this Behavior is
         * 		associated with
         * @param child
         * 		the child view of the CoordinatorLayout this Behavior is associated with
         * @param rect
         * 		the rect to update with the dodge rectangle
         * @return true the rect was updated, false if we should use the child's bounds
         */
        public boolean getInsetDodgeRect(@android.support.annotation.NonNull
        android.support.design.widget.CoordinatorLayout parent, @android.support.annotation.NonNull
        V child, @android.support.annotation.NonNull
        android.graphics.Rect rect) {
            return false;
        }
    }

    /**
     * Parameters describing the desired layout for a child of a {@link CoordinatorLayout}.
     */
    public static class LayoutParams extends android.view.ViewGroup.MarginLayoutParams {
        /**
         * A {@link Behavior} that the child view should obey.
         */
        android.support.design.widget.CoordinatorLayout.Behavior mBehavior;

        boolean mBehaviorResolved = false;

        /**
         * A {@link Gravity} value describing how this child view should lay out.
         * If an {@link #setAnchorId(int) anchor} is also specified, the gravity describes
         * how this child view should be positioned relative to its anchored position.
         */
        public int gravity = android.view.Gravity.NO_GRAVITY;

        /**
         * A {@link Gravity} value describing which edge of a child view's
         * {@link #getAnchorId() anchor} view the child should position itself relative to.
         */
        public int anchorGravity = android.view.Gravity.NO_GRAVITY;

        /**
         * The index of the horizontal keyline specified to the parent CoordinatorLayout that this
         * child should align relative to. If an {@link #setAnchorId(int) anchor} is present the
         * keyline will be ignored.
         */
        public int keyline = -1;

        /**
         * A {@link View#getId() view id} of a descendant view of the CoordinatorLayout that
         * this child should position relative to.
         */
        int mAnchorId = android.view.View.NO_ID;

        /**
         * A {@link Gravity} value describing how this child view insets the CoordinatorLayout.
         * Other child views which are set to dodge the same inset edges will be moved appropriately
         * so that the views do not overlap.
         */
        public int insetEdge = android.view.Gravity.NO_GRAVITY;

        /**
         * A {@link Gravity} value describing how this child view dodges any inset child views in
         * the CoordinatorLayout. Any views which are inset on the same edge as this view is set to
         * dodge will result in this view being moved so that the views do not overlap.
         */
        public int dodgeInsetEdges = android.view.Gravity.NO_GRAVITY;

        int mInsetOffsetX;

        int mInsetOffsetY;

        android.view.View mAnchorView;

        android.view.View mAnchorDirectChild;

        private boolean mDidBlockInteraction;

        private boolean mDidAcceptNestedScroll;

        private boolean mDidChangeAfterNestedScroll;

        final android.graphics.Rect mLastChildRect = new android.graphics.Rect();

        java.lang.Object mBehaviorTag;

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        LayoutParams(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
            final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CoordinatorLayout_Layout);
            this.gravity = a.getInteger(R.styleable.CoordinatorLayout_Layout_android_layout_gravity, android.view.Gravity.NO_GRAVITY);
            mAnchorId = a.getResourceId(R.styleable.CoordinatorLayout_Layout_layout_anchor, android.view.View.NO_ID);
            this.anchorGravity = a.getInteger(R.styleable.CoordinatorLayout_Layout_layout_anchorGravity, android.view.Gravity.NO_GRAVITY);
            this.keyline = a.getInteger(R.styleable.CoordinatorLayout_Layout_layout_keyline, -1);
            insetEdge = a.getInt(R.styleable.CoordinatorLayout_Layout_layout_insetEdge, 0);
            dodgeInsetEdges = a.getInt(R.styleable.CoordinatorLayout_Layout_layout_dodgeInsetEdges, 0);
            mBehaviorResolved = a.hasValue(R.styleable.CoordinatorLayout_Layout_layout_behavior);
            if (mBehaviorResolved) {
                mBehavior = android.support.design.widget.CoordinatorLayout.parseBehavior(context, attrs, a.getString(R.styleable.CoordinatorLayout_Layout_layout_behavior));
            }
            a.recycle();
            if (mBehavior != null) {
                // If we have a Behavior, dispatch that it has been attached
                mBehavior.onAttachedToLayoutParams(this);
            }
        }

        public LayoutParams(android.support.design.widget.CoordinatorLayout.LayoutParams p) {
            super(p);
        }

        public LayoutParams(android.view.ViewGroup.MarginLayoutParams p) {
            super(p);
        }

        public LayoutParams(android.view.ViewGroup.LayoutParams p) {
            super(p);
        }

        /**
         * Get the id of this view's anchor.
         *
         * @return A {@link View#getId() view id} or {@link View#NO_ID} if there is no anchor
         */
        @android.support.annotation.IdRes
        public int getAnchorId() {
            return mAnchorId;
        }

        /**
         * Set the id of this view's anchor.
         *
         * <p>The view with this id must be a descendant of the CoordinatorLayout containing
         * the child view this LayoutParams belongs to. It may not be the child view with
         * this LayoutParams or a descendant of it.</p>
         *
         * @param id
         * 		The {@link View#getId() view id} of the anchor or
         * 		{@link View#NO_ID} if there is no anchor
         */
        public void setAnchorId(@android.support.annotation.IdRes
        int id) {
            invalidateAnchor();
            mAnchorId = id;
        }

        /**
         * Get the behavior governing the layout and interaction of the child view within
         * a parent CoordinatorLayout.
         *
         * @return The current behavior or null if no behavior is specified
         */
        @android.support.annotation.Nullable
        public android.support.design.widget.CoordinatorLayout.Behavior getBehavior() {
            return mBehavior;
        }

        /**
         * Set the behavior governing the layout and interaction of the child view within
         * a parent CoordinatorLayout.
         *
         * <p>Setting a new behavior will remove any currently associated
         * {@link Behavior#setTag(android.view.View, Object) Behavior tag}.</p>
         *
         * @param behavior
         * 		The behavior to set or null for no special behavior
         */
        public void setBehavior(@android.support.annotation.Nullable
        android.support.design.widget.CoordinatorLayout.Behavior behavior) {
            if (mBehavior != behavior) {
                if (mBehavior != null) {
                    // First detach any old behavior
                    mBehavior.onDetachedFromLayoutParams();
                }
                mBehavior = behavior;
                mBehaviorTag = null;
                mBehaviorResolved = true;
                if (behavior != null) {
                    // Now dispatch that the Behavior has been attached
                    behavior.onAttachedToLayoutParams(this);
                }
            }
        }

        /**
         * Set the last known position rect for this child view
         *
         * @param r
         * 		the rect to set
         */
        void setLastChildRect(android.graphics.Rect r) {
            mLastChildRect.set(r);
        }

        /**
         * Get the last known position rect for this child view.
         * Note: do not mutate the result of this call.
         */
        android.graphics.Rect getLastChildRect() {
            return mLastChildRect;
        }

        /**
         * Returns true if the anchor id changed to another valid view id since the anchor view
         * was resolved.
         */
        boolean checkAnchorChanged() {
            return (mAnchorView == null) && (mAnchorId != android.view.View.NO_ID);
        }

        /**
         * Returns true if the associated Behavior previously blocked interaction with other views
         * below the associated child since the touch behavior tracking was last
         * {@link #resetTouchBehaviorTracking() reset}.
         *
         * @see #isBlockingInteractionBelow(CoordinatorLayout, android.view.View)
         */
        boolean didBlockInteraction() {
            if (mBehavior == null) {
                mDidBlockInteraction = false;
            }
            return mDidBlockInteraction;
        }

        /**
         * Check if the associated Behavior wants to block interaction below the given child
         * view. The given child view should be the child this LayoutParams is associated with.
         *
         * <p>Once interaction is blocked, it will remain blocked until touch interaction tracking
         * is {@link #resetTouchBehaviorTracking() reset}.</p>
         *
         * @param parent
         * 		the parent CoordinatorLayout
         * @param child
         * 		the child view this LayoutParams is associated with
         * @return true to block interaction below the given child
         */
        boolean isBlockingInteractionBelow(android.support.design.widget.CoordinatorLayout parent, android.view.View child) {
            if (mDidBlockInteraction) {
                return true;
            }
            return mDidBlockInteraction |= (mBehavior != null) ? mBehavior.blocksInteractionBelow(parent, child) : false;
        }

        /**
         * Reset tracking of Behavior-specific touch interactions. This includes
         * interaction blocking.
         *
         * @see #isBlockingInteractionBelow(CoordinatorLayout, android.view.View)
         * @see #didBlockInteraction()
         */
        void resetTouchBehaviorTracking() {
            mDidBlockInteraction = false;
        }

        void resetNestedScroll() {
            mDidAcceptNestedScroll = false;
        }

        void acceptNestedScroll(boolean accept) {
            mDidAcceptNestedScroll = accept;
        }

        boolean isNestedScrollAccepted() {
            return mDidAcceptNestedScroll;
        }

        boolean getChangedAfterNestedScroll() {
            return mDidChangeAfterNestedScroll;
        }

        void setChangedAfterNestedScroll(boolean changed) {
            mDidChangeAfterNestedScroll = changed;
        }

        void resetChangedAfterNestedScroll() {
            mDidChangeAfterNestedScroll = false;
        }

        /**
         * Check if an associated child view depends on another child view of the CoordinatorLayout.
         *
         * @param parent
         * 		the parent CoordinatorLayout
         * @param child
         * 		the child to check
         * @param dependency
         * 		the proposed dependency to check
         * @return true if child depends on dependency
         */
        boolean dependsOn(android.support.design.widget.CoordinatorLayout parent, android.view.View child, android.view.View dependency) {
            return ((dependency == mAnchorDirectChild) || shouldDodge(dependency, android.support.v4.view.ViewCompat.getLayoutDirection(parent))) || ((mBehavior != null) && mBehavior.layoutDependsOn(parent, child, dependency));
        }

        /**
         * Invalidate the cached anchor view and direct child ancestor of that anchor.
         * The anchor will need to be
         * {@link #findAnchorView(CoordinatorLayout, android.view.View) found} before
         * being used again.
         */
        void invalidateAnchor() {
            mAnchorView = mAnchorDirectChild = null;
        }

        /**
         * Locate the appropriate anchor view by the current {@link #setAnchorId(int) anchor id}
         * or return the cached anchor view if already known.
         *
         * @param parent
         * 		the parent CoordinatorLayout
         * @param forChild
         * 		the child this LayoutParams is associated with
         * @return the located descendant anchor view, or null if the anchor id is
        {@link View#NO_ID}.
         */
        android.view.View findAnchorView(android.support.design.widget.CoordinatorLayout parent, android.view.View forChild) {
            if (mAnchorId == android.view.View.NO_ID) {
                mAnchorView = mAnchorDirectChild = null;
                return null;
            }
            if ((mAnchorView == null) || (!verifyAnchorView(forChild, parent))) {
                resolveAnchorView(forChild, parent);
            }
            return mAnchorView;
        }

        /**
         * Determine the anchor view for the child view this LayoutParams is assigned to.
         * Assumes mAnchorId is valid.
         */
        private void resolveAnchorView(final android.view.View forChild, final android.support.design.widget.CoordinatorLayout parent) {
            mAnchorView = parent.findViewById(mAnchorId);
            if (mAnchorView != null) {
                if (mAnchorView == parent) {
                    if (parent.isInEditMode()) {
                        mAnchorView = mAnchorDirectChild = null;
                        return;
                    }
                    throw new java.lang.IllegalStateException("View can not be anchored to the the parent CoordinatorLayout");
                }
                android.view.View directChild = mAnchorView;
                for (android.view.ViewParent p = mAnchorView.getParent(); (p != parent) && (p != null); p = p.getParent()) {
                    if (p == forChild) {
                        if (parent.isInEditMode()) {
                            mAnchorView = mAnchorDirectChild = null;
                            return;
                        }
                        throw new java.lang.IllegalStateException("Anchor must not be a descendant of the anchored view");
                    }
                    if (p instanceof android.view.View) {
                        directChild = ((android.view.View) (p));
                    }
                }
                mAnchorDirectChild = directChild;
            } else {
                if (parent.isInEditMode()) {
                    mAnchorView = mAnchorDirectChild = null;
                    return;
                }
                throw new java.lang.IllegalStateException(((("Could not find CoordinatorLayout descendant view" + " with id ") + parent.getResources().getResourceName(mAnchorId)) + " to anchor view ") + forChild);
            }
        }

        /**
         * Verify that the previously resolved anchor view is still valid - that it is still
         * a descendant of the expected parent view, it is not the child this LayoutParams
         * is assigned to or a descendant of it, and it has the expected id.
         */
        private boolean verifyAnchorView(android.view.View forChild, android.support.design.widget.CoordinatorLayout parent) {
            if (mAnchorView.getId() != mAnchorId) {
                return false;
            }
            android.view.View directChild = mAnchorView;
            for (android.view.ViewParent p = mAnchorView.getParent(); p != parent; p = p.getParent()) {
                if ((p == null) || (p == forChild)) {
                    mAnchorView = mAnchorDirectChild = null;
                    return false;
                }
                if (p instanceof android.view.View) {
                    directChild = ((android.view.View) (p));
                }
            }
            mAnchorDirectChild = directChild;
            return true;
        }

        /**
         * Checks whether the view with this LayoutParams should dodge the specified view.
         */
        private boolean shouldDodge(android.view.View other, int layoutDirection) {
            android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (other.getLayoutParams()));
            final int absInset = android.support.v4.view.GravityCompat.getAbsoluteGravity(lp.insetEdge, layoutDirection);
            return (absInset != android.view.Gravity.NO_GRAVITY) && ((absInset & android.support.v4.view.GravityCompat.getAbsoluteGravity(dodgeInsetEdges, layoutDirection)) == absInset);
        }
    }

    private class HierarchyChangeListener implements android.view.ViewGroup.OnHierarchyChangeListener {
        HierarchyChangeListener() {
        }

        @java.lang.Override
        public void onChildViewAdded(android.view.View parent, android.view.View child) {
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @java.lang.Override
        public void onChildViewRemoved(android.view.View parent, android.view.View child) {
            onChildViewsChanged(android.support.design.widget.CoordinatorLayout.EVENT_VIEW_REMOVED);
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }

    @java.lang.Override
    protected void onRestoreInstanceState(android.os.Parcelable state) {
        if (!(state instanceof android.support.design.widget.CoordinatorLayout.SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }
        final android.support.design.widget.CoordinatorLayout.SavedState ss = ((android.support.design.widget.CoordinatorLayout.SavedState) (state));
        super.onRestoreInstanceState(ss.getSuperState());
        final android.util.SparseArray<android.os.Parcelable> behaviorStates = ss.behaviorStates;
        for (int i = 0, count = getChildCount(); i < count; i++) {
            final android.view.View child = getChildAt(i);
            final int childId = child.getId();
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = getResolvedLayoutParams(child);
            final android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
            if ((childId != android.view.View.NO_ID) && (b != null)) {
                android.os.Parcelable savedState = behaviorStates.get(childId);
                if (savedState != null) {
                    b.onRestoreInstanceState(this, child, savedState);
                }
            }
        }
    }

    @java.lang.Override
    protected android.os.Parcelable onSaveInstanceState() {
        final android.support.design.widget.CoordinatorLayout.SavedState ss = new android.support.design.widget.CoordinatorLayout.SavedState(super.onSaveInstanceState());
        final android.util.SparseArray<android.os.Parcelable> behaviorStates = new android.util.SparseArray<>();
        for (int i = 0, count = getChildCount(); i < count; i++) {
            final android.view.View child = getChildAt(i);
            final int childId = child.getId();
            final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
            final android.support.design.widget.CoordinatorLayout.Behavior b = lp.getBehavior();
            if ((childId != android.view.View.NO_ID) && (b != null)) {
                // If the child has an ID and a Behavior, let it save some state...
                android.os.Parcelable state = b.onSaveInstanceState(this, child);
                if (state != null) {
                    behaviorStates.append(childId, state);
                }
            }
        }
        ss.behaviorStates = behaviorStates;
        return ss;
    }

    @java.lang.Override
    public boolean requestChildRectangleOnScreen(android.view.View child, android.graphics.Rect rectangle, boolean immediate) {
        final android.support.design.widget.CoordinatorLayout.LayoutParams lp = ((android.support.design.widget.CoordinatorLayout.LayoutParams) (child.getLayoutParams()));
        final android.support.design.widget.CoordinatorLayout.Behavior behavior = lp.getBehavior();
        if ((behavior != null) && behavior.onRequestChildRectangleOnScreen(this, child, rectangle, immediate)) {
            return true;
        }
        return super.requestChildRectangleOnScreen(child, rectangle, immediate);
    }

    private void setupForInsets() {
        if (android.os.Build.VERSION.SDK_INT < 21) {
            return;
        }
        if (android.support.v4.view.ViewCompat.getFitsSystemWindows(this)) {
            if (mApplyWindowInsetsListener == null) {
                mApplyWindowInsetsListener = new android.support.v4.view.OnApplyWindowInsetsListener() {
                    @java.lang.Override
                    public android.support.v4.view.WindowInsetsCompat onApplyWindowInsets(android.view.View v, android.support.v4.view.WindowInsetsCompat insets) {
                        return setWindowInsets(insets);
                    }
                };
            }
            // First apply the insets listener
            android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(this, mApplyWindowInsetsListener);
            // Now set the sys ui flags to enable us to lay out in the window insets
            setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_STABLE | android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            android.support.v4.view.ViewCompat.setOnApplyWindowInsetsListener(this, null);
        }
    }

    protected static class SavedState extends android.support.v4.view.AbsSavedState {
        android.util.SparseArray<android.os.Parcelable> behaviorStates;

        public SavedState(android.os.Parcel source, java.lang.ClassLoader loader) {
            super(source, loader);
            final int size = source.readInt();
            final int[] ids = new int[size];
            source.readIntArray(ids);
            final android.os.Parcelable[] states = source.readParcelableArray(loader);
            behaviorStates = new android.util.SparseArray<>(size);
            for (int i = 0; i < size; i++) {
                behaviorStates.append(ids[i], states[i]);
            }
        }

        public SavedState(android.os.Parcelable superState) {
            super(superState);
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            final int size = (behaviorStates != null) ? behaviorStates.size() : 0;
            dest.writeInt(size);
            final int[] ids = new int[size];
            final android.os.Parcelable[] states = new android.os.Parcelable[size];
            for (int i = 0; i < size; i++) {
                ids[i] = behaviorStates.keyAt(i);
                states[i] = behaviorStates.valueAt(i);
            }
            dest.writeIntArray(ids);
            dest.writeParcelableArray(states, flags);
        }

        public static final android.os.Parcelable.Creator<android.support.design.widget.CoordinatorLayout.SavedState> CREATOR = android.support.v4.os.ParcelableCompat.newCreator(new android.support.v4.os.ParcelableCompatCreatorCallbacks<android.support.design.widget.CoordinatorLayout.SavedState>() {
            @java.lang.Override
            public android.support.design.widget.CoordinatorLayout.SavedState createFromParcel(android.os.Parcel in, java.lang.ClassLoader loader) {
                return new android.support.design.widget.CoordinatorLayout.SavedState(in, loader);
            }

            @java.lang.Override
            public android.support.design.widget.CoordinatorLayout.SavedState[] newArray(int size) {
                return new android.support.design.widget.CoordinatorLayout.SavedState[size];
            }
        });
    }
}

