/**
 * Copyright (C) 2006 The Android Open Source Project
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
 * System private API for talking with the activity manager service.  This
 * provides calls from the application back to the activity manager.
 *
 * {@hide }
 */
public interface IActivityManager extends android.os.IInterface {
    public int startActivity(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options) throws android.os.RemoteException;

    public int startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, int userId) throws android.os.RemoteException;

    public int startActivityAsCaller(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, boolean ignoreTargetSecurity, int userId) throws android.os.RemoteException;

    public android.app.IActivityManager.WaitResult startActivityAndWait(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, int userId) throws android.os.RemoteException;

    public int startActivityWithConfig(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.content.res.Configuration newConfig, android.os.Bundle options, int userId) throws android.os.RemoteException;

    public int startActivityIntentSender(android.app.IApplicationThread caller, android.content.IntentSender intent, android.content.Intent fillInIntent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flagsMask, int flagsValues, android.os.Bundle options) throws android.os.RemoteException;

    public int startVoiceActivity(java.lang.String callingPackage, int callingPid, int callingUid, android.content.Intent intent, java.lang.String resolvedType, android.service.voice.IVoiceInteractionSession session, com.android.internal.app.IVoiceInteractor interactor, int flags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, int userId) throws android.os.RemoteException;

    public boolean startNextMatchingActivity(android.os.IBinder callingActivity, android.content.Intent intent, android.os.Bundle options) throws android.os.RemoteException;

    public int startActivityFromRecents(int taskId, android.os.Bundle options) throws android.os.RemoteException;

    public boolean finishActivity(android.os.IBinder token, int code, android.content.Intent data, int finishTask) throws android.os.RemoteException;

    public void finishSubActivity(android.os.IBinder token, java.lang.String resultWho, int requestCode) throws android.os.RemoteException;

    public boolean finishActivityAffinity(android.os.IBinder token) throws android.os.RemoteException;

    public void finishVoiceTask(android.service.voice.IVoiceInteractionSession session) throws android.os.RemoteException;

    public boolean releaseActivityInstance(android.os.IBinder token) throws android.os.RemoteException;

    public void releaseSomeActivities(android.app.IApplicationThread app) throws android.os.RemoteException;

    public boolean willActivityBeVisible(android.os.IBinder token) throws android.os.RemoteException;

    public android.content.Intent registerReceiver(android.app.IApplicationThread caller, java.lang.String callerPackage, android.content.IIntentReceiver receiver, android.content.IntentFilter filter, java.lang.String requiredPermission, int userId) throws android.os.RemoteException;

    public void unregisterReceiver(android.content.IIntentReceiver receiver) throws android.os.RemoteException;

    public int broadcastIntent(android.app.IApplicationThread caller, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle map, java.lang.String[] requiredPermissions, int appOp, android.os.Bundle options, boolean serialized, boolean sticky, int userId) throws android.os.RemoteException;

    public void unbroadcastIntent(android.app.IApplicationThread caller, android.content.Intent intent, int userId) throws android.os.RemoteException;

    public void finishReceiver(android.os.IBinder who, int resultCode, java.lang.String resultData, android.os.Bundle map, boolean abortBroadcast, int flags) throws android.os.RemoteException;

    public void attachApplication(android.app.IApplicationThread app) throws android.os.RemoteException;

    public void activityResumed(android.os.IBinder token) throws android.os.RemoteException;

    public void activityIdle(android.os.IBinder token, android.content.res.Configuration config, boolean stopProfiling) throws android.os.RemoteException;

    public void activityPaused(android.os.IBinder token) throws android.os.RemoteException;

    public void activityStopped(android.os.IBinder token, android.os.Bundle state, android.os.PersistableBundle persistentState, java.lang.CharSequence description) throws android.os.RemoteException;

    public void activitySlept(android.os.IBinder token) throws android.os.RemoteException;

    public void activityDestroyed(android.os.IBinder token) throws android.os.RemoteException;

    public void activityRelaunched(android.os.IBinder token) throws android.os.RemoteException;

    public void reportSizeConfigurations(android.os.IBinder token, int[] horizontalSizeConfiguration, int[] verticalSizeConfigurations, int[] smallestWidthConfigurations) throws android.os.RemoteException;

    public java.lang.String getCallingPackage(android.os.IBinder token) throws android.os.RemoteException;

    public android.content.ComponentName getCallingActivity(android.os.IBinder token) throws android.os.RemoteException;

    public java.util.List<android.app.IAppTask> getAppTasks(java.lang.String callingPackage) throws android.os.RemoteException;

    public int addAppTask(android.os.IBinder activityToken, android.content.Intent intent, android.app.ActivityManager.TaskDescription description, android.graphics.Bitmap thumbnail) throws android.os.RemoteException;

    public android.graphics.Point getAppTaskThumbnailSize() throws android.os.RemoteException;

    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getTasks(int maxNum, int flags) throws android.os.RemoteException;

    public android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) throws android.os.RemoteException;

    public android.app.ActivityManager.TaskThumbnail getTaskThumbnail(int taskId) throws android.os.RemoteException;

    public java.util.List<android.app.ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) throws android.os.RemoteException;

    public java.util.List<android.app.ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws android.os.RemoteException;

    public void moveTaskToFront(int task, int flags, android.os.Bundle options) throws android.os.RemoteException;

    public boolean moveActivityTaskToBack(android.os.IBinder token, boolean nonRoot) throws android.os.RemoteException;

    public void moveTaskBackwards(int task) throws android.os.RemoteException;

    public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws android.os.RemoteException;

    public boolean moveTaskToDockedStack(int taskId, int createMode, boolean toTop, boolean animate, android.graphics.Rect initialBounds, boolean moveHomeStackFront) throws android.os.RemoteException;

    public boolean moveTopActivityToPinnedStack(int stackId, android.graphics.Rect bounds) throws android.os.RemoteException;

    /**
     * Resizes the input stack id to the given bounds.
     *
     * @param stackId
     * 		Id of the stack to resize.
     * @param bounds
     * 		Bounds to resize the stack to or {@code null} for fullscreen.
     * @param allowResizeInDockedMode
     * 		True if the resize should be allowed when the docked stack is
     * 		active.
     * @param preserveWindows
     * 		True if the windows of activities contained in the stack should be
     * 		preserved.
     * @param animate
     * 		True if the stack resize should be animated.
     * @param animationDuration
     * 		The duration of the resize animation in milliseconds or -1 if the
     * 		default animation duration should be used.
     * @throws RemoteException
     * 		
     */
    public void resizeStack(int stackId, android.graphics.Rect bounds, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws android.os.RemoteException;

    /**
     * Moves all tasks from the docked stack in the fullscreen stack and puts the top task of the
     * fullscreen stack into the docked stack.
     */
    public void swapDockedAndFullscreenStack() throws android.os.RemoteException;

    /**
     * Resizes the docked stack, and all other stacks as the result of the dock stack bounds change.
     *
     * @param dockedBounds
     * 		The bounds for the docked stack.
     * @param tempDockedTaskBounds
     * 		The temporary bounds for the tasks in the docked stack, which
     * 		might be different from the stack bounds to allow more
     * 		flexibility while resizing, or {@code null} if they should be the
     * 		same as the stack bounds.
     * @param tempDockedTaskInsetBounds
     * 		The temporary bounds for the tasks to calculate the insets.
     * 		When resizing, we usually "freeze" the layout of a task. To
     * 		achieve that, we also need to "freeze" the insets, which
     * 		gets achieved by changing task bounds but not bounds used
     * 		to calculate the insets in this transient state
     * @param tempOtherTaskBounds
     * 		The temporary bounds for the tasks in all other stacks, or
     * 		{@code null} if they should be the same as the stack bounds.
     * @param tempOtherTaskInsetBounds
     * 		Like {@code tempDockedTaskInsetBounds}, but for the other
     * 		stacks.
     * @throws RemoteException
     * 		
     */
    public void resizeDockedStack(android.graphics.Rect dockedBounds, android.graphics.Rect tempDockedTaskBounds, android.graphics.Rect tempDockedTaskInsetBounds, android.graphics.Rect tempOtherTaskBounds, android.graphics.Rect tempOtherTaskInsetBounds) throws android.os.RemoteException;

    /**
     * Resizes the pinned stack.
     *
     * @param pinnedBounds
     * 		The bounds for the pinned stack.
     * @param tempPinnedTaskBounds
     * 		The temporary bounds for the tasks in the pinned stack, which
     * 		might be different from the stack bounds to allow more
     * 		flexibility while resizing, or {@code null} if they should be the
     * 		same as the stack bounds.
     */
    public void resizePinnedStack(android.graphics.Rect pinnedBounds, android.graphics.Rect tempPinnedTaskBounds) throws android.os.RemoteException;

    public void positionTaskInStack(int taskId, int stackId, int position) throws android.os.RemoteException;

    public java.util.List<android.app.ActivityManager.StackInfo> getAllStackInfos() throws android.os.RemoteException;

    public android.app.ActivityManager.StackInfo getStackInfo(int stackId) throws android.os.RemoteException;

    public boolean isInHomeStack(int taskId) throws android.os.RemoteException;

    public void setFocusedStack(int stackId) throws android.os.RemoteException;

    public int getFocusedStackId() throws android.os.RemoteException;

    public void setFocusedTask(int taskId) throws android.os.RemoteException;

    public void registerTaskStackListener(android.app.ITaskStackListener listener) throws android.os.RemoteException;

    public int getTaskForActivity(android.os.IBinder token, boolean onlyRoot) throws android.os.RemoteException;

    public android.app.IActivityManager.ContentProviderHolder getContentProvider(android.app.IApplicationThread caller, java.lang.String name, int userId, boolean stable) throws android.os.RemoteException;

    public android.app.IActivityManager.ContentProviderHolder getContentProviderExternal(java.lang.String name, int userId, android.os.IBinder token) throws android.os.RemoteException;

    public void removeContentProvider(android.os.IBinder connection, boolean stable) throws android.os.RemoteException;

    public void removeContentProviderExternal(java.lang.String name, android.os.IBinder token) throws android.os.RemoteException;

    public void publishContentProviders(android.app.IApplicationThread caller, java.util.List<android.app.IActivityManager.ContentProviderHolder> providers) throws android.os.RemoteException;

    public boolean refContentProvider(android.os.IBinder connection, int stableDelta, int unstableDelta) throws android.os.RemoteException;

    public void unstableProviderDied(android.os.IBinder connection) throws android.os.RemoteException;

    public void appNotRespondingViaProvider(android.os.IBinder connection) throws android.os.RemoteException;

    public android.app.PendingIntent getRunningServiceControlPanel(android.content.ComponentName service) throws android.os.RemoteException;

    public android.content.ComponentName startService(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, java.lang.String callingPackage, int userId) throws android.os.RemoteException;

    public int stopService(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException;

    public boolean stopServiceToken(android.content.ComponentName className, android.os.IBinder token, int startId) throws android.os.RemoteException;

    public void setServiceForeground(android.content.ComponentName className, android.os.IBinder token, int id, android.app.Notification notification, int flags) throws android.os.RemoteException;

    public int bindService(android.app.IApplicationThread caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, int flags, java.lang.String callingPackage, int userId) throws android.os.RemoteException;

    public boolean unbindService(android.app.IServiceConnection connection) throws android.os.RemoteException;

    public void publishService(android.os.IBinder token, android.content.Intent intent, android.os.IBinder service) throws android.os.RemoteException;

    public void unbindFinished(android.os.IBinder token, android.content.Intent service, boolean doRebind) throws android.os.RemoteException;

    /* oneway */
    public void serviceDoneExecuting(android.os.IBinder token, int type, int startId, int res) throws android.os.RemoteException;

    public android.os.IBinder peekService(android.content.Intent service, java.lang.String resolvedType, java.lang.String callingPackage) throws android.os.RemoteException;

    public boolean bindBackupAgent(java.lang.String packageName, int backupRestoreMode, int userId) throws android.os.RemoteException;

    public void clearPendingBackup() throws android.os.RemoteException;

    public void backupAgentCreated(java.lang.String packageName, android.os.IBinder agent) throws android.os.RemoteException;

    public void unbindBackupAgent(android.content.pm.ApplicationInfo appInfo) throws android.os.RemoteException;

    public void killApplicationProcess(java.lang.String processName, int uid) throws android.os.RemoteException;

    public boolean startInstrumentation(android.content.ComponentName className, java.lang.String profileFile, int flags, android.os.Bundle arguments, android.app.IInstrumentationWatcher watcher, android.app.IUiAutomationConnection connection, int userId, java.lang.String abiOverride) throws android.os.RemoteException;

    public void finishInstrumentation(android.app.IApplicationThread target, int resultCode, android.os.Bundle results) throws android.os.RemoteException;

    public android.content.res.Configuration getConfiguration() throws android.os.RemoteException;

    public void updateConfiguration(android.content.res.Configuration values) throws android.os.RemoteException;

    public void setRequestedOrientation(android.os.IBinder token, int requestedOrientation) throws android.os.RemoteException;

    public int getRequestedOrientation(android.os.IBinder token) throws android.os.RemoteException;

    public android.content.ComponentName getActivityClassForToken(android.os.IBinder token) throws android.os.RemoteException;

    public java.lang.String getPackageForToken(android.os.IBinder token) throws android.os.RemoteException;

    public android.content.IIntentSender getIntentSender(int type, java.lang.String packageName, android.os.IBinder token, java.lang.String resultWho, int requestCode, android.content.Intent[] intents, java.lang.String[] resolvedTypes, int flags, android.os.Bundle options, int userId) throws android.os.RemoteException;

    public void cancelIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException;

    public boolean clearApplicationUserData(final java.lang.String packageName, final android.content.pm.IPackageDataObserver observer, int userId) throws android.os.RemoteException;

    public java.lang.String getPackageForIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException;

    public int getUidForIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException;

    public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, java.lang.String name, java.lang.String callerPackage) throws android.os.RemoteException;

    public void setProcessLimit(int max) throws android.os.RemoteException;

    public int getProcessLimit() throws android.os.RemoteException;

    public void setProcessForeground(android.os.IBinder token, int pid, boolean isForeground) throws android.os.RemoteException;

    public int checkPermission(java.lang.String permission, int pid, int uid) throws android.os.RemoteException;

    public int checkPermissionWithToken(java.lang.String permission, int pid, int uid, android.os.IBinder callerToken) throws android.os.RemoteException;

    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int mode, int userId, android.os.IBinder callerToken) throws android.os.RemoteException;

    public void grantUriPermission(android.app.IApplicationThread caller, java.lang.String targetPkg, android.net.Uri uri, int mode, int userId) throws android.os.RemoteException;

    public void revokeUriPermission(android.app.IApplicationThread caller, android.net.Uri uri, int mode, int userId) throws android.os.RemoteException;

    public void takePersistableUriPermission(android.net.Uri uri, int modeFlags, int userId) throws android.os.RemoteException;

    public void releasePersistableUriPermission(android.net.Uri uri, int modeFlags, int userId) throws android.os.RemoteException;

    public android.content.pm.ParceledListSlice<android.content.UriPermission> getPersistedUriPermissions(java.lang.String packageName, boolean incoming) throws android.os.RemoteException;

    // Gets the URI permissions granted to an arbitrary package.
    // NOTE: this is different from getPersistedUriPermissions(), which returns the URIs the package
    // granted to another packages (instead of those granted to it).
    public android.content.pm.ParceledListSlice<android.content.UriPermission> getGrantedUriPermissions(java.lang.String packageName, int userId) throws android.os.RemoteException;

    // Clears the URI permissions granted to an arbitrary package.
    public void clearGrantedUriPermissions(java.lang.String packageName, int userId) throws android.os.RemoteException;

    public void showWaitingForDebugger(android.app.IApplicationThread who, boolean waiting) throws android.os.RemoteException;

    public void getMemoryInfo(android.app.ActivityManager.MemoryInfo outInfo) throws android.os.RemoteException;

    public void killBackgroundProcesses(final java.lang.String packageName, int userId) throws android.os.RemoteException;

    public void killAllBackgroundProcesses() throws android.os.RemoteException;

    public void killPackageDependents(final java.lang.String packageName, int userId) throws android.os.RemoteException;

    public void forceStopPackage(final java.lang.String packageName, int userId) throws android.os.RemoteException;

    // Note: probably don't want to allow applications access to these.
    public void setLockScreenShown(boolean showing, boolean occluded) throws android.os.RemoteException;

    public void unhandledBack() throws android.os.RemoteException;

    public android.os.ParcelFileDescriptor openContentUri(android.net.Uri uri) throws android.os.RemoteException;

    public void setDebugApp(java.lang.String packageName, boolean waitForDebugger, boolean persistent) throws android.os.RemoteException;

    public void setAlwaysFinish(boolean enabled) throws android.os.RemoteException;

    public void setActivityController(android.app.IActivityController watcher, boolean imAMonkey) throws android.os.RemoteException;

    public void setLenientBackgroundCheck(boolean enabled) throws android.os.RemoteException;

    public int getMemoryTrimLevel() throws android.os.RemoteException;

    public void enterSafeMode() throws android.os.RemoteException;

    public void noteWakeupAlarm(android.content.IIntentSender sender, int sourceUid, java.lang.String sourcePkg, java.lang.String tag) throws android.os.RemoteException;

    public void noteAlarmStart(android.content.IIntentSender sender, int sourceUid, java.lang.String tag) throws android.os.RemoteException;

    public void noteAlarmFinish(android.content.IIntentSender sender, int sourceUid, java.lang.String tag) throws android.os.RemoteException;

    public boolean killPids(int[] pids, java.lang.String reason, boolean secure) throws android.os.RemoteException;

    public boolean killProcessesBelowForeground(java.lang.String reason) throws android.os.RemoteException;

    // Special low-level communication with activity manager.
    public void handleApplicationCrash(android.os.IBinder app, android.app.ApplicationErrorReport.CrashInfo crashInfo) throws android.os.RemoteException;

    public boolean handleApplicationWtf(android.os.IBinder app, java.lang.String tag, boolean system, android.app.ApplicationErrorReport.CrashInfo crashInfo) throws android.os.RemoteException;

    // A StrictMode violation to be handled.  The violationMask is a
    // subset of the original StrictMode policy bitmask, with only the
    // bit violated and penalty bits to be executed by the
    // ActivityManagerService remaining set.
    public void handleApplicationStrictModeViolation(android.os.IBinder app, int violationMask, android.os.StrictMode.ViolationInfo crashInfo) throws android.os.RemoteException;

    /* This will deliver the specified signal to all the persistent processes. Currently only
    SIGUSR1 is delivered. All others are ignored.
     */
    public void signalPersistentProcesses(int signal) throws android.os.RemoteException;

    // Retrieve running application processes in the system
    public java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws android.os.RemoteException;

    // Retrieve info of applications installed on external media that are currently
    // running.
    public java.util.List<android.content.pm.ApplicationInfo> getRunningExternalApplications() throws android.os.RemoteException;

    // Get memory information about the calling process.
    public void getMyMemoryState(android.app.ActivityManager.RunningAppProcessInfo outInfo) throws android.os.RemoteException;

    // Get device configuration
    public android.content.pm.ConfigurationInfo getDeviceConfigurationInfo() throws android.os.RemoteException;

    // Turn on/off profiling in a particular process.
    public boolean profileControl(java.lang.String process, int userId, boolean start, android.app.ProfilerInfo profilerInfo, int profileType) throws android.os.RemoteException;

    public boolean shutdown(int timeout) throws android.os.RemoteException;

    public void stopAppSwitches() throws android.os.RemoteException;

    public void resumeAppSwitches() throws android.os.RemoteException;

    public void addPackageDependency(java.lang.String packageName) throws android.os.RemoteException;

    public void killApplication(java.lang.String pkg, int appId, int userId, java.lang.String reason) throws android.os.RemoteException;

    public void closeSystemDialogs(java.lang.String reason) throws android.os.RemoteException;

    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws android.os.RemoteException;

    public void overridePendingTransition(android.os.IBinder token, java.lang.String packageName, int enterAnim, int exitAnim) throws android.os.RemoteException;

    public boolean isUserAMonkey() throws android.os.RemoteException;

    public void setUserIsMonkey(boolean monkey) throws android.os.RemoteException;

    public void finishHeavyWeightApp() throws android.os.RemoteException;

    public boolean convertFromTranslucent(android.os.IBinder token) throws android.os.RemoteException;

    public boolean convertToTranslucent(android.os.IBinder token, android.app.ActivityOptions options) throws android.os.RemoteException;

    public void notifyActivityDrawn(android.os.IBinder token) throws android.os.RemoteException;

    public android.app.ActivityOptions getActivityOptions(android.os.IBinder token) throws android.os.RemoteException;

    public void bootAnimationComplete() throws android.os.RemoteException;

    public void setImmersive(android.os.IBinder token, boolean immersive) throws android.os.RemoteException;

    public boolean isImmersive(android.os.IBinder token) throws android.os.RemoteException;

    public boolean isTopActivityImmersive() throws android.os.RemoteException;

    public boolean isTopOfTask(android.os.IBinder token) throws android.os.RemoteException;

    public void crashApplication(int uid, int initialPid, java.lang.String packageName, java.lang.String message) throws android.os.RemoteException;

    public java.lang.String getProviderMimeType(android.net.Uri uri, int userId) throws android.os.RemoteException;

    public android.os.IBinder newUriPermissionOwner(java.lang.String name) throws android.os.RemoteException;

    public android.os.IBinder getUriPermissionOwnerForActivity(android.os.IBinder activityToken) throws android.os.RemoteException;

    public void grantUriPermissionFromOwner(android.os.IBinder owner, int fromUid, java.lang.String targetPkg, android.net.Uri uri, int mode, int sourceUserId, int targetUserId) throws android.os.RemoteException;

    public void revokeUriPermissionFromOwner(android.os.IBinder owner, android.net.Uri uri, int mode, int userId) throws android.os.RemoteException;

    public int checkGrantUriPermission(int callingUid, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int userId) throws android.os.RemoteException;

    // Cause the specified process to dump the specified heap.
    public boolean dumpHeap(java.lang.String process, int userId, boolean managed, java.lang.String path, android.os.ParcelFileDescriptor fd) throws android.os.RemoteException;

    public int startActivities(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent[] intents, java.lang.String[] resolvedTypes, android.os.IBinder resultTo, android.os.Bundle options, int userId) throws android.os.RemoteException;

    public int getFrontActivityScreenCompatMode() throws android.os.RemoteException;

    public void setFrontActivityScreenCompatMode(int mode) throws android.os.RemoteException;

    public int getPackageScreenCompatMode(java.lang.String packageName) throws android.os.RemoteException;

    public void setPackageScreenCompatMode(java.lang.String packageName, int mode) throws android.os.RemoteException;

    public boolean getPackageAskScreenCompat(java.lang.String packageName) throws android.os.RemoteException;

    public void setPackageAskScreenCompat(java.lang.String packageName, boolean ask) throws android.os.RemoteException;

    // Multi-user APIs
    public boolean switchUser(int userid) throws android.os.RemoteException;

    public boolean startUserInBackground(int userid) throws android.os.RemoteException;

    public boolean unlockUser(int userid, byte[] token, byte[] secret, android.os.IProgressListener listener) throws android.os.RemoteException;

    public int stopUser(int userid, boolean force, android.app.IStopUserCallback callback) throws android.os.RemoteException;

    public android.content.pm.UserInfo getCurrentUser() throws android.os.RemoteException;

    public boolean isUserRunning(int userid, int flags) throws android.os.RemoteException;

    public int[] getRunningUserIds() throws android.os.RemoteException;

    public boolean removeTask(int taskId) throws android.os.RemoteException;

    public void registerProcessObserver(android.app.IProcessObserver observer) throws android.os.RemoteException;

    public void unregisterProcessObserver(android.app.IProcessObserver observer) throws android.os.RemoteException;

    public void registerUidObserver(android.app.IUidObserver observer, int which) throws android.os.RemoteException;

    public void unregisterUidObserver(android.app.IUidObserver observer) throws android.os.RemoteException;

    public boolean isIntentSenderTargetedToPackage(android.content.IIntentSender sender) throws android.os.RemoteException;

    public boolean isIntentSenderAnActivity(android.content.IIntentSender sender) throws android.os.RemoteException;

    public android.content.Intent getIntentForIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException;

    public java.lang.String getTagForIntentSender(android.content.IIntentSender sender, java.lang.String prefix) throws android.os.RemoteException;

    public void updatePersistentConfiguration(android.content.res.Configuration values) throws android.os.RemoteException;

    public long[] getProcessPss(int[] pids) throws android.os.RemoteException;

    public void showBootMessage(java.lang.CharSequence msg, boolean always) throws android.os.RemoteException;

    public void keyguardWaitingForActivityDrawn() throws android.os.RemoteException;

    /**
     * Notify the system that the keyguard is going away.
     *
     * @param flags
     * 		See {@link android.view.WindowManagerPolicy#KEYGUARD_GOING_AWAY_FLAG_TO_SHADE}
     * 		etc.
     */
    public void keyguardGoingAway(int flags) throws android.os.RemoteException;

    public boolean shouldUpRecreateTask(android.os.IBinder token, java.lang.String destAffinity) throws android.os.RemoteException;

    public boolean navigateUpTo(android.os.IBinder token, android.content.Intent target, int resultCode, android.content.Intent resultData) throws android.os.RemoteException;

    // These are not public because you need to be very careful in how you
    // manage your activity to make sure it is always the uid you expect.
    public int getLaunchedFromUid(android.os.IBinder activityToken) throws android.os.RemoteException;

    public java.lang.String getLaunchedFromPackage(android.os.IBinder activityToken) throws android.os.RemoteException;

    public void registerUserSwitchObserver(android.app.IUserSwitchObserver observer, java.lang.String name) throws android.os.RemoteException;

    public void unregisterUserSwitchObserver(android.app.IUserSwitchObserver observer) throws android.os.RemoteException;

    public void requestBugReport(int bugreportType) throws android.os.RemoteException;

    public long inputDispatchingTimedOut(int pid, boolean aboveSystem, java.lang.String reason) throws android.os.RemoteException;

    public android.os.Bundle getAssistContextExtras(int requestType) throws android.os.RemoteException;

    public boolean requestAssistContextExtras(int requestType, com.android.internal.os.IResultReceiver receiver, android.os.Bundle receiverExtras, android.os.IBinder activityToken, boolean focused, boolean newSessionId) throws android.os.RemoteException;

    public void reportAssistContextExtras(android.os.IBinder token, android.os.Bundle extras, android.app.assist.AssistStructure structure, android.app.assist.AssistContent content, android.net.Uri referrer) throws android.os.RemoteException;

    public boolean launchAssistIntent(android.content.Intent intent, int requestType, java.lang.String hint, int userHandle, android.os.Bundle args) throws android.os.RemoteException;

    public boolean isAssistDataAllowedOnCurrentActivity() throws android.os.RemoteException;

    public boolean showAssistFromActivity(android.os.IBinder token, android.os.Bundle args) throws android.os.RemoteException;

    public void killUid(int appId, int userId, java.lang.String reason) throws android.os.RemoteException;

    public void hang(android.os.IBinder who, boolean allowRestart) throws android.os.RemoteException;

    public void reportActivityFullyDrawn(android.os.IBinder token) throws android.os.RemoteException;

    public void restart() throws android.os.RemoteException;

    public void performIdleMaintenance() throws android.os.RemoteException;

    public void sendIdleJobTrigger() throws android.os.RemoteException;

    public android.app.IActivityContainer createVirtualActivityContainer(android.os.IBinder parentActivityToken, android.app.IActivityContainerCallback callback) throws android.os.RemoteException;

    public android.app.IActivityContainer createStackOnDisplay(int displayId) throws android.os.RemoteException;

    public void deleteActivityContainer(android.app.IActivityContainer container) throws android.os.RemoteException;

    public int getActivityDisplayId(android.os.IBinder activityToken) throws android.os.RemoteException;

    public void startSystemLockTaskMode(int taskId) throws android.os.RemoteException;

    public void startLockTaskMode(int taskId) throws android.os.RemoteException;

    public void startLockTaskMode(android.os.IBinder token) throws android.os.RemoteException;

    public void stopLockTaskMode() throws android.os.RemoteException;

    public void stopSystemLockTaskMode() throws android.os.RemoteException;

    public boolean isInLockTaskMode() throws android.os.RemoteException;

    public int getLockTaskModeState() throws android.os.RemoteException;

    public void showLockTaskEscapeMessage(android.os.IBinder token) throws android.os.RemoteException;

    public void setTaskDescription(android.os.IBinder token, android.app.ActivityManager.TaskDescription values) throws android.os.RemoteException;

    public void setTaskResizeable(int taskId, int resizeableMode) throws android.os.RemoteException;

    public void resizeTask(int taskId, android.graphics.Rect bounds, int resizeMode) throws android.os.RemoteException;

    public android.graphics.Rect getTaskBounds(int taskId) throws android.os.RemoteException;

    public android.graphics.Bitmap getTaskDescriptionIcon(java.lang.String filename, int userId) throws android.os.RemoteException;

    public void startInPlaceAnimationOnFrontMostApplication(android.app.ActivityOptions opts) throws android.os.RemoteException;

    public boolean requestVisibleBehind(android.os.IBinder token, boolean visible) throws android.os.RemoteException;

    public boolean isBackgroundVisibleBehind(android.os.IBinder token) throws android.os.RemoteException;

    public void backgroundResourcesReleased(android.os.IBinder token) throws android.os.RemoteException;

    public void notifyLaunchTaskBehindComplete(android.os.IBinder token) throws android.os.RemoteException;

    public void notifyEnterAnimationComplete(android.os.IBinder token) throws android.os.RemoteException;

    public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws android.os.RemoteException;

    public void setDumpHeapDebugLimit(java.lang.String processName, int uid, long maxMemSize, java.lang.String reportPackage) throws android.os.RemoteException;

    public void dumpHeapFinished(java.lang.String path) throws android.os.RemoteException;

    public void setVoiceKeepAwake(android.service.voice.IVoiceInteractionSession session, boolean keepAwake) throws android.os.RemoteException;

    public void updateLockTaskPackages(int userId, java.lang.String[] packages) throws android.os.RemoteException;

    public void updateDeviceOwner(java.lang.String packageName) throws android.os.RemoteException;

    public int getPackageProcessState(java.lang.String packageName, java.lang.String callingPackage) throws android.os.RemoteException;

    public boolean setProcessMemoryTrimLevel(java.lang.String process, int uid, int level) throws android.os.RemoteException;

    public boolean isRootVoiceInteraction(android.os.IBinder token) throws android.os.RemoteException;

    // Start Binder transaction tracking for all applications.
    public boolean startBinderTracking() throws android.os.RemoteException;

    // Stop Binder transaction tracking for all applications and dump trace data to the given file
    // descriptor.
    public boolean stopBinderTrackingAndDump(android.os.ParcelFileDescriptor fd) throws android.os.RemoteException;

    public int getActivityStackId(android.os.IBinder token) throws android.os.RemoteException;

    public void exitFreeformMode(android.os.IBinder token) throws android.os.RemoteException;

    public void suppressResizeConfigChanges(boolean suppress) throws android.os.RemoteException;

    public void moveTasksToFullscreenStack(int fromStackId, boolean onTop) throws android.os.RemoteException;

    public int getAppStartMode(int uid, java.lang.String packageName) throws android.os.RemoteException;

    public boolean isInMultiWindowMode(android.os.IBinder token) throws android.os.RemoteException;

    public boolean isInPictureInPictureMode(android.os.IBinder token) throws android.os.RemoteException;

    public void enterPictureInPictureMode(android.os.IBinder token) throws android.os.RemoteException;

    public int setVrMode(android.os.IBinder token, boolean enabled, android.content.ComponentName packageName) throws android.os.RemoteException;

    public boolean isVrModePackageEnabled(android.content.ComponentName packageName) throws android.os.RemoteException;

    public boolean isAppForeground(int uid) throws android.os.RemoteException;

    public void startLocalVoiceInteraction(android.os.IBinder token, android.os.Bundle options) throws android.os.RemoteException;

    public void stopLocalVoiceInteraction(android.os.IBinder token) throws android.os.RemoteException;

    public boolean supportsLocalVoiceInteraction() throws android.os.RemoteException;

    public void notifyPinnedStackAnimationEnded() throws android.os.RemoteException;

    public void removeStack(int stackId) throws android.os.RemoteException;

    public void notifyLockedProfile(@android.annotation.UserIdInt
    int userId) throws android.os.RemoteException;

    public void startConfirmDeviceCredentialIntent(android.content.Intent intent) throws android.os.RemoteException;

    public int sendIntentSender(android.content.IIntentSender target, int code, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) throws android.os.RemoteException;

    public void setVrThread(int tid) throws android.os.RemoteException;

    public void setRenderThread(int tid) throws android.os.RemoteException;

    /**
     * Lets activity manager know whether the calling process is currently showing "top-level" UI
     * that is not an activity, i.e. windows on the screen the user is currently interacting with.
     *
     * <p>This flag can only be set for persistent processes.
     *
     * @param hasTopUi
     * 		Whether the calling process has "top-level" UI.
     */
    public void setHasTopUi(boolean hasTopUi) throws android.os.RemoteException;

    /**
     * Returns if the target of the PendingIntent can be fired directly, without triggering
     * a work profile challenge. This can happen if the PendingIntent is to start direct-boot
     * aware activities, and the target user is in RUNNING_LOCKED state, i.e. we should allow
     * direct-boot aware activity to bypass work challenge when the user hasn't unlocked yet.
     *
     * @param intent
     * 		the {@link PendingIntent} to be tested.
     * @return {@code true} if the intent should not trigger a work challenge, {@code false}
    otherwise.
     * @throws RemoteException
     * 		
     */
    public boolean canBypassWorkChallenge(android.app.PendingIntent intent) throws android.os.RemoteException;

    /* Private non-Binder interfaces */
    /* package */
    boolean testIsSystemReady();

    /**
     * Information you can retrieve about a particular application.
     */
    public static class ContentProviderHolder implements android.os.Parcelable {
        public final android.content.pm.ProviderInfo info;

        public android.content.IContentProvider provider;

        public android.os.IBinder connection;

        public boolean noReleaseNeeded;

        public ContentProviderHolder(android.content.pm.ProviderInfo _info) {
            info = _info;
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            info.writeToParcel(dest, 0);
            if (provider != null) {
                dest.writeStrongBinder(provider.asBinder());
            } else {
                dest.writeStrongBinder(null);
            }
            dest.writeStrongBinder(connection);
            dest.writeInt(noReleaseNeeded ? 1 : 0);
        }

        public static final android.os.Parcelable.Creator<android.app.IActivityManager.ContentProviderHolder> CREATOR = new android.os.Parcelable.Creator<android.app.IActivityManager.ContentProviderHolder>() {
            @java.lang.Override
            public android.app.IActivityManager.ContentProviderHolder createFromParcel(android.os.Parcel source) {
                return new android.app.IActivityManager.ContentProviderHolder(source);
            }

            @java.lang.Override
            public android.app.IActivityManager.ContentProviderHolder[] newArray(int size) {
                return new android.app.IActivityManager.ContentProviderHolder[size];
            }
        };

        private ContentProviderHolder(android.os.Parcel source) {
            info = android.content.pm.ProviderInfo.CREATOR.createFromParcel(source);
            provider = android.content.ContentProviderNative.asInterface(source.readStrongBinder());
            connection = source.readStrongBinder();
            noReleaseNeeded = source.readInt() != 0;
        }
    }

    /**
     * Information returned after waiting for an activity start.
     */
    public static class WaitResult implements android.os.Parcelable {
        public int result;

        public boolean timeout;

        public android.content.ComponentName who;

        public long thisTime;

        public long totalTime;

        public WaitResult() {
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(result);
            dest.writeInt(timeout ? 1 : 0);
            android.content.ComponentName.writeToParcel(who, dest);
            dest.writeLong(thisTime);
            dest.writeLong(totalTime);
        }

        public static final android.os.Parcelable.Creator<android.app.IActivityManager.WaitResult> CREATOR = new android.os.Parcelable.Creator<android.app.IActivityManager.WaitResult>() {
            @java.lang.Override
            public android.app.IActivityManager.WaitResult createFromParcel(android.os.Parcel source) {
                return new android.app.IActivityManager.WaitResult(source);
            }

            @java.lang.Override
            public android.app.IActivityManager.WaitResult[] newArray(int size) {
                return new android.app.IActivityManager.WaitResult[size];
            }
        };

        private WaitResult(android.os.Parcel source) {
            result = source.readInt();
            timeout = source.readInt() != 0;
            who = android.content.ComponentName.readFromParcel(source);
            thisTime = source.readLong();
            totalTime = source.readLong();
        }
    }

    java.lang.String descriptor = "android.app.IActivityManager";

    // Please keep these transaction codes the same -- they are also
    // sent by C++ code.
    int HANDLE_APPLICATION_CRASH_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 1;

    int START_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 2;

    int UNHANDLED_BACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 3;

    int OPEN_CONTENT_URI_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 4;

    // Remaining non-native transaction codes.
    int FINISH_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 10;

    int REGISTER_RECEIVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 11;

    int UNREGISTER_RECEIVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 12;

    int BROADCAST_INTENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 13;

    int UNBROADCAST_INTENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 14;

    int FINISH_RECEIVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 15;

    int ATTACH_APPLICATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 16;

    int ACTIVITY_IDLE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 17;

    int ACTIVITY_PAUSED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 18;

    int ACTIVITY_STOPPED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 19;

    int GET_CALLING_PACKAGE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 20;

    int GET_CALLING_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 21;

    int GET_TASKS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 22;

    int MOVE_TASK_TO_FRONT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 23;

    int MOVE_TASK_BACKWARDS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 25;

    int GET_TASK_FOR_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 26;

    int GET_CONTENT_PROVIDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 28;

    int PUBLISH_CONTENT_PROVIDERS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 29;

    int REF_CONTENT_PROVIDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 30;

    int FINISH_SUB_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 31;

    int GET_RUNNING_SERVICE_CONTROL_PANEL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 32;

    int START_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 33;

    int STOP_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 34;

    int BIND_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 35;

    int UNBIND_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 36;

    int PUBLISH_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 37;

    int ACTIVITY_RESUMED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 38;

    int SET_DEBUG_APP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 41;

    int SET_ALWAYS_FINISH_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 42;

    int START_INSTRUMENTATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 43;

    int FINISH_INSTRUMENTATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 44;

    int GET_CONFIGURATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 45;

    int UPDATE_CONFIGURATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 46;

    int STOP_SERVICE_TOKEN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 47;

    int GET_ACTIVITY_CLASS_FOR_TOKEN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 48;

    int GET_PACKAGE_FOR_TOKEN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 49;

    int SET_PROCESS_LIMIT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 50;

    int GET_PROCESS_LIMIT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 51;

    int CHECK_PERMISSION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 52;

    int CHECK_URI_PERMISSION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 53;

    int GRANT_URI_PERMISSION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 54;

    int REVOKE_URI_PERMISSION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 55;

    int SET_ACTIVITY_CONTROLLER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 56;

    int SHOW_WAITING_FOR_DEBUGGER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 57;

    int SIGNAL_PERSISTENT_PROCESSES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 58;

    int GET_RECENT_TASKS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 59;

    int SERVICE_DONE_EXECUTING_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 60;

    int ACTIVITY_DESTROYED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 61;

    int GET_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 62;

    int CANCEL_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 63;

    int GET_PACKAGE_FOR_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 64;

    int ENTER_SAFE_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 65;

    int START_NEXT_MATCHING_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 66;

    int NOTE_WAKEUP_ALARM_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 67;

    int REMOVE_CONTENT_PROVIDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 68;

    int SET_REQUESTED_ORIENTATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 69;

    int GET_REQUESTED_ORIENTATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 70;

    int UNBIND_FINISHED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 71;

    int SET_PROCESS_FOREGROUND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 72;

    int SET_SERVICE_FOREGROUND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 73;

    int MOVE_ACTIVITY_TASK_TO_BACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 74;

    int GET_MEMORY_INFO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 75;

    int GET_PROCESSES_IN_ERROR_STATE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 76;

    int CLEAR_APP_DATA_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 77;

    int FORCE_STOP_PACKAGE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 78;

    int KILL_PIDS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 79;

    int GET_SERVICES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 80;

    int GET_TASK_THUMBNAIL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 81;

    int GET_RUNNING_APP_PROCESSES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 82;

    int GET_DEVICE_CONFIGURATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 83;

    int PEEK_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 84;

    int PROFILE_CONTROL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 85;

    int SHUTDOWN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 86;

    int STOP_APP_SWITCHES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 87;

    int RESUME_APP_SWITCHES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 88;

    int START_BACKUP_AGENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 89;

    int BACKUP_AGENT_CREATED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 90;

    int UNBIND_BACKUP_AGENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 91;

    int GET_UID_FOR_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 92;

    int HANDLE_INCOMING_USER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 93;

    int ADD_PACKAGE_DEPENDENCY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 94;

    int KILL_APPLICATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 95;

    int CLOSE_SYSTEM_DIALOGS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 96;

    int GET_PROCESS_MEMORY_INFO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 97;

    int KILL_APPLICATION_PROCESS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 98;

    int START_ACTIVITY_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 99;

    int OVERRIDE_PENDING_TRANSITION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 100;

    int HANDLE_APPLICATION_WTF_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 101;

    int KILL_BACKGROUND_PROCESSES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 102;

    int IS_USER_A_MONKEY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 103;

    int START_ACTIVITY_AND_WAIT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 104;

    int WILL_ACTIVITY_BE_VISIBLE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 105;

    int START_ACTIVITY_WITH_CONFIG_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 106;

    int GET_RUNNING_EXTERNAL_APPLICATIONS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 107;

    int FINISH_HEAVY_WEIGHT_APP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 108;

    int HANDLE_APPLICATION_STRICT_MODE_VIOLATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 109;

    int IS_IMMERSIVE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 110;

    int SET_IMMERSIVE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 111;

    int IS_TOP_ACTIVITY_IMMERSIVE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 112;

    int CRASH_APPLICATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 113;

    int GET_PROVIDER_MIME_TYPE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 114;

    int NEW_URI_PERMISSION_OWNER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 115;

    int GRANT_URI_PERMISSION_FROM_OWNER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 116;

    int REVOKE_URI_PERMISSION_FROM_OWNER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 117;

    int CHECK_GRANT_URI_PERMISSION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 118;

    int DUMP_HEAP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 119;

    int START_ACTIVITIES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 120;

    int IS_USER_RUNNING_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 121;

    int ACTIVITY_SLEPT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 122;

    int GET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 123;

    int SET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 124;

    int GET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 125;

    int SET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 126;

    int GET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 127;

    int SET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 128;

    int SWITCH_USER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 129;

    int SET_FOCUSED_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 130;

    int REMOVE_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 131;

    int REGISTER_PROCESS_OBSERVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 132;

    int UNREGISTER_PROCESS_OBSERVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 133;

    int IS_INTENT_SENDER_TARGETED_TO_PACKAGE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 134;

    int UPDATE_PERSISTENT_CONFIGURATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 135;

    int GET_PROCESS_PSS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 136;

    int SHOW_BOOT_MESSAGE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 137;

    int KILL_ALL_BACKGROUND_PROCESSES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 139;

    int GET_CONTENT_PROVIDER_EXTERNAL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 140;

    int REMOVE_CONTENT_PROVIDER_EXTERNAL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 141;

    int GET_MY_MEMORY_STATE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 142;

    int KILL_PROCESSES_BELOW_FOREGROUND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 143;

    int GET_CURRENT_USER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 144;

    int SHOULD_UP_RECREATE_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 145;

    int NAVIGATE_UP_TO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 146;

    int SET_LOCK_SCREEN_SHOWN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 147;

    int FINISH_ACTIVITY_AFFINITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 148;

    int GET_LAUNCHED_FROM_UID_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 149;

    int UNSTABLE_PROVIDER_DIED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 150;

    int IS_INTENT_SENDER_AN_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 151;

    int START_ACTIVITY_AS_USER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 152;

    int STOP_USER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 153;

    int REGISTER_USER_SWITCH_OBSERVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 154;

    int UNREGISTER_USER_SWITCH_OBSERVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 155;

    int GET_RUNNING_USER_IDS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 156;

    int REQUEST_BUG_REPORT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 157;

    int INPUT_DISPATCHING_TIMED_OUT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 158;

    int CLEAR_PENDING_BACKUP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 159;

    int GET_INTENT_FOR_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 160;

    int GET_ASSIST_CONTEXT_EXTRAS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 161;

    int REPORT_ASSIST_CONTEXT_EXTRAS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 162;

    int GET_LAUNCHED_FROM_PACKAGE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 163;

    int KILL_UID_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 164;

    int SET_USER_IS_MONKEY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 165;

    int HANG_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 166;

    int CREATE_VIRTUAL_ACTIVITY_CONTAINER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 167;

    int MOVE_TASK_TO_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 168;

    int RESIZE_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 169;

    int GET_ALL_STACK_INFOS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 170;

    int SET_FOCUSED_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 171;

    int GET_STACK_INFO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 172;

    int CONVERT_FROM_TRANSLUCENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 173;

    int CONVERT_TO_TRANSLUCENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 174;

    int NOTIFY_ACTIVITY_DRAWN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 175;

    int REPORT_ACTIVITY_FULLY_DRAWN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 176;

    int RESTART_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 177;

    int PERFORM_IDLE_MAINTENANCE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 178;

    int TAKE_PERSISTABLE_URI_PERMISSION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 179;

    int RELEASE_PERSISTABLE_URI_PERMISSION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 180;

    int GET_PERSISTED_URI_PERMISSIONS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 181;

    int APP_NOT_RESPONDING_VIA_PROVIDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 182;

    int GET_TASK_BOUNDS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 183;

    int GET_ACTIVITY_DISPLAY_ID_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 184;

    int DELETE_ACTIVITY_CONTAINER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 185;

    int SET_PROCESS_MEMORY_TRIM_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 186;

    // Start of L transactions
    int GET_TAG_FOR_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 210;

    int START_USER_IN_BACKGROUND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 211;

    int IS_IN_HOME_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 212;

    int START_LOCK_TASK_BY_TASK_ID_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 213;

    int START_LOCK_TASK_BY_TOKEN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 214;

    int STOP_LOCK_TASK_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 215;

    int IS_IN_LOCK_TASK_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 216;

    int SET_TASK_DESCRIPTION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 217;

    int START_VOICE_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 218;

    int GET_ACTIVITY_OPTIONS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 219;

    int GET_APP_TASKS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 220;

    int START_SYSTEM_LOCK_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 221;

    int STOP_SYSTEM_LOCK_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 222;

    int FINISH_VOICE_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 223;

    int IS_TOP_OF_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 224;

    int REQUEST_VISIBLE_BEHIND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 225;

    int IS_BACKGROUND_VISIBLE_BEHIND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 226;

    int BACKGROUND_RESOURCES_RELEASED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 227;

    int NOTIFY_LAUNCH_TASK_BEHIND_COMPLETE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 228;

    int START_ACTIVITY_FROM_RECENTS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 229;

    int NOTIFY_ENTER_ANIMATION_COMPLETE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 230;

    int KEYGUARD_WAITING_FOR_ACTIVITY_DRAWN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 231;

    int START_ACTIVITY_AS_CALLER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 232;

    int ADD_APP_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 233;

    int GET_APP_TASK_THUMBNAIL_SIZE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 234;

    int RELEASE_ACTIVITY_INSTANCE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 235;

    int RELEASE_SOME_ACTIVITIES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 236;

    int BOOT_ANIMATION_COMPLETE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 237;

    int GET_TASK_DESCRIPTION_ICON_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 238;

    int LAUNCH_ASSIST_INTENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 239;

    int START_IN_PLACE_ANIMATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 240;

    int CHECK_PERMISSION_WITH_TOKEN_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 241;

    int REGISTER_TASK_STACK_LISTENER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 242;

    // Start of M transactions
    int NOTIFY_CLEARTEXT_NETWORK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 280;

    int CREATE_STACK_ON_DISPLAY = android.os.IBinder.FIRST_CALL_TRANSACTION + 281;

    int GET_FOCUSED_STACK_ID_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 282;

    int SET_TASK_RESIZEABLE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 283;

    int REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 284;

    int RESIZE_TASK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 285;

    int GET_LOCK_TASK_MODE_STATE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 286;

    int SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 287;

    int DUMP_HEAP_FINISHED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 288;

    int SET_VOICE_KEEP_AWAKE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 289;

    int UPDATE_LOCK_TASK_PACKAGES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 290;

    int NOTE_ALARM_START_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 291;

    int NOTE_ALARM_FINISH_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 292;

    int GET_PACKAGE_PROCESS_STATE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 293;

    int SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 294;

    int UPDATE_DEVICE_OWNER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 295;

    int KEYGUARD_GOING_AWAY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 296;

    int REGISTER_UID_OBSERVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 297;

    int UNREGISTER_UID_OBSERVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 298;

    int IS_SCREEN_CAPTURE_ALLOWED_ON_CURRENT_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 299;

    int SHOW_ASSIST_FROM_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 300;

    int IS_ROOT_VOICE_INTERACTION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 301;

    // Start of N transactions
    int START_BINDER_TRACKING_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 340;

    int STOP_BINDER_TRACKING_AND_DUMP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 341;

    int POSITION_TASK_IN_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 342;

    int GET_ACTIVITY_STACK_ID_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 343;

    int EXIT_FREEFORM_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 344;

    int REPORT_SIZE_CONFIGURATIONS = android.os.IBinder.FIRST_CALL_TRANSACTION + 345;

    int MOVE_TASK_TO_DOCKED_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 346;

    int SUPPRESS_RESIZE_CONFIG_CHANGES_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 347;

    int MOVE_TASKS_TO_FULLSCREEN_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 348;

    int MOVE_TOP_ACTIVITY_TO_PINNED_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 349;

    int GET_APP_START_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 350;

    int UNLOCK_USER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 351;

    int IN_MULTI_WINDOW_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 352;

    int IN_PICTURE_IN_PICTURE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 353;

    int KILL_PACKAGE_DEPENDENTS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 354;

    int ENTER_PICTURE_IN_PICTURE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 355;

    int ACTIVITY_RELAUNCHED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 356;

    int GET_URI_PERMISSION_OWNER_FOR_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 357;

    int RESIZE_DOCKED_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 358;

    int SET_VR_MODE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 359;

    int GET_GRANTED_URI_PERMISSIONS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 360;

    int CLEAR_GRANTED_URI_PERMISSIONS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 361;

    int IS_APP_FOREGROUND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 362;

    int START_LOCAL_VOICE_INTERACTION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 363;

    int STOP_LOCAL_VOICE_INTERACTION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 364;

    int SUPPORTS_LOCAL_VOICE_INTERACTION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 365;

    int NOTIFY_PINNED_STACK_ANIMATION_ENDED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 366;

    int REMOVE_STACK = android.os.IBinder.FIRST_CALL_TRANSACTION + 367;

    int SET_LENIENT_BACKGROUND_CHECK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 368;

    int GET_MEMORY_TRIM_LEVEL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 369;

    int RESIZE_PINNED_STACK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 370;

    int IS_VR_PACKAGE_ENABLED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 371;

    int SWAP_DOCKED_AND_FULLSCREEN_STACK = android.os.IBinder.FIRST_CALL_TRANSACTION + 372;

    int NOTIFY_LOCKED_PROFILE = android.os.IBinder.FIRST_CALL_TRANSACTION + 373;

    int START_CONFIRM_DEVICE_CREDENTIAL_INTENT = android.os.IBinder.FIRST_CALL_TRANSACTION + 374;

    int SEND_IDLE_JOB_TRIGGER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 375;

    int SEND_INTENT_SENDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 376;

    // Start of N MR1 transactions
    int SET_VR_THREAD_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 377;

    int SET_RENDER_THREAD_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 378;

    int SET_HAS_TOP_UI = android.os.IBinder.FIRST_CALL_TRANSACTION + 379;

    int CAN_BYPASS_WORK_CHALLENGE = android.os.IBinder.FIRST_CALL_TRANSACTION + 380;
}

