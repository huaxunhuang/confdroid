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
package android.app;


/**
 * Interact with the overall activities running in the system.
 */
public class ActivityManager {
    private static java.lang.String TAG = "ActivityManager";

    private static int gMaxRecentTasks = -1;

    private final android.content.Context mContext;

    private final android.os.Handler mHandler;

    /**
     * Defines acceptable types of bugreports.
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.app.ActivityManager.BUGREPORT_OPTION_FULL, android.app.ActivityManager.BUGREPORT_OPTION_INTERACTIVE, android.app.ActivityManager.BUGREPORT_OPTION_REMOTE, android.app.ActivityManager.BUGREPORT_OPTION_WEAR })
    public @interface BugreportMode {}

    /**
     * Takes a bugreport without user interference (and hence causing less
     * interference to the system), but includes all sections.
     *
     * @unknown 
     */
    public static final int BUGREPORT_OPTION_FULL = 0;

    /**
     * Allows user to monitor progress and enter additional data; might not include all
     * sections.
     *
     * @unknown 
     */
    public static final int BUGREPORT_OPTION_INTERACTIVE = 1;

    /**
     * Takes a bugreport requested remotely by administrator of the Device Owner app,
     * not the device's user.
     *
     * @unknown 
     */
    public static final int BUGREPORT_OPTION_REMOTE = 2;

    /**
     * Takes a bugreport on a wearable device.
     *
     * @unknown 
     */
    public static final int BUGREPORT_OPTION_WEAR = 3;

    /**
     * <a href="{@docRoot }guide/topics/manifest/meta-data-element.html">{@code <meta-data>}</a> name for a 'home' Activity that declares a package that is to be
     * uninstalled in lieu of the declaring one.  The package named here must be
     * signed with the same certificate as the one declaring the {@code <meta-data>}.
     */
    public static final java.lang.String META_HOME_ALTERNATE = "android.app.home.alternate";

    /**
     * Result for IActivityManager.startVoiceActivity: active session is currently hidden.
     *
     * @unknown 
     */
    public static final int START_VOICE_HIDDEN_SESSION = -10;

    /**
     * Result for IActivityManager.startVoiceActivity: active session does not match
     * the requesting token.
     *
     * @unknown 
     */
    public static final int START_VOICE_NOT_ACTIVE_SESSION = -9;

    /**
     * Result for IActivityManager.startActivity: trying to start a background user
     * activity that shouldn't be displayed for all users.
     *
     * @unknown 
     */
    public static final int START_NOT_CURRENT_USER_ACTIVITY = -8;

    /**
     * Result for IActivityManager.startActivity: trying to start an activity under voice
     * control when that activity does not support the VOICE category.
     *
     * @unknown 
     */
    public static final int START_NOT_VOICE_COMPATIBLE = -7;

    /**
     * Result for IActivityManager.startActivity: an error where the
     * start had to be canceled.
     *
     * @unknown 
     */
    public static final int START_CANCELED = -6;

    /**
     * Result for IActivityManager.startActivity: an error where the
     * thing being started is not an activity.
     *
     * @unknown 
     */
    public static final int START_NOT_ACTIVITY = -5;

    /**
     * Result for IActivityManager.startActivity: an error where the
     * caller does not have permission to start the activity.
     *
     * @unknown 
     */
    public static final int START_PERMISSION_DENIED = -4;

    /**
     * Result for IActivityManager.startActivity: an error where the
     * caller has requested both to forward a result and to receive
     * a result.
     *
     * @unknown 
     */
    public static final int START_FORWARD_AND_REQUEST_CONFLICT = -3;

    /**
     * Result for IActivityManager.startActivity: an error where the
     * requested class is not found.
     *
     * @unknown 
     */
    public static final int START_CLASS_NOT_FOUND = -2;

    /**
     * Result for IActivityManager.startActivity: an error where the
     * given Intent could not be resolved to an activity.
     *
     * @unknown 
     */
    public static final int START_INTENT_NOT_RESOLVED = -1;

    /**
     * Result for IActivityManaqer.startActivity: the activity was started
     * successfully as normal.
     *
     * @unknown 
     */
    public static final int START_SUCCESS = 0;

    /**
     * Result for IActivityManaqer.startActivity: the caller asked that the Intent not
     * be executed if it is the recipient, and that is indeed the case.
     *
     * @unknown 
     */
    public static final int START_RETURN_INTENT_TO_CALLER = 1;

    /**
     * Result for IActivityManaqer.startActivity: activity wasn't really started, but
     * a task was simply brought to the foreground.
     *
     * @unknown 
     */
    public static final int START_TASK_TO_FRONT = 2;

    /**
     * Result for IActivityManaqer.startActivity: activity wasn't really started, but
     * the given Intent was given to the existing top activity.
     *
     * @unknown 
     */
    public static final int START_DELIVERED_TO_TOP = 3;

    /**
     * Result for IActivityManaqer.startActivity: request was canceled because
     * app switches are temporarily canceled to ensure the user's last request
     * (such as pressing home) is performed.
     *
     * @unknown 
     */
    public static final int START_SWITCHES_CANCELED = 4;

    /**
     * Result for IActivityManaqer.startActivity: a new activity was attempted to be started
     * while in Lock Task Mode.
     *
     * @unknown 
     */
    public static final int START_RETURN_LOCK_TASK_MODE_VIOLATION = 5;

    /**
     * Flag for IActivityManaqer.startActivity: do special start mode where
     * a new activity is launched only if it is needed.
     *
     * @unknown 
     */
    public static final int START_FLAG_ONLY_IF_NEEDED = 1 << 0;

    /**
     * Flag for IActivityManaqer.startActivity: launch the app for
     * debugging.
     *
     * @unknown 
     */
    public static final int START_FLAG_DEBUG = 1 << 1;

    /**
     * Flag for IActivityManaqer.startActivity: launch the app for
     * allocation tracking.
     *
     * @unknown 
     */
    public static final int START_FLAG_TRACK_ALLOCATION = 1 << 2;

    /**
     * Flag for IActivityManaqer.startActivity: launch the app with
     * native debugging support.
     *
     * @unknown 
     */
    public static final int START_FLAG_NATIVE_DEBUGGING = 1 << 3;

    /**
     * Result for IActivityManaqer.broadcastIntent: success!
     *
     * @unknown 
     */
    public static final int BROADCAST_SUCCESS = 0;

    /**
     * Result for IActivityManaqer.broadcastIntent: attempt to broadcast
     * a sticky intent without appropriate permission.
     *
     * @unknown 
     */
    public static final int BROADCAST_STICKY_CANT_HAVE_PERMISSION = -1;

    /**
     * Result for IActivityManager.broadcastIntent: trying to send a broadcast
     * to a stopped user. Fail.
     *
     * @unknown 
     */
    public static final int BROADCAST_FAILED_USER_STOPPED = -2;

    /**
     * Type for IActivityManaqer.getIntentSender: this PendingIntent is
     * for a sendBroadcast operation.
     *
     * @unknown 
     */
    public static final int INTENT_SENDER_BROADCAST = 1;

    /**
     * Type for IActivityManaqer.getIntentSender: this PendingIntent is
     * for a startActivity operation.
     *
     * @unknown 
     */
    public static final int INTENT_SENDER_ACTIVITY = 2;

    /**
     * Type for IActivityManaqer.getIntentSender: this PendingIntent is
     * for an activity result operation.
     *
     * @unknown 
     */
    public static final int INTENT_SENDER_ACTIVITY_RESULT = 3;

    /**
     * Type for IActivityManaqer.getIntentSender: this PendingIntent is
     * for a startService operation.
     *
     * @unknown 
     */
    public static final int INTENT_SENDER_SERVICE = 4;

    /**
     *
     *
     * @unknown User operation call: success!
     */
    public static final int USER_OP_SUCCESS = 0;

    /**
     *
     *
     * @unknown User operation call: given user id is not known.
     */
    public static final int USER_OP_UNKNOWN_USER = -1;

    /**
     *
     *
     * @unknown User operation call: given user id is the current user, can't be stopped.
     */
    public static final int USER_OP_IS_CURRENT = -2;

    /**
     *
     *
     * @unknown User operation call: system user can't be stopped.
     */
    public static final int USER_OP_ERROR_IS_SYSTEM = -3;

    /**
     *
     *
     * @unknown User operation call: one of related users cannot be stopped.
     */
    public static final int USER_OP_ERROR_RELATED_USERS_CANNOT_STOP = -4;

    /**
     *
     *
     * @unknown Process does not exist.
     */
    public static final int PROCESS_STATE_NONEXISTENT = -1;

    /**
     *
     *
     * @unknown Process is a persistent system process.
     */
    public static final int PROCESS_STATE_PERSISTENT = 0;

    /**
     *
     *
     * @unknown Process is a persistent system process and is doing UI.
     */
    public static final int PROCESS_STATE_PERSISTENT_UI = 1;

    /**
     *
     *
     * @unknown Process is hosting the current top activities.  Note that this covers
    all activities that are visible to the user.
     */
    public static final int PROCESS_STATE_TOP = 2;

    /**
     *
     *
     * @unknown Process is hosting a foreground service due to a system binding.
     */
    public static final int PROCESS_STATE_BOUND_FOREGROUND_SERVICE = 3;

    /**
     *
     *
     * @unknown Process is hosting a foreground service.
     */
    public static final int PROCESS_STATE_FOREGROUND_SERVICE = 4;

    /**
     *
     *
     * @unknown Same as {@link #PROCESS_STATE_TOP} but while device is sleeping.
     */
    public static final int PROCESS_STATE_TOP_SLEEPING = 5;

    /**
     *
     *
     * @unknown Process is important to the user, and something they are aware of.
     */
    public static final int PROCESS_STATE_IMPORTANT_FOREGROUND = 6;

    /**
     *
     *
     * @unknown Process is important to the user, but not something they are aware of.
     */
    public static final int PROCESS_STATE_IMPORTANT_BACKGROUND = 7;

    /**
     *
     *
     * @unknown Process is in the background running a backup/restore operation.
     */
    public static final int PROCESS_STATE_BACKUP = 8;

    /**
     *
     *
     * @unknown Process is in the background, but it can't restore its state so we want
    to try to avoid killing it.
     */
    public static final int PROCESS_STATE_HEAVY_WEIGHT = 9;

    /**
     *
     *
     * @unknown Process is in the background running a service.  Unlike oom_adj, this level
    is used for both the normal running in background state and the executing
    operations state.
     */
    public static final int PROCESS_STATE_SERVICE = 10;

    /**
     *
     *
     * @unknown Process is in the background running a receiver.   Note that from the
    perspective of oom_adj receivers run at a higher foreground level, but for our
    prioritization here that is not necessary and putting them below services means
    many fewer changes in some process states as they receive broadcasts.
     */
    public static final int PROCESS_STATE_RECEIVER = 11;

    /**
     *
     *
     * @unknown Process is in the background but hosts the home activity.
     */
    public static final int PROCESS_STATE_HOME = 12;

    /**
     *
     *
     * @unknown Process is in the background but hosts the last shown activity.
     */
    public static final int PROCESS_STATE_LAST_ACTIVITY = 13;

    /**
     *
     *
     * @unknown Process is being cached for later use and contains activities.
     */
    public static final int PROCESS_STATE_CACHED_ACTIVITY = 14;

    /**
     *
     *
     * @unknown Process is being cached for later use and is a client of another cached
    process that contains activities.
     */
    public static final int PROCESS_STATE_CACHED_ACTIVITY_CLIENT = 15;

    /**
     *
     *
     * @unknown Process is being cached for later use and is empty.
     */
    public static final int PROCESS_STATE_CACHED_EMPTY = 16;

    /**
     *
     *
     * @unknown The lowest process state number
     */
    public static final int MIN_PROCESS_STATE = android.app.ActivityManager.PROCESS_STATE_NONEXISTENT;

    /**
     *
     *
     * @unknown The highest process state number
     */
    public static final int MAX_PROCESS_STATE = android.app.ActivityManager.PROCESS_STATE_CACHED_EMPTY;

    /**
     *
     *
     * @unknown Should this process state be considered a background state?
     */
    public static final boolean isProcStateBackground(int procState) {
        return procState >= android.app.ActivityManager.PROCESS_STATE_BACKUP;
    }

    /**
     *
     *
     * @unknown requestType for assist context: only basic information.
     */
    public static final int ASSIST_CONTEXT_BASIC = 0;

    /**
     *
     *
     * @unknown requestType for assist context: generate full AssistStructure.
     */
    public static final int ASSIST_CONTEXT_FULL = 1;

    /**
     *
     *
     * @unknown Flag for registerUidObserver: report changes in process state.
     */
    public static final int UID_OBSERVER_PROCSTATE = 1 << 0;

    /**
     *
     *
     * @unknown Flag for registerUidObserver: report uid gone.
     */
    public static final int UID_OBSERVER_GONE = 1 << 1;

    /**
     *
     *
     * @unknown Flag for registerUidObserver: report uid has become idle.
     */
    public static final int UID_OBSERVER_IDLE = 1 << 2;

    /**
     *
     *
     * @unknown Flag for registerUidObserver: report uid has become active.
     */
    public static final int UID_OBSERVER_ACTIVE = 1 << 3;

    /**
     *
     *
     * @unknown Mode for {@link IActivityManager#getAppStartMode}: normal free-to-run operation.
     */
    public static final int APP_START_MODE_NORMAL = 0;

    /**
     *
     *
     * @unknown Mode for {@link IActivityManager#getAppStartMode}: delay running until later.
     */
    public static final int APP_START_MODE_DELAYED = 1;

    /**
     *
     *
     * @unknown Mode for {@link IActivityManager#getAppStartMode}: disable/cancel pending
    launches.
     */
    public static final int APP_START_MODE_DISABLED = 2;

    /**
     * Lock task mode is not active.
     */
    public static final int LOCK_TASK_MODE_NONE = 0;

    /**
     * Full lock task mode is active.
     */
    public static final int LOCK_TASK_MODE_LOCKED = 1;

    /**
     * App pinning mode is active.
     */
    public static final int LOCK_TASK_MODE_PINNED = 2;

    android.graphics.Point mAppTaskThumbnailSize;

    /* package */
    ActivityManager(android.content.Context context, android.os.Handler handler) {
        mContext = context;
        mHandler = handler;
    }

    /**
     * Screen compatibility mode: the application most always run in
     * compatibility mode.
     *
     * @unknown 
     */
    public static final int COMPAT_MODE_ALWAYS = -1;

    /**
     * Screen compatibility mode: the application can never run in
     * compatibility mode.
     *
     * @unknown 
     */
    public static final int COMPAT_MODE_NEVER = -2;

    /**
     * Screen compatibility mode: unknown.
     *
     * @unknown 
     */
    public static final int COMPAT_MODE_UNKNOWN = -3;

    /**
     * Screen compatibility mode: the application currently has compatibility
     * mode disabled.
     *
     * @unknown 
     */
    public static final int COMPAT_MODE_DISABLED = 0;

    /**
     * Screen compatibility mode: the application currently has compatibility
     * mode enabled.
     *
     * @unknown 
     */
    public static final int COMPAT_MODE_ENABLED = 1;

    /**
     * Screen compatibility mode: request to toggle the application's
     * compatibility mode.
     *
     * @unknown 
     */
    public static final int COMPAT_MODE_TOGGLE = 2;

    /**
     *
     *
     * @unknown 
     */
    public static class StackId {
        /**
         * Invalid stack ID.
         */
        public static final int INVALID_STACK_ID = -1;

        /**
         * First static stack ID.
         */
        public static final int FIRST_STATIC_STACK_ID = 0;

        /**
         * Home activity stack ID.
         */
        public static final int HOME_STACK_ID = android.app.ActivityManager.StackId.FIRST_STATIC_STACK_ID;

        /**
         * ID of stack where fullscreen activities are normally launched into.
         */
        public static final int FULLSCREEN_WORKSPACE_STACK_ID = 1;

        /**
         * ID of stack where freeform/resized activities are normally launched into.
         */
        public static final int FREEFORM_WORKSPACE_STACK_ID = android.app.ActivityManager.StackId.FULLSCREEN_WORKSPACE_STACK_ID + 1;

        /**
         * ID of stack that occupies a dedicated region of the screen.
         */
        public static final int DOCKED_STACK_ID = android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID + 1;

        /**
         * ID of stack that always on top (always visible) when it exist.
         */
        public static final int PINNED_STACK_ID = android.app.ActivityManager.StackId.DOCKED_STACK_ID + 1;

        /**
         * Last static stack stack ID.
         */
        public static final int LAST_STATIC_STACK_ID = android.app.ActivityManager.StackId.PINNED_STACK_ID;

        /**
         * Start of ID range used by stacks that are created dynamically.
         */
        public static final int FIRST_DYNAMIC_STACK_ID = android.app.ActivityManager.StackId.LAST_STATIC_STACK_ID + 1;

        public static boolean isStaticStack(int stackId) {
            return (stackId >= android.app.ActivityManager.StackId.FIRST_STATIC_STACK_ID) && (stackId <= android.app.ActivityManager.StackId.LAST_STATIC_STACK_ID);
        }

        /**
         * Returns true if the activities contained in the input stack display a shadow around
         * their border.
         */
        public static boolean hasWindowShadow(int stackId) {
            return (stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID) || (stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID);
        }

        /**
         * Returns true if the activities contained in the input stack display a decor view.
         */
        public static boolean hasWindowDecor(int stackId) {
            return stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID;
        }

        /**
         * Returns true if the tasks contained in the stack can be resized independently of the
         * stack.
         */
        public static boolean isTaskResizeAllowed(int stackId) {
            return stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID;
        }

        /**
         * Returns true if the task bounds should persist across power cycles.
         */
        public static boolean persistTaskBounds(int stackId) {
            return stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID;
        }

        /**
         * Returns true if dynamic stacks are allowed to be visible behind the input stack.
         */
        public static boolean isDynamicStacksVisibleBehindAllowed(int stackId) {
            return stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }

        /**
         * Returns true if we try to maintain focus in the current stack when the top activity
         * finishes.
         */
        public static boolean keepFocusInStackIfPossible(int stackId) {
            return ((stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID) || (stackId == android.app.ActivityManager.StackId.DOCKED_STACK_ID)) || (stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID);
        }

        /**
         * Returns true if Stack size is affected by the docked stack changing size.
         */
        public static boolean isResizeableByDockedStack(int stackId) {
            return (android.app.ActivityManager.StackId.isStaticStack(stackId) && (stackId != android.app.ActivityManager.StackId.DOCKED_STACK_ID)) && (stackId != android.app.ActivityManager.StackId.PINNED_STACK_ID);
        }

        /**
         * Returns true if the size of tasks in the input stack are affected by the docked stack
         * changing size.
         */
        public static boolean isTaskResizeableByDockedStack(int stackId) {
            return ((android.app.ActivityManager.StackId.isStaticStack(stackId) && (stackId != android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID)) && (stackId != android.app.ActivityManager.StackId.DOCKED_STACK_ID)) && (stackId != android.app.ActivityManager.StackId.PINNED_STACK_ID);
        }

        /**
         * Returns true if the windows of tasks being moved to the target stack from the source
         * stack should be replaced, meaning that window manager will keep the old window around
         * until the new is ready.
         */
        public static boolean replaceWindowsOnTaskMove(int sourceStackId, int targetStackId) {
            return (sourceStackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID) || (targetStackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID);
        }

        /**
         * Return whether a stackId is a stack containing floating windows. Floating windows
         * are laid out differently as they are allowed to extend past the display bounds
         * without overscan insets.
         */
        public static boolean tasksAreFloating(int stackId) {
            return (stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID) || (stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID);
        }

        /**
         * Returns true if animation specs should be constructed for app transition that moves
         * the task to the specified stack.
         */
        public static boolean useAnimationSpecForAppTransition(int stackId) {
            // TODO: INVALID_STACK_ID is also animated because we don't persist stack id's across
            // reboots.
            return (((stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID) || (stackId == android.app.ActivityManager.StackId.FULLSCREEN_WORKSPACE_STACK_ID)) || (stackId == android.app.ActivityManager.StackId.DOCKED_STACK_ID)) || (stackId == android.app.ActivityManager.StackId.INVALID_STACK_ID);
        }

        /**
         * Returns true if the windows in the stack can receive input keys.
         */
        public static boolean canReceiveKeys(int stackId) {
            return stackId != android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }

        /**
         * Returns true if the stack can be visible above lockscreen.
         */
        public static boolean isAllowedOverLockscreen(int stackId) {
            return (stackId == android.app.ActivityManager.StackId.HOME_STACK_ID) || (stackId == android.app.ActivityManager.StackId.FULLSCREEN_WORKSPACE_STACK_ID);
        }

        public static boolean isAlwaysOnTop(int stackId) {
            return stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }

        /**
         * Returns true if the top task in the task is allowed to return home when finished and
         * there are other tasks in the stack.
         */
        public static boolean allowTopTaskToReturnHome(int stackId) {
            return stackId != android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }

        /**
         * Returns true if the stack should be resized to match the bounds specified by
         * {@link ActivityOptions#setLaunchBounds} when launching an activity into the stack.
         */
        public static boolean resizeStackWithLaunchBounds(int stackId) {
            return stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }

        /**
         * Returns true if any visible windows belonging to apps in this stack should be kept on
         * screen when the app is killed due to something like the low memory killer.
         */
        public static boolean keepVisibleDeadAppWindowOnScreen(int stackId) {
            return stackId != android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }

        /**
         * Returns true if the backdrop on the client side should match the frame of the window.
         * Returns false, if the backdrop should be fullscreen.
         */
        public static boolean useWindowFrameForBackdrop(int stackId) {
            return (stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID) || (stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID);
        }

        /**
         * Returns true if a window from the specified stack with {@param stackId} are normally
         * fullscreen, i. e. they can become the top opaque fullscreen window, meaning that it
         * controls system bars, lockscreen occluded/dismissing state, screen rotation animation,
         * etc.
         */
        public static boolean normallyFullscreenWindows(int stackId) {
            return ((stackId != android.app.ActivityManager.StackId.PINNED_STACK_ID) && (stackId != android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID)) && (stackId != android.app.ActivityManager.StackId.DOCKED_STACK_ID);
        }

        /**
         * Returns true if the input stack id should only be present on a device that supports
         * multi-window mode.
         *
         * @see android.app.ActivityManager#supportsMultiWindow
         */
        public static boolean isMultiWindowStack(int stackId) {
            return ((android.app.ActivityManager.StackId.isStaticStack(stackId) || (stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID)) || (stackId == android.app.ActivityManager.StackId.FREEFORM_WORKSPACE_STACK_ID)) || (stackId == android.app.ActivityManager.StackId.DOCKED_STACK_ID);
        }

        /**
         * Returns true if activities contained in this stack can request visible behind by
         * calling {@link Activity#requestVisibleBehind}.
         */
        public static boolean activitiesCanRequestVisibleBehind(int stackId) {
            return stackId == android.app.ActivityManager.StackId.FULLSCREEN_WORKSPACE_STACK_ID;
        }

        /**
         * Returns true if this stack may be scaled without resizing,
         * and windows within may need to be configured as such.
         */
        public static boolean windowsAreScaleable(int stackId) {
            return stackId == android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }

        /**
         * Returns true if windows in this stack should be given move animations
         * by default.
         */
        public static boolean hasMovementAnimations(int stackId) {
            return stackId != android.app.ActivityManager.StackId.PINNED_STACK_ID;
        }
    }

    /**
     * Input parameter to {@link android.app.IActivityManager#moveTaskToDockedStack} which
     * specifies the position of the created docked stack at the top half of the screen if
     * in portrait mode or at the left half of the screen if in landscape mode.
     *
     * @unknown 
     */
    public static final int DOCKED_STACK_CREATE_MODE_TOP_OR_LEFT = 0;

    /**
     * Input parameter to {@link android.app.IActivityManager#moveTaskToDockedStack} which
     * specifies the position of the created docked stack at the bottom half of the screen if
     * in portrait mode or at the right half of the screen if in landscape mode.
     *
     * @unknown 
     */
    public static final int DOCKED_STACK_CREATE_MODE_BOTTOM_OR_RIGHT = 1;

    /**
     * Input parameter to {@link android.app.IActivityManager#resizeTask} which indicates
     * that the resize doesn't need to preserve the window, and can be skipped if bounds
     * is unchanged. This mode is used by window manager in most cases.
     *
     * @unknown 
     */
    public static final int RESIZE_MODE_SYSTEM = 0;

    /**
     * Input parameter to {@link android.app.IActivityManager#resizeTask} which indicates
     * that the resize should preserve the window if possible.
     *
     * @unknown 
     */
    public static final int RESIZE_MODE_PRESERVE_WINDOW = 0x1 << 0;

    /**
     * Input parameter to {@link android.app.IActivityManager#resizeTask} which indicates
     * that the resize should be performed even if the bounds appears unchanged.
     *
     * @unknown 
     */
    public static final int RESIZE_MODE_FORCED = 0x1 << 1;

    /**
     * Input parameter to {@link android.app.IActivityManager#resizeTask} used by window
     * manager during a screen rotation.
     *
     * @unknown 
     */
    public static final int RESIZE_MODE_SYSTEM_SCREEN_ROTATION = android.app.ActivityManager.RESIZE_MODE_PRESERVE_WINDOW;

    /**
     * Input parameter to {@link android.app.IActivityManager#resizeTask} used when the
     * resize is due to a drag action.
     *
     * @unknown 
     */
    public static final int RESIZE_MODE_USER = android.app.ActivityManager.RESIZE_MODE_PRESERVE_WINDOW;

    /**
     * Input parameter to {@link android.app.IActivityManager#resizeTask} which indicates
     * that the resize should preserve the window if possible, and should not be skipped
     * even if the bounds is unchanged. Usually used to force a resizing when a drag action
     * is ending.
     *
     * @unknown 
     */
    public static final int RESIZE_MODE_USER_FORCED = android.app.ActivityManager.RESIZE_MODE_PRESERVE_WINDOW | android.app.ActivityManager.RESIZE_MODE_FORCED;

    /**
     *
     *
     * @unknown 
     */
    public int getFrontActivityScreenCompatMode() {
        try {
            return android.app.ActivityManagerNative.getDefault().getFrontActivityScreenCompatMode();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setFrontActivityScreenCompatMode(int mode) {
        try {
            android.app.ActivityManagerNative.getDefault().setFrontActivityScreenCompatMode(mode);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public int getPackageScreenCompatMode(java.lang.String packageName) {
        try {
            return android.app.ActivityManagerNative.getDefault().getPackageScreenCompatMode(packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setPackageScreenCompatMode(java.lang.String packageName, int mode) {
        try {
            android.app.ActivityManagerNative.getDefault().setPackageScreenCompatMode(packageName, mode);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean getPackageAskScreenCompat(java.lang.String packageName) {
        try {
            return android.app.ActivityManagerNative.getDefault().getPackageAskScreenCompat(packageName);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void setPackageAskScreenCompat(java.lang.String packageName, boolean ask) {
        try {
            android.app.ActivityManagerNative.getDefault().setPackageAskScreenCompat(packageName, ask);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return the approximate per-application memory class of the current
     * device.  This gives you an idea of how hard a memory limit you should
     * impose on your application to let the overall system work best.  The
     * returned value is in megabytes; the baseline Android memory class is
     * 16 (which happens to be the Java heap limit of those devices); some
     * device with more memory may return 24 or even higher numbers.
     */
    public int getMemoryClass() {
        return android.app.ActivityManager.staticGetMemoryClass();
    }

    /**
     *
     *
     * @unknown 
     */
    public static int staticGetMemoryClass() {
        // Really brain dead right now -- just take this from the configured
        // vm heap size, and assume it is in megabytes and thus ends with "m".
        java.lang.String vmHeapSize = android.os.SystemProperties.get("dalvik.vm.heapgrowthlimit", "");
        if ((vmHeapSize != null) && (!"".equals(vmHeapSize))) {
            return java.lang.Integer.parseInt(vmHeapSize.substring(0, vmHeapSize.length() - 1));
        }
        return android.app.ActivityManager.staticGetLargeMemoryClass();
    }

    /**
     * Return the approximate per-application memory class of the current
     * device when an application is running with a large heap.  This is the
     * space available for memory-intensive applications; most applications
     * should not need this amount of memory, and should instead stay with the
     * {@link #getMemoryClass()} limit.  The returned value is in megabytes.
     * This may be the same size as {@link #getMemoryClass()} on memory
     * constrained devices, or it may be significantly larger on devices with
     * a large amount of available RAM.
     *
     * <p>The is the size of the application's Dalvik heap if it has
     * specified <code>android:largeHeap="true"</code> in its manifest.
     */
    public int getLargeMemoryClass() {
        return android.app.ActivityManager.staticGetLargeMemoryClass();
    }

    /**
     *
     *
     * @unknown 
     */
    public static int staticGetLargeMemoryClass() {
        // Really brain dead right now -- just take this from the configured
        // vm heap size, and assume it is in megabytes and thus ends with "m".
        java.lang.String vmHeapSize = android.os.SystemProperties.get("dalvik.vm.heapsize", "16m");
        return java.lang.Integer.parseInt(vmHeapSize.substring(0, vmHeapSize.length() - 1));
    }

    /**
     * Returns true if this is a low-RAM device.  Exactly whether a device is low-RAM
     * is ultimately up to the device configuration, but currently it generally means
     * something in the class of a 512MB device with about a 800x480 or less screen.
     * This is mostly intended to be used by apps to determine whether they should turn
     * off certain features that require more RAM.
     */
    public boolean isLowRamDevice() {
        return android.app.ActivityManager.isLowRamDeviceStatic();
    }

    /**
     *
     *
     * @unknown 
     */
    public static boolean isLowRamDeviceStatic() {
        return "true".equals(android.os.SystemProperties.get("ro.config.low_ram", "false"));
    }

    /**
     * Used by persistent processes to determine if they are running on a
     * higher-end device so should be okay using hardware drawing acceleration
     * (which tends to consume a lot more RAM).
     *
     * @unknown 
     */
    public static boolean isHighEndGfx() {
        return (!android.app.ActivityManager.isLowRamDeviceStatic()) && (!android.content.res.Resources.getSystem().getBoolean(com.android.internal.R.bool.config_avoidGfxAccel));
    }

    /**
     * Return the maximum number of recents entries that we will maintain and show.
     *
     * @unknown 
     */
    public static int getMaxRecentTasksStatic() {
        if (android.app.ActivityManager.gMaxRecentTasks < 0) {
            return android.app.ActivityManager.gMaxRecentTasks = (android.app.ActivityManager.isLowRamDeviceStatic()) ? 36 : 48;
        }
        return android.app.ActivityManager.gMaxRecentTasks;
    }

    /**
     * Return the default limit on the number of recents that an app can make.
     *
     * @unknown 
     */
    public static int getDefaultAppRecentsLimitStatic() {
        return android.app.ActivityManager.getMaxRecentTasksStatic() / 6;
    }

    /**
     * Return the maximum limit on the number of recents that an app can make.
     *
     * @unknown 
     */
    public static int getMaxAppRecentsLimitStatic() {
        return android.app.ActivityManager.getMaxRecentTasksStatic() / 2;
    }

    /**
     * Returns true if the system supports at least one form of multi-window.
     * E.g. freeform, split-screen, picture-in-picture.
     *
     * @unknown 
     */
    public static boolean supportsMultiWindow() {
        return (!android.app.ActivityManager.isLowRamDeviceStatic()) && android.content.res.Resources.getSystem().getBoolean(com.android.internal.R.bool.config_supportsMultiWindow);
    }

    /**
     * Information you can set and retrieve about the current activity within the recent task list.
     */
    public static class TaskDescription implements android.os.Parcelable {
        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String ATTR_TASKDESCRIPTION_PREFIX = "task_description_";

        private static final java.lang.String ATTR_TASKDESCRIPTIONLABEL = android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTION_PREFIX + "label";

        private static final java.lang.String ATTR_TASKDESCRIPTIONCOLOR_PRIMARY = android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTION_PREFIX + "color";

        private static final java.lang.String ATTR_TASKDESCRIPTIONCOLOR_BACKGROUND = android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTION_PREFIX + "colorBackground";

        private static final java.lang.String ATTR_TASKDESCRIPTIONICONFILENAME = android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTION_PREFIX + "icon_filename";

        private java.lang.String mLabel;

        private android.graphics.Bitmap mIcon;

        private java.lang.String mIconFilename;

        private int mColorPrimary;

        private int mColorBackground;

        /**
         * Creates the TaskDescription to the specified values.
         *
         * @param label
         * 		A label and description of the current state of this task.
         * @param icon
         * 		An icon that represents the current state of this task.
         * @param colorPrimary
         * 		A color to override the theme's primary color.  This color must be
         * 		opaque.
         */
        public TaskDescription(java.lang.String label, android.graphics.Bitmap icon, int colorPrimary) {
            this(label, icon, null, colorPrimary, 0);
            if ((colorPrimary != 0) && (android.graphics.Color.alpha(colorPrimary) != 255)) {
                throw new java.lang.RuntimeException("A TaskDescription's primary color should be opaque");
            }
        }

        /**
         * Creates the TaskDescription to the specified values.
         *
         * @param label
         * 		A label and description of the current state of this activity.
         * @param icon
         * 		An icon that represents the current state of this activity.
         */
        public TaskDescription(java.lang.String label, android.graphics.Bitmap icon) {
            this(label, icon, null, 0, 0);
        }

        /**
         * Creates the TaskDescription to the specified values.
         *
         * @param label
         * 		A label and description of the current state of this activity.
         */
        public TaskDescription(java.lang.String label) {
            this(label, null, null, 0, 0);
        }

        /**
         * Creates an empty TaskDescription.
         */
        public TaskDescription() {
            this(null, null, null, 0, 0);
        }

        /**
         *
         *
         * @unknown 
         */
        public TaskDescription(java.lang.String label, android.graphics.Bitmap icon, java.lang.String iconFilename, int colorPrimary, int colorBackground) {
            mLabel = label;
            mIcon = icon;
            mIconFilename = iconFilename;
            mColorPrimary = colorPrimary;
            mColorBackground = colorBackground;
        }

        /**
         * Creates a copy of another TaskDescription.
         */
        public TaskDescription(android.app.ActivityManager.TaskDescription td) {
            copyFrom(td);
        }

        /**
         * Copies this the values from another TaskDescription.
         *
         * @unknown 
         */
        public void copyFrom(android.app.ActivityManager.TaskDescription other) {
            mLabel = other.mLabel;
            mIcon = other.mIcon;
            mIconFilename = other.mIconFilename;
            mColorPrimary = other.mColorPrimary;
            mColorBackground = other.mColorBackground;
        }

        private TaskDescription(android.os.Parcel source) {
            readFromParcel(source);
        }

        /**
         * Sets the label for this task description.
         *
         * @unknown 
         */
        public void setLabel(java.lang.String label) {
            mLabel = label;
        }

        /**
         * Sets the primary color for this task description.
         *
         * @unknown 
         */
        public void setPrimaryColor(int primaryColor) {
            // Ensure that the given color is valid
            if ((primaryColor != 0) && (android.graphics.Color.alpha(primaryColor) != 255)) {
                throw new java.lang.RuntimeException("A TaskDescription's primary color should be opaque");
            }
            mColorPrimary = primaryColor;
        }

        /**
         * Sets the background color for this task description.
         *
         * @unknown 
         */
        public void setBackgroundColor(int backgroundColor) {
            // Ensure that the given color is valid
            if ((backgroundColor != 0) && (android.graphics.Color.alpha(backgroundColor) != 255)) {
                throw new java.lang.RuntimeException("A TaskDescription's background color should be opaque");
            }
            mColorBackground = backgroundColor;
        }

        /**
         * Sets the icon for this task description.
         *
         * @unknown 
         */
        public void setIcon(android.graphics.Bitmap icon) {
            mIcon = icon;
        }

        /**
         * Moves the icon bitmap reference from an actual Bitmap to a file containing the
         * bitmap.
         *
         * @unknown 
         */
        public void setIconFilename(java.lang.String iconFilename) {
            mIconFilename = iconFilename;
            mIcon = null;
        }

        /**
         *
         *
         * @return The label and description of the current state of this task.
         */
        public java.lang.String getLabel() {
            return mLabel;
        }

        /**
         *
         *
         * @return The icon that represents the current state of this task.
         */
        public android.graphics.Bitmap getIcon() {
            if (mIcon != null) {
                return mIcon;
            }
            return android.app.ActivityManager.TaskDescription.loadTaskDescriptionIcon(mIconFilename, android.os.UserHandle.myUserId());
        }

        /**
         *
         *
         * @unknown 
         */
        public java.lang.String getIconFilename() {
            return mIconFilename;
        }

        /**
         *
         *
         * @unknown 
         */
        public android.graphics.Bitmap getInMemoryIcon() {
            return mIcon;
        }

        /**
         *
         *
         * @unknown 
         */
        public static android.graphics.Bitmap loadTaskDescriptionIcon(java.lang.String iconFilename, int userId) {
            if (iconFilename != null) {
                try {
                    return android.app.ActivityManagerNative.getDefault().getTaskDescriptionIcon(iconFilename, userId);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return null;
        }

        /**
         *
         *
         * @return The color override on the theme's primary color.
         */
        public int getPrimaryColor() {
            return mColorPrimary;
        }

        /**
         *
         *
         * @return The background color.
         * @unknown 
         */
        public int getBackgroundColor() {
            return mColorBackground;
        }

        /**
         *
         *
         * @unknown 
         */
        public void saveToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            if (mLabel != null) {
                out.attribute(null, android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONLABEL, mLabel);
            }
            if (mColorPrimary != 0) {
                out.attribute(null, android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONCOLOR_PRIMARY, java.lang.Integer.toHexString(mColorPrimary));
            }
            if (mColorBackground != 0) {
                out.attribute(null, android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONCOLOR_BACKGROUND, java.lang.Integer.toHexString(mColorBackground));
            }
            if (mIconFilename != null) {
                out.attribute(null, android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONICONFILENAME, mIconFilename);
            }
        }

        /**
         *
         *
         * @unknown 
         */
        public void restoreFromXml(java.lang.String attrName, java.lang.String attrValue) {
            if (android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONLABEL.equals(attrName)) {
                setLabel(attrValue);
            } else
                if (android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONCOLOR_PRIMARY.equals(attrName)) {
                    setPrimaryColor(((int) (java.lang.Long.parseLong(attrValue, 16))));
                } else
                    if (android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONCOLOR_BACKGROUND.equals(attrName)) {
                        setBackgroundColor(((int) (java.lang.Long.parseLong(attrValue, 16))));
                    } else
                        if (android.app.ActivityManager.TaskDescription.ATTR_TASKDESCRIPTIONICONFILENAME.equals(attrName)) {
                            setIconFilename(attrValue);
                        }



        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            if (mLabel == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                dest.writeString(mLabel);
            }
            if (mIcon == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                mIcon.writeToParcel(dest, 0);
            }
            dest.writeInt(mColorPrimary);
            dest.writeInt(mColorBackground);
            if (mIconFilename == null) {
                dest.writeInt(0);
            } else {
                dest.writeInt(1);
                dest.writeString(mIconFilename);
            }
        }

        public void readFromParcel(android.os.Parcel source) {
            mLabel = (source.readInt() > 0) ? source.readString() : null;
            mIcon = (source.readInt() > 0) ? android.graphics.Bitmap.CREATOR.createFromParcel(source) : null;
            mColorPrimary = source.readInt();
            mColorBackground = source.readInt();
            mIconFilename = (source.readInt() > 0) ? source.readString() : null;
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.TaskDescription> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.TaskDescription>() {
            public android.app.ActivityManager.TaskDescription createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.TaskDescription(source);
            }

            public android.app.ActivityManager.TaskDescription[] newArray(int size) {
                return new android.app.ActivityManager.TaskDescription[size];
            }
        };

        @java.lang.Override
        public java.lang.String toString() {
            return (((((((("TaskDescription Label: " + mLabel) + " Icon: ") + mIcon) + " IconFilename: ") + mIconFilename) + " colorPrimary: ") + mColorPrimary) + " colorBackground: ") + mColorBackground;
        }
    }

    /**
     * Information you can retrieve about tasks that the user has most recently
     * started or visited.
     */
    public static class RecentTaskInfo implements android.os.Parcelable {
        /**
         * If this task is currently running, this is the identifier for it.
         * If it is not running, this will be -1.
         */
        public int id;

        /**
         * The true identifier of this task, valid even if it is not running.
         */
        public int persistentId;

        /**
         * The original Intent used to launch the task.  You can use this
         * Intent to re-launch the task (if it is no longer running) or bring
         * the current task to the front.
         */
        public android.content.Intent baseIntent;

        /**
         * If this task was started from an alias, this is the actual
         * activity component that was initially started; the component of
         * the baseIntent in this case is the name of the actual activity
         * implementation that the alias referred to.  Otherwise, this is null.
         */
        public android.content.ComponentName origActivity;

        /**
         * The actual activity component that started the task.
         *
         * @unknown 
         */
        @android.annotation.Nullable
        public android.content.ComponentName realActivity;

        /**
         * Description of the task's last state.
         */
        public java.lang.CharSequence description;

        /**
         * The id of the ActivityStack this Task was on most recently.
         *
         * @unknown 
         */
        public int stackId;

        /**
         * The id of the user the task was running as.
         *
         * @unknown 
         */
        public int userId;

        /**
         * The first time this task was active.
         *
         * @unknown 
         */
        public long firstActiveTime;

        /**
         * The last time this task was active.
         *
         * @unknown 
         */
        public long lastActiveTime;

        /**
         * The recent activity values for the highest activity in the stack to have set the values.
         * {@link Activity#setTaskDescription(android.app.ActivityManager.TaskDescription)}.
         */
        public android.app.ActivityManager.TaskDescription taskDescription;

        /**
         * Task affiliation for grouping with other tasks.
         */
        public int affiliatedTaskId;

        /**
         * Task affiliation color of the source task with the affiliated task id.
         *
         * @unknown 
         */
        public int affiliatedTaskColor;

        /**
         * The component launched as the first activity in the task.
         * This can be considered the "application" of this task.
         */
        public android.content.ComponentName baseActivity;

        /**
         * The activity component at the top of the history stack of the task.
         * This is what the user is currently doing.
         */
        public android.content.ComponentName topActivity;

        /**
         * Number of activities in this task.
         */
        public int numActivities;

        /**
         * The bounds of the task.
         *
         * @unknown 
         */
        public android.graphics.Rect bounds;

        /**
         * True if the task can go in the docked stack.
         *
         * @unknown 
         */
        public boolean isDockable;

        /**
         * The resize mode of the task. See {@link ActivityInfo#resizeMode}.
         *
         * @unknown 
         */
        public int resizeMode;

        public RecentTaskInfo() {
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(persistentId);
            if (baseIntent != null) {
                dest.writeInt(1);
                baseIntent.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            android.content.ComponentName.writeToParcel(origActivity, dest);
            android.content.ComponentName.writeToParcel(realActivity, dest);
            android.text.TextUtils.writeToParcel(description, dest, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            if (taskDescription != null) {
                dest.writeInt(1);
                taskDescription.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(stackId);
            dest.writeInt(userId);
            dest.writeLong(firstActiveTime);
            dest.writeLong(lastActiveTime);
            dest.writeInt(affiliatedTaskId);
            dest.writeInt(affiliatedTaskColor);
            android.content.ComponentName.writeToParcel(baseActivity, dest);
            android.content.ComponentName.writeToParcel(topActivity, dest);
            dest.writeInt(numActivities);
            if (bounds != null) {
                dest.writeInt(1);
                bounds.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            dest.writeInt(isDockable ? 1 : 0);
            dest.writeInt(resizeMode);
        }

        public void readFromParcel(android.os.Parcel source) {
            id = source.readInt();
            persistentId = source.readInt();
            baseIntent = (source.readInt() > 0) ? android.content.Intent.CREATOR.createFromParcel(source) : null;
            origActivity = android.content.ComponentName.readFromParcel(source);
            realActivity = android.content.ComponentName.readFromParcel(source);
            description = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
            taskDescription = (source.readInt() > 0) ? android.app.ActivityManager.TaskDescription.CREATOR.createFromParcel(source) : null;
            stackId = source.readInt();
            userId = source.readInt();
            firstActiveTime = source.readLong();
            lastActiveTime = source.readLong();
            affiliatedTaskId = source.readInt();
            affiliatedTaskColor = source.readInt();
            baseActivity = android.content.ComponentName.readFromParcel(source);
            topActivity = android.content.ComponentName.readFromParcel(source);
            numActivities = source.readInt();
            bounds = (source.readInt() > 0) ? android.graphics.Rect.CREATOR.createFromParcel(source) : null;
            isDockable = source.readInt() == 1;
            resizeMode = source.readInt();
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.RecentTaskInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.RecentTaskInfo>() {
            public android.app.ActivityManager.RecentTaskInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.RecentTaskInfo(source);
            }

            public android.app.ActivityManager.RecentTaskInfo[] newArray(int size) {
                return new android.app.ActivityManager.RecentTaskInfo[size];
            }
        };

        private RecentTaskInfo(android.os.Parcel source) {
            readFromParcel(source);
        }
    }

    /**
     * Flag for use with {@link #getRecentTasks}: return all tasks, even those
     * that have set their
     * {@link android.content.Intent#FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS} flag.
     */
    public static final int RECENT_WITH_EXCLUDED = 0x1;

    /**
     * Provides a list that does not contain any
     * recent tasks that currently are not available to the user.
     */
    public static final int RECENT_IGNORE_UNAVAILABLE = 0x2;

    /**
     * Provides a list that contains recent tasks for all
     * profiles of a user.
     *
     * @unknown 
     */
    public static final int RECENT_INCLUDE_PROFILES = 0x4;

    /**
     * Ignores all tasks that are on the home stack.
     *
     * @unknown 
     */
    public static final int RECENT_IGNORE_HOME_STACK_TASKS = 0x8;

    /**
     * Ignores the top task in the docked stack.
     *
     * @unknown 
     */
    public static final int RECENT_INGORE_DOCKED_STACK_TOP_TASK = 0x10;

    /**
     * Ignores all tasks that are on the pinned stack.
     *
     * @unknown 
     */
    public static final int RECENT_INGORE_PINNED_STACK_TASKS = 0x20;

    /**
     * <p></p>Return a list of the tasks that the user has recently launched, with
     * the most recent being first and older ones after in order.
     *
     * <p><b>Note: this method is only intended for debugging and presenting
     * task management user interfaces</b>.  This should never be used for
     * core logic in an application, such as deciding between different
     * behaviors based on the information found here.  Such uses are
     * <em>not</em> supported, and will likely break in the future.  For
     * example, if multiple applications can be actively running at the
     * same time, assumptions made about the meaning of the data here for
     * purposes of control flow will be incorrect.</p>
     *
     * @deprecated As of {@link android.os.Build.VERSION_CODES#LOLLIPOP}, this method is
    no longer available to third party applications: the introduction of
    document-centric recents means
    it can leak personal information to the caller.  For backwards compatibility,
    it will still return a small subset of its data: at least the caller's
    own tasks (though see {@link #getAppTasks()} for the correct supported
    way to retrieve that information), and possibly some other tasks
    such as home that are known to not be sensitive.
     * @param maxNum
     * 		The maximum number of entries to return in the list.  The
     * 		actual number returned may be smaller, depending on how many tasks the
     * 		user has started and the maximum number the system can remember.
     * @param flags
     * 		Information about what to return.  May be any combination
     * 		of {@link #RECENT_WITH_EXCLUDED} and {@link #RECENT_IGNORE_UNAVAILABLE}.
     * @return Returns a list of RecentTaskInfo records describing each of
    the recent tasks.
     */
    @java.lang.Deprecated
    public java.util.List<android.app.ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags) throws java.lang.SecurityException {
        try {
            return android.app.ActivityManagerNative.getDefault().getRecentTasks(maxNum, flags, android.os.UserHandle.myUserId()).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Same as {@link #getRecentTasks(int, int)} but returns the recent tasks for a
     * specific user. It requires holding
     * the {@link android.Manifest.permission#INTERACT_ACROSS_USERS_FULL} permission.
     *
     * @param maxNum
     * 		The maximum number of entries to return in the list.  The
     * 		actual number returned may be smaller, depending on how many tasks the
     * 		user has started and the maximum number the system can remember.
     * @param flags
     * 		Information about what to return.  May be any combination
     * 		of {@link #RECENT_WITH_EXCLUDED} and {@link #RECENT_IGNORE_UNAVAILABLE}.
     * @return Returns a list of RecentTaskInfo records describing each of
    the recent tasks. Most recently activated tasks go first.
     * @unknown 
     */
    public java.util.List<android.app.ActivityManager.RecentTaskInfo> getRecentTasksForUser(int maxNum, int flags, int userId) throws java.lang.SecurityException {
        try {
            return android.app.ActivityManagerNative.getDefault().getRecentTasks(maxNum, flags, userId).getList();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Information you can retrieve about a particular task that is currently
     * "running" in the system.  Note that a running task does not mean the
     * given task actually has a process it is actively running in; it simply
     * means that the user has gone to it and never closed it, but currently
     * the system may have killed its process and is only holding on to its
     * last state in order to restart it when the user returns.
     */
    public static class RunningTaskInfo implements android.os.Parcelable {
        /**
         * A unique identifier for this task.
         */
        public int id;

        /**
         * The stack that currently contains this task.
         *
         * @unknown 
         */
        public int stackId;

        /**
         * The component launched as the first activity in the task.  This can
         * be considered the "application" of this task.
         */
        public android.content.ComponentName baseActivity;

        /**
         * The activity component at the top of the history stack of the task.
         * This is what the user is currently doing.
         */
        public android.content.ComponentName topActivity;

        /**
         * Thumbnail representation of the task's current state.  Currently
         * always null.
         */
        public android.graphics.Bitmap thumbnail;

        /**
         * Description of the task's current state.
         */
        public java.lang.CharSequence description;

        /**
         * Number of activities in this task.
         */
        public int numActivities;

        /**
         * Number of activities that are currently running (not stopped
         * and persisted) in this task.
         */
        public int numRunning;

        /**
         * Last time task was run. For sorting.
         *
         * @unknown 
         */
        public long lastActiveTime;

        /**
         * True if the task can go in the docked stack.
         *
         * @unknown 
         */
        public boolean isDockable;

        /**
         * The resize mode of the task. See {@link ActivityInfo#resizeMode}.
         *
         * @unknown 
         */
        public int resizeMode;

        public RunningTaskInfo() {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeInt(stackId);
            android.content.ComponentName.writeToParcel(baseActivity, dest);
            android.content.ComponentName.writeToParcel(topActivity, dest);
            if (thumbnail != null) {
                dest.writeInt(1);
                thumbnail.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
            android.text.TextUtils.writeToParcel(description, dest, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
            dest.writeInt(numActivities);
            dest.writeInt(numRunning);
            dest.writeInt(isDockable ? 1 : 0);
            dest.writeInt(resizeMode);
        }

        public void readFromParcel(android.os.Parcel source) {
            id = source.readInt();
            stackId = source.readInt();
            baseActivity = android.content.ComponentName.readFromParcel(source);
            topActivity = android.content.ComponentName.readFromParcel(source);
            if (source.readInt() != 0) {
                thumbnail = android.graphics.Bitmap.CREATOR.createFromParcel(source);
            } else {
                thumbnail = null;
            }
            description = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
            numActivities = source.readInt();
            numRunning = source.readInt();
            isDockable = source.readInt() != 0;
            resizeMode = source.readInt();
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.RunningTaskInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.RunningTaskInfo>() {
            public android.app.ActivityManager.RunningTaskInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.RunningTaskInfo(source);
            }

            public android.app.ActivityManager.RunningTaskInfo[] newArray(int size) {
                return new android.app.ActivityManager.RunningTaskInfo[size];
            }
        };

        private RunningTaskInfo(android.os.Parcel source) {
            readFromParcel(source);
        }
    }

    /**
     * Get the list of tasks associated with the calling application.
     *
     * @return The list of tasks associated with the application making this call.
     * @throws SecurityException
     * 		
     */
    public java.util.List<android.app.ActivityManager.AppTask> getAppTasks() {
        java.util.ArrayList<android.app.ActivityManager.AppTask> tasks = new java.util.ArrayList<android.app.ActivityManager.AppTask>();
        java.util.List<android.app.IAppTask> appTasks;
        try {
            appTasks = android.app.ActivityManagerNative.getDefault().getAppTasks(mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        int numAppTasks = appTasks.size();
        for (int i = 0; i < numAppTasks; i++) {
            tasks.add(new android.app.ActivityManager.AppTask(appTasks.get(i)));
        }
        return tasks;
    }

    /**
     * Return the current design dimensions for {@link AppTask} thumbnails, for use
     * with {@link #addAppTask}.
     */
    public android.util.Size getAppTaskThumbnailSize() {
        synchronized(this) {
            ensureAppTaskThumbnailSizeLocked();
            return new android.util.Size(mAppTaskThumbnailSize.x, mAppTaskThumbnailSize.y);
        }
    }

    private void ensureAppTaskThumbnailSizeLocked() {
        if (mAppTaskThumbnailSize == null) {
            try {
                mAppTaskThumbnailSize = android.app.ActivityManagerNative.getDefault().getAppTaskThumbnailSize();
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    /**
     * Add a new {@link AppTask} for the calling application.  This will create a new
     * recents entry that is added to the <b>end</b> of all existing recents.
     *
     * @param activity
     * 		The activity that is adding the entry.   This is used to help determine
     * 		the context that the new recents entry will be in.
     * @param intent
     * 		The Intent that describes the recents entry.  This is the same Intent that
     * 		you would have used to launch the activity for it.  In generally you will want to set
     * 		both {@link Intent#FLAG_ACTIVITY_NEW_DOCUMENT} and
     * 		{@link Intent#FLAG_ACTIVITY_RETAIN_IN_RECENTS}; the latter is required since this recents
     * 		entry will exist without an activity, so it doesn't make sense to not retain it when
     * 		its activity disappears.  The given Intent here also must have an explicit ComponentName
     * 		set on it.
     * @param description
     * 		Optional additional description information.
     * @param thumbnail
     * 		Thumbnail to use for the recents entry.  Should be the size given by
     * 		{@link #getAppTaskThumbnailSize()}.  If the bitmap is not that exact size, it will be
     * 		recreated in your process, probably in a way you don't like, before the recents entry
     * 		is added.
     * @return Returns the task id of the newly added app task, or -1 if the add failed.  The
    most likely cause of failure is that there is no more room for more tasks for your app.
     */
    public int addAppTask(@android.annotation.NonNull
    android.app.Activity activity, @android.annotation.NonNull
    android.content.Intent intent, @android.annotation.Nullable
    android.app.ActivityManager.TaskDescription description, @android.annotation.NonNull
    android.graphics.Bitmap thumbnail) {
        android.graphics.Point size;
        synchronized(this) {
            ensureAppTaskThumbnailSizeLocked();
            size = mAppTaskThumbnailSize;
        }
        final int tw = thumbnail.getWidth();
        final int th = thumbnail.getHeight();
        if ((tw != size.x) || (th != size.y)) {
            android.graphics.Bitmap bm = android.graphics.Bitmap.createBitmap(size.x, size.y, thumbnail.getConfig());
            // Use ScaleType.CENTER_CROP, except we leave the top edge at the top.
            float scale;
            float dx = 0;
            float dy = 0;
            if ((tw * size.x) > (size.y * th)) {
                scale = ((float) (size.x)) / ((float) (th));
                dx = (size.y - (tw * scale)) * 0.5F;
            } else {
                scale = ((float) (size.y)) / ((float) (tw));
                dy = (size.x - (th * scale)) * 0.5F;
            }
            android.graphics.Matrix matrix = new android.graphics.Matrix();
            matrix.setScale(scale, scale);
            matrix.postTranslate(((int) (dx + 0.5F)), 0);
            android.graphics.Canvas canvas = new android.graphics.Canvas(bm);
            canvas.drawBitmap(thumbnail, matrix, null);
            canvas.setBitmap(null);
            thumbnail = bm;
        }
        if (description == null) {
            description = new android.app.ActivityManager.TaskDescription();
        }
        try {
            return android.app.ActivityManagerNative.getDefault().addAppTask(activity.getActivityToken(), intent, description, thumbnail);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return a list of the tasks that are currently running, with
     * the most recent being first and older ones after in order.  Note that
     * "running" does not mean any of the task's code is currently loaded or
     * activity -- the task may have been frozen by the system, so that it
     * can be restarted in its previous state when next brought to the
     * foreground.
     *
     * <p><b>Note: this method is only intended for debugging and presenting
     * task management user interfaces</b>.  This should never be used for
     * core logic in an application, such as deciding between different
     * behaviors based on the information found here.  Such uses are
     * <em>not</em> supported, and will likely break in the future.  For
     * example, if multiple applications can be actively running at the
     * same time, assumptions made about the meaning of the data here for
     * purposes of control flow will be incorrect.</p>
     *
     * @deprecated As of {@link android.os.Build.VERSION_CODES#LOLLIPOP}, this method
    is no longer available to third party
    applications: the introduction of document-centric recents means
    it can leak person information to the caller.  For backwards compatibility,
    it will still retu rn a small subset of its data: at least the caller's
    own tasks, and possibly some other tasks
    such as home that are known to not be sensitive.
     * @param maxNum
     * 		The maximum number of entries to return in the list.  The
     * 		actual number returned may be smaller, depending on how many tasks the
     * 		user has started.
     * @return Returns a list of RunningTaskInfo records describing each of
    the running tasks.
     */
    @java.lang.Deprecated
    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getRunningTasks(int maxNum) throws java.lang.SecurityException {
        try {
            return android.app.ActivityManagerNative.getDefault().getTasks(maxNum, 0);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Completely remove the given task.
     *
     * @param taskId
     * 		Identifier of the task to be removed.
     * @return Returns true if the given task was found and removed.
     * @unknown 
     */
    public boolean removeTask(int taskId) throws java.lang.SecurityException {
        try {
            return android.app.ActivityManagerNative.getDefault().removeTask(taskId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Metadata related to the {@link TaskThumbnail}.
     *
     * @unknown 
     */
    public static class TaskThumbnailInfo implements android.os.Parcelable {
        /**
         *
         *
         * @unknown 
         */
        public static final java.lang.String ATTR_TASK_THUMBNAILINFO_PREFIX = "task_thumbnailinfo_";

        private static final java.lang.String ATTR_TASK_WIDTH = android.app.ActivityManager.TaskThumbnailInfo.ATTR_TASK_THUMBNAILINFO_PREFIX + "task_width";

        private static final java.lang.String ATTR_TASK_HEIGHT = android.app.ActivityManager.TaskThumbnailInfo.ATTR_TASK_THUMBNAILINFO_PREFIX + "task_height";

        private static final java.lang.String ATTR_SCREEN_ORIENTATION = android.app.ActivityManager.TaskThumbnailInfo.ATTR_TASK_THUMBNAILINFO_PREFIX + "screen_orientation";

        public int taskWidth;

        public int taskHeight;

        public int screenOrientation = android.content.res.Configuration.ORIENTATION_UNDEFINED;

        public TaskThumbnailInfo() {
            // Do nothing
        }

        private TaskThumbnailInfo(android.os.Parcel source) {
            readFromParcel(source);
        }

        /**
         * Resets this info state to the initial state.
         *
         * @unknown 
         */
        public void reset() {
            taskWidth = 0;
            taskHeight = 0;
            screenOrientation = android.content.res.Configuration.ORIENTATION_UNDEFINED;
        }

        /**
         * Copies from another ThumbnailInfo.
         */
        public void copyFrom(android.app.ActivityManager.TaskThumbnailInfo o) {
            taskWidth = o.taskWidth;
            taskHeight = o.taskHeight;
            screenOrientation = o.screenOrientation;
        }

        /**
         *
         *
         * @unknown 
         */
        public void saveToXml(org.xmlpull.v1.XmlSerializer out) throws java.io.IOException {
            out.attribute(null, android.app.ActivityManager.TaskThumbnailInfo.ATTR_TASK_WIDTH, java.lang.Integer.toString(taskWidth));
            out.attribute(null, android.app.ActivityManager.TaskThumbnailInfo.ATTR_TASK_HEIGHT, java.lang.Integer.toString(taskHeight));
            out.attribute(null, android.app.ActivityManager.TaskThumbnailInfo.ATTR_SCREEN_ORIENTATION, java.lang.Integer.toString(screenOrientation));
        }

        /**
         *
         *
         * @unknown 
         */
        public void restoreFromXml(java.lang.String attrName, java.lang.String attrValue) {
            if (android.app.ActivityManager.TaskThumbnailInfo.ATTR_TASK_WIDTH.equals(attrName)) {
                taskWidth = java.lang.Integer.parseInt(attrValue);
            } else
                if (android.app.ActivityManager.TaskThumbnailInfo.ATTR_TASK_HEIGHT.equals(attrName)) {
                    taskHeight = java.lang.Integer.parseInt(attrValue);
                } else
                    if (android.app.ActivityManager.TaskThumbnailInfo.ATTR_SCREEN_ORIENTATION.equals(attrName)) {
                        screenOrientation = java.lang.Integer.parseInt(attrValue);
                    }


        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(taskWidth);
            dest.writeInt(taskHeight);
            dest.writeInt(screenOrientation);
        }

        public void readFromParcel(android.os.Parcel source) {
            taskWidth = source.readInt();
            taskHeight = source.readInt();
            screenOrientation = source.readInt();
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.TaskThumbnailInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.TaskThumbnailInfo>() {
            public android.app.ActivityManager.TaskThumbnailInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.TaskThumbnailInfo(source);
            }

            public android.app.ActivityManager.TaskThumbnailInfo[] newArray(int size) {
                return new android.app.ActivityManager.TaskThumbnailInfo[size];
            }
        };
    }

    /**
     *
     *
     * @unknown 
     */
    public static class TaskThumbnail implements android.os.Parcelable {
        public android.graphics.Bitmap mainThumbnail;

        public android.os.ParcelFileDescriptor thumbnailFileDescriptor;

        public android.app.ActivityManager.TaskThumbnailInfo thumbnailInfo;

        public TaskThumbnail() {
        }

        private TaskThumbnail(android.os.Parcel source) {
            readFromParcel(source);
        }

        public int describeContents() {
            if (thumbnailFileDescriptor != null) {
                return thumbnailFileDescriptor.describeContents();
            }
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            if (mainThumbnail != null) {
                dest.writeInt(1);
                mainThumbnail.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            if (thumbnailFileDescriptor != null) {
                dest.writeInt(1);
                thumbnailFileDescriptor.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
            if (thumbnailInfo != null) {
                dest.writeInt(1);
                thumbnailInfo.writeToParcel(dest, flags);
            } else {
                dest.writeInt(0);
            }
        }

        public void readFromParcel(android.os.Parcel source) {
            if (source.readInt() != 0) {
                mainThumbnail = android.graphics.Bitmap.CREATOR.createFromParcel(source);
            } else {
                mainThumbnail = null;
            }
            if (source.readInt() != 0) {
                thumbnailFileDescriptor = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(source);
            } else {
                thumbnailFileDescriptor = null;
            }
            if (source.readInt() != 0) {
                thumbnailInfo = android.app.ActivityManager.TaskThumbnailInfo.CREATOR.createFromParcel(source);
            } else {
                thumbnailInfo = null;
            }
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.TaskThumbnail> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.TaskThumbnail>() {
            public android.app.ActivityManager.TaskThumbnail createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.TaskThumbnail(source);
            }

            public android.app.ActivityManager.TaskThumbnail[] newArray(int size) {
                return new android.app.ActivityManager.TaskThumbnail[size];
            }
        };
    }

    /**
     *
     *
     * @unknown 
     */
    public android.app.ActivityManager.TaskThumbnail getTaskThumbnail(int id) throws java.lang.SecurityException {
        try {
            return android.app.ActivityManagerNative.getDefault().getTaskThumbnail(id);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean isInHomeStack(int taskId) {
        try {
            return android.app.ActivityManagerNative.getDefault().isInHomeStack(taskId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Flag for {@link #moveTaskToFront(int, int)}: also move the "home"
     * activity along with the task, so it is positioned immediately behind
     * the task.
     */
    public static final int MOVE_TASK_WITH_HOME = 0x1;

    /**
     * Flag for {@link #moveTaskToFront(int, int)}: don't count this as a
     * user-instigated action, so the current activity will not receive a
     * hint that the user is leaving.
     */
    public static final int MOVE_TASK_NO_USER_ACTION = 0x2;

    /**
     * Equivalent to calling {@link #moveTaskToFront(int, int, Bundle)}
     * with a null options argument.
     *
     * @param taskId
     * 		The identifier of the task to be moved, as found in
     * 		{@link RunningTaskInfo} or {@link RecentTaskInfo}.
     * @param flags
     * 		Additional operational flags, 0 or more of
     * 		{@link #MOVE_TASK_WITH_HOME}, {@link #MOVE_TASK_NO_USER_ACTION}.
     */
    public void moveTaskToFront(int taskId, int flags) {
        moveTaskToFront(taskId, flags, null);
    }

    /**
     * Ask that the task associated with a given task ID be moved to the
     * front of the stack, so it is now visible to the user.  Requires that
     * the caller hold permission {@link android.Manifest.permission#REORDER_TASKS}
     * or a SecurityException will be thrown.
     *
     * @param taskId
     * 		The identifier of the task to be moved, as found in
     * 		{@link RunningTaskInfo} or {@link RecentTaskInfo}.
     * @param flags
     * 		Additional operational flags, 0 or more of
     * 		{@link #MOVE_TASK_WITH_HOME}, {@link #MOVE_TASK_NO_USER_ACTION}.
     * @param options
     * 		Additional options for the operation, either null or
     * 		as per {@link Context#startActivity(Intent, android.os.Bundle)
     * 		Context.startActivity(Intent, Bundle)}.
     */
    public void moveTaskToFront(int taskId, int flags, android.os.Bundle options) {
        try {
            android.app.ActivityManagerNative.getDefault().moveTaskToFront(taskId, flags, options);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Information you can retrieve about a particular Service that is
     * currently running in the system.
     */
    public static class RunningServiceInfo implements android.os.Parcelable {
        /**
         * The service component.
         */
        public android.content.ComponentName service;

        /**
         * If non-zero, this is the process the service is running in.
         */
        public int pid;

        /**
         * The UID that owns this service.
         */
        public int uid;

        /**
         * The name of the process this service runs in.
         */
        public java.lang.String process;

        /**
         * Set to true if the service has asked to run as a foreground process.
         */
        public boolean foreground;

        /**
         * The time when the service was first made active, either by someone
         * starting or binding to it.  This
         * is in units of {@link android.os.SystemClock#elapsedRealtime()}.
         */
        public long activeSince;

        /**
         * Set to true if this service has been explicitly started.
         */
        public boolean started;

        /**
         * Number of clients connected to the service.
         */
        public int clientCount;

        /**
         * Number of times the service's process has crashed while the service
         * is running.
         */
        public int crashCount;

        /**
         * The time when there was last activity in the service (either
         * explicit requests to start it or clients binding to it).  This
         * is in units of {@link android.os.SystemClock#uptimeMillis()}.
         */
        public long lastActivityTime;

        /**
         * If non-zero, this service is not currently running, but scheduled to
         * restart at the given time.
         */
        public long restarting;

        /**
         * Bit for {@link #flags}: set if this service has been
         * explicitly started.
         */
        public static final int FLAG_STARTED = 1 << 0;

        /**
         * Bit for {@link #flags}: set if the service has asked to
         * run as a foreground process.
         */
        public static final int FLAG_FOREGROUND = 1 << 1;

        /**
         * Bit for {@link #flags): set if the service is running in a
         * core system process.
         */
        public static final int FLAG_SYSTEM_PROCESS = 1 << 2;

        /**
         * Bit for {@link #flags): set if the service is running in a
         * persistent process.
         */
        public static final int FLAG_PERSISTENT_PROCESS = 1 << 3;

        /**
         * Running flags.
         */
        public int flags;

        /**
         * For special services that are bound to by system code, this is
         * the package that holds the binding.
         */
        public java.lang.String clientPackage;

        /**
         * For special services that are bound to by system code, this is
         * a string resource providing a user-visible label for who the
         * client is.
         */
        public int clientLabel;

        public RunningServiceInfo() {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            android.content.ComponentName.writeToParcel(service, dest);
            dest.writeInt(pid);
            dest.writeInt(uid);
            dest.writeString(process);
            dest.writeInt(foreground ? 1 : 0);
            dest.writeLong(activeSince);
            dest.writeInt(started ? 1 : 0);
            dest.writeInt(clientCount);
            dest.writeInt(crashCount);
            dest.writeLong(lastActivityTime);
            dest.writeLong(restarting);
            dest.writeInt(this.flags);
            dest.writeString(clientPackage);
            dest.writeInt(clientLabel);
        }

        public void readFromParcel(android.os.Parcel source) {
            service = android.content.ComponentName.readFromParcel(source);
            pid = source.readInt();
            uid = source.readInt();
            process = source.readString();
            foreground = source.readInt() != 0;
            activeSince = source.readLong();
            started = source.readInt() != 0;
            clientCount = source.readInt();
            crashCount = source.readInt();
            lastActivityTime = source.readLong();
            restarting = source.readLong();
            flags = source.readInt();
            clientPackage = source.readString();
            clientLabel = source.readInt();
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.RunningServiceInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.RunningServiceInfo>() {
            public android.app.ActivityManager.RunningServiceInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.RunningServiceInfo(source);
            }

            public android.app.ActivityManager.RunningServiceInfo[] newArray(int size) {
                return new android.app.ActivityManager.RunningServiceInfo[size];
            }
        };

        private RunningServiceInfo(android.os.Parcel source) {
            readFromParcel(source);
        }
    }

    /**
     * Return a list of the services that are currently running.
     *
     * <p><b>Note: this method is only intended for debugging or implementing
     * service management type user interfaces.</b></p>
     *
     * @param maxNum
     * 		The maximum number of entries to return in the list.  The
     * 		actual number returned may be smaller, depending on how many services
     * 		are running.
     * @return Returns a list of RunningServiceInfo records describing each of
    the running tasks.
     */
    public java.util.List<android.app.ActivityManager.RunningServiceInfo> getRunningServices(int maxNum) throws java.lang.SecurityException {
        try {
            return android.app.ActivityManagerNative.getDefault().getServices(maxNum, 0);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a PendingIntent you can start to show a control panel for the
     * given running service.  If the service does not have a control panel,
     * null is returned.
     */
    public android.app.PendingIntent getRunningServiceControlPanel(android.content.ComponentName service) throws java.lang.SecurityException {
        try {
            return android.app.ActivityManagerNative.getDefault().getRunningServiceControlPanel(service);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Information you can retrieve about the available memory through
     * {@link ActivityManager#getMemoryInfo}.
     */
    public static class MemoryInfo implements android.os.Parcelable {
        /**
         * The available memory on the system.  This number should not
         * be considered absolute: due to the nature of the kernel, a significant
         * portion of this memory is actually in use and needed for the overall
         * system to run well.
         */
        public long availMem;

        /**
         * The total memory accessible by the kernel.  This is basically the
         * RAM size of the device, not including below-kernel fixed allocations
         * like DMA buffers, RAM for the baseband CPU, etc.
         */
        public long totalMem;

        /**
         * The threshold of {@link #availMem} at which we consider memory to be
         * low and start killing background services and other non-extraneous
         * processes.
         */
        public long threshold;

        /**
         * Set to true if the system considers itself to currently be in a low
         * memory situation.
         */
        public boolean lowMemory;

        /**
         *
         *
         * @unknown 
         */
        public long hiddenAppThreshold;

        /**
         *
         *
         * @unknown 
         */
        public long secondaryServerThreshold;

        /**
         *
         *
         * @unknown 
         */
        public long visibleAppThreshold;

        /**
         *
         *
         * @unknown 
         */
        public long foregroundAppThreshold;

        public MemoryInfo() {
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeLong(availMem);
            dest.writeLong(totalMem);
            dest.writeLong(threshold);
            dest.writeInt(lowMemory ? 1 : 0);
            dest.writeLong(hiddenAppThreshold);
            dest.writeLong(secondaryServerThreshold);
            dest.writeLong(visibleAppThreshold);
            dest.writeLong(foregroundAppThreshold);
        }

        public void readFromParcel(android.os.Parcel source) {
            availMem = source.readLong();
            totalMem = source.readLong();
            threshold = source.readLong();
            lowMemory = source.readInt() != 0;
            hiddenAppThreshold = source.readLong();
            secondaryServerThreshold = source.readLong();
            visibleAppThreshold = source.readLong();
            foregroundAppThreshold = source.readLong();
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.MemoryInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.MemoryInfo>() {
            public android.app.ActivityManager.MemoryInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.MemoryInfo(source);
            }

            public android.app.ActivityManager.MemoryInfo[] newArray(int size) {
                return new android.app.ActivityManager.MemoryInfo[size];
            }
        };

        private MemoryInfo(android.os.Parcel source) {
            readFromParcel(source);
        }
    }

    /**
     * Return general information about the memory state of the system.  This
     * can be used to help decide how to manage your own memory, though note
     * that polling is not recommended and
     * {@link android.content.ComponentCallbacks2#onTrimMemory(int)
     * ComponentCallbacks2.onTrimMemory(int)} is the preferred way to do this.
     * Also see {@link #getMyMemoryState} for how to retrieve the current trim
     * level of your process as needed, which gives a better hint for how to
     * manage its memory.
     */
    public void getMemoryInfo(android.app.ActivityManager.MemoryInfo outInfo) {
        try {
            android.app.ActivityManagerNative.getDefault().getMemoryInfo(outInfo);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Information you can retrieve about an ActivityStack in the system.
     *
     * @unknown 
     */
    public static class StackInfo implements android.os.Parcelable {
        public int stackId;

        public android.graphics.Rect bounds = new android.graphics.Rect();

        public int[] taskIds;

        public java.lang.String[] taskNames;

        public android.graphics.Rect[] taskBounds;

        public int[] taskUserIds;

        public android.content.ComponentName topActivity;

        public int displayId;

        public int userId;

        public boolean visible;

        // Index of the stack in the display's stack list, can be used for comparison of stack order
        public int position;

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(stackId);
            dest.writeInt(bounds.left);
            dest.writeInt(bounds.top);
            dest.writeInt(bounds.right);
            dest.writeInt(bounds.bottom);
            dest.writeIntArray(taskIds);
            dest.writeStringArray(taskNames);
            final int boundsCount = (taskBounds == null) ? 0 : taskBounds.length;
            dest.writeInt(boundsCount);
            for (int i = 0; i < boundsCount; i++) {
                dest.writeInt(taskBounds[i].left);
                dest.writeInt(taskBounds[i].top);
                dest.writeInt(taskBounds[i].right);
                dest.writeInt(taskBounds[i].bottom);
            }
            dest.writeIntArray(taskUserIds);
            dest.writeInt(displayId);
            dest.writeInt(userId);
            dest.writeInt(visible ? 1 : 0);
            dest.writeInt(position);
            if (topActivity != null) {
                dest.writeInt(1);
                topActivity.writeToParcel(dest, 0);
            } else {
                dest.writeInt(0);
            }
        }

        public void readFromParcel(android.os.Parcel source) {
            stackId = source.readInt();
            bounds = new android.graphics.Rect(source.readInt(), source.readInt(), source.readInt(), source.readInt());
            taskIds = source.createIntArray();
            taskNames = source.createStringArray();
            final int boundsCount = source.readInt();
            if (boundsCount > 0) {
                taskBounds = new android.graphics.Rect[boundsCount];
                for (int i = 0; i < boundsCount; i++) {
                    taskBounds[i] = new android.graphics.Rect();
                    taskBounds[i].set(source.readInt(), source.readInt(), source.readInt(), source.readInt());
                }
            } else {
                taskBounds = null;
            }
            taskUserIds = source.createIntArray();
            displayId = source.readInt();
            userId = source.readInt();
            visible = source.readInt() > 0;
            position = source.readInt();
            if (source.readInt() > 0) {
                topActivity = android.content.ComponentName.readFromParcel(source);
            }
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.StackInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.StackInfo>() {
            @java.lang.Override
            public android.app.ActivityManager.StackInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.StackInfo(source);
            }

            @java.lang.Override
            public android.app.ActivityManager.StackInfo[] newArray(int size) {
                return new android.app.ActivityManager.StackInfo[size];
            }
        };

        public StackInfo() {
        }

        private StackInfo(android.os.Parcel source) {
            readFromParcel(source);
        }

        public java.lang.String toString(java.lang.String prefix) {
            java.lang.StringBuilder sb = new java.lang.StringBuilder(256);
            sb.append(prefix);
            sb.append("Stack id=");
            sb.append(stackId);
            sb.append(" bounds=");
            sb.append(bounds.toShortString());
            sb.append(" displayId=");
            sb.append(displayId);
            sb.append(" userId=");
            sb.append(userId);
            sb.append("\n");
            prefix = prefix + "  ";
            for (int i = 0; i < taskIds.length; ++i) {
                sb.append(prefix);
                sb.append("taskId=");
                sb.append(taskIds[i]);
                sb.append(": ");
                sb.append(taskNames[i]);
                if (taskBounds != null) {
                    sb.append(" bounds=");
                    sb.append(taskBounds[i].toShortString());
                }
                sb.append(" userId=").append(taskUserIds[i]);
                sb.append(" visible=").append(visible);
                if (topActivity != null) {
                    sb.append(" topActivity=").append(topActivity);
                }
                sb.append("\n");
            }
            return sb.toString();
        }

        @java.lang.Override
        public java.lang.String toString() {
            return toString("");
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public boolean clearApplicationUserData(java.lang.String packageName, android.content.pm.IPackageDataObserver observer) {
        try {
            return android.app.ActivityManagerNative.getDefault().clearApplicationUserData(packageName, observer, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Permits an application to erase its own data from disk.  This is equivalent to
     * the user choosing to clear the app's data from within the device settings UI.  It
     * erases all dynamic data associated with the app -- its private data and data in its
     * private area on external storage -- but does not remove the installed application
     * itself, nor any OBB files.
     *
     * @return {@code true} if the application successfully requested that the application's
    data be erased; {@code false} otherwise.
     */
    public boolean clearApplicationUserData() {
        return clearApplicationUserData(mContext.getPackageName(), null);
    }

    /**
     * Permits an application to get the persistent URI permissions granted to another.
     *
     * <p>Typically called by Settings.
     *
     * @param packageName
     * 		application to look for the granted permissions
     * @return list of granted URI permissions
     * @unknown 
     */
    public android.content.pm.ParceledListSlice<android.content.UriPermission> getGrantedUriPermissions(java.lang.String packageName) {
        try {
            return android.app.ActivityManagerNative.getDefault().getGrantedUriPermissions(packageName, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Permits an application to clear the persistent URI permissions granted to another.
     *
     * <p>Typically called by Settings.
     *
     * @param packageName
     * 		application to clear its granted permissions
     * @unknown 
     */
    public void clearGrantedUriPermissions(java.lang.String packageName) {
        try {
            android.app.ActivityManagerNative.getDefault().clearGrantedUriPermissions(packageName, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Information you can retrieve about any processes that are in an error condition.
     */
    public static class ProcessErrorStateInfo implements android.os.Parcelable {
        /**
         * Condition codes
         */
        public static final int NO_ERROR = 0;

        public static final int CRASHED = 1;

        public static final int NOT_RESPONDING = 2;

        /**
         * The condition that the process is in.
         */
        public int condition;

        /**
         * The process name in which the crash or error occurred.
         */
        public java.lang.String processName;

        /**
         * The pid of this process; 0 if none
         */
        public int pid;

        /**
         * The kernel user-ID that has been assigned to this process;
         * currently this is not a unique ID (multiple applications can have
         * the same uid).
         */
        public int uid;

        /**
         * The activity name associated with the error, if known.  May be null.
         */
        public java.lang.String tag;

        /**
         * A short message describing the error condition.
         */
        public java.lang.String shortMsg;

        /**
         * A long message describing the error condition.
         */
        public java.lang.String longMsg;

        /**
         * The stack trace where the error originated.  May be null.
         */
        public java.lang.String stackTrace;

        /**
         * to be deprecated: This value will always be null.
         */
        public byte[] crashData = null;

        public ProcessErrorStateInfo() {
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(condition);
            dest.writeString(processName);
            dest.writeInt(pid);
            dest.writeInt(uid);
            dest.writeString(tag);
            dest.writeString(shortMsg);
            dest.writeString(longMsg);
            dest.writeString(stackTrace);
        }

        public void readFromParcel(android.os.Parcel source) {
            condition = source.readInt();
            processName = source.readString();
            pid = source.readInt();
            uid = source.readInt();
            tag = source.readString();
            shortMsg = source.readString();
            longMsg = source.readString();
            stackTrace = source.readString();
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.ProcessErrorStateInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.ProcessErrorStateInfo>() {
            public android.app.ActivityManager.ProcessErrorStateInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.ProcessErrorStateInfo(source);
            }

            public android.app.ActivityManager.ProcessErrorStateInfo[] newArray(int size) {
                return new android.app.ActivityManager.ProcessErrorStateInfo[size];
            }
        };

        private ProcessErrorStateInfo(android.os.Parcel source) {
            readFromParcel(source);
        }
    }

    /**
     * Returns a list of any processes that are currently in an error condition.  The result
     * will be null if all processes are running properly at this time.
     *
     * @return Returns a list of ProcessErrorStateInfo records, or null if there are no
    current error conditions (it will not return an empty list).  This list ordering is not
    specified.
     */
    public java.util.List<android.app.ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() {
        try {
            return android.app.ActivityManagerNative.getDefault().getProcessesInErrorState();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Information you can retrieve about a running process.
     */
    public static class RunningAppProcessInfo implements android.os.Parcelable {
        /**
         * The name of the process that this object is associated with
         */
        public java.lang.String processName;

        /**
         * The pid of this process; 0 if none
         */
        public int pid;

        /**
         * The user id of this process.
         */
        public int uid;

        /**
         * All packages that have been loaded into the process.
         */
        public java.lang.String[] pkgList;

        /**
         * Constant for {@link #flags}: this is an app that is unable to
         * correctly save its state when going to the background,
         * so it can not be killed while in the background.
         *
         * @unknown 
         */
        public static final int FLAG_CANT_SAVE_STATE = 1 << 0;

        /**
         * Constant for {@link #flags}: this process is associated with a
         * persistent system app.
         *
         * @unknown 
         */
        public static final int FLAG_PERSISTENT = 1 << 1;

        /**
         * Constant for {@link #flags}: this process is associated with a
         * persistent system app.
         *
         * @unknown 
         */
        public static final int FLAG_HAS_ACTIVITIES = 1 << 2;

        /**
         * Flags of information.  May be any of
         * {@link #FLAG_CANT_SAVE_STATE}.
         *
         * @unknown 
         */
        public int flags;

        /**
         * Last memory trim level reported to the process: corresponds to
         * the values supplied to {@link android.content.ComponentCallbacks2#onTrimMemory(int)
         * ComponentCallbacks2.onTrimMemory(int)}.
         */
        public int lastTrimLevel;

        /**
         * Constant for {@link #importance}: This process is running the
         * foreground UI; that is, it is the thing currently at the top of the screen
         * that the user is interacting with.
         */
        public static final int IMPORTANCE_FOREGROUND = 100;

        /**
         * Constant for {@link #importance}: This process is running a foreground
         * service, for example to perform music playback even while the user is
         * not immediately in the app.  This generally indicates that the process
         * is doing something the user actively cares about.
         */
        public static final int IMPORTANCE_FOREGROUND_SERVICE = 125;

        /**
         * Constant for {@link #importance}: This process is running the foreground
         * UI, but the device is asleep so it is not visible to the user.  This means
         * the user is not really aware of the process, because they can not see or
         * interact with it, but it is quite important because it what they expect to
         * return to once unlocking the device.
         */
        public static final int IMPORTANCE_TOP_SLEEPING = 150;

        /**
         * Constant for {@link #importance}: This process is running something
         * that is actively visible to the user, though not in the immediate
         * foreground.  This may be running a window that is behind the current
         * foreground (so paused and with its state saved, not interacting with
         * the user, but visible to them to some degree); it may also be running
         * other services under the system's control that it inconsiders important.
         */
        public static final int IMPORTANCE_VISIBLE = 200;

        /**
         * Constant for {@link #importance}: This process is not something the user
         * is directly aware of, but is otherwise perceptable to them to some degree.
         */
        public static final int IMPORTANCE_PERCEPTIBLE = 130;

        /**
         * Constant for {@link #importance}: This process is running an
         * application that can not save its state, and thus can't be killed
         * while in the background.
         *
         * @unknown 
         */
        public static final int IMPORTANCE_CANT_SAVE_STATE = 170;

        /**
         * Constant for {@link #importance}: This process is contains services
         * that should remain running.  These are background services apps have
         * started, not something the user is aware of, so they may be killed by
         * the system relatively freely (though it is generally desired that they
         * stay running as long as they want to).
         */
        public static final int IMPORTANCE_SERVICE = 300;

        /**
         * Constant for {@link #importance}: This process process contains
         * background code that is expendable.
         */
        public static final int IMPORTANCE_BACKGROUND = 400;

        /**
         * Constant for {@link #importance}: This process is empty of any
         * actively running code.
         */
        public static final int IMPORTANCE_EMPTY = 500;

        /**
         * Constant for {@link #importance}: This process does not exist.
         */
        public static final int IMPORTANCE_GONE = 1000;

        /**
         *
         *
         * @unknown 
         */
        public static int procStateToImportance(int procState) {
            if (procState == android.app.ActivityManager.PROCESS_STATE_NONEXISTENT) {
                return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE;
            } else
                if (procState >= android.app.ActivityManager.PROCESS_STATE_HOME) {
                    return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND;
                } else
                    if (procState >= android.app.ActivityManager.PROCESS_STATE_SERVICE) {
                        return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_SERVICE;
                    } else
                        if (procState > android.app.ActivityManager.PROCESS_STATE_HEAVY_WEIGHT) {
                            return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_CANT_SAVE_STATE;
                        } else
                            if (procState >= android.app.ActivityManager.PROCESS_STATE_IMPORTANT_BACKGROUND) {
                                return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_PERCEPTIBLE;
                            } else
                                if (procState >= android.app.ActivityManager.PROCESS_STATE_IMPORTANT_FOREGROUND) {
                                    return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;
                                } else
                                    if (procState >= android.app.ActivityManager.PROCESS_STATE_TOP_SLEEPING) {
                                        return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_TOP_SLEEPING;
                                    } else
                                        if (procState >= android.app.ActivityManager.PROCESS_STATE_FOREGROUND_SERVICE) {
                                            return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND_SERVICE;
                                        } else {
                                            return android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                                        }







        }

        /**
         * The relative importance level that the system places on this
         * process.  May be one of {@link #IMPORTANCE_FOREGROUND},
         * {@link #IMPORTANCE_VISIBLE}, {@link #IMPORTANCE_SERVICE},
         * {@link #IMPORTANCE_BACKGROUND}, or {@link #IMPORTANCE_EMPTY}.  These
         * constants are numbered so that "more important" values are always
         * smaller than "less important" values.
         */
        public int importance;

        /**
         * An additional ordering within a particular {@link #importance}
         * category, providing finer-grained information about the relative
         * utility of processes within a category.  This number means nothing
         * except that a smaller values are more recently used (and thus
         * more important).  Currently an LRU value is only maintained for
         * the {@link #IMPORTANCE_BACKGROUND} category, though others may
         * be maintained in the future.
         */
        public int lru;

        /**
         * Constant for {@link #importanceReasonCode}: nothing special has
         * been specified for the reason for this level.
         */
        public static final int REASON_UNKNOWN = 0;

        /**
         * Constant for {@link #importanceReasonCode}: one of the application's
         * content providers is being used by another process.  The pid of
         * the client process is in {@link #importanceReasonPid} and the
         * target provider in this process is in
         * {@link #importanceReasonComponent}.
         */
        public static final int REASON_PROVIDER_IN_USE = 1;

        /**
         * Constant for {@link #importanceReasonCode}: one of the application's
         * content providers is being used by another process.  The pid of
         * the client process is in {@link #importanceReasonPid} and the
         * target provider in this process is in
         * {@link #importanceReasonComponent}.
         */
        public static final int REASON_SERVICE_IN_USE = 2;

        /**
         * The reason for {@link #importance}, if any.
         */
        public int importanceReasonCode;

        /**
         * For the specified values of {@link #importanceReasonCode}, this
         * is the process ID of the other process that is a client of this
         * process.  This will be 0 if no other process is using this one.
         */
        public int importanceReasonPid;

        /**
         * For the specified values of {@link #importanceReasonCode}, this
         * is the name of the component that is being used in this process.
         */
        public android.content.ComponentName importanceReasonComponent;

        /**
         * When {@link #importanceReasonPid} is non-0, this is the importance
         * of the other pid. @hide
         */
        public int importanceReasonImportance;

        /**
         * Current process state, as per PROCESS_STATE_* constants.
         *
         * @unknown 
         */
        public int processState;

        public RunningAppProcessInfo() {
            importance = android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
            importanceReasonCode = android.app.ActivityManager.RunningAppProcessInfo.REASON_UNKNOWN;
            processState = android.app.ActivityManager.PROCESS_STATE_IMPORTANT_FOREGROUND;
        }

        public RunningAppProcessInfo(java.lang.String pProcessName, int pPid, java.lang.String[] pArr) {
            processName = pProcessName;
            pid = pPid;
            pkgList = pArr;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeString(processName);
            dest.writeInt(pid);
            dest.writeInt(uid);
            dest.writeStringArray(pkgList);
            dest.writeInt(this.flags);
            dest.writeInt(lastTrimLevel);
            dest.writeInt(importance);
            dest.writeInt(lru);
            dest.writeInt(importanceReasonCode);
            dest.writeInt(importanceReasonPid);
            android.content.ComponentName.writeToParcel(importanceReasonComponent, dest);
            dest.writeInt(importanceReasonImportance);
            dest.writeInt(processState);
        }

        public void readFromParcel(android.os.Parcel source) {
            processName = source.readString();
            pid = source.readInt();
            uid = source.readInt();
            pkgList = source.readStringArray();
            flags = source.readInt();
            lastTrimLevel = source.readInt();
            importance = source.readInt();
            lru = source.readInt();
            importanceReasonCode = source.readInt();
            importanceReasonPid = source.readInt();
            importanceReasonComponent = android.content.ComponentName.readFromParcel(source);
            importanceReasonImportance = source.readInt();
            processState = source.readInt();
        }

        public static final android.os.Parcelable.Creator<android.app.ActivityManager.RunningAppProcessInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ActivityManager.RunningAppProcessInfo>() {
            public android.app.ActivityManager.RunningAppProcessInfo createFromParcel(android.os.Parcel source) {
                return new android.app.ActivityManager.RunningAppProcessInfo(source);
            }

            public android.app.ActivityManager.RunningAppProcessInfo[] newArray(int size) {
                return new android.app.ActivityManager.RunningAppProcessInfo[size];
            }
        };

        private RunningAppProcessInfo(android.os.Parcel source) {
            readFromParcel(source);
        }
    }

    /**
     * Returns a list of application processes installed on external media
     * that are running on the device.
     *
     * <p><b>Note: this method is only intended for debugging or building
     * a user-facing process management UI.</b></p>
     *
     * @return Returns a list of ApplicationInfo records, or null if none
    This list ordering is not specified.
     * @unknown 
     */
    public java.util.List<android.content.pm.ApplicationInfo> getRunningExternalApplications() {
        try {
            return android.app.ActivityManagerNative.getDefault().getRunningExternalApplications();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Sets the memory trim mode for a process and schedules a memory trim operation.
     *
     * <p><b>Note: this method is only intended for testing framework.</b></p>
     *
     * @return Returns true if successful.
     * @unknown 
     */
    public boolean setProcessMemoryTrimLevel(java.lang.String process, int userId, int level) {
        try {
            return android.app.ActivityManagerNative.getDefault().setProcessMemoryTrimLevel(process, userId, level);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a list of application processes that are running on the device.
     *
     * <p><b>Note: this method is only intended for debugging or building
     * a user-facing process management UI.</b></p>
     *
     * @return Returns a list of RunningAppProcessInfo records, or null if there are no
    running processes (it will not return an empty list).  This list ordering is not
    specified.
     */
    public java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() {
        try {
            return android.app.ActivityManagerNative.getDefault().getRunningAppProcesses();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return the importance of a given package name, based on the processes that are
     * currently running.  The return value is one of the importance constants defined
     * in {@link RunningAppProcessInfo}, giving you the highest importance of all the
     * processes that this package has code running inside of.  If there are no processes
     * running its code, {@link RunningAppProcessInfo#IMPORTANCE_GONE} is returned.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public int getPackageImportance(java.lang.String packageName) {
        try {
            int procState = android.app.ActivityManagerNative.getDefault().getPackageProcessState(packageName, mContext.getOpPackageName());
            return android.app.ActivityManager.RunningAppProcessInfo.procStateToImportance(procState);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return global memory state information for the calling process.  This
     * does not fill in all fields of the {@link RunningAppProcessInfo}.  The
     * only fields that will be filled in are
     * {@link RunningAppProcessInfo#pid},
     * {@link RunningAppProcessInfo#uid},
     * {@link RunningAppProcessInfo#lastTrimLevel},
     * {@link RunningAppProcessInfo#importance},
     * {@link RunningAppProcessInfo#lru}, and
     * {@link RunningAppProcessInfo#importanceReasonCode}.
     */
    public static void getMyMemoryState(android.app.ActivityManager.RunningAppProcessInfo outState) {
        try {
            android.app.ActivityManagerNative.getDefault().getMyMemoryState(outState);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return information about the memory usage of one or more processes.
     *
     * <p><b>Note: this method is only intended for debugging or building
     * a user-facing process management UI.</b></p>
     *
     * @param pids
     * 		The pids of the processes whose memory usage is to be
     * 		retrieved.
     * @return Returns an array of memory information, one for each
    requested pid.
     */
    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) {
        try {
            return android.app.ActivityManagerNative.getDefault().getProcessMemoryInfo(pids);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @deprecated This is now just a wrapper for
    {@link #killBackgroundProcesses(String)}; the previous behavior here
    is no longer available to applications because it allows them to
    break other applications by removing their alarms, stopping their
    services, etc.
     */
    @java.lang.Deprecated
    public void restartPackage(java.lang.String packageName) {
        killBackgroundProcesses(packageName);
    }

    /**
     * Have the system immediately kill all background processes associated
     * with the given package.  This is the same as the kernel killing those
     * processes to reclaim memory; the system will take care of restarting
     * these processes in the future as needed.
     *
     * <p>You must hold the permission
     * {@link android.Manifest.permission#KILL_BACKGROUND_PROCESSES} to be able to
     * call this method.
     *
     * @param packageName
     * 		The name of the package whose processes are to
     * 		be killed.
     */
    public void killBackgroundProcesses(java.lang.String packageName) {
        try {
            android.app.ActivityManagerNative.getDefault().killBackgroundProcesses(packageName, android.os.UserHandle.myUserId());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Kills the specified UID.
     *
     * @param uid
     * 		The UID to kill.
     * @param reason
     * 		The reason for the kill.
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.RequiresPermission(Manifest.permission.KILL_UID)
    public void killUid(int uid, java.lang.String reason) {
        try {
            android.app.ActivityManagerNative.getDefault().killUid(android.os.UserHandle.getAppId(uid), android.os.UserHandle.getUserId(uid), reason);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Have the system perform a force stop of everything associated with
     * the given application package.  All processes that share its uid
     * will be killed, all services it has running stopped, all activities
     * removed, etc.  In addition, a {@link Intent#ACTION_PACKAGE_RESTARTED}
     * broadcast will be sent, so that any of its registered alarms can
     * be stopped, notifications removed, etc.
     *
     * <p>You must hold the permission
     * {@link android.Manifest.permission#FORCE_STOP_PACKAGES} to be able to
     * call this method.
     *
     * @param packageName
     * 		The name of the package to be stopped.
     * @param userId
     * 		The user for which the running package is to be stopped.
     * @unknown This is not available to third party applications due to
    it allowing them to break other applications by stopping their
    services, removing their alarms, etc.
     */
    public void forceStopPackageAsUser(java.lang.String packageName, int userId) {
        try {
            android.app.ActivityManagerNative.getDefault().forceStopPackage(packageName, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @see #forceStopPackageAsUser(String, int)
     * @unknown 
     */
    public void forceStopPackage(java.lang.String packageName) {
        forceStopPackageAsUser(packageName, android.os.UserHandle.myUserId());
    }

    /**
     * Get the device configuration attributes.
     */
    public android.content.pm.ConfigurationInfo getDeviceConfigurationInfo() {
        try {
            return android.app.ActivityManagerNative.getDefault().getDeviceConfigurationInfo();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Get the preferred density of icons for the launcher. This is used when
     * custom drawables are created (e.g., for shortcuts).
     *
     * @return density in terms of DPI
     */
    public int getLauncherLargeIconDensity() {
        final android.content.res.Resources res = mContext.getResources();
        final int density = res.getDisplayMetrics().densityDpi;
        final int sw = res.getConfiguration().smallestScreenWidthDp;
        if (sw < 600) {
            // Smaller than approx 7" tablets, use the regular icon size.
            return density;
        }
        switch (density) {
            case android.util.DisplayMetrics.DENSITY_LOW :
                return android.util.DisplayMetrics.DENSITY_MEDIUM;
            case android.util.DisplayMetrics.DENSITY_MEDIUM :
                return android.util.DisplayMetrics.DENSITY_HIGH;
            case android.util.DisplayMetrics.DENSITY_TV :
                return android.util.DisplayMetrics.DENSITY_XHIGH;
            case android.util.DisplayMetrics.DENSITY_HIGH :
                return android.util.DisplayMetrics.DENSITY_XHIGH;
            case android.util.DisplayMetrics.DENSITY_XHIGH :
                return android.util.DisplayMetrics.DENSITY_XXHIGH;
            case android.util.DisplayMetrics.DENSITY_XXHIGH :
                return android.util.DisplayMetrics.DENSITY_XHIGH * 2;
            default :
                // The density is some abnormal value.  Return some other
                // abnormal value that is a reasonable scaling of it.
                return ((int) ((density * 1.5F) + 0.5F));
        }
    }

    /**
     * Get the preferred launcher icon size. This is used when custom drawables
     * are created (e.g., for shortcuts).
     *
     * @return dimensions of square icons in terms of pixels
     */
    public int getLauncherLargeIconSize() {
        return android.app.ActivityManager.getLauncherLargeIconSizeInner(mContext);
    }

    static int getLauncherLargeIconSizeInner(android.content.Context context) {
        final android.content.res.Resources res = context.getResources();
        final int size = res.getDimensionPixelSize(android.R.dimen.app_icon_size);
        final int sw = res.getConfiguration().smallestScreenWidthDp;
        if (sw < 600) {
            // Smaller than approx 7" tablets, use the regular icon size.
            return size;
        }
        final int density = res.getDisplayMetrics().densityDpi;
        switch (density) {
            case android.util.DisplayMetrics.DENSITY_LOW :
                return (size * android.util.DisplayMetrics.DENSITY_MEDIUM) / android.util.DisplayMetrics.DENSITY_LOW;
            case android.util.DisplayMetrics.DENSITY_MEDIUM :
                return (size * android.util.DisplayMetrics.DENSITY_HIGH) / android.util.DisplayMetrics.DENSITY_MEDIUM;
            case android.util.DisplayMetrics.DENSITY_TV :
                return (size * android.util.DisplayMetrics.DENSITY_XHIGH) / android.util.DisplayMetrics.DENSITY_HIGH;
            case android.util.DisplayMetrics.DENSITY_HIGH :
                return (size * android.util.DisplayMetrics.DENSITY_XHIGH) / android.util.DisplayMetrics.DENSITY_HIGH;
            case android.util.DisplayMetrics.DENSITY_XHIGH :
                return (size * android.util.DisplayMetrics.DENSITY_XXHIGH) / android.util.DisplayMetrics.DENSITY_XHIGH;
            case android.util.DisplayMetrics.DENSITY_XXHIGH :
                return ((size * android.util.DisplayMetrics.DENSITY_XHIGH) * 2) / android.util.DisplayMetrics.DENSITY_XXHIGH;
            default :
                // The density is some abnormal value.  Return some other
                // abnormal value that is a reasonable scaling of it.
                return ((int) ((size * 1.5F) + 0.5F));
        }
    }

    /**
     * Returns "true" if the user interface is currently being messed with
     * by a monkey.
     */
    public static boolean isUserAMonkey() {
        try {
            return android.app.ActivityManagerNative.getDefault().isUserAMonkey();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns "true" if device is running in a test harness.
     */
    public static boolean isRunningInTestHarness() {
        return android.os.SystemProperties.getBoolean("ro.test_harness", false);
    }

    /**
     * Returns the launch count of each installed package.
     *
     * @unknown 
     */
    /* public Map<String, Integer> getAllPackageLaunchCounts() {
    try {
    IUsageStats usageStatsService = IUsageStats.Stub.asInterface(
    ServiceManager.getService("usagestats"));
    if (usageStatsService == null) {
    return new HashMap<String, Integer>();
    }

    UsageStats.PackageStats[] allPkgUsageStats = usageStatsService.getAllPkgUsageStats(
    ActivityThread.currentPackageName());
    if (allPkgUsageStats == null) {
    return new HashMap<String, Integer>();
    }

    Map<String, Integer> launchCounts = new HashMap<String, Integer>();
    for (UsageStats.PackageStats pkgUsageStats : allPkgUsageStats) {
    launchCounts.put(pkgUsageStats.getPackageName(), pkgUsageStats.getLaunchCount());
    }

    return launchCounts;
    } catch (RemoteException e) {
    Log.w(TAG, "Could not query launch counts", e);
    return new HashMap<String, Integer>();
    }
    }
     */
    /**
     *
     *
     * @unknown 
     */
    public static int checkComponentPermission(java.lang.String permission, int uid, int owningUid, boolean exported) {
        // Root, system server get to do everything.
        final int appId = android.os.UserHandle.getAppId(uid);
        if ((appId == android.os.Process.ROOT_UID) || (appId == android.os.Process.SYSTEM_UID)) {
            return android.content.pm.PackageManager.PERMISSION_GRANTED;
        }
        // Isolated processes don't get any permissions.
        if (android.os.UserHandle.isIsolated(uid)) {
            return android.content.pm.PackageManager.PERMISSION_DENIED;
        }
        // If there is a uid that owns whatever is being accessed, it has
        // blanket access to it regardless of the permissions it requires.
        if ((owningUid >= 0) && android.os.UserHandle.isSameApp(uid, owningUid)) {
            return android.content.pm.PackageManager.PERMISSION_GRANTED;
        }
        // If the target is not exported, then nobody else can get to it.
        if (!exported) {
            /* RuntimeException here = new RuntimeException("here");
            here.fillInStackTrace();
            Slog.w(TAG, "Permission denied: checkComponentPermission() owningUid=" + owningUid,
            here);
             */
            return android.content.pm.PackageManager.PERMISSION_DENIED;
        }
        if (permission == null) {
            return android.content.pm.PackageManager.PERMISSION_GRANTED;
        }
        try {
            return android.app.AppGlobals.getPackageManager().checkUidPermission(permission, uid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static int checkUidPermission(java.lang.String permission, int uid) {
        try {
            return android.app.AppGlobals.getPackageManager().checkUidPermission(permission, uid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown Helper for dealing with incoming user arguments to system service calls.
    Takes care of checking permissions and converting USER_CURRENT to the
    actual current user.
     * @param callingPid
     * 		The pid of the incoming call, as per Binder.getCallingPid().
     * @param callingUid
     * 		The uid of the incoming call, as per Binder.getCallingUid().
     * @param userId
     * 		The user id argument supplied by the caller -- this is the user
     * 		they want to run as.
     * @param allowAll
     * 		If true, we will allow USER_ALL.  This means you must be prepared
     * 		to get a USER_ALL returned and deal with it correctly.  If false,
     * 		an exception will be thrown if USER_ALL is supplied.
     * @param requireFull
     * 		If true, the caller must hold
     * 		{@link android.Manifest.permission#INTERACT_ACROSS_USERS_FULL} to be able to run as a
     * 		different user than their current process; otherwise they must hold
     * 		{@link android.Manifest.permission#INTERACT_ACROSS_USERS}.
     * @param name
     * 		Optional textual name of the incoming call; only for generating error messages.
     * @param callerPackage
     * 		Optional package name of caller; only for error messages.
     * @return Returns the user ID that the call should run as.  Will always be a concrete
    user number, unless <var>allowAll</var> is true in which case it could also be
    USER_ALL.
     */
    public static int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, java.lang.String name, java.lang.String callerPackage) {
        if (android.os.UserHandle.getUserId(callingUid) == userId) {
            return userId;
        }
        try {
            return android.app.ActivityManagerNative.getDefault().handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull, name, callerPackage);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the userId of the current foreground user. Requires system permissions.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static int getCurrentUser() {
        android.content.pm.UserInfo ui;
        try {
            ui = android.app.ActivityManagerNative.getDefault().getCurrentUser();
            return ui != null ? ui.id : 0;
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @param userid
     * 		the user's id. Zero indicates the default user.
     * @unknown 
     */
    public boolean switchUser(int userid) {
        try {
            return android.app.ActivityManagerNative.getDefault().switchUser(userid);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Logs out current current foreground user by switching to the system user and stopping the
     * user being switched from.
     *
     * @unknown 
     */
    public static void logoutCurrentUser() {
        int currentUser = android.app.ActivityManager.getCurrentUser();
        if (currentUser != android.os.UserHandle.USER_SYSTEM) {
            try {
                android.app.ActivityManagerNative.getDefault().switchUser(android.os.UserHandle.USER_SYSTEM);
                /* force= */
                android.app.ActivityManagerNative.getDefault().stopUser(currentUser, false, null);
            } catch (android.os.RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    /**
     * {@hide }
     */
    public static final int FLAG_OR_STOPPED = 1 << 0;

    /**
     * {@hide }
     */
    public static final int FLAG_AND_LOCKED = 1 << 1;

    /**
     * {@hide }
     */
    public static final int FLAG_AND_UNLOCKED = 1 << 2;

    /**
     * {@hide }
     */
    public static final int FLAG_AND_UNLOCKING_OR_UNLOCKED = 1 << 3;

    /**
     * Return whether the given user is actively running.  This means that
     * the user is in the "started" state, not "stopped" -- it is currently
     * allowed to run code through scheduled alarms, receiving broadcasts,
     * etc.  A started user may be either the current foreground user or a
     * background user; the result here does not distinguish between the two.
     *
     * @param userid
     * 		the user's id. Zero indicates the default user.
     * @unknown 
     */
    public boolean isUserRunning(int userId) {
        try {
            return android.app.ActivityManagerNative.getDefault().isUserRunning(userId, 0);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * {@hide }
     */
    public boolean isVrModePackageEnabled(android.content.ComponentName component) {
        try {
            return android.app.ActivityManagerNative.getDefault().isVrModePackageEnabled(component);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Perform a system dump of various state associated with the given application
     * package name.  This call blocks while the dump is being performed, so should
     * not be done on a UI thread.  The data will be written to the given file
     * descriptor as text.  An application must hold the
     * {@link android.Manifest.permission#DUMP} permission to make this call.
     *
     * @param fd
     * 		The file descriptor that the dump should be written to.  The file
     * 		descriptor is <em>not</em> closed by this function; the caller continues to
     * 		own it.
     * @param packageName
     * 		The name of the package that is to be dumped.
     */
    public void dumpPackageState(java.io.FileDescriptor fd, java.lang.String packageName) {
        android.app.ActivityManager.dumpPackageStateStatic(fd, packageName);
    }

    /**
     *
     *
     * @unknown 
     */
    public static void dumpPackageStateStatic(java.io.FileDescriptor fd, java.lang.String packageName) {
        java.io.FileOutputStream fout = new java.io.FileOutputStream(fd);
        java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(fout);
        android.app.ActivityManager.dumpService(pw, fd, "package", new java.lang.String[]{ packageName });
        pw.println();
        android.app.ActivityManager.dumpService(pw, fd, android.content.Context.ACTIVITY_SERVICE, new java.lang.String[]{ "-a", "package", packageName });
        pw.println();
        android.app.ActivityManager.dumpService(pw, fd, "meminfo", new java.lang.String[]{ "--local", "--package", packageName });
        pw.println();
        android.app.ActivityManager.dumpService(pw, fd, ProcessStats.SERVICE_NAME, new java.lang.String[]{ packageName });
        pw.println();
        android.app.ActivityManager.dumpService(pw, fd, "usagestats", new java.lang.String[]{ "--packages", packageName });
        pw.println();
        android.app.ActivityManager.dumpService(pw, fd, android.os.BatteryStats.SERVICE_NAME, new java.lang.String[]{ packageName });
        pw.flush();
    }

    private static void dumpService(java.io.PrintWriter pw, java.io.FileDescriptor fd, java.lang.String name, java.lang.String[] args) {
        pw.print("DUMP OF SERVICE ");
        pw.print(name);
        pw.println(":");
        android.os.IBinder service = android.os.ServiceManager.checkService(name);
        if (service == null) {
            pw.println("  (Service not found)");
            return;
        }
        com.android.internal.os.TransferPipe tp = null;
        try {
            pw.flush();
            tp = new com.android.internal.os.TransferPipe();
            tp.setBufferPrefix("  ");
            service.dumpAsync(tp.getWriteFd().getFileDescriptor(), args);
            tp.go(fd, 10000);
        } catch (java.lang.Throwable e) {
            if (tp != null) {
                tp.kill();
            }
            pw.println("Failure dumping service:");
            e.printStackTrace(pw);
        }
    }

    /**
     * Request that the system start watching for the calling process to exceed a pss
     * size as given here.  Once called, the system will look for any occasions where it
     * sees the associated process with a larger pss size and, when this happens, automatically
     * pull a heap dump from it and allow the user to share the data.  Note that this request
     * continues running even if the process is killed and restarted.  To remove the watch,
     * use {@link #clearWatchHeapLimit()}.
     *
     * <p>This API only work if the calling process has been marked as
     * {@link ApplicationInfo#FLAG_DEBUGGABLE} or this is running on a debuggable
     * (userdebug or eng) build.</p>
     *
     * <p>Callers can optionally implement {@link #ACTION_REPORT_HEAP_LIMIT} to directly
     * handle heap limit reports themselves.</p>
     *
     * @param pssSize
     * 		The size in bytes to set the limit at.
     */
    public void setWatchHeapLimit(long pssSize) {
        try {
            android.app.ActivityManagerNative.getDefault().setDumpHeapDebugLimit(null, 0, pssSize, mContext.getPackageName());
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Action an app can implement to handle reports from {@link #setWatchHeapLimit(long)}.
     * If your package has an activity handling this action, it will be launched with the
     * heap data provided to it the same way as {@link Intent#ACTION_SEND}.  Note that to
     * match the activty must support this action and a MIME type of "*&#47;*".
     */
    public static final java.lang.String ACTION_REPORT_HEAP_LIMIT = "android.app.action.REPORT_HEAP_LIMIT";

    /**
     * Clear a heap watch limit previously set by {@link #setWatchHeapLimit(long)}.
     */
    public void clearWatchHeapLimit() {
        try {
            android.app.ActivityManagerNative.getDefault().setDumpHeapDebugLimit(null, 0, 0, null);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void startLockTaskMode(int taskId) {
        try {
            android.app.ActivityManagerNative.getDefault().startLockTaskMode(taskId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public void stopLockTaskMode() {
        try {
            android.app.ActivityManagerNative.getDefault().stopLockTaskMode();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Return whether currently in lock task mode.  When in this mode
     * no new tasks can be created or switched to.
     *
     * @see Activity#startLockTask()
     * @deprecated Use {@link #getLockTaskModeState} instead.
     */
    public boolean isInLockTaskMode() {
        return getLockTaskModeState() != android.app.ActivityManager.LOCK_TASK_MODE_NONE;
    }

    /**
     * Return the current state of task locking. The three possible outcomes
     * are {@link #LOCK_TASK_MODE_NONE}, {@link #LOCK_TASK_MODE_LOCKED}
     * and {@link #LOCK_TASK_MODE_PINNED}.
     *
     * @see Activity#startLockTask()
     */
    public int getLockTaskModeState() {
        try {
            return android.app.ActivityManagerNative.getDefault().getLockTaskModeState();
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Enable more aggressive scheduling for latency-sensitive low-runtime VR threads. Only one
     * thread can be a VR thread in a process at a time, and that thread may be subject to
     * restrictions on the amount of time it can run.
     *
     * To reset the VR thread for an application, a tid of 0 can be passed.
     *
     * @see android.os.Process#myTid()
     * @param tid
     * 		tid of the VR thread
     */
    public static void setVrThread(int tid) {
        try {
            android.app.ActivityManagerNative.getDefault().setVrThread(tid);
        } catch (android.os.RemoteException e) {
            // pass
        }
    }

    /**
     * The AppTask allows you to manage your own application's tasks.
     * See {@link android.app.ActivityManager#getAppTasks()}
     */
    public static class AppTask {
        private android.app.IAppTask mAppTaskImpl;

        /**
         *
         *
         * @unknown 
         */
        public AppTask(android.app.IAppTask task) {
            mAppTaskImpl = task;
        }

        /**
         * Finishes all activities in this task and removes it from the recent tasks list.
         */
        public void finishAndRemoveTask() {
            try {
                mAppTaskImpl.finishAndRemoveTask();
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Get the RecentTaskInfo associated with this task.
         *
         * @return The RecentTaskInfo for this task, or null if the task no longer exists.
         */
        public android.app.ActivityManager.RecentTaskInfo getTaskInfo() {
            try {
                return mAppTaskImpl.getTaskInfo();
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Bring this task to the foreground.  If it contains activities, they will be
         * brought to the foreground with it and their instances re-created if needed.
         * If it doesn't contain activities, the root activity of the task will be
         * re-launched.
         */
        public void moveToFront() {
            try {
                mAppTaskImpl.moveToFront();
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }

        /**
         * Start an activity in this task.  Brings the task to the foreground.  If this task
         * is not currently active (that is, its id < 0), then a new activity for the given
         * Intent will be launched as the root of the task and the task brought to the
         * foreground.  Otherwise, if this task is currently active and the Intent does not specify
         * an activity to launch in a new task, then a new activity for the given Intent will
         * be launched on top of the task and the task brought to the foreground.  If this
         * task is currently active and the Intent specifies {@link Intent#FLAG_ACTIVITY_NEW_TASK}
         * or would otherwise be launched in to a new task, then the activity not launched but
         * this task be brought to the foreground and a new intent delivered to the top
         * activity if appropriate.
         *
         * <p>In other words, you generally want to use an Intent here that does not specify
         * {@link Intent#FLAG_ACTIVITY_NEW_TASK} or {@link Intent#FLAG_ACTIVITY_NEW_DOCUMENT},
         * and let the system do the right thing.</p>
         *
         * @param intent
         * 		The Intent describing the new activity to be launched on the task.
         * @param options
         * 		Optional launch options.
         * @see Activity#startActivity(android.content.Intent, android.os.Bundle)
         */
        public void startActivity(android.content.Context context, android.content.Intent intent, android.os.Bundle options) {
            android.app.ActivityThread thread = android.app.ActivityThread.currentActivityThread();
            thread.getInstrumentation().execStartActivityFromAppTask(context, thread.getApplicationThread(), mAppTaskImpl, intent, options);
        }

        /**
         * Modify the {@link Intent#FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS} flag in the root
         * Intent of this AppTask.
         *
         * @param exclude
         * 		If true, {@link Intent#FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS} will
         * 		be set; otherwise, it will be cleared.
         */
        public void setExcludeFromRecents(boolean exclude) {
            try {
                mAppTaskImpl.setExcludeFromRecents(exclude);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }
}

