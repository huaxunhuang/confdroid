/**
 * Copyright (C) 2016 The Android Open Source Project
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
package android.view.autofill;


/**
 *
 *
 * @unknown 
 */
public final class Helper {
    // Debug-level flags are defined when service is bound.
    public static boolean sDebug = false;

    public static boolean sVerbose = false;

    /**
     * Appends {@code value} to the {@code builder} redacting its contents.
     */
    public static void appendRedacted(@android.annotation.NonNull
    java.lang.StringBuilder builder, @android.annotation.Nullable
    java.lang.CharSequence value) {
        builder.append(android.view.autofill.Helper.getRedacted(value));
    }

    /**
     * Gets the redacted version of a value.
     */
    @android.annotation.NonNull
    public static java.lang.String getRedacted(@android.annotation.Nullable
    java.lang.CharSequence value) {
        return value == null ? "null" : value.length() + "_chars";
    }

    /**
     * Appends {@code values} to the {@code builder} redacting its contents.
     */
    public static void appendRedacted(@android.annotation.NonNull
    java.lang.StringBuilder builder, @android.annotation.Nullable
    java.lang.String[] values) {
        if (values == null) {
            builder.append("N/A");
            return;
        }
        builder.append("[");
        for (java.lang.String value : values) {
            builder.append(" '");
            android.view.autofill.Helper.appendRedacted(builder, value);
            builder.append("'");
        }
        builder.append(" ]");
    }

    /**
     * Converts a collaction of {@link AutofillId AutofillIds} to an array.
     *
     * @param collection
     * 		The collection.
     * @return The array.
     */
    @android.annotation.NonNull
    public static android.view.autofill.AutofillId[] toArray(java.util.Collection<android.view.autofill.AutofillId> collection) {
        if (collection == null) {
            return new android.view.autofill.AutofillId[0];
        }
        final android.view.autofill.AutofillId[] array = new android.view.autofill.AutofillId[collection.size()];
        collection.toArray(array);
        return array;
    }

    /**
     * Converts a Set to a List.
     */
    @android.annotation.Nullable
    public static <T> java.util.ArrayList<T> toList(@android.annotation.Nullable
    java.util.Set<T> set) {
        return set == null ? null : new java.util.ArrayList<T>(set);
    }

    private Helper() {
        throw new java.lang.UnsupportedOperationException("contains static members only");
    }
}

