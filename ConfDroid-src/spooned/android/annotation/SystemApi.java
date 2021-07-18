/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.annotation;


/**
 * Indicates an API is exposed for use by bundled system applications.
 * <p>
 * These APIs are not guaranteed to remain consistent release-to-release,
 * and are not for use by apps linking against the Android SDK.
 * </p><p>
 * This annotation should only appear on API that is already marked <pre>@hide</pre>.
 * </p>
 *
 * @unknown 
 */
@java.lang.annotation.Target({ java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.ANNOTATION_TYPE, java.lang.annotation.ElementType.PACKAGE })
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
public @interface SystemApi {}

