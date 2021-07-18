/**
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *            http://www.apache.org/licenses/LICENSE-2.0
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
public abstract class Filter {
    static final int STATUS_PREINIT = 0;

    static final int STATUS_UNPREPARED = 1;

    static final int STATUS_PREPARED = 2;

    static final int STATUS_PROCESSING = 3;

    static final int STATUS_SLEEPING = 4;

    static final int STATUS_FINISHED = 5;

    static final int STATUS_ERROR = 6;

    static final int STATUS_RELEASED = 7;

    private java.lang.String mName;

    private int mInputCount = -1;

    private int mOutputCount = -1;

    private java.util.HashMap<java.lang.String, android.filterfw.core.InputPort> mInputPorts;

    private java.util.HashMap<java.lang.String, android.filterfw.core.OutputPort> mOutputPorts;

    private java.util.HashSet<android.filterfw.core.Frame> mFramesToRelease;

    private java.util.HashMap<java.lang.String, android.filterfw.core.Frame> mFramesToSet;

    private int mStatus = 0;

    private boolean mIsOpen = false;

    private int mSleepDelay;

    private long mCurrentTimestamp;

    private boolean mLogVerbose;

    private static final java.lang.String TAG = "Filter";

    public Filter(java.lang.String name) {
        mName = name;
        mFramesToRelease = new java.util.HashSet<android.filterfw.core.Frame>();
        mFramesToSet = new java.util.HashMap<java.lang.String, android.filterfw.core.Frame>();
        mStatus = android.filterfw.core.Filter.STATUS_PREINIT;
        mLogVerbose = android.util.Log.isLoggable(android.filterfw.core.Filter.TAG, android.util.Log.VERBOSE);
    }

    /**
     * Tests to see if a given filter is installed on the system. Requires
     * full filter package name, including filterpack.
     */
    public static final boolean isAvailable(java.lang.String filterName) {
        java.lang.ClassLoader contextClassLoader = java.lang.Thread.currentThread().getContextClassLoader();
        java.lang.Class filterClass;
        // First see if a class of that name exists
        try {
            filterClass = contextClassLoader.loadClass(filterName);
        } catch (java.lang.ClassNotFoundException e) {
            return false;
        }
        // Then make sure it's a subclass of Filter.
        try {
            filterClass.asSubclass(android.filterfw.core.Filter.class);
        } catch (java.lang.ClassCastException e) {
            return false;
        }
        return true;
    }

    public final void initWithValueMap(android.filterfw.core.KeyValueMap valueMap) {
        // Initialization
        initFinalPorts(valueMap);
        // Setup remaining ports
        initRemainingPorts(valueMap);
        // This indicates that final ports can no longer be set
        mStatus = android.filterfw.core.Filter.STATUS_UNPREPARED;
    }

    public final void initWithAssignmentString(java.lang.String assignments) {
        try {
            android.filterfw.core.KeyValueMap valueMap = new android.filterfw.io.TextGraphReader().readKeyValueAssignments(assignments);
            initWithValueMap(valueMap);
        } catch (android.filterfw.io.GraphIOException e) {
            throw new java.lang.IllegalArgumentException(e.getMessage());
        }
    }

    public final void initWithAssignmentList(java.lang.Object... keyValues) {
        android.filterfw.core.KeyValueMap valueMap = new android.filterfw.core.KeyValueMap();
        valueMap.setKeyValues(keyValues);
        initWithValueMap(valueMap);
    }

    public final void init() throws android.filterfw.core.ProtocolException {
        android.filterfw.core.KeyValueMap valueMap = new android.filterfw.core.KeyValueMap();
        initWithValueMap(valueMap);
    }

    public java.lang.String getFilterClassName() {
        return getClass().getSimpleName();
    }

    public final java.lang.String getName() {
        return mName;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public void setInputFrame(java.lang.String inputName, android.filterfw.core.Frame frame) {
        android.filterfw.core.FilterPort port = getInputPort(inputName);
        if (!port.isOpen()) {
            port.open();
        }
        port.setFrame(frame);
    }

    public final void setInputValue(java.lang.String inputName, java.lang.Object value) {
        setInputFrame(inputName, wrapInputValue(inputName, value));
    }

    protected void prepare(android.filterfw.core.FilterContext context) {
    }

    protected void parametersUpdated(java.util.Set<java.lang.String> updated) {
    }

    protected void delayNextProcess(int millisecs) {
        mSleepDelay = millisecs;
        mStatus = android.filterfw.core.Filter.STATUS_SLEEPING;
    }

    public abstract void setupPorts();

    public android.filterfw.core.FrameFormat getOutputFormat(java.lang.String portName, android.filterfw.core.FrameFormat inputFormat) {
        return null;
    }

    public final android.filterfw.core.FrameFormat getInputFormat(java.lang.String portName) {
        android.filterfw.core.InputPort inputPort = getInputPort(portName);
        return inputPort.getSourceFormat();
    }

    public void open(android.filterfw.core.FilterContext context) {
    }

    public abstract void process(android.filterfw.core.FilterContext context);

    public final int getSleepDelay() {
        return 250;
    }

    public void close(android.filterfw.core.FilterContext context) {
    }

    public void tearDown(android.filterfw.core.FilterContext context) {
    }

    public final int getNumberOfConnectedInputs() {
        int c = 0;
        for (android.filterfw.core.InputPort inputPort : mInputPorts.values()) {
            if (inputPort.isConnected()) {
                ++c;
            }
        }
        return c;
    }

    public final int getNumberOfConnectedOutputs() {
        int c = 0;
        for (android.filterfw.core.OutputPort outputPort : mOutputPorts.values()) {
            if (outputPort.isConnected()) {
                ++c;
            }
        }
        return c;
    }

    public final int getNumberOfInputs() {
        return mOutputPorts == null ? 0 : mInputPorts.size();
    }

    public final int getNumberOfOutputs() {
        return mInputPorts == null ? 0 : mOutputPorts.size();
    }

    public final android.filterfw.core.InputPort getInputPort(java.lang.String portName) {
        if (mInputPorts == null) {
            throw new java.lang.NullPointerException(((("Attempting to access input port '" + portName) + "' of ") + this) + " before Filter has been initialized!");
        }
        android.filterfw.core.InputPort result = mInputPorts.get(portName);
        if (result == null) {
            throw new java.lang.IllegalArgumentException(((("Unknown input port '" + portName) + "' on filter ") + this) + "!");
        }
        return result;
    }

    public final android.filterfw.core.OutputPort getOutputPort(java.lang.String portName) {
        if (mInputPorts == null) {
            throw new java.lang.NullPointerException(((("Attempting to access output port '" + portName) + "' of ") + this) + " before Filter has been initialized!");
        }
        android.filterfw.core.OutputPort result = mOutputPorts.get(portName);
        if (result == null) {
            throw new java.lang.IllegalArgumentException(((("Unknown output port '" + portName) + "' on filter ") + this) + "!");
        }
        return result;
    }

    protected final void pushOutput(java.lang.String name, android.filterfw.core.Frame frame) {
        if (frame.getTimestamp() == android.filterfw.core.Frame.TIMESTAMP_NOT_SET) {
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.Filter.TAG, (("Default-setting output Frame timestamp on port " + name) + " to ") + mCurrentTimestamp);

            frame.setTimestamp(mCurrentTimestamp);
        }
        getOutputPort(name).pushFrame(frame);
    }

    protected final android.filterfw.core.Frame pullInput(java.lang.String name) {
        android.filterfw.core.Frame result = getInputPort(name).pullFrame();
        if (mCurrentTimestamp == android.filterfw.core.Frame.TIMESTAMP_UNKNOWN) {
            mCurrentTimestamp = result.getTimestamp();
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.Filter.TAG, (("Default-setting current timestamp from input port " + name) + " to ") + mCurrentTimestamp);

        }
        // As result is retained, we add it to the release pool here
        mFramesToRelease.add(result);
        return result;
    }

    public void fieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
    }

    /**
     * Transfers any frame from an input port to its destination. This is useful to force a
     * transfer from a FieldPort or ProgramPort to its connected target (field or program variable).
     */
    protected void transferInputPortFrame(java.lang.String name, android.filterfw.core.FilterContext context) {
        getInputPort(name).transfer(context);
    }

    /**
     * Assigns all program variables to the ports they are connected to. Call this after
     * constructing a Program instance with attached ProgramPorts.
     */
    protected void initProgramInputs(android.filterfw.core.Program program, android.filterfw.core.FilterContext context) {
        if (program != null) {
            for (android.filterfw.core.InputPort inputPort : mInputPorts.values()) {
                if (inputPort.getTarget() == program) {
                    inputPort.transfer(context);
                }
            }
        }
    }

    /**
     * Adds an input port to the filter. You should call this from within setupPorts, if your
     * filter has input ports. No type-checking is performed on the input. If you would like to
     * check against a type mask, use
     * {@link #addMaskedInputPort(String, FrameFormat) addMaskedInputPort} instead.
     *
     * @param name
     * 		the name of the input port
     */
    protected void addInputPort(java.lang.String name) {
        addMaskedInputPort(name, null);
    }

    /**
     * Adds an input port to the filter. You should call this from within setupPorts, if your
     * filter has input ports. When type-checking is performed, the input format is
     * checked against the provided format mask. An exception is thrown in case of a conflict.
     *
     * @param name
     * 		the name of the input port
     * @param formatMask
     * 		a format mask, which filters the allowable input types
     */
    protected void addMaskedInputPort(java.lang.String name, android.filterfw.core.FrameFormat formatMask) {
        android.filterfw.core.InputPort port = new android.filterfw.core.StreamPort(this, name);
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, (("Filter " + this) + " adding ") + port);

        mInputPorts.put(name, port);
        port.setPortFormat(formatMask);
    }

    /**
     * Adds an output port to the filter with a fixed output format. You should call this from
     * within setupPorts, if your filter has output ports. You cannot use this method, if your
     * output format depends on the input format (e.g. in a pass-through filter). In this case, use
     * {@link #addOutputBasedOnInput(String, String) addOutputBasedOnInput} instead.
     *
     * @param name
     * 		the name of the output port
     * @param format
     * 		the fixed output format of this port
     */
    protected void addOutputPort(java.lang.String name, android.filterfw.core.FrameFormat format) {
        android.filterfw.core.OutputPort port = new android.filterfw.core.OutputPort(this, name);
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, (("Filter " + this) + " adding ") + port);

        port.setPortFormat(format);
        mOutputPorts.put(name, port);
    }

    /**
     * Adds an output port to the filter. You should call this from within setupPorts, if your
     * filter has output ports. Using this method indicates that the output format for this
     * particular port, depends on the format of an input port. You MUST also override
     * {@link #getOutputFormat(String, FrameFormat) getOutputFormat} to specify what format your
     * filter will output for a given input. If the output format of your filter port does not
     * depend on the input, use {@link #addOutputPort(String, FrameFormat) addOutputPort} instead.
     *
     * @param outputName
     * 		the name of the output port
     * @param inputName
     * 		the name of the input port, that this output depends on
     */
    protected void addOutputBasedOnInput(java.lang.String outputName, java.lang.String inputName) {
        android.filterfw.core.OutputPort port = new android.filterfw.core.OutputPort(this, outputName);
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, (("Filter " + this) + " adding ") + port);

        port.setBasePort(getInputPort(inputName));
        mOutputPorts.put(outputName, port);
    }

    protected void addFieldPort(java.lang.String name, java.lang.reflect.Field field, boolean hasDefault, boolean isFinal) {
        // Make sure field is accessible
        field.setAccessible(true);
        // Create port for this input
        android.filterfw.core.InputPort fieldPort = (isFinal) ? new android.filterfw.core.FinalPort(this, name, field, hasDefault) : new android.filterfw.core.FieldPort(this, name, field, hasDefault);
        // Create format for this input
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, (("Filter " + this) + " adding ") + fieldPort);

        android.filterfw.core.MutableFrameFormat format = android.filterfw.format.ObjectFormat.fromClass(field.getType(), android.filterfw.core.FrameFormat.TARGET_SIMPLE);
        fieldPort.setPortFormat(format);
        // Add port
        mInputPorts.put(name, fieldPort);
    }

    protected void addProgramPort(java.lang.String name, java.lang.String varName, java.lang.reflect.Field field, java.lang.Class varType, boolean hasDefault) {
        // Make sure field is accessible
        field.setAccessible(true);
        // Create port for this input
        android.filterfw.core.InputPort programPort = new android.filterfw.core.ProgramPort(this, name, varName, field, hasDefault);
        // Create format for this input
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, (("Filter " + this) + " adding ") + programPort);

        android.filterfw.core.MutableFrameFormat format = android.filterfw.format.ObjectFormat.fromClass(varType, android.filterfw.core.FrameFormat.TARGET_SIMPLE);
        programPort.setPortFormat(format);
        // Add port
        mInputPorts.put(name, programPort);
    }

    protected void closeOutputPort(java.lang.String name) {
        getOutputPort(name).close();
    }

    /**
     * Specifies whether the filter should not be scheduled until a frame is available on that
     * input port. Note, that setting this to false, does not block a new frame from coming in
     * (though there is no necessity to pull that frame for processing).
     *
     * @param portName
     * 		the name of the input port.
     * @param waits
     * 		true, if the filter should wait for a frame on this port.
     */
    protected void setWaitsOnInputPort(java.lang.String portName, boolean waits) {
        getInputPort(portName).setBlocking(waits);
    }

    /**
     * Specifies whether the filter should not be scheduled until the output port is free, i.e.
     * there is no frame waiting on that output.
     *
     * @param portName
     * 		the name of the output port.
     * @param waits
     * 		true, if the filter should wait for the port to become free.
     */
    protected void setWaitsOnOutputPort(java.lang.String portName, boolean waits) {
        getOutputPort(portName).setBlocking(waits);
    }

    public java.lang.String toString() {
        return ((("'" + getName()) + "' (") + getFilterClassName()) + ")";
    }

    // Core internal methods ///////////////////////////////////////////////////////////////////////
    final java.util.Collection<android.filterfw.core.InputPort> getInputPorts() {
        return mInputPorts.values();
    }

    final java.util.Collection<android.filterfw.core.OutputPort> getOutputPorts() {
        return mOutputPorts.values();
    }

    final synchronized int getStatus() {
        return mStatus;
    }

    final synchronized void unsetStatus(int flag) {
        mStatus &= ~flag;
    }

    final synchronized void performOpen(android.filterfw.core.FilterContext context) {
        if (!mIsOpen) {
            if (mStatus == android.filterfw.core.Filter.STATUS_UNPREPARED) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.Filter.TAG, "Preparing " + this);

                prepare(context);
                mStatus = android.filterfw.core.Filter.STATUS_PREPARED;
            }
            if (mStatus == android.filterfw.core.Filter.STATUS_PREPARED) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.Filter.TAG, "Opening " + this);

                open(context);
                mStatus = android.filterfw.core.Filter.STATUS_PROCESSING;
            }
            if (mStatus != android.filterfw.core.Filter.STATUS_PROCESSING) {
                throw new java.lang.RuntimeException((((("Filter " + this) + " was brought into invalid state during ") + "opening (state: ") + mStatus) + ")!");
            }
            mIsOpen = true;
        }
    }

    final synchronized void performProcess(android.filterfw.core.FilterContext context) {
        if (mStatus == android.filterfw.core.Filter.STATUS_RELEASED) {
            throw new java.lang.RuntimeException(("Filter " + this) + " is already torn down!");
        }
        transferInputFrames(context);
        if (mStatus < android.filterfw.core.Filter.STATUS_PROCESSING) {
            performOpen(context);
        }
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, "Processing " + this);

        mCurrentTimestamp = android.filterfw.core.Frame.TIMESTAMP_UNKNOWN;
        process(context);
        releasePulledFrames(context);
        if (filterMustClose()) {
            performClose(context);
        }
    }

    final synchronized void performClose(android.filterfw.core.FilterContext context) {
        if (mIsOpen) {
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.Filter.TAG, "Closing " + this);

            mIsOpen = false;
            mStatus = android.filterfw.core.Filter.STATUS_PREPARED;
            close(context);
            closePorts();
        }
    }

    final synchronized void performTearDown(android.filterfw.core.FilterContext context) {
        performClose(context);
        if (mStatus != android.filterfw.core.Filter.STATUS_RELEASED) {
            tearDown(context);
            mStatus = android.filterfw.core.Filter.STATUS_RELEASED;
        }
    }

    final synchronized boolean canProcess() {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, ((("Checking if can process: " + this) + " (") + mStatus) + ").");

        if (mStatus <= android.filterfw.core.Filter.STATUS_PROCESSING) {
            return inputConditionsMet() && outputConditionsMet();
        } else {
            return false;
        }
    }

    final void openOutputs() {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, ("Opening all output ports on " + this) + "!");

        for (android.filterfw.core.OutputPort outputPort : mOutputPorts.values()) {
            if (!outputPort.isOpen()) {
                outputPort.open();
            }
        }
    }

    final void clearInputs() {
        for (android.filterfw.core.InputPort inputPort : mInputPorts.values()) {
            inputPort.clear();
        }
    }

    final void clearOutputs() {
        for (android.filterfw.core.OutputPort outputPort : mOutputPorts.values()) {
            outputPort.clear();
        }
    }

    final void notifyFieldPortValueUpdated(java.lang.String name, android.filterfw.core.FilterContext context) {
        if ((mStatus == android.filterfw.core.Filter.STATUS_PROCESSING) || (mStatus == android.filterfw.core.Filter.STATUS_PREPARED)) {
            fieldPortValueUpdated(name, context);
        }
    }

    final synchronized void pushInputFrame(java.lang.String inputName, android.filterfw.core.Frame frame) {
        android.filterfw.core.FilterPort port = getInputPort(inputName);
        if (!port.isOpen()) {
            port.open();
        }
        port.pushFrame(frame);
    }

    final synchronized void pushInputValue(java.lang.String inputName, java.lang.Object value) {
        pushInputFrame(inputName, wrapInputValue(inputName, value));
    }

    // Filter internal methods /////////////////////////////////////////////////////////////////////
    private final void initFinalPorts(android.filterfw.core.KeyValueMap values) {
        mInputPorts = new java.util.HashMap<java.lang.String, android.filterfw.core.InputPort>();
        mOutputPorts = new java.util.HashMap<java.lang.String, android.filterfw.core.OutputPort>();
        addAndSetFinalPorts(values);
    }

    private final void initRemainingPorts(android.filterfw.core.KeyValueMap values) {
        addAnnotatedPorts();
        setupPorts();// TODO: rename to addFilterPorts() ?

        setInitialInputValues(values);
    }

    private final void addAndSetFinalPorts(android.filterfw.core.KeyValueMap values) {
        java.lang.Class filterClass = getClass();
        java.lang.annotation.Annotation annotation;
        for (java.lang.reflect.Field field : filterClass.getDeclaredFields()) {
            if ((annotation = field.getAnnotation(android.filterfw.core.GenerateFinalPort.class)) != null) {
                android.filterfw.core.GenerateFinalPort generator = ((android.filterfw.core.GenerateFinalPort) (annotation));
                java.lang.String name = (generator.name().isEmpty()) ? field.getName() : generator.name();
                boolean hasDefault = generator.hasDefault();
                addFieldPort(name, field, hasDefault, true);
                if (values.containsKey(name)) {
                    setImmediateInputValue(name, values.get(name));
                    values.remove(name);
                } else
                    if (!generator.hasDefault()) {
                        throw new java.lang.RuntimeException(((("No value specified for final input port '" + name) + "' of filter ") + this) + "!");
                    }

            }
        }
    }

    private final void addAnnotatedPorts() {
        java.lang.Class filterClass = getClass();
        java.lang.annotation.Annotation annotation;
        for (java.lang.reflect.Field field : filterClass.getDeclaredFields()) {
            if ((annotation = field.getAnnotation(android.filterfw.core.GenerateFieldPort.class)) != null) {
                android.filterfw.core.GenerateFieldPort generator = ((android.filterfw.core.GenerateFieldPort) (annotation));
                addFieldGenerator(generator, field);
            } else
                if ((annotation = field.getAnnotation(android.filterfw.core.GenerateProgramPort.class)) != null) {
                    android.filterfw.core.GenerateProgramPort generator = ((android.filterfw.core.GenerateProgramPort) (annotation));
                    addProgramGenerator(generator, field);
                } else
                    if ((annotation = field.getAnnotation(android.filterfw.core.GenerateProgramPorts.class)) != null) {
                        android.filterfw.core.GenerateProgramPorts generators = ((android.filterfw.core.GenerateProgramPorts) (annotation));
                        for (android.filterfw.core.GenerateProgramPort generator : generators.value()) {
                            addProgramGenerator(generator, field);
                        }
                    }


        }
    }

    private final void addFieldGenerator(android.filterfw.core.GenerateFieldPort generator, java.lang.reflect.Field field) {
        java.lang.String name = (generator.name().isEmpty()) ? field.getName() : generator.name();
        boolean hasDefault = generator.hasDefault();
        addFieldPort(name, field, hasDefault, false);
    }

    private final void addProgramGenerator(android.filterfw.core.GenerateProgramPort generator, java.lang.reflect.Field field) {
        java.lang.String name = generator.name();
        java.lang.String varName = (generator.variableName().isEmpty()) ? name : generator.variableName();
        java.lang.Class varType = generator.type();
        boolean hasDefault = generator.hasDefault();
        addProgramPort(name, varName, field, varType, hasDefault);
    }

    private final void setInitialInputValues(android.filterfw.core.KeyValueMap values) {
        for (java.util.Map.Entry<java.lang.String, java.lang.Object> entry : values.entrySet()) {
            setInputValue(entry.getKey(), entry.getValue());
        }
    }

    private final void setImmediateInputValue(java.lang.String name, java.lang.Object value) {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, ((("Setting immediate value " + value) + " for port ") + name) + "!");

        android.filterfw.core.FilterPort port = getInputPort(name);
        port.open();
        port.setFrame(android.filterfw.core.SimpleFrame.wrapObject(value, null));
    }

    private final void transferInputFrames(android.filterfw.core.FilterContext context) {
        for (android.filterfw.core.InputPort inputPort : mInputPorts.values()) {
            inputPort.transfer(context);
        }
    }

    private final android.filterfw.core.Frame wrapInputValue(java.lang.String inputName, java.lang.Object value) {
        android.filterfw.core.MutableFrameFormat inputFormat = android.filterfw.format.ObjectFormat.fromObject(value, android.filterfw.core.FrameFormat.TARGET_SIMPLE);
        if (value == null) {
            // If the value is null, the format cannot guess the class, so we adjust it to the
            // class of the input port here
            android.filterfw.core.FrameFormat portFormat = getInputPort(inputName).getPortFormat();
            java.lang.Class portClass = (portFormat == null) ? null : portFormat.getObjectClass();
            inputFormat.setObjectClass(portClass);
        }
        // Serialize if serializable, and type is not an immutable primitive.
        boolean shouldSerialize = (((!(value instanceof java.lang.Number)) && (!(value instanceof java.lang.Boolean))) && (!(value instanceof java.lang.String))) && (value instanceof java.io.Serializable);
        // Create frame wrapper
        android.filterfw.core.Frame frame = (shouldSerialize) ? new android.filterfw.core.SerializedFrame(inputFormat, null) : new android.filterfw.core.SimpleFrame(inputFormat, null);
        frame.setObjectValue(value);
        return frame;
    }

    private final void releasePulledFrames(android.filterfw.core.FilterContext context) {
        for (android.filterfw.core.Frame frame : mFramesToRelease) {
            context.getFrameManager().releaseFrame(frame);
        }
        mFramesToRelease.clear();
    }

    private final boolean inputConditionsMet() {
        for (android.filterfw.core.FilterPort port : mInputPorts.values()) {
            if (!port.isReady()) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.Filter.TAG, ("Input condition not met: " + port) + "!");

                return false;
            }
        }
        return true;
    }

    private final boolean outputConditionsMet() {
        for (android.filterfw.core.FilterPort port : mOutputPorts.values()) {
            if (!port.isReady()) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.Filter.TAG, ("Output condition not met: " + port) + "!");

                return false;
            }
        }
        return true;
    }

    private final void closePorts() {
        if (mLogVerbose)
            android.util.Log.v(android.filterfw.core.Filter.TAG, ("Closing all ports on " + this) + "!");

        for (android.filterfw.core.InputPort inputPort : mInputPorts.values()) {
            inputPort.close();
        }
        for (android.filterfw.core.OutputPort outputPort : mOutputPorts.values()) {
            outputPort.close();
        }
    }

    private final boolean filterMustClose() {
        for (android.filterfw.core.InputPort inputPort : mInputPorts.values()) {
            if (inputPort.filterMustClose()) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.Filter.TAG, (("Filter " + this) + " must close due to port ") + inputPort);

                return true;
            }
        }
        for (android.filterfw.core.OutputPort outputPort : mOutputPorts.values()) {
            if (outputPort.filterMustClose()) {
                if (mLogVerbose)
                    android.util.Log.v(android.filterfw.core.Filter.TAG, (("Filter " + this) + " must close due to port ") + outputPort);

                return true;
            }
        }
        return false;
    }
}

