/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.support.customtabs;


/**
 * A class to be used for Custom Tabs related communication. Clients that want to launch Custom Tabs
 * can use this class exclusively to handle all related communication.
 */
public final class CustomTabsSession {
    private static final java.lang.String TAG = "CustomTabsSession";

    private final android.support.customtabs.ICustomTabsService mService;

    private final android.support.customtabs.ICustomTabsCallback mCallback;

    private final android.content.ComponentName mComponentName;

    /* package */
    CustomTabsSession(android.support.customtabs.ICustomTabsService service, android.support.customtabs.ICustomTabsCallback callback, android.content.ComponentName componentName) {
        mService = service;
        mCallback = callback;
        mComponentName = componentName;
    }

    /**
     * Tells the browser of a likely future navigation to a URL.
     * The most likely URL has to be specified first. Optionally, a list of
     * other likely URLs can be provided. They are treated as less likely than
     * the first one, and have to be sorted in decreasing priority order. These
     * additional URLs may be ignored.
     * All previous calls to this method will be deprioritized.
     *
     * @param url
     * 		Most likely URL.
     * @param extras
     * 		Reserved for future use.
     * @param otherLikelyBundles
     * 		Other likely destinations, sorted in decreasing
     * 		likelihood order. Inside each Bundle, the client should provide a
     * 		{@link Uri} using {@link CustomTabsService#KEY_URL} with
     * 		{@link Bundle#putParcelable(String, android.os.Parcelable)}.
     * @return true for success.
     */
    public boolean mayLaunchUrl(android.net.Uri url, android.os.Bundle extras, java.util.List<android.os.Bundle> otherLikelyBundles) {
        try {
            return mService.mayLaunchUrl(mCallback, url, extras, otherLikelyBundles);
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    /**
     * This sets the action button on the toolbar with ID
     * {@link CustomTabsIntent#TOOLBAR_ACTION_BUTTON_ID}.
     *
     * @param icon
     * 		The new icon of the action button.
     * @param description
     * 		Content description of the action button.
     * @see {@link CustomTabsSession#setToolbarItem(int, Bitmap, String)}
     */
    public boolean setActionButton(@android.support.annotation.NonNull
    android.graphics.Bitmap icon, @android.support.annotation.NonNull
    java.lang.String description) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelable(android.support.customtabs.CustomTabsIntent.KEY_ICON, icon);
        bundle.putString(android.support.customtabs.CustomTabsIntent.KEY_DESCRIPTION, description);
        android.os.Bundle metaBundle = new android.os.Bundle();
        metaBundle.putBundle(android.support.customtabs.CustomTabsIntent.EXTRA_ACTION_BUTTON_BUNDLE, bundle);
        try {
            return mService.updateVisuals(mCallback, metaBundle);
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    /**
     * Updates the {@link RemoteViews} of the secondary toolbar in an existing custom tab session.
     *
     * @param remoteViews
     * 		The updated {@link RemoteViews} that will be shown in secondary toolbar.
     * 		If null, the current secondary toolbar will be dismissed.
     * @param clickableIDs
     * 		The ids of clickable views. The onClick event of these views will be
     * 		handled by custom tabs.
     * @param pendingIntent
     * 		The {@link PendingIntent} that will be sent when the user clicks on one
     * 		of the {@link View}s in clickableIDs.
     */
    public boolean setSecondaryToolbarViews(@android.support.annotation.Nullable
    android.widget.RemoteViews remoteViews, @android.support.annotation.Nullable
    int[] clickableIDs, @android.support.annotation.Nullable
    android.app.PendingIntent pendingIntent) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putParcelable(android.support.customtabs.CustomTabsIntent.EXTRA_REMOTEVIEWS, remoteViews);
        bundle.putIntArray(android.support.customtabs.CustomTabsIntent.EXTRA_REMOTEVIEWS_VIEW_IDS, clickableIDs);
        bundle.putParcelable(android.support.customtabs.CustomTabsIntent.EXTRA_REMOTEVIEWS_PENDINGINTENT, pendingIntent);
        try {
            return mService.updateVisuals(mCallback, bundle);
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    /**
     * Updates the visuals for toolbar items. Will only succeed if a custom tab created using this
     * session is in the foreground in browser and the given id is valid.
     *
     * @param id
     * 		The id for the item to update.
     * @param icon
     * 		The new icon of the toolbar item.
     * @param description
     * 		Content description of the toolbar item.
     * @return Whether the update succeeded.
     * @deprecated Use
    CustomTabsSession#setSecondaryToolbarViews(RemoteViews, int[], PendingIntent)
     */
    @java.lang.Deprecated
    public boolean setToolbarItem(int id, @android.support.annotation.NonNull
    android.graphics.Bitmap icon, @android.support.annotation.NonNull
    java.lang.String description) {
        android.os.Bundle bundle = new android.os.Bundle();
        bundle.putInt(android.support.customtabs.CustomTabsIntent.KEY_ID, id);
        bundle.putParcelable(android.support.customtabs.CustomTabsIntent.KEY_ICON, icon);
        bundle.putString(android.support.customtabs.CustomTabsIntent.KEY_DESCRIPTION, description);
        android.os.Bundle metaBundle = new android.os.Bundle();
        metaBundle.putBundle(android.support.customtabs.CustomTabsIntent.EXTRA_ACTION_BUTTON_BUNDLE, bundle);
        try {
            return mService.updateVisuals(mCallback, metaBundle);
        } catch (android.os.RemoteException e) {
            return false;
        }
    }

    /* package */
    android.os.IBinder getBinder() {
        return mCallback.asBinder();
    }

    /* package */
    android.content.ComponentName getComponentName() {
        return mComponentName;
    }
}

