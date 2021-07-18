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
 * System private API for communicating with the application.  This is given to
 * the activity manager by an application  when it starts up, for the activity
 * manager to tell the application about things it needs to do.
 *
 * {@hide }
 */
public interface IApplicationThread extends android.os.IInterface {
    void schedulePauseActivity(android.os.IBinder token, boolean finished, boolean userLeaving, int configChanges, boolean dontReport) throws android.os.RemoteException;

    void scheduleStopActivity(android.os.IBinder token, boolean showWindow, int configChanges) throws android.os.RemoteException;

    void scheduleWindowVisibility(android.os.IBinder token, boolean showWindow) throws android.os.RemoteException;

    void scheduleSleeping(android.os.IBinder token, boolean sleeping) throws android.os.RemoteException;

    void scheduleResumeActivity(android.os.IBinder token, int procState, boolean isForward, android.os.Bundle resumeArgs) throws android.os.RemoteException;

    void scheduleSendResult(android.os.IBinder token, java.util.List<android.app.ResultInfo> results) throws android.os.RemoteException;

    void scheduleLaunchActivity(android.content.Intent intent, android.os.IBinder token, int ident, android.content.pm.ActivityInfo info, android.content.res.Configuration curConfig, android.content.res.Configuration overrideConfig, android.content.res.CompatibilityInfo compatInfo, java.lang.String referrer, com.android.internal.app.IVoiceInteractor voiceInteractor, int procState, android.os.Bundle state, android.os.PersistableBundle persistentState, java.util.List<android.app.ResultInfo> pendingResults, java.util.List<com.android.internal.content.ReferrerIntent> pendingNewIntents, boolean notResumed, boolean isForward, android.app.ProfilerInfo profilerInfo) throws android.os.RemoteException;

    void scheduleRelaunchActivity(android.os.IBinder token, java.util.List<android.app.ResultInfo> pendingResults, java.util.List<com.android.internal.content.ReferrerIntent> pendingNewIntents, int configChanges, boolean notResumed, android.content.res.Configuration config, android.content.res.Configuration overrideConfig, boolean preserveWindow) throws android.os.RemoteException;

    void scheduleNewIntent(java.util.List<com.android.internal.content.ReferrerIntent> intent, android.os.IBinder token, boolean andPause) throws android.os.RemoteException;

    void scheduleDestroyActivity(android.os.IBinder token, boolean finished, int configChanges) throws android.os.RemoteException;

    void scheduleReceiver(android.content.Intent intent, android.content.pm.ActivityInfo info, android.content.res.CompatibilityInfo compatInfo, int resultCode, java.lang.String data, android.os.Bundle extras, boolean sync, int sendingUser, int processState) throws android.os.RemoteException;

    static final int BACKUP_MODE_INCREMENTAL = 0;

    static final int BACKUP_MODE_FULL = 1;

    static final int BACKUP_MODE_RESTORE = 2;

    static final int BACKUP_MODE_RESTORE_FULL = 3;

    void scheduleCreateBackupAgent(android.content.pm.ApplicationInfo app, android.content.res.CompatibilityInfo compatInfo, int backupMode) throws android.os.RemoteException;

    void scheduleDestroyBackupAgent(android.content.pm.ApplicationInfo app, android.content.res.CompatibilityInfo compatInfo) throws android.os.RemoteException;

    void scheduleCreateService(android.os.IBinder token, android.content.pm.ServiceInfo info, android.content.res.CompatibilityInfo compatInfo, int processState) throws android.os.RemoteException;

    void scheduleBindService(android.os.IBinder token, android.content.Intent intent, boolean rebind, int processState) throws android.os.RemoteException;

    void scheduleUnbindService(android.os.IBinder token, android.content.Intent intent) throws android.os.RemoteException;

    void scheduleServiceArgs(android.os.IBinder token, boolean taskRemoved, int startId, int flags, android.content.Intent args) throws android.os.RemoteException;

    void scheduleStopService(android.os.IBinder token) throws android.os.RemoteException;

    static final int DEBUG_OFF = 0;

    static final int DEBUG_ON = 1;

    static final int DEBUG_WAIT = 2;

    void bindApplication(java.lang.String packageName, android.content.pm.ApplicationInfo info, java.util.List<android.content.pm.ProviderInfo> providers, android.content.ComponentName testName, android.app.ProfilerInfo profilerInfo, android.os.Bundle testArguments, android.app.IInstrumentationWatcher testWatcher, android.app.IUiAutomationConnection uiAutomationConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean restrictedBackupMode, boolean persistent, android.content.res.Configuration config, android.content.res.CompatibilityInfo compatInfo, java.util.Map<java.lang.String, android.os.IBinder> services, android.os.Bundle coreSettings) throws android.os.RemoteException;

    void scheduleExit() throws android.os.RemoteException;

    void scheduleSuicide() throws android.os.RemoteException;

    void scheduleConfigurationChanged(android.content.res.Configuration config) throws android.os.RemoteException;

    void updateTimeZone() throws android.os.RemoteException;

    void clearDnsCache() throws android.os.RemoteException;

    void setHttpProxy(java.lang.String proxy, java.lang.String port, java.lang.String exclList, android.net.Uri pacFileUrl) throws android.os.RemoteException;

    void processInBackground() throws android.os.RemoteException;

    void dumpService(java.io.FileDescriptor fd, android.os.IBinder servicetoken, java.lang.String[] args) throws android.os.RemoteException;

    void dumpProvider(java.io.FileDescriptor fd, android.os.IBinder servicetoken, java.lang.String[] args) throws android.os.RemoteException;

    void scheduleRegisteredReceiver(android.content.IIntentReceiver receiver, android.content.Intent intent, int resultCode, java.lang.String data, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws android.os.RemoteException;

    void scheduleLowMemory() throws android.os.RemoteException;

    void scheduleActivityConfigurationChanged(android.os.IBinder token, android.content.res.Configuration overrideConfig, boolean reportToActivity) throws android.os.RemoteException;

    void profilerControl(boolean start, android.app.ProfilerInfo profilerInfo, int profileType) throws android.os.RemoteException;

    void dumpHeap(boolean managed, java.lang.String path, android.os.ParcelFileDescriptor fd) throws android.os.RemoteException;

    void setSchedulingGroup(int group) throws android.os.RemoteException;

    // the package has been removed, clean up internal references
    static final int PACKAGE_REMOVED = 0;

    static final int EXTERNAL_STORAGE_UNAVAILABLE = 1;

    // the package is being modified in-place, don't kill it and retain references to it
    static final int PACKAGE_REMOVED_DONT_KILL = 2;

    // a previously removed package was replaced with a new version [eg. upgrade, split added, ...]
    static final int PACKAGE_REPLACED = 3;

    void dispatchPackageBroadcast(int cmd, java.lang.String[] packages) throws android.os.RemoteException;

    void scheduleCrash(java.lang.String msg) throws android.os.RemoteException;

    void dumpActivity(java.io.FileDescriptor fd, android.os.IBinder servicetoken, java.lang.String prefix, java.lang.String[] args) throws android.os.RemoteException;

    void setCoreSettings(android.os.Bundle coreSettings) throws android.os.RemoteException;

    void updatePackageCompatibilityInfo(java.lang.String pkg, android.content.res.CompatibilityInfo info) throws android.os.RemoteException;

    void scheduleTrimMemory(int level) throws android.os.RemoteException;

    void dumpMemInfo(java.io.FileDescriptor fd, android.os.Debug.MemoryInfo mem, boolean checkin, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, java.lang.String[] args) throws android.os.RemoteException;

    void dumpGfxInfo(java.io.FileDescriptor fd, java.lang.String[] args) throws android.os.RemoteException;

    void dumpDbInfo(java.io.FileDescriptor fd, java.lang.String[] args) throws android.os.RemoteException;

    void unstableProviderDied(android.os.IBinder provider) throws android.os.RemoteException;

    void requestAssistContextExtras(android.os.IBinder activityToken, android.os.IBinder requestToken, int requestType, int sessionId) throws android.os.RemoteException;

    void scheduleTranslucentConversionComplete(android.os.IBinder token, boolean timeout) throws android.os.RemoteException;

    void scheduleOnNewActivityOptions(android.os.IBinder token, android.app.ActivityOptions options) throws android.os.RemoteException;

    void setProcessState(int state) throws android.os.RemoteException;

    void scheduleInstallProvider(android.content.pm.ProviderInfo provider) throws android.os.RemoteException;

    void updateTimePrefs(boolean is24Hour) throws android.os.RemoteException;

    void scheduleCancelVisibleBehind(android.os.IBinder token) throws android.os.RemoteException;

    void scheduleBackgroundVisibleBehindChanged(android.os.IBinder token, boolean enabled) throws android.os.RemoteException;

    void scheduleEnterAnimationComplete(android.os.IBinder token) throws android.os.RemoteException;

    void notifyCleartextNetwork(byte[] firstPacket) throws android.os.RemoteException;

    void startBinderTracking() throws android.os.RemoteException;

    void stopBinderTrackingAndDump(java.io.FileDescriptor fd) throws android.os.RemoteException;

    void scheduleMultiWindowModeChanged(android.os.IBinder token, boolean isInMultiWindowMode) throws android.os.RemoteException;

    void schedulePictureInPictureModeChanged(android.os.IBinder token, boolean isInPictureInPictureMode) throws android.os.RemoteException;

    void scheduleLocalVoiceInteractionStarted(android.os.IBinder token, com.android.internal.app.IVoiceInteractor voiceInteractor) throws android.os.RemoteException;

    java.lang.String descriptor = "android.app.IApplicationThread";

    int SCHEDULE_PAUSE_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION;

    int SCHEDULE_STOP_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 2;

    int SCHEDULE_WINDOW_VISIBILITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 3;

    int SCHEDULE_RESUME_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 4;

    int SCHEDULE_SEND_RESULT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 5;

    int SCHEDULE_LAUNCH_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 6;

    int SCHEDULE_NEW_INTENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 7;

    int SCHEDULE_FINISH_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 8;

    int SCHEDULE_RECEIVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 9;

    int SCHEDULE_CREATE_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 10;

    int SCHEDULE_STOP_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 11;

    int BIND_APPLICATION_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 12;

    int SCHEDULE_EXIT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 13;

    int SCHEDULE_CONFIGURATION_CHANGED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 15;

    int SCHEDULE_SERVICE_ARGS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 16;

    int UPDATE_TIME_ZONE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 17;

    int PROCESS_IN_BACKGROUND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 18;

    int SCHEDULE_BIND_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 19;

    int SCHEDULE_UNBIND_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 20;

    int DUMP_SERVICE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 21;

    int SCHEDULE_REGISTERED_RECEIVER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 22;

    int SCHEDULE_LOW_MEMORY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 23;

    int SCHEDULE_ACTIVITY_CONFIGURATION_CHANGED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 24;

    int SCHEDULE_RELAUNCH_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 25;

    int SCHEDULE_SLEEPING_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 26;

    int PROFILER_CONTROL_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 27;

    int SET_SCHEDULING_GROUP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 28;

    int SCHEDULE_CREATE_BACKUP_AGENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 29;

    int SCHEDULE_DESTROY_BACKUP_AGENT_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 30;

    int SCHEDULE_ON_NEW_ACTIVITY_OPTIONS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 31;

    int SCHEDULE_SUICIDE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 32;

    int DISPATCH_PACKAGE_BROADCAST_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 33;

    int SCHEDULE_CRASH_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 34;

    int DUMP_HEAP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 35;

    int DUMP_ACTIVITY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 36;

    int CLEAR_DNS_CACHE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 37;

    int SET_HTTP_PROXY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 38;

    int SET_CORE_SETTINGS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 39;

    int UPDATE_PACKAGE_COMPATIBILITY_INFO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 40;

    int SCHEDULE_TRIM_MEMORY_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 41;

    int DUMP_MEM_INFO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 42;

    int DUMP_GFX_INFO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 43;

    int DUMP_PROVIDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 44;

    int DUMP_DB_INFO_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 45;

    int UNSTABLE_PROVIDER_DIED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 46;

    int REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 47;

    int SCHEDULE_TRANSLUCENT_CONVERSION_COMPLETE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 48;

    int SET_PROCESS_STATE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 49;

    int SCHEDULE_INSTALL_PROVIDER_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 50;

    int UPDATE_TIME_PREFS_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 51;

    int CANCEL_VISIBLE_BEHIND_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 52;

    int BACKGROUND_VISIBLE_BEHIND_CHANGED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 53;

    int ENTER_ANIMATION_COMPLETE_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 54;

    int NOTIFY_CLEARTEXT_NETWORK_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 55;

    int START_BINDER_TRACKING_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 56;

    int STOP_BINDER_TRACKING_AND_DUMP_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 57;

    int SCHEDULE_MULTI_WINDOW_CHANGED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 58;

    int SCHEDULE_PICTURE_IN_PICTURE_CHANGED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 59;

    int SCHEDULE_LOCAL_VOICE_INTERACTION_STARTED_TRANSACTION = android.os.IBinder.FIRST_CALL_TRANSACTION + 60;
}

