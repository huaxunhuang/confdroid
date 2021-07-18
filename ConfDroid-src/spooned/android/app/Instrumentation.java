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
 * Base class for implementing application instrumentation code.  When running
 * with instrumentation turned on, this class will be instantiated for you
 * before any of the application code, allowing you to monitor all of the
 * interaction the system has with the application.  An Instrumentation
 * implementation is described to the system through an AndroidManifest.xml's
 * &lt;instrumentation&gt; tag.
 */
public class Instrumentation {
    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies the class that is writing the report.  This can be used to provide more structured
     * logging or reporting capabilities in the IInstrumentationWatcher.
     */
    public static final java.lang.String REPORT_KEY_IDENTIFIER = "id";

    /**
     * If included in the status or final bundle sent to an IInstrumentationWatcher, this key
     * identifies a string which can simply be printed to the output stream.  Using these streams
     * provides a "pretty printer" version of the status & final packets.  Any bundles including
     * this key should also include the complete set of raw key/value pairs, so that the
     * instrumentation can also be launched, and results collected, by an automated system.
     */
    public static final java.lang.String REPORT_KEY_STREAMRESULT = "stream";

    private static final java.lang.String TAG = "Instrumentation";

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ 0, android.app.UiAutomation.FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES })
    public @interface UiAutomationFlags {}

    private final java.lang.Object mSync = new java.lang.Object();

    private android.app.ActivityThread mThread = null;

    private android.os.MessageQueue mMessageQueue = null;

    private android.content.Context mInstrContext;

    private android.content.Context mAppContext;

    private android.content.ComponentName mComponent;

    private java.lang.Thread mRunner;

    private java.util.List<android.app.Instrumentation.ActivityWaiter> mWaitingActivities;

    private java.util.List<android.app.Instrumentation.ActivityMonitor> mActivityMonitors;

    private android.app.IInstrumentationWatcher mWatcher;

    private android.app.IUiAutomationConnection mUiAutomationConnection;

    private boolean mAutomaticPerformanceSnapshots = false;

    private android.os.PerformanceCollector mPerformanceCollector;

    private android.os.Bundle mPerfMetrics = new android.os.Bundle();

    private android.app.UiAutomation mUiAutomation;

    public Instrumentation() {
    }

    /**
     * Called when the instrumentation is starting, before any application code
     * has been loaded.  Usually this will be implemented to simply call
     * {@link #start} to begin the instrumentation thread, which will then
     * continue execution in {@link #onStart}.
     *
     * <p>If you do not need your own thread -- that is you are writing your
     * instrumentation to be completely asynchronous (returning to the event
     * loop so that the application can run), you can simply begin your
     * instrumentation here, for example call {@link Context#startActivity} to
     * begin the appropriate first activity of the application.
     *
     * @param arguments
     * 		Any additional arguments that were supplied when the
     * 		instrumentation was started.
     */
    public void onCreate(android.os.Bundle arguments) {
    }

    /**
     * Create and start a new thread in which to run instrumentation.  This new
     * thread will call to {@link #onStart} where you can implement the
     * instrumentation.
     */
    public void start() {
        if (mRunner != null) {
            throw new java.lang.RuntimeException("Instrumentation already started");
        }
        mRunner = new android.app.Instrumentation.InstrumentationThread("Instr: " + getClass().getName());
        mRunner.start();
    }

    /**
     * Method where the instrumentation thread enters execution.  This allows
     * you to run your instrumentation code in a separate thread than the
     * application, so that it can perform blocking operation such as
     * {@link #sendKeySync} or {@link #startActivitySync}.
     *
     * <p>You will typically want to call finish() when this function is done,
     * to end your instrumentation.
     */
    public void onStart() {
    }

    /**
     * This is called whenever the system captures an unhandled exception that
     * was thrown by the application.  The default implementation simply
     * returns false, allowing normal system handling of the exception to take
     * place.
     *
     * @param obj
     * 		The client object that generated the exception.  May be an
     * 		Application, Activity, BroadcastReceiver, Service, or null.
     * @param e
     * 		The exception that was thrown.
     * @return To allow normal system exception process to occur, return false.
    If true is returned, the system will proceed as if the exception
    didn't happen.
     */
    public boolean onException(java.lang.Object obj, java.lang.Throwable e) {
        return false;
    }

    /**
     * Provide a status report about the application.
     *
     * @param resultCode
     * 		Current success/failure of instrumentation.
     * @param results
     * 		Any results to send back to the code that started the instrumentation.
     */
    public void sendStatus(int resultCode, android.os.Bundle results) {
        if (mWatcher != null) {
            try {
                mWatcher.instrumentationStatus(mComponent, resultCode, results);
            } catch (android.os.RemoteException e) {
                mWatcher = null;
            }
        }
    }

    /**
     * Terminate instrumentation of the application.  This will cause the
     * application process to exit, removing this instrumentation from the next
     * time the application is started.
     *
     * @param resultCode
     * 		Overall success/failure of instrumentation.
     * @param results
     * 		Any results to send back to the code that started the
     * 		instrumentation.
     */
    public void finish(int resultCode, android.os.Bundle results) {
        if (mAutomaticPerformanceSnapshots) {
            endPerformanceSnapshot();
        }
        if (mPerfMetrics != null) {
            if (results == null) {
                results = new android.os.Bundle();
            }
            results.putAll(mPerfMetrics);
        }
        if ((mUiAutomation != null) && (!mUiAutomation.isDestroyed())) {
            mUiAutomation.disconnect();
            mUiAutomation = null;
        }
        mThread.finishInstrumentation(resultCode, results);
    }

    public void setAutomaticPerformanceSnapshots() {
        mAutomaticPerformanceSnapshots = true;
        mPerformanceCollector = new android.os.PerformanceCollector();
    }

    public void startPerformanceSnapshot() {
        if (!isProfiling()) {
            mPerformanceCollector.beginSnapshot(null);
        }
    }

    public void endPerformanceSnapshot() {
        if (!isProfiling()) {
            mPerfMetrics = mPerformanceCollector.endSnapshot();
        }
    }

    /**
     * Called when the instrumented application is stopping, after all of the
     * normal application cleanup has occurred.
     */
    public void onDestroy() {
    }

    /**
     * Return the Context of this instrumentation's package.  Note that this is
     * often different than the Context of the application being
     * instrumentated, since the instrumentation code often lives is a
     * different package than that of the application it is running against.
     * See {@link #getTargetContext} to retrieve a Context for the target
     * application.
     *
     * @return The instrumentation's package context.
     * @see #getTargetContext
     */
    public android.content.Context getContext() {
        return mInstrContext;
    }

    /**
     * Returns complete component name of this instrumentation.
     *
     * @return Returns the complete component name for this instrumentation.
     */
    public android.content.ComponentName getComponentName() {
        return mComponent;
    }

    /**
     * Return a Context for the target application being instrumented.  Note
     * that this is often different than the Context of the instrumentation
     * code, since the instrumentation code often lives is a different package
     * than that of the application it is running against. See
     * {@link #getContext} to retrieve a Context for the instrumentation code.
     *
     * @return A Context in the target application.
     * @see #getContext
     */
    public android.content.Context getTargetContext() {
        return mAppContext;
    }

    /**
     * Check whether this instrumentation was started with profiling enabled.
     *
     * @return Returns true if profiling was enabled when starting, else false.
     */
    public boolean isProfiling() {
        return mThread.isProfiling();
    }

    /**
     * This method will start profiling if isProfiling() returns true. You should
     * only call this method if you set the handleProfiling attribute in the
     * manifest file for this Instrumentation to true.
     */
    public void startProfiling() {
        if (mThread.isProfiling()) {
            java.io.File file = new java.io.File(mThread.getProfileFilePath());
            file.getParentFile().mkdirs();
            android.os.Debug.startMethodTracing(file.toString(), (8 * 1024) * 1024);
        }
    }

    /**
     * Stops profiling if isProfiling() returns true.
     */
    public void stopProfiling() {
        if (mThread.isProfiling()) {
            android.os.Debug.stopMethodTracing();
        }
    }

    /**
     * Force the global system in or out of touch mode.  This can be used if
     * your instrumentation relies on the UI being in one more or the other
     * when it starts.
     *
     * @param inTouch
     * 		Set to true to be in touch mode, false to be in
     * 		focus mode.
     */
    public void setInTouchMode(boolean inTouch) {
        try {
            setInTouchMode(inTouch);
        } catch (android.os.RemoteException e) {
            // Shouldn't happen!
        }
    }

    /**
     * Schedule a callback for when the application's main thread goes idle
     * (has no more events to process).
     *
     * @param recipient
     * 		Called the next time the thread's message queue is
     * 		idle.
     */
    public void waitForIdle(java.lang.Runnable recipient) {
        mMessageQueue.addIdleHandler(new android.app.Instrumentation.Idler(recipient));
        mThread.getHandler().post(new android.app.Instrumentation.EmptyRunnable());
    }

    /**
     * Synchronously wait for the application to be idle.  Can not be called
     * from the main application thread -- use {@link #start} to execute
     * instrumentation in its own thread.
     */
    public void waitForIdleSync() {
        validateNotAppThread();
        android.app.Instrumentation.Idler idler = new android.app.Instrumentation.Idler(null);
        mMessageQueue.addIdleHandler(idler);
        mThread.getHandler().post(new android.app.Instrumentation.EmptyRunnable());
        idler.waitForIdle();
    }

    /**
     * Execute a call on the application's main thread, blocking until it is
     * complete.  Useful for doing things that are not thread-safe, such as
     * looking at or modifying the view hierarchy.
     *
     * @param runner
     * 		The code to run on the main thread.
     */
    public void runOnMainSync(java.lang.Runnable runner) {
        validateNotAppThread();
        android.app.Instrumentation.SyncRunnable sr = new android.app.Instrumentation.SyncRunnable(runner);
        mThread.getHandler().post(sr);
        sr.waitForComplete();
    }

    /**
     * Start a new activity and wait for it to begin running before returning.
     * In addition to being synchronous, this method as some semantic
     * differences from the standard {@link Context#startActivity} call: the
     * activity component is resolved before talking with the activity manager
     * (its class name is specified in the Intent that this method ultimately
     * starts), and it does not allow you to start activities that run in a
     * different process.  In addition, if the given Intent resolves to
     * multiple activities, instead of displaying a dialog for the user to
     * select an activity, an exception will be thrown.
     *
     * <p>The function returns as soon as the activity goes idle following the
     * call to its {@link Activity#onCreate}.  Generally this means it has gone
     * through the full initialization including {@link Activity#onResume} and
     * drawn and displayed its initial window.
     *
     * @param intent
     * 		Description of the activity to start.
     * @see Context#startActivity
     */
    public android.app.Activity startActivitySync(android.content.Intent intent) {
        validateNotAppThread();
        synchronized(mSync) {
            intent = new android.content.Intent(intent);
            android.content.pm.ActivityInfo ai = intent.resolveActivityInfo(getTargetContext().getPackageManager(), 0);
            if (ai == null) {
                throw new java.lang.RuntimeException("Unable to resolve activity for: " + intent);
            }
            java.lang.String myProc = mThread.getProcessName();
            if (!ai.processName.equals(myProc)) {
                // todo: if this intent is ambiguous, look here to see if
                // there is a single match that is in our package.
                throw new java.lang.RuntimeException((((("Intent in process " + myProc) + " resolved to different process ") + ai.processName) + ": ") + intent);
            }
            intent.setComponent(new android.content.ComponentName(ai.applicationInfo.packageName, ai.name));
            final android.app.Instrumentation.ActivityWaiter aw = new android.app.Instrumentation.ActivityWaiter(intent);
            if (mWaitingActivities == null) {
                mWaitingActivities = new java.util.ArrayList();
            }
            mWaitingActivities.add(aw);
            getTargetContext().startActivity(intent);
            do {
                try {
                    mSync.wait();
                } catch (java.lang.InterruptedException e) {
                }
            } while (mWaitingActivities.contains(aw) );
            return aw.activity;
        }
    }

    /**
     * Information about a particular kind of Intent that is being monitored.
     * An instance of this class is added to the
     * current instrumentation through {@link #addMonitor}; after being added,
     * when a new activity is being started the monitor will be checked and, if
     * matching, its hit count updated and (optionally) the call stopped and a
     * canned result returned.
     *
     * <p>An ActivityMonitor can also be used to look for the creation of an
     * activity, through the {@link #waitForActivity} method.  This will return
     * after a matching activity has been created with that activity object.
     */
    public static class ActivityMonitor {
        private final android.content.IntentFilter mWhich;

        private final java.lang.String mClass;

        private final android.app.Instrumentation.ActivityResult mResult;

        private final boolean mBlock;

        // This is protected by 'Instrumentation.this.mSync'.
        /* package */
        int mHits = 0;

        // This is protected by 'this'.
        /* package */
        android.app.Activity mLastActivity = null;

        /**
         * Create a new ActivityMonitor that looks for a particular kind of
         * intent to be started.
         *
         * @param which
         * 		The set of intents this monitor is responsible for.
         * @param result
         * 		A canned result to return if the monitor is hit; can
         * 		be null.
         * @param block
         * 		Controls whether the monitor should block the activity
         * 		start (returning its canned result) or let the call
         * 		proceed.
         * @see Instrumentation#addMonitor
         */
        public ActivityMonitor(android.content.IntentFilter which, android.app.Instrumentation.ActivityResult result, boolean block) {
            mWhich = which;
            mClass = null;
            mResult = result;
            mBlock = block;
        }

        /**
         * Create a new ActivityMonitor that looks for a specific activity
         * class to be started.
         *
         * @param cls
         * 		The activity class this monitor is responsible for.
         * @param result
         * 		A canned result to return if the monitor is hit; can
         * 		be null.
         * @param block
         * 		Controls whether the monitor should block the activity
         * 		start (returning its canned result) or let the call
         * 		proceed.
         * @see Instrumentation#addMonitor
         */
        public ActivityMonitor(java.lang.String cls, android.app.Instrumentation.ActivityResult result, boolean block) {
            mWhich = null;
            mClass = cls;
            mResult = result;
            mBlock = block;
        }

        /**
         * Retrieve the filter associated with this ActivityMonitor.
         */
        public final android.content.IntentFilter getFilter() {
            return mWhich;
        }

        /**
         * Retrieve the result associated with this ActivityMonitor, or null if
         * none.
         */
        public final android.app.Instrumentation.ActivityResult getResult() {
            return mResult;
        }

        /**
         * Check whether this monitor blocks activity starts (not allowing the
         * actual activity to run) or allows them to execute normally.
         */
        public final boolean isBlocking() {
            return mBlock;
        }

        /**
         * Retrieve the number of times the monitor has been hit so far.
         */
        public final int getHits() {
            return mHits;
        }

        /**
         * Retrieve the most recent activity class that was seen by this
         * monitor.
         */
        public final android.app.Activity getLastActivity() {
            return mLastActivity;
        }

        /**
         * Block until an Activity is created that matches this monitor,
         * returning the resulting activity.
         *
         * @return Activity
         */
        public final android.app.Activity waitForActivity() {
            synchronized(this) {
                while (mLastActivity == null) {
                    try {
                        wait();
                    } catch (java.lang.InterruptedException e) {
                    }
                } 
                android.app.Activity res = mLastActivity;
                mLastActivity = null;
                return res;
            }
        }

        /**
         * Block until an Activity is created that matches this monitor,
         * returning the resulting activity or till the timeOut period expires.
         * If the timeOut expires before the activity is started, return null.
         *
         * @param timeOut
         * 		Time to wait in milliseconds before the activity is created.
         * @return Activity
         */
        public final android.app.Activity waitForActivityWithTimeout(long timeOut) {
            synchronized(this) {
                if (mLastActivity == null) {
                    try {
                        wait(timeOut);
                    } catch (java.lang.InterruptedException e) {
                    }
                }
                if (mLastActivity == null) {
                    return null;
                } else {
                    android.app.Activity res = mLastActivity;
                    mLastActivity = null;
                    return res;
                }
            }
        }

        final boolean match(android.content.Context who, android.app.Activity activity, android.content.Intent intent) {
            synchronized(this) {
                if ((mWhich != null) && (mWhich.match(who.getContentResolver(), intent, true, "Instrumentation") < 0)) {
                    return false;
                }
                if (mClass != null) {
                    java.lang.String cls = null;
                    if (activity != null) {
                        cls = activity.getClass().getName();
                    } else
                        if (intent.getComponent() != null) {
                            cls = intent.getComponent().getClassName();
                        }

                    if ((cls == null) || (!mClass.equals(cls))) {
                        return false;
                    }
                }
                if (activity != null) {
                    mLastActivity = activity;
                    notifyAll();
                }
                return true;
            }
        }
    }

    /**
     * Add a new {@link ActivityMonitor} that will be checked whenever an
     * activity is started.  The monitor is added
     * after any existing ones; the monitor will be hit only if none of the
     * existing monitors can themselves handle the Intent.
     *
     * @param monitor
     * 		The new ActivityMonitor to see.
     * @see #addMonitor(IntentFilter, ActivityResult, boolean)
     * @see #checkMonitorHit
     */
    public void addMonitor(android.app.Instrumentation.ActivityMonitor monitor) {
        synchronized(mSync) {
            if (mActivityMonitors == null) {
                mActivityMonitors = new java.util.ArrayList();
            }
            mActivityMonitors.add(monitor);
        }
    }

    /**
     * A convenience wrapper for {@link #addMonitor(ActivityMonitor)} that
     * creates an intent filter matching {@link ActivityMonitor} for you and
     * returns it.
     *
     * @param filter
     * 		The set of intents this monitor is responsible for.
     * @param result
     * 		A canned result to return if the monitor is hit; can
     * 		be null.
     * @param block
     * 		Controls whether the monitor should block the activity
     * 		start (returning its canned result) or let the call
     * 		proceed.
     * @return The newly created and added activity monitor.
     * @see #addMonitor(ActivityMonitor)
     * @see #checkMonitorHit
     */
    public android.app.Instrumentation.ActivityMonitor addMonitor(android.content.IntentFilter filter, android.app.Instrumentation.ActivityResult result, boolean block) {
        android.app.Instrumentation.ActivityMonitor am = new android.app.Instrumentation.ActivityMonitor(filter, result, block);
        addMonitor(am);
        return am;
    }

    /**
     * A convenience wrapper for {@link #addMonitor(ActivityMonitor)} that
     * creates a class matching {@link ActivityMonitor} for you and returns it.
     *
     * @param cls
     * 		The activity class this monitor is responsible for.
     * @param result
     * 		A canned result to return if the monitor is hit; can
     * 		be null.
     * @param block
     * 		Controls whether the monitor should block the activity
     * 		start (returning its canned result) or let the call
     * 		proceed.
     * @return The newly created and added activity monitor.
     * @see #addMonitor(ActivityMonitor)
     * @see #checkMonitorHit
     */
    public android.app.Instrumentation.ActivityMonitor addMonitor(java.lang.String cls, android.app.Instrumentation.ActivityResult result, boolean block) {
        android.app.Instrumentation.ActivityMonitor am = new android.app.Instrumentation.ActivityMonitor(cls, result, block);
        addMonitor(am);
        return am;
    }

    /**
     * Test whether an existing {@link ActivityMonitor} has been hit.  If the
     * monitor has been hit at least <var>minHits</var> times, then it will be
     * removed from the activity monitor list and true returned.  Otherwise it
     * is left as-is and false is returned.
     *
     * @param monitor
     * 		The ActivityMonitor to check.
     * @param minHits
     * 		The minimum number of hits required.
     * @return True if the hit count has been reached, else false.
     * @see #addMonitor
     */
    public boolean checkMonitorHit(android.app.Instrumentation.ActivityMonitor monitor, int minHits) {
        waitForIdleSync();
        synchronized(mSync) {
            if (monitor.getHits() < minHits) {
                return false;
            }
            mActivityMonitors.remove(monitor);
        }
        return true;
    }

    /**
     * Wait for an existing {@link ActivityMonitor} to be hit.  Once the
     * monitor has been hit, it is removed from the activity monitor list and
     * the first created Activity object that matched it is returned.
     *
     * @param monitor
     * 		The ActivityMonitor to wait for.
     * @return The Activity object that matched the monitor.
     */
    public android.app.Activity waitForMonitor(android.app.Instrumentation.ActivityMonitor monitor) {
        android.app.Activity activity = monitor.waitForActivity();
        synchronized(mSync) {
            mActivityMonitors.remove(monitor);
        }
        return activity;
    }

    /**
     * Wait for an existing {@link ActivityMonitor} to be hit till the timeout
     * expires.  Once the monitor has been hit, it is removed from the activity
     * monitor list and the first created Activity object that matched it is
     * returned.  If the timeout expires, a null object is returned.
     *
     * @param monitor
     * 		The ActivityMonitor to wait for.
     * @param timeOut
     * 		The timeout value in secs.
     * @return The Activity object that matched the monitor.
     */
    public android.app.Activity waitForMonitorWithTimeout(android.app.Instrumentation.ActivityMonitor monitor, long timeOut) {
        android.app.Activity activity = monitor.waitForActivityWithTimeout(timeOut);
        synchronized(mSync) {
            mActivityMonitors.remove(monitor);
        }
        return activity;
    }

    /**
     * Remove an {@link ActivityMonitor} that was previously added with
     * {@link #addMonitor}.
     *
     * @param monitor
     * 		The monitor to remove.
     * @see #addMonitor
     */
    public void removeMonitor(android.app.Instrumentation.ActivityMonitor monitor) {
        synchronized(mSync) {
            mActivityMonitors.remove(monitor);
        }
    }

    /**
     * Execute a particular menu item.
     *
     * @param targetActivity
     * 		The activity in question.
     * @param id
     * 		The identifier associated with the menu item.
     * @param flag
     * 		Additional flags, if any.
     * @return Whether the invocation was successful (for example, it could be
    false if item is disabled).
     */
    public boolean invokeMenuActionSync(android.app.Activity targetActivity, int id, int flag) {
        class MenuRunnable implements java.lang.Runnable {
            private final android.app.Activity activity;

            private final int identifier;

            private final int flags;

            boolean returnValue;

            public MenuRunnable(android.app.Activity _activity, int _identifier, int _flags) {
                activity = _activity;
                identifier = _identifier;
                flags = _flags;
            }

            public void run() {
                android.view.Window win = activity.getWindow();
                returnValue = win.performPanelIdentifierAction(android.view.Window.FEATURE_OPTIONS_PANEL, identifier, flags);
            }
        }
        MenuRunnable mr = new MenuRunnable(targetActivity, id, flag);
        runOnMainSync(mr);
        return mr.returnValue;
    }

    /**
     * Show the context menu for the currently focused view and executes a
     * particular context menu item.
     *
     * @param targetActivity
     * 		The activity in question.
     * @param id
     * 		The identifier associated with the context menu item.
     * @param flag
     * 		Additional flags, if any.
     * @return Whether the invocation was successful (for example, it could be
    false if item is disabled).
     */
    public boolean invokeContextMenuAction(android.app.Activity targetActivity, int id, int flag) {
        validateNotAppThread();
        // Bring up context menu for current focus.
        // It'd be nice to do this through code, but currently ListView depends on
        // long press to set metadata for its selected child
        final android.view.KeyEvent downEvent = new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        sendKeySync(downEvent);
        // Need to wait for long press
        waitForIdleSync();
        try {
            java.lang.Thread.sleep(android.view.ViewConfiguration.getLongPressTimeout());
        } catch (java.lang.InterruptedException e) {
            android.util.Log.e(android.app.Instrumentation.TAG, "Could not sleep for long press timeout", e);
            return false;
        }
        final android.view.KeyEvent upEvent = new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, android.view.KeyEvent.KEYCODE_DPAD_CENTER);
        sendKeySync(upEvent);
        // Wait for context menu to appear
        waitForIdleSync();
        class ContextMenuRunnable implements java.lang.Runnable {
            private final android.app.Activity activity;

            private final int identifier;

            private final int flags;

            boolean returnValue;

            public ContextMenuRunnable(android.app.Activity _activity, int _identifier, int _flags) {
                activity = _activity;
                identifier = _identifier;
                flags = _flags;
            }

            public void run() {
                android.view.Window win = activity.getWindow();
                returnValue = win.performContextMenuIdentifierAction(identifier, flags);
            }
        }
        ContextMenuRunnable cmr = new ContextMenuRunnable(targetActivity, id, flag);
        runOnMainSync(cmr);
        return cmr.returnValue;
    }

    /**
     * Sends the key events corresponding to the text to the app being
     * instrumented.
     *
     * @param text
     * 		The text to be sent.
     */
    public void sendStringSync(java.lang.String text) {
        if (text == null) {
            return;
        }
        android.view.KeyCharacterMap keyCharacterMap = android.view.KeyCharacterMap.load(android.view.KeyCharacterMap.VIRTUAL_KEYBOARD);
        android.view.KeyEvent[] events = keyCharacterMap.getEvents(text.toCharArray());
        if (events != null) {
            for (int i = 0; i < events.length; i++) {
                // We have to change the time of an event before injecting it because
                // all KeyEvents returned by KeyCharacterMap.getEvents() have the same
                // time stamp and the system rejects too old events. Hence, it is
                // possible for an event to become stale before it is injected if it
                // takes too long to inject the preceding ones.
                sendKeySync(android.view.KeyEvent.changeTimeRepeat(events[i], android.os.SystemClock.uptimeMillis(), 0));
            }
        }
    }

    /**
     * Send a key event to the currently focused window/view and wait for it to
     * be processed.  Finished at some point after the recipient has returned
     * from its event processing, though it may <em>not</em> have completely
     * finished reacting from the event -- for example, if it needs to update
     * its display as a result, it may still be in the process of doing that.
     *
     * @param event
     * 		The event to send to the current focus.
     */
    public void sendKeySync(android.view.KeyEvent event) {
        validateNotAppThread();
        long downTime = event.getDownTime();
        long eventTime = event.getEventTime();
        int action = event.getAction();
        int code = event.getKeyCode();
        int repeatCount = event.getRepeatCount();
        int metaState = event.getMetaState();
        int deviceId = event.getDeviceId();
        int scancode = event.getScanCode();
        int source = event.getSource();
        int flags = event.getFlags();
        if (source == android.view.InputDevice.SOURCE_UNKNOWN) {
            source = android.view.InputDevice.SOURCE_KEYBOARD;
        }
        if (eventTime == 0) {
            eventTime = android.os.SystemClock.uptimeMillis();
        }
        if (downTime == 0) {
            downTime = eventTime;
        }
        android.view.KeyEvent newEvent = new android.view.KeyEvent(downTime, eventTime, action, code, repeatCount, metaState, deviceId, scancode, flags | android.view.KeyEvent.FLAG_FROM_SYSTEM, source);
        android.hardware.input.InputManager.getInstance().injectInputEvent(newEvent, android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

    /**
     * Sends an up and down key event sync to the currently focused window.
     *
     * @param key
     * 		The integer keycode for the event.
     */
    public void sendKeyDownUpSync(int key) {
        sendKeySync(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, key));
        sendKeySync(new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, key));
    }

    /**
     * Higher-level method for sending both the down and up key events for a
     * particular character key code.  Equivalent to creating both KeyEvent
     * objects by hand and calling {@link #sendKeySync}.  The event appears
     * as if it came from keyboard 0, the built in one.
     *
     * @param keyCode
     * 		The key code of the character to send.
     */
    public void sendCharacterSync(int keyCode) {
        sendKeySync(new android.view.KeyEvent(android.view.KeyEvent.ACTION_DOWN, keyCode));
        sendKeySync(new android.view.KeyEvent(android.view.KeyEvent.ACTION_UP, keyCode));
    }

    /**
     * Dispatch a pointer event. Finished at some point after the recipient has
     * returned from its event processing, though it may <em>not</em> have
     * completely finished reacting from the event -- for example, if it needs
     * to update its display as a result, it may still be in the process of
     * doing that.
     *
     * @param event
     * 		A motion event describing the pointer action.  (As noted in
     * 		{@link MotionEvent#obtain(long, long, int, float, float, int)}, be sure to use
     * 		{@link SystemClock#uptimeMillis()} as the timebase.
     */
    public void sendPointerSync(android.view.MotionEvent event) {
        validateNotAppThread();
        if ((event.getSource() & android.view.InputDevice.SOURCE_CLASS_POINTER) == 0) {
            event.setSource(android.view.InputDevice.SOURCE_TOUCHSCREEN);
        }
        android.hardware.input.InputManager.getInstance().injectInputEvent(event, android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

    /**
     * Dispatch a trackball event. Finished at some point after the recipient has
     * returned from its event processing, though it may <em>not</em> have
     * completely finished reacting from the event -- for example, if it needs
     * to update its display as a result, it may still be in the process of
     * doing that.
     *
     * @param event
     * 		A motion event describing the trackball action.  (As noted in
     * 		{@link MotionEvent#obtain(long, long, int, float, float, int)}, be sure to use
     * 		{@link SystemClock#uptimeMillis()} as the timebase.
     */
    public void sendTrackballEventSync(android.view.MotionEvent event) {
        validateNotAppThread();
        if ((event.getSource() & android.view.InputDevice.SOURCE_CLASS_TRACKBALL) == 0) {
            event.setSource(android.view.InputDevice.SOURCE_TRACKBALL);
        }
        android.hardware.input.InputManager.getInstance().injectInputEvent(event, android.hardware.input.InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
    }

    /**
     * Perform instantiation of the process's {@link Application} object.  The
     * default implementation provides the normal system behavior.
     *
     * @param cl
     * 		The ClassLoader with which to instantiate the object.
     * @param className
     * 		The name of the class implementing the Application
     * 		object.
     * @param context
     * 		The context to initialize the application with
     * @return The newly instantiated Application object.
     */
    public android.app.Application newApplication(java.lang.ClassLoader cl, java.lang.String className, android.content.Context context) throws java.lang.ClassNotFoundException, java.lang.IllegalAccessException, java.lang.InstantiationException {
        return android.app.Instrumentation.newApplication(cl.loadClass(className), context);
    }

    /**
     * Perform instantiation of the process's {@link Application} object.  The
     * default implementation provides the normal system behavior.
     *
     * @param clazz
     * 		The class used to create an Application object from.
     * @param context
     * 		The context to initialize the application with
     * @return The newly instantiated Application object.
     */
    public static android.app.Application newApplication(java.lang.Class<?> clazz, android.content.Context context) throws java.lang.ClassNotFoundException, java.lang.IllegalAccessException, java.lang.InstantiationException {
        android.app.Application app = ((android.app.Application) (clazz.newInstance()));
        app.attach(context);
        return app;
    }

    /**
     * Perform calling of the application's {@link Application#onCreate}
     * method.  The default implementation simply calls through to that method.
     *
     * <p>Note: This method will be called immediately after {@link #onCreate(Bundle)}.
     * Often instrumentation tests start their test thread in onCreate(); you
     * need to be careful of races between these.  (Well between it and
     * everything else, but let's start here.)
     *
     * @param app
     * 		The application being created.
     */
    public void callApplicationOnCreate(android.app.Application app) {
        app.onCreate();
    }

    /**
     * Perform instantiation of an {@link Activity} object.  This method is intended for use with
     * unit tests, such as android.test.ActivityUnitTestCase.  The activity will be useable
     * locally but will be missing some of the linkages necessary for use within the sytem.
     *
     * @param clazz
     * 		The Class of the desired Activity
     * @param context
     * 		The base context for the activity to use
     * @param token
     * 		The token for this activity to communicate with
     * @param application
     * 		The application object (if any)
     * @param intent
     * 		The intent that started this Activity
     * @param info
     * 		ActivityInfo from the manifest
     * @param title
     * 		The title, typically retrieved from the ActivityInfo record
     * @param parent
     * 		The parent Activity (if any)
     * @param id
     * 		The embedded Id (if any)
     * @param lastNonConfigurationInstance
     * 		Arbitrary object that will be
     * 		available via {@link Activity#getLastNonConfigurationInstance()
     * 		Activity.getLastNonConfigurationInstance()}.
     * @return Returns the instantiated activity
     * @throws InstantiationException
     * 		
     * @throws IllegalAccessException
     * 		
     */
    public android.app.Activity newActivity(java.lang.Class<?> clazz, android.content.Context context, android.os.IBinder token, android.app.Application application, android.content.Intent intent, android.content.pm.ActivityInfo info, java.lang.CharSequence title, android.app.Activity parent, java.lang.String id, java.lang.Object lastNonConfigurationInstance) throws java.lang.IllegalAccessException, java.lang.InstantiationException {
        android.app.Activity activity = ((android.app.Activity) (clazz.newInstance()));
        android.app.ActivityThread aThread = null;
        activity.attach(context, aThread, this, token, 0, application, intent, info, title, parent, id, ((android.app.Activity.NonConfigurationInstances) (lastNonConfigurationInstance)), new android.content.res.Configuration(), null, null, null);
        return activity;
    }

    /**
     * Perform instantiation of the process's {@link Activity} object.  The
     * default implementation provides the normal system behavior.
     *
     * @param cl
     * 		The ClassLoader with which to instantiate the object.
     * @param className
     * 		The name of the class implementing the Activity
     * 		object.
     * @param intent
     * 		The Intent object that specified the activity class being
     * 		instantiated.
     * @return The newly instantiated Activity object.
     */
    public android.app.Activity newActivity(java.lang.ClassLoader cl, java.lang.String className, android.content.Intent intent) throws java.lang.ClassNotFoundException, java.lang.IllegalAccessException, java.lang.InstantiationException {
        return ((android.app.Activity) (cl.loadClass(className).newInstance()));
    }

    private void prePerformCreate(android.app.Activity activity) {
        if (mWaitingActivities != null) {
            synchronized(mSync) {
                final int N = mWaitingActivities.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityWaiter aw = mWaitingActivities.get(i);
                    final android.content.Intent intent = aw.intent;
                    if (intent.filterEquals(activity.getIntent())) {
                        aw.activity = activity;
                        mMessageQueue.addIdleHandler(new android.app.Instrumentation.ActivityGoing(aw));
                    }
                }
            }
        }
    }

    private void postPerformCreate(android.app.Activity activity) {
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    am.match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    /**
     * Perform calling of an activity's {@link Activity#onCreate}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being created.
     * @param icicle
     * 		The previously frozen state (or null) to pass through to onCreate().
     */
    public void callActivityOnCreate(android.app.Activity activity, android.os.Bundle icicle) {
        prePerformCreate(activity);
        activity.performCreate(icicle);
        postPerformCreate(activity);
    }

    /**
     * Perform calling of an activity's {@link Activity#onCreate}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being created.
     * @param icicle
     * 		The previously frozen state (or null) to pass through to
     * @param persistentState
     * 		The previously persisted state (or null)
     */
    public void callActivityOnCreate(android.app.Activity activity, android.os.Bundle icicle, android.os.PersistableBundle persistentState) {
        prePerformCreate(activity);
        activity.performCreate(icicle, persistentState);
        postPerformCreate(activity);
    }

    public void callActivityOnDestroy(android.app.Activity activity) {
        // TODO: the following block causes intermittent hangs when using startActivity
        // temporarily comment out until root cause is fixed (bug 2630683)
        // if (mWaitingActivities != null) {
        // synchronized (mSync) {
        // final int N = mWaitingActivities.size();
        // for (int i=0; i<N; i++) {
        // final ActivityWaiter aw = mWaitingActivities.get(i);
        // final Intent intent = aw.intent;
        // if (intent.filterEquals(activity.getIntent())) {
        // aw.activity = activity;
        // mMessageQueue.addIdleHandler(new ActivityGoing(aw));
        // }
        // }
        // }
        // }
        activity.performDestroy();
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    am.match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    /**
     * Perform calling of an activity's {@link Activity#onRestoreInstanceState}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being restored.
     * @param savedInstanceState
     * 		The previously saved state being restored.
     */
    public void callActivityOnRestoreInstanceState(android.app.Activity activity, android.os.Bundle savedInstanceState) {
        activity.performRestoreInstanceState(savedInstanceState);
    }

    /**
     * Perform calling of an activity's {@link Activity#onRestoreInstanceState}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being restored.
     * @param savedInstanceState
     * 		The previously saved state being restored.
     * @param persistentState
     * 		The previously persisted state (or null)
     */
    public void callActivityOnRestoreInstanceState(android.app.Activity activity, android.os.Bundle savedInstanceState, android.os.PersistableBundle persistentState) {
        activity.performRestoreInstanceState(savedInstanceState, persistentState);
    }

    /**
     * Perform calling of an activity's {@link Activity#onPostCreate} method.
     * The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being created.
     * @param icicle
     * 		The previously frozen state (or null) to pass through to
     * 		onPostCreate().
     */
    public void callActivityOnPostCreate(android.app.Activity activity, android.os.Bundle icicle) {
        activity.onPostCreate(icicle);
    }

    /**
     * Perform calling of an activity's {@link Activity#onPostCreate} method.
     * The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being created.
     * @param icicle
     * 		The previously frozen state (or null) to pass through to
     * 		onPostCreate().
     */
    public void callActivityOnPostCreate(android.app.Activity activity, android.os.Bundle icicle, android.os.PersistableBundle persistentState) {
        activity.onPostCreate(icicle, persistentState);
    }

    /**
     * Perform calling of an activity's {@link Activity#onNewIntent}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity receiving a new Intent.
     * @param intent
     * 		The new intent being received.
     */
    public void callActivityOnNewIntent(android.app.Activity activity, android.content.Intent intent) {
        activity.onNewIntent(intent);
    }

    /**
     *
     *
     * @unknown 
     */
    public void callActivityOnNewIntent(android.app.Activity activity, com.android.internal.content.ReferrerIntent intent) {
        final java.lang.String oldReferrer = activity.mReferrer;
        try {
            if (intent != null) {
                activity.mReferrer = intent.mReferrer;
            }
            callActivityOnNewIntent(activity, intent != null ? new android.content.Intent(intent) : null);
        } finally {
            activity.mReferrer = oldReferrer;
        }
    }

    /**
     * Perform calling of an activity's {@link Activity#onStart}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being started.
     */
    public void callActivityOnStart(android.app.Activity activity) {
        activity.onStart();
    }

    /**
     * Perform calling of an activity's {@link Activity#onRestart}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being restarted.
     */
    public void callActivityOnRestart(android.app.Activity activity) {
        activity.onRestart();
    }

    /**
     * Perform calling of an activity's {@link Activity#onResume} method.  The
     * default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being resumed.
     */
    public void callActivityOnResume(android.app.Activity activity) {
        activity.mResumed = true;
        activity.onResume();
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    am.match(activity, activity, activity.getIntent());
                }
            }
        }
    }

    /**
     * Perform calling of an activity's {@link Activity#onStop}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being stopped.
     */
    public void callActivityOnStop(android.app.Activity activity) {
        activity.onStop();
    }

    /**
     * Perform calling of an activity's {@link Activity#onSaveInstanceState}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being saved.
     * @param outState
     * 		The bundle to pass to the call.
     */
    public void callActivityOnSaveInstanceState(android.app.Activity activity, android.os.Bundle outState) {
        activity.performSaveInstanceState(outState);
    }

    /**
     * Perform calling of an activity's {@link Activity#onSaveInstanceState}
     * method.  The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being saved.
     * @param outState
     * 		The bundle to pass to the call.
     * @param outPersistentState
     * 		The persistent bundle to pass to the call.
     */
    public void callActivityOnSaveInstanceState(android.app.Activity activity, android.os.Bundle outState, android.os.PersistableBundle outPersistentState) {
        activity.performSaveInstanceState(outState, outPersistentState);
    }

    /**
     * Perform calling of an activity's {@link Activity#onPause} method.  The
     * default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being paused.
     */
    public void callActivityOnPause(android.app.Activity activity) {
        activity.performPause();
    }

    /**
     * Perform calling of an activity's {@link Activity#onUserLeaveHint} method.
     * The default implementation simply calls through to that method.
     *
     * @param activity
     * 		The activity being notified that the user has navigated away
     */
    public void callActivityOnUserLeaving(android.app.Activity activity) {
        activity.performUserLeaving();
    }

    /* Starts allocation counting. This triggers a gc and resets the counts.

    @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public void startAllocCounting() {
        // Before we start trigger a GC and reset the debug counts. Run the
        // finalizers and another GC before starting and stopping the alloc
        // counts. This will free up any objects that were just sitting around
        // waiting for their finalizers to be run.
        java.lang.Runtime.getRuntime().gc();
        java.lang.Runtime.getRuntime().runFinalization();
        java.lang.Runtime.getRuntime().gc();
        android.os.Debug.resetAllCounts();
        // start the counts
        android.os.Debug.startAllocCounting();
    }

    /* Stops allocation counting.

    @deprecated Accurate counting is a burden on the runtime and may be removed.
     */
    @java.lang.Deprecated
    public void stopAllocCounting() {
        java.lang.Runtime.getRuntime().gc();
        java.lang.Runtime.getRuntime().runFinalization();
        java.lang.Runtime.getRuntime().gc();
        android.os.Debug.stopAllocCounting();
    }

    /**
     * If Results already contains Key, it appends Value to the key's ArrayList
     * associated with the key. If the key doesn't already exist in results, it
     * adds the key/value pair to results.
     */
    private void addValue(java.lang.String key, int value, android.os.Bundle results) {
        if (results.containsKey(key)) {
            java.util.List<java.lang.Integer> list = results.getIntegerArrayList(key);
            if (list != null) {
                list.add(value);
            }
        } else {
            java.util.ArrayList<java.lang.Integer> list = new java.util.ArrayList<java.lang.Integer>();
            list.add(value);
            results.putIntegerArrayList(key, list);
        }
    }

    /**
     * Returns a bundle with the current results from the allocation counting.
     */
    public android.os.Bundle getAllocCounts() {
        android.os.Bundle results = new android.os.Bundle();
        results.putLong("global_alloc_count", android.os.Debug.getGlobalAllocCount());
        results.putLong("global_alloc_size", android.os.Debug.getGlobalAllocSize());
        results.putLong("global_freed_count", android.os.Debug.getGlobalFreedCount());
        results.putLong("global_freed_size", android.os.Debug.getGlobalFreedSize());
        results.putLong("gc_invocation_count", android.os.Debug.getGlobalGcInvocationCount());
        return results;
    }

    /**
     * Returns a bundle with the counts for various binder counts for this process. Currently the only two that are
     * reported are the number of send and the number of received transactions.
     */
    public android.os.Bundle getBinderCounts() {
        android.os.Bundle results = new android.os.Bundle();
        results.putLong("sent_transactions", android.os.Debug.getBinderSentTransactions());
        results.putLong("received_transactions", android.os.Debug.getBinderReceivedTransactions());
        return results;
    }

    /**
     * Description of a Activity execution result to return to the original
     * activity.
     */
    public static final class ActivityResult {
        /**
         * Create a new activity result.  See {@link Activity#setResult} for
         * more information.
         *
         * @param resultCode
         * 		The result code to propagate back to the
         * 		originating activity, often RESULT_CANCELED or RESULT_OK
         * @param resultData
         * 		The data to propagate back to the originating
         * 		activity.
         */
        public ActivityResult(int resultCode, android.content.Intent resultData) {
            mResultCode = resultCode;
            mResultData = resultData;
        }

        /**
         * Retrieve the result code contained in this result.
         */
        public int getResultCode() {
            return mResultCode;
        }

        /**
         * Retrieve the data contained in this result.
         */
        public android.content.Intent getResultData() {
            return mResultData;
        }

        private final int mResultCode;

        private final android.content.Intent mResultData;
    }

    /**
     * Execute a startActivity call made by the application.  The default
     * implementation takes care of updating any active {@link ActivityMonitor}
     * objects and dispatches this call to the system activity manager; you can
     * override this to watch for the application to start an activity, and
     * modify what happens when it does.
     *
     * <p>This method returns an {@link ActivityResult} object, which you can
     * use when intercepting application calls to avoid performing the start
     * activity action but still return the result the application is
     * expecting.  To do this, override this method to catch the call to start
     * activity so that it returns a new ActivityResult containing the results
     * you would like the application to see, and don't call up to the super
     * class.  Note that an application is only expecting a result if
     * <var>requestCode</var> is &gt;= 0.
     *
     * <p>This method throws {@link android.content.ActivityNotFoundException}
     * if there was no Activity found to run the given Intent.
     *
     * @param who
     * 		The Context from which the activity is being started.
     * @param contextThread
     * 		The main thread of the Context from which the activity
     * 		is being started.
     * @param token
     * 		Internal token identifying to the system who is starting
     * 		the activity; may be null.
     * @param target
     * 		Which activity is performing the start (and thus receiving
     * 		any result); may be null if this call is not being made
     * 		from an activity.
     * @param intent
     * 		The actual Intent to start.
     * @param requestCode
     * 		Identifier for this request's result; less than zero
     * 		if the caller is not expecting a result.
     * @param options
     * 		Addition options.
     * @return To force the return of a particular result, return an
    ActivityResult object containing the desired data; otherwise
    return null.  The default implementation always returns null.
     * @throws android.content.ActivityNotFoundException
     * 		
     * @see Activity#startActivity(Intent)
     * @see Activity#startActivityForResult(Intent, int)
     * @see Activity#startActivityFromChild

    {@hide }
     */
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context who, android.os.IBinder contextThread, android.os.IBinder token, android.app.Activity target, android.content.Intent intent, int requestCode, android.os.Bundle options) {
        android.app.IApplicationThread whoThread = ((android.app.IApplicationThread) (contextThread));
        android.net.Uri referrer = (target != null) ? target.onProvideReferrer() : null;
        if (referrer != null) {
            intent.putExtra(android.content.Intent.EXTRA_REFERRER, referrer);
        }
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    if (am.match(who, null, intent)) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return requestCode >= 0 ? am.getResult() : null;
                        }
                        break;
                    }
                }
            }
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            int result = android.app.ActivityManagerNative.getDefault().startActivity(whoThread, who.getBasePackageName(), intent, intent.resolveTypeIfNeeded(who.getContentResolver()), token, target != null ? target.mEmbeddedID : null, requestCode, 0, null, options);
            android.app.Instrumentation.checkStartActivityResult(result, intent);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Failure from system", e);
        }
        return null;
    }

    /**
     * Like {@link #execStartActivity(Context, IBinder, IBinder, Activity, Intent, int, Bundle)},
     * but accepts an array of activities to be started.  Note that active
     * {@link ActivityMonitor} objects only match against the first activity in
     * the array.
     *
     * {@hide }
     */
    public void execStartActivities(android.content.Context who, android.os.IBinder contextThread, android.os.IBinder token, android.app.Activity target, android.content.Intent[] intents, android.os.Bundle options) {
        execStartActivitiesAsUser(who, contextThread, token, target, intents, options, android.os.UserHandle.myUserId());
    }

    /**
     * Like {@link #execStartActivity(Context, IBinder, IBinder, Activity, Intent, int, Bundle)},
     * but accepts an array of activities to be started.  Note that active
     * {@link ActivityMonitor} objects only match against the first activity in
     * the array.
     *
     * {@hide }
     */
    public void execStartActivitiesAsUser(android.content.Context who, android.os.IBinder contextThread, android.os.IBinder token, android.app.Activity target, android.content.Intent[] intents, android.os.Bundle options, int userId) {
        android.app.IApplicationThread whoThread = ((android.app.IApplicationThread) (contextThread));
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    if (am.match(who, null, intents[0])) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return;
                        }
                        break;
                    }
                }
            }
        }
        try {
            java.lang.String[] resolvedTypes = new java.lang.String[intents.length];
            for (int i = 0; i < intents.length; i++) {
                intents[i].migrateExtraStreamToClipData();
                intents[i].prepareToLeaveProcess(who);
                resolvedTypes[i] = intents[i].resolveTypeIfNeeded(who.getContentResolver());
            }
            int result = android.app.ActivityManagerNative.getDefault().startActivities(whoThread, who.getBasePackageName(), intents, resolvedTypes, token, options, userId);
            android.app.Instrumentation.checkStartActivityResult(result, intents[0]);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Failure from system", e);
        }
    }

    /**
     * Like {@link #execStartActivity(android.content.Context, android.os.IBinder,
     * android.os.IBinder, String, android.content.Intent, int, android.os.Bundle)},
     * but for calls from a {#link Fragment}.
     *
     * @param who
     * 		The Context from which the activity is being started.
     * @param contextThread
     * 		The main thread of the Context from which the activity
     * 		is being started.
     * @param token
     * 		Internal token identifying to the system who is starting
     * 		the activity; may be null.
     * @param target
     * 		Which element is performing the start (and thus receiving
     * 		any result).
     * @param intent
     * 		The actual Intent to start.
     * @param requestCode
     * 		Identifier for this request's result; less than zero
     * 		if the caller is not expecting a result.
     * @return To force the return of a particular result, return an
    ActivityResult object containing the desired data; otherwise
    return null.  The default implementation always returns null.
     * @throws android.content.ActivityNotFoundException
     * 		
     * @see Activity#startActivity(Intent)
     * @see Activity#startActivityForResult(Intent, int)
     * @see Activity#startActivityFromChild

    {@hide }
     */
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context who, android.os.IBinder contextThread, android.os.IBinder token, java.lang.String target, android.content.Intent intent, int requestCode, android.os.Bundle options) {
        android.app.IApplicationThread whoThread = ((android.app.IApplicationThread) (contextThread));
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    if (am.match(who, null, intent)) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return requestCode >= 0 ? am.getResult() : null;
                        }
                        break;
                    }
                }
            }
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            int result = android.app.ActivityManagerNative.getDefault().startActivity(whoThread, who.getBasePackageName(), intent, intent.resolveTypeIfNeeded(who.getContentResolver()), token, target, requestCode, 0, null, options);
            android.app.Instrumentation.checkStartActivityResult(result, intent);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Failure from system", e);
        }
        return null;
    }

    /**
     * Like {@link #execStartActivity(Context, IBinder, IBinder, Activity, Intent, int, Bundle)},
     * but for starting as a particular user.
     *
     * @param who
     * 		The Context from which the activity is being started.
     * @param contextThread
     * 		The main thread of the Context from which the activity
     * 		is being started.
     * @param token
     * 		Internal token identifying to the system who is starting
     * 		the activity; may be null.
     * @param target
     * 		Which fragment is performing the start (and thus receiving
     * 		any result).
     * @param intent
     * 		The actual Intent to start.
     * @param requestCode
     * 		Identifier for this request's result; less than zero
     * 		if the caller is not expecting a result.
     * @return To force the return of a particular result, return an
    ActivityResult object containing the desired data; otherwise
    return null.  The default implementation always returns null.
     * @throws android.content.ActivityNotFoundException
     * 		
     * @see Activity#startActivity(Intent)
     * @see Activity#startActivityForResult(Intent, int)
     * @see Activity#startActivityFromChild

    {@hide }
     */
    public android.app.Instrumentation.ActivityResult execStartActivity(android.content.Context who, android.os.IBinder contextThread, android.os.IBinder token, android.app.Activity target, android.content.Intent intent, int requestCode, android.os.Bundle options, android.os.UserHandle user) {
        android.app.IApplicationThread whoThread = ((android.app.IApplicationThread) (contextThread));
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    if (am.match(who, null, intent)) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return requestCode >= 0 ? am.getResult() : null;
                        }
                        break;
                    }
                }
            }
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            int result = android.app.ActivityManagerNative.getDefault().startActivityAsUser(whoThread, who.getBasePackageName(), intent, intent.resolveTypeIfNeeded(who.getContentResolver()), token, target != null ? target.mEmbeddedID : null, requestCode, 0, null, options, user.getIdentifier());
            android.app.Instrumentation.checkStartActivityResult(result, intent);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Failure from system", e);
        }
        return null;
    }

    /**
     * Special version!
     *
     * @unknown 
     */
    public android.app.Instrumentation.ActivityResult execStartActivityAsCaller(android.content.Context who, android.os.IBinder contextThread, android.os.IBinder token, android.app.Activity target, android.content.Intent intent, int requestCode, android.os.Bundle options, boolean ignoreTargetSecurity, int userId) {
        android.app.IApplicationThread whoThread = ((android.app.IApplicationThread) (contextThread));
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    if (am.match(who, null, intent)) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return requestCode >= 0 ? am.getResult() : null;
                        }
                        break;
                    }
                }
            }
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            int result = android.app.ActivityManagerNative.getDefault().startActivityAsCaller(whoThread, who.getBasePackageName(), intent, intent.resolveTypeIfNeeded(who.getContentResolver()), token, target != null ? target.mEmbeddedID : null, requestCode, 0, null, options, ignoreTargetSecurity, userId);
            android.app.Instrumentation.checkStartActivityResult(result, intent);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Failure from system", e);
        }
        return null;
    }

    /**
     * Special version!
     *
     * @unknown 
     */
    public void execStartActivityFromAppTask(android.content.Context who, android.os.IBinder contextThread, android.app.IAppTask appTask, android.content.Intent intent, android.os.Bundle options) {
        android.app.IApplicationThread whoThread = ((android.app.IApplicationThread) (contextThread));
        if (mActivityMonitors != null) {
            synchronized(mSync) {
                final int N = mActivityMonitors.size();
                for (int i = 0; i < N; i++) {
                    final android.app.Instrumentation.ActivityMonitor am = mActivityMonitors.get(i);
                    if (am.match(who, null, intent)) {
                        am.mHits++;
                        if (am.isBlocking()) {
                            return;
                        }
                        break;
                    }
                }
            }
        }
        try {
            intent.migrateExtraStreamToClipData();
            intent.prepareToLeaveProcess(who);
            int result = appTask.startActivity(whoThread.asBinder(), who.getBasePackageName(), intent, intent.resolveTypeIfNeeded(who.getContentResolver()), options);
            android.app.Instrumentation.checkStartActivityResult(result, intent);
        } catch (android.os.RemoteException e) {
            throw new java.lang.RuntimeException("Failure from system", e);
        }
        return;
    }

    /* package */
    final void init(android.app.ActivityThread thread, android.content.Context instrContext, android.content.Context appContext, android.content.ComponentName component, android.app.IInstrumentationWatcher watcher, android.app.IUiAutomationConnection uiAutomationConnection) {
        mThread = thread;
        mMessageQueue = mThread.getLooper().myQueue();
        mInstrContext = instrContext;
        mAppContext = appContext;
        mComponent = component;
        mWatcher = watcher;
        mUiAutomationConnection = uiAutomationConnection;
    }

    /**
     *
     *
     * @unknown 
     */
    public static void checkStartActivityResult(int res, java.lang.Object intent) {
        if (res >= android.app.ActivityManager.START_SUCCESS) {
            return;
        }
        switch (res) {
            case android.app.ActivityManager.START_INTENT_NOT_RESOLVED :
            case android.app.ActivityManager.START_CLASS_NOT_FOUND :
                if ((intent instanceof android.content.Intent) && (((android.content.Intent) (intent)).getComponent() != null))
                    throw new android.content.ActivityNotFoundException(("Unable to find explicit activity class " + ((android.content.Intent) (intent)).getComponent().toShortString()) + "; have you declared this activity in your AndroidManifest.xml?");

                throw new android.content.ActivityNotFoundException("No Activity found to handle " + intent);
            case android.app.ActivityManager.START_PERMISSION_DENIED :
                throw new java.lang.SecurityException("Not allowed to start activity " + intent);
            case android.app.ActivityManager.START_FORWARD_AND_REQUEST_CONFLICT :
                throw new android.util.AndroidRuntimeException("FORWARD_RESULT_FLAG used while also requesting a result");
            case android.app.ActivityManager.START_NOT_ACTIVITY :
                throw new java.lang.IllegalArgumentException("PendingIntent is not an activity");
            case android.app.ActivityManager.START_NOT_VOICE_COMPATIBLE :
                throw new java.lang.SecurityException("Starting under voice control not allowed for: " + intent);
            case android.app.ActivityManager.START_VOICE_NOT_ACTIVE_SESSION :
                throw new java.lang.IllegalStateException("Session calling startVoiceActivity does not match active session");
            case android.app.ActivityManager.START_VOICE_HIDDEN_SESSION :
                throw new java.lang.IllegalStateException("Cannot start voice activity on a hidden session");
            case android.app.ActivityManager.START_CANCELED :
                throw new android.util.AndroidRuntimeException("Activity could not be started for " + intent);
            default :
                throw new android.util.AndroidRuntimeException((("Unknown error code " + res) + " when starting ") + intent);
        }
    }

    private final void validateNotAppThread() {
        if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
            throw new java.lang.RuntimeException("This method can not be called from the main application thread");
        }
    }

    /**
     * Gets the {@link UiAutomation} instance with no flags set.
     * <p>
     * <strong>Note:</strong> The APIs exposed via the returned {@link UiAutomation}
     * work across application boundaries while the APIs exposed by the instrumentation
     * do not. For example, {@link Instrumentation#sendPointerSync(MotionEvent)} will
     * not allow you to inject the event in an app different from the instrumentation
     * target, while {@link UiAutomation#injectInputEvent(android.view.InputEvent, boolean)}
     * will work regardless of the current application.
     * </p>
     * <p>
     * A typical test case should be using either the {@link UiAutomation} or
     * {@link Instrumentation} APIs. Using both APIs at the same time is not
     * a mistake by itself but a client has to be aware of the APIs limitations.
     * </p>
     * <p>
     * Equivalent to {@code getUiAutomation(0)}. If a {@link UiAutomation} exists with different
     * flags, the flags on that instance will be changed, and then it will be returned.
     * </p>
     *
     * @return The UI automation instance.
     * @see UiAutomation
     */
    public android.app.UiAutomation getUiAutomation() {
        return getUiAutomation(0);
    }

    /**
     * Gets the {@link UiAutomation} instance with flags set.
     * <p>
     * <strong>Note:</strong> The APIs exposed via the returned {@link UiAutomation}
     * work across application boundaries while the APIs exposed by the instrumentation
     * do not. For example, {@link Instrumentation#sendPointerSync(MotionEvent)} will
     * not allow you to inject the event in an app different from the instrumentation
     * target, while {@link UiAutomation#injectInputEvent(android.view.InputEvent, boolean)}
     * will work regardless of the current application.
     * </p>
     * <p>
     * A typical test case should be using either the {@link UiAutomation} or
     * {@link Instrumentation} APIs. Using both APIs at the same time is not
     * a mistake by itself but a client has to be aware of the APIs limitations.
     * </p>
     * <p>
     * If a {@link UiAutomation} exists with different flags, the flags on that instance will be
     * changed, and then it will be returned.
     * </p>
     *
     * @param flags
     * 		The flags to be passed to the UiAutomation, for example
     * 		{@link UiAutomation#FLAG_DONT_SUPPRESS_ACCESSIBILITY_SERVICES}.
     * @return The UI automation instance.
     * @see UiAutomation
     */
    public android.app.UiAutomation getUiAutomation(@android.app.Instrumentation.UiAutomationFlags
    int flags) {
        boolean mustCreateNewAutomation = (mUiAutomation == null) || mUiAutomation.isDestroyed();
        if (mUiAutomationConnection != null) {
            if ((!mustCreateNewAutomation) && (mUiAutomation.getFlags() == flags)) {
                return mUiAutomation;
            }
            if (mustCreateNewAutomation) {
                mUiAutomation = new android.app.UiAutomation(getTargetContext().getMainLooper(), mUiAutomationConnection);
            } else {
                mUiAutomation.disconnect();
            }
            mUiAutomation.connect(flags);
            return mUiAutomation;
        }
        return null;
    }

    private final class InstrumentationThread extends java.lang.Thread {
        public InstrumentationThread(java.lang.String name) {
            super(name);
        }

        public void run() {
            try {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_DISPLAY);
            } catch (java.lang.RuntimeException e) {
                android.util.Log.w(android.app.Instrumentation.TAG, "Exception setting priority of instrumentation thread " + android.os.Process.myTid(), e);
            }
            if (mAutomaticPerformanceSnapshots) {
                startPerformanceSnapshot();
            }
            onStart();
        }
    }

    private static final class EmptyRunnable implements java.lang.Runnable {
        public void run() {
        }
    }

    private static final class SyncRunnable implements java.lang.Runnable {
        private final java.lang.Runnable mTarget;

        private boolean mComplete;

        public SyncRunnable(java.lang.Runnable target) {
            mTarget = target;
        }

        public void run() {
            mTarget.run();
            synchronized(this) {
                mComplete = true;
                notifyAll();
            }
        }

        public void waitForComplete() {
            synchronized(this) {
                while (!mComplete) {
                    try {
                        wait();
                    } catch (java.lang.InterruptedException e) {
                    }
                } 
            }
        }
    }

    private static final class ActivityWaiter {
        public final android.content.Intent intent;

        public android.app.Activity activity;

        public ActivityWaiter(android.content.Intent _intent) {
            intent = _intent;
        }
    }

    private final class ActivityGoing implements android.os.MessageQueue.IdleHandler {
        private final android.app.Instrumentation.ActivityWaiter mWaiter;

        public ActivityGoing(android.app.Instrumentation.ActivityWaiter waiter) {
            mWaiter = waiter;
        }

        public final boolean queueIdle() {
            synchronized(mSync) {
                mWaitingActivities.remove(mWaiter);
                mSync.notifyAll();
            }
            return false;
        }
    }

    private static final class Idler implements android.os.MessageQueue.IdleHandler {
        private final java.lang.Runnable mCallback;

        private boolean mIdle;

        public Idler(java.lang.Runnable callback) {
            mCallback = callback;
            mIdle = false;
        }

        public final boolean queueIdle() {
            if (mCallback != null) {
                mCallback.run();
            }
            synchronized(this) {
                mIdle = true;
                notifyAll();
            }
            return false;
        }

        public void waitForIdle() {
            synchronized(this) {
                while (!mIdle) {
                    try {
                        wait();
                    } catch (java.lang.InterruptedException e) {
                    }
                } 
            }
        }
    }
}

