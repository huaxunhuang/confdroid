/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.graphics.fonts;


/**
 * Delegate implementing the native methods of android.graphics.fonts.SystemFonts
 * <p>
 * Through the layoutlib_create tool, the original native methods of SystemFonts have been
 * replaced by calls to methods of the same name in this delegate class.
 * <p>
 * This class behaves like the original native implementation, but in Java, keeping previously
 * native data into its own objects and mapping them to int that are sent back and forth between it
 * and the original SystemFonts class.
 *
 * @see DelegateManager
 */
public class SystemFonts_Delegate {
    /* package */
    @com.android.tools.layoutlib.annotations.LayoutlibDelegate
    static FontConfig.Alias[] buildSystemFallback(@android.annotation.NonNull
    java.lang.String xmlPath, @android.annotation.NonNull
    java.lang.String fontDir, @android.annotation.NonNull
    android.graphics.fonts.FontCustomizationParser.Result oemCustomization, @android.annotation.NonNull
    android.util.ArrayMap<java.lang.String, android.graphics.fonts.FontFamily[]> fallbackMap, @android.annotation.NonNull
    java.util.ArrayList<android.graphics.fonts.Font> availableFonts) {
        com.android.layoutlib.bridge.Bridge.sIsTypefaceInitialized = true;
        return android.graphics.fonts.SystemFonts.buildSystemFallback_Original(android.graphics.FontFamily_Delegate.getFontLocation() + "/fonts.xml", android.graphics.FontFamily_Delegate.getFontLocation() + "/", oemCustomization, fallbackMap, availableFonts);
    }
}

