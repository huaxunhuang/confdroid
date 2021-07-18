/**
 * Copyright (C) 2014 The Android Open Source Project
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


/**
 * Base class for ExitTransitionCoordinator and EnterTransitionCoordinator, classes
 * that manage activity transitions and the communications coordinating them between
 * Activities. The ExitTransitionCoordinator is created in the
 * ActivityOptions#makeSceneTransitionAnimation. The EnterTransitionCoordinator
 * is created by ActivityOptions#createEnterActivityTransition by Activity when the window is
 * attached.
 *
 * Typical startActivity goes like this:
 * 1) ExitTransitionCoordinator created with ActivityOptions#makeSceneTransitionAnimation
 * 2) Activity#startActivity called and that calls startExit() through
 *    ActivityOptions#dispatchStartExit
 *    - Exit transition starts by setting transitioning Views to INVISIBLE
 * 3) Launched Activity starts, creating an EnterTransitionCoordinator.
 *    - The Window is made translucent
 *    - The Window background alpha is set to 0
 *    - The transitioning views are made INVISIBLE
 *    - MSG_SET_LISTENER is sent back to the ExitTransitionCoordinator.
 * 4) The shared element transition completes.
 *    - MSG_TAKE_SHARED_ELEMENTS is sent to the EnterTransitionCoordinator
 * 5) The MSG_TAKE_SHARED_ELEMENTS is received by the EnterTransitionCoordinator.
 *    - Shared elements are made VISIBLE
 *    - Shared elements positions and size are set to match the end state of the calling
 *      Activity.
 *    - The shared element transition is started
 *    - If the window allows overlapping transitions, the views transition is started by setting
 *      the entering Views to VISIBLE and the background alpha is animated to opaque.
 *    - MSG_HIDE_SHARED_ELEMENTS is sent to the ExitTransitionCoordinator
 * 6) MSG_HIDE_SHARED_ELEMENTS is received by the ExitTransitionCoordinator
 *    - The shared elements are made INVISIBLE
 * 7) The exit transition completes in the calling Activity.
 *    - MSG_EXIT_TRANSITION_COMPLETE is sent to the EnterTransitionCoordinator.
 * 8) The MSG_EXIT_TRANSITION_COMPLETE is received by the EnterTransitionCoordinator.
 *    - If the window doesn't allow overlapping enter transitions, the enter transition is started
 *      by setting entering views to VISIBLE and the background is animated to opaque.
 * 9) The background opacity animation completes.
 *    - The window is made opaque
 * 10) The calling Activity gets an onStop() call
 *    - onActivityStopped() is called and all exited Views are made VISIBLE.
 *
 * Typical finishAfterTransition goes like this:
 * 1) finishAfterTransition() creates an ExitTransitionCoordinator and calls startExit()
 *    - The Window start transitioning to Translucent with a new ActivityOptions.
 *    - If no background exists, a black background is substituted
 *    - The shared elements in the scene are matched against those shared elements
 *      that were sent by comparing the names.
 *    - The exit transition is started by setting Views to INVISIBLE.
 * 2) The ActivityOptions is received by the Activity and an EnterTransitionCoordinator is created.
 *    - All transitioning views are made VISIBLE to reverse what was done when onActivityStopped()
 *      was called
 * 3) The Window is made translucent and a callback is received
 *    - The background alpha is animated to 0
 * 4) The background alpha animation completes
 * 5) The shared element transition completes
 *    - After both 4 & 5 complete, MSG_TAKE_SHARED_ELEMENTS is sent to the
 *      EnterTransitionCoordinator
 * 6) MSG_TAKE_SHARED_ELEMENTS is received by EnterTransitionCoordinator
 *    - Shared elements are made VISIBLE
 *    - Shared elements positions and size are set to match the end state of the calling
 *      Activity.
 *    - The shared element transition is started
 *    - If the window allows overlapping transitions, the views transition is started by setting
 *      the entering Views to VISIBLE.
 *    - MSG_HIDE_SHARED_ELEMENTS is sent to the ExitTransitionCoordinator
 * 7) MSG_HIDE_SHARED_ELEMENTS is received by the ExitTransitionCoordinator
 *    - The shared elements are made INVISIBLE
 * 8) The exit transition completes in the finishing Activity.
 *    - MSG_EXIT_TRANSITION_COMPLETE is sent to the EnterTransitionCoordinator.
 *    - finish() is called on the exiting Activity
 * 9) The MSG_EXIT_TRANSITION_COMPLETE is received by the EnterTransitionCoordinator.
 *    - If the window doesn't allow overlapping enter transitions, the enter transition is started
 *      by setting entering views to VISIBLE.
 */
abstract class ActivityTransitionCoordinator extends android.os.ResultReceiver {
    private static final java.lang.String TAG = "ActivityTransitionCoordinator";

    /**
     * For Activity transitions, the called Activity's listener to receive calls
     * when transitions complete.
     */
    static final java.lang.String KEY_REMOTE_RECEIVER = "android:remoteReceiver";

    protected static final java.lang.String KEY_SCREEN_LEFT = "shared_element:screenLeft";

    protected static final java.lang.String KEY_SCREEN_TOP = "shared_element:screenTop";

    protected static final java.lang.String KEY_SCREEN_RIGHT = "shared_element:screenRight";

    protected static final java.lang.String KEY_SCREEN_BOTTOM = "shared_element:screenBottom";

    protected static final java.lang.String KEY_TRANSLATION_Z = "shared_element:translationZ";

    protected static final java.lang.String KEY_SNAPSHOT = "shared_element:bitmap";

    protected static final java.lang.String KEY_SCALE_TYPE = "shared_element:scaleType";

    protected static final java.lang.String KEY_IMAGE_MATRIX = "shared_element:imageMatrix";

    protected static final java.lang.String KEY_ELEVATION = "shared_element:elevation";

    protected static final android.widget.ImageView.ScaleType[] SCALE_TYPE_VALUES = android.widget.ImageView.ScaleType.values();

    /**
     * Sent by the exiting coordinator (either EnterTransitionCoordinator
     * or ExitTransitionCoordinator) after the shared elements have
     * become stationary (shared element transition completes). This tells
     * the remote coordinator to take control of the shared elements and
     * that animations may begin. The remote Activity won't start entering
     * until this message is received, but may wait for
     * MSG_EXIT_TRANSITION_COMPLETE if allowOverlappingTransitions() is true.
     */
    public static final int MSG_SET_REMOTE_RECEIVER = 100;

    /**
     * Sent by the entering coordinator to tell the exiting coordinator
     * to hide its shared elements after it has started its shared
     * element transition. This is temporary until the
     * interlock of shared elements is figured out.
     */
    public static final int MSG_HIDE_SHARED_ELEMENTS = 101;

    /**
     * Sent by the exiting coordinator (either EnterTransitionCoordinator
     * or ExitTransitionCoordinator) after the shared elements have
     * become stationary (shared element transition completes). This tells
     * the remote coordinator to take control of the shared elements and
     * that animations may begin. The remote Activity won't start entering
     * until this message is received, but may wait for
     * MSG_EXIT_TRANSITION_COMPLETE if allowOverlappingTransitions() is true.
     */
    public static final int MSG_TAKE_SHARED_ELEMENTS = 103;

    /**
     * Sent by the exiting coordinator (either
     * EnterTransitionCoordinator or ExitTransitionCoordinator) after
     * the exiting Views have finished leaving the scene. This will
     * be ignored if allowOverlappingTransitions() is true on the
     * remote coordinator. If it is false, it will trigger the enter
     * transition to start.
     */
    public static final int MSG_EXIT_TRANSITION_COMPLETE = 104;

    /**
     * Sent by Activity#startActivity to begin the exit transition.
     */
    public static final int MSG_START_EXIT_TRANSITION = 105;

    /**
     * It took too long for a message from the entering Activity, so we canceled the transition.
     */
    public static final int MSG_CANCEL = 106;

    /**
     * When returning, this is the destination location for the shared element.
     */
    public static final int MSG_SHARED_ELEMENT_DESTINATION = 107;

    private android.view.Window mWindow;

    protected final java.util.ArrayList<java.lang.String> mAllSharedElementNames;

    protected final java.util.ArrayList<android.view.View> mSharedElements = new java.util.ArrayList<android.view.View>();

    protected final java.util.ArrayList<java.lang.String> mSharedElementNames = new java.util.ArrayList<java.lang.String>();

    protected java.util.ArrayList<android.view.View> mTransitioningViews = new java.util.ArrayList<android.view.View>();

    protected android.app.SharedElementCallback mListener;

    protected android.os.ResultReceiver mResultReceiver;

    private final android.app.ActivityTransitionCoordinator.FixedEpicenterCallback mEpicenterCallback = new android.app.ActivityTransitionCoordinator.FixedEpicenterCallback();

    protected final boolean mIsReturning;

    private java.lang.Runnable mPendingTransition;

    private boolean mIsStartingTransition;

    private java.util.ArrayList<android.app.ActivityTransitionCoordinator.GhostViewListeners> mGhostViewListeners = new java.util.ArrayList<android.app.ActivityTransitionCoordinator.GhostViewListeners>();

    private android.util.ArrayMap<android.view.View, java.lang.Float> mOriginalAlphas = new android.util.ArrayMap<android.view.View, java.lang.Float>();

    private java.util.ArrayList<android.graphics.Matrix> mSharedElementParentMatrices;

    private boolean mSharedElementTransitionComplete;

    private boolean mViewsTransitionComplete;

    public ActivityTransitionCoordinator(android.view.Window window, java.util.ArrayList<java.lang.String> allSharedElementNames, android.app.SharedElementCallback listener, boolean isReturning) {
        super(new android.os.Handler());
        mWindow = window;
        mListener = listener;
        mAllSharedElementNames = allSharedElementNames;
        mIsReturning = isReturning;
    }

    protected void viewsReady(android.util.ArrayMap<java.lang.String, android.view.View> sharedElements) {
        sharedElements.retainAll(mAllSharedElementNames);
        if (mListener != null) {
            mListener.onMapSharedElements(mAllSharedElementNames, sharedElements);
        }
        setSharedElements(sharedElements);
        if ((getViewsTransition() != null) && (mTransitioningViews != null)) {
            android.view.ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.captureTransitioningViews(mTransitioningViews);
            }
            mTransitioningViews.removeAll(mSharedElements);
        }
        setEpicenter();
    }

    /**
     * Iterates over the shared elements and adds them to the members in order.
     * Shared elements that are nested in other shared elements are placed after the
     * elements that they are nested in. This means that layout ordering can be done
     * from first to last.
     *
     * @param sharedElements
     * 		The map of transition names to shared elements to set into
     * 		the member fields.
     */
    private void setSharedElements(android.util.ArrayMap<java.lang.String, android.view.View> sharedElements) {
        boolean isFirstRun = true;
        while (!sharedElements.isEmpty()) {
            final int numSharedElements = sharedElements.size();
            for (int i = numSharedElements - 1; i >= 0; i--) {
                final android.view.View view = sharedElements.valueAt(i);
                final java.lang.String name = sharedElements.keyAt(i);
                if (isFirstRun && (((view == null) || (!view.isAttachedToWindow())) || (name == null))) {
                    sharedElements.removeAt(i);
                } else
                    if (!android.app.ActivityTransitionCoordinator.isNested(view, sharedElements)) {
                        mSharedElementNames.add(name);
                        mSharedElements.add(view);
                        sharedElements.removeAt(i);
                    }

            }
            isFirstRun = false;
        } 
    }

    /**
     * Returns true when view is nested in any of the values of sharedElements.
     */
    private static boolean isNested(android.view.View view, android.util.ArrayMap<java.lang.String, android.view.View> sharedElements) {
        android.view.ViewParent parent = view.getParent();
        boolean isNested = false;
        while (parent instanceof android.view.View) {
            android.view.View parentView = ((android.view.View) (parent));
            if (sharedElements.containsValue(parentView)) {
                isNested = true;
                break;
            }
            parent = parentView.getParent();
        } 
        return isNested;
    }

    protected void stripOffscreenViews() {
        if (mTransitioningViews == null) {
            return;
        }
        android.graphics.Rect r = new android.graphics.Rect();
        for (int i = mTransitioningViews.size() - 1; i >= 0; i--) {
            android.view.View view = mTransitioningViews.get(i);
            if (!view.getGlobalVisibleRect(r)) {
                mTransitioningViews.remove(i);
                showView(view, true);
            }
        }
    }

    protected android.view.Window getWindow() {
        return mWindow;
    }

    public android.view.ViewGroup getDecor() {
        return mWindow == null ? null : ((android.view.ViewGroup) (mWindow.getDecorView()));
    }

    /**
     * Sets the transition epicenter to the position of the first shared element.
     */
    protected void setEpicenter() {
        android.view.View epicenter = null;
        if ((!mAllSharedElementNames.isEmpty()) && (!mSharedElementNames.isEmpty())) {
            int index = mSharedElementNames.indexOf(mAllSharedElementNames.get(0));
            if (index >= 0) {
                epicenter = mSharedElements.get(index);
            }
        }
        setEpicenter(epicenter);
    }

    private void setEpicenter(android.view.View view) {
        if (view == null) {
            mEpicenterCallback.setEpicenter(null);
        } else {
            android.graphics.Rect epicenter = new android.graphics.Rect();
            view.getBoundsOnScreen(epicenter);
            mEpicenterCallback.setEpicenter(epicenter);
        }
    }

    public java.util.ArrayList<java.lang.String> getAcceptedNames() {
        return mSharedElementNames;
    }

    public java.util.ArrayList<java.lang.String> getMappedNames() {
        java.util.ArrayList<java.lang.String> names = new java.util.ArrayList<java.lang.String>(mSharedElements.size());
        for (int i = 0; i < mSharedElements.size(); i++) {
            names.add(mSharedElements.get(i).getTransitionName());
        }
        return names;
    }

    public java.util.ArrayList<android.view.View> copyMappedViews() {
        return new java.util.ArrayList<android.view.View>(mSharedElements);
    }

    public java.util.ArrayList<java.lang.String> getAllSharedElementNames() {
        return mAllSharedElementNames;
    }

    protected android.transition.Transition setTargets(android.transition.Transition transition, boolean add) {
        if ((transition == null) || (add && ((mTransitioningViews == null) || mTransitioningViews.isEmpty()))) {
            return null;
        }
        // Add the targets to a set containing transition so that transition
        // remains unaffected. We don't want to modify the targets of transition itself.
        android.transition.TransitionSet set = new android.transition.TransitionSet();
        if (mTransitioningViews != null) {
            for (int i = mTransitioningViews.size() - 1; i >= 0; i--) {
                android.view.View view = mTransitioningViews.get(i);
                if (add) {
                    set.addTarget(view);
                } else {
                    set.excludeTarget(view, true);
                }
            }
        }
        // By adding the transition after addTarget, we prevent addTarget from
        // affecting transition.
        set.addTransition(transition);
        if (((!add) && (mTransitioningViews != null)) && (!mTransitioningViews.isEmpty())) {
            // Allow children of excluded transitioning views, but not the views themselves
            set = new android.transition.TransitionSet().addTransition(set);
        }
        return set;
    }

    protected android.transition.Transition configureTransition(android.transition.Transition transition, boolean includeTransitioningViews) {
        if (transition != null) {
            transition = transition.clone();
            transition.setEpicenterCallback(mEpicenterCallback);
            transition = setTargets(transition, includeTransitioningViews);
        }
        android.app.ActivityTransitionCoordinator.noLayoutSuppressionForVisibilityTransitions(transition);
        return transition;
    }

    protected static android.transition.Transition mergeTransitions(android.transition.Transition transition1, android.transition.Transition transition2) {
        if (transition1 == null) {
            return transition2;
        } else
            if (transition2 == null) {
                return transition1;
            } else {
                android.transition.TransitionSet transitionSet = new android.transition.TransitionSet();
                transitionSet.addTransition(transition1);
                transitionSet.addTransition(transition2);
                return transitionSet;
            }

    }

    protected android.util.ArrayMap<java.lang.String, android.view.View> mapSharedElements(java.util.ArrayList<java.lang.String> accepted, java.util.ArrayList<android.view.View> localViews) {
        android.util.ArrayMap<java.lang.String, android.view.View> sharedElements = new android.util.ArrayMap<java.lang.String, android.view.View>();
        if (accepted != null) {
            for (int i = 0; i < accepted.size(); i++) {
                sharedElements.put(accepted.get(i), localViews.get(i));
            }
        } else {
            android.view.ViewGroup decorView = getDecor();
            if (decorView != null) {
                decorView.findNamedViews(sharedElements);
            }
        }
        return sharedElements;
    }

    protected void setResultReceiver(android.os.ResultReceiver resultReceiver) {
        mResultReceiver = resultReceiver;
    }

    protected abstract android.transition.Transition getViewsTransition();

    private void setSharedElementState(android.view.View view, java.lang.String name, android.os.Bundle transitionArgs, android.graphics.Matrix tempMatrix, android.graphics.RectF tempRect, int[] decorLoc) {
        android.os.Bundle sharedElementBundle = transitionArgs.getBundle(name);
        if (sharedElementBundle == null) {
            return;
        }
        if (view instanceof android.widget.ImageView) {
            int scaleTypeInt = sharedElementBundle.getInt(android.app.ActivityTransitionCoordinator.KEY_SCALE_TYPE, -1);
            if (scaleTypeInt >= 0) {
                android.widget.ImageView imageView = ((android.widget.ImageView) (view));
                android.widget.ImageView.ScaleType scaleType = android.app.ActivityTransitionCoordinator.SCALE_TYPE_VALUES[scaleTypeInt];
                imageView.setScaleType(scaleType);
                if (scaleType == android.widget.ImageView.ScaleType.MATRIX) {
                    float[] matrixValues = sharedElementBundle.getFloatArray(android.app.ActivityTransitionCoordinator.KEY_IMAGE_MATRIX);
                    tempMatrix.setValues(matrixValues);
                    imageView.setImageMatrix(tempMatrix);
                }
            }
        }
        float z = sharedElementBundle.getFloat(android.app.ActivityTransitionCoordinator.KEY_TRANSLATION_Z);
        view.setTranslationZ(z);
        float elevation = sharedElementBundle.getFloat(android.app.ActivityTransitionCoordinator.KEY_ELEVATION);
        view.setElevation(elevation);
        float left = sharedElementBundle.getFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_LEFT);
        float top = sharedElementBundle.getFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_TOP);
        float right = sharedElementBundle.getFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_RIGHT);
        float bottom = sharedElementBundle.getFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_BOTTOM);
        if (decorLoc != null) {
            left -= decorLoc[0];
            top -= decorLoc[1];
            right -= decorLoc[0];
            bottom -= decorLoc[1];
        } else {
            // Find the location in the view's parent
            getSharedElementParentMatrix(view, tempMatrix);
            tempRect.set(left, top, right, bottom);
            tempMatrix.mapRect(tempRect);
            float leftInParent = tempRect.left;
            float topInParent = tempRect.top;
            // Find the size of the view
            view.getInverseMatrix().mapRect(tempRect);
            float width = tempRect.width();
            float height = tempRect.height();
            // Now determine the offset due to view transform:
            view.setLeft(0);
            view.setTop(0);
            view.setRight(java.lang.Math.round(width));
            view.setBottom(java.lang.Math.round(height));
            tempRect.set(0, 0, width, height);
            view.getMatrix().mapRect(tempRect);
            left = leftInParent - tempRect.left;
            top = topInParent - tempRect.top;
            right = left + width;
            bottom = top + height;
        }
        int x = java.lang.Math.round(left);
        int y = java.lang.Math.round(top);
        int width = java.lang.Math.round(right) - x;
        int height = java.lang.Math.round(bottom) - y;
        int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(width, android.view.View.MeasureSpec.EXACTLY);
        int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(height, android.view.View.MeasureSpec.EXACTLY);
        view.measure(widthSpec, heightSpec);
        view.layout(x, y, x + width, y + height);
    }

    private void setSharedElementMatrices() {
        int numSharedElements = mSharedElements.size();
        if (numSharedElements > 0) {
            mSharedElementParentMatrices = new java.util.ArrayList<android.graphics.Matrix>(numSharedElements);
        }
        for (int i = 0; i < numSharedElements; i++) {
            android.view.View view = mSharedElements.get(i);
            // Find the location in the view's parent
            android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            parent.transformMatrixToLocal(matrix);
            matrix.postTranslate(parent.getScrollX(), parent.getScrollY());
            mSharedElementParentMatrices.add(matrix);
        }
    }

    private void getSharedElementParentMatrix(android.view.View view, android.graphics.Matrix matrix) {
        final int index = (mSharedElementParentMatrices == null) ? -1 : mSharedElements.indexOf(view);
        if (index < 0) {
            matrix.reset();
            android.view.ViewParent viewParent = view.getParent();
            if (viewParent instanceof android.view.ViewGroup) {
                // Find the location in the view's parent
                android.view.ViewGroup parent = ((android.view.ViewGroup) (viewParent));
                parent.transformMatrixToLocal(matrix);
                matrix.postTranslate(parent.getScrollX(), parent.getScrollY());
            }
        } else {
            // The indices of mSharedElementParentMatrices matches the
            // mSharedElement matrices.
            android.graphics.Matrix parentMatrix = mSharedElementParentMatrices.get(index);
            matrix.set(parentMatrix);
        }
    }

    protected java.util.ArrayList<android.app.ActivityTransitionCoordinator.SharedElementOriginalState> setSharedElementState(android.os.Bundle sharedElementState, final java.util.ArrayList<android.view.View> snapshots) {
        java.util.ArrayList<android.app.ActivityTransitionCoordinator.SharedElementOriginalState> originalImageState = new java.util.ArrayList<android.app.ActivityTransitionCoordinator.SharedElementOriginalState>();
        if (sharedElementState != null) {
            android.graphics.Matrix tempMatrix = new android.graphics.Matrix();
            android.graphics.RectF tempRect = new android.graphics.RectF();
            final int numSharedElements = mSharedElements.size();
            for (int i = 0; i < numSharedElements; i++) {
                android.view.View sharedElement = mSharedElements.get(i);
                java.lang.String name = mSharedElementNames.get(i);
                android.app.ActivityTransitionCoordinator.SharedElementOriginalState originalState = android.app.ActivityTransitionCoordinator.getOldSharedElementState(sharedElement, name, sharedElementState);
                originalImageState.add(originalState);
                setSharedElementState(sharedElement, name, sharedElementState, tempMatrix, tempRect, null);
            }
        }
        if (mListener != null) {
            mListener.onSharedElementStart(mSharedElementNames, mSharedElements, snapshots);
        }
        return originalImageState;
    }

    protected void notifySharedElementEnd(java.util.ArrayList<android.view.View> snapshots) {
        if (mListener != null) {
            mListener.onSharedElementEnd(mSharedElementNames, mSharedElements, snapshots);
        }
    }

    protected void scheduleSetSharedElementEnd(final java.util.ArrayList<android.view.View> snapshots) {
        final android.view.View decorView = getDecor();
        if (decorView != null) {
            decorView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                    notifySharedElementEnd(snapshots);
                    return true;
                }
            });
        }
    }

    private static android.app.ActivityTransitionCoordinator.SharedElementOriginalState getOldSharedElementState(android.view.View view, java.lang.String name, android.os.Bundle transitionArgs) {
        android.app.ActivityTransitionCoordinator.SharedElementOriginalState state = new android.app.ActivityTransitionCoordinator.SharedElementOriginalState();
        state.mLeft = view.getLeft();
        state.mTop = view.getTop();
        state.mRight = view.getRight();
        state.mBottom = view.getBottom();
        state.mMeasuredWidth = view.getMeasuredWidth();
        state.mMeasuredHeight = view.getMeasuredHeight();
        state.mTranslationZ = view.getTranslationZ();
        state.mElevation = view.getElevation();
        if (!(view instanceof android.widget.ImageView)) {
            return state;
        }
        android.os.Bundle bundle = transitionArgs.getBundle(name);
        if (bundle == null) {
            return state;
        }
        int scaleTypeInt = bundle.getInt(android.app.ActivityTransitionCoordinator.KEY_SCALE_TYPE, -1);
        if (scaleTypeInt < 0) {
            return state;
        }
        android.widget.ImageView imageView = ((android.widget.ImageView) (view));
        state.mScaleType = imageView.getScaleType();
        if (state.mScaleType == android.widget.ImageView.ScaleType.MATRIX) {
            state.mMatrix = new android.graphics.Matrix(imageView.getImageMatrix());
        }
        return state;
    }

    protected java.util.ArrayList<android.view.View> createSnapshots(android.os.Bundle state, java.util.Collection<java.lang.String> names) {
        int numSharedElements = names.size();
        java.util.ArrayList<android.view.View> snapshots = new java.util.ArrayList<android.view.View>(numSharedElements);
        if (numSharedElements == 0) {
            return snapshots;
        }
        android.content.Context context = getWindow().getContext();
        int[] decorLoc = new int[2];
        android.view.ViewGroup decorView = getDecor();
        if (decorView != null) {
            decorView.getLocationOnScreen(decorLoc);
        }
        android.graphics.Matrix tempMatrix = new android.graphics.Matrix();
        for (java.lang.String name : names) {
            android.os.Bundle sharedElementBundle = state.getBundle(name);
            android.view.View snapshot = null;
            if (sharedElementBundle != null) {
                android.os.Parcelable parcelable = sharedElementBundle.getParcelable(android.app.ActivityTransitionCoordinator.KEY_SNAPSHOT);
                if ((parcelable != null) && (mListener != null)) {
                    snapshot = mListener.onCreateSnapshotView(context, parcelable);
                }
                if (snapshot != null) {
                    setSharedElementState(snapshot, name, state, tempMatrix, null, decorLoc);
                }
            }
            // Even null snapshots are added so they remain in the same order as shared elements.
            snapshots.add(snapshot);
        }
        return snapshots;
    }

    protected static void setOriginalSharedElementState(java.util.ArrayList<android.view.View> sharedElements, java.util.ArrayList<android.app.ActivityTransitionCoordinator.SharedElementOriginalState> originalState) {
        for (int i = 0; i < originalState.size(); i++) {
            android.view.View view = sharedElements.get(i);
            android.app.ActivityTransitionCoordinator.SharedElementOriginalState state = originalState.get(i);
            if ((view instanceof android.widget.ImageView) && (state.mScaleType != null)) {
                android.widget.ImageView imageView = ((android.widget.ImageView) (view));
                imageView.setScaleType(state.mScaleType);
                if (state.mScaleType == android.widget.ImageView.ScaleType.MATRIX) {
                    imageView.setImageMatrix(state.mMatrix);
                }
            }
            view.setElevation(state.mElevation);
            view.setTranslationZ(state.mTranslationZ);
            int widthSpec = android.view.View.MeasureSpec.makeMeasureSpec(state.mMeasuredWidth, android.view.View.MeasureSpec.EXACTLY);
            int heightSpec = android.view.View.MeasureSpec.makeMeasureSpec(state.mMeasuredHeight, android.view.View.MeasureSpec.EXACTLY);
            view.measure(widthSpec, heightSpec);
            view.layout(state.mLeft, state.mTop, state.mRight, state.mBottom);
        }
    }

    protected android.os.Bundle captureSharedElementState() {
        android.os.Bundle bundle = new android.os.Bundle();
        android.graphics.RectF tempBounds = new android.graphics.RectF();
        android.graphics.Matrix tempMatrix = new android.graphics.Matrix();
        for (int i = 0; i < mSharedElements.size(); i++) {
            android.view.View sharedElement = mSharedElements.get(i);
            java.lang.String name = mSharedElementNames.get(i);
            captureSharedElementState(sharedElement, name, bundle, tempMatrix, tempBounds);
        }
        return bundle;
    }

    protected void clearState() {
        // Clear the state so that we can't hold any references accidentally and leak memory.
        mWindow = null;
        mSharedElements.clear();
        mTransitioningViews = null;
        mOriginalAlphas.clear();
        mResultReceiver = null;
        mPendingTransition = null;
        mListener = null;
        mSharedElementParentMatrices = null;
    }

    protected long getFadeDuration() {
        return getWindow().getTransitionBackgroundFadeDuration();
    }

    protected void hideViews(java.util.ArrayList<android.view.View> views) {
        int count = views.size();
        for (int i = 0; i < count; i++) {
            android.view.View view = views.get(i);
            if (!mOriginalAlphas.containsKey(view)) {
                mOriginalAlphas.put(view, view.getAlpha());
            }
            view.setAlpha(0.0F);
        }
    }

    protected void showViews(java.util.ArrayList<android.view.View> views, boolean setTransitionAlpha) {
        int count = views.size();
        for (int i = 0; i < count; i++) {
            showView(views.get(i), setTransitionAlpha);
        }
    }

    private void showView(android.view.View view, boolean setTransitionAlpha) {
        java.lang.Float alpha = mOriginalAlphas.remove(view);
        if (alpha != null) {
            view.setAlpha(alpha);
        }
        if (setTransitionAlpha) {
            view.setTransitionAlpha(1.0F);
        }
    }

    /**
     * Captures placement information for Views with a shared element name for
     * Activity Transitions.
     *
     * @param view
     * 		The View to capture the placement information for.
     * @param name
     * 		The shared element name in the target Activity to apply the placement
     * 		information for.
     * @param transitionArgs
     * 		Bundle to store shared element placement information.
     * @param tempBounds
     * 		A temporary Rect for capturing the current location of views.
     */
    protected void captureSharedElementState(android.view.View view, java.lang.String name, android.os.Bundle transitionArgs, android.graphics.Matrix tempMatrix, android.graphics.RectF tempBounds) {
        android.os.Bundle sharedElementBundle = new android.os.Bundle();
        tempMatrix.reset();
        view.transformMatrixToGlobal(tempMatrix);
        tempBounds.set(0, 0, view.getWidth(), view.getHeight());
        tempMatrix.mapRect(tempBounds);
        sharedElementBundle.putFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_LEFT, tempBounds.left);
        sharedElementBundle.putFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_RIGHT, tempBounds.right);
        sharedElementBundle.putFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_TOP, tempBounds.top);
        sharedElementBundle.putFloat(android.app.ActivityTransitionCoordinator.KEY_SCREEN_BOTTOM, tempBounds.bottom);
        sharedElementBundle.putFloat(android.app.ActivityTransitionCoordinator.KEY_TRANSLATION_Z, view.getTranslationZ());
        sharedElementBundle.putFloat(android.app.ActivityTransitionCoordinator.KEY_ELEVATION, view.getElevation());
        android.os.Parcelable bitmap = null;
        if (mListener != null) {
            bitmap = mListener.onCaptureSharedElementSnapshot(view, tempMatrix, tempBounds);
        }
        if (bitmap != null) {
            sharedElementBundle.putParcelable(android.app.ActivityTransitionCoordinator.KEY_SNAPSHOT, bitmap);
        }
        if (view instanceof android.widget.ImageView) {
            android.widget.ImageView imageView = ((android.widget.ImageView) (view));
            int scaleTypeInt = android.app.ActivityTransitionCoordinator.scaleTypeToInt(imageView.getScaleType());
            sharedElementBundle.putInt(android.app.ActivityTransitionCoordinator.KEY_SCALE_TYPE, scaleTypeInt);
            if (imageView.getScaleType() == android.widget.ImageView.ScaleType.MATRIX) {
                float[] matrix = new float[9];
                imageView.getImageMatrix().getValues(matrix);
                sharedElementBundle.putFloatArray(android.app.ActivityTransitionCoordinator.KEY_IMAGE_MATRIX, matrix);
            }
        }
        transitionArgs.putBundle(name, sharedElementBundle);
    }

    protected void startTransition(java.lang.Runnable runnable) {
        if (mIsStartingTransition) {
            mPendingTransition = runnable;
        } else {
            mIsStartingTransition = true;
            runnable.run();
        }
    }

    protected void transitionStarted() {
        mIsStartingTransition = false;
    }

    /**
     * Cancels any pending transitions and returns true if there is a transition is in
     * the middle of starting.
     */
    protected boolean cancelPendingTransitions() {
        mPendingTransition = null;
        return mIsStartingTransition;
    }

    protected void moveSharedElementsToOverlay() {
        if ((mWindow == null) || (!mWindow.getSharedElementsUseOverlay())) {
            return;
        }
        setSharedElementMatrices();
        int numSharedElements = mSharedElements.size();
        android.view.ViewGroup decor = getDecor();
        if (decor != null) {
            boolean moveWithParent = moveSharedElementWithParent();
            android.graphics.Matrix tempMatrix = new android.graphics.Matrix();
            for (int i = 0; i < numSharedElements; i++) {
                android.view.View view = mSharedElements.get(i);
                tempMatrix.reset();
                mSharedElementParentMatrices.get(i).invert(tempMatrix);
                android.view.GhostView.addGhost(view, decor, tempMatrix);
                android.view.ViewGroup parent = ((android.view.ViewGroup) (view.getParent()));
                if (moveWithParent && (!android.app.ActivityTransitionCoordinator.isInTransitionGroup(parent, decor))) {
                    android.app.ActivityTransitionCoordinator.GhostViewListeners listener = new android.app.ActivityTransitionCoordinator.GhostViewListeners(view, parent, decor);
                    parent.getViewTreeObserver().addOnPreDrawListener(listener);
                    mGhostViewListeners.add(listener);
                }
            }
        }
    }

    protected boolean moveSharedElementWithParent() {
        return true;
    }

    public static boolean isInTransitionGroup(android.view.ViewParent viewParent, android.view.ViewGroup decor) {
        if ((viewParent == decor) || (!(viewParent instanceof android.view.ViewGroup))) {
            return false;
        }
        android.view.ViewGroup parent = ((android.view.ViewGroup) (viewParent));
        if (parent.isTransitionGroup()) {
            return true;
        } else {
            return android.app.ActivityTransitionCoordinator.isInTransitionGroup(parent.getParent(), decor);
        }
    }

    protected void moveSharedElementsFromOverlay() {
        int numListeners = mGhostViewListeners.size();
        for (int i = 0; i < numListeners; i++) {
            android.app.ActivityTransitionCoordinator.GhostViewListeners listener = mGhostViewListeners.get(i);
            android.view.ViewGroup parent = ((android.view.ViewGroup) (listener.getView().getParent()));
            parent.getViewTreeObserver().removeOnPreDrawListener(listener);
        }
        mGhostViewListeners.clear();
        if ((mWindow == null) || (!mWindow.getSharedElementsUseOverlay())) {
            return;
        }
        android.view.ViewGroup decor = getDecor();
        if (decor != null) {
            android.view.ViewGroupOverlay overlay = decor.getOverlay();
            int count = mSharedElements.size();
            for (int i = 0; i < count; i++) {
                android.view.View sharedElement = mSharedElements.get(i);
                android.view.GhostView.removeGhost(sharedElement);
            }
        }
    }

    protected void setGhostVisibility(int visibility) {
        int numSharedElements = mSharedElements.size();
        for (int i = 0; i < numSharedElements; i++) {
            android.view.GhostView ghostView = android.view.GhostView.getGhost(mSharedElements.get(i));
            if (ghostView != null) {
                ghostView.setVisibility(visibility);
            }
        }
    }

    protected void scheduleGhostVisibilityChange(final int visibility) {
        final android.view.View decorView = getDecor();
        if (decorView != null) {
            decorView.getViewTreeObserver().addOnPreDrawListener(new android.view.ViewTreeObserver.OnPreDrawListener() {
                @java.lang.Override
                public boolean onPreDraw() {
                    decorView.getViewTreeObserver().removeOnPreDrawListener(this);
                    setGhostVisibility(visibility);
                    return true;
                }
            });
        }
    }

    protected boolean isViewsTransitionComplete() {
        return mViewsTransitionComplete;
    }

    protected void viewsTransitionComplete() {
        mViewsTransitionComplete = true;
        startInputWhenTransitionsComplete();
    }

    protected void sharedElementTransitionComplete() {
        mSharedElementTransitionComplete = true;
        startInputWhenTransitionsComplete();
    }

    private void startInputWhenTransitionsComplete() {
        if (mViewsTransitionComplete && mSharedElementTransitionComplete) {
            final android.view.View decor = getDecor();
            if (decor != null) {
                final android.view.ViewRootImpl viewRoot = decor.getViewRootImpl();
                if (viewRoot != null) {
                    viewRoot.setPausedForTransition(false);
                }
            }
            onTransitionsComplete();
        }
    }

    protected void pauseInput() {
        final android.view.View decor = getDecor();
        final android.view.ViewRootImpl viewRoot = (decor == null) ? null : decor.getViewRootImpl();
        if (viewRoot != null) {
            viewRoot.setPausedForTransition(true);
        }
    }

    protected void onTransitionsComplete() {
    }

    protected class ContinueTransitionListener extends android.transition.Transition.TransitionListenerAdapter {
        @java.lang.Override
        public void onTransitionStart(android.transition.Transition transition) {
            mIsStartingTransition = false;
            java.lang.Runnable pending = mPendingTransition;
            mPendingTransition = null;
            if (pending != null) {
                startTransition(pending);
            }
        }
    }

    private static int scaleTypeToInt(android.widget.ImageView.ScaleType scaleType) {
        for (int i = 0; i < android.app.ActivityTransitionCoordinator.SCALE_TYPE_VALUES.length; i++) {
            if (scaleType == android.app.ActivityTransitionCoordinator.SCALE_TYPE_VALUES[i]) {
                return i;
            }
        }
        return -1;
    }

    protected void setTransitioningViewsVisiblity(int visiblity, boolean invalidate) {
        final int numElements = (mTransitioningViews == null) ? 0 : mTransitioningViews.size();
        for (int i = 0; i < numElements; i++) {
            final android.view.View view = mTransitioningViews.get(i);
            view.setTransitionVisibility(visiblity);
            if (invalidate) {
                view.invalidate();
            }
        }
    }

    /**
     * Blocks suppressLayout from Visibility transitions. It is ok to suppress the layout,
     * but we don't want to force the layout when suppressLayout becomes false. This leads
     * to visual glitches.
     */
    private static void noLayoutSuppressionForVisibilityTransitions(android.transition.Transition transition) {
        if (transition instanceof android.transition.Visibility) {
            final android.transition.Visibility visibility = ((android.transition.Visibility) (transition));
            visibility.setSuppressLayout(false);
        } else
            if (transition instanceof android.transition.TransitionSet) {
                final android.transition.TransitionSet set = ((android.transition.TransitionSet) (transition));
                final int count = set.getTransitionCount();
                for (int i = 0; i < count; i++) {
                    android.app.ActivityTransitionCoordinator.noLayoutSuppressionForVisibilityTransitions(set.getTransitionAt(i));
                }
            }

    }

    private static class FixedEpicenterCallback extends android.transition.Transition.EpicenterCallback {
        private android.graphics.Rect mEpicenter;

        public void setEpicenter(android.graphics.Rect epicenter) {
            mEpicenter = epicenter;
        }

        @java.lang.Override
        public android.graphics.Rect onGetEpicenter(android.transition.Transition transition) {
            return mEpicenter;
        }
    }

    private static class GhostViewListeners implements android.view.ViewTreeObserver.OnPreDrawListener {
        private android.view.View mView;

        private android.view.ViewGroup mDecor;

        private android.view.View mParent;

        private android.graphics.Matrix mMatrix = new android.graphics.Matrix();

        public GhostViewListeners(android.view.View view, android.view.View parent, android.view.ViewGroup decor) {
            mView = view;
            mParent = parent;
            mDecor = decor;
        }

        public android.view.View getView() {
            return mView;
        }

        @java.lang.Override
        public boolean onPreDraw() {
            android.view.GhostView ghostView = android.view.GhostView.getGhost(mView);
            if (ghostView == null) {
                mParent.getViewTreeObserver().removeOnPreDrawListener(this);
            } else {
                android.view.GhostView.calculateMatrix(mView, mDecor, mMatrix);
                ghostView.setMatrix(mMatrix);
            }
            return true;
        }
    }

    static class SharedElementOriginalState {
        int mLeft;

        int mTop;

        int mRight;

        int mBottom;

        int mMeasuredWidth;

        int mMeasuredHeight;

        android.widget.ImageView.ScaleType mScaleType;

        android.graphics.Matrix mMatrix;

        float mTranslationZ;

        float mElevation;
    }
}

