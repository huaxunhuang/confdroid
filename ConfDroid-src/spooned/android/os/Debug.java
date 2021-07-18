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
package android.os;


/**
 * Provides various debugging methods for Android applications, including
 * tracing and allocation counts.
 * <p><strong>Logging Trace Files</strong></p>
 * <p>Debug can create log files that give details about an application, such as
 * a call stack and start/stop times for any running methods. See <a
 * href="{@docRoot }guide/developing/tools/traceview.html">Traceview: A Graphical Log Viewer</a> for
 * information about reading trace files. To start logging trace files, call one
 * of the startMethodTracing() methods. To stop tracing, call
 * {@link #stopMethodTracing()}.
 */
public final class Debug {
    private static final java.lang.String TAG = "Debug";

    /**
     * Flags for startMethodTracing().  These can be ORed together.
     *
     * TRACE_COUNT_ALLOCS adds the results from startAllocCounting to the
     * trace key file.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static final int TRACE_COUNT_ALLOCS = dalvik.system.VMDebug.TRACE_COUNT_ALLOCS;

    /**
     * Flags for printLoadedClasses().  Default behavior is to only show
     * the class name.
     */
    public static final int SHOW_FULL_DETAIL = 1;

    public static final int SHOW_CLASSLOADER = 1 << 1;

    public static final int SHOW_INITIALIZED = 1 << 2;

    // set/cleared by waitForDebugger()
    private static volatile boolean mWaiting = false;

    private Debug() {
    }

    /* How long to wait for the debugger to finish sending requests.  I've
    seen this hit 800msec on the device while waiting for a response
    to travel over USB and get processed, so we take that and add
    half a second.
     */
    private static final int MIN_DEBUGGER_IDLE = 1300;// msec


    /* how long to sleep when polling for activity */
    private static final int SPIN_DELAY = 200;// msec


    /**
     * Default trace file path and file
     */
    private static final java.lang.String DEFAULT_TRACE_BODY = "dmtrace";

    private static final java.lang.String DEFAULT_TRACE_EXTENSION = ".trace";

    /**
     * This class is used to retrieved various statistics about the memory mappings for this
     * process. The returned info is broken down by dalvik, native, and other. All results are in kB.
     */
    public static class MemoryInfo implements android.os.Parcelable {
        /**
         * The proportional set size for dalvik heap.  (Doesn't include other Dalvik overhead.)
         */
        public int dalvikPss;

        /**
         * The proportional set size that is swappable for dalvik heap.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int dalvikSwappablePss;

        /**
         * The private dirty pages used by dalvik heap.
         */
        public int dalvikPrivateDirty;

        /**
         * The shared dirty pages used by dalvik heap.
         */
        public int dalvikSharedDirty;

        /**
         * The private clean pages used by dalvik heap.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int dalvikPrivateClean;

        /**
         * The shared clean pages used by dalvik heap.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int dalvikSharedClean;

        /**
         * The dirty dalvik pages that have been swapped out.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int dalvikSwappedOut;

        /**
         * The dirty dalvik pages that have been swapped out, proportional.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int dalvikSwappedOutPss;

        /**
         * The proportional set size for the native heap.
         */
        public int nativePss;

        /**
         * The proportional set size that is swappable for the native heap.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int nativeSwappablePss;

        /**
         * The private dirty pages used by the native heap.
         */
        public int nativePrivateDirty;

        /**
         * The shared dirty pages used by the native heap.
         */
        public int nativeSharedDirty;

        /**
         * The private clean pages used by the native heap.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int nativePrivateClean;

        /**
         * The shared clean pages used by the native heap.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int nativeSharedClean;

        /**
         * The dirty native pages that have been swapped out.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int nativeSwappedOut;

        /**
         * The dirty native pages that have been swapped out, proportional.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int nativeSwappedOutPss;

        /**
         * The proportional set size for everything else.
         */
        public int otherPss;

        /**
         * The proportional set size that is swappable for everything else.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int otherSwappablePss;

        /**
         * The private dirty pages used by everything else.
         */
        public int otherPrivateDirty;

        /**
         * The shared dirty pages used by everything else.
         */
        public int otherSharedDirty;

        /**
         * The private clean pages used by everything else.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int otherPrivateClean;

        /**
         * The shared clean pages used by everything else.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int otherSharedClean;

        /**
         * The dirty pages used by anyting else that have been swapped out.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int otherSwappedOut;

        /**
         * The dirty pages used by anyting else that have been swapped out, proportional.
         */
        /**
         *
         *
         * @unknown We may want to expose this, eventually.
         */
        public int otherSwappedOutPss;

        /**
         * Whether the kernel reports proportional swap usage
         */
        /**
         *
         *
         * @unknown 
         */
        public boolean hasSwappedOutPss;

        /**
         *
         *
         * @unknown 
         */
        public static final int HEAP_UNKNOWN = 0;

        /**
         *
         *
         * @unknown 
         */
        public static final int HEAP_DALVIK = 1;

        /**
         *
         *
         * @unknown 
         */
        public static final int HEAP_NATIVE = 2;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_OTHER = 0;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_STACK = 1;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_CURSOR = 2;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_ASHMEM = 3;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_GL_DEV = 4;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_UNKNOWN_DEV = 5;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_SO = 6;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_JAR = 7;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_APK = 8;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_TTF = 9;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DEX = 10;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_OAT = 11;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_ART = 12;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_UNKNOWN_MAP = 13;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_GRAPHICS = 14;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_GL = 15;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_OTHER_MEMTRACK = 16;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_NORMAL = 17;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_LARGE = 18;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_LINEARALLOC = 19;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_ACCOUNTING = 20;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_CODE_CACHE = 21;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_ZYGOTE = 22;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_NON_MOVING = 23;

        /**
         *
         *
         * @unknown 
         */
        public static final int OTHER_DALVIK_INDIRECT_REFERENCE_TABLE = 24;

        /**
         *
         *
         * @unknown 
         */
        public static final int NUM_OTHER_STATS = 17;

        /**
         *
         *
         * @unknown 
         */
        public static final int NUM_DVK_STATS = 8;

        /**
         *
         *
         * @unknown 
         */
        public static final int NUM_CATEGORIES = 8;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetPss = 0;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetSwappablePss = 1;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetPrivateDirty = 2;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetSharedDirty = 3;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetPrivateClean = 4;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetSharedClean = 5;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetSwappedOut = 6;

        /**
         *
         *
         * @unknown 
         */
        public static final int offsetSwappedOutPss = 7;

        private int[] otherStats = new int[(android.os.Debug.MemoryInfo.NUM_OTHER_STATS + android.os.Debug.MemoryInfo.NUM_DVK_STATS) * android.os.Debug.MemoryInfo.NUM_CATEGORIES];

        public MemoryInfo() {
        }

        /**
         * Return total PSS memory usage in kB.
         */
        public int getTotalPss() {
            return ((dalvikPss + nativePss) + otherPss) + getTotalSwappedOutPss();
        }

        /**
         *
         *
         * @unknown Return total PSS memory usage in kB.
         */
        public int getTotalUss() {
            return ((((dalvikPrivateClean + dalvikPrivateDirty) + nativePrivateClean) + nativePrivateDirty) + otherPrivateClean) + otherPrivateDirty;
        }

        /**
         * Return total PSS memory usage in kB mapping a file of one of the following extension:
         * .so, .jar, .apk, .ttf, .dex, .odex, .oat, .art .
         */
        public int getTotalSwappablePss() {
            return (dalvikSwappablePss + nativeSwappablePss) + otherSwappablePss;
        }

        /**
         * Return total private dirty memory usage in kB.
         */
        public int getTotalPrivateDirty() {
            return (dalvikPrivateDirty + nativePrivateDirty) + otherPrivateDirty;
        }

        /**
         * Return total shared dirty memory usage in kB.
         */
        public int getTotalSharedDirty() {
            return (dalvikSharedDirty + nativeSharedDirty) + otherSharedDirty;
        }

        /**
         * Return total shared clean memory usage in kB.
         */
        public int getTotalPrivateClean() {
            return (dalvikPrivateClean + nativePrivateClean) + otherPrivateClean;
        }

        /**
         * Return total shared clean memory usage in kB.
         */
        public int getTotalSharedClean() {
            return (dalvikSharedClean + nativeSharedClean) + otherSharedClean;
        }

        /**
         * Return total swapped out memory in kB.
         *
         * @unknown 
         */
        public int getTotalSwappedOut() {
            return (dalvikSwappedOut + nativeSwappedOut) + otherSwappedOut;
        }

        /**
         * Return total swapped out memory in kB, proportional.
         *
         * @unknown 
         */
        public int getTotalSwappedOutPss() {
            return (dalvikSwappedOutPss + nativeSwappedOutPss) + otherSwappedOutPss;
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherPss(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetPss];
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherSwappablePss(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetSwappablePss];
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherPrivateDirty(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetPrivateDirty];
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherSharedDirty(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetSharedDirty];
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherPrivateClean(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetPrivateClean];
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherPrivate(int which) {
            return getOtherPrivateClean(which) + getOtherPrivateDirty(which);
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherSharedClean(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetSharedClean];
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherSwappedOut(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetSwappedOut];
        }

        /**
         *
         *
         * @unknown 
         */
        public int getOtherSwappedOutPss(int which) {
            return otherStats[(which * android.os.Debug.MemoryInfo.NUM_CATEGORIES) + android.os.Debug.MemoryInfo.offsetSwappedOutPss];
        }

        /**
         *
         *
         * @unknown 
         */
        public static java.lang.String getOtherLabel(int which) {
            switch (which) {
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_OTHER :
                    return "Dalvik Other";
                case android.os.Debug.MemoryInfo.OTHER_STACK :
                    return "Stack";
                case android.os.Debug.MemoryInfo.OTHER_CURSOR :
                    return "Cursor";
                case android.os.Debug.MemoryInfo.OTHER_ASHMEM :
                    return "Ashmem";
                case android.os.Debug.MemoryInfo.OTHER_GL_DEV :
                    return "Gfx dev";
                case android.os.Debug.MemoryInfo.OTHER_UNKNOWN_DEV :
                    return "Other dev";
                case android.os.Debug.MemoryInfo.OTHER_SO :
                    return ".so mmap";
                case android.os.Debug.MemoryInfo.OTHER_JAR :
                    return ".jar mmap";
                case android.os.Debug.MemoryInfo.OTHER_APK :
                    return ".apk mmap";
                case android.os.Debug.MemoryInfo.OTHER_TTF :
                    return ".ttf mmap";
                case android.os.Debug.MemoryInfo.OTHER_DEX :
                    return ".dex mmap";
                case android.os.Debug.MemoryInfo.OTHER_OAT :
                    return ".oat mmap";
                case android.os.Debug.MemoryInfo.OTHER_ART :
                    return ".art mmap";
                case android.os.Debug.MemoryInfo.OTHER_UNKNOWN_MAP :
                    return "Other mmap";
                case android.os.Debug.MemoryInfo.OTHER_GRAPHICS :
                    return "EGL mtrack";
                case android.os.Debug.MemoryInfo.OTHER_GL :
                    return "GL mtrack";
                case android.os.Debug.MemoryInfo.OTHER_OTHER_MEMTRACK :
                    return "Other mtrack";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_NORMAL :
                    return ".Heap";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_LARGE :
                    return ".LOS";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_LINEARALLOC :
                    return ".LinearAlloc";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_ACCOUNTING :
                    return ".GC";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_CODE_CACHE :
                    return ".JITCache";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_ZYGOTE :
                    return ".Zygote";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_NON_MOVING :
                    return ".NonMoving";
                case android.os.Debug.MemoryInfo.OTHER_DALVIK_INDIRECT_REFERENCE_TABLE :
                    return ".IndirectRef";
                default :
                    return "????";
            }
        }

        /**
         * Returns the value of a particular memory statistic or {@code null} if no
         * such memory statistic exists.
         *
         * <p>The following table lists the memory statistics that are supported.
         * Note that memory statistics may be added or removed in a future API level.</p>
         *
         * <table>
         *     <thead>
         *         <tr>
         *             <th>Memory statistic name</th>
         *             <th>Meaning</th>
         *             <th>Example</th>
         *             <th>Supported (API Levels)</th>
         *         </tr>
         *     </thead>
         *     <tbody>
         *         <tr>
         *             <td>summary.java-heap</td>
         *             <td>The private Java Heap usage in kB. This corresponds to the Java Heap field
         *                 in the App Summary section output by dumpsys meminfo.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.native-heap</td>
         *             <td>The private Native Heap usage in kB. This corresponds to the Native Heap
         *                 field in the App Summary section output by dumpsys meminfo.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.code</td>
         *             <td>The memory usage for static code and resources in kB. This corresponds to
         *                 the Code field in the App Summary section output by dumpsys meminfo.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.stack</td>
         *             <td>The stack usage in kB. This corresponds to the Stack field in the
         *                 App Summary section output by dumpsys meminfo.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.graphics</td>
         *             <td>The graphics usage in kB. This corresponds to the Graphics field in the
         *                 App Summary section output by dumpsys meminfo.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.private-other</td>
         *             <td>Other private memory usage in kB. This corresponds to the Private Other
         *                 field output in the App Summary section by dumpsys meminfo.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.system</td>
         *             <td>Shared and system memory usage in kB. This corresponds to the System
         *                 field output in the App Summary section by dumpsys meminfo.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.total-pss</td>
         *             <td>Total PPS memory usage in kB.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *         <tr>
         *             <td>summary.total-swap</td>
         *             <td>Total swap usage in kB.</td>
         *             <td>{@code 1442}</td>
         *             <td>23</td>
         *         </tr>
         *     </tbody>
         * </table>
         */
        public java.lang.String getMemoryStat(java.lang.String statName) {
            switch (statName) {
                case "summary.java-heap" :
                    return java.lang.Integer.toString(getSummaryJavaHeap());
                case "summary.native-heap" :
                    return java.lang.Integer.toString(getSummaryNativeHeap());
                case "summary.code" :
                    return java.lang.Integer.toString(getSummaryCode());
                case "summary.stack" :
                    return java.lang.Integer.toString(getSummaryStack());
                case "summary.graphics" :
                    return java.lang.Integer.toString(getSummaryGraphics());
                case "summary.private-other" :
                    return java.lang.Integer.toString(getSummaryPrivateOther());
                case "summary.system" :
                    return java.lang.Integer.toString(getSummarySystem());
                case "summary.total-pss" :
                    return java.lang.Integer.toString(getSummaryTotalPss());
                case "summary.total-swap" :
                    return java.lang.Integer.toString(getSummaryTotalSwap());
                default :
                    return null;
            }
        }

        /**
         * Returns a map of the names/values of the memory statistics
         * that {@link #getMemoryStat(String)} supports.
         *
         * @return a map of the names/values of the supported memory statistics.
         */
        public java.util.Map<java.lang.String, java.lang.String> getMemoryStats() {
            java.util.Map<java.lang.String, java.lang.String> stats = new java.util.HashMap<java.lang.String, java.lang.String>();
            stats.put("summary.java-heap", java.lang.Integer.toString(getSummaryJavaHeap()));
            stats.put("summary.native-heap", java.lang.Integer.toString(getSummaryNativeHeap()));
            stats.put("summary.code", java.lang.Integer.toString(getSummaryCode()));
            stats.put("summary.stack", java.lang.Integer.toString(getSummaryStack()));
            stats.put("summary.graphics", java.lang.Integer.toString(getSummaryGraphics()));
            stats.put("summary.private-other", java.lang.Integer.toString(getSummaryPrivateOther()));
            stats.put("summary.system", java.lang.Integer.toString(getSummarySystem()));
            stats.put("summary.total-pss", java.lang.Integer.toString(getSummaryTotalPss()));
            stats.put("summary.total-swap", java.lang.Integer.toString(getSummaryTotalSwap()));
            return stats;
        }

        /**
         * Pss of Java Heap bytes in KB due to the application.
         * Notes:
         *  * OTHER_ART is the boot image. Anything private here is blamed on
         *    the application, not the system.
         *  * dalvikPrivateDirty includes private zygote, which means the
         *    application dirtied something allocated by the zygote. We blame
         *    the application for that memory, not the system.
         *  * Does not include OTHER_DALVIK_OTHER, which is considered VM
         *    Overhead and lumped into Private Other.
         *  * We don't include dalvikPrivateClean, because there should be no
         *    such thing as private clean for the Java Heap.
         *
         * @unknown 
         */
        public int getSummaryJavaHeap() {
            return dalvikPrivateDirty + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_ART);
        }

        /**
         * Pss of Native Heap bytes in KB due to the application.
         * Notes:
         *  * Includes private dirty malloc space.
         *  * We don't include nativePrivateClean, because there should be no
         *    such thing as private clean for the Native Heap.
         *
         * @unknown 
         */
        public int getSummaryNativeHeap() {
            return nativePrivateDirty;
        }

        /**
         * Pss of code and other static resource bytes in KB due to
         * the application.
         *
         * @unknown 
         */
        public int getSummaryCode() {
            return ((((getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_SO) + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_JAR)) + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_APK)) + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_TTF)) + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_DEX)) + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_OAT);
        }

        /**
         * Pss in KB of the stack due to the application.
         * Notes:
         *  * Includes private dirty stack, which includes both Java and Native
         *    stack.
         *  * Does not include private clean stack, because there should be no
         *    such thing as private clean for the stack.
         *
         * @unknown 
         */
        public int getSummaryStack() {
            return getOtherPrivateDirty(android.os.Debug.MemoryInfo.OTHER_STACK);
        }

        /**
         * Pss in KB of graphics due to the application.
         * Notes:
         *  * Includes private Gfx, EGL, and GL.
         *  * Warning: These numbers can be misreported by the graphics drivers.
         *  * We don't include shared graphics. It may make sense to, because
         *    shared graphics are likely buffers due to the application
         *    anyway, but it's simpler to implement to just group all shared
         *    memory into the System category.
         *
         * @unknown 
         */
        public int getSummaryGraphics() {
            return (getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_GL_DEV) + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_GRAPHICS)) + getOtherPrivate(android.os.Debug.MemoryInfo.OTHER_GL);
        }

        /**
         * Pss in KB due to the application that haven't otherwise been
         * accounted for.
         *
         * @unknown 
         */
        public int getSummaryPrivateOther() {
            return (((((getTotalPrivateClean() + getTotalPrivateDirty()) - getSummaryJavaHeap()) - getSummaryNativeHeap()) - getSummaryCode()) - getSummaryStack()) - getSummaryGraphics();
        }

        /**
         * Pss in KB due to the system.
         * Notes:
         *  * Includes all shared memory.
         *
         * @unknown 
         */
        public int getSummarySystem() {
            return (getTotalPss() - getTotalPrivateClean()) - getTotalPrivateDirty();
        }

        /**
         * Total Pss in KB.
         *
         * @unknown 
         */
        public int getSummaryTotalPss() {
            return getTotalPss();
        }

        /**
         * Total Swap in KB.
         * Notes:
         *  * Some of this memory belongs in other categories, but we don't
         *    know if the Swap memory is shared or private, so we don't know
         *    what to blame on the application and what on the system.
         *    For now, just lump all the Swap in one place.
         *    For kernels reporting SwapPss {@link #getSummaryTotalSwapPss()}
         *    will report the application proportional Swap.
         *
         * @unknown 
         */
        public int getSummaryTotalSwap() {
            return getTotalSwappedOut();
        }

        /**
         * Total proportional Swap in KB.
         * Notes:
         *  * Always 0 if {@link #hasSwappedOutPss} is false.
         *
         * @unknown 
         */
        public int getSummaryTotalSwapPss() {
            return getTotalSwappedOutPss();
        }

        /**
         * Return true if the kernel is reporting pss swapped out...  that is, if
         * {@link #getSummaryTotalSwapPss()} will return non-0 values.
         *
         * @unknown 
         */
        public boolean hasSwappedOutPss() {
            return hasSwappedOutPss;
        }

        public int describeContents() {
            return 0;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeInt(dalvikPss);
            dest.writeInt(dalvikSwappablePss);
            dest.writeInt(dalvikPrivateDirty);
            dest.writeInt(dalvikSharedDirty);
            dest.writeInt(dalvikPrivateClean);
            dest.writeInt(dalvikSharedClean);
            dest.writeInt(dalvikSwappedOut);
            dest.writeInt(nativePss);
            dest.writeInt(nativeSwappablePss);
            dest.writeInt(nativePrivateDirty);
            dest.writeInt(nativeSharedDirty);
            dest.writeInt(nativePrivateClean);
            dest.writeInt(nativeSharedClean);
            dest.writeInt(nativeSwappedOut);
            dest.writeInt(otherPss);
            dest.writeInt(otherSwappablePss);
            dest.writeInt(otherPrivateDirty);
            dest.writeInt(otherSharedDirty);
            dest.writeInt(otherPrivateClean);
            dest.writeInt(otherSharedClean);
            dest.writeInt(otherSwappedOut);
            dest.writeInt(hasSwappedOutPss ? 1 : 0);
            dest.writeInt(otherSwappedOutPss);
            dest.writeIntArray(otherStats);
        }

        public void readFromParcel(android.os.Parcel source) {
            dalvikPss = source.readInt();
            dalvikSwappablePss = source.readInt();
            dalvikPrivateDirty = source.readInt();
            dalvikSharedDirty = source.readInt();
            dalvikPrivateClean = source.readInt();
            dalvikSharedClean = source.readInt();
            dalvikSwappedOut = source.readInt();
            nativePss = source.readInt();
            nativeSwappablePss = source.readInt();
            nativePrivateDirty = source.readInt();
            nativeSharedDirty = source.readInt();
            nativePrivateClean = source.readInt();
            nativeSharedClean = source.readInt();
            nativeSwappedOut = source.readInt();
            otherPss = source.readInt();
            otherSwappablePss = source.readInt();
            otherPrivateDirty = source.readInt();
            otherSharedDirty = source.readInt();
            otherPrivateClean = source.readInt();
            otherSharedClean = source.readInt();
            otherSwappedOut = source.readInt();
            hasSwappedOutPss = source.readInt() != 0;
            otherSwappedOutPss = source.readInt();
            otherStats = source.createIntArray();
        }

        public static final android.os.Parcelable.Creator<android.os.Debug.MemoryInfo> CREATOR = new android.os.Parcelable.Creator<android.os.Debug.MemoryInfo>() {
            public android.os.Debug.MemoryInfo createFromParcel(android.os.Parcel source) {
                return new android.os.Debug.MemoryInfo(source);
            }

            public android.os.Debug.MemoryInfo[] newArray(int size) {
                return new android.os.Debug.MemoryInfo[size];
            }
        };

        private MemoryInfo(android.os.Parcel source) {
            readFromParcel(source);
        }
    }

    /**
     * Wait until a debugger attaches.  As soon as the debugger attaches,
     * this returns, so you will need to place a breakpoint after the
     * waitForDebugger() call if you want to start tracing immediately.
     */
    public static void waitForDebugger() {
        if (!dalvik.system.VMDebug.isDebuggingEnabled()) {
            // System.out.println("debugging not enabled, not waiting");
            return;
        }
        if (android.os.Debug.isDebuggerConnected())
            return;

        // if DDMS is listening, inform them of our plight
        java.lang.System.out.println("Sending WAIT chunk");
        byte[] data = new byte[]{ 0 };// 0 == "waiting for debugger"

        org.apache.harmony.dalvik.ddmc.Chunk waitChunk = new org.apache.harmony.dalvik.ddmc.Chunk(org.apache.harmony.dalvik.ddmc.ChunkHandler.type("WAIT"), data, 0, 1);
        org.apache.harmony.dalvik.ddmc.DdmServer.sendChunk(waitChunk);
        android.os.Debug.mWaiting = true;
        while (!android.os.Debug.isDebuggerConnected()) {
            try {
                java.lang.Thread.sleep(android.os.Debug.SPIN_DELAY);
            } catch (java.lang.InterruptedException ie) {
            }
        } 
        android.os.Debug.mWaiting = false;
        java.lang.System.out.println("Debugger has connected");
        /* There is no "ready to go" signal from the debugger, and we're
        not allowed to suspend ourselves -- the debugger expects us to
        be running happily, and gets confused if we aren't.  We need to
        allow the debugger a chance to set breakpoints before we start
        running again.

        Sit and spin until the debugger has been idle for a short while.
         */
        while (true) {
            long delta = dalvik.system.VMDebug.lastDebuggerActivity();
            if (delta < 0) {
                java.lang.System.out.println("debugger detached?");
                break;
            }
            if (delta < android.os.Debug.MIN_DEBUGGER_IDLE) {
                java.lang.System.out.println("waiting for debugger to settle...");
                try {
                    java.lang.Thread.sleep(android.os.Debug.SPIN_DELAY);
                } catch (java.lang.InterruptedException ie) {
                }
            } else {
                java.lang.System.out.println(("debugger has settled (" + delta) + ")");
                break;
            }
        } 
    }

    /**
     * Returns "true" if one or more threads is waiting for a debugger
     * to attach.
     */
    public static boolean waitingForDebugger() {
        return android.os.Debug.mWaiting;
    }

    /**
     * Determine if a debugger is currently attached.
     */
    public static boolean isDebuggerConnected() {
        return dalvik.system.VMDebug.isDebuggerConnected();
    }

    /**
     * Returns an array of strings that identify VM features.  This is
     * used by DDMS to determine what sorts of operations the VM can
     * perform.
     *
     * @unknown 
     */
    public static java.lang.String[] getVmFeatureList() {
        return dalvik.system.VMDebug.getVmFeatureList();
    }

    /**
     * Change the JDWP port.
     *
     * @deprecated no longer needed or useful
     */
    @java.lang.Deprecated
    public static void changeDebugPort(int port) {
    }

    /**
     * This is the pathname to the sysfs file that enables and disables
     * tracing on the qemu emulator.
     */
    private static final java.lang.String SYSFS_QEMU_TRACE_STATE = "/sys/qemu_trace/state";

    /**
     * Enable qemu tracing. For this to work requires running everything inside
     * the qemu emulator; otherwise, this method will have no effect. The trace
     * file is specified on the command line when the emulator is started. For
     * example, the following command line <br />
     * <code>emulator -trace foo</code><br />
     * will start running the emulator and create a trace file named "foo". This
     * method simply enables writing the trace records to the trace file.
     *
     * <p>
     * The main differences between this and {@link #startMethodTracing()} are
     * that tracing in the qemu emulator traces every cpu instruction of every
     * process, including kernel code, so we have more complete information,
     * including all context switches. We can also get more detailed information
     * such as cache misses. The sequence of calls is determined by
     * post-processing the instruction trace. The qemu tracing is also done
     * without modifying the application or perturbing the timing of calls
     * because no instrumentation is added to the application being traced.
     * </p>
     *
     * <p>
     * One limitation of using this method compared to using
     * {@link #startMethodTracing()} on the real device is that the emulator
     * does not model all of the real hardware effects such as memory and
     * bus contention.  The emulator also has a simple cache model and cannot
     * capture all the complexities of a real cache.
     * </p>
     */
    public static void startNativeTracing() {
        // Open the sysfs file for writing and write "1" to it.
        java.io.PrintWriter outStream = null;
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(android.os.Debug.SYSFS_QEMU_TRACE_STATE);
            outStream = new com.android.internal.util.FastPrintWriter(fos);
            outStream.println("1");
        } catch (java.lang.Exception e) {
        } finally {
            if (outStream != null)
                outStream.close();

        }
        dalvik.system.VMDebug.startEmulatorTracing();
    }

    /**
     * Stop qemu tracing.  See {@link #startNativeTracing()} to start tracing.
     *
     * <p>Tracing can be started and stopped as many times as desired.  When
     * the qemu emulator itself is stopped then the buffered trace records
     * are flushed and written to the trace file.  In fact, it is not necessary
     * to call this method at all; simply killing qemu is sufficient.  But
     * starting and stopping a trace is useful for examining a specific
     * region of code.</p>
     */
    public static void stopNativeTracing() {
        dalvik.system.VMDebug.stopEmulatorTracing();
        // Open the sysfs file for writing and write "0" to it.
        java.io.PrintWriter outStream = null;
        try {
            java.io.FileOutputStream fos = new java.io.FileOutputStream(android.os.Debug.SYSFS_QEMU_TRACE_STATE);
            outStream = new com.android.internal.util.FastPrintWriter(fos);
            outStream.println("0");
        } catch (java.lang.Exception e) {
            // We could print an error message here but we probably want
            // to quietly ignore errors if we are not running in the emulator.
        } finally {
            if (outStream != null)
                outStream.close();

        }
    }

    /**
     * Enable "emulator traces", in which information about the current
     * method is made available to the "emulator -trace" feature.  There
     * is no corresponding "disable" call -- this is intended for use by
     * the framework when tracing should be turned on and left that way, so
     * that traces captured with F9/F10 will include the necessary data.
     *
     * This puts the VM into "profile" mode, which has performance
     * consequences.
     *
     * To temporarily enable tracing, use {@link #startNativeTracing()}.
     */
    public static void enableEmulatorTraceOutput() {
        dalvik.system.VMDebug.startEmulatorTracing();
    }

    /**
     * Start method tracing with default log name and buffer size.
     * <p>
     * By default, the trace file is called "dmtrace.trace" and it's placed
     * under your package-specific directory on primary shared/external storage,
     * as returned by {@link Context#getExternalFilesDir(String)}.
     * <p>
     * See <a href="{@docRoot }guide/developing/tools/traceview.html">Traceview:
     * A Graphical Log Viewer</a> for information about reading trace files.
     * <p class="note">
     * When method tracing is enabled, the VM will run more slowly than usual,
     * so the timings from the trace files should only be considered in relative
     * terms (e.g. was run #1 faster than run #2). The times for native methods
     * will not change, so don't try to use this to compare the performance of
     * interpreted and native implementations of the same method. As an
     * alternative, consider using sampling-based method tracing via
     * {@link #startMethodTracingSampling(String, int, int)} or "native" tracing
     * in the emulator via {@link #startNativeTracing()}.
     * </p>
     */
    public static void startMethodTracing() {
        dalvik.system.VMDebug.startMethodTracing(android.os.Debug.fixTracePath(null), 0, 0, false, 0);
    }

    /**
     * Start method tracing, specifying the trace log file path.
     * <p>
     * When a relative file path is given, the trace file will be placed under
     * your package-specific directory on primary shared/external storage, as
     * returned by {@link Context#getExternalFilesDir(String)}.
     * <p>
     * See <a href="{@docRoot }guide/developing/tools/traceview.html">Traceview:
     * A Graphical Log Viewer</a> for information about reading trace files.
     * <p class="note">
     * When method tracing is enabled, the VM will run more slowly than usual,
     * so the timings from the trace files should only be considered in relative
     * terms (e.g. was run #1 faster than run #2). The times for native methods
     * will not change, so don't try to use this to compare the performance of
     * interpreted and native implementations of the same method. As an
     * alternative, consider using sampling-based method tracing via
     * {@link #startMethodTracingSampling(String, int, int)} or "native" tracing
     * in the emulator via {@link #startNativeTracing()}.
     * </p>
     *
     * @param tracePath
     * 		Path to the trace log file to create. If {@code null},
     * 		this will default to "dmtrace.trace". If the file already
     * 		exists, it will be truncated. If the path given does not end
     * 		in ".trace", it will be appended for you.
     */
    public static void startMethodTracing(java.lang.String tracePath) {
        android.os.Debug.startMethodTracing(tracePath, 0, 0);
    }

    /**
     * Start method tracing, specifying the trace log file name and the buffer
     * size.
     * <p>
     * When a relative file path is given, the trace file will be placed under
     * your package-specific directory on primary shared/external storage, as
     * returned by {@link Context#getExternalFilesDir(String)}.
     * <p>
     * See <a href="{@docRoot }guide/developing/tools/traceview.html">Traceview:
     * A Graphical Log Viewer</a> for information about reading trace files.
     * <p class="note">
     * When method tracing is enabled, the VM will run more slowly than usual,
     * so the timings from the trace files should only be considered in relative
     * terms (e.g. was run #1 faster than run #2). The times for native methods
     * will not change, so don't try to use this to compare the performance of
     * interpreted and native implementations of the same method. As an
     * alternative, consider using sampling-based method tracing via
     * {@link #startMethodTracingSampling(String, int, int)} or "native" tracing
     * in the emulator via {@link #startNativeTracing()}.
     * </p>
     *
     * @param tracePath
     * 		Path to the trace log file to create. If {@code null},
     * 		this will default to "dmtrace.trace". If the file already
     * 		exists, it will be truncated. If the path given does not end
     * 		in ".trace", it will be appended for you.
     * @param bufferSize
     * 		The maximum amount of trace data we gather. If not
     * 		given, it defaults to 8MB.
     */
    public static void startMethodTracing(java.lang.String tracePath, int bufferSize) {
        android.os.Debug.startMethodTracing(tracePath, bufferSize, 0);
    }

    /**
     * Start method tracing, specifying the trace log file name, the buffer
     * size, and flags.
     * <p>
     * When a relative file path is given, the trace file will be placed under
     * your package-specific directory on primary shared/external storage, as
     * returned by {@link Context#getExternalFilesDir(String)}.
     * <p>
     * See <a href="{@docRoot }guide/developing/tools/traceview.html">Traceview:
     * A Graphical Log Viewer</a> for information about reading trace files.
     * <p class="note">
     * When method tracing is enabled, the VM will run more slowly than usual,
     * so the timings from the trace files should only be considered in relative
     * terms (e.g. was run #1 faster than run #2). The times for native methods
     * will not change, so don't try to use this to compare the performance of
     * interpreted and native implementations of the same method. As an
     * alternative, consider using sampling-based method tracing via
     * {@link #startMethodTracingSampling(String, int, int)} or "native" tracing
     * in the emulator via {@link #startNativeTracing()}.
     * </p>
     *
     * @param tracePath
     * 		Path to the trace log file to create. If {@code null},
     * 		this will default to "dmtrace.trace". If the file already
     * 		exists, it will be truncated. If the path given does not end
     * 		in ".trace", it will be appended for you.
     * @param bufferSize
     * 		The maximum amount of trace data we gather. If not
     * 		given, it defaults to 8MB.
     * @param flags
     * 		Flags to control method tracing. The only one that is
     * 		currently defined is {@link #TRACE_COUNT_ALLOCS}.
     */
    public static void startMethodTracing(java.lang.String tracePath, int bufferSize, int flags) {
        dalvik.system.VMDebug.startMethodTracing(android.os.Debug.fixTracePath(tracePath), bufferSize, flags, false, 0);
    }

    /**
     * Start sampling-based method tracing, specifying the trace log file name,
     * the buffer size, and the sampling interval.
     * <p>
     * When a relative file path is given, the trace file will be placed under
     * your package-specific directory on primary shared/external storage, as
     * returned by {@link Context#getExternalFilesDir(String)}.
     * <p>
     * See <a href="{@docRoot }guide/developing/tools/traceview.html">Traceview:
     * A Graphical Log Viewer</a> for information about reading trace files.
     *
     * @param tracePath
     * 		Path to the trace log file to create. If {@code null},
     * 		this will default to "dmtrace.trace". If the file already
     * 		exists, it will be truncated. If the path given does not end
     * 		in ".trace", it will be appended for you.
     * @param bufferSize
     * 		The maximum amount of trace data we gather. If not
     * 		given, it defaults to 8MB.
     * @param intervalUs
     * 		The amount of time between each sample in microseconds.
     */
    public static void startMethodTracingSampling(java.lang.String tracePath, int bufferSize, int intervalUs) {
        dalvik.system.VMDebug.startMethodTracing(android.os.Debug.fixTracePath(tracePath), bufferSize, 0, true, intervalUs);
    }

    /**
     * Formats name of trace log file for method tracing.
     */
    private static java.lang.String fixTracePath(java.lang.String tracePath) {
        if ((tracePath == null) || (tracePath.charAt(0) != '/')) {
            final android.content.Context context = android.app.AppGlobals.getInitialApplication();
            final java.io.File dir;
            if (context != null) {
                dir = context.getExternalFilesDir(null);
            } else {
                dir = android.os.Environment.getExternalStorageDirectory();
            }
            if (tracePath == null) {
                tracePath = new java.io.File(dir, android.os.Debug.DEFAULT_TRACE_BODY).getAbsolutePath();
            } else {
                tracePath = new java.io.File(dir, tracePath).getAbsolutePath();
            }
        }
        if (!tracePath.endsWith(android.os.Debug.DEFAULT_TRACE_EXTENSION)) {
            tracePath += android.os.Debug.DEFAULT_TRACE_EXTENSION;
        }
        return tracePath;
    }

    /**
     * Like startMethodTracing(String, int, int), but taking an already-opened
     * FileDescriptor in which the trace is written.  The file name is also
     * supplied simply for logging.  Makes a dup of the file descriptor.
     *
     * Not exposed in the SDK unless we are really comfortable with supporting
     * this and find it would be useful.
     *
     * @unknown 
     */
    public static void startMethodTracing(java.lang.String traceName, java.io.FileDescriptor fd, int bufferSize, int flags) {
        dalvik.system.VMDebug.startMethodTracing(traceName, fd, bufferSize, flags, false, 0);
    }

    /**
     * Starts method tracing without a backing file.  When stopMethodTracing
     * is called, the result is sent directly to DDMS.  (If DDMS is not
     * attached when tracing ends, the profiling data will be discarded.)
     *
     * @unknown 
     */
    public static void startMethodTracingDdms(int bufferSize, int flags, boolean samplingEnabled, int intervalUs) {
        dalvik.system.VMDebug.startMethodTracingDdms(bufferSize, flags, samplingEnabled, intervalUs);
    }

    /**
     * Determine whether method tracing is currently active and what type is
     * active.
     *
     * @unknown 
     */
    public static int getMethodTracingMode() {
        return dalvik.system.VMDebug.getMethodTracingMode();
    }

    /**
     * Stop method tracing.
     */
    public static void stopMethodTracing() {
        dalvik.system.VMDebug.stopMethodTracing();
    }

    /**
     * Get an indication of thread CPU usage.  The value returned
     * indicates the amount of time that the current thread has spent
     * executing code or waiting for certain types of I/O.
     *
     * The time is expressed in nanoseconds, and is only meaningful
     * when compared to the result from an earlier call.  Note that
     * nanosecond resolution does not imply nanosecond accuracy.
     *
     * On system which don't support this operation, the call returns -1.
     */
    public static long threadCpuTimeNanos() {
        return dalvik.system.VMDebug.threadCpuTimeNanos();
    }

    /**
     * Start counting the number and aggregate size of memory allocations.
     *
     * <p>The {@link #startAllocCounting() start} method resets the counts and enables counting.
     * The {@link #stopAllocCounting() stop} method disables the counting so that the analysis
     * code doesn't cause additional allocations.  The various <code>get</code> methods return
     * the specified value. And the various <code>reset</code> methods reset the specified
     * count.</p>
     *
     * <p>Counts are kept for the system as a whole (global) and for each thread.
     * The per-thread counts for threads other than the current thread
     * are not cleared by the "reset" or "start" calls.</p>
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void startAllocCounting() {
        dalvik.system.VMDebug.startAllocCounting();
    }

    /**
     * Stop counting the number and aggregate size of memory allocations.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void stopAllocCounting() {
        dalvik.system.VMDebug.stopAllocCounting();
    }

    /**
     * Returns the global count of objects allocated by the runtime between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getGlobalAllocCount() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_GLOBAL_ALLOCATED_OBJECTS);
    }

    /**
     * Clears the global count of objects allocated.
     *
     * @see #getGlobalAllocCount()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetGlobalAllocCount() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_GLOBAL_ALLOCATED_OBJECTS);
    }

    /**
     * Returns the global size, in bytes, of objects allocated by the runtime between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getGlobalAllocSize() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_GLOBAL_ALLOCATED_BYTES);
    }

    /**
     * Clears the global size of objects allocated.
     *
     * @see #getGlobalAllocSize()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetGlobalAllocSize() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_GLOBAL_ALLOCATED_BYTES);
    }

    /**
     * Returns the global count of objects freed by the runtime between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getGlobalFreedCount() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_GLOBAL_FREED_OBJECTS);
    }

    /**
     * Clears the global count of objects freed.
     *
     * @see #getGlobalFreedCount()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetGlobalFreedCount() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_GLOBAL_FREED_OBJECTS);
    }

    /**
     * Returns the global size, in bytes, of objects freed by the runtime between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getGlobalFreedSize() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_GLOBAL_FREED_BYTES);
    }

    /**
     * Clears the global size of objects freed.
     *
     * @see #getGlobalFreedSize()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetGlobalFreedSize() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_GLOBAL_FREED_BYTES);
    }

    /**
     * Returns the number of non-concurrent GC invocations between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getGlobalGcInvocationCount() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_GLOBAL_GC_INVOCATIONS);
    }

    /**
     * Clears the count of non-concurrent GC invocations.
     *
     * @see #getGlobalGcInvocationCount()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetGlobalGcInvocationCount() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_GLOBAL_GC_INVOCATIONS);
    }

    /**
     * Returns the number of classes successfully initialized (ie those that executed without
     * throwing an exception) between a {@link #startAllocCounting() start} and
     * {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getGlobalClassInitCount() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_GLOBAL_CLASS_INIT_COUNT);
    }

    /**
     * Clears the count of classes initialized.
     *
     * @see #getGlobalClassInitCount()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetGlobalClassInitCount() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_GLOBAL_CLASS_INIT_COUNT);
    }

    /**
     * Returns the time spent successfully initializing classes between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getGlobalClassInitTime() {
        /* cumulative elapsed time for class initialization, in usec */
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_GLOBAL_CLASS_INIT_TIME);
    }

    /**
     * Clears the count of time spent initializing classes.
     *
     * @see #getGlobalClassInitTime()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetGlobalClassInitTime() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_GLOBAL_CLASS_INIT_TIME);
    }

    /**
     * This method exists for compatibility and always returns 0.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int getGlobalExternalAllocCount() {
        return 0;
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static void resetGlobalExternalAllocSize() {
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static void resetGlobalExternalAllocCount() {
    }

    /**
     * This method exists for compatibility and always returns 0.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int getGlobalExternalAllocSize() {
        return 0;
    }

    /**
     * This method exists for compatibility and always returns 0.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int getGlobalExternalFreedCount() {
        return 0;
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static void resetGlobalExternalFreedCount() {
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int getGlobalExternalFreedSize() {
        return 0;
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static void resetGlobalExternalFreedSize() {
    }

    /**
     * Returns the thread-local count of objects allocated by the runtime between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getThreadAllocCount() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_THREAD_ALLOCATED_OBJECTS);
    }

    /**
     * Clears the thread-local count of objects allocated.
     *
     * @see #getThreadAllocCount()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetThreadAllocCount() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_THREAD_ALLOCATED_OBJECTS);
    }

    /**
     * Returns the thread-local size of objects allocated by the runtime between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @return The allocated size in bytes.
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getThreadAllocSize() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_THREAD_ALLOCATED_BYTES);
    }

    /**
     * Clears the thread-local count of objects allocated.
     *
     * @see #getThreadAllocSize()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetThreadAllocSize() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_THREAD_ALLOCATED_BYTES);
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int getThreadExternalAllocCount() {
        return 0;
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static void resetThreadExternalAllocCount() {
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int getThreadExternalAllocSize() {
        return 0;
    }

    /**
     * This method exists for compatibility and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static void resetThreadExternalAllocSize() {
    }

    /**
     * Returns the number of thread-local non-concurrent GC invocations between a
     * {@link #startAllocCounting() start} and {@link #stopAllocCounting() stop}.
     *
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static int getThreadGcInvocationCount() {
        return dalvik.system.VMDebug.getAllocCount(VMDebug.KIND_THREAD_GC_INVOCATIONS);
    }

    /**
     * Clears the thread-local count of non-concurrent GC invocations.
     *
     * @see #getThreadGcInvocationCount()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetThreadGcInvocationCount() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_THREAD_GC_INVOCATIONS);
    }

    /**
     * Clears all the global and thread-local memory allocation counters.
     *
     * @see #startAllocCounting()
     * @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public static void resetAllCounts() {
        dalvik.system.VMDebug.resetAllocCount(VMDebug.KIND_ALL_COUNTS);
    }

    /**
     * Returns the value of a particular runtime statistic or {@code null} if no
     * such runtime statistic exists.
     *
     * <p>The following table lists the runtime statistics that the runtime supports.
     * Note runtime statistics may be added or removed in a future API level.</p>
     *
     * <table>
     *     <thead>
     *         <tr>
     *             <th>Runtime statistic name</th>
     *             <th>Meaning</th>
     *             <th>Example</th>
     *             <th>Supported (API Levels)</th>
     *         </tr>
     *     </thead>
     *     <tbody>
     *         <tr>
     *             <td>art.gc.gc-count</td>
     *             <td>The number of garbage collection runs.</td>
     *             <td>{@code 164}</td>
     *             <td>23</td>
     *         </tr>
     *         <tr>
     *             <td>art.gc.gc-time</td>
     *             <td>The total duration of garbage collection runs in ms.</td>
     *             <td>{@code 62364}</td>
     *             <td>23</td>
     *         </tr>
     *         <tr>
     *             <td>art.gc.bytes-allocated</td>
     *             <td>The total number of bytes that the application allocated.</td>
     *             <td>{@code 1463948408}</td>
     *             <td>23</td>
     *         </tr>
     *         <tr>
     *             <td>art.gc.bytes-freed</td>
     *             <td>The total number of bytes that garbage collection reclaimed.</td>
     *             <td>{@code 1313493084}</td>
     *             <td>23</td>
     *         </tr>
     *         <tr>
     *             <td>art.gc.blocking-gc-count</td>
     *             <td>The number of blocking garbage collection runs.</td>
     *             <td>{@code 2}</td>
     *             <td>23</td>
     *         </tr>
     *         <tr>
     *             <td>art.gc.blocking-gc-time</td>
     *             <td>The total duration of blocking garbage collection runs in ms.</td>
     *             <td>{@code 804}</td>
     *             <td>23</td>
     *         </tr>
     *         <tr>
     *             <td>art.gc.gc-count-rate-histogram</td>
     *             <td>Every 10 seconds, the gc-count-rate is computed as the number of garbage
     *                 collection runs that have occurred over the last 10
     *                 seconds. art.gc.gc-count-rate-histogram is a histogram of the gc-count-rate
     *                 samples taken since the process began. The histogram can be used to identify
     *                 instances of high rates of garbage collection runs. For example, a histogram
     *                 of "0:34503,1:45350,2:11281,3:8088,4:43,5:8" shows that most of the time
     *                 there are between 0 and 2 garbage collection runs every 10 seconds, but there
     *                 were 8 distinct 10-second intervals in which 5 garbage collection runs
     *                 occurred.</td>
     *             <td>{@code 0:34503,1:45350,2:11281,3:8088,4:43,5:8}</td>
     *             <td>23</td>
     *         </tr>
     *         <tr>
     *             <td>art.gc.blocking-gc-count-rate-histogram</td>
     *             <td>Every 10 seconds, the blocking-gc-count-rate is computed as the number of
     *                 blocking garbage collection runs that have occurred over the last 10
     *                 seconds. art.gc.blocking-gc-count-rate-histogram is a histogram of the
     *                 blocking-gc-count-rate samples taken since the process began. The histogram
     *                 can be used to identify instances of high rates of blocking garbage
     *                 collection runs. For example, a histogram of "0:99269,1:1,2:1" shows that
     *                 most of the time there are zero blocking garbage collection runs every 10
     *                 seconds, but there was one 10-second interval in which one blocking garbage
     *                 collection run occurred, and there was one interval in which two blocking
     *                 garbage collection runs occurred.</td>
     *             <td>{@code 0:99269,1:1,2:1}</td>
     *             <td>23</td>
     *         </tr>
     *     </tbody>
     * </table>
     *
     * @param statName
     * 		the name of the runtime statistic to look up.
     * @return the value of the specified runtime statistic or {@code null} if the
    runtime statistic doesn't exist.
     */
    public static java.lang.String getRuntimeStat(java.lang.String statName) {
        return dalvik.system.VMDebug.getRuntimeStat(statName);
    }

    /**
     * Returns a map of the names/values of the runtime statistics
     * that {@link #getRuntimeStat(String)} supports.
     *
     * @return a map of the names/values of the supported runtime statistics.
     */
    public static java.util.Map<java.lang.String, java.lang.String> getRuntimeStats() {
        return dalvik.system.VMDebug.getRuntimeStats();
    }

    /**
     * Returns the size of the native heap.
     *
     * @return The size of the native heap in bytes.
     */
    public static native long getNativeHeapSize();

    /**
     * Returns the amount of allocated memory in the native heap.
     *
     * @return The allocated size in bytes.
     */
    public static native long getNativeHeapAllocatedSize();

    /**
     * Returns the amount of free memory in the native heap.
     *
     * @return The freed size in bytes.
     */
    public static native long getNativeHeapFreeSize();

    /**
     * Retrieves information about this processes memory usages. This information is broken down by
     * how much is in use by dalvik, the native heap, and everything else.
     *
     * <p><b>Note:</b> this method directly retrieves memory information for the give process
     * from low-level data available to it.  It may not be able to retrieve information about
     * some protected allocations, such as graphics.  If you want to be sure you can see
     * all information about allocations by the process, use instead
     * {@link android.app.ActivityManager#getProcessMemoryInfo(int[])}.</p>
     */
    public static native void getMemoryInfo(android.os.Debug.MemoryInfo memoryInfo);

    /**
     * Note: currently only works when the requested pid has the same UID
     * as the caller.
     *
     * @unknown 
     */
    public static native void getMemoryInfo(int pid, android.os.Debug.MemoryInfo memoryInfo);

    /**
     * Retrieves the PSS memory used by the process as given by the
     * smaps.
     */
    public static native long getPss();

    /**
     * Retrieves the PSS memory used by the process as given by the
     * smaps.  Optionally supply a long array of 2 entries to also
     * receive the Uss and SwapPss of the process, and another array to also
     * retrieve the separate memtrack size.
     *
     * @unknown 
     */
    public static native long getPss(int pid, long[] outUssSwapPss, long[] outMemtrack);

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_TOTAL = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_FREE = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_BUFFERS = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_CACHED = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_SHMEM = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_SLAB = 5;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_SWAP_TOTAL = 6;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_SWAP_FREE = 7;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_ZRAM_TOTAL = 8;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_MAPPED = 9;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_VM_ALLOC_USED = 10;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_PAGE_TABLES = 11;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_KERNEL_STACK = 12;

    /**
     *
     *
     * @unknown 
     */
    public static final int MEMINFO_COUNT = 13;

    /**
     * Retrieves /proc/meminfo.  outSizes is filled with fields
     * as defined by MEMINFO_* offsets.
     *
     * @unknown 
     */
    public static native void getMemInfo(long[] outSizes);

    /**
     * Establish an object allocation limit in the current thread.
     * This feature was never enabled in release builds.  The
     * allocation limits feature was removed in Honeycomb.  This
     * method exists for compatibility and always returns -1 and has
     * no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int setAllocationLimit(int limit) {
        return -1;
    }

    /**
     * Establish a global object allocation limit.  This feature was
     * never enabled in release builds.  The allocation limits feature
     * was removed in Honeycomb.  This method exists for compatibility
     * and always returns -1 and has no effect.
     *
     * @deprecated This method is now obsolete.
     */
    @java.lang.Deprecated
    public static int setGlobalAllocationLimit(int limit) {
        return -1;
    }

    /**
     * Dump a list of all currently loaded class to the log file.
     *
     * @param flags
     * 		See constants above.
     */
    public static void printLoadedClasses(int flags) {
        dalvik.system.VMDebug.printLoadedClasses(flags);
    }

    /**
     * Get the number of loaded classes.
     *
     * @return the number of loaded classes.
     */
    public static int getLoadedClassCount() {
        return dalvik.system.VMDebug.getLoadedClassCount();
    }

    /**
     * Dump "hprof" data to the specified file.  This may cause a GC.
     *
     * @param fileName
     * 		Full pathname of output file (e.g. "/sdcard/dump.hprof").
     * @throws UnsupportedOperationException
     * 		if the VM was built without
     * 		HPROF support.
     * @throws IOException
     * 		if an error occurs while opening or writing files.
     */
    public static void dumpHprofData(java.lang.String fileName) throws java.io.IOException {
        dalvik.system.VMDebug.dumpHprofData(fileName);
    }

    /**
     * Like dumpHprofData(String), but takes an already-opened
     * FileDescriptor to which the trace is written.  The file name is also
     * supplied simply for logging.  Makes a dup of the file descriptor.
     *
     * Primarily for use by the "am" shell command.
     *
     * @unknown 
     */
    public static void dumpHprofData(java.lang.String fileName, java.io.FileDescriptor fd) throws java.io.IOException {
        dalvik.system.VMDebug.dumpHprofData(fileName, fd);
    }

    /**
     * Collect "hprof" and send it to DDMS.  This may cause a GC.
     *
     * @throws UnsupportedOperationException
     * 		if the VM was built without
     * 		HPROF support.
     * @unknown 
     */
    public static void dumpHprofDataDdms() {
        dalvik.system.VMDebug.dumpHprofDataDdms();
    }

    /**
     * Writes native heap data to the specified file descriptor.
     *
     * @unknown 
     */
    public static native void dumpNativeHeap(java.io.FileDescriptor fd);

    /**
     * Returns a count of the extant instances of a class.
     *
     * @unknown 
     */
    public static long countInstancesOfClass(java.lang.Class cls) {
        return dalvik.system.VMDebug.countInstancesOfClass(cls, true);
    }

    /**
     * Returns the number of sent transactions from this process.
     *
     * @return The number of sent transactions or -1 if it could not read t.
     */
    public static native int getBinderSentTransactions();

    /**
     * Returns the number of received transactions from the binder driver.
     *
     * @return The number of received transactions or -1 if it could not read the stats.
     */
    public static native int getBinderReceivedTransactions();

    /**
     * Returns the number of active local Binder objects that exist in the
     * current process.
     */
    public static final native int getBinderLocalObjectCount();

    /**
     * Returns the number of references to remote proxy Binder objects that
     * exist in the current process.
     */
    public static final native int getBinderProxyObjectCount();

    /**
     * Returns the number of death notification links to Binder objects that
     * exist in the current process.
     */
    public static final native int getBinderDeathObjectCount();

    /**
     * Primes the register map cache.
     *
     * Only works for classes in the bootstrap class loader.  Does not
     * cause classes to be loaded if they're not already present.
     *
     * The classAndMethodDesc argument is a concatentation of the VM-internal
     * class descriptor, method name, and method descriptor.  Examples:
     *     Landroid/os/Looper;.loop:()V
     *     Landroid/app/ActivityThread;.main:([Ljava/lang/String;)V
     *
     * @param classAndMethodDesc
     * 		the method to prepare
     * @unknown 
     */
    public static final boolean cacheRegisterMap(java.lang.String classAndMethodDesc) {
        return dalvik.system.VMDebug.cacheRegisterMap(classAndMethodDesc);
    }

    /**
     * Dumps the contents of VM reference tables (e.g. JNI locals and
     * globals) to the log file.
     *
     * @unknown 
     */
    public static final void dumpReferenceTables() {
        dalvik.system.VMDebug.dumpReferenceTables();
    }

    /**
     * API for gathering and querying instruction counts.
     *
     * Example usage:
     * <pre>
     *   Debug.InstructionCount icount = new Debug.InstructionCount();
     *   icount.resetAndStart();
     *    [... do lots of stuff ...]
     *   if (icount.collect()) {
     *       System.out.println("Total instructions executed: "
     *           + icount.globalTotal());
     *       System.out.println("Method invocations: "
     *           + icount.globalMethodInvocations());
     *   }
     * </pre>
     *
     * @deprecated Instruction counting is no longer supported.
     */
    @java.lang.Deprecated
    public static class InstructionCount {
        private static final int NUM_INSTR = dalvik.bytecode.OpcodeInfo.MAXIMUM_PACKED_VALUE + 1;

        private int[] mCounts;

        public InstructionCount() {
            mCounts = new int[android.os.Debug.InstructionCount.NUM_INSTR];
        }

        /**
         * Reset counters and ensure counts are running.  Counts may
         * have already been running.
         *
         * @return true if counting was started
         */
        public boolean resetAndStart() {
            try {
                dalvik.system.VMDebug.startInstructionCounting();
                dalvik.system.VMDebug.resetInstructionCount();
            } catch (java.lang.UnsupportedOperationException uoe) {
                return false;
            }
            return true;
        }

        /**
         * Collect instruction counts.  May or may not stop the
         * counting process.
         */
        public boolean collect() {
            try {
                dalvik.system.VMDebug.stopInstructionCounting();
                dalvik.system.VMDebug.getInstructionCount(mCounts);
            } catch (java.lang.UnsupportedOperationException uoe) {
                return false;
            }
            return true;
        }

        /**
         * Return the total number of instructions executed globally (i.e. in
         * all threads).
         */
        public int globalTotal() {
            int count = 0;
            for (int i = 0; i < android.os.Debug.InstructionCount.NUM_INSTR; i++) {
                count += mCounts[i];
            }
            return count;
        }

        /**
         * Return the total number of method-invocation instructions
         * executed globally.
         */
        public int globalMethodInvocations() {
            int count = 0;
            for (int i = 0; i < android.os.Debug.InstructionCount.NUM_INSTR; i++) {
                if (dalvik.bytecode.OpcodeInfo.isInvoke(i)) {
                    count += mCounts[i];
                }
            }
            return count;
        }
    }

    /**
     * A Map of typed debug properties.
     */
    private static final com.android.internal.util.TypedProperties debugProperties;

    /* Load the debug properties from the standard files into debugProperties. */
    static {
        if (false) {
            final java.lang.String TAG = "DebugProperties";
            final java.lang.String[] files = new java.lang.String[]{ "/system/debug.prop", "/debug.prop", "/data/debug.prop" };
            final com.android.internal.util.TypedProperties tp = new com.android.internal.util.TypedProperties();
            // Read the properties from each of the files, if present.
            for (java.lang.String file : files) {
                java.io.Reader r;
                try {
                    r = new java.io.FileReader(file);
                } catch (java.io.FileNotFoundException ex) {
                    // It's ok if a file is missing.
                    continue;
                }
                try {
                    tp.load(r);
                } catch (java.lang.Exception ex) {
                    throw new java.lang.RuntimeException("Problem loading " + file, ex);
                } finally {
                    try {
                        r.close();
                    } catch (java.io.IOException ex) {
                        // Ignore this error.
                    }
                }
            }
            debugProperties = (tp.isEmpty()) ? null : tp;
        } else {
            debugProperties = null;
        }
    }

    /**
     * Returns true if the type of the field matches the specified class.
     * Handles the case where the class is, e.g., java.lang.Boolean, but
     * the field is of the primitive "boolean" type.  Also handles all of
     * the java.lang.Number subclasses.
     */
    private static boolean fieldTypeMatches(java.lang.reflect.Field field, java.lang.Class<?> cl) {
        java.lang.Class<?> fieldClass = field.getType();
        if (fieldClass == cl) {
            return true;
        }
        java.lang.reflect.Field primitiveTypeField;
        try {
            /* All of the classes we care about (Boolean, Integer, etc.)
            have a Class field called "TYPE" that points to the corresponding
            primitive class.
             */
            primitiveTypeField = cl.getField("TYPE");
        } catch (java.lang.NoSuchFieldException ex) {
            return false;
        }
        try {
            return fieldClass == ((java.lang.Class<?>) (primitiveTypeField.get(null)));
        } catch (java.lang.IllegalAccessException ex) {
            return false;
        }
    }

    /**
     * Looks up the property that corresponds to the field, and sets the field's value
     * if the types match.
     */
    private static void modifyFieldIfSet(final java.lang.reflect.Field field, final com.android.internal.util.TypedProperties properties, final java.lang.String propertyName) {
        if (field.getType() == java.lang.String.class) {
            int stringInfo = properties.getStringInfo(propertyName);
            switch (stringInfo) {
                case com.android.internal.util.TypedProperties.STRING_SET :
                    // Handle as usual below.
                    break;
                case com.android.internal.util.TypedProperties.STRING_NULL :
                    try {
                        field.set(null, null);// null object for static fields; null string

                    } catch (java.lang.IllegalAccessException ex) {
                        throw new java.lang.IllegalArgumentException("Cannot set field for " + propertyName, ex);
                    }
                    return;
                case com.android.internal.util.TypedProperties.STRING_NOT_SET :
                    return;
                case com.android.internal.util.TypedProperties.STRING_TYPE_MISMATCH :
                    throw new java.lang.IllegalArgumentException((((("Type of " + propertyName) + " ") + " does not match field type (") + field.getType()) + ")");
                default :
                    throw new java.lang.IllegalStateException((("Unexpected getStringInfo(" + propertyName) + ") return value ") + stringInfo);
            }
        }
        java.lang.Object value = properties.get(propertyName);
        if (value != null) {
            if (!android.os.Debug.fieldTypeMatches(field, value.getClass())) {
                throw new java.lang.IllegalArgumentException((((((("Type of " + propertyName) + " (") + value.getClass()) + ") ") + " does not match field type (") + field.getType()) + ")");
            }
            try {
                field.set(null, value);// null object for static fields

            } catch (java.lang.IllegalAccessException ex) {
                throw new java.lang.IllegalArgumentException("Cannot set field for " + propertyName, ex);
            }
        }
    }

    /**
     * Equivalent to <code>setFieldsOn(cl, false)</code>.
     *
     * @see #setFieldsOn(Class, boolean)
     * @unknown 
     */
    public static void setFieldsOn(java.lang.Class<?> cl) {
        android.os.Debug.setFieldsOn(cl, false);
    }

    /**
     * Reflectively sets static fields of a class based on internal debugging
     * properties.  This method is a no-op if false is
     * false.
     * <p>
     * <strong>NOTE TO APPLICATION DEVELOPERS</strong>: false will
     * always be false in release builds.  This API is typically only useful
     * for platform developers.
     * </p>
     * Class setup: define a class whose only fields are non-final, static
     * primitive types (except for "char") or Strings.  In a static block
     * after the field definitions/initializations, pass the class to
     * this method, Debug.setFieldsOn(). Example:
     * <pre>
     * package com.example;
     *
     * import android.os.Debug;
     *
     * public class MyDebugVars {
     *    public static String s = "a string";
     *    public static String s2 = "second string";
     *    public static String ns = null;
     *    public static boolean b = false;
     *    public static int i = 5;
     *
     * @unknown public static float f = 0.1f;
     * @unknown public static double d = 0.5d;

    // This MUST appear AFTER all fields are defined and initialized!
    static {
    // Sets all the fields
    Debug.setFieldsOn(MyDebugVars.class);

    // Sets only the fields annotated with @Debug.DebugProperty
    // Debug.setFieldsOn(MyDebugVars.class, true);
    }
    }
    </pre>
    setFieldsOn() may override the value of any field in the class based
    on internal properties that are fixed at boot time.
    <p>
    These properties are only set during platform debugging, and are not
    meant to be used as a general-purpose properties store.

    {@hide }
     * @param cl
     * 		The class to (possibly) modify
     * @param partial
     * 		If false, sets all static fields, otherwise, only set
     * 		fields with the {@link android.os.Debug.DebugProperty}
     * 		annotation
     * @throws IllegalArgumentException
     * 		if any fields are final or non-static,
     * 		or if the type of the field does not match the type of
     * 		the internal debugging property value.
     */
    public static void setFieldsOn(java.lang.Class<?> cl, boolean partial) {
        if (false) {
            if (android.os.Debug.debugProperties != null) {
                /* Only look for fields declared directly by the class,
                so we don't mysteriously change static fields in superclasses.
                 */
                for (java.lang.reflect.Field field : cl.getDeclaredFields()) {
                    if ((!partial) || (field.getAnnotation(android.os.Debug.DebugProperty.class) != null)) {
                        final java.lang.String propertyName = (cl.getName() + ".") + field.getName();
                        boolean isStatic = java.lang.reflect.Modifier.isStatic(field.getModifiers());
                        boolean isFinal = java.lang.reflect.Modifier.isFinal(field.getModifiers());
                        if ((!isStatic) || isFinal) {
                            throw new java.lang.IllegalArgumentException(propertyName + " must be static and non-final");
                        }
                        android.os.Debug.modifyFieldIfSet(field, android.os.Debug.debugProperties, propertyName);
                    }
                }
            }
        } else {
            android.util.Log.wtf(android.os.Debug.TAG, ("setFieldsOn(" + (cl == null ? "null" : cl.getName())) + ") called in non-DEBUG build");
        }
    }

    /**
     * Annotation to put on fields you want to set with
     * {@link Debug#setFieldsOn(Class, boolean)}.
     *
     * @unknown 
     */
    @java.lang.annotation.Target({ java.lang.annotation.ElementType.FIELD })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
    public @interface DebugProperty {}

    /**
     * Get a debugging dump of a system service by name.
     *
     * <p>Most services require the caller to hold android.permission.DUMP.
     *
     * @param name
     * 		of the service to dump
     * @param fd
     * 		to write dump output to (usually an output log file)
     * @param args
     * 		to pass to the service's dump method, may be null
     * @return true if the service was dumped successfully, false if
    the service could not be found or had an error while dumping
     */
    public static boolean dumpService(java.lang.String name, java.io.FileDescriptor fd, java.lang.String[] args) {
        android.os.IBinder service = android.os.ServiceManager.getService(name);
        if (service == null) {
            android.util.Log.e(android.os.Debug.TAG, "Can't find service to dump: " + name);
            return false;
        }
        try {
            service.dump(fd, args);
            return true;
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.os.Debug.TAG, "Can't dump service: " + name, e);
            return false;
        }
    }

    /**
     * Have the stack traces of the given native process dumped to the
     * specified file.  Will be appended to the file.
     *
     * @unknown 
     */
    public static native void dumpNativeBacktraceToFile(int pid, java.lang.String file);

    /**
     * Get description of unreachable native memory.
     *
     * @param limit
     * 		the number of leaks to provide info on, 0 to only get a summary.
     * @param contents
     * 		true to include a hex dump of the contents of unreachable memory.
     * @return the String containing a description of unreachable memory.
     * @unknown 
     */
    public static native java.lang.String getUnreachableMemory(int limit, boolean contents);

    /**
     * Return a String describing the calling method and location at a particular stack depth.
     *
     * @param callStack
     * 		the Thread stack
     * @param depth
     * 		the depth of stack to return information for.
     * @return the String describing the caller at that depth.
     */
    private static java.lang.String getCaller(java.lang.StackTraceElement[] callStack, int depth) {
        // callStack[4] is the caller of the method that called getCallers()
        if ((4 + depth) >= callStack.length) {
            return "<bottom of call stack>";
        }
        java.lang.StackTraceElement caller = callStack[4 + depth];
        return (((caller.getClassName() + ".") + caller.getMethodName()) + ":") + caller.getLineNumber();
    }

    /**
     * Return a string consisting of methods and locations at multiple call stack levels.
     *
     * @param depth
     * 		the number of levels to return, starting with the immediate caller.
     * @return a string describing the call stack.
    {@hide }
     */
    public static java.lang.String getCallers(final int depth) {
        final java.lang.StackTraceElement[] callStack = java.lang.Thread.currentThread().getStackTrace();
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        for (int i = 0; i < depth; i++) {
            sb.append(android.os.Debug.getCaller(callStack, i)).append(" ");
        }
        return sb.toString();
    }

    /**
     * Return a string consisting of methods and locations at multiple call stack levels.
     *
     * @param depth
     * 		the number of levels to return, starting with the immediate caller.
     * @return a string describing the call stack.
    {@hide }
     */
    public static java.lang.String getCallers(final int start, int depth) {
        final java.lang.StackTraceElement[] callStack = java.lang.Thread.currentThread().getStackTrace();
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        depth += start;
        for (int i = start; i < depth; i++) {
            sb.append(android.os.Debug.getCaller(callStack, i)).append(" ");
        }
        return sb.toString();
    }

    /**
     * Like {@link #getCallers(int)}, but each location is append to the string
     * as a new line with <var>linePrefix</var> in front of it.
     *
     * @param depth
     * 		the number of levels to return, starting with the immediate caller.
     * @param linePrefix
     * 		prefix to put in front of each location.
     * @return a string describing the call stack.
    {@hide }
     */
    public static java.lang.String getCallers(final int depth, java.lang.String linePrefix) {
        final java.lang.StackTraceElement[] callStack = java.lang.Thread.currentThread().getStackTrace();
        java.lang.StringBuffer sb = new java.lang.StringBuffer();
        for (int i = 0; i < depth; i++) {
            sb.append(linePrefix).append(android.os.Debug.getCaller(callStack, i)).append("\n");
        }
        return sb.toString();
    }

    /**
     *
     *
     * @return a String describing the immediate caller of the calling method.
    {@hide }
     */
    public static java.lang.String getCaller() {
        return android.os.Debug.getCaller(java.lang.Thread.currentThread().getStackTrace(), 0);
    }
}

