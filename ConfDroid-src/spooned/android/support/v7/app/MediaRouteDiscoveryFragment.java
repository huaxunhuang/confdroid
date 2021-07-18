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
package android.support.v7.app;


/**
 * Media route discovery fragment.
 * <p>
 * This fragment takes care of registering a callback for media route discovery
 * during the {@link Fragment#onStart onStart()} phase
 * and removing it during the {@link Fragment#onStop onStop()} phase.
 * </p><p>
 * The application must supply a route selector to specify the kinds of routes
 * to discover.  The application may also override {@link #onCreateCallback} to
 * provide the {@link MediaRouter} callback to register.
 * </p><p>
 * Note that the discovery callback makes the application be connected with all the
 * {@link android.support.v7.media.MediaRouteProviderService media route provider services}
 * while it is registered.
 * </p>
 */
public class MediaRouteDiscoveryFragment extends android.support.v4.app.Fragment {
    private final java.lang.String ARGUMENT_SELECTOR = "selector";

    private android.support.v7.media.MediaRouter mRouter;

    private android.support.v7.media.MediaRouteSelector mSelector;

    private android.support.v7.media.MediaRouter.Callback mCallback;

    public MediaRouteDiscoveryFragment() {
    }

    /**
     * Gets the media router instance.
     */
    public android.support.v7.media.MediaRouter getMediaRouter() {
        ensureRouter();
        return mRouter;
    }

    private void ensureRouter() {
        if (mRouter == null) {
            mRouter = android.support.v7.media.MediaRouter.getInstance(getContext());
        }
    }

    /**
     * Gets the media route selector for filtering the routes to be discovered.
     *
     * @return The selector, never null.
     */
    public android.support.v7.media.MediaRouteSelector getRouteSelector() {
        ensureRouteSelector();
        return mSelector;
    }

    /**
     * Sets the media route selector for filtering the routes to be discovered.
     * This method must be called before the fragment is added.
     *
     * @param selector
     * 		The selector to set.
     */
    public void setRouteSelector(android.support.v7.media.MediaRouteSelector selector) {
        if (selector == null) {
            throw new java.lang.IllegalArgumentException("selector must not be null");
        }
        ensureRouteSelector();
        if (!mSelector.equals(selector)) {
            mSelector = selector;
            android.os.Bundle args = getArguments();
            if (args == null) {
                args = new android.os.Bundle();
            }
            args.putBundle(ARGUMENT_SELECTOR, selector.asBundle());
            setArguments(args);
            if (mCallback != null) {
                mRouter.removeCallback(mCallback);
                mRouter.addCallback(mSelector, mCallback, onPrepareCallbackFlags());
            }
        }
    }

    private void ensureRouteSelector() {
        if (mSelector == null) {
            android.os.Bundle args = getArguments();
            if (args != null) {
                mSelector = android.support.v7.media.MediaRouteSelector.fromBundle(args.getBundle(ARGUMENT_SELECTOR));
            }
            if (mSelector == null) {
                mSelector = android.support.v7.media.MediaRouteSelector.EMPTY;
            }
        }
    }

    /**
     * Called to create the {@link android.support.v7.media.MediaRouter.Callback callback}
     * that will be registered.
     * <p>
     * The default callback does nothing.  The application may override this method to
     * supply its own callback.
     * </p>
     *
     * @return The new callback, or null if no callback should be registered.
     */
    public android.support.v7.media.MediaRouter.Callback onCreateCallback() {
        return new android.support.v7.media.MediaRouter.Callback() {};
    }

    /**
     * Called to prepare the callback flags that will be used when the
     * {@link android.support.v7.media.MediaRouter.Callback callback} is registered.
     * <p>
     * The default implementation returns {@link MediaRouter#CALLBACK_FLAG_REQUEST_DISCOVERY}.
     * </p>
     *
     * @return The desired callback flags.
     */
    public int onPrepareCallbackFlags() {
        return android.support.v7.media.MediaRouter.CALLBACK_FLAG_REQUEST_DISCOVERY;
    }

    @java.lang.Override
    public void onStart() {
        super.onStart();
        ensureRouteSelector();
        ensureRouter();
        mCallback = onCreateCallback();
        if (mCallback != null) {
            mRouter.addCallback(mSelector, mCallback, onPrepareCallbackFlags());
        }
    }

    @java.lang.Override
    public void onStop() {
        if (mCallback != null) {
            mRouter.removeCallback(mCallback);
            mCallback = null;
        }
        super.onStop();
    }
}

