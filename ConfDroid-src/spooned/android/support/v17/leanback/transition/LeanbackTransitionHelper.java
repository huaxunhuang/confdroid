/**
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package android.support.v17.leanback.transition;


/**
 * Helper class to load Leanback specific transition.
 *
 * @unknown 
 */
@android.support.annotation.RestrictTo(android.support.annotation.RestrictTo.Scope.GROUP_ID)
public class LeanbackTransitionHelper {
    interface LeanbackTransitionHelperVersion {
        java.lang.Object loadTitleInTransition(android.content.Context context);

        java.lang.Object loadTitleOutTransition(android.content.Context context);
    }

    /* Kitkat does not allow load custom transition from resource, calling
    LeanbackTransitionHelperKitKat to build custom transition in code.
     */
    static class LeanbackTransitionHelperKitKatImpl implements android.support.v17.leanback.transition.LeanbackTransitionHelper.LeanbackTransitionHelperVersion {
        @java.lang.Override
        public java.lang.Object loadTitleInTransition(android.content.Context context) {
            return android.support.v17.leanback.transition.LeanbackTransitionHelperKitKat.loadTitleInTransition(context);
        }

        @java.lang.Override
        public java.lang.Object loadTitleOutTransition(android.content.Context context) {
            return android.support.v17.leanback.transition.LeanbackTransitionHelperKitKat.loadTitleOutTransition(context);
        }
    }

    /* Load transition from resource or just return stub for API17. */
    static class LeanbackTransitionHelperDefault implements android.support.v17.leanback.transition.LeanbackTransitionHelper.LeanbackTransitionHelperVersion {
        @java.lang.Override
        public java.lang.Object loadTitleInTransition(android.content.Context context) {
            return android.support.v17.leanback.transition.TransitionHelper.loadTransition(context, R.transition.lb_title_in);
        }

        @java.lang.Override
        public java.lang.Object loadTitleOutTransition(android.content.Context context) {
            return android.support.v17.leanback.transition.TransitionHelper.loadTransition(context, R.transition.lb_title_out);
        }
    }

    static android.support.v17.leanback.transition.LeanbackTransitionHelper.LeanbackTransitionHelperVersion sImpl;

    static {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            android.support.v17.leanback.transition.LeanbackTransitionHelper.sImpl = new android.support.v17.leanback.transition.LeanbackTransitionHelper.LeanbackTransitionHelperDefault();
        } else
            if (android.os.Build.VERSION.SDK_INT >= 19) {
                android.support.v17.leanback.transition.LeanbackTransitionHelper.sImpl = new android.support.v17.leanback.transition.LeanbackTransitionHelper.LeanbackTransitionHelperKitKatImpl();
            } else {
                // Helper will create a stub object for transition in this case.
                android.support.v17.leanback.transition.LeanbackTransitionHelper.sImpl = new android.support.v17.leanback.transition.LeanbackTransitionHelper.LeanbackTransitionHelperDefault();
            }

    }

    public static java.lang.Object loadTitleInTransition(android.content.Context context) {
        return android.support.v17.leanback.transition.LeanbackTransitionHelper.sImpl.loadTitleInTransition(context);
    }

    public static java.lang.Object loadTitleOutTransition(android.content.Context context) {
        return android.support.v17.leanback.transition.LeanbackTransitionHelper.sImpl.loadTitleOutTransition(context);
    }
}

