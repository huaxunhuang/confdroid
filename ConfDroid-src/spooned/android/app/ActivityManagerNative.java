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
public abstract class ActivityManagerNative extends android.os.Binder implements android.app.IActivityManager {
    /**
     * Cast a Binder object into an activity manager interface, generating
     * a proxy if needed.
     */
    public static android.app.IActivityManager asInterface(android.os.IBinder obj) {
        if (obj == null) {
            return null;
        }
        android.app.IActivityManager in = ((android.app.IActivityManager) (obj.queryLocalInterface(android.app.IActivityManager.descriptor)));
        if (in != null) {
            return in;
        }
        return new android.app.ActivityManagerProxy(obj);
    }

    /**
     * Retrieve the system's default/global activity manager.
     */
    public static android.app.IActivityManager getDefault() {
        return android.app.ActivityManagerNative.gDefault.get();
    }

    /**
     * Convenience for checking whether the system is ready.  For internal use only.
     */
    public static boolean isSystemReady() {
        if (!android.app.ActivityManagerNative.sSystemReady) {
            android.app.ActivityManagerNative.sSystemReady = android.app.ActivityManagerNative.getDefault().testIsSystemReady();
        }
        return android.app.ActivityManagerNative.sSystemReady;
    }

    static volatile boolean sSystemReady = false;

    public static void broadcastStickyIntent(android.content.Intent intent, java.lang.String permission, int userId) {
        android.app.ActivityManagerNative.broadcastStickyIntent(intent, permission, android.app.AppOpsManager.OP_NONE, userId);
    }

    /**
     * Convenience for sending a sticky broadcast.  For internal use only.
     * If you don't care about permission, use null.
     */
    public static void broadcastStickyIntent(android.content.Intent intent, java.lang.String permission, int appOp, int userId) {
        try {
            /* permission */
            android.app.ActivityManagerNative.getDefault().broadcastIntent(null, intent, null, null, android.app.Activity.RESULT_OK, null, null, null, appOp, null, false, true, userId);
        } catch (android.os.RemoteException ex) {
        }
    }

    public static void noteWakeupAlarm(android.app.PendingIntent ps, int sourceUid, java.lang.String sourcePkg, java.lang.String tag) {
        try {
            android.app.ActivityManagerNative.getDefault().noteWakeupAlarm(ps != null ? ps.getTarget() : null, sourceUid, sourcePkg, tag);
        } catch (android.os.RemoteException ex) {
        }
    }

    public static void noteAlarmStart(android.app.PendingIntent ps, int sourceUid, java.lang.String tag) {
        try {
            android.app.ActivityManagerNative.getDefault().noteAlarmStart(ps != null ? ps.getTarget() : null, sourceUid, tag);
        } catch (android.os.RemoteException ex) {
        }
    }

    public static void noteAlarmFinish(android.app.PendingIntent ps, int sourceUid, java.lang.String tag) {
        try {
            android.app.ActivityManagerNative.getDefault().noteAlarmFinish(ps != null ? ps.getTarget() : null, sourceUid, tag);
        } catch (android.os.RemoteException ex) {
        }
    }

    public ActivityManagerNative() {
        attachInterface(this, android.app.IActivityManager.descriptor);
    }

    @java.lang.Override
    public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
        switch (code) {
            case android.app.IActivityManager.START_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String callingPackage = data.readString();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    android.os.IBinder resultTo = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    int startFlags = data.readInt();
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int result = startActivity(app, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, options);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.START_ACTIVITY_AS_USER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String callingPackage = data.readString();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    android.os.IBinder resultTo = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    int startFlags = data.readInt();
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int userId = data.readInt();
                    int result = startActivityAsUser(app, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, options, userId);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.START_ACTIVITY_AS_CALLER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String callingPackage = data.readString();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    android.os.IBinder resultTo = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    int startFlags = data.readInt();
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    boolean ignoreTargetSecurity = data.readInt() != 0;
                    int userId = data.readInt();
                    int result = startActivityAsCaller(app, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, options, ignoreTargetSecurity, userId);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.START_ACTIVITY_AND_WAIT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String callingPackage = data.readString();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    android.os.IBinder resultTo = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    int startFlags = data.readInt();
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int userId = data.readInt();
                    android.app.IActivityManager.WaitResult result = startActivityAndWait(app, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, profilerInfo, options, userId);
                    reply.writeNoException();
                    result.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.START_ACTIVITY_WITH_CONFIG_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String callingPackage = data.readString();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    android.os.IBinder resultTo = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    int startFlags = data.readInt();
                    android.content.res.Configuration config = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int userId = data.readInt();
                    int result = startActivityWithConfig(app, callingPackage, intent, resolvedType, resultTo, resultWho, requestCode, startFlags, config, options, userId);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.START_ACTIVITY_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    android.content.IntentSender intent = android.content.IntentSender.CREATOR.createFromParcel(data);
                    android.content.Intent fillInIntent = null;
                    if (data.readInt() != 0) {
                        fillInIntent = android.content.Intent.CREATOR.createFromParcel(data);
                    }
                    java.lang.String resolvedType = data.readString();
                    android.os.IBinder resultTo = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    int flagsMask = data.readInt();
                    int flagsValues = data.readInt();
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int result = startActivityIntentSender(app, intent, fillInIntent, resolvedType, resultTo, resultWho, requestCode, flagsMask, flagsValues, options);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.START_VOICE_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String callingPackage = data.readString();
                    int callingPid = data.readInt();
                    int callingUid = data.readInt();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    android.service.voice.IVoiceInteractionSession session = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    com.android.internal.app.IVoiceInteractor interactor = IVoiceInteractor.Stub.asInterface(data.readStrongBinder());
                    int startFlags = data.readInt();
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int userId = data.readInt();
                    int result = startVoiceActivity(callingPackage, callingPid, callingUid, intent, resolvedType, session, interactor, startFlags, profilerInfo, options, userId);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.START_LOCAL_VOICE_INTERACTION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.os.Bundle options = data.readBundle();
                    startLocalVoiceInteraction(token, options);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.STOP_LOCAL_VOICE_INTERACTION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    stopLocalVoiceInteraction(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SUPPORTS_LOCAL_VOICE_INTERACTION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean result = supportsLocalVoiceInteraction();
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.START_NEXT_MATCHING_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder callingActivity = data.readStrongBinder();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    boolean result = startNextMatchingActivity(callingActivity, intent, options);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.START_ACTIVITY_FROM_RECENTS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int taskId = data.readInt();
                    final android.os.Bundle options = (data.readInt() == 0) ? null : android.os.Bundle.CREATOR.createFromParcel(data);
                    final int result = startActivityFromRecents(taskId, options);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.FINISH_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.Intent resultData = null;
                    int resultCode = data.readInt();
                    if (data.readInt() != 0) {
                        resultData = android.content.Intent.CREATOR.createFromParcel(data);
                    }
                    int finishTask = data.readInt();
                    boolean res = finishActivity(token, resultCode, resultData, finishTask);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.FINISH_SUB_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    finishSubActivity(token, resultWho, requestCode);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.FINISH_ACTIVITY_AFFINITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean res = finishActivityAffinity(token);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.FINISH_VOICE_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.service.voice.IVoiceInteractionSession session = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    finishVoiceTask(session);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.RELEASE_ACTIVITY_INSTANCE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean res = releaseActivityInstance(token);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.RELEASE_SOME_ACTIVITIES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(data.readStrongBinder());
                    releaseSomeActivities(app);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.WILL_ACTIVITY_BE_VISIBLE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean res = willActivityBeVisible(token);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.REGISTER_RECEIVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = (b != null) ? android.app.ApplicationThreadNative.asInterface(b) : null;
                    java.lang.String packageName = data.readString();
                    b = data.readStrongBinder();
                    android.content.IIntentReceiver rec = (b != null) ? IIntentReceiver.Stub.asInterface(b) : null;
                    android.content.IntentFilter filter = android.content.IntentFilter.CREATOR.createFromParcel(data);
                    java.lang.String perm = data.readString();
                    int userId = data.readInt();
                    android.content.Intent intent = registerReceiver(app, packageName, rec, filter, perm, userId);
                    reply.writeNoException();
                    if (intent != null) {
                        reply.writeInt(1);
                        intent.writeToParcel(reply, 0);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.UNREGISTER_RECEIVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    if (b == null) {
                        return true;
                    }
                    android.content.IIntentReceiver rec = IIntentReceiver.Stub.asInterface(b);
                    unregisterReceiver(rec);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.BROADCAST_INTENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = (b != null) ? android.app.ApplicationThreadNative.asInterface(b) : null;
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    b = data.readStrongBinder();
                    android.content.IIntentReceiver resultTo = (b != null) ? IIntentReceiver.Stub.asInterface(b) : null;
                    int resultCode = data.readInt();
                    java.lang.String resultData = data.readString();
                    android.os.Bundle resultExtras = data.readBundle();
                    java.lang.String[] perms = data.readStringArray();
                    int appOp = data.readInt();
                    android.os.Bundle options = data.readBundle();
                    boolean serialized = data.readInt() != 0;
                    boolean sticky = data.readInt() != 0;
                    int userId = data.readInt();
                    int res = broadcastIntent(app, intent, resolvedType, resultTo, resultCode, resultData, resultExtras, perms, appOp, options, serialized, sticky, userId);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.UNBROADCAST_INTENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = (b != null) ? android.app.ApplicationThreadNative.asInterface(b) : null;
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    int userId = data.readInt();
                    unbroadcastIntent(app, intent, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.FINISH_RECEIVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder who = data.readStrongBinder();
                    int resultCode = data.readInt();
                    java.lang.String resultData = data.readString();
                    android.os.Bundle resultExtras = data.readBundle();
                    boolean resultAbort = data.readInt() != 0;
                    int intentFlags = data.readInt();
                    if (who != null) {
                        finishReceiver(who, resultCode, resultData, resultExtras, resultAbort, intentFlags);
                    }
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ATTACH_APPLICATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(data.readStrongBinder());
                    if (app != null) {
                        attachApplication(app);
                    }
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ACTIVITY_IDLE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.res.Configuration config = null;
                    if (data.readInt() != 0) {
                        config = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    }
                    boolean stopProfiling = data.readInt() != 0;
                    if (token != null) {
                        activityIdle(token, config, stopProfiling);
                    }
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ACTIVITY_RESUMED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    activityResumed(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ACTIVITY_PAUSED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    activityPaused(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ACTIVITY_STOPPED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.os.Bundle map = data.readBundle();
                    android.os.PersistableBundle persistentState = data.readPersistableBundle();
                    java.lang.CharSequence description = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    activityStopped(token, map, persistentState, description);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ACTIVITY_SLEPT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    activitySlept(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ACTIVITY_DESTROYED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    activityDestroyed(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ACTIVITY_RELAUNCHED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    activityRelaunched(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_CALLING_PACKAGE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    java.lang.String res = (token != null) ? getCallingPackage(token) : null;
                    reply.writeNoException();
                    reply.writeString(res);
                    return true;
                }
            case android.app.IActivityManager.GET_CALLING_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.ComponentName cn = getCallingActivity(token);
                    reply.writeNoException();
                    android.content.ComponentName.writeToParcel(cn, reply);
                    return true;
                }
            case android.app.IActivityManager.GET_APP_TASKS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String callingPackage = data.readString();
                    java.util.List<android.app.IAppTask> list = getAppTasks(callingPackage);
                    reply.writeNoException();
                    int N = (list != null) ? list.size() : -1;
                    reply.writeInt(N);
                    int i;
                    for (i = 0; i < N; i++) {
                        android.app.IAppTask task = list.get(i);
                        reply.writeStrongBinder(task.asBinder());
                    }
                    return true;
                }
            case android.app.IActivityManager.ADD_APP_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder activityToken = data.readStrongBinder();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    android.app.ActivityManager.TaskDescription descr = android.app.ActivityManager.TaskDescription.CREATOR.createFromParcel(data);
                    android.graphics.Bitmap thumbnail = android.graphics.Bitmap.CREATOR.createFromParcel(data);
                    int res = addAppTask(activityToken, intent, descr, thumbnail);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.GET_APP_TASK_THUMBNAIL_SIZE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.graphics.Point size = getAppTaskThumbnailSize();
                    reply.writeNoException();
                    size.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.GET_TASKS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int maxNum = data.readInt();
                    int fl = data.readInt();
                    java.util.List<android.app.ActivityManager.RunningTaskInfo> list = getTasks(maxNum, fl);
                    reply.writeNoException();
                    int N = (list != null) ? list.size() : -1;
                    reply.writeInt(N);
                    int i;
                    for (i = 0; i < N; i++) {
                        android.app.ActivityManager.RunningTaskInfo info = list.get(i);
                        info.writeToParcel(reply, 0);
                    }
                    return true;
                }
            case android.app.IActivityManager.GET_RECENT_TASKS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int maxNum = data.readInt();
                    int fl = data.readInt();
                    int userId = data.readInt();
                    android.content.pm.ParceledListSlice<android.app.ActivityManager.RecentTaskInfo> list = getRecentTasks(maxNum, fl, userId);
                    reply.writeNoException();
                    list.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    return true;
                }
            case android.app.IActivityManager.GET_TASK_THUMBNAIL_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int id = data.readInt();
                    android.app.ActivityManager.TaskThumbnail taskThumbnail = getTaskThumbnail(id);
                    reply.writeNoException();
                    if (taskThumbnail != null) {
                        reply.writeInt(1);
                        taskThumbnail.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.GET_SERVICES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int maxNum = data.readInt();
                    int fl = data.readInt();
                    java.util.List<android.app.ActivityManager.RunningServiceInfo> list = getServices(maxNum, fl);
                    reply.writeNoException();
                    int N = (list != null) ? list.size() : -1;
                    reply.writeInt(N);
                    int i;
                    for (i = 0; i < N; i++) {
                        android.app.ActivityManager.RunningServiceInfo info = list.get(i);
                        info.writeToParcel(reply, 0);
                    }
                    return true;
                }
            case android.app.IActivityManager.GET_PROCESSES_IN_ERROR_STATE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.util.List<android.app.ActivityManager.ProcessErrorStateInfo> list = getProcessesInErrorState();
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }
            case android.app.IActivityManager.GET_RUNNING_APP_PROCESSES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.util.List<android.app.ActivityManager.RunningAppProcessInfo> list = getRunningAppProcesses();
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }
            case android.app.IActivityManager.GET_RUNNING_EXTERNAL_APPLICATIONS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.util.List<android.content.pm.ApplicationInfo> list = getRunningExternalApplications();
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }
            case android.app.IActivityManager.MOVE_TASK_TO_FRONT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int task = data.readInt();
                    int fl = data.readInt();
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    moveTaskToFront(task, fl, options);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.MOVE_ACTIVITY_TASK_TO_BACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean nonRoot = data.readInt() != 0;
                    boolean res = moveActivityTaskToBack(token, nonRoot);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.MOVE_TASK_BACKWARDS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int task = data.readInt();
                    moveTaskBackwards(task);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.MOVE_TASK_TO_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    int stackId = data.readInt();
                    boolean toTop = data.readInt() != 0;
                    moveTaskToStack(taskId, stackId, toTop);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.MOVE_TASK_TO_DOCKED_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    int createMode = data.readInt();
                    boolean toTop = data.readInt() != 0;
                    boolean animate = data.readInt() != 0;
                    android.graphics.Rect bounds = null;
                    boolean hasBounds = data.readInt() != 0;
                    if (hasBounds) {
                        bounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    final boolean moveHomeStackFront = data.readInt() != 0;
                    final boolean res = moveTaskToDockedStack(taskId, createMode, toTop, animate, bounds, moveHomeStackFront);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.MOVE_TOP_ACTIVITY_TO_PINNED_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int stackId = data.readInt();
                    final android.graphics.Rect r = android.graphics.Rect.CREATOR.createFromParcel(data);
                    final boolean res = moveTopActivityToPinnedStack(stackId, r);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.RESIZE_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int stackId = data.readInt();
                    final boolean hasRect = data.readInt() != 0;
                    android.graphics.Rect r = null;
                    if (hasRect) {
                        r = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    final boolean allowResizeInDockedMode = data.readInt() == 1;
                    final boolean preserveWindows = data.readInt() == 1;
                    final boolean animate = data.readInt() == 1;
                    final int animationDuration = data.readInt();
                    resizeStack(stackId, r, allowResizeInDockedMode, preserveWindows, animate, animationDuration);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.RESIZE_PINNED_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final boolean hasBounds = data.readInt() != 0;
                    android.graphics.Rect bounds = null;
                    if (hasBounds) {
                        bounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    final boolean hasTempPinnedTaskBounds = data.readInt() != 0;
                    android.graphics.Rect tempPinnedTaskBounds = null;
                    if (hasTempPinnedTaskBounds) {
                        tempPinnedTaskBounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    resizePinnedStack(bounds, tempPinnedTaskBounds);
                    return true;
                }
            case android.app.IActivityManager.SWAP_DOCKED_AND_FULLSCREEN_STACK :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    swapDockedAndFullscreenStack();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.RESIZE_DOCKED_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final boolean hasBounds = data.readInt() != 0;
                    android.graphics.Rect bounds = null;
                    if (hasBounds) {
                        bounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    final boolean hasTempDockedTaskBounds = data.readInt() != 0;
                    android.graphics.Rect tempDockedTaskBounds = null;
                    if (hasTempDockedTaskBounds) {
                        tempDockedTaskBounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    final boolean hasTempDockedTaskInsetBounds = data.readInt() != 0;
                    android.graphics.Rect tempDockedTaskInsetBounds = null;
                    if (hasTempDockedTaskInsetBounds) {
                        tempDockedTaskInsetBounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    final boolean hasTempOtherTaskBounds = data.readInt() != 0;
                    android.graphics.Rect tempOtherTaskBounds = null;
                    if (hasTempOtherTaskBounds) {
                        tempOtherTaskBounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    final boolean hasTempOtherTaskInsetBounds = data.readInt() != 0;
                    android.graphics.Rect tempOtherTaskInsetBounds = null;
                    if (hasTempOtherTaskInsetBounds) {
                        tempOtherTaskInsetBounds = android.graphics.Rect.CREATOR.createFromParcel(data);
                    }
                    resizeDockedStack(bounds, tempDockedTaskBounds, tempDockedTaskInsetBounds, tempOtherTaskBounds, tempOtherTaskInsetBounds);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.POSITION_TASK_IN_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    int stackId = data.readInt();
                    int position = data.readInt();
                    positionTaskInStack(taskId, stackId, position);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_ALL_STACK_INFOS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.util.List<android.app.ActivityManager.StackInfo> list = getAllStackInfos();
                    reply.writeNoException();
                    reply.writeTypedList(list);
                    return true;
                }
            case android.app.IActivityManager.GET_STACK_INFO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int stackId = data.readInt();
                    android.app.ActivityManager.StackInfo info = getStackInfo(stackId);
                    reply.writeNoException();
                    if (info != null) {
                        reply.writeInt(1);
                        info.writeToParcel(reply, 0);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.IS_IN_HOME_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    boolean isInHomeStack = isInHomeStack(taskId);
                    reply.writeNoException();
                    reply.writeInt(isInHomeStack ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.SET_FOCUSED_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int stackId = data.readInt();
                    setFocusedStack(stackId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_FOCUSED_STACK_ID_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int focusedStackId = getFocusedStackId();
                    reply.writeNoException();
                    reply.writeInt(focusedStackId);
                    return true;
                }
            case android.app.IActivityManager.SET_FOCUSED_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    setFocusedTask(taskId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REGISTER_TASK_STACK_LISTENER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    registerTaskStackListener(ITaskStackListener.Stub.asInterface(token));
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_TASK_FOR_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean onlyRoot = data.readInt() != 0;
                    int res = (token != null) ? getTaskForActivity(token, onlyRoot) : -1;
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.GET_CONTENT_PROVIDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String name = data.readString();
                    int userId = data.readInt();
                    boolean stable = data.readInt() != 0;
                    android.app.IActivityManager.ContentProviderHolder cph = getContentProvider(app, name, userId, stable);
                    reply.writeNoException();
                    if (cph != null) {
                        reply.writeInt(1);
                        cph.writeToParcel(reply, 0);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.GET_CONTENT_PROVIDER_EXTERNAL_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String name = data.readString();
                    int userId = data.readInt();
                    android.os.IBinder token = data.readStrongBinder();
                    android.app.IActivityManager.ContentProviderHolder cph = getContentProviderExternal(name, userId, token);
                    reply.writeNoException();
                    if (cph != null) {
                        reply.writeInt(1);
                        cph.writeToParcel(reply, 0);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.PUBLISH_CONTENT_PROVIDERS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.util.ArrayList<android.app.IActivityManager.ContentProviderHolder> providers = data.createTypedArrayList(android.app.IActivityManager.ContentProviderHolder.CREATOR);
                    publishContentProviders(app, providers);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REF_CONTENT_PROVIDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    int stable = data.readInt();
                    int unstable = data.readInt();
                    boolean res = refContentProvider(b, stable, unstable);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.UNSTABLE_PROVIDER_DIED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    unstableProviderDied(b);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.APP_NOT_RESPONDING_VIA_PROVIDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    appNotRespondingViaProvider(b);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REMOVE_CONTENT_PROVIDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    boolean stable = data.readInt() != 0;
                    removeContentProvider(b, stable);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REMOVE_CONTENT_PROVIDER_EXTERNAL_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String name = data.readString();
                    android.os.IBinder token = data.readStrongBinder();
                    removeContentProviderExternal(name, token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_RUNNING_SERVICE_CONTROL_PANEL_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.ComponentName comp = android.content.ComponentName.CREATOR.createFromParcel(data);
                    android.app.PendingIntent pi = getRunningServiceControlPanel(comp);
                    reply.writeNoException();
                    android.app.PendingIntent.writePendingIntentOrNullToParcel(pi, reply);
                    return true;
                }
            case android.app.IActivityManager.START_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    android.content.Intent service = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    java.lang.String callingPackage = data.readString();
                    int userId = data.readInt();
                    android.content.ComponentName cn = startService(app, service, resolvedType, callingPackage, userId);
                    reply.writeNoException();
                    android.content.ComponentName.writeToParcel(cn, reply);
                    return true;
                }
            case android.app.IActivityManager.STOP_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    android.content.Intent service = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    int userId = data.readInt();
                    int res = stopService(app, service, resolvedType, userId);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.STOP_SERVICE_TOKEN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.ComponentName className = android.content.ComponentName.readFromParcel(data);
                    android.os.IBinder token = data.readStrongBinder();
                    int startId = data.readInt();
                    boolean res = stopServiceToken(className, token, startId);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.SET_SERVICE_FOREGROUND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.ComponentName className = android.content.ComponentName.readFromParcel(data);
                    android.os.IBinder token = data.readStrongBinder();
                    int id = data.readInt();
                    android.app.Notification notification = null;
                    if (data.readInt() != 0) {
                        notification = android.app.Notification.CREATOR.createFromParcel(data);
                    }
                    int sflags = data.readInt();
                    setServiceForeground(className, token, id, notification, sflags);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.BIND_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.Intent service = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    b = data.readStrongBinder();
                    int fl = data.readInt();
                    java.lang.String callingPackage = data.readString();
                    int userId = data.readInt();
                    android.app.IServiceConnection conn = IServiceConnection.Stub.asInterface(b);
                    int res = bindService(app, token, service, resolvedType, conn, fl, callingPackage, userId);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.UNBIND_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IServiceConnection conn = IServiceConnection.Stub.asInterface(b);
                    boolean res = unbindService(conn);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.PUBLISH_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    android.os.IBinder service = data.readStrongBinder();
                    publishService(token, intent, service);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.UNBIND_FINISHED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    boolean doRebind = data.readInt() != 0;
                    unbindFinished(token, intent, doRebind);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SERVICE_DONE_EXECUTING_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    int type = data.readInt();
                    int startId = data.readInt();
                    int res = data.readInt();
                    serviceDoneExecuting(token, type, startId, res);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.START_INSTRUMENTATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.ComponentName className = android.content.ComponentName.readFromParcel(data);
                    java.lang.String profileFile = data.readString();
                    int fl = data.readInt();
                    android.os.Bundle arguments = data.readBundle();
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IInstrumentationWatcher w = IInstrumentationWatcher.Stub.asInterface(b);
                    b = data.readStrongBinder();
                    android.app.IUiAutomationConnection c = IUiAutomationConnection.Stub.asInterface(b);
                    int userId = data.readInt();
                    java.lang.String abiOverride = data.readString();
                    boolean res = startInstrumentation(className, profileFile, fl, arguments, w, c, userId, abiOverride);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.FINISH_INSTRUMENTATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    int resultCode = data.readInt();
                    android.os.Bundle results = data.readBundle();
                    finishInstrumentation(app, resultCode, results);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_CONFIGURATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.res.Configuration config = getConfiguration();
                    reply.writeNoException();
                    config.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.UPDATE_CONFIGURATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.res.Configuration config = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    updateConfiguration(config);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_REQUESTED_ORIENTATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    int requestedOrientation = data.readInt();
                    setRequestedOrientation(token, requestedOrientation);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_REQUESTED_ORIENTATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    int req = getRequestedOrientation(token);
                    reply.writeNoException();
                    reply.writeInt(req);
                    return true;
                }
            case android.app.IActivityManager.GET_ACTIVITY_CLASS_FOR_TOKEN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.ComponentName cn = getActivityClassForToken(token);
                    reply.writeNoException();
                    android.content.ComponentName.writeToParcel(cn, reply);
                    return true;
                }
            case android.app.IActivityManager.GET_PACKAGE_FOR_TOKEN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    reply.writeNoException();
                    reply.writeString(getPackageForToken(token));
                    return true;
                }
            case android.app.IActivityManager.GET_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int type = data.readInt();
                    java.lang.String packageName = data.readString();
                    android.os.IBinder token = data.readStrongBinder();
                    java.lang.String resultWho = data.readString();
                    int requestCode = data.readInt();
                    android.content.Intent[] requestIntents;
                    java.lang.String[] requestResolvedTypes;
                    if (data.readInt() != 0) {
                        requestIntents = data.createTypedArray(android.content.Intent.CREATOR);
                        requestResolvedTypes = data.createStringArray();
                    } else {
                        requestIntents = null;
                        requestResolvedTypes = null;
                    }
                    int fl = data.readInt();
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int userId = data.readInt();
                    android.content.IIntentSender res = getIntentSender(type, packageName, token, resultWho, requestCode, requestIntents, requestResolvedTypes, fl, options, userId);
                    reply.writeNoException();
                    reply.writeStrongBinder(res != null ? res.asBinder() : null);
                    return true;
                }
            case android.app.IActivityManager.CANCEL_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    cancelIntentSender(r);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_PACKAGE_FOR_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    java.lang.String res = getPackageForIntentSender(r);
                    reply.writeNoException();
                    reply.writeString(res);
                    return true;
                }
            case android.app.IActivityManager.GET_UID_FOR_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    int res = getUidForIntentSender(r);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.HANDLE_INCOMING_USER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int callingPid = data.readInt();
                    int callingUid = data.readInt();
                    int userId = data.readInt();
                    boolean allowAll = data.readInt() != 0;
                    boolean requireFull = data.readInt() != 0;
                    java.lang.String name = data.readString();
                    java.lang.String callerPackage = data.readString();
                    int res = handleIncomingUser(callingPid, callingUid, userId, allowAll, requireFull, name, callerPackage);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.SET_PROCESS_LIMIT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int max = data.readInt();
                    setProcessLimit(max);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_PROCESS_LIMIT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int limit = getProcessLimit();
                    reply.writeNoException();
                    reply.writeInt(limit);
                    return true;
                }
            case android.app.IActivityManager.SET_PROCESS_FOREGROUND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    int pid = data.readInt();
                    boolean isForeground = data.readInt() != 0;
                    setProcessForeground(token, pid, isForeground);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.CHECK_PERMISSION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String perm = data.readString();
                    int pid = data.readInt();
                    int uid = data.readInt();
                    int res = checkPermission(perm, pid, uid);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.CHECK_PERMISSION_WITH_TOKEN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String perm = data.readString();
                    int pid = data.readInt();
                    int uid = data.readInt();
                    android.os.IBinder token = data.readStrongBinder();
                    int res = checkPermissionWithToken(perm, pid, uid, token);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.CHECK_URI_PERMISSION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int pid = data.readInt();
                    int uid = data.readInt();
                    int mode = data.readInt();
                    int userId = data.readInt();
                    android.os.IBinder callerToken = data.readStrongBinder();
                    int res = checkUriPermission(uri, pid, uid, mode, userId, callerToken);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.CLEAR_APP_DATA_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    android.content.pm.IPackageDataObserver observer = IPackageDataObserver.Stub.asInterface(data.readStrongBinder());
                    int userId = data.readInt();
                    boolean res = clearApplicationUserData(packageName, observer, userId);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.GRANT_URI_PERMISSION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String targetPkg = data.readString();
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    int userId = data.readInt();
                    grantUriPermission(app, targetPkg, uri, mode, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REVOKE_URI_PERMISSION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    int userId = data.readInt();
                    revokeUriPermission(app, uri, mode, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.TAKE_PERSISTABLE_URI_PERMISSION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    int userId = data.readInt();
                    takePersistableUriPermission(uri, mode, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.RELEASE_PERSISTABLE_URI_PERMISSION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    int userId = data.readInt();
                    releasePersistableUriPermission(uri, mode, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_PERSISTED_URI_PERMISSIONS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final java.lang.String packageName = data.readString();
                    final boolean incoming = data.readInt() != 0;
                    final android.content.pm.ParceledListSlice<android.content.UriPermission> perms = getPersistedUriPermissions(packageName, incoming);
                    reply.writeNoException();
                    perms.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    return true;
                }
            case android.app.IActivityManager.GET_GRANTED_URI_PERMISSIONS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final java.lang.String packageName = data.readString();
                    final int userId = data.readInt();
                    final android.content.pm.ParceledListSlice<android.content.UriPermission> perms = getGrantedUriPermissions(packageName, userId);
                    reply.writeNoException();
                    perms.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    return true;
                }
            case android.app.IActivityManager.CLEAR_GRANTED_URI_PERMISSIONS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final java.lang.String packageName = data.readString();
                    final int userId = data.readInt();
                    clearGrantedUriPermissions(packageName, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SHOW_WAITING_FOR_DEBUGGER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    boolean waiting = data.readInt() != 0;
                    showWaitingForDebugger(app, waiting);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_MEMORY_INFO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.ActivityManager.MemoryInfo mi = new android.app.ActivityManager.MemoryInfo();
                    getMemoryInfo(mi);
                    reply.writeNoException();
                    mi.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.UNHANDLED_BACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    unhandledBack();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.OPEN_CONTENT_URI_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.net.Uri uri = android.net.Uri.parse(data.readString());
                    android.os.ParcelFileDescriptor pfd = openContentUri(uri);
                    reply.writeNoException();
                    if (pfd != null) {
                        reply.writeInt(1);
                        pfd.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.SET_LOCK_SCREEN_SHOWN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final boolean showing = data.readInt() != 0;
                    final boolean occluded = data.readInt() != 0;
                    setLockScreenShown(showing, occluded);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_DEBUG_APP_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String pn = data.readString();
                    boolean wfd = data.readInt() != 0;
                    boolean per = data.readInt() != 0;
                    setDebugApp(pn, wfd, per);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_ALWAYS_FINISH_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean enabled = data.readInt() != 0;
                    setAlwaysFinish(enabled);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_ACTIVITY_CONTROLLER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IActivityController watcher = IActivityController.Stub.asInterface(data.readStrongBinder());
                    boolean imAMonkey = data.readInt() != 0;
                    setActivityController(watcher, imAMonkey);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_LENIENT_BACKGROUND_CHECK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean enabled = data.readInt() != 0;
                    setLenientBackgroundCheck(enabled);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_MEMORY_TRIM_LEVEL_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int level = getMemoryTrimLevel();
                    reply.writeNoException();
                    reply.writeInt(level);
                    return true;
                }
            case android.app.IActivityManager.ENTER_SAFE_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    enterSafeMode();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTE_WAKEUP_ALARM_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender is = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    int sourceUid = data.readInt();
                    java.lang.String sourcePkg = data.readString();
                    java.lang.String tag = data.readString();
                    android.app.ActivityManagerNative.noteWakeupAlarm(is, sourceUid, sourcePkg, tag);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTE_ALARM_START_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender is = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    int sourceUid = data.readInt();
                    java.lang.String tag = data.readString();
                    android.app.ActivityManagerNative.noteAlarmStart(is, sourceUid, tag);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTE_ALARM_FINISH_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender is = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    int sourceUid = data.readInt();
                    java.lang.String tag = data.readString();
                    android.app.ActivityManagerNative.noteAlarmFinish(is, sourceUid, tag);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.KILL_PIDS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int[] pids = data.createIntArray();
                    java.lang.String reason = data.readString();
                    boolean secure = data.readInt() != 0;
                    boolean res = killPids(pids, reason, secure);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.KILL_PROCESSES_BELOW_FOREGROUND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String reason = data.readString();
                    boolean res = killProcessesBelowForeground(reason);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.HANDLE_APPLICATION_CRASH_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder app = data.readStrongBinder();
                    android.app.ApplicationErrorReport.CrashInfo ci = new android.app.ApplicationErrorReport.CrashInfo(data);
                    handleApplicationCrash(app, ci);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.HANDLE_APPLICATION_WTF_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder app = data.readStrongBinder();
                    java.lang.String tag = data.readString();
                    boolean system = data.readInt() != 0;
                    android.app.ApplicationErrorReport.CrashInfo ci = new android.app.ApplicationErrorReport.CrashInfo(data);
                    boolean res = handleApplicationWtf(app, tag, system, ci);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.HANDLE_APPLICATION_STRICT_MODE_VIOLATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder app = data.readStrongBinder();
                    int violationMask = data.readInt();
                    android.os.StrictMode.ViolationInfo info = new android.os.StrictMode.ViolationInfo(data);
                    handleApplicationStrictModeViolation(app, violationMask, info);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SIGNAL_PERSISTENT_PROCESSES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int sig = data.readInt();
                    signalPersistentProcesses(sig);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.KILL_BACKGROUND_PROCESSES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    int userId = data.readInt();
                    killBackgroundProcesses(packageName, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.KILL_ALL_BACKGROUND_PROCESSES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    killAllBackgroundProcesses();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.KILL_PACKAGE_DEPENDENTS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    int userId = data.readInt();
                    killPackageDependents(packageName, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.FORCE_STOP_PACKAGE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    int userId = data.readInt();
                    forceStopPackage(packageName, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_MY_MEMORY_STATE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.ActivityManager.RunningAppProcessInfo info = new android.app.ActivityManager.RunningAppProcessInfo();
                    getMyMemoryState(info);
                    reply.writeNoException();
                    info.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.GET_DEVICE_CONFIGURATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.pm.ConfigurationInfo config = getDeviceConfigurationInfo();
                    reply.writeNoException();
                    config.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.PROFILE_CONTROL_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String process = data.readString();
                    int userId = data.readInt();
                    boolean start = data.readInt() != 0;
                    int profileType = data.readInt();
                    android.app.ProfilerInfo profilerInfo = (data.readInt() != 0) ? android.app.ProfilerInfo.CREATOR.createFromParcel(data) : null;
                    boolean res = profileControl(process, userId, start, profilerInfo, profileType);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.SHUTDOWN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean res = shutdown(data.readInt());
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.STOP_APP_SWITCHES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    stopAppSwitches();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.RESUME_APP_SWITCHES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    resumeAppSwitches();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.PEEK_SERVICE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.Intent service = android.content.Intent.CREATOR.createFromParcel(data);
                    java.lang.String resolvedType = data.readString();
                    java.lang.String callingPackage = data.readString();
                    android.os.IBinder binder = peekService(service, resolvedType, callingPackage);
                    reply.writeNoException();
                    reply.writeStrongBinder(binder);
                    return true;
                }
            case android.app.IActivityManager.START_BACKUP_AGENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    int backupRestoreMode = data.readInt();
                    int userId = data.readInt();
                    boolean success = bindBackupAgent(packageName, backupRestoreMode, userId);
                    reply.writeNoException();
                    reply.writeInt(success ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.BACKUP_AGENT_CREATED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    android.os.IBinder agent = data.readStrongBinder();
                    backupAgentCreated(packageName, agent);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.UNBIND_BACKUP_AGENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.pm.ApplicationInfo info = android.content.pm.ApplicationInfo.CREATOR.createFromParcel(data);
                    unbindBackupAgent(info);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.ADD_PACKAGE_DEPENDENCY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    addPackageDependency(packageName);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.KILL_APPLICATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String pkg = data.readString();
                    int appId = data.readInt();
                    int userId = data.readInt();
                    java.lang.String reason = data.readString();
                    killApplication(pkg, appId, userId, reason);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.CLOSE_SYSTEM_DIALOGS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String reason = data.readString();
                    closeSystemDialogs(reason);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_PROCESS_MEMORY_INFO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int[] pids = data.createIntArray();
                    android.os.Debug.MemoryInfo[] res = getProcessMemoryInfo(pids);
                    reply.writeNoException();
                    reply.writeTypedArray(res, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    return true;
                }
            case android.app.IActivityManager.KILL_APPLICATION_PROCESS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String processName = data.readString();
                    int uid = data.readInt();
                    killApplicationProcess(processName, uid);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.OVERRIDE_PENDING_TRANSITION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    java.lang.String packageName = data.readString();
                    int enterAnim = data.readInt();
                    int exitAnim = data.readInt();
                    overridePendingTransition(token, packageName, enterAnim, exitAnim);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.IS_USER_A_MONKEY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean areThey = isUserAMonkey();
                    reply.writeNoException();
                    reply.writeInt(areThey ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.SET_USER_IS_MONKEY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final boolean monkey = data.readInt() == 1;
                    setUserIsMonkey(monkey);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.FINISH_HEAVY_WEIGHT_APP_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    finishHeavyWeightApp();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.IS_IMMERSIVE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean isit = isImmersive(token);
                    reply.writeNoException();
                    reply.writeInt(isit ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.IS_TOP_OF_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    final boolean isTopOfTask = isTopOfTask(token);
                    reply.writeNoException();
                    reply.writeInt(isTopOfTask ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.CONVERT_FROM_TRANSLUCENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean converted = convertFromTranslucent(token);
                    reply.writeNoException();
                    reply.writeInt(converted ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.CONVERT_TO_TRANSLUCENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    final android.os.Bundle bundle;
                    if (data.readInt() == 0) {
                        bundle = null;
                    } else {
                        bundle = data.readBundle();
                    }
                    final android.app.ActivityOptions options = android.app.ActivityOptions.fromBundle(bundle);
                    boolean converted = convertToTranslucent(token, options);
                    reply.writeNoException();
                    reply.writeInt(converted ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.GET_ACTIVITY_OPTIONS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    final android.app.ActivityOptions options = getActivityOptions(token);
                    reply.writeNoException();
                    reply.writeBundle(options == null ? null : options.toBundle());
                    return true;
                }
            case android.app.IActivityManager.SET_IMMERSIVE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean imm = data.readInt() == 1;
                    setImmersive(token, imm);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.IS_TOP_ACTIVITY_IMMERSIVE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean isit = isTopActivityImmersive();
                    reply.writeNoException();
                    reply.writeInt(isit ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.CRASH_APPLICATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int uid = data.readInt();
                    int initialPid = data.readInt();
                    java.lang.String packageName = data.readString();
                    java.lang.String message = data.readString();
                    crashApplication(uid, initialPid, packageName, message);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_PROVIDER_MIME_TYPE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int userId = data.readInt();
                    java.lang.String type = getProviderMimeType(uri, userId);
                    reply.writeNoException();
                    reply.writeString(type);
                    return true;
                }
            case android.app.IActivityManager.NEW_URI_PERMISSION_OWNER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String name = data.readString();
                    android.os.IBinder perm = newUriPermissionOwner(name);
                    reply.writeNoException();
                    reply.writeStrongBinder(perm);
                    return true;
                }
            case android.app.IActivityManager.GET_URI_PERMISSION_OWNER_FOR_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder activityToken = data.readStrongBinder();
                    android.os.IBinder perm = getUriPermissionOwnerForActivity(activityToken);
                    reply.writeNoException();
                    reply.writeStrongBinder(perm);
                    return true;
                }
            case android.app.IActivityManager.GRANT_URI_PERMISSION_FROM_OWNER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder owner = data.readStrongBinder();
                    int fromUid = data.readInt();
                    java.lang.String targetPkg = data.readString();
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int mode = data.readInt();
                    int sourceUserId = data.readInt();
                    int targetUserId = data.readInt();
                    grantUriPermissionFromOwner(owner, fromUid, targetPkg, uri, mode, sourceUserId, targetUserId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REVOKE_URI_PERMISSION_FROM_OWNER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder owner = data.readStrongBinder();
                    android.net.Uri uri = null;
                    if (data.readInt() != 0) {
                        uri = android.net.Uri.CREATOR.createFromParcel(data);
                    }
                    int mode = data.readInt();
                    int userId = data.readInt();
                    revokeUriPermissionFromOwner(owner, uri, mode, userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.CHECK_GRANT_URI_PERMISSION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int callingUid = data.readInt();
                    java.lang.String targetPkg = data.readString();
                    android.net.Uri uri = android.net.Uri.CREATOR.createFromParcel(data);
                    int modeFlags = data.readInt();
                    int userId = data.readInt();
                    int res = checkGrantUriPermission(callingUid, targetPkg, uri, modeFlags, userId);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.DUMP_HEAP_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String process = data.readString();
                    int userId = data.readInt();
                    boolean managed = data.readInt() != 0;
                    java.lang.String path = data.readString();
                    android.os.ParcelFileDescriptor fd = (data.readInt() != 0) ? android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    boolean res = dumpHeap(process, userId, managed, path, fd);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.START_ACTIVITIES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder b = data.readStrongBinder();
                    android.app.IApplicationThread app = android.app.ApplicationThreadNative.asInterface(b);
                    java.lang.String callingPackage = data.readString();
                    android.content.Intent[] intents = data.createTypedArray(android.content.Intent.CREATOR);
                    java.lang.String[] resolvedTypes = data.createStringArray();
                    android.os.IBinder resultTo = data.readStrongBinder();
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int userId = data.readInt();
                    int result = startActivities(app, callingPackage, intents, resolvedTypes, resultTo, options, userId);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.GET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int mode = getFrontActivityScreenCompatMode();
                    reply.writeNoException();
                    reply.writeInt(mode);
                    return true;
                }
            case android.app.IActivityManager.SET_FRONT_ACTIVITY_SCREEN_COMPAT_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int mode = data.readInt();
                    setFrontActivityScreenCompatMode(mode);
                    reply.writeNoException();
                    reply.writeInt(mode);
                    return true;
                }
            case android.app.IActivityManager.GET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String pkg = data.readString();
                    int mode = getPackageScreenCompatMode(pkg);
                    reply.writeNoException();
                    reply.writeInt(mode);
                    return true;
                }
            case android.app.IActivityManager.SET_PACKAGE_SCREEN_COMPAT_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String pkg = data.readString();
                    int mode = data.readInt();
                    setPackageScreenCompatMode(pkg, mode);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SWITCH_USER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int userid = data.readInt();
                    boolean result = switchUser(userid);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.START_USER_IN_BACKGROUND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int userid = data.readInt();
                    boolean result = startUserInBackground(userid);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.UNLOCK_USER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int userId = data.readInt();
                    byte[] token = data.createByteArray();
                    byte[] secret = data.createByteArray();
                    android.os.IProgressListener listener = IProgressListener.Stub.asInterface(data.readStrongBinder());
                    boolean result = unlockUser(userId, token, secret, listener);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.STOP_USER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int userid = data.readInt();
                    boolean force = data.readInt() != 0;
                    android.app.IStopUserCallback callback = IStopUserCallback.Stub.asInterface(data.readStrongBinder());
                    int result = stopUser(userid, force, callback);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.GET_CURRENT_USER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.pm.UserInfo userInfo = getCurrentUser();
                    reply.writeNoException();
                    userInfo.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.IS_USER_RUNNING_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int userid = data.readInt();
                    int _flags = data.readInt();
                    boolean result = isUserRunning(userid, _flags);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.GET_RUNNING_USER_IDS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int[] result = getRunningUserIds();
                    reply.writeNoException();
                    reply.writeIntArray(result);
                    return true;
                }
            case android.app.IActivityManager.REMOVE_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    boolean result = removeTask(taskId);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.REGISTER_PROCESS_OBSERVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IProcessObserver observer = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                    registerProcessObserver(observer);
                    return true;
                }
            case android.app.IActivityManager.UNREGISTER_PROCESS_OBSERVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IProcessObserver observer = IProcessObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterProcessObserver(observer);
                    return true;
                }
            case android.app.IActivityManager.REGISTER_UID_OBSERVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IUidObserver observer = IUidObserver.Stub.asInterface(data.readStrongBinder());
                    int which = data.readInt();
                    registerUidObserver(observer, which);
                    return true;
                }
            case android.app.IActivityManager.UNREGISTER_UID_OBSERVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IUidObserver observer = IUidObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterUidObserver(observer);
                    return true;
                }
            case android.app.IActivityManager.GET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String pkg = data.readString();
                    boolean ask = getPackageAskScreenCompat(pkg);
                    reply.writeNoException();
                    reply.writeInt(ask ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.SET_PACKAGE_ASK_SCREEN_COMPAT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String pkg = data.readString();
                    boolean ask = data.readInt() != 0;
                    setPackageAskScreenCompat(pkg, ask);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.IS_INTENT_SENDER_TARGETED_TO_PACKAGE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean res = isIntentSenderTargetedToPackage(r);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.IS_INTENT_SENDER_AN_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    boolean res = isIntentSenderAnActivity(r);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.GET_INTENT_FOR_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    android.content.Intent intent = getIntentForIntentSender(r);
                    reply.writeNoException();
                    if (intent != null) {
                        reply.writeInt(1);
                        intent.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.GET_TAG_FOR_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender r = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    java.lang.String prefix = data.readString();
                    java.lang.String tag = getTagForIntentSender(r, prefix);
                    reply.writeNoException();
                    reply.writeString(tag);
                    return true;
                }
            case android.app.IActivityManager.UPDATE_PERSISTENT_CONFIGURATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.res.Configuration config = android.content.res.Configuration.CREATOR.createFromParcel(data);
                    updatePersistentConfiguration(config);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_PROCESS_PSS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int[] pids = data.createIntArray();
                    long[] pss = getProcessPss(pids);
                    reply.writeNoException();
                    reply.writeLongArray(pss);
                    return true;
                }
            case android.app.IActivityManager.SHOW_BOOT_MESSAGE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.CharSequence msg = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(data);
                    boolean always = data.readInt() != 0;
                    showBootMessage(msg, always);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.KEYGUARD_WAITING_FOR_ACTIVITY_DRAWN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    keyguardWaitingForActivityDrawn();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.KEYGUARD_GOING_AWAY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    keyguardGoingAway(data.readInt());
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SHOULD_UP_RECREATE_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    java.lang.String destAffinity = data.readString();
                    boolean res = shouldUpRecreateTask(token, destAffinity);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.NAVIGATE_UP_TO_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.content.Intent target = android.content.Intent.CREATOR.createFromParcel(data);
                    int resultCode = data.readInt();
                    android.content.Intent resultData = null;
                    if (data.readInt() != 0) {
                        resultData = android.content.Intent.CREATOR.createFromParcel(data);
                    }
                    boolean res = navigateUpTo(token, target, resultCode, resultData);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.GET_LAUNCHED_FROM_UID_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    int res = getLaunchedFromUid(token);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.GET_LAUNCHED_FROM_PACKAGE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    java.lang.String res = getLaunchedFromPackage(token);
                    reply.writeNoException();
                    reply.writeString(res);
                    return true;
                }
            case android.app.IActivityManager.REGISTER_USER_SWITCH_OBSERVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IUserSwitchObserver observer = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                    java.lang.String name = data.readString();
                    registerUserSwitchObserver(observer, name);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.UNREGISTER_USER_SWITCH_OBSERVER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IUserSwitchObserver observer = IUserSwitchObserver.Stub.asInterface(data.readStrongBinder());
                    unregisterUserSwitchObserver(observer);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REQUEST_BUG_REPORT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int bugreportType = data.readInt();
                    requestBugReport(bugreportType);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.INPUT_DISPATCHING_TIMED_OUT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int pid = data.readInt();
                    boolean aboveSystem = data.readInt() != 0;
                    java.lang.String reason = data.readString();
                    long res = inputDispatchingTimedOut(pid, aboveSystem, reason);
                    reply.writeNoException();
                    reply.writeLong(res);
                    return true;
                }
            case android.app.IActivityManager.GET_ASSIST_CONTEXT_EXTRAS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int requestType = data.readInt();
                    android.os.Bundle res = getAssistContextExtras(requestType);
                    reply.writeNoException();
                    reply.writeBundle(res);
                    return true;
                }
            case android.app.IActivityManager.REQUEST_ASSIST_CONTEXT_EXTRAS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int requestType = data.readInt();
                    com.android.internal.os.IResultReceiver receiver = IResultReceiver.Stub.asInterface(data.readStrongBinder());
                    android.os.Bundle receiverExtras = data.readBundle();
                    android.os.IBinder activityToken = data.readStrongBinder();
                    boolean focused = data.readInt() == 1;
                    boolean newSessionId = data.readInt() == 1;
                    boolean res = requestAssistContextExtras(requestType, receiver, receiverExtras, activityToken, focused, newSessionId);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.REPORT_ASSIST_CONTEXT_EXTRAS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.os.Bundle extras = data.readBundle();
                    android.app.assist.AssistStructure structure = android.app.assist.AssistStructure.CREATOR.createFromParcel(data);
                    android.app.assist.AssistContent content = android.app.assist.AssistContent.CREATOR.createFromParcel(data);
                    android.net.Uri referrer = (data.readInt() != 0) ? android.net.Uri.CREATOR.createFromParcel(data) : null;
                    reportAssistContextExtras(token, extras, structure, content, referrer);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.LAUNCH_ASSIST_INTENT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    int requestType = data.readInt();
                    java.lang.String hint = data.readString();
                    int userHandle = data.readInt();
                    android.os.Bundle args = data.readBundle();
                    boolean res = launchAssistIntent(intent, requestType, hint, userHandle, args);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.IS_SCREEN_CAPTURE_ALLOWED_ON_CURRENT_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean res = isAssistDataAllowedOnCurrentActivity();
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.SHOW_ASSIST_FROM_ACTIVITY_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.os.Bundle args = data.readBundle();
                    boolean res = showAssistFromActivity(token, args);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.KILL_UID_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int appId = data.readInt();
                    int userId = data.readInt();
                    java.lang.String reason = data.readString();
                    killUid(appId, userId, reason);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.HANG_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder who = data.readStrongBinder();
                    boolean allowRestart = data.readInt() != 0;
                    hang(who, allowRestart);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REPORT_ACTIVITY_FULLY_DRAWN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    reportActivityFullyDrawn(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTIFY_ACTIVITY_DRAWN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    notifyActivityDrawn(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.RESTART_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    restart();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.PERFORM_IDLE_MAINTENANCE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    performIdleMaintenance();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.CREATE_VIRTUAL_ACTIVITY_CONTAINER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder parentActivityToken = data.readStrongBinder();
                    android.app.IActivityContainerCallback callback = IActivityContainerCallback.Stub.asInterface(data.readStrongBinder());
                    android.app.IActivityContainer activityContainer = createVirtualActivityContainer(parentActivityToken, callback);
                    reply.writeNoException();
                    if (activityContainer != null) {
                        reply.writeInt(1);
                        reply.writeStrongBinder(activityContainer.asBinder());
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.DELETE_ACTIVITY_CONTAINER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.app.IActivityContainer activityContainer = IActivityContainer.Stub.asInterface(data.readStrongBinder());
                    deleteActivityContainer(activityContainer);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.CREATE_STACK_ON_DISPLAY :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int displayId = data.readInt();
                    android.app.IActivityContainer activityContainer = createStackOnDisplay(displayId);
                    reply.writeNoException();
                    if (activityContainer != null) {
                        reply.writeInt(1);
                        reply.writeStrongBinder(activityContainer.asBinder());
                    } else {
                        reply.writeInt(0);
                    }
                    return true;
                }
            case android.app.IActivityManager.GET_ACTIVITY_DISPLAY_ID_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder activityToken = data.readStrongBinder();
                    int displayId = getActivityDisplayId(activityToken);
                    reply.writeNoException();
                    reply.writeInt(displayId);
                    return true;
                }
            case android.app.IActivityManager.START_LOCK_TASK_BY_TASK_ID_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int taskId = data.readInt();
                    startLockTaskMode(taskId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.START_LOCK_TASK_BY_TOKEN_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    startLockTaskMode(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.START_SYSTEM_LOCK_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    startSystemLockTaskMode(taskId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.STOP_LOCK_TASK_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    stopLockTaskMode();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.STOP_SYSTEM_LOCK_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    stopSystemLockTaskMode();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.IS_IN_LOCK_TASK_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final boolean isInLockTaskMode = isInLockTaskMode();
                    reply.writeNoException();
                    reply.writeInt(isInLockTaskMode ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.GET_LOCK_TASK_MODE_STATE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int lockTaskModeState = getLockTaskModeState();
                    reply.writeNoException();
                    reply.writeInt(lockTaskModeState);
                    return true;
                }
            case android.app.IActivityManager.SHOW_LOCK_TASK_ESCAPE_MESSAGE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.os.IBinder token = data.readStrongBinder();
                    showLockTaskEscapeMessage(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_TASK_DESCRIPTION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    android.app.ActivityManager.TaskDescription values = android.app.ActivityManager.TaskDescription.CREATOR.createFromParcel(data);
                    setTaskDescription(token, values);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_TASK_RESIZEABLE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int taskId = data.readInt();
                    final int resizeableMode = data.readInt();
                    setTaskResizeable(taskId, resizeableMode);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.RESIZE_TASK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    int resizeMode = data.readInt();
                    android.graphics.Rect r = android.graphics.Rect.CREATOR.createFromParcel(data);
                    resizeTask(taskId, r, resizeMode);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_TASK_BOUNDS_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int taskId = data.readInt();
                    android.graphics.Rect r = getTaskBounds(taskId);
                    reply.writeNoException();
                    r.writeToParcel(reply, 0);
                    return true;
                }
            case android.app.IActivityManager.GET_TASK_DESCRIPTION_ICON_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String filename = data.readString();
                    int userId = data.readInt();
                    android.graphics.Bitmap icon = getTaskDescriptionIcon(filename, userId);
                    reply.writeNoException();
                    if (icon == null) {
                        reply.writeInt(0);
                    } else {
                        reply.writeInt(1);
                        icon.writeToParcel(reply, 0);
                    }
                    return true;
                }
            case android.app.IActivityManager.START_IN_PLACE_ANIMATION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.os.Bundle bundle;
                    if (data.readInt() == 0) {
                        bundle = null;
                    } else {
                        bundle = data.readBundle();
                    }
                    final android.app.ActivityOptions options = android.app.ActivityOptions.fromBundle(bundle);
                    startInPlaceAnimationOnFrontMostApplication(options);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REQUEST_VISIBLE_BEHIND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean enable = data.readInt() > 0;
                    boolean success = requestVisibleBehind(token, enable);
                    reply.writeNoException();
                    reply.writeInt(success ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.IS_BACKGROUND_VISIBLE_BEHIND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    final boolean enabled = isBackgroundVisibleBehind(token);
                    reply.writeNoException();
                    reply.writeInt(enabled ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.BACKGROUND_RESOURCES_RELEASED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    backgroundResourcesReleased(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTIFY_LAUNCH_TASK_BEHIND_COMPLETE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    notifyLaunchTaskBehindComplete(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTIFY_ENTER_ANIMATION_COMPLETE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    notifyEnterAnimationComplete(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.BOOT_ANIMATION_COMPLETE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    bootAnimationComplete();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTIFY_CLEARTEXT_NETWORK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int uid = data.readInt();
                    final byte[] firstPacket = data.createByteArray();
                    notifyCleartextNetwork(uid, firstPacket);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_DUMP_HEAP_DEBUG_LIMIT_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String procName = data.readString();
                    int uid = data.readInt();
                    long maxMemSize = data.readLong();
                    java.lang.String reportPackage = data.readString();
                    setDumpHeapDebugLimit(procName, uid, maxMemSize, reportPackage);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.DUMP_HEAP_FINISHED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String path = data.readString();
                    dumpHeapFinished(path);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_VOICE_KEEP_AWAKE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.service.voice.IVoiceInteractionSession session = IVoiceInteractionSession.Stub.asInterface(data.readStrongBinder());
                    boolean keepAwake = data.readInt() != 0;
                    setVoiceKeepAwake(session, keepAwake);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.UPDATE_LOCK_TASK_PACKAGES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    int userId = data.readInt();
                    java.lang.String[] packages = data.readStringArray();
                    updateLockTaskPackages(userId, packages);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.UPDATE_DEVICE_OWNER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String packageName = data.readString();
                    updateDeviceOwner(packageName);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_PACKAGE_PROCESS_STATE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String pkg = data.readString();
                    java.lang.String callingPackage = data.readString();
                    int res = getPackageProcessState(pkg, callingPackage);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.SET_PROCESS_MEMORY_TRIM_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    java.lang.String process = data.readString();
                    int userId = data.readInt();
                    int level = data.readInt();
                    boolean res = setProcessMemoryTrimLevel(process, userId, level);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.IS_ROOT_VOICE_INTERACTION_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    boolean res = isRootVoiceInteraction(token);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.START_BINDER_TRACKING_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    boolean res = startBinderTracking();
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.STOP_BINDER_TRACKING_AND_DUMP_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.ParcelFileDescriptor fd = (data.readInt() != 0) ? android.os.ParcelFileDescriptor.CREATOR.createFromParcel(data) : null;
                    boolean res = stopBinderTrackingAndDump(fd);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.GET_ACTIVITY_STACK_ID_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    int stackId = getActivityStackId(token);
                    reply.writeNoException();
                    reply.writeInt(stackId);
                    return true;
                }
            case android.app.IActivityManager.EXIT_FREEFORM_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    exitFreeformMode(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REPORT_SIZE_CONFIGURATIONS :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.os.IBinder token = data.readStrongBinder();
                    int[] horizontal = readIntArray(data);
                    int[] vertical = readIntArray(data);
                    int[] smallest = readIntArray(data);
                    reportSizeConfigurations(token, horizontal, vertical, smallest);
                    return true;
                }
            case android.app.IActivityManager.SUPPRESS_RESIZE_CONFIG_CHANGES_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final boolean suppress = data.readInt() == 1;
                    suppressResizeConfigChanges(suppress);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.MOVE_TASKS_TO_FULLSCREEN_STACK_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int stackId = data.readInt();
                    final boolean onTop = data.readInt() == 1;
                    moveTasksToFullscreenStack(stackId, onTop);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.GET_APP_START_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int uid = data.readInt();
                    final java.lang.String pkg = data.readString();
                    int res = getAppStartMode(uid, pkg);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.IN_MULTI_WINDOW_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.os.IBinder token = data.readStrongBinder();
                    final boolean inMultiWindow = isInMultiWindowMode(token);
                    reply.writeNoException();
                    reply.writeInt(inMultiWindow ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.IN_PICTURE_IN_PICTURE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.os.IBinder token = data.readStrongBinder();
                    final boolean inPip = isInPictureInPictureMode(token);
                    reply.writeNoException();
                    reply.writeInt(inPip ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.ENTER_PICTURE_IN_PICTURE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.os.IBinder token = data.readStrongBinder();
                    enterPictureInPictureMode(token);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_VR_MODE_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.os.IBinder token = data.readStrongBinder();
                    final boolean enable = data.readInt() == 1;
                    final android.content.ComponentName packageName = android.content.ComponentName.CREATOR.createFromParcel(data);
                    int res = setVrMode(token, enable, packageName);
                    reply.writeNoException();
                    reply.writeInt(res);
                    return true;
                }
            case android.app.IActivityManager.IS_VR_PACKAGE_ENABLED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.content.ComponentName packageName = android.content.ComponentName.CREATOR.createFromParcel(data);
                    boolean res = isVrModePackageEnabled(packageName);
                    reply.writeNoException();
                    reply.writeInt(res ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.IS_APP_FOREGROUND_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int userHandle = data.readInt();
                    final boolean isForeground = isAppForeground(userHandle);
                    reply.writeNoException();
                    reply.writeInt(isForeground ? 1 : 0);
                    return true;
                }
            case android.app.IActivityManager.NOTIFY_PINNED_STACK_ANIMATION_ENDED_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.REMOVE_STACK :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int stackId = data.readInt();
                    removeStack(stackId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.NOTIFY_LOCKED_PROFILE :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int userId = data.readInt();
                    notifyLockedProfile(userId);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.START_CONFIRM_DEVICE_CREDENTIAL_INTENT :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.content.Intent intent = android.content.Intent.CREATOR.createFromParcel(data);
                    startConfirmDeviceCredentialIntent(intent);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SEND_IDLE_JOB_TRIGGER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    sendIdleJobTrigger();
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SEND_INTENT_SENDER_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    android.content.IIntentSender sender = IIntentSender.Stub.asInterface(data.readStrongBinder());
                    int scode = data.readInt();
                    android.content.Intent intent = (data.readInt() != 0) ? android.content.Intent.CREATOR.createFromParcel(data) : null;
                    java.lang.String resolvedType = data.readString();
                    android.content.IIntentReceiver finishedReceiver = IIntentReceiver.Stub.asInterface(data.readStrongBinder());
                    java.lang.String requiredPermission = data.readString();
                    android.os.Bundle options = (data.readInt() != 0) ? android.os.Bundle.CREATOR.createFromParcel(data) : null;
                    int result = sendIntentSender(sender, scode, intent, resolvedType, finishedReceiver, requiredPermission, options);
                    reply.writeNoException();
                    reply.writeInt(result);
                    return true;
                }
            case android.app.IActivityManager.SET_VR_THREAD_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int tid = data.readInt();
                    setVrThread(tid);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_RENDER_THREAD_TRANSACTION :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final int tid = data.readInt();
                    setRenderThread(tid);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.SET_HAS_TOP_UI :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final boolean hasTopUi = data.readInt() != 0;
                    setHasTopUi(hasTopUi);
                    reply.writeNoException();
                    return true;
                }
            case android.app.IActivityManager.CAN_BYPASS_WORK_CHALLENGE :
                {
                    data.enforceInterface(android.app.IActivityManager.descriptor);
                    final android.app.PendingIntent intent = android.app.PendingIntent.CREATOR.createFromParcel(data);
                    final boolean result = canBypassWorkChallenge(intent);
                    reply.writeNoException();
                    reply.writeInt(result ? 1 : 0);
                    return true;
                }
        }
        return super.onTransact(code, data, reply, flags);
    }

    private int[] readIntArray(android.os.Parcel data) {
        int[] smallest = null;
        int smallestSize = data.readInt();
        if (smallestSize > 0) {
            smallest = new int[smallestSize];
            data.readIntArray(smallest);
        }
        return smallest;
    }

    public android.os.IBinder asBinder() {
        return this;
    }

    private static final android.util.Singleton<android.app.IActivityManager> gDefault = new android.util.Singleton<android.app.IActivityManager>() {
        protected android.app.IActivityManager create() {
            android.os.IBinder b = android.os.ServiceManager.getService("activity");
            if (false) {
                android.util.Log.v("ActivityManager", "default service binder = " + b);
            }
            android.app.IActivityManager am = android.app.ActivityManagerNative.asInterface(b);
            if (false) {
                android.util.Log.v("ActivityManager", "default service = " + am);
            }
            return am;
        }
    };
}

