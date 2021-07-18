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


public class ParserHelper {
    public static java.lang.String toClassName(java.lang.String name) {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        for (java.lang.String item : name.split("[_-]")) {
            builder.append(android.databinding.tool.util.StringUtils.capitalize(item));
        }
        return builder.toString();
    }

    public static java.lang.String stripExtension(java.lang.String name) {
        final int dot = name.lastIndexOf('.');
        return dot < 0 ? name : name.substring(0, dot);
    }
}

