/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.provider;


/**
 * A provider of user defined words for input methods to use for predictive text input.
 * Applications and input methods may add words into the dictionary. Words can have associated
 * frequency information and locale information.
 *
 * <p><strong>NOTE: </strong>Starting on API 23, the user dictionary is only accessible through
 * IME and spellchecker.
 */
public class UserDictionary {
    /**
     * Authority string for this provider.
     */
    public static final java.lang.String AUTHORITY = "user_dictionary";

    /**
     * The content:// style URL for this provider
     */
    public static final android.net.Uri CONTENT_URI = android.net.Uri.parse("content://" + android.provider.UserDictionary.AUTHORITY);

    private static final int FREQUENCY_MIN = 0;

    private static final int FREQUENCY_MAX = 255;

    /**
     * Contains the user defined words.
     */
    public static class Words implements android.provider.BaseColumns {
        /**
         * The content:// style URL for this table
         */
        public static final android.net.Uri CONTENT_URI = android.net.Uri.parse(("content://" + android.provider.UserDictionary.AUTHORITY) + "/words");

        /**
         * The MIME type of {@link #CONTENT_URI} providing a directory of words.
         */
        public static final java.lang.String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.google.userword";

        /**
         * The MIME type of a {@link #CONTENT_URI} sub-directory of a single word.
         */
        public static final java.lang.String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.google.userword";

        public static final java.lang.String _ID = android.provider.BaseColumns._ID;

        /**
         * The word column.
         * <p>TYPE: TEXT</p>
         */
        public static final java.lang.String WORD = "word";

        /**
         * The frequency column. A value between 1 and 255. Higher values imply higher frequency.
         * <p>TYPE: INTEGER</p>
         */
        public static final java.lang.String FREQUENCY = "frequency";

        /**
         * The locale that this word belongs to. Null if it pertains to all
         * locales. Locale is as defined by the string returned by Locale.toString().
         * <p>TYPE: TEXT</p>
         */
        public static final java.lang.String LOCALE = "locale";

        /**
         * The uid of the application that inserted the word.
         * <p>TYPE: INTEGER</p>
         */
        public static final java.lang.String APP_ID = "appid";

        /**
         * An optional shortcut for this word. When the shortcut is typed, supporting IMEs should
         * suggest the word in this row as an alternate spelling too.
         */
        public static final java.lang.String SHORTCUT = "shortcut";

        /**
         *
         *
         * @deprecated Use {@link #addWord(Context, String, int, String, Locale)}.
         */
        @java.lang.Deprecated
        public static final int LOCALE_TYPE_ALL = 0;

        /**
         *
         *
         * @deprecated Use {@link #addWord(Context, String, int, String, Locale)}.
         */
        @java.lang.Deprecated
        public static final int LOCALE_TYPE_CURRENT = 1;

        /**
         * Sort by descending order of frequency.
         */
        public static final java.lang.String DEFAULT_SORT_ORDER = android.provider.UserDictionary.Words.FREQUENCY + " DESC";

        /**
         * Adds a word to the dictionary, with the given frequency and the specified
         *  specified locale type.
         *
         * @deprecated Please use
        {@link #addWord(Context, String, int, String, Locale)} instead.
         * @param context
         * 		the current application context
         * @param word
         * 		the word to add to the dictionary. This should not be null or
         * 		empty.
         * @param localeType
         * 		the locale type for this word. It should be one of
         * 		{@link #LOCALE_TYPE_ALL} or {@link #LOCALE_TYPE_CURRENT}.
         */
        @java.lang.Deprecated
        public static void addWord(android.content.Context context, java.lang.String word, int frequency, int localeType) {
            if ((localeType != android.provider.UserDictionary.Words.LOCALE_TYPE_ALL) && (localeType != android.provider.UserDictionary.Words.LOCALE_TYPE_CURRENT)) {
                return;
            }
            final java.util.Locale locale;
            if (localeType == android.provider.UserDictionary.Words.LOCALE_TYPE_CURRENT) {
                locale = java.util.Locale.getDefault();
            } else {
                locale = null;
            }
            android.provider.UserDictionary.Words.addWord(context, word, frequency, null, locale);
        }

        /**
         * Adds a word to the dictionary, with the given frequency and the specified
         *  locale type.
         *
         * @param context
         * 		the current application context
         * @param word
         * 		the word to add to the dictionary. This should not be null or
         * 		empty.
         * @param shortcut
         * 		optional shortcut spelling for this word. When the shortcut
         * 		is typed, the word may be suggested by applications that support it. May be null.
         * @param locale
         * 		the locale to insert the word for, or null to insert the word
         * 		for all locales.
         */
        public static void addWord(android.content.Context context, java.lang.String word, int frequency, java.lang.String shortcut, java.util.Locale locale) {
            final android.content.ContentResolver resolver = context.getContentResolver();
            if (android.text.TextUtils.isEmpty(word)) {
                return;
            }
            if (frequency < android.provider.UserDictionary.FREQUENCY_MIN)
                frequency = android.provider.UserDictionary.FREQUENCY_MIN;

            if (frequency > android.provider.UserDictionary.FREQUENCY_MAX)
                frequency = android.provider.UserDictionary.FREQUENCY_MAX;

            final int COLUMN_COUNT = 5;
            android.content.ContentValues values = new android.content.ContentValues(COLUMN_COUNT);
            values.put(android.provider.UserDictionary.Words.WORD, word);
            values.put(android.provider.UserDictionary.Words.FREQUENCY, frequency);
            values.put(android.provider.UserDictionary.Words.LOCALE, null == locale ? null : locale.toString());
            values.put(android.provider.UserDictionary.Words.APP_ID, 0);// TODO: Get App UID

            values.put(android.provider.UserDictionary.Words.SHORTCUT, shortcut);
            android.net.Uri result = resolver.insert(android.provider.UserDictionary.Words.CONTENT_URI, values);
            // It's ok if the insert doesn't succeed because the word
            // already exists.
        }
    }
}

