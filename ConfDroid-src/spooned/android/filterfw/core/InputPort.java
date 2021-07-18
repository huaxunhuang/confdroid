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
public abstract class InputPort extends android.filterfw.core.FilterPort {
    protected android.filterfw.core.OutputPort mSourcePort;

    public InputPort(android.filterfw.core.Filter filter, java.lang.String name) {
        super(filter, name);
    }

    public void setSourcePort(android.filterfw.core.OutputPort source) {
        if (mSourcePort != null) {
            throw new java.lang.RuntimeException(((this + " already connected to ") + mSourcePort) + "!");
        }
        mSourcePort = source;
    }

    public boolean isConnected() {
        return mSourcePort != null;
    }

    public void open() {
        super.open();
        if ((mSourcePort != null) && (!mSourcePort.isOpen())) {
            mSourcePort.open();
        }
    }

    public void close() {
        if ((mSourcePort != null) && mSourcePort.isOpen()) {
            mSourcePort.close();
        }
        super.close();
    }

    public android.filterfw.core.OutputPort getSourcePort() {
        return mSourcePort;
    }

    public android.filterfw.core.Filter getSourceFilter() {
        return mSourcePort == null ? null : mSourcePort.getFilter();
    }

    public android.filterfw.core.FrameFormat getSourceFormat() {
        return mSourcePort != null ? mSourcePort.getPortFormat() : getPortFormat();
    }

    public java.lang.Object getTarget() {
        return null;
    }

    public boolean filterMustClose() {
        return ((!isOpen()) && isBlocking()) && (!hasFrame());
    }

    public boolean isReady() {
        return hasFrame() || (!isBlocking());
    }

    public boolean acceptsFrame() {
        return !hasFrame();
    }

    public abstract void transfer(android.filterfw.core.FilterContext context);
}

