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
 * A predicate that checks to see if a {@link android.test.suitebuilder.TestMethod} has a specific annotation on the
 * containing class. Consider using the public {@link HasAnnotation} class instead of this class.
 *
 * {@hide } Not needed for 1.0 SDK.
 */
class HasClassAnnotation implements com.android.internal.util.Predicate<android.test.suitebuilder.TestMethod> {
    private java.lang.Class<? extends java.lang.annotation.Annotation> annotationClass;

    public HasClassAnnotation(java.lang.Class<? extends java.lang.annotation.Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    public boolean apply(android.test.suitebuilder.TestMethod testMethod) {
        return testMethod.getEnclosingClass().getAnnotation(annotationClass) != null;
    }
}

