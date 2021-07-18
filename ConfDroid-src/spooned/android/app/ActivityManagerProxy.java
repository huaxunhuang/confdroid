package android.app;


class ActivityManagerProxy implements android.app.IActivityManager {
    public ActivityManagerProxy(android.os.IBinder remote) {
        mRemote = remote;
    }

    public android.os.IBinder asBinder() {
        return mRemote;
    }

    public int startActivity(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(callingPackage);
        intent.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(resultTo);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        data.writeInt(startFlags);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.START_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public int startActivityAsUser(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(callingPackage);
        intent.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(resultTo);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        data.writeInt(startFlags);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_ACTIVITY_AS_USER_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public int startActivityAsCaller(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, boolean ignoreTargetSecurity, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(callingPackage);
        intent.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(resultTo);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        data.writeInt(startFlags);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(ignoreTargetSecurity ? 1 : 0);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_ACTIVITY_AS_CALLER_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public android.app.IActivityManager.WaitResult startActivityAndWait(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(callingPackage);
        intent.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(resultTo);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        data.writeInt(startFlags);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_ACTIVITY_AND_WAIT_TRANSACTION, data, reply, 0);
        reply.readException();
        android.app.IActivityManager.WaitResult result = android.app.IActivityManager.WaitResult.CREATOR.createFromParcel(reply);
        reply.recycle();
        data.recycle();
        return result;
    }

    public int startActivityWithConfig(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent intent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int startFlags, android.content.res.Configuration config, android.os.Bundle options, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(callingPackage);
        intent.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(resultTo);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        data.writeInt(startFlags);
        config.writeToParcel(data, 0);
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public int startActivityIntentSender(android.app.IApplicationThread caller, android.content.IntentSender intent, android.content.Intent fillInIntent, java.lang.String resolvedType, android.os.IBinder resultTo, java.lang.String resultWho, int requestCode, int flagsMask, int flagsValues, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        intent.writeToParcel(data, 0);
        if (fillInIntent != null) {
            data.writeInt(1);
            fillInIntent.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeString(resolvedType);
        data.writeStrongBinder(resultTo);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        data.writeInt(flagsMask);
        data.writeInt(flagsValues);
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.START_ACTIVITY_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public int startVoiceActivity(java.lang.String callingPackage, int callingPid, int callingUid, android.content.Intent intent, java.lang.String resolvedType, android.service.voice.IVoiceInteractionSession session, com.android.internal.app.IVoiceInteractor interactor, int startFlags, android.app.ProfilerInfo profilerInfo, android.os.Bundle options, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(callingPackage);
        data.writeInt(callingPid);
        data.writeInt(callingUid);
        intent.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(session.asBinder());
        data.writeStrongBinder(interactor.asBinder());
        data.writeInt(startFlags);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_VOICE_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public void startLocalVoiceInteraction(android.os.IBinder callingActivity, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(callingActivity);
        data.writeBundle(options);
        mRemote.transact(android.app.IActivityManager.START_LOCAL_VOICE_INTERACTION_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public void stopLocalVoiceInteraction(android.os.IBinder callingActivity) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(callingActivity);
        mRemote.transact(android.app.IActivityManager.STOP_LOCAL_VOICE_INTERACTION_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public boolean supportsLocalVoiceInteraction() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.SUPPORTS_LOCAL_VOICE_INTERACTION_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result != 0;
    }

    public boolean startNextMatchingActivity(android.os.IBinder callingActivity, android.content.Intent intent, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(callingActivity);
        intent.writeToParcel(data, 0);
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.START_NEXT_MATCHING_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result != 0;
    }

    public int startActivityFromRecents(int taskId, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        if (options == null) {
            data.writeInt(0);
        } else {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        }
        mRemote.transact(android.app.IActivityManager.START_ACTIVITY_FROM_RECENTS_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public boolean finishActivity(android.os.IBinder token, int resultCode, android.content.Intent resultData, int finishTask) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(resultCode);
        if (resultData != null) {
            data.writeInt(1);
            resultData.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(finishTask);
        mRemote.transact(android.app.IActivityManager.FINISH_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void finishSubActivity(android.os.IBinder token, java.lang.String resultWho, int requestCode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        mRemote.transact(android.app.IActivityManager.FINISH_SUB_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean finishActivityAffinity(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.FINISH_ACTIVITY_AFFINITY_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void finishVoiceTask(android.service.voice.IVoiceInteractionSession session) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(session.asBinder());
        mRemote.transact(android.app.IActivityManager.FINISH_VOICE_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean releaseActivityInstance(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.RELEASE_ACTIVITY_INSTANCE_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void releaseSomeActivities(android.app.IApplicationThread app) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(app.asBinder());
        mRemote.transact(android.app.IActivityManager.RELEASE_SOME_ACTIVITIES_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean willActivityBeVisible(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.WILL_ACTIVITY_BE_VISIBLE_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.content.Intent registerReceiver(android.app.IApplicationThread caller, java.lang.String packageName, android.content.IIntentReceiver receiver, android.content.IntentFilter filter, java.lang.String perm, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(packageName);
        data.writeStrongBinder(receiver != null ? receiver.asBinder() : null);
        filter.writeToParcel(data, 0);
        data.writeString(perm);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.REGISTER_RECEIVER_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.Intent intent = null;
        int haveIntent = reply.readInt();
        if (haveIntent != 0) {
            intent = android.content.Intent.CREATOR.createFromParcel(reply);
        }
        reply.recycle();
        data.recycle();
        return intent;
    }

    public void unregisterReceiver(android.content.IIntentReceiver receiver) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(receiver.asBinder());
        mRemote.transact(android.app.IActivityManager.UNREGISTER_RECEIVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int broadcastIntent(android.app.IApplicationThread caller, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver resultTo, int resultCode, java.lang.String resultData, android.os.Bundle map, java.lang.String[] requiredPermissions, int appOp, android.os.Bundle options, boolean serialized, boolean sticky, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        intent.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(resultTo != null ? resultTo.asBinder() : null);
        data.writeInt(resultCode);
        data.writeString(resultData);
        data.writeBundle(map);
        data.writeStringArray(requiredPermissions);
        data.writeInt(appOp);
        data.writeBundle(options);
        data.writeInt(serialized ? 1 : 0);
        data.writeInt(sticky ? 1 : 0);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.BROADCAST_INTENT_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        reply.recycle();
        data.recycle();
        return res;
    }

    public void unbroadcastIntent(android.app.IApplicationThread caller, android.content.Intent intent, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        intent.writeToParcel(data, 0);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.UNBROADCAST_INTENT_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void finishReceiver(android.os.IBinder who, int resultCode, java.lang.String resultData, android.os.Bundle map, boolean abortBroadcast, int flags) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(who);
        data.writeInt(resultCode);
        data.writeString(resultData);
        data.writeBundle(map);
        data.writeInt(abortBroadcast ? 1 : 0);
        data.writeInt(flags);
        mRemote.transact(android.app.IActivityManager.FINISH_RECEIVER_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void attachApplication(android.app.IApplicationThread app) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(app.asBinder());
        mRemote.transact(android.app.IActivityManager.ATTACH_APPLICATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void activityIdle(android.os.IBinder token, android.content.res.Configuration config, boolean stopProfiling) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        if (config != null) {
            data.writeInt(1);
            config.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(stopProfiling ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.ACTIVITY_IDLE_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void activityResumed(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.ACTIVITY_RESUMED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void activityPaused(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.ACTIVITY_PAUSED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void activityStopped(android.os.IBinder token, android.os.Bundle state, android.os.PersistableBundle persistentState, java.lang.CharSequence description) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeBundle(state);
        data.writePersistableBundle(persistentState);
        android.text.TextUtils.writeToParcel(description, data, 0);
        mRemote.transact(android.app.IActivityManager.ACTIVITY_STOPPED_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void activitySlept(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.ACTIVITY_SLEPT_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void activityDestroyed(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.ACTIVITY_DESTROYED_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void activityRelaunched(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.ACTIVITY_RELAUNCHED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public java.lang.String getCallingPackage(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_CALLING_PACKAGE_TRANSACTION, data, reply, 0);
        reply.readException();
        java.lang.String res = reply.readString();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.content.ComponentName getCallingActivity(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_CALLING_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.ComponentName res = android.content.ComponentName.readFromParcel(reply);
        data.recycle();
        reply.recycle();
        return res;
    }

    public java.util.List<android.app.IAppTask> getAppTasks(java.lang.String callingPackage) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(callingPackage);
        mRemote.transact(android.app.IActivityManager.GET_APP_TASKS_TRANSACTION, data, reply, 0);
        reply.readException();
        java.util.ArrayList<android.app.IAppTask> list = null;
        int N = reply.readInt();
        if (N >= 0) {
            list = new java.util.ArrayList();
            while (N > 0) {
                android.app.IAppTask task = IAppTask.Stub.asInterface(reply.readStrongBinder());
                list.add(task);
                N--;
            } 
        }
        data.recycle();
        reply.recycle();
        return list;
    }

    public int addAppTask(android.os.IBinder activityToken, android.content.Intent intent, android.app.ActivityManager.TaskDescription description, android.graphics.Bitmap thumbnail) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(activityToken);
        intent.writeToParcel(data, 0);
        description.writeToParcel(data, 0);
        thumbnail.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.ADD_APP_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.graphics.Point getAppTaskThumbnailSize() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_APP_TASK_THUMBNAIL_SIZE_TRANSACTION, data, reply, 0);
        reply.readException();
        android.graphics.Point size = android.graphics.Point.CREATOR.createFromParcel(reply);
        data.recycle();
        reply.recycle();
        return size;
    }

    public java.util.List<android.app.ActivityManager.RunningTaskInfo> getTasks(int maxNum, int flags) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(maxNum);
        data.writeInt(flags);
        mRemote.transact(android.app.IActivityManager.GET_TASKS_TRANSACTION, data, reply, 0);
        reply.readException();
        java.util.ArrayList<android.app.ActivityManager.RunningTaskInfo> list = null;
        int N = reply.readInt();
        if (N >= 0) {
            list = new java.util.ArrayList<>();
            while (N > 0) {
                android.app.ActivityManager.RunningTaskInfo info = android.app.ActivityManager.RunningTaskInfo.CREATOR.createFromParcel(reply);
                list.add(info);
                N--;
            } 
        }
        data.recycle();
        reply.recycle();
        return list;
    }

    public android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> getRecentTasks(int maxNum, int flags, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(maxNum);
        data.writeInt(flags);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.GET_RECENT_TASKS_TRANSACTION, data, reply, 0);
        reply.readException();
        final android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> list = android.content.pm.ParceledListSlice.CREATOR.createFromParcel(reply);
        data.recycle();
        reply.recycle();
        return list;
    }

    public android.app.ActivityManager.TaskThumbnail getTaskThumbnail(int id) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(id);
        mRemote.transact(android.app.IActivityManager.GET_TASK_THUMBNAIL_TRANSACTION, data, reply, 0);
        reply.readException();
        android.app.ActivityManager.TaskThumbnail taskThumbnail = null;
        if (reply.readInt() != 0) {
            taskThumbnail = android.app.ActivityManager.TaskThumbnail.CREATOR.createFromParcel(reply);
        }
        data.recycle();
        reply.recycle();
        return taskThumbnail;
    }

    public java.util.List<android.app.ActivityManager.RunningServiceInfo> getServices(int maxNum, int flags) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(maxNum);
        data.writeInt(flags);
        mRemote.transact(android.app.IActivityManager.GET_SERVICES_TRANSACTION, data, reply, 0);
        reply.readException();
        java.util.ArrayList<android.app.ActivityManager.RunningServiceInfo> list = null;
        int N = reply.readInt();
        if (N >= 0) {
            list = new java.util.ArrayList<>();
            while (N > 0) {
                android.app.ActivityManager.RunningServiceInfo info = android.app.ActivityManager.RunningServiceInfo.CREATOR.createFromParcel(reply);
                list.add(info);
                N--;
            } 
        }
        data.recycle();
        reply.recycle();
        return list;
    }

    public java.util.List<android.app.ActivityManager.ProcessErrorStateInfo> getProcessesInErrorState() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_PROCESSES_IN_ERROR_STATE_TRANSACTION, data, reply, 0);
        reply.readException();
        java.util.ArrayList<android.app.ActivityManager.ProcessErrorStateInfo> list = reply.createTypedArrayList(android.app.ActivityManager.ProcessErrorStateInfo.CREATOR);
        data.recycle();
        reply.recycle();
        return list;
    }

    public java.util.List<android.app.ActivityManager.RunningAppProcessInfo> getRunningAppProcesses() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_RUNNING_APP_PROCESSES_TRANSACTION, data, reply, 0);
        reply.readException();
        java.util.ArrayList<android.app.ActivityManager.RunningAppProcessInfo> list = reply.createTypedArrayList(android.app.ActivityManager.RunningAppProcessInfo.CREATOR);
        data.recycle();
        reply.recycle();
        return list;
    }

    public java.util.List<android.content.pm.ApplicationInfo> getRunningExternalApplications() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_RUNNING_EXTERNAL_APPLICATIONS_TRANSACTION, data, reply, 0);
        reply.readException();
        java.util.ArrayList<android.content.pm.ApplicationInfo> list = reply.createTypedArrayList(android.content.pm.ApplicationInfo.CREATOR);
        data.recycle();
        reply.recycle();
        return list;
    }

    public void moveTaskToFront(int task, int flags, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(task);
        data.writeInt(flags);
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.MOVE_TASK_TO_FRONT_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean moveActivityTaskToBack(android.os.IBinder token, boolean nonRoot) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(nonRoot ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.MOVE_ACTIVITY_TASK_TO_BACK_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void moveTaskBackwards(int task) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(task);
        mRemote.transact(android.app.IActivityManager.MOVE_TASK_BACKWARDS_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void moveTaskToStack(int taskId, int stackId, boolean toTop) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        data.writeInt(stackId);
        data.writeInt(toTop ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.MOVE_TASK_TO_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public boolean moveTaskToDockedStack(int taskId, int createMode, boolean toTop, boolean animate, android.graphics.Rect initialBounds, boolean moveHomeStackFront) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        data.writeInt(createMode);
        data.writeInt(toTop ? 1 : 0);
        data.writeInt(animate ? 1 : 0);
        if (initialBounds != null) {
            data.writeInt(1);
            initialBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(moveHomeStackFront ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.MOVE_TASK_TO_DOCKED_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() > 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    @java.lang.Override
    public boolean moveTopActivityToPinnedStack(int stackId, android.graphics.Rect r) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(stackId);
        r.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.MOVE_TOP_ACTIVITY_TO_PINNED_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        final boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    @java.lang.Override
    public void resizeStack(int stackId, android.graphics.Rect r, boolean allowResizeInDockedMode, boolean preserveWindows, boolean animate, int animationDuration) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(stackId);
        if (r != null) {
            data.writeInt(1);
            r.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(allowResizeInDockedMode ? 1 : 0);
        data.writeInt(preserveWindows ? 1 : 0);
        data.writeInt(animate ? 1 : 0);
        data.writeInt(animationDuration);
        mRemote.transact(android.app.IActivityManager.RESIZE_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void swapDockedAndFullscreenStack() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.SWAP_DOCKED_AND_FULLSCREEN_STACK, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void resizeDockedStack(android.graphics.Rect dockedBounds, android.graphics.Rect tempDockedTaskBounds, android.graphics.Rect tempDockedTaskInsetBounds, android.graphics.Rect tempOtherTaskBounds, android.graphics.Rect tempOtherTaskInsetBounds) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        if (dockedBounds != null) {
            data.writeInt(1);
            dockedBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        if (tempDockedTaskBounds != null) {
            data.writeInt(1);
            tempDockedTaskBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        if (tempDockedTaskInsetBounds != null) {
            data.writeInt(1);
            tempDockedTaskInsetBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        if (tempOtherTaskBounds != null) {
            data.writeInt(1);
            tempOtherTaskBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        if (tempOtherTaskInsetBounds != null) {
            data.writeInt(1);
            tempOtherTaskInsetBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.RESIZE_DOCKED_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void resizePinnedStack(android.graphics.Rect pinnedBounds, android.graphics.Rect tempPinnedTaskBounds) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        if (pinnedBounds != null) {
            data.writeInt(1);
            pinnedBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        if (tempPinnedTaskBounds != null) {
            data.writeInt(1);
            tempPinnedTaskBounds.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.RESIZE_PINNED_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void positionTaskInStack(int taskId, int stackId, int position) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        data.writeInt(stackId);
        data.writeInt(position);
        mRemote.transact(android.app.IActivityManager.POSITION_TASK_IN_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public java.util.List<android.app.ActivityManager.StackInfo> getAllStackInfos() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_ALL_STACK_INFOS_TRANSACTION, data, reply, 0);
        reply.readException();
        java.util.ArrayList<android.app.ActivityManager.StackInfo> list = reply.createTypedArrayList(android.app.ActivityManager.StackInfo.CREATOR);
        data.recycle();
        reply.recycle();
        return list;
    }

    @java.lang.Override
    public android.app.ActivityManager.StackInfo getStackInfo(int stackId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(stackId);
        mRemote.transact(android.app.IActivityManager.GET_STACK_INFO_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        android.app.ActivityManager.StackInfo info = null;
        if (res != 0) {
            info = android.app.ActivityManager.StackInfo.CREATOR.createFromParcel(reply);
        }
        data.recycle();
        reply.recycle();
        return info;
    }

    @java.lang.Override
    public boolean isInHomeStack(int taskId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        mRemote.transact(android.app.IActivityManager.IS_IN_HOME_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean isInHomeStack = reply.readInt() > 0;
        data.recycle();
        reply.recycle();
        return isInHomeStack;
    }

    @java.lang.Override
    public void setFocusedStack(int stackId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(stackId);
        mRemote.transact(android.app.IActivityManager.SET_FOCUSED_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public int getFocusedStackId() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION, data, reply, 0);
        reply.readException();
        int focusedStackId = reply.readInt();
        data.recycle();
        reply.recycle();
        return focusedStackId;
    }

    @java.lang.Override
    public void setFocusedTask(int taskId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        mRemote.transact(android.app.IActivityManager.SET_FOCUSED_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void registerTaskStackListener(android.app.ITaskStackListener listener) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(listener.asBinder());
        mRemote.transact(android.app.IActivityManager.REGISTER_TASK_STACK_LISTENER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int getTaskForActivity(android.os.IBinder token, boolean onlyRoot) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(onlyRoot ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.GET_TASK_FOR_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.app.IActivityManager.ContentProviderHolder getContentProvider(android.app.IApplicationThread caller, java.lang.String name, int userId, boolean stable) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(name);
        data.writeInt(userId);
        data.writeInt(stable ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.GET_CONTENT_PROVIDER_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        android.app.IActivityManager.ContentProviderHolder cph = null;
        if (res != 0) {
            cph = android.app.IActivityManager.ContentProviderHolder.CREATOR.createFromParcel(reply);
        }
        data.recycle();
        reply.recycle();
        return cph;
    }

    public android.app.IActivityManager.ContentProviderHolder getContentProviderExternal(java.lang.String name, int userId, android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(name);
        data.writeInt(userId);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_CONTENT_PROVIDER_EXTERNAL_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        android.app.IActivityManager.ContentProviderHolder cph = null;
        if (res != 0) {
            cph = android.app.IActivityManager.ContentProviderHolder.CREATOR.createFromParcel(reply);
        }
        data.recycle();
        reply.recycle();
        return cph;
    }

    public void publishContentProviders(android.app.IApplicationThread caller, java.util.List<android.app.IActivityManager.ContentProviderHolder> providers) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeTypedList(providers);
        mRemote.transact(android.app.IActivityManager.PUBLISH_CONTENT_PROVIDERS_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean refContentProvider(android.os.IBinder connection, int stable, int unstable) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(connection);
        data.writeInt(stable);
        data.writeInt(unstable);
        mRemote.transact(android.app.IActivityManager.REF_CONTENT_PROVIDER_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void unstableProviderDied(android.os.IBinder connection) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(connection);
        mRemote.transact(android.app.IActivityManager.UNSTABLE_PROVIDER_DIED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void appNotRespondingViaProvider(android.os.IBinder connection) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(connection);
        mRemote.transact(android.app.IActivityManager.APP_NOT_RESPONDING_VIA_PROVIDER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void removeContentProvider(android.os.IBinder connection, boolean stable) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(connection);
        data.writeInt(stable ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.REMOVE_CONTENT_PROVIDER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void removeContentProviderExternal(java.lang.String name, android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(name);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.REMOVE_CONTENT_PROVIDER_EXTERNAL_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public android.app.PendingIntent getRunningServiceControlPanel(android.content.ComponentName service) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        service.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.GET_RUNNING_SERVICE_CONTROL_PANEL_TRANSACTION, data, reply, 0);
        reply.readException();
        android.app.PendingIntent res = android.app.PendingIntent.readPendingIntentOrNullFromParcel(reply);
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.content.ComponentName startService(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, java.lang.String callingPackage, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        service.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeString(callingPackage);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_SERVICE_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.ComponentName res = android.content.ComponentName.readFromParcel(reply);
        data.recycle();
        reply.recycle();
        return res;
    }

    public int stopService(android.app.IApplicationThread caller, android.content.Intent service, java.lang.String resolvedType, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        service.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.STOP_SERVICE_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        reply.recycle();
        data.recycle();
        return res;
    }

    public boolean stopServiceToken(android.content.ComponentName className, android.os.IBinder token, int startId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        android.content.ComponentName.writeToParcel(className, data);
        data.writeStrongBinder(token);
        data.writeInt(startId);
        mRemote.transact(android.app.IActivityManager.STOP_SERVICE_TOKEN_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void setServiceForeground(android.content.ComponentName className, android.os.IBinder token, int id, android.app.Notification notification, int flags) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        android.content.ComponentName.writeToParcel(className, data);
        data.writeStrongBinder(token);
        data.writeInt(id);
        if (notification != null) {
            data.writeInt(1);
            notification.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(flags);
        mRemote.transact(android.app.IActivityManager.SET_SERVICE_FOREGROUND_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int bindService(android.app.IApplicationThread caller, android.os.IBinder token, android.content.Intent service, java.lang.String resolvedType, android.app.IServiceConnection connection, int flags, java.lang.String callingPackage, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeStrongBinder(token);
        service.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeStrongBinder(connection.asBinder());
        data.writeInt(flags);
        data.writeString(callingPackage);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.BIND_SERVICE_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean unbindService(android.app.IServiceConnection connection) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(connection.asBinder());
        mRemote.transact(android.app.IActivityManager.UNBIND_SERVICE_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void publishService(android.os.IBinder token, android.content.Intent intent, android.os.IBinder service) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        intent.writeToParcel(data, 0);
        data.writeStrongBinder(service);
        mRemote.transact(android.app.IActivityManager.PUBLISH_SERVICE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void unbindFinished(android.os.IBinder token, android.content.Intent intent, boolean doRebind) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        intent.writeToParcel(data, 0);
        data.writeInt(doRebind ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.UNBIND_FINISHED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void serviceDoneExecuting(android.os.IBinder token, int type, int startId, int res) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(type);
        data.writeInt(startId);
        data.writeInt(res);
        mRemote.transact(android.app.IActivityManager.SERVICE_DONE_EXECUTING_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public android.os.IBinder peekService(android.content.Intent service, java.lang.String resolvedType, java.lang.String callingPackage) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        service.writeToParcel(data, 0);
        data.writeString(resolvedType);
        data.writeString(callingPackage);
        mRemote.transact(android.app.IActivityManager.PEEK_SERVICE_TRANSACTION, data, reply, 0);
        reply.readException();
        android.os.IBinder binder = reply.readStrongBinder();
        reply.recycle();
        data.recycle();
        return binder;
    }

    public boolean bindBackupAgent(java.lang.String packageName, int backupRestoreMode, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(backupRestoreMode);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_BACKUP_AGENT_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean success = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return success;
    }

    public void clearPendingBackup() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.CLEAR_PENDING_BACKUP_TRANSACTION, data, reply, 0);
        reply.recycle();
        data.recycle();
    }

    public void backupAgentCreated(java.lang.String packageName, android.os.IBinder agent) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeStrongBinder(agent);
        mRemote.transact(android.app.IActivityManager.BACKUP_AGENT_CREATED_TRANSACTION, data, reply, 0);
        reply.recycle();
        data.recycle();
    }

    public void unbindBackupAgent(android.content.pm.ApplicationInfo app) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        app.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.UNBIND_BACKUP_AGENT_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public boolean startInstrumentation(android.content.ComponentName className, java.lang.String profileFile, int flags, android.os.Bundle arguments, android.app.IInstrumentationWatcher watcher, android.app.IUiAutomationConnection connection, int userId, java.lang.String instructionSet) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        android.content.ComponentName.writeToParcel(className, data);
        data.writeString(profileFile);
        data.writeInt(flags);
        data.writeBundle(arguments);
        data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
        data.writeStrongBinder(connection != null ? connection.asBinder() : null);
        data.writeInt(userId);
        data.writeString(instructionSet);
        mRemote.transact(android.app.IActivityManager.START_INSTRUMENTATION_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return res;
    }

    public void finishInstrumentation(android.app.IApplicationThread target, int resultCode, android.os.Bundle results) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(target != null ? target.asBinder() : null);
        data.writeInt(resultCode);
        data.writeBundle(results);
        mRemote.transact(android.app.IActivityManager.FINISH_INSTRUMENTATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public android.content.res.Configuration getConfiguration() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_CONFIGURATION_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.res.Configuration res = android.content.res.Configuration.CREATOR.createFromParcel(reply);
        reply.recycle();
        data.recycle();
        return res;
    }

    public void updateConfiguration(android.content.res.Configuration values) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        values.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.UPDATE_CONFIGURATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setRequestedOrientation(android.os.IBinder token, int requestedOrientation) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(requestedOrientation);
        mRemote.transact(android.app.IActivityManager.SET_REQUESTED_ORIENTATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int getRequestedOrientation(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_REQUESTED_ORIENTATION_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.content.ComponentName getActivityClassForToken(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_ACTIVITY_CLASS_FOR_TOKEN_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.ComponentName res = android.content.ComponentName.readFromParcel(reply);
        data.recycle();
        reply.recycle();
        return res;
    }

    public java.lang.String getPackageForToken(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_PACKAGE_FOR_TOKEN_TRANSACTION, data, reply, 0);
        reply.readException();
        java.lang.String res = reply.readString();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.content.IIntentSender getIntentSender(int type, java.lang.String packageName, android.os.IBinder token, java.lang.String resultWho, int requestCode, android.content.Intent[] intents, java.lang.String[] resolvedTypes, int flags, android.os.Bundle options, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(type);
        data.writeString(packageName);
        data.writeStrongBinder(token);
        data.writeString(resultWho);
        data.writeInt(requestCode);
        if (intents != null) {
            data.writeInt(1);
            data.writeTypedArray(intents, 0);
            data.writeStringArray(resolvedTypes);
        } else {
            data.writeInt(0);
        }
        data.writeInt(flags);
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.GET_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.IIntentSender res = IIntentSender.Stub.asInterface(reply.readStrongBinder());
        data.recycle();
        reply.recycle();
        return res;
    }

    public void cancelIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        mRemote.transact(android.app.IActivityManager.CANCEL_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public java.lang.String getPackageForIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        mRemote.transact(android.app.IActivityManager.GET_PACKAGE_FOR_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        java.lang.String res = reply.readString();
        data.recycle();
        reply.recycle();
        return res;
    }

    public int getUidForIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        mRemote.transact(android.app.IActivityManager.GET_UID_FOR_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public int handleIncomingUser(int callingPid, int callingUid, int userId, boolean allowAll, boolean requireFull, java.lang.String name, java.lang.String callerPackage) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(callingPid);
        data.writeInt(callingUid);
        data.writeInt(userId);
        data.writeInt(allowAll ? 1 : 0);
        data.writeInt(requireFull ? 1 : 0);
        data.writeString(name);
        data.writeString(callerPackage);
        mRemote.transact(android.app.IActivityManager.HANDLE_INCOMING_USER_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public void setProcessLimit(int max) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(max);
        mRemote.transact(android.app.IActivityManager.SET_PROCESS_LIMIT_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int getProcessLimit() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_PROCESS_LIMIT_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public void setProcessForeground(android.os.IBinder token, int pid, boolean isForeground) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(pid);
        data.writeInt(isForeground ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_PROCESS_FOREGROUND_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int checkPermission(java.lang.String permission, int pid, int uid) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(permission);
        data.writeInt(pid);
        data.writeInt(uid);
        mRemote.transact(android.app.IActivityManager.CHECK_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public int checkPermissionWithToken(java.lang.String permission, int pid, int uid, android.os.IBinder callerToken) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(permission);
        data.writeInt(pid);
        data.writeInt(uid);
        data.writeStrongBinder(callerToken);
        mRemote.transact(android.app.IActivityManager.CHECK_PERMISSION_WITH_TOKEN_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean clearApplicationUserData(final java.lang.String packageName, final android.content.pm.IPackageDataObserver observer, final int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.CLEAR_APP_DATA_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public int checkUriPermission(android.net.Uri uri, int pid, int uid, int mode, int userId, android.os.IBinder callerToken) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        uri.writeToParcel(data, 0);
        data.writeInt(pid);
        data.writeInt(uid);
        data.writeInt(mode);
        data.writeInt(userId);
        data.writeStrongBinder(callerToken);
        mRemote.transact(android.app.IActivityManager.CHECK_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public void grantUriPermission(android.app.IApplicationThread caller, java.lang.String targetPkg, android.net.Uri uri, int mode, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller.asBinder());
        data.writeString(targetPkg);
        uri.writeToParcel(data, 0);
        data.writeInt(mode);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.GRANT_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void revokeUriPermission(android.app.IApplicationThread caller, android.net.Uri uri, int mode, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller.asBinder());
        uri.writeToParcel(data, 0);
        data.writeInt(mode);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.REVOKE_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void takePersistableUriPermission(android.net.Uri uri, int mode, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        uri.writeToParcel(data, 0);
        data.writeInt(mode);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.TAKE_PERSISTABLE_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void releasePersistableUriPermission(android.net.Uri uri, int mode, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        uri.writeToParcel(data, 0);
        data.writeInt(mode);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.RELEASE_PERSISTABLE_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public android.content.pm.ParceledListSlice<android.content.UriPermission> getPersistedUriPermissions(java.lang.String packageName, boolean incoming) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(incoming ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.GET_PERSISTED_URI_PERMISSIONS_TRANSACTION, data, reply, 0);
        reply.readException();
        @java.lang.SuppressWarnings("unchecked")
        final android.content.pm.ParceledListSlice<android.content.UriPermission> perms = android.content.pm.ParceledListSlice.CREATOR.createFromParcel(reply);
        data.recycle();
        reply.recycle();
        return perms;
    }

    @java.lang.Override
    public android.content.pm.ParceledListSlice<android.content.UriPermission> getGrantedUriPermissions(java.lang.String packageName, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.GET_GRANTED_URI_PERMISSIONS_TRANSACTION, data, reply, 0);
        reply.readException();
        @java.lang.SuppressWarnings("unchecked")
        final android.content.pm.ParceledListSlice<android.content.UriPermission> perms = android.content.pm.ParceledListSlice.CREATOR.createFromParcel(reply);
        data.recycle();
        reply.recycle();
        return perms;
    }

    @java.lang.Override
    public void clearGrantedUriPermissions(java.lang.String packageName, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.CLEAR_GRANTED_URI_PERMISSIONS_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void showWaitingForDebugger(android.app.IApplicationThread who, boolean waiting) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(who.asBinder());
        data.writeInt(waiting ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SHOW_WAITING_FOR_DEBUGGER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void getMemoryInfo(android.app.ActivityManager.MemoryInfo outInfo) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_MEMORY_INFO_TRANSACTION, data, reply, 0);
        reply.readException();
        outInfo.readFromParcel(reply);
        data.recycle();
        reply.recycle();
    }

    public void unhandledBack() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.UNHANDLED_BACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public android.os.ParcelFileDescriptor openContentUri(android.net.Uri uri) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.OPEN_CONTENT_URI_TRANSACTION, data, reply, 0);
        reply.readException();
        android.os.ParcelFileDescriptor pfd = null;
        if (reply.readInt() != 0) {
            pfd = android.os.ParcelFileDescriptor.CREATOR.createFromParcel(reply);
        }
        data.recycle();
        reply.recycle();
        return pfd;
    }

    public void setLockScreenShown(boolean showing, boolean occluded) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(showing ? 1 : 0);
        data.writeInt(occluded ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_LOCK_SCREEN_SHOWN_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setDebugApp(java.lang.String packageName, boolean waitForDebugger, boolean persistent) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(waitForDebugger ? 1 : 0);
        data.writeInt(persistent ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_DEBUG_APP_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setAlwaysFinish(boolean enabled) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(enabled ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_ALWAYS_FINISH_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setActivityController(android.app.IActivityController watcher, boolean imAMonkey) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(watcher != null ? watcher.asBinder() : null);
        data.writeInt(imAMonkey ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_ACTIVITY_CONTROLLER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void setLenientBackgroundCheck(boolean enabled) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(enabled ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_LENIENT_BACKGROUND_CHECK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int getMemoryTrimLevel() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_MEMORY_TRIM_LEVEL_TRANSACTION, data, reply, 0);
        reply.readException();
        int level = reply.readInt();
        data.recycle();
        reply.recycle();
        return level;
    }

    public void enterSafeMode() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.ENTER_SAFE_MODE_TRANSACTION, data, null, 0);
        data.recycle();
    }

    public void noteWakeupAlarm(android.content.IIntentSender sender, int sourceUid, java.lang.String sourcePkg, java.lang.String tag) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        data.writeInt(sourceUid);
        data.writeString(sourcePkg);
        data.writeString(tag);
        mRemote.transact(android.app.IActivityManager.NOTE_WAKEUP_ALARM_TRANSACTION, data, null, 0);
        data.recycle();
    }

    public void noteAlarmStart(android.content.IIntentSender sender, int sourceUid, java.lang.String tag) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        data.writeInt(sourceUid);
        data.writeString(tag);
        mRemote.transact(android.app.IActivityManager.NOTE_ALARM_START_TRANSACTION, data, null, 0);
        data.recycle();
    }

    public void noteAlarmFinish(android.content.IIntentSender sender, int sourceUid, java.lang.String tag) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        data.writeInt(sourceUid);
        data.writeString(tag);
        mRemote.transact(android.app.IActivityManager.NOTE_ALARM_FINISH_TRANSACTION, data, null, 0);
        data.recycle();
    }

    public boolean killPids(int[] pids, java.lang.String reason, boolean secure) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeIntArray(pids);
        data.writeString(reason);
        data.writeInt(secure ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.KILL_PIDS_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    @java.lang.Override
    public boolean killProcessesBelowForeground(java.lang.String reason) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(reason);
        mRemote.transact(android.app.IActivityManager.KILL_PROCESSES_BELOW_FOREGROUND_TRANSACTION, data, reply, 0);
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean testIsSystemReady() {
        /* this base class version is never called */
        return true;
    }

    public void handleApplicationCrash(android.os.IBinder app, android.app.ApplicationErrorReport.CrashInfo crashInfo) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(app);
        crashInfo.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.HANDLE_APPLICATION_CRASH_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public boolean handleApplicationWtf(android.os.IBinder app, java.lang.String tag, boolean system, android.app.ApplicationErrorReport.CrashInfo crashInfo) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(app);
        data.writeString(tag);
        data.writeInt(system ? 1 : 0);
        crashInfo.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.HANDLE_APPLICATION_WTF_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return res;
    }

    public void handleApplicationStrictModeViolation(android.os.IBinder app, int violationMask, android.os.StrictMode.ViolationInfo info) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(app);
        data.writeInt(violationMask);
        info.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.HANDLE_APPLICATION_STRICT_MODE_VIOLATION_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public void signalPersistentProcesses(int sig) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(sig);
        mRemote.transact(android.app.IActivityManager.SIGNAL_PERSISTENT_PROCESSES_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void killBackgroundProcesses(java.lang.String packageName, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.KILL_BACKGROUND_PROCESSES_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void killAllBackgroundProcesses() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.KILL_ALL_BACKGROUND_PROCESSES_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void killPackageDependents(java.lang.String packageName, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.KILL_PACKAGE_DEPENDENTS_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void forceStopPackage(java.lang.String packageName, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.FORCE_STOP_PACKAGE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void getMyMemoryState(android.app.ActivityManager.RunningAppProcessInfo outInfo) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_MY_MEMORY_STATE_TRANSACTION, data, reply, 0);
        reply.readException();
        outInfo.readFromParcel(reply);
        reply.recycle();
        data.recycle();
    }

    public android.content.pm.ConfigurationInfo getDeviceConfigurationInfo() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_DEVICE_CONFIGURATION_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.pm.ConfigurationInfo res = android.content.pm.ConfigurationInfo.CREATOR.createFromParcel(reply);
        reply.recycle();
        data.recycle();
        return res;
    }

    public boolean profileControl(java.lang.String process, int userId, boolean start, android.app.ProfilerInfo profilerInfo, int profileType) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(process);
        data.writeInt(userId);
        data.writeInt(start ? 1 : 0);
        data.writeInt(profileType);
        if (profilerInfo != null) {
            data.writeInt(1);
            profilerInfo.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.PROFILE_CONTROL_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return res;
    }

    public boolean shutdown(int timeout) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(timeout);
        mRemote.transact(android.app.IActivityManager.SHUTDOWN_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return res;
    }

    public void stopAppSwitches() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.STOP_APP_SWITCHES_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public void resumeAppSwitches() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.RESUME_APP_SWITCHES_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public void addPackageDependency(java.lang.String packageName) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        mRemote.transact(android.app.IActivityManager.ADD_PACKAGE_DEPENDENCY_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void killApplication(java.lang.String pkg, int appId, int userId, java.lang.String reason) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(pkg);
        data.writeInt(appId);
        data.writeInt(userId);
        data.writeString(reason);
        mRemote.transact(android.app.IActivityManager.KILL_APPLICATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void closeSystemDialogs(java.lang.String reason) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(reason);
        mRemote.transact(android.app.IActivityManager.CLOSE_SYSTEM_DIALOGS_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public android.os.Debug.MemoryInfo[] getProcessMemoryInfo(int[] pids) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeIntArray(pids);
        mRemote.transact(android.app.IActivityManager.GET_PROCESS_MEMORY_INFO_TRANSACTION, data, reply, 0);
        reply.readException();
        android.os.Debug.MemoryInfo[] res = reply.createTypedArray(android.os.Debug.MemoryInfo.CREATOR);
        data.recycle();
        reply.recycle();
        return res;
    }

    public void killApplicationProcess(java.lang.String processName, int uid) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(processName);
        data.writeInt(uid);
        mRemote.transact(android.app.IActivityManager.KILL_APPLICATION_PROCESS_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void overridePendingTransition(android.os.IBinder token, java.lang.String packageName, int enterAnim, int exitAnim) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeString(packageName);
        data.writeInt(enterAnim);
        data.writeInt(exitAnim);
        mRemote.transact(android.app.IActivityManager.OVERRIDE_PENDING_TRANSITION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean isUserAMonkey() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.IS_USER_A_MONKEY_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void setUserIsMonkey(boolean monkey) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(monkey ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_USER_IS_MONKEY_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void finishHeavyWeightApp() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.FINISH_HEAVY_WEIGHT_APP_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean convertFromTranslucent(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.CONVERT_FROM_TRANSLUCENT_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean convertToTranslucent(android.os.IBinder token, android.app.ActivityOptions options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        if (options == null) {
            data.writeInt(0);
        } else {
            data.writeInt(1);
            data.writeBundle(options.toBundle());
        }
        mRemote.transact(android.app.IActivityManager.CONVERT_TO_TRANSLUCENT_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.app.ActivityOptions getActivityOptions(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_ACTIVITY_OPTIONS_TRANSACTION, data, reply, 0);
        reply.readException();
        android.app.ActivityOptions options = android.app.ActivityOptions.fromBundle(reply.readBundle());
        data.recycle();
        reply.recycle();
        return options;
    }

    public void setImmersive(android.os.IBinder token, boolean immersive) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(immersive ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_IMMERSIVE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean isImmersive(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.IS_IMMERSIVE_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() == 1;
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean isTopOfTask(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.IS_TOP_OF_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() == 1;
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean isTopActivityImmersive() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.IS_TOP_ACTIVITY_IMMERSIVE_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() == 1;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void crashApplication(int uid, int initialPid, java.lang.String packageName, java.lang.String message) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(uid);
        data.writeInt(initialPid);
        data.writeString(packageName);
        data.writeString(message);
        mRemote.transact(android.app.IActivityManager.CRASH_APPLICATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public java.lang.String getProviderMimeType(android.net.Uri uri, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        uri.writeToParcel(data, 0);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.GET_PROVIDER_MIME_TYPE_TRANSACTION, data, reply, 0);
        reply.readException();
        java.lang.String res = reply.readString();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.os.IBinder newUriPermissionOwner(java.lang.String name) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(name);
        mRemote.transact(android.app.IActivityManager.NEW_URI_PERMISSION_OWNER_TRANSACTION, data, reply, 0);
        reply.readException();
        android.os.IBinder res = reply.readStrongBinder();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.os.IBinder getUriPermissionOwnerForActivity(android.os.IBinder activityToken) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(activityToken);
        mRemote.transact(android.app.IActivityManager.GET_URI_PERMISSION_OWNER_FOR_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        android.os.IBinder res = reply.readStrongBinder();
        data.recycle();
        reply.recycle();
        return res;
    }

    public void grantUriPermissionFromOwner(android.os.IBinder owner, int fromUid, java.lang.String targetPkg, android.net.Uri uri, int mode, int sourceUserId, int targetUserId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(owner);
        data.writeInt(fromUid);
        data.writeString(targetPkg);
        uri.writeToParcel(data, 0);
        data.writeInt(mode);
        data.writeInt(sourceUserId);
        data.writeInt(targetUserId);
        mRemote.transact(android.app.IActivityManager.GRANT_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void revokeUriPermissionFromOwner(android.os.IBinder owner, android.net.Uri uri, int mode, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(owner);
        if (uri != null) {
            data.writeInt(1);
            uri.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(mode);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.REVOKE_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public int checkGrantUriPermission(int callingUid, java.lang.String targetPkg, android.net.Uri uri, int modeFlags, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(callingUid);
        data.writeString(targetPkg);
        uri.writeToParcel(data, 0);
        data.writeInt(modeFlags);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.CHECK_GRANT_URI_PERMISSION_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean dumpHeap(java.lang.String process, int userId, boolean managed, java.lang.String path, android.os.ParcelFileDescriptor fd) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(process);
        data.writeInt(userId);
        data.writeInt(managed ? 1 : 0);
        data.writeString(path);
        if (fd != null) {
            data.writeInt(1);
            fd.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.DUMP_HEAP_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return res;
    }

    public int startActivities(android.app.IApplicationThread caller, java.lang.String callingPackage, android.content.Intent[] intents, java.lang.String[] resolvedTypes, android.os.IBinder resultTo, android.os.Bundle options, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(caller != null ? caller.asBinder() : null);
        data.writeString(callingPackage);
        data.writeTypedArray(intents, 0);
        data.writeStringArray(resolvedTypes);
        data.writeStrongBinder(resultTo);
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.START_ACTIVITIES_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public int getFrontActivityScreenCompatMode() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        int mode = reply.readInt();
        reply.recycle();
        data.recycle();
        return mode;
    }

    public void setFrontActivityScreenCompatMode(int mode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(mode);
        mRemote.transact(android.app.IActivityManager.SET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public int getPackageScreenCompatMode(java.lang.String packageName) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        mRemote.transact(android.app.IActivityManager.GET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        int mode = reply.readInt();
        reply.recycle();
        data.recycle();
        return mode;
    }

    public void setPackageScreenCompatMode(java.lang.String packageName, int mode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(mode);
        mRemote.transact(android.app.IActivityManager.SET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public boolean getPackageAskScreenCompat(java.lang.String packageName) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        mRemote.transact(android.app.IActivityManager.GET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean ask = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return ask;
    }

    public void setPackageAskScreenCompat(java.lang.String packageName, boolean ask) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeInt(ask ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION, data, reply, 0);
        reply.readException();
        reply.recycle();
        data.recycle();
    }

    public boolean switchUser(int userid) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(userid);
        mRemote.transact(android.app.IActivityManager.SWITCH_USER_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean result = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return result;
    }

    public boolean startUserInBackground(int userid) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(userid);
        mRemote.transact(android.app.IActivityManager.START_USER_IN_BACKGROUND_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean result = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return result;
    }

    public boolean unlockUser(int userId, byte[] token, byte[] secret, android.os.IProgressListener listener) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(userId);
        data.writeByteArray(token);
        data.writeByteArray(secret);
        data.writeStrongInterface(listener);
        mRemote.transact(android.app.IActivityManager.UNLOCK_USER_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean result = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return result;
    }

    public int stopUser(int userid, boolean force, android.app.IStopUserCallback callback) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(userid);
        data.writeInt(force ? 1 : 0);
        data.writeStrongInterface(callback);
        mRemote.transact(android.app.IActivityManager.STOP_USER_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        reply.recycle();
        data.recycle();
        return result;
    }

    public android.content.pm.UserInfo getCurrentUser() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_CURRENT_USER_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.pm.UserInfo userInfo = android.content.pm.UserInfo.CREATOR.createFromParcel(reply);
        reply.recycle();
        data.recycle();
        return userInfo;
    }

    public boolean isUserRunning(int userid, int flags) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(userid);
        data.writeInt(flags);
        mRemote.transact(android.app.IActivityManager.IS_USER_RUNNING_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean result = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return result;
    }

    public int[] getRunningUserIds() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_RUNNING_USER_IDS_TRANSACTION, data, reply, 0);
        reply.readException();
        int[] result = reply.createIntArray();
        reply.recycle();
        data.recycle();
        return result;
    }

    public boolean removeTask(int taskId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        mRemote.transact(android.app.IActivityManager.REMOVE_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean result = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return result;
    }

    public void registerProcessObserver(android.app.IProcessObserver observer) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        mRemote.transact(android.app.IActivityManager.REGISTER_PROCESS_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void unregisterProcessObserver(android.app.IProcessObserver observer) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        mRemote.transact(android.app.IActivityManager.UNREGISTER_PROCESS_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void registerUidObserver(android.app.IUidObserver observer, int which) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        data.writeInt(which);
        mRemote.transact(android.app.IActivityManager.REGISTER_UID_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void unregisterUidObserver(android.app.IUidObserver observer) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        mRemote.transact(android.app.IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean isIntentSenderTargetedToPackage(android.content.IIntentSender sender) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        mRemote.transact(android.app.IActivityManager.IS_INTENT_SENDER_TARGETED_TO_PACKAGE_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean isIntentSenderAnActivity(android.content.IIntentSender sender) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        mRemote.transact(android.app.IActivityManager.IS_INTENT_SENDER_AN_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.content.Intent getIntentForIntentSender(android.content.IIntentSender sender) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        mRemote.transact(android.app.IActivityManager.GET_INTENT_FOR_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        android.content.Intent res = (reply.readInt() != 0) ? android.content.Intent.CREATOR.createFromParcel(reply) : null;
        data.recycle();
        reply.recycle();
        return res;
    }

    public java.lang.String getTagForIntentSender(android.content.IIntentSender sender, java.lang.String prefix) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(sender.asBinder());
        data.writeString(prefix);
        mRemote.transact(android.app.IActivityManager.GET_TAG_FOR_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        java.lang.String res = reply.readString();
        data.recycle();
        reply.recycle();
        return res;
    }

    public void updatePersistentConfiguration(android.content.res.Configuration values) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        values.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.UPDATE_PERSISTENT_CONFIGURATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public long[] getProcessPss(int[] pids) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeIntArray(pids);
        mRemote.transact(android.app.IActivityManager.GET_PROCESS_PSS_TRANSACTION, data, reply, 0);
        reply.readException();
        long[] res = reply.createLongArray();
        data.recycle();
        reply.recycle();
        return res;
    }

    public void showBootMessage(java.lang.CharSequence msg, boolean always) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        android.text.TextUtils.writeToParcel(msg, data, 0);
        data.writeInt(always ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SHOW_BOOT_MESSAGE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void keyguardWaitingForActivityDrawn() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.KEYGUARD_WAITING_FOR_ACTIVITY_DRAWN_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void keyguardGoingAway(int flags) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(flags);
        mRemote.transact(android.app.IActivityManager.KEYGUARD_GOING_AWAY_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean shouldUpRecreateTask(android.os.IBinder token, java.lang.String destAffinity) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeString(destAffinity);
        mRemote.transact(android.app.IActivityManager.SHOULD_UP_RECREATE_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean result = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return result;
    }

    public boolean navigateUpTo(android.os.IBinder token, android.content.Intent target, int resultCode, android.content.Intent resultData) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        target.writeToParcel(data, 0);
        data.writeInt(resultCode);
        if (resultData != null) {
            data.writeInt(1);
            resultData.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.NAVIGATE_UP_TO_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean result = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return result;
    }

    public int getLaunchedFromUid(android.os.IBinder activityToken) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(activityToken);
        mRemote.transact(android.app.IActivityManager.GET_LAUNCHED_FROM_UID_TRANSACTION, data, reply, 0);
        reply.readException();
        int result = reply.readInt();
        data.recycle();
        reply.recycle();
        return result;
    }

    public java.lang.String getLaunchedFromPackage(android.os.IBinder activityToken) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(activityToken);
        mRemote.transact(android.app.IActivityManager.GET_LAUNCHED_FROM_PACKAGE_TRANSACTION, data, reply, 0);
        reply.readException();
        java.lang.String result = reply.readString();
        data.recycle();
        reply.recycle();
        return result;
    }

    public void registerUserSwitchObserver(android.app.IUserSwitchObserver observer, java.lang.String name) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        data.writeString(name);
        mRemote.transact(android.app.IActivityManager.REGISTER_USER_SWITCH_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void unregisterUserSwitchObserver(android.app.IUserSwitchObserver observer) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(observer != null ? observer.asBinder() : null);
        mRemote.transact(android.app.IActivityManager.UNREGISTER_USER_SWITCH_OBSERVER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void requestBugReport(@android.app.ActivityManager.BugreportMode
    int bugreportType) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(bugreportType);
        mRemote.transact(android.app.IActivityManager.REQUEST_BUG_REPORT_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public long inputDispatchingTimedOut(int pid, boolean aboveSystem, java.lang.String reason) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(pid);
        data.writeInt(aboveSystem ? 1 : 0);
        data.writeString(reason);
        mRemote.transact(android.app.IActivityManager.INPUT_DISPATCHING_TIMED_OUT_TRANSACTION, data, reply, 0);
        reply.readException();
        long res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public android.os.Bundle getAssistContextExtras(int requestType) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(requestType);
        mRemote.transact(android.app.IActivityManager.GET_ASSIST_CONTEXT_EXTRAS_TRANSACTION, data, reply, 0);
        reply.readException();
        android.os.Bundle res = reply.readBundle();
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean requestAssistContextExtras(int requestType, com.android.internal.os.IResultReceiver receiver, android.os.Bundle receiverExtras, android.os.IBinder activityToken, boolean focused, boolean newSessionId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(requestType);
        data.writeStrongBinder(receiver.asBinder());
        data.writeBundle(receiverExtras);
        data.writeStrongBinder(activityToken);
        data.writeInt(focused ? 1 : 0);
        data.writeInt(newSessionId ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void reportAssistContextExtras(android.os.IBinder token, android.os.Bundle extras, android.app.assist.AssistStructure structure, android.app.assist.AssistContent content, android.net.Uri referrer) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeBundle(extras);
        structure.writeToParcel(data, 0);
        content.writeToParcel(data, 0);
        if (referrer != null) {
            data.writeInt(1);
            referrer.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.REPORT_ASSIST_CONTEXT_EXTRAS_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean launchAssistIntent(android.content.Intent intent, int requestType, java.lang.String hint, int userHandle, android.os.Bundle args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        intent.writeToParcel(data, 0);
        data.writeInt(requestType);
        data.writeString(hint);
        data.writeInt(userHandle);
        data.writeBundle(args);
        mRemote.transact(android.app.IActivityManager.LAUNCH_ASSIST_INTENT_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean isAssistDataAllowedOnCurrentActivity() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.IS_SCREEN_CAPTURE_ALLOWED_ON_CURRENT_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean showAssistFromActivity(android.os.IBinder token, android.os.Bundle args) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeBundle(args);
        mRemote.transact(android.app.IActivityManager.SHOW_ASSIST_FROM_ACTIVITY_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        data.recycle();
        reply.recycle();
        return res;
    }

    public void killUid(int appId, int userId, java.lang.String reason) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(appId);
        data.writeInt(userId);
        data.writeString(reason);
        mRemote.transact(android.app.IActivityManager.KILL_UID_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void hang(android.os.IBinder who, boolean allowRestart) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(who);
        data.writeInt(allowRestart ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.HANG_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void reportActivityFullyDrawn(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.REPORT_ACTIVITY_FULLY_DRAWN_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void notifyActivityDrawn(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.NOTIFY_ACTIVITY_DRAWN_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void restart() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.RESTART_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void performIdleMaintenance() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.PERFORM_IDLE_MAINTENANCE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public void sendIdleJobTrigger() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.SEND_IDLE_JOB_TRIGGER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public android.app.IActivityContainer createVirtualActivityContainer(android.os.IBinder parentActivityToken, android.app.IActivityContainerCallback callback) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(parentActivityToken);
        data.writeStrongBinder(callback == null ? null : callback.asBinder());
        mRemote.transact(android.app.IActivityManager.CREATE_VIRTUAL_ACTIVITY_CONTAINER_TRANSACTION, data, reply, 0);
        reply.readException();
        final int result = reply.readInt();
        final android.app.IActivityContainer res;
        if (result == 1) {
            res = IActivityContainer.Stub.asInterface(reply.readStrongBinder());
        } else {
            res = null;
        }
        data.recycle();
        reply.recycle();
        return res;
    }

    public void deleteActivityContainer(android.app.IActivityContainer activityContainer) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(activityContainer.asBinder());
        mRemote.transact(android.app.IActivityManager.DELETE_ACTIVITY_CONTAINER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    public boolean startBinderTracking() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.START_BINDER_TRACKING_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return res;
    }

    public boolean stopBinderTrackingAndDump(android.os.ParcelFileDescriptor fd) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        if (fd != null) {
            data.writeInt(1);
            fd.writeToParcel(data, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.STOP_BINDER_TRACKING_AND_DUMP_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean res = reply.readInt() != 0;
        reply.recycle();
        data.recycle();
        return res;
    }

    public int setVrMode(android.os.IBinder token, boolean enabled, android.content.ComponentName packageName) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(enabled ? 1 : 0);
        packageName.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.SET_VR_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    public boolean isVrModePackageEnabled(android.content.ComponentName packageName) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        packageName.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.IS_VR_PACKAGE_ENABLED_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res == 1;
    }

    @java.lang.Override
    public android.app.IActivityContainer createStackOnDisplay(int displayId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(displayId);
        mRemote.transact(android.app.IActivityManager.CREATE_STACK_ON_DISPLAY, data, reply, 0);
        reply.readException();
        final int result = reply.readInt();
        final android.app.IActivityContainer res;
        if (result == 1) {
            res = IActivityContainer.Stub.asInterface(reply.readStrongBinder());
        } else {
            res = null;
        }
        data.recycle();
        reply.recycle();
        return res;
    }

    @java.lang.Override
    public int getActivityDisplayId(android.os.IBinder activityToken) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(activityToken);
        mRemote.transact(android.app.IActivityManager.GET_ACTIVITY_DISPLAY_ID_TRANSACTION, data, reply, 0);
        reply.readException();
        final int displayId = reply.readInt();
        data.recycle();
        reply.recycle();
        return displayId;
    }

    @java.lang.Override
    public void startLockTaskMode(int taskId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        mRemote.transact(android.app.IActivityManager.START_LOCK_TASK_BY_TASK_ID_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void startLockTaskMode(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.START_LOCK_TASK_BY_TOKEN_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void startSystemLockTaskMode(int taskId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        mRemote.transact(android.app.IActivityManager.START_SYSTEM_LOCK_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void stopLockTaskMode() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.STOP_LOCK_TASK_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void stopSystemLockTaskMode() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.STOP_SYSTEM_LOCK_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public boolean isInLockTaskMode() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.IS_IN_LOCK_TASK_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean isInLockTaskMode = reply.readInt() == 1;
        data.recycle();
        reply.recycle();
        return isInLockTaskMode;
    }

    @java.lang.Override
    public int getLockTaskModeState() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.GET_LOCK_TASK_MODE_STATE_TRANSACTION, data, reply, 0);
        reply.readException();
        int lockTaskModeState = reply.readInt();
        data.recycle();
        reply.recycle();
        return lockTaskModeState;
    }

    @java.lang.Override
    public void showLockTaskEscapeMessage(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION, data, reply, android.os.IBinder.FLAG_ONEWAY);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void setTaskDescription(android.os.IBinder token, android.app.ActivityManager.TaskDescription values) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        values.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.SET_TASK_DESCRIPTION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void setTaskResizeable(int taskId, int resizeableMode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        data.writeInt(resizeableMode);
        mRemote.transact(android.app.IActivityManager.SET_TASK_RESIZEABLE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void resizeTask(int taskId, android.graphics.Rect r, int resizeMode) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        data.writeInt(resizeMode);
        r.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.RESIZE_TASK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public android.graphics.Rect getTaskBounds(int taskId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(taskId);
        mRemote.transact(android.app.IActivityManager.GET_TASK_BOUNDS_TRANSACTION, data, reply, 0);
        reply.readException();
        android.graphics.Rect rect = android.graphics.Rect.CREATOR.createFromParcel(reply);
        data.recycle();
        reply.recycle();
        return rect;
    }

    @java.lang.Override
    public android.graphics.Bitmap getTaskDescriptionIcon(java.lang.String filename, int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(filename);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.GET_TASK_DESCRIPTION_ICON_TRANSACTION, data, reply, 0);
        reply.readException();
        final android.graphics.Bitmap icon = (reply.readInt() == 0) ? null : android.graphics.Bitmap.CREATOR.createFromParcel(reply);
        data.recycle();
        reply.recycle();
        return icon;
    }

    @java.lang.Override
    public void startInPlaceAnimationOnFrontMostApplication(android.app.ActivityOptions options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        if (options == null) {
            data.writeInt(0);
        } else {
            data.writeInt(1);
            data.writeBundle(options.toBundle());
        }
        mRemote.transact(android.app.IActivityManager.START_IN_PLACE_ANIMATION_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public boolean requestVisibleBehind(android.os.IBinder token, boolean visible) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        data.writeInt(visible ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.REQUEST_VISIBLE_BEHIND_TRANSACTION, data, reply, 0);
        reply.readException();
        boolean success = reply.readInt() > 0;
        data.recycle();
        reply.recycle();
        return success;
    }

    @java.lang.Override
    public boolean isBackgroundVisibleBehind(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.IS_BACKGROUND_VISIBLE_BEHIND_TRANSACTION, data, reply, 0);
        reply.readException();
        final boolean visible = reply.readInt() > 0;
        data.recycle();
        reply.recycle();
        return visible;
    }

    @java.lang.Override
    public void backgroundResourcesReleased(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.BACKGROUND_RESOURCES_RELEASED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void notifyLaunchTaskBehindComplete(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.NOTIFY_LAUNCH_TASK_BEHIND_COMPLETE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void notifyEnterAnimationComplete(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.NOTIFY_ENTER_ANIMATION_COMPLETE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void bootAnimationComplete() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.BOOT_ANIMATION_COMPLETE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void notifyCleartextNetwork(int uid, byte[] firstPacket) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(uid);
        data.writeByteArray(firstPacket);
        mRemote.transact(android.app.IActivityManager.NOTIFY_CLEARTEXT_NETWORK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void setDumpHeapDebugLimit(java.lang.String processName, int uid, long maxMemSize, java.lang.String reportPackage) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(processName);
        data.writeInt(uid);
        data.writeLong(maxMemSize);
        data.writeString(reportPackage);
        mRemote.transact(android.app.IActivityManager.SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void dumpHeapFinished(java.lang.String path) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(path);
        mRemote.transact(android.app.IActivityManager.DUMP_HEAP_FINISHED_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void setVoiceKeepAwake(android.service.voice.IVoiceInteractionSession session, boolean keepAwake) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(session.asBinder());
        data.writeInt(keepAwake ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_VOICE_KEEP_AWAKE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void updateLockTaskPackages(int userId, java.lang.String[] packages) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(userId);
        data.writeStringArray(packages);
        mRemote.transact(android.app.IActivityManager.UPDATE_LOCK_TASK_PACKAGES_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void updateDeviceOwner(java.lang.String packageName) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        mRemote.transact(android.app.IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public int getPackageProcessState(java.lang.String packageName, java.lang.String callingPackage) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(packageName);
        data.writeString(callingPackage);
        mRemote.transact(android.app.IActivityManager.GET_PACKAGE_PROCESS_STATE_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    @java.lang.Override
    public boolean setProcessMemoryTrimLevel(java.lang.String process, int userId, int level) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeString(process);
        data.writeInt(userId);
        data.writeInt(level);
        mRemote.transact(android.app.IActivityManager.SET_PROCESS_MEMORY_TRIM_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res != 0;
    }

    @java.lang.Override
    public boolean isRootVoiceInteraction(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.IS_ROOT_VOICE_INTERACTION_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res != 0;
    }

    @java.lang.Override
    public void exitFreeformMode(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.EXIT_FREEFORM_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public int getActivityStackId(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.GET_ACTIVITY_STACK_ID_TRANSACTION, data, reply, 0);
        reply.readException();
        int stackId = reply.readInt();
        data.recycle();
        reply.recycle();
        return stackId;
    }

    @java.lang.Override
    public void reportSizeConfigurations(android.os.IBinder token, int[] horizontalSizeConfiguration, int[] verticalSizeConfigurations, int[] smallestSizeConfigurations) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        android.app.ActivityManagerProxy.writeIntArray(horizontalSizeConfiguration, data);
        android.app.ActivityManagerProxy.writeIntArray(verticalSizeConfigurations, data);
        android.app.ActivityManagerProxy.writeIntArray(smallestSizeConfigurations, data);
        mRemote.transact(android.app.IActivityManager.REPORT_SIZE_CONFIGURATIONS, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    private static void writeIntArray(int[] array, android.os.Parcel data) {
        if (array == null) {
            data.writeInt(0);
        } else {
            data.writeInt(array.length);
            data.writeIntArray(array);
        }
    }

    @java.lang.Override
    public void suppressResizeConfigChanges(boolean suppress) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(suppress ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SUPPRESS_RESIZE_CONFIG_CHANGES_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void moveTasksToFullscreenStack(int fromStackId, boolean onTop) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(fromStackId);
        data.writeInt(onTop ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.MOVE_TASKS_TO_FULLSCREEN_STACK_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public int getAppStartMode(int uid, java.lang.String packageName) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(uid);
        data.writeString(packageName);
        mRemote.transact(android.app.IActivityManager.GET_APP_START_MODE_TRANSACTION, data, reply, 0);
        reply.readException();
        int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    @java.lang.Override
    public boolean isInMultiWindowMode(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.IN_MULTI_WINDOW_TRANSACTION, data, reply, 0);
        reply.readException();
        final boolean multiWindowMode = (reply.readInt() == 1) ? true : false;
        data.recycle();
        reply.recycle();
        return multiWindowMode;
    }

    @java.lang.Override
    public boolean isInPictureInPictureMode(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.IN_PICTURE_IN_PICTURE_TRANSACTION, data, reply, 0);
        reply.readException();
        final boolean pipMode = (reply.readInt() == 1) ? true : false;
        data.recycle();
        reply.recycle();
        return pipMode;
    }

    @java.lang.Override
    public void enterPictureInPictureMode(android.os.IBinder token) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(token);
        mRemote.transact(android.app.IActivityManager.ENTER_PICTURE_IN_PICTURE_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public boolean isAppForeground(int uid) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(uid);
        mRemote.transact(android.app.IActivityManager.IS_APP_FOREGROUND_TRANSACTION, data, reply, 0);
        final boolean isForeground = (reply.readInt() == 1) ? true : false;
        data.recycle();
        reply.recycle();
        return isForeground;
    }

    @java.lang.Override
    public void notifyPinnedStackAnimationEnded() throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        mRemote.transact(android.app.IActivityManager.NOTIFY_PINNED_STACK_ANIMATION_ENDED_TRANSACTION, data, reply, 0);
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void removeStack(int stackId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(stackId);
        mRemote.transact(android.app.IActivityManager.REMOVE_STACK, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void notifyLockedProfile(@android.annotation.UserIdInt
    int userId) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(userId);
        mRemote.transact(android.app.IActivityManager.NOTIFY_LOCKED_PROFILE, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public void startConfirmDeviceCredentialIntent(android.content.Intent intent) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        intent.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.START_CONFIRM_DEVICE_CREDENTIAL_INTENT, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
    }

    @java.lang.Override
    public int sendIntentSender(android.content.IIntentSender target, int code, android.content.Intent intent, java.lang.String resolvedType, android.content.IIntentReceiver finishedReceiver, java.lang.String requiredPermission, android.os.Bundle options) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeStrongBinder(target.asBinder());
        data.writeInt(code);
        if (intent != null) {
            data.writeInt(1);
            intent.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        data.writeString(resolvedType);
        data.writeStrongBinder(finishedReceiver != null ? finishedReceiver.asBinder() : null);
        data.writeString(requiredPermission);
        if (options != null) {
            data.writeInt(1);
            options.writeToParcel(data, 0);
        } else {
            data.writeInt(0);
        }
        mRemote.transact(android.app.IActivityManager.SEND_INTENT_SENDER_TRANSACTION, data, reply, 0);
        reply.readException();
        final int res = reply.readInt();
        data.recycle();
        reply.recycle();
        return res;
    }

    @java.lang.Override
    public void setVrThread(int tid) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(tid);
        mRemote.transact(android.app.IActivityManager.SET_VR_THREAD_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
        return;
    }

    public void setRenderThread(int tid) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(tid);
        mRemote.transact(android.app.IActivityManager.SET_RENDER_THREAD_TRANSACTION, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
        return;
    }

    public void setHasTopUi(boolean hasTopUi) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        data.writeInt(hasTopUi ? 1 : 0);
        mRemote.transact(android.app.IActivityManager.SET_HAS_TOP_UI, data, reply, 0);
        reply.readException();
        data.recycle();
        reply.recycle();
        return;
    }

    @java.lang.Override
    public boolean canBypassWorkChallenge(android.app.PendingIntent intent) throws android.os.RemoteException {
        android.os.Parcel data = android.os.Parcel.obtain();
        android.os.Parcel reply = android.os.Parcel.obtain();
        data.writeInterfaceToken(android.app.IActivityManager.descriptor);
        intent.writeToParcel(data, 0);
        mRemote.transact(android.app.IActivityManager.CAN_BYPASS_WORK_CHALLENGE, data, reply, 0);
        reply.readException();
        final int result = reply.readInt();
        data.recycle();
        reply.recycle();
        return result != 0;
    }

    private android.os.IBinder mRemote;
}

