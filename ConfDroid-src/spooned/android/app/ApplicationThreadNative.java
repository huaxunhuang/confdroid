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
 * {@hide }
 */
public abstract class ApplicationThreadNative extends android.os.Binder implements android.app.IApplicationThread {
    /**
     * Cast a Binder object into an application thread interface, generating
     * a proxy if needed.
     */
    public static android.app.IApplicationThread asInterface(android.os.IBinder obj) {
        if (obj == null) {
            return null;
        }
        android.app.IApplicationThread in = ((android.app.IApplicationThread) (obj.queryLocalInterface(android.app.IApplicationThread.descriptor)));
        if (in != null) {
            return in;
        }
        return new android.app.ApplicationThreadProxy(obj);
    }

    public ApplicationThreadNative() {
        attachInterface(this, android.app.IApplicationThread.descriptor);
    }

    @java.lang.Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        switch (code) {
            case android.app.IApplicationThread.SCHEDULE_PAUSE_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    boolean finished = data.readInt() != 0;
                    boolean userLeaving = data.readInt() != 0;
                    int configChanges = data.readInt();
                    boolean dontReport = data.readInt() != 0;
                    schedulePauseActivity(b, finished, userLeaving, configChanges, dontReport);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_STOP_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    boolean show = data.readInt() != 0;
                    int configChanges = data.readInt();
                    scheduleStopActivity(b, show, configChanges);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_WINDOW_VISIBILITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    boolean show = data.readInt() != 0;
                    scheduleWindowVisibility(b, show);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_SLEEPING_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    boolean sleeping = data.readInt() != 0;
                    scheduleSleeping(b, sleeping);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_RESUME_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    int procState = data.readInt();
                    boolean isForward = data.readInt() != 0;
                    android.os.Bundle resumeArgs = data.readBundle();
                    scheduleResumeActivity(b, procState, isForward, resumeArgs);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_SEND_RESULT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    java.util.List<android.app.ResultInfo> ri = data.createTypedArrayList(android.app.ResultInfo.CREATOR);
                    scheduleSendResult(b, ri);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_LAUNCH_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    android.os.IBinder b = data.readStrongBinder();
                    int ident = data.readInt();
                    android.content.pm.ActivityInfo info = android.content.pm.ActivityInfo.CREATOR.createFromParcel(data);
                    android.content.res.Configuration curConfig = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    android.content.res.Configuration overrideConfig = null;
                    if (data.readInt() != 0) {
                        overrideConfig = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    }
                    android.content.res.CompatibilityInfo compatInfo = android.content.res.CompatibilityInfo.CREATOR.createFromParcel(data);
                    java.lang.String referrer = data.readString();
                    com.android.internal.app.IVoiceInteractor voiceInteractor = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                    int procState = data.readInt();
                    android.os.Bundle state = data.readBundle();
                    android.os.PersistableBundle persistentState = data.readPersistableBundle();
                    java.util.List<android.app.ResultInfo> ri = data.createTypedArrayList(android.app.ResultInfo.CREATOR);
                    java.util.List<com.android.internal.content.ReferrerIntent> pi = data.createTypedArrayList(ReferrerIntent.CREATOR);
                    boolean notResumed = data.readInt() != 0;
                    boolean isForward = data.readInt() != 0;
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    scheduleLaunchActivity(intent, b, ident, info, curConfig, overrideConfig, compatInfo, referrer, voiceInteractor, procState, state, persistentState, ri, pi, notResumed, isForward, profilerInfo);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_RELAUNCH_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    java.util.List<android.app.ResultInfo> ri = data.createTypedArrayList(android.app.ResultInfo.CREATOR);
                    java.util.List<com.android.internal.content.ReferrerIntent> pi = data.createTypedArrayList(ReferrerIntent.CREATOR);
                    int configChanges = data.readInt();
                    boolean notResumed = data.readInt() != 0;
                    android.content.res.Configuration config = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    android.content.res.Configuration overrideConfig = null;
                    if (data.readInt() != 0) {
                        overrideConfig = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    }
                    boolean preserveWindows = data.readInt() == 1;
                    scheduleRelaunchActivity(b, ri, pi, configChanges, notResumed, config, overrideConfig, preserveWindows);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_NEW_INTENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    java.util.List<com.android.internal.content.ReferrerIntent> pi = data.createTypedArrayList(ReferrerIntent.CREATOR);
                    android.os.IBinder b = data.readStrongBinder();
                    final boolean andPause = data.readInt() == 1;
                    scheduleNewIntent(pi, b, andPause);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_FINISH_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    boolean finishing = data.readInt() != 0;
                    int configChanges = data.readInt();
                    scheduleDestroyActivity(b, finishing, configChanges);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_RECEIVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    android.content.pm.ActivityInfo info = android.content.pm.ActivityInfo.CREATOR.createFromParcel(data);
                    android.content.res.CompatibilityInfo compatInfo = android.content.res.CompatibilityInfo.CREATOR.createFromParcel(data);
                    int resultCode = data.readInt();
                    java.lang.String resultData = data.readString();
                    android.os.Bundle resultExtras = data.readBundle();
                    boolean sync = data.readInt() != 0;
                    int sendingUser = data.readInt();
                    int processState = data.readInt();
                    scheduleReceiver(intent, info, compatInfo, resultCode, resultData, resultExtras, sync, sendingUser, processState);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_CREATE_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.pm.ServiceInfo info = android.content.pm.ServiceInfo.CREATOR.createFromParcel(data);
                    android.content.res.CompatibilityInfo compatInfo = android.content.res.CompatibilityInfo.CREATOR.createFromParcel(data);
                    int processState = data.readInt();
                    scheduleCreateService(token, info, compatInfo, processState);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_BIND_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    boolean rebind = data.readInt() != 0;
                    int processState = data.readInt();
                    scheduleBindService(token, intent, rebind, processState);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_UNBIND_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    scheduleUnbindService(token, intent);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_SERVICE_ARGS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean taskRemoved = data.readInt() != 0;
                    int startId = data.readInt();
                    int fl = data.readInt();
                    android.content.Intent args;
                    if (data.readInt() != 0) {
                        args = android.content.Intent.CREATOR.createFromParcel(data);
                    } else {
                        args = null;
                    }
                    scheduleServiceArgs(token, taskRemoved, startId, fl, args);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_STOP_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    scheduleStopService(token);
                    return true;
                }
            case android.app.IApplicationThread.BIND_APPLICATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    java.lang.String packageName = data.readString();
                    android.content.pm.ApplicationInfo info = android.content.pm.ApplicationInfo.CREATOR.createFromParcel(data);
                    java.util.List<android.content.pm.ProviderInfo> providers = data.createTypedArrayList(android.content.pm.ProviderInfo.CREATOR);
                    android.content.ComponentName testName = (data.readInt() != 0) ? new android.content.ComponentName(data) : null;
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    android.os.Bundle testArgs = data.readBundle();
                    android.os.IBinder binder = data.readStrongBinder();
                    android.app.IInstrumentationWatcher testWatcher = IInstrumentationWatcher.Stub.asInterface(binder);
                    binder = data.readStrongBinder();
                    android.app.IUiAutomationConnection uiAutomationConnection = IUiAutomationConnection.Stub.asInterface(binder);
                    int testMode = data.readInt();
                    boolean enableBinderTracking = data.readInt() != 0;
                    boolean trackAllocation = data.readInt() != 0;
                    boolean restrictedBackupMode = data.readInt() != 0;
                    boolean persistent = data.readInt() != 0;
                    android.content.res.Configuration config = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    android.content.res.CompatibilityInfo compatInfo = android.content.res.CompatibilityInfo.CREATOR.createFromParcel(data);
                    java.util.HashMap<java.lang.String, android.os.IBinder> services = data.readHashMap(null);
                    android.os.Bundle coreSettings = data.readBundle();
                    bindApplication(packageName, info, providers, testName, profilerInfo, testArgs, testWatcher, uiAutomationConnection, testMode, enableBinderTracking, trackAllocation, restrictedBackupMode, persistent, config, compatInfo, services, coreSettings);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_EXIT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    scheduleExit();
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_SUICIDE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    scheduleSuicide();
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_CONFIGURATION_CHANGED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.content.res.Configuration config = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    scheduleConfigurationChanged(config);
                    return true;
                }
            case android.app.IApplicationThread.UPDATE_TIME_ZONE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    updateTimeZone();
                    return true;
                }
            case android.app.IApplicationThread.CLEAR_DNS_CACHE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    clearDnsCache();
                    return true;
                }
            case android.app.IApplicationThread.SET_HTTP_PROXY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    final java.lang.String proxy = data.readString();
                    final java.lang.String port = data.readString();
                    final java.lang.String exclList = data.readString();
                    final android.net.Uri pacFileUrl = android.net.Uri.CREATOR.createFromParcel(data);
                    setHttpProxy(proxy, port, exclList, pacFileUrl);
                    return true;
                }
            case android.app.IApplicationThread.PROCESS_IN_BACKGROUND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    processInBackground();
                    return true;
                }
            case android.app.IApplicationThread.DUMP_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                    final android.os.IBinder service = data.readStrongBinder();
                    final java.lang.String[] args = data.readStringArray();
                    if (fd != null) {
                        dumpService(fd.getFileDescriptor(), service, args);
                        try {
                            fd.close();
                        } catch (java.io.IOException e) {
                        }
                    }
                    return true;
                }
            case android.app.IApplicationThread.DUMP_PROVIDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                    final android.os.IBinder service = data.readStrongBinder();
                    final java.lang.String[] args = data.readStringArray();
                    if (fd != null) {
                        dumpProvider(fd.getFileDescriptor(), service, args);
                        try {
                            fd.close();
                        } catch (java.io.IOException e) {
                        }
                    }
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_REGISTERED_RECEIVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.content.IIntentReceiver receiver = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    int resultCode = data.readInt();
                    java.lang.String dataStr = data.readString();
                    android.os.Bundle extras = data.readBundle();
                    boolean ordered = data.readInt() != 0;
                    boolean sticky = data.readInt() != 0;
                    int sendingUser = data.readInt();
                    int processState = data.readInt();
                    scheduleRegisteredReceiver(receiver, intent, resultCode, dataStr, extras, ordered, sticky, sendingUser, processState);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_LOW_MEMORY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    scheduleLowMemory();
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_ACTIVITY_CONFIGURATION_CHANGED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.content.res.Configuration overrideConfig = null;
                    if (data.readInt() != 0) {
                        overrideConfig = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    }
                    final boolean reportToActivity = data.readInt() == 1;
                    scheduleActivityConfigurationChanged(b, overrideConfig, reportToActivity);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_LOCAL_VOICE_INTERACTION_STARTED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    com.android.internal.app.IVoiceInteractor voiceInteractor = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                    scheduleLocalVoiceInteractionStarted(token, voiceInteractor);
                    return true;
                }
            case android.app.IApplicationThread.PROFILER_CONTROL_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    boolean start = data.readInt() != 0;
                    int profileType = data.readInt();
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    profilerControl(start, profilerInfo, profileType);
                    return true;
                }
            case android.app.IApplicationThread.SET_SCHEDULING_GROUP_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    int group = data.readInt();
                    setSchedulingGroup(group);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_CREATE_BACKUP_AGENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.content.pm.ApplicationInfo appInfo = android.content.pm.ApplicationInfo.CREATOR.createFromParcel(data);
                    android.content.res.CompatibilityInfo compatInfo = android.content.res.CompatibilityInfo.CREATOR.createFromParcel(data);
                    int backupMode = data.readInt();
                    scheduleCreateBackupAgent(appInfo, compatInfo, backupMode);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_DESTROY_BACKUP_AGENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.content.pm.ApplicationInfo appInfo = android.content.pm.ApplicationInfo.CREATOR.createFromParcel(data);
                    android.content.res.CompatibilityInfo compatInfo = android.content.res.CompatibilityInfo.CREATOR.createFromParcel(data);
                    scheduleDestroyBackupAgent(appInfo, compatInfo);
                    return true;
                }
            case android.app.IApplicationThread.DISPATCH_PACKAGE_BROADCAST_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    int cmd = data.readInt();
                    java.lang.String[] packages = data.readStringArray();
                    dispatchPackageBroadcast(cmd, packages);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_CRASH_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    java.lang.String msg = data.readString();
                    scheduleCrash(msg);
                    return true;
                }
            case android.app.IApplicationThread.DUMP_HEAP_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    boolean managed = data.readInt() != 0;
                    java.lang.String path = data.readString();
                    android.os.ParcelFileDescriptor fd = (data.readInt() != 0) ? android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    dumpHeap(managed, path, fd);
                    return true;
                }
            case android.app.IApplicationThread.DUMP_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                    final android.os.IBinder activity = data.readStrongBinder();
                    final java.lang.String prefix = data.readString();
                    final java.lang.String[] args = data.readStringArray();
                    if (fd != null) {
                        dumpActivity(fd.getFileDescriptor(), activity, prefix, args);
                        try {
                            fd.close();
                        } catch (java.io.IOException e) {
                        }
                    }
                    return true;
                }
            case android.app.IApplicationThread.SET_CORE_SETTINGS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.Bundle settings = data.readBundle();
                    setCoreSettings(settings);
                    return true;
                }
            case android.app.IApplicationThread.UPDATE_PACKAGE_COMPATIBILITY_INFO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    java.lang.String pkg = data.readString();
                    android.content.res.CompatibilityInfo compat = android.content.res.CompatibilityInfo.CREATOR.createFromParcel(data);
                    updatePackageCompatibilityInfo(pkg, compat);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_TRIM_MEMORY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    int level = data.readInt();
                    scheduleTrimMemory(level);
                    return true;
                }
            case android.app.IApplicationThread.DUMP_MEM_INFO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                    android.os.Debug.MemoryInfo mi = android.os.Debug.MemoryInfo.CREATOR.createFromParcel(data);
                    boolean checkin = data.readInt() != 0;
                    boolean dumpInfo = data.readInt() != 0;
                    boolean dumpDalvik = data.readInt() != 0;
                    boolean dumpSummaryOnly = data.readInt() != 0;
                    boolean dumpUnreachable = data.readInt() != 0;
                    java.lang.String[] args = data.readStringArray();
                    if (fd != null) {
                        try {
                            dumpMemInfo(fd.getFileDescriptor(), mi, checkin, dumpInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable, args);
                        } finally {
                            try {
                                fd.close();
                            } catch (java.io.IOException e) {
                                // swallowed, not propagated back to the caller
                            }
                        }
                    }
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.DUMP_GFX_INFO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                    java.lang.String[] args = data.readStringArray();
                    if (fd != null) {
                        try {
                            dumpGfxInfo(fd.getFileDescriptor(), args);
                        } finally {
                            try {
                                fd.close();
                            } catch (java.io.IOException e) {
                                // swallowed, not propagated back to the caller
                            }
                        }
                    }
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.DUMP_DB_INFO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                    java.lang.String[] args = data.readStringArray();
                    if (fd != null) {
                        try {
                            dumpDbInfo(fd.getFileDescriptor(), args);
                        } finally {
                            try {
                                fd.close();
                            } catch (java.io.IOException e) {
                                // swallowed, not propagated back to the caller
                            }
                        }
                    }
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.UNSTABLE_PROVIDER_DIED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder provider = data.readStrongBinder();
                    unstableProviderDied(provider);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder activityToken = data.readStrongBinder();
                    android.os.IBinder requestToken = data.readStrongBinder();
                    int requestType = data.readInt();
                    int sessionId = data.readInt();
                    requestAssistContextExtras(activityToken, requestToken, requestType, sessionId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_TRANSLUCENT_CONVERSION_COMPLETE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean timeout = data.readInt() == 1;
                    scheduleTranslucentConversionComplete(token, timeout);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_ON_NEW_ACTIVITY_OPTIONS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.app.ActivityOptions options = new android.app.ActivityOptions(data.readBundle());
                    scheduleOnNewActivityOptions(token, options);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.SET_PROCESS_STATE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    int state = data.readInt();
                    setProcessState(state);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_INSTALL_PROVIDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.content.pm.ProviderInfo provider = android.content.pm.ProviderInfo.CREATOR.createFromParcel(data);
                    scheduleInstallProvider(provider);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.UPDATE_TIME_PREFS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    byte is24Hour = data.readByte();
                    updateTimePrefs(is24Hour == ((byte) (1)));
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.CANCEL_VISIBLE_BEHIND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    scheduleCancelVisibleBehind(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.BACKGROUND_VISIBLE_BEHIND_CHANGED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean enabled = data.readInt() > 0;
                    scheduleBackgroundVisibleBehindChanged(token, enabled);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.ENTER_ANIMATION_COMPLETE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    scheduleEnterAnimationComplete(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.NOTIFY_CLEARTEXT_NETWORK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    final byte[] firstPacket = data.createByteArray();
                    notifyCleartextNetwork(firstPacket);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IApplicationThread.START_BINDER_TRACKING_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    startBinderTracking();
                    return true;
                }
            case android.app.IApplicationThread.STOP_BINDER_TRACKING_AND_DUMP_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    android.os.ParcelFileDescriptor fd = data.readFileDescriptor();
                    if (fd != null) {
                        stopBinderTrackingAndDump(fd.getFileDescriptor());
                        try {
                            fd.close();
                        } catch (java.io.IOException e) {
                        }
                    }
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_MULTI_WINDOW_CHANGED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    final android.os.IBinder b = data.readStrongBinder();
                    final boolean inMultiWindow = data.readInt() != 0;
                    scheduleMultiWindowModeChanged(b, inMultiWindow);
                    return true;
                }
            case android.app.IApplicationThread.SCHEDULE_PICTURE_IN_PICTURE_CHANGED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IApplicationThread.descriptor);
                    final android.os.IBinder b = data.readStrongBinder();
                    final boolean inPip = data.readInt() != 0;
                    schedulePictureInPictureModeChanged(b, inPip);
                    return true;
                }
        }
        return super.onTransact(code, data, reply, flags);
    }

    public android.os.IBinder asBinder() {
        return this;
    }
}

