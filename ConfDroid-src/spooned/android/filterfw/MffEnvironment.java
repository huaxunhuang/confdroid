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
 * Base class for mobile filter framework (MFF) frontend environments. These convenience classes
 * allow using the filter framework without the requirement of performing manual setup of its
 * required components.
 *
 * @unknown 
 */
public class MffEnvironment {
    private android.filterfw.core.FilterContext mContext;

    /**
     * Protected constructor to initialize the environment's essential components. These are the
     * frame-manager and the filter-context. Passing in null for the frame-manager causes this
     * to be auto-created.
     *
     * @param frameManager
     * 		The FrameManager to use or null to auto-create one.
     */
    protected MffEnvironment(android.filterfw.core.FrameManager frameManager) {
        // Get or create the frame manager
        if (frameManager == null) {
            frameManager = new android.filterfw.core.CachedFrameManager();
        }
        // Setup the environment
        mContext = new android.filterfw.core.FilterContext();
        mContext.setFrameManager(frameManager);
    }

    /**
     * Returns the environment's filter-context.
     */
    public android.filterfw.core.FilterContext getContext() {
        return mContext;
    }

    /**
     * Set the environment's GL environment to the specified environment. This does not activate
     * the environment.
     */
    public void setGLEnvironment(android.filterfw.core.GLEnvironment glEnvironment) {
        mContext.initGLEnvironment(glEnvironment);
    }

    /**
     * Create and activate a new GL environment for use in this filter context.
     */
    public void createGLEnvironment() {
        android.filterfw.core.GLEnvironment glEnvironment = new android.filterfw.core.GLEnvironment();
        glEnvironment.initWithNewContext();
        setGLEnvironment(glEnvironment);
    }

    /**
     * Activate the GL environment for use in the current thread. A GL environment must have been
     * previously set or created using setGLEnvironment() or createGLEnvironment()! Call this after
     * having switched to a new thread for GL filter execution.
     */
    public void activateGLEnvironment() {
        android.filterfw.core.GLEnvironment glEnv = mContext.getGLEnvironment();
        if (glEnv != null) {
            mContext.getGLEnvironment().activate();
        } else {
            throw new java.lang.NullPointerException("No GLEnvironment in place to activate!");
        }
    }

    /**
     * Deactivate the GL environment from use in the current thread. A GL environment must have been
     * previously set or created using setGLEnvironment() or createGLEnvironment()! Call this before
     * running GL filters in another thread.
     */
    public void deactivateGLEnvironment() {
        android.filterfw.core.GLEnvironment glEnv = mContext.getGLEnvironment();
        if (glEnv != null) {
            mContext.getGLEnvironment().deactivate();
        } else {
            throw new java.lang.NullPointerException("No GLEnvironment in place to deactivate!");
        }
    }
}

