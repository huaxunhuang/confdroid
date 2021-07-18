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
package android.media.effect;


/**
 * Effect subclass for effects based on a single Filter. Subclasses need only invoke the
 * constructor with the correct arguments to obtain an Effect implementation.
 *
 * @unknown 
 */
public class FilterGraphEffect extends android.media.effect.FilterEffect {
    private static final java.lang.String TAG = "FilterGraphEffect";

    protected java.lang.String mInputName;

    protected java.lang.String mOutputName;

    protected android.filterfw.core.GraphRunner mRunner;

    protected android.filterfw.core.FilterGraph mGraph;

    protected java.lang.Class mSchedulerClass;

    /**
     * Constructs a new FilterGraphEffect.
     *
     * @param name
     * 		The name of this effect (used to create it in the EffectFactory).
     * @param graphString
     * 		The graph string to create the graph.
     * @param inputName
     * 		The name of the input GLTextureSource filter.
     * @param outputName
     * 		The name of the output GLTextureSource filter.
     */
    public FilterGraphEffect(android.media.effect.EffectContext context, java.lang.String name, java.lang.String graphString, java.lang.String inputName, java.lang.String outputName, java.lang.Class scheduler) {
        super(context, name);
        mInputName = inputName;
        mOutputName = outputName;
        mSchedulerClass = scheduler;
        createGraph(graphString);
    }

    private void createGraph(java.lang.String graphString) {
        android.filterfw.io.GraphReader reader = new android.filterfw.io.TextGraphReader();
        try {
            mGraph = reader.readGraphString(graphString);
        } catch (android.filterfw.io.GraphIOException e) {
            throw new java.lang.RuntimeException("Could not setup effect", e);
        }
        if (mGraph == null) {
            throw new java.lang.RuntimeException("Could not setup effect");
        }
        mRunner = new android.filterfw.core.SyncRunner(getFilterContext(), mGraph, mSchedulerClass);
    }

    @java.lang.Override
    public void apply(int inputTexId, int width, int height, int outputTexId) {
        beginGLEffect();
        android.filterfw.core.Filter src = mGraph.getFilter(mInputName);
        if (src != null) {
            src.setInputValue("texId", inputTexId);
            src.setInputValue("width", width);
            src.setInputValue("height", height);
        } else {
            throw new java.lang.RuntimeException("Internal error applying effect");
        }
        android.filterfw.core.Filter dest = mGraph.getFilter(mOutputName);
        if (dest != null) {
            dest.setInputValue("texId", outputTexId);
        } else {
            throw new java.lang.RuntimeException("Internal error applying effect");
        }
        try {
            mRunner.run();
        } catch (java.lang.RuntimeException e) {
            throw new java.lang.RuntimeException("Internal error applying effect: ", e);
        }
        endGLEffect();
    }

    @java.lang.Override
    public void setParameter(java.lang.String parameterKey, java.lang.Object value) {
    }

    @java.lang.Override
    public void release() {
        mGraph.tearDown(getFilterContext());
        mGraph = null;
    }
}

