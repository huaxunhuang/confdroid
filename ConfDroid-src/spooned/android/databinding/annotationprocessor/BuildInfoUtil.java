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


public class BuildInfoUtil {
    private static android.databinding.BindingBuildInfo sCached;

    public static android.databinding.BindingBuildInfo load(javax.annotation.processing.RoundEnvironment roundEnvironment) {
        if (android.databinding.annotationprocessor.BuildInfoUtil.sCached == null) {
            android.databinding.annotationprocessor.BuildInfoUtil.sCached = android.databinding.annotationprocessor.BuildInfoUtil.extractNotNull(roundEnvironment, android.databinding.BindingBuildInfo.class);
            if (android.databinding.annotationprocessor.BuildInfoUtil.sCached != null) {
                android.databinding.tool.util.L.setDebugLog(android.databinding.annotationprocessor.BuildInfoUtil.sCached.enableDebugLogs());
                android.databinding.tool.processing.ScopedException.encodeOutput(android.databinding.annotationprocessor.BuildInfoUtil.sCached.printEncodedError());
            }
        }
        return android.databinding.annotationprocessor.BuildInfoUtil.sCached;
    }

    private static <T extends java.lang.annotation.Annotation> T extractNotNull(javax.annotation.processing.RoundEnvironment roundEnv, java.lang.Class<T> annotationClass) {
        T result = null;
        for (javax.lang.model.element.Element element : roundEnv.getElementsAnnotatedWith(annotationClass)) {
            final T info = element.getAnnotation(annotationClass);
            if (info == null) {
                continue;// It gets confused between BindingAppInfo and BinderBundle

            }
            android.databinding.tool.util.Preconditions.check(result == null, "Should have only one %s", annotationClass.getCanonicalName());
            result = info;
        }
        return result;
    }
}

