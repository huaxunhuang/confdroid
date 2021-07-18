/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.app;


/**
 * Stub activity that launches another activity (and then finishes itself)
 * based on information in its component's manifest meta-data.  This is a
 * simple way to implement an alias-like mechanism.
 *
 * To use this activity, you should include in the manifest for the associated
 * component an entry named "android.app.alias".  It is a reference to an XML
 * resource describing an intent that launches the real application.
 */
public class AliasActivity extends android.app.Activity {
    /**
     * This is the name under which you should store in your component the
     * meta-data information about the alias.  It is a reference to an XML
     * resource describing an intent that launches the real application.
     * {@hide }
     */
    public final java.lang.String ALIAS_META_DATA = "android.app.alias";

    @java.lang.Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        android.content.res.XmlResourceParser parser = null;
        try {
            android.content.pm.ActivityInfo ai = getPackageManager().getActivityInfo(getComponentName(), android.content.pm.PackageManager.GET_META_DATA);
            parser = ai.loadXmlMetaData(getPackageManager(), ALIAS_META_DATA);
            if (parser == null) {
                throw new java.lang.RuntimeException("Alias requires a meta-data field " + ALIAS_META_DATA);
            }
            android.content.Intent intent = parseAlias(parser);
            if (intent == null) {
                throw new java.lang.RuntimeException("No <intent> tag found in alias description");
            }
            startActivity(intent);
            finish();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new java.lang.RuntimeException("Error parsing alias", e);
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            throw new java.lang.RuntimeException("Error parsing alias", e);
        } catch (java.io.IOException e) {
            throw new java.lang.RuntimeException("Error parsing alias", e);
        } finally {
            if (parser != null)
                parser.close();

        }
    }

    private android.content.Intent parseAlias(org.xmlpull.v1.XmlPullParser parser) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
        android.content.Intent intent = null;
        int type;
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
        } 
        java.lang.String nodeName = parser.getName();
        if (!"alias".equals(nodeName)) {
            throw new java.lang.RuntimeException((("Alias meta-data must start with <alias> tag; found" + nodeName) + " at ") + parser.getPositionDescription());
        }
        int outerDepth = parser.getDepth();
        while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && ((type != org.xmlpull.v1.XmlPullParser.END_TAG) || (parser.getDepth() > outerDepth))) {
            if ((type == org.xmlpull.v1.XmlPullParser.END_TAG) || (type == org.xmlpull.v1.XmlPullParser.TEXT)) {
                continue;
            }
            nodeName = parser.getName();
            if ("intent".equals(nodeName)) {
                android.content.Intent gotIntent = android.content.Intent.parseIntent(getResources(), parser, attrs);
                if (intent == null)
                    intent = gotIntent;

            } else {
                com.android.internal.util.XmlUtils.skipCurrentTag(parser);
            }
        } 
        return intent;
    }
}

