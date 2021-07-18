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


/**
 * Helper class for building an options Bundle that can be used with
 * {@link android.content.Context#startActivity(android.content.Intent, android.os.Bundle)
 * Context.startActivity(Intent, Bundle)} and related methods.
 */
public class ActivityOptions {
    private static final java.lang.String TAG = "ActivityOptions";

    /**
     * A long in the extras delivered by {@link #requestUsageTimeReport} that contains
     * the total time (in ms) the user spent in the app flow.
     */
    public static final java.lang.String EXTRA_USAGE_TIME_REPORT = "android.activity.usage_time";

    /**
     * A Bundle in the extras delivered by {@link #requestUsageTimeReport} that contains
     * detailed information about the time spent in each package associated with the app;
     * each key is a package name, whose value is a long containing the time (in ms).
     */
    public static final java.lang.String EXTRA_USAGE_TIME_REPORT_PACKAGES = "android.usage_time_packages";

    /**
     * The package name that created the options.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_PACKAGE_NAME = "android:activity.packageName";

    /**
     * The bounds (window size) that the activity should be launched in. Set to null explicitly for
     * full screen. If the key is not found, previous bounds will be preserved.
     * NOTE: This value is ignored on devices that don't have
     * {@link android.content.pm.PackageManager#FEATURE_FREEFORM_WINDOW_MANAGEMENT} or
     * {@link android.content.pm.PackageManager#FEATURE_PICTURE_IN_PICTURE} enabled.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_LAUNCH_BOUNDS = "android:activity.launchBounds";

    /**
     * Type of animation that arguments specify.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_TYPE = "android:activity.animType";

    /**
     * Custom enter animation resource ID.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_ENTER_RES_ID = "android:activity.animEnterRes";

    /**
     * Custom exit animation resource ID.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_EXIT_RES_ID = "android:activity.animExitRes";

    /**
     * Custom in-place animation resource ID.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_IN_PLACE_RES_ID = "android:activity.animInPlaceRes";

    /**
     * Bitmap for thumbnail animation.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_THUMBNAIL = "android:activity.animThumbnail";

    /**
     * Start X position of thumbnail animation.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_START_X = "android:activity.animStartX";

    /**
     * Start Y position of thumbnail animation.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_START_Y = "android:activity.animStartY";

    /**
     * Initial width of the animation.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_WIDTH = "android:activity.animWidth";

    /**
     * Initial height of the animation.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_HEIGHT = "android:activity.animHeight";

    /**
     * Callback for when animation is started.
     *
     * @unknown 
     */
    public static final java.lang.String KEY_ANIM_START_LISTENER = "android:activity.animStartListener";

    /**
     * Callback for when the last frame of the animation is played.
     *
     * @unknown 
     */
    private static final java.lang.String KEY_ANIMATION_FINISHED_LISTENER = "android:activity.animationFinishedListener";

    /**
     * Descriptions of app transition animations to be played during the activity launch.
     */
    private static final java.lang.String KEY_ANIM_SPECS = "android:activity.animSpecs";

    /**
     * The stack id the activity should be launched into.
     *
     * @unknown 
     */
    private static final java.lang.String KEY_LAUNCH_STACK_ID = "android.activity.launchStackId";

    /**
     * The task id the activity should be launched into.
     *
     * @unknown 
     */
    private static final java.lang.String KEY_LAUNCH_TASK_ID = "android.activity.launchTaskId";

    /**
     * See {@link #setTaskOverlay}.
     *
     * @unknown 
     */
    private static final java.lang.String KEY_TASK_OVERLAY = "android.activity.taskOverlay";

    /**
     * Where the docked stack should be positioned.
     *
     * @unknown 
     */
    private static final java.lang.String KEY_DOCK_CREATE_MODE = "android:activity.dockCreateMode";

    /**
     * For Activity transitions, the calling Activity's TransitionListener used to
     * notify the called Activity when the shared element and the exit transitions
     * complete.
     */
    private static final java.lang.String KEY_TRANSITION_COMPLETE_LISTENER = "android:activity.transitionCompleteListener";

    private static final java.lang.String KEY_TRANSITION_IS_RETURNING = "android:activity.transitionIsReturning";

    private static final java.lang.String KEY_TRANSITION_SHARED_ELEMENTS = "android:activity.sharedElementNames";

    private static final java.lang.String KEY_RESULT_DATA = "android:activity.resultData";

    private static final java.lang.String KEY_RESULT_CODE = "android:activity.resultCode";

    private static final java.lang.String KEY_EXIT_COORDINATOR_INDEX = "android:activity.exitCoordinatorIndex";

    private static final java.lang.String KEY_USAGE_TIME_REPORT = "android:activity.usageTimeReport";

    private static final java.lang.String KEY_ROTATION_ANIMATION_HINT = "android:activity.rotationAnimationHint";

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_NONE = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_CUSTOM = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_SCALE_UP = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_THUMBNAIL_SCALE_UP = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_THUMBNAIL_SCALE_DOWN = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_SCENE_TRANSITION = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_DEFAULT = 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_LAUNCH_TASK_BEHIND = 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_THUMBNAIL_ASPECT_SCALE_UP = 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_THUMBNAIL_ASPECT_SCALE_DOWN = 9;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_CUSTOM_IN_PLACE = 10;

    /**
     *
     *
     * @unknown 
     */
    public static final int ANIM_CLIP_REVEAL = 11;

    private java.lang.String mPackageName;

    private android.graphics.Rect mLaunchBounds;

    private int mAnimationType = android.app.ActivityOptions.ANIM_NONE;

    private int mCustomEnterResId;

    private int mCustomExitResId;

    private int mCustomInPlaceResId;

    private android.graphics.Bitmap mThumbnail;

    private int mStartX;

    private int mStartY;

    private int mWidth;

    private int mHeight;

    private android.os.IRemoteCallback mAnimationStartedListener;

    private android.os.IRemoteCallback mAnimationFinishedListener;

    private android.os.ResultReceiver mTransitionReceiver;

    private boolean mIsReturning;

    private java.util.ArrayList<java.lang.String> mSharedElementNames;

    private android.content.Intent mResultData;

    private int mResultCode;

    private int mExitCoordinatorIndex;

    private android.app.PendingIntent mUsageTimeReport;

    private int mLaunchStackId = android.app.ActivityManager.StackId.INVALID_STACK_ID;

    private int mLaunchTaskId = -1;

    private int mDockCreateMode = android.app.ActivityManager.DOCKED_STACK_CREATE_MODE_TOP_OR_LEFT;

    private boolean mTaskOverlay;

    private android.view.AppTransitionAnimationSpec[] mAnimSpecs;

    private int mRotationAnimationHint = -1;

    /**
     * Create an ActivityOptions specifying a custom animation to run when
     * the activity is displayed.
     *
     * @param context
     * 		Who is defining this.  This is the application that the
     * 		animation resources will be loaded from.
     * @param enterResId
     * 		A resource ID of the animation resource to use for
     * 		the incoming activity.  Use 0 for no animation.
     * @param exitResId
     * 		A resource ID of the animation resource to use for
     * 		the outgoing activity.  Use 0 for no animation.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     */
    public static android.app.ActivityOptions makeCustomAnimation(android.content.Context context, int enterResId, int exitResId) {
        return android.app.ActivityOptions.makeCustomAnimation(context, enterResId, exitResId, null, null);
    }

    /**
     * Create an ActivityOptions specifying a custom animation to run when
     * the activity is displayed.
     *
     * @param context
     * 		Who is defining this.  This is the application that the
     * 		animation resources will be loaded from.
     * @param enterResId
     * 		A resource ID of the animation resource to use for
     * 		the incoming activity.  Use 0 for no animation.
     * @param exitResId
     * 		A resource ID of the animation resource to use for
     * 		the outgoing activity.  Use 0 for no animation.
     * @param handler
     * 		If <var>listener</var> is non-null this must be a valid
     * 		Handler on which to dispatch the callback; otherwise it should be null.
     * @param listener
     * 		Optional OnAnimationStartedListener to find out when the
     * 		requested animation has started running.  If for some reason the animation
     * 		is not executed, the callback will happen immediately.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     * @unknown 
     */
    public static android.app.ActivityOptions makeCustomAnimation(android.content.Context context, int enterResId, int exitResId, android.os.Handler handler, android.app.ActivityOptions.OnAnimationStartedListener listener) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mPackageName = context.getPackageName();
        opts.mAnimationType = android.app.ActivityOptions.ANIM_CUSTOM;
        opts.mCustomEnterResId = enterResId;
        opts.mCustomExitResId = exitResId;
        opts.setOnAnimationStartedListener(handler, listener);
        return opts;
    }

    /**
     * Creates an ActivityOptions specifying a custom animation to run in place on an existing
     * activity.
     *
     * @param context
     * 		Who is defining this.  This is the application that the
     * 		animation resources will be loaded from.
     * @param animId
     * 		A resource ID of the animation resource to use for
     * 		the incoming activity.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when running an in-place animation.
     * @unknown 
     */
    public static android.app.ActivityOptions makeCustomInPlaceAnimation(android.content.Context context, int animId) {
        if (animId == 0) {
            throw new java.lang.RuntimeException("You must specify a valid animation.");
        }
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mPackageName = context.getPackageName();
        opts.mAnimationType = android.app.ActivityOptions.ANIM_CUSTOM_IN_PLACE;
        opts.mCustomInPlaceResId = animId;
        return opts;
    }

    private void setOnAnimationStartedListener(final android.os.Handler handler, final android.app.ActivityOptions.OnAnimationStartedListener listener) {
        if (listener != null) {
            mAnimationStartedListener = new android.os.IRemoteCallback.Stub() {
                @java.lang.Override
                public void sendResult(android.os.Bundle data) throws android.os.RemoteException {
                    handler.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            listener.onAnimationStarted();
                        }
                    });
                }
            };
        }
    }

    /**
     * Callback for use with {@link ActivityOptions#makeThumbnailScaleUpAnimation}
     * to find out when the given animation has started running.
     *
     * @unknown 
     */
    public interface OnAnimationStartedListener {
        void onAnimationStarted();
    }

    private void setOnAnimationFinishedListener(final android.os.Handler handler, final android.app.ActivityOptions.OnAnimationFinishedListener listener) {
        if (listener != null) {
            mAnimationFinishedListener = new android.os.IRemoteCallback.Stub() {
                @java.lang.Override
                public void sendResult(android.os.Bundle data) throws android.os.RemoteException {
                    handler.post(new java.lang.Runnable() {
                        @java.lang.Override
                        public void run() {
                            listener.onAnimationFinished();
                        }
                    });
                }
            };
        }
    }

    /**
     * Callback for use with {@link ActivityOptions#makeThumbnailAspectScaleDownAnimation}
     * to find out when the given animation has drawn its last frame.
     *
     * @unknown 
     */
    public interface OnAnimationFinishedListener {
        void onAnimationFinished();
    }

    /**
     * Create an ActivityOptions specifying an animation where the new
     * activity is scaled from a small originating area of the screen to
     * its final full representation.
     *
     * <p>If the Intent this is being used with has not set its
     * {@link android.content.Intent#setSourceBounds Intent.setSourceBounds},
     * those bounds will be filled in for you based on the initial
     * bounds passed in here.
     *
     * @param source
     * 		The View that the new activity is animating from.  This
     * 		defines the coordinate space for <var>startX</var> and <var>startY</var>.
     * @param startX
     * 		The x starting location of the new activity, relative to <var>source</var>.
     * @param startY
     * 		The y starting location of the activity, relative to <var>source</var>.
     * @param width
     * 		The initial width of the new activity.
     * @param height
     * 		The initial height of the new activity.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     */
    public static android.app.ActivityOptions makeScaleUpAnimation(android.view.View source, int startX, int startY, int width, int height) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = android.app.ActivityOptions.ANIM_SCALE_UP;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = width;
        opts.mHeight = height;
        return opts;
    }

    /**
     * Create an ActivityOptions specifying an animation where the new
     * activity is revealed from a small originating area of the screen to
     * its final full representation.
     *
     * @param source
     * 		The View that the new activity is animating from.  This
     * 		defines the coordinate space for <var>startX</var> and <var>startY</var>.
     * @param startX
     * 		The x starting location of the new activity, relative to <var>source</var>.
     * @param startY
     * 		The y starting location of the activity, relative to <var>source</var>.
     * @param width
     * 		The initial width of the new activity.
     * @param height
     * 		The initial height of the new activity.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     */
    public static android.app.ActivityOptions makeClipRevealAnimation(android.view.View source, int startX, int startY, int width, int height) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mAnimationType = android.app.ActivityOptions.ANIM_CLIP_REVEAL;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = width;
        opts.mHeight = height;
        return opts;
    }

    /**
     * Create an ActivityOptions specifying an animation where a thumbnail
     * is scaled from a given position to the new activity window that is
     * being started.
     *
     * <p>If the Intent this is being used with has not set its
     * {@link android.content.Intent#setSourceBounds Intent.setSourceBounds},
     * those bounds will be filled in for you based on the initial
     * thumbnail location and size provided here.
     *
     * @param source
     * 		The View that this thumbnail is animating from.  This
     * 		defines the coordinate space for <var>startX</var> and <var>startY</var>.
     * @param thumbnail
     * 		The bitmap that will be shown as the initial thumbnail
     * 		of the animation.
     * @param startX
     * 		The x starting location of the bitmap, relative to <var>source</var>.
     * @param startY
     * 		The y starting location of the bitmap, relative to <var>source</var>.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     */
    public static android.app.ActivityOptions makeThumbnailScaleUpAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY) {
        return android.app.ActivityOptions.makeThumbnailScaleUpAnimation(source, thumbnail, startX, startY, null);
    }

    /**
     * Create an ActivityOptions specifying an animation where a thumbnail
     * is scaled from a given position to the new activity window that is
     * being started.
     *
     * @param source
     * 		The View that this thumbnail is animating from.  This
     * 		defines the coordinate space for <var>startX</var> and <var>startY</var>.
     * @param thumbnail
     * 		The bitmap that will be shown as the initial thumbnail
     * 		of the animation.
     * @param startX
     * 		The x starting location of the bitmap, relative to <var>source</var>.
     * @param startY
     * 		The y starting location of the bitmap, relative to <var>source</var>.
     * @param listener
     * 		Optional OnAnimationStartedListener to find out when the
     * 		requested animation has started running.  If for some reason the animation
     * 		is not executed, the callback will happen immediately.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     * @unknown 
     */
    public static android.app.ActivityOptions makeThumbnailScaleUpAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY, android.app.ActivityOptions.OnAnimationStartedListener listener) {
        return android.app.ActivityOptions.makeThumbnailAnimation(source, thumbnail, startX, startY, listener, true);
    }

    /**
     * Create an ActivityOptions specifying an animation where an activity window
     * is scaled from a given position to a thumbnail at a specified location.
     *
     * @param source
     * 		The View that this thumbnail is animating to.  This
     * 		defines the coordinate space for <var>startX</var> and <var>startY</var>.
     * @param thumbnail
     * 		The bitmap that will be shown as the final thumbnail
     * 		of the animation.
     * @param startX
     * 		The x end location of the bitmap, relative to <var>source</var>.
     * @param startY
     * 		The y end location of the bitmap, relative to <var>source</var>.
     * @param listener
     * 		Optional OnAnimationStartedListener to find out when the
     * 		requested animation has started running.  If for some reason the animation
     * 		is not executed, the callback will happen immediately.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     * @unknown 
     */
    public static android.app.ActivityOptions makeThumbnailScaleDownAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY, android.app.ActivityOptions.OnAnimationStartedListener listener) {
        return android.app.ActivityOptions.makeThumbnailAnimation(source, thumbnail, startX, startY, listener, false);
    }

    private static android.app.ActivityOptions makeThumbnailAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY, android.app.ActivityOptions.OnAnimationStartedListener listener, boolean scaleUp) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = (scaleUp) ? android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_UP : android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_DOWN;
        opts.mThumbnail = thumbnail;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.setOnAnimationStartedListener(source.getHandler(), listener);
        return opts;
    }

    /**
     * Create an ActivityOptions specifying an animation where the new activity
     * window and a thumbnail is aspect-scaled to a new location.
     *
     * @param source
     * 		The View that this thumbnail is animating from.  This
     * 		defines the coordinate space for <var>startX</var> and <var>startY</var>.
     * @param thumbnail
     * 		The bitmap that will be shown as the initial thumbnail
     * 		of the animation.
     * @param startX
     * 		The x starting location of the bitmap, relative to <var>source</var>.
     * @param startY
     * 		The y starting location of the bitmap, relative to <var>source</var>.
     * @param handler
     * 		If <var>listener</var> is non-null this must be a valid
     * 		Handler on which to dispatch the callback; otherwise it should be null.
     * @param listener
     * 		Optional OnAnimationStartedListener to find out when the
     * 		requested animation has started running.  If for some reason the animation
     * 		is not executed, the callback will happen immediately.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     * @unknown 
     */
    public static android.app.ActivityOptions makeThumbnailAspectScaleUpAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, android.os.Handler handler, android.app.ActivityOptions.OnAnimationStartedListener listener) {
        return android.app.ActivityOptions.makeAspectScaledThumbnailAnimation(source, thumbnail, startX, startY, targetWidth, targetHeight, handler, listener, true);
    }

    /**
     * Create an ActivityOptions specifying an animation where the new activity
     * window and a thumbnail is aspect-scaled to a new location.
     *
     * @param source
     * 		The View that this thumbnail is animating to.  This
     * 		defines the coordinate space for <var>startX</var> and <var>startY</var>.
     * @param thumbnail
     * 		The bitmap that will be shown as the final thumbnail
     * 		of the animation.
     * @param startX
     * 		The x end location of the bitmap, relative to <var>source</var>.
     * @param startY
     * 		The y end location of the bitmap, relative to <var>source</var>.
     * @param handler
     * 		If <var>listener</var> is non-null this must be a valid
     * 		Handler on which to dispatch the callback; otherwise it should be null.
     * @param listener
     * 		Optional OnAnimationStartedListener to find out when the
     * 		requested animation has started running.  If for some reason the animation
     * 		is not executed, the callback will happen immediately.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     * @unknown 
     */
    public static android.app.ActivityOptions makeThumbnailAspectScaleDownAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, android.os.Handler handler, android.app.ActivityOptions.OnAnimationStartedListener listener) {
        return android.app.ActivityOptions.makeAspectScaledThumbnailAnimation(source, thumbnail, startX, startY, targetWidth, targetHeight, handler, listener, false);
    }

    private static android.app.ActivityOptions makeAspectScaledThumbnailAnimation(android.view.View source, android.graphics.Bitmap thumbnail, int startX, int startY, int targetWidth, int targetHeight, android.os.Handler handler, android.app.ActivityOptions.OnAnimationStartedListener listener, boolean scaleUp) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = (scaleUp) ? android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_UP : android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_DOWN;
        opts.mThumbnail = thumbnail;
        int[] pts = new int[2];
        source.getLocationOnScreen(pts);
        opts.mStartX = pts[0] + startX;
        opts.mStartY = pts[1] + startY;
        opts.mWidth = targetWidth;
        opts.mHeight = targetHeight;
        opts.setOnAnimationStartedListener(handler, listener);
        return opts;
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.app.ActivityOptions makeThumbnailAspectScaleDownAnimation(android.view.View source, android.view.AppTransitionAnimationSpec[] specs, android.os.Handler handler, android.app.ActivityOptions.OnAnimationStartedListener onAnimationStartedListener, android.app.ActivityOptions.OnAnimationFinishedListener onAnimationFinishedListener) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mPackageName = source.getContext().getPackageName();
        opts.mAnimationType = android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_DOWN;
        opts.mAnimSpecs = specs;
        opts.setOnAnimationStartedListener(handler, onAnimationStartedListener);
        opts.setOnAnimationFinishedListener(handler, onAnimationFinishedListener);
        return opts;
    }

    /**
     * Create an ActivityOptions to transition between Activities using cross-Activity scene
     * animations. This method carries the position of one shared element to the started Activity.
     * The position of <code>sharedElement</code> will be used as the epicenter for the
     * exit Transition. The position of the shared element in the launched Activity will be the
     * epicenter of its entering Transition.
     *
     * <p>This requires {@link android.view.Window#FEATURE_ACTIVITY_TRANSITIONS} to be
     * enabled on the calling Activity to cause an exit transition. The same must be in
     * the called Activity to get an entering transition.</p>
     *
     * @param activity
     * 		The Activity whose window contains the shared elements.
     * @param sharedElement
     * 		The View to transition to the started Activity.
     * @param sharedElementName
     * 		The shared element name as used in the target Activity. This
     * 		must not be null.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     * @see android.transition.Transition#setEpicenterCallback(
    android.transition.Transition.EpicenterCallback)
     */
    public static android.app.ActivityOptions makeSceneTransitionAnimation(android.app.Activity activity, android.view.View sharedElement, java.lang.String sharedElementName) {
        return android.app.ActivityOptions.makeSceneTransitionAnimation(activity, android.util.Pair.create(sharedElement, sharedElementName));
    }

    /**
     * Create an ActivityOptions to transition between Activities using cross-Activity scene
     * animations. This method carries the position of multiple shared elements to the started
     * Activity. The position of the first element in sharedElements
     * will be used as the epicenter for the exit Transition. The position of the associated
     * shared element in the launched Activity will be the epicenter of its entering Transition.
     *
     * <p>This requires {@link android.view.Window#FEATURE_ACTIVITY_TRANSITIONS} to be
     * enabled on the calling Activity to cause an exit transition. The same must be in
     * the called Activity to get an entering transition.</p>
     *
     * @param activity
     * 		The Activity whose window contains the shared elements.
     * @param sharedElements
     * 		The names of the shared elements to transfer to the called
     * 		Activity and their associated Views. The Views must each have
     * 		a unique shared element name.
     * @return Returns a new ActivityOptions object that you can use to
    supply these options as the options Bundle when starting an activity.
     * @see android.transition.Transition#setEpicenterCallback(
    android.transition.Transition.EpicenterCallback)
     */
    @java.lang.SafeVarargs
    public static android.app.ActivityOptions makeSceneTransitionAnimation(android.app.Activity activity, android.util.Pair<android.view.View, java.lang.String>... sharedElements) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        android.app.ActivityOptions.makeSceneTransitionAnimation(activity, activity.getWindow(), opts, activity.mExitTransitionListener, sharedElements);
        return opts;
    }

    /**
     * Call this immediately prior to startActivity to begin a shared element transition
     * from a non-Activity. The window must support Window.FEATURE_ACTIVITY_TRANSITIONS.
     * The exit transition will start immediately and the shared element transition will
     * start once the launched Activity's shared element is ready.
     * <p>
     * When all transitions have completed and the shared element has been transfered,
     * the window's decor View will have its visibility set to View.GONE.
     *
     * @unknown 
     */
    @java.lang.SafeVarargs
    public static android.app.ActivityOptions startSharedElementAnimation(android.view.Window window, android.util.Pair<android.view.View, java.lang.String>... sharedElements) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        final android.view.View decorView = window.getDecorView();
        if (decorView == null) {
            return opts;
        }
        final android.app.ExitTransitionCoordinator exit = android.app.ActivityOptions.makeSceneTransitionAnimation(null, window, opts, null, sharedElements);
        if (exit != null) {
            android.app.ActivityOptions.HideWindowListener listener = new android.app.ActivityOptions.HideWindowListener(window, exit);
            exit.setHideSharedElementsCallback(listener);
            exit.startExit();
        }
        return opts;
    }

    /**
     * This method should be called when the {@link #startSharedElementAnimation(Window, Pair[])}
     * animation must be stopped and the Views reset. This can happen if there was an error
     * from startActivity or a springboard activity and the animation should stop and reset.
     *
     * @unknown 
     */
    public static void stopSharedElementAnimation(android.view.Window window) {
        final android.view.View decorView = window.getDecorView();
        if (decorView == null) {
            return;
        }
        final android.app.ExitTransitionCoordinator exit = ((android.app.ExitTransitionCoordinator) (decorView.getTag(com.android.internal.R.id.cross_task_transition)));
        if (exit != null) {
            exit.cancelPendingTransitions();
            decorView.setTagInternal(com.android.internal.R.id.cross_task_transition, null);
            android.transition.TransitionManager.endTransitions(((android.view.ViewGroup) (decorView)));
            exit.resetViews();
            exit.clearState();
            decorView.setVisibility(android.view.View.VISIBLE);
        }
    }

    static android.app.ExitTransitionCoordinator makeSceneTransitionAnimation(android.app.Activity activity, android.view.Window window, android.app.ActivityOptions opts, android.app.SharedElementCallback callback, android.util.Pair<android.view.View, java.lang.String>[] sharedElements) {
        if (!window.hasFeature(android.view.Window.FEATURE_ACTIVITY_TRANSITIONS)) {
            opts.mAnimationType = android.app.ActivityOptions.ANIM_DEFAULT;
            return null;
        }
        opts.mAnimationType = android.app.ActivityOptions.ANIM_SCENE_TRANSITION;
        java.util.ArrayList<java.lang.String> names = new java.util.ArrayList<java.lang.String>();
        java.util.ArrayList<android.view.View> views = new java.util.ArrayList<android.view.View>();
        if (sharedElements != null) {
            for (int i = 0; i < sharedElements.length; i++) {
                android.util.Pair<android.view.View, java.lang.String> sharedElement = sharedElements[i];
                java.lang.String sharedElementName = sharedElement.second;
                if (sharedElementName == null) {
                    throw new java.lang.IllegalArgumentException("Shared element name must not be null");
                }
                names.add(sharedElementName);
                android.view.View view = sharedElement.first;
                if (view == null) {
                    throw new java.lang.IllegalArgumentException("Shared element must not be null");
                }
                views.add(sharedElement.first);
            }
        }
        android.app.ExitTransitionCoordinator exit = new android.app.ExitTransitionCoordinator(activity, window, callback, names, names, views, false);
        opts.mTransitionReceiver = exit;
        opts.mSharedElementNames = names;
        opts.mIsReturning = activity == null;
        if (activity == null) {
            opts.mExitCoordinatorIndex = -1;
        } else {
            opts.mExitCoordinatorIndex = activity.mActivityTransitionState.addExitTransitionCoordinator(exit);
        }
        return exit;
    }

    /**
     *
     *
     * @unknown 
     */
    static android.app.ActivityOptions makeSceneTransitionAnimation(android.app.Activity activity, android.app.ExitTransitionCoordinator exitCoordinator, java.util.ArrayList<java.lang.String> sharedElementNames, int resultCode, android.content.Intent resultData) {
        android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mAnimationType = android.app.ActivityOptions.ANIM_SCENE_TRANSITION;
        opts.mSharedElementNames = sharedElementNames;
        opts.mTransitionReceiver = exitCoordinator;
        opts.mIsReturning = true;
        opts.mResultCode = resultCode;
        opts.mResultData = resultData;
        opts.mExitCoordinatorIndex = activity.mActivityTransitionState.addExitTransitionCoordinator(exitCoordinator);
        return opts;
    }

    /**
     * If set along with Intent.FLAG_ACTIVITY_NEW_DOCUMENT then the task being launched will not be
     * presented to the user but will instead be only available through the recents task list.
     * In addition, the new task wil be affiliated with the launching activity's task.
     * Affiliated tasks are grouped together in the recents task list.
     *
     * <p>This behavior is not supported for activities with {@link android.R.styleable#AndroidManifestActivity_launchMode launchMode} values of
     * <code>singleInstance</code> or <code>singleTask</code>.
     */
    public static android.app.ActivityOptions makeTaskLaunchBehind() {
        final android.app.ActivityOptions opts = new android.app.ActivityOptions();
        opts.mAnimationType = android.app.ActivityOptions.ANIM_LAUNCH_TASK_BEHIND;
        return opts;
    }

    /**
     * Create a basic ActivityOptions that has no special animation associated with it.
     * Other options can still be set.
     */
    public static android.app.ActivityOptions makeBasic() {
        final android.app.ActivityOptions opts = new android.app.ActivityOptions();
        return opts;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean getLaunchTaskBehind() {
        return mAnimationType == android.app.ActivityOptions.ANIM_LAUNCH_TASK_BEHIND;
    }

    private ActivityOptions() {
    }

    /**
     *
     *
     * @unknown 
     */
    public ActivityOptions(android.os.Bundle opts) {
        // If the remote side sent us bad parcelables, they won't get the
        // results they want, which is their loss.
        opts.setDefusable(true);
        mPackageName = opts.getString(android.app.ActivityOptions.KEY_PACKAGE_NAME);
        try {
            mUsageTimeReport = opts.getParcelable(android.app.ActivityOptions.KEY_USAGE_TIME_REPORT);
        } catch (java.lang.RuntimeException e) {
            android.util.Slog.w(android.app.ActivityOptions.TAG, e);
        }
        mLaunchBounds = opts.getParcelable(android.app.ActivityOptions.KEY_LAUNCH_BOUNDS);
        mAnimationType = opts.getInt(android.app.ActivityOptions.KEY_ANIM_TYPE);
        switch (mAnimationType) {
            case android.app.ActivityOptions.ANIM_CUSTOM :
                mCustomEnterResId = opts.getInt(android.app.ActivityOptions.KEY_ANIM_ENTER_RES_ID, 0);
                mCustomExitResId = opts.getInt(android.app.ActivityOptions.KEY_ANIM_EXIT_RES_ID, 0);
                mAnimationStartedListener = IRemoteCallback.Stub.asInterface(opts.getBinder(android.app.ActivityOptions.KEY_ANIM_START_LISTENER));
                break;
            case android.app.ActivityOptions.ANIM_CUSTOM_IN_PLACE :
                mCustomInPlaceResId = opts.getInt(android.app.ActivityOptions.KEY_ANIM_IN_PLACE_RES_ID, 0);
                break;
            case android.app.ActivityOptions.ANIM_SCALE_UP :
            case android.app.ActivityOptions.ANIM_CLIP_REVEAL :
                mStartX = opts.getInt(android.app.ActivityOptions.KEY_ANIM_START_X, 0);
                mStartY = opts.getInt(android.app.ActivityOptions.KEY_ANIM_START_Y, 0);
                mWidth = opts.getInt(android.app.ActivityOptions.KEY_ANIM_WIDTH, 0);
                mHeight = opts.getInt(android.app.ActivityOptions.KEY_ANIM_HEIGHT, 0);
                break;
            case android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_UP :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_DOWN :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_UP :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_DOWN :
                mThumbnail = ((android.graphics.Bitmap) (opts.getParcelable(android.app.ActivityOptions.KEY_ANIM_THUMBNAIL)));
                mStartX = opts.getInt(android.app.ActivityOptions.KEY_ANIM_START_X, 0);
                mStartY = opts.getInt(android.app.ActivityOptions.KEY_ANIM_START_Y, 0);
                mWidth = opts.getInt(android.app.ActivityOptions.KEY_ANIM_WIDTH, 0);
                mHeight = opts.getInt(android.app.ActivityOptions.KEY_ANIM_HEIGHT, 0);
                mAnimationStartedListener = IRemoteCallback.Stub.asInterface(opts.getBinder(android.app.ActivityOptions.KEY_ANIM_START_LISTENER));
                break;
            case android.app.ActivityOptions.ANIM_SCENE_TRANSITION :
                mTransitionReceiver = opts.getParcelable(android.app.ActivityOptions.KEY_TRANSITION_COMPLETE_LISTENER);
                mIsReturning = opts.getBoolean(android.app.ActivityOptions.KEY_TRANSITION_IS_RETURNING, false);
                mSharedElementNames = opts.getStringArrayList(android.app.ActivityOptions.KEY_TRANSITION_SHARED_ELEMENTS);
                mResultData = opts.getParcelable(android.app.ActivityOptions.KEY_RESULT_DATA);
                mResultCode = opts.getInt(android.app.ActivityOptions.KEY_RESULT_CODE);
                mExitCoordinatorIndex = opts.getInt(android.app.ActivityOptions.KEY_EXIT_COORDINATOR_INDEX);
                break;
        }
        mLaunchStackId = opts.getInt(android.app.ActivityOptions.KEY_LAUNCH_STACK_ID, android.app.ActivityManager.StackId.INVALID_STACK_ID);
        mLaunchTaskId = opts.getInt(android.app.ActivityOptions.KEY_LAUNCH_TASK_ID, -1);
        mTaskOverlay = opts.getBoolean(android.app.ActivityOptions.KEY_TASK_OVERLAY, false);
        mDockCreateMode = opts.getInt(android.app.ActivityOptions.KEY_DOCK_CREATE_MODE, android.app.ActivityManager.DOCKED_STACK_CREATE_MODE_TOP_OR_LEFT);
        if (opts.containsKey(android.app.ActivityOptions.KEY_ANIM_SPECS)) {
            android.os.Parcelable[] specs = opts.getParcelableArray(android.app.ActivityOptions.KEY_ANIM_SPECS);
            mAnimSpecs = new android.view.AppTransitionAnimationSpec[specs.length];
            for (int i = specs.length - 1; i >= 0; i--) {
                mAnimSpecs[i] = ((android.view.AppTransitionAnimationSpec) (specs[i]));
            }
        }
        if (opts.containsKey(android.app.ActivityOptions.KEY_ANIMATION_FINISHED_LISTENER)) {
            mAnimationFinishedListener = IRemoteCallback.Stub.asInterface(opts.getBinder(android.app.ActivityOptions.KEY_ANIMATION_FINISHED_LISTENER));
        }
        mRotationAnimationHint = opts.getInt(android.app.ActivityOptions.KEY_ROTATION_ANIMATION_HINT);
    }

    /**
     * Sets the bounds (window size) that the activity should be launched in.
     * Rect position should be provided in pixels and in screen coordinates.
     * Set to null explicitly for fullscreen.
     * <p>
     * <strong>NOTE:<strong/> This value is ignored on devices that don't have
     * {@link android.content.pm.PackageManager#FEATURE_FREEFORM_WINDOW_MANAGEMENT} or
     * {@link android.content.pm.PackageManager#FEATURE_PICTURE_IN_PICTURE} enabled.
     *
     * @param screenSpacePixelRect
     * 		Launch bounds to use for the activity or null for fullscreen.
     */
    public android.app.ActivityOptions setLaunchBounds(@android.annotation.Nullable
    android.graphics.Rect screenSpacePixelRect) {
        mLaunchBounds = (screenSpacePixelRect != null) ? new android.graphics.Rect(screenSpacePixelRect) : null;
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.lang.String getPackageName() {
        return mPackageName;
    }

    /**
     * Returns the bounds that should be used to launch the activity.
     *
     * @see #setLaunchBounds(Rect)
     * @return Bounds used to launch the activity.
     */
    @android.annotation.Nullable
    public android.graphics.Rect getLaunchBounds() {
        return mLaunchBounds;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getAnimationType() {
        return mAnimationType;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCustomEnterResId() {
        return mCustomEnterResId;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCustomExitResId() {
        return mCustomExitResId;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getCustomInPlaceResId() {
        return mCustomInPlaceResId;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.graphics.Bitmap getThumbnail() {
        return mThumbnail;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getStartX() {
        return mStartX;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getStartY() {
        return mStartY;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getWidth() {
        return mWidth;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getHeight() {
        return mHeight;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.IRemoteCallback getOnAnimationStartListener() {
        return mAnimationStartedListener;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.IRemoteCallback getAnimationFinishedListener() {
        return mAnimationFinishedListener;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getExitCoordinatorKey() {
        return mExitCoordinatorIndex;
    }

    /**
     *
     *
     * @unknown 
     */
    public void abort() {
        if (mAnimationStartedListener != null) {
            try {
                mAnimationStartedListener.sendResult(null);
            } catch (android.os.RemoteException e) {
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isReturning() {
        return mIsReturning;
    }

    /**
     * Returns whether or not the ActivityOptions was created with
     * {@link #startSharedElementAnimation(Window, Pair[])}.
     *
     * @unknown 
     */
    boolean isCrossTask() {
        return mExitCoordinatorIndex < 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public java.util.ArrayList<java.lang.String> getSharedElementNames() {
        return mSharedElementNames;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.os.ResultReceiver getResultReceiver() {
        return mTransitionReceiver;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getResultCode() {
        return mResultCode;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.content.Intent getResultData() {
        return mResultData;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.app.PendingIntent getUsageTimeReport() {
        return mUsageTimeReport;
    }

    /**
     *
     *
     * @unknown 
     */
    public android.view.AppTransitionAnimationSpec[] getAnimSpecs() {
        return mAnimSpecs;
    }

    /**
     *
     *
     * @unknown 
     */
    public static android.app.ActivityOptions fromBundle(android.os.Bundle bOptions) {
        return bOptions != null ? new android.app.ActivityOptions(bOptions) : null;
    }

    /**
     *
     *
     * @unknown 
     */
    public static void abort(android.app.ActivityOptions options) {
        if (options != null) {
            options.abort();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLaunchStackId() {
        return mLaunchStackId;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.TestApi
    public void setLaunchStackId(int launchStackId) {
        mLaunchStackId = launchStackId;
    }

    /**
     * Sets the task the activity will be launched in.
     *
     * @unknown 
     */
    public void setLaunchTaskId(int taskId) {
        mLaunchTaskId = taskId;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getLaunchTaskId() {
        return mLaunchTaskId;
    }

    /**
     * Set's whether the activity launched with this option should be a task overlay. That is the
     * activity will always be the top activity of the task and doesn't cause the task to be moved
     * to the front when it is added.
     *
     * @unknown 
     */
    public void setTaskOverlay(boolean taskOverlay) {
        mTaskOverlay = taskOverlay;
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean getTaskOverlay() {
        return mTaskOverlay;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getDockCreateMode() {
        return mDockCreateMode;
    }

    /**
     *
     *
     * @unknown 
     */
    public void setDockCreateMode(int dockCreateMode) {
        mDockCreateMode = dockCreateMode;
    }

    /**
     * Update the current values in this ActivityOptions from those supplied
     * in <var>otherOptions</var>.  Any values
     * defined in <var>otherOptions</var> replace those in the base options.
     */
    public void update(android.app.ActivityOptions otherOptions) {
        if (otherOptions.mPackageName != null) {
            mPackageName = otherOptions.mPackageName;
        }
        mUsageTimeReport = otherOptions.mUsageTimeReport;
        mTransitionReceiver = null;
        mSharedElementNames = null;
        mIsReturning = false;
        mResultData = null;
        mResultCode = 0;
        mExitCoordinatorIndex = 0;
        mAnimationType = otherOptions.mAnimationType;
        switch (otherOptions.mAnimationType) {
            case android.app.ActivityOptions.ANIM_CUSTOM :
                mCustomEnterResId = otherOptions.mCustomEnterResId;
                mCustomExitResId = otherOptions.mCustomExitResId;
                mThumbnail = null;
                if (mAnimationStartedListener != null) {
                    try {
                        mAnimationStartedListener.sendResult(null);
                    } catch (android.os.RemoteException e) {
                    }
                }
                mAnimationStartedListener = otherOptions.mAnimationStartedListener;
                break;
            case android.app.ActivityOptions.ANIM_CUSTOM_IN_PLACE :
                mCustomInPlaceResId = otherOptions.mCustomInPlaceResId;
                break;
            case android.app.ActivityOptions.ANIM_SCALE_UP :
                mStartX = otherOptions.mStartX;
                mStartY = otherOptions.mStartY;
                mWidth = otherOptions.mWidth;
                mHeight = otherOptions.mHeight;
                if (mAnimationStartedListener != null) {
                    try {
                        mAnimationStartedListener.sendResult(null);
                    } catch (android.os.RemoteException e) {
                    }
                }
                mAnimationStartedListener = null;
                break;
            case android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_UP :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_DOWN :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_UP :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_DOWN :
                mThumbnail = otherOptions.mThumbnail;
                mStartX = otherOptions.mStartX;
                mStartY = otherOptions.mStartY;
                mWidth = otherOptions.mWidth;
                mHeight = otherOptions.mHeight;
                if (mAnimationStartedListener != null) {
                    try {
                        mAnimationStartedListener.sendResult(null);
                    } catch (android.os.RemoteException e) {
                    }
                }
                mAnimationStartedListener = otherOptions.mAnimationStartedListener;
                break;
            case android.app.ActivityOptions.ANIM_SCENE_TRANSITION :
                mTransitionReceiver = otherOptions.mTransitionReceiver;
                mSharedElementNames = otherOptions.mSharedElementNames;
                mIsReturning = otherOptions.mIsReturning;
                mThumbnail = null;
                mAnimationStartedListener = null;
                mResultData = otherOptions.mResultData;
                mResultCode = otherOptions.mResultCode;
                mExitCoordinatorIndex = otherOptions.mExitCoordinatorIndex;
                break;
        }
        mAnimSpecs = otherOptions.mAnimSpecs;
        mAnimationFinishedListener = otherOptions.mAnimationFinishedListener;
    }

    /**
     * Returns the created options as a Bundle, which can be passed to
     * {@link android.content.Context#startActivity(android.content.Intent, android.os.Bundle)
     * Context.startActivity(Intent, Bundle)} and related methods.
     * Note that the returned Bundle is still owned by the ActivityOptions
     * object; you must not modify it, but can supply it to the startActivity
     * methods that take an options Bundle.
     */
    public android.os.Bundle toBundle() {
        if (mAnimationType == android.app.ActivityOptions.ANIM_DEFAULT) {
            return null;
        }
        android.os.Bundle b = new android.os.Bundle();
        if (mPackageName != null) {
            b.putString(android.app.ActivityOptions.KEY_PACKAGE_NAME, mPackageName);
        }
        if (mLaunchBounds != null) {
            b.putParcelable(android.app.ActivityOptions.KEY_LAUNCH_BOUNDS, mLaunchBounds);
        }
        b.putInt(android.app.ActivityOptions.KEY_ANIM_TYPE, mAnimationType);
        if (mUsageTimeReport != null) {
            b.putParcelable(android.app.ActivityOptions.KEY_USAGE_TIME_REPORT, mUsageTimeReport);
        }
        switch (mAnimationType) {
            case android.app.ActivityOptions.ANIM_CUSTOM :
                b.putInt(android.app.ActivityOptions.KEY_ANIM_ENTER_RES_ID, mCustomEnterResId);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_EXIT_RES_ID, mCustomExitResId);
                b.putBinder(android.app.ActivityOptions.KEY_ANIM_START_LISTENER, mAnimationStartedListener != null ? mAnimationStartedListener.asBinder() : null);
                break;
            case android.app.ActivityOptions.ANIM_CUSTOM_IN_PLACE :
                b.putInt(android.app.ActivityOptions.KEY_ANIM_IN_PLACE_RES_ID, mCustomInPlaceResId);
                break;
            case android.app.ActivityOptions.ANIM_SCALE_UP :
            case android.app.ActivityOptions.ANIM_CLIP_REVEAL :
                b.putInt(android.app.ActivityOptions.KEY_ANIM_START_X, mStartX);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_START_Y, mStartY);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_WIDTH, mWidth);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_HEIGHT, mHeight);
                break;
            case android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_UP :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_SCALE_DOWN :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_UP :
            case android.app.ActivityOptions.ANIM_THUMBNAIL_ASPECT_SCALE_DOWN :
                b.putParcelable(android.app.ActivityOptions.KEY_ANIM_THUMBNAIL, mThumbnail);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_START_X, mStartX);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_START_Y, mStartY);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_WIDTH, mWidth);
                b.putInt(android.app.ActivityOptions.KEY_ANIM_HEIGHT, mHeight);
                b.putBinder(android.app.ActivityOptions.KEY_ANIM_START_LISTENER, mAnimationStartedListener != null ? mAnimationStartedListener.asBinder() : null);
                break;
            case android.app.ActivityOptions.ANIM_SCENE_TRANSITION :
                if (mTransitionReceiver != null) {
                    b.putParcelable(android.app.ActivityOptions.KEY_TRANSITION_COMPLETE_LISTENER, mTransitionReceiver);
                }
                b.putBoolean(android.app.ActivityOptions.KEY_TRANSITION_IS_RETURNING, mIsReturning);
                b.putStringArrayList(android.app.ActivityOptions.KEY_TRANSITION_SHARED_ELEMENTS, mSharedElementNames);
                b.putParcelable(android.app.ActivityOptions.KEY_RESULT_DATA, mResultData);
                b.putInt(android.app.ActivityOptions.KEY_RESULT_CODE, mResultCode);
                b.putInt(android.app.ActivityOptions.KEY_EXIT_COORDINATOR_INDEX, mExitCoordinatorIndex);
                break;
        }
        b.putInt(android.app.ActivityOptions.KEY_LAUNCH_STACK_ID, mLaunchStackId);
        b.putInt(android.app.ActivityOptions.KEY_LAUNCH_TASK_ID, mLaunchTaskId);
        b.putBoolean(android.app.ActivityOptions.KEY_TASK_OVERLAY, mTaskOverlay);
        b.putInt(android.app.ActivityOptions.KEY_DOCK_CREATE_MODE, mDockCreateMode);
        if (mAnimSpecs != null) {
            b.putParcelableArray(android.app.ActivityOptions.KEY_ANIM_SPECS, mAnimSpecs);
        }
        if (mAnimationFinishedListener != null) {
            b.putBinder(android.app.ActivityOptions.KEY_ANIMATION_FINISHED_LISTENER, mAnimationFinishedListener.asBinder());
        }
        b.putInt(android.app.ActivityOptions.KEY_ROTATION_ANIMATION_HINT, mRotationAnimationHint);
        return b;
    }

    /**
     * Ask the the system track that time the user spends in the app being launched, and
     * report it back once done.  The report will be sent to the given receiver, with
     * the extras {@link #EXTRA_USAGE_TIME_REPORT} and {@link #EXTRA_USAGE_TIME_REPORT_PACKAGES}
     * filled in.
     *
     * <p>The time interval tracked is from launching this activity until the user leaves
     * that activity's flow.  They are considered to stay in the flow as long as
     * new activities are being launched or returned to from the original flow,
     * even if this crosses package or task boundaries.  For example, if the originator
     * starts an activity to view an image, and while there the user selects to share,
     * which launches their email app in a new task, and they complete the share, the
     * time during that entire operation will be included until they finally hit back from
     * the original image viewer activity.</p>
     *
     * <p>The user is considered to complete a flow once they switch to another
     * activity that is not part of the tracked flow.  This may happen, for example, by
     * using the notification shade, launcher, or recents to launch or switch to another
     * app.  Simply going in to these navigation elements does not break the flow (although
     * the launcher and recents stops time tracking of the session); it is the act of
     * going somewhere else that completes the tracking.</p>
     *
     * @param receiver
     * 		A broadcast receiver that willl receive the report.
     */
    public void requestUsageTimeReport(android.app.PendingIntent receiver) {
        mUsageTimeReport = receiver;
    }

    /**
     * Return the filtered options only meant to be seen by the target activity itself
     *
     * @unknown 
     */
    public android.app.ActivityOptions forTargetActivity() {
        if (mAnimationType == android.app.ActivityOptions.ANIM_SCENE_TRANSITION) {
            final android.app.ActivityOptions result = new android.app.ActivityOptions();
            result.update(this);
            return result;
        }
        return null;
    }

    /**
     * Returns the rotation animation set by {@link setRotationAnimationHint} or -1
     * if unspecified.
     *
     * @unknown 
     */
    public int getRotationAnimationHint() {
        return mRotationAnimationHint;
    }

    /**
     * Set a rotation animation to be used if launching the activity
     * triggers an orientation change, or -1 to clear. See
     * {@link android.view.WindowManager.LayoutParams} for rotation
     * animation values.
     *
     * @unknown 
     */
    public void setRotationAnimationHint(int hint) {
        mRotationAnimationHint = hint;
    }

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Override
    public java.lang.String toString() {
        return (((((((((((("ActivityOptions(" + hashCode()) + "), mPackageName=") + mPackageName) + ", mAnimationType=") + mAnimationType) + ", mStartX=") + mStartX) + ", mStartY=") + mStartY) + ", mWidth=") + mWidth) + ", mHeight=") + mHeight;
    }

    private static class HideWindowListener extends android.transition.Transition.TransitionListenerAdapter implements android.app.ExitTransitionCoordinator.HideSharedElementsCallback {
        private final android.view.Window mWindow;

        private final android.app.ExitTransitionCoordinator mExit;

        private final boolean mWaitingForTransition;

        private boolean mTransitionEnded;

        private boolean mSharedElementHidden;

        private java.util.ArrayList<android.view.View> mSharedElements;

        public HideWindowListener(android.view.Window window, android.app.ExitTransitionCoordinator exit) {
            mWindow = window;
            mExit = exit;
            mSharedElements = new java.util.ArrayList<>(exit.mSharedElements);
            android.transition.Transition transition = mWindow.getExitTransition();
            if (transition != null) {
                transition.addListener(this);
                mWaitingForTransition = true;
            } else {
                mWaitingForTransition = false;
            }
            android.view.View decorView = mWindow.getDecorView();
            if (decorView != null) {
                if (decorView.getTag(com.android.internal.R.id.cross_task_transition) != null) {
                    throw new java.lang.IllegalStateException("Cannot start a transition while one is running");
                }
                decorView.setTagInternal(com.android.internal.R.id.cross_task_transition, exit);
            }
        }

        @java.lang.Override
        public void onTransitionEnd(android.transition.Transition transition) {
            mTransitionEnded = true;
            hideWhenDone();
            transition.removeListener(this);
        }

        @java.lang.Override
        public void hideSharedElements() {
            mSharedElementHidden = true;
            hideWhenDone();
        }

        private void hideWhenDone() {
            if (mSharedElementHidden && ((!mWaitingForTransition) || mTransitionEnded)) {
                mExit.resetViews();
                int numSharedElements = mSharedElements.size();
                for (int i = 0; i < numSharedElements; i++) {
                    android.view.View view = mSharedElements.get(i);
                    view.requestLayout();
                }
                android.view.View decorView = mWindow.getDecorView();
                if (decorView != null) {
                    decorView.setTagInternal(com.android.internal.R.id.cross_task_transition, null);
                    decorView.setVisibility(android.view.View.GONE);
                }
            }
        }
    }
}

