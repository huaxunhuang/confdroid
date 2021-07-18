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
package android.test;


/**
 * The Package object doesn't allow you to iterate over the contained
 * classes and subpackages of that package.  This is a version that does.
 *
 * {@hide } Not needed for 1.0 SDK.
 */
@java.lang.Deprecated
public class ClassPathPackageInfo {
    private final android.test.ClassPathPackageInfoSource source;

    private final java.lang.String packageName;

    private final java.util.Set<java.lang.String> subpackageNames;

    private final java.util.Set<java.lang.Class<?>> topLevelClasses;

    ClassPathPackageInfo(android.test.ClassPathPackageInfoSource source, java.lang.String packageName, java.util.Set<java.lang.String> subpackageNames, java.util.Set<java.lang.Class<?>> topLevelClasses) {
        this.source = source;
        this.packageName = packageName;
        this.subpackageNames = java.util.Collections.unmodifiableSet(subpackageNames);
        this.topLevelClasses = java.util.Collections.unmodifiableSet(topLevelClasses);
    }

    public java.util.Set<android.test.ClassPathPackageInfo> getSubpackages() {
        java.util.Set<android.test.ClassPathPackageInfo> info = com.google.android.collect.Sets.newHashSet();
        for (java.lang.String name : subpackageNames) {
            info.add(source.getPackageInfo(name));
        }
        return info;
    }

    public java.util.Set<java.lang.Class<?>> getTopLevelClassesRecursive() {
        java.util.Set<java.lang.Class<?>> set = com.google.android.collect.Sets.newHashSet();
        addTopLevelClassesTo(set);
        return set;
    }

    private void addTopLevelClassesTo(java.util.Set<java.lang.Class<?>> set) {
        set.addAll(topLevelClasses);
        for (android.test.ClassPathPackageInfo info : getSubpackages()) {
            info.addTopLevelClassesTo(set);
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        if (obj instanceof android.test.ClassPathPackageInfo) {
            android.test.ClassPathPackageInfo that = ((android.test.ClassPathPackageInfo) (obj));
            return this.packageName.equals(that.packageName);
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return packageName.hashCode();
    }
}

