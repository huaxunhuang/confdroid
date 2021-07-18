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
package android.support.annotation;


/**
 * Denotes that the annotated element should only be accessed from within a
 * specific scope (as defined by {@link Scope}).
 * <p>
 * Example of restricting usage within a library (based on gradle group ID):
 * <pre><code>
 *   &#64;RestrictTo(GROUP_ID)
 *   public void resetPaddingToInitialValues() { ...
 * </code></pre>
 * Example of restricting usage to tests:
 * <pre><code>
 *   &#64;RestrictScope(TESTS)
 *   public abstract int getUserId();
 * </code></pre>
 * Example of restricting usage to subclasses:
 * <pre><code>
 *   &#64;RestrictScope(SUBCLASSES)
 *   public void onDrawForeground(Canvas canvas) { ...
 * </code></pre>
 */
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
@java.lang.annotation.Target({ java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PACKAGE })
public @interface RestrictTo {
    /**
     * The scope to which usage should be restricted.
     */
    android.support.annotation.RestrictTo.Scope[] value();

    enum Scope {

        /**
         * Restrict usage to code within the same group ID (based on gradle
         * group ID).
         */
        GROUP_ID,
        /**
         * Restrict usage to tests.
         */
        TESTS,
        /**
         * Restrict usage to subclasses of the enclosing class.
         * <p>
         * <strong>Note:</strong> This scope should not be used to annotate
         * packages.
         */
        SUBCLASSES;}
}

