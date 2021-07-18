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
 * Represents the state of a single window generating insets for clients.
 *
 * @unknown 
 */
public class InsetsSource implements android.os.Parcelable {
    @android.view.InsetsState.InternalInsetType
    private final int mType;

    /**
     * Frame of the source in screen coordinate space
     */
    private final android.graphics.Rect mFrame;

    private boolean mVisible;

    private final android.graphics.Rect mTmpFrame = new android.graphics.Rect();

    public InsetsSource(@android.view.InsetsState.InternalInsetType
    int type) {
        mType = type;
        mFrame = new android.graphics.Rect();
    }

    public InsetsSource(android.view.InsetsSource other) {
        mType = other.mType;
        mFrame = new android.graphics.Rect(other.mFrame);
        mVisible = other.mVisible;
    }

    public void setFrame(android.graphics.Rect frame) {
        mFrame.set(frame);
    }

    public void setVisible(boolean visible) {
        mVisible = visible;
    }

    @android.view.InsetsState.InternalInsetType
    public int getType() {
        return mType;
    }

    public android.graphics.Rect getFrame() {
        return mFrame;
    }

    public boolean isVisible() {
        return mVisible;
    }

    /**
     * Calculates the insets this source will cause to a client window.
     *
     * @param relativeFrame
     * 		The frame to calculate the insets relative to.
     * @param ignoreVisibility
     * 		If true, always reports back insets even if source isn't visible.
     * @return The resulting insets. The contract is that only one side will be occupied by a
    source.
     */
    public android.graphics.Insets calculateInsets(android.graphics.Rect relativeFrame, boolean ignoreVisibility) {
        if ((!ignoreVisibility) && (!mVisible)) {
            return android.graphics.Insets.NONE;
        }
        if (!mTmpFrame.setIntersect(mFrame, relativeFrame)) {
            return android.graphics.Insets.NONE;
        }
        // Intersecting at top/bottom
        if (mTmpFrame.width() == relativeFrame.width()) {
            if (mTmpFrame.top == relativeFrame.top) {
                return android.graphics.Insets.of(0, mTmpFrame.height(), 0, 0);
            } else {
                return android.graphics.Insets.of(0, 0, 0, mTmpFrame.height());
            }
        } else// Intersecting at left/right

            if (mTmpFrame.height() == relativeFrame.height()) {
                if (mTmpFrame.left == relativeFrame.left) {
                    return android.graphics.Insets.of(mTmpFrame.width(), 0, 0, 0);
                } else {
                    return android.graphics.Insets.of(0, 0, mTmpFrame.width(), 0);
                }
            } else {
                return android.graphics.Insets.NONE;
            }

    }

    public void dump(java.lang.String prefix, java.io.PrintWriter pw) {
        pw.print(prefix);
        pw.print("InsetsSource type=");
        pw.print(android.view.InsetsState.typeToString(mType));
        pw.print(" frame=");
        pw.print(mFrame.toShortString());
        pw.print(" visible=");
        pw.print(mVisible);
        pw.println();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (this == o)
            return true;

        if ((o == null) || (getClass() != o.getClass()))
            return false;

        android.view.InsetsSource that = ((android.view.InsetsSource) (o));
        if (mType != that.mType)
            return false;

        if (mVisible != that.mVisible)
            return false;

        return mFrame.equals(that.mFrame);
    }

    @java.lang.Override
    public int hashCode() {
        int result = mType;
        result = (31 * result) + mFrame.hashCode();
        result = (31 * result) + (mVisible ? 1 : 0);
        return result;
    }

    public InsetsSource(android.os.Parcel in) {
        mType = in.readInt();
        mFrame = /* loader */
        in.readParcelable(null);
        mVisible = in.readBoolean();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mType);
        /* flags */
        dest.writeParcelable(mFrame, 0);
        dest.writeBoolean(mVisible);
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.InsetsSource> CREATOR = new android.view.Creator<android.view.InsetsSource>() {
        public android.view.InsetsSource createFromParcel(android.os.Parcel in) {
            return new android.view.InsetsSource(in);
        }

        public android.view.InsetsSource[] newArray(int size) {
            return new android.view.InsetsSource[size];
        }
    };
}

