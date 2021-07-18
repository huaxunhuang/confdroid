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
public class ParsedInstrumentationUtils {
    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedInstrumentation> parseInstrumentation(android.content.pm.parsing.ParsingPackage pkg, android.content.res.Resources res, android.content.res.XmlResourceParser parser, boolean useRoundIcon, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.content.pm.parsing.component.ParsedInstrumentation instrumentation = new android.content.pm.parsing.component.ParsedInstrumentation();
        java.lang.String tag = ("<" + parser.getName()) + ">";
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestInstrumentation);
        try {
            android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedInstrumentation> result = /* descriptionAttr */
            android.content.pm.parsing.component.ParsedComponentUtils.parseComponent(instrumentation, tag, pkg, sa, useRoundIcon, input, R.styleable.AndroidManifestInstrumentation_banner, null, R.styleable.AndroidManifestInstrumentation_icon, R.styleable.AndroidManifestInstrumentation_label, R.styleable.AndroidManifestInstrumentation_logo, R.styleable.AndroidManifestInstrumentation_name, R.styleable.AndroidManifestInstrumentation_roundIcon);
            if (result.isError()) {
                return result;
            }
            // @formatter:off
            // Note: don't allow this value to be a reference to a resource
            // that may change.
            instrumentation.setTargetPackage(sa.getNonResourceString(R.styleable.AndroidManifestInstrumentation_targetPackage));
            instrumentation.setTargetProcesses(sa.getNonResourceString(R.styleable.AndroidManifestInstrumentation_targetProcesses));
            instrumentation.handleProfiling = sa.getBoolean(R.styleable.AndroidManifestInstrumentation_handleProfiling, false);
            instrumentation.functionalTest = sa.getBoolean(R.styleable.AndroidManifestInstrumentation_functionalTest, false);
            // @formatter:on
        } finally {
            sa.recycle();
        }
        return android.content.pm.parsing.component.ComponentParseUtils.parseAllMetaData(pkg, res, parser, tag, instrumentation, input);
    }
}

