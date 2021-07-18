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
package android.app;


/**
 * System private API for passing profiler settings.
 *
 * {@hide }
 */
public class ProfilerInfo implements android.os.Parcelable {
    /* Name of profile output file. */
    public final java.lang.String profileFile;

    /* File descriptor for profile output file, can be null. */
    public android.os.ParcelFileDescriptor profileFd;

    /* Indicates sample profiling when nonzero, interval in microseconds. */
    public final int samplingInterval;

    /* Automatically stop the profiler when the app goes idle. */
    public final boolean autoStopProfiler;

    public ProfilerInfo(java.lang.String filename, android.os.ParcelFileDescriptor fd, int interval, boolean autoStop) {
        profileFile = filename;
        profileFd = fd;
        samplingInterval = interval;
        autoStopProfiler = autoStop;
    }

    public int describeContents() {
        if (profileFd != null) {
            return profileFd.describeContents();
        } else {
            return 0;
        }
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeString(profileFile);
        if (profileFd != null) {
            out.writeInt(1);
            profileFd.writeToParcel(out, flags);
        } else {
            out.writeInt(0);
        }
        out.writeInt(samplingInterval);
        out.writeInt(autoStopProfiler ? 1 : 0);
    }

    public static final android.os.Parcelable.Creator<android.app.ProfilerInfo> CREATOR = new android.os.Parcelable.Creator<android.app.ProfilerInfo>() {
        public android.app.ProfilerInfo createFromParcel(android.os.Parcel in) {
            return new android.app.ProfilerInfo(in);
        }

        public android.app.ProfilerInfo[] newArray(int size) {
            return new android.app.ProfilerInfo[size];
        }
    };

    private ProfilerInfo(android.os.Parcel in) {
        profileFile = in.readString();
        profileFd = (in.readInt() != 0) ? android.os.ParcelFileDescriptor.CREATOR.createFromParcel(in) : null;
        samplingInterval = in.readInt();
        autoStopProfiler = in.readInt() != 0;
    }
}

