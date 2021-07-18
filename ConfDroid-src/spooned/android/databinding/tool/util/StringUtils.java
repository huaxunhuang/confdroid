/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.tool.util;


public class StringUtils {
    public static final java.lang.String LINE_SEPARATOR = com.google.common.base.StandardSystemProperty.LINE_SEPARATOR.value();

    /**
     * The entity for the ampersand character
     */
    private static final java.lang.String AMP_ENTITY = "&amp;";

    /**
     * The entity for the quote character
     */
    private static final java.lang.String QUOT_ENTITY = "&quot;";

    /**
     * The entity for the apostrophe character
     */
    private static final java.lang.String APOS_ENTITY = "&apos;";

    /**
     * The entity for the less than character
     */
    private static final java.lang.String LT_ENTITY = "&lt;";

    /**
     * The entity for the greater than character
     */
    private static final java.lang.String GT_ENTITY = "&gt;";

    /**
     * The entity for the tab character
     */
    private static final java.lang.String TAB_ENTITY = "&#x9;";

    /**
     * The entity for the carriage return character
     */
    private static final java.lang.String CR_ENTITY = "&#xD;";

    /**
     * The entity for the line feed character
     */
    private static final java.lang.String LFEED_ENTITY = "&#xA;";

    public static boolean isNotBlank(@org.antlr.v4.runtime.misc.Nullable
    java.lang.CharSequence string) {
        if (string == null) {
            return false;
        }
        for (int i = 0, n = string.length(); i < n; i++) {
            if (!java.lang.Character.isWhitespace(string.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static java.lang.String capitalize(@org.antlr.v4.runtime.misc.Nullable
    java.lang.String string) {
        if (com.google.common.base.Strings.isNullOrEmpty(string)) {
            return string;
        }
        char ch = string.charAt(0);
        if (java.lang.Character.isTitleCase(ch)) {
            return string;
        }
        return java.lang.Character.toTitleCase(ch) + string.substring(1);
    }

    public static java.lang.String unescapeXml(java.lang.String escaped) {
        // TODO: unescape unicode codepoints
        return escaped.replace(android.databinding.tool.util.StringUtils.QUOT_ENTITY, "\"").replace(android.databinding.tool.util.StringUtils.LT_ENTITY, "<").replace(android.databinding.tool.util.StringUtils.GT_ENTITY, ">").replace(android.databinding.tool.util.StringUtils.APOS_ENTITY, "'").replace(android.databinding.tool.util.StringUtils.AMP_ENTITY, "&").replace(android.databinding.tool.util.StringUtils.TAB_ENTITY, "\t").replace(android.databinding.tool.util.StringUtils.CR_ENTITY, "\r").replace(android.databinding.tool.util.StringUtils.LFEED_ENTITY, "\n");
    }

    private StringUtils() {
    }
}

