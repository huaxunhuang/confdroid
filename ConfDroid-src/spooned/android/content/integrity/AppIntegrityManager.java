/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.content.integrity;


/**
 * Class for pushing rules used to check the integrity of app installs.
 *
 * <p>Note: applications using methods of this class must be a system app and have their package
 * name whitelisted as an integrity rule provider. Otherwise a {@link SecurityException} will be
 * thrown.
 *
 * @unknown 
 */
@android.annotation.TestApi
@android.annotation.SystemApi
@android.annotation.SystemService(android.content.Context.APP_INTEGRITY_SERVICE)
public class AppIntegrityManager {
    /**
     * The operation succeeded.
     */
    public static final int STATUS_SUCCESS = 0;

    /**
     * The operation failed.
     */
    public static final int STATUS_FAILURE = 1;

    /**
     * Current status of an operation. Will be one of {@link #STATUS_SUCCESS}, {@link #STATUS_FAILURE}.
     *
     * <p>More information about a status may be available through additional extras; see the
     * individual status documentation for details.
     *
     * @see android.content.Intent#getIntExtra(String, int)
     */
    public static final java.lang.String EXTRA_STATUS = "android.content.integrity.extra.STATUS";

    android.content.integrity.IAppIntegrityManager mManager;

    /**
     *
     *
     * @unknown 
     */
    public AppIntegrityManager(android.content.integrity.IAppIntegrityManager manager) {
        mManager = manager;
    }

    /**
     * Update the rules to evaluate during install time.
     *
     * @param updateRequest
     * 		request containing the data of the rule set update
     * @param statusReceiver
     * 		Called when the state of the session changes. Intents sent to this
     * 		receiver contain {@link #EXTRA_STATUS}. Refer to the individual status codes on how to
     * 		handle them.
     */
    public void updateRuleSet(@android.annotation.NonNull
    android.content.integrity.RuleSet updateRequest, @android.annotation.NonNull
    android.content.IntentSender statusReceiver) {
        try {
            mManager.updateRuleSet(updateRequest.getVersion(), new android.content.pm.ParceledListSlice(updateRequest.getRules()), statusReceiver);
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /**
     * Get the current version of the rule set.
     */
    @android.annotation.NonNull
    public java.lang.String getCurrentRuleSetVersion() {
        try {
            return mManager.getCurrentRuleSetVersion();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /**
     * Get the name of the package that provided the current rule set.
     */
    @android.annotation.NonNull
    public java.lang.String getCurrentRuleSetProvider() {
        try {
            return mManager.getCurrentRuleSetProvider();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /**
     * Get current RuleSet on device.
     *
     * <p>Warning: this method is only used for tests.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.NonNull
    public android.content.integrity.RuleSet getCurrentRuleSet() {
        try {
            android.content.pm.ParceledListSlice<android.content.integrity.Rule> rules = mManager.getCurrentRules();
            java.lang.String version = mManager.getCurrentRuleSetVersion();
            return new android.content.integrity.RuleSet.Builder().setVersion(version).addRules(rules.getList()).build();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }

    /**
     * Get the package names of all whitelisted rule providers.
     *
     * <p>Warning: this method is only used for tests.
     *
     * @unknown 
     */
    @android.annotation.TestApi
    @android.annotation.NonNull
    public java.util.List<java.lang.String> getWhitelistedRuleProviders() {
        try {
            return mManager.getWhitelistedRuleProviders();
        } catch (android.os.RemoteException e) {
            throw e.rethrowAsRuntimeException();
        }
    }
}

