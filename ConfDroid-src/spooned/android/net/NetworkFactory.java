/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.net;


/**
 * A NetworkFactory is an entity that creates NetworkAgent objects.
 * The bearers register with ConnectivityService using {@link #register} and
 * their factory will start receiving scored NetworkRequests.  NetworkRequests
 * can be filtered 3 ways: by NetworkCapabilities, by score and more complexly by
 * overridden function.  All of these can be dynamic - changing NetworkCapabilities
 * or score forces re-evaluation of all current requests.
 *
 * If any requests pass the filter some overrideable functions will be called.
 * If the bearer only cares about very simple start/stopNetwork callbacks, those
 * functions can be overridden.  If the bearer needs more interaction, it can
 * override addNetworkRequest and removeNetworkRequest which will give it each
 * request that passes their current filters.
 *
 * @unknown 
 */
public class NetworkFactory extends android.os.Handler {
    private static final boolean DBG = true;

    private static final boolean VDBG = false;

    private static final int BASE = com.android.internal.util.Protocol.BASE_NETWORK_FACTORY;

    /**
     * Pass a network request to the bearer.  If the bearer believes it can
     * satisfy the request it should connect to the network and create a
     * NetworkAgent.  Once the NetworkAgent is fully functional it will
     * register itself with ConnectivityService using registerNetworkAgent.
     * If the bearer cannot immediately satisfy the request (no network,
     * user disabled the radio, lower-scored network) it should remember
     * any NetworkRequests it may be able to satisfy in the future.  It may
     * disregard any that it will never be able to service, for example
     * those requiring a different bearer.
     * msg.obj = NetworkRequest
     * msg.arg1 = score - the score of the any network currently satisfying this
     *            request.  If this bearer knows in advance it cannot
     *            exceed this score it should not try to connect, holding the request
     *            for the future.
     *            Note that subsequent events may give a different (lower
     *            or higher) score for this request, transmitted to each
     *            NetworkFactory through additional CMD_REQUEST_NETWORK msgs
     *            with the same NetworkRequest but an updated score.
     *            Also, network conditions may change for this bearer
     *            allowing for a better score in the future.
     */
    public static final int CMD_REQUEST_NETWORK = android.net.NetworkFactory.BASE;

    /**
     * Cancel a network request
     * msg.obj = NetworkRequest
     */
    public static final int CMD_CANCEL_REQUEST = android.net.NetworkFactory.BASE + 1;

    /**
     * Internally used to set our best-guess score.
     * msg.arg1 = new score
     */
    private static final int CMD_SET_SCORE = android.net.NetworkFactory.BASE + 2;

    /**
     * Internally used to set our current filter for coarse bandwidth changes with
     * technology changes.
     * msg.obj = new filter
     */
    private static final int CMD_SET_FILTER = android.net.NetworkFactory.BASE + 3;

    private final android.content.Context mContext;

    private final java.lang.String LOG_TAG;

    private final android.util.SparseArray<android.net.NetworkFactory.NetworkRequestInfo> mNetworkRequests = new android.util.SparseArray<android.net.NetworkFactory.NetworkRequestInfo>();

    private int mScore;

    private android.net.NetworkCapabilities mCapabilityFilter;

    private int mRefCount = 0;

    private android.os.Messenger mMessenger = null;

    public NetworkFactory(android.os.Looper looper, android.content.Context context, java.lang.String logTag, android.net.NetworkCapabilities filter) {
        super(looper);
        LOG_TAG = logTag;
        mContext = context;
        mCapabilityFilter = filter;
    }

    public void register() {
        if (android.net.NetworkFactory.DBG)
            log("Registering NetworkFactory");

        if (mMessenger == null) {
            mMessenger = new android.os.Messenger(this);
            android.net.ConnectivityManager.from(mContext).registerNetworkFactory(mMessenger, LOG_TAG);
        }
    }

    public void unregister() {
        if (android.net.NetworkFactory.DBG)
            log("Unregistering NetworkFactory");

        if (mMessenger != null) {
            android.net.ConnectivityManager.from(mContext).unregisterNetworkFactory(mMessenger);
            mMessenger = null;
        }
    }

    @java.lang.Override
    public void handleMessage(android.os.Message msg) {
        switch (msg.what) {
            case android.net.NetworkFactory.CMD_REQUEST_NETWORK :
                {
                    handleAddRequest(((android.net.NetworkRequest) (msg.obj)), msg.arg1);
                    break;
                }
            case android.net.NetworkFactory.CMD_CANCEL_REQUEST :
                {
                    handleRemoveRequest(((android.net.NetworkRequest) (msg.obj)));
                    break;
                }
            case android.net.NetworkFactory.CMD_SET_SCORE :
                {
                    handleSetScore(msg.arg1);
                    break;
                }
            case android.net.NetworkFactory.CMD_SET_FILTER :
                {
                    handleSetFilter(((android.net.NetworkCapabilities) (msg.obj)));
                    break;
                }
        }
    }

    private class NetworkRequestInfo {
        public final android.net.NetworkRequest request;

        public int score;

        public boolean requested;// do we have a request outstanding, limited by score


        public NetworkRequestInfo(android.net.NetworkRequest request, int score) {
            this.request = request;
            this.score = score;
            this.requested = false;
        }

        @java.lang.Override
        public java.lang.String toString() {
            return ((((("{" + request) + ", score=") + score) + ", requested=") + requested) + "}";
        }
    }

    @com.android.internal.annotations.VisibleForTesting
    protected void handleAddRequest(android.net.NetworkRequest request, int score) {
        android.net.NetworkFactory.NetworkRequestInfo n = mNetworkRequests.get(request.requestId);
        if (n == null) {
            if (android.net.NetworkFactory.DBG)
                log((("got request " + request) + " with score ") + score);

            n = new android.net.NetworkFactory.NetworkRequestInfo(request, score);
            mNetworkRequests.put(n.request.requestId, n);
        } else {
            if (android.net.NetworkFactory.VDBG)
                log((("new score " + score) + " for exisiting request ") + request);

            n.score = score;
        }
        if (android.net.NetworkFactory.VDBG)
            log((("  my score=" + mScore) + ", my filter=") + mCapabilityFilter);

        evalRequest(n);
    }

    @com.android.internal.annotations.VisibleForTesting
    protected void handleRemoveRequest(android.net.NetworkRequest request) {
        android.net.NetworkFactory.NetworkRequestInfo n = mNetworkRequests.get(request.requestId);
        if (n != null) {
            mNetworkRequests.remove(request.requestId);
            if (n.requested)
                releaseNetworkFor(n.request);

        }
    }

    private void handleSetScore(int score) {
        mScore = score;
        evalRequests();
    }

    private void handleSetFilter(android.net.NetworkCapabilities netCap) {
        mCapabilityFilter = netCap;
        evalRequests();
    }

    /**
     * Overridable function to provide complex filtering.
     * Called for every request every time a new NetworkRequest is seen
     * and whenever the filterScore or filterNetworkCapabilities change.
     *
     * acceptRequest can be overriden to provide complex filter behavior
     * for the incoming requests
     *
     * For output, this class will call {@link #needNetworkFor} and
     * {@link #releaseNetworkFor} for every request that passes the filters.
     * If you don't need to see every request, you can leave the base
     * implementations of those two functions and instead override
     * {@link #startNetwork} and {@link #stopNetwork}.
     *
     * If you want to see every score fluctuation on every request, set
     * your score filter to a very high number and watch {@link #needNetworkFor}.
     *
     * @return {@code true} to accept the request.
     */
    public boolean acceptRequest(android.net.NetworkRequest request, int score) {
        return true;
    }

    private void evalRequest(android.net.NetworkFactory.NetworkRequestInfo n) {
        if (android.net.NetworkFactory.VDBG)
            log("evalRequest");

        if ((((n.requested == false) && (n.score < mScore)) && n.request.networkCapabilities.satisfiedByNetworkCapabilities(mCapabilityFilter)) && acceptRequest(n.request, n.score)) {
            if (android.net.NetworkFactory.VDBG)
                log("  needNetworkFor");

            needNetworkFor(n.request, n.score);
            n.requested = true;
        } else
            if ((n.requested == true) && (((n.score > mScore) || (n.request.networkCapabilities.satisfiedByNetworkCapabilities(mCapabilityFilter) == false)) || (acceptRequest(n.request, n.score) == false))) {
                if (android.net.NetworkFactory.VDBG)
                    log("  releaseNetworkFor");

                releaseNetworkFor(n.request);
                n.requested = false;
            } else {
                if (android.net.NetworkFactory.VDBG)
                    log("  done");

            }

    }

    private void evalRequests() {
        for (int i = 0; i < mNetworkRequests.size(); i++) {
            android.net.NetworkFactory.NetworkRequestInfo n = mNetworkRequests.valueAt(i);
            evalRequest(n);
        }
    }

    // override to do simple mode (request independent)
    protected void startNetwork() {
    }

    protected void stopNetwork() {
    }

    // override to do fancier stuff
    protected void needNetworkFor(android.net.NetworkRequest networkRequest, int score) {
        if ((++mRefCount) == 1)
            startNetwork();

    }

    protected void releaseNetworkFor(android.net.NetworkRequest networkRequest) {
        if ((--mRefCount) == 0)
            stopNetwork();

    }

    public void addNetworkRequest(android.net.NetworkRequest networkRequest, int score) {
        sendMessage(obtainMessage(android.net.NetworkFactory.CMD_REQUEST_NETWORK, new android.net.NetworkFactory.NetworkRequestInfo(networkRequest, score)));
    }

    public void removeNetworkRequest(android.net.NetworkRequest networkRequest) {
        sendMessage(obtainMessage(android.net.NetworkFactory.CMD_CANCEL_REQUEST, networkRequest));
    }

    public void setScoreFilter(int score) {
        sendMessage(obtainMessage(android.net.NetworkFactory.CMD_SET_SCORE, score, 0));
    }

    public void setCapabilityFilter(android.net.NetworkCapabilities netCap) {
        sendMessage(obtainMessage(android.net.NetworkFactory.CMD_SET_FILTER, new android.net.NetworkCapabilities(netCap)));
    }

    @com.android.internal.annotations.VisibleForTesting
    protected int getRequestCount() {
        return mNetworkRequests.size();
    }

    protected void log(java.lang.String s) {
        android.util.Log.d(LOG_TAG, s);
    }

    public void dump(java.io.FileDescriptor fd, java.io.PrintWriter writer, java.lang.String[] args) {
        final com.android.internal.util.IndentingPrintWriter pw = new com.android.internal.util.IndentingPrintWriter(writer, "  ");
        pw.println(toString());
        pw.increaseIndent();
        for (int i = 0; i < mNetworkRequests.size(); i++) {
            pw.println(mNetworkRequests.valueAt(i));
        }
        pw.decreaseIndent();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder("{").append(LOG_TAG).append(" - ScoreFilter=").append(mScore).append(", Filter=").append(mCapabilityFilter).append(", requests=").append(mNetworkRequests.size()).append(", refCount=").append(mRefCount).append("}");
        return sb.toString();
    }
}

