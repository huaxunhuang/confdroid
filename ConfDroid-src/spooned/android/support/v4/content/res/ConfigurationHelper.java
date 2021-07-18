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
package android.support.v4.content.res;


/**
 * Helper class which allows access to properties of {@link android.content.res.Configuration} in
 * a backward compatible fashion.
 */
public final class ConfigurationHelper {
    private static final android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl IMPL;

    static {
        final int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk >= 17) {
            IMPL = new android.support.v4.content.res.ConfigurationHelper.JellybeanMr1Impl();
        } else
            if (sdk >= 13) {
                IMPL = new android.support.v4.content.res.ConfigurationHelper.HoneycombMr2Impl();
            } else {
                IMPL = new android.support.v4.content.res.ConfigurationHelper.GingerbreadImpl();
            }

    }

    private ConfigurationHelper() {
    }

    private interface ConfigurationHelperImpl {
        int getScreenHeightDp(@android.support.annotation.NonNull
        android.content.res.Resources resources);

        int getScreenWidthDp(@android.support.annotation.NonNull
        android.content.res.Resources resources);

        int getSmallestScreenWidthDp(@android.support.annotation.NonNull
        android.content.res.Resources resources);

        int getDensityDpi(@android.support.annotation.NonNull
        android.content.res.Resources resources);
    }

    private static class GingerbreadImpl implements android.support.v4.content.res.ConfigurationHelper.ConfigurationHelperImpl {
        GingerbreadImpl() {
        }

        @java.lang.Override
        public int getScreenHeightDp(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperGingerbread.getScreenHeightDp(resources);
        }

        @java.lang.Override
        public int getScreenWidthDp(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperGingerbread.getScreenWidthDp(resources);
        }

        @java.lang.Override
        public int getSmallestScreenWidthDp(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperGingerbread.getSmallestScreenWidthDp(resources);
        }

        @java.lang.Override
        public int getDensityDpi(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperGingerbread.getDensityDpi(resources);
        }
    }

    private static class HoneycombMr2Impl extends android.support.v4.content.res.ConfigurationHelper.GingerbreadImpl {
        HoneycombMr2Impl() {
        }

        @java.lang.Override
        public int getScreenHeightDp(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperHoneycombMr2.getScreenHeightDp(resources);
        }

        @java.lang.Override
        public int getScreenWidthDp(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperHoneycombMr2.getScreenWidthDp(resources);
        }

        @java.lang.Override
        public int getSmallestScreenWidthDp(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperHoneycombMr2.getSmallestScreenWidthDp(resources);
        }
    }

    private static class JellybeanMr1Impl extends android.support.v4.content.res.ConfigurationHelper.HoneycombMr2Impl {
        JellybeanMr1Impl() {
        }

        @java.lang.Override
        public int getDensityDpi(@android.support.annotation.NonNull
        android.content.res.Resources resources) {
            return android.support.v4.content.res.ConfigurationHelperJellybeanMr1.getDensityDpi(resources);
        }
    }

    /**
     * Returns the current height of the available screen space, in dp units.
     *
     * <p>Uses {@code Configuration.screenHeightDp} when available, otherwise an approximation
     * is computed and returned.</p>
     */
    public static int getScreenHeightDp(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        return android.support.v4.content.res.ConfigurationHelper.IMPL.getScreenHeightDp(resources);
    }

    /**
     * Returns the current width of the available screen space, in dp units.
     *
     * <p>Uses {@code Configuration.screenWidthDp} when available, otherwise an approximation
     * is computed and returned.</p>
     */
    public static int getScreenWidthDp(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        return android.support.v4.content.res.ConfigurationHelper.IMPL.getScreenWidthDp(resources);
    }

    /**
     * Returns The smallest screen size an application will see in normal operation, in dp units.
     *
     * <p>Uses {@code Configuration.smallestScreenWidthDp} when available, otherwise an
     * approximation is computed and returned.</p>
     */
    public static int getSmallestScreenWidthDp(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        return android.support.v4.content.res.ConfigurationHelper.IMPL.getSmallestScreenWidthDp(resources);
    }

    /**
     * Returns the target screen density being rendered to.
     *
     * <p>Uses {@code Configuration.densityDpi} when available, otherwise an approximation
     * is computed and returned.</p>
     */
    public static int getDensityDpi(@android.support.annotation.NonNull
    android.content.res.Resources resources) {
        return android.support.v4.content.res.ConfigurationHelper.IMPL.getDensityDpi(resources);
    }
}

