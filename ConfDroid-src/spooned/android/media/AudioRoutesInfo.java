/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.media;


/**
 * Information available from AudioService about the current routes.
 *
 * @unknown 
 */
public class AudioRoutesInfo implements android.os.Parcelable {
    public static final int MAIN_SPEAKER = 0;

    public static final int MAIN_HEADSET = 1 << 0;

    public static final int MAIN_HEADPHONES = 1 << 1;

    public static final int MAIN_DOCK_SPEAKERS = 1 << 2;

    public static final int MAIN_HDMI = 1 << 3;

    public static final int MAIN_USB = 1 << 4;

    public java.lang.CharSequence bluetoothName;

    public int mainType = android.media.AudioRoutesInfo.MAIN_SPEAKER;

    public AudioRoutesInfo() {
    }

    public AudioRoutesInfo(android.media.AudioRoutesInfo o) {
        bluetoothName = o.bluetoothName;
        mainType = o.mainType;
    }

    AudioRoutesInfo(android.os.Parcel src) {
        bluetoothName = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(src);
        mainType = src.readInt();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return (((getClass().getSimpleName() + "{ type=") + android.media.AudioRoutesInfo.typeToString(mainType)) + (android.text.TextUtils.isEmpty(bluetoothName) ? "" : ", bluetoothName=" + bluetoothName)) + " }";
    }

    private static java.lang.String typeToString(int type) {
        if (type == android.media.AudioRoutesInfo.MAIN_SPEAKER)
            return "SPEAKER";

        if ((type & android.media.AudioRoutesInfo.MAIN_HEADSET) != 0)
            return "HEADSET";

        if ((type & android.media.AudioRoutesInfo.MAIN_HEADPHONES) != 0)
            return "HEADPHONES";

        if ((type & android.media.AudioRoutesInfo.MAIN_DOCK_SPEAKERS) != 0)
            return "DOCK_SPEAKERS";

        if ((type & android.media.AudioRoutesInfo.MAIN_HDMI) != 0)
            return "HDMI";

        if ((type & android.media.AudioRoutesInfo.MAIN_USB) != 0)
            return "USB";

        return java.lang.Integer.toHexString(type);
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        android.text.TextUtils.writeToParcel(bluetoothName, dest, flags);
        dest.writeInt(mainType);
    }

    public static final android.os.Parcelable.Creator<android.media.AudioRoutesInfo> CREATOR = new android.os.Parcelable.Creator<android.media.AudioRoutesInfo>() {
        public android.media.AudioRoutesInfo createFromParcel(android.os.Parcel in) {
            return new android.media.AudioRoutesInfo(in);
        }

        public android.media.AudioRoutesInfo[] newArray(int size) {
            return new android.media.AudioRoutesInfo[size];
        }
    };
}

