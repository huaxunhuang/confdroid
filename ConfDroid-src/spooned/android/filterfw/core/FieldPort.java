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
public class FieldPort extends android.filterfw.core.InputPort {
    protected java.lang.reflect.Field mField;

    protected boolean mHasFrame;

    protected boolean mValueWaiting = false;

    protected java.lang.Object mValue;

    public FieldPort(android.filterfw.core.Filter filter, java.lang.String name, java.lang.reflect.Field field, boolean hasDefault) {
        super(filter, name);
        mField = field;
        mHasFrame = hasDefault;
    }

    @java.lang.Override
    public void clear() {
    }

    @java.lang.Override
    public void pushFrame(android.filterfw.core.Frame frame) {
        setFieldFrame(frame, false);
    }

    @java.lang.Override
    public void setFrame(android.filterfw.core.Frame frame) {
        setFieldFrame(frame, true);
    }

    @java.lang.Override
    public java.lang.Object getTarget() {
        try {
            return mField.get(mFilter);
        } catch (java.lang.IllegalAccessException e) {
            return null;
        }
    }

    @java.lang.Override
    public synchronized void transfer(android.filterfw.core.FilterContext context) {
        if (mValueWaiting) {
            try {
                mField.set(mFilter, mValue);
            } catch (java.lang.IllegalAccessException e) {
                throw new java.lang.RuntimeException(("Access to field '" + mField.getName()) + "' was denied!");
            }
            mValueWaiting = false;
            if (context != null) {
                mFilter.notifyFieldPortValueUpdated(mName, context);
            }
        }
    }

    @java.lang.Override
    public synchronized android.filterfw.core.Frame pullFrame() {
        throw new java.lang.RuntimeException(("Cannot pull frame on " + this) + "!");
    }

    @java.lang.Override
    public synchronized boolean hasFrame() {
        return mHasFrame;
    }

    @java.lang.Override
    public synchronized boolean acceptsFrame() {
        return !mValueWaiting;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "field " + super.toString();
    }

    protected synchronized void setFieldFrame(android.filterfw.core.Frame frame, boolean isAssignment) {
        assertPortIsOpen();
        checkFrameType(frame, isAssignment);
        // Store the object value
        java.lang.Object value = frame.getObjectValue();
        if (((value == null) && (mValue != null)) || (!value.equals(mValue))) {
            mValue = value;
            mValueWaiting = true;
        }
        // Since a frame was set, mark this port as having a frame to pull
        mHasFrame = true;
    }
}

