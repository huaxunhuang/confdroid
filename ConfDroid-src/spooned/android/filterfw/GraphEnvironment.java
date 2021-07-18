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
package android.filterfw;


/**
 * A GraphEnvironment provides a simple front-end to filter graph setup and execution using the
 * mobile filter framework. Typically, you use a GraphEnvironment in the following fashion:
 *   1. Instantiate a new GraphEnvironment instance.
 *   2. Perform any configuration, such as adding graph references and setting a GL environment.
 *   3. Load a graph file using loadGraph() or add a graph using addGraph().
 *   4. Obtain a GraphRunner instance using getRunner().
 *   5. Execute the obtained runner.
 * Note that it is possible to add multiple graphs and runners to a single GraphEnvironment.
 *
 * @unknown 
 */
public class GraphEnvironment extends android.filterfw.MffEnvironment {
    public static final int MODE_ASYNCHRONOUS = 1;

    public static final int MODE_SYNCHRONOUS = 2;

    private android.filterfw.io.GraphReader mGraphReader;

    private java.util.ArrayList<android.filterfw.GraphEnvironment.GraphHandle> mGraphs = new java.util.ArrayList<android.filterfw.GraphEnvironment.GraphHandle>();

    private class GraphHandle {
        private android.filterfw.core.FilterGraph mGraph;

        private android.filterfw.core.AsyncRunner mAsyncRunner;

        private android.filterfw.core.SyncRunner mSyncRunner;

        public GraphHandle(android.filterfw.core.FilterGraph graph) {
            mGraph = graph;
        }

        public android.filterfw.core.FilterGraph getGraph() {
            return mGraph;
        }

        public android.filterfw.core.AsyncRunner getAsyncRunner(android.filterfw.core.FilterContext environment) {
            if (mAsyncRunner == null) {
                mAsyncRunner = new android.filterfw.core.AsyncRunner(environment, android.filterfw.core.RoundRobinScheduler.class);
                mAsyncRunner.setGraph(mGraph);
            }
            return mAsyncRunner;
        }

        public android.filterfw.core.GraphRunner getSyncRunner(android.filterfw.core.FilterContext environment) {
            if (mSyncRunner == null) {
                mSyncRunner = new android.filterfw.core.SyncRunner(environment, mGraph, android.filterfw.core.RoundRobinScheduler.class);
            }
            return mSyncRunner;
        }
    }

    /**
     * Create a new GraphEnvironment with default components.
     */
    public GraphEnvironment() {
        super(null);
    }

    /**
     * Create a new GraphEnvironment with a custom FrameManager and GraphReader. Specifying null
     * for either of these, will auto-create a default instance.
     *
     * @param frameManager
     * 		The FrameManager to use, or null to auto-create one.
     * @param reader
     * 		The GraphReader to use for graph loading, or null to auto-create one.
     * 		Note, that the reader will not be created until it is required. Pass
     * 		null if you will not load any graph files.
     */
    public GraphEnvironment(android.filterfw.core.FrameManager frameManager, android.filterfw.io.GraphReader reader) {
        super(frameManager);
        mGraphReader = reader;
    }

    /**
     * Returns the used graph reader. This will create one, if a reader has not been set already.
     */
    public android.filterfw.io.GraphReader getGraphReader() {
        if (mGraphReader == null) {
            mGraphReader = new android.filterfw.io.TextGraphReader();
        }
        return mGraphReader;
    }

    /**
     * Add graph references to resolve during graph reading. The references added here are shared
     * among all graphs.
     *
     * @param references
     * 		An alternating argument list of keys (Strings) and values.
     */
    public void addReferences(java.lang.Object... references) {
        getGraphReader().addReferencesByKeysAndValues(references);
    }

    /**
     * Loads a graph file from the specified resource and adds it to this environment.
     *
     * @param context
     * 		The context in which to read the resource.
     * @param resourceId
     * 		The ID of the graph resource to load.
     * @return A unique ID for the graph.
     */
    public int loadGraph(android.content.Context context, int resourceId) {
        // Read the file into a graph
        android.filterfw.core.FilterGraph graph = null;
        try {
            graph = getGraphReader().readGraphResource(context, resourceId);
        } catch (android.filterfw.io.GraphIOException e) {
            throw new java.lang.RuntimeException("Could not read graph: " + e.getMessage());
        }
        // Add graph to our list of graphs
        return addGraph(graph);
    }

    /**
     * Add a graph to the environment. Consider using loadGraph() if you are loading a graph from
     * a graph file.
     *
     * @param graph
     * 		The graph to add to the environment.
     * @return A unique ID for the added graph.
     */
    public int addGraph(android.filterfw.core.FilterGraph graph) {
        android.filterfw.GraphEnvironment.GraphHandle graphHandle = new android.filterfw.GraphEnvironment.GraphHandle(graph);
        mGraphs.add(graphHandle);
        return mGraphs.size() - 1;
    }

    /**
     * Access a specific graph of this environment given a graph ID (previously returned from
     * loadGraph() or addGraph()). Throws an InvalidArgumentException if no graph with the
     * specified ID could be found.
     *
     * @param graphId
     * 		The ID of the graph to get.
     * @return The graph with the specified ID.
     */
    public android.filterfw.core.FilterGraph getGraph(int graphId) {
        if ((graphId < 0) || (graphId >= mGraphs.size())) {
            throw new java.lang.IllegalArgumentException(("Invalid graph ID " + graphId) + " specified in runGraph()!");
        }
        return mGraphs.get(graphId).getGraph();
    }

    /**
     * Get a GraphRunner instance for the graph with the specified ID. The GraphRunner instance can
     * be used to execute the graph. Throws an InvalidArgumentException if no graph with the
     * specified ID could be found.
     *
     * @param graphId
     * 		The ID of the graph to get.
     * @param executionMode
     * 		The mode of graph execution. Currently this can be either
     * 		MODE_SYNCHRONOUS or MODE_ASYNCHRONOUS.
     * @return A GraphRunner instance for this graph.
     */
    public android.filterfw.core.GraphRunner getRunner(int graphId, int executionMode) {
        switch (executionMode) {
            case android.filterfw.GraphEnvironment.MODE_ASYNCHRONOUS :
                return mGraphs.get(graphId).getAsyncRunner(getContext());
            case android.filterfw.GraphEnvironment.MODE_SYNCHRONOUS :
                return mGraphs.get(graphId).getSyncRunner(getContext());
            default :
                throw new java.lang.RuntimeException(("Invalid execution mode " + executionMode) + " specified in getRunner()!");
        }
    }
}

