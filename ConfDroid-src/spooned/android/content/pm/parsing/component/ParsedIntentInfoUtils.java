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
public class ParsedIntentInfoUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> parseIntentInfo(java.lang.String className, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean allowGlobs, boolean allowAutoVerify, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.component.ParsedIntentInfo intentInfo = new android.content.pm.parsing.component.ParsedIntentInfo();
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestIntentFilter);
        try {
            intentInfo.setPriority(sa.getInt(R.styleable.AndroidManifestIntentFilter_priority, 0));
            intentInfo.setOrder(sa.getInt(R.styleable.AndroidManifestIntentFilter_order, 0));
            android.util.TypedValue v = sa.peekValue(R.styleable.AndroidManifestIntentFilter_label);
            if (v != null) {
                intentInfo.labelRes = v.resourceId;
                if (v.resourceId == 0) {
                    intentInfo.nonLocalizedLabel = v.coerceToString();
                }
            }
            if (android.content.pm.PackageParser.sUseRoundIcon) {
                intentInfo.icon = sa.getResourceId(R.styleable.AndroidManifestIntentFilter_roundIcon, 0);
            }
            if (intentInfo.icon == 0) {
                intentInfo.icon = sa.getResourceId(R.styleable.AndroidManifestIntentFilter_icon, 0);
            }
            if (allowAutoVerify) {
                intentInfo.setAutoVerify(sa.getBoolean(R.styleable.AndroidManifestIntentFilter_autoVerify, false));
            }
        } finally {
            sa.recycle();
        }
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final android.content.pm.parsing.result.ParseResult result;
            java.lang.String nodeName = parser.getName();
            switch (nodeName) {
                case "action" :
                    {
                        java.lang.String value = parser.getAttributeValue(android.content.pm.PackageParser.ANDROID_RESOURCES, "name");
                        if (value == null) {
                            result = input.error("No value supplied for <android:name>");
                        } else
                            if (value.isEmpty()) {
                                intentInfo.addAction(value);
                                // Prior to R, this was not a failure
                                result = input.deferError("No value supplied for <android:name>", android.content.pm.parsing.result.ParseInput.DeferredError.EMPTY_INTENT_ACTION_CATEGORY);
                            } else {
                                intentInfo.addAction(value);
                                result = input.success(null);
                            }

                        break;
                    }
                case "category" :
                    {
                        java.lang.String value = parser.getAttributeValue(android.content.pm.PackageParser.ANDROID_RESOURCES, "name");
                        if (value == null) {
                            result = input.error("No value supplied for <android:name>");
                        } else
                            if (value.isEmpty()) {
                                intentInfo.addCategory(value);
                                // Prior to R, this was not a failure
                                result = input.deferError("No value supplied for <android:name>", android.content.pm.parsing.result.ParseInput.DeferredError.EMPTY_INTENT_ACTION_CATEGORY);
                            } else {
                                intentInfo.addCategory(value);
                                result = input.success(null);
                            }

                        break;
                    }
                case "data" :
                    result = android.content.pm.parsing.component.ParsedIntentInfoUtils.parseData(intentInfo, res, parser, allowGlobs, input);
                    break;
                default :
                    result = android.content.pm.parsing.ParsingUtils.unknownTag("<intent-filter>", pkg, parser, input);
                    break;
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        intentInfo.hasDefault = intentInfo.hasCategory(android.content.Intent.CATEGORY_DEFAULT);
        if (android.content.pm.PackageParser.DEBUG_PARSER) {
            final java.lang.StringBuilder cats = new java.lang.StringBuilder("Intent d=");
            cats.append(intentInfo.isHasDefault());
            cats.append(", cat=");
            final java.util.Iterator<java.lang.String> it = intentInfo.categoriesIterator();
            if (it != null) {
                while (it.hasNext()) {
                    cats.append(' ');
                    cats.append(it.next());
                } 
            }
            android.util.Slog.d(android.content.pm.parsing.component.ParsedIntentInfoUtils.TAG, cats.toString());
        }
        return input.success(intentInfo);
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> parseData(android.content.pm.parsing.component.ParsedIntentInfo intentInfo, android.content.res.Resources resources, android.content.res.XmlResourceParser parser, boolean allowGlobs, android.content.pm.parsing.result.ParseInput input) {
        android.content.res.TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestData);
        try {
            java.lang.String str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_mimeType, 0);
            if (str != null) {
                try {
                    intentInfo.addDataType(str);
                } catch (android.content.IntentFilter.MalformedMimeTypeException e) {
                    return input.error(e.toString());
                }
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_mimeGroup, 0);
            if (str != null) {
                intentInfo.addMimeGroup(str);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_scheme, 0);
            if (str != null) {
                intentInfo.addDataScheme(str);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_ssp, 0);
            if (str != null) {
                intentInfo.addDataSchemeSpecificPart(str, PatternMatcher.PATTERN_LITERAL);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_sspPrefix, 0);
            if (str != null) {
                intentInfo.addDataSchemeSpecificPart(str, PatternMatcher.PATTERN_PREFIX);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_sspPattern, 0);
            if (str != null) {
                if (!allowGlobs) {
                    return input.error("sspPattern not allowed here; ssp must be literal");
                }
                intentInfo.addDataSchemeSpecificPart(str, PatternMatcher.PATTERN_SIMPLE_GLOB);
            }
            java.lang.String host = sa.getNonConfigurationString(R.styleable.AndroidManifestData_host, 0);
            java.lang.String port = sa.getNonConfigurationString(R.styleable.AndroidManifestData_port, 0);
            if (host != null) {
                intentInfo.addDataAuthority(host, port);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_path, 0);
            if (str != null) {
                intentInfo.addDataPath(str, PatternMatcher.PATTERN_LITERAL);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_pathPrefix, 0);
            if (str != null) {
                intentInfo.addDataPath(str, PatternMatcher.PATTERN_PREFIX);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_pathPattern, 0);
            if (str != null) {
                if (!allowGlobs) {
                    return input.error("pathPattern not allowed here; path must be literal");
                }
                intentInfo.addDataPath(str, PatternMatcher.PATTERN_SIMPLE_GLOB);
            }
            str = sa.getNonConfigurationString(R.styleable.AndroidManifestData_pathAdvancedPattern, 0);
            if (str != null) {
                if (!allowGlobs) {
                    return input.error("pathAdvancedPattern not allowed here; path must be literal");
                }
                intentInfo.addDataPath(str, PatternMatcher.PATTERN_ADVANCED_GLOB);
            }
            return input.success(null);
        } finally {
            sa.recycle();
        }
    }
}

