/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.content.pm;


/**
 * Definition of a single optional hardware or software feature of an Android
 * device.
 * <p>
 * This object is used to represent both features supported by a device and
 * features requested by an app. Apps can request that certain features be
 * available as a prerequisite to being installed through the
 * {@code uses-feature} tag in their manifests.
 * <p>
 * Starting in {@link android.os.Build.VERSION_CODES#N}, features can have a
 * version, which must always be backwards compatible. That is, a device
 * claiming to support version 3 of a specific feature must support apps
 * requesting version 1 of that feature.
 */
public class FeatureInfo implements android.os.Parcelable {
    /**
     * The name of this feature, for example "android.hardware.camera".  If
     * this is null, then this is an OpenGL ES version feature as described
     * in {@link #reqGlEsVersion}.
     */
    public java.lang.String name;

    /**
     * If this object represents a feature supported by a device, this is the
     * maximum version of this feature supported by the device. The device
     * implicitly supports all older versions of this feature.
     * <p>
     * If this object represents a feature requested by an app, this is the
     * minimum version of the feature required by the app.
     * <p>
     * When a feature version is undefined by a device, it's assumed to be
     * version 0.
     */
    public int version;

    /**
     * Default value for {@link #reqGlEsVersion};
     */
    public static final int GL_ES_VERSION_UNDEFINED = 0;

    /**
     * The GLES version used by an application. The upper order 16 bits represent the
     * major version and the lower order 16 bits the minor version.  Only valid
     * if {@link #name} is null.
     */
    public int reqGlEsVersion;

    /**
     * Set on {@link #flags} if this feature has been required by the application.
     */
    public static final int FLAG_REQUIRED = 0x1;

    /**
     * Additional flags.  May be zero or more of {@link #FLAG_REQUIRED}.
     */
    public int flags;

    public FeatureInfo() {
    }

    public FeatureInfo(android.content.pm.FeatureInfo orig) {
        name = orig.name;
        version = orig.version;
        reqGlEsVersion = orig.reqGlEsVersion;
        flags = orig.flags;
    }

    @java.lang.Override
    public java.lang.String toString() {
        if (name != null) {
            return ((((((("FeatureInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + name) + " v=") + version) + " fl=0x") + java.lang.Integer.toHexString(flags)) + "}";
        } else {
            return ((((("FeatureInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " glEsVers=") + getGlEsVersion()) + " fl=0x") + java.lang.Integer.toHexString(flags)) + "}";
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeString(name);
        dest.writeInt(version);
        dest.writeInt(reqGlEsVersion);
        dest.writeInt(flags);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        long token = proto.start(fieldId);
        if (name != null) {
            proto.write(FeatureInfoProto.NAME, name);
        }
        proto.write(FeatureInfoProto.VERSION, version);
        proto.write(FeatureInfoProto.GLES_VERSION, getGlEsVersion());
        proto.write(FeatureInfoProto.FLAGS, flags);
        proto.end(token);
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.FeatureInfo> CREATOR = new android.content.pm.Creator<android.content.pm.FeatureInfo>() {
        @java.lang.Override
        public android.content.pm.FeatureInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.FeatureInfo(source);
        }

        @java.lang.Override
        public android.content.pm.FeatureInfo[] newArray(int size) {
            return new android.content.pm.FeatureInfo[size];
        }
    };

    private FeatureInfo(android.os.Parcel source) {
        name = source.readString();
        version = source.readInt();
        reqGlEsVersion = source.readInt();
        flags = source.readInt();
    }

    /**
     * This method extracts the major and minor version of reqGLEsVersion attribute
     * and returns it as a string. Say reqGlEsVersion value of 0x00010002 is returned
     * as 1.2
     *
     * @return String representation of the reqGlEsVersion attribute
     */
    public java.lang.String getGlEsVersion() {
        int major = (reqGlEsVersion & 0xffff0000) >> 16;
        int minor = reqGlEsVersion & 0xffff;
        return (java.lang.String.valueOf(major) + ".") + java.lang.String.valueOf(minor);
    }
}

