/**
 * Copyright (C) 2007 The Android Open Source Project
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
 * Information you can retrieve about a particular application
 * service. This corresponds to information collected from the
 * AndroidManifest.xml's &lt;service&gt; tags.
 */
public class ServiceInfo extends android.content.pm.ComponentInfo implements android.os.Parcelable {
    /**
     * Optional name of a permission required to be able to access this
     * Service.  From the "permission" attribute.
     */
    public java.lang.String permission;

    /**
     * Bit in {@link #flags}: If set, the service will automatically be
     * stopped by the system if the user removes a task that is rooted
     * in one of the application's activities.  Set from the
     * {@link android.R.attr#stopWithTask} attribute.
     */
    public static final int FLAG_STOP_WITH_TASK = 0x1;

    /**
     * Bit in {@link #flags}: If set, the service will run in its own
     * isolated process.  Set from the
     * {@link android.R.attr#isolatedProcess} attribute.
     */
    public static final int FLAG_ISOLATED_PROCESS = 0x2;

    /**
     * Bit in {@link #flags}: If set, the service can be bound and run in the
     * calling application's package, rather than the package in which it is
     * declared.  Set from {@link android.R.attr#externalService} attribute.
     */
    public static final int FLAG_EXTERNAL_SERVICE = 0x4;

    /**
     * Bit in {@link #flags}: If set, the service (which must be isolated)
     * will be spawned from an Application Zygote, instead of the regular Zygote.
     * The Application Zygote will pre-initialize the application's class loader,
     * and call a static callback into the application to allow it to perform
     * application-specific preloads (such as loading a shared library). Therefore,
     * spawning from the Application Zygote will typically reduce the service
     * launch time and reduce its memory usage. The downside of using this flag
     * is that you will have an additional process (the app zygote itself) that
     * is taking up memory. Whether actual memory usage is improved therefore
     * strongly depends on the number of isolated services that an application
     * starts, and how much memory those services save by preloading. Therefore,
     * it is recommended to measure memory usage under typical workloads to
     * determine whether it makes sense to use this flag.
     */
    public static final int FLAG_USE_APP_ZYGOTE = 0x8;

    /**
     * Bit in {@link #flags} indicating if the service is visible to ephemeral applications.
     *
     * @unknown 
     */
    public static final int FLAG_VISIBLE_TO_INSTANT_APP = 0x100000;

    /**
     * Bit in {@link #flags}: If set, a single instance of the service will
     * run for all users on the device.  Set from the
     * {@link android.R.attr#singleUser} attribute.
     */
    public static final int FLAG_SINGLE_USER = 0x40000000;

    /**
     * Options that have been set in the service declaration in the
     * manifest.
     * These include:
     * {@link #FLAG_STOP_WITH_TASK}, {@link #FLAG_ISOLATED_PROCESS},
     * {@link #FLAG_SINGLE_USER}.
     */
    public int flags;

    /**
     * The default foreground service type if not been set in manifest file.
     */
    public static final int FOREGROUND_SERVICE_TYPE_NONE = 0;

    /**
     * Constant corresponding to <code>dataSync</code> in
     * the {@link android.R.attr#foregroundServiceType} attribute.
     * Data(photo, file, account) upload/download, backup/restore, import/export, fetch,
     * transfer over network between device and cloud.
     */
    public static final int FOREGROUND_SERVICE_TYPE_DATA_SYNC = 1 << 0;

    /**
     * Constant corresponding to <code>mediaPlayback</code> in
     * the {@link android.R.attr#foregroundServiceType} attribute.
     * Music, video, news or other media playback.
     */
    public static final int FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK = 1 << 1;

    /**
     * Constant corresponding to <code>phoneCall</code> in
     * the {@link android.R.attr#foregroundServiceType} attribute.
     * Ongoing phone call or video conference.
     */
    public static final int FOREGROUND_SERVICE_TYPE_PHONE_CALL = 1 << 2;

    /**
     * Constant corresponding to <code>location</code> in
     * the {@link android.R.attr#foregroundServiceType} attribute.
     * GPS, map, navigation location update.
     */
    public static final int FOREGROUND_SERVICE_TYPE_LOCATION = 1 << 3;

    /**
     * Constant corresponding to <code>connectedDevice</code> in
     * the {@link android.R.attr#foregroundServiceType} attribute.
     * Auto, bluetooth, TV or other devices connection, monitoring and interaction.
     */
    public static final int FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE = 1 << 4;

    /**
     * Constant corresponding to {@code mediaProjection} in
     * the {@link android.R.attr#foregroundServiceType} attribute.
     * Managing a media projection session, e.g for screen recording or taking screenshots.
     */
    public static final int FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION = 1 << 5;

    /**
     * A special value indicates to use all types set in manifest file.
     */
    public static final int FOREGROUND_SERVICE_TYPE_MANIFEST = -1;

    /**
     * The set of flags for foreground service type.
     * The foreground service type is set in {@link android.R.attr#foregroundServiceType}
     * attribute.
     *
     * @unknown 
     */
    @android.annotation.IntDef(flag = true, prefix = { "FOREGROUND_SERVICE_TYPE_" }, value = { android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MANIFEST, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_PHONE_CALL, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_LOCATION, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE, android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PROJECTION })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface ForegroundServiceType {}

    /**
     * The type of foreground service, set in
     * {@link android.R.attr#foregroundServiceType} attribute by ORing flags in
     * {@link ForegroundServiceType}
     *
     * @unknown 
     */
    @android.content.pm.ServiceInfo.ForegroundServiceType
    public int mForegroundServiceType = android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE;

    public ServiceInfo() {
    }

    public ServiceInfo(android.content.pm.ServiceInfo orig) {
        super(orig);
        permission = orig.permission;
        flags = orig.flags;
        mForegroundServiceType = orig.mForegroundServiceType;
    }

    /**
     * Return foreground service type specified in the manifest..
     *
     * @return foreground service type specified in the manifest.
     */
    @android.content.pm.ServiceInfo.ForegroundServiceType
    public int getForegroundServiceType() {
        return mForegroundServiceType;
    }

    public void dump(android.util.Printer pw, java.lang.String prefix) {
        dump(pw, prefix, android.content.pm.PackageItemInfo.DUMP_FLAG_ALL);
    }

    /**
     *
     *
     * @unknown 
     */
    void dump(android.util.Printer pw, java.lang.String prefix, int dumpFlags) {
        super.dumpFront(pw, prefix);
        pw.println((prefix + "permission=") + permission);
        pw.println((prefix + "flags=0x") + java.lang.Integer.toHexString(flags));
        super.dumpBack(pw, prefix, dumpFlags);
    }

    public java.lang.String toString() {
        return ((("ServiceInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + name) + "}";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        super.writeToParcel(dest, parcelableFlags);
        dest.writeString(permission);
        dest.writeInt(flags);
        dest.writeInt(mForegroundServiceType);
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.ServiceInfo> CREATOR = new android.content.pm.Creator<android.content.pm.ServiceInfo>() {
        public android.content.pm.ServiceInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.ServiceInfo(source);
        }

        public android.content.pm.ServiceInfo[] newArray(int size) {
            return new android.content.pm.ServiceInfo[size];
        }
    };

    private ServiceInfo(android.os.Parcel source) {
        super(source);
        permission = source.readString();
        flags = source.readInt();
        mForegroundServiceType = source.readInt();
    }
}

