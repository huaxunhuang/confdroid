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
public class ParsedPermissionUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermission> parsePermission(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String packageName = pkg.getPackageName();
        android.content.pm.parsing.component.ParsedPermission permission = new android.content.pm.parsing.component.ParsedPermission();
        java.lang.String tag = ("<" + parser.getName()) + ">";
        final android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermission> result;
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermission);
        try {
            result = android.content.pm.parsing.component.ParsedComponentUtils.parseComponent(permission, tag, pkg, sa, useRoundIcon, input, R.styleable.AndroidManifestPermission_banner, R.styleable.AndroidManifestPermission_description, R.styleable.AndroidManifestPermission_icon, R.styleable.AndroidManifestPermission_label, R.styleable.AndroidManifestPermission_logo, R.styleable.AndroidManifestPermission_name, R.styleable.AndroidManifestPermission_roundIcon);
            if (result.isError()) {
                return result;
            }
            if (sa.hasValue(R.styleable.AndroidManifestPermission_backgroundPermission)) {
                if ("android".equals(packageName)) {
                    permission.backgroundPermission = sa.getNonResourceString(R.styleable.AndroidManifestPermission_backgroundPermission);
                } else {
                    android.util.Slog.w(android.content.pm.parsing.component.ParsedPermissionUtils.TAG, (packageName + " defines a background permission. Only the ") + "'android' package can do that.");
                }
            }
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            permission.setGroup(sa.getNonResourceString(R.styleable.AndroidManifestPermission_permissionGroup));
            permission.requestRes = sa.getResourceId(R.styleable.AndroidManifestPermission_request, 0);
            permission.protectionLevel = sa.getInt(R.styleable.AndroidManifestPermission_protectionLevel, android.content.pm.PermissionInfo.PROTECTION_NORMAL);
            permission.flags = sa.getInt(R.styleable.AndroidManifestPermission_permissionFlags, 0);
            // For now only platform runtime permissions can be restricted
            if ((!permission.isRuntime()) || (!"android".equals(permission.getPackageName()))) {
                permission.flags &= ~android.content.pm.PermissionInfo.FLAG_HARD_RESTRICTED;
                permission.flags &= ~android.content.pm.PermissionInfo.FLAG_SOFT_RESTRICTED;
            } else {
                // The platform does not get to specify conflicting permissions
                if (((permission.flags & android.content.pm.PermissionInfo.FLAG_HARD_RESTRICTED) != 0) && ((permission.flags & android.content.pm.PermissionInfo.FLAG_SOFT_RESTRICTED) != 0)) {
                    throw new java.lang.IllegalStateException(("Permission cannot be both soft and hard" + " restricted: ") + permission.getName());
                }
            }
        } finally {
            sa.recycle();
        }
        // TODO(b/135203078): This is impossible because of default value in above getInt
        if (permission.protectionLevel == (-1)) {
            return input.error("<permission> does not specify protectionLevel");
        }
        permission.protectionLevel = android.content.pm.PermissionInfo.fixProtectionLevel(permission.protectionLevel);
        if (permission.getProtectionFlags() != 0) {
            if ((((permission.protectionLevel & android.content.pm.PermissionInfo.PROTECTION_FLAG_INSTANT) == 0) && ((permission.protectionLevel & android.content.pm.PermissionInfo.PROTECTION_FLAG_RUNTIME_ONLY) == 0)) && ((permission.protectionLevel & android.content.pm.PermissionInfo.PROTECTION_MASK_BASE) != android.content.pm.PermissionInfo.PROTECTION_SIGNATURE)) {
                return input.error("<permission>  protectionLevel specifies a non-instant flag " + "but is not based on signature type");
            }
        }
        return android.content.pm.parsing.component.ComponentParseUtils.parseAllMetaData(pkg, res, parser, tag, permission, input);
    }

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermission> parsePermissionTree(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.component.ParsedPermission permission = new android.content.pm.parsing.component.ParsedPermission();
        java.lang.String tag = ("<" + parser.getName()) + ">";
        final android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermission> result;
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionTree);
        try {
            result = /* descriptionAttr */
            android.content.pm.parsing.component.ParsedComponentUtils.parseComponent(permission, tag, pkg, sa, useRoundIcon, input, R.styleable.AndroidManifestPermissionTree_banner, null, R.styleable.AndroidManifestPermissionTree_icon, R.styleable.AndroidManifestPermissionTree_label, R.styleable.AndroidManifestPermissionTree_logo, R.styleable.AndroidManifestPermissionTree_name, R.styleable.AndroidManifestPermissionTree_roundIcon);
            if (result.isError()) {
                return result;
            }
        } finally {
            sa.recycle();
        }
        int index = permission.getName().indexOf('.');
        if (index > 0) {
            index = permission.getName().indexOf('.', index + 1);
        }
        if (index < 0) {
            return input.error("<permission-tree> name has less than three segments: " + permission.getName());
        }
        permission.protectionLevel = android.content.pm.PermissionInfo.PROTECTION_NORMAL;
        permission.tree = true;
        return android.content.pm.parsing.component.ComponentParseUtils.parseAllMetaData(pkg, res, parser, tag, permission, input);
    }

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermissionGroup> parsePermissionGroup(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.component.ParsedPermissionGroup permissionGroup = new android.content.pm.parsing.component.ParsedPermissionGroup();
        java.lang.String tag = ("<" + parser.getName()) + ">";
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestPermissionGroup);
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedPermissionGroup> result = android.content.pm.parsing.component.ParsedComponentUtils.parseComponent(permissionGroup, tag, pkg, sa, useRoundIcon, input, R.styleable.AndroidManifestPermissionGroup_banner, R.styleable.AndroidManifestPermissionGroup_description, R.styleable.AndroidManifestPermissionGroup_icon, R.styleable.AndroidManifestPermissionGroup_label, R.styleable.AndroidManifestPermissionGroup_logo, R.styleable.AndroidManifestPermissionGroup_name, R.styleable.AndroidManifestPermissionGroup_roundIcon);
            if (result.isError()) {
                return result;
            }
            // @formatter:off
            permissionGroup.requestDetailResourceId = sa.getResourceId(R.styleable.AndroidManifestPermissionGroup_requestDetail, 0);
            permissionGroup.backgroundRequestResourceId = sa.getResourceId(R.styleable.AndroidManifestPermissionGroup_backgroundRequest, 0);
            permissionGroup.backgroundRequestDetailResourceId = sa.getResourceId(R.styleable.AndroidManifestPermissionGroup_backgroundRequestDetail, 0);
            permissionGroup.requestRes = sa.getResourceId(R.styleable.AndroidManifestPermissionGroup_request, 0);
            permissionGroup.flags = sa.getInt(R.styleable.AndroidManifestPermissionGroup_permissionGroupFlags, 0);
            permissionGroup.priority = sa.getInt(R.styleable.AndroidManifestPermissionGroup_priority, 0);
            // @formatter:on
        } finally {
            sa.recycle();
        }
        return android.content.pm.parsing.component.ComponentParseUtils.parseAllMetaData(pkg, res, parser, tag, permissionGroup, input);
    }
}

