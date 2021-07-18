package android.app;


class ApplicationThreadProxy implements android.app.IApplicationThread {
    private final android.os.IBinder mRemote;

    public ApplicationThreadProxy(android.os.IBinder remote) {
        mRemote = remote;
    }

    public final android.os.IBinder asBinder() {
        return mRemote;
    }

    public final void schedulePauseActivity(android.os.IBinder token, boolean finished, boolean userLeaving, int configChanges, boolean dontReport) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(finished ? 1 : 0);
        data.writeInt(userLeaving ? 1 : 0);
        data.writeInt(configChanges);
        data.writeInt(dontReport ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_PAUSE_ACTIVITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleStopActivity(android.os.IBinder token, boolean showWindow, int configChanges) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(showWindow ? 1 : 0);
        data.writeInt(configChanges);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_STOP_ACTIVITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleWindowVisibility(android.os.IBinder token, boolean showWindow) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(showWindow ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_WINDOW_VISIBILITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleSleeping(android.os.IBinder token, boolean sleeping) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(sleeping ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_SLEEPING_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleResumeActivity(android.os.IBinder token, int procState, boolean isForward, android.os.Bundle resumeArgs) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(procState);
        data.writeInt(isForward ? 1 : 0);
        data.writeBundle(resumeArgs);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_RESUME_ACTIVITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleSendResult(android.os.IBinder token, java.util.List<android.app.ResultInfo> results) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeTypedList(results);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_SEND_RESULT_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleLaunchActivity(android.content.Intent intent, android.os.IBinder token, int ident, android.content.pm.ActivityInfo info, android.content.res.Configuration curConfig, android.content.res.Configuration overrideConfig, android.content.res.CompatibilityInfo compatInfo, java.lang.String referrer, com.android.internal.app.IVoiceInteractor voiceInteractor, int procState, android.os.Bundle state, android.os.PersistableBundle persistentState, java.util.List<android.app.ResultInfo> pendingResults, java.util.List<com.android.internal.content.ReferrerIntent> pendingNewIntents, boolean notResumed, boolean isForward, android.app.ProfilerInfo profilerInfo) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        intent.writeToParcel(data, 0);
        data.writeStrongBinder(token);
        data.writeInt(ident);
        info.writeToParcel(data, 0);
        curConfig.writeToParcel(data, 0);
        if (overrideConfig != null) {
            data.writeInt(1);
            overrideConfig.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        compatInfo.writeToParcel(data, 0);
        data.writeString(referrer);
        data.writeStrongBinder(voiceInteractor != null ? voiceInteractor.asBinder() : null);
        data.writeInt(procState);
        data.writeBundle(state);
        data.writePersistableBundle(persistentState);
        data.writeTypedList(pendingResults);
        data.writeTypedList(pendingNewIntents);
        data.writeInt(notResumed ? 1 : 0);
        data.writeInt(isForward ? 1 : 0);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_LAUNCH_ACTIVITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleRelaunchActivity(android.os.IBinder token, java.util.List<android.app.ResultInfo> pendingResults, java.util.List<com.android.internal.content.ReferrerIntent> pendingNewIntents, int configChanges, boolean notResumed, android.content.res.Configuration config, android.content.res.Configuration overrideConfig, boolean preserveWindow) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeTypedList(pendingResults);
        data.writeTypedList(pendingNewIntents);
        data.writeInt(configChanges);
        data.writeInt(notResumed ? 1 : 0);
        config.writeToParcel(data, 0);
        if (overrideConfig != null) {
            data.writeInt(1);
            overrideConfig.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(preserveWindow ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_RELAUNCH_ACTIVITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void scheduleNewIntent(java.util.List<com.android.internal.content.ReferrerIntent> intents, android.os.IBinder token, boolean andPause) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeTypedList(intents);
        data.writeStrongBinder(token);
        data.writeInt(andPause ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_NEW_INTENT_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleDestroyActivity(android.os.IBinder token, boolean finishing, int configChanges) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(finishing ? 1 : 0);
        data.writeInt(configChanges);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_FINISH_ACTIVITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleReceiver(android.content.Intent intent, android.content.pm.ActivityInfo info, android.content.res.CompatibilityInfo compatInfo, int resultCode, java.lang.String resultData, android.os.Bundle map, boolean sync, int sendingUser, int processState) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        intent.writeToParcel(data, 0);
        info.writeToParcel(data, 0);
        compatInfo.writeToParcel(data, 0);
        data.writeInt(resultCode);
        data.writeString(resultData);
        data.writeBundle(map);
        data.writeInt(sync ? 1 : 0);
        data.writeInt(sendingUser);
        data.writeInt(processState);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_RECEIVER_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleCreateBackupAgent(android.content.pm.ApplicationInfo app, android.content.res.CompatibilityInfo compatInfo, int backupMode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        app.writeToParcel(data, 0);
        compatInfo.writeToParcel(data, 0);
        data.writeInt(backupMode);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_CREATE_BACKUP_AGENT_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleDestroyBackupAgent(android.content.pm.ApplicationInfo app, android.content.res.CompatibilityInfo compatInfo) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        app.writeToParcel(data, 0);
        compatInfo.writeToParcel(data, 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_DESTROY_BACKUP_AGENT_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleCreateService(android.os.IBinder token, android.content.pm.ServiceInfo info, android.content.res.CompatibilityInfo compatInfo, int processState) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        info.writeToParcel(data, 0);
        compatInfo.writeToParcel(data, 0);
        data.writeInt(processState);
        try {
            mRemote.transact(android.app.IApplicationThread.SCHEDULE_CREATE_SERVICE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        } catch (android.os.TransactionTooLargeException e) {
            android.util.Log.e("CREATE_SERVICE", "Binder failure starting service; service=" + info);
            throw e;
        }
        data.recycle();
    }

    public final void scheduleBindService(android.os.IBinder token, android.content.Intent intent, boolean rebind, int processState) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        intent.writeToParcel(data, 0);
        data.writeInt(rebind ? 1 : 0);
        data.writeInt(processState);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_BIND_SERVICE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleUnbindService(android.os.IBinder token, android.content.Intent intent) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        intent.writeToParcel(data, 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_UNBIND_SERVICE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleServiceArgs(android.os.IBinder token, boolean taskRemoved, int startId, int flags, android.content.Intent args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(taskRemoved ? 1 : 0);
        data.writeInt(startId);
        data.writeInt(flags);
        if (args != null) {
            data.writeInt(1);
            args.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_SERVICE_ARGS_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleStopService(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_STOP_SERVICE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public final void bindApplication(java.lang.String packageName, android.content.pm.ApplicationInfo info, java.util.List<android.content.pm.ProviderInfo> providers, android.content.ComponentName testName, android.app.ProfilerInfo profilerInfo, android.os.Bundle testArgs, android.app.IInstrumentationWatcher testWatcher, android.app.IUiAutomationConnection uiAutomationConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean restrictedBackupMode, boolean persistent, android.content.res.Configuration config, android.content.res.CompatibilityInfo compatInfo, java.util.Map<java.lang.String, android.os.IBinder> services, android.os.Bundle coreSettings) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeString(packageName);
        info.writeToParcel(data, 0);
        data.writeTypedList(providers);
        if (testName == null) {
            data.writeInt(0);
        } else {
            data.writeInt(1);
            testName.writeToParcel(data, 0);
        }
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        data.writeBundle(testArgs);
        data.writeStrongInterface(testWatcher);
        data.writeStrongInterface(uiAutomationConnection);
        data.writeInt(debugMode);
        data.writeInt(enableBinderTracking ? 1 : 0);
        data.writeInt(trackAllocation ? 1 : 0);
        data.writeInt(restrictedBackupMode ? 1 : 0);
        data.writeInt(persistent ? 1 : 0);
        config.writeToParcel(data, 0);
        compatInfo.writeToParcel(data, 0);
        data.writeMap(services);
        data.writeBundle(coreSettings);
        mRemote.transact(android.app.IApplicationThread.BIND_APPLICATION_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleExit() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_EXIT_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleSuicide() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_SUICIDE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleConfigurationChanged(android.content.res.Configuration config) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        config.writeToParcel(data, 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_CONFIGURATION_CHANGED_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public final void scheduleLocalVoiceInteractionStarted(android.os.IBinder token, com.android.internal.app.IVoiceInteractor voiceInteractor) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeStrongBinder(voiceInteractor != null ? voiceInteractor.asBinder() : null);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_LOCAL_VOICE_INTERACTION_STARTED_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void updateTimeZone() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        mRemote.transact(android.app.IApplicationThread.UPDATE_TIME_ZONE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void clearDnsCache() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        mRemote.transact(android.app.IApplicationThread.CLEAR_DNS_CACHE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void setHttpProxy(java.lang.String proxy, java.lang.String port, java.lang.String exclList, android.net.Uri pacFileUrl) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeString(proxy);
        data.writeString(port);
        data.writeString(exclList);
        pacFileUrl.writeToParcel(data, 0);
        mRemote.transact(android.app.IApplicationThread.SET_HTTP_PROXY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void processInBackground() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        mRemote.transact(android.app.IApplicationThread.PROCESS_IN_BACKGROUND_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void dumpService(java.io.FileDescriptor fd, android.os.IBinder token, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        data.writeStrongBinder(token);
        data.writeStringArray(args);
        mRemote.transact(android.app.IApplicationThread.DUMP_SERVICE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void dumpProvider(java.io.FileDescriptor fd, android.os.IBinder token, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        data.writeStrongBinder(token);
        data.writeStringArray(args);
        mRemote.transact(android.app.IApplicationThread.DUMP_PROVIDER_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void scheduleRegisteredReceiver(android.content.IIntentReceiver receiver, android.content.Intent intent, int resultCode, java.lang.String dataStr, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(receiver.asBinder());
        intent.writeToParcel(data, 0);
        data.writeInt(resultCode);
        data.writeString(dataStr);
        data.writeBundle(extras);
        data.writeInt(ordered ? 1 : 0);
        data.writeInt(sticky ? 1 : 0);
        data.writeInt(sendingUser);
        data.writeInt(processState);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_REGISTERED_RECEIVER_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public final void scheduleLowMemory() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_LOW_MEMORY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public final void scheduleActivityConfigurationChanged(android.os.IBinder token, android.content.res.Configuration overrideConfig, boolean reportToActivity) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        if (overrideConfig != null) {
            data.writeInt(1);
            overrideConfig.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(reportToActivity ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_ACTIVITY_CONFIGURATION_CHANGED_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void profilerControl(boolean start, android.app.ProfilerInfo profilerInfo, int profileType) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeInt(start ? 1 : 0);
        data.writeInt(profileType);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IApplicationThread.PROFILER_CONTROL_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void setSchedulingGroup(int group) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeInt(group);
        mRemote.transact(android.app.IApplicationThread.SET_SCHEDULING_GROUP_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void dispatchPackageBroadcast(int cmd, java.lang.String[] packages) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeInt(cmd);
        data.writeStringArray(packages);
        mRemote.transact(android.app.IApplicationThread.DISPATCH_PACKAGE_BROADCAST_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void scheduleCrash(java.lang.String msg) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeString(msg);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_CRASH_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void dumpHeap(boolean managed, java.lang.String path, android.os.ParcelFileDescriptor fd) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeInt(managed ? 1 : 0);
        data.writeString(path);
        if (fd != null) {
            data.writeInt(1);
            fd.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IApplicationThread.DUMP_HEAP_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void dumpActivity(java.io.FileDescriptor fd, android.os.IBinder token, java.lang.String prefix, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        data.writeStrongBinder(token);
        data.writeString(prefix);
        data.writeStringArray(args);
        mRemote.transact(android.app.IApplicationThread.DUMP_ACTIVITY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void setCoreSettings(android.os.Bundle coreSettings) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeBundle(coreSettings);
        mRemote.transact(android.app.IApplicationThread.SET_CORE_SETTINGS_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
    }

    public void updatePackageCompatibilityInfo(java.lang.String pkg, android.content.res.CompatibilityInfo info) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeString(pkg);
        info.writeToParcel(data, 0);
        mRemote.transact(android.app.IApplicationThread.UPDATE_PACKAGE_COMPATIBILITY_INFO_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
    }

    public void scheduleTrimMemory(int level) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeInt(level);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_TRIM_MEMORY_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void dumpMemInfo(java.io.FileDescriptor fd, android.os.Debug.MemoryInfo mem, boolean checkin, boolean dumpInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        mem.writeToParcel(data, 0);
        data.writeInt(checkin ? 1 : 0);
        data.writeInt(dumpInfo ? 1 : 0);
        data.writeInt(dumpDalvik ? 1 : 0);
        data.writeInt(dumpSummaryOnly ? 1 : 0);
        data.writeInt(dumpUnreachable ? 1 : 0);
        data.writeStringArray(args);
        mRemote.transact(android.app.IApplicationThread.DUMP_MEM_INFO_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void dumpGfxInfo(java.io.FileDescriptor fd, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        data.writeStringArray(args);
        mRemote.transact(android.app.IApplicationThread.DUMP_GFX_INFO_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    public void dumpDbInfo(java.io.FileDescriptor fd, java.lang.String[] args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        data.writeStringArray(args);
        mRemote.transact(android.app.IApplicationThread.DUMP_DB_INFO_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void unstableProviderDied(android.os.IBinder provider) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(provider);
        mRemote.transact(android.app.IApplicationThread.UNSTABLE_PROVIDER_DIED_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void requestAssistContextExtras(android.os.IBinder activityToken, android.os.IBinder requestToken, int requestType, int sessionId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(activityToken);
        data.writeStrongBinder(requestToken);
        data.writeInt(requestType);
        data.writeInt(sessionId);
        mRemote.transact(android.app.IApplicationThread.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void scheduleTranslucentConversionComplete(android.os.IBinder token, boolean timeout) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(timeout ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_TRANSLUCENT_CONVERSION_COMPLETE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void scheduleOnNewActivityOptions(android.os.IBinder token, android.app.ActivityOptions options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeBundle(options == null ? null : options.toBundle());
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_ON_NEW_ACTIVITY_OPTIONS_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void setProcessState(int state) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeInt(state);
        mRemote.transact(android.app.IApplicationThread.SET_PROCESS_STATE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void scheduleInstallProvider(android.content.pm.ProviderInfo provider) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        provider.writeToParcel(data, 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_INSTALL_PROVIDER_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void updateTimePrefs(boolean is24Hour) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeByte(is24Hour ? ((byte) (1)) : ((byte) (0)));
        mRemote.transact(android.app.IApplicationThread.UPDATE_TIME_PREFS_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void scheduleCancelVisibleBehind(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IApplicationThread.CANCEL_VISIBLE_BEHIND_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void scheduleBackgroundVisibleBehindChanged(android.os.IBinder token, boolean enabled) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(enabled ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.BACKGROUND_VISIBLE_BEHIND_CHANGED_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void scheduleEnterAnimationComplete(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IApplicationThread.ENTER_ANIMATION_COMPLETE_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void notifyCleartextNetwork(byte[] firstPacket) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeByteArray(firstPacket);
        mRemote.transact(android.app.IApplicationThread.NOTIFY_CLEARTEXT_NETWORK_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void startBinderTracking() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        mRemote.transact(android.app.IApplicationThread.START_BINDER_TRACKING_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public void stopBinderTrackingAndDump(java.io.FileDescriptor fd) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeFileDescriptor(fd);
        mRemote.transact(android.app.IApplicationThread.STOP_BINDER_TRACKING_AND_DUMP_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public final void scheduleMultiWindowModeChanged(android.os.IBinder token, boolean isInMultiWindowMode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(isInMultiWindowMode ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_MULTI_WINDOW_CHANGED_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }

    @java.lang.Override
    public final void schedulePictureInPictureModeChanged(android.os.IBinder token, boolean isInPipMode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IApplicationThread.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(isInPipMode ? 1 : 0);
        mRemote.transact(android.app.IApplicationThread.SCHEDULE_PICTURE_IN_PICTURE_CHANGED_TRANSACTION, data, null, android.os.IBinder.FLAG_ONEWAY);
        data.recycle();
    }
}

