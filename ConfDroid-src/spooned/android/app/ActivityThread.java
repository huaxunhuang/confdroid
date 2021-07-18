package android.app;


/**
 * This manages the execution of the main thread in an
 * application process, scheduling and executing activities,
 * broadcasts, and other operations on it as the activity
 * manager requests.
 *
 * {@hide }
 */
public final class ActivityThread {
    /**
     *
     *
     * @unknown 
     */
    public static final java.lang.String TAG = "ActivityThread";

    private static final android.graphics.Bitmap.Config THUMBNAIL_FORMAT = android.graphics.Bitmap.Config.RGB_565;

    static final boolean localLOGV = false;

    static final boolean DEBUG_MESSAGES = false;

    /**
     *
     *
     * @unknown 
     */
    public static final boolean DEBUG_BROADCAST = false;

    private static final boolean DEBUG_RESULTS = false;

    private static final boolean DEBUG_BACKUP = false;

    public static final boolean DEBUG_CONFIGURATION = false;

    private static final boolean DEBUG_SERVICE = false;

    private static final boolean DEBUG_MEMORY_TRIM = false;

    private static final boolean DEBUG_PROVIDER = false;

    private static final boolean DEBUG_ORDER = false;

    private static final long MIN_TIME_BETWEEN_GCS = 5 * 1000;

    private static final int SQLITE_MEM_RELEASED_EVENT_LOG_TAG = 75003;

    private static final int LOG_AM_ON_PAUSE_CALLED = 30021;

    private static final int LOG_AM_ON_RESUME_CALLED = 30022;

    private static final int LOG_AM_ON_STOP_CALLED = 30049;

    /**
     * Type for IActivityManager.serviceDoneExecuting: anonymous operation
     */
    public static final int SERVICE_DONE_EXECUTING_ANON = 0;

    /**
     * Type for IActivityManager.serviceDoneExecuting: done with an onStart call
     */
    public static final int SERVICE_DONE_EXECUTING_START = 1;

    /**
     * Type for IActivityManager.serviceDoneExecuting: done stopping (destroying) service
     */
    public static final int SERVICE_DONE_EXECUTING_STOP = 2;

    // Details for pausing activity.
    private static final int USER_LEAVING = 1;

    private static final int DONT_REPORT = 2;

    // Whether to invoke an activity callback after delivering new configuration.
    private static final boolean REPORT_TO_ACTIVITY = true;

    private android.app.ContextImpl mSystemContext;

    static volatile android.content.pm.IPackageManager sPackageManager;

    final android.app.ActivityThread.ApplicationThread mAppThread = new android.app.ActivityThread.ApplicationThread();

    final android.os.Looper mLooper = android.os.Looper.myLooper();

    final android.app.ActivityThread.H mH = new android.app.ActivityThread.H();

    final android.util.ArrayMap<android.os.IBinder, android.app.ActivityThread.ActivityClientRecord> mActivities = new android.util.ArrayMap<>();

    // List of new activities (via ActivityRecord.nextIdle) that should
    // be reported when next we idle.
    android.app.ActivityThread.ActivityClientRecord mNewActivities = null;

    // Number of activities that are currently visible on-screen.
    int mNumVisibleActivities = 0;

    java.util.ArrayList<java.lang.ref.WeakReference<android.app.assist.AssistStructure>> mLastAssistStructures = new java.util.ArrayList<>();

    private int mLastSessionId;

    final android.util.ArrayMap<android.os.IBinder, android.app.Service> mServices = new android.util.ArrayMap<>();

    android.app.ActivityThread.AppBindData mBoundApplication;

    android.app.ActivityThread.Profiler mProfiler;

    int mCurDefaultDisplayDpi;

    boolean mDensityCompatMode;

    android.content.res.Configuration mConfiguration;

    android.content.res.Configuration mCompatConfiguration;

    android.app.Application mInitialApplication;

    final java.util.ArrayList<android.app.Application> mAllApplications = new java.util.ArrayList<android.app.Application>();

    // set of instantiated backup agents, keyed by package name
    final android.util.ArrayMap<java.lang.String, android.app.backup.BackupAgent> mBackupAgents = new android.util.ArrayMap<java.lang.String, android.app.backup.BackupAgent>();

    /**
     * Reference to singleton {@link ActivityThread}
     */
    private static volatile android.app.ActivityThread sCurrentActivityThread;

    android.app.Instrumentation mInstrumentation;

    java.lang.String mInstrumentationPackageName = null;

    java.lang.String mInstrumentationAppDir = null;

    java.lang.String[] mInstrumentationSplitAppDirs = null;

    java.lang.String mInstrumentationLibDir = null;

    java.lang.String mInstrumentedAppDir = null;

    java.lang.String[] mInstrumentedSplitAppDirs = null;

    java.lang.String mInstrumentedLibDir = null;

    boolean mSystemThread = false;

    boolean mJitEnabled = false;

    boolean mSomeActivitiesChanged = false;

    boolean mUpdatingSystemConfig = false;

    // These can be accessed by multiple threads; mPackages is the lock.
    // XXX For now we keep around information about all packages we have
    // seen, not removing entries from this map.
    // NOTE: The activity and window managers need to call in to
    // ActivityThread to do things like update resource configurations,
    // which means this lock gets held while the activity and window managers
    // holds their own lock.  Thus you MUST NEVER call back into the activity manager
    // or window manager or anything that depends on them while holding this lock.
    // These LoadedApk are only valid for the userId that we're running as.
    final android.util.ArrayMap<java.lang.String, java.lang.ref.WeakReference<android.app.LoadedApk>> mPackages = new android.util.ArrayMap<java.lang.String, java.lang.ref.WeakReference<android.app.LoadedApk>>();

    final android.util.ArrayMap<java.lang.String, java.lang.ref.WeakReference<android.app.LoadedApk>> mResourcePackages = new android.util.ArrayMap<java.lang.String, java.lang.ref.WeakReference<android.app.LoadedApk>>();

    final java.util.ArrayList<android.app.ActivityThread.ActivityClientRecord> mRelaunchingActivities = new java.util.ArrayList<android.app.ActivityThread.ActivityClientRecord>();

    android.content.res.Configuration mPendingConfiguration = null;

    // Because we merge activity relaunch operations we can't depend on the ordering provided by
    // the handler messages. We need to introduce secondary ordering mechanism, which will allow
    // us to drop certain events, if we know that they happened before relaunch we already executed.
    // This represents the order of receiving the request from AM.
    @com.android.internal.annotations.GuardedBy("mResourcesManager")
    int mLifecycleSeq = 0;

    private final android.app.ResourcesManager mResourcesManager;

    private static final class ProviderKey {
        final java.lang.String authority;

        final int userId;

        public ProviderKey(java.lang.String authority, int userId) {
            this.authority = authority;
            this.userId = userId;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object o) {
            if (o instanceof android.app.ActivityThread.ProviderKey) {
                final android.app.ActivityThread.ProviderKey other = ((android.app.ActivityThread.ProviderKey) (o));
                return java.util.Objects.equals(authority, other.authority) && (userId == other.userId);
            }
            return false;
        }

        @java.lang.Override
        public int hashCode() {
            return (authority != null ? authority.hashCode() : 0) ^ userId;
        }
    }

    // The lock of mProviderMap protects the following variables.
    final android.util.ArrayMap<android.app.ActivityThread.ProviderKey, android.app.ActivityThread.ProviderClientRecord> mProviderMap = new android.util.ArrayMap<android.app.ActivityThread.ProviderKey, android.app.ActivityThread.ProviderClientRecord>();

    final android.util.ArrayMap<android.os.IBinder, android.app.ActivityThread.ProviderRefCount> mProviderRefCountMap = new android.util.ArrayMap<android.os.IBinder, android.app.ActivityThread.ProviderRefCount>();

    final android.util.ArrayMap<android.os.IBinder, android.app.ActivityThread.ProviderClientRecord> mLocalProviders = new android.util.ArrayMap<android.os.IBinder, android.app.ActivityThread.ProviderClientRecord>();

    final android.util.ArrayMap<android.content.ComponentName, android.app.ActivityThread.ProviderClientRecord> mLocalProvidersByName = new android.util.ArrayMap<android.content.ComponentName, android.app.ActivityThread.ProviderClientRecord>();

    final android.util.ArrayMap<android.app.Activity, java.util.ArrayList<android.app.OnActivityPausedListener>> mOnPauseListeners = new android.util.ArrayMap<android.app.Activity, java.util.ArrayList<android.app.OnActivityPausedListener>>();

    final android.app.ActivityThread.GcIdler mGcIdler = new android.app.ActivityThread.GcIdler();

    boolean mGcIdlerScheduled = false;

    static volatile android.os.Handler sMainThreadHandler;// set once in main()


    android.os.Bundle mCoreSettings = null;

    static final class ActivityClientRecord {
        android.os.IBinder token;

        int ident;

        android.content.Intent intent;

        java.lang.String referrer;

        com.android.internal.app.IVoiceInteractor voiceInteractor;

        android.os.Bundle state;

        android.os.PersistableBundle persistentState;

        android.app.Activity activity;

        android.view.Window window;

        android.app.Activity parent;

        java.lang.String embeddedID;

        android.app.Activity.NonConfigurationInstances lastNonConfigurationInstances;

        boolean paused;

        boolean stopped;

        boolean hideForNow;

        android.content.res.Configuration newConfig;

        android.content.res.Configuration createdConfig;

        android.content.res.Configuration overrideConfig;

        // Used for consolidating configs before sending on to Activity.
        private android.content.res.Configuration tmpConfig = new android.content.res.Configuration();

        android.app.ActivityThread.ActivityClientRecord nextIdle;

        android.app.ProfilerInfo profilerInfo;

        android.content.pm.ActivityInfo activityInfo;

        android.content.res.CompatibilityInfo compatInfo;

        android.app.LoadedApk packageInfo;

        java.util.List<android.app.ResultInfo> pendingResults;

        java.util.List<com.android.internal.content.ReferrerIntent> pendingIntents;

        boolean startsNotResumed;

        boolean isForward;

        int pendingConfigChanges;

        boolean onlyLocalRequest;

        android.view.Window mPendingRemoveWindow;

        android.view.WindowManager mPendingRemoveWindowManager;

        boolean mPreserveWindow;

        // Set for relaunch requests, indicates the order number of the relaunch operation, so it
        // can be compared with other lifecycle operations.
        int relaunchSeq = 0;

        // Can only be accessed from the UI thread. This represents the latest processed message
        // that is related to lifecycle events/
        int lastProcessedSeq = 0;

        ActivityClientRecord() {
            parent = null;
            embeddedID = null;
            paused = false;
            stopped = false;
            hideForNow = false;
            nextIdle = null;
        }

        public boolean isPreHoneycomb() {
            if (activity != null) {
                return activity.getApplicationInfo().targetSdkVersion < android.os.Build.VERSION_CODES.HONEYCOMB;
            }
            return false;
        }

        public boolean isPersistable() {
            return activityInfo.persistableMode == android.content.pm.ActivityInfo.PERSIST_ACROSS_REBOOTS;
        }

        public java.lang.String toString() {
            android.content.ComponentName componentName = (intent != null) ? intent.getComponent() : null;
            return ((((("ActivityRecord{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " token=") + token) + " ") + (componentName == null ? "no component name" : componentName.toShortString())) + "}";
        }

        public java.lang.String getStateString() {
            java.lang.StringBuilder sb = new java.lang.StringBuilder();
            sb.append("ActivityClientRecord{");
            sb.append("paused=").append(paused);
            sb.append(", stopped=").append(stopped);
            sb.append(", hideForNow=").append(hideForNow);
            sb.append(", startsNotResumed=").append(startsNotResumed);
            sb.append(", isForward=").append(isForward);
            sb.append(", pendingConfigChanges=").append(pendingConfigChanges);
            sb.append(", onlyLocalRequest=").append(onlyLocalRequest);
            sb.append(", preserveWindow=").append(mPreserveWindow);
            if (activity != null) {
                sb.append(", Activity{");
                sb.append("resumed=").append(activity.mResumed);
                sb.append(", stopped=").append(activity.mStopped);
                sb.append(", finished=").append(activity.isFinishing());
                sb.append(", destroyed=").append(activity.isDestroyed());
                sb.append(", startedActivity=").append(activity.mStartedActivity);
                sb.append(", temporaryPause=").append(activity.mTemporaryPause);
                sb.append(", changingConfigurations=").append(activity.mChangingConfigurations);
                sb.append(", visibleBehind=").append(activity.mVisibleBehind);
                sb.append("}");
            }
            sb.append("}");
            return sb.toString();
        }
    }

    final class ProviderClientRecord {
        final java.lang.String[] mNames;

        final android.content.IContentProvider mProvider;

        final android.content.ContentProvider mLocalProvider;

        final android.app.IActivityManager.ContentProviderHolder mHolder;

        ProviderClientRecord(java.lang.String[] names, android.content.IContentProvider provider, android.content.ContentProvider localProvider, android.app.IActivityManager.ContentProviderHolder holder) {
            mNames = names;
            mProvider = provider;
            mLocalProvider = localProvider;
            mHolder = holder;
        }
    }

    static final class NewIntentData {
        java.util.List<com.android.internal.content.ReferrerIntent> intents;

        android.os.IBinder token;

        boolean andPause;

        public java.lang.String toString() {
            return ((((("NewIntentData{intents=" + intents) + " token=") + token) + " andPause=") + andPause) + "}";
        }
    }

    static final class ReceiverData extends android.content.BroadcastReceiver.PendingResult {
        public ReceiverData(android.content.Intent intent, int resultCode, java.lang.String resultData, android.os.Bundle resultExtras, boolean ordered, boolean sticky, android.os.IBinder token, int sendingUser) {
            super(resultCode, resultData, resultExtras, android.content.BroadcastReceiver.PendingResult.TYPE_COMPONENT, ordered, sticky, token, sendingUser, intent.getFlags());
            this.intent = intent;
        }

        android.content.Intent intent;

        android.content.pm.ActivityInfo info;

        android.content.res.CompatibilityInfo compatInfo;

        public java.lang.String toString() {
            return ((((((((("ReceiverData{intent=" + intent) + " packageName=") + info.packageName) + " resultCode=") + getResultCode()) + " resultData=") + getResultData()) + " resultExtras=") + getResultExtras(false)) + "}";
        }
    }

    static final class CreateBackupAgentData {
        android.content.pm.ApplicationInfo appInfo;

        android.content.res.CompatibilityInfo compatInfo;

        int backupMode;

        public java.lang.String toString() {
            return ((((("CreateBackupAgentData{appInfo=" + appInfo) + " backupAgent=") + appInfo.backupAgentName) + " mode=") + backupMode) + "}";
        }
    }

    static final class CreateServiceData {
        android.os.IBinder token;

        android.content.pm.ServiceInfo info;

        android.content.res.CompatibilityInfo compatInfo;

        android.content.Intent intent;

        public java.lang.String toString() {
            return ((((((("CreateServiceData{token=" + token) + " className=") + info.name) + " packageName=") + info.packageName) + " intent=") + intent) + "}";
        }
    }

    static final class BindServiceData {
        android.os.IBinder token;

        android.content.Intent intent;

        boolean rebind;

        public java.lang.String toString() {
            return ((("BindServiceData{token=" + token) + " intent=") + intent) + "}";
        }
    }

    static final class ServiceArgsData {
        android.os.IBinder token;

        boolean taskRemoved;

        int startId;

        int flags;

        android.content.Intent args;

        public java.lang.String toString() {
            return ((((("ServiceArgsData{token=" + token) + " startId=") + startId) + " args=") + args) + "}";
        }
    }

    static final class AppBindData {
        android.app.LoadedApk info;

        java.lang.String processName;

        android.content.pm.ApplicationInfo appInfo;

        java.util.List<android.content.pm.ProviderInfo> providers;

        android.content.ComponentName instrumentationName;

        android.os.Bundle instrumentationArgs;

        android.app.IInstrumentationWatcher instrumentationWatcher;

        android.app.IUiAutomationConnection instrumentationUiAutomationConnection;

        int debugMode;

        boolean enableBinderTracking;

        boolean trackAllocation;

        boolean restrictedBackupMode;

        boolean persistent;

        android.content.res.Configuration config;

        android.content.res.CompatibilityInfo compatInfo;

        /**
         * Initial values for {@link Profiler}.
         */
        android.app.ProfilerInfo initProfilerInfo;

        public java.lang.String toString() {
            return ("AppBindData{appInfo=" + appInfo) + "}";
        }
    }

    static final class Profiler {
        java.lang.String profileFile;

        android.os.ParcelFileDescriptor profileFd;

        int samplingInterval;

        boolean autoStopProfiler;

        boolean profiling;

        boolean handlingProfiling;

        public void setProfiler(android.app.ProfilerInfo profilerInfo) {
            android.os.ParcelFileDescriptor fd = profilerInfo.profileFd;
            if (profiling) {
                if (fd != null) {
                    try {
                        fd.close();
                    } catch (java.io.IOException e) {
                        // Ignore
                    }
                }
                return;
            }
            if (profileFd != null) {
                try {
                    profileFd.close();
                } catch (java.io.IOException e) {
                    // Ignore
                }
            }
            profileFile = profilerInfo.profileFile;
            profileFd = fd;
            samplingInterval = profilerInfo.samplingInterval;
            autoStopProfiler = profilerInfo.autoStopProfiler;
        }

        public void startProfiling() {
            if ((profileFd == null) || profiling) {
                return;
            }
            try {
                int bufferSize = android.os.SystemProperties.getInt("debug.traceview-buffer-size-mb", 8);
                dalvik.system.VMDebug.startMethodTracing(profileFile, profileFd.getFileDescriptor(), (bufferSize * 1024) * 1024, 0, samplingInterval != 0, samplingInterval);
                profiling = true;
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, "Profiling failed on path " + profileFile);
                try {
                    profileFd.close();
                    profileFd = null;
                } catch (java.io.IOException e2) {
                    android.util.Slog.w(android.app.ActivityThread.TAG, "Failure closing profile fd", e2);
                }
            }
        }

        public void stopProfiling() {
            if (profiling) {
                profiling = false;
                android.os.Debug.stopMethodTracing();
                if (profileFd != null) {
                    try {
                        profileFd.close();
                    } catch (java.io.IOException e) {
                    }
                }
                profileFd = null;
                profileFile = null;
            }
        }
    }

    static final class DumpComponentInfo {
        android.os.ParcelFileDescriptor fd;

        android.os.IBinder token;

        java.lang.String prefix;

        java.lang.String[] args;
    }

    static final class ResultData {
        android.os.IBinder token;

        java.util.List<android.app.ResultInfo> results;

        public java.lang.String toString() {
            return ((("ResultData{token=" + token) + " results") + results) + "}";
        }
    }

    static final class ContextCleanupInfo {
        android.app.ContextImpl context;

        java.lang.String what;

        java.lang.String who;
    }

    static final class DumpHeapData {
        java.lang.String path;

        android.os.ParcelFileDescriptor fd;
    }

    static final class UpdateCompatibilityData {
        java.lang.String pkg;

        android.content.res.CompatibilityInfo info;
    }

    static final class RequestAssistContextExtras {
        android.os.IBinder activityToken;

        android.os.IBinder requestToken;

        int requestType;

        int sessionId;
    }

    static final class ActivityConfigChangeData {
        final android.os.IBinder activityToken;

        final android.content.res.Configuration overrideConfig;

        public ActivityConfigChangeData(android.os.IBinder token, android.content.res.Configuration config) {
            activityToken = token;
            overrideConfig = config;
        }
    }

    private native void dumpGraphicsInfo(java.io.FileDescriptor fd);

    private class ApplicationThread extends android.app.ApplicationThreadNative {
        private static final java.lang.String DB_INFO_FORMAT = "  %8s %8s %14s %14s  %s";

        private int mLastProcessState = -1;

        private void updatePendingConfiguration(android.content.res.Configuration config) {
            synchronized(mResourcesManager) {
                if ((mPendingConfiguration == null) || mPendingConfiguration.isOtherSeqNewer(config)) {
                    mPendingConfiguration = config;
                }
            }
        }

        public final void schedulePauseActivity(android.os.IBinder token, boolean finished, boolean userLeaving, int configChanges, boolean dontReport) {
            int seq = getLifecycleSeq();
            if (android.app.ActivityThread.DEBUG_ORDER)
                android.util.Slog.d(android.app.ActivityThread.TAG, (("pauseActivity " + android.app.ActivityThread.this) + " operation received seq: ") + seq);

            sendMessage(finished ? android.app.ActivityThread.H.PAUSE_ACTIVITY_FINISHING : android.app.ActivityThread.H.PAUSE_ACTIVITY, token, (userLeaving ? android.app.ActivityThread.USER_LEAVING : 0) | (dontReport ? android.app.ActivityThread.DONT_REPORT : 0), configChanges, seq);
        }

        public final void scheduleStopActivity(android.os.IBinder token, boolean showWindow, int configChanges) {
            int seq = getLifecycleSeq();
            if (android.app.ActivityThread.DEBUG_ORDER)
                android.util.Slog.d(android.app.ActivityThread.TAG, (("stopActivity " + android.app.ActivityThread.this) + " operation received seq: ") + seq);

            sendMessage(showWindow ? android.app.ActivityThread.H.STOP_ACTIVITY_SHOW : android.app.ActivityThread.H.STOP_ACTIVITY_HIDE, token, 0, configChanges, seq);
        }

        public final void scheduleWindowVisibility(android.os.IBinder token, boolean showWindow) {
            sendMessage(showWindow ? android.app.ActivityThread.H.SHOW_WINDOW : android.app.ActivityThread.H.HIDE_WINDOW, token);
        }

        public final void scheduleSleeping(android.os.IBinder token, boolean sleeping) {
            sendMessage(android.app.ActivityThread.H.SLEEPING, token, sleeping ? 1 : 0);
        }

        public final void scheduleResumeActivity(android.os.IBinder token, int processState, boolean isForward, android.os.Bundle resumeArgs) {
            int seq = getLifecycleSeq();
            if (android.app.ActivityThread.DEBUG_ORDER)
                android.util.Slog.d(android.app.ActivityThread.TAG, (("resumeActivity " + android.app.ActivityThread.this) + " operation received seq: ") + seq);

            updateProcessState(processState, false);
            sendMessage(android.app.ActivityThread.H.RESUME_ACTIVITY, token, isForward ? 1 : 0, 0, seq);
        }

        public final void scheduleSendResult(android.os.IBinder token, java.util.List<android.app.ResultInfo> results) {
            android.app.ActivityThread.ResultData res = new android.app.ActivityThread.ResultData();
            res.token = token;
            res.results = results;
            sendMessage(android.app.ActivityThread.H.SEND_RESULT, res);
        }

        // we use token to identify this activity without having to send the
        // activity itself back to the activity manager. (matters more with ipc)
        @java.lang.Override
        public final void scheduleLaunchActivity(android.content.Intent intent, android.os.IBinder token, int ident, android.content.pm.ActivityInfo info, android.content.res.Configuration curConfig, android.content.res.Configuration overrideConfig, android.content.res.CompatibilityInfo compatInfo, java.lang.String referrer, com.android.internal.app.IVoiceInteractor voiceInteractor, int procState, android.os.Bundle state, android.os.PersistableBundle persistentState, java.util.List<android.app.ResultInfo> pendingResults, java.util.List<com.android.internal.content.ReferrerIntent> pendingNewIntents, boolean notResumed, boolean isForward, android.app.ProfilerInfo profilerInfo) {
            updateProcessState(procState, false);
            android.app.ActivityThread.ActivityClientRecord r = new android.app.ActivityThread.ActivityClientRecord();
            r.token = token;
            r.ident = ident;
            r.intent = intent;
            r.referrer = referrer;
            r.voiceInteractor = voiceInteractor;
            r.activityInfo = info;
            r.compatInfo = compatInfo;
            r.state = state;
            r.persistentState = persistentState;
            r.pendingResults = pendingResults;
            r.pendingIntents = pendingNewIntents;
            r.startsNotResumed = notResumed;
            r.isForward = isForward;
            r.profilerInfo = profilerInfo;
            r.overrideConfig = overrideConfig;
            updatePendingConfiguration(curConfig);
            sendMessage(android.app.ActivityThread.H.LAUNCH_ACTIVITY, r);
        }

        @java.lang.Override
        public final void scheduleRelaunchActivity(android.os.IBinder token, java.util.List<android.app.ResultInfo> pendingResults, java.util.List<com.android.internal.content.ReferrerIntent> pendingNewIntents, int configChanges, boolean notResumed, android.content.res.Configuration config, android.content.res.Configuration overrideConfig, boolean preserveWindow) {
            requestRelaunchActivity(token, pendingResults, pendingNewIntents, configChanges, notResumed, config, overrideConfig, true, preserveWindow);
        }

        public final void scheduleNewIntent(java.util.List<com.android.internal.content.ReferrerIntent> intents, android.os.IBinder token, boolean andPause) {
            android.app.ActivityThread.NewIntentData data = new android.app.ActivityThread.NewIntentData();
            data.intents = intents;
            data.token = token;
            data.andPause = andPause;
            sendMessage(android.app.ActivityThread.H.NEW_INTENT, data);
        }

        public final void scheduleDestroyActivity(android.os.IBinder token, boolean finishing, int configChanges) {
            sendMessage(android.app.ActivityThread.H.DESTROY_ACTIVITY, token, finishing ? 1 : 0, configChanges);
        }

        public final void scheduleReceiver(android.content.Intent intent, android.content.pm.ActivityInfo info, android.content.res.CompatibilityInfo compatInfo, int resultCode, java.lang.String data, android.os.Bundle extras, boolean sync, int sendingUser, int processState) {
            updateProcessState(processState, false);
            android.app.ActivityThread.ReceiverData r = new android.app.ActivityThread.ReceiverData(intent, resultCode, data, extras, sync, false, mAppThread.asBinder(), sendingUser);
            r.info = info;
            r.compatInfo = compatInfo;
            sendMessage(android.app.ActivityThread.H.RECEIVER, r);
        }

        public final void scheduleCreateBackupAgent(android.content.pm.ApplicationInfo app, android.content.res.CompatibilityInfo compatInfo, int backupMode) {
            android.app.ActivityThread.CreateBackupAgentData d = new android.app.ActivityThread.CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            d.backupMode = backupMode;
            sendMessage(android.app.ActivityThread.H.CREATE_BACKUP_AGENT, d);
        }

        public final void scheduleDestroyBackupAgent(android.content.pm.ApplicationInfo app, android.content.res.CompatibilityInfo compatInfo) {
            android.app.ActivityThread.CreateBackupAgentData d = new android.app.ActivityThread.CreateBackupAgentData();
            d.appInfo = app;
            d.compatInfo = compatInfo;
            sendMessage(android.app.ActivityThread.H.DESTROY_BACKUP_AGENT, d);
        }

        public final void scheduleCreateService(android.os.IBinder token, android.content.pm.ServiceInfo info, android.content.res.CompatibilityInfo compatInfo, int processState) {
            updateProcessState(processState, false);
            android.app.ActivityThread.CreateServiceData s = new android.app.ActivityThread.CreateServiceData();
            s.token = token;
            s.info = info;
            s.compatInfo = compatInfo;
            sendMessage(android.app.ActivityThread.H.CREATE_SERVICE, s);
        }

        public final void scheduleBindService(android.os.IBinder token, android.content.Intent intent, boolean rebind, int processState) {
            updateProcessState(processState, false);
            android.app.ActivityThread.BindServiceData s = new android.app.ActivityThread.BindServiceData();
            s.token = token;
            s.intent = intent;
            s.rebind = rebind;
            if (android.app.ActivityThread.DEBUG_SERVICE)
                android.util.Slog.v(android.app.ActivityThread.TAG, (((((("scheduleBindService token=" + token) + " intent=") + intent) + " uid=") + android.os.Binder.getCallingUid()) + " pid=") + android.os.Binder.getCallingPid());

            sendMessage(android.app.ActivityThread.H.BIND_SERVICE, s);
        }

        public final void scheduleUnbindService(android.os.IBinder token, android.content.Intent intent) {
            android.app.ActivityThread.BindServiceData s = new android.app.ActivityThread.BindServiceData();
            s.token = token;
            s.intent = intent;
            sendMessage(android.app.ActivityThread.H.UNBIND_SERVICE, s);
        }

        public final void scheduleServiceArgs(android.os.IBinder token, boolean taskRemoved, int startId, int flags, android.content.Intent args) {
            android.app.ActivityThread.ServiceArgsData s = new android.app.ActivityThread.ServiceArgsData();
            s.token = token;
            s.taskRemoved = taskRemoved;
            s.startId = startId;
            s.flags = flags;
            s.args = args;
            sendMessage(android.app.ActivityThread.H.SERVICE_ARGS, s);
        }

        public final void scheduleStopService(android.os.IBinder token) {
            sendMessage(android.app.ActivityThread.H.STOP_SERVICE, token);
        }

        public final void bindApplication(java.lang.String processName, android.content.pm.ApplicationInfo appInfo, java.util.List<android.content.pm.ProviderInfo> providers, android.content.ComponentName instrumentationName, android.app.ProfilerInfo profilerInfo, android.os.Bundle instrumentationArgs, android.app.IInstrumentationWatcher instrumentationWatcher, android.app.IUiAutomationConnection instrumentationUiConnection, int debugMode, boolean enableBinderTracking, boolean trackAllocation, boolean isRestrictedBackupMode, boolean persistent, android.content.res.Configuration config, android.content.res.CompatibilityInfo compatInfo, java.util.Map<java.lang.String, android.os.IBinder> services, android.os.Bundle coreSettings) {
            if (services != null) {
                // Setup the service cache in the ServiceManager
                android.os.ServiceManager.initServiceCache(services);
            }
            setCoreSettings(coreSettings);
            android.app.ActivityThread.AppBindData data = new android.app.ActivityThread.AppBindData();
            data.processName = processName;
            data.appInfo = appInfo;
            data.providers = providers;
            data.instrumentationName = instrumentationName;
            data.instrumentationArgs = instrumentationArgs;
            data.instrumentationWatcher = instrumentationWatcher;
            data.instrumentationUiAutomationConnection = instrumentationUiConnection;
            data.debugMode = debugMode;
            data.enableBinderTracking = enableBinderTracking;
            data.trackAllocation = trackAllocation;
            data.restrictedBackupMode = isRestrictedBackupMode;
            data.persistent = persistent;
            data.config = config;
            data.compatInfo = compatInfo;
            data.initProfilerInfo = profilerInfo;
            sendMessage(android.app.ActivityThread.H.BIND_APPLICATION, data);
        }

        public final void scheduleExit() {
            sendMessage(android.app.ActivityThread.H.EXIT_APPLICATION, null);
        }

        public final void scheduleSuicide() {
            sendMessage(android.app.ActivityThread.H.SUICIDE, null);
        }

        public void scheduleConfigurationChanged(android.content.res.Configuration config) {
            updatePendingConfiguration(config);
            sendMessage(android.app.ActivityThread.H.CONFIGURATION_CHANGED, config);
        }

        public void updateTimeZone() {
            java.util.TimeZone.setDefault(null);
        }

        public void clearDnsCache() {
            // a non-standard API to get this to libcore
            clearDnsCache();
            // Allow libcore to perform the necessary actions as it sees fit upon a network
            // configuration change.
            libcore.net.event.NetworkEventDispatcher.getInstance().onNetworkConfigurationChanged();
        }

        public void setHttpProxy(java.lang.String host, java.lang.String port, java.lang.String exclList, android.net.Uri pacFileUrl) {
            final android.net.ConnectivityManager cm = android.net.ConnectivityManager.from(getSystemContext());
            final android.net.Network network = cm.getBoundNetworkForProcess();
            if (network != null) {
                android.net.Proxy.setHttpProxySystemProperty(cm.getDefaultProxy());
            } else {
                android.net.Proxy.setHttpProxySystemProperty(host, port, exclList, pacFileUrl);
            }
        }

        public void processInBackground() {
            mH.removeMessages(android.app.ActivityThread.H.GC_WHEN_IDLE);
            mH.sendMessage(mH.obtainMessage(android.app.ActivityThread.H.GC_WHEN_IDLE));
        }

        public void dumpService(java.io.FileDescriptor fd, android.os.IBinder servicetoken, java.lang.String[] args) {
            android.app.ActivityThread.DumpComponentInfo data = new android.app.ActivityThread.DumpComponentInfo();
            try {
                data.fd = android.os.ParcelFileDescriptor.dup(fd);
                data.token = servicetoken;
                data.args = args;
                /* async */
                sendMessage(android.app.ActivityThread.H.DUMP_SERVICE, data, 0, 0, true);
            } catch (java.io.IOException e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, "dumpService failed", e);
            }
        }

        // This function exists to make sure all receiver dispatching is
        // correctly ordered, since these are one-way calls and the binder driver
        // applies transaction ordering per object for such calls.
        public void scheduleRegisteredReceiver(android.content.IIntentReceiver receiver, android.content.Intent intent, int resultCode, java.lang.String dataStr, android.os.Bundle extras, boolean ordered, boolean sticky, int sendingUser, int processState) throws android.os.RemoteException {
            updateProcessState(processState, false);
            receiver.performReceive(intent, resultCode, dataStr, extras, ordered, sticky, sendingUser);
        }

        @java.lang.Override
        public void scheduleLowMemory() {
            sendMessage(android.app.ActivityThread.H.LOW_MEMORY, null);
        }

        @java.lang.Override
        public void scheduleActivityConfigurationChanged(android.os.IBinder token, android.content.res.Configuration overrideConfig, boolean reportToActivity) {
            sendMessage(android.app.ActivityThread.H.ACTIVITY_CONFIGURATION_CHANGED, new android.app.ActivityThread.ActivityConfigChangeData(token, overrideConfig), reportToActivity ? 1 : 0);
        }

        @java.lang.Override
        public void profilerControl(boolean start, android.app.ProfilerInfo profilerInfo, int profileType) {
            sendMessage(android.app.ActivityThread.H.PROFILER_CONTROL, profilerInfo, start ? 1 : 0, profileType);
        }

        public void dumpHeap(boolean managed, java.lang.String path, android.os.ParcelFileDescriptor fd) {
            android.app.ActivityThread.DumpHeapData dhd = new android.app.ActivityThread.DumpHeapData();
            dhd.path = path;
            dhd.fd = fd;
            /* async */
            sendMessage(android.app.ActivityThread.H.DUMP_HEAP, dhd, managed ? 1 : 0, 0, true);
        }

        public void setSchedulingGroup(int group) {
            // Note: do this immediately, since going into the foreground
            // should happen regardless of what pending work we have to do
            // and the activity manager will wait for us to report back that
            // we are done before sending us to the background.
            try {
                android.os.Process.setProcessGroup(android.os.Process.myPid(), group);
            } catch (java.lang.Exception e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, "Failed setting process group to " + group, e);
            }
        }

        public void dispatchPackageBroadcast(int cmd, java.lang.String[] packages) {
            sendMessage(android.app.ActivityThread.H.DISPATCH_PACKAGE_BROADCAST, packages, cmd);
        }

        public void scheduleCrash(java.lang.String msg) {
            sendMessage(android.app.ActivityThread.H.SCHEDULE_CRASH, msg);
        }

        public void dumpActivity(java.io.FileDescriptor fd, android.os.IBinder activitytoken, java.lang.String prefix, java.lang.String[] args) {
            android.app.ActivityThread.DumpComponentInfo data = new android.app.ActivityThread.DumpComponentInfo();
            try {
                data.fd = android.os.ParcelFileDescriptor.dup(fd);
                data.token = activitytoken;
                data.prefix = prefix;
                data.args = args;
                /* async */
                sendMessage(android.app.ActivityThread.H.DUMP_ACTIVITY, data, 0, 0, true);
            } catch (java.io.IOException e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, "dumpActivity failed", e);
            }
        }

        public void dumpProvider(java.io.FileDescriptor fd, android.os.IBinder providertoken, java.lang.String[] args) {
            android.app.ActivityThread.DumpComponentInfo data = new android.app.ActivityThread.DumpComponentInfo();
            try {
                data.fd = android.os.ParcelFileDescriptor.dup(fd);
                data.token = providertoken;
                data.args = args;
                /* async */
                sendMessage(android.app.ActivityThread.H.DUMP_PROVIDER, data, 0, 0, true);
            } catch (java.io.IOException e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, "dumpProvider failed", e);
            }
        }

        @java.lang.Override
        public void dumpMemInfo(java.io.FileDescriptor fd, android.os.Debug.MemoryInfo mem, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable, java.lang.String[] args) {
            java.io.FileOutputStream fout = new java.io.FileOutputStream(fd);
            java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(fout);
            try {
                dumpMemInfo(pw, mem, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, dumpUnreachable);
            } finally {
                pw.flush();
            }
        }

        private void dumpMemInfo(java.io.PrintWriter pw, android.os.Debug.MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, boolean dumpUnreachable) {
            long nativeMax = android.os.Debug.getNativeHeapSize() / 1024;
            long nativeAllocated = android.os.Debug.getNativeHeapAllocatedSize() / 1024;
            long nativeFree = android.os.Debug.getNativeHeapFreeSize() / 1024;
            java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
            runtime.gc();// Do GC since countInstancesOfClass counts unreachable objects.

            long dalvikMax = runtime.totalMemory() / 1024;
            long dalvikFree = runtime.freeMemory() / 1024;
            long dalvikAllocated = dalvikMax - dalvikFree;
            java.lang.Class[] classesToCount = new java.lang.Class[]{ android.app.ContextImpl.class, android.app.Activity.class, android.webkit.WebView.class, com.android.org.conscrypt.OpenSSLSocketImpl.class };
            long[] instanceCounts = dalvik.system.VMDebug.countInstancesOfClasses(classesToCount, true);
            long appContextInstanceCount = instanceCounts[0];
            long activityInstanceCount = instanceCounts[1];
            long webviewInstanceCount = instanceCounts[2];
            long openSslSocketCount = instanceCounts[3];
            long viewInstanceCount = android.view.ViewDebug.getViewInstanceCount();
            long viewRootInstanceCount = android.view.ViewDebug.getViewRootImplCount();
            int globalAssetCount = android.content.res.AssetManager.getGlobalAssetCount();
            int globalAssetManagerCount = android.content.res.AssetManager.getGlobalAssetManagerCount();
            int binderLocalObjectCount = android.os.Debug.getBinderLocalObjectCount();
            int binderProxyObjectCount = android.os.Debug.getBinderProxyObjectCount();
            int binderDeathObjectCount = android.os.Debug.getBinderDeathObjectCount();
            long parcelSize = android.os.Parcel.getGlobalAllocSize();
            long parcelCount = android.os.Parcel.getGlobalAllocCount();
            android.database.sqlite.SQLiteDebug.PagerStats stats = android.database.sqlite.SQLiteDebug.getDatabaseInfo();
            android.app.ActivityThread.dumpMemInfoTable(pw, memInfo, checkin, dumpFullInfo, dumpDalvik, dumpSummaryOnly, android.os.Process.myPid(), mBoundApplication != null ? mBoundApplication.processName : "unknown", nativeMax, nativeAllocated, nativeFree, dalvikMax, dalvikAllocated, dalvikFree);
            if (checkin) {
                // NOTE: if you change anything significant below, also consider changing
                // ACTIVITY_THREAD_CHECKIN_VERSION.
                // Object counts
                pw.print(viewInstanceCount);
                pw.print(',');
                pw.print(viewRootInstanceCount);
                pw.print(',');
                pw.print(appContextInstanceCount);
                pw.print(',');
                pw.print(activityInstanceCount);
                pw.print(',');
                pw.print(globalAssetCount);
                pw.print(',');
                pw.print(globalAssetManagerCount);
                pw.print(',');
                pw.print(binderLocalObjectCount);
                pw.print(',');
                pw.print(binderProxyObjectCount);
                pw.print(',');
                pw.print(binderDeathObjectCount);
                pw.print(',');
                pw.print(openSslSocketCount);
                pw.print(',');
                // SQL
                pw.print(stats.memoryUsed / 1024);
                pw.print(',');
                pw.print(stats.memoryUsed / 1024);
                pw.print(',');
                pw.print(stats.pageCacheOverflow / 1024);
                pw.print(',');
                pw.print(stats.largestMemAlloc / 1024);
                for (int i = 0; i < stats.dbStats.size(); i++) {
                    android.database.sqlite.SQLiteDebug.DbStats dbStats = stats.dbStats.get(i);
                    pw.print(',');
                    pw.print(dbStats.dbName);
                    pw.print(',');
                    pw.print(dbStats.pageSize);
                    pw.print(',');
                    pw.print(dbStats.dbSize);
                    pw.print(',');
                    pw.print(dbStats.lookaside);
                    pw.print(',');
                    pw.print(dbStats.cache);
                    pw.print(',');
                    pw.print(dbStats.cache);
                }
                pw.println();
                return;
            }
            pw.println(" ");
            pw.println(" Objects");
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "Views:", viewInstanceCount, "ViewRootImpl:", viewRootInstanceCount);
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "AppContexts:", appContextInstanceCount, "Activities:", activityInstanceCount);
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "Assets:", globalAssetCount, "AssetManagers:", globalAssetManagerCount);
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "Local Binders:", binderLocalObjectCount, "Proxy Binders:", binderProxyObjectCount);
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "Parcel memory:", parcelSize / 1024, "Parcel count:", parcelCount);
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "Death Recipients:", binderDeathObjectCount, "OpenSSL Sockets:", openSslSocketCount);
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "WebViews:", webviewInstanceCount);
            // SQLite mem info
            pw.println(" ");
            pw.println(" SQL");
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "MEMORY_USED:", stats.memoryUsed / 1024);
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "PAGECACHE_OVERFLOW:", stats.pageCacheOverflow / 1024, "MALLOC_SIZE:", stats.largestMemAlloc / 1024);
            pw.println(" ");
            int N = stats.dbStats.size();
            if (N > 0) {
                pw.println(" DATABASES");
                android.app.ActivityThread.printRow(pw, "  %8s %8s %14s %14s  %s", "pgsz", "dbsz", "Lookaside(b)", "cache", "Dbname");
                for (int i = 0; i < N; i++) {
                    android.database.sqlite.SQLiteDebug.DbStats dbStats = stats.dbStats.get(i);
                    android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ApplicationThread.DB_INFO_FORMAT, dbStats.pageSize > 0 ? java.lang.String.valueOf(dbStats.pageSize) : " ", dbStats.dbSize > 0 ? java.lang.String.valueOf(dbStats.dbSize) : " ", dbStats.lookaside > 0 ? java.lang.String.valueOf(dbStats.lookaside) : " ", dbStats.cache, dbStats.dbName);
                }
            }
            // Asset details.
            java.lang.String assetAlloc = android.content.res.AssetManager.getAssetAllocations();
            if (assetAlloc != null) {
                pw.println(" ");
                pw.println(" Asset Allocations");
                pw.print(assetAlloc);
            }
            // Unreachable native memory
            if (dumpUnreachable) {
                boolean showContents = ((mBoundApplication != null) && ((mBoundApplication.appInfo.flags & android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0)) || android.os.Build.IS_DEBUGGABLE;
                pw.println(" ");
                pw.println(" Unreachable memory");
                pw.print(android.os.Debug.getUnreachableMemory(100, showContents));
            }
        }

        @java.lang.Override
        public void dumpGfxInfo(java.io.FileDescriptor fd, java.lang.String[] args) {
            dumpGraphicsInfo(fd);
            android.view.WindowManagerGlobal.getInstance().dumpGfxInfo(fd, args);
        }

        private void dumpDatabaseInfo(java.io.FileDescriptor fd, java.lang.String[] args) {
            java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(new java.io.FileOutputStream(fd));
            android.util.PrintWriterPrinter printer = new android.util.PrintWriterPrinter(pw);
            android.database.sqlite.SQLiteDebug.dump(printer, args);
            pw.flush();
        }

        @java.lang.Override
        public void dumpDbInfo(final java.io.FileDescriptor fd, final java.lang.String[] args) {
            if (mSystemThread) {
                // Ensure this invocation is asynchronous to prevent writer waiting if buffer cannot
                // be consumed. But it must duplicate the file descriptor first, since caller might
                // be closing it.
                final android.os.ParcelFileDescriptor dup;
                try {
                    dup = android.os.ParcelFileDescriptor.dup(fd);
                } catch (java.io.IOException e) {
                    android.util.Log.w(android.app.ActivityThread.TAG, "Could not dup FD " + fd.getInt$());
                    return;
                }
                android.os.AsyncTask.THREAD_POOL_EXECUTOR.execute(new java.lang.Runnable() {
                    @java.lang.Override
                    public void run() {
                        try {
                            dumpDatabaseInfo(dup.getFileDescriptor(), args);
                        } finally {
                            libcore.io.IoUtils.closeQuietly(dup);
                        }
                    }
                });
            } else {
                dumpDatabaseInfo(fd, args);
            }
        }

        @java.lang.Override
        public void unstableProviderDied(android.os.IBinder provider) {
            sendMessage(android.app.ActivityThread.H.UNSTABLE_PROVIDER_DIED, provider);
        }

        @java.lang.Override
        public void requestAssistContextExtras(android.os.IBinder activityToken, android.os.IBinder requestToken, int requestType, int sessionId) {
            android.app.ActivityThread.RequestAssistContextExtras cmd = new android.app.ActivityThread.RequestAssistContextExtras();
            cmd.activityToken = activityToken;
            cmd.requestToken = requestToken;
            cmd.requestType = requestType;
            cmd.sessionId = sessionId;
            sendMessage(android.app.ActivityThread.H.REQUEST_ASSIST_CONTEXT_EXTRAS, cmd);
        }

        public void setCoreSettings(android.os.Bundle coreSettings) {
            sendMessage(android.app.ActivityThread.H.SET_CORE_SETTINGS, coreSettings);
        }

        public void updatePackageCompatibilityInfo(java.lang.String pkg, android.content.res.CompatibilityInfo info) {
            android.app.ActivityThread.UpdateCompatibilityData ucd = new android.app.ActivityThread.UpdateCompatibilityData();
            ucd.pkg = pkg;
            ucd.info = info;
            sendMessage(android.app.ActivityThread.H.UPDATE_PACKAGE_COMPATIBILITY_INFO, ucd);
        }

        public void scheduleTrimMemory(int level) {
            sendMessage(android.app.ActivityThread.H.TRIM_MEMORY, null, level);
        }

        public void scheduleTranslucentConversionComplete(android.os.IBinder token, boolean drawComplete) {
            sendMessage(android.app.ActivityThread.H.TRANSLUCENT_CONVERSION_COMPLETE, token, drawComplete ? 1 : 0);
        }

        public void scheduleOnNewActivityOptions(android.os.IBinder token, android.app.ActivityOptions options) {
            sendMessage(android.app.ActivityThread.H.ON_NEW_ACTIVITY_OPTIONS, new android.util.Pair<android.os.IBinder, android.app.ActivityOptions>(token, options));
        }

        public void setProcessState(int state) {
            updateProcessState(state, true);
        }

        public void updateProcessState(int processState, boolean fromIpc) {
            synchronized(this) {
                if (mLastProcessState != processState) {
                    mLastProcessState = processState;
                    // Update Dalvik state based on ActivityManager.PROCESS_STATE_* constants.
                    final int DALVIK_PROCESS_STATE_JANK_PERCEPTIBLE = 0;
                    final int DALVIK_PROCESS_STATE_JANK_IMPERCEPTIBLE = 1;
                    int dalvikProcessState = DALVIK_PROCESS_STATE_JANK_IMPERCEPTIBLE;
                    // TODO: Tune this since things like gmail sync are important background but not jank perceptible.
                    if (processState <= android.app.ActivityManager.PROCESS_STATE_IMPORTANT_FOREGROUND) {
                        dalvikProcessState = DALVIK_PROCESS_STATE_JANK_PERCEPTIBLE;
                    }
                    updateProcessState(dalvikProcessState);
                    if (false) {
                        android.util.Slog.i(android.app.ActivityThread.TAG, ("******************* PROCESS STATE CHANGED TO: " + processState) + (fromIpc ? " (from ipc" : ""));
                    }
                }
            }
        }

        @java.lang.Override
        public void scheduleInstallProvider(android.content.pm.ProviderInfo provider) {
            sendMessage(android.app.ActivityThread.H.INSTALL_PROVIDER, provider);
        }

        @java.lang.Override
        public final void updateTimePrefs(boolean is24Hour) {
            java.text.DateFormat.set24HourTimePref(is24Hour);
        }

        @java.lang.Override
        public void scheduleCancelVisibleBehind(android.os.IBinder token) {
            sendMessage(android.app.ActivityThread.H.CANCEL_VISIBLE_BEHIND, token);
        }

        @java.lang.Override
        public void scheduleBackgroundVisibleBehindChanged(android.os.IBinder token, boolean visible) {
            sendMessage(android.app.ActivityThread.H.BACKGROUND_VISIBLE_BEHIND_CHANGED, token, visible ? 1 : 0);
        }

        @java.lang.Override
        public void scheduleEnterAnimationComplete(android.os.IBinder token) {
            sendMessage(android.app.ActivityThread.H.ENTER_ANIMATION_COMPLETE, token);
        }

        @java.lang.Override
        public void notifyCleartextNetwork(byte[] firstPacket) {
            if (android.os.StrictMode.vmCleartextNetworkEnabled()) {
                android.os.StrictMode.onCleartextNetworkDetected(firstPacket);
            }
        }

        @java.lang.Override
        public void startBinderTracking() {
            sendMessage(android.app.ActivityThread.H.START_BINDER_TRACKING, null);
        }

        @java.lang.Override
        public void stopBinderTrackingAndDump(java.io.FileDescriptor fd) {
            try {
                sendMessage(android.app.ActivityThread.H.STOP_BINDER_TRACKING_AND_DUMP, android.os.ParcelFileDescriptor.dup(fd));
            } catch (java.io.IOException e) {
            }
        }

        @java.lang.Override
        public void scheduleMultiWindowModeChanged(android.os.IBinder token, boolean isInMultiWindowMode) throws android.os.RemoteException {
            sendMessage(android.app.ActivityThread.H.MULTI_WINDOW_MODE_CHANGED, token, isInMultiWindowMode ? 1 : 0);
        }

        @java.lang.Override
        public void schedulePictureInPictureModeChanged(android.os.IBinder token, boolean isInPipMode) throws android.os.RemoteException {
            sendMessage(android.app.ActivityThread.H.PICTURE_IN_PICTURE_MODE_CHANGED, token, isInPipMode ? 1 : 0);
        }

        @java.lang.Override
        public void scheduleLocalVoiceInteractionStarted(android.os.IBinder token, com.android.internal.app.IVoiceInteractor voiceInteractor) throws android.os.RemoteException {
            com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
            args.arg1 = token;
            args.arg2 = voiceInteractor;
            sendMessage(android.app.ActivityThread.H.LOCAL_VOICE_INTERACTION_STARTED, args);
        }
    }

    private int getLifecycleSeq() {
        synchronized(mResourcesManager) {
            return mLifecycleSeq++;
        }
    }

    private class H extends android.os.Handler {
        public static final int LAUNCH_ACTIVITY = 100;

        public static final int PAUSE_ACTIVITY = 101;

        public static final int PAUSE_ACTIVITY_FINISHING = 102;

        public static final int STOP_ACTIVITY_SHOW = 103;

        public static final int STOP_ACTIVITY_HIDE = 104;

        public static final int SHOW_WINDOW = 105;

        public static final int HIDE_WINDOW = 106;

        public static final int RESUME_ACTIVITY = 107;

        public static final int SEND_RESULT = 108;

        public static final int DESTROY_ACTIVITY = 109;

        public static final int BIND_APPLICATION = 110;

        public static final int EXIT_APPLICATION = 111;

        public static final int NEW_INTENT = 112;

        public static final int RECEIVER = 113;

        public static final int CREATE_SERVICE = 114;

        public static final int SERVICE_ARGS = 115;

        public static final int STOP_SERVICE = 116;

        public static final int CONFIGURATION_CHANGED = 118;

        public static final int CLEAN_UP_CONTEXT = 119;

        public static final int GC_WHEN_IDLE = 120;

        public static final int BIND_SERVICE = 121;

        public static final int UNBIND_SERVICE = 122;

        public static final int DUMP_SERVICE = 123;

        public static final int LOW_MEMORY = 124;

        public static final int ACTIVITY_CONFIGURATION_CHANGED = 125;

        public static final int RELAUNCH_ACTIVITY = 126;

        public static final int PROFILER_CONTROL = 127;

        public static final int CREATE_BACKUP_AGENT = 128;

        public static final int DESTROY_BACKUP_AGENT = 129;

        public static final int SUICIDE = 130;

        public static final int REMOVE_PROVIDER = 131;

        public static final int ENABLE_JIT = 132;

        public static final int DISPATCH_PACKAGE_BROADCAST = 133;

        public static final int SCHEDULE_CRASH = 134;

        public static final int DUMP_HEAP = 135;

        public static final int DUMP_ACTIVITY = 136;

        public static final int SLEEPING = 137;

        public static final int SET_CORE_SETTINGS = 138;

        public static final int UPDATE_PACKAGE_COMPATIBILITY_INFO = 139;

        public static final int TRIM_MEMORY = 140;

        public static final int DUMP_PROVIDER = 141;

        public static final int UNSTABLE_PROVIDER_DIED = 142;

        public static final int REQUEST_ASSIST_CONTEXT_EXTRAS = 143;

        public static final int TRANSLUCENT_CONVERSION_COMPLETE = 144;

        public static final int INSTALL_PROVIDER = 145;

        public static final int ON_NEW_ACTIVITY_OPTIONS = 146;

        public static final int CANCEL_VISIBLE_BEHIND = 147;

        public static final int BACKGROUND_VISIBLE_BEHIND_CHANGED = 148;

        public static final int ENTER_ANIMATION_COMPLETE = 149;

        public static final int START_BINDER_TRACKING = 150;

        public static final int STOP_BINDER_TRACKING_AND_DUMP = 151;

        public static final int MULTI_WINDOW_MODE_CHANGED = 152;

        public static final int PICTURE_IN_PICTURE_MODE_CHANGED = 153;

        public static final int LOCAL_VOICE_INTERACTION_STARTED = 154;

        java.lang.String codeToString(int code) {
            if (android.app.ActivityThread.DEBUG_MESSAGES) {
                switch (code) {
                    case android.app.ActivityThread.H.LAUNCH_ACTIVITY :
                        return "LAUNCH_ACTIVITY";
                    case android.app.ActivityThread.H.PAUSE_ACTIVITY :
                        return "PAUSE_ACTIVITY";
                    case android.app.ActivityThread.H.PAUSE_ACTIVITY_FINISHING :
                        return "PAUSE_ACTIVITY_FINISHING";
                    case android.app.ActivityThread.H.STOP_ACTIVITY_SHOW :
                        return "STOP_ACTIVITY_SHOW";
                    case android.app.ActivityThread.H.STOP_ACTIVITY_HIDE :
                        return "STOP_ACTIVITY_HIDE";
                    case android.app.ActivityThread.H.SHOW_WINDOW :
                        return "SHOW_WINDOW";
                    case android.app.ActivityThread.H.HIDE_WINDOW :
                        return "HIDE_WINDOW";
                    case android.app.ActivityThread.H.RESUME_ACTIVITY :
                        return "RESUME_ACTIVITY";
                    case android.app.ActivityThread.H.SEND_RESULT :
                        return "SEND_RESULT";
                    case android.app.ActivityThread.H.DESTROY_ACTIVITY :
                        return "DESTROY_ACTIVITY";
                    case android.app.ActivityThread.H.BIND_APPLICATION :
                        return "BIND_APPLICATION";
                    case android.app.ActivityThread.H.EXIT_APPLICATION :
                        return "EXIT_APPLICATION";
                    case android.app.ActivityThread.H.NEW_INTENT :
                        return "NEW_INTENT";
                    case android.app.ActivityThread.H.RECEIVER :
                        return "RECEIVER";
                    case android.app.ActivityThread.H.CREATE_SERVICE :
                        return "CREATE_SERVICE";
                    case android.app.ActivityThread.H.SERVICE_ARGS :
                        return "SERVICE_ARGS";
                    case android.app.ActivityThread.H.STOP_SERVICE :
                        return "STOP_SERVICE";
                    case android.app.ActivityThread.H.CONFIGURATION_CHANGED :
                        return "CONFIGURATION_CHANGED";
                    case android.app.ActivityThread.H.CLEAN_UP_CONTEXT :
                        return "CLEAN_UP_CONTEXT";
                    case android.app.ActivityThread.H.GC_WHEN_IDLE :
                        return "GC_WHEN_IDLE";
                    case android.app.ActivityThread.H.BIND_SERVICE :
                        return "BIND_SERVICE";
                    case android.app.ActivityThread.H.UNBIND_SERVICE :
                        return "UNBIND_SERVICE";
                    case android.app.ActivityThread.H.DUMP_SERVICE :
                        return "DUMP_SERVICE";
                    case android.app.ActivityThread.H.LOW_MEMORY :
                        return "LOW_MEMORY";
                    case android.app.ActivityThread.H.ACTIVITY_CONFIGURATION_CHANGED :
                        return "ACTIVITY_CONFIGURATION_CHANGED";
                    case android.app.ActivityThread.H.RELAUNCH_ACTIVITY :
                        return "RELAUNCH_ACTIVITY";
                    case android.app.ActivityThread.H.PROFILER_CONTROL :
                        return "PROFILER_CONTROL";
                    case android.app.ActivityThread.H.CREATE_BACKUP_AGENT :
                        return "CREATE_BACKUP_AGENT";
                    case android.app.ActivityThread.H.DESTROY_BACKUP_AGENT :
                        return "DESTROY_BACKUP_AGENT";
                    case android.app.ActivityThread.H.SUICIDE :
                        return "SUICIDE";
                    case android.app.ActivityThread.H.REMOVE_PROVIDER :
                        return "REMOVE_PROVIDER";
                    case android.app.ActivityThread.H.ENABLE_JIT :
                        return "ENABLE_JIT";
                    case android.app.ActivityThread.H.DISPATCH_PACKAGE_BROADCAST :
                        return "DISPATCH_PACKAGE_BROADCAST";
                    case android.app.ActivityThread.H.SCHEDULE_CRASH :
                        return "SCHEDULE_CRASH";
                    case android.app.ActivityThread.H.DUMP_HEAP :
                        return "DUMP_HEAP";
                    case android.app.ActivityThread.H.DUMP_ACTIVITY :
                        return "DUMP_ACTIVITY";
                    case android.app.ActivityThread.H.SLEEPING :
                        return "SLEEPING";
                    case android.app.ActivityThread.H.SET_CORE_SETTINGS :
                        return "SET_CORE_SETTINGS";
                    case android.app.ActivityThread.H.UPDATE_PACKAGE_COMPATIBILITY_INFO :
                        return "UPDATE_PACKAGE_COMPATIBILITY_INFO";
                    case android.app.ActivityThread.H.TRIM_MEMORY :
                        return "TRIM_MEMORY";
                    case android.app.ActivityThread.H.DUMP_PROVIDER :
                        return "DUMP_PROVIDER";
                    case android.app.ActivityThread.H.UNSTABLE_PROVIDER_DIED :
                        return "UNSTABLE_PROVIDER_DIED";
                    case android.app.ActivityThread.H.REQUEST_ASSIST_CONTEXT_EXTRAS :
                        return "REQUEST_ASSIST_CONTEXT_EXTRAS";
                    case android.app.ActivityThread.H.TRANSLUCENT_CONVERSION_COMPLETE :
                        return "TRANSLUCENT_CONVERSION_COMPLETE";
                    case android.app.ActivityThread.H.INSTALL_PROVIDER :
                        return "INSTALL_PROVIDER";
                    case android.app.ActivityThread.H.ON_NEW_ACTIVITY_OPTIONS :
                        return "ON_NEW_ACTIVITY_OPTIONS";
                    case android.app.ActivityThread.H.CANCEL_VISIBLE_BEHIND :
                        return "CANCEL_VISIBLE_BEHIND";
                    case android.app.ActivityThread.H.BACKGROUND_VISIBLE_BEHIND_CHANGED :
                        return "BACKGROUND_VISIBLE_BEHIND_CHANGED";
                    case android.app.ActivityThread.H.ENTER_ANIMATION_COMPLETE :
                        return "ENTER_ANIMATION_COMPLETE";
                    case android.app.ActivityThread.H.MULTI_WINDOW_MODE_CHANGED :
                        return "MULTI_WINDOW_MODE_CHANGED";
                    case android.app.ActivityThread.H.PICTURE_IN_PICTURE_MODE_CHANGED :
                        return "PICTURE_IN_PICTURE_MODE_CHANGED";
                    case android.app.ActivityThread.H.LOCAL_VOICE_INTERACTION_STARTED :
                        return "LOCAL_VOICE_INTERACTION_STARTED";
                }
            }
            return java.lang.Integer.toString(code);
        }

        public void handleMessage(android.os.Message msg) {
            if (android.app.ActivityThread.DEBUG_MESSAGES)
                android.util.Slog.v(android.app.ActivityThread.TAG, ">>> handling: " + codeToString(msg.what));

            switch (msg.what) {
                case android.app.ActivityThread.H.LAUNCH_ACTIVITY :
                    {
                        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStart");
                        final android.app.ActivityThread.ActivityClientRecord r = ((android.app.ActivityThread.ActivityClientRecord) (msg.obj));
                        r.packageInfo = getPackageInfoNoCheck(r.activityInfo.applicationInfo, r.compatInfo);
                        handleLaunchActivity(r, null, "LAUNCH_ACTIVITY");
                        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    }
                    break;
                case android.app.ActivityThread.H.RELAUNCH_ACTIVITY :
                    {
                        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityRestart");
                        android.app.ActivityThread.ActivityClientRecord r = ((android.app.ActivityThread.ActivityClientRecord) (msg.obj));
                        handleRelaunchActivity(r);
                        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    }
                    break;
                case android.app.ActivityThread.H.PAUSE_ACTIVITY :
                    {
                        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityPause");
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        handlePauseActivity(((android.os.IBinder) (args.arg1)), false, (args.argi1 & android.app.ActivityThread.USER_LEAVING) != 0, args.argi2, (args.argi1 & android.app.ActivityThread.DONT_REPORT) != 0, args.argi3);
                        maybeSnapshot();
                        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    }
                    break;
                case android.app.ActivityThread.H.PAUSE_ACTIVITY_FINISHING :
                    {
                        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityPause");
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        handlePauseActivity(((android.os.IBinder) (args.arg1)), true, (args.argi1 & android.app.ActivityThread.USER_LEAVING) != 0, args.argi2, (args.argi1 & android.app.ActivityThread.DONT_REPORT) != 0, args.argi3);
                        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    }
                    break;
                case android.app.ActivityThread.H.STOP_ACTIVITY_SHOW :
                    {
                        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStop");
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        handleStopActivity(((android.os.IBinder) (args.arg1)), true, args.argi2, args.argi3);
                        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    }
                    break;
                case android.app.ActivityThread.H.STOP_ACTIVITY_HIDE :
                    {
                        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityStop");
                        com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                        handleStopActivity(((android.os.IBinder) (args.arg1)), false, args.argi2, args.argi3);
                        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    }
                    break;
                case android.app.ActivityThread.H.SHOW_WINDOW :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityShowWindow");
                    handleWindowVisibility(((android.os.IBinder) (msg.obj)), true);
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.HIDE_WINDOW :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityHideWindow");
                    handleWindowVisibility(((android.os.IBinder) (msg.obj)), false);
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.RESUME_ACTIVITY :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityResume");
                    com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (msg.obj));
                    handleResumeActivity(((android.os.IBinder) (args.arg1)), true, args.argi1 != 0, true, args.argi3, "RESUME_ACTIVITY");
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.SEND_RESULT :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityDeliverResult");
                    handleSendResult(((android.app.ActivityThread.ResultData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.DESTROY_ACTIVITY :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityDestroy");
                    handleDestroyActivity(((android.os.IBinder) (msg.obj)), msg.arg1 != 0, msg.arg2, false);
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.BIND_APPLICATION :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "bindApplication");
                    android.app.ActivityThread.AppBindData data = ((android.app.ActivityThread.AppBindData) (msg.obj));
                    handleBindApplication(data);
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.EXIT_APPLICATION :
                    if (mInitialApplication != null) {
                        mInitialApplication.onTerminate();
                    }
                    android.os.Looper.myLooper().quit();
                    break;
                case android.app.ActivityThread.H.NEW_INTENT :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityNewIntent");
                    handleNewIntent(((android.app.ActivityThread.NewIntentData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.RECEIVER :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "broadcastReceiveComp");
                    handleReceiver(((android.app.ActivityThread.ReceiverData) (msg.obj)));
                    maybeSnapshot();
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.CREATE_SERVICE :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceCreate: " + java.lang.String.valueOf(msg.obj));
                    handleCreateService(((android.app.ActivityThread.CreateServiceData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.BIND_SERVICE :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceBind");
                    handleBindService(((android.app.ActivityThread.BindServiceData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.UNBIND_SERVICE :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceUnbind");
                    handleUnbindService(((android.app.ActivityThread.BindServiceData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.SERVICE_ARGS :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceStart: " + java.lang.String.valueOf(msg.obj));
                    handleServiceArgs(((android.app.ActivityThread.ServiceArgsData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.STOP_SERVICE :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "serviceStop");
                    handleStopService(((android.os.IBinder) (msg.obj)));
                    maybeSnapshot();
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.CONFIGURATION_CHANGED :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "configChanged");
                    mCurDefaultDisplayDpi = ((android.content.res.Configuration) (msg.obj)).densityDpi;
                    mUpdatingSystemConfig = true;
                    handleConfigurationChanged(((android.content.res.Configuration) (msg.obj)), null);
                    mUpdatingSystemConfig = false;
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.CLEAN_UP_CONTEXT :
                    android.app.ActivityThread.ContextCleanupInfo cci = ((android.app.ActivityThread.ContextCleanupInfo) (msg.obj));
                    cci.context.performFinalCleanup(cci.who, cci.what);
                    break;
                case android.app.ActivityThread.H.GC_WHEN_IDLE :
                    scheduleGcIdler();
                    break;
                case android.app.ActivityThread.H.DUMP_SERVICE :
                    handleDumpService(((android.app.ActivityThread.DumpComponentInfo) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.LOW_MEMORY :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "lowMemory");
                    handleLowMemory();
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.ACTIVITY_CONFIGURATION_CHANGED :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "activityConfigChanged");
                    handleActivityConfigurationChanged(((android.app.ActivityThread.ActivityConfigChangeData) (msg.obj)), msg.arg1 == 1 ? android.app.ActivityThread.REPORT_TO_ACTIVITY : !android.app.ActivityThread.REPORT_TO_ACTIVITY);
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.PROFILER_CONTROL :
                    handleProfilerControl(msg.arg1 != 0, ((android.app.ProfilerInfo) (msg.obj)), msg.arg2);
                    break;
                case android.app.ActivityThread.H.CREATE_BACKUP_AGENT :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "backupCreateAgent");
                    handleCreateBackupAgent(((android.app.ActivityThread.CreateBackupAgentData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.DESTROY_BACKUP_AGENT :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "backupDestroyAgent");
                    handleDestroyBackupAgent(((android.app.ActivityThread.CreateBackupAgentData) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.SUICIDE :
                    android.os.Process.killProcess(android.os.Process.myPid());
                    break;
                case android.app.ActivityThread.H.REMOVE_PROVIDER :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "providerRemove");
                    completeRemoveProvider(((android.app.ActivityThread.ProviderRefCount) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.ENABLE_JIT :
                    ensureJitEnabled();
                    break;
                case android.app.ActivityThread.H.DISPATCH_PACKAGE_BROADCAST :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "broadcastPackage");
                    handleDispatchPackageBroadcast(msg.arg1, ((java.lang.String[]) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.SCHEDULE_CRASH :
                    throw new android.app.RemoteServiceException(((java.lang.String) (msg.obj)));
                case android.app.ActivityThread.H.DUMP_HEAP :
                    android.app.ActivityThread.handleDumpHeap(msg.arg1 != 0, ((android.app.ActivityThread.DumpHeapData) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.DUMP_ACTIVITY :
                    handleDumpActivity(((android.app.ActivityThread.DumpComponentInfo) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.DUMP_PROVIDER :
                    handleDumpProvider(((android.app.ActivityThread.DumpComponentInfo) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.SLEEPING :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "sleeping");
                    handleSleeping(((android.os.IBinder) (msg.obj)), msg.arg1 != 0);
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.SET_CORE_SETTINGS :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "setCoreSettings");
                    handleSetCoreSettings(((android.os.Bundle) (msg.obj)));
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.UPDATE_PACKAGE_COMPATIBILITY_INFO :
                    handleUpdatePackageCompatibilityInfo(((android.app.ActivityThread.UpdateCompatibilityData) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.TRIM_MEMORY :
                    android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "trimMemory");
                    handleTrimMemory(msg.arg1);
                    android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                    break;
                case android.app.ActivityThread.H.UNSTABLE_PROVIDER_DIED :
                    handleUnstableProviderDied(((android.os.IBinder) (msg.obj)), false);
                    break;
                case android.app.ActivityThread.H.REQUEST_ASSIST_CONTEXT_EXTRAS :
                    handleRequestAssistContextExtras(((android.app.ActivityThread.RequestAssistContextExtras) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.TRANSLUCENT_CONVERSION_COMPLETE :
                    handleTranslucentConversionComplete(((android.os.IBinder) (msg.obj)), msg.arg1 == 1);
                    break;
                case android.app.ActivityThread.H.INSTALL_PROVIDER :
                    handleInstallProvider(((android.content.pm.ProviderInfo) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.ON_NEW_ACTIVITY_OPTIONS :
                    android.util.Pair<android.os.IBinder, android.app.ActivityOptions> pair = ((android.util.Pair<android.os.IBinder, android.app.ActivityOptions>) (msg.obj));
                    onNewActivityOptions(pair.first, pair.second);
                    break;
                case android.app.ActivityThread.H.CANCEL_VISIBLE_BEHIND :
                    handleCancelVisibleBehind(((android.os.IBinder) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.BACKGROUND_VISIBLE_BEHIND_CHANGED :
                    handleOnBackgroundVisibleBehindChanged(((android.os.IBinder) (msg.obj)), msg.arg1 > 0);
                    break;
                case android.app.ActivityThread.H.ENTER_ANIMATION_COMPLETE :
                    handleEnterAnimationComplete(((android.os.IBinder) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.START_BINDER_TRACKING :
                    handleStartBinderTracking();
                    break;
                case android.app.ActivityThread.H.STOP_BINDER_TRACKING_AND_DUMP :
                    handleStopBinderTrackingAndDump(((android.os.ParcelFileDescriptor) (msg.obj)));
                    break;
                case android.app.ActivityThread.H.MULTI_WINDOW_MODE_CHANGED :
                    handleMultiWindowModeChanged(((android.os.IBinder) (msg.obj)), msg.arg1 == 1);
                    break;
                case android.app.ActivityThread.H.PICTURE_IN_PICTURE_MODE_CHANGED :
                    handlePictureInPictureModeChanged(((android.os.IBinder) (msg.obj)), msg.arg1 == 1);
                    break;
                case android.app.ActivityThread.H.LOCAL_VOICE_INTERACTION_STARTED :
                    handleLocalVoiceInteractionStarted(((android.os.IBinder) (((com.android.internal.os.SomeArgs) (msg.obj)).arg1)), ((com.android.internal.app.IVoiceInteractor) (((com.android.internal.os.SomeArgs) (msg.obj)).arg2)));
                    break;
            }
            java.lang.Object obj = msg.obj;
            if (obj instanceof com.android.internal.os.SomeArgs) {
                ((com.android.internal.os.SomeArgs) (obj)).recycle();
            }
            if (android.app.ActivityThread.DEBUG_MESSAGES)
                android.util.Slog.v(android.app.ActivityThread.TAG, "<<< done: " + codeToString(msg.what));

        }

        private void maybeSnapshot() {
            if ((mBoundApplication != null) && com.android.internal.os.SamplingProfilerIntegration.isEnabled()) {
                // convert the *private* ActivityThread.PackageInfo to *public* known
                // android.content.pm.PackageInfo
                java.lang.String packageName = mBoundApplication.info.mPackageName;
                android.content.pm.PackageInfo packageInfo = null;
                try {
                    android.content.Context context = getSystemContext();
                    if (context == null) {
                        android.util.Log.e(android.app.ActivityThread.TAG, "cannot get a valid context");
                        return;
                    }
                    android.content.pm.PackageManager pm = context.getPackageManager();
                    if (pm == null) {
                        android.util.Log.e(android.app.ActivityThread.TAG, "cannot get a valid PackageManager");
                        return;
                    }
                    packageInfo = pm.getPackageInfo(packageName, android.content.pm.PackageManager.GET_ACTIVITIES);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.e(android.app.ActivityThread.TAG, "cannot get package info for " + packageName, e);
                }
                com.android.internal.os.SamplingProfilerIntegration.writeSnapshot(mBoundApplication.processName, packageInfo);
            }
        }
    }

    private class Idler implements android.os.MessageQueue.IdleHandler {
        @java.lang.Override
        public final boolean queueIdle() {
            android.app.ActivityThread.ActivityClientRecord a = mNewActivities;
            boolean stopProfiling = false;
            if (((mBoundApplication != null) && (mProfiler.profileFd != null)) && mProfiler.autoStopProfiler) {
                stopProfiling = true;
            }
            if (a != null) {
                mNewActivities = null;
                android.app.IActivityManager am = android.app.ActivityManagerNative.getDefault();
                android.app.ActivityThread.ActivityClientRecord prev;
                do {
                    if (android.app.ActivityThread.localLOGV)
                        android.util.Slog.v(android.app.ActivityThread.TAG, (("Reporting idle of " + a) + " finished=") + ((a.activity != null) && a.activity.mFinished));

                    if ((a.activity != null) && (!a.activity.mFinished)) {
                        try {
                            am.activityIdle(a.token, a.createdConfig, stopProfiling);
                            a.createdConfig = null;
                        } catch (android.os.RemoteException ex) {
                            throw ex.rethrowFromSystemServer();
                        }
                    }
                    prev = a;
                    a = a.nextIdle;
                    prev.nextIdle = null;
                } while (a != null );
            }
            if (stopProfiling) {
                mProfiler.stopProfiling();
            }
            ensureJitEnabled();
            return false;
        }
    }

    final class GcIdler implements android.os.MessageQueue.IdleHandler {
        @java.lang.Override
        public final boolean queueIdle() {
            doGcIfNeeded();
            return false;
        }
    }

    public static android.app.ActivityThread currentActivityThread() {
        return android.app.ActivityThread.sCurrentActivityThread;
    }

    public static boolean isSystem() {
        return android.app.ActivityThread.sCurrentActivityThread != null ? android.app.ActivityThread.sCurrentActivityThread.mSystemThread : false;
    }

    public static java.lang.String currentOpPackageName() {
        android.app.ActivityThread am = android.app.ActivityThread.currentActivityThread();
        return (am != null) && (am.getApplication() != null) ? am.getApplication().getOpPackageName() : null;
    }

    public static java.lang.String currentPackageName() {
        android.app.ActivityThread am = android.app.ActivityThread.currentActivityThread();
        return (am != null) && (am.mBoundApplication != null) ? am.mBoundApplication.appInfo.packageName : null;
    }

    public static java.lang.String currentProcessName() {
        android.app.ActivityThread am = android.app.ActivityThread.currentActivityThread();
        return (am != null) && (am.mBoundApplication != null) ? am.mBoundApplication.processName : null;
    }

    public static android.app.Application currentApplication() {
        android.app.ActivityThread am = android.app.ActivityThread.currentActivityThread();
        return am != null ? am.mInitialApplication : null;
    }

    public static android.content.pm.IPackageManager getPackageManager() {
        if (android.app.ActivityThread.sPackageManager != null) {
            // Slog.v("PackageManager", "returning cur default = " + sPackageManager);
            return android.app.ActivityThread.sPackageManager;
        }
        android.os.IBinder b = android.os.ServiceManager.getService("package");
        // Slog.v("PackageManager", "default service binder = " + b);
        android.app.ActivityThread.sPackageManager = IPackageManager.Stub.asInterface(b);
        // Slog.v("PackageManager", "default service = " + sPackageManager);
        return android.app.ActivityThread.sPackageManager;
    }

    private android.content.res.Configuration mMainThreadConfig = new android.content.res.Configuration();

    android.content.res.Configuration applyConfigCompatMainThread(int displayDensity, android.content.res.Configuration config, android.content.res.CompatibilityInfo compat) {
        if (config == null) {
            return null;
        }
        if (!compat.supportsScreen()) {
            mMainThreadConfig.setTo(config);
            config = mMainThreadConfig;
            compat.applyToConfiguration(displayDensity, config);
        }
        return config;
    }

    /**
     * Creates the top level resources for the given package. Will return an existing
     * Resources if one has already been created.
     */
    android.content.res.Resources getTopLevelResources(java.lang.String resDir, java.lang.String[] splitResDirs, java.lang.String[] overlayDirs, java.lang.String[] libDirs, int displayId, android.app.LoadedApk pkgInfo) {
        return mResourcesManager.getResources(null, resDir, splitResDirs, overlayDirs, libDirs, displayId, null, pkgInfo.getCompatibilityInfo(), pkgInfo.getClassLoader());
    }

    final android.os.Handler getHandler() {
        return mH;
    }

    public final android.app.LoadedApk getPackageInfo(java.lang.String packageName, android.content.res.CompatibilityInfo compatInfo, int flags) {
        return getPackageInfo(packageName, compatInfo, flags, android.os.UserHandle.myUserId());
    }

    public final android.app.LoadedApk getPackageInfo(java.lang.String packageName, android.content.res.CompatibilityInfo compatInfo, int flags, int userId) {
        final boolean differentUser = android.os.UserHandle.myUserId() != userId;
        synchronized(mResourcesManager) {
            java.lang.ref.WeakReference<android.app.LoadedApk> ref;
            if (differentUser) {
                // Caching not supported across users
                ref = null;
            } else
                if ((flags & android.content.Context.CONTEXT_INCLUDE_CODE) != 0) {
                    ref = mPackages.get(packageName);
                } else {
                    ref = mResourcePackages.get(packageName);
                }

            android.app.LoadedApk packageInfo = (ref != null) ? ref.get() : null;
            // Slog.i(TAG, "getPackageInfo " + packageName + ": " + packageInfo);
            // if (packageInfo != null) Slog.i(TAG, "isUptoDate " + packageInfo.mResDir
            // + ": " + packageInfo.mResources.getAssets().isUpToDate());
            if ((packageInfo != null) && ((packageInfo.mResources == null) || packageInfo.mResources.getAssets().isUpToDate())) {
                if (packageInfo.isSecurityViolation() && ((flags & android.content.Context.CONTEXT_IGNORE_SECURITY) == 0)) {
                    throw new java.lang.SecurityException((((("Requesting code from " + packageName) + " to be run in process ") + mBoundApplication.processName) + "/") + mBoundApplication.appInfo.uid);
                }
                return packageInfo;
            }
        }
        android.content.pm.ApplicationInfo ai = null;
        try {
            ai = android.app.ActivityThread.getPackageManager().getApplicationInfo(packageName, android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES | android.content.pm.PackageManager.MATCH_DEBUG_TRIAGED_MISSING, userId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        if (ai != null) {
            return getPackageInfo(ai, compatInfo, flags);
        }
        return null;
    }

    public final android.app.LoadedApk getPackageInfo(android.content.pm.ApplicationInfo ai, android.content.res.CompatibilityInfo compatInfo, int flags) {
        boolean includeCode = (flags & android.content.Context.CONTEXT_INCLUDE_CODE) != 0;
        boolean securityViolation = ((includeCode && (ai.uid != 0)) && (ai.uid != android.os.Process.SYSTEM_UID)) && (mBoundApplication != null ? !android.os.UserHandle.isSameApp(ai.uid, mBoundApplication.appInfo.uid) : true);
        boolean registerPackage = includeCode && ((flags & android.content.Context.CONTEXT_REGISTER_PACKAGE) != 0);
        if ((flags & (android.content.Context.CONTEXT_INCLUDE_CODE | android.content.Context.CONTEXT_IGNORE_SECURITY)) == android.content.Context.CONTEXT_INCLUDE_CODE) {
            if (securityViolation) {
                java.lang.String msg = ((("Requesting code from " + ai.packageName) + " (with uid ") + ai.uid) + ")";
                if (mBoundApplication != null) {
                    msg = ((((msg + " to be run in process ") + mBoundApplication.processName) + " (with uid ") + mBoundApplication.appInfo.uid) + ")";
                }
                throw new java.lang.SecurityException(msg);
            }
        }
        return getPackageInfo(ai, compatInfo, null, securityViolation, includeCode, registerPackage);
    }

    public final android.app.LoadedApk getPackageInfoNoCheck(android.content.pm.ApplicationInfo ai, android.content.res.CompatibilityInfo compatInfo) {
        return getPackageInfo(ai, compatInfo, null, false, true, false);
    }

    public final android.app.LoadedApk peekPackageInfo(java.lang.String packageName, boolean includeCode) {
        synchronized(mResourcesManager) {
            java.lang.ref.WeakReference<android.app.LoadedApk> ref;
            if (includeCode) {
                ref = mPackages.get(packageName);
            } else {
                ref = mResourcePackages.get(packageName);
            }
            return ref != null ? ref.get() : null;
        }
    }

    private android.app.LoadedApk getPackageInfo(android.content.pm.ApplicationInfo aInfo, android.content.res.CompatibilityInfo compatInfo, java.lang.ClassLoader baseLoader, boolean securityViolation, boolean includeCode, boolean registerPackage) {
        final boolean differentUser = android.os.UserHandle.myUserId() != android.os.UserHandle.getUserId(aInfo.uid);
        synchronized(mResourcesManager) {
            java.lang.ref.WeakReference<android.app.LoadedApk> ref;
            if (differentUser) {
                // Caching not supported across users
                ref = null;
            } else
                if (includeCode) {
                    ref = mPackages.get(aInfo.packageName);
                } else {
                    ref = mResourcePackages.get(aInfo.packageName);
                }

            android.app.LoadedApk packageInfo = (ref != null) ? ref.get() : null;
            if ((packageInfo == null) || ((packageInfo.mResources != null) && (!packageInfo.mResources.getAssets().isUpToDate()))) {
                if (android.app.ActivityThread.localLOGV)
                    android.util.Slog.v(android.app.ActivityThread.TAG, ((((includeCode ? "Loading code package " : "Loading resource-only package ") + aInfo.packageName) + " (in ") + (mBoundApplication != null ? mBoundApplication.processName : null)) + ")");

                packageInfo = new android.app.LoadedApk(this, aInfo, compatInfo, baseLoader, securityViolation, includeCode && ((aInfo.flags & android.content.pm.ApplicationInfo.FLAG_HAS_CODE) != 0), registerPackage);
                if (mSystemThread && "android".equals(aInfo.packageName)) {
                    packageInfo.installSystemApplicationInfo(aInfo, getSystemContext().mPackageInfo.getClassLoader());
                }
                if (differentUser) {
                    // Caching not supported across users
                } else
                    if (includeCode) {
                        mPackages.put(aInfo.packageName, new java.lang.ref.WeakReference<android.app.LoadedApk>(packageInfo));
                    } else {
                        mResourcePackages.put(aInfo.packageName, new java.lang.ref.WeakReference<android.app.LoadedApk>(packageInfo));
                    }

            }
            return packageInfo;
        }
    }

    ActivityThread() {
        mResourcesManager = android.app.ResourcesManager.getInstance();
    }

    public android.app.ActivityThread.ApplicationThread getApplicationThread() {
        return mAppThread;
    }

    public android.app.Instrumentation getInstrumentation() {
        return mInstrumentation;
    }

    public boolean isProfiling() {
        return ((mProfiler != null) && (mProfiler.profileFile != null)) && (mProfiler.profileFd == null);
    }

    public java.lang.String getProfileFilePath() {
        return mProfiler.profileFile;
    }

    public android.os.Looper getLooper() {
        return mLooper;
    }

    public android.app.Application getApplication() {
        return mInitialApplication;
    }

    public java.lang.String getProcessName() {
        return mBoundApplication.processName;
    }

    public android.app.ContextImpl getSystemContext() {
        synchronized(this) {
            if (mSystemContext == null) {
                mSystemContext = android.app.ContextImpl.createSystemContext(this);
            }
            return mSystemContext;
        }
    }

    public void installSystemApplicationInfo(android.content.pm.ApplicationInfo info, java.lang.ClassLoader classLoader) {
        synchronized(this) {
            getSystemContext().installSystemApplicationInfo(info, classLoader);
            // give ourselves a default profiler
            mProfiler = new android.app.ActivityThread.Profiler();
        }
    }

    void ensureJitEnabled() {
        if (!mJitEnabled) {
            mJitEnabled = true;
            dalvik.system.VMRuntime.getRuntime().startJitCompilation();
        }
    }

    void scheduleGcIdler() {
        if (!mGcIdlerScheduled) {
            mGcIdlerScheduled = true;
            android.os.Looper.myQueue().addIdleHandler(mGcIdler);
        }
        mH.removeMessages(android.app.ActivityThread.H.GC_WHEN_IDLE);
    }

    void unscheduleGcIdler() {
        if (mGcIdlerScheduled) {
            mGcIdlerScheduled = false;
            android.os.Looper.myQueue().removeIdleHandler(mGcIdler);
        }
        mH.removeMessages(android.app.ActivityThread.H.GC_WHEN_IDLE);
    }

    void doGcIfNeeded() {
        mGcIdlerScheduled = false;
        final long now = android.os.SystemClock.uptimeMillis();
        // Slog.i(TAG, "**** WE MIGHT WANT TO GC: then=" + Binder.getLastGcTime()
        // + "m now=" + now);
        if ((com.android.internal.os.BinderInternal.getLastGcTime() + android.app.ActivityThread.MIN_TIME_BETWEEN_GCS) < now) {
            // Slog.i(TAG, "**** WE DO, WE DO WANT TO GC!");
            com.android.internal.os.BinderInternal.forceGc("bg");
        }
    }

    private static final java.lang.String HEAP_FULL_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s %8s %8s %8s";

    private static final java.lang.String HEAP_COLUMN = "%13s %8s %8s %8s %8s %8s %8s %8s";

    private static final java.lang.String ONE_COUNT_COLUMN = "%21s %8d";

    private static final java.lang.String TWO_COUNT_COLUMNS = "%21s %8d %21s %8d";

    private static final java.lang.String ONE_COUNT_COLUMN_HEADER = "%21s %8s";

    // Formatting for checkin service - update version if row format changes
    private static final int ACTIVITY_THREAD_CHECKIN_VERSION = 4;

    static void printRow(java.io.PrintWriter pw, java.lang.String format, java.lang.Object... objs) {
        pw.println(java.lang.String.format(format, objs));
    }

    public static void dumpMemInfoTable(java.io.PrintWriter pw, android.os.Debug.MemoryInfo memInfo, boolean checkin, boolean dumpFullInfo, boolean dumpDalvik, boolean dumpSummaryOnly, int pid, java.lang.String processName, long nativeMax, long nativeAllocated, long nativeFree, long dalvikMax, long dalvikAllocated, long dalvikFree) {
        // For checkin, we print one long comma-separated list of values
        if (checkin) {
            // NOTE: if you change anything significant below, also consider changing
            // ACTIVITY_THREAD_CHECKIN_VERSION.
            // Header
            pw.print(android.app.ActivityThread.ACTIVITY_THREAD_CHECKIN_VERSION);
            pw.print(',');
            pw.print(pid);
            pw.print(',');
            pw.print(processName);
            pw.print(',');
            // Heap info - max
            pw.print(nativeMax);
            pw.print(',');
            pw.print(dalvikMax);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeMax + dalvikMax);
            pw.print(',');
            // Heap info - allocated
            pw.print(nativeAllocated);
            pw.print(',');
            pw.print(dalvikAllocated);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeAllocated + dalvikAllocated);
            pw.print(',');
            // Heap info - free
            pw.print(nativeFree);
            pw.print(',');
            pw.print(dalvikFree);
            pw.print(',');
            pw.print("N/A,");
            pw.print(nativeFree + dalvikFree);
            pw.print(',');
            // Heap info - proportional set size
            pw.print(memInfo.nativePss);
            pw.print(',');
            pw.print(memInfo.dalvikPss);
            pw.print(',');
            pw.print(memInfo.otherPss);
            pw.print(',');
            pw.print(memInfo.getTotalPss());
            pw.print(',');
            // Heap info - swappable set size
            pw.print(memInfo.nativeSwappablePss);
            pw.print(',');
            pw.print(memInfo.dalvikSwappablePss);
            pw.print(',');
            pw.print(memInfo.otherSwappablePss);
            pw.print(',');
            pw.print(memInfo.getTotalSwappablePss());
            pw.print(',');
            // Heap info - shared dirty
            pw.print(memInfo.nativeSharedDirty);
            pw.print(',');
            pw.print(memInfo.dalvikSharedDirty);
            pw.print(',');
            pw.print(memInfo.otherSharedDirty);
            pw.print(',');
            pw.print(memInfo.getTotalSharedDirty());
            pw.print(',');
            // Heap info - shared clean
            pw.print(memInfo.nativeSharedClean);
            pw.print(',');
            pw.print(memInfo.dalvikSharedClean);
            pw.print(',');
            pw.print(memInfo.otherSharedClean);
            pw.print(',');
            pw.print(memInfo.getTotalSharedClean());
            pw.print(',');
            // Heap info - private Dirty
            pw.print(memInfo.nativePrivateDirty);
            pw.print(',');
            pw.print(memInfo.dalvikPrivateDirty);
            pw.print(',');
            pw.print(memInfo.otherPrivateDirty);
            pw.print(',');
            pw.print(memInfo.getTotalPrivateDirty());
            pw.print(',');
            // Heap info - private Clean
            pw.print(memInfo.nativePrivateClean);
            pw.print(',');
            pw.print(memInfo.dalvikPrivateClean);
            pw.print(',');
            pw.print(memInfo.otherPrivateClean);
            pw.print(',');
            pw.print(memInfo.getTotalPrivateClean());
            pw.print(',');
            // Heap info - swapped out
            pw.print(memInfo.nativeSwappedOut);
            pw.print(',');
            pw.print(memInfo.dalvikSwappedOut);
            pw.print(',');
            pw.print(memInfo.otherSwappedOut);
            pw.print(',');
            pw.print(memInfo.getTotalSwappedOut());
            pw.print(',');
            // Heap info - swapped out pss
            if (memInfo.hasSwappedOutPss) {
                pw.print(memInfo.nativeSwappedOutPss);
                pw.print(',');
                pw.print(memInfo.dalvikSwappedOutPss);
                pw.print(',');
                pw.print(memInfo.otherSwappedOutPss);
                pw.print(',');
                pw.print(memInfo.getTotalSwappedOutPss());
                pw.print(',');
            } else {
                pw.print("N/A,");
                pw.print("N/A,");
                pw.print("N/A,");
                pw.print("N/A,");
            }
            // Heap info - other areas
            for (int i = 0; i < android.os.Debug.MemoryInfo.NUM_OTHER_STATS; i++) {
                pw.print(android.os.Debug.MemoryInfo.getOtherLabel(i));
                pw.print(',');
                pw.print(memInfo.getOtherPss(i));
                pw.print(',');
                pw.print(memInfo.getOtherSwappablePss(i));
                pw.print(',');
                pw.print(memInfo.getOtherSharedDirty(i));
                pw.print(',');
                pw.print(memInfo.getOtherSharedClean(i));
                pw.print(',');
                pw.print(memInfo.getOtherPrivateDirty(i));
                pw.print(',');
                pw.print(memInfo.getOtherPrivateClean(i));
                pw.print(',');
                pw.print(memInfo.getOtherSwappedOut(i));
                pw.print(',');
                if (memInfo.hasSwappedOutPss) {
                    pw.print(memInfo.getOtherSwappedOutPss(i));
                    pw.print(',');
                } else {
                    pw.print("N/A,");
                }
            }
            return;
        }
        if (!dumpSummaryOnly) {
            if (dumpFullInfo) {
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, "", "Pss", "Pss", "Shared", "Private", "Shared", "Private", memInfo.hasSwappedOutPss ? "SwapPss" : "Swap", "Heap", "Heap", "Heap");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, "", "Total", "Clean", "Dirty", "Dirty", "Clean", "Clean", "Dirty", "Size", "Alloc", "Free");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, "", "------", "------", "------", "------", "------", "------", "------", "------", "------", "------");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, "Native Heap", memInfo.nativePss, memInfo.nativeSwappablePss, memInfo.nativeSharedDirty, memInfo.nativePrivateDirty, memInfo.nativeSharedClean, memInfo.nativePrivateClean, memInfo.hasSwappedOutPss ? memInfo.nativeSwappedOut : memInfo.nativeSwappedOutPss, nativeMax, nativeAllocated, nativeFree);
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, "Dalvik Heap", memInfo.dalvikPss, memInfo.dalvikSwappablePss, memInfo.dalvikSharedDirty, memInfo.dalvikPrivateDirty, memInfo.dalvikSharedClean, memInfo.dalvikPrivateClean, memInfo.hasSwappedOutPss ? memInfo.dalvikSwappedOut : memInfo.dalvikSwappedOutPss, dalvikMax, dalvikAllocated, dalvikFree);
            } else {
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, "", "Pss", "Private", "Private", memInfo.hasSwappedOutPss ? "SwapPss" : "Swap", "Heap", "Heap", "Heap");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, "", "Total", "Dirty", "Clean", "Dirty", "Size", "Alloc", "Free");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, "", "------", "------", "------", "------", "------", "------", "------", "------");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, "Native Heap", memInfo.nativePss, memInfo.nativePrivateDirty, memInfo.nativePrivateClean, memInfo.hasSwappedOutPss ? memInfo.nativeSwappedOutPss : memInfo.nativeSwappedOut, nativeMax, nativeAllocated, nativeFree);
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, "Dalvik Heap", memInfo.dalvikPss, memInfo.dalvikPrivateDirty, memInfo.dalvikPrivateClean, memInfo.hasSwappedOutPss ? memInfo.dalvikSwappedOutPss : memInfo.dalvikSwappedOut, dalvikMax, dalvikAllocated, dalvikFree);
            }
            int otherPss = memInfo.otherPss;
            int otherSwappablePss = memInfo.otherSwappablePss;
            int otherSharedDirty = memInfo.otherSharedDirty;
            int otherPrivateDirty = memInfo.otherPrivateDirty;
            int otherSharedClean = memInfo.otherSharedClean;
            int otherPrivateClean = memInfo.otherPrivateClean;
            int otherSwappedOut = memInfo.otherSwappedOut;
            int otherSwappedOutPss = memInfo.otherSwappedOutPss;
            for (int i = 0; i < android.os.Debug.MemoryInfo.NUM_OTHER_STATS; i++) {
                final int myPss = memInfo.getOtherPss(i);
                final int mySwappablePss = memInfo.getOtherSwappablePss(i);
                final int mySharedDirty = memInfo.getOtherSharedDirty(i);
                final int myPrivateDirty = memInfo.getOtherPrivateDirty(i);
                final int mySharedClean = memInfo.getOtherSharedClean(i);
                final int myPrivateClean = memInfo.getOtherPrivateClean(i);
                final int mySwappedOut = memInfo.getOtherSwappedOut(i);
                final int mySwappedOutPss = memInfo.getOtherSwappedOutPss(i);
                if ((((((myPss != 0) || (mySharedDirty != 0)) || (myPrivateDirty != 0)) || (mySharedClean != 0)) || (myPrivateClean != 0)) || ((memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) != 0)) {
                    if (dumpFullInfo) {
                        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, android.os.Debug.MemoryInfo.getOtherLabel(i), myPss, mySwappablePss, mySharedDirty, myPrivateDirty, mySharedClean, myPrivateClean, memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut, "", "", "");
                    } else {
                        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, android.os.Debug.MemoryInfo.getOtherLabel(i), myPss, myPrivateDirty, myPrivateClean, memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut, "", "", "");
                    }
                    otherPss -= myPss;
                    otherSwappablePss -= mySwappablePss;
                    otherSharedDirty -= mySharedDirty;
                    otherPrivateDirty -= myPrivateDirty;
                    otherSharedClean -= mySharedClean;
                    otherPrivateClean -= myPrivateClean;
                    otherSwappedOut -= mySwappedOut;
                    otherSwappedOutPss -= mySwappedOutPss;
                }
            }
            if (dumpFullInfo) {
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, "Unknown", otherPss, otherSwappablePss, otherSharedDirty, otherPrivateDirty, otherSharedClean, otherPrivateClean, memInfo.hasSwappedOutPss ? otherSwappedOutPss : otherSwappedOut, "", "", "");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, "TOTAL", memInfo.getTotalPss(), memInfo.getTotalSwappablePss(), memInfo.getTotalSharedDirty(), memInfo.getTotalPrivateDirty(), memInfo.getTotalSharedClean(), memInfo.getTotalPrivateClean(), memInfo.hasSwappedOutPss ? memInfo.getTotalSwappedOutPss() : memInfo.getTotalSwappedOut(), nativeMax + dalvikMax, nativeAllocated + dalvikAllocated, nativeFree + dalvikFree);
            } else {
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, "Unknown", otherPss, otherPrivateDirty, otherPrivateClean, memInfo.hasSwappedOutPss ? otherSwappedOutPss : otherSwappedOut, "", "", "");
                android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, "TOTAL", memInfo.getTotalPss(), memInfo.getTotalPrivateDirty(), memInfo.getTotalPrivateClean(), memInfo.hasSwappedOutPss ? memInfo.getTotalSwappedOutPss() : memInfo.getTotalSwappedOut(), nativeMax + dalvikMax, nativeAllocated + dalvikAllocated, nativeFree + dalvikFree);
            }
            if (dumpDalvik) {
                pw.println(" ");
                pw.println(" Dalvik Details");
                for (int i = android.os.Debug.MemoryInfo.NUM_OTHER_STATS; i < (android.os.Debug.MemoryInfo.NUM_OTHER_STATS + android.os.Debug.MemoryInfo.NUM_DVK_STATS); i++) {
                    final int myPss = memInfo.getOtherPss(i);
                    final int mySwappablePss = memInfo.getOtherSwappablePss(i);
                    final int mySharedDirty = memInfo.getOtherSharedDirty(i);
                    final int myPrivateDirty = memInfo.getOtherPrivateDirty(i);
                    final int mySharedClean = memInfo.getOtherSharedClean(i);
                    final int myPrivateClean = memInfo.getOtherPrivateClean(i);
                    final int mySwappedOut = memInfo.getOtherSwappedOut(i);
                    final int mySwappedOutPss = memInfo.getOtherSwappedOutPss(i);
                    if ((((((myPss != 0) || (mySharedDirty != 0)) || (myPrivateDirty != 0)) || (mySharedClean != 0)) || (myPrivateClean != 0)) || ((memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut) != 0)) {
                        if (dumpFullInfo) {
                            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_FULL_COLUMN, android.os.Debug.MemoryInfo.getOtherLabel(i), myPss, mySwappablePss, mySharedDirty, myPrivateDirty, mySharedClean, myPrivateClean, memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut, "", "", "");
                        } else {
                            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.HEAP_COLUMN, android.os.Debug.MemoryInfo.getOtherLabel(i), myPss, myPrivateDirty, myPrivateClean, memInfo.hasSwappedOutPss ? mySwappedOutPss : mySwappedOut, "", "", "");
                        }
                    }
                }
            }
        }
        pw.println(" ");
        pw.println(" App Summary");
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN_HEADER, "", "Pss(KB)");
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN_HEADER, "", "------");
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "Java Heap:", memInfo.getSummaryJavaHeap());
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "Native Heap:", memInfo.getSummaryNativeHeap());
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "Code:", memInfo.getSummaryCode());
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "Stack:", memInfo.getSummaryStack());
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "Graphics:", memInfo.getSummaryGraphics());
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "Private Other:", memInfo.getSummaryPrivateOther());
        android.app.ActivityThread.printRow(pw, android.app.ActivityThread.ONE_COUNT_COLUMN, "System:", memInfo.getSummarySystem());
        pw.println(" ");
        if (memInfo.hasSwappedOutPss) {
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "TOTAL:", memInfo.getSummaryTotalPss(), "TOTAL SWAP PSS:", memInfo.getSummaryTotalSwapPss());
        } else {
            android.app.ActivityThread.printRow(pw, android.app.ActivityThread.TWO_COUNT_COLUMNS, "TOTAL:", memInfo.getSummaryTotalPss(), "TOTAL SWAP (KB):", memInfo.getSummaryTotalSwap());
        }
    }

    public void registerOnActivityPausedListener(android.app.Activity activity, android.app.OnActivityPausedListener listener) {
        synchronized(mOnPauseListeners) {
            java.util.ArrayList<android.app.OnActivityPausedListener> list = mOnPauseListeners.get(activity);
            if (list == null) {
                list = new java.util.ArrayList<android.app.OnActivityPausedListener>();
                mOnPauseListeners.put(activity, list);
            }
            list.add(listener);
        }
    }

    public void unregisterOnActivityPausedListener(android.app.Activity activity, android.app.OnActivityPausedListener listener) {
        synchronized(mOnPauseListeners) {
            java.util.ArrayList<android.app.OnActivityPausedListener> list = mOnPauseListeners.get(activity);
            if (list != null) {
                list.remove(listener);
            }
        }
    }

    public final android.content.pm.ActivityInfo resolveActivityInfo(android.content.Intent intent) {
        android.content.pm.ActivityInfo aInfo = intent.resolveActivityInfo(mInitialApplication.getPackageManager(), android.content.pm.PackageManager.GET_SHARED_LIBRARY_FILES);
        if (aInfo == null) {
            // Throw an exception.
            android.app.Instrumentation.checkStartActivityResult(android.app.ActivityManager.START_CLASS_NOT_FOUND, intent);
        }
        return aInfo;
    }

    public final android.app.Activity startActivityNow(android.app.Activity parent, java.lang.String id, android.content.Intent intent, android.content.pm.ActivityInfo activityInfo, android.os.IBinder token, android.os.Bundle state, android.app.Activity.NonConfigurationInstances lastNonConfigurationInstances) {
        android.app.ActivityThread.ActivityClientRecord r = new android.app.ActivityThread.ActivityClientRecord();
        r.token = token;
        r.ident = 0;
        r.intent = intent;
        r.state = state;
        r.parent = parent;
        r.embeddedID = id;
        r.activityInfo = activityInfo;
        r.lastNonConfigurationInstances = lastNonConfigurationInstances;
        if (android.app.ActivityThread.localLOGV) {
            android.content.ComponentName compname = intent.getComponent();
            java.lang.String name;
            if (compname != null) {
                name = compname.toShortString();
            } else {
                name = ("(Intent " + intent) + ").getComponent() returned null";
            }
            android.util.Slog.v(android.app.ActivityThread.TAG, (((("Performing launch: action=" + intent.getAction()) + ", comp=") + name) + ", token=") + token);
        }
        return performLaunchActivity(r, null);
    }

    public final android.app.Activity getActivity(android.os.IBinder token) {
        return mActivities.get(token).activity;
    }

    public final void sendActivityResult(android.os.IBinder token, java.lang.String id, int requestCode, int resultCode, android.content.Intent data) {
        if (android.app.ActivityThread.DEBUG_RESULTS)
            android.util.Slog.v(android.app.ActivityThread.TAG, (((((("sendActivityResult: id=" + id) + " req=") + requestCode) + " res=") + resultCode) + " data=") + data);

        java.util.ArrayList<android.app.ResultInfo> list = new java.util.ArrayList<android.app.ResultInfo>();
        list.add(new android.app.ResultInfo(id, requestCode, resultCode, data));
        mAppThread.scheduleSendResult(token, list);
    }

    private void sendMessage(int what, java.lang.Object obj) {
        sendMessage(what, obj, 0, 0, false);
    }

    private void sendMessage(int what, java.lang.Object obj, int arg1) {
        sendMessage(what, obj, arg1, 0, false);
    }

    private void sendMessage(int what, java.lang.Object obj, int arg1, int arg2) {
        sendMessage(what, obj, arg1, arg2, false);
    }

    private void sendMessage(int what, java.lang.Object obj, int arg1, int arg2, boolean async) {
        if (android.app.ActivityThread.DEBUG_MESSAGES)
            android.util.Slog.v(android.app.ActivityThread.TAG, (((((("SCHEDULE " + what) + " ") + mH.codeToString(what)) + ": ") + arg1) + " / ") + obj);

        android.os.Message msg = android.os.Message.obtain();
        msg.what = what;
        msg.obj = obj;
        msg.arg1 = arg1;
        msg.arg2 = arg2;
        if (async) {
            msg.setAsynchronous(true);
        }
        mH.sendMessage(msg);
    }

    private void sendMessage(int what, java.lang.Object obj, int arg1, int arg2, int seq) {
        if (android.app.ActivityThread.DEBUG_MESSAGES)
            android.util.Slog.v(android.app.ActivityThread.TAG, (((((("SCHEDULE " + mH.codeToString(what)) + " arg1=") + arg1) + " arg2=") + arg2) + "seq= ") + seq);

        android.os.Message msg = android.os.Message.obtain();
        msg.what = what;
        com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
        args.arg1 = obj;
        args.argi1 = arg1;
        args.argi2 = arg2;
        args.argi3 = seq;
        msg.obj = args;
        mH.sendMessage(msg);
    }

    final void scheduleContextCleanup(android.app.ContextImpl context, java.lang.String who, java.lang.String what) {
        android.app.ActivityThread.ContextCleanupInfo cci = new android.app.ActivityThread.ContextCleanupInfo();
        cci.context = context;
        cci.who = who;
        cci.what = what;
        sendMessage(android.app.ActivityThread.H.CLEAN_UP_CONTEXT, cci);
    }

    private android.app.Activity performLaunchActivity(android.app.ActivityThread.ActivityClientRecord r, android.content.Intent customIntent) {
        // System.out.println("##### [" + System.currentTimeMillis() + "] ActivityThread.performLaunchActivity(" + r + ")");
        android.content.pm.ActivityInfo aInfo = r.activityInfo;
        if (r.packageInfo == null) {
            r.packageInfo = getPackageInfo(aInfo.applicationInfo, r.compatInfo, android.content.Context.CONTEXT_INCLUDE_CODE);
        }
        android.content.ComponentName component = r.intent.getComponent();
        if (component == null) {
            component = r.intent.resolveActivity(mInitialApplication.getPackageManager());
            r.intent.setComponent(component);
        }
        if (r.activityInfo.targetActivity != null) {
            component = new android.content.ComponentName(r.activityInfo.packageName, r.activityInfo.targetActivity);
        }
        android.app.Activity activity = null;
        try {
            java.lang.ClassLoader cl = r.packageInfo.getClassLoader();
            activity = mInstrumentation.newActivity(cl, component.getClassName(), r.intent);
            android.os.StrictMode.incrementExpectedActivityCount(activity.getClass());
            r.intent.setExtrasClassLoader(cl);
            r.intent.prepareToEnterProcess();
            if (r.state != null) {
                r.state.setClassLoader(cl);
            }
        } catch (java.lang.Exception e) {
            if (!mInstrumentation.onException(activity, e)) {
                throw new java.lang.RuntimeException((("Unable to instantiate activity " + component) + ": ") + e.toString(), e);
            }
        }
        try {
            android.app.Application app = r.packageInfo.makeApplication(false, mInstrumentation);
            if (android.app.ActivityThread.localLOGV)
                android.util.Slog.v(android.app.ActivityThread.TAG, "Performing launch of " + r);

            if (android.app.ActivityThread.localLOGV)
                android.util.Slog.v(android.app.ActivityThread.TAG, (((((((((r + ": app=") + app) + ", appName=") + app.getPackageName()) + ", pkg=") + r.packageInfo.getPackageName()) + ", comp=") + r.intent.getComponent().toShortString()) + ", dir=") + r.packageInfo.getAppDir());

            if (activity != null) {
                android.content.Context appContext = createBaseContextForActivity(r, activity);
                java.lang.CharSequence title = r.activityInfo.loadLabel(appContext.getPackageManager());
                android.content.res.Configuration config = new android.content.res.Configuration(mCompatConfiguration);
                if (r.overrideConfig != null) {
                    config.updateFrom(r.overrideConfig);
                }
                if (android.app.ActivityThread.DEBUG_CONFIGURATION)
                    android.util.Slog.v(android.app.ActivityThread.TAG, (("Launching activity " + r.activityInfo.name) + " with config ") + config);

                android.view.Window window = null;
                if ((r.mPendingRemoveWindow != null) && r.mPreserveWindow) {
                    window = r.mPendingRemoveWindow;
                    r.mPendingRemoveWindow = null;
                    r.mPendingRemoveWindowManager = null;
                }
                activity.attach(appContext, this, getInstrumentation(), r.token, r.ident, app, r.intent, r.activityInfo, title, r.parent, r.embeddedID, r.lastNonConfigurationInstances, config, r.referrer, r.voiceInteractor, window);
                if (customIntent != null) {
                    activity.mIntent = customIntent;
                }
                r.lastNonConfigurationInstances = null;
                activity.mStartedActivity = false;
                int theme = r.activityInfo.getThemeResource();
                if (theme != 0) {
                    activity.setTheme(theme);
                }
                activity.mCalled = false;
                if (r.isPersistable()) {
                    mInstrumentation.callActivityOnCreate(activity, r.state, r.persistentState);
                } else {
                    mInstrumentation.callActivityOnCreate(activity, r.state);
                }
                if (!activity.mCalled) {
                    throw new android.util.SuperNotCalledException(("Activity " + r.intent.getComponent().toShortString()) + " did not call through to super.onCreate()");
                }
                r.activity = activity;
                r.stopped = true;
                if (!r.activity.mFinished) {
                    activity.performStart();
                    r.stopped = false;
                }
                if (!r.activity.mFinished) {
                    if (r.isPersistable()) {
                        if ((r.state != null) || (r.persistentState != null)) {
                            mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state, r.persistentState);
                        }
                    } else
                        if (r.state != null) {
                            mInstrumentation.callActivityOnRestoreInstanceState(activity, r.state);
                        }

                }
                if (!r.activity.mFinished) {
                    activity.mCalled = false;
                    if (r.isPersistable()) {
                        mInstrumentation.callActivityOnPostCreate(activity, r.state, r.persistentState);
                    } else {
                        mInstrumentation.callActivityOnPostCreate(activity, r.state);
                    }
                    if (!activity.mCalled) {
                        throw new android.util.SuperNotCalledException(("Activity " + r.intent.getComponent().toShortString()) + " did not call through to super.onPostCreate()");
                    }
                }
            }
            r.paused = true;
            mActivities.put(r.token, r);
        } catch (android.util.SuperNotCalledException e) {
            throw e;
        } catch (java.lang.Exception e) {
            if (!mInstrumentation.onException(activity, e)) {
                throw new java.lang.RuntimeException((("Unable to start activity " + component) + ": ") + e.toString(), e);
            }
        }
        return activity;
    }

    private android.content.Context createBaseContextForActivity(android.app.ActivityThread.ActivityClientRecord r, final android.app.Activity activity) {
        int displayId = android.view.Display.DEFAULT_DISPLAY;
        try {
            displayId = android.app.ActivityManagerNative.getDefault().getActivityDisplayId(r.token);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        android.app.ContextImpl appContext = android.app.ContextImpl.createActivityContext(this, r.packageInfo, r.token, displayId, r.overrideConfig);
        appContext.setOuterContext(activity);
        android.content.Context baseContext = appContext;
        final android.hardware.display.DisplayManagerGlobal dm = android.hardware.display.DisplayManagerGlobal.getInstance();
        // For debugging purposes, if the activity's package name contains the value of
        // the "debug.use-second-display" system property as a substring, then show
        // its content on a secondary display if there is one.
        java.lang.String pkgName = android.os.SystemProperties.get("debug.second-display.pkg");
        if (((pkgName != null) && (!pkgName.isEmpty())) && r.packageInfo.mPackageName.contains(pkgName)) {
            for (int id : dm.getDisplayIds()) {
                if (id != android.view.Display.DEFAULT_DISPLAY) {
                    android.view.Display display = dm.getCompatibleDisplay(id, appContext.getDisplayAdjustments(id));
                    baseContext = appContext.createDisplayContext(display);
                    break;
                }
            }
        }
        return baseContext;
    }

    private void handleLaunchActivity(android.app.ActivityThread.ActivityClientRecord r, android.content.Intent customIntent, java.lang.String reason) {
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();
        mSomeActivitiesChanged = true;
        if (r.profilerInfo != null) {
            mProfiler.setProfiler(r.profilerInfo);
            mProfiler.startProfiling();
        }
        // Make sure we are running with the most recent config.
        handleConfigurationChanged(null, null);
        if (android.app.ActivityThread.localLOGV)
            android.util.Slog.v(android.app.ActivityThread.TAG, "Handling launch of " + r);

        // Initialize before creating the activity
        android.view.WindowManagerGlobal.initialize();
        android.app.Activity a = performLaunchActivity(r, customIntent);
        if (a != null) {
            r.createdConfig = new android.content.res.Configuration(mConfiguration);
            reportSizeConfigurations(r);
            android.os.Bundle oldState = r.state;
            handleResumeActivity(r.token, false, r.isForward, (!r.activity.mFinished) && (!r.startsNotResumed), r.lastProcessedSeq, reason);
            if ((!r.activity.mFinished) && r.startsNotResumed) {
                // The activity manager actually wants this one to start out paused, because it
                // needs to be visible but isn't in the foreground. We accomplish this by going
                // through the normal startup (because activities expect to go through onResume()
                // the first time they run, before their window is displayed), and then pausing it.
                // However, in this case we do -not- need to do the full pause cycle (of freezing
                // and such) because the activity manager assumes it can just retain the current
                // state it has.
                performPauseActivityIfNeeded(r, reason);
                // We need to keep around the original state, in case we need to be created again.
                // But we only do this for pre-Honeycomb apps, which always save their state when
                // pausing, so we can not have them save their state when restarting from a paused
                // state. For HC and later, we want to (and can) let the state be saved as the
                // normal part of stopping the activity.
                if (r.isPreHoneycomb()) {
                    r.state = oldState;
                }
            }
        } else {
            // If there was an error, for any reason, tell the activity manager to stop us.
            try {
                android.app.ActivityManagerNative.getDefault().finishActivity(r.token, android.app.Activity.RESULT_CANCELED, null, android.app.Activity.DONT_FINISH_TASK_WITH_ACTIVITY);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    private void reportSizeConfigurations(android.app.ActivityThread.ActivityClientRecord r) {
        android.content.res.Configuration[] configurations = r.activity.getResources().getSizeConfigurations();
        if (configurations == null) {
            return;
        }
        android.util.SparseIntArray horizontal = new android.util.SparseIntArray();
        android.util.SparseIntArray vertical = new android.util.SparseIntArray();
        android.util.SparseIntArray smallest = new android.util.SparseIntArray();
        for (int i = configurations.length - 1; i >= 0; i--) {
            android.content.res.Configuration config = configurations[i];
            if (config.screenHeightDp != android.content.res.Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                vertical.put(config.screenHeightDp, 0);
            }
            if (config.screenWidthDp != android.content.res.Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                horizontal.put(config.screenWidthDp, 0);
            }
            if (config.smallestScreenWidthDp != android.content.res.Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED) {
                smallest.put(config.smallestScreenWidthDp, 0);
            }
        }
        try {
            android.app.ActivityManagerNative.getDefault().reportSizeConfigurations(r.token, horizontal.copyKeys(), vertical.copyKeys(), smallest.copyKeys());
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private void deliverNewIntents(android.app.ActivityThread.ActivityClientRecord r, java.util.List<com.android.internal.content.ReferrerIntent> intents) {
        final int N = intents.size();
        for (int i = 0; i < N; i++) {
            com.android.internal.content.ReferrerIntent intent = intents.get(i);
            intent.setExtrasClassLoader(r.activity.getClassLoader());
            intent.prepareToEnterProcess();
            r.activity.mFragments.noteStateNotSaved();
            mInstrumentation.callActivityOnNewIntent(r.activity, intent);
        }
    }

    void performNewIntents(android.os.IBinder token, java.util.List<com.android.internal.content.ReferrerIntent> intents, boolean andPause) {
        final android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r == null) {
            return;
        }
        final boolean resumed = !r.paused;
        if (resumed) {
            r.activity.mTemporaryPause = true;
            mInstrumentation.callActivityOnPause(r.activity);
        }
        deliverNewIntents(r, intents);
        if (resumed) {
            r.activity.performResume();
            r.activity.mTemporaryPause = false;
        }
        if (r.paused && andPause) {
            // In this case the activity was in the paused state when we delivered the intent,
            // to guarantee onResume gets called after onNewIntent we temporarily resume the
            // activity and pause again as the caller wanted.
            performResumeActivity(token, false, "performNewIntents");
            performPauseActivityIfNeeded(r, "performNewIntents");
        }
    }

    private void handleNewIntent(android.app.ActivityThread.NewIntentData data) {
        performNewIntents(data.token, data.intents, data.andPause);
    }

    public void handleRequestAssistContextExtras(android.app.ActivityThread.RequestAssistContextExtras cmd) {
        if (mLastSessionId != cmd.sessionId) {
            // Clear the existing structures
            mLastSessionId = cmd.sessionId;
            for (int i = mLastAssistStructures.size() - 1; i >= 0; i--) {
                android.app.assist.AssistStructure structure = mLastAssistStructures.get(i).get();
                if (structure != null) {
                    structure.clearSendChannel();
                }
                mLastAssistStructures.remove(i);
            }
        }
        android.os.Bundle data = new android.os.Bundle();
        android.app.assist.AssistStructure structure = null;
        android.app.assist.AssistContent content = new android.app.assist.AssistContent();
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(cmd.activityToken);
        android.net.Uri referrer = null;
        if (r != null) {
            r.activity.getApplication().dispatchOnProvideAssistData(r.activity, data);
            r.activity.onProvideAssistData(data);
            referrer = r.activity.onProvideReferrer();
            if (cmd.requestType == android.app.ActivityManager.ASSIST_CONTEXT_FULL) {
                structure = new android.app.assist.AssistStructure(r.activity);
                android.content.Intent activityIntent = r.activity.getIntent();
                if ((activityIntent != null) && ((r.window == null) || ((r.window.getAttributes().flags & android.view.WindowManager.LayoutParams.FLAG_SECURE) == 0))) {
                    android.content.Intent intent = new android.content.Intent(activityIntent);
                    intent.setFlags(intent.getFlags() & (~(android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION | android.content.Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)));
                    intent.removeUnsafeExtras();
                    content.setDefaultIntent(intent);
                } else {
                    content.setDefaultIntent(new android.content.Intent());
                }
                r.activity.onProvideAssistContent(content);
            }
        }
        if (structure == null) {
            structure = new android.app.assist.AssistStructure();
        }
        mLastAssistStructures.add(new java.lang.ref.WeakReference<>(structure));
        android.app.IActivityManager mgr = android.app.ActivityManagerNative.getDefault();
        try {
            mgr.reportAssistContextExtras(cmd.requestToken, data, structure, content, referrer);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void handleTranslucentConversionComplete(android.os.IBinder token, boolean drawComplete) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            r.activity.onTranslucentConversionComplete(drawComplete);
        }
    }

    public void onNewActivityOptions(android.os.IBinder token, android.app.ActivityOptions options) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            r.activity.onNewActivityOptions(options);
        }
    }

    public void handleCancelVisibleBehind(android.os.IBinder token) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            mSomeActivitiesChanged = true;
            final android.app.Activity activity = r.activity;
            if (activity.mVisibleBehind) {
                activity.mCalled = false;
                activity.onVisibleBehindCanceled();
                // Tick, tick, tick. The activity has 500 msec to return or it will be destroyed.
                if (!activity.mCalled) {
                    throw new android.util.SuperNotCalledException(("Activity " + activity.getLocalClassName()) + " did not call through to super.onVisibleBehindCanceled()");
                }
                activity.mVisibleBehind = false;
            }
        }
        try {
            android.app.ActivityManagerNative.getDefault().backgroundResourcesReleased(token);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    public void handleOnBackgroundVisibleBehindChanged(android.os.IBinder token, boolean visible) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            r.activity.onBackgroundVisibleBehindChanged(visible);
        }
    }

    public void handleInstallProvider(android.content.pm.ProviderInfo info) {
        final android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
        try {
            installContentProviders(mInitialApplication, com.google.android.collect.Lists.newArrayList(info));
        } finally {
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleEnterAnimationComplete(android.os.IBinder token) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            r.activity.dispatchEnterAnimationComplete();
        }
    }

    private void handleStartBinderTracking() {
        android.os.Binder.enableTracing();
    }

    private void handleStopBinderTrackingAndDump(android.os.ParcelFileDescriptor fd) {
        try {
            android.os.Binder.disableTracing();
            android.os.Binder.getTransactionTracker().writeTracesToFile(fd);
        } finally {
            libcore.io.IoUtils.closeQuietly(fd);
            android.os.Binder.getTransactionTracker().clearTraces();
        }
    }

    private void handleMultiWindowModeChanged(android.os.IBinder token, boolean isInMultiWindowMode) {
        final android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            r.activity.dispatchMultiWindowModeChanged(isInMultiWindowMode);
        }
    }

    private void handlePictureInPictureModeChanged(android.os.IBinder token, boolean isInPipMode) {
        final android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            r.activity.dispatchPictureInPictureModeChanged(isInPipMode);
        }
    }

    private void handleLocalVoiceInteractionStarted(android.os.IBinder token, com.android.internal.app.IVoiceInteractor interactor) {
        final android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r != null) {
            r.voiceInteractor = interactor;
            r.activity.setVoiceInteractor(interactor);
            if (interactor == null) {
                r.activity.onLocalVoiceInteractionStopped();
            } else {
                r.activity.onLocalVoiceInteractionStarted();
            }
        }
    }

    private static final java.lang.ThreadLocal<android.content.Intent> sCurrentBroadcastIntent = new java.lang.ThreadLocal<android.content.Intent>();

    /**
     * Return the Intent that's currently being handled by a
     * BroadcastReceiver on this thread, or null if none.
     *
     * @unknown 
     */
    public static android.content.Intent getIntentBeingBroadcast() {
        return android.app.ActivityThread.sCurrentBroadcastIntent.get();
    }

    private void handleReceiver(android.app.ActivityThread.ReceiverData data) {
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();
        java.lang.String component = data.intent.getComponent().getClassName();
        android.app.LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        android.app.IActivityManager mgr = android.app.ActivityManagerNative.getDefault();
        android.content.BroadcastReceiver receiver;
        try {
            java.lang.ClassLoader cl = packageInfo.getClassLoader();
            data.intent.setExtrasClassLoader(cl);
            data.intent.prepareToEnterProcess();
            data.setExtrasClassLoader(cl);
            receiver = ((android.content.BroadcastReceiver) (cl.loadClass(component).newInstance()));
        } catch (java.lang.Exception e) {
            if (android.app.ActivityThread.DEBUG_BROADCAST)
                android.util.Slog.i(android.app.ActivityThread.TAG, "Finishing failed broadcast to " + data.intent.getComponent());

            data.sendFinished(mgr);
            throw new java.lang.RuntimeException((("Unable to instantiate receiver " + component) + ": ") + e.toString(), e);
        }
        try {
            android.app.Application app = packageInfo.makeApplication(false, mInstrumentation);
            if (android.app.ActivityThread.localLOGV)
                android.util.Slog.v(android.app.ActivityThread.TAG, (((((((((("Performing receive of " + data.intent) + ": app=") + app) + ", appName=") + app.getPackageName()) + ", pkg=") + packageInfo.getPackageName()) + ", comp=") + data.intent.getComponent().toShortString()) + ", dir=") + packageInfo.getAppDir());

            android.app.ContextImpl context = ((android.app.ContextImpl) (app.getBaseContext()));
            android.app.ActivityThread.sCurrentBroadcastIntent.set(data.intent);
            receiver.setPendingResult(data);
            receiver.onReceive(context.getReceiverRestrictedContext(), data.intent);
        } catch (java.lang.Exception e) {
            if (android.app.ActivityThread.DEBUG_BROADCAST)
                android.util.Slog.i(android.app.ActivityThread.TAG, "Finishing failed broadcast to " + data.intent.getComponent());

            data.sendFinished(mgr);
            if (!mInstrumentation.onException(receiver, e)) {
                throw new java.lang.RuntimeException((("Unable to start receiver " + component) + ": ") + e.toString(), e);
            }
        } finally {
            android.app.ActivityThread.sCurrentBroadcastIntent.set(null);
        }
        if (receiver.getPendingResult() != null) {
            data.finish();
        }
    }

    // Instantiate a BackupAgent and tell it that it's alive
    private void handleCreateBackupAgent(android.app.ActivityThread.CreateBackupAgentData data) {
        if (android.app.ActivityThread.DEBUG_BACKUP)
            android.util.Slog.v(android.app.ActivityThread.TAG, "handleCreateBackupAgent: " + data);

        // Sanity check the requested target package's uid against ours
        try {
            android.content.pm.PackageInfo requestedPackage = android.app.ActivityThread.getPackageManager().getPackageInfo(data.appInfo.packageName, 0, android.os.UserHandle.myUserId());
            if (requestedPackage.applicationInfo.uid != android.os.Process.myUid()) {
                android.util.Slog.w(android.app.ActivityThread.TAG, "Asked to instantiate non-matching package " + data.appInfo.packageName);
                return;
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        // no longer idle; we have backup work to do
        unscheduleGcIdler();
        // instantiate the BackupAgent class named in the manifest
        android.app.LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        java.lang.String packageName = packageInfo.mPackageName;
        if (packageName == null) {
            android.util.Slog.d(android.app.ActivityThread.TAG, "Asked to create backup agent for nonexistent package");
            return;
        }
        java.lang.String classname = data.appInfo.backupAgentName;
        // full backup operation but no app-supplied agent?  use the default implementation
        if ((classname == null) && ((data.backupMode == android.app.IApplicationThread.BACKUP_MODE_FULL) || (data.backupMode == android.app.IApplicationThread.BACKUP_MODE_RESTORE_FULL))) {
            classname = "android.app.backup.FullBackupAgent";
        }
        try {
            android.os.IBinder binder = null;
            android.app.backup.BackupAgent agent = mBackupAgents.get(packageName);
            if (agent != null) {
                // reusing the existing instance
                if (android.app.ActivityThread.DEBUG_BACKUP) {
                    android.util.Slog.v(android.app.ActivityThread.TAG, "Reusing existing agent instance");
                }
                binder = agent.onBind();
            } else {
                try {
                    if (android.app.ActivityThread.DEBUG_BACKUP)
                        android.util.Slog.v(android.app.ActivityThread.TAG, "Initializing agent class " + classname);

                    java.lang.ClassLoader cl = packageInfo.getClassLoader();
                    agent = ((android.app.backup.BackupAgent) (cl.loadClass(classname).newInstance()));
                    // set up the agent's context
                    android.app.ContextImpl context = android.app.ContextImpl.createAppContext(this, packageInfo);
                    context.setOuterContext(agent);
                    agent.attach(context);
                    agent.onCreate();
                    binder = agent.onBind();
                    mBackupAgents.put(packageName, agent);
                } catch (java.lang.Exception e) {
                    // If this is during restore, fail silently; otherwise go
                    // ahead and let the user see the crash.
                    android.util.Slog.e(android.app.ActivityThread.TAG, "Agent threw during creation: " + e);
                    if ((data.backupMode != android.app.IApplicationThread.BACKUP_MODE_RESTORE) && (data.backupMode != android.app.IApplicationThread.BACKUP_MODE_RESTORE_FULL)) {
                        throw e;
                    }
                    // falling through with 'binder' still null
                }
            }
            // tell the OS that we're live now
            try {
                android.app.ActivityManagerNative.getDefault().backupAgentCreated(packageName, binder);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } catch (java.lang.Exception e) {
            throw new java.lang.RuntimeException((("Unable to create BackupAgent " + classname) + ": ") + e.toString(), e);
        }
    }

    // Tear down a BackupAgent
    private void handleDestroyBackupAgent(android.app.ActivityThread.CreateBackupAgentData data) {
        if (android.app.ActivityThread.DEBUG_BACKUP)
            android.util.Slog.v(android.app.ActivityThread.TAG, "handleDestroyBackupAgent: " + data);

        android.app.LoadedApk packageInfo = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        java.lang.String packageName = packageInfo.mPackageName;
        android.app.backup.BackupAgent agent = mBackupAgents.get(packageName);
        if (agent != null) {
            try {
                agent.onDestroy();
            } catch (java.lang.Exception e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, "Exception thrown in onDestroy by backup agent of " + data.appInfo);
                e.printStackTrace();
            }
            mBackupAgents.remove(packageName);
        } else {
            android.util.Slog.w(android.app.ActivityThread.TAG, "Attempt to destroy unknown backup agent " + data);
        }
    }

    private void handleCreateService(android.app.ActivityThread.CreateServiceData data) {
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();
        android.app.LoadedApk packageInfo = getPackageInfoNoCheck(data.info.applicationInfo, data.compatInfo);
        android.app.Service service = null;
        try {
            java.lang.ClassLoader cl = packageInfo.getClassLoader();
            service = ((android.app.Service) (cl.loadClass(data.info.name).newInstance()));
        } catch (java.lang.Exception e) {
            if (!mInstrumentation.onException(service, e)) {
                throw new java.lang.RuntimeException((("Unable to instantiate service " + data.info.name) + ": ") + e.toString(), e);
            }
        }
        try {
            if (android.app.ActivityThread.localLOGV)
                android.util.Slog.v(android.app.ActivityThread.TAG, "Creating service " + data.info.name);

            android.app.ContextImpl context = android.app.ContextImpl.createAppContext(this, packageInfo);
            context.setOuterContext(service);
            android.app.Application app = packageInfo.makeApplication(false, mInstrumentation);
            service.attach(context, this, data.info.name, data.token, app, android.app.ActivityManagerNative.getDefault());
            service.onCreate();
            mServices.put(data.token, service);
            try {
                android.app.ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, android.app.ActivityThread.SERVICE_DONE_EXECUTING_ANON, 0, 0);
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        } catch (java.lang.Exception e) {
            if (!mInstrumentation.onException(service, e)) {
                throw new java.lang.RuntimeException((("Unable to create service " + data.info.name) + ": ") + e.toString(), e);
            }
        }
    }

    private void handleBindService(android.app.ActivityThread.BindServiceData data) {
        android.app.Service s = mServices.get(data.token);
        if (android.app.ActivityThread.DEBUG_SERVICE)
            android.util.Slog.v(android.app.ActivityThread.TAG, (("handleBindService s=" + s) + " rebind=") + data.rebind);

        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                try {
                    if (!data.rebind) {
                        android.os.IBinder binder = s.onBind(data.intent);
                        android.app.ActivityManagerNative.getDefault().publishService(data.token, data.intent, binder);
                    } else {
                        s.onRebind(data.intent);
                        android.app.ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, android.app.ActivityThread.SERVICE_DONE_EXECUTING_ANON, 0, 0);
                    }
                    ensureJitEnabled();
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new java.lang.RuntimeException((((("Unable to bind to service " + s) + " with ") + data.intent) + ": ") + e.toString(), e);
                }
            }
        }
    }

    private void handleUnbindService(android.app.ActivityThread.BindServiceData data) {
        android.app.Service s = mServices.get(data.token);
        if (s != null) {
            try {
                data.intent.setExtrasClassLoader(s.getClassLoader());
                data.intent.prepareToEnterProcess();
                boolean doRebind = s.onUnbind(data.intent);
                try {
                    if (doRebind) {
                        android.app.ActivityManagerNative.getDefault().unbindFinished(data.token, data.intent, doRebind);
                    } else {
                        android.app.ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, android.app.ActivityThread.SERVICE_DONE_EXECUTING_ANON, 0, 0);
                    }
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new java.lang.RuntimeException((((("Unable to unbind to service " + s) + " with ") + data.intent) + ": ") + e.toString(), e);
                }
            }
        }
    }

    private void handleDumpService(android.app.ActivityThread.DumpComponentInfo info) {
        final android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
        try {
            android.app.Service s = mServices.get(info.token);
            if (s != null) {
                java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(new java.io.FileOutputStream(info.fd.getFileDescriptor()));
                s.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(info.fd);
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpActivity(android.app.ActivityThread.DumpComponentInfo info) {
        final android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
        try {
            android.app.ActivityThread.ActivityClientRecord r = mActivities.get(info.token);
            if ((r != null) && (r.activity != null)) {
                java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(new java.io.FileOutputStream(info.fd.getFileDescriptor()));
                r.activity.dump(info.prefix, info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(info.fd);
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleDumpProvider(android.app.ActivityThread.DumpComponentInfo info) {
        final android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskWrites();
        try {
            android.app.ActivityThread.ProviderClientRecord r = mLocalProviders.get(info.token);
            if ((r != null) && (r.mLocalProvider != null)) {
                java.io.PrintWriter pw = new com.android.internal.util.FastPrintWriter(new java.io.FileOutputStream(info.fd.getFileDescriptor()));
                r.mLocalProvider.dump(info.fd.getFileDescriptor(), pw, info.args);
                pw.flush();
            }
        } finally {
            libcore.io.IoUtils.closeQuietly(info.fd);
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }

    private void handleServiceArgs(android.app.ActivityThread.ServiceArgsData data) {
        android.app.Service s = mServices.get(data.token);
        if (s != null) {
            try {
                if (data.args != null) {
                    data.args.setExtrasClassLoader(s.getClassLoader());
                    data.args.prepareToEnterProcess();
                }
                int res;
                if (!data.taskRemoved) {
                    res = s.onStartCommand(data.args, data.flags, data.startId);
                } else {
                    s.onTaskRemoved(data.args);
                    res = android.app.Service.START_TASK_REMOVED_COMPLETE;
                }
                android.app.QueuedWork.waitToFinish();
                try {
                    android.app.ActivityManagerNative.getDefault().serviceDoneExecuting(data.token, android.app.ActivityThread.SERVICE_DONE_EXECUTING_START, data.startId, res);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
                ensureJitEnabled();
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new java.lang.RuntimeException((((("Unable to start service " + s) + " with ") + data.args) + ": ") + e.toString(), e);
                }
            }
        }
    }

    private void handleStopService(android.os.IBinder token) {
        android.app.Service s = mServices.remove(token);
        if (s != null) {
            try {
                if (android.app.ActivityThread.localLOGV)
                    android.util.Slog.v(android.app.ActivityThread.TAG, "Destroying service " + s);

                s.onDestroy();
                android.content.Context context = s.getBaseContext();
                if (context instanceof android.app.ContextImpl) {
                    final java.lang.String who = s.getClassName();
                    ((android.app.ContextImpl) (context)).scheduleFinalCleanup(who, "Service");
                }
                android.app.QueuedWork.waitToFinish();
                try {
                    android.app.ActivityManagerNative.getDefault().serviceDoneExecuting(token, android.app.ActivityThread.SERVICE_DONE_EXECUTING_STOP, 0, 0);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(s, e)) {
                    throw new java.lang.RuntimeException((("Unable to stop service " + s) + ": ") + e.toString(), e);
                }
                android.util.Slog.i(android.app.ActivityThread.TAG, "handleStopService: exception for " + token, e);
            }
        } else {
            android.util.Slog.i(android.app.ActivityThread.TAG, ("handleStopService: token=" + token) + " not found.");
        }
        // Slog.i(TAG, "Running services: " + mServices);
    }

    public final android.app.ActivityThread.ActivityClientRecord performResumeActivity(android.os.IBinder token, boolean clearHide, java.lang.String reason) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (android.app.ActivityThread.localLOGV)
            android.util.Slog.v(android.app.ActivityThread.TAG, (("Performing resume of " + r) + " finished=") + r.activity.mFinished);

        if ((r != null) && (!r.activity.mFinished)) {
            if (clearHide) {
                r.hideForNow = false;
                r.activity.mStartedActivity = false;
            }
            try {
                r.activity.onStateNotSaved();
                r.activity.mFragments.noteStateNotSaved();
                if (r.pendingIntents != null) {
                    deliverNewIntents(r, r.pendingIntents);
                    r.pendingIntents = null;
                }
                if (r.pendingResults != null) {
                    deliverResults(r, r.pendingResults);
                    r.pendingResults = null;
                }
                r.activity.performResume();
                // If there is a pending local relaunch that was requested when the activity was
                // paused, it will put the activity into paused state when it finally happens.
                // Since the activity resumed before being relaunched, we don't want that to happen,
                // so we need to clear the request to relaunch paused.
                for (int i = mRelaunchingActivities.size() - 1; i >= 0; i--) {
                    final android.app.ActivityThread.ActivityClientRecord relaunching = mRelaunchingActivities.get(i);
                    if (((relaunching.token == r.token) && relaunching.onlyLocalRequest) && relaunching.startsNotResumed) {
                        relaunching.startsNotResumed = false;
                    }
                }
                android.util.EventLog.writeEvent(android.app.ActivityThread.LOG_AM_ON_RESUME_CALLED, android.os.UserHandle.myUserId(), r.activity.getComponentName().getClassName(), reason);
                r.paused = false;
                r.stopped = false;
                r.state = null;
                r.persistentState = null;
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new java.lang.RuntimeException((("Unable to resume activity " + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
                }
            }
        }
        return r;
    }

    static final void cleanUpPendingRemoveWindows(android.app.ActivityThread.ActivityClientRecord r, boolean force) {
        if (r.mPreserveWindow && (!force)) {
            return;
        }
        if (r.mPendingRemoveWindow != null) {
            r.mPendingRemoveWindowManager.removeViewImmediate(r.mPendingRemoveWindow.getDecorView());
            android.os.IBinder wtoken = r.mPendingRemoveWindow.getDecorView().getWindowToken();
            if (wtoken != null) {
                android.view.WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), "Activity");
            }
        }
        r.mPendingRemoveWindow = null;
        r.mPendingRemoveWindowManager = null;
    }

    final void handleResumeActivity(android.os.IBinder token, boolean clearHide, boolean isForward, boolean reallyResume, int seq, java.lang.String reason) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (!android.app.ActivityThread.checkAndUpdateLifecycleSeq(seq, r, "resumeActivity")) {
            return;
        }
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();
        mSomeActivitiesChanged = true;
        // TODO Push resumeArgs into the activity for consideration
        r = performResumeActivity(token, clearHide, reason);
        if (r != null) {
            final android.app.Activity a = r.activity;
            if (android.app.ActivityThread.localLOGV)
                android.util.Slog.v(android.app.ActivityThread.TAG, (((((("Resume " + r) + " started activity: ") + a.mStartedActivity) + ", hideForNow: ") + r.hideForNow) + ", finished: ") + a.mFinished);

            final int forwardBit = (isForward) ? android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION : 0;
            // If the window hasn't yet been added to the window manager,
            // and this guy didn't finish itself or start another activity,
            // then go ahead and add the window.
            boolean willBeVisible = !a.mStartedActivity;
            if (!willBeVisible) {
                try {
                    willBeVisible = android.app.ActivityManagerNative.getDefault().willActivityBeVisible(a.getActivityToken());
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            if (((r.window == null) && (!a.mFinished)) && willBeVisible) {
                r.window = r.activity.getWindow();
                android.view.View decor = r.window.getDecorView();
                decor.setVisibility(android.view.View.INVISIBLE);
                android.view.ViewManager wm = a.getWindowManager();
                android.view.WindowManager.LayoutParams l = r.window.getAttributes();
                a.mDecor = decor;
                l.type = android.view.WindowManager.LayoutParams.TYPE_BASE_APPLICATION;
                l.softInputMode |= forwardBit;
                if (r.mPreserveWindow) {
                    a.mWindowAdded = true;
                    r.mPreserveWindow = false;
                    // Normally the ViewRoot sets up callbacks with the Activity
                    // in addView->ViewRootImpl#setView. If we are instead reusing
                    // the decor view we have to notify the view root that the
                    // callbacks may have changed.
                    android.view.ViewRootImpl impl = decor.getViewRootImpl();
                    if (impl != null) {
                        impl.notifyChildRebuilt();
                    }
                }
                if (a.mVisibleFromClient && (!a.mWindowAdded)) {
                    a.mWindowAdded = true;
                    wm.addView(decor, l);
                }
                // If the window has already been added, but during resume
                // we started another activity, then don't yet make the
                // window visible.
            } else
                if (!willBeVisible) {
                    if (android.app.ActivityThread.localLOGV)
                        android.util.Slog.v(android.app.ActivityThread.TAG, ("Launch " + r) + " mStartedActivity set");

                    r.hideForNow = true;
                }

            // Get rid of anything left hanging around.
            /* force */
            android.app.ActivityThread.cleanUpPendingRemoveWindows(r, false);
            // The window is now visible if it has been added, we are not
            // simply finishing, and we are not starting another activity.
            if ((((!r.activity.mFinished) && willBeVisible) && (r.activity.mDecor != null)) && (!r.hideForNow)) {
                if (r.newConfig != null) {
                    performConfigurationChangedForActivity(r, r.newConfig, android.app.ActivityThread.REPORT_TO_ACTIVITY);
                    if (android.app.ActivityThread.DEBUG_CONFIGURATION)
                        android.util.Slog.v(android.app.ActivityThread.TAG, (("Resuming activity " + r.activityInfo.name) + " with newConfig ") + r.activity.mCurrentConfig);

                    r.newConfig = null;
                }
                if (android.app.ActivityThread.localLOGV)
                    android.util.Slog.v(android.app.ActivityThread.TAG, (("Resuming " + r) + " with isForward=") + isForward);

                android.view.WindowManager.LayoutParams l = r.window.getAttributes();
                if ((l.softInputMode & android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION) != forwardBit) {
                    l.softInputMode = (l.softInputMode & (~android.view.WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION)) | forwardBit;
                    if (r.activity.mVisibleFromClient) {
                        android.view.ViewManager wm = a.getWindowManager();
                        android.view.View decor = r.window.getDecorView();
                        wm.updateViewLayout(decor, l);
                    }
                }
                r.activity.mVisibleFromServer = true;
                mNumVisibleActivities++;
                if (r.activity.mVisibleFromClient) {
                    r.activity.makeVisible();
                }
            }
            if (!r.onlyLocalRequest) {
                r.nextIdle = mNewActivities;
                mNewActivities = r;
                if (android.app.ActivityThread.localLOGV)
                    android.util.Slog.v(android.app.ActivityThread.TAG, "Scheduling idle handler for " + r);

                android.os.Looper.myQueue().addIdleHandler(new android.app.ActivityThread.Idler());
            }
            r.onlyLocalRequest = false;
            // Tell the activity manager we have resumed.
            if (reallyResume) {
                try {
                    android.app.ActivityManagerNative.getDefault().activityResumed(token);
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            }
        } else {
            // If an exception was thrown when trying to resume, then
            // just end this activity.
            try {
                android.app.ActivityManagerNative.getDefault().finishActivity(token, android.app.Activity.RESULT_CANCELED, null, android.app.Activity.DONT_FINISH_TASK_WITH_ACTIVITY);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    private int mThumbnailWidth = -1;

    private int mThumbnailHeight = -1;

    private android.graphics.Bitmap mAvailThumbnailBitmap = null;

    private android.graphics.Canvas mThumbnailCanvas = null;

    private android.graphics.Bitmap createThumbnailBitmap(android.app.ActivityThread.ActivityClientRecord r) {
        android.graphics.Bitmap thumbnail = mAvailThumbnailBitmap;
        try {
            if (thumbnail == null) {
                int w = mThumbnailWidth;
                int h;
                if (w < 0) {
                    android.content.res.Resources res = r.activity.getResources();
                    int wId = com.android.internal.R.dimen.thumbnail_width;
                    int hId = com.android.internal.R.dimen.thumbnail_height;
                    mThumbnailWidth = w = res.getDimensionPixelSize(wId);
                    mThumbnailHeight = h = res.getDimensionPixelSize(hId);
                } else {
                    h = mThumbnailHeight;
                }
                // On platforms where we don't want thumbnails, set dims to (0,0)
                if ((w > 0) && (h > 0)) {
                    thumbnail = android.graphics.Bitmap.createBitmap(r.activity.getResources().getDisplayMetrics(), w, h, android.app.ActivityThread.THUMBNAIL_FORMAT);
                    thumbnail.eraseColor(0);
                }
            }
            if (thumbnail != null) {
                android.graphics.Canvas cv = mThumbnailCanvas;
                if (cv == null) {
                    mThumbnailCanvas = cv = new android.graphics.Canvas();
                }
                cv.setBitmap(thumbnail);
                if (!r.activity.onCreateThumbnail(thumbnail, cv)) {
                    mAvailThumbnailBitmap = thumbnail;
                    thumbnail = null;
                }
                cv.setBitmap(null);
            }
        } catch (java.lang.Exception e) {
            if (!mInstrumentation.onException(r.activity, e)) {
                throw new java.lang.RuntimeException((("Unable to create thumbnail of " + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
            }
            thumbnail = null;
        }
        return thumbnail;
    }

    private void handlePauseActivity(android.os.IBinder token, boolean finished, boolean userLeaving, int configChanges, boolean dontReport, int seq) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (android.app.ActivityThread.DEBUG_ORDER)
            android.util.Slog.d(android.app.ActivityThread.TAG, (("handlePauseActivity " + r) + ", seq: ") + seq);

        if (!android.app.ActivityThread.checkAndUpdateLifecycleSeq(seq, r, "pauseActivity")) {
            return;
        }
        if (r != null) {
            // Slog.v(TAG, "userLeaving=" + userLeaving + " handling pause of " + r);
            if (userLeaving) {
                performUserLeavingActivity(r);
            }
            r.activity.mConfigChangeFlags |= configChanges;
            performPauseActivity(token, finished, r.isPreHoneycomb(), "handlePauseActivity");
            // Make sure any pending writes are now committed.
            if (r.isPreHoneycomb()) {
                android.app.QueuedWork.waitToFinish();
            }
            // Tell the activity manager we have paused.
            if (!dontReport) {
                try {
                    android.app.ActivityManagerNative.getDefault().activityPaused(token);
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            }
            mSomeActivitiesChanged = true;
        }
    }

    final void performUserLeavingActivity(android.app.ActivityThread.ActivityClientRecord r) {
        mInstrumentation.callActivityOnUserLeaving(r.activity);
    }

    final android.os.Bundle performPauseActivity(android.os.IBinder token, boolean finished, boolean saveState, java.lang.String reason) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        return r != null ? performPauseActivity(r, finished, saveState, reason) : null;
    }

    final android.os.Bundle performPauseActivity(android.app.ActivityThread.ActivityClientRecord r, boolean finished, boolean saveState, java.lang.String reason) {
        if (r.paused) {
            if (r.activity.mFinished) {
                // If we are finishing, we won't call onResume() in certain cases.
                // So here we likewise don't want to call onPause() if the activity
                // isn't resumed.
                return null;
            }
            java.lang.RuntimeException e = new java.lang.RuntimeException("Performing pause of activity that is not resumed: " + r.intent.getComponent().toShortString());
            android.util.Slog.e(android.app.ActivityThread.TAG, e.getMessage(), e);
        }
        if (finished) {
            r.activity.mFinished = true;
        }
        // Next have the activity save its current state and managed dialogs...
        if ((!r.activity.mFinished) && saveState) {
            callCallActivityOnSaveInstanceState(r);
        }
        performPauseActivityIfNeeded(r, reason);
        // Notify any outstanding on paused listeners
        java.util.ArrayList<android.app.OnActivityPausedListener> listeners;
        synchronized(mOnPauseListeners) {
            listeners = mOnPauseListeners.remove(r.activity);
        }
        int size = (listeners != null) ? listeners.size() : 0;
        for (int i = 0; i < size; i++) {
            listeners.get(i).onPaused(r.activity);
        }
        return (!r.activity.mFinished) && saveState ? r.state : null;
    }

    private void performPauseActivityIfNeeded(android.app.ActivityThread.ActivityClientRecord r, java.lang.String reason) {
        if (r.paused) {
            // You are already paused silly...
            return;
        }
        try {
            r.activity.mCalled = false;
            mInstrumentation.callActivityOnPause(r.activity);
            android.util.EventLog.writeEvent(android.app.ActivityThread.LOG_AM_ON_PAUSE_CALLED, android.os.UserHandle.myUserId(), r.activity.getComponentName().getClassName(), reason);
            if (!r.activity.mCalled) {
                throw new android.util.SuperNotCalledException(("Activity " + android.app.ActivityThread.safeToComponentShortString(r.intent)) + " did not call through to super.onPause()");
            }
        } catch (android.util.SuperNotCalledException e) {
            throw e;
        } catch (java.lang.Exception e) {
            if (!mInstrumentation.onException(r.activity, e)) {
                throw new java.lang.RuntimeException((("Unable to pause activity " + android.app.ActivityThread.safeToComponentShortString(r.intent)) + ": ") + e.toString(), e);
            }
        }
        r.paused = true;
    }

    final void performStopActivity(android.os.IBinder token, boolean saveState, java.lang.String reason) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        performStopActivityInner(r, null, false, saveState, reason);
    }

    private static class StopInfo implements java.lang.Runnable {
        android.app.ActivityThread.ActivityClientRecord activity;

        android.os.Bundle state;

        android.os.PersistableBundle persistentState;

        java.lang.CharSequence description;

        @java.lang.Override
        public void run() {
            // Tell activity manager we have been stopped.
            try {
                if (android.app.ActivityThread.DEBUG_MEMORY_TRIM)
                    android.util.Slog.v(android.app.ActivityThread.TAG, "Reporting activity stopped: " + activity);

                android.app.ActivityManagerNative.getDefault().activityStopped(activity.token, state, persistentState, description);
            } catch (android.os.RemoteException ex) {
                if ((ex instanceof android.os.TransactionTooLargeException) && (activity.packageInfo.getTargetSdkVersion() < android.os.Build.VERSION_CODES.N)) {
                    android.util.Log.e(android.app.ActivityThread.TAG, "App sent too much data in instance state, so it was ignored", ex);
                    return;
                }
                throw ex.rethrowFromSystemServer();
            }
        }
    }

    private static final class ProviderRefCount {
        public final android.app.IActivityManager.ContentProviderHolder holder;

        public final android.app.ActivityThread.ProviderClientRecord client;

        public int stableCount;

        public int unstableCount;

        // When this is set, the stable and unstable ref counts are 0 and
        // we have a pending operation scheduled to remove the ref count
        // from the activity manager.  On the activity manager we are still
        // holding an unstable ref, though it is not reflected in the counts
        // here.
        public boolean removePending;

        ProviderRefCount(android.app.IActivityManager.ContentProviderHolder inHolder, android.app.ActivityThread.ProviderClientRecord inClient, int sCount, int uCount) {
            holder = inHolder;
            client = inClient;
            stableCount = sCount;
            unstableCount = uCount;
        }
    }

    /**
     * Core implementation of stopping an activity.  Note this is a little
     * tricky because the server's meaning of stop is slightly different
     * than our client -- for the server, stop means to save state and give
     * it the result when it is done, but the window may still be visible.
     * For the client, we want to call onStop()/onStart() to indicate when
     * the activity's UI visibility changes.
     */
    private void performStopActivityInner(android.app.ActivityThread.ActivityClientRecord r, android.app.ActivityThread.StopInfo info, boolean keepShown, boolean saveState, java.lang.String reason) {
        if (android.app.ActivityThread.localLOGV)
            android.util.Slog.v(android.app.ActivityThread.TAG, "Performing stop of " + r);

        if (r != null) {
            if ((!keepShown) && r.stopped) {
                if (r.activity.mFinished) {
                    // If we are finishing, we won't call onResume() in certain
                    // cases.  So here we likewise don't want to call onStop()
                    // if the activity isn't resumed.
                    return;
                }
                java.lang.RuntimeException e = new java.lang.RuntimeException("Performing stop of activity that is already stopped: " + r.intent.getComponent().toShortString());
                android.util.Slog.e(android.app.ActivityThread.TAG, e.getMessage(), e);
                android.util.Slog.e(android.app.ActivityThread.TAG, r.getStateString());
            }
            // One must first be paused before stopped...
            performPauseActivityIfNeeded(r, reason);
            if (info != null) {
                try {
                    // First create a thumbnail for the activity...
                    // For now, don't create the thumbnail here; we are
                    // doing that by doing a screen snapshot.
                    info.description = r.activity.onCreateDescription();
                } catch (java.lang.Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new java.lang.RuntimeException((("Unable to save state of activity " + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
                    }
                }
            }
            // Next have the activity save its current state and managed dialogs...
            if ((!r.activity.mFinished) && saveState) {
                if (r.state == null) {
                    callCallActivityOnSaveInstanceState(r);
                }
            }
            if (!keepShown) {
                try {
                    // Now we are idle.
                    /* preserveWindow */
                    r.activity.performStop(false);
                } catch (java.lang.Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new java.lang.RuntimeException((("Unable to stop activity " + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
                    }
                }
                r.stopped = true;
                android.util.EventLog.writeEvent(android.app.ActivityThread.LOG_AM_ON_STOP_CALLED, android.os.UserHandle.myUserId(), r.activity.getComponentName().getClassName(), reason);
            }
        }
    }

    private void updateVisibility(android.app.ActivityThread.ActivityClientRecord r, boolean show) {
        android.view.View v = r.activity.mDecor;
        if (v != null) {
            if (show) {
                if (!r.activity.mVisibleFromServer) {
                    r.activity.mVisibleFromServer = true;
                    mNumVisibleActivities++;
                    if (r.activity.mVisibleFromClient) {
                        r.activity.makeVisible();
                    }
                }
                if (r.newConfig != null) {
                    performConfigurationChangedForActivity(r, r.newConfig, android.app.ActivityThread.REPORT_TO_ACTIVITY);
                    if (android.app.ActivityThread.DEBUG_CONFIGURATION)
                        android.util.Slog.v(android.app.ActivityThread.TAG, (("Updating activity vis " + r.activityInfo.name) + " with new config ") + r.activity.mCurrentConfig);

                    r.newConfig = null;
                }
            } else {
                if (r.activity.mVisibleFromServer) {
                    r.activity.mVisibleFromServer = false;
                    mNumVisibleActivities--;
                    v.setVisibility(android.view.View.INVISIBLE);
                }
            }
        }
    }

    private void handleStopActivity(android.os.IBinder token, boolean show, int configChanges, int seq) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (!android.app.ActivityThread.checkAndUpdateLifecycleSeq(seq, r, "stopActivity")) {
            return;
        }
        r.activity.mConfigChangeFlags |= configChanges;
        android.app.ActivityThread.StopInfo info = new android.app.ActivityThread.StopInfo();
        performStopActivityInner(r, info, show, true, "handleStopActivity");
        if (android.app.ActivityThread.localLOGV)
            android.util.Slog.v(android.app.ActivityThread.TAG, (((("Finishing stop of " + r) + ": show=") + show) + " win=") + r.window);

        updateVisibility(r, show);
        // Make sure any pending writes are now committed.
        if (!r.isPreHoneycomb()) {
            android.app.QueuedWork.waitToFinish();
        }
        // Schedule the call to tell the activity manager we have
        // stopped.  We don't do this immediately, because we want to
        // have a chance for any other pending work (in particular memory
        // trim requests) to complete before you tell the activity
        // manager to proceed and allow us to go fully into the background.
        info.activity = r;
        info.state = r.state;
        info.persistentState = r.persistentState;
        mH.post(info);
        mSomeActivitiesChanged = true;
    }

    private static boolean checkAndUpdateLifecycleSeq(int seq, android.app.ActivityThread.ActivityClientRecord r, java.lang.String action) {
        if (r == null) {
            return true;
        }
        if (seq < r.lastProcessedSeq) {
            if (android.app.ActivityThread.DEBUG_ORDER)
                android.util.Slog.d(android.app.ActivityThread.TAG, (((((action + " for ") + r) + " ignored, because seq=") + seq) + " < mCurrentLifecycleSeq=") + r.lastProcessedSeq);

            return false;
        }
        r.lastProcessedSeq = seq;
        return true;
    }

    final void performRestartActivity(android.os.IBinder token) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r.stopped) {
            r.activity.performRestart();
            r.stopped = false;
        }
    }

    private void handleWindowVisibility(android.os.IBinder token, boolean show) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r == null) {
            android.util.Log.w(android.app.ActivityThread.TAG, "handleWindowVisibility: no activity for token " + token);
            return;
        }
        if ((!show) && (!r.stopped)) {
            performStopActivityInner(r, null, show, false, "handleWindowVisibility");
        } else
            if (show && r.stopped) {
                // If we are getting ready to gc after going to the background, well
                // we are back active so skip it.
                unscheduleGcIdler();
                r.activity.performRestart();
                r.stopped = false;
            }

        if (r.activity.mDecor != null) {
            if (false)
                android.util.Slog.v(android.app.ActivityThread.TAG, (("Handle window " + r) + " visibility: ") + show);

            updateVisibility(r, show);
        }
        mSomeActivitiesChanged = true;
    }

    // TODO: This method should be changed to use {@link #performStopActivityInner} to perform to
    // stop operation on the activity to reduce code duplication and the chance of fixing a bug in
    // one place and missing the other.
    private void handleSleeping(android.os.IBinder token, boolean sleeping) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        if (r == null) {
            android.util.Log.w(android.app.ActivityThread.TAG, "handleSleeping: no activity for token " + token);
            return;
        }
        if (sleeping) {
            if ((!r.stopped) && (!r.isPreHoneycomb())) {
                if ((!r.activity.mFinished) && (r.state == null)) {
                    callCallActivityOnSaveInstanceState(r);
                }
                try {
                    // Now we are idle.
                    /* preserveWindow */
                    r.activity.performStop(false);
                } catch (java.lang.Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new java.lang.RuntimeException((("Unable to stop activity " + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
                    }
                }
                r.stopped = true;
                android.util.EventLog.writeEvent(android.app.ActivityThread.LOG_AM_ON_STOP_CALLED, android.os.UserHandle.myUserId(), r.activity.getComponentName().getClassName(), "sleeping");
            }
            // Make sure any pending writes are now committed.
            if (!r.isPreHoneycomb()) {
                android.app.QueuedWork.waitToFinish();
            }
            // Tell activity manager we slept.
            try {
                android.app.ActivityManagerNative.getDefault().activitySlept(r.token);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        } else {
            if (r.stopped && r.activity.mVisibleFromServer) {
                r.activity.performRestart();
                r.stopped = false;
            }
        }
    }

    private void handleSetCoreSettings(android.os.Bundle coreSettings) {
        synchronized(mResourcesManager) {
            mCoreSettings = coreSettings;
        }
        onCoreSettingsChange();
    }

    private void onCoreSettingsChange() {
        boolean debugViewAttributes = mCoreSettings.getInt(android.provider.Settings.Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0;
        if (debugViewAttributes != android.view.View.mDebugViewAttributes) {
            android.view.View.mDebugViewAttributes = debugViewAttributes;
            // request all activities to relaunch for the changes to take place
            for (java.util.Map.Entry<android.os.IBinder, android.app.ActivityThread.ActivityClientRecord> entry : mActivities.entrySet()) {
                /* preserveWindow */
                requestRelaunchActivity(entry.getKey(), null, null, 0, false, null, null, false, false);
            }
        }
    }

    private void handleUpdatePackageCompatibilityInfo(android.app.ActivityThread.UpdateCompatibilityData data) {
        android.app.LoadedApk apk = peekPackageInfo(data.pkg, false);
        if (apk != null) {
            apk.setCompatibilityInfo(data.info);
        }
        apk = peekPackageInfo(data.pkg, true);
        if (apk != null) {
            apk.setCompatibilityInfo(data.info);
        }
        handleConfigurationChanged(mConfiguration, data.info);
        android.view.WindowManagerGlobal.getInstance().reportNewConfiguration(mConfiguration);
    }

    private void deliverResults(android.app.ActivityThread.ActivityClientRecord r, java.util.List<android.app.ResultInfo> results) {
        final int N = results.size();
        for (int i = 0; i < N; i++) {
            android.app.ResultInfo ri = results.get(i);
            try {
                if (ri.mData != null) {
                    ri.mData.setExtrasClassLoader(r.activity.getClassLoader());
                    ri.mData.prepareToEnterProcess();
                }
                if (android.app.ActivityThread.DEBUG_RESULTS)
                    android.util.Slog.v(android.app.ActivityThread.TAG, (("Delivering result to activity " + r) + " : ") + ri);

                r.activity.dispatchActivityResult(ri.mResultWho, ri.mRequestCode, ri.mResultCode, ri.mData);
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new java.lang.RuntimeException((((("Failure delivering result " + ri) + " to activity ") + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
                }
            }
        }
    }

    private void handleSendResult(android.app.ActivityThread.ResultData res) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(res.token);
        if (android.app.ActivityThread.DEBUG_RESULTS)
            android.util.Slog.v(android.app.ActivityThread.TAG, "Handling send result to " + r);

        if (r != null) {
            final boolean resumed = !r.paused;
            if ((((!r.activity.mFinished) && (r.activity.mDecor != null)) && r.hideForNow) && resumed) {
                // We had hidden the activity because it started another
                // one...  we have gotten a result back and we are not
                // paused, so make sure our window is visible.
                updateVisibility(r, true);
            }
            if (resumed) {
                try {
                    // Now we are idle.
                    r.activity.mCalled = false;
                    r.activity.mTemporaryPause = true;
                    mInstrumentation.callActivityOnPause(r.activity);
                    if (!r.activity.mCalled) {
                        throw new android.util.SuperNotCalledException(("Activity " + r.intent.getComponent().toShortString()) + " did not call through to super.onPause()");
                    }
                } catch (android.util.SuperNotCalledException e) {
                    throw e;
                } catch (java.lang.Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new java.lang.RuntimeException((("Unable to pause activity " + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
                    }
                }
            }
            deliverResults(r, res.results);
            if (resumed) {
                r.activity.performResume();
                r.activity.mTemporaryPause = false;
            }
        }
    }

    public final android.app.ActivityThread.ActivityClientRecord performDestroyActivity(android.os.IBinder token, boolean finishing) {
        return performDestroyActivity(token, finishing, 0, false);
    }

    private android.app.ActivityThread.ActivityClientRecord performDestroyActivity(android.os.IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(token);
        java.lang.Class<? extends android.app.Activity> activityClass = null;
        if (android.app.ActivityThread.localLOGV)
            android.util.Slog.v(android.app.ActivityThread.TAG, "Performing finish of " + r);

        if (r != null) {
            activityClass = r.activity.getClass();
            r.activity.mConfigChangeFlags |= configChanges;
            if (finishing) {
                r.activity.mFinished = true;
            }
            performPauseActivityIfNeeded(r, "destroy");
            if (!r.stopped) {
                try {
                    r.activity.performStop(r.mPreserveWindow);
                } catch (android.util.SuperNotCalledException e) {
                    throw e;
                } catch (java.lang.Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new java.lang.RuntimeException((("Unable to stop activity " + android.app.ActivityThread.safeToComponentShortString(r.intent)) + ": ") + e.toString(), e);
                    }
                }
                r.stopped = true;
                android.util.EventLog.writeEvent(android.app.ActivityThread.LOG_AM_ON_STOP_CALLED, android.os.UserHandle.myUserId(), r.activity.getComponentName().getClassName(), "destroy");
            }
            if (getNonConfigInstance) {
                try {
                    r.lastNonConfigurationInstances = r.activity.retainNonConfigurationInstances();
                } catch (java.lang.Exception e) {
                    if (!mInstrumentation.onException(r.activity, e)) {
                        throw new java.lang.RuntimeException((("Unable to retain activity " + r.intent.getComponent().toShortString()) + ": ") + e.toString(), e);
                    }
                }
            }
            try {
                r.activity.mCalled = false;
                mInstrumentation.callActivityOnDestroy(r.activity);
                if (!r.activity.mCalled) {
                    throw new android.util.SuperNotCalledException(("Activity " + android.app.ActivityThread.safeToComponentShortString(r.intent)) + " did not call through to super.onDestroy()");
                }
                if (r.window != null) {
                    r.window.closeAllPanels();
                }
            } catch (android.util.SuperNotCalledException e) {
                throw e;
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(r.activity, e)) {
                    throw new java.lang.RuntimeException((("Unable to destroy activity " + android.app.ActivityThread.safeToComponentShortString(r.intent)) + ": ") + e.toString(), e);
                }
            }
        }
        mActivities.remove(token);
        android.os.StrictMode.decrementExpectedActivityCount(activityClass);
        return r;
    }

    private static java.lang.String safeToComponentShortString(android.content.Intent intent) {
        android.content.ComponentName component = intent.getComponent();
        return component == null ? "[Unknown]" : component.toShortString();
    }

    private void handleDestroyActivity(android.os.IBinder token, boolean finishing, int configChanges, boolean getNonConfigInstance) {
        android.app.ActivityThread.ActivityClientRecord r = performDestroyActivity(token, finishing, configChanges, getNonConfigInstance);
        if (r != null) {
            android.app.ActivityThread.cleanUpPendingRemoveWindows(r, finishing);
            android.view.WindowManager wm = r.activity.getWindowManager();
            android.view.View v = r.activity.mDecor;
            if (v != null) {
                if (r.activity.mVisibleFromServer) {
                    mNumVisibleActivities--;
                }
                android.os.IBinder wtoken = v.getWindowToken();
                if (r.activity.mWindowAdded) {
                    if (r.mPreserveWindow) {
                        // Hold off on removing this until the new activity's
                        // window is being added.
                        r.mPendingRemoveWindow = r.window;
                        r.mPendingRemoveWindowManager = wm;
                        // We can only keep the part of the view hierarchy that we control,
                        // everything else must be removed, because it might not be able to
                        // behave properly when activity is relaunching.
                        r.window.clearContentView();
                    } else {
                        wm.removeViewImmediate(v);
                    }
                }
                if ((wtoken != null) && (r.mPendingRemoveWindow == null)) {
                    android.view.WindowManagerGlobal.getInstance().closeAll(wtoken, r.activity.getClass().getName(), "Activity");
                } else
                    if (r.mPendingRemoveWindow != null) {
                        // We're preserving only one window, others should be closed so app views
                        // will be detached before the final tear down. It should be done now because
                        // some components (e.g. WebView) rely on detach callbacks to perform receiver
                        // unregister and other cleanup.
                        android.view.WindowManagerGlobal.getInstance().closeAllExceptView(token, v, r.activity.getClass().getName(), "Activity");
                    }

                r.activity.mDecor = null;
            }
            if (r.mPendingRemoveWindow == null) {
                // If we are delaying the removal of the activity window, then
                // we can't clean up all windows here.  Note that we can't do
                // so later either, which means any windows that aren't closed
                // by the app will leak.  Well we try to warning them a lot
                // about leaking windows, because that is a bug, so if they are
                // using this recreate facility then they get to live with leaks.
                android.view.WindowManagerGlobal.getInstance().closeAll(token, r.activity.getClass().getName(), "Activity");
            }
            // Mocked out contexts won't be participating in the normal
            // process lifecycle, but if we're running with a proper
            // ApplicationContext we need to have it tear down things
            // cleanly.
            android.content.Context c = r.activity.getBaseContext();
            if (c instanceof android.app.ContextImpl) {
                ((android.app.ContextImpl) (c)).scheduleFinalCleanup(r.activity.getClass().getName(), "Activity");
            }
        }
        if (finishing) {
            try {
                android.app.ActivityManagerNative.getDefault().activityDestroyed(token);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
        }
        mSomeActivitiesChanged = true;
    }

    /**
     *
     *
     * @param preserveWindow
     * 		Whether the activity should try to reuse the window it created,
     * 		including the decor view after the relaunch.
     */
    public final void requestRelaunchActivity(android.os.IBinder token, java.util.List<android.app.ResultInfo> pendingResults, java.util.List<com.android.internal.content.ReferrerIntent> pendingNewIntents, int configChanges, boolean notResumed, android.content.res.Configuration config, android.content.res.Configuration overrideConfig, boolean fromServer, boolean preserveWindow) {
        android.app.ActivityThread.ActivityClientRecord target = null;
        synchronized(mResourcesManager) {
            for (int i = 0; i < mRelaunchingActivities.size(); i++) {
                android.app.ActivityThread.ActivityClientRecord r = mRelaunchingActivities.get(i);
                if (android.app.ActivityThread.DEBUG_ORDER)
                    android.util.Slog.d(android.app.ActivityThread.TAG, (("requestRelaunchActivity: " + this) + ", trying: ") + r);

                if (r.token == token) {
                    target = r;
                    if (pendingResults != null) {
                        if (r.pendingResults != null) {
                            r.pendingResults.addAll(pendingResults);
                        } else {
                            r.pendingResults = pendingResults;
                        }
                    }
                    if (pendingNewIntents != null) {
                        if (r.pendingIntents != null) {
                            r.pendingIntents.addAll(pendingNewIntents);
                        } else {
                            r.pendingIntents = pendingNewIntents;
                        }
                    }
                    // For each relaunch request, activity manager expects an answer
                    if ((!r.onlyLocalRequest) && fromServer) {
                        try {
                            android.app.ActivityManagerNative.getDefault().activityRelaunched(token);
                        } catch (android.os.RemoteException e) {
                            throw e.rethrowFromSystemServer();
                        }
                    }
                    break;
                }
            }
            if (target == null) {
                if (android.app.ActivityThread.DEBUG_ORDER)
                    android.util.Slog.d(android.app.ActivityThread.TAG, "requestRelaunchActivity: target is null, fromServer:" + fromServer);

                target = new android.app.ActivityThread.ActivityClientRecord();
                target.token = token;
                target.pendingResults = pendingResults;
                target.pendingIntents = pendingNewIntents;
                target.mPreserveWindow = preserveWindow;
                if (!fromServer) {
                    final android.app.ActivityThread.ActivityClientRecord existing = mActivities.get(token);
                    if (android.app.ActivityThread.DEBUG_ORDER)
                        android.util.Slog.d(android.app.ActivityThread.TAG, "requestRelaunchActivity: " + existing);

                    if (existing != null) {
                        if (android.app.ActivityThread.DEBUG_ORDER)
                            android.util.Slog.d(android.app.ActivityThread.TAG, "requestRelaunchActivity: paused= " + existing.paused);

                        target.startsNotResumed = existing.paused;
                        target.overrideConfig = existing.overrideConfig;
                    }
                    target.onlyLocalRequest = true;
                }
                mRelaunchingActivities.add(target);
                sendMessage(android.app.ActivityThread.H.RELAUNCH_ACTIVITY, target);
            }
            if (fromServer) {
                target.startsNotResumed = notResumed;
                target.onlyLocalRequest = false;
            }
            if (config != null) {
                target.createdConfig = config;
            }
            if (overrideConfig != null) {
                target.overrideConfig = overrideConfig;
            }
            target.pendingConfigChanges |= configChanges;
            target.relaunchSeq = getLifecycleSeq();
        }
        if (android.app.ActivityThread.DEBUG_ORDER)
            android.util.Slog.d(android.app.ActivityThread.TAG, (((("relaunchActivity " + this) + ", target ") + target) + " operation received seq: ") + target.relaunchSeq);

    }

    private void handleRelaunchActivity(android.app.ActivityThread.ActivityClientRecord tmp) {
        // If we are getting ready to gc after going to the background, well
        // we are back active so skip it.
        unscheduleGcIdler();
        mSomeActivitiesChanged = true;
        android.content.res.Configuration changedConfig = null;
        int configChanges = 0;
        // First: make sure we have the most recent configuration and most
        // recent version of the activity, or skip it if some previous call
        // had taken a more recent version.
        synchronized(mResourcesManager) {
            int N = mRelaunchingActivities.size();
            android.os.IBinder token = tmp.token;
            tmp = null;
            for (int i = 0; i < N; i++) {
                android.app.ActivityThread.ActivityClientRecord r = mRelaunchingActivities.get(i);
                if (r.token == token) {
                    tmp = r;
                    configChanges |= tmp.pendingConfigChanges;
                    mRelaunchingActivities.remove(i);
                    i--;
                    N--;
                }
            }
            if (tmp == null) {
                if (android.app.ActivityThread.DEBUG_CONFIGURATION)
                    android.util.Slog.v(android.app.ActivityThread.TAG, "Abort, activity not relaunching!");

                return;
            }
            if (android.app.ActivityThread.DEBUG_CONFIGURATION)
                android.util.Slog.v(android.app.ActivityThread.TAG, (("Relaunching activity " + tmp.token) + " with configChanges=0x") + java.lang.Integer.toHexString(configChanges));

            if (mPendingConfiguration != null) {
                changedConfig = mPendingConfiguration;
                mPendingConfiguration = null;
            }
        }
        if (tmp.lastProcessedSeq > tmp.relaunchSeq) {
            android.util.Slog.wtf(android.app.ActivityThread.TAG, (((("For some reason target: " + tmp) + " has lower sequence: ") + tmp.relaunchSeq) + " than current sequence: ") + tmp.lastProcessedSeq);
        } else {
            tmp.lastProcessedSeq = tmp.relaunchSeq;
        }
        if (tmp.createdConfig != null) {
            // If the activity manager is passing us its current config,
            // assume that is really what we want regardless of what we
            // may have pending.
            if ((mConfiguration == null) || (tmp.createdConfig.isOtherSeqNewer(mConfiguration) && (mConfiguration.diff(tmp.createdConfig) != 0))) {
                if ((changedConfig == null) || tmp.createdConfig.isOtherSeqNewer(changedConfig)) {
                    changedConfig = tmp.createdConfig;
                }
            }
        }
        if (android.app.ActivityThread.DEBUG_CONFIGURATION)
            android.util.Slog.v(android.app.ActivityThread.TAG, (("Relaunching activity " + tmp.token) + ": changedConfig=") + changedConfig);

        // If there was a pending configuration change, execute it first.
        if (changedConfig != null) {
            mCurDefaultDisplayDpi = changedConfig.densityDpi;
            updateDefaultDensity();
            handleConfigurationChanged(changedConfig, null);
        }
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(tmp.token);
        if (android.app.ActivityThread.DEBUG_CONFIGURATION)
            android.util.Slog.v(android.app.ActivityThread.TAG, "Handling relaunch of " + r);

        if (r == null) {
            if (!tmp.onlyLocalRequest) {
                try {
                    android.app.ActivityManagerNative.getDefault().activityRelaunched(tmp.token);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
            return;
        }
        r.activity.mConfigChangeFlags |= configChanges;
        r.onlyLocalRequest = tmp.onlyLocalRequest;
        r.mPreserveWindow = tmp.mPreserveWindow;
        r.lastProcessedSeq = tmp.lastProcessedSeq;
        r.relaunchSeq = tmp.relaunchSeq;
        android.content.Intent currentIntent = r.activity.mIntent;
        r.activity.mChangingConfigurations = true;
        // If we are preserving the main window across relaunches we would also like to preserve
        // the children. However the client side view system does not support preserving
        // the child views so we notify the window manager to expect these windows to
        // be replaced and defer requests to destroy or hide them. This way we can achieve
        // visual continuity. It's important that we do this here prior to pause and destroy
        // as that is when we may hide or remove the child views.
        // 
        // There is another scenario, if we have decided locally to relaunch the app from a
        // call to recreate, then none of the windows will be prepared for replacement or
        // preserved by the server, so we want to notify it that we are preparing to replace
        // everything
        try {
            if (r.mPreserveWindow || r.onlyLocalRequest) {
                android.view.WindowManagerGlobal.getWindowSession().prepareToReplaceWindows(r.token, !r.onlyLocalRequest);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
        // Need to ensure state is saved.
        if (!r.paused) {
            performPauseActivity(r.token, false, r.isPreHoneycomb(), "handleRelaunchActivity");
        }
        if (((r.state == null) && (!r.stopped)) && (!r.isPreHoneycomb())) {
            callCallActivityOnSaveInstanceState(r);
        }
        handleDestroyActivity(r.token, false, configChanges, true);
        r.activity = null;
        r.window = null;
        r.hideForNow = false;
        r.nextIdle = null;
        // Merge any pending results and pending intents; don't just replace them
        if (tmp.pendingResults != null) {
            if (r.pendingResults == null) {
                r.pendingResults = tmp.pendingResults;
            } else {
                r.pendingResults.addAll(tmp.pendingResults);
            }
        }
        if (tmp.pendingIntents != null) {
            if (r.pendingIntents == null) {
                r.pendingIntents = tmp.pendingIntents;
            } else {
                r.pendingIntents.addAll(tmp.pendingIntents);
            }
        }
        r.startsNotResumed = tmp.startsNotResumed;
        r.overrideConfig = tmp.overrideConfig;
        handleLaunchActivity(r, currentIntent, "handleRelaunchActivity");
        if (!tmp.onlyLocalRequest) {
            try {
                android.app.ActivityManagerNative.getDefault().activityRelaunched(r.token);
                if (r.window != null) {
                    r.window.reportActivityRelaunched();
                }
            } catch (android.os.RemoteException e) {
                throw e.rethrowFromSystemServer();
            }
        }
    }

    private void callCallActivityOnSaveInstanceState(android.app.ActivityThread.ActivityClientRecord r) {
        r.state = new android.os.Bundle();
        r.state.setAllowFds(false);
        if (r.isPersistable()) {
            r.persistentState = new android.os.PersistableBundle();
            mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state, r.persistentState);
        } else {
            mInstrumentation.callActivityOnSaveInstanceState(r.activity, r.state);
        }
    }

    java.util.ArrayList<android.content.ComponentCallbacks2> collectComponentCallbacks(boolean allActivities, android.content.res.Configuration newConfig) {
        java.util.ArrayList<android.content.ComponentCallbacks2> callbacks = new java.util.ArrayList<android.content.ComponentCallbacks2>();
        synchronized(mResourcesManager) {
            final int NAPP = mAllApplications.size();
            for (int i = 0; i < NAPP; i++) {
                callbacks.add(mAllApplications.get(i));
            }
            final int NACT = mActivities.size();
            for (int i = 0; i < NACT; i++) {
                android.app.ActivityThread.ActivityClientRecord ar = mActivities.valueAt(i);
                android.app.Activity a = ar.activity;
                if (a != null) {
                    android.content.res.Configuration thisConfig = applyConfigCompatMainThread(mCurDefaultDisplayDpi, newConfig, ar.packageInfo.getCompatibilityInfo());
                    if ((!ar.activity.mFinished) && (allActivities || (!ar.paused))) {
                        // If the activity is currently resumed, its configuration
                        // needs to change right now.
                        callbacks.add(a);
                    } else
                        if (thisConfig != null) {
                            // Otherwise, we will tell it about the change
                            // the next time it is resumed or shown.  Note that
                            // the activity manager may, before then, decide the
                            // activity needs to be destroyed to handle its new
                            // configuration.
                            if (android.app.ActivityThread.DEBUG_CONFIGURATION) {
                                android.util.Slog.v(android.app.ActivityThread.TAG, (("Setting activity " + ar.activityInfo.name) + " newConfig=") + thisConfig);
                            }
                            ar.newConfig = thisConfig;
                        }

                }
            }
            final int NSVC = mServices.size();
            for (int i = 0; i < NSVC; i++) {
                callbacks.add(mServices.valueAt(i));
            }
        }
        synchronized(mProviderMap) {
            final int NPRV = mLocalProviders.size();
            for (int i = 0; i < NPRV; i++) {
                callbacks.add(mLocalProviders.valueAt(i).mLocalProvider);
            }
        }
        return callbacks;
    }

    /**
     * Updates the configuration for an Activity. The ActivityClientRecord's
     * {@link ActivityClientRecord#overrideConfig} is used to compute the final Configuration for
     * that Activity. {@link ActivityClientRecord#tmpConfig} is used as a temporary for delivering
     * the updated Configuration.
     *
     * @param r
     * 		ActivityClientRecord representing the Activity.
     * @param newBaseConfig
     * 		The new configuration to use. This may be augmented with
     * 		{@link ActivityClientRecord#overrideConfig}.
     * @param reportToActivity
     * 		true if the change should be reported to the Activity's callback.
     */
    private void performConfigurationChangedForActivity(android.app.ActivityThread.ActivityClientRecord r, android.content.res.Configuration newBaseConfig, boolean reportToActivity) {
        r.tmpConfig.setTo(newBaseConfig);
        if (r.overrideConfig != null) {
            r.tmpConfig.updateFrom(r.overrideConfig);
        }
        performConfigurationChanged(r.activity, r.token, r.tmpConfig, r.overrideConfig, reportToActivity);
        android.app.ActivityThread.freeTextLayoutCachesIfNeeded(r.activity.mCurrentConfig.diff(r.tmpConfig));
    }

    /**
     * Creates a new Configuration only if override would modify base. Otherwise returns base.
     *
     * @param base
     * 		The base configuration.
     * @param override
     * 		The update to apply to the base configuration. Can be null.
     * @return A Configuration representing base with override applied.
     */
    private static android.content.res.Configuration createNewConfigAndUpdateIfNotNull(@android.annotation.NonNull
    android.content.res.Configuration base, @android.annotation.Nullable
    android.content.res.Configuration override) {
        if (override == null) {
            return base;
        }
        android.content.res.Configuration newConfig = new android.content.res.Configuration(base);
        newConfig.updateFrom(override);
        return newConfig;
    }

    /**
     * Decides whether to update an Activity's configuration and whether to tell the
     * Activity/Component about it.
     *
     * @param cb
     * 		The component callback to notify of configuration change.
     * @param activityToken
     * 		The Activity binder token for which this configuration change happened.
     * 		If the change is global, this is null.
     * @param newConfig
     * 		The new configuration.
     * @param amOverrideConfig
     * 		The override config that differentiates the Activity's configuration
     * 		from the base global configuration.
     * 		This is supplied by ActivityManager.
     * @param reportToActivity
     * 		Notify the Activity of the change.
     */
    private void performConfigurationChanged(android.content.ComponentCallbacks2 cb, android.os.IBinder activityToken, android.content.res.Configuration newConfig, android.content.res.Configuration amOverrideConfig, boolean reportToActivity) {
        // Only for Activity objects, check that they actually call up to their
        // superclass implementation.  ComponentCallbacks2 is an interface, so
        // we check the runtime type and act accordingly.
        android.app.Activity activity = (cb instanceof android.app.Activity) ? ((android.app.Activity) (cb)) : null;
        if (activity != null) {
            activity.mCalled = false;
        }
        boolean shouldChangeConfig = false;
        if ((activity == null) || (activity.mCurrentConfig == null)) {
            shouldChangeConfig = true;
        } else {
            // If the new config is the same as the config this Activity is already
            // running with and the override config also didn't change, then don't
            // bother calling onConfigurationChanged.
            int diff = activity.mCurrentConfig.diff(newConfig);
            if ((diff != 0) || (!mResourcesManager.isSameResourcesOverrideConfig(activityToken, amOverrideConfig))) {
                // Always send the task-level config changes. For system-level configuration, if
                // this activity doesn't handle any of the config changes, then don't bother
                // calling onConfigurationChanged as we're going to destroy it.
                if (((!mUpdatingSystemConfig) || (((~activity.mActivityInfo.getRealConfigChanged()) & diff) == 0)) || (!reportToActivity)) {
                    shouldChangeConfig = true;
                }
            }
        }
        if (shouldChangeConfig) {
            // Propagate the configuration change to the Activity and ResourcesManager.
            // ContextThemeWrappers may override the configuration for that context.
            // We must check and apply any overrides defined.
            android.content.res.Configuration contextThemeWrapperOverrideConfig = null;
            if (cb instanceof android.view.ContextThemeWrapper) {
                final android.view.ContextThemeWrapper contextThemeWrapper = ((android.view.ContextThemeWrapper) (cb));
                contextThemeWrapperOverrideConfig = contextThemeWrapper.getOverrideConfiguration();
            }
            // We only update an Activity's configuration if this is not a global
            // configuration change. This must also be done before the callback,
            // or else we violate the contract that the new resources are available
            // in {@link ComponentCallbacks2#onConfigurationChanged(Configuration)}.
            if (activityToken != null) {
                // Apply the ContextThemeWrapper override if necessary.
                // NOTE: Make sure the configurations are not modified, as they are treated
                // as immutable in many places.
                final android.content.res.Configuration finalOverrideConfig = android.app.ActivityThread.createNewConfigAndUpdateIfNotNull(amOverrideConfig, contextThemeWrapperOverrideConfig);
                mResourcesManager.updateResourcesForActivity(activityToken, finalOverrideConfig);
            }
            if (reportToActivity) {
                // Apply the ContextThemeWrapper override if necessary.
                // NOTE: Make sure the configurations are not modified, as they are treated
                // as immutable in many places.
                final android.content.res.Configuration configToReport = android.app.ActivityThread.createNewConfigAndUpdateIfNotNull(newConfig, contextThemeWrapperOverrideConfig);
                cb.onConfigurationChanged(configToReport);
            }
            if (activity != null) {
                if (reportToActivity && (!activity.mCalled)) {
                    throw new android.util.SuperNotCalledException(("Activity " + activity.getLocalClassName()) + " did not call through to super.onConfigurationChanged()");
                }
                activity.mConfigChangeFlags = 0;
                activity.mCurrentConfig = new android.content.res.Configuration(newConfig);
            }
        }
    }

    public final void applyConfigurationToResources(android.content.res.Configuration config) {
        synchronized(mResourcesManager) {
            mResourcesManager.applyConfigurationToResourcesLocked(config, null);
        }
    }

    final android.content.res.Configuration applyCompatConfiguration(int displayDensity) {
        android.content.res.Configuration config = mConfiguration;
        if (mCompatConfiguration == null) {
            mCompatConfiguration = new android.content.res.Configuration();
        }
        mCompatConfiguration.setTo(mConfiguration);
        if (mResourcesManager.applyCompatConfigurationLocked(displayDensity, mCompatConfiguration)) {
            config = mCompatConfiguration;
        }
        return config;
    }

    final void handleConfigurationChanged(android.content.res.Configuration config, android.content.res.CompatibilityInfo compat) {
        int configDiff = 0;
        synchronized(mResourcesManager) {
            if (mPendingConfiguration != null) {
                if (!mPendingConfiguration.isOtherSeqNewer(config)) {
                    config = mPendingConfiguration;
                    mCurDefaultDisplayDpi = config.densityDpi;
                    updateDefaultDensity();
                }
                mPendingConfiguration = null;
            }
            if (config == null) {
                return;
            }
            if (android.app.ActivityThread.DEBUG_CONFIGURATION)
                android.util.Slog.v(android.app.ActivityThread.TAG, "Handle configuration changed: " + config);

            mResourcesManager.applyConfigurationToResourcesLocked(config, compat);
            updateLocaleListFromAppContext(mInitialApplication.getApplicationContext(), mResourcesManager.getConfiguration().getLocales());
            if (mConfiguration == null) {
                mConfiguration = new android.content.res.Configuration();
            }
            if ((!mConfiguration.isOtherSeqNewer(config)) && (compat == null)) {
                return;
            }
            configDiff = mConfiguration.updateFrom(config);
            config = applyCompatConfiguration(mCurDefaultDisplayDpi);
            final android.content.res.Resources.Theme systemTheme = getSystemContext().getTheme();
            if ((systemTheme.getChangingConfigurations() & configDiff) != 0) {
                systemTheme.rebase();
            }
        }
        java.util.ArrayList<android.content.ComponentCallbacks2> callbacks = collectComponentCallbacks(false, config);
        android.app.ActivityThread.freeTextLayoutCachesIfNeeded(configDiff);
        if (callbacks != null) {
            final int N = callbacks.size();
            for (int i = 0; i < N; i++) {
                android.content.ComponentCallbacks2 cb = callbacks.get(i);
                if (cb instanceof android.app.Activity) {
                    // If callback is an Activity - call corresponding method to consider override
                    // config and avoid onConfigurationChanged if it hasn't changed.
                    android.app.Activity a = ((android.app.Activity) (cb));
                    performConfigurationChangedForActivity(mActivities.get(a.getActivityToken()), config, android.app.ActivityThread.REPORT_TO_ACTIVITY);
                } else {
                    performConfigurationChanged(cb, null, config, null, android.app.ActivityThread.REPORT_TO_ACTIVITY);
                }
            }
        }
    }

    static void freeTextLayoutCachesIfNeeded(int configDiff) {
        if (configDiff != 0) {
            // Ask text layout engine to free its caches if there is a locale change
            boolean hasLocaleConfigChange = (configDiff & android.content.pm.ActivityInfo.CONFIG_LOCALE) != 0;
            if (hasLocaleConfigChange) {
                android.graphics.Canvas.freeTextLayoutCaches();
                if (android.app.ActivityThread.DEBUG_CONFIGURATION)
                    android.util.Slog.v(android.app.ActivityThread.TAG, "Cleared TextLayout Caches");

            }
        }
    }

    final void handleActivityConfigurationChanged(android.app.ActivityThread.ActivityConfigChangeData data, boolean reportToActivity) {
        android.app.ActivityThread.ActivityClientRecord r = mActivities.get(data.activityToken);
        if ((r == null) || (r.activity == null)) {
            return;
        }
        if (android.app.ActivityThread.DEBUG_CONFIGURATION)
            android.util.Slog.v(android.app.ActivityThread.TAG, (("Handle activity config changed: " + r.activityInfo.name) + ", with callback=") + reportToActivity);

        r.overrideConfig = data.overrideConfig;
        performConfigurationChangedForActivity(r, mCompatConfiguration, reportToActivity);
        mSomeActivitiesChanged = true;
    }

    final void handleProfilerControl(boolean start, android.app.ProfilerInfo profilerInfo, int profileType) {
        if (start) {
            try {
                switch (profileType) {
                    default :
                        mProfiler.setProfiler(profilerInfo);
                        mProfiler.startProfiling();
                        break;
                }
            } catch (java.lang.RuntimeException e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, ("Profiling failed on path " + profilerInfo.profileFile) + " -- can the process access this path?");
            } finally {
                try {
                    profilerInfo.profileFd.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.w(android.app.ActivityThread.TAG, "Failure closing profile fd", e);
                }
            }
        } else {
            switch (profileType) {
                default :
                    mProfiler.stopProfiling();
                    break;
            }
        }
    }

    /**
     * Public entrypoint to stop profiling. This is required to end profiling when the app crashes,
     * so that profiler data won't be lost.
     *
     * @unknown 
     */
    public void stopProfiling() {
        mProfiler.stopProfiling();
    }

    static final void handleDumpHeap(boolean managed, android.app.ActivityThread.DumpHeapData dhd) {
        if (managed) {
            try {
                android.os.Debug.dumpHprofData(dhd.path, dhd.fd.getFileDescriptor());
            } catch (java.io.IOException e) {
                android.util.Slog.w(android.app.ActivityThread.TAG, ("Managed heap dump failed on path " + dhd.path) + " -- can the process access this path?");
            } finally {
                try {
                    dhd.fd.close();
                } catch (java.io.IOException e) {
                    android.util.Slog.w(android.app.ActivityThread.TAG, "Failure closing profile fd", e);
                }
            }
        } else {
            android.os.Debug.dumpNativeHeap(dhd.fd.getFileDescriptor());
        }
        try {
            android.app.ActivityManagerNative.getDefault().dumpHeapFinished(dhd.path);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    final void handleDispatchPackageBroadcast(int cmd, java.lang.String[] packages) {
        boolean hasPkgInfo = false;
        switch (cmd) {
            case android.app.IApplicationThread.PACKAGE_REMOVED :
            case android.app.IApplicationThread.PACKAGE_REMOVED_DONT_KILL :
                {
                    final boolean killApp = cmd == android.app.IApplicationThread.PACKAGE_REMOVED;
                    if (packages == null) {
                        break;
                    }
                    synchronized(mResourcesManager) {
                        for (int i = packages.length - 1; i >= 0; i--) {
                            if (!hasPkgInfo) {
                                java.lang.ref.WeakReference<android.app.LoadedApk> ref = mPackages.get(packages[i]);
                                if ((ref != null) && (ref.get() != null)) {
                                    hasPkgInfo = true;
                                } else {
                                    ref = mResourcePackages.get(packages[i]);
                                    if ((ref != null) && (ref.get() != null)) {
                                        hasPkgInfo = true;
                                    }
                                }
                            }
                            if (killApp) {
                                mPackages.remove(packages[i]);
                                mResourcePackages.remove(packages[i]);
                            }
                        }
                    }
                    break;
                }
            case android.app.IApplicationThread.PACKAGE_REPLACED :
                {
                    if (packages == null) {
                        break;
                    }
                    synchronized(mResourcesManager) {
                        for (int i = packages.length - 1; i >= 0; i--) {
                            java.lang.ref.WeakReference<android.app.LoadedApk> ref = mPackages.get(packages[i]);
                            android.app.LoadedApk pkgInfo = (ref != null) ? ref.get() : null;
                            if (pkgInfo != null) {
                                hasPkgInfo = true;
                            } else {
                                ref = mResourcePackages.get(packages[i]);
                                pkgInfo = (ref != null) ? ref.get() : null;
                                if (pkgInfo != null) {
                                    hasPkgInfo = true;
                                }
                            }
                            // If the package is being replaced, yet it still has a valid
                            // LoadedApk object, the package was updated with _DONT_KILL.
                            // Adjust it's internal references to the application info and
                            // resources.
                            if (pkgInfo != null) {
                                try {
                                    final java.lang.String packageName = packages[i];
                                    final android.content.pm.ApplicationInfo aInfo = /* flags */
                                    android.app.ActivityThread.sPackageManager.getApplicationInfo(packageName, 0, android.os.UserHandle.myUserId());
                                    if (mActivities.size() > 0) {
                                        for (android.app.ActivityThread.ActivityClientRecord ar : mActivities.values()) {
                                            if (ar.activityInfo.applicationInfo.packageName.equals(packageName)) {
                                                ar.activityInfo.applicationInfo = aInfo;
                                                ar.packageInfo = pkgInfo;
                                            }
                                        }
                                    }
                                    final java.util.List<java.lang.String> oldPaths = android.app.ActivityThread.sPackageManager.getPreviousCodePaths(packageName);
                                    pkgInfo.updateApplicationInfo(aInfo, oldPaths);
                                } catch (android.os.RemoteException e) {
                                }
                            }
                        }
                    }
                    break;
                }
        }
        android.app.ApplicationPackageManager.handlePackageBroadcast(cmd, packages, hasPkgInfo);
    }

    final void handleLowMemory() {
        java.util.ArrayList<android.content.ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        final int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            callbacks.get(i).onLowMemory();
        }
        // Ask SQLite to free up as much memory as it can, mostly from its page caches.
        if (android.os.Process.myUid() != android.os.Process.SYSTEM_UID) {
            int sqliteReleased = android.database.sqlite.SQLiteDatabase.releaseMemory();
            android.util.EventLog.writeEvent(android.app.ActivityThread.SQLITE_MEM_RELEASED_EVENT_LOG_TAG, sqliteReleased);
        }
        // Ask graphics to free up as much as possible (font/image caches)
        android.graphics.Canvas.freeCaches();
        // Ask text layout engine to free also as much as possible
        android.graphics.Canvas.freeTextLayoutCaches();
        com.android.internal.os.BinderInternal.forceGc("mem");
    }

    final void handleTrimMemory(int level) {
        if (android.app.ActivityThread.DEBUG_MEMORY_TRIM)
            android.util.Slog.v(android.app.ActivityThread.TAG, "Trimming memory to level: " + level);

        java.util.ArrayList<android.content.ComponentCallbacks2> callbacks = collectComponentCallbacks(true, null);
        final int N = callbacks.size();
        for (int i = 0; i < N; i++) {
            callbacks.get(i).onTrimMemory(level);
        }
        android.view.WindowManagerGlobal.getInstance().trimMemory(level);
    }

    private void setupGraphicsSupport(android.app.LoadedApk info, java.io.File cacheDir) {
        if (android.os.Process.isIsolated()) {
            // Isolated processes aren't going to do UI.
            return;
        }
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "setupGraphicsSupport");
        try {
            int uid = android.os.Process.myUid();
            java.lang.String[] packages = android.app.ActivityThread.getPackageManager().getPackagesForUid(uid);
            if (packages != null) {
                android.view.ThreadedRenderer.setupDiskCache(cacheDir);
                android.renderscript.RenderScriptCacheDir.setupDiskCache(cacheDir);
            }
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        } finally {
            android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
        }
    }

    private void updateDefaultDensity() {
        final int densityDpi = mCurDefaultDisplayDpi;
        if (((!mDensityCompatMode) && (densityDpi != android.content.res.Configuration.DENSITY_DPI_UNDEFINED)) && (densityDpi != android.util.DisplayMetrics.DENSITY_DEVICE)) {
            android.util.DisplayMetrics.DENSITY_DEVICE = densityDpi;
            android.graphics.Bitmap.setDefaultDensity(densityDpi);
        }
    }

    /**
     * Returns the correct library directory for the current ABI.
     * <p>
     * If we're dealing with a multi-arch application that has both 32 and 64 bit shared
     * libraries, we might need to choose the secondary depending on what the current
     * runtime's instruction set is.
     */
    private java.lang.String getInstrumentationLibrary(android.content.pm.ApplicationInfo appInfo, android.content.pm.InstrumentationInfo insInfo) {
        if ((appInfo.primaryCpuAbi != null) && (appInfo.secondaryCpuAbi != null)) {
            // Get the instruction set supported by the secondary ABI. In the presence
            // of a native bridge this might be different than the one secondary ABI used.
            java.lang.String secondaryIsa = dalvik.system.VMRuntime.getInstructionSet(appInfo.secondaryCpuAbi);
            final java.lang.String secondaryDexCodeIsa = android.os.SystemProperties.get("ro.dalvik.vm.isa." + secondaryIsa);
            secondaryIsa = (secondaryDexCodeIsa.isEmpty()) ? secondaryIsa : secondaryDexCodeIsa;
            final java.lang.String runtimeIsa = dalvik.system.VMRuntime.getRuntime().vmInstructionSet();
            if (runtimeIsa.equals(secondaryIsa)) {
                return insInfo.secondaryNativeLibraryDir;
            }
        }
        return insInfo.nativeLibraryDir;
    }

    /**
     * The LocaleList set for the app's resources may have been shuffled so that the preferred
     * Locale is at position 0. We must find the index of this preferred Locale in the
     * original LocaleList.
     */
    private void updateLocaleListFromAppContext(android.content.Context context, android.os.LocaleList newLocaleList) {
        final java.util.Locale bestLocale = context.getResources().getConfiguration().getLocales().get(0);
        final int newLocaleListSize = newLocaleList.size();
        for (int i = 0; i < newLocaleListSize; i++) {
            if (bestLocale.equals(newLocaleList.get(i))) {
                android.os.LocaleList.setDefault(newLocaleList, i);
                return;
            }
        }
        // The app may have overridden the LocaleList with its own Locale
        // (not present in the available list). Push the chosen Locale
        // to the front of the list.
        android.os.LocaleList.setDefault(new android.os.LocaleList(bestLocale, newLocaleList));
    }

    private void handleBindApplication(android.app.ActivityThread.AppBindData data) {
        // Register the UI Thread as a sensitive thread to the runtime.
        dalvik.system.VMRuntime.registerSensitiveThread();
        if (data.trackAllocation) {
            org.apache.harmony.dalvik.ddmc.DdmVmInternal.enableRecentAllocations(true);
        }
        // Note when this process has started.
        android.os.Process.setStartTimes(android.os.SystemClock.elapsedRealtime(), android.os.SystemClock.uptimeMillis());
        mBoundApplication = data;
        mConfiguration = new android.content.res.Configuration(data.config);
        mCompatConfiguration = new android.content.res.Configuration(data.config);
        mProfiler = new android.app.ActivityThread.Profiler();
        if (data.initProfilerInfo != null) {
            mProfiler.profileFile = data.initProfilerInfo.profileFile;
            mProfiler.profileFd = data.initProfilerInfo.profileFd;
            mProfiler.samplingInterval = data.initProfilerInfo.samplingInterval;
            mProfiler.autoStopProfiler = data.initProfilerInfo.autoStopProfiler;
        }
        // send up app name; do this *before* waiting for debugger
        android.os.Process.setArgV0(data.processName);
        android.ddm.DdmHandleAppName.setAppName(data.processName, android.os.UserHandle.myUserId());
        if (data.persistent) {
            // Persistent processes on low-memory devices do not get to
            // use hardware accelerated drawing, since this can add too much
            // overhead to the process.
            if (!android.app.ActivityManager.isHighEndGfx()) {
                android.view.ThreadedRenderer.disable(false);
            }
        }
        if (mProfiler.profileFd != null) {
            mProfiler.startProfiling();
        }
        // If the app is Honeycomb MR1 or earlier, switch its AsyncTask
        // implementation to use the pool executor.  Normally, we use the
        // serialized executor as the default. This has to happen in the
        // main thread so the main looper is set right.
        if (data.appInfo.targetSdkVersion <= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
            android.os.AsyncTask.setDefaultExecutor(android.os.AsyncTask.THREAD_POOL_EXECUTOR);
        }
        android.os.Message.updateCheckRecycle(data.appInfo.targetSdkVersion);
        /* Before spawning a new process, reset the time zone to be the system time zone.
        This needs to be done because the system time zone could have changed after the
        the spawning of this process. Without doing this this process would have the incorrect
        system time zone.
         */
        java.util.TimeZone.setDefault(null);
        /* Set the LocaleList. This may change once we create the App Context. */
        android.os.LocaleList.setDefault(data.config.getLocales());
        synchronized(mResourcesManager) {
            /* Update the system configuration since its preloaded and might not
            reflect configuration changes. The configuration object passed
            in AppBindData can be safely assumed to be up to date
             */
            mResourcesManager.applyConfigurationToResourcesLocked(data.config, data.compatInfo);
            mCurDefaultDisplayDpi = data.config.densityDpi;
            // This calls mResourcesManager so keep it within the synchronized block.
            applyCompatConfiguration(mCurDefaultDisplayDpi);
        }
        data.info = getPackageInfoNoCheck(data.appInfo, data.compatInfo);
        /**
         * Switch this process to density compatibility mode if needed.
         */
        if ((data.appInfo.flags & android.content.pm.ApplicationInfo.FLAG_SUPPORTS_SCREEN_DENSITIES) == 0) {
            mDensityCompatMode = true;
            android.graphics.Bitmap.setDefaultDensity(android.util.DisplayMetrics.DENSITY_DEFAULT);
        }
        updateDefaultDensity();
        final boolean is24Hr = "24".equals(mCoreSettings.getString(android.provider.Settings.System.TIME_12_24));
        java.text.DateFormat.set24HourTimePref(is24Hr);
        android.view.View.mDebugViewAttributes = mCoreSettings.getInt(android.provider.Settings.Global.DEBUG_VIEW_ATTRIBUTES, 0) != 0;
        /**
         * For system applications on userdebug/eng builds, log stack
         * traces of disk and network access to dropbox for analysis.
         */
        if ((data.appInfo.flags & (android.content.pm.ApplicationInfo.FLAG_SYSTEM | android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)) != 0) {
            android.os.StrictMode.conditionallyEnableDebugLogging();
        }
        /**
         * For apps targetting Honeycomb or later, we don't allow network usage
         * on the main event loop / UI thread. This is what ultimately throws
         * {@link NetworkOnMainThreadException}.
         */
        if (data.appInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.os.StrictMode.enableDeathOnNetwork();
        }
        /**
         * For apps targetting N or later, we don't allow file:// Uri exposure.
         * This is what ultimately throws {@link FileUriExposedException}.
         */
        if (data.appInfo.targetSdkVersion >= android.os.Build.VERSION_CODES.N) {
            android.os.StrictMode.enableDeathOnFileUriExposure();
        }
        android.security.NetworkSecurityPolicy.getInstance().setCleartextTrafficPermitted((data.appInfo.flags & android.content.pm.ApplicationInfo.FLAG_USES_CLEARTEXT_TRAFFIC) != 0);
        if (data.debugMode != android.app.IApplicationThread.DEBUG_OFF) {
            // XXX should have option to change the port.
            android.os.Debug.changeDebugPort(8100);
            if (data.debugMode == android.app.IApplicationThread.DEBUG_WAIT) {
                android.util.Slog.w(android.app.ActivityThread.TAG, ("Application " + data.info.getPackageName()) + " is waiting for the debugger on port 8100...");
                android.app.IActivityManager mgr = android.app.ActivityManagerNative.getDefault();
                try {
                    mgr.showWaitingForDebugger(mAppThread, true);
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
                android.os.Debug.waitForDebugger();
                try {
                    mgr.showWaitingForDebugger(mAppThread, false);
                } catch (android.os.RemoteException ex) {
                    throw ex.rethrowFromSystemServer();
                }
            } else {
                android.util.Slog.w(android.app.ActivityThread.TAG, ("Application " + data.info.getPackageName()) + " can be debugged on port 8100...");
            }
        }
        // Allow application-generated systrace messages if we're debuggable.
        boolean isAppDebuggable = (data.appInfo.flags & android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        android.os.Trace.setAppTracingAllowed(isAppDebuggable);
        if (isAppDebuggable && data.enableBinderTracking) {
            android.os.Binder.enableTracing();
        }
        /**
         * Initialize the default http proxy in this process for the reasons we set the time zone.
         */
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "Setup proxies");
        final android.os.IBinder b = android.os.ServiceManager.getService(android.content.Context.CONNECTIVITY_SERVICE);
        if (b != null) {
            // In pre-boot mode (doing initial launch to collect password), not
            // all system is up.  This includes the connectivity service, so don't
            // crash if we can't get it.
            final android.net.IConnectivityManager service = IConnectivityManager.Stub.asInterface(b);
            try {
                final android.net.ProxyInfo proxyInfo = service.getProxyForNetwork(null);
                android.net.Proxy.setHttpProxySystemProperty(proxyInfo);
            } catch (android.os.RemoteException e) {
                android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
                throw e.rethrowFromSystemServer();
            }
        }
        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
        // Instrumentation info affects the class loader, so load it before
        // setting up the app context.
        final android.content.pm.InstrumentationInfo ii;
        if (data.instrumentationName != null) {
            try {
                ii = new android.app.ApplicationPackageManager(null, android.app.ActivityThread.getPackageManager()).getInstrumentationInfo(data.instrumentationName, 0);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new java.lang.RuntimeException("Unable to find instrumentation info for: " + data.instrumentationName);
            }
            mInstrumentationPackageName = ii.packageName;
            mInstrumentationAppDir = ii.sourceDir;
            mInstrumentationSplitAppDirs = ii.splitSourceDirs;
            mInstrumentationLibDir = getInstrumentationLibrary(data.appInfo, ii);
            mInstrumentedAppDir = data.info.getAppDir();
            mInstrumentedSplitAppDirs = data.info.getSplitAppDirs();
            mInstrumentedLibDir = data.info.getLibDir();
        } else {
            ii = null;
        }
        final android.app.ContextImpl appContext = android.app.ContextImpl.createAppContext(this, data.info);
        updateLocaleListFromAppContext(appContext, mResourcesManager.getConfiguration().getLocales());
        if ((!android.os.Process.isIsolated()) && (!"android".equals(appContext.getPackageName()))) {
            // This cache location probably points at credential-encrypted
            // storage which may not be accessible yet; assign it anyway instead
            // of pointing at device-encrypted storage.
            final java.io.File cacheDir = appContext.getCacheDir();
            if (cacheDir != null) {
                // Provide a usable directory for temporary files
                java.lang.System.setProperty("java.io.tmpdir", cacheDir.getAbsolutePath());
            } else {
                android.util.Log.v(android.app.ActivityThread.TAG, "Unable to initialize \"java.io.tmpdir\" property " + "due to missing cache directory");
            }
            // Setup a location to store generated/compiled graphics code.
            final android.content.Context deviceContext = appContext.createDeviceProtectedStorageContext();
            final java.io.File codeCacheDir = deviceContext.getCodeCacheDir();
            if (codeCacheDir != null) {
                setupGraphicsSupport(data.info, codeCacheDir);
            } else {
                android.util.Log.e(android.app.ActivityThread.TAG, "Unable to setupGraphicsSupport due to missing code-cache directory");
            }
        }
        // Install the Network Security Config Provider. This must happen before the application
        // code is loaded to prevent issues with instances of TLS objects being created before
        // the provider is installed.
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "NetworkSecurityConfigProvider.install");
        android.security.net.config.NetworkSecurityConfigProvider.install(appContext);
        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
        // Continue loading instrumentation.
        if (ii != null) {
            final android.content.pm.ApplicationInfo instrApp = new android.content.pm.ApplicationInfo();
            ii.copyTo(instrApp);
            instrApp.initForUser(android.os.UserHandle.myUserId());
            final android.app.LoadedApk pi = getPackageInfo(instrApp, data.compatInfo, appContext.getClassLoader(), false, true, false);
            final android.app.ContextImpl instrContext = android.app.ContextImpl.createAppContext(this, pi);
            try {
                final java.lang.ClassLoader cl = instrContext.getClassLoader();
                mInstrumentation = ((android.app.Instrumentation) (cl.loadClass(data.instrumentationName.getClassName()).newInstance()));
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException((("Unable to instantiate instrumentation " + data.instrumentationName) + ": ") + e.toString(), e);
            }
            final android.content.ComponentName component = new android.content.ComponentName(ii.packageName, ii.name);
            mInstrumentation.init(this, instrContext, appContext, component, data.instrumentationWatcher, data.instrumentationUiAutomationConnection);
            if (((mProfiler.profileFile != null) && (!ii.handleProfiling)) && (mProfiler.profileFd == null)) {
                mProfiler.handlingProfiling = true;
                final java.io.File file = new java.io.File(mProfiler.profileFile);
                file.getParentFile().mkdirs();
                android.os.Debug.startMethodTracing(file.toString(), (8 * 1024) * 1024);
            }
        } else {
            mInstrumentation = new android.app.Instrumentation();
        }
        if ((data.appInfo.flags & android.content.pm.ApplicationInfo.FLAG_LARGE_HEAP) != 0) {
            dalvik.system.VMRuntime.getRuntime().clearGrowthLimit();
        } else {
            // Small heap, clamp to the current growth limit and let the heap release
            // pages after the growth limit to the non growth limit capacity. b/18387825
            dalvik.system.VMRuntime.getRuntime().clampGrowthLimit();
        }
        // Allow disk access during application and provider setup. This could
        // block processing ordered broadcasts, but later processing would
        // probably end up doing the same disk access.
        final android.os.StrictMode.ThreadPolicy savedPolicy = android.os.StrictMode.allowThreadDiskWrites();
        try {
            // If the app is being launched for full backup or restore, bring it up in
            // a restricted environment with the base application class.
            android.app.Application app = data.info.makeApplication(data.restrictedBackupMode, null);
            mInitialApplication = app;
            // don't bring up providers in restricted mode; they may depend on the
            // app's custom Application class
            if (!data.restrictedBackupMode) {
                if (!com.android.internal.util.ArrayUtils.isEmpty(data.providers)) {
                    installContentProviders(app, data.providers);
                    // For process that contains content providers, we want to
                    // ensure that the JIT is enabled "at some point".
                    mH.sendEmptyMessageDelayed(android.app.ActivityThread.H.ENABLE_JIT, 10 * 1000);
                }
            }
            // Do this after providers, since instrumentation tests generally start their
            // test thread at this point, and we don't want that racing.
            try {
                mInstrumentation.onCreate(data.instrumentationArgs);
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException((("Exception thrown in onCreate() of " + data.instrumentationName) + ": ") + e.toString(), e);
            }
            try {
                mInstrumentation.callApplicationOnCreate(app);
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(app, e)) {
                    throw new java.lang.RuntimeException((("Unable to create application " + app.getClass().getName()) + ": ") + e.toString(), e);
                }
            }
        } finally {
            android.os.StrictMode.setThreadPolicy(savedPolicy);
        }
    }

    /* package */
    final void finishInstrumentation(int resultCode, android.os.Bundle results) {
        android.app.IActivityManager am = android.app.ActivityManagerNative.getDefault();
        if (((mProfiler.profileFile != null) && mProfiler.handlingProfiling) && (mProfiler.profileFd == null)) {
            android.os.Debug.stopMethodTracing();
        }
        // Slog.i(TAG, "am: " + ActivityManagerNative.getDefault()
        // + ", app thr: " + mAppThread);
        try {
            am.finishInstrumentation(mAppThread, resultCode, results);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    private void installContentProviders(android.content.Context context, java.util.List<android.content.pm.ProviderInfo> providers) {
        final java.util.ArrayList<android.app.IActivityManager.ContentProviderHolder> results = new java.util.ArrayList<android.app.IActivityManager.ContentProviderHolder>();
        for (android.content.pm.ProviderInfo cpi : providers) {
            if (android.app.ActivityThread.DEBUG_PROVIDER) {
                java.lang.StringBuilder buf = new java.lang.StringBuilder(128);
                buf.append("Pub ");
                buf.append(cpi.authority);
                buf.append(": ");
                buf.append(cpi.name);
                android.util.Log.i(android.app.ActivityThread.TAG, buf.toString());
            }
            android.app.IActivityManager.ContentProviderHolder cph = /* noisy */
            /* noReleaseNeeded */
            /* stable */
            installProvider(context, null, cpi, false, true, true);
            if (cph != null) {
                cph.noReleaseNeeded = true;
                results.add(cph);
            }
        }
        try {
            android.app.ActivityManagerNative.getDefault().publishContentProviders(getApplicationThread(), results);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
    }

    public final android.content.IContentProvider acquireProvider(android.content.Context c, java.lang.String auth, int userId, boolean stable) {
        final android.content.IContentProvider provider = acquireExistingProvider(c, auth, userId, stable);
        if (provider != null) {
            return provider;
        }
        // There is a possible race here.  Another thread may try to acquire
        // the same provider at the same time.  When this happens, we want to ensure
        // that the first one wins.
        // Note that we cannot hold the lock while acquiring and installing the
        // provider since it might take a long time to run and it could also potentially
        // be re-entrant in the case where the provider is in the same process.
        android.app.IActivityManager.ContentProviderHolder holder = null;
        try {
            holder = android.app.ActivityManagerNative.getDefault().getContentProvider(getApplicationThread(), auth, userId, stable);
        } catch (android.os.RemoteException ex) {
            throw ex.rethrowFromSystemServer();
        }
        if (holder == null) {
            android.util.Slog.e(android.app.ActivityThread.TAG, "Failed to find provider info for " + auth);
            return null;
        }
        // Install provider will increment the reference count for us, and break
        // any ties in the race.
        holder = /* noisy */
        installProvider(c, holder, holder.info, true, holder.noReleaseNeeded, stable);
        return holder.provider;
    }

    private final void incProviderRefLocked(android.app.ActivityThread.ProviderRefCount prc, boolean stable) {
        if (stable) {
            prc.stableCount += 1;
            if (prc.stableCount == 1) {
                // We are acquiring a new stable reference on the provider.
                int unstableDelta;
                if (prc.removePending) {
                    // We have a pending remove operation, which is holding the
                    // last unstable reference.  At this point we are converting
                    // that unstable reference to our new stable reference.
                    unstableDelta = -1;
                    // Cancel the removal of the provider.
                    if (android.app.ActivityThread.DEBUG_PROVIDER) {
                        android.util.Slog.v(android.app.ActivityThread.TAG, "incProviderRef: stable " + "snatched provider from the jaws of death");
                    }
                    prc.removePending = false;
                    // There is a race! It fails to remove the message, which
                    // will be handled in completeRemoveProvider().
                    mH.removeMessages(android.app.ActivityThread.H.REMOVE_PROVIDER, prc);
                } else {
                    unstableDelta = 0;
                }
                try {
                    if (android.app.ActivityThread.DEBUG_PROVIDER) {
                        android.util.Slog.v(android.app.ActivityThread.TAG, (("incProviderRef Now stable - " + prc.holder.info.name) + ": unstableDelta=") + unstableDelta);
                    }
                    android.app.ActivityManagerNative.getDefault().refContentProvider(prc.holder.connection, 1, unstableDelta);
                } catch (android.os.RemoteException e) {
                    // do nothing content provider object is dead any way
                }
            }
        } else {
            prc.unstableCount += 1;
            if (prc.unstableCount == 1) {
                // We are acquiring a new unstable reference on the provider.
                if (prc.removePending) {
                    // Oh look, we actually have a remove pending for the
                    // provider, which is still holding the last unstable
                    // reference.  We just need to cancel that to take new
                    // ownership of the reference.
                    if (android.app.ActivityThread.DEBUG_PROVIDER) {
                        android.util.Slog.v(android.app.ActivityThread.TAG, "incProviderRef: unstable " + "snatched provider from the jaws of death");
                    }
                    prc.removePending = false;
                    mH.removeMessages(android.app.ActivityThread.H.REMOVE_PROVIDER, prc);
                } else {
                    // First unstable ref, increment our count in the
                    // activity manager.
                    try {
                        if (android.app.ActivityThread.DEBUG_PROVIDER) {
                            android.util.Slog.v(android.app.ActivityThread.TAG, "incProviderRef: Now unstable - " + prc.holder.info.name);
                        }
                        android.app.ActivityManagerNative.getDefault().refContentProvider(prc.holder.connection, 0, 1);
                    } catch (android.os.RemoteException e) {
                        // do nothing content provider object is dead any way
                    }
                }
            }
        }
    }

    public final android.content.IContentProvider acquireExistingProvider(android.content.Context c, java.lang.String auth, int userId, boolean stable) {
        synchronized(mProviderMap) {
            final android.app.ActivityThread.ProviderKey key = new android.app.ActivityThread.ProviderKey(auth, userId);
            final android.app.ActivityThread.ProviderClientRecord pr = mProviderMap.get(key);
            if (pr == null) {
                return null;
            }
            android.content.IContentProvider provider = pr.mProvider;
            android.os.IBinder jBinder = provider.asBinder();
            if (!jBinder.isBinderAlive()) {
                // The hosting process of the provider has died; we can't
                // use this one.
                android.util.Log.i(android.app.ActivityThread.TAG, ((("Acquiring provider " + auth) + " for user ") + userId) + ": existing object's process dead");
                handleUnstableProviderDiedLocked(jBinder, true);
                return null;
            }
            // Only increment the ref count if we have one.  If we don't then the
            // provider is not reference counted and never needs to be released.
            android.app.ActivityThread.ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
            if (prc != null) {
                incProviderRefLocked(prc, stable);
            }
            return provider;
        }
    }

    public final boolean releaseProvider(android.content.IContentProvider provider, boolean stable) {
        if (provider == null) {
            return false;
        }
        android.os.IBinder jBinder = provider.asBinder();
        synchronized(mProviderMap) {
            android.app.ActivityThread.ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
            if (prc == null) {
                // The provider has no ref count, no release is needed.
                return false;
            }
            boolean lastRef = false;
            if (stable) {
                if (prc.stableCount == 0) {
                    if (android.app.ActivityThread.DEBUG_PROVIDER)
                        android.util.Slog.v(android.app.ActivityThread.TAG, "releaseProvider: stable ref count already 0, how?");

                    return false;
                }
                prc.stableCount -= 1;
                if (prc.stableCount == 0) {
                    // What we do at this point depends on whether there are
                    // any unstable refs left: if there are, we just tell the
                    // activity manager to decrement its stable count; if there
                    // aren't, we need to enqueue this provider to be removed,
                    // and convert to holding a single unstable ref while
                    // doing so.
                    lastRef = prc.unstableCount == 0;
                    try {
                        if (android.app.ActivityThread.DEBUG_PROVIDER) {
                            android.util.Slog.v(android.app.ActivityThread.TAG, (("releaseProvider: No longer stable w/lastRef=" + lastRef) + " - ") + prc.holder.info.name);
                        }
                        android.app.ActivityManagerNative.getDefault().refContentProvider(prc.holder.connection, -1, lastRef ? 1 : 0);
                    } catch (android.os.RemoteException e) {
                        // do nothing content provider object is dead any way
                    }
                }
            } else {
                if (prc.unstableCount == 0) {
                    if (android.app.ActivityThread.DEBUG_PROVIDER)
                        android.util.Slog.v(android.app.ActivityThread.TAG, "releaseProvider: unstable ref count already 0, how?");

                    return false;
                }
                prc.unstableCount -= 1;
                if (prc.unstableCount == 0) {
                    // If this is the last reference, we need to enqueue
                    // this provider to be removed instead of telling the
                    // activity manager to remove it at this point.
                    lastRef = prc.stableCount == 0;
                    if (!lastRef) {
                        try {
                            if (android.app.ActivityThread.DEBUG_PROVIDER) {
                                android.util.Slog.v(android.app.ActivityThread.TAG, "releaseProvider: No longer unstable - " + prc.holder.info.name);
                            }
                            android.app.ActivityManagerNative.getDefault().refContentProvider(prc.holder.connection, 0, -1);
                        } catch (android.os.RemoteException e) {
                            // do nothing content provider object is dead any way
                        }
                    }
                }
            }
            if (lastRef) {
                if (!prc.removePending) {
                    // Schedule the actual remove asynchronously, since we don't know the context
                    // this will be called in.
                    // TODO: it would be nice to post a delayed message, so
                    // if we come back and need the same provider quickly
                    // we will still have it available.
                    if (android.app.ActivityThread.DEBUG_PROVIDER) {
                        android.util.Slog.v(android.app.ActivityThread.TAG, "releaseProvider: Enqueueing pending removal - " + prc.holder.info.name);
                    }
                    prc.removePending = true;
                    android.os.Message msg = mH.obtainMessage(android.app.ActivityThread.H.REMOVE_PROVIDER, prc);
                    mH.sendMessage(msg);
                } else {
                    android.util.Slog.w(android.app.ActivityThread.TAG, "Duplicate remove pending of provider " + prc.holder.info.name);
                }
            }
            return true;
        }
    }

    final void completeRemoveProvider(android.app.ActivityThread.ProviderRefCount prc) {
        synchronized(mProviderMap) {
            if (!prc.removePending) {
                // There was a race!  Some other client managed to acquire
                // the provider before the removal was completed.
                // Abort the removal.  We will do it later.
                if (android.app.ActivityThread.DEBUG_PROVIDER)
                    android.util.Slog.v(android.app.ActivityThread.TAG, "completeRemoveProvider: lost the race, " + "provider still in use");

                return;
            }
            // More complicated race!! Some client managed to acquire the
            // provider and release it before the removal was completed.
            // Continue the removal, and abort the next remove message.
            prc.removePending = false;
            final android.os.IBinder jBinder = prc.holder.provider.asBinder();
            android.app.ActivityThread.ProviderRefCount existingPrc = mProviderRefCountMap.get(jBinder);
            if (existingPrc == prc) {
                mProviderRefCountMap.remove(jBinder);
            }
            for (int i = mProviderMap.size() - 1; i >= 0; i--) {
                android.app.ActivityThread.ProviderClientRecord pr = mProviderMap.valueAt(i);
                android.os.IBinder myBinder = pr.mProvider.asBinder();
                if (myBinder == jBinder) {
                    mProviderMap.removeAt(i);
                }
            }
        }
        try {
            if (android.app.ActivityThread.DEBUG_PROVIDER) {
                android.util.Slog.v(android.app.ActivityThread.TAG, (("removeProvider: Invoking ActivityManagerNative." + "removeContentProvider(") + prc.holder.info.name) + ")");
            }
            android.app.ActivityManagerNative.getDefault().removeContentProvider(prc.holder.connection, false);
        } catch (android.os.RemoteException e) {
            // do nothing content provider object is dead any way
        }
    }

    final void handleUnstableProviderDied(android.os.IBinder provider, boolean fromClient) {
        synchronized(mProviderMap) {
            handleUnstableProviderDiedLocked(provider, fromClient);
        }
    }

    final void handleUnstableProviderDiedLocked(android.os.IBinder provider, boolean fromClient) {
        android.app.ActivityThread.ProviderRefCount prc = mProviderRefCountMap.get(provider);
        if (prc != null) {
            if (android.app.ActivityThread.DEBUG_PROVIDER)
                android.util.Slog.v(android.app.ActivityThread.TAG, (("Cleaning up dead provider " + provider) + " ") + prc.holder.info.name);

            mProviderRefCountMap.remove(provider);
            for (int i = mProviderMap.size() - 1; i >= 0; i--) {
                android.app.ActivityThread.ProviderClientRecord pr = mProviderMap.valueAt(i);
                if ((pr != null) && (pr.mProvider.asBinder() == provider)) {
                    android.util.Slog.i(android.app.ActivityThread.TAG, "Removing dead content provider:" + pr.mProvider.toString());
                    mProviderMap.removeAt(i);
                }
            }
            if (fromClient) {
                // We found out about this due to execution in our client
                // code.  Tell the activity manager about it now, to ensure
                // that the next time we go to do anything with the provider
                // it knows it is dead (so we don't race with its death
                // notification).
                try {
                    android.app.ActivityManagerNative.getDefault().unstableProviderDied(prc.holder.connection);
                } catch (android.os.RemoteException e) {
                    // do nothing content provider object is dead any way
                }
            }
        }
    }

    final void appNotRespondingViaProvider(android.os.IBinder provider) {
        synchronized(mProviderMap) {
            android.app.ActivityThread.ProviderRefCount prc = mProviderRefCountMap.get(provider);
            if (prc != null) {
                try {
                    android.app.ActivityManagerNative.getDefault().appNotRespondingViaProvider(prc.holder.connection);
                } catch (android.os.RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            }
        }
    }

    private android.app.ActivityThread.ProviderClientRecord installProviderAuthoritiesLocked(android.content.IContentProvider provider, android.content.ContentProvider localProvider, android.app.IActivityManager.ContentProviderHolder holder) {
        final java.lang.String[] auths = holder.info.authority.split(";");
        final int userId = android.os.UserHandle.getUserId(holder.info.applicationInfo.uid);
        final android.app.ActivityThread.ProviderClientRecord pcr = new android.app.ActivityThread.ProviderClientRecord(auths, provider, localProvider, holder);
        for (java.lang.String auth : auths) {
            final android.app.ActivityThread.ProviderKey key = new android.app.ActivityThread.ProviderKey(auth, userId);
            final android.app.ActivityThread.ProviderClientRecord existing = mProviderMap.get(key);
            if (existing != null) {
                android.util.Slog.w(android.app.ActivityThread.TAG, (("Content provider " + pcr.mHolder.info.name) + " already published as ") + auth);
            } else {
                mProviderMap.put(key, pcr);
            }
        }
        return pcr;
    }

    /**
     * Installs the provider.
     *
     * Providers that are local to the process or that come from the system server
     * may be installed permanently which is indicated by setting noReleaseNeeded to true.
     * Other remote providers are reference counted.  The initial reference count
     * for all reference counted providers is one.  Providers that are not reference
     * counted do not have a reference count (at all).
     *
     * This method detects when a provider has already been installed.  When this happens,
     * it increments the reference count of the existing provider (if appropriate)
     * and returns the existing provider.  This can happen due to concurrent
     * attempts to acquire the same provider.
     */
    private android.app.IActivityManager.ContentProviderHolder installProvider(android.content.Context context, android.app.IActivityManager.ContentProviderHolder holder, android.content.pm.ProviderInfo info, boolean noisy, boolean noReleaseNeeded, boolean stable) {
        android.content.ContentProvider localProvider = null;
        android.content.IContentProvider provider;
        if ((holder == null) || (holder.provider == null)) {
            if (android.app.ActivityThread.DEBUG_PROVIDER || noisy) {
                android.util.Slog.d(android.app.ActivityThread.TAG, (("Loading provider " + info.authority) + ": ") + info.name);
            }
            android.content.Context c = null;
            android.content.pm.ApplicationInfo ai = info.applicationInfo;
            if (context.getPackageName().equals(ai.packageName)) {
                c = context;
            } else
                if ((mInitialApplication != null) && mInitialApplication.getPackageName().equals(ai.packageName)) {
                    c = mInitialApplication;
                } else {
                    try {
                        c = context.createPackageContext(ai.packageName, android.content.Context.CONTEXT_INCLUDE_CODE);
                    } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                        // Ignore
                    }
                }

            if (c == null) {
                android.util.Slog.w(android.app.ActivityThread.TAG, (("Unable to get context for package " + ai.packageName) + " while loading content provider ") + info.name);
                return null;
            }
            try {
                final java.lang.ClassLoader cl = c.getClassLoader();
                localProvider = ((android.content.ContentProvider) (cl.loadClass(info.name).newInstance()));
                provider = localProvider.getIContentProvider();
                if (provider == null) {
                    android.util.Slog.e(android.app.ActivityThread.TAG, (("Failed to instantiate class " + info.name) + " from sourceDir ") + info.applicationInfo.sourceDir);
                    return null;
                }
                if (android.app.ActivityThread.DEBUG_PROVIDER)
                    android.util.Slog.v(android.app.ActivityThread.TAG, "Instantiating local provider " + info.name);

                // XXX Need to create the correct context for this provider.
                localProvider.attachInfo(c, info);
            } catch (java.lang.Exception e) {
                if (!mInstrumentation.onException(null, e)) {
                    throw new java.lang.RuntimeException((("Unable to get provider " + info.name) + ": ") + e.toString(), e);
                }
                return null;
            }
        } else {
            provider = holder.provider;
            if (android.app.ActivityThread.DEBUG_PROVIDER)
                android.util.Slog.v(android.app.ActivityThread.TAG, (("Installing external provider " + info.authority) + ": ") + info.name);

        }
        android.app.IActivityManager.ContentProviderHolder retHolder;
        synchronized(mProviderMap) {
            if (android.app.ActivityThread.DEBUG_PROVIDER)
                android.util.Slog.v(android.app.ActivityThread.TAG, (("Checking to add " + provider) + " / ") + info.name);

            android.os.IBinder jBinder = provider.asBinder();
            if (localProvider != null) {
                android.content.ComponentName cname = new android.content.ComponentName(info.packageName, info.name);
                android.app.ActivityThread.ProviderClientRecord pr = mLocalProvidersByName.get(cname);
                if (pr != null) {
                    if (android.app.ActivityThread.DEBUG_PROVIDER) {
                        android.util.Slog.v(android.app.ActivityThread.TAG, "installProvider: lost the race, " + "using existing local provider");
                    }
                    provider = pr.mProvider;
                } else {
                    holder = new android.app.IActivityManager.ContentProviderHolder(info);
                    holder.provider = provider;
                    holder.noReleaseNeeded = true;
                    pr = installProviderAuthoritiesLocked(provider, localProvider, holder);
                    mLocalProviders.put(jBinder, pr);
                    mLocalProvidersByName.put(cname, pr);
                }
                retHolder = pr.mHolder;
            } else {
                android.app.ActivityThread.ProviderRefCount prc = mProviderRefCountMap.get(jBinder);
                if (prc != null) {
                    if (android.app.ActivityThread.DEBUG_PROVIDER) {
                        android.util.Slog.v(android.app.ActivityThread.TAG, "installProvider: lost the race, updating ref count");
                    }
                    // We need to transfer our new reference to the existing
                    // ref count, releasing the old one...  but only if
                    // release is needed (that is, it is not running in the
                    // system process).
                    if (!noReleaseNeeded) {
                        incProviderRefLocked(prc, stable);
                        try {
                            android.app.ActivityManagerNative.getDefault().removeContentProvider(holder.connection, stable);
                        } catch (android.os.RemoteException e) {
                            // do nothing content provider object is dead any way
                        }
                    }
                } else {
                    android.app.ActivityThread.ProviderClientRecord client = installProviderAuthoritiesLocked(provider, localProvider, holder);
                    if (noReleaseNeeded) {
                        prc = new android.app.ActivityThread.ProviderRefCount(holder, client, 1000, 1000);
                    } else {
                        prc = (stable) ? new android.app.ActivityThread.ProviderRefCount(holder, client, 1, 0) : new android.app.ActivityThread.ProviderRefCount(holder, client, 0, 1);
                    }
                    mProviderRefCountMap.put(jBinder, prc);
                }
                retHolder = prc.holder;
            }
        }
        return retHolder;
    }

    private void attach(boolean system) {
        android.app.ActivityThread.sCurrentActivityThread = this;
        mSystemThread = system;
        if (!system) {
            android.view.ViewRootImpl.addFirstDrawHandler(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    ensureJitEnabled();
                }
            });
            android.ddm.DdmHandleAppName.setAppName("<pre-initialized>", android.os.UserHandle.myUserId());
            com.android.internal.os.RuntimeInit.setApplicationObject(mAppThread.asBinder());
            final android.app.IActivityManager mgr = android.app.ActivityManagerNative.getDefault();
            try {
                mgr.attachApplication(mAppThread);
            } catch (android.os.RemoteException ex) {
                throw ex.rethrowFromSystemServer();
            }
            // Watch for getting close to heap limit.
            com.android.internal.os.BinderInternal.addGcWatcher(new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    if (!mSomeActivitiesChanged) {
                        return;
                    }
                    java.lang.Runtime runtime = java.lang.Runtime.getRuntime();
                    long dalvikMax = runtime.maxMemory();
                    long dalvikUsed = runtime.totalMemory() - runtime.freeMemory();
                    if (dalvikUsed > ((3 * dalvikMax) / 4)) {
                        if (android.app.ActivityThread.DEBUG_MEMORY_TRIM)
                            android.util.Slog.d(android.app.ActivityThread.TAG, (((("Dalvik max=" + (dalvikMax / 1024)) + " total=") + (runtime.totalMemory() / 1024)) + " used=") + (dalvikUsed / 1024));

                        mSomeActivitiesChanged = false;
                        try {
                            mgr.releaseSomeActivities(mAppThread);
                        } catch (android.os.RemoteException e) {
                            throw e.rethrowFromSystemServer();
                        }
                    }
                }
            });
        } else {
            // Don't set application object here -- if the system crashes,
            // we can't display an alert, we just want to die die die.
            android.ddm.DdmHandleAppName.setAppName("system_process", android.os.UserHandle.myUserId());
            try {
                mInstrumentation = new android.app.Instrumentation();
                android.app.ContextImpl context = android.app.ContextImpl.createAppContext(this, getSystemContext().mPackageInfo);
                mInitialApplication = context.mPackageInfo.makeApplication(true, null);
                mInitialApplication.onCreate();
            } catch (java.lang.Exception e) {
                throw new java.lang.RuntimeException("Unable to instantiate Application():" + e.toString(), e);
            }
        }
        // add dropbox logging to libcore
        libcore.io.DropBox.setReporter(new android.app.ActivityThread.DropBoxReporter());
        android.view.ViewRootImpl.addConfigCallback(new android.content.ComponentCallbacks2() {
            @java.lang.Override
            public void onConfigurationChanged(android.content.res.Configuration newConfig) {
                synchronized(mResourcesManager) {
                    // We need to apply this change to the resources
                    // immediately, because upon returning the view
                    // hierarchy will be informed about it.
                    if (mResourcesManager.applyConfigurationToResourcesLocked(newConfig, null)) {
                        updateLocaleListFromAppContext(mInitialApplication.getApplicationContext(), mResourcesManager.getConfiguration().getLocales());
                        // This actually changed the resources!  Tell
                        // everyone about it.
                        if ((mPendingConfiguration == null) || mPendingConfiguration.isOtherSeqNewer(newConfig)) {
                            mPendingConfiguration = newConfig;
                            sendMessage(android.app.ActivityThread.H.CONFIGURATION_CHANGED, newConfig);
                        }
                    }
                }
            }

            @java.lang.Override
            public void onLowMemory() {
            }

            @java.lang.Override
            public void onTrimMemory(int level) {
            }
        });
    }

    public static android.app.ActivityThread systemMain() {
        // The system process on low-memory devices do not get to use hardware
        // accelerated drawing, since this can add too much overhead to the
        // process.
        if (!android.app.ActivityManager.isHighEndGfx()) {
            android.view.ThreadedRenderer.disable(true);
        } else {
            android.view.ThreadedRenderer.enableForegroundTrimming();
        }
        android.app.ActivityThread thread = new android.app.ActivityThread();
        thread.attach(true);
        return thread;
    }

    public final void installSystemProviders(java.util.List<android.content.pm.ProviderInfo> providers) {
        if (providers != null) {
            installContentProviders(mInitialApplication, providers);
        }
    }

    public int getIntCoreSetting(java.lang.String key, int defaultValue) {
        synchronized(mResourcesManager) {
            if (mCoreSettings != null) {
                return mCoreSettings.getInt(key, defaultValue);
            }
            return defaultValue;
        }
    }

    private static class EventLoggingReporter implements libcore.io.EventLogger.Reporter {
        @java.lang.Override
        public void report(int code, java.lang.Object... list) {
            android.util.EventLog.writeEvent(code, list);
        }
    }

    private class DropBoxReporter implements libcore.io.DropBox.Reporter {
        private android.os.DropBoxManager dropBox;

        public DropBoxReporter() {
        }

        @java.lang.Override
        public void addData(java.lang.String tag, byte[] data, int flags) {
            ensureInitialized();
            dropBox.addData(tag, data, flags);
        }

        @java.lang.Override
        public void addText(java.lang.String tag, java.lang.String data) {
            ensureInitialized();
            dropBox.addText(tag, data);
        }

        private synchronized void ensureInitialized() {
            if (dropBox == null) {
                dropBox = ((android.os.DropBoxManager) (getSystemContext().getSystemService(android.content.Context.DROPBOX_SERVICE)));
            }
        }
    }

    public static void main(java.lang.String[] args) {
        android.os.Trace.traceBegin(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER, "ActivityThreadMain");
        com.android.internal.os.SamplingProfilerIntegration.start();
        // CloseGuard defaults to true and can be quite spammy.  We
        // disable it here, but selectively enable it later (via
        // StrictMode) on debug builds, but using DropBox, not logs.
        dalvik.system.CloseGuard.setEnabled(false);
        android.os.Environment.initForCurrentUser();
        // Set the reporter for event logging in libcore
        libcore.io.EventLogger.setReporter(new android.app.ActivityThread.EventLoggingReporter());
        // Make sure TrustedCertificateStore looks in the right place for CA certificates
        final java.io.File configDir = android.os.Environment.getUserConfigDirectory(android.os.UserHandle.myUserId());
        com.android.org.conscrypt.TrustedCertificateStore.setDefaultUserDirectory(configDir);
        android.os.Process.setArgV0("<pre-initialized>");
        android.os.Looper.prepareMainLooper();
        android.app.ActivityThread thread = new android.app.ActivityThread();
        thread.attach(false);
        if (android.app.ActivityThread.sMainThreadHandler == null) {
            android.app.ActivityThread.sMainThreadHandler = thread.getHandler();
        }
        if (false) {
            android.os.Looper.myLooper().setMessageLogging(new android.util.LogPrinter(android.util.Log.DEBUG, "ActivityThread"));
        }
        // End of event ActivityThreadMain.
        android.os.Trace.traceEnd(android.os.Trace.TRACE_TAG_ACTIVITY_MANAGER);
        android.os.Looper.loop();
        throw new java.lang.RuntimeException("Main thread loop unexpectedly exited");
    }
}

