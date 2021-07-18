/**
 * Copyright (C) 2008 The Android Open Source Project
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
 * Information you can retrieve about hardware configuration preferences
 * declared by an application. This corresponds to information collected from the
 * AndroidManifest.xml's &lt;uses-configuration&gt; and &lt;uses-feature&gt; tags.
 */
public class ConfigurationInfo implements android.os.Parcelable {
    /**
     * The kind of touch screen attached to the device.
     * One of: {@link android.content.res.Configuration#TOUCHSCREEN_NOTOUCH},
     * {@link android.content.res.Configuration#TOUCHSCREEN_STYLUS},
     * {@link android.content.res.Configuration#TOUCHSCREEN_FINGER}.
     */
    public int reqTouchScreen;

    /**
     * Application's input method preference.
     * One of: {@link android.content.res.Configuration#KEYBOARD_UNDEFINED},
     * {@link android.content.res.Configuration#KEYBOARD_NOKEYS},
     * {@link android.content.res.Configuration#KEYBOARD_QWERTY},
     * {@link android.content.res.Configuration#KEYBOARD_12KEY}
     */
    public int reqKeyboardType;

    /**
     * A flag indicating whether any keyboard is available.
     * one of: {@link android.content.res.Configuration#NAVIGATION_UNDEFINED},
     * {@link android.content.res.Configuration#NAVIGATION_DPAD},
     * {@link android.content.res.Configuration#NAVIGATION_TRACKBALL},
     * {@link android.content.res.Configuration#NAVIGATION_WHEEL}
     */
    public int reqNavigation;

    /**
     * Value for {@link #reqInputFeatures}: if set, indicates that the application
     * requires a hard keyboard
     */
    public static final int INPUT_FEATURE_HARD_KEYBOARD = 0x1;

    /**
     * Value for {@link #reqInputFeatures}: if set, indicates that the application
     * requires a five way navigation device
     */
    public static final int INPUT_FEATURE_FIVE_WAY_NAV = 0x2;

    /**
     * Flags associated with the input features.  Any combination of
     * {@link #INPUT_FEATURE_HARD_KEYBOARD},
     * {@link #INPUT_FEATURE_FIVE_WAY_NAV}
     */
    public int reqInputFeatures = 0;

    /**
     * Default value for {@link #reqGlEsVersion};
     */
    public static final int GL_ES_VERSION_UNDEFINED = 0;

    /**
     * The GLES version used by an application. The upper order 16 bits represent the
     * major version and the lower order 16 bits the minor version.
     */
    public int reqGlEsVersion;

    public ConfigurationInfo() {
    }

    public ConfigurationInfo(android.content.pm.ConfigurationInfo orig) {
        reqTouchScreen = orig.reqTouchScreen;
        reqKeyboardType = orig.reqKeyboardType;
        reqNavigation = orig.reqNavigation;
        reqInputFeatures = orig.reqInputFeatures;
        reqGlEsVersion = orig.reqGlEsVersion;
    }

    public java.lang.String toString() {
        return ((((((((((("ConfigurationInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " touchscreen = ") + reqTouchScreen) + " inputMethod = ") + reqKeyboardType) + " navigation = ") + reqNavigation) + " reqInputFeatures = ") + reqInputFeatures) + " reqGlEsVersion = ") + reqGlEsVersion) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeInt(reqTouchScreen);
        dest.writeInt(reqKeyboardType);
        dest.writeInt(reqNavigation);
        dest.writeInt(reqInputFeatures);
        dest.writeInt(reqGlEsVersion);
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.ConfigurationInfo> CREATOR = new android.content.pm.Creator<android.content.pm.ConfigurationInfo>() {
        public android.content.pm.ConfigurationInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.ConfigurationInfo(source);
        }

        public android.content.pm.ConfigurationInfo[] newArray(int size) {
            return new android.content.pm.ConfigurationInfo[size];
        }
    };

    private ConfigurationInfo(android.os.Parcel source) {
        reqTouchScreen = source.readInt();
        reqKeyboardType = source.readInt();
        reqNavigation = source.readInt();
        reqInputFeatures = source.readInt();
        reqGlEsVersion = source.readInt();
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

