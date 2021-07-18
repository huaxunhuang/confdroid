package android.os;


/**
 * Tools for managing OS processes.
 */
public class Process {
    private static final java.lang.String LOG_TAG = "Process";

    /**
     *
     *
     * @unknown for internal use only.
     */
    public static final java.lang.String ZYGOTE_SOCKET = "zygote";

    /**
     *
     *
     * @unknown for internal use only.
     */
    public static final java.lang.String SECONDARY_ZYGOTE_SOCKET = "zygote_secondary";

    /**
     * Defines the root UID.
     *
     * @unknown 
     */
    public static final int ROOT_UID = 0;

    /**
     * Defines the UID/GID under which system code runs.
     */
    public static final int SYSTEM_UID = 1000;

    /**
     * Defines the UID/GID under which the telephony code runs.
     */
    public static final int PHONE_UID = 1001;

    /**
     * Defines the UID/GID for the user shell.
     *
     * @unknown 
     */
    public static final int SHELL_UID = 2000;

    /**
     * Defines the UID/GID for the log group.
     *
     * @unknown 
     */
    public static final int LOG_UID = 1007;

    /**
     * Defines the UID/GID for the WIFI supplicant process.
     *
     * @unknown 
     */
    public static final int WIFI_UID = 1010;

    /**
     * Defines the UID/GID for the mediaserver process.
     *
     * @unknown 
     */
    public static final int MEDIA_UID = 1013;

    /**
     * Defines the UID/GID for the DRM process.
     *
     * @unknown 
     */
    public static final int DRM_UID = 1019;

    /**
     * Defines the UID/GID for the group that controls VPN services.
     *
     * @unknown 
     */
    public static final int VPN_UID = 1016;

    /**
     * Defines the UID/GID for the NFC service process.
     *
     * @unknown 
     */
    public static final int NFC_UID = 1027;

    /**
     * Defines the UID/GID for the Bluetooth service process.
     *
     * @unknown 
     */
    public static final int BLUETOOTH_UID = 1002;

    /**
     * Defines the GID for the group that allows write access to the internal media storage.
     *
     * @unknown 
     */
    public static final int MEDIA_RW_GID = 1023;

    /**
     * Access to installed package details
     *
     * @unknown 
     */
    public static final int PACKAGE_INFO_GID = 1032;

    /**
     * Defines the UID/GID for the shared RELRO file updater process.
     *
     * @unknown 
     */
    public static final int SHARED_RELRO_UID = 1037;

    /**
     * Defines the UID/GID for the audioserver process.
     *
     * @unknown 
     */
    public static final int AUDIOSERVER_UID = 1041;

    /**
     * Defines the UID/GID for the cameraserver process
     *
     * @unknown 
     */
    public static final int CAMERASERVER_UID = 1047;

    /**
     * Defines the start of a range of UIDs (and GIDs), going from this
     * number to {@link #LAST_APPLICATION_UID} that are reserved for assigning
     * to applications.
     */
    public static final int FIRST_APPLICATION_UID = 10000;

    /**
     * Last of application-specific UIDs starting at
     * {@link #FIRST_APPLICATION_UID}.
     */
    public static final int LAST_APPLICATION_UID = 19999;

    /**
     * First uid used for fully isolated sandboxed processes (with no permissions of their own)
     *
     * @unknown 
     */
    public static final int FIRST_ISOLATED_UID = 99000;

    /**
     * Last uid used for fully isolated sandboxed processes (with no permissions of their own)
     *
     * @unknown 
     */
    public static final int LAST_ISOLATED_UID = 99999;

    /**
     * Defines the gid shared by all applications running under the same profile.
     *
     * @unknown 
     */
    public static final int SHARED_USER_GID = 9997;

    /**
     * First gid for applications to share resources. Used when forward-locking
     * is enabled but all UserHandles need to be able to read the resources.
     *
     * @unknown 
     */
    public static final int FIRST_SHARED_APPLICATION_GID = 50000;

    /**
     * Last gid for applications to share resources. Used when forward-locking
     * is enabled but all UserHandles need to be able to read the resources.
     *
     * @unknown 
     */
    public static final int LAST_SHARED_APPLICATION_GID = 59999;

    /**
     * Standard priority of application threads.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_DEFAULT = 0;

    /* ***************************************
    ** Keep in sync with utils/threads.h **
    ***************************************
     */
    /**
     * Lowest available thread priority.  Only for those who really, really
     * don't want to run if anything else is happening.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_LOWEST = 19;

    /**
     * Standard priority background threads.  This gives your thread a slightly
     * lower than normal priority, so that it will have less chance of impacting
     * the responsiveness of the user interface.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_BACKGROUND = 10;

    /**
     * Standard priority of threads that are currently running a user interface
     * that the user is interacting with.  Applications can not normally
     * change to this priority; the system will automatically adjust your
     * application threads as the user moves through the UI.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_FOREGROUND = -2;

    /**
     * Standard priority of system display threads, involved in updating
     * the user interface.  Applications can not
     * normally change to this priority.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_DISPLAY = -4;

    /**
     * Standard priority of the most important display threads, for compositing
     * the screen and retrieving input events.  Applications can not normally
     * change to this priority.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_URGENT_DISPLAY = -8;

    /**
     * Standard priority of audio threads.  Applications can not normally
     * change to this priority.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_AUDIO = -16;

    /**
     * Standard priority of the most important audio threads.
     * Applications can not normally change to this priority.
     * Use with {@link #setThreadPriority(int)} and
     * {@link #setThreadPriority(int, int)}, <b>not</b> with the normal
     * {@link java.lang.Thread} class.
     */
    public static final int THREAD_PRIORITY_URGENT_AUDIO = -19;

    /**
     * Minimum increment to make a priority more favorable.
     */
    public static final int THREAD_PRIORITY_MORE_FAVORABLE = -1;

    /**
     * Minimum increment to make a priority less favorable.
     */
    public static final int THREAD_PRIORITY_LESS_FAVORABLE = +1;

    /**
     * Default scheduling policy
     *
     * @unknown 
     */
    public static final int SCHED_OTHER = 0;

    /**
     * First-In First-Out scheduling policy
     *
     * @unknown 
     */
    public static final int SCHED_FIFO = 1;

    /**
     * Round-Robin scheduling policy
     *
     * @unknown 
     */
    public static final int SCHED_RR = 2;

    /**
     * Batch scheduling policy
     *
     * @unknown 
     */
    public static final int SCHED_BATCH = 3;

    /**
     * Idle scheduling policy
     *
     * @unknown 
     */
    public static final int SCHED_IDLE = 5;

    /**
     * Reset scheduler choice on fork.
     *
     * @unknown 
     */
    public static final int SCHED_RESET_ON_FORK = 0x40000000;

    // Keep in sync with SP_* constants of enum type SchedPolicy
    // declared in system/core/include/cutils/sched_policy.h,
    // except THREAD_GROUP_DEFAULT does not correspond to any SP_* value.
    /**
     * Default thread group -
     * has meaning with setProcessGroup() only, cannot be used with setThreadGroup().
     * When used with setProcessGroup(), the group of each thread in the process
     * is conditionally changed based on that thread's current priority, as follows:
     * threads with priority numerically less than THREAD_PRIORITY_BACKGROUND
     * are moved to foreground thread group.  All other threads are left unchanged.
     *
     * @unknown 
     */
    public static final int THREAD_GROUP_DEFAULT = -1;

    /**
     * Background thread group - All threads in
     * this group are scheduled with a reduced share of the CPU.
     * Value is same as constant SP_BACKGROUND of enum SchedPolicy.
     * FIXME rename to THREAD_GROUP_BACKGROUND.
     *
     * @unknown 
     */
    public static final int THREAD_GROUP_BG_NONINTERACTIVE = 0;

    /**
     * Foreground thread group - All threads in
     * this group are scheduled with a normal share of the CPU.
     * Value is same as constant SP_FOREGROUND of enum SchedPolicy.
     * Not used at this level.
     *
     * @unknown 
     */
    private static final int THREAD_GROUP_FOREGROUND = 1;

    /**
     * System thread group.
     *
     * @unknown 
     */
    public static final int THREAD_GROUP_SYSTEM = 2;

    /**
     * Application audio thread group.
     *
     * @unknown 
     */
    public static final int THREAD_GROUP_AUDIO_APP = 3;

    /**
     * System audio thread group.
     *
     * @unknown 
     */
    public static final int THREAD_GROUP_AUDIO_SYS = 4;

    /**
     * Thread group for top foreground app.
     *
     * @unknown 
     */
    public static final int THREAD_GROUP_TOP_APP = 5;

    public static final int SIGNAL_QUIT = 3;

    public static final int SIGNAL_KILL = 9;

    public static final int SIGNAL_USR1 = 10;

    private static long sStartElapsedRealtime;

    private static long sStartUptimeMillis;

    /**
     * State for communicating with the zygote process.
     *
     * @unknown for internal use only.
     */
    public static class ZygoteState {
        final android.net.LocalSocket socket;

        final java.io.DataInputStream inputStream;

        final java.io.BufferedWriter writer;

        final java.util.List<java.lang.String> abiList;

        boolean mClosed;

        private ZygoteState(android.net.LocalSocket socket, java.io.DataInputStream inputStream, java.io.BufferedWriter writer, java.util.List<java.lang.String> abiList) {
            this.socket = socket;
            this.inputStream = inputStream;
            this.writer = writer;
            this.abiList = abiList;
        }

        public static android.os.Process.ZygoteState connect(java.lang.String socketAddress) throws java.io.IOException {
            java.io.DataInputStream zygoteInputStream = null;
            java.io.BufferedWriter zygoteWriter = null;
            final android.net.LocalSocket zygoteSocket = new android.net.LocalSocket();
            try {
                zygoteSocket.connect(new android.net.LocalSocketAddress(socketAddress, android.net.LocalSocketAddress.Namespace.RESERVED));
                zygoteInputStream = new java.io.DataInputStream(zygoteSocket.getInputStream());
                zygoteWriter = new java.io.BufferedWriter(new java.io.OutputStreamWriter(zygoteSocket.getOutputStream()), 256);
            } catch (java.io.IOException ex) {
                try {
                    zygoteSocket.close();
                } catch (java.io.IOException ignore) {
                }
                throw ex;
            }
            java.lang.String abiListString = android.os.Process.getAbiList(zygoteWriter, zygoteInputStream);
            android.util.Log.i("Zygote", "Process: zygote socket opened, supported ABIS: " + abiListString);
            return new android.os.Process.ZygoteState(zygoteSocket, zygoteInputStream, zygoteWriter, java.util.Arrays.asList(abiListString.split(",")));
        }

        boolean matches(java.lang.String abi) {
            return abiList.contains(abi);
        }

        public void close() {
            try {
                socket.close();
            } catch (java.io.IOException ex) {
                android.util.Log.e(android.os.Process.LOG_TAG, "I/O exception on routine close", ex);
            }
            mClosed = true;
        }

        boolean isClosed() {
            return mClosed;
        }
    }

    /**
     * The state of the connection to the primary zygote.
     */
    static android.os.Process.ZygoteState primaryZygoteState;

    /**
     * The state of the connection to the secondary zygote.
     */
    static android.os.Process.ZygoteState secondaryZygoteState;

    /**
     * Start a new process.
     *
     * <p>If processes are enabled, a new process is created and the
     * static main() function of a <var>processClass</var> is executed there.
     * The process will continue running after this function returns.
     *
     * <p>If processes are not enabled, a new thread in the caller's
     * process is created and main() of <var>processClass</var> called there.
     *
     * <p>The niceName parameter, if not an empty string, is a custom name to
     * give to the process instead of using processClass.  This allows you to
     * make easily identifyable processes even if you are using the same base
     * <var>processClass</var> to start them.
     *
     * @param processClass
     * 		The class to use as the process's main entry
     * 		point.
     * @param niceName
     * 		A more readable name to use for the process.
     * @param uid
     * 		The user-id under which the process will run.
     * @param gid
     * 		The group-id under which the process will run.
     * @param gids
     * 		Additional group-ids associated with the process.
     * @param debugFlags
     * 		Additional flags.
     * @param targetSdkVersion
     * 		The target SDK version for the app.
     * @param seInfo
     * 		null-ok SELinux information for the new process.
     * @param abi
     * 		non-null the ABI this app should be started with.
     * @param instructionSet
     * 		null-ok the instruction set to use.
     * @param appDataDir
     * 		null-ok the data directory of the app.
     * @param zygoteArgs
     * 		Additional arguments to supply to the zygote process.
     * @return An object that describes the result of the attempt to start the process.
     * @throws RuntimeException
     * 		on fatal start failure
     * 		
     * 		{@hide }
     */
    public static final android.os.Process.ProcessStartResult start(final java.lang.String processClass, final java.lang.String niceName, int uid, int gid, int[] gids, int debugFlags, int mountExternal, int targetSdkVersion, java.lang.String seInfo, java.lang.String abi, java.lang.String instructionSet, java.lang.String appDataDir, java.lang.String[] zygoteArgs) {
        try {
            return android.os.Process.startViaZygote(processClass, niceName, uid, gid, gids, debugFlags, mountExternal, targetSdkVersion, seInfo, abi, instructionSet, appDataDir, zygoteArgs);
        } catch (android.os.ZygoteStartFailedEx ex) {
            android.util.Log.e(android.os.Process.LOG_TAG, "Starting VM process through Zygote failed");
            throw new java.lang.RuntimeException("Starting VM process through Zygote failed", ex);
        }
    }

    /**
     * retry interval for opening a zygote socket
     */
    static final int ZYGOTE_RETRY_MILLIS = 500;

    /**
     * Queries the zygote for the list of ABIS it supports.
     *
     * @throws ZygoteStartFailedEx
     * 		if the query failed.
     */
    private static java.lang.String getAbiList(java.io.BufferedWriter writer, java.io.DataInputStream inputStream) throws java.io.IOException {
        // Each query starts with the argument count (1 in this case)
        writer.write("1");
        // ... followed by a new-line.
        writer.newLine();
        // ... followed by our only argument.
        writer.write("--query-abi-list");
        writer.newLine();
        writer.flush();
        // The response is a length prefixed stream of ASCII bytes.
        int numBytes = inputStream.readInt();
        byte[] bytes = new byte[numBytes];
        inputStream.readFully(bytes);
        return new java.lang.String(bytes, java.nio.charset.StandardCharsets.US_ASCII);
    }

    /**
     * Sends an argument list to the zygote process, which starts a new child
     * and returns the child's pid. Please note: the present implementation
     * replaces newlines in the argument list with spaces.
     *
     * @throws ZygoteStartFailedEx
     * 		if process start failed for any reason
     */
    private static android.os.Process.ProcessStartResult zygoteSendArgsAndGetResult(android.os.Process.ZygoteState zygoteState, java.util.ArrayList<java.lang.String> args) throws android.os.ZygoteStartFailedEx {
        try {
            // Throw early if any of the arguments are malformed. This means we can
            // avoid writing a partial response to the zygote.
            int sz = args.size();
            for (int i = 0; i < sz; i++) {
                if (args.get(i).indexOf('\n') >= 0) {
                    throw new android.os.ZygoteStartFailedEx("embedded newlines not allowed");
                }
            }
            /**
             * See com.android.internal.os.ZygoteInit.readArgumentList()
             * Presently the wire format to the zygote process is:
             * a) a count of arguments (argc, in essence)
             * b) a number of newline-separated argument strings equal to count
             *
             * After the zygote process reads these it will write the pid of
             * the child or -1 on failure, followed by boolean to
             * indicate whether a wrapper process was used.
             */
            final java.io.BufferedWriter writer = zygoteState.writer;
            final java.io.DataInputStream inputStream = zygoteState.inputStream;
            writer.write(java.lang.Integer.toString(args.size()));
            writer.newLine();
            for (int i = 0; i < sz; i++) {
                java.lang.String arg = args.get(i);
                writer.write(arg);
                writer.newLine();
            }
            writer.flush();
            // Should there be a timeout on this?
            android.os.Process.ProcessStartResult result = new android.os.Process.ProcessStartResult();
            // Always read the entire result from the input stream to avoid leaving
            // bytes in the stream for future process starts to accidentally stumble
            // upon.
            result.pid = inputStream.readInt();
            result.usingWrapper = inputStream.readBoolean();
            if (result.pid < 0) {
                throw new android.os.ZygoteStartFailedEx("fork() failed");
            }
            return result;
        } catch (java.io.IOException ex) {
            zygoteState.close();
            throw new android.os.ZygoteStartFailedEx(ex);
        }
    }

    /**
     * Starts a new process via the zygote mechanism.
     *
     * @param processClass
     * 		Class name whose static main() to run
     * @param niceName
     * 		'nice' process name to appear in ps
     * @param uid
     * 		a POSIX uid that the new process should setuid() to
     * @param gid
     * 		a POSIX gid that the new process shuold setgid() to
     * @param gids
     * 		null-ok; a list of supplementary group IDs that the
     * 		new process should setgroup() to.
     * @param debugFlags
     * 		Additional flags.
     * @param targetSdkVersion
     * 		The target SDK version for the app.
     * @param seInfo
     * 		null-ok SELinux information for the new process.
     * @param abi
     * 		the ABI the process should use.
     * @param instructionSet
     * 		null-ok the instruction set to use.
     * @param appDataDir
     * 		null-ok the data directory of the app.
     * @param extraArgs
     * 		Additional arguments to supply to the zygote process.
     * @return An object that describes the result of the attempt to start the process.
     * @throws ZygoteStartFailedEx
     * 		if process start failed for any reason
     */
    private static android.os.Process.ProcessStartResult startViaZygote(final java.lang.String processClass, final java.lang.String niceName, final int uid, final int gid, final int[] gids, int debugFlags, int mountExternal, int targetSdkVersion, java.lang.String seInfo, java.lang.String abi, java.lang.String instructionSet, java.lang.String appDataDir, java.lang.String[] extraArgs) throws android.os.ZygoteStartFailedEx {
        synchronized(android.os.Process.class) {
            java.util.ArrayList<java.lang.String> argsForZygote = new java.util.ArrayList<java.lang.String>();
            // --runtime-args, --setuid=, --setgid=,
            // and --setgroups= must go first
            argsForZygote.add("--runtime-args");
            argsForZygote.add("--setuid=" + uid);
            argsForZygote.add("--setgid=" + gid);
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_ENABLE_JNI_LOGGING) != 0) {
                argsForZygote.add("--enable-jni-logging");
            }
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_ENABLE_SAFEMODE) != 0) {
                argsForZygote.add("--enable-safemode");
            }
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_ENABLE_DEBUGGER) != 0) {
                argsForZygote.add("--enable-debugger");
            }
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_ENABLE_CHECKJNI) != 0) {
                argsForZygote.add("--enable-checkjni");
            }
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_GENERATE_DEBUG_INFO) != 0) {
                argsForZygote.add("--generate-debug-info");
            }
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_ALWAYS_JIT) != 0) {
                argsForZygote.add("--always-jit");
            }
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_NATIVE_DEBUGGABLE) != 0) {
                argsForZygote.add("--native-debuggable");
            }
            if ((debugFlags & com.android.internal.os.Zygote.DEBUG_ENABLE_ASSERT) != 0) {
                argsForZygote.add("--enable-assert");
            }
            if (mountExternal == com.android.internal.os.Zygote.MOUNT_EXTERNAL_DEFAULT) {
                argsForZygote.add("--mount-external-default");
            } else
                if (mountExternal == com.android.internal.os.Zygote.MOUNT_EXTERNAL_READ) {
                    argsForZygote.add("--mount-external-read");
                } else
                    if (mountExternal == com.android.internal.os.Zygote.MOUNT_EXTERNAL_WRITE) {
                        argsForZygote.add("--mount-external-write");
                    }


            argsForZygote.add("--target-sdk-version=" + targetSdkVersion);
            // TODO optionally enable debuger
            // argsForZygote.add("--enable-debugger");
            // --setgroups is a comma-separated list
            if ((gids != null) && (gids.length > 0)) {
                java.lang.StringBuilder sb = new java.lang.StringBuilder();
                sb.append("--setgroups=");
                int sz = gids.length;
                for (int i = 0; i < sz; i++) {
                    if (i != 0) {
                        sb.append(',');
                    }
                    sb.append(gids[i]);
                }
                argsForZygote.add(sb.toString());
            }
            if (niceName != null) {
                argsForZygote.add("--nice-name=" + niceName);
            }
            if (seInfo != null) {
                argsForZygote.add("--seinfo=" + seInfo);
            }
            if (instructionSet != null) {
                argsForZygote.add("--instruction-set=" + instructionSet);
            }
            if (appDataDir != null) {
                argsForZygote.add("--app-data-dir=" + appDataDir);
            }
            argsForZygote.add(processClass);
            if (extraArgs != null) {
                for (java.lang.String arg : extraArgs) {
                    argsForZygote.add(arg);
                }
            }
            return android.os.Process.zygoteSendArgsAndGetResult(android.os.Process.openZygoteSocketIfNeeded(abi), argsForZygote);
        }
    }

    /**
     * Tries to establish a connection to the zygote that handles a given {@code abi}. Might block and retry if the
     * zygote is unresponsive. This method is a no-op if a connection is already open.
     *
     * @unknown 
     */
    public static void establishZygoteConnectionForAbi(java.lang.String abi) {
        try {
            android.os.Process.openZygoteSocketIfNeeded(abi);
        } catch (android.os.ZygoteStartFailedEx ex) {
            throw new java.lang.RuntimeException("Unable to connect to zygote for abi: " + abi, ex);
        }
    }

    /**
     * Tries to open socket to Zygote process if not already open. If
     * already open, does nothing.  May block and retry.
     */
    private static android.os.Process.ZygoteState openZygoteSocketIfNeeded(java.lang.String abi) throws android.os.ZygoteStartFailedEx {
        if ((android.os.Process.primaryZygoteState == null) || android.os.Process.primaryZygoteState.isClosed()) {
            try {
                android.os.Process.primaryZygoteState = android.os.Process.ZygoteState.connect(android.os.Process.ZYGOTE_SOCKET);
            } catch (java.io.IOException ioe) {
                throw new android.os.ZygoteStartFailedEx("Error connecting to primary zygote", ioe);
            }
        }
        if (android.os.Process.primaryZygoteState.matches(abi)) {
            return android.os.Process.primaryZygoteState;
        }
        // The primary zygote didn't match. Try the secondary.
        if ((android.os.Process.secondaryZygoteState == null) || android.os.Process.secondaryZygoteState.isClosed()) {
            try {
                android.os.Process.secondaryZygoteState = android.os.Process.ZygoteState.connect(android.os.Process.SECONDARY_ZYGOTE_SOCKET);
            } catch (java.io.IOException ioe) {
                throw new android.os.ZygoteStartFailedEx("Error connecting to secondary zygote", ioe);
            }
        }
        if (android.os.Process.secondaryZygoteState.matches(abi)) {
            return android.os.Process.secondaryZygoteState;
        }
        throw new android.os.ZygoteStartFailedEx("Unsupported zygote ABI: " + abi);
    }

    /**
     * Returns elapsed milliseconds of the time this process has run.
     *
     * @return Returns the number of milliseconds this process has return.
     */
    public static final native long getElapsedCpuTime();

    /**
     * Return the {@link SystemClock#elapsedRealtime()} at which this process was started.
     */
    public static final long getStartElapsedRealtime() {
        return android.os.Process.sStartElapsedRealtime;
    }

    /**
     * Return the {@link SystemClock#uptimeMillis()} at which this process was started.
     */
    public static final long getStartUptimeMillis() {
        return android.os.Process.sStartUptimeMillis;
    }

    /**
     *
     *
     * @unknown 
     */
    public static final void setStartTimes(long elapsedRealtime, long uptimeMillis) {
        android.os.Process.sStartElapsedRealtime = elapsedRealtime;
        android.os.Process.sStartUptimeMillis = uptimeMillis;
    }

    /**
     * Returns true if the current process is a 64-bit runtime.
     */
    public static final boolean is64Bit() {
        return is64Bit();
    }

    /**
     * Returns the identifier of this process, which can be used with
     * {@link #killProcess} and {@link #sendSignal}.
     */
    public static final int myPid() {
        return android.system.Os.getpid();
    }

    /**
     * Returns the identifier of this process' parent.
     *
     * @unknown 
     */
    public static final int myPpid() {
        return android.system.Os.getppid();
    }

    /**
     * Returns the identifier of the calling thread, which be used with
     * {@link #setThreadPriority(int, int)}.
     */
    public static final int myTid() {
        return android.system.Os.gettid();
    }

    /**
     * Returns the identifier of this process's uid.  This is the kernel uid
     * that the process is running under, which is the identity of its
     * app-specific sandbox.  It is different from {@link #myUserHandle} in that
     * a uid identifies a specific app sandbox in a specific user.
     */
    public static final int myUid() {
        return android.system.Os.getuid();
    }

    /**
     * Returns this process's user handle.  This is the
     * user the process is running under.  It is distinct from
     * {@link #myUid()} in that a particular user will have multiple
     * distinct apps running under it each with their own uid.
     */
    public static android.os.UserHandle myUserHandle() {
        return android.os.UserHandle.of(android.os.UserHandle.getUserId(android.os.Process.myUid()));
    }

    /**
     * Returns whether the given uid belongs to an application.
     *
     * @param uid
     * 		A kernel uid.
     * @return Whether the uid corresponds to an application sandbox running in
    a specific user.
     */
    public static boolean isApplicationUid(int uid) {
        return android.os.UserHandle.isApp(uid);
    }

    /**
     * Returns whether the current process is in an isolated sandbox.
     *
     * @unknown 
     */
    public static final boolean isIsolated() {
        return android.os.Process.isIsolated(android.os.Process.myUid());
    }

    /**
     * {@hide }
     */
    public static final boolean isIsolated(int uid) {
        uid = android.os.UserHandle.getAppId(uid);
        return (uid >= android.os.Process.FIRST_ISOLATED_UID) && (uid <= android.os.Process.LAST_ISOLATED_UID);
    }

    /**
     * Returns the UID assigned to a particular user name, or -1 if there is
     * none.  If the given string consists of only numbers, it is converted
     * directly to a uid.
     */
    public static final native int getUidForName(java.lang.String name);

    /**
     * Returns the GID assigned to a particular user name, or -1 if there is
     * none.  If the given string consists of only numbers, it is converted
     * directly to a gid.
     */
    public static final native int getGidForName(java.lang.String name);

    /**
     * Returns a uid for a currently running process.
     *
     * @param pid
     * 		the process id
     * @return the uid of the process, or -1 if the process is not running.
     * @unknown pending API council review
     */
    public static final int getUidForPid(int pid) {
        java.lang.String[] procStatusLabels = new java.lang.String[]{ "Uid:" };
        long[] procStatusValues = new long[1];
        procStatusValues[0] = -1;
        android.os.Process.readProcLines(("/proc/" + pid) + "/status", procStatusLabels, procStatusValues);
        return ((int) (procStatusValues[0]));
    }

    /**
     * Returns the parent process id for a currently running process.
     *
     * @param pid
     * 		the process id
     * @return the parent process id of the process, or -1 if the process is not running.
     * @unknown 
     */
    public static final int getParentPid(int pid) {
        java.lang.String[] procStatusLabels = new java.lang.String[]{ "PPid:" };
        long[] procStatusValues = new long[1];
        procStatusValues[0] = -1;
        android.os.Process.readProcLines(("/proc/" + pid) + "/status", procStatusLabels, procStatusValues);
        return ((int) (procStatusValues[0]));
    }

    /**
     * Returns the thread group leader id for a currently running thread.
     *
     * @param tid
     * 		the thread id
     * @return the thread group leader id of the thread, or -1 if the thread is not running.
    This is same as what getpid(2) would return if called by tid.
     * @unknown 
     */
    public static final int getThreadGroupLeader(int tid) {
        java.lang.String[] procStatusLabels = new java.lang.String[]{ "Tgid:" };
        long[] procStatusValues = new long[1];
        procStatusValues[0] = -1;
        android.os.Process.readProcLines(("/proc/" + tid) + "/status", procStatusLabels, procStatusValues);
        return ((int) (procStatusValues[0]));
    }

    /**
     * Set the priority of a thread, based on Linux priorities.
     *
     * @param tid
     * 		The identifier of the thread/process to change.
     * @param priority
     * 		A Linux priority level, from -20 for highest scheduling
     * 		priority to 19 for lowest scheduling priority.
     * @throws IllegalArgumentException
     * 		Throws IllegalArgumentException if
     * 		<var>tid</var> does not exist.
     * @throws SecurityException
     * 		Throws SecurityException if your process does
     * 		not have permission to modify the given thread, or to use the given
     * 		priority.
     */
    public static final native void setThreadPriority(int tid, int priority) throws java.lang.IllegalArgumentException, java.lang.SecurityException;

    /**
     * Call with 'false' to cause future calls to {@link #setThreadPriority(int)} to
     * throw an exception if passed a background-level thread priority.  This is only
     * effective if the JNI layer is built with GUARD_THREAD_PRIORITY defined to 1.
     *
     * @unknown 
     */
    public static final native void setCanSelfBackground(boolean backgroundOk);

    /**
     * Sets the scheduling group for a thread.
     *
     * @unknown 
     * @param tid
     * 		The identifier of the thread to change.
     * @param group
     * 		The target group for this thread from THREAD_GROUP_*.
     * @throws IllegalArgumentException
     * 		Throws IllegalArgumentException if
     * 		<var>tid</var> does not exist.
     * @throws SecurityException
     * 		Throws SecurityException if your process does
     * 		not have permission to modify the given thread, or to use the given
     * 		priority.
     * 		If the thread is a thread group leader, that is it's gettid() == getpid(),
     * 		then the other threads in the same thread group are _not_ affected.
     * 		
     * 		Does not set cpuset for some historical reason, just calls
     * 		libcutils::set_sched_policy().
     */
    public static final native void setThreadGroup(int tid, int group) throws java.lang.IllegalArgumentException, java.lang.SecurityException;

    /**
     * Sets the scheduling group for a process and all child threads
     *
     * @unknown 
     * @param pid
     * 		The identifier of the process to change.
     * @param group
     * 		The target group for this process from THREAD_GROUP_*.
     * @throws IllegalArgumentException
     * 		Throws IllegalArgumentException if
     * 		<var>tid</var> does not exist.
     * @throws SecurityException
     * 		Throws SecurityException if your process does
     * 		not have permission to modify the given thread, or to use the given
     * 		priority.
     * 		
     * 		group == THREAD_GROUP_DEFAULT means to move all non-background priority
     * 		threads to the foreground scheduling group, but to leave background
     * 		priority threads alone.  group == THREAD_GROUP_BG_NONINTERACTIVE moves all
     * 		threads, regardless of priority, to the background scheduling group.
     * 		group == THREAD_GROUP_FOREGROUND is not allowed.
     * 		
     * 		Always sets cpusets.
     */
    public static final native void setProcessGroup(int pid, int group) throws java.lang.IllegalArgumentException, java.lang.SecurityException;

    /**
     * Return the scheduling group of requested process.
     *
     * @unknown 
     */
    public static final native int getProcessGroup(int pid) throws java.lang.IllegalArgumentException, java.lang.SecurityException;

    /**
     * On some devices, the foreground process may have one or more CPU
     * cores exclusively reserved for it. This method can be used to
     * retrieve which cores that are (if any), so the calling process
     * can then use sched_setaffinity() to lock a thread to these cores.
     * Note that the calling process must currently be running in the
     * foreground for this method to return any cores.
     *
     * The CPU core(s) exclusively reserved for the foreground process will
     * stay reserved for as long as the process stays in the foreground.
     *
     * As soon as a process leaves the foreground, those CPU cores will
     * no longer be reserved for it, and will most likely be reserved for
     * the new foreground process. It's not necessary to change the affinity
     * of your process when it leaves the foreground (if you had previously
     * set it to use a reserved core); the OS will automatically take care
     * of resetting the affinity at that point.
     *
     * @return an array of integers, indicating the CPU cores exclusively
    reserved for this process. The array will have length zero if no
    CPU cores are exclusively reserved for this process at this point
    in time.
     */
    public static final native int[] getExclusiveCores();

    /**
     * Set the priority of the calling thread, based on Linux priorities.  See
     * {@link #setThreadPriority(int, int)} for more information.
     *
     * @param priority
     * 		A Linux priority level, from -20 for highest scheduling
     * 		priority to 19 for lowest scheduling priority.
     * @throws IllegalArgumentException
     * 		Throws IllegalArgumentException if
     * 		<var>tid</var> does not exist.
     * @throws SecurityException
     * 		Throws SecurityException if your process does
     * 		not have permission to modify the given thread, or to use the given
     * 		priority.
     * @see #setThreadPriority(int, int)
     */
    public static final native void setThreadPriority(int priority) throws java.lang.IllegalArgumentException, java.lang.SecurityException;

    /**
     * Return the current priority of a thread, based on Linux priorities.
     *
     * @param tid
     * 		The identifier of the thread/process to change.
     * @return Returns the current priority, as a Linux priority level,
    from -20 for highest scheduling priority to 19 for lowest scheduling
    priority.
     * @throws IllegalArgumentException
     * 		Throws IllegalArgumentException if
     * 		<var>tid</var> does not exist.
     */
    public static final native int getThreadPriority(int tid) throws java.lang.IllegalArgumentException;

    /**
     * Return the current scheduling policy of a thread, based on Linux.
     *
     * @param tid
     * 		The identifier of the thread/process to get the scheduling policy.
     * @throws IllegalArgumentException
     * 		Throws IllegalArgumentException if
     * 		<var>tid</var> does not exist, or if <var>priority</var> is out of range for the policy.
     * @throws SecurityException
     * 		Throws SecurityException if your process does
     * 		not have permission to modify the given thread, or to use the given
     * 		scheduling policy or priority.
     * 		
     * 		{@hide }
     */
    @android.annotation.TestApi
    public static final native int getThreadScheduler(int tid) throws java.lang.IllegalArgumentException;

    /**
     * Set the scheduling policy and priority of a thread, based on Linux.
     *
     * @param tid
     * 		The identifier of the thread/process to change.
     * @param policy
     * 		A Linux scheduling policy such as SCHED_OTHER etc.
     * @param priority
     * 		A Linux priority level in a range appropriate for the given policy.
     * @throws IllegalArgumentException
     * 		Throws IllegalArgumentException if
     * 		<var>tid</var> does not exist, or if <var>priority</var> is out of range for the policy.
     * @throws SecurityException
     * 		Throws SecurityException if your process does
     * 		not have permission to modify the given thread, or to use the given
     * 		scheduling policy or priority.
     * 		
     * 		{@hide }
     */
    public static final native void setThreadScheduler(int tid, int policy, int priority) throws java.lang.IllegalArgumentException;

    /**
     * Determine whether the current environment supports multiple processes.
     *
     * @return Returns true if the system can run in multiple processes, else
    false if everything is running in a single process.
     * @deprecated This method always returns true.  Do not use.
     */
    @java.lang.Deprecated
    public static final boolean supportsProcesses() {
        return true;
    }

    /**
     * Adjust the swappiness level for a process.
     *
     * @param pid
     * 		The process identifier to set.
     * @param is_increased
     * 		Whether swappiness should be increased or default.
     * @return Returns true if the underlying system supports this
    feature, else false.

    {@hide }
     */
    public static final native boolean setSwappiness(int pid, boolean is_increased);

    /**
     * Change this process's argv[0] parameter.  This can be useful to show
     * more descriptive information in things like the 'ps' command.
     *
     * @param text
     * 		The new name of this process.
     * 		
     * 		{@hide }
     */
    public static final native void setArgV0(java.lang.String text);

    /**
     * Kill the process with the given PID.
     * Note that, though this API allows us to request to
     * kill any process based on its PID, the kernel will
     * still impose standard restrictions on which PIDs you
     * are actually able to kill.  Typically this means only
     * the process running the caller's packages/application
     * and any additional processes created by that app; packages
     * sharing a common UID will also be able to kill each
     * other's processes.
     */
    public static final void killProcess(int pid) {
        android.os.Process.sendSignal(pid, android.os.Process.SIGNAL_KILL);
    }

    /**
     *
     *
     * @unknown 
     */
    public static final native int setUid(int uid);

    /**
     *
     *
     * @unknown 
     */
    public static final native int setGid(int uid);

    /**
     * Send a signal to the given process.
     *
     * @param pid
     * 		The pid of the target process.
     * @param signal
     * 		The signal to send.
     */
    public static final native void sendSignal(int pid, int signal);

    /**
     *
     *
     * @unknown Private impl for avoiding a log message...  DO NOT USE without doing
    your own log, or the Android Illuminati will find you some night and
    beat you up.
     */
    public static final void killProcessQuiet(int pid) {
        android.os.Process.sendSignalQuiet(pid, android.os.Process.SIGNAL_KILL);
    }

    /**
     *
     *
     * @unknown Private impl for avoiding a log message...  DO NOT USE without doing
    your own log, or the Android Illuminati will find you some night and
    beat you up.
     */
    public static final native void sendSignalQuiet(int pid, int signal);

    /**
     *
     *
     * @unknown 
     */
    public static final native long getFreeMemory();

    /**
     *
     *
     * @unknown 
     */
    public static final native long getTotalMemory();

    /**
     *
     *
     * @unknown 
     */
    public static final native void readProcLines(java.lang.String path, java.lang.String[] reqFields, long[] outSizes);

    /**
     *
     *
     * @unknown 
     */
    public static final native int[] getPids(java.lang.String path, int[] lastArray);

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_TERM_MASK = 0xff;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_ZERO_TERM = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_SPACE_TERM = ((int) (' '));

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_TAB_TERM = ((int) ('\t'));

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_COMBINE = 0x100;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_PARENS = 0x200;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_QUOTES = 0x400;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_CHAR = 0x800;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_OUT_STRING = 0x1000;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_OUT_LONG = 0x2000;

    /**
     *
     *
     * @unknown 
     */
    public static final int PROC_OUT_FLOAT = 0x4000;

    /**
     *
     *
     * @unknown 
     */
    public static final native boolean readProcFile(java.lang.String file, int[] format, java.lang.String[] outStrings, long[] outLongs, float[] outFloats);

    /**
     *
     *
     * @unknown 
     */
    public static final native boolean parseProcLine(byte[] buffer, int startIndex, int endIndex, int[] format, java.lang.String[] outStrings, long[] outLongs, float[] outFloats);

    /**
     *
     *
     * @unknown 
     */
    public static final native int[] getPidsForCommands(java.lang.String[] cmds);

    /**
     * Gets the total Pss value for a given process, in bytes.
     *
     * @param pid
     * 		the process to the Pss for
     * @return the total Pss value for the given process in bytes,
    or -1 if the value cannot be determined
     * @unknown 
     */
    public static final native long getPss(int pid);

    /**
     * Specifies the outcome of having started a process.
     *
     * @unknown 
     */
    public static final class ProcessStartResult {
        /**
         * The PID of the newly started process.
         * Always >= 0.  (If the start failed, an exception will have been thrown instead.)
         */
        public int pid;

        /**
         * True if the process was started with a wrapper attached.
         */
        public boolean usingWrapper;
    }

    /**
     * Kill all processes in a process group started for the given
     * pid.
     *
     * @unknown 
     */
    public static final native int killProcessGroup(int uid, int pid);

    /**
     * Remove all process groups.  Expected to be called when ActivityManager
     * is restarted.
     *
     * @unknown 
     */
    public static final native void removeAllProcessGroups();

    /**
     * Check to see if a thread belongs to a given process. This may require
     * more permissions than apps generally have.
     *
     * @return true if this thread belongs to a process
     * @unknown 
     */
    public static final boolean isThreadInProcess(int tid, int pid) {
        android.os.StrictMode.ThreadPolicy oldPolicy = android.os.StrictMode.allowThreadDiskReads();
        try {
            if (android.system.Os.access((("/proc/" + tid) + "/task/") + pid, android.system.OsConstants.F_OK)) {
                return true;
            } else {
                return false;
            }
        } catch (java.lang.Exception e) {
            return false;
        } finally {
            android.os.StrictMode.setThreadPolicy(oldPolicy);
        }
    }
}

