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
 * Abstract service class for implementing Custom Tabs related functionality. The service should
 * be responding to the action ACTION_CUSTOM_TABS_CONNECTION. This class should be used by
 * implementers that want to provide Custom Tabs functionality, not by clients that want to launch
 * Custom Tabs.
 */
public abstract class CustomTabsService extends android.app.Service {
    /**
     * The Intent action that a CustomTabsService must respond to.
     */
    public static final java.lang.String ACTION_CUSTOM_TABS_CONNECTION = "android.support.customtabs.action.CustomTabsService";

    /**
     * For {@link CustomTabsService#mayLaunchUrl} calls that wants to specify more than one url,
     * this key can be used with {@link Bundle#putParcelable(String, android.os.Parcelable)}
     * to insert a new url to each bundle inside list of bundles.
     */
    public static final java.lang.String KEY_URL = "android.support.customtabs.otherurls.URL";

    private final java.util.Map<android.os.IBinder, android.os.IBinder.DeathRecipient> mDeathRecipientMap = new android.support.v4.util.ArrayMap<>();

    private ICustomTabsService.Stub mBinder = new android.support.customtabs.ICustomTabsService.Stub() {
        @java.lang.Override
        public boolean warmup(long flags) {
            return android.support.customtabs.CustomTabsService.this.warmup(flags);
        }

        @java.lang.Override
        public boolean newSession(android.support.customtabs.ICustomTabsCallback callback) {
            final android.support.customtabs.CustomTabsSessionToken sessionToken = new android.support.customtabs.CustomTabsSessionToken(callback);
            try {
                android.os.IBinder.DeathRecipient deathRecipient = new android.os.IBinder.DeathRecipient() {
                    @java.lang.Override
                    public void binderDied() {
                        cleanUpSession(sessionToken);
                    }
                };
                synchronized(mDeathRecipientMap) {
                    callback.asBinder().linkToDeath(deathRecipient, 0);
                    mDeathRecipientMap.put(callback.asBinder(), deathRecipient);
                }
                return android.support.customtabs.CustomTabsService.this.newSession(sessionToken);
            } catch (android.os.RemoteException e) {
                return false;
            }
        }

        @java.lang.Override
        public boolean mayLaunchUrl(android.support.customtabs.ICustomTabsCallback callback, android.net.Uri url, android.os.Bundle extras, java.util.List<android.os.Bundle> otherLikelyBundles) {
            return android.support.customtabs.CustomTabsService.this.mayLaunchUrl(new android.support.customtabs.CustomTabsSessionToken(callback), url, extras, otherLikelyBundles);
        }

        @java.lang.Override
        public android.os.Bundle extraCommand(java.lang.String commandName, android.os.Bundle args) {
            return android.support.customtabs.CustomTabsService.this.extraCommand(commandName, args);
        }

        @java.lang.Override
        public boolean updateVisuals(android.support.customtabs.ICustomTabsCallback callback, android.os.Bundle bundle) {
            return android.support.customtabs.CustomTabsService.this.updateVisuals(new android.support.customtabs.CustomTabsSessionToken(callback), bundle);
        }
    };

    @java.lang.Override
    public android.os.IBinder onBind(android.content.Intent intent) {
        return mBinder;
    }

    /**
     * Called when the client side {@link IBinder} for this {@link CustomTabsSessionToken} is dead.
     * Can also be used to clean up {@link DeathRecipient} instances allocated for the given token.
     *
     * @param sessionToken
     * 		The session token for which the {@link DeathRecipient} call has been
     * 		received.
     * @return Whether the clean up was successful. Multiple calls with two tokens holdings the
    same binder will return false.
     */
    protected boolean cleanUpSession(android.support.customtabs.CustomTabsSessionToken sessionToken) {
        try {
            synchronized(mDeathRecipientMap) {
                android.os.IBinder binder = sessionToken.getCallbackBinder();
                android.os.IBinder.DeathRecipient deathRecipient = mDeathRecipientMap.get(binder);
                binder.unlinkToDeath(deathRecipient, 0);
                mDeathRecipientMap.remove(binder);
            }
        } catch (java.util.NoSuchElementException e) {
            return false;
        }
        return true;
    }

    /**
     * Warms up the browser process asynchronously.
     *
     * @param flags
     * 		Reserved for future use.
     * @return Whether warmup was/had been completed successfully. Multiple successful
    calls will return true.
     */
    protected abstract boolean warmup(long flags);

    /**
     * Creates a new session through an ICustomTabsService with the optional callback. This session
     * can be used to associate any related communication through the service with an intent and
     * then later with a Custom Tab. The client can then send later service calls or intents to
     * through same session-intent-Custom Tab association.
     *
     * @param sessionToken
     * 		Session token to be used as a unique identifier. This also has access
     * 		to the {@link CustomTabsCallback} passed from the client side through
     * 		{@link CustomTabsSessionToken#getCallback()}.
     * @return Whether a new session was successfully created.
     */
    protected abstract boolean newSession(android.support.customtabs.CustomTabsSessionToken sessionToken);

    /**
     * Tells the browser of a likely future navigation to a URL.
     *
     * The method {@link CustomTabsService#warmup(long)} has to be called beforehand.
     * The most likely URL has to be specified explicitly. Optionally, a list of
     * other likely URLs can be provided. They are treated as less likely than
     * the first one, and have to be sorted in decreasing priority order. These
     * additional URLs may be ignored.
     * All previous calls to this method will be deprioritized.
     *
     * @param sessionToken
     * 		The unique identifier for the session. Can not be null.
     * @param url
     * 		Most likely URL.
     * @param extras
     * 		Reserved for future use.
     * @param otherLikelyBundles
     * 		Other likely destinations, sorted in decreasing
     * 		likelihood order. Each Bundle has to provide a url.
     * @return Whether the call was successful.
     */
    protected abstract boolean mayLaunchUrl(android.support.customtabs.CustomTabsSessionToken sessionToken, android.net.Uri url, android.os.Bundle extras, java.util.List<android.os.Bundle> otherLikelyBundles);

    /**
     * Unsupported commands that may be provided by the implementation.
     *
     * <p>
     * <strong>Note:</strong>Clients should <strong>never</strong> rely on this method to have a
     * defined behavior, as it is entirely implementation-defined and not supported.
     *
     * <p> This call can be used by implementations to add extra commands, for testing or
     * experimental purposes.
     *
     * @param commandName
     * 		Name of the extra command to execute.
     * @param args
     * 		Arguments for the command
     * @return The result {@link Bundle}, or null.
     */
    protected abstract android.os.Bundle extraCommand(java.lang.String commandName, android.os.Bundle args);

    /**
     * Updates the visuals of custom tabs for the given session. Will only succeed if the given
     * session matches the currently active one.
     *
     * @param sessionToken
     * 		The currently active session that the custom tab belongs to.
     * @param bundle
     * 		The action button configuration bundle. This bundle should be constructed
     * 		with the same structure in {@link CustomTabsIntent.Builder}.
     * @return Whether the operation was successful.
     */
    protected abstract boolean updateVisuals(android.support.customtabs.CustomTabsSessionToken sessionToken, android.os.Bundle bundle);
}

