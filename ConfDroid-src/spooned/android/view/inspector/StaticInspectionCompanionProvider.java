/**
 * Copyright 2019 The Android Open Source Project
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
package android.view.inspector;


/**
 * An inspection companion provider that finds companions as inner classes or generated code.
 *
 * @see android.processor.view.inspector.PlatformInspectableProcessor
 */
public class StaticInspectionCompanionProvider implements android.view.inspector.InspectionCompanionProvider {
    /**
     * The suffix used for the generated classes and inner classes
     */
    private static final java.lang.String COMPANION_SUFFIX = "$InspectionCompanion";

    @java.lang.Override
    @android.annotation.Nullable
    @java.lang.SuppressWarnings("unchecked")
    public <T> android.view.inspector.InspectionCompanion<T> provide(@android.annotation.NonNull
    java.lang.Class<T> cls) {
        final java.lang.String companionName = cls.getName() + android.view.inspector.StaticInspectionCompanionProvider.COMPANION_SUFFIX;
        try {
            final java.lang.Class<android.view.inspector.InspectionCompanion<T>> companionClass = ((java.lang.Class<android.view.inspector.InspectionCompanion<T>>) (cls.getClassLoader().loadClass(companionName)));
            if (android.view.inspector.InspectionCompanion.class.isAssignableFrom(companionClass)) {
                return companionClass.newInstance();
            } else {
                return null;
            }
        } catch (java.lang.ClassNotFoundException e) {
            return null;
        } catch (java.lang.IllegalAccessException e) {
            throw new java.lang.RuntimeException(e);
        } catch (java.lang.InstantiationException e) {
            java.lang.Throwable cause = e.getCause();
            if (cause instanceof java.lang.RuntimeException)
                throw ((java.lang.RuntimeException) (cause));

            if (cause instanceof java.lang.Error)
                throw ((java.lang.Error) (cause));

            throw new java.lang.RuntimeException(cause);
        }
    }
}

