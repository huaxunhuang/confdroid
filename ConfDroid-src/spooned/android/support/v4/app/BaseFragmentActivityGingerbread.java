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
package android.support.v4.app;


/**
 * Base class for {@code FragmentActivity} to be able to use Gingerbread APIs.
 *
 * @unknown 
 */
abstract class BaseFragmentActivityGingerbread extends android.app.Activity {
    // We need to keep track of whether startIntentSenderForResult originated from a Fragment, so we
    // can conditionally check whether the requestCode collides with our reserved ID space for the
    // request index (see above). Unfortunately we can't just call
    // super.startIntentSenderForResult(...) to bypass the check when the call didn't come from a
    // fragment, since we need to use the ActivityCompat version for backward compatibility.
    boolean mStartedIntentSenderFromFragment;

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        if ((android.os.Build.VERSION.SDK_INT < 11) && (getLayoutInflater().getFactory() == null)) {
            // On pre-HC devices we need to manually install ourselves as a Factory.
            // On HC and above, we are automatically installed as a private factory
            getLayoutInflater().setFactory(this);
        }
        super.onCreate(savedInstanceState);
    }

    @java.lang.Override
    public android.view.View onCreateView(java.lang.String name, android.content.Context context, android.util.AttributeSet attrs) {
        final android.view.View v = dispatchFragmentsOnCreateView(null, name, context, attrs);
        if (v == null) {
            return super.onCreateView(name, context, attrs);
        }
        return v;
    }

    abstract android.view.View dispatchFragmentsOnCreateView(android.view.View parent, java.lang.String name, android.content.Context context, android.util.AttributeSet attrs);

    @java.lang.Override
    public void startIntentSenderForResult(android.content.IntentSender intent, int requestCode, @android.support.annotation.Nullable
    android.content.Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags) throws android.content.IntentSender.SendIntentException {
        // If this was started from a Fragment we've already checked the upper 16 bits were not in
        // use, and then repurposed them for the Fragment's index.
        if (!mStartedIntentSenderFromFragment) {
            if (requestCode != (-1)) {
                android.support.v4.app.BaseFragmentActivityGingerbread.checkForValidRequestCode(requestCode);
            }
        }
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags);
    }

    /**
     * Checks whether the given request code is a valid code by masking it with 0xffff0000. Throws
     * an {@link IllegalArgumentException} if the code is not valid.
     */
    static void checkForValidRequestCode(int requestCode) {
        if ((requestCode & 0xffff0000) != 0) {
            throw new java.lang.IllegalArgumentException("Can only use lower 16 bits for requestCode");
        }
    }
}

