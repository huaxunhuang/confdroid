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
public class ParsedProviderUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProvider> parseProvider(java.lang.String[] separateProcesses, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String authority;
        boolean visibleToEphemeral;
        final int targetSdkVersion = pkg.getTargetSdkVersion();
        final java.lang.String packageName = pkg.getPackageName();
        final android.content.pm.parsing.component.ParsedProvider provider = new android.content.pm.parsing.component.ParsedProvider();
        final java.lang.String tag = parser.getName();
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestProvider);
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProvider> result = android.content.pm.parsing.component.ParsedMainComponentUtils.parseMainComponent(provider, tag, separateProcesses, pkg, sa, flags, useRoundIcon, input, R.styleable.AndroidManifestProvider_banner, R.styleable.AndroidManifestProvider_description, R.styleable.AndroidManifestProvider_directBootAware, R.styleable.AndroidManifestProvider_enabled, R.styleable.AndroidManifestProvider_icon, R.styleable.AndroidManifestProvider_label, R.styleable.AndroidManifestProvider_logo, R.styleable.AndroidManifestProvider_name, R.styleable.AndroidManifestProvider_process, R.styleable.AndroidManifestProvider_roundIcon, R.styleable.AndroidManifestProvider_splitName);
            if (result.isError()) {
                return result;
            }
            authority = sa.getNonConfigurationString(R.styleable.AndroidManifestProvider_authorities, 0);
            // For compatibility, applications targeting API level 16 or lower
            // should have their content providers exported by default, unless they
            // specify otherwise.
            provider.exported = sa.getBoolean(R.styleable.AndroidManifestProvider_exported, targetSdkVersion < Build.VERSION_CODES.JELLY_BEAN_MR1);
            provider.syncable = sa.getBoolean(R.styleable.AndroidManifestProvider_syncable, false);
            java.lang.String permission = sa.getNonConfigurationString(R.styleable.AndroidManifestProvider_permission, 0);
            java.lang.String readPermission = sa.getNonConfigurationString(R.styleable.AndroidManifestProvider_readPermission, 0);
            if (readPermission == null) {
                readPermission = permission;
            }
            if (readPermission == null) {
                provider.setReadPermission(pkg.getPermission());
            } else {
                provider.setReadPermission(readPermission);
            }
            java.lang.String writePermission = sa.getNonConfigurationString(R.styleable.AndroidManifestProvider_writePermission, 0);
            if (writePermission == null) {
                writePermission = permission;
            }
            if (writePermission == null) {
                provider.setWritePermission(pkg.getPermission());
            } else {
                provider.setWritePermission(writePermission);
            }
            provider.grantUriPermissions = sa.getBoolean(R.styleable.AndroidManifestProvider_grantUriPermissions, false);
            provider.forceUriPermissions = sa.getBoolean(R.styleable.AndroidManifestProvider_forceUriPermissions, false);
            provider.multiProcess = sa.getBoolean(R.styleable.AndroidManifestProvider_multiprocess, false);
            provider.initOrder = sa.getInt(R.styleable.AndroidManifestProvider_initOrder, 0);
            provider.flags |= android.content.pm.parsing.component.ComponentParseUtils.flag(android.content.pm.ProviderInfo.FLAG_SINGLE_USER, R.styleable.AndroidManifestProvider_singleUser, sa);
            visibleToEphemeral = sa.getBoolean(R.styleable.AndroidManifestProvider_visibleToInstantApps, false);
            if (visibleToEphemeral) {
                provider.flags |= android.content.pm.ProviderInfo.FLAG_VISIBLE_TO_INSTANT_APP;
                pkg.setVisibleToInstantApps(true);
            }
        } finally {
            sa.recycle();
        }
        if (pkg.isCantSaveState()) {
            // A heavy-weight application can not have providers in its main process
            if (java.util.Objects.equals(provider.getProcessName(), packageName)) {
                return input.error("Heavy-weight applications can not have providers" + " in main process");
            }
        }
        if (authority == null) {
            return input.error("<provider> does not include authorities attribute");
        }
        if (authority.length() <= 0) {
            return input.error("<provider> has empty authorities attribute");
        }
        provider.setAuthority(authority);
        return android.content.pm.parsing.component.ParsedProviderUtils.parseProviderTags(pkg, tag, res, parser, visibleToEphemeral, provider, input);
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProvider> parseProviderTags(android.content.pm.parsing.ParsingPackage pkg, java.lang.String tag, android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean visibleToEphemeral, android.content.pm.parsing.component.ParsedProvider provider, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            java.lang.String name = parser.getName();
            final android.content.pm.parsing.result.ParseResult result;
            switch (name) {
                case "intent-filter" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedIntentInfo> intentResult = /* allowGlobs */
                    /* allowAutoVerify */
                    /* allowImplicitEphemeralVisibility */
                    /* failOnNoActions */
                    android.content.pm.parsing.component.ParsedMainComponentUtils.parseIntentFilter(provider, pkg, res, parser, visibleToEphemeral, true, false, false, false, input);
                    result = intentResult;
                    if (intentResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedIntentInfo intent = intentResult.getResult();
                        provider.order = java.lang.Math.max(intent.getOrder(), provider.order);
                        provider.addIntent(intent);
                    }
                    break;
                case "meta-data" :
                    result = android.content.pm.parsing.component.ParsedComponentUtils.addMetaData(provider, pkg, res, parser, input);
                    break;
                case "grant-uri-permission" :
                    {
                        result = android.content.pm.parsing.component.ParsedProviderUtils.parseGrantUriPermission(provider, pkg, res, parser, input);
                        break;
                    }
                case "path-permission" :
                    {
                        result = android.content.pm.parsing.component.ParsedProviderUtils.parsePathPermission(provider, pkg, res, parser, input);
                        break;
                    }
                default :
                    result = android.content.pm.parsing.ParsingUtils.unknownTag(tag, pkg, parser, input);
                    break;
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        return input.success(provider);
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProvider> parseGrantUriPermission(android.content.pm.parsing.component.ParsedProvider provider, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources resources, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) {
        android.content.res.TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestGrantUriPermission);
        try {
            java.lang.String name = parser.getName();
            // Pattern has priority over prefix over literal path
            android.os.PatternMatcher pa = null;
            java.lang.String str = sa.getNonConfigurationString(R.styleable.AndroidManifestGrantUriPermission_pathPattern, 0);
            if (str != null) {
                pa = new android.os.PatternMatcher(str, android.os.PatternMatcher.PATTERN_SIMPLE_GLOB);
            } else {
                str = sa.getNonConfigurationString(R.styleable.AndroidManifestGrantUriPermission_pathPrefix, 0);
                if (str != null) {
                    pa = new android.os.PatternMatcher(str, android.os.PatternMatcher.PATTERN_PREFIX);
                } else {
                    str = sa.getNonConfigurationString(R.styleable.AndroidManifestGrantUriPermission_path, 0);
                    if (str != null) {
                        pa = new android.os.PatternMatcher(str, android.os.PatternMatcher.PATTERN_LITERAL);
                    }
                }
            }
            if (pa != null) {
                if (provider.uriPermissionPatterns == null) {
                    provider.uriPermissionPatterns = new android.os.PatternMatcher[1];
                    provider.uriPermissionPatterns[0] = pa;
                } else {
                    final int N = provider.uriPermissionPatterns.length;
                    android.os.PatternMatcher[] newp = new android.os.PatternMatcher[N + 1];
                    java.lang.System.arraycopy(provider.uriPermissionPatterns, 0, newp, 0, N);
                    newp[N] = pa;
                    provider.uriPermissionPatterns = newp;
                }
                provider.grantUriPermissions = true;
            } else {
                if (android.content.pm.PackageParser.RIGID_PARSER) {
                    return input.error("No path, pathPrefix, or pathPattern for <path-permission>");
                }
                android.util.Slog.w(android.content.pm.parsing.component.ParsedProviderUtils.TAG, (((("Unknown element under <path-permission>: " + name) + " at ") + pkg.getBaseCodePath()) + " ") + parser.getPositionDescription());
            }
            return input.success(provider);
        } finally {
            sa.recycle();
        }
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProvider> parsePathPermission(android.content.pm.parsing.component.ParsedProvider provider, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources resources, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) {
        android.content.res.TypedArray sa = resources.obtainAttributes(parser, R.styleable.AndroidManifestPathPermission);
        try {
            java.lang.String name = parser.getName();
            java.lang.String permission = sa.getNonConfigurationString(R.styleable.AndroidManifestPathPermission_permission, 0);
            java.lang.String readPermission = sa.getNonConfigurationString(R.styleable.AndroidManifestPathPermission_readPermission, 0);
            if (readPermission == null) {
                readPermission = permission;
            }
            java.lang.String writePermission = sa.getNonConfigurationString(R.styleable.AndroidManifestPathPermission_writePermission, 0);
            if (writePermission == null) {
                writePermission = permission;
            }
            boolean havePerm = false;
            if (readPermission != null) {
                readPermission = readPermission.intern();
                havePerm = true;
            }
            if (writePermission != null) {
                writePermission = writePermission.intern();
                havePerm = true;
            }
            if (!havePerm) {
                if (android.content.pm.PackageParser.RIGID_PARSER) {
                    return input.error("No readPermission or writePermission for <path-permission>");
                }
                android.util.Slog.w(android.content.pm.parsing.component.ParsedProviderUtils.TAG, (((("No readPermission or writePermission for <path-permission>: " + name) + " at ") + pkg.getBaseCodePath()) + " ") + parser.getPositionDescription());
                return input.success(provider);
            }
            // Advanced has priority over simply over prefix over literal
            android.content.pm.PathPermission pa = null;
            java.lang.String path = sa.getNonConfigurationString(R.styleable.AndroidManifestPathPermission_pathAdvancedPattern, 0);
            if (path != null) {
                pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_ADVANCED_GLOB, readPermission, writePermission);
            } else {
                path = sa.getNonConfigurationString(R.styleable.AndroidManifestPathPermission_pathPattern, 0);
                if (path != null) {
                    pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_SIMPLE_GLOB, readPermission, writePermission);
                } else {
                    path = sa.getNonConfigurationString(R.styleable.AndroidManifestPathPermission_pathPrefix, 0);
                    if (path != null) {
                        pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_PREFIX, readPermission, writePermission);
                    } else {
                        path = sa.getNonConfigurationString(R.styleable.AndroidManifestPathPermission_path, 0);
                        if (path != null) {
                            pa = new android.content.pm.PathPermission(path, android.os.PatternMatcher.PATTERN_LITERAL, readPermission, writePermission);
                        }
                    }
                }
            }
            if (pa != null) {
                if (provider.pathPermissions == null) {
                    provider.pathPermissions = new android.content.pm.PathPermission[1];
                    provider.pathPermissions[0] = pa;
                } else {
                    final int N = provider.pathPermissions.length;
                    android.content.pm.PathPermission[] newp = new android.content.pm.PathPermission[N + 1];
                    java.lang.System.arraycopy(provider.pathPermissions, 0, newp, 0, N);
                    newp[N] = pa;
                    provider.pathPermissions = newp;
                }
            } else {
                if (android.content.pm.PackageParser.RIGID_PARSER) {
                    return input.error("No path, pathPrefix, or pathPattern for <path-permission>");
                }
                android.util.Slog.w(android.content.pm.parsing.component.ParsedProviderUtils.TAG, (((("No path, pathPrefix, or pathPattern for <path-permission>: " + name) + " at ") + pkg.getBaseCodePath()) + " ") + parser.getPositionDescription());
            }
            return input.success(provider);
        } finally {
            sa.recycle();
        }
    }
}

