/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.tool.processing;


public class ScopedErrorReport {
    private final java.lang.String mFilePath;

    private final java.util.List<android.databinding.tool.store.Location> mLocations;

    /**
     * Only created by Scope
     */
    ScopedErrorReport(java.lang.String filePath, java.util.List<android.databinding.tool.store.Location> locations) {
        mFilePath = filePath;
        mLocations = locations;
    }

    public java.lang.String getFilePath() {
        return mFilePath;
    }

    public java.util.List<android.databinding.tool.store.Location> getLocations() {
        return mLocations;
    }

    public boolean isValid() {
        return android.databinding.tool.util.StringUtils.isNotBlank(mFilePath);
    }

    public java.lang.String toUserReadableString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder();
        if (mFilePath != null) {
            sb.append("File:");
            sb.append(mFilePath);
        }
        if ((mLocations != null) && (mLocations.size() > 0)) {
            if (mLocations.size() > 1) {
                sb.append("Locations:");
                for (android.databinding.tool.store.Location location : mLocations) {
                    sb.append("\n    ").append(location.toUserReadableString());
                }
            } else {
                sb.append("\n    Location: ").append(mLocations.get(0).toUserReadableString());
            }
        }
        return sb.toString();
    }
}

