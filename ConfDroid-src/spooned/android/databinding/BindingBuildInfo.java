/**
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.databinding;


/**
 *
 *
 * @unknown 
 */
@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE })
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
public @interface BindingBuildInfo {
    java.lang.String buildId();

    java.lang.String modulePackage();

    java.lang.String sdkRoot();

    int minSdk();

    /**
     * The folder that includes xml files which are exported by aapt or gradle plugin from layout files
     */
    java.lang.String layoutInfoDir();

    /**
     * The file to which the list of generated classes should be exported
     */
    java.lang.String exportClassListTo();

    boolean isLibrary();

    boolean enableDebugLogs() default false;

    boolean printEncodedError() default false;
}

