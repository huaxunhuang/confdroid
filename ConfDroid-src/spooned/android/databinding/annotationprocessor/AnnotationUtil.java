/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.databinding.annotationprocessor;


class AnnotationUtil {
    /**
     * Returns only the elements that are annotated with the given class. For some reason
     * RoundEnvironment is returning elements annotated by other annotations.
     */
    static java.util.List<javax.lang.model.element.Element> getElementsAnnotatedWith(javax.annotation.processing.RoundEnvironment roundEnv, java.lang.Class<? extends java.lang.annotation.Annotation> annotationClass) {
        java.util.ArrayList<javax.lang.model.element.Element> elements = new java.util.ArrayList<javax.lang.model.element.Element>();
        for (javax.lang.model.element.Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            if (element.getAnnotation(annotationClass) != null) {
                elements.add(element);
            }
        }
        return elements;
    }
}

