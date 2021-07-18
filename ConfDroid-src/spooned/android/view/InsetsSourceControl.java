/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.view;


/**
 * Represents a parcelable object to allow controlling a single {@link InsetsSource}.
 *
 * @unknown 
 */
public class InsetsSourceControl implements android.os.Parcelable {
    @android.view.InsetsState.InternalInsetType
    private final int mType;

    private final android.view.SurfaceControl mLeash;

    private final android.graphics.Point mSurfacePosition;

    public InsetsSourceControl(@android.view.InsetsState.InternalInsetType
    int type, android.view.SurfaceControl leash, android.graphics.Point surfacePosition) {
        mType = type;
        mLeash = leash;
        mSurfacePosition = surfacePosition;
    }

    public int getType() {
        return mType;
    }

    public android.view.SurfaceControl getLeash() {
        return mLeash;
    }

    public InsetsSourceControl(android.os.Parcel in) {
        mType = in.readInt();
        mLeash = /* loader */
        in.readParcelable(null);
        mSurfacePosition = /* loader */
        in.readParcelable(null);
    }

    public boolean setSurfacePosition(int left, int top) {
        if (mSurfacePosition.equals(left, top)) {
            return false;
        }
        mSurfacePosition.set(left, top);
        return true;
    }

    public android.graphics.Point getSurfacePosition() {
        return mSurfacePosition;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mType);
        /* flags */
        dest.writeParcelable(mLeash, 0);
        /* flags */
        dest.writeParcelable(mSurfacePosition, 0);
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.InsetsSourceControl> CREATOR = new android.view.Creator<android.view.InsetsSourceControl>() {
        public android.view.InsetsSourceControl createFromParcel(android.os.Parcel in) {
            return new android.view.InsetsSourceControl(in);
        }

        public android.view.InsetsSourceControl[] newArray(int size) {
            return new android.view.InsetsSourceControl[size];
        }
    };
}

