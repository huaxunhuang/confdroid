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
public class FinalPort extends android.filterfw.core.FieldPort {
    public FinalPort(android.filterfw.core.Filter filter, java.lang.String name, java.lang.reflect.Field field, boolean hasDefault) {
        super(filter, name, field, hasDefault);
    }

    @java.lang.Override
    protected synchronized void setFieldFrame(android.filterfw.core.Frame frame, boolean isAssignment) {
        assertPortIsOpen();
        checkFrameType(frame, isAssignment);
        if (mFilter.getStatus() != android.filterfw.core.Filter.STATUS_PREINIT) {
            throw new java.lang.RuntimeException(("Attempting to modify " + this) + "!");
        } else {
            super.setFieldFrame(frame, isAssignment);
            super.transfer(null);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        return "final " + super.toString();
    }
}

