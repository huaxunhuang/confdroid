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
public class ComponentParseUtils {
    private static final java.lang.String TAG = android.content.pm.parsing.ParsingPackageUtils.TAG;

    public static boolean isImplicitlyExposedIntent(android.content.pm.parsing.component.ParsedIntentInfo intentInfo) {
        return ((intentInfo.hasCategory(android.content.Intent.CATEGORY_BROWSABLE) || intentInfo.hasAction(android.content.Intent.ACTION_SEND)) || intentInfo.hasAction(android.content.Intent.ACTION_SENDTO)) || intentInfo.hasAction(android.content.Intent.ACTION_SEND_MULTIPLE);
    }

    static <Component extends android.content.pm.parsing.component.ParsedComponent> android.content.pm.parsing.result.ParseResult<Component> parseAllMetaData(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, java.lang.String tag, Component component, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        final int depth = parser.getDepth();
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > depth))) {
            if (type != org.xmlpull.v1.XmlPullParser.START_TAG) {
                continue;
            }
            final android.content.pm.parsing.result.ParseResult result;
            if ("meta-data".equals(parser.getName())) {
                result = android.content.pm.parsing.component.ParsedComponentUtils.addMetaData(component, pkg, res, parser, input);
            } else {
                result = android.content.pm.parsing.ParsingUtils.unknownTag(tag, pkg, parser, input);
            }
            if (result.isError()) {
                return input.error(result);
            }
        } 
        return input.success(component);
    }

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<java.lang.String> buildProcessName(@android.annotation.NonNull
    java.lang.String pkg, java.lang.String defProc, java.lang.CharSequence procSeq, int flags, java.lang.String[] separateProcesses, android.content.pm.parsing.result.ParseInput input) {
        if (((flags & android.content.pm.PackageParser.PARSE_IGNORE_PROCESSES) != 0) && (!"system".contentEquals(procSeq))) {
            return input.success(defProc != null ? defProc : pkg);
        }
        if (separateProcesses != null) {
            for (int i = separateProcesses.length - 1; i >= 0; i--) {
                java.lang.String sp = separateProcesses[i];
                if ((sp.equals(pkg) || sp.equals(defProc)) || sp.contentEquals(procSeq)) {
                    return input.success(pkg);
                }
            }
        }
        if ((procSeq == null) || (procSeq.length() <= 0)) {
            return input.success(defProc);
        }
        android.content.pm.parsing.result.ParseResult<java.lang.String> nameResult = android.content.pm.parsing.component.ComponentParseUtils.buildCompoundName(pkg, procSeq, "process", input);
        return input.success(android.text.TextUtils.safeIntern(nameResult.getResult()));
    }

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<java.lang.String> buildTaskAffinityName(java.lang.String pkg, java.lang.String defProc, java.lang.CharSequence procSeq, android.content.pm.parsing.result.ParseInput input) {
        if (procSeq == null) {
            return input.success(defProc);
        }
        if (procSeq.length() <= 0) {
            return input.success(null);
        }
        return android.content.pm.parsing.component.ComponentParseUtils.buildCompoundName(pkg, procSeq, "taskAffinity", input);
    }

    public static android.content.pm.parsing.result.ParseResult<java.lang.String> buildCompoundName(java.lang.String pkg, java.lang.CharSequence procSeq, java.lang.String type, android.content.pm.parsing.result.ParseInput input) {
        java.lang.String proc = procSeq.toString();
        char c = proc.charAt(0);
        if ((pkg != null) && (c == ':')) {
            if (proc.length() < 2) {
                return input.error(((((("Bad " + type) + " name ") + proc) + " in package ") + pkg) + ": must be at least two characters");
            }
            java.lang.String subName = proc.substring(1);
            java.lang.String nameError = android.content.pm.PackageParser.validateName(subName, false, false);
            if (nameError != null) {
                return input.error((((((("Invalid " + type) + " name ") + proc) + " in package ") + pkg) + ": ") + nameError);
            }
            return input.success(pkg + proc);
        }
        java.lang.String nameError = android.content.pm.PackageParser.validateName(proc, true, false);
        if ((nameError != null) && (!"system".equals(proc))) {
            return input.error((((((("Invalid " + type) + " name ") + proc) + " in package ") + pkg) + ": ") + nameError);
        }
        return input.success(proc);
    }

    public static int flag(int flag, @android.annotation.AttrRes
    int attribute, android.content.res.TypedArray typedArray) {
        return typedArray.getBoolean(attribute, false) ? flag : 0;
    }

    public static int flag(int flag, @android.annotation.AttrRes
    int attribute, boolean defaultValue, android.content.res.TypedArray typedArray) {
        return typedArray.getBoolean(attribute, defaultValue) ? flag : 0;
    }

    /**
     * This is not state aware. Avoid and access through PackageInfoUtils in the system server.
     */
    @android.annotation.Nullable
    public static java.lang.CharSequence getNonLocalizedLabel(android.content.pm.parsing.component.ParsedComponent component) {
        return component.nonLocalizedLabel;
    }

    /**
     * This is not state aware. Avoid and access through PackageInfoUtils in the system server.
     *
     * This is a method of the utility class to discourage use.
     */
    public static int getIcon(android.content.pm.parsing.component.ParsedComponent component) {
        return component.icon;
    }

    public static boolean isMatch(android.content.pm.PackageUserState state, boolean isSystem, boolean isPackageEnabled, android.content.pm.parsing.component.ParsedMainComponent component, int flags) {
        return state.isMatch(isSystem, isPackageEnabled, component.isEnabled(), component.isDirectBootAware(), component.getName(), flags);
    }

    public static boolean isEnabled(android.content.pm.PackageUserState state, boolean isPackageEnabled, android.content.pm.parsing.component.ParsedMainComponent parsedComponent, int flags) {
        return state.isEnabled(isPackageEnabled, parsedComponent.isEnabled(), parsedComponent.getName(), flags);
    }
}

