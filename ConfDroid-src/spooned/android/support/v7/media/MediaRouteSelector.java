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
 * Describes the capabilities of routes that applications would like to discover and use.
 * <p>
 * This object is immutable once created using a {@link Builder} instance.
 * </p>
 *
 * <h3>Example</h3>
 * <pre>
 * MediaRouteSelector selectorBuilder = new MediaRouteSelector.Builder()
 *         .addControlCategory(MediaControlIntent.CATEGORY_LIVE_VIDEO)
 *         .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
 *         .build();
 *
 * MediaRouter router = MediaRouter.getInstance(context);
 * router.addCallback(selector, callback, MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY);
 * </pre>
 */
public final class MediaRouteSelector {
    static final java.lang.String KEY_CONTROL_CATEGORIES = "controlCategories";

    private final android.os.Bundle mBundle;

    java.util.List<java.lang.String> mControlCategories;

    /**
     * An empty media route selector that will not match any routes.
     */
    public static final android.support.v7.media.MediaRouteSelector EMPTY = new android.support.v7.media.MediaRouteSelector(new android.os.Bundle(), null);

    MediaRouteSelector(android.os.Bundle bundle, java.util.List<java.lang.String> controlCategories) {
        mBundle = bundle;
        mControlCategories = controlCategories;
    }

    /**
     * Gets the list of {@link MediaControlIntent media control categories} in the selector.
     *
     * @return The list of categories.
     */
    public java.util.List<java.lang.String> getControlCategories() {
        ensureControlCategories();
        return mControlCategories;
    }

    void ensureControlCategories() {
        if (mControlCategories == null) {
            mControlCategories = mBundle.getStringArrayList(android.support.v7.media.MediaRouteSelector.KEY_CONTROL_CATEGORIES);
            if ((mControlCategories == null) || mControlCategories.isEmpty()) {
                mControlCategories = java.util.Collections.<java.lang.String>emptyList();
            }
        }
    }

    /**
     * Returns true if the selector contains the specified category.
     *
     * @param category
     * 		The category to check.
     * @return True if the category is present.
     */
    public boolean hasControlCategory(java.lang.String category) {
        if (category != null) {
            ensureControlCategories();
            final int categoryCount = mControlCategories.size();
            for (int i = 0; i < categoryCount; i++) {
                if (mControlCategories.get(i).equals(category)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if the selector matches at least one of the specified control filters.
     *
     * @param filters
     * 		The list of control filters to consider.
     * @return True if a match is found.
     */
    public boolean matchesControlFilters(java.util.List<android.content.IntentFilter> filters) {
        if (filters != null) {
            ensureControlCategories();
            final int categoryCount = mControlCategories.size();
            if (categoryCount != 0) {
                final int filterCount = filters.size();
                for (int i = 0; i < filterCount; i++) {
                    final android.content.IntentFilter filter = filters.get(i);
                    if (filter != null) {
                        for (int j = 0; j < categoryCount; j++) {
                            if (filter.hasCategory(mControlCategories.get(j))) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if this selector contains all of the capabilities described
     * by the specified selector.
     *
     * @param selector
     * 		The selector to be examined.
     * @return True if this selector contains all of the capabilities described
    by the specified selector.
     */
    public boolean contains(android.support.v7.media.MediaRouteSelector selector) {
        if (selector != null) {
            ensureControlCategories();
            selector.ensureControlCategories();
            return mControlCategories.containsAll(selector.mControlCategories);
        }
        return false;
    }

    /**
     * Returns true if the selector does not specify any capabilities.
     */
    public boolean isEmpty() {
        ensureControlCategories();
        return mControlCategories.isEmpty();
    }

    /**
     * Returns true if the selector has all of the required fields.
     */
    public boolean isValid() {
        ensureControlCategories();
        if (mControlCategories.contains(null)) {
            return false;
        }
        return true;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (o instanceof android.support.v7.media.MediaRouteSelector) {
            android.support.v7.media.MediaRouteSelector other = ((android.support.v7.media.MediaRouteSelector) (o));
            ensureControlCategories();
            other.ensureControlCategories();
            return mControlCategories.equals(other.mControlCategories);
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        ensureControlCategories();
        return mControlCategories.hashCode();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder result = new java.lang.StringBuilder();
        result.append("MediaRouteSelector{ ");
        result.append("controlCategories=").append(java.util.Arrays.toString(getControlCategories().toArray()));
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
    public static android.support.v7.media.MediaRouteSelector fromBundle(@android.support.annotation.Nullable
    android.os.Bundle bundle) {
        return bundle != null ? new android.support.v7.media.MediaRouteSelector(bundle, null) : null;
    }

    /**
     * Builder for {@link MediaRouteSelector media route selectors}.
     */
    public static final class Builder {
        private java.util.ArrayList<java.lang.String> mControlCategories;

        /**
         * Creates an empty media route selector builder.
         */
        public Builder() {
        }

        /**
         * Creates a media route selector descriptor builder whose initial contents are
         * copied from an existing selector.
         */
        public Builder(@android.support.annotation.NonNull
        android.support.v7.media.MediaRouteSelector selector) {
            if (selector == null) {
                throw new java.lang.IllegalArgumentException("selector must not be null");
            }
            selector.ensureControlCategories();
            if (!selector.mControlCategories.isEmpty()) {
                mControlCategories = new java.util.ArrayList<java.lang.String>(selector.mControlCategories);
            }
        }

        /**
         * Adds a {@link MediaControlIntent media control category} to the builder.
         *
         * @param category
         * 		The category to add to the set of desired capabilities, such as
         * 		{@link MediaControlIntent#CATEGORY_LIVE_AUDIO}.
         * @return The builder instance for chaining.
         */
        @android.support.annotation.NonNull
        public android.support.v7.media.MediaRouteSelector.Builder addControlCategory(@android.support.annotation.NonNull
        java.lang.String category) {
            if (category == null) {
                throw new java.lang.IllegalArgumentException("category must not be null");
            }
            if (mControlCategories == null) {
                mControlCategories = new java.util.ArrayList<java.lang.String>();
            }
            if (!mControlCategories.contains(category)) {
                mControlCategories.add(category);
            }
            return this;
        }

        /**
         * Adds a list of {@link MediaControlIntent media control categories} to the builder.
         *
         * @param categories
         * 		The list categories to add to the set of desired capabilities,
         * 		such as {@link MediaControlIntent#CATEGORY_LIVE_AUDIO}.
         * @return The builder instance for chaining.
         */
        @android.support.annotation.NonNull
        public android.support.v7.media.MediaRouteSelector.Builder addControlCategories(@android.support.annotation.NonNull
        java.util.Collection<java.lang.String> categories) {
            if (categories == null) {
                throw new java.lang.IllegalArgumentException("categories must not be null");
            }
            if (!categories.isEmpty()) {
                for (java.lang.String category : categories) {
                    addControlCategory(category);
                }
            }
            return this;
        }

        /**
         * Adds the contents of an existing media route selector to the builder.
         *
         * @param selector
         * 		The media route selector whose contents are to be added.
         * @return The builder instance for chaining.
         */
        @android.support.annotation.NonNull
        public android.support.v7.media.MediaRouteSelector.Builder addSelector(@android.support.annotation.NonNull
        android.support.v7.media.MediaRouteSelector selector) {
            if (selector == null) {
                throw new java.lang.IllegalArgumentException("selector must not be null");
            }
            addControlCategories(selector.getControlCategories());
            return this;
        }

        /**
         * Builds the {@link MediaRouteSelector media route selector}.
         */
        @android.support.annotation.NonNull
        public android.support.v7.media.MediaRouteSelector build() {
            if (mControlCategories == null) {
                return android.support.v7.media.MediaRouteSelector.EMPTY;
            }
            android.os.Bundle bundle = new android.os.Bundle();
            bundle.putStringArrayList(android.support.v7.media.MediaRouteSelector.KEY_CONTROL_CATEGORIES, mControlCategories);
            return new android.support.v7.media.MediaRouteSelector(bundle, mControlCategories);
        }
    }
}

