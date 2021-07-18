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
public abstract class FilterPort {
    protected android.filterfw.core.Filter mFilter;

    protected java.lang.String mName;

    protected android.filterfw.core.FrameFormat mPortFormat;

    protected boolean mIsBlocking = true;

    protected boolean mIsOpen = false;

    protected boolean mChecksType = false;

    private boolean mLogVerbose;

    private static final java.lang.String TAG = "FilterPort";

    public FilterPort(android.filterfw.core.Filter filter, java.lang.String name) {
        mName = name;
        mFilter = filter;
        mLogVerbose = android.util.Log.isLoggable(android.filterfw.core.FilterPort.TAG, android.util.Log.VERBOSE);
    }

    public boolean isAttached() {
        return mFilter != null;
    }

    public android.filterfw.core.FrameFormat getPortFormat() {
        return mPortFormat;
    }

    public void setPortFormat(android.filterfw.core.FrameFormat format) {
        mPortFormat = format;
    }

    public android.filterfw.core.Filter getFilter() {
        return mFilter;
    }

    public java.lang.String getName() {
        return mName;
    }

    public void setBlocking(boolean blocking) {
        mIsBlocking = blocking;
    }

    public void setChecksType(boolean checksType) {
        mChecksType = checksType;
    }

    public void open() {
        if (!mIsOpen) {
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.FilterPort.TAG, "Opening " + this);

        }
        mIsOpen = true;
    }

    public void close() {
        if (mIsOpen) {
            if (mLogVerbose)
                android.util.Log.v(android.filterfw.core.FilterPort.TAG, "Closing " + this);

        }
        mIsOpen = false;
    }

    public boolean isOpen() {
        return mIsOpen;
    }

    public boolean isBlocking() {
        return mIsBlocking;
    }

    public abstract boolean filterMustClose();

    public abstract boolean isReady();

    public abstract void pushFrame(android.filterfw.core.Frame frame);

    public abstract void setFrame(android.filterfw.core.Frame frame);

    public abstract android.filterfw.core.Frame pullFrame();

    public abstract boolean hasFrame();

    public abstract void clear();

    public java.lang.String toString() {
        return (("port '" + mName) + "' of ") + mFilter;
    }

    protected void assertPortIsOpen() {
        if (!isOpen()) {
            throw new java.lang.RuntimeException(("Illegal operation on closed " + this) + "!");
        }
    }

    protected void checkFrameType(android.filterfw.core.Frame frame, boolean forceCheck) {
        if (((mChecksType || forceCheck) && (mPortFormat != null)) && (!frame.getFormat().isCompatibleWith(mPortFormat))) {
            throw new java.lang.RuntimeException(((((("Frame passed to " + this) + " is of incorrect type! ") + "Expected ") + mPortFormat) + " but got ") + frame.getFormat());
        }
    }

    protected void checkFrameManager(android.filterfw.core.Frame frame, android.filterfw.core.FilterContext context) {
        if ((frame.getFrameManager() != null) && (frame.getFrameManager() != context.getFrameManager())) {
            throw new java.lang.RuntimeException(("Frame " + frame) + " is managed by foreign FrameManager! ");
        }
    }
}

