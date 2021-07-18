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
package android.test.suitebuilder;


/**
 * {@hide } Not needed for 1.0 SDK.
 */
public class TestPredicates {
    public static final com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> SELECT_INSTRUMENTATION = new android.test.suitebuilder.AssignableFrom(android.test.InstrumentationTestCase.class);

    public static final com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> REJECT_INSTRUMENTATION = com.android.internal.util.Predicates.not(android.test.suitebuilder.TestPredicates.SELECT_INSTRUMENTATION);

    public static final com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> SELECT_SMOKE = new android.test.suitebuilder.annotation.HasAnnotation(android.test.suitebuilder.annotation.Smoke.class);

    public static final com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> SELECT_SMALL = new android.test.suitebuilder.annotation.HasAnnotation(android.test.suitebuilder.annotation.SmallTest.class);

    public static final com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> SELECT_MEDIUM = new android.test.suitebuilder.annotation.HasAnnotation(android.test.suitebuilder.annotation.MediumTest.class);

    public static final com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> SELECT_LARGE = new android.test.suitebuilder.annotation.HasAnnotation(android.test.suitebuilder.annotation.LargeTest.class);

    public static final com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> REJECT_SUPPRESSED = com.android.internal.util.Predicates.not(new android.test.suitebuilder.annotation.HasAnnotation(android.test.suitebuilder.annotation.Suppress.class));
}

