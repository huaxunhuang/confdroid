/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.util;


/**
 * PrefixPrinter is a Printer which prefixes all lines with a given
 * prefix.
 *
 * @unknown 
 */
public class PrefixPrinter implements android.util.Printer {
    private final android.util.Printer mPrinter;

    private final java.lang.String mPrefix;

    /**
     * Creates a new PrefixPrinter.
     *
     * <p>If prefix is null or empty, the provided printer is returned, rather
     * than making a prefixing printer.
     */
    public static android.util.Printer create(android.util.Printer printer, java.lang.String prefix) {
        if ((prefix == null) || prefix.equals("")) {
            return printer;
        }
        return new android.util.PrefixPrinter(printer, prefix);
    }

    private PrefixPrinter(android.util.Printer printer, java.lang.String prefix) {
        mPrinter = printer;
        mPrefix = prefix;
    }

    public void println(java.lang.String str) {
        mPrinter.println(mPrefix + str);
    }
}

