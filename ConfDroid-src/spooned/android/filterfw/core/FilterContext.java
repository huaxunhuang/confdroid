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
public class FilterContext {
    private android.filterfw.core.FrameManager mFrameManager;

    private android.filterfw.core.GLEnvironment mGLEnvironment;

    private java.util.HashMap<java.lang.String, android.filterfw.core.Frame> mStoredFrames = new java.util.HashMap<java.lang.String, android.filterfw.core.Frame>();

    private java.util.Set<android.filterfw.core.FilterGraph> mGraphs = new java.util.HashSet<android.filterfw.core.FilterGraph>();

    public android.filterfw.core.FrameManager getFrameManager() {
        return mFrameManager;
    }

    public void setFrameManager(android.filterfw.core.FrameManager manager) {
        if (manager == null) {
            throw new java.lang.NullPointerException("Attempting to set null FrameManager!");
        } else
            if (manager.getContext() != null) {
                throw new java.lang.IllegalArgumentException("Attempting to set FrameManager which is already " + "bound to another FilterContext!");
            } else {
                mFrameManager = manager;
                mFrameManager.setContext(this);
            }

    }

    public android.filterfw.core.GLEnvironment getGLEnvironment() {
        return mGLEnvironment;
    }

    public void initGLEnvironment(android.filterfw.core.GLEnvironment environment) {
        if (mGLEnvironment == null) {
            mGLEnvironment = environment;
        } else {
            throw new java.lang.RuntimeException("Attempting to re-initialize GL Environment for " + "FilterContext!");
        }
    }

    public interface OnFrameReceivedListener {
        public void onFrameReceived(android.filterfw.core.Filter filter, android.filterfw.core.Frame frame, java.lang.Object userData);
    }

    public synchronized void storeFrame(java.lang.String key, android.filterfw.core.Frame frame) {
        android.filterfw.core.Frame storedFrame = fetchFrame(key);
        if (storedFrame != null) {
            storedFrame.release();
        }
        frame.onFrameStore();
        mStoredFrames.put(key, frame.retain());
    }

    public synchronized android.filterfw.core.Frame fetchFrame(java.lang.String key) {
        android.filterfw.core.Frame frame = mStoredFrames.get(key);
        if (frame != null) {
            frame.onFrameFetch();
        }
        return frame;
    }

    public synchronized void removeFrame(java.lang.String key) {
        android.filterfw.core.Frame frame = mStoredFrames.get(key);
        if (frame != null) {
            mStoredFrames.remove(key);
            frame.release();
        }
    }

    public synchronized void tearDown() {
        // Release stored frames
        for (android.filterfw.core.Frame frame : mStoredFrames.values()) {
            frame.release();
        }
        mStoredFrames.clear();
        // Release graphs
        for (android.filterfw.core.FilterGraph graph : mGraphs) {
            graph.tearDown(this);
        }
        mGraphs.clear();
        // Release frame manager
        if (mFrameManager != null) {
            mFrameManager.tearDown();
            mFrameManager = null;
        }
        // Release GL context
        if (mGLEnvironment != null) {
            mGLEnvironment.tearDown();
            mGLEnvironment = null;
        }
    }

    final void addGraph(android.filterfw.core.FilterGraph graph) {
        mGraphs.add(graph);
    }
}

