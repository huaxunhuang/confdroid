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
package android.media.tv;


/**
 * This class is used to specify meta information of a TV input.
 */
public final class TvInputInfo implements android.os.Parcelable {
    private static final boolean DEBUG = false;

    private static final java.lang.String TAG = "TvInputInfo";

    /**
     *
     *
     * @unknown 
     */
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    @android.annotation.IntDef({ android.media.tv.TvInputInfo.TYPE_TUNER, android.media.tv.TvInputInfo.TYPE_OTHER, android.media.tv.TvInputInfo.TYPE_COMPOSITE, android.media.tv.TvInputInfo.TYPE_SVIDEO, android.media.tv.TvInputInfo.TYPE_SCART, android.media.tv.TvInputInfo.TYPE_COMPONENT, android.media.tv.TvInputInfo.TYPE_VGA, android.media.tv.TvInputInfo.TYPE_DVI, android.media.tv.TvInputInfo.TYPE_HDMI, android.media.tv.TvInputInfo.TYPE_DISPLAY_PORT })
    public @interface Type {}

    // Should be in sync with frameworks/base/core/res/res/values/attrs.xml
    /**
     * TV input type: the TV input service is a tuner which provides channels.
     */
    public static final int TYPE_TUNER = 0;

    /**
     * TV input type: a generic hardware TV input type.
     */
    public static final int TYPE_OTHER = 1000;

    /**
     * TV input type: the TV input service represents a composite port.
     */
    public static final int TYPE_COMPOSITE = 1001;

    /**
     * TV input type: the TV input service represents a SVIDEO port.
     */
    public static final int TYPE_SVIDEO = 1002;

    /**
     * TV input type: the TV input service represents a SCART port.
     */
    public static final int TYPE_SCART = 1003;

    /**
     * TV input type: the TV input service represents a component port.
     */
    public static final int TYPE_COMPONENT = 1004;

    /**
     * TV input type: the TV input service represents a VGA port.
     */
    public static final int TYPE_VGA = 1005;

    /**
     * TV input type: the TV input service represents a DVI port.
     */
    public static final int TYPE_DVI = 1006;

    /**
     * TV input type: the TV input service is HDMI. (e.g. HDMI 1)
     */
    public static final int TYPE_HDMI = 1007;

    /**
     * TV input type: the TV input service represents a display port.
     */
    public static final int TYPE_DISPLAY_PORT = 1008;

    /**
     * Used as a String extra field in setup intents created by {@link #createSetupIntent()} to
     * supply the ID of a specific TV input to set up.
     */
    public static final java.lang.String EXTRA_INPUT_ID = "android.media.tv.extra.INPUT_ID";

    private final android.content.pm.ResolveInfo mService;

    private final java.lang.String mId;

    private final int mType;

    private final boolean mIsHardwareInput;

    // TODO: Remove mIconUri when createTvInputInfo() is removed.
    private android.net.Uri mIconUri;

    private final java.lang.CharSequence mLabel;

    private final int mLabelResId;

    private final android.graphics.drawable.Icon mIcon;

    private final android.graphics.drawable.Icon mIconStandby;

    private final android.graphics.drawable.Icon mIconDisconnected;

    // Attributes from XML meta data.
    private final java.lang.String mSetupActivity;

    private final java.lang.String mSettingsActivity;

    private final boolean mCanRecord;

    private final int mTunerCount;

    // Attributes specific to HDMI
    private final android.hardware.hdmi.HdmiDeviceInfo mHdmiDeviceInfo;

    private final boolean mIsConnectedToHdmiSwitch;

    private final java.lang.String mParentId;

    private final android.os.Bundle mExtras;

    /**
     * Create a new instance of the TvInputInfo class, instantiating it from the given Context,
     * ResolveInfo, and HdmiDeviceInfo.
     *
     * @param service
     * 		The ResolveInfo returned from the package manager about this TV input service.
     * @param hdmiDeviceInfo
     * 		The HdmiDeviceInfo for a HDMI CEC logical device.
     * @param parentId
     * 		The ID of this TV input's parent input. {@code null} if none exists.
     * @param label
     * 		The label of this TvInputInfo. If it is {@code null} or empty, {@code service}
     * 		label will be loaded.
     * @param iconUri
     * 		The {@link android.net.Uri} to load the icon image. See
     * 		{@link android.content.ContentResolver#openInputStream}. If it is {@code null},
     * 		the application icon of {@code service} will be loaded.
     * @unknown 
     * @deprecated Use {@link Builder} instead.
     */
    @java.lang.Deprecated
    @android.annotation.SystemApi
    public static android.media.tv.TvInputInfo createTvInputInfo(android.content.Context context, android.content.pm.ResolveInfo service, android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo, java.lang.String parentId, java.lang.String label, android.net.Uri iconUri) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.media.tv.TvInputInfo info = new android.media.tv.TvInputInfo.Builder(context, service).setHdmiDeviceInfo(hdmiDeviceInfo).setParentId(parentId).setLabel(label).build();
        info.mIconUri = iconUri;
        return info;
    }

    /**
     * Create a new instance of the TvInputInfo class, instantiating it from the given Context,
     * ResolveInfo, and HdmiDeviceInfo.
     *
     * @param service
     * 		The ResolveInfo returned from the package manager about this TV input service.
     * @param hdmiDeviceInfo
     * 		The HdmiDeviceInfo for a HDMI CEC logical device.
     * @param parentId
     * 		The ID of this TV input's parent input. {@code null} if none exists.
     * @param labelRes
     * 		The label resource ID of this TvInputInfo. If it is {@code 0},
     * 		{@code service} label will be loaded.
     * @param icon
     * 		The {@link android.graphics.drawable.Icon} to load the icon image. If it is
     * 		{@code null}, the application icon of {@code service} will be loaded.
     * @unknown 
     * @deprecated Use {@link Builder} instead.
     */
    @java.lang.Deprecated
    @android.annotation.SystemApi
    public static android.media.tv.TvInputInfo createTvInputInfo(android.content.Context context, android.content.pm.ResolveInfo service, android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo, java.lang.String parentId, int labelRes, android.graphics.drawable.Icon icon) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return new android.media.tv.TvInputInfo.Builder(context, service).setHdmiDeviceInfo(hdmiDeviceInfo).setParentId(parentId).setLabel(labelRes).setIcon(icon).build();
    }

    /**
     * Create a new instance of the TvInputInfo class, instantiating it from the given Context,
     * ResolveInfo, and TvInputHardwareInfo.
     *
     * @param service
     * 		The ResolveInfo returned from the package manager about this TV input service.
     * @param hardwareInfo
     * 		The TvInputHardwareInfo for a TV input hardware device.
     * @param label
     * 		The label of this TvInputInfo. If it is {@code null} or empty, {@code service}
     * 		label will be loaded.
     * @param iconUri
     * 		The {@link android.net.Uri} to load the icon image. See
     * 		{@link android.content.ContentResolver#openInputStream}. If it is {@code null},
     * 		the application icon of {@code service} will be loaded.
     * @unknown 
     * @deprecated Use {@link Builder} instead.
     */
    @java.lang.Deprecated
    @android.annotation.SystemApi
    public static android.media.tv.TvInputInfo createTvInputInfo(android.content.Context context, android.content.pm.ResolveInfo service, android.media.tv.TvInputHardwareInfo hardwareInfo, java.lang.String label, android.net.Uri iconUri) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.media.tv.TvInputInfo info = new android.media.tv.TvInputInfo.Builder(context, service).setTvInputHardwareInfo(hardwareInfo).setLabel(label).build();
        info.mIconUri = iconUri;
        return info;
    }

    /**
     * Create a new instance of the TvInputInfo class, instantiating it from the given Context,
     * ResolveInfo, and TvInputHardwareInfo.
     *
     * @param service
     * 		The ResolveInfo returned from the package manager about this TV input service.
     * @param hardwareInfo
     * 		The TvInputHardwareInfo for a TV input hardware device.
     * @param labelRes
     * 		The label resource ID of this TvInputInfo. If it is {@code 0},
     * 		{@code service} label will be loaded.
     * @param icon
     * 		The {@link android.graphics.drawable.Icon} to load the icon image. If it is
     * 		{@code null}, the application icon of {@code service} will be loaded.
     * @unknown 
     * @deprecated Use {@link Builder} instead.
     */
    @java.lang.Deprecated
    @android.annotation.SystemApi
    public static android.media.tv.TvInputInfo createTvInputInfo(android.content.Context context, android.content.pm.ResolveInfo service, android.media.tv.TvInputHardwareInfo hardwareInfo, int labelRes, android.graphics.drawable.Icon icon) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return new android.media.tv.TvInputInfo.Builder(context, service).setTvInputHardwareInfo(hardwareInfo).setLabel(labelRes).setIcon(icon).build();
    }

    private TvInputInfo(android.content.pm.ResolveInfo service, java.lang.String id, int type, boolean isHardwareInput, java.lang.CharSequence label, int labelResId, android.graphics.drawable.Icon icon, android.graphics.drawable.Icon iconStandby, android.graphics.drawable.Icon iconDisconnected, java.lang.String setupActivity, java.lang.String settingsActivity, boolean canRecord, int tunerCount, android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo, boolean isConnectedToHdmiSwitch, java.lang.String parentId, android.os.Bundle extras) {
        mService = service;
        mId = id;
        mType = type;
        mIsHardwareInput = isHardwareInput;
        mLabel = label;
        mLabelResId = labelResId;
        mIcon = icon;
        mIconStandby = iconStandby;
        mIconDisconnected = iconDisconnected;
        mSetupActivity = setupActivity;
        mSettingsActivity = settingsActivity;
        mCanRecord = canRecord;
        mTunerCount = tunerCount;
        mHdmiDeviceInfo = hdmiDeviceInfo;
        mIsConnectedToHdmiSwitch = isConnectedToHdmiSwitch;
        mParentId = parentId;
        mExtras = extras;
    }

    /**
     * Returns a unique ID for this TV input. The ID is generated from the package and class name
     * implementing the TV input service.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     * Returns the parent input ID.
     *
     * <p>A TV input may have a parent input if the TV input is actually a logical representation of
     * a device behind the hardware port represented by the parent input.
     * For example, a HDMI CEC logical device, connected to a HDMI port, appears as another TV
     * input. In this case, the parent input of this logical device is the HDMI port.
     *
     * <p>Applications may group inputs by parent input ID to provide an easier access to inputs
     * sharing the same physical port. In the example of HDMI CEC, logical HDMI CEC devices behind
     * the same HDMI port have the same parent ID, which is the ID representing the port. Thus
     * applications can group the hardware HDMI port and the logical HDMI CEC devices behind it
     * together using this method.
     *
     * @return the ID of the parent input, if exists. Returns {@code null} if the parent input is
    not specified.
     */
    public java.lang.String getParentId() {
        return mParentId;
    }

    /**
     * Returns the information of the service that implements this TV input.
     */
    public android.content.pm.ServiceInfo getServiceInfo() {
        return mService.serviceInfo;
    }

    /**
     * Returns the component of the service that implements this TV input.
     *
     * @unknown 
     */
    public android.content.ComponentName getComponent() {
        return new android.content.ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
    }

    /**
     * Returns an intent to start the setup activity for this TV input.
     */
    public android.content.Intent createSetupIntent() {
        if (!android.text.TextUtils.isEmpty(mSetupActivity)) {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN);
            intent.setClassName(mService.serviceInfo.packageName, mSetupActivity);
            intent.putExtra(android.media.tv.TvInputInfo.EXTRA_INPUT_ID, getId());
            return intent;
        }
        return null;
    }

    /**
     * Returns an intent to start the settings activity for this TV input.
     */
    public android.content.Intent createSettingsIntent() {
        if (!android.text.TextUtils.isEmpty(mSettingsActivity)) {
            android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_MAIN);
            intent.setClassName(mService.serviceInfo.packageName, mSettingsActivity);
            intent.putExtra(android.media.tv.TvInputInfo.EXTRA_INPUT_ID, getId());
            return intent;
        }
        return null;
    }

    /**
     * Returns the type of this TV input.
     */
    @android.media.tv.TvInputInfo.Type
    public int getType() {
        return mType;
    }

    /**
     * Returns the number of tuners this TV input has.
     *
     * <p>This method is valid only for inputs of type {@link #TYPE_TUNER}. For inputs of other
     * types, it returns 0.
     *
     * <p>Tuners correspond to physical/logical resources that allow reception of TV signal. Having
     * <i>N</i> tuners means that the TV input is capable of receiving <i>N</i> different channels
     * concurrently.
     */
    public int getTunerCount() {
        return mTunerCount;
    }

    /**
     * Returns {@code true} if this TV input can record TV programs, {@code false} otherwise.
     */
    public boolean canRecord() {
        return mCanRecord;
    }

    /**
     * Returns domain-specific extras associated with this TV input.
     */
    public android.os.Bundle getExtras() {
        return mExtras;
    }

    /**
     * Returns the HDMI device information of this TV input.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.hardware.hdmi.HdmiDeviceInfo getHdmiDeviceInfo() {
        if (mType == android.media.tv.TvInputInfo.TYPE_HDMI) {
            return mHdmiDeviceInfo;
        }
        return null;
    }

    /**
     * Returns {@code true} if this TV input is pass-though which does not have any real channels in
     * TvProvider. {@code false} otherwise.
     *
     * @see TvContract#buildChannelUriForPassthroughInput(String)
     */
    public boolean isPassthroughInput() {
        return mType != android.media.tv.TvInputInfo.TYPE_TUNER;
    }

    /**
     * Returns {@code true} if this TV input represents a hardware device. (e.g. built-in tuner,
     * HDMI1) {@code false} otherwise.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isHardwareInput() {
        return mIsHardwareInput;
    }

    /**
     * Returns {@code true}, if a CEC device for this TV input is connected to an HDMI switch, i.e.,
     * the device isn't directly connected to a HDMI port.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean isConnectedToHdmiSwitch() {
        return mIsConnectedToHdmiSwitch;
    }

    /**
     * Checks if this TV input is marked hidden by the user in the settings.
     *
     * @param context
     * 		Supplies a {@link Context} used to check if this TV input is hidden.
     * @return {@code true} if the user marked this TV input hidden in settings. {@code false}
    otherwise.
     */
    public boolean isHidden(android.content.Context context) {
        return android.media.tv.TvInputInfo.TvInputSettings.isHidden(context, mId, android.os.UserHandle.myUserId());
    }

    /**
     * Loads the user-displayed label for this TV input.
     *
     * @param context
     * 		Supplies a {@link Context} used to load the label.
     * @return a CharSequence containing the TV input's label. If the TV input does not have
    a label, its name is returned.
     */
    public java.lang.CharSequence loadLabel(@android.annotation.NonNull
    android.content.Context context) {
        if (mLabelResId != 0) {
            return context.getPackageManager().getText(mService.serviceInfo.packageName, mLabelResId, null);
        } else
            if (!android.text.TextUtils.isEmpty(mLabel)) {
                return mLabel;
            }

        return mService.loadLabel(context.getPackageManager());
    }

    /**
     * Loads the custom label set by user in settings.
     *
     * @param context
     * 		Supplies a {@link Context} used to load the custom label.
     * @return a CharSequence containing the TV input's custom label. {@code null} if there is no
    custom label.
     */
    public java.lang.CharSequence loadCustomLabel(android.content.Context context) {
        return android.media.tv.TvInputInfo.TvInputSettings.getCustomLabel(context, mId, android.os.UserHandle.myUserId());
    }

    /**
     * Loads the user-displayed icon for this TV input.
     *
     * @param context
     * 		Supplies a {@link Context} used to load the icon.
     * @return a Drawable containing the TV input's icon. If the TV input does not have an icon,
    application's icon is returned. If it's unavailable too, {@code null} is returned.
     */
    public android.graphics.drawable.Drawable loadIcon(@android.annotation.NonNull
    android.content.Context context) {
        if (mIcon != null) {
            return mIcon.loadDrawable(context);
        } else
            if (mIconUri != null) {
                try (java.io.InputStream is = context.getContentResolver().openInputStream(mIconUri)) {
                    android.graphics.drawable.Drawable drawable = android.graphics.drawable.Drawable.createFromStream(is, null);
                    if (drawable != null) {
                        return drawable;
                    }
                } catch (java.io.IOException e) {
                    android.util.Log.w(android.media.tv.TvInputInfo.TAG, "Loading the default icon due to a failure on loading " + mIconUri, e);
                    // Falls back.
                }
            }

        return loadServiceIcon(context);
    }

    /**
     * Loads the user-displayed icon for this TV input per input state.
     *
     * @param context
     * 		Supplies a {@link Context} used to load the icon.
     * @param state
     * 		The input state. Should be one of the followings.
     * 		{@link TvInputManager#INPUT_STATE_CONNECTED},
     * 		{@link TvInputManager#INPUT_STATE_CONNECTED_STANDBY} and
     * 		{@link TvInputManager#INPUT_STATE_DISCONNECTED}.
     * @return a Drawable containing the TV input's icon for the given state or {@code null} if such
    an icon is not defined.
     * @unknown 
     */
    @android.annotation.SystemApi
    public android.graphics.drawable.Drawable loadIcon(@android.annotation.NonNull
    android.content.Context context, int state) {
        if (state == android.media.tv.TvInputManager.INPUT_STATE_CONNECTED) {
            return loadIcon(context);
        } else
            if (state == android.media.tv.TvInputManager.INPUT_STATE_CONNECTED_STANDBY) {
                if (mIconStandby != null) {
                    return mIconStandby.loadDrawable(context);
                }
            } else
                if (state == android.media.tv.TvInputManager.INPUT_STATE_DISCONNECTED) {
                    if (mIconDisconnected != null) {
                        return mIconDisconnected.loadDrawable(context);
                    }
                } else {
                    throw new java.lang.IllegalArgumentException("Unknown state: " + state);
                }


        return null;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public int hashCode() {
        return mId.hashCode();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof android.media.tv.TvInputInfo)) {
            return false;
        }
        android.media.tv.TvInputInfo obj = ((android.media.tv.TvInputInfo) (o));
        return ((((((((((((((((java.util.Objects.equals(mService, obj.mService) && android.text.TextUtils.equals(mId, obj.mId)) && (mType == obj.mType)) && (mIsHardwareInput == obj.mIsHardwareInput)) && android.text.TextUtils.equals(mLabel, obj.mLabel)) && java.util.Objects.equals(mIconUri, obj.mIconUri)) && (mLabelResId == obj.mLabelResId)) && java.util.Objects.equals(mIcon, obj.mIcon)) && java.util.Objects.equals(mIconStandby, obj.mIconStandby)) && java.util.Objects.equals(mIconDisconnected, obj.mIconDisconnected)) && android.text.TextUtils.equals(mSetupActivity, obj.mSetupActivity)) && android.text.TextUtils.equals(mSettingsActivity, obj.mSettingsActivity)) && (mCanRecord == obj.mCanRecord)) && (mTunerCount == obj.mTunerCount)) && java.util.Objects.equals(mHdmiDeviceInfo, obj.mHdmiDeviceInfo)) && (mIsConnectedToHdmiSwitch == obj.mIsConnectedToHdmiSwitch)) && android.text.TextUtils.equals(mParentId, obj.mParentId)) && java.util.Objects.equals(mExtras, obj.mExtras);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("TvInputInfo{id=" + mId) + ", pkg=") + mService.serviceInfo.packageName) + ", service=") + mService.serviceInfo.name) + "}";
    }

    /**
     * Used to package this object into a {@link Parcel}.
     *
     * @param dest
     * 		The {@link Parcel} to be written.
     * @param flags
     * 		The flags used for parceling.
     */
    @java.lang.Override
    public void writeToParcel(@android.annotation.NonNull
    android.os.Parcel dest, int flags) {
        mService.writeToParcel(dest, flags);
        dest.writeString(mId);
        dest.writeInt(mType);
        dest.writeByte(mIsHardwareInput ? ((byte) (1)) : 0);
        android.text.TextUtils.writeToParcel(mLabel, dest, flags);
        dest.writeParcelable(mIconUri, flags);
        dest.writeInt(mLabelResId);
        dest.writeParcelable(mIcon, flags);
        dest.writeParcelable(mIconStandby, flags);
        dest.writeParcelable(mIconDisconnected, flags);
        dest.writeString(mSetupActivity);
        dest.writeString(mSettingsActivity);
        dest.writeByte(mCanRecord ? ((byte) (1)) : 0);
        dest.writeInt(mTunerCount);
        dest.writeParcelable(mHdmiDeviceInfo, flags);
        dest.writeByte(mIsConnectedToHdmiSwitch ? ((byte) (1)) : 0);
        dest.writeString(mParentId);
        dest.writeBundle(mExtras);
    }

    private android.graphics.drawable.Drawable loadServiceIcon(android.content.Context context) {
        if ((mService.serviceInfo.icon == 0) && (mService.serviceInfo.applicationInfo.icon == 0)) {
            return null;
        }
        return mService.serviceInfo.loadIcon(context.getPackageManager());
    }

    public static final android.os.Parcelable.Creator<android.media.tv.TvInputInfo> CREATOR = new android.os.Parcelable.Creator<android.media.tv.TvInputInfo>() {
        @java.lang.Override
        public android.media.tv.TvInputInfo createFromParcel(android.os.Parcel in) {
            return new android.media.tv.TvInputInfo(in);
        }

        @java.lang.Override
        public android.media.tv.TvInputInfo[] newArray(int size) {
            return new android.media.tv.TvInputInfo[size];
        }
    };

    private TvInputInfo(android.os.Parcel in) {
        mService = android.content.pm.ResolveInfo.CREATOR.createFromParcel(in);
        mId = in.readString();
        mType = in.readInt();
        mIsHardwareInput = in.readByte() == 1;
        mLabel = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(in);
        mIconUri = in.readParcelable(null);
        mLabelResId = in.readInt();
        mIcon = in.readParcelable(null);
        mIconStandby = in.readParcelable(null);
        mIconDisconnected = in.readParcelable(null);
        mSetupActivity = in.readString();
        mSettingsActivity = in.readString();
        mCanRecord = in.readByte() == 1;
        mTunerCount = in.readInt();
        mHdmiDeviceInfo = in.readParcelable(null);
        mIsConnectedToHdmiSwitch = in.readByte() == 1;
        mParentId = in.readString();
        mExtras = in.readBundle();
    }

    /**
     * A convenience builder for creating {@link TvInputInfo} objects.
     */
    public static final class Builder {
        private static final int LENGTH_HDMI_PHYSICAL_ADDRESS = 4;

        private static final int LENGTH_HDMI_DEVICE_ID = 2;

        private static final java.lang.String XML_START_TAG_NAME = "tv-input";

        private static final java.lang.String DELIMITER_INFO_IN_ID = "/";

        private static final java.lang.String PREFIX_HDMI_DEVICE = "HDMI";

        private static final java.lang.String PREFIX_HARDWARE_DEVICE = "HW";

        private static final android.util.SparseIntArray sHardwareTypeToTvInputType = new android.util.SparseIntArray();

        static {
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_OTHER_HARDWARE, android.media.tv.TvInputInfo.TYPE_OTHER);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_TUNER, android.media.tv.TvInputInfo.TYPE_TUNER);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_COMPOSITE, android.media.tv.TvInputInfo.TYPE_COMPOSITE);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_SVIDEO, android.media.tv.TvInputInfo.TYPE_SVIDEO);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_SCART, android.media.tv.TvInputInfo.TYPE_SCART);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_COMPONENT, android.media.tv.TvInputInfo.TYPE_COMPONENT);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_VGA, android.media.tv.TvInputInfo.TYPE_VGA);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_DVI, android.media.tv.TvInputInfo.TYPE_DVI);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_HDMI, android.media.tv.TvInputInfo.TYPE_HDMI);
            android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.put(android.media.tv.TvInputHardwareInfo.TV_INPUT_TYPE_DISPLAY_PORT, android.media.tv.TvInputInfo.TYPE_DISPLAY_PORT);
        }

        private final android.content.Context mContext;

        private final android.content.pm.ResolveInfo mResolveInfo;

        private java.lang.CharSequence mLabel;

        private int mLabelResId;

        private android.graphics.drawable.Icon mIcon;

        private android.graphics.drawable.Icon mIconStandby;

        private android.graphics.drawable.Icon mIconDisconnected;

        private java.lang.String mSetupActivity;

        private java.lang.String mSettingsActivity;

        private java.lang.Boolean mCanRecord;

        private java.lang.Integer mTunerCount;

        private android.media.tv.TvInputHardwareInfo mTvInputHardwareInfo;

        private android.hardware.hdmi.HdmiDeviceInfo mHdmiDeviceInfo;

        private java.lang.String mParentId;

        private android.os.Bundle mExtras;

        /**
         * Constructs a new builder for {@link TvInputInfo}.
         *
         * @param context
         * 		A Context of the application package implementing this class.
         * @param component
         * 		The name of the application component to be used for the
         * 		{@link TvInputService}.
         */
        public Builder(android.content.Context context, android.content.ComponentName component) {
            mContext = context;
            android.content.Intent intent = new android.content.Intent(android.media.tv.TvInputService.SERVICE_INTERFACE).setComponent(component);
            mResolveInfo = context.getPackageManager().resolveService(intent, android.content.pm.PackageManager.GET_SERVICES | android.content.pm.PackageManager.GET_META_DATA);
        }

        /**
         * Constructs a new builder for {@link TvInputInfo}.
         *
         * @param resolveInfo
         * 		The ResolveInfo returned from the package manager about this TV input
         * 		service.
         * @unknown 
         */
        public Builder(android.content.Context context, android.content.pm.ResolveInfo resolveInfo) {
            if (context == null) {
                throw new java.lang.IllegalArgumentException("context cannot be null");
            }
            if (resolveInfo == null) {
                throw new java.lang.IllegalArgumentException("resolveInfo cannot be null");
            }
            mContext = context;
            mResolveInfo = resolveInfo;
        }

        /**
         * Sets the icon.
         *
         * @param icon
         * 		The icon that represents this TV input.
         * @return This Builder object to allow for chaining of calls to builder methods.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.media.tv.TvInputInfo.Builder setIcon(android.graphics.drawable.Icon icon) {
            this.mIcon = icon;
            return this;
        }

        /**
         * Sets the icon for a given input state.
         *
         * @param icon
         * 		The icon that represents this TV input for the given state.
         * @param state
         * 		The input state. Should be one of the followings.
         * 		{@link TvInputManager#INPUT_STATE_CONNECTED},
         * 		{@link TvInputManager#INPUT_STATE_CONNECTED_STANDBY} and
         * 		{@link TvInputManager#INPUT_STATE_DISCONNECTED}.
         * @return This Builder object to allow for chaining of calls to builder methods.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.media.tv.TvInputInfo.Builder setIcon(android.graphics.drawable.Icon icon, int state) {
            if (state == android.media.tv.TvInputManager.INPUT_STATE_CONNECTED) {
                this.mIcon = icon;
            } else
                if (state == android.media.tv.TvInputManager.INPUT_STATE_CONNECTED_STANDBY) {
                    this.mIconStandby = icon;
                } else
                    if (state == android.media.tv.TvInputManager.INPUT_STATE_DISCONNECTED) {
                        this.mIconDisconnected = icon;
                    } else {
                        throw new java.lang.IllegalArgumentException("Unknown state: " + state);
                    }


            return this;
        }

        /**
         * Sets the label.
         *
         * @param label
         * 		The text to be used as label.
         * @return This Builder object to allow for chaining of calls to builder methods.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.media.tv.TvInputInfo.Builder setLabel(java.lang.CharSequence label) {
            if (mLabelResId != 0) {
                throw new java.lang.IllegalStateException("Resource ID for label is already set.");
            }
            this.mLabel = label;
            return this;
        }

        /**
         * Sets the label.
         *
         * @param resId
         * 		The resource ID of the text to use.
         * @return This Builder object to allow for chaining of calls to builder methods.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.media.tv.TvInputInfo.Builder setLabel(@android.annotation.StringRes
        int resId) {
            if (mLabel != null) {
                throw new java.lang.IllegalStateException("Label text is already set.");
            }
            this.mLabelResId = resId;
            return this;
        }

        /**
         * Sets the HdmiDeviceInfo.
         *
         * @param hdmiDeviceInfo
         * 		The HdmiDeviceInfo for a HDMI CEC logical device.
         * @return This Builder object to allow for chaining of calls to builder methods.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.media.tv.TvInputInfo.Builder setHdmiDeviceInfo(android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo) {
            if (mTvInputHardwareInfo != null) {
                android.util.Log.w(android.media.tv.TvInputInfo.TAG, "TvInputHardwareInfo will not be used to build this TvInputInfo");
                mTvInputHardwareInfo = null;
            }
            this.mHdmiDeviceInfo = hdmiDeviceInfo;
            return this;
        }

        /**
         * Sets the parent ID.
         *
         * @param parentId
         * 		The parent ID.
         * @return This Builder object to allow for chaining of calls to builder methods.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.media.tv.TvInputInfo.Builder setParentId(java.lang.String parentId) {
            this.mParentId = parentId;
            return this;
        }

        /**
         * Sets the TvInputHardwareInfo.
         *
         * @param tvInputHardwareInfo
         * 		
         * @return This Builder object to allow for chaining of calls to builder methods.
         * @unknown 
         */
        @android.annotation.SystemApi
        public android.media.tv.TvInputInfo.Builder setTvInputHardwareInfo(android.media.tv.TvInputHardwareInfo tvInputHardwareInfo) {
            if (mHdmiDeviceInfo != null) {
                android.util.Log.w(android.media.tv.TvInputInfo.TAG, "mHdmiDeviceInfo will not be used to build this TvInputInfo");
                mHdmiDeviceInfo = null;
            }
            this.mTvInputHardwareInfo = tvInputHardwareInfo;
            return this;
        }

        /**
         * Sets the tuner count. Valid only for {@link #TYPE_TUNER}.
         *
         * @param tunerCount
         * 		The number of tuners this TV input has.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public android.media.tv.TvInputInfo.Builder setTunerCount(int tunerCount) {
            this.mTunerCount = tunerCount;
            return this;
        }

        /**
         * Sets whether this TV input can record TV programs or not.
         *
         * @param canRecord
         * 		Whether this TV input can record TV programs.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public android.media.tv.TvInputInfo.Builder setCanRecord(boolean canRecord) {
            this.mCanRecord = canRecord;
            return this;
        }

        /**
         * Sets domain-specific extras associated with this TV input.
         *
         * @param extras
         * 		Domain-specific extras associated with this TV input. Keys <em>must</em> be
         * 		a scoped name, i.e. prefixed with a package name you own, so that different
         * 		developers will not create conflicting keys.
         * @return This Builder object to allow for chaining of calls to builder methods.
         */
        public android.media.tv.TvInputInfo.Builder setExtras(android.os.Bundle extras) {
            this.mExtras = extras;
            return this;
        }

        /**
         * Creates a {@link TvInputInfo} instance with the specified fields. Most of the information
         * is obtained by parsing the AndroidManifest and {@link TvInputService#SERVICE_META_DATA}
         * for the {@link TvInputService} this TV input implements.
         *
         * @return TvInputInfo containing information about this TV input.
         */
        public android.media.tv.TvInputInfo build() {
            android.content.ComponentName componentName = new android.content.ComponentName(mResolveInfo.serviceInfo.packageName, mResolveInfo.serviceInfo.name);
            java.lang.String id;
            int type;
            boolean isHardwareInput = false;
            boolean isConnectedToHdmiSwitch = false;
            if (mHdmiDeviceInfo != null) {
                id = android.media.tv.TvInputInfo.Builder.generateInputId(componentName, mHdmiDeviceInfo);
                type = android.media.tv.TvInputInfo.TYPE_HDMI;
                isHardwareInput = true;
                isConnectedToHdmiSwitch = (mHdmiDeviceInfo.getPhysicalAddress() & 0xfff) != 0;
            } else
                if (mTvInputHardwareInfo != null) {
                    id = android.media.tv.TvInputInfo.Builder.generateInputId(componentName, mTvInputHardwareInfo);
                    type = android.media.tv.TvInputInfo.Builder.sHardwareTypeToTvInputType.get(mTvInputHardwareInfo.getType(), android.media.tv.TvInputInfo.TYPE_TUNER);
                    isHardwareInput = true;
                } else {
                    id = android.media.tv.TvInputInfo.Builder.generateInputId(componentName);
                    type = android.media.tv.TvInputInfo.TYPE_TUNER;
                }

            parseServiceMetadata(type);
            return new android.media.tv.TvInputInfo(mResolveInfo, id, type, isHardwareInput, mLabel, mLabelResId, mIcon, mIconStandby, mIconDisconnected, mSetupActivity, mSettingsActivity, mCanRecord == null ? false : mCanRecord, mTunerCount == null ? 0 : mTunerCount, mHdmiDeviceInfo, isConnectedToHdmiSwitch, mParentId, mExtras);
        }

        private static java.lang.String generateInputId(android.content.ComponentName name) {
            return name.flattenToShortString();
        }

        private static java.lang.String generateInputId(android.content.ComponentName name, android.hardware.hdmi.HdmiDeviceInfo hdmiDeviceInfo) {
            // Example of the format : "/HDMI%04X%02X"
            java.lang.String format = ((((((android.media.tv.TvInputInfo.Builder.DELIMITER_INFO_IN_ID + android.media.tv.TvInputInfo.Builder.PREFIX_HDMI_DEVICE) + "%0") + android.media.tv.TvInputInfo.Builder.LENGTH_HDMI_PHYSICAL_ADDRESS) + "X") + "%0") + android.media.tv.TvInputInfo.Builder.LENGTH_HDMI_DEVICE_ID) + "X";
            return name.flattenToShortString() + java.lang.String.format(java.util.Locale.ENGLISH, format, hdmiDeviceInfo.getPhysicalAddress(), hdmiDeviceInfo.getId());
        }

        private static java.lang.String generateInputId(android.content.ComponentName name, android.media.tv.TvInputHardwareInfo tvInputHardwareInfo) {
            return ((name.flattenToShortString() + android.media.tv.TvInputInfo.Builder.DELIMITER_INFO_IN_ID) + android.media.tv.TvInputInfo.Builder.PREFIX_HARDWARE_DEVICE) + tvInputHardwareInfo.getDeviceId();
        }

        private void parseServiceMetadata(int inputType) {
            android.content.pm.ServiceInfo si = mResolveInfo.serviceInfo;
            android.content.pm.PackageManager pm = mContext.getPackageManager();
            try (android.content.res.XmlResourceParser parser = si.loadXmlMetaData(pm, android.media.tv.TvInputService.SERVICE_META_DATA)) {
                if (parser == null) {
                    throw new java.lang.IllegalStateException((("No " + android.media.tv.TvInputService.SERVICE_META_DATA) + " meta-data found for ") + si.name);
                }
                android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
                android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
                int type;
                while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
                } 
                java.lang.String nodeName = parser.getName();
                if (!android.media.tv.TvInputInfo.Builder.XML_START_TAG_NAME.equals(nodeName)) {
                    throw new java.lang.IllegalStateException((("Meta-data does not start with " + android.media.tv.TvInputInfo.Builder.XML_START_TAG_NAME) + " tag for ") + si.name);
                }
                android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.TvInputService);
                mSetupActivity = sa.getString(com.android.internal.R.styleable.TvInputService_setupActivity);
                if ((inputType == android.media.tv.TvInputInfo.TYPE_TUNER) && android.text.TextUtils.isEmpty(mSetupActivity)) {
                    throw new java.lang.IllegalStateException("Setup activity not found for " + si.name);
                }
                mSettingsActivity = sa.getString(com.android.internal.R.styleable.TvInputService_settingsActivity);
                if (mCanRecord == null) {
                    mCanRecord = sa.getBoolean(com.android.internal.R.styleable.TvInputService_canRecord, false);
                }
                if ((mTunerCount == null) && (inputType == android.media.tv.TvInputInfo.TYPE_TUNER)) {
                    mTunerCount = sa.getInt(com.android.internal.R.styleable.TvInputService_tunerCount, 1);
                }
                sa.recycle();
            } catch (java.io.IOException | org.xmlpull.v1.XmlPullParserException e) {
                throw new java.lang.IllegalStateException("Failed reading meta-data for " + si.packageName, e);
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                throw new java.lang.IllegalStateException("No resources found for " + si.packageName, e);
            }
        }
    }

    /**
     * Utility class for putting and getting settings for TV input.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static final class TvInputSettings {
        private static final java.lang.String TV_INPUT_SEPARATOR = ":";

        private static final java.lang.String CUSTOM_NAME_SEPARATOR = ",";

        private TvInputSettings() {
        }

        private static boolean isHidden(android.content.Context context, java.lang.String inputId, int userId) {
            return android.media.tv.TvInputInfo.TvInputSettings.getHiddenTvInputIds(context, userId).contains(inputId);
        }

        private static java.lang.String getCustomLabel(android.content.Context context, java.lang.String inputId, int userId) {
            return android.media.tv.TvInputInfo.TvInputSettings.getCustomLabels(context, userId).get(inputId);
        }

        /**
         * Returns a set of TV input IDs which are marked as hidden by user in the settings.
         *
         * @param context
         * 		The application context
         * @param userId
         * 		The user ID for the stored hidden input set
         * @unknown 
         */
        @android.annotation.SystemApi
        public static java.util.Set<java.lang.String> getHiddenTvInputIds(android.content.Context context, int userId) {
            java.lang.String hiddenIdsString = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), android.provider.Settings.Secure.TV_INPUT_HIDDEN_INPUTS, userId);
            java.util.Set<java.lang.String> set = new java.util.HashSet<>();
            if (android.text.TextUtils.isEmpty(hiddenIdsString)) {
                return set;
            }
            java.lang.String[] ids = hiddenIdsString.split(android.media.tv.TvInputInfo.TvInputSettings.TV_INPUT_SEPARATOR);
            for (java.lang.String id : ids) {
                set.add(android.net.Uri.decode(id));
            }
            return set;
        }

        /**
         * Returns a map of TV input ID/custom label pairs set by the user in the settings.
         *
         * @param context
         * 		The application context
         * @param userId
         * 		The user ID for the stored hidden input map
         * @unknown 
         */
        @android.annotation.SystemApi
        public static java.util.Map<java.lang.String, java.lang.String> getCustomLabels(android.content.Context context, int userId) {
            java.lang.String labelsString = android.provider.Settings.Secure.getStringForUser(context.getContentResolver(), android.provider.Settings.Secure.TV_INPUT_CUSTOM_LABELS, userId);
            java.util.Map<java.lang.String, java.lang.String> map = new java.util.HashMap<>();
            if (android.text.TextUtils.isEmpty(labelsString)) {
                return map;
            }
            java.lang.String[] pairs = labelsString.split(android.media.tv.TvInputInfo.TvInputSettings.TV_INPUT_SEPARATOR);
            for (java.lang.String pairString : pairs) {
                java.lang.String[] pair = pairString.split(android.media.tv.TvInputInfo.TvInputSettings.CUSTOM_NAME_SEPARATOR);
                map.put(android.net.Uri.decode(pair[0]), android.net.Uri.decode(pair[1]));
            }
            return map;
        }

        /**
         * Stores a set of TV input IDs which are marked as hidden by user. This is expected to
         * be called from the settings app.
         *
         * @param context
         * 		The application context
         * @param hiddenInputIds
         * 		A set including all the hidden TV input IDs
         * @param userId
         * 		The user ID for the stored hidden input set
         * @unknown 
         */
        @android.annotation.SystemApi
        public static void putHiddenTvInputs(android.content.Context context, java.util.Set<java.lang.String> hiddenInputIds, int userId) {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            boolean firstItem = true;
            for (java.lang.String inputId : hiddenInputIds) {
                android.media.tv.TvInputInfo.TvInputSettings.ensureValidField(inputId);
                if (firstItem) {
                    firstItem = false;
                } else {
                    builder.append(android.media.tv.TvInputInfo.TvInputSettings.TV_INPUT_SEPARATOR);
                }
                builder.append(android.net.Uri.encode(inputId));
            }
            android.provider.Settings.Secure.putStringForUser(context.getContentResolver(), android.provider.Settings.Secure.TV_INPUT_HIDDEN_INPUTS, builder.toString(), userId);
            // Notify of the TvInputInfo changes.
            android.media.tv.TvInputManager tm = ((android.media.tv.TvInputManager) (context.getSystemService(android.content.Context.TV_INPUT_SERVICE)));
            for (java.lang.String inputId : hiddenInputIds) {
                android.media.tv.TvInputInfo info = tm.getTvInputInfo(inputId);
                if (info != null) {
                    tm.updateTvInputInfo(info);
                }
            }
        }

        /**
         * Stores a map of TV input ID/custom label set by user. This is expected to be
         * called from the settings app.
         *
         * @param context
         * 		The application context.
         * @param customLabels
         * 		A map of TV input ID/custom label pairs
         * @param userId
         * 		The user ID for the stored hidden input map
         * @unknown 
         */
        @android.annotation.SystemApi
        public static void putCustomLabels(android.content.Context context, java.util.Map<java.lang.String, java.lang.String> customLabels, int userId) {
            java.lang.StringBuilder builder = new java.lang.StringBuilder();
            boolean firstItem = true;
            for (java.util.Map.Entry<java.lang.String, java.lang.String> entry : customLabels.entrySet()) {
                android.media.tv.TvInputInfo.TvInputSettings.ensureValidField(entry.getKey());
                android.media.tv.TvInputInfo.TvInputSettings.ensureValidField(entry.getValue());
                if (firstItem) {
                    firstItem = false;
                } else {
                    builder.append(android.media.tv.TvInputInfo.TvInputSettings.TV_INPUT_SEPARATOR);
                }
                builder.append(android.net.Uri.encode(entry.getKey()));
                builder.append(android.media.tv.TvInputInfo.TvInputSettings.CUSTOM_NAME_SEPARATOR);
                builder.append(android.net.Uri.encode(entry.getValue()));
            }
            android.provider.Settings.Secure.putStringForUser(context.getContentResolver(), android.provider.Settings.Secure.TV_INPUT_CUSTOM_LABELS, builder.toString(), userId);
            // Notify of the TvInputInfo changes.
            android.media.tv.TvInputManager tm = ((android.media.tv.TvInputManager) (context.getSystemService(android.content.Context.TV_INPUT_SERVICE)));
            for (java.lang.String inputId : customLabels.keySet()) {
                android.media.tv.TvInputInfo info = tm.getTvInputInfo(inputId);
                if (info != null) {
                    tm.updateTvInputInfo(info);
                }
            }
        }

        private static void ensureValidField(java.lang.String value) {
            if (android.text.TextUtils.isEmpty(value)) {
                throw new java.lang.IllegalArgumentException(value + " should not empty ");
            }
        }
    }
}

