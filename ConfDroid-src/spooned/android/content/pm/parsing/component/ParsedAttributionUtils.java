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
public class ParsedAttributionUtils {
    @android.annotation.NonNull
    public static android.content.pm.parsing.result.ParseResult<android.content.pm.parsing.component.ParsedAttribution> parseAttribution(android.content.res.Resources res, android.content.res.XmlResourceParser parser, android.content.pm.parsing.result.ParseInput input) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        java.lang.String attributionTag;
        int label;
        java.util.List<java.lang.String> inheritFrom = null;
        android.content.res.TypedArray sa = res.obtainAttributes(parser, R.styleable.AndroidManifestAttribution);
        if (sa == null) {
            return input.error("<attribution> could not be parsed");
        }
        try {
            attributionTag = sa.getNonConfigurationString(R.styleable.AndroidManifestAttribution_tag, 0);
            if (attributionTag == null) {
                // TODO moltmann: Remove handling of featureId
                attributionTag = sa.getNonConfigurationString(R.styleable.AndroidManifestAttribution_featureId, 0);
                if (attributionTag == null) {
                    return input.error("<attribution> does not specify android:tag");
                }
            }
            if (attributionTag.length() > android.content.pm.parsing.component.ParsedAttribution.MAX_ATTRIBUTION_TAG_LEN) {
                return input.error("android:tag is too long. Max length is " + android.content.pm.parsing.component.ParsedAttribution.MAX_ATTRIBUTION_TAG_LEN);
            }
            label = sa.getResourceId(R.styleable.AndroidManifestAttribution_label, 0);
            if (label == android.content.res.Resources.ID_NULL) {
                return input.error("<attribution> does not specify android:label");
            }
        } finally {
            sa.recycle();
        }
        int type;
        final int innerDepth = parser.getDepth();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > innerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            java.lang.String tagName = parser.getName();
            if (tagName.equals("inherit-from")) {
                sa = res.obtainAttributes(parser, R.styleable.AndroidManifestAttributionInheritFrom);
                if (sa == null) {
                    return input.error("<inherit-from> could not be parsed");
                }
                try {
                    java.lang.String inheritFromId = sa.getNonConfigurationString(R.styleable.AndroidManifestAttributionInheritFrom_tag, 0);
                    if (inheritFrom == null) {
                        inheritFrom = new java.util.ArrayList<>();
                    }
                    inheritFrom.add(inheritFromId);
                } finally {
                    sa.recycle();
                }
            } else {
                return input.error("Bad element under <attribution>: " + tagName);
            }
        } 
        if (inheritFrom == null) {
            inheritFrom = java.util.Collections.emptyList();
        } else {
            ((java.util.ArrayList) (inheritFrom)).trimToSize();
        }
        return input.success(new android.content.pm.parsing.component.ParsedAttribution(attributionTag, label, inheritFrom));
    }
}

