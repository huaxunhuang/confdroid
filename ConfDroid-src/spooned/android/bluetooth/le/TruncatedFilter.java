/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.bluetooth.le;


/**
 * A special scan filter that lets the client decide how the scan record should be stored.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class TruncatedFilter {
    private final android.bluetooth.le.ScanFilter mFilter;

    private final java.util.List<android.bluetooth.le.ResultStorageDescriptor> mStorageDescriptors;

    /**
     * Constructor for {@link TruncatedFilter}.
     *
     * @param filter
     * 		Scan filter of the truncated filter.
     * @param storageDescriptors
     * 		Describes how the scan should be stored.
     */
    public TruncatedFilter(android.bluetooth.le.ScanFilter filter, java.util.List<android.bluetooth.le.ResultStorageDescriptor> storageDescriptors) {
        mFilter = filter;
        mStorageDescriptors = storageDescriptors;
    }

    /**
     * Returns the scan filter.
     */
    public android.bluetooth.le.ScanFilter getFilter() {
        return mFilter;
    }

    /**
     * Returns a list of descriptor for scan result storage.
     */
    public java.util.List<android.bluetooth.le.ResultStorageDescriptor> getStorageDescriptors() {
        return mStorageDescriptors;
    }
}

