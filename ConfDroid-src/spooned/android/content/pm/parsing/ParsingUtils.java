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
package android.content.pm.parsing;


/**
 *
 *
 * @unknown *
 */
public class ParsingUtils {
    // TODO(b/135203078): Consolidate log tags
    public static final java.lang.String TAG = "PackageParsing";

    @android.annotation.Nullable
    public static java.lang.String buildClassName(java.lang.String pkg, java.lang.CharSequence clsSeq) {
        if ((clsSeq == null) || (clsSeq.length() <= 0)) {
            return null;
        }
        java.lang.String cls = clsSeq.toString();
        char c = cls.charAt(0);
        if (c == '.') {
            return pkg + cls;
        }
        if (cls.indexOf('.') < 0) {
            java.lang.StringBuilder b = new java.lang.StringBuilder(pkg);
            b.append('.');
            b.append(cls);
            return b.toString();
        }
        return cls;
    }

    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult unknownTag(java.lang.String parentTag, android.content.pm.parsing.ParsingPackage pkg, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        if (android.content.pm.PackageParser.RIGID_PARSER) {
            return input.error((("Bad element under " + parentTag) + ": ") + parser.getName());
        }
        android.util.Slog.w(android.content.pm.parsing.ParsingUtils.TAG, (((((("Unknown element under " + parentTag) + ": ") + parser.getName()) + " at ") + pkg.getBaseCodePath()) + " ") + parser.getPositionDescription());
        com.android.internal.util.XmlUtils.skipCurrentTag(parser);
        return input.success(null);// Type doesn't matter

    }
}

