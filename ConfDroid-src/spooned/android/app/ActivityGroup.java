/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.app;


/**
 * A screen that contains and runs multiple embedded activities.
 *
 * @deprecated Use the new {@link Fragment} and {@link FragmentManager} APIs
instead; these are also
available on older platforms through the Android compatibility package.
 */
@java.lang.Deprecated
public class ActivityGroup extends android.app.Activity {
    private static final java.lang.String STATES_KEY = "android:states";

    static final java.lang.String PARENT_NON_CONFIG_INSTANCE_KEY = "android:parent_non_config_instance";

    /**
     * This field should be made private, so it is hidden from the SDK.
     * {@hide }
     */
    protected android.app.LocalActivityManager mLocalActivityManager;

    public ActivityGroup() {
        this(true);
    }

    public ActivityGroup(boolean singleActivityMode) {
        mLocalActivityManager = new android.app.LocalActivityManager(this, singleActivityMode);
    }

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.os.Bundle states = (savedInstanceState != null) ? ((android.os.Bundle) (savedInstanceState.getBundle(android.app.ActivityGroup.STATES_KEY))) : null;
        mLocalActivityManager.dispatchCreate(states);
    }

    @java.lang.Override
    protected void onResume() {
        super.onResume();
        mLocalActivityManager.dispatchResume();
    }

    @java.lang.Override
    protected void onSaveInstanceState(android.os.Bundle outState) {
        super.onSaveInstanceState(outState);
        android.os.Bundle state = mLocalActivityManager.saveInstanceState();
        if (state != null) {
            outState.putBundle(android.app.ActivityGroup.STATES_KEY, state);
        }
    }

    @java.lang.Override
    protected void onPause() {
        super.onPause();
        mLocalActivityManager.dispatchPause(isFinishing());
    }

    @java.lang.Override
    protected void onStop() {
        super.onStop();
        mLocalActivityManager.dispatchStop();
    }

    @java.lang.Override
    protected void onDestroy() {
        super.onDestroy();
        mLocalActivityManager.dispatchDestroy(isFinishing());
    }

    /**
     * Returns a HashMap mapping from child activity ids to the return values
     * from calls to their onRetainNonConfigurationInstance methods.
     *
     * {@hide }
     */
    @java.lang.Override
    public java.util.HashMap<java.lang.String, java.lang.Object> onRetainNonConfigurationChildInstances() {
        return mLocalActivityManager.dispatchRetainNonConfigurationInstance();
    }

    public android.app.Activity getCurrentActivity() {
        return mLocalActivityManager.getCurrentActivity();
    }

    public final android.app.LocalActivityManager getLocalActivityManager() {
        return mLocalActivityManager;
    }

    @java.lang.Override
    void dispatchActivityResult(java.lang.String who, int requestCode, int resultCode, android.content.Intent data) {
        if (who != null) {
            android.app.Activity act = mLocalActivityManager.getActivity(who);
            /* if (false) Log.v(
            TAG, "Dispatching result: who=" + who + ", reqCode=" + requestCode
            + ", resCode=" + resultCode + ", data=" + data
            + ", rec=" + rec);
             */
            if (act != null) {
                act.onActivityResult(requestCode, resultCode, data);
                return;
            }
        }
        super.dispatchActivityResult(who, requestCode, resultCode, data);
    }
}

