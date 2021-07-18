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
package android.widget;


/**
 * <p>A filter constrains data with a filtering pattern.</p>
 *
 * <p>Filters are usually created by {@link android.widget.Filterable}
 * classes.</p>
 *
 * <p>Filtering operations performed by calling {@link #filter(CharSequence)} or
 * {@link #filter(CharSequence, android.widget.Filter.FilterListener)} are
 * performed asynchronously. When these methods are called, a filtering request
 * is posted in a request queue and processed later. Any call to one of these
 * methods will cancel any previous non-executed filtering request.</p>
 *
 * @see android.widget.Filterable
 */
public abstract class Filter {
    private static final java.lang.String LOG_TAG = "Filter";

    private static final java.lang.String THREAD_NAME = "Filter";

    private static final int FILTER_TOKEN = 0xd0d0f00d;

    private static final int FINISH_TOKEN = 0xdeadbeef;

    private android.os.Handler mThreadHandler;

    private android.os.Handler mResultHandler;

    private android.widget.Filter.Delayer mDelayer;

    private final java.lang.Object mLock = new java.lang.Object();

    /**
     * <p>Creates a new asynchronous filter.</p>
     */
    public Filter() {
        mResultHandler = new android.widget.Filter.ResultsHandler();
    }

    /**
     * Provide an interface that decides how long to delay the message for a given query.  Useful
     * for heuristics such as posting a delay for the delete key to avoid doing any work while the
     * user holds down the delete key.
     *
     * @param delayer
     * 		The delayer.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public void setDelayer(android.widget.Filter.Delayer delayer) {
        synchronized(mLock) {
            mDelayer = delayer;
        }
    }

    /**
     * <p>Starts an asynchronous filtering operation. Calling this method
     * cancels all previous non-executed filtering requests and posts a new
     * filtering request that will be executed later.</p>
     *
     * @param constraint
     * 		the constraint used to filter the data
     * @see #filter(CharSequence, android.widget.Filter.FilterListener)
     */
    public final void filter(java.lang.CharSequence constraint) {
        filter(constraint, null);
    }

    /**
     * <p>Starts an asynchronous filtering operation. Calling this method
     * cancels all previous non-executed filtering requests and posts a new
     * filtering request that will be executed later.</p>
     *
     * <p>Upon completion, the listener is notified.</p>
     *
     * @param constraint
     * 		the constraint used to filter the data
     * @param listener
     * 		a listener notified upon completion of the operation
     * @see #filter(CharSequence)
     * @see #performFiltering(CharSequence)
     * @see #publishResults(CharSequence, android.widget.Filter.FilterResults)
     */
    public final void filter(java.lang.CharSequence constraint, android.widget.Filter.FilterListener listener) {
        synchronized(mLock) {
            if (mThreadHandler == null) {
                android.os.HandlerThread thread = new android.os.HandlerThread(android.widget.Filter.THREAD_NAME, android.os.Process.THREAD_PRIORITY_BACKGROUND);
                thread.start();
                mThreadHandler = new android.widget.Filter.RequestHandler(thread.getLooper());
            }
            final long delay = (mDelayer == null) ? 0 : mDelayer.getPostingDelay(constraint);
            android.os.Message message = mThreadHandler.obtainMessage(android.widget.Filter.FILTER_TOKEN);
            android.widget.Filter.RequestArguments args = new android.widget.Filter.RequestArguments();
            // make sure we use an immutable copy of the constraint, so that
            // it doesn't change while the filter operation is in progress
            args.constraint = (constraint != null) ? constraint.toString() : null;
            args.listener = listener;
            message.obj = args;
            mThreadHandler.removeMessages(android.widget.Filter.FILTER_TOKEN);
            mThreadHandler.removeMessages(android.widget.Filter.FINISH_TOKEN);
            mThreadHandler.sendMessageDelayed(message, delay);
        }
    }

    /**
     * <p>Invoked in a worker thread to filter the data according to the
     * constraint. Subclasses must implement this method to perform the
     * filtering operation. Results computed by the filtering operation
     * must be returned as a {@link android.widget.Filter.FilterResults} that
     * will then be published in the UI thread through
     * {@link #publishResults(CharSequence,
     * android.widget.Filter.FilterResults)}.</p>
     *
     * <p><strong>Contract:</strong> When the constraint is null, the original
     * data must be restored.</p>
     *
     * @param constraint
     * 		the constraint used to filter the data
     * @return the results of the filtering operation
     * @see #filter(CharSequence, android.widget.Filter.FilterListener)
     * @see #publishResults(CharSequence, android.widget.Filter.FilterResults)
     * @see android.widget.Filter.FilterResults
     */
    protected abstract android.widget.Filter.FilterResults performFiltering(java.lang.CharSequence constraint);

    /**
     * <p>Invoked in the UI thread to publish the filtering results in the
     * user interface. Subclasses must implement this method to display the
     * results computed in {@link #performFiltering}.</p>
     *
     * @param constraint
     * 		the constraint used to filter the data
     * @param results
     * 		the results of the filtering operation
     * @see #filter(CharSequence, android.widget.Filter.FilterListener)
     * @see #performFiltering(CharSequence)
     * @see android.widget.Filter.FilterResults
     */
    protected abstract void publishResults(java.lang.CharSequence constraint, android.widget.Filter.FilterResults results);

    /**
     * <p>Converts a value from the filtered set into a CharSequence. Subclasses
     * should override this method to convert their results. The default
     * implementation returns an empty String for null values or the default
     * String representation of the value.</p>
     *
     * @param resultValue
     * 		the value to convert to a CharSequence
     * @return a CharSequence representing the value
     */
    public java.lang.CharSequence convertResultToString(java.lang.Object resultValue) {
        return resultValue == null ? "" : resultValue.toString();
    }

    /**
     * <p>Holds the results of a filtering operation. The results are the values
     * computed by the filtering operation and the number of these values.</p>
     */
    protected static class FilterResults {
        public FilterResults() {
            // nothing to see here
        }

        /**
         * <p>Contains all the values computed by the filtering operation.</p>
         */
        public java.lang.Object values;

        /**
         * <p>Contains the number of values computed by the filtering
         * operation.</p>
         */
        public int count;
    }

    /**
     * <p>Listener used to receive a notification upon completion of a filtering
     * operation.</p>
     */
    public static interface FilterListener {
        /**
         * <p>Notifies the end of a filtering operation.</p>
         *
         * @param count
         * 		the number of values computed by the filter
         */
        public void onFilterComplete(int count);
    }

    /**
     * <p>Worker thread handler. When a new filtering request is posted from
     * {@link android.widget.Filter#filter(CharSequence, android.widget.Filter.FilterListener)},
     * it is sent to this handler.</p>
     */
    private class RequestHandler extends android.os.Handler {
        public RequestHandler(android.os.Looper looper) {
            super(looper);
        }

        /**
         * <p>Handles filtering requests by calling
         * {@link Filter#performFiltering} and then sending a message
         * with the results to the results handler.</p>
         *
         * @param msg
         * 		the filtering request
         */
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            android.os.Message message;
            switch (what) {
                case android.widget.Filter.FILTER_TOKEN :
                    android.widget.Filter.RequestArguments args = ((android.widget.Filter.RequestArguments) (msg.obj));
                    try {
                        args.results = performFiltering(args.constraint);
                    } catch (java.lang.Exception e) {
                        args.results = new android.widget.Filter.FilterResults();
                        android.util.Log.w(android.widget.Filter.LOG_TAG, "An exception occured during performFiltering()!", e);
                    } finally {
                        message = mResultHandler.obtainMessage(what);
                        message.obj = args;
                        message.sendToTarget();
                    }
                    synchronized(mLock) {
                        if (mThreadHandler != null) {
                            android.os.Message finishMessage = mThreadHandler.obtainMessage(android.widget.Filter.FINISH_TOKEN);
                            mThreadHandler.sendMessageDelayed(finishMessage, 3000);
                        }
                    }
                    break;
                case android.widget.Filter.FINISH_TOKEN :
                    synchronized(mLock) {
                        if (mThreadHandler != null) {
                            mThreadHandler.getLooper().quit();
                            mThreadHandler = null;
                        }
                    }
                    break;
            }
        }
    }

    /**
     * <p>Handles the results of a filtering operation. The results are
     * handled in the UI thread.</p>
     */
    private class ResultsHandler extends android.os.Handler {
        /**
         * <p>Messages received from the request handler are processed in the
         * UI thread. The processing involves calling
         * {@link Filter#publishResults(CharSequence,
         * android.widget.Filter.FilterResults)}
         * to post the results back in the UI and then notifying the listener,
         * if any.</p>
         *
         * @param msg
         * 		the filtering results
         */
        @java.lang.Override
        public void handleMessage(android.os.Message msg) {
            android.widget.Filter.RequestArguments args = ((android.widget.Filter.RequestArguments) (msg.obj));
            publishResults(args.constraint, args.results);
            if (args.listener != null) {
                int count = (args.results != null) ? args.results.count : -1;
                args.listener.onFilterComplete(count);
            }
        }
    }

    /**
     * <p>Holds the arguments of a filtering request as well as the results
     * of the request.</p>
     */
    private static class RequestArguments {
        /**
         * <p>The constraint used to filter the data.</p>
         */
        java.lang.CharSequence constraint;

        /**
         * <p>The listener to notify upon completion. Can be null.</p>
         */
        android.widget.Filter.FilterListener listener;

        /**
         * <p>The results of the filtering operation.</p>
         */
        android.widget.Filter.FilterResults results;
    }

    /**
     *
     *
     * @unknown 
     */
    public interface Delayer {
        /**
         *
         *
         * @param constraint
         * 		The constraint passed to {@link Filter#filter(CharSequence)}
         * @return The delay that should be used for
        {@link Handler#sendMessageDelayed(android.os.Message, long)}
         */
        long getPostingDelay(java.lang.CharSequence constraint);
    }
}

