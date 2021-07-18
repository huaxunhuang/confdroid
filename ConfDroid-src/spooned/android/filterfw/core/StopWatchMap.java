package android.filterfw.core;


public class StopWatchMap {
    public boolean LOG_MFF_RUNNING_TIMES = false;

    private java.util.HashMap<java.lang.String, android.filterfw.core.StopWatch> mStopWatches = null;

    public StopWatchMap() {
        mStopWatches = new java.util.HashMap<java.lang.String, android.filterfw.core.StopWatch>();
    }

    public void start(java.lang.String stopWatchName) {
        if (!LOG_MFF_RUNNING_TIMES) {
            return;
        }
        if (!mStopWatches.containsKey(stopWatchName)) {
            mStopWatches.put(stopWatchName, new android.filterfw.core.StopWatch(stopWatchName));
        }
        mStopWatches.get(stopWatchName).start();
    }

    public void stop(java.lang.String stopWatchName) {
        if (!LOG_MFF_RUNNING_TIMES) {
            return;
        }
        if (!mStopWatches.containsKey(stopWatchName)) {
            throw new java.lang.RuntimeException("Calling stop with unknown stopWatchName: " + stopWatchName);
        }
        mStopWatches.get(stopWatchName).stop();
    }
}

