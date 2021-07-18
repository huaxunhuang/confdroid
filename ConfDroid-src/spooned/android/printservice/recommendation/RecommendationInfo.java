/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.printservice.recommendation;


/**
 * A recommendation to install a {@link PrintService print service}.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public final class RecommendationInfo implements android.os.Parcelable {
    /**
     * Package name of the print service.
     */
    @android.annotation.NonNull
    private final java.lang.CharSequence mPackageName;

    /**
     * Display name of the print service.
     */
    @android.annotation.NonNull
    private final java.lang.CharSequence mName;

    /**
     * Number of printers the print service would discover if installed.
     */
    @android.annotation.IntRange(from = 0)
    private final int mNumDiscoveredPrinters;

    /**
     * If the service detects printer from multiple vendors.
     */
    private final boolean mRecommendsMultiVendorService;

    /**
     * Create a new recommendation.
     *
     * @param packageName
     * 		Package name of the print service
     * @param name
     * 		Display name of the print service
     * @param numDiscoveredPrinters
     * 		Number of printers the print service would discover if
     * 		installed
     * @param recommendsMultiVendorService
     * 		If the service detects printer from multiple vendor
     */
    public RecommendationInfo(@android.annotation.NonNull
    java.lang.CharSequence packageName, @android.annotation.NonNull
    java.lang.CharSequence name, @android.annotation.IntRange(from = 0)
    int numDiscoveredPrinters, boolean recommendsMultiVendorService) {
        mPackageName = com.android.internal.util.Preconditions.checkStringNotEmpty(packageName);
        mName = com.android.internal.util.Preconditions.checkStringNotEmpty(name);
        mNumDiscoveredPrinters = com.android.internal.util.Preconditions.checkArgumentNonnegative(numDiscoveredPrinters);
        mRecommendsMultiVendorService = recommendsMultiVendorService;
    }

    /**
     * Create a new recommendation from a parcel.
     *
     * @param parcel
     * 		The parcel containing the data
     * @see #CREATOR
     */
    private RecommendationInfo(@android.annotation.NonNull
    android.os.Parcel parcel) {
        this(parcel.readCharSequence(), parcel.readCharSequence(), parcel.readInt(), parcel.readByte() != 0);
    }

    /**
     *
     *
     * @return The package name the recommendations recommends.
     */
    public java.lang.CharSequence getPackageName() {
        return mPackageName;
    }

    /**
     *
     *
     * @return Whether the recommended print service detects printers of more than one vendor.
     */
    public boolean recommendsMultiVendorService() {
        return mRecommendsMultiVendorService;
    }

    /**
     *
     *
     * @return The number of printer the print service would detect.
     */
    public int getNumDiscoveredPrinters() {
        return mNumDiscoveredPrinters;
    }

    /**
     *
     *
     * @return The name of the recommended print service.
     */
    public java.lang.CharSequence getName() {
        return mName;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeCharSequence(mPackageName);
        dest.writeCharSequence(mName);
        dest.writeInt(mNumDiscoveredPrinters);
        dest.writeByte(((byte) (mRecommendsMultiVendorService ? 1 : 0)));
    }

    /**
     * Utility class used to create new print service recommendation objects from parcels.
     *
     * @see #RecommendationInfo(Parcel)
     */
    public static final android.os.Parcelable.Creator<android.printservice.recommendation.RecommendationInfo> CREATOR = new android.os.Parcelable.Creator<android.printservice.recommendation.RecommendationInfo>() {
        @java.lang.Override
        public android.printservice.recommendation.RecommendationInfo createFromParcel(android.os.Parcel in) {
            return new android.printservice.recommendation.RecommendationInfo(in);
        }

        @java.lang.Override
        public android.printservice.recommendation.RecommendationInfo[] newArray(int size) {
            return new android.printservice.recommendation.RecommendationInfo[size];
        }
    };
}

