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
package android.hardware.usb;


/**
 * A class representing a USB accessory, which is an external hardware component
 * that communicates with an android application over USB.
 * The accessory is the USB host and android the device side of the USB connection.
 *
 * <p>When the accessory connects, it reports its manufacturer and model names,
 * the version of the accessory, and a user visible description of the accessory to the device.
 * The manufacturer, model and version strings are used by the USB Manager to choose
 * an appropriate application for the accessory.
 * The accessory may optionally provide a unique serial number
 * and a URL to for the accessory's website to the device as well.
 *
 * <p>An instance of this class is sent to the application via the
 * {@link UsbManager#ACTION_USB_ACCESSORY_ATTACHED} Intent.
 * The application can then call {@link UsbManager#openAccessory} to open a file descriptor
 * for reading and writing data to and from the accessory.
 *
 * <div class="special reference">
 * <h3>Developer Guides</h3>
 * <p>For more information about communicating with USB hardware, read the
 * <a href="{@docRoot }guide/topics/usb/index.html">USB</a> developer guide.</p>
 * </div>
 */
public class UsbAccessory implements android.os.Parcelable {
    private static final java.lang.String TAG = "UsbAccessory";

    private final java.lang.String mManufacturer;

    private final java.lang.String mModel;

    private final java.lang.String mDescription;

    private final java.lang.String mVersion;

    private final java.lang.String mUri;

    private final java.lang.String mSerial;

    /**
     *
     *
     * @unknown 
     */
    public static final int MANUFACTURER_STRING = 0;

    /**
     *
     *
     * @unknown 
     */
    public static final int MODEL_STRING = 1;

    /**
     *
     *
     * @unknown 
     */
    public static final int DESCRIPTION_STRING = 2;

    /**
     *
     *
     * @unknown 
     */
    public static final int VERSION_STRING = 3;

    /**
     *
     *
     * @unknown 
     */
    public static final int URI_STRING = 4;

    /**
     *
     *
     * @unknown 
     */
    public static final int SERIAL_STRING = 5;

    /**
     * UsbAccessory should only be instantiated by UsbService implementation
     *
     * @unknown 
     */
    public UsbAccessory(java.lang.String manufacturer, java.lang.String model, java.lang.String description, java.lang.String version, java.lang.String uri, java.lang.String serial) {
        mManufacturer = manufacturer;
        mModel = model;
        mDescription = description;
        mVersion = version;
        mUri = uri;
        mSerial = serial;
    }

    /**
     * UsbAccessory should only be instantiated by UsbService implementation
     *
     * @unknown 
     */
    public UsbAccessory(java.lang.String[] strings) {
        mManufacturer = strings[android.hardware.usb.UsbAccessory.MANUFACTURER_STRING];
        mModel = strings[android.hardware.usb.UsbAccessory.MODEL_STRING];
        mDescription = strings[android.hardware.usb.UsbAccessory.DESCRIPTION_STRING];
        mVersion = strings[android.hardware.usb.UsbAccessory.VERSION_STRING];
        mUri = strings[android.hardware.usb.UsbAccessory.URI_STRING];
        mSerial = strings[android.hardware.usb.UsbAccessory.SERIAL_STRING];
    }

    /**
     * Returns the manufacturer name of the accessory.
     *
     * @return the accessory manufacturer
     */
    public java.lang.String getManufacturer() {
        return mManufacturer;
    }

    /**
     * Returns the model name of the accessory.
     *
     * @return the accessory model
     */
    public java.lang.String getModel() {
        return mModel;
    }

    /**
     * Returns a user visible description of the accessory.
     *
     * @return the accessory description
     */
    public java.lang.String getDescription() {
        return mDescription;
    }

    /**
     * Returns the version of the accessory.
     *
     * @return the accessory version
     */
    public java.lang.String getVersion() {
        return mVersion;
    }

    /**
     * Returns the URI for the accessory.
     * This is an optional URI that might show information about the accessory
     * or provide the option to download an application for the accessory
     *
     * @return the accessory URI
     */
    public java.lang.String getUri() {
        return mUri;
    }

    /**
     * Returns the unique serial number for the accessory.
     * This is an optional serial number that can be used to differentiate
     * between individual accessories of the same model and manufacturer
     *
     * @return the unique serial number
     */
    public java.lang.String getSerial() {
        return mSerial;
    }

    private static boolean compare(java.lang.String s1, java.lang.String s2) {
        if (s1 == null)
            return s2 == null;

        return s1.equals(s2);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.hardware.usb.UsbAccessory) {
            android.hardware.usb.UsbAccessory accessory = ((android.hardware.usb.UsbAccessory) (obj));
            return ((((android.hardware.usb.UsbAccessory.compare(mManufacturer, accessory.getManufacturer()) && android.hardware.usb.UsbAccessory.compare(mModel, accessory.getModel())) && android.hardware.usb.UsbAccessory.compare(mDescription, accessory.getDescription())) && android.hardware.usb.UsbAccessory.compare(mVersion, accessory.getVersion())) && android.hardware.usb.UsbAccessory.compare(mUri, accessory.getUri())) && android.hardware.usb.UsbAccessory.compare(mSerial, accessory.getSerial());
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return (((((mManufacturer == null ? 0 : mManufacturer.hashCode()) ^ (mModel == null ? 0 : mModel.hashCode())) ^ (mDescription == null ? 0 : mDescription.hashCode())) ^ (mVersion == null ? 0 : mVersion.hashCode())) ^ (mUri == null ? 0 : mUri.hashCode())) ^ (mSerial == null ? 0 : mSerial.hashCode());
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((((((((("UsbAccessory[mManufacturer=" + mManufacturer) + ", mModel=") + mModel) + ", mDescription=") + mDescription) + ", mVersion=") + mVersion) + ", mUri=") + mUri) + ", mSerial=") + mSerial) + "]";
    }

    public static final android.os.Parcelable.Creator<android.hardware.usb.UsbAccessory> CREATOR = new android.os.Parcelable.Creator<android.hardware.usb.UsbAccessory>() {
        public android.hardware.usb.UsbAccessory createFromParcel(android.os.Parcel in) {
            java.lang.String manufacturer = in.readString();
            java.lang.String model = in.readString();
            java.lang.String description = in.readString();
            java.lang.String version = in.readString();
            java.lang.String uri = in.readString();
            java.lang.String serial = in.readString();
            return new android.hardware.usb.UsbAccessory(manufacturer, model, description, version, uri, serial);
        }

        public android.hardware.usb.UsbAccessory[] newArray(int size) {
            return new android.hardware.usb.UsbAccessory[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeString(mManufacturer);
        parcel.writeString(mModel);
        parcel.writeString(mDescription);
        parcel.writeString(mVersion);
        parcel.writeString(mUri);
        parcel.writeString(mSerial);
    }
}

