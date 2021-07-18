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
package android.view;


/**
 *
 *
 * @unknown 
 */
public class DisplayAdjustments {
    public static final android.view.DisplayAdjustments DEFAULT_DISPLAY_ADJUSTMENTS = new android.view.DisplayAdjustments();

    private volatile android.content.res.CompatibilityInfo mCompatInfo = android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;

    private android.content.res.Configuration mConfiguration;

    @android.annotation.UnsupportedAppUsage
    public DisplayAdjustments() {
    }

    public DisplayAdjustments(android.content.res.Configuration configuration) {
        mConfiguration = new android.content.res.Configuration(configuration != null ? configuration : android.content.res.Configuration.EMPTY);
    }

    public DisplayAdjustments(android.view.DisplayAdjustments daj) {
        setCompatibilityInfo(daj.mCompatInfo);
        mConfiguration = new android.content.res.Configuration(daj.mConfiguration != null ? daj.mConfiguration : android.content.res.Configuration.EMPTY);
    }

    @android.annotation.UnsupportedAppUsage
    public void setCompatibilityInfo(android.content.res.CompatibilityInfo compatInfo) {
        if (this == android.view.DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS) {
            throw new java.lang.IllegalArgumentException("setCompatbilityInfo: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
        }
        if ((compatInfo != null) && (compatInfo.isScalingRequired() || (!compatInfo.supportsScreen()))) {
            mCompatInfo = compatInfo;
        } else {
            mCompatInfo = android.content.res.CompatibilityInfo.DEFAULT_COMPATIBILITY_INFO;
        }
    }

    public android.content.res.CompatibilityInfo getCompatibilityInfo() {
        return mCompatInfo;
    }

    public void setConfiguration(android.content.res.Configuration configuration) {
        if (this == android.view.DisplayAdjustments.DEFAULT_DISPLAY_ADJUSTMENTS) {
            throw new java.lang.IllegalArgumentException("setConfiguration: Cannot modify DEFAULT_DISPLAY_ADJUSTMENTS");
        }
        mConfiguration.setTo(configuration != null ? configuration : android.content.res.Configuration.EMPTY);
    }

    @android.annotation.UnsupportedAppUsage
    public android.content.res.Configuration getConfiguration() {
        return mConfiguration;
    }

    @java.lang.Override
    public int hashCode() {
        int hash = 17;
        hash = (hash * 31) + java.util.Objects.hashCode(mCompatInfo);
        hash = (hash * 31) + java.util.Objects.hashCode(mConfiguration);
        return hash;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.view.DisplayAdjustments)) {
            return false;
        }
        android.view.DisplayAdjustments daj = ((android.view.DisplayAdjustments) (o));
        return java.util.Objects.equals(daj.mCompatInfo, mCompatInfo) && java.util.Objects.equals(daj.mConfiguration, mConfiguration);
    }
}

