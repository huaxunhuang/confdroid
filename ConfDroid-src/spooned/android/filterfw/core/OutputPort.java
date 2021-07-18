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
public class OutputPort extends android.filterfw.core.FilterPort {
    protected android.filterfw.core.InputPort mTargetPort;

    protected android.filterfw.core.InputPort mBasePort;

    public OutputPort(android.filterfw.core.Filter filter, java.lang.String name) {
        super(filter, name);
    }

    public void connectTo(android.filterfw.core.InputPort target) {
        if (mTargetPort != null) {
            throw new java.lang.RuntimeException(((this + " already connected to ") + mTargetPort) + "!");
        }
        mTargetPort = target;
        mTargetPort.setSourcePort(this);
    }

    public boolean isConnected() {
        return mTargetPort != null;
    }

    public void open() {
        super.open();
        if ((mTargetPort != null) && (!mTargetPort.isOpen())) {
            mTargetPort.open();
        }
    }

    public void close() {
        super.close();
        if ((mTargetPort != null) && mTargetPort.isOpen()) {
            mTargetPort.close();
        }
    }

    public android.filterfw.core.InputPort getTargetPort() {
        return mTargetPort;
    }

    public android.filterfw.core.Filter getTargetFilter() {
        return mTargetPort == null ? null : mTargetPort.getFilter();
    }

    public void setBasePort(android.filterfw.core.InputPort basePort) {
        mBasePort = basePort;
    }

    public android.filterfw.core.InputPort getBasePort() {
        return mBasePort;
    }

    public boolean filterMustClose() {
        return (!isOpen()) && isBlocking();
    }

    public boolean isReady() {
        return (isOpen() && mTargetPort.acceptsFrame()) || (!isBlocking());
    }

    @java.lang.Override
    public void clear() {
        if (mTargetPort != null) {
            mTargetPort.clear();
        }
    }

    @java.lang.Override
    public void pushFrame(android.filterfw.core.Frame frame) {
        if (mTargetPort == null) {
            throw new java.lang.RuntimeException(("Attempting to push frame on unconnected port: " + this) + "!");
        }
        mTargetPort.pushFrame(frame);
    }

    @java.lang.Override
    public void setFrame(android.filterfw.core.Frame frame) {
        assertPortIsOpen();
        if (mTargetPort == null) {
            throw new java.lang.RuntimeException(("Attempting to set frame on unconnected port: " + this) + "!");
        }
        mTargetPort.setFrame(frame);
    }

    @java.lang.Override
    public android.filterfw.core.Frame pullFrame() {
        throw new java.lang.RuntimeException(("Cannot pull frame on " + this) + "!");
    }

    @java.lang.Override
    public boolean hasFrame() {
        return mTargetPort == null ? false : mTargetPort.hasFrame();
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "output " + super.toString();
    }
}

