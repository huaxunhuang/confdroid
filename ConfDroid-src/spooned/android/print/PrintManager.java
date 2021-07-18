/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.print;


/**
 * System level service for accessing the printing capabilities of the platform.
 * <p>
 * To obtain a handle to the print manager do the following:
 * </p>
 *
 * <pre>
 * PrintManager printManager =
 *         (PrintManager) context.getSystemService(Context.PRINT_SERVICE);
 * </pre>
 *
 * <h3>Print mechanics</h3>
 * <p>
 * The key idea behind printing on the platform is that the content to be printed
 * should be laid out for the currently selected print options resulting in an
 * optimized output and higher user satisfaction. To achieve this goal the platform
 * declares a contract that the printing application has to follow which is defined
 * by the {@link PrintDocumentAdapter} class. At a higher level the contract is that
 * when the user selects some options from the print UI that may affect the way
 * content is laid out, for example page size, the application receives a callback
 * allowing it to layout the content to better fit these new constraints. After a
 * layout pass the system may ask the application to render one or more pages one
 * or more times. For example, an application may produce a single column list for
 * smaller page sizes and a multi-column table for larger page sizes.
 * </p>
 * <h3>Print jobs</h3>
 * <p>
 * Print jobs are started by calling the {@link #print(String, PrintDocumentAdapter,
 * PrintAttributes)} from an activity which results in bringing up the system print
 * UI. Once the print UI is up, when the user changes a selected print option that
 * affects the way content is laid out the system starts to interact with the
 * application following the mechanics described the section above.
 * </p>
 * <p>
 * Print jobs can be in {@link PrintJobInfo#STATE_CREATED created}, {@link PrintJobInfo#STATE_QUEUED queued}, {@link PrintJobInfo#STATE_STARTED started},
 * {@link PrintJobInfo#STATE_BLOCKED blocked}, {@link PrintJobInfo#STATE_COMPLETED
 * completed}, {@link PrintJobInfo#STATE_FAILED failed}, and {@link PrintJobInfo#STATE_CANCELED canceled} state. Print jobs are stored in dedicated
 * system spooler until they are handled which is they are cancelled or completed.
 * Active print jobs, ones that are not cancelled or completed, are considered failed
 * if the device reboots as the new boot may be after a very long time. The user may
 * choose to restart such print jobs. Once a print job is queued all relevant content
 * is stored in the system spooler and its lifecycle becomes detached from this of
 * the application that created it.
 * </p>
 * <p>
 * An applications can query the print spooler for current print jobs it created
 * but not print jobs created by other applications.
 * </p>
 *
 * @see PrintJob
 * @see PrintJobInfo
 */
public final class PrintManager {
    private static final java.lang.String LOG_TAG = "PrintManager";

    private static final boolean DEBUG = false;

    private static final int MSG_NOTIFY_PRINT_JOB_STATE_CHANGED = 1;

    private static final int MSG_NOTIFY_PRINT_SERVICES_CHANGED = 2;

    private static final int MSG_NOTIFY_PRINT_SERVICE_RECOMMENDATIONS_CHANGED = 3;

    /**
     * Package name of print spooler.
     *
     * @unknown 
     */
    public static final java.lang.String PRINT_SPOOLER_PACKAGE_NAME = "com.android.printspooler";

    /**
     * Select enabled services.
     * </p>
     *
     * @see #getPrintServices
     * @unknown 
     */
    public static final int ENABLED_SERVICES = 1 << 0;

    /**
     * Select disabled services.
     * </p>
     *
     * @see #getPrintServices
     * @unknown 
     */
    public static final int DISABLED_SERVICES = 1 << 1;

    /**
     * Select all services.
     * </p>
     *
     * @see #getPrintServices
     * @unknown 
     */
    public static final int ALL_SERVICES = android.print.PrintManager.ENABLED_SERVICES | android.print.PrintManager.DISABLED_SERVICES;

    /**
     * The action for launching the print dialog activity.
     *
     * @unknown 
     */
    public static final java.lang.String ACTION_PRINT_DIALOG = "android.print.PRINT_DIALOG";

    /**
     * Extra with the intent for starting the print dialog.
     * <p>
     * <strong>Type:</strong> {@link android.content.IntentSender}
     * </p>
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_PRINT_DIALOG_INTENT = "android.print.intent.extra.EXTRA_PRINT_DIALOG_INTENT";

    /**
     * Extra with a print job.
     * <p>
     * <strong>Type:</strong> {@link android.print.PrintJobInfo}
     * </p>
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_PRINT_JOB = "android.print.intent.extra.EXTRA_PRINT_JOB";

    /**
     * Extra with the print document adapter to be printed.
     * <p>
     * <strong>Type:</strong> {@link android.print.IPrintDocumentAdapter}
     * </p>
     *
     * @unknown 
     */
    public static final java.lang.String EXTRA_PRINT_DOCUMENT_ADAPTER = "android.print.intent.extra.EXTRA_PRINT_DOCUMENT_ADAPTER";

    /**
     *
     *
     * @unknown 
     */
    public static final int APP_ID_ANY = -2;

    private final android.content.Context mContext;

    private final android.print.IPrintManager mService;

    private final int mUserId;

    private final int mAppId;

    private final android.os.Handler mHandler;

    private java.util.Map<android.print.PrintManager.PrintJobStateChangeListener, android.print.PrintManager.PrintJobStateChangeListenerWrapper> mPrintJobStateChangeListeners;

    private java.util.Map<android.print.PrintManager.PrintServicesChangeListener, android.print.PrintManager.PrintServicesChangeListenerWrapper> mPrintServicesChangeListeners;

    private java.util.Map<android.print.PrintManager.PrintServiceRecommendationsChangeListener, android.print.PrintManager.PrintServiceRecommendationsChangeListenerWrapper> mPrintServiceRecommendationsChangeListeners;

    /**
     *
     *
     * @unknown 
     */
    public interface PrintJobStateChangeListener {
        /**
         * Callback notifying that a print job state changed.
         *
         * @param printJobId
         * 		The print job id.
         */
        public void onPrintJobStateChanged(android.print.PrintJobId printJobId);
    }

    /**
     *
     *
     * @unknown 
     */
    public interface PrintServicesChangeListener {
        /**
         * Callback notifying that the print services changed.
         */
        public void onPrintServicesChanged();
    }

    /**
     *
     *
     * @unknown 
     */
    public interface PrintServiceRecommendationsChangeListener {
        /**
         * Callback notifying that the print service recommendations changed.
         */
        void onPrintServiceRecommendationsChanged();
    }

    /**
     * Creates a new instance.
     *
     * @param context
     * 		The current context in which to operate.
     * @param service
     * 		The backing system service.
     * @param userId
     * 		The user id in which to operate.
     * @param appId
     * 		The application id in which to operate.
     * @unknown 
     */
    public PrintManager(android.content.Context context, android.print.IPrintManager service, int userId, int appId) {
        mContext = context;
        mService = service;
        mUserId = userId;
        mAppId = appId;
        mHandler = new android.os.Handler(context.getMainLooper(), null, false) {
            @java.lang.Override
            public void handleMessage(android.os.Message message) {
                switch (message.what) {
                    case android.print.PrintManager.MSG_NOTIFY_PRINT_JOB_STATE_CHANGED :
                        {
                            com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
                            android.print.PrintManager.PrintJobStateChangeListenerWrapper wrapper = ((android.print.PrintManager.PrintJobStateChangeListenerWrapper) (args.arg1));
                            android.print.PrintManager.PrintJobStateChangeListener listener = wrapper.getListener();
                            if (listener != null) {
                                android.print.PrintJobId printJobId = ((android.print.PrintJobId) (args.arg2));
                                listener.onPrintJobStateChanged(printJobId);
                            }
                            args.recycle();
                        }
                        break;
                    case android.print.PrintManager.MSG_NOTIFY_PRINT_SERVICES_CHANGED :
                        {
                            android.print.PrintManager.PrintServicesChangeListenerWrapper wrapper = ((android.print.PrintManager.PrintServicesChangeListenerWrapper) (message.obj));
                            android.print.PrintManager.PrintServicesChangeListener listener = wrapper.getListener();
                            if (listener != null) {
                                listener.onPrintServicesChanged();
                            }
                        }
                        break;
                    case android.print.PrintManager.MSG_NOTIFY_PRINT_SERVICE_RECOMMENDATIONS_CHANGED :
                        {
                            android.print.PrintManager.PrintServiceRecommendationsChangeListenerWrapper wrapper = ((android.print.PrintManager.PrintServiceRecommendationsChangeListenerWrapper) (message.obj));
                            android.print.PrintManager.PrintServiceRecommendationsChangeListener listener = wrapper.getListener();
                            if (listener != null) {
                                listener.onPrintServiceRecommendationsChanged();
                            }
                        }
                        break;
                }
            }
        };
    }

    /**
     * Creates an instance that can access all print jobs.
     *
     * @param userId
     * 		The user id for which to get all print jobs.
     * @return An instance if the caller has the permission to access all print
    jobs, null otherwise.
     * @unknown 
     */
    public android.print.PrintManager getGlobalPrintManagerForUser(int userId) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return null;
        }
        return new android.print.PrintManager(mContext, mService, userId, android.print.PrintManager.APP_ID_ANY);
    }

    android.print.PrintJobInfo getPrintJobInfo(android.print.PrintJobId printJobId) {
        try {
            return mService.getPrintJobInfo(printJobId, mAppId, mUserId);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Adds a listener for observing the state of print jobs.
     *
     * @param listener
     * 		The listener to add.
     * @unknown 
     */
    public void addPrintJobStateChangeListener(android.print.PrintManager.PrintJobStateChangeListener listener) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        if (mPrintJobStateChangeListeners == null) {
            mPrintJobStateChangeListeners = new android.util.ArrayMap<android.print.PrintManager.PrintJobStateChangeListener, android.print.PrintManager.PrintJobStateChangeListenerWrapper>();
        }
        android.print.PrintManager.PrintJobStateChangeListenerWrapper wrappedListener = new android.print.PrintManager.PrintJobStateChangeListenerWrapper(listener, mHandler);
        try {
            mService.addPrintJobStateChangeListener(wrappedListener, mAppId, mUserId);
            mPrintJobStateChangeListeners.put(listener, wrappedListener);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Removes a listener for observing the state of print jobs.
     *
     * @param listener
     * 		The listener to remove.
     * @unknown 
     */
    public void removePrintJobStateChangeListener(android.print.PrintManager.PrintJobStateChangeListener listener) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        if (mPrintJobStateChangeListeners == null) {
            return;
        }
        android.print.PrintManager.PrintJobStateChangeListenerWrapper wrappedListener = mPrintJobStateChangeListeners.remove(listener);
        if (wrappedListener == null) {
            return;
        }
        if (mPrintJobStateChangeListeners.isEmpty()) {
            mPrintJobStateChangeListeners = null;
        }
        wrappedListener.destroy();
        try {
            mService.removePrintJobStateChangeListener(wrappedListener, mUserId);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Gets a print job given its id.
     *
     * @param printJobId
     * 		The id of the print job.
     * @return The print job list.
     * @see PrintJob
     * @unknown 
     */
    public android.print.PrintJob getPrintJob(android.print.PrintJobId printJobId) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return null;
        }
        try {
            android.print.PrintJobInfo printJob = mService.getPrintJobInfo(printJobId, mAppId, mUserId);
            if (printJob != null) {
                return new android.print.PrintJob(printJob, this);
            }
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
        return null;
    }

    /**
     * Get the custom icon for a printer. If the icon is not cached, the icon is
     * requested asynchronously. Once it is available the printer is updated.
     *
     * @param printerId
     * 		the id of the printer the icon should be loaded for
     * @return the custom icon to be used for the printer or null if the icon is
    not yet available
     * @see android.print.PrinterInfo.Builder#setHasCustomPrinterIcon()
     * @unknown 
     */
    public android.graphics.drawable.Icon getCustomPrinterIcon(android.print.PrinterId printerId) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return null;
        }
        try {
            return mService.getCustomPrinterIcon(printerId, mUserId);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the print jobs for this application.
     *
     * @return The print job list.
     * @see PrintJob
     */
    @android.annotation.NonNull
    public java.util.List<android.print.PrintJob> getPrintJobs() {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return java.util.Collections.emptyList();
        }
        try {
            java.util.List<android.print.PrintJobInfo> printJobInfos = mService.getPrintJobInfos(mAppId, mUserId);
            if (printJobInfos == null) {
                return java.util.Collections.emptyList();
            }
            final int printJobCount = printJobInfos.size();
            java.util.List<android.print.PrintJob> printJobs = new java.util.ArrayList<android.print.PrintJob>(printJobCount);
            for (int i = 0; i < printJobCount; i++) {
                printJobs.add(new android.print.PrintJob(printJobInfos.get(i), this));
            }
            return printJobs;
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    void cancelPrintJob(android.print.PrintJobId printJobId) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        try {
            mService.cancelPrintJob(printJobId, mAppId, mUserId);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    void restartPrintJob(android.print.PrintJobId printJobId) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        try {
            mService.restartPrintJob(printJobId, mAppId, mUserId);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Creates a print job for printing a {@link PrintDocumentAdapter} with
     * default print attributes.
     * <p>
     * Calling this method brings the print UI allowing the user to customize
     * the print job and returns a {@link PrintJob} object without waiting for the
     * user to customize or confirm the print job. The returned print job instance
     * is in a {@link PrintJobInfo#STATE_CREATED created} state.
     * <p>
     * This method can be called only from an {@link Activity}. The rationale is that
     * printing from a service will create an inconsistent user experience as the print
     * UI would appear without any context.
     * </p>
     * <p>
     * Also the passed in {@link PrintDocumentAdapter} will be considered invalid if
     * your activity is finished. The rationale is that once the activity that
     * initiated printing is finished, the provided adapter may be in an inconsistent
     * state as it may depend on the UI presented by the activity.
     * </p>
     * <p>
     * The default print attributes are a hint to the system how the data is to
     * be printed. For example, a photo editor may look at the photo aspect ratio
     * to determine the default orientation and provide a hint whether the printing
     * should be in portrait or landscape. The system will do a best effort to
     * selected the hinted options in the print dialog, given the current printer
     * supports them.
     * </p>
     * <p>
     * <strong>Note:</strong> Calling this method will bring the print dialog and
     * the system will connect to the provided {@link PrintDocumentAdapter}. If a
     * configuration change occurs that you application does not handle, for example
     * a rotation change, the system will drop the connection to the adapter as the
     * activity has to be recreated and the old adapter may be invalid in this context,
     * hence a new adapter instance is required. As a consequence, if your activity
     * does not handle configuration changes (default behavior), you have to save the
     * state that you were printing and call this method again when your activity
     * is recreated.
     * </p>
     *
     * @param printJobName
     * 		A name for the new print job which is shown to the user.
     * @param documentAdapter
     * 		An adapter that emits the document to print.
     * @param attributes
     * 		The default print job attributes or <code>null</code>.
     * @return The created print job on success or null on failure.
     * @throws IllegalStateException
     * 		If not called from an {@link Activity}.
     * @throws IllegalArgumentException
     * 		If the print job name is empty or the
     * 		document adapter is null.
     * @see PrintJob
     */
    @android.annotation.NonNull
    public android.print.PrintJob print(@android.annotation.NonNull
    java.lang.String printJobName, @android.annotation.NonNull
    android.print.PrintDocumentAdapter documentAdapter, @android.annotation.Nullable
    android.print.PrintAttributes attributes) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return null;
        }
        if (!(mContext instanceof android.app.Activity)) {
            throw new java.lang.IllegalStateException("Can print only from an activity");
        }
        if (android.text.TextUtils.isEmpty(printJobName)) {
            throw new java.lang.IllegalArgumentException("printJobName cannot be empty");
        }
        if (documentAdapter == null) {
            throw new java.lang.IllegalArgumentException("documentAdapter cannot be null");
        }
        android.print.PrintManager.PrintDocumentAdapterDelegate delegate = new android.print.PrintManager.PrintDocumentAdapterDelegate(((android.app.Activity) (mContext)), documentAdapter);
        try {
            android.os.Bundle result = mService.print(printJobName, delegate, attributes, mContext.getPackageName(), mAppId, mUserId);
            if (result != null) {
                android.print.PrintJobInfo printJob = result.getParcelable(android.print.PrintManager.EXTRA_PRINT_JOB);
                android.content.IntentSender intent = result.getParcelable(android.print.PrintManager.EXTRA_PRINT_DIALOG_INTENT);
                if ((printJob == null) || (intent == null)) {
                    return null;
                }
                try {
                    mContext.startIntentSender(intent, null, 0, 0, 0);
                    return new android.print.PrintJob(printJob, this);
                } catch (android.content.IntentSender.SendIntentException sie) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "Couldn't start print job config activity.", sie);
                }
            }
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
        return null;
    }

    /**
     * Listen for changes to the installed and enabled print services.
     *
     * @param listener
     * 		the listener to add
     * @see android.print.PrintManager#getPrintServices
     */
    void addPrintServicesChangeListener(@android.annotation.NonNull
    android.print.PrintManager.PrintServicesChangeListener listener) {
        com.android.internal.util.Preconditions.checkNotNull(listener);
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        if (mPrintServicesChangeListeners == null) {
            mPrintServicesChangeListeners = new android.util.ArrayMap<>();
        }
        android.print.PrintManager.PrintServicesChangeListenerWrapper wrappedListener = new android.print.PrintManager.PrintServicesChangeListenerWrapper(listener, mHandler);
        try {
            mService.addPrintServicesChangeListener(wrappedListener, mUserId);
            mPrintServicesChangeListeners.put(listener, wrappedListener);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Stop listening for changes to the installed and enabled print services.
     *
     * @param listener
     * 		the listener to remove
     * @see android.print.PrintManager#getPrintServices
     */
    void removePrintServicesChangeListener(@android.annotation.NonNull
    android.print.PrintManager.PrintServicesChangeListener listener) {
        com.android.internal.util.Preconditions.checkNotNull(listener);
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        if (mPrintServicesChangeListeners == null) {
            return;
        }
        android.print.PrintManager.PrintServicesChangeListenerWrapper wrappedListener = mPrintServicesChangeListeners.remove(listener);
        if (wrappedListener == null) {
            return;
        }
        if (mPrintServicesChangeListeners.isEmpty()) {
            mPrintServicesChangeListeners = null;
        }
        wrappedListener.destroy();
        try {
            mService.removePrintServicesChangeListener(wrappedListener, mUserId);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error removing print services change listener", re);
        }
    }

    /**
     * Gets the list of print services, but does not register for updates. The user has to register
     * for updates by itself, or use {@link PrintServicesLoader}.
     *
     * @param selectionFlags
     * 		flags selecting which services to get. Either
     * 		{@link #ENABLED_SERVICES},{@link #DISABLED_SERVICES}, or both.
     * @return The print service list or an empty list.
     * @see #addPrintServicesChangeListener(PrintServicesChangeListener)
     * @see #removePrintServicesChangeListener(PrintServicesChangeListener)
     * @unknown 
     */
    @android.annotation.NonNull
    public java.util.List<android.printservice.PrintServiceInfo> getPrintServices(int selectionFlags) {
        com.android.internal.util.Preconditions.checkFlagsArgument(selectionFlags, android.print.PrintManager.ALL_SERVICES);
        try {
            java.util.List<android.printservice.PrintServiceInfo> services = mService.getPrintServices(selectionFlags, mUserId);
            if (services != null) {
                return services;
            }
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
        return java.util.Collections.emptyList();
    }

    /**
     * Listen for changes to the print service recommendations.
     *
     * @param listener
     * 		the listener to add
     * @see android.print.PrintManager#getPrintServiceRecommendations
     */
    void addPrintServiceRecommendationsChangeListener(@android.annotation.NonNull
    android.print.PrintManager.PrintServiceRecommendationsChangeListener listener) {
        com.android.internal.util.Preconditions.checkNotNull(listener);
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        if (mPrintServiceRecommendationsChangeListeners == null) {
            mPrintServiceRecommendationsChangeListeners = new android.util.ArrayMap<>();
        }
        android.print.PrintManager.PrintServiceRecommendationsChangeListenerWrapper wrappedListener = new android.print.PrintManager.PrintServiceRecommendationsChangeListenerWrapper(listener, mHandler);
        try {
            mService.addPrintServiceRecommendationsChangeListener(wrappedListener, mUserId);
            mPrintServiceRecommendationsChangeListeners.put(listener, wrappedListener);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Stop listening for changes to the print service recommendations.
     *
     * @param listener
     * 		the listener to remove
     * @see android.print.PrintManager#getPrintServiceRecommendations
     */
    void removePrintServiceRecommendationsChangeListener(@android.annotation.NonNull
    android.print.PrintManager.PrintServiceRecommendationsChangeListener listener) {
        com.android.internal.util.Preconditions.checkNotNull(listener);
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        if (mPrintServiceRecommendationsChangeListeners == null) {
            return;
        }
        android.print.PrintManager.PrintServiceRecommendationsChangeListenerWrapper wrappedListener = mPrintServiceRecommendationsChangeListeners.remove(listener);
        if (wrappedListener == null) {
            return;
        }
        if (mPrintServiceRecommendationsChangeListeners.isEmpty()) {
            mPrintServiceRecommendationsChangeListeners = null;
        }
        wrappedListener.destroy();
        try {
            mService.removePrintServiceRecommendationsChangeListener(wrappedListener, mUserId);
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
    }

    /**
     * Gets the list of print service recommendations, but does not register for updates. The user
     * has to register for updates by itself, or use {@link PrintServiceRecommendationsLoader}.
     *
     * @return The print service recommendations list or an empty list.
     * @see #addPrintServiceRecommendationsChangeListener
     * @see #removePrintServiceRecommendationsChangeListener
     * @unknown 
     */
    @android.annotation.NonNull
    public java.util.List<android.printservice.recommendation.RecommendationInfo> getPrintServiceRecommendations() {
        try {
            java.util.List<android.printservice.recommendation.RecommendationInfo> recommendations = mService.getPrintServiceRecommendations(mUserId);
            if (recommendations != null) {
                return recommendations;
            }
        } catch (android.os.RemoteException re) {
            throw re.rethrowFromSystemServer();
        }
        return java.util.Collections.emptyList();
    }

    /**
     *
     *
     * @unknown 
     */
    public android.print.PrinterDiscoverySession createPrinterDiscoverySession() {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return null;
        }
        return new android.print.PrinterDiscoverySession(mService, mContext, mUserId);
    }

    /**
     * Enable or disable a print service.
     *
     * @param service
     * 		The service to enabled or disable
     * @param isEnabled
     * 		whether the service should be enabled or disabled
     * @unknown 
     */
    public void setPrintServiceEnabled(@android.annotation.NonNull
    android.content.ComponentName service, boolean isEnabled) {
        if (mService == null) {
            android.util.Log.w(android.print.PrintManager.LOG_TAG, "Feature android.software.print not available");
            return;
        }
        try {
            mService.setPrintServiceEnabled(service, isEnabled, mUserId);
        } catch (android.os.RemoteException re) {
            android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error enabling or disabling " + service, re);
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class PrintDocumentAdapterDelegate extends android.print.IPrintDocumentAdapter.Stub implements android.app.Application.ActivityLifecycleCallbacks {
        private final java.lang.Object mLock = new java.lang.Object();

        private android.app.Activity mActivity;// Strong reference OK - cleared in destroy


        private android.print.PrintDocumentAdapter mDocumentAdapter;// Strong reference OK - cleared in destroy


        private android.os.Handler mHandler;// Strong reference OK - cleared in destroy


        private android.print.IPrintDocumentAdapterObserver mObserver;// Strong reference OK - cleared in destroy


        private android.print.PrintManager.PrintDocumentAdapterDelegate.DestroyableCallback mPendingCallback;

        public PrintDocumentAdapterDelegate(android.app.Activity activity, android.print.PrintDocumentAdapter documentAdapter) {
            if (activity.isFinishing()) {
                // The activity is already dead hence the onActivityDestroyed callback won't be
                // triggered. Hence it is not save to print in this situation.
                throw new java.lang.IllegalStateException("Cannot start printing for finishing activity");
            }
            mActivity = activity;
            mDocumentAdapter = documentAdapter;
            mHandler = new android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler(mActivity.getMainLooper());
            mActivity.getApplication().registerActivityLifecycleCallbacks(this);
        }

        @java.lang.Override
        public void setObserver(android.print.IPrintDocumentAdapterObserver observer) {
            final boolean destroyed;
            synchronized(mLock) {
                mObserver = observer;
                destroyed = isDestroyedLocked();
            }
            if (destroyed && (observer != null)) {
                try {
                    observer.onDestroy();
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error announcing destroyed state", re);
                }
            }
        }

        @java.lang.Override
        public void start() {
            synchronized(mLock) {
                // If destroyed the handler is null.
                if (!isDestroyedLocked()) {
                    mHandler.obtainMessage(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_START, mDocumentAdapter).sendToTarget();
                }
            }
        }

        @java.lang.Override
        public void layout(android.print.PrintAttributes oldAttributes, android.print.PrintAttributes newAttributes, android.print.ILayoutResultCallback callback, android.os.Bundle metadata, int sequence) {
            android.os.ICancellationSignal cancellationTransport = android.os.CancellationSignal.createTransport();
            try {
                callback.onLayoutStarted(cancellationTransport, sequence);
            } catch (android.os.RemoteException re) {
                // The spooler is dead - can't recover.
                android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error notifying for layout start", re);
                return;
            }
            synchronized(mLock) {
                // If destroyed the handler is null.
                if (isDestroyedLocked()) {
                    return;
                }
                android.os.CancellationSignal cancellationSignal = android.os.CancellationSignal.fromTransport(cancellationTransport);
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = mDocumentAdapter;
                args.arg2 = oldAttributes;
                args.arg3 = newAttributes;
                args.arg4 = cancellationSignal;
                args.arg5 = new android.print.PrintManager.PrintDocumentAdapterDelegate.MyLayoutResultCallback(callback, sequence);
                args.arg6 = metadata;
                mHandler.obtainMessage(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_LAYOUT, args).sendToTarget();
            }
        }

        @java.lang.Override
        public void write(android.print.PageRange[] pages, android.os.ParcelFileDescriptor fd, android.print.IWriteResultCallback callback, int sequence) {
            android.os.ICancellationSignal cancellationTransport = android.os.CancellationSignal.createTransport();
            try {
                callback.onWriteStarted(cancellationTransport, sequence);
            } catch (android.os.RemoteException re) {
                // The spooler is dead - can't recover.
                android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error notifying for write start", re);
                return;
            }
            synchronized(mLock) {
                // If destroyed the handler is null.
                if (isDestroyedLocked()) {
                    return;
                }
                android.os.CancellationSignal cancellationSignal = android.os.CancellationSignal.fromTransport(cancellationTransport);
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = mDocumentAdapter;
                args.arg2 = pages;
                args.arg3 = fd;
                args.arg4 = cancellationSignal;
                args.arg5 = new android.print.PrintManager.PrintDocumentAdapterDelegate.MyWriteResultCallback(callback, fd, sequence);
                mHandler.obtainMessage(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_WRITE, args).sendToTarget();
            }
        }

        @java.lang.Override
        public void finish() {
            synchronized(mLock) {
                // If destroyed the handler is null.
                if (!isDestroyedLocked()) {
                    mHandler.obtainMessage(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_FINISH, mDocumentAdapter).sendToTarget();
                }
            }
        }

        @java.lang.Override
        public void kill(java.lang.String reason) {
            synchronized(mLock) {
                // If destroyed the handler is null.
                if (!isDestroyedLocked()) {
                    mHandler.obtainMessage(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_KILL, reason).sendToTarget();
                }
            }
        }

        @java.lang.Override
        public void onActivityPaused(android.app.Activity activity) {
            /* do nothing */
        }

        @java.lang.Override
        public void onActivityCreated(android.app.Activity activity, android.os.Bundle savedInstanceState) {
            /* do nothing */
        }

        @java.lang.Override
        public void onActivityStarted(android.app.Activity activity) {
            /* do nothing */
        }

        @java.lang.Override
        public void onActivityResumed(android.app.Activity activity) {
            /* do nothing */
        }

        @java.lang.Override
        public void onActivityStopped(android.app.Activity activity) {
            /* do nothing */
        }

        @java.lang.Override
        public void onActivitySaveInstanceState(android.app.Activity activity, android.os.Bundle outState) {
            /* do nothing */
        }

        @java.lang.Override
        public void onActivityDestroyed(android.app.Activity activity) {
            // We really care only if the activity is being destroyed to
            // notify the the print spooler so it can close the print dialog.
            // Note the the spooler has a death recipient that observes if
            // this process gets killed so we cover the case of onDestroy not
            // being called due to this process being killed to reclaim memory.
            android.print.IPrintDocumentAdapterObserver observer = null;
            synchronized(mLock) {
                if (activity == mActivity) {
                    observer = mObserver;
                    destroyLocked();
                }
            }
            if (observer != null) {
                try {
                    observer.onDestroy();
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error announcing destroyed state", re);
                }
            }
        }

        private boolean isDestroyedLocked() {
            return mActivity == null;
        }

        private void destroyLocked() {
            mActivity.getApplication().unregisterActivityLifecycleCallbacks(this);
            mActivity = null;
            mDocumentAdapter = null;
            // This method is only called from the main thread, so
            // clearing the messages guarantees that any time a
            // message is handled we are not in a destroyed state.
            mHandler.removeMessages(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_START);
            mHandler.removeMessages(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_LAYOUT);
            mHandler.removeMessages(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_WRITE);
            mHandler.removeMessages(android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_FINISH);
            mHandler = null;
            mObserver = null;
            if (mPendingCallback != null) {
                mPendingCallback.destroy();
                mPendingCallback = null;
            }
        }

        private final class MyHandler extends android.os.Handler {
            public static final int MSG_ON_START = 1;

            public static final int MSG_ON_LAYOUT = 2;

            public static final int MSG_ON_WRITE = 3;

            public static final int MSG_ON_FINISH = 4;

            public static final int MSG_ON_KILL = 5;

            public MyHandler(android.os.Looper looper) {
                super(looper, null, true);
            }

            @java.lang.Override
            public void handleMessage(android.os.Message message) {
                switch (message.what) {
                    case android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_START :
                        {
                            if (android.print.PrintManager.DEBUG) {
                                android.util.Log.i(android.print.PrintManager.LOG_TAG, "onStart()");
                            }
                            ((android.print.PrintDocumentAdapter) (message.obj)).onStart();
                        }
                        break;
                    case android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_LAYOUT :
                        {
                            com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
                            android.print.PrintDocumentAdapter adapter = ((android.print.PrintDocumentAdapter) (args.arg1));
                            android.print.PrintAttributes oldAttributes = ((android.print.PrintAttributes) (args.arg2));
                            android.print.PrintAttributes newAttributes = ((android.print.PrintAttributes) (args.arg3));
                            android.os.CancellationSignal cancellation = ((android.os.CancellationSignal) (args.arg4));
                            android.print.PrintDocumentAdapter.LayoutResultCallback callback = ((android.print.PrintDocumentAdapter.LayoutResultCallback) (args.arg5));
                            android.os.Bundle metadata = ((android.os.Bundle) (args.arg6));
                            args.recycle();
                            if (android.print.PrintManager.DEBUG) {
                                java.lang.StringBuilder builder = new java.lang.StringBuilder();
                                builder.append("PrintDocumentAdapter#onLayout() {\n");
                                builder.append("\n  oldAttributes:").append(oldAttributes);
                                builder.append("\n  newAttributes:").append(newAttributes);
                                builder.append("\n  preview:").append(metadata.getBoolean(android.print.PrintDocumentAdapter.EXTRA_PRINT_PREVIEW));
                                builder.append("\n}");
                                android.util.Log.i(android.print.PrintManager.LOG_TAG, builder.toString());
                            }
                            adapter.onLayout(oldAttributes, newAttributes, cancellation, callback, metadata);
                        }
                        break;
                    case android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_WRITE :
                        {
                            com.android.internal.os.SomeArgs args = ((com.android.internal.os.SomeArgs) (message.obj));
                            android.print.PrintDocumentAdapter adapter = ((android.print.PrintDocumentAdapter) (args.arg1));
                            android.print.PageRange[] pages = ((android.print.PageRange[]) (args.arg2));
                            android.os.ParcelFileDescriptor fd = ((android.os.ParcelFileDescriptor) (args.arg3));
                            android.os.CancellationSignal cancellation = ((android.os.CancellationSignal) (args.arg4));
                            android.print.PrintDocumentAdapter.WriteResultCallback callback = ((android.print.PrintDocumentAdapter.WriteResultCallback) (args.arg5));
                            args.recycle();
                            if (android.print.PrintManager.DEBUG) {
                                java.lang.StringBuilder builder = new java.lang.StringBuilder();
                                builder.append("PrintDocumentAdapter#onWrite() {\n");
                                builder.append("\n  pages:").append(java.util.Arrays.toString(pages));
                                builder.append("\n}");
                                android.util.Log.i(android.print.PrintManager.LOG_TAG, builder.toString());
                            }
                            adapter.onWrite(pages, fd, cancellation, callback);
                        }
                        break;
                    case android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_FINISH :
                        {
                            if (android.print.PrintManager.DEBUG) {
                                android.util.Log.i(android.print.PrintManager.LOG_TAG, "onFinish()");
                            }
                            ((android.print.PrintDocumentAdapter) (message.obj)).onFinish();
                            // Done printing, so destroy this instance as it
                            // should not be used anymore.
                            synchronized(mLock) {
                                destroyLocked();
                            }
                        }
                        break;
                    case android.print.PrintManager.PrintDocumentAdapterDelegate.MyHandler.MSG_ON_KILL :
                        {
                            if (android.print.PrintManager.DEBUG) {
                                android.util.Log.i(android.print.PrintManager.LOG_TAG, "onKill()");
                            }
                            java.lang.String reason = ((java.lang.String) (message.obj));
                            throw new java.lang.RuntimeException(reason);
                        }
                    default :
                        {
                            throw new java.lang.IllegalArgumentException("Unknown message: " + message.what);
                        }
                }
            }
        }

        private interface DestroyableCallback {
            public void destroy();
        }

        private final class MyLayoutResultCallback extends android.print.PrintDocumentAdapter.LayoutResultCallback implements android.print.PrintManager.PrintDocumentAdapterDelegate.DestroyableCallback {
            private android.print.ILayoutResultCallback mCallback;

            private final int mSequence;

            public MyLayoutResultCallback(android.print.ILayoutResultCallback callback, int sequence) {
                mCallback = callback;
                mSequence = sequence;
            }

            @java.lang.Override
            public void onLayoutFinished(android.print.PrintDocumentInfo info, boolean changed) {
                final android.print.ILayoutResultCallback callback;
                synchronized(mLock) {
                    callback = mCallback;
                }
                // If the callback is null we are destroyed.
                if (callback == null) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "PrintDocumentAdapter is destroyed. Did you " + ("finish the printing activity before print completion " + "or did you invoke a callback after finish?"));
                    return;
                }
                try {
                    if (info == null) {
                        throw new java.lang.NullPointerException("document info cannot be null");
                    }
                    try {
                        callback.onLayoutFinished(info, changed, mSequence);
                    } catch (android.os.RemoteException re) {
                        android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error calling onLayoutFinished", re);
                    }
                } finally {
                    destroy();
                }
            }

            @java.lang.Override
            public void onLayoutFailed(java.lang.CharSequence error) {
                final android.print.ILayoutResultCallback callback;
                synchronized(mLock) {
                    callback = mCallback;
                }
                // If the callback is null we are destroyed.
                if (callback == null) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "PrintDocumentAdapter is destroyed. Did you " + ("finish the printing activity before print completion " + "or did you invoke a callback after finish?"));
                    return;
                }
                try {
                    callback.onLayoutFailed(error, mSequence);
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error calling onLayoutFailed", re);
                } finally {
                    destroy();
                }
            }

            @java.lang.Override
            public void onLayoutCancelled() {
                final android.print.ILayoutResultCallback callback;
                synchronized(mLock) {
                    callback = mCallback;
                }
                // If the callback is null we are destroyed.
                if (callback == null) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "PrintDocumentAdapter is destroyed. Did you " + ("finish the printing activity before print completion " + "or did you invoke a callback after finish?"));
                    return;
                }
                try {
                    callback.onLayoutCanceled(mSequence);
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error calling onLayoutFailed", re);
                } finally {
                    destroy();
                }
            }

            @java.lang.Override
            public void destroy() {
                synchronized(mLock) {
                    mCallback = null;
                    mPendingCallback = null;
                }
            }
        }

        private final class MyWriteResultCallback extends android.print.PrintDocumentAdapter.WriteResultCallback implements android.print.PrintManager.PrintDocumentAdapterDelegate.DestroyableCallback {
            private android.os.ParcelFileDescriptor mFd;

            private android.print.IWriteResultCallback mCallback;

            private final int mSequence;

            public MyWriteResultCallback(android.print.IWriteResultCallback callback, android.os.ParcelFileDescriptor fd, int sequence) {
                mFd = fd;
                mSequence = sequence;
                mCallback = callback;
            }

            @java.lang.Override
            public void onWriteFinished(android.print.PageRange[] pages) {
                final android.print.IWriteResultCallback callback;
                synchronized(mLock) {
                    callback = mCallback;
                }
                // If the callback is null we are destroyed.
                if (callback == null) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "PrintDocumentAdapter is destroyed. Did you " + ("finish the printing activity before print completion " + "or did you invoke a callback after finish?"));
                    return;
                }
                try {
                    if (pages == null) {
                        throw new java.lang.IllegalArgumentException("pages cannot be null");
                    }
                    if (pages.length == 0) {
                        throw new java.lang.IllegalArgumentException("pages cannot be empty");
                    }
                    try {
                        callback.onWriteFinished(pages, mSequence);
                    } catch (android.os.RemoteException re) {
                        android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error calling onWriteFinished", re);
                    }
                } finally {
                    destroy();
                }
            }

            @java.lang.Override
            public void onWriteFailed(java.lang.CharSequence error) {
                final android.print.IWriteResultCallback callback;
                synchronized(mLock) {
                    callback = mCallback;
                }
                // If the callback is null we are destroyed.
                if (callback == null) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "PrintDocumentAdapter is destroyed. Did you " + ("finish the printing activity before print completion " + "or did you invoke a callback after finish?"));
                    return;
                }
                try {
                    callback.onWriteFailed(error, mSequence);
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error calling onWriteFailed", re);
                } finally {
                    destroy();
                }
            }

            @java.lang.Override
            public void onWriteCancelled() {
                final android.print.IWriteResultCallback callback;
                synchronized(mLock) {
                    callback = mCallback;
                }
                // If the callback is null we are destroyed.
                if (callback == null) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "PrintDocumentAdapter is destroyed. Did you " + ("finish the printing activity before print completion " + "or did you invoke a callback after finish?"));
                    return;
                }
                try {
                    callback.onWriteCanceled(mSequence);
                } catch (android.os.RemoteException re) {
                    android.util.Log.e(android.print.PrintManager.LOG_TAG, "Error calling onWriteCanceled", re);
                } finally {
                    destroy();
                }
            }

            @java.lang.Override
            public void destroy() {
                synchronized(mLock) {
                    libcore.io.IoUtils.closeQuietly(mFd);
                    mCallback = null;
                    mFd = null;
                    mPendingCallback = null;
                }
            }
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class PrintJobStateChangeListenerWrapper extends android.print.IPrintJobStateChangeListener.Stub {
        private final java.lang.ref.WeakReference<android.print.PrintManager.PrintJobStateChangeListener> mWeakListener;

        private final java.lang.ref.WeakReference<android.os.Handler> mWeakHandler;

        public PrintJobStateChangeListenerWrapper(android.print.PrintManager.PrintJobStateChangeListener listener, android.os.Handler handler) {
            mWeakListener = new java.lang.ref.WeakReference<android.print.PrintManager.PrintJobStateChangeListener>(listener);
            mWeakHandler = new java.lang.ref.WeakReference<android.os.Handler>(handler);
        }

        @java.lang.Override
        public void onPrintJobStateChanged(android.print.PrintJobId printJobId) {
            android.os.Handler handler = mWeakHandler.get();
            android.print.PrintManager.PrintJobStateChangeListener listener = mWeakListener.get();
            if ((handler != null) && (listener != null)) {
                com.android.internal.os.SomeArgs args = com.android.internal.os.SomeArgs.obtain();
                args.arg1 = this;
                args.arg2 = printJobId;
                handler.obtainMessage(android.print.PrintManager.MSG_NOTIFY_PRINT_JOB_STATE_CHANGED, args).sendToTarget();
            }
        }

        public void destroy() {
            mWeakListener.clear();
        }

        public android.print.PrintManager.PrintJobStateChangeListener getListener() {
            return mWeakListener.get();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class PrintServicesChangeListenerWrapper extends android.print.IPrintServicesChangeListener.Stub {
        private final java.lang.ref.WeakReference<android.print.PrintManager.PrintServicesChangeListener> mWeakListener;

        private final java.lang.ref.WeakReference<android.os.Handler> mWeakHandler;

        public PrintServicesChangeListenerWrapper(android.print.PrintManager.PrintServicesChangeListener listener, android.os.Handler handler) {
            mWeakListener = new java.lang.ref.WeakReference<>(listener);
            mWeakHandler = new java.lang.ref.WeakReference<>(handler);
        }

        @java.lang.Override
        public void onPrintServicesChanged() {
            android.os.Handler handler = mWeakHandler.get();
            android.print.PrintManager.PrintServicesChangeListener listener = mWeakListener.get();
            if ((handler != null) && (listener != null)) {
                handler.obtainMessage(android.print.PrintManager.MSG_NOTIFY_PRINT_SERVICES_CHANGED, this).sendToTarget();
            }
        }

        public void destroy() {
            mWeakListener.clear();
        }

        public android.print.PrintManager.PrintServicesChangeListener getListener() {
            return mWeakListener.get();
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static final class PrintServiceRecommendationsChangeListenerWrapper extends android.printservice.recommendation.IRecommendationsChangeListener.Stub {
        private final java.lang.ref.WeakReference<android.print.PrintManager.PrintServiceRecommendationsChangeListener> mWeakListener;

        private final java.lang.ref.WeakReference<android.os.Handler> mWeakHandler;

        public PrintServiceRecommendationsChangeListenerWrapper(android.print.PrintManager.PrintServiceRecommendationsChangeListener listener, android.os.Handler handler) {
            mWeakListener = new java.lang.ref.WeakReference<>(listener);
            mWeakHandler = new java.lang.ref.WeakReference<>(handler);
        }

        @java.lang.Override
        public void onRecommendationsChanged() {
            android.os.Handler handler = mWeakHandler.get();
            android.print.PrintManager.PrintServiceRecommendationsChangeListener listener = mWeakListener.get();
            if ((handler != null) && (listener != null)) {
                handler.obtainMessage(android.print.PrintManager.MSG_NOTIFY_PRINT_SERVICE_RECOMMENDATIONS_CHANGED, this).sendToTarget();
            }
        }

        public void destroy() {
            mWeakListener.clear();
        }

        public android.print.PrintManager.PrintServiceRecommendationsChangeListener getListener() {
            return mWeakListener.get();
        }
    }
}

