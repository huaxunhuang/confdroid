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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class FilterGraph {
    private java.util.HashSet<android.filterfw.core.Filter> mFilters = new java.util.HashSet<android.filterfw.core.Filter>();

    private java.util.HashMap<java.lang.String, android.filterfw.core.Filter> mNameMap = new java.util.HashMap<java.lang.String, android.filterfw.core.Filter>();

    private java.util.HashMap<android.filterfw.core.OutputPort, java.util.LinkedList<android.filterfw.core.InputPort>> mPreconnections = new java.util.HashMap<android.filterfw.core.OutputPort, java.util.LinkedList<android.filterfw.core.InputPort>>();

    public static final int AUTOBRANCH_OFF = 0;

    public static final int AUTOBRANCH_SYNCED = 1;

    public static final int AUTOBRANCH_UNSYNCED = 2;

    public static final int TYPECHECK_OFF = 0;

    public static final int TYPECHECK_DYNAMIC = 1;

    public static final int TYPECHECK_STRICT = 2;

    private boolean mIsReady = false;

    private int mAutoBranchMode = android.filterfw.core.FilterGraph.AUTOBRANCH_OFF;

    private int mTypeCheckMode = android.filterfw.core.FilterGraph.TYPECHECK_STRICT;

    private boolean mDiscardUnconnectedOutputs = false;

    private boolean mLogVerbose;

    private java.lang.String TAG = "FilterGraph";

    public FilterGraph() {
        mLogVerbose = android.util.Log.isLoggable(TAG, android.util.Log.VERBOSE);
    }

    public boolean addFilter(android.filterfw.core.Filter filter) {
        if (!containsFilter(filter)) {
            mFilters.add(filter);
            mNameMap.put(filter.getName(), filter);
            return true;
        }
        return false;
    }

    public boolean containsFilter(android.filterfw.core.Filter filter) {
        return mFilters.contains(filter);
    }

    public android.filterfw.core.Filter getFilter(java.lang.String name) {
        return mNameMap.get(name);
    }

    public void connect(android.filterfw.core.Filter source, java.lang.String outputName, android.filterfw.core.Filter target, java.lang.String inputName) {
        if ((source == null) || (target == null)) {
            throw new java.lang.IllegalArgumentException("Passing null Filter in connect()!");
        } else
            if ((!containsFilter(source)) || (!containsFilter(target))) {
                throw new java.lang.RuntimeException("Attempting to connect filter not in graph!");
            }

        android.filterfw.core.OutputPort outPort = source.getOutputPort(outputName);
        android.filterfw.core.InputPort inPort = target.getInputPort(inputName);
        if (outPort == null) {
            throw new java.lang.RuntimeException(((("Unknown output port '" + outputName) + "' on Filter ") + source) + "!");
        } else
            if (inPort == null) {
                throw new java.lang.RuntimeException(((("Unknown input port '" + inputName) + "' on Filter ") + target) + "!");
            }

        preconnect(outPort, inPort);
    }

    public void connect(java.lang.String sourceName, java.lang.String outputName, java.lang.String targetName, java.lang.String inputName) {
        android.filterfw.core.Filter source = getFilter(sourceName);
        android.filterfw.core.Filter target = getFilter(targetName);
        if (source == null) {
            throw new java.lang.RuntimeException(("Attempting to connect unknown source filter '" + sourceName) + "'!");
        } else
            if (target == null) {
                throw new java.lang.RuntimeException(("Attempting to connect unknown target filter '" + targetName) + "'!");
            }

        connect(source, outputName, target, inputName);
    }

    public java.util.Set<android.filterfw.core.Filter> getFilters() {
        return mFilters;
    }

    public void beginProcessing() {
        if (mLogVerbose)
            android.util.Log.v(TAG, "Opening all filter connections...");

        for (android.filterfw.core.Filter filter : mFilters) {
            filter.openOutputs();
        }
        mIsReady = true;
    }

    public void flushFrames() {
        for (android.filterfw.core.Filter filter : mFilters) {
            filter.clearOutputs();
        }
    }

    public void closeFilters(android.filterfw.core.FilterContext context) {
        if (mLogVerbose)
            android.util.Log.v(TAG, "Closing all filters...");

        for (android.filterfw.core.Filter filter : mFilters) {
            filter.performClose(context);
        }
        mIsReady = false;
    }

    public boolean isReady() {
        return mIsReady;
    }

    public void setAutoBranchMode(int autoBranchMode) {
        mAutoBranchMode = autoBranchMode;
    }

    public void setDiscardUnconnectedOutputs(boolean discard) {
        mDiscardUnconnectedOutputs = discard;
    }

    public void setTypeCheckMode(int typeCheckMode) {
        mTypeCheckMode = typeCheckMode;
    }

    public void tearDown(android.filterfw.core.FilterContext context) {
        if (!mFilters.isEmpty()) {
            flushFrames();
            for (android.filterfw.core.Filter filter : mFilters) {
                filter.performTearDown(context);
            }
            mFilters.clear();
            mNameMap.clear();
            mIsReady = false;
        }
    }

    private boolean readyForProcessing(android.filterfw.core.Filter filter, java.util.Set<android.filterfw.core.Filter> processed) {
        // Check if this has been already processed
        if (processed.contains(filter)) {
            return false;
        }
        // Check if all dependencies have been processed
        for (android.filterfw.core.InputPort port : filter.getInputPorts()) {
            android.filterfw.core.Filter dependency = port.getSourceFilter();
            if ((dependency != null) && (!processed.contains(dependency))) {
                return false;
            }
        }
        return true;
    }

    private void runTypeCheck() {
        java.util.Stack<android.filterfw.core.Filter> filterStack = new java.util.Stack<android.filterfw.core.Filter>();
        java.util.Set<android.filterfw.core.Filter> processedFilters = new java.util.HashSet<android.filterfw.core.Filter>();
        filterStack.addAll(getSourceFilters());
        while (!filterStack.empty()) {
            // Get current filter and mark as processed
            android.filterfw.core.Filter filter = filterStack.pop();
            processedFilters.add(filter);
            // Anchor output formats
            updateOutputs(filter);
            // Perform type check
            if (mLogVerbose)
                android.util.Log.v(TAG, ("Running type check on " + filter) + "...");

            runTypeCheckOn(filter);
            // Push connected filters onto stack
            for (android.filterfw.core.OutputPort port : filter.getOutputPorts()) {
                android.filterfw.core.Filter target = port.getTargetFilter();
                if ((target != null) && readyForProcessing(target, processedFilters)) {
                    filterStack.push(target);
                }
            }
        } 
        // Make sure all ports were setup
        if (processedFilters.size() != getFilters().size()) {
            throw new java.lang.RuntimeException("Could not schedule all filters! Is your graph malformed?");
        }
    }

    private void updateOutputs(android.filterfw.core.Filter filter) {
        for (android.filterfw.core.OutputPort outputPort : filter.getOutputPorts()) {
            android.filterfw.core.InputPort inputPort = outputPort.getBasePort();
            if (inputPort != null) {
                android.filterfw.core.FrameFormat inputFormat = inputPort.getSourceFormat();
                android.filterfw.core.FrameFormat outputFormat = filter.getOutputFormat(outputPort.getName(), inputFormat);
                if (outputFormat == null) {
                    throw new java.lang.RuntimeException(("Filter did not return an output format for " + outputPort) + "!");
                }
                outputPort.setPortFormat(outputFormat);
            }
        }
    }

    private void runTypeCheckOn(android.filterfw.core.Filter filter) {
        for (android.filterfw.core.InputPort inputPort : filter.getInputPorts()) {
            if (mLogVerbose)
                android.util.Log.v(TAG, "Type checking port " + inputPort);

            android.filterfw.core.FrameFormat sourceFormat = inputPort.getSourceFormat();
            android.filterfw.core.FrameFormat targetFormat = inputPort.getPortFormat();
            if ((sourceFormat != null) && (targetFormat != null)) {
                if (mLogVerbose)
                    android.util.Log.v(TAG, ((("Checking " + sourceFormat) + " against ") + targetFormat) + ".");

                boolean compatible = true;
                switch (mTypeCheckMode) {
                    case android.filterfw.core.FilterGraph.TYPECHECK_OFF :
                        inputPort.setChecksType(false);
                        break;
                    case android.filterfw.core.FilterGraph.TYPECHECK_DYNAMIC :
                        compatible = sourceFormat.mayBeCompatibleWith(targetFormat);
                        inputPort.setChecksType(true);
                        break;
                    case android.filterfw.core.FilterGraph.TYPECHECK_STRICT :
                        compatible = sourceFormat.isCompatibleWith(targetFormat);
                        inputPort.setChecksType(false);
                        break;
                }
                if (!compatible) {
                    throw new java.lang.RuntimeException((((((("Type mismatch: Filter " + filter) + " expects a ") + "format of type ") + targetFormat) + " but got a format of type ") + sourceFormat) + "!");
                }
            }
        }
    }

    private void checkConnections() {
        // TODO
    }

    private void discardUnconnectedOutputs() {
        // Connect unconnected ports to Null filters
        java.util.LinkedList<android.filterfw.core.Filter> addedFilters = new java.util.LinkedList<android.filterfw.core.Filter>();
        for (android.filterfw.core.Filter filter : mFilters) {
            int id = 0;
            for (android.filterfw.core.OutputPort port : filter.getOutputPorts()) {
                if (!port.isConnected()) {
                    if (mLogVerbose)
                        android.util.Log.v(TAG, ("Autoconnecting unconnected " + port) + " to Null filter.");

                    android.filterpacks.base.NullFilter nullFilter = new android.filterpacks.base.NullFilter((filter.getName() + "ToNull") + id);
                    nullFilter.init();
                    addedFilters.add(nullFilter);
                    port.connectTo(nullFilter.getInputPort("frame"));
                    ++id;
                }
            }
        }
        // Add all added filters to this graph
        for (android.filterfw.core.Filter filter : addedFilters) {
            addFilter(filter);
        }
    }

    private void removeFilter(android.filterfw.core.Filter filter) {
        mFilters.remove(filter);
        mNameMap.remove(filter.getName());
    }

    private void preconnect(android.filterfw.core.OutputPort outPort, android.filterfw.core.InputPort inPort) {
        java.util.LinkedList<android.filterfw.core.InputPort> targets;
        targets = mPreconnections.get(outPort);
        if (targets == null) {
            targets = new java.util.LinkedList<android.filterfw.core.InputPort>();
            mPreconnections.put(outPort, targets);
        }
        targets.add(inPort);
    }

    private void connectPorts() {
        int branchId = 1;
        for (java.util.Map.Entry<android.filterfw.core.OutputPort, java.util.LinkedList<android.filterfw.core.InputPort>> connection : mPreconnections.entrySet()) {
            android.filterfw.core.OutputPort outputPort = connection.getKey();
            java.util.LinkedList<android.filterfw.core.InputPort> inputPorts = connection.getValue();
            if (inputPorts.size() == 1) {
                outputPort.connectTo(inputPorts.get(0));
            } else
                if (mAutoBranchMode == android.filterfw.core.FilterGraph.AUTOBRANCH_OFF) {
                    throw new java.lang.RuntimeException((("Attempting to connect " + outputPort) + " to multiple ") + "filter ports! Enable auto-branching to allow this.");
                } else {
                    if (mLogVerbose)
                        android.util.Log.v(TAG, ("Creating branch for " + outputPort) + "!");

                    android.filterpacks.base.FrameBranch branch = null;
                    if (mAutoBranchMode == android.filterfw.core.FilterGraph.AUTOBRANCH_SYNCED) {
                        branch = new android.filterpacks.base.FrameBranch("branch" + (branchId++));
                    } else {
                        throw new java.lang.RuntimeException("TODO: Unsynced branches not implemented yet!");
                    }
                    android.filterfw.core.KeyValueMap branchParams = new android.filterfw.core.KeyValueMap();
                    branch.initWithAssignmentList("outputs", inputPorts.size());
                    addFilter(branch);
                    outputPort.connectTo(branch.getInputPort("in"));
                    java.util.Iterator<android.filterfw.core.InputPort> inputPortIter = inputPorts.iterator();
                    for (android.filterfw.core.OutputPort branchOutPort : ((android.filterfw.core.Filter) (branch)).getOutputPorts()) {
                        branchOutPort.connectTo(inputPortIter.next());
                    }
                }

        }
        mPreconnections.clear();
    }

    private java.util.HashSet<android.filterfw.core.Filter> getSourceFilters() {
        java.util.HashSet<android.filterfw.core.Filter> sourceFilters = new java.util.HashSet<android.filterfw.core.Filter>();
        for (android.filterfw.core.Filter filter : getFilters()) {
            if (filter.getNumberOfConnectedInputs() == 0) {
                if (mLogVerbose)
                    android.util.Log.v(TAG, "Found source filter: " + filter);

                sourceFilters.add(filter);
            }
        }
        return sourceFilters;
    }

    // Core internal methods /////////////////////////////////////////////////////////////////////////
    void setupFilters() {
        if (mDiscardUnconnectedOutputs) {
            discardUnconnectedOutputs();
        }
        connectPorts();
        checkConnections();
        runTypeCheck();
    }
}

