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
public class ParsedProcessUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingUtils.TAG;

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<java.util.Set<java.lang.String>> parseDenyPermission(java.util.Set<java.lang.String> perms, android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestDenyPermission);
        try {
            java.lang.String perm = sa.getNonConfigurationString(R.styleable.AndroidManifestDenyPermission_name, 0);
            if ((perm != null) && perm.equals(android.content.pm.parsing.component.android.Manifest.permission.INTERNET)) {
                perms = com.android.internal.util.CollectionUtils.add(perms, perm);
            }
        } finally {
            sa.recycle();
        }
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        return input.success(perms);
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<java.util.Set<java.lang.String>> parseAllowPermission(java.util.Set<java.lang.String> perms, android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestAllowPermission);
        try {
            java.lang.String perm = sa.getNonConfigurationString(R.styleable.AndroidManifestAllowPermission_name, 0);
            if ((perm != null) && perm.equals(android.content.pm.parsing.component.android.Manifest.permission.INTERNET)) {
                perms = com.android.internal.util.CollectionUtils.remove(perms, perm);
            }
        } finally {
            sa.recycle();
        }
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        return input.success(perms);
    }

    @android.annotation.NonNull
    private static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProcess> parseProcess(java.util.Set<java.lang.String> perms, java.lang.String[] separateProcesses, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.component.ParsedProcess proc = new android.content.pm.parsing.component.ParsedProcess();
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestProcess);
        try {
            if (perms != null) {
                proc.deniedPermissions = new android.util.ArraySet(perms);
            }
            proc.name = sa.getNonConfigurationString(R.styleable.AndroidManifestProcess_process, 0);
            android.content.pm.parsing.result.ParseResult<java.lang.String> processNameResult = android.content.pm.parsing.component.ComponentParseUtils.buildProcessName(pkg.getPackageName(), pkg.getPackageName(), proc.name, flags, separateProcesses, input);
            if (processNameResult.isError()) {
                return input.error(processNameResult);
            }
            proc.name = processNameResult.getResult();
            if ((proc.name == null) || (proc.name.length() <= 0)) {
                return input.error("<process> does not specify android:process");
            }
            proc.gwpAsanMode = sa.getInt(R.styleable.AndroidManifestProcess_gwpAsanMode, -1);
        } finally {
            sa.recycle();
        }
        int type;
        final int innerDepth = parser.getDepth();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            android.content.pm.parsing.result.ParseResult<?> result;
            java.lang.String tagName = parser.getName();
            switch (tagName) {
                case "deny-permission" :
                    android.content.pm.parsing.result.ParseResult<java.util.Set<java.lang.String>> denyResult = android.content.pm.parsing.component.ParsedProcessUtils.parseDenyPermission(proc.deniedPermissions, res, parser, input);
                    result = denyResult;
                    if (denyResult.isSuccess()) {
                        proc.deniedPermissions = denyResult.getResult();
                    }
                    break;
                case "allow-permission" :
                    android.content.pm.parsing.result.ParseResult<java.util.Set<java.lang.String>> allowResult = android.content.pm.parsing.component.ParsedProcessUtils.parseAllowPermission(proc.deniedPermissions, res, parser, input);
                    result = allowResult;
                    if (allowResult.isSuccess()) {
                        proc.deniedPermissions = allowResult.getResult();
                    }
                    break;
                default :
                    result = android.content.pm.parsing.ParsingUtils.unknownTag("<process>", pkg, parser, input);
                    break;
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        return input.success(proc);
    }

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.util.ArrayMap<java.lang.String, android.content.pm.parsing.component.ParsedProcess>> parseProcesses(java.lang.String[] separateProcesses, android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, int flags, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.util.Set<java.lang.String> deniedPerms = null;
        android.util.ArrayMap<java.lang.String, android.content.pm.parsing.component.ParsedProcess> processes = new android.util.ArrayMap();
        int type;
        final int innerDepth = parser.getDepth();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            android.content.pm.parsing.result.ParseResult<?> result;
            java.lang.String tagName = parser.getName();
            switch (tagName) {
                case "deny-permission" :
                    android.content.pm.parsing.result.ParseResult<java.util.Set<java.lang.String>> denyResult = android.content.pm.parsing.component.ParsedProcessUtils.parseDenyPermission(deniedPerms, res, parser, input);
                    result = denyResult;
                    if (denyResult.isSuccess()) {
                        deniedPerms = denyResult.getResult();
                    }
                    break;
                case "allow-permission" :
                    android.content.pm.parsing.result.ParseResult<java.util.Set<java.lang.String>> allowResult = android.content.pm.parsing.component.ParsedProcessUtils.parseAllowPermission(deniedPerms, res, parser, input);
                    result = allowResult;
                    if (allowResult.isSuccess()) {
                        deniedPerms = allowResult.getResult();
                    }
                    break;
                case "process" :
                    android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedProcess> processResult = android.content.pm.parsing.component.ParsedProcessUtils.parseProcess(deniedPerms, separateProcesses, pkg, res, parser, flags, input);
                    result = processResult;
                    if (processResult.isSuccess()) {
                        android.content.pm.parsing.component.ParsedProcess process = processResult.getResult();
                        if (processes.put(process.name, process) != null) {
                            result = input.error(("<process> specified existing name '" + process.name) + "'");
                        }
                    }
                    break;
                default :
                    result = android.content.pm.parsing.ParsingUtils.unknownTag("<processes>", pkg, parser, input);
                    break;
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        return input.success(processes);
    }
}

