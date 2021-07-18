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
package android.media;


/**
 * Encapsulates initialization data required by a {@link MediaDrm} instance.
 */
public abstract class DrmInitData {
    /**
     * Prevent public constuctor access
     */
    /* package private */
    DrmInitData() {
    }

    /**
     * Retrieves initialization data for a given DRM scheme, specified by its UUID.
     *
     * @param schemeUuid
     * 		The DRM scheme's UUID.
     * @return The initialization data for the scheme, or null if the scheme is not supported.
     */
    public abstract android.media.DrmInitData.SchemeInitData get(java.util.UUID schemeUuid);

    /**
     * Scheme initialization data.
     */
    public static final class SchemeInitData {
        /**
         * The mimeType of {@link #data}.
         */
        public final java.lang.String mimeType;

        /**
         * The initialization data.
         */
        public final byte[] data;

        /**
         *
         *
         * @param mimeType
         * 		The mimeType of the initialization data.
         * @param data
         * 		The initialization data.
         * @unknown 
         */
        public SchemeInitData(java.lang.String mimeType, byte[] data) {
            this.mimeType = mimeType;
            this.data = data;
        }

        @java.lang.Override
        public boolean equals(java.lang.Object obj) {
            if (!(obj instanceof android.media.DrmInitData.SchemeInitData)) {
                return false;
            }
            if (obj == this) {
                return true;
            }
            android.media.DrmInitData.SchemeInitData other = ((android.media.DrmInitData.SchemeInitData) (obj));
            return mimeType.equals(other.mimeType) && java.util.Arrays.equals(data, other.data);
        }

        @java.lang.Override
        public int hashCode() {
            return mimeType.hashCode() + (31 * java.util.Arrays.hashCode(data));
        }
    }
}

