/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.widget;


/**
 * <p>
 * This class represents a data model for choosing a component for handing a
 * given {@link Intent}. The model is responsible for querying the system for
 * activities that can handle the given intent and order found activities
 * based on historical data of previous choices. The historical data is stored
 * in an application private file. If a client does not want to have persistent
 * choice history the file can be omitted, thus the activities will be ordered
 * based on historical usage for the current session.
 * <p>
 * </p>
 * For each backing history file there is a singleton instance of this class. Thus,
 * several clients that specify the same history file will share the same model. Note
 * that if multiple clients are sharing the same model they should implement semantically
 * equivalent functionality since setting the model intent will change the found
 * activities and they may be inconsistent with the functionality of some of the clients.
 * For example, choosing a share activity can be implemented by a single backing
 * model and two different views for performing the selection. If however, one of the
 * views is used for sharing but the other for importing, for example, then each
 * view should be backed by a separate model.
 * </p>
 * <p>
 * The way clients interact with this class is as follows:
 * </p>
 * <p>
 * <pre>
 * <code>
 *  // Get a model and set it to a couple of clients with semantically similar function.
 *  ActivityChooserModel dataModel =
 *      ActivityChooserModel.get(context, "task_specific_history_file_name.xml");
 *
 *  ActivityChooserModelClient modelClient1 = getActivityChooserModelClient1();
 *  modelClient1.setActivityChooserModel(dataModel);
 *
 *  ActivityChooserModelClient modelClient2 = getActivityChooserModelClient2();
 *  modelClient2.setActivityChooserModel(dataModel);
 *
 *  // Set an intent to choose a an activity for.
 *  dataModel.setIntent(intent);
 * <pre>
 * <code>
 * </p>
 * <p>
 * <strong>Note:</strong> This class is thread safe.
 * </p>
 *
 * @unknown 
 */
public class ActivityChooserModel extends android.database.DataSetObservable {
    /**
     * Client that utilizes an {@link ActivityChooserModel}.
     */
    public interface ActivityChooserModelClient {
        /**
         * Sets the {@link ActivityChooserModel}.
         *
         * @param dataModel
         * 		The model.
         */
        public void setActivityChooserModel(android.widget.ActivityChooserModel dataModel);
    }

    /**
     * Defines a sorter that is responsible for sorting the activities
     * based on the provided historical choices and an intent.
     */
    public interface ActivitySorter {
        /**
         * Sorts the <code>activities</code> in descending order of relevance
         * based on previous history and an intent.
         *
         * @param intent
         * 		The {@link Intent}.
         * @param activities
         * 		Activities to be sorted.
         * @param historicalRecords
         * 		Historical records.
         */
        // This cannot be done by a simple comparator since an Activity weight
        // is computed from history. Note that Activity implements Comparable.
        public void sort(android.content.Intent intent, java.util.List<android.widget.ActivityChooserModel.ActivityResolveInfo> activities, java.util.List<android.widget.ActivityChooserModel.HistoricalRecord> historicalRecords);
    }

    /**
     * Listener for choosing an activity.
     */
    public interface OnChooseActivityListener {
        /**
         * Called when an activity has been chosen. The client can decide whether
         * an activity can be chosen and if so the caller of
         * {@link ActivityChooserModel#chooseActivity(int)} will receive and {@link Intent}
         * for launching it.
         * <p>
         * <strong>Note:</strong> Modifying the intent is not permitted and
         *     any changes to the latter will be ignored.
         * </p>
         *
         * @param host
         * 		The listener's host model.
         * @param intent
         * 		The intent for launching the chosen activity.
         * @return Whether the intent is handled and should not be delivered to clients.
         * @see ActivityChooserModel#chooseActivity(int)
         */
        public boolean onChooseActivity(android.widget.ActivityChooserModel host, android.content.Intent intent);
    }

    /**
     * Flag for selecting debug mode.
     */
    private static final boolean DEBUG = false;

    /**
     * Tag used for logging.
     */
    private static final java.lang.String LOG_TAG = android.widget.ActivityChooserModel.class.getSimpleName();

    /**
     * The root tag in the history file.
     */
    private static final java.lang.String TAG_HISTORICAL_RECORDS = "historical-records";

    /**
     * The tag for a record in the history file.
     */
    private static final java.lang.String TAG_HISTORICAL_RECORD = "historical-record";

    /**
     * Attribute for the activity.
     */
    private static final java.lang.String ATTRIBUTE_ACTIVITY = "activity";

    /**
     * Attribute for the choice time.
     */
    private static final java.lang.String ATTRIBUTE_TIME = "time";

    /**
     * Attribute for the choice weight.
     */
    private static final java.lang.String ATTRIBUTE_WEIGHT = "weight";

    /**
     * The default name of the choice history file.
     */
    public static final java.lang.String DEFAULT_HISTORY_FILE_NAME = "activity_choser_model_history.xml";

    /**
     * The default maximal length of the choice history.
     */
    public static final int DEFAULT_HISTORY_MAX_LENGTH = 50;

    /**
     * The amount with which to inflate a chosen activity when set as default.
     */
    private static final int DEFAULT_ACTIVITY_INFLATION = 5;

    /**
     * Default weight for a choice record.
     */
    private static final float DEFAULT_HISTORICAL_RECORD_WEIGHT = 1.0F;

    /**
     * The extension of the history file.
     */
    private static final java.lang.String HISTORY_FILE_EXTENSION = ".xml";

    /**
     * An invalid item index.
     */
    private static final int INVALID_INDEX = -1;

    /**
     * Lock to guard the model registry.
     */
    private static final java.lang.Object sRegistryLock = new java.lang.Object();

    /**
     * This the registry for data models.
     */
    private static final java.util.Map<java.lang.String, android.widget.ActivityChooserModel> sDataModelRegistry = new java.util.HashMap<java.lang.String, android.widget.ActivityChooserModel>();

    /**
     * Lock for synchronizing on this instance.
     */
    private final java.lang.Object mInstanceLock = new java.lang.Object();

    /**
     * List of activities that can handle the current intent.
     */
    private final java.util.List<android.widget.ActivityChooserModel.ActivityResolveInfo> mActivities = new java.util.ArrayList<android.widget.ActivityChooserModel.ActivityResolveInfo>();

    /**
     * List with historical choice records.
     */
    private final java.util.List<android.widget.ActivityChooserModel.HistoricalRecord> mHistoricalRecords = new java.util.ArrayList<android.widget.ActivityChooserModel.HistoricalRecord>();

    /**
     * Monitor for added and removed packages.
     */
    private final com.android.internal.content.PackageMonitor mPackageMonitor = new android.widget.ActivityChooserModel.DataModelPackageMonitor();

    /**
     * Context for accessing resources.
     */
    private final android.content.Context mContext;

    /**
     * The name of the history file that backs this model.
     */
    private final java.lang.String mHistoryFileName;

    /**
     * The intent for which a activity is being chosen.
     */
    private android.content.Intent mIntent;

    /**
     * The sorter for ordering activities based on intent and past choices.
     */
    private android.widget.ActivityChooserModel.ActivitySorter mActivitySorter = new android.widget.ActivityChooserModel.DefaultSorter();

    /**
     * The maximal length of the choice history.
     */
    private int mHistoryMaxSize = android.widget.ActivityChooserModel.DEFAULT_HISTORY_MAX_LENGTH;

    /**
     * Flag whether choice history can be read. In general many clients can
     * share the same data model and {@link #readHistoricalDataIfNeeded()} may be called
     * by arbitrary of them any number of times. Therefore, this class guarantees
     * that the very first read succeeds and subsequent reads can be performed
     * only after a call to {@link #persistHistoricalDataIfNeeded()} followed by change
     * of the share records.
     */
    private boolean mCanReadHistoricalData = true;

    /**
     * Flag whether the choice history was read. This is used to enforce that
     * before calling {@link #persistHistoricalDataIfNeeded()} a call to
     * {@link #persistHistoricalDataIfNeeded()} has been made. This aims to avoid a
     * scenario in which a choice history file exits, it is not read yet and
     * it is overwritten. Note that always all historical records are read in
     * full and the file is rewritten. This is necessary since we need to
     * purge old records that are outside of the sliding window of past choices.
     */
    private boolean mReadShareHistoryCalled = false;

    /**
     * Flag whether the choice records have changed. In general many clients can
     * share the same data model and {@link #persistHistoricalDataIfNeeded()} may be called
     * by arbitrary of them any number of times. Therefore, this class guarantees
     * that choice history will be persisted only if it has changed.
     */
    private boolean mHistoricalRecordsChanged = true;

    /**
     * Flag whether to reload the activities for the current intent.
     */
    private boolean mReloadActivities = false;

    /**
     * Policy for controlling how the model handles chosen activities.
     */
    private android.widget.ActivityChooserModel.OnChooseActivityListener mActivityChoserModelPolicy;

    /**
     * Gets the data model backed by the contents of the provided file with historical data.
     * Note that only one data model is backed by a given file, thus multiple calls with
     * the same file name will return the same model instance. If no such instance is present
     * it is created.
     * <p>
     * <strong>Note:</strong> To use the default historical data file clients should explicitly
     * pass as file name {@link #DEFAULT_HISTORY_FILE_NAME}. If no persistence of the choice
     * history is desired clients should pass <code>null</code> for the file name. In such
     * case a new model is returned for each invocation.
     * </p>
     *
     * <p>
     * <strong>Always use difference historical data files for semantically different actions.
     * For example, sharing is different from importing.</strong>
     * </p>
     *
     * @param context
     * 		Context for loading resources.
     * @param historyFileName
     * 		File name with choice history, <code>null</code>
     * 		if the model should not be backed by a file. In this case the activities
     * 		will be ordered only by data from the current session.
     * @return The model.
     */
    @android.annotation.UnsupportedAppUsage
    public static android.widget.ActivityChooserModel get(android.content.Context context, java.lang.String historyFileName) {
        synchronized(android.widget.ActivityChooserModel.sRegistryLock) {
            android.widget.ActivityChooserModel dataModel = android.widget.ActivityChooserModel.sDataModelRegistry.get(historyFileName);
            if (dataModel == null) {
                dataModel = new android.widget.ActivityChooserModel(context, historyFileName);
                android.widget.ActivityChooserModel.sDataModelRegistry.put(historyFileName, dataModel);
            }
            return dataModel;
        }
    }

    /**
     * Creates a new instance.
     *
     * @param context
     * 		Context for loading resources.
     * @param historyFileName
     * 		The history XML file.
     */
    private ActivityChooserModel(android.content.Context context, java.lang.String historyFileName) {
        mContext = context.getApplicationContext();
        if ((!android.text.TextUtils.isEmpty(historyFileName)) && (!historyFileName.endsWith(android.widget.ActivityChooserModel.HISTORY_FILE_EXTENSION))) {
            mHistoryFileName = historyFileName + android.widget.ActivityChooserModel.HISTORY_FILE_EXTENSION;
        } else {
            mHistoryFileName = historyFileName;
        }
        mPackageMonitor.register(mContext, null, true);
    }

    /**
     * Sets an intent for which to choose a activity.
     * <p>
     * <strong>Note:</strong> Clients must set only semantically similar
     * intents for each data model.
     * <p>
     *
     * @param intent
     * 		The intent.
     */
    @android.annotation.UnsupportedAppUsage
    public void setIntent(android.content.Intent intent) {
        synchronized(mInstanceLock) {
            if (mIntent == intent) {
                return;
            }
            mIntent = intent;
            mReloadActivities = true;
            ensureConsistentState();
        }
    }

    /**
     * Gets the intent for which a activity is being chosen.
     *
     * @return The intent.
     */
    public android.content.Intent getIntent() {
        synchronized(mInstanceLock) {
            return mIntent;
        }
    }

    /**
     * Gets the number of activities that can handle the intent.
     *
     * @return The activity count.
     * @see #setIntent(Intent)
     */
    @android.annotation.UnsupportedAppUsage
    public int getActivityCount() {
        synchronized(mInstanceLock) {
            ensureConsistentState();
            return mActivities.size();
        }
    }

    /**
     * Gets an activity at a given index.
     *
     * @return The activity.
     * @see ActivityResolveInfo
     * @see #setIntent(Intent)
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.pm.ResolveInfo getActivity(int index) {
        synchronized(mInstanceLock) {
            ensureConsistentState();
            return mActivities.get(index).resolveInfo;
        }
    }

    /**
     * Gets the index of a the given activity.
     *
     * @param activity
     * 		The activity index.
     * @return The index if found, -1 otherwise.
     */
    public int getActivityIndex(android.content.pm.ResolveInfo activity) {
        synchronized(mInstanceLock) {
            ensureConsistentState();
            java.util.List<android.widget.ActivityChooserModel.ActivityResolveInfo> activities = mActivities;
            final int activityCount = activities.size();
            for (int i = 0; i < activityCount; i++) {
                android.widget.ActivityChooserModel.ActivityResolveInfo currentActivity = activities.get(i);
                if (currentActivity.resolveInfo == activity) {
                    return i;
                }
            }
            return android.widget.ActivityChooserModel.INVALID_INDEX;
        }
    }

    /**
     * Chooses a activity to handle the current intent. This will result in
     * adding a historical record for that action and construct intent with
     * its component name set such that it can be immediately started by the
     * client.
     * <p>
     * <strong>Note:</strong> By calling this method the client guarantees
     * that the returned intent will be started. This intent is returned to
     * the client solely to let additional customization before the start.
     * </p>
     *
     * @return An {@link Intent} for launching the activity or null if the
    policy has consumed the intent or there is not current intent
    set via {@link #setIntent(Intent)}.
     * @see HistoricalRecord
     * @see OnChooseActivityListener
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.Intent chooseActivity(int index) {
        synchronized(mInstanceLock) {
            if (mIntent == null) {
                return null;
            }
            ensureConsistentState();
            android.widget.ActivityChooserModel.ActivityResolveInfo chosenActivity = mActivities.get(index);
            android.content.ComponentName chosenName = new android.content.ComponentName(chosenActivity.resolveInfo.activityInfo.packageName, chosenActivity.resolveInfo.activityInfo.name);
            android.content.Intent choiceIntent = new android.content.Intent(mIntent);
            choiceIntent.setComponent(chosenName);
            if (mActivityChoserModelPolicy != null) {
                // Do not allow the policy to change the intent.
                android.content.Intent choiceIntentCopy = new android.content.Intent(choiceIntent);
                final boolean handled = mActivityChoserModelPolicy.onChooseActivity(this, choiceIntentCopy);
                if (handled) {
                    return null;
                }
            }
            android.widget.ActivityChooserModel.HistoricalRecord historicalRecord = new android.widget.ActivityChooserModel.HistoricalRecord(chosenName, java.lang.System.currentTimeMillis(), android.widget.ActivityChooserModel.DEFAULT_HISTORICAL_RECORD_WEIGHT);
            addHisoricalRecord(historicalRecord);
            return choiceIntent;
        }
    }

    /**
     * Sets the listener for choosing an activity.
     *
     * @param listener
     * 		The listener.
     */
    @android.annotation.UnsupportedAppUsage
    public void setOnChooseActivityListener(android.widget.ActivityChooserModel.OnChooseActivityListener listener) {
        synchronized(mInstanceLock) {
            mActivityChoserModelPolicy = listener;
        }
    }

    /**
     * Gets the default activity, The default activity is defined as the one
     * with highest rank i.e. the first one in the list of activities that can
     * handle the intent.
     *
     * @return The default activity, <code>null</code> id not activities.
     * @see #getActivity(int)
     */
    public android.content.pm.ResolveInfo getDefaultActivity() {
        synchronized(mInstanceLock) {
            ensureConsistentState();
            if (!mActivities.isEmpty()) {
                return mActivities.get(0).resolveInfo;
            }
        }
        return null;
    }

    /**
     * Sets the default activity. The default activity is set by adding a
     * historical record with weight high enough that this activity will
     * become the highest ranked. Such a strategy guarantees that the default
     * will eventually change if not used. Also the weight of the record for
     * setting a default is inflated with a constant amount to guarantee that
     * it will stay as default for awhile.
     *
     * @param index
     * 		The index of the activity to set as default.
     */
    public void setDefaultActivity(int index) {
        synchronized(mInstanceLock) {
            ensureConsistentState();
            android.widget.ActivityChooserModel.ActivityResolveInfo newDefaultActivity = mActivities.get(index);
            android.widget.ActivityChooserModel.ActivityResolveInfo oldDefaultActivity = mActivities.get(0);
            final float weight;
            if (oldDefaultActivity != null) {
                // Add a record with weight enough to boost the chosen at the top.
                weight = (oldDefaultActivity.weight - newDefaultActivity.weight) + android.widget.ActivityChooserModel.DEFAULT_ACTIVITY_INFLATION;
            } else {
                weight = android.widget.ActivityChooserModel.DEFAULT_HISTORICAL_RECORD_WEIGHT;
            }
            android.content.ComponentName defaultName = new android.content.ComponentName(newDefaultActivity.resolveInfo.activityInfo.packageName, newDefaultActivity.resolveInfo.activityInfo.name);
            android.widget.ActivityChooserModel.HistoricalRecord historicalRecord = new android.widget.ActivityChooserModel.HistoricalRecord(defaultName, java.lang.System.currentTimeMillis(), weight);
            addHisoricalRecord(historicalRecord);
        }
    }

    /**
     * Persists the history data to the backing file if the latter
     * was provided. Calling this method before a call to {@link #readHistoricalDataIfNeeded()}
     * throws an exception. Calling this method more than one without choosing an
     * activity has not effect.
     *
     * @throws IllegalStateException
     * 		If this method is called before a call to
     * 		{@link #readHistoricalDataIfNeeded()}.
     */
    private void persistHistoricalDataIfNeeded() {
        if (!mReadShareHistoryCalled) {
            throw new java.lang.IllegalStateException("No preceding call to #readHistoricalData");
        }
        if (!mHistoricalRecordsChanged) {
            return;
        }
        mHistoricalRecordsChanged = false;
        if (!android.text.TextUtils.isEmpty(mHistoryFileName)) {
            new android.widget.ActivityChooserModel.PersistHistoryAsyncTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new java.util.ArrayList<android.widget.ActivityChooserModel.HistoricalRecord>(mHistoricalRecords), mHistoryFileName);
        }
    }

    /**
     * Sets the sorter for ordering activities based on historical data and an intent.
     *
     * @param activitySorter
     * 		The sorter.
     * @see ActivitySorter
     */
    public void setActivitySorter(android.widget.ActivityChooserModel.ActivitySorter activitySorter) {
        synchronized(mInstanceLock) {
            if (mActivitySorter == activitySorter) {
                return;
            }
            mActivitySorter = activitySorter;
            if (sortActivitiesIfNeeded()) {
                notifyChanged();
            }
        }
    }

    /**
     * Sets the maximal size of the historical data. Defaults to
     * {@link #DEFAULT_HISTORY_MAX_LENGTH}
     * <p>
     *   <strong>Note:</strong> Setting this property will immediately
     *   enforce the specified max history size by dropping enough old
     *   historical records to enforce the desired size. Thus, any
     *   records that exceed the history size will be discarded and
     *   irreversibly lost.
     * </p>
     *
     * @param historyMaxSize
     * 		The max history size.
     */
    public void setHistoryMaxSize(int historyMaxSize) {
        synchronized(mInstanceLock) {
            if (mHistoryMaxSize == historyMaxSize) {
                return;
            }
            mHistoryMaxSize = historyMaxSize;
            pruneExcessiveHistoricalRecordsIfNeeded();
            if (sortActivitiesIfNeeded()) {
                notifyChanged();
            }
        }
    }

    /**
     * Gets the history max size.
     *
     * @return The history max size.
     */
    public int getHistoryMaxSize() {
        synchronized(mInstanceLock) {
            return mHistoryMaxSize;
        }
    }

    /**
     * Gets the history size.
     *
     * @return The history size.
     */
    public int getHistorySize() {
        synchronized(mInstanceLock) {
            ensureConsistentState();
            return mHistoricalRecords.size();
        }
    }

    @java.lang.Override
    protected void finalize() throws java.lang.Throwable {
        super.finalize();
        mPackageMonitor.unregister();
    }

    /**
     * Ensures the model is in a consistent state which is the
     * activities for the current intent have been loaded, the
     * most recent history has been read, and the activities
     * are sorted.
     */
    private void ensureConsistentState() {
        boolean stateChanged = loadActivitiesIfNeeded();
        stateChanged |= readHistoricalDataIfNeeded();
        pruneExcessiveHistoricalRecordsIfNeeded();
        if (stateChanged) {
            sortActivitiesIfNeeded();
            notifyChanged();
        }
    }

    /**
     * Sorts the activities if necessary which is if there is a
     * sorter, there are some activities to sort, and there is some
     * historical data.
     *
     * @return Whether sorting was performed.
     */
    private boolean sortActivitiesIfNeeded() {
        if ((((mActivitySorter != null) && (mIntent != null)) && (!mActivities.isEmpty())) && (!mHistoricalRecords.isEmpty())) {
            mActivitySorter.sort(mIntent, mActivities, java.util.Collections.unmodifiableList(mHistoricalRecords));
            return true;
        }
        return false;
    }

    /**
     * Loads the activities for the current intent if needed which is
     * if they are not already loaded for the current intent.
     *
     * @return Whether loading was performed.
     */
    private boolean loadActivitiesIfNeeded() {
        if (mReloadActivities && (mIntent != null)) {
            mReloadActivities = false;
            mActivities.clear();
            java.util.List<android.content.pm.ResolveInfo> resolveInfos = mContext.getPackageManager().queryIntentActivities(mIntent, 0);
            final int resolveInfoCount = resolveInfos.size();
            for (int i = 0; i < resolveInfoCount; i++) {
                android.content.pm.ResolveInfo resolveInfo = resolveInfos.get(i);
                android.content.pm.ActivityInfo activityInfo = resolveInfo.activityInfo;
                if (android.app.ActivityManager.checkComponentPermission(activityInfo.permission, android.widget.android.os.Process.myUid(), activityInfo.applicationInfo.uid, activityInfo.exported) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
                    mActivities.add(new android.widget.ActivityChooserModel.ActivityResolveInfo(resolveInfo));
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Reads the historical data if necessary which is it has
     * changed, there is a history file, and there is not persist
     * in progress.
     *
     * @return Whether reading was performed.
     */
    private boolean readHistoricalDataIfNeeded() {
        if ((mCanReadHistoricalData && mHistoricalRecordsChanged) && (!android.text.TextUtils.isEmpty(mHistoryFileName))) {
            mCanReadHistoricalData = false;
            mReadShareHistoryCalled = true;
            readHistoricalDataImpl();
            return true;
        }
        return false;
    }

    /**
     * Adds a historical record.
     *
     * @param historicalRecord
     * 		The record to add.
     * @return True if the record was added.
     */
    private boolean addHisoricalRecord(android.widget.ActivityChooserModel.HistoricalRecord historicalRecord) {
        final boolean added = mHistoricalRecords.add(historicalRecord);
        if (added) {
            mHistoricalRecordsChanged = true;
            pruneExcessiveHistoricalRecordsIfNeeded();
            persistHistoricalDataIfNeeded();
            sortActivitiesIfNeeded();
            notifyChanged();
        }
        return added;
    }

    /**
     * Prunes older excessive records to guarantee maxHistorySize.
     */
    private void pruneExcessiveHistoricalRecordsIfNeeded() {
        final int pruneCount = mHistoricalRecords.size() - mHistoryMaxSize;
        if (pruneCount <= 0) {
            return;
        }
        mHistoricalRecordsChanged = true;
        for (int i = 0; i < pruneCount; i++) {
            android.widget.ActivityChooserModel.HistoricalRecord prunedRecord = mHistoricalRecords.remove(0);
            if (android.widget.ActivityChooserModel.DEBUG) {
                android.util.Log.i(android.widget.ActivityChooserModel.LOG_TAG, "Pruned: " + prunedRecord);
            }
        }
    }

    /**
     * Represents a record in the history.
     */
    public static final class HistoricalRecord {
        /**
         * The activity name.
         */
        public final android.content.ComponentName activity;

        /**
         * The choice time.
         */
        public final long time;

        /**
         * The record weight.
         */
        public final float weight;

        /**
         * Creates a new instance.
         *
         * @param activityName
         * 		The activity component name flattened to string.
         * @param time
         * 		The time the activity was chosen.
         * @param weight
         * 		The weight of the record.
         */
        public HistoricalRecord(java.lang.String activityName, long time, float weight) {
            this(android.content.ComponentName.unflattenFromString(activityName), time, weight);
        }

        /**
         * Creates a new instance.
         *
         * @param activityName
         * 		The activity name.
         * @param time
         * 		The time the activity was chosen.
         * @param weight
         * 		The weight of the record.
         */
        public HistoricalRecord(android.content.ComponentName activityName, long time, float weight) {
            this.activity = activityName;
            this.time = time;
            this.weight = weight;
        }

        @java.lang.Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = (prime * result) + (activity == null ? 0 : activity.hashCode());
            result = (prime * result) + ((int) (time ^ (time >>> 32)));
            result = (prime * result) + java.lang.Float.floatToIntBits(weight);
            return result;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            android.widget.ActivityChooserModel.HistoricalRecord other = ((android.widget.ActivityChooserModel.HistoricalRecord) (obj));
            if (activity == null) {
                if (other.activity != null) {
                    return false;
                }
            } else
                if (!activity.equals(other.activity)) {
                    return false;
                }

            if (time != other.time) {
                return false;
            }
            if (java.lang.Float.floatToIntBits(weight) != java.lang.Float.floatToIntBits(other.weight)) {
                return false;
            }
            return true;
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("[");
            builder.append("; activity:").append(activity);
            builder.append("; time:").append(time);
            builder.append("; weight:").append(new java.math.BigDecimal(weight));
            builder.append("]");
            return builder.toString();
        }
    }

    /**
     * Represents an activity.
     */
    public final class ActivityResolveInfo implements java.lang.Comparable<android.widget.ActivityChooserModel.ActivityResolveInfo> {
        /**
         * The {@link ResolveInfo} of the activity.
         */
        public final android.content.pm.ResolveInfo resolveInfo;

        /**
         * Weight of the activity. Useful for sorting.
         */
        public float weight;

        /**
         * Creates a new instance.
         *
         * @param resolveInfo
         * 		activity {@link ResolveInfo}.
         */
        public ActivityResolveInfo(android.content.pm.ResolveInfo resolveInfo) {
            this.resolveInfo = resolveInfo;
        }

        @java.lang.Override
        public int hashCode() {
            return 31 + java.lang.Float.floatToIntBits(weight);
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            android.widget.ActivityChooserModel.ActivityResolveInfo other = ((android.widget.ActivityChooserModel.ActivityResolveInfo) (obj));
            if (java.lang.Float.floatToIntBits(weight) != java.lang.Float.floatToIntBits(other.weight)) {
                return false;
            }
            return true;
        }

        public int compareTo(android.widget.ActivityChooserModel.ActivityResolveInfo another) {
            return java.lang.Float.floatToIntBits(another.weight) - java.lang.Float.floatToIntBits(weight);
        }

        @java.lang.Override
        public java.lang.String toString() {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            builder.append("[");
            builder.append("resolveInfo:").append(resolveInfo.toString());
            builder.append("; weight:").append(new java.math.BigDecimal(weight));
            builder.append("]");
            return builder.toString();
        }
    }

    /**
     * Default activity sorter implementation.
     */
    private final class DefaultSorter implements android.widget.ActivityChooserModel.ActivitySorter {
        private static final float WEIGHT_DECAY_COEFFICIENT = 0.95F;

        private final java.util.Map<android.content.ComponentName, android.widget.ActivityChooserModel.ActivityResolveInfo> mPackageNameToActivityMap = new java.util.HashMap<android.content.ComponentName, android.widget.ActivityChooserModel.ActivityResolveInfo>();

        public void sort(android.content.Intent intent, java.util.List<android.widget.ActivityChooserModel.ActivityResolveInfo> activities, java.util.List<android.widget.ActivityChooserModel.HistoricalRecord> historicalRecords) {
            java.util.Map<android.content.ComponentName, android.widget.ActivityChooserModel.ActivityResolveInfo> componentNameToActivityMap = mPackageNameToActivityMap;
            componentNameToActivityMap.clear();
            final int activityCount = activities.size();
            for (int i = 0; i < activityCount; i++) {
                android.widget.ActivityChooserModel.ActivityResolveInfo activity = activities.get(i);
                activity.weight = 0.0F;
                android.content.ComponentName componentName = new android.content.ComponentName(activity.resolveInfo.activityInfo.packageName, activity.resolveInfo.activityInfo.name);
                componentNameToActivityMap.put(componentName, activity);
            }
            final int lastShareIndex = historicalRecords.size() - 1;
            float nextRecordWeight = 1;
            for (int i = lastShareIndex; i >= 0; i--) {
                android.widget.ActivityChooserModel.HistoricalRecord historicalRecord = historicalRecords.get(i);
                android.content.ComponentName componentName = historicalRecord.activity;
                android.widget.ActivityChooserModel.ActivityResolveInfo activity = componentNameToActivityMap.get(componentName);
                if (activity != null) {
                    activity.weight += historicalRecord.weight * nextRecordWeight;
                    nextRecordWeight = nextRecordWeight * android.widget.ActivityChooserModel.DefaultSorter.WEIGHT_DECAY_COEFFICIENT;
                }
            }
            java.util.Collections.sort(activities);
            if (android.widget.ActivityChooserModel.DEBUG) {
                for (int i = 0; i < activityCount; i++) {
                    android.util.Log.i(android.widget.ActivityChooserModel.LOG_TAG, "Sorted: " + activities.get(i));
                }
            }
        }
    }

    private void readHistoricalDataImpl() {
        java.io.FileInputStream fis = null;
        try {
            fis = mContext.openFileInput(mHistoryFileName);
        } catch (java.io.FileNotFoundException fnfe) {
            if (android.widget.ActivityChooserModel.DEBUG) {
                android.util.Log.i(android.widget.ActivityChooserModel.LOG_TAG, "Could not open historical records file: " + mHistoryFileName);
            }
            return;
        }
        try {
            org.xmlpull.v1.XmlPullParser parser = android.util.Xml.newPullParser();
            parser.setInput(fis, java.nio.charset.StandardCharsets.UTF_8.name());
            int type = org.xmlpull.v1.XmlPullParser.START_DOCUMENT;
            while ((type != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
                type = parser.next();
            } 
            if (!android.widget.ActivityChooserModel.TAG_HISTORICAL_RECORDS.equals(parser.getName())) {
                throw new org.xmlpull.v1.XmlPullParserException(("Share records file does not start with " + android.widget.ActivityChooserModel.TAG_HISTORICAL_RECORDS) + " tag.");
            }
            java.util.List<android.widget.ActivityChooserModel.HistoricalRecord> historicalRecords = mHistoricalRecords;
            historicalRecords.clear();
            while (true) {
                type = parser.next();
                if (type == org.xmlpull.v1.XmlPullParser.END_DOCUMENT) {
                    break;
                }
                if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                    continue;
                }
                java.lang.String nodeName = parser.getName();
                if (!android.widget.ActivityChooserModel.TAG_HISTORICAL_RECORD.equals(nodeName)) {
                    throw new org.xmlpull.v1.XmlPullParserException("Share records file not well-formed.");
                }
                java.lang.String activity = parser.getAttributeValue(null, android.widget.ActivityChooserModel.ATTRIBUTE_ACTIVITY);
                final long time = java.lang.Long.parseLong(parser.getAttributeValue(null, android.widget.ActivityChooserModel.ATTRIBUTE_TIME));
                final float weight = java.lang.Float.parseFloat(parser.getAttributeValue(null, android.widget.ActivityChooserModel.ATTRIBUTE_WEIGHT));
                android.widget.ActivityChooserModel.HistoricalRecord readRecord = new android.widget.ActivityChooserModel.HistoricalRecord(activity, time, weight);
                historicalRecords.add(readRecord);
                if (android.widget.ActivityChooserModel.DEBUG) {
                    android.util.Log.i(android.widget.ActivityChooserModel.LOG_TAG, "Read " + readRecord.toString());
                }
            } 
            if (android.widget.ActivityChooserModel.DEBUG) {
                android.util.Log.i(android.widget.ActivityChooserModel.LOG_TAG, ("Read " + historicalRecords.size()) + " historical records.");
            }
        } catch (org.xmlpull.v1.XmlPullParserException xppe) {
            android.util.Log.e(android.widget.ActivityChooserModel.LOG_TAG, "Error reading historical recrod file: " + mHistoryFileName, xppe);
        } catch (java.io.IOException ioe) {
            android.util.Log.e(android.widget.ActivityChooserModel.LOG_TAG, "Error reading historical recrod file: " + mHistoryFileName, ioe);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (java.io.IOException ioe) {
                    /* ignore */
                }
            }
        }
    }

    /**
     * Command for persisting the historical records to a file off the UI thread.
     */
    private final class PersistHistoryAsyncTask extends android.os.AsyncTask<java.lang.Object, java.lang.Void, java.lang.Void> {
        @java.lang.Override
        @java.lang.SuppressWarnings("unchecked")
        public java.lang.Void doInBackground(java.lang.Object... args) {
            java.util.List<android.widget.ActivityChooserModel.HistoricalRecord> historicalRecords = ((java.util.List<android.widget.ActivityChooserModel.HistoricalRecord>) (args[0]));
            java.lang.String hostoryFileName = ((java.lang.String) (args[1]));
            java.io.FileOutputStream fos = null;
            try {
                fos = mContext.openFileOutput(hostoryFileName, android.content.Context.MODE_PRIVATE);
            } catch (java.io.FileNotFoundException fnfe) {
                android.util.Log.e(android.widget.ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + hostoryFileName, fnfe);
                return null;
            }
            org.xmlpull.v1.XmlSerializer serializer = android.util.Xml.newSerializer();
            try {
                serializer.setOutput(fos, null);
                serializer.startDocument(java.nio.charset.StandardCharsets.UTF_8.name(), true);
                serializer.startTag(null, android.widget.ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                final int recordCount = historicalRecords.size();
                for (int i = 0; i < recordCount; i++) {
                    android.widget.ActivityChooserModel.HistoricalRecord record = historicalRecords.remove(0);
                    serializer.startTag(null, android.widget.ActivityChooserModel.TAG_HISTORICAL_RECORD);
                    serializer.attribute(null, android.widget.ActivityChooserModel.ATTRIBUTE_ACTIVITY, record.activity.flattenToString());
                    serializer.attribute(null, android.widget.ActivityChooserModel.ATTRIBUTE_TIME, java.lang.String.valueOf(record.time));
                    serializer.attribute(null, android.widget.ActivityChooserModel.ATTRIBUTE_WEIGHT, java.lang.String.valueOf(record.weight));
                    serializer.endTag(null, android.widget.ActivityChooserModel.TAG_HISTORICAL_RECORD);
                    if (android.widget.ActivityChooserModel.DEBUG) {
                        android.util.Log.i(android.widget.ActivityChooserModel.LOG_TAG, "Wrote " + record.toString());
                    }
                }
                serializer.endTag(null, android.widget.ActivityChooserModel.TAG_HISTORICAL_RECORDS);
                serializer.endDocument();
                if (android.widget.ActivityChooserModel.DEBUG) {
                    android.util.Log.i(android.widget.ActivityChooserModel.LOG_TAG, ("Wrote " + recordCount) + " historical records.");
                }
            } catch (java.lang.IllegalArgumentException iae) {
                android.util.Log.e(android.widget.ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + mHistoryFileName, iae);
            } catch (java.lang.IllegalStateException ise) {
                android.util.Log.e(android.widget.ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + mHistoryFileName, ise);
            } catch (java.io.IOException ioe) {
                android.util.Log.e(android.widget.ActivityChooserModel.LOG_TAG, "Error writing historical recrod file: " + mHistoryFileName, ioe);
            } finally {
                mCanReadHistoricalData = true;
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (java.io.IOException e) {
                        /* ignore */
                    }
                }
            }
            return null;
        }
    }

    /**
     * Keeps in sync the historical records and activities with the installed applications.
     */
    private final class DataModelPackageMonitor extends com.android.internal.content.PackageMonitor {
        @java.lang.Override
        public void onSomePackagesChanged() {
            mReloadActivities = true;
        }
    }
}

