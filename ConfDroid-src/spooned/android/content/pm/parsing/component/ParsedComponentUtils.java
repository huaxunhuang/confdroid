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
class ParsedComponentUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    @android.annotation.NonNull
    @com.android.internal.annotations.VisibleForTesting(visibility = VisibleForTesting.Visibility.PACKAGE)
    static <Component extends android.content.pm.parsing.component.ParsedComponent> android.content.pm.parsing.result.ParseResult<Component> parseComponent(Component component, java.lang.String tag, android.content.pm.parsing.ParsingPackage pkg, android.content.res.TypedArray array, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input, int bannerAttr, @android.annotation.Nullable
    java.lang.Integer descriptionAttr, int iconAttr, int labelAttr, int logoAttr, int nameAttr, int roundIconAttr) {
        java.lang.String name = array.getNonConfigurationString(nameAttr, 0);
        if (android.text.TextUtils.isEmpty(name)) {
            return input.error(tag + " does not specify android:name");
        }
        java.lang.String packageName = pkg.getPackageName();
        java.lang.String className = android.content.pm.parsing.ParsingUtils.buildClassName(packageName, name);
        if (android.content.pm.PackageManager.APP_DETAILS_ACTIVITY_CLASS_NAME.equals(className)) {
            return input.error(tag + " invalid android:name");
        }
        // noinspection ConstantConditions; null check done above with isEmpty
        component.setName(className);
        component.setPackageName(packageName);
        int roundIconVal = (useRoundIcon) ? array.getResourceId(roundIconAttr, 0) : 0;
        if (roundIconVal != 0) {
            component.icon = roundIconVal;
            component.nonLocalizedLabel = null;
        } else {
            int iconVal = array.getResourceId(iconAttr, 0);
            if (iconVal != 0) {
                component.icon = iconVal;
                component.nonLocalizedLabel = null;
            }
        }
        int logoVal = array.getResourceId(logoAttr, 0);
        if (logoVal != 0) {
            component.logo = logoVal;
        }
        int bannerVal = array.getResourceId(bannerAttr, 0);
        if (bannerVal != 0) {
            component.banner = bannerVal;
        }
        if (descriptionAttr != null) {
            component.descriptionRes = array.getResourceId(descriptionAttr, 0);
        }
        android.util.TypedValue v = array.peekValue(labelAttr);
        if (v != null) {
            component.labelRes = v.resourceId;
            if (v.resourceId == 0) {
                component.nonLocalizedLabel = v.coerceToString();
            }
        }
        return input.success(component);
    }

    static android.content.pm.parsing.result.ParseResult<android.os.Bundle> addMetaData(android.content.pm.parsing.component.ParsedComponent component, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources resources, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) {
        android.content.pm.parsing.result.ParseResult<android.os.Bundle> result = android.content.pm.parsing.ParsingPackageUtils.parseMetaData(pkg, resources, parser, component.metaData, input);
        if (result.isError()) {
            return input.error(result);
        }
        android.os.Bundle bundle = result.getResult();
        component.metaData = bundle;
        return input.success(bundle);
    }
}

