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
package android.test.suitebuilder.annotation;


/**
 * A predicate that checks to see if a {@link TestMethod} has a specific annotation, either on the
 * method or on the containing class.
 *
 * {@hide } Not needed for 1.0 SDK.
 */
public class HasAnnotation implements com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> {
    private com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> hasMethodOrClassAnnotation;

    public HasAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation> annotationClass) {
        this.hasMethodOrClassAnnotation = com.android.internal.util.Predicates.or(new android.test.suitebuilder.annotation.HasMethodAnnotation(annotationClass), new android.test.suitebuilder.annotation.HasClassAnnotation(annotationClass));
    }

    public boolean apply(android.test.suitebuilder.TestMethod testMethod) {
        return hasMethodOrClassAnnotation.apply(testMethod);
    }
}

