/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.pm.parsing.component;


/**
 *
 *
 * @unknown 
 */
class ParsedMainComponentUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    @android.annotation.NonNull
    @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    static <Component extends android.content.pm.parsing.component.ParsedMainComponent> android.content.pm.parsing.result.ParseResult<Component> parseMainComponent(Component component, java.lang.String tag, java.lang.String[] separateProcesses, android.content.pm.parsing.ParsingPackage pkg, android.content.res.TypedArray array, int flags, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input, int bannerAttr, int descriptionAttr, @android.annotation.Nullable
    java.lang.Integer directBootAwareAttr, @android.annotation.Nullable
    java.lang.Integer enabledAttr, int iconAttr, int labelAttr, int logoAttr, int nameAttr, @android.annotation.Nullable
    java.lang.Integer processAttr, int roundIconAttr, @android.annotation.Nullable
    java.lang.Integer splitNameAttr) {
        android.content.pm.parsing.result.ParseResult<Component> result = android.content.pm.parsing.component.ParsedComponentUtils.parseComponent(component, tag, pkg, array, useRoundIcon, input, bannerAttr, descriptionAttr, iconAttr, labelAttr, logoAttr, nameAttr, roundIconAttr);
        if (result.isError()) {
            return result;
        }
        if (directBootAwareAttr != null) {
            component.directBootAware = array.getBoolean(directBootAwareAttr, false);
            if (component.isDirectBootAware()) {
                pkg.setPartiallyDirectBootAware(true);
            }
        }
        if (enabledAttr != null) {
            component.enabled = array.getBoolean(enabledAttr, true);
        }
        if (processAttr != null) {
            java.lang.CharSequence processName;
            if (pkg.getTargetSdkVersion() >= Build.VERSION_CODES.FROYO) {
                processName = array.getNonConfigurationString(processAttr, android.content.res.Configuration.NATIVE_CONFIG_VERSION);
            } else {
                // Some older apps have been seen to use a resource reference
                // here that on older builds was ignored (with a warning).  We
                // need to continue to do this for them so they don't break.
                processName = array.getNonResourceString(processAttr);
            }
            // Backwards-compat, ignore error
            android.content.pm.parsing.result.ParseResult<java.lang.String> processNameResult = android.content.pm.parsing.component.ComponentParseUtils.buildProcessName(pkg.getPackageName(), pkg.getProcessName(), processName, flags, separateProcesses, input);
            if (processNameResult.isError()) {
                return input.error(processNameResult);
            }
            component.setProcessName(processNameResult.getResult());
        }
        if (splitNameAttr != null) {
            component.splitName = array.getNonConfigurationString(splitNameAttr, 0);
        }
        return input.success(component);
    }

    static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> parseIntentFilter(android.content.pm.parsing.component.ParsedMainComponent mainComponent, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources resources, android.content.res.XmlResourceParser parser, boolean visibleToEphemeral, boolean allowGlobs, boolean allowAutoVerify, boolean allowImplicitEphemeralVisibility, boolean failOnNoActions, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> intentResult = android.content.pm.parsing.component.ParsedIntentInfoUtils.parseIntentInfo(mainComponent.getName(), pkg, resources, parser, allowGlobs, allowAutoVerify, input);
        if (intentResult.isError()) {
            return input.error(intentResult);
        }
        android.content.pm.parsing.component.ParsedIntentInfo intent = intentResult.getResult();
        int actionCount = intent.countActions();
        if ((actionCount == 0) && failOnNoActions) {
            android.util.Slog.w(android.content.pm.parsing.component.ParsedMainComponentUtils.TAG, (((("No actions in " + parser.getName()) + " at ") + pkg.getBaseCodePath()) + " ") + parser.getPositionDescription());
            // Backward-compat, do not actually fail
            return input.success(null);
        }
        int intentVisibility;
        if (visibleToEphemeral) {
            intentVisibility = android.content.IntentFilter.VISIBILITY_EXPLICIT;
        } else
            if (allowImplicitEphemeralVisibility && android.content.pm.parsing.component.ComponentParseUtils.isImplicitlyExposedIntent(intent)) {
                intentVisibility = android.content.IntentFilter.VISIBILITY_IMPLICIT;
            } else {
                intentVisibility = android.content.IntentFilter.VISIBILITY_NONE;
            }

        intent.setVisibilityToInstantApp(intentVisibility);
        return input.success(intentResult.getResult());
    }
}

