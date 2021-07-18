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
 * Media route chooser dialog fragment.
 * <p>
 * Creates a {@link MediaRouteChooserDialog}.  The application may subclass
 * this dialog fragment to customize the media route chooser dialog.
 * </p>
 */
public class MediaRouteChooserDialogFragment extends android.support.v4.app.DialogFragment {
    private final java.lang.String ARGUMENT_SELECTOR = "selector";

    private android.support.v7.app.MediaRouteChooserDialog mDialog;

    private android.support.v7.media.MediaRouteSelector mSelector;

    /**
     * Creates a media route chooser dialog fragment.
     * <p>
     * All subclasses of this class must also possess a default constructor.
     * </p>
     */
    public MediaRouteChooserDialogFragment() {
        setCancelable(true);
    }

    /**
     * Gets the media route selector for filtering the routes that the user can select.
     *
     * @return The selector, never null.
     */
    public android.support.v7.media.MediaRouteSelector getRouteSelector() {
        ensureRouteSelector();
        return mSelector;
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
     * Sets the media route selector for filtering the routes that the user can select.
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
            android.support.v7.app.MediaRouteChooserDialog dialog = ((android.support.v7.app.MediaRouteChooserDialog) (getDialog()));
            if (dialog != null) {
                dialog.setRouteSelector(selector);
            }
        }
    }

    /**
     * Called when the chooser dialog is being created.
     * <p>
     * Subclasses may override this method to customize the dialog.
     * </p>
     */
    public android.support.v7.app.MediaRouteChooserDialog onCreateChooserDialog(android.content.Context context, android.os.Bundle savedInstanceState) {
        return new android.support.v7.app.MediaRouteChooserDialog(context);
    }

    @java.lang.Override
    public android.app.Dialog onCreateDialog(android.os.Bundle savedInstanceState) {
        mDialog = onCreateChooserDialog(getContext(), savedInstanceState);
        mDialog.setRouteSelector(getRouteSelector());
        return mDialog;
    }

    @java.lang.Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mDialog != null) {
            mDialog.updateLayout();
        }
    }
}

