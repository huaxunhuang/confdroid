/**
 * Copyright (C) 2017 The Android Open Source Project
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
package android.graphics;


/**
 * This class is used for Parceling Typeface object.
 * Note: Typeface object can not be passed over the process boundary.
 *
 * @unknown 
 */
public class LeakyTypefaceStorage {
    private static final java.lang.Object sLock = new java.lang.Object();

    @com.android.internal.annotations.GuardedBy("sLock")
    private static final java.util.ArrayList<android.graphics.Typeface> sStorage = new java.util.ArrayList<>();

    @com.android.internal.annotations.GuardedBy("sLock")
    private static final android.util.ArrayMap<android.graphics.Typeface, java.lang.Integer> sTypefaceMap = new android.util.ArrayMap();

    /**
     * Write typeface to parcel.
     *
     * You can't transfer Typeface to a different process. {@link readTypefaceFromParcel} will
     * return {@code null} if the {@link readTypefaceFromParcel} is called in a different process.
     *
     * @param typeface
     * 		A {@link Typeface} to be written.
     * @param parcel
     * 		A {@link Parcel} object.
     */
    public static void writeTypefaceToParcel(@android.annotation.Nullable
    android.graphics.Typeface typeface, @android.annotation.NonNull
    android.os.Parcel parcel) {
        parcel.writeInt(java.lang.Process.myPid());
        synchronized(android.graphics.LeakyTypefaceStorage.sLock) {
            final int id;
            final java.lang.Integer i = android.graphics.LeakyTypefaceStorage.sTypefaceMap.get(typeface);
            if (i != null) {
                id = i.intValue();
            } else {
                id = android.graphics.LeakyTypefaceStorage.sStorage.size();
                android.graphics.LeakyTypefaceStorage.sStorage.add(typeface);
                android.graphics.LeakyTypefaceStorage.sTypefaceMap.put(typeface, id);
            }
            parcel.writeInt(id);
        }
    }

    /**
     * Read typeface from parcel.
     *
     * If the {@link Typeface} was created in another process, this method returns null.
     *
     * @param parcel
     * 		A {@link Parcel} object
     * @return A {@link Typeface} object.
     */
    @android.annotation.Nullable
    public static android.graphics.Typeface readTypefaceFromParcel(@android.annotation.NonNull
    android.os.Parcel parcel) {
        final int pid = parcel.readInt();
        final int typefaceId = parcel.readInt();
        if (pid != java.lang.Process.myPid()) {
            return null;// The Typeface was created and written in another process.

        }
        synchronized(android.graphics.LeakyTypefaceStorage.sLock) {
            return android.graphics.LeakyTypefaceStorage.sStorage.get(typefaceId);
        }
    }
}

