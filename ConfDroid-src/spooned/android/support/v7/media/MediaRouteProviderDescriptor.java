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
 * Describes the state of a media route provider and the routes that it publishes.
 * <p>
 * This object is immutable once created using a {@link Builder} instance.
 * </p>
 */
public final class MediaRouteProviderDescriptor {
    static final java.lang.String KEY_ROUTES = "routes";

    final android.os.Bundle mBundle;

    java.util.List<android.support.v7.media.MediaRouteDescriptor> mRoutes;

    MediaRouteProviderDescriptor(android.os.Bundle bundle, java.util.List<android.support.v7.media.MediaRouteDescriptor> routes) {
        mBundle = bundle;
        mRoutes = routes;
    }

    /**
     * Gets the list of all routes that this provider has published.
     */
    public java.util.List<android.support.v7.media.MediaRouteDescriptor> getRoutes() {
        ensureRoutes();
        return mRoutes;
    }

    void ensureRoutes() {
        if (mRoutes == null) {
            java.util.ArrayList<android.os.Bundle> routeBundles = mBundle.<android.os.Bundle>getParcelableArrayList(android.support.v7.media.MediaRouteProviderDescriptor.KEY_ROUTES);
            if ((routeBundles == null) || routeBundles.isEmpty()) {
                mRoutes = java.util.Collections.<android.support.v7.media.MediaRouteDescriptor>emptyList();
            } else {
                final int count = routeBundles.size();
                mRoutes = new java.util.ArrayList<android.support.v7.media.MediaRouteDescriptor>(count);
                for (int i = 0; i < count; i++) {
                    mRoutes.add(android.support.v7.media.MediaRouteDescriptor.fromBundle(routeBundles.get(i)));
                }
            }
        }
    }

    /**
     * Returns true if the route provider descriptor and all of the routes that
     * it contains have all of the required fields.
     * <p>
     * This verification is deep.  If the provider descriptor is known to be
     * valid then it is not necessary to call {@link #isValid} on each of its routes.
     * </p>
     */
    public boolean isValid() {
        ensureRoutes();
        final int routeCount = mRoutes.size();
        for (int i = 0; i < routeCount; i++) {
            android.support.v7.media.MediaRouteDescriptor route = mRoutes.get(i);
            if ((route == null) || (!route.isValid())) {
                return false;
            }
        }
        return true;
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        result.append("MediaRouteProviderDescriptor{ ");
        result.append("routes=").append(java.util.Arrays.toString(getRoutes().toArray()));
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
    public static android.support.v7.media.MediaRouteProviderDescriptor fromBundle(android.os.Bundle bundle) {
        return bundle != null ? new android.support.v7.media.MediaRouteProviderDescriptor(bundle, null) : null;
    }

    /**
     * Builder for {@link MediaRouteProviderDescriptor media route provider descriptors}.
     */
    public static final class Builder {
        private final android.os.Bundle mBundle;

        private java.util.ArrayList<android.support.v7.media.MediaRouteDescriptor> mRoutes;

        /**
         * Creates an empty media route provider descriptor builder.
         */
        public Builder() {
            mBundle = new android.os.Bundle();
        }

        /**
         * Creates a media route provider descriptor builder whose initial contents are
         * copied from an existing descriptor.
         */
        public Builder(android.support.v7.media.MediaRouteProviderDescriptor descriptor) {
            if (descriptor == null) {
                throw new java.lang.IllegalArgumentException("descriptor must not be null");
            }
            mBundle = new android.os.Bundle(descriptor.mBundle);
            descriptor.ensureRoutes();
            if (!descriptor.mRoutes.isEmpty()) {
                mRoutes = new java.util.ArrayList<android.support.v7.media.MediaRouteDescriptor>(descriptor.mRoutes);
            }
        }

        /**
         * Adds a route.
         */
        public android.support.v7.media.MediaRouteProviderDescriptor.Builder addRoute(android.support.v7.media.MediaRouteDescriptor route) {
            if (route == null) {
                throw new java.lang.IllegalArgumentException("route must not be null");
            }
            if (mRoutes == null) {
                mRoutes = new java.util.ArrayList<android.support.v7.media.MediaRouteDescriptor>();
            } else
                if (mRoutes.contains(route)) {
                    throw new java.lang.IllegalArgumentException("route descriptor already added");
                }

            mRoutes.add(route);
            return this;
        }

        /**
         * Adds a list of routes.
         */
        public android.support.v7.media.MediaRouteProviderDescriptor.Builder addRoutes(java.util.Collection<android.support.v7.media.MediaRouteDescriptor> routes) {
            if (routes == null) {
                throw new java.lang.IllegalArgumentException("routes must not be null");
            }
            if (!routes.isEmpty()) {
                for (android.support.v7.media.MediaRouteDescriptor route : routes) {
                    addRoute(route);
                }
            }
            return this;
        }

        /**
         * Builds the {@link MediaRouteProviderDescriptor media route provider descriptor}.
         */
        public android.support.v7.media.MediaRouteProviderDescriptor build() {
            if (mRoutes != null) {
                final int count = mRoutes.size();
                java.util.ArrayList<android.os.Bundle> routeBundles = new java.util.ArrayList<android.os.Bundle>(count);
                for (int i = 0; i < count; i++) {
                    routeBundles.add(mRoutes.get(i).asBundle());
                }
                mBundle.putParcelableArrayList(android.support.v7.media.MediaRouteProviderDescriptor.KEY_ROUTES, routeBundles);
            }
            return new android.support.v7.media.MediaRouteProviderDescriptor(mBundle, mRoutes);
        }
    }
}

