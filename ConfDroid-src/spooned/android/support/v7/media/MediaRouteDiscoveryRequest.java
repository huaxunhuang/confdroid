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
package android.support.v7.media;


/**
 * Describes the kinds of routes that the media router would like to discover
 * and whether to perform active scanning.
 * <p>
 * This object is immutable once created.
 * </p>
 */
public final class MediaRouteDiscoveryRequest {
    private static final java.lang.String KEY_SELECTOR = "selector";

    private static final java.lang.String KEY_ACTIVE_SCAN = "activeScan";

    private final android.os.Bundle mBundle;

    private android.support.v7.media.MediaRouteSelector mSelector;

    /**
     * Creates a media route discovery request.
     *
     * @param selector
     * 		The route selector that specifies the kinds of routes to discover.
     * @param activeScan
     * 		True if active scanning should be performed.
     */
    public MediaRouteDiscoveryRequest(android.support.v7.media.MediaRouteSelector selector, boolean activeScan) {
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("selector must not be null");
        }
        mBundle = new android.os.Bundle();
        mSelector = selector;
        mBundle.putBundle(android.support.v7.media.MediaRouteDiscoveryRequest.KEY_SELECTOR, selector.asBundle());
        mBundle.putBoolean(android.support.v7.media.MediaRouteDiscoveryRequest.KEY_ACTIVE_SCAN, activeScan);
    }

    private MediaRouteDiscoveryRequest(android.os.Bundle bundle) {
        mBundle = bundle;
    }

    /**
     * Gets the route selector that specifies the kinds of routes to discover.
     */
    public android.support.v7.media.MediaRouteSelector getSelector() {
        ensureSelector();
        return mSelector;
    }

    private void ensureSelector() {
        if (mSelector == null) {
            mSelector = android.support.v7.media.MediaRouteSelector.fromBundle(mBundle.getBundle(android.support.v7.media.MediaRouteDiscoveryRequest.KEY_SELECTOR));
            if (mSelector == null) {
                mSelector = android.support.v7.media.MediaRouteSelector.EMPTY;
            }
        }
    }

    /**
     * Returns true if active scanning should be performed.
     *
     * @see MediaRouter#CALLBACK_FLAG_PERFORM_ACTIVE_SCAN
     */
    public boolean isActiveScan() {
        return mBundle.getBoolean(android.support.v7.media.MediaRouteDiscoveryRequest.KEY_ACTIVE_SCAN);
    }

    /**
     * Returns true if the discovery request has all of the required fields.
     */
    public boolean isValid() {
        ensureSelector();
        return mSelector.isValid();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.support.v7.media.MediaRouteDiscoveryRequest) {
            android.support.v7.media.MediaRouteDiscoveryRequest other = ((android.support.v7.media.MediaRouteDiscoveryRequest) (o));
            return getSelector().equals(other.getSelector()) && (isActiveScan() == other.isActiveScan());
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return getSelector().hashCode() ^ (isActiveScan() ? 1 : 0);
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        result.append("DiscoveryRequest{ selector=").append(getSelector());
        result.append(", activeScan=").append(isActiveScan());
        result.append(", isValid=").append(isValid());
        result.append(" }");
        return result.toString();
    }

    /**
     * Converts this object to a bundle for serialization.
     *
     * @return The contents of the object represented as a bundle.
     */
    public android.os.Bundle asBundle() {
        return mBundle;
    }

    /**
     * Creates an instance from a bundle.
     *
     * @param bundle
     * 		The bundle, or null if none.
     * @return The new instance, or null if the bundle was null.
     */
    public static android.support.v7.media.MediaRouteDiscoveryRequest fromBundle(android.os.Bundle bundle) {
        return bundle != null ? new android.support.v7.media.MediaRouteDiscoveryRequest(bundle) : null;
    }
}

