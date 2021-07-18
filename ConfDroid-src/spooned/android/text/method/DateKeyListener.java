/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.text.method;


/**
 * For entering dates in a text field.
 * <p></p>
 * As for all implementations of {@link KeyListener}, this class is only concerned
 * with hardware keyboards.  Software input methods have no obligation to trigger
 * the methods in this class.
 */
public class DateKeyListener extends android.text.method.NumberKeyListener {
    public int getInputType() {
        return android.text.InputType.TYPE_CLASS_DATETIME | android.text.InputType.TYPE_DATETIME_VARIATION_DATE;
    }

    @java.lang.Override
    protected char[] getAcceptedChars() {
        return android.text.method.DateKeyListener.CHARACTERS;
    }

    public static android.text.method.DateKeyListener getInstance() {
        if (android.text.method.DateKeyListener.sInstance != null)
            return android.text.method.DateKeyListener.sInstance;

        android.text.method.DateKeyListener.sInstance = new android.text.method.DateKeyListener();
        return android.text.method.DateKeyListener.sInstance;
    }

    /**
     * The characters that are used.
     *
     * @see KeyEvent#getMatch
     * @see #getAcceptedChars
     */
    public static final char[] CHARACTERS = new char[]{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '/', '-', '.' };

    private static android.text.method.DateKeyListener sInstance;
}

