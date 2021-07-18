/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.print;


/**
 * This class represents the description of a printer. Instances of
 * this class are created by print services to report to the system
 * the printers they manage. The information of this class has two
 * major components, printer properties such as name, id, status,
 * description and printer capabilities which describe the various
 * print modes a printer supports such as media sizes, margins, etc.
 * <p>
 * Once {@link PrinterInfo.Builder#build() built} the objects are immutable.
 * </p>
 */
public final class PrinterInfo implements android.os.Parcelable {
    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.print.PrinterInfo.STATUS_IDLE, android.print.PrinterInfo.STATUS_BUSY, android.print.PrinterInfo.STATUS_UNAVAILABLE })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Status {}

    /**
     * Printer status: the printer is idle and ready to print.
     */
    public static final int STATUS_IDLE = 1;

    /**
     * Printer status: the printer is busy printing.
     */
    public static final int STATUS_BUSY = 2;

    /**
     * Printer status: the printer is not available.
     */
    public static final int STATUS_UNAVAILABLE = 3;

    @android.annotation.NonNull
    private final android.print.PrinterId mId;

    /**
     * Resource inside the printer's services's package to be used as an icon
     */
    private final int mIconResourceId;

    /**
     * If a custom icon can be loaded for the printer
     */
    private final boolean mHasCustomPrinterIcon;

    /**
     * The generation of the icon in the cache.
     */
    private final int mCustomPrinterIconGen;

    /**
     * Intent that launches the activity showing more information about the printer.
     */
    @android.annotation.Nullable
    private final android.app.PendingIntent mInfoIntent;

    @android.annotation.NonNull
    private final java.lang.String mName;

    @android.print.PrinterInfo.Status
    private final int mStatus;

    @android.annotation.Nullable
    private final java.lang.String mDescription;

    @android.annotation.Nullable
    private final android.print.PrinterCapabilitiesInfo mCapabilities;

    private PrinterInfo(@android.annotation.NonNull
    android.print.PrinterId printerId, @android.annotation.NonNull
    java.lang.String name, @android.print.PrinterInfo.Status
    int status, int iconResourceId, boolean hasCustomPrinterIcon, java.lang.String description, android.app.PendingIntent infoIntent, android.print.PrinterCapabilitiesInfo capabilities, int customPrinterIconGen) {
        mId = printerId;
        mName = name;
        mStatus = status;
        mIconResourceId = iconResourceId;
        mHasCustomPrinterIcon = hasCustomPrinterIcon;
        mDescription = description;
        mInfoIntent = infoIntent;
        mCapabilities = capabilities;
        mCustomPrinterIconGen = customPrinterIconGen;
    }

    /**
     * Get the globally unique printer id.
     *
     * @return The printer id.
     */
    @android.annotation.NonNull
    public android.print.PrinterId getId() {
        return mId;
    }

    /**
     * Get the icon to be used for this printer. If no per printer icon is available, the printer's
     * service's icon is returned. If the printer has a custom icon this icon might get requested
     * asynchronously. Once the icon is loaded the discovery sessions will be notified that the
     * printer changed.
     *
     * @param context
     * 		The context that will be using the icons
     * @return The icon to be used for the printer or null if no icon could be found.
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.Nullable
    public android.graphics.drawable.Drawable loadIcon(@android.annotation.NonNull
    android.content.Context context) {
        android.graphics.drawable.Drawable drawable = null;
        android.content.pm.PackageManager packageManager = context.getPackageManager();
        if (mHasCustomPrinterIcon) {
            android.print.PrintManager printManager = ((android.print.PrintManager) (context.getSystemService(android.content.Context.PRINT_SERVICE)));
            android.graphics.drawable.Icon icon = printManager.getCustomPrinterIcon(mId);
            if (icon != null) {
                drawable = icon.loadDrawable(context);
            }
        }
        if (drawable == null) {
            try {
                java.lang.String packageName = mId.getServiceName().getPackageName();
                android.content.pm.PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
                android.content.pm.ApplicationInfo appInfo = packageInfo.applicationInfo;
                // If no custom icon is available, try the icon from the resources
                if (mIconResourceId != 0) {
                    drawable = packageManager.getDrawable(packageName, mIconResourceId, appInfo);
                }
                // Fall back to the printer's service's icon if no per printer icon could be found
                if (drawable == null) {
                    drawable = appInfo.loadIcon(packageManager);
                }
            } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            }
        }
        return drawable;
    }

    /**
     * Get the printer name.
     *
     * @return The printer name.
     */
    @android.annotation.NonNull
    public java.lang.String getName() {
        return mName;
    }

    /**
     * Gets the printer status.
     *
     * @return The status.
     * @see #STATUS_BUSY
     * @see #STATUS_IDLE
     * @see #STATUS_UNAVAILABLE
     */
    @android.print.PrinterInfo.Status
    public int getStatus() {
        return mStatus;
    }

    /**
     * Gets the  printer description.
     *
     * @return The description.
     */
    @android.annotation.Nullable
    public java.lang.String getDescription() {
        return mDescription;
    }

    /**
     * Get the {@link PendingIntent} that launches the activity showing more information about the
     * printer.
     *
     * @return the {@link PendingIntent} that launches the activity showing more information about
    the printer or null if it is not configured
     * @unknown 
     */
    @android.annotation.Nullable
    public android.app.PendingIntent getInfoIntent() {
        return mInfoIntent;
    }

    /**
     * Gets the printer capabilities.
     *
     * @return The capabilities.
     */
    @android.annotation.Nullable
    public android.print.PrinterCapabilitiesInfo getCapabilities() {
        return mCapabilities;
    }

    /**
     * Check if printerId is valid.
     *
     * @param printerId
     * 		The printerId that might be valid
     * @return The valid printerId
     * @throws IllegalArgumentException
     * 		if printerId is not valid.
     */
    @android.annotation.NonNull
    private static android.print.PrinterId checkPrinterId(android.print.PrinterId printerId) {
        return com.android.internal.util.Preconditions.checkNotNull(printerId, "printerId cannot be null.");
    }

    /**
     * Check if status is valid.
     *
     * @param status
     * 		The status that might be valid
     * @return The valid status
     * @throws IllegalArgumentException
     * 		if status is not valid.
     */
    @android.print.PrinterInfo.Status
    private static int checkStatus(int status) {
        if (!(((status == android.print.PrinterInfo.STATUS_IDLE) || (status == android.print.PrinterInfo.STATUS_BUSY)) || (status == android.print.PrinterInfo.STATUS_UNAVAILABLE))) {
            throw new java.lang.IllegalArgumentException("status is invalid.");
        }
        return status;
    }

    /**
     * Check if name is valid.
     *
     * @param name
     * 		The name that might be valid
     * @return The valid name
     * @throws IllegalArgumentException
     * 		if name is not valid.
     */
    @android.annotation.NonNull
    private static java.lang.String checkName(java.lang.String name) {
        return com.android.internal.util.Preconditions.checkStringNotEmpty(name, "name cannot be empty.");
    }

    private PrinterInfo(android.os.Parcel parcel) {
        // mName can be null due to unchecked set in Builder.setName and status can be invalid
        // due to unchecked set in Builder.setStatus, hence we can only check mId for a valid state
        mId = android.print.PrinterInfo.checkPrinterId(((android.print.PrinterId) (parcel.readParcelable(null))));
        mName = android.print.PrinterInfo.checkName(parcel.readString());
        mStatus = android.print.PrinterInfo.checkStatus(parcel.readInt());
        mDescription = parcel.readString();
        mCapabilities = parcel.readParcelable(null);
        mIconResourceId = parcel.readInt();
        mHasCustomPrinterIcon = parcel.readByte() != 0;
        mCustomPrinterIconGen = parcel.readInt();
        mInfoIntent = parcel.readParcelable(null);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeParcelable(mId, flags);
        parcel.writeString(mName);
        parcel.writeInt(mStatus);
        parcel.writeString(mDescription);
        parcel.writeParcelable(mCapabilities, flags);
        parcel.writeInt(mIconResourceId);
        parcel.writeByte(((byte) (mHasCustomPrinterIcon ? 1 : 0)));
        parcel.writeInt(mCustomPrinterIconGen);
        parcel.writeParcelable(mInfoIntent, flags);
    }

    @java.lang.Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + mId.hashCode();
        result = (prime * result) + mName.hashCode();
        result = (prime * result) + mStatus;
        result = (prime * result) + (mDescription != null ? mDescription.hashCode() : 0);
        result = (prime * result) + (mCapabilities != null ? mCapabilities.hashCode() : 0);
        result = (prime * result) + mIconResourceId;
        result = (prime * result) + (mHasCustomPrinterIcon ? 1 : 0);
        result = (prime * result) + mCustomPrinterIconGen;
        result = (prime * result) + (mInfoIntent != null ? mInfoIntent.hashCode() : 0);
        return result;
    }

    /**
     * Compare two {@link PrinterInfo printerInfos} in all aspects beside being null and the
     * {@link #mStatus}.
     *
     * @param other
     * 		the other {@link PrinterInfo}
     * @return true iff the infos are equivalent
     * @unknown 
     */
    public boolean equalsIgnoringStatus(android.print.PrinterInfo other) {
        if (!mId.equals(other.mId)) {
            return false;
        }
        if (!mName.equals(other.mName)) {
            return false;
        }
        if (!android.text.TextUtils.equals(mDescription, other.mDescription)) {
            return false;
        }
        if (mCapabilities == null) {
            if (other.mCapabilities != null) {
                return false;
            }
        } else
            if (!mCapabilities.equals(other.mCapabilities)) {
                return false;
            }

        if (mIconResourceId != other.mIconResourceId) {
            return false;
        }
        if (mHasCustomPrinterIcon != other.mHasCustomPrinterIcon) {
            return false;
        }
        if (mCustomPrinterIconGen != other.mCustomPrinterIconGen) {
            return false;
        }
        if (mInfoIntent == null) {
            if (other.mInfoIntent != null) {
                return false;
            }
        } else
            if (!mInfoIntent.equals(other.mInfoIntent)) {
                return false;
            }

        return true;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        android.print.PrinterInfo other = ((android.print.PrinterInfo) (obj));
        if (!equalsIgnoringStatus(other)) {
            return false;
        }
        if (mStatus != other.mStatus) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("PrinterInfo{");
        builder.append("id=").append(mId);
        builder.append(", name=").append(mName);
        builder.append(", status=").append(mStatus);
        builder.append(", description=").append(mDescription);
        builder.append(", capabilities=").append(mCapabilities);
        builder.append(", iconResId=").append(mIconResourceId);
        builder.append(", hasCustomPrinterIcon=").append(mHasCustomPrinterIcon);
        builder.append(", customPrinterIconGen=").append(mCustomPrinterIconGen);
        builder.append(", infoIntent=").append(mInfoIntent);
        builder.append("\"}");
        return builder.toString();
    }

    /**
     * Builder for creating of a {@link PrinterInfo}.
     */
    public static final class Builder {
        @android.annotation.NonNull
        private android.print.PrinterId mPrinterId;

        @android.annotation.NonNull
        private java.lang.String mName;

        @android.print.PrinterInfo.Status
        private int mStatus;

        private int mIconResourceId;

        private boolean mHasCustomPrinterIcon;

        private java.lang.String mDescription;

        private android.app.PendingIntent mInfoIntent;

        private android.print.PrinterCapabilitiesInfo mCapabilities;

        private int mCustomPrinterIconGen;

        /**
         * Constructor.
         *
         * @param printerId
         * 		The printer id. Cannot be null.
         * @param name
         * 		The printer name. Cannot be empty.
         * @param status
         * 		The printer status. Must be a valid status.
         * @throws IllegalArgumentException
         * 		If the printer id is null, or the
         * 		printer name is empty or the status is not a valid one.
         */
        public Builder(@android.annotation.NonNull
        android.print.PrinterId printerId, @android.annotation.NonNull
        java.lang.String name, @android.print.PrinterInfo.Status
        int status) {
            mPrinterId = android.print.PrinterInfo.checkPrinterId(printerId);
            mName = android.print.PrinterInfo.checkName(name);
            mStatus = android.print.PrinterInfo.checkStatus(status);
        }

        /**
         * Constructor.
         *
         * @param other
         * 		Other info from which to start building.
         */
        public Builder(@android.annotation.NonNull
        android.print.PrinterInfo other) {
            mPrinterId = other.mId;
            mName = other.mName;
            mStatus = other.mStatus;
            mIconResourceId = other.mIconResourceId;
            mHasCustomPrinterIcon = other.mHasCustomPrinterIcon;
            mDescription = other.mDescription;
            mInfoIntent = other.mInfoIntent;
            mCapabilities = other.mCapabilities;
            mCustomPrinterIconGen = other.mCustomPrinterIconGen;
        }

        /**
         * Sets the printer status.
         *
         * @param status
         * 		The status.
         * @return This builder.
         * @see PrinterInfo#STATUS_IDLE
         * @see PrinterInfo#STATUS_BUSY
         * @see PrinterInfo#STATUS_UNAVAILABLE
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder setStatus(@android.print.PrinterInfo.Status
        int status) {
            mStatus = android.print.PrinterInfo.checkStatus(status);
            return this;
        }

        /**
         * Set a drawable resource as icon for this printer. If no icon is set the printer's
         * service's icon is used for the printer.
         *
         * @param iconResourceId
         * 		The resource ID of the icon.
         * @return This builder.
         * @see PrinterInfo.Builder#setHasCustomPrinterIcon
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder setIconResourceId(@android.annotation.DrawableRes
        int iconResourceId) {
            mIconResourceId = com.android.internal.util.Preconditions.checkArgumentNonnegative(iconResourceId, "iconResourceId can't be negative");
            return this;
        }

        /**
         * Declares that the print service can load a custom per printer's icon. If both
         * {@link PrinterInfo.Builder#setIconResourceId} and a custom icon are set the resource icon
         * is shown while the custom icon loads but then the custom icon is used. If
         * {@link PrinterInfo.Builder#setIconResourceId} is not set the printer's service's icon is
         * shown while loading.
         * <p>
         * The icon is requested asynchronously and only when needed via
         * {@link android.printservice.PrinterDiscoverySession#onRequestCustomPrinterIcon}.
         * </p>
         *
         * @param hasCustomPrinterIcon
         * 		If the printer has a custom icon or not.
         * @return This builder.
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder setHasCustomPrinterIcon(boolean hasCustomPrinterIcon) {
            mHasCustomPrinterIcon = hasCustomPrinterIcon;
            return this;
        }

        /**
         * Sets the <strong>localized</strong> printer name which
         * is shown to the user
         *
         * @param name
         * 		The name.
         * @return This builder.
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder setName(@android.annotation.NonNull
        java.lang.String name) {
            mName = android.print.PrinterInfo.checkName(name);
            return this;
        }

        /**
         * Sets the <strong>localized</strong> printer description
         * which is shown to the user
         *
         * @param description
         * 		The description.
         * @return This builder.
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder setDescription(@android.annotation.NonNull
        java.lang.String description) {
            mDescription = description;
            return this;
        }

        /**
         * Sets the {@link PendingIntent} that launches an activity showing more information about
         * the printer.
         *
         * @param infoIntent
         * 		The {@link PendingIntent intent}.
         * @return This builder.
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder setInfoIntent(@android.annotation.NonNull
        android.app.PendingIntent infoIntent) {
            mInfoIntent = infoIntent;
            return this;
        }

        /**
         * Sets the printer capabilities.
         *
         * @param capabilities
         * 		The capabilities.
         * @return This builder.
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder setCapabilities(@android.annotation.NonNull
        android.print.PrinterCapabilitiesInfo capabilities) {
            mCapabilities = capabilities;
            return this;
        }

        /**
         * Creates a new {@link PrinterInfo}.
         *
         * @return A new {@link PrinterInfo}.
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo build() {
            return new android.print.PrinterInfo(mPrinterId, mName, mStatus, mIconResourceId, mHasCustomPrinterIcon, mDescription, mInfoIntent, mCapabilities, mCustomPrinterIconGen);
        }

        /**
         * Increments the generation number of the custom printer icon. As the {@link PrinterInfo}
         * does not match the previous one anymore, users of the {@link PrinterInfo} will reload the
         * icon if needed.
         *
         * @return This builder.
         * @unknown 
         */
        @android.annotation.NonNull
        public android.print.PrinterInfo.Builder incCustomPrinterIconGen() {
            mCustomPrinterIconGen++;
            return this;
        }
    }

    public static final android.os.Parcelable.Creator<android.print.PrinterInfo> CREATOR = new android.os.Parcelable.Creator<android.print.PrinterInfo>() {
        @java.lang.Override
        public android.print.PrinterInfo createFromParcel(android.os.Parcel parcel) {
            return new android.print.PrinterInfo(parcel);
        }

        @java.lang.Override
        public android.print.PrinterInfo[] newArray(int size) {
            return new android.print.PrinterInfo[size];
        }
    };
}

