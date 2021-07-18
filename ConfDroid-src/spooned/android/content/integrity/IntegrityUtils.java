/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.content.integrity;


/**
 * Utils class for simple operations used in integrity module.
 *
 * @unknown 
 */
public class IntegrityUtils {
    private static final char[] HEX_CHARS = "0123456789ABCDEF".toCharArray();

    /**
     * Obtain the raw bytes from hex encoded string.
     *
     * @throws IllegalArgumentException
     * 		if {@code hexDigest} is not a valid hex encoding of some
     * 		bytes
     */
    public static byte[] getBytesFromHexDigest(java.lang.String hexDigest) {
        checkArgument((hexDigest.length() % 2) == 0, ("Invalid hex encoding " + hexDigest) + ": must have even length");
        byte[] rawBytes = new byte[hexDigest.length() / 2];
        for (int i = 0; i < rawBytes.length; i++) {
            int upperNibble = hexDigest.charAt(2 * i);
            int lowerNibble = hexDigest.charAt((2 * i) + 1);
            rawBytes[i] = ((byte) ((android.content.integrity.IntegrityUtils.hexToDec(upperNibble) << 4) | android.content.integrity.IntegrityUtils.hexToDec(lowerNibble)));
        }
        return rawBytes;
    }

    /**
     * Obtain hex encoded string from raw bytes.
     */
    public static java.lang.String getHexDigest(byte[] rawBytes) {
        char[] hexChars = new char[rawBytes.length * 2];
        for (int i = 0; i < rawBytes.length; i++) {
            int upperNibble = (rawBytes[i] >>> 4) & 0xf;
            int lowerNibble = rawBytes[i] & 0xf;
            hexChars[i * 2] = android.content.integrity.IntegrityUtils.decToHex(upperNibble);
            hexChars[(i * 2) + 1] = android.content.integrity.IntegrityUtils.decToHex(lowerNibble);
        }
        return new java.lang.String(hexChars);
    }

    private static int hexToDec(int hexChar) {
        if ((hexChar >= '0') && (hexChar <= '9')) {
            return hexChar - '0';
        }
        if ((hexChar >= 'a') && (hexChar <= 'f')) {
            return (hexChar - 'a') + 10;
        }
        if ((hexChar >= 'A') && (hexChar <= 'F')) {
            return (hexChar - 'A') + 10;
        }
        throw new java.lang.IllegalArgumentException("Invalid hex char " + hexChar);
    }

    private static char decToHex(int dec) {
        if ((dec >= 0) && (dec < android.content.integrity.IntegrityUtils.HEX_CHARS.length)) {
            return android.content.integrity.IntegrityUtils.HEX_CHARS[dec];
        }
        throw new java.lang.IllegalArgumentException("Invalid dec value to be converted to hex digit " + dec);
    }
}

