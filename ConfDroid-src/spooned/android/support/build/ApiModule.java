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
package android.support.build;


/**
 * Defines an API specific support library modules.
 * e.g. Honeycomb implementation of Support-v4.
 *
 * These ApiModules are converted into real modules when project is opened in AndroidStudio.
 * When project is run from the command line, they are converted into source sets.
 * This allows us to rely on previous compile setup to deploy projects with their dependencies while
 * supporting development on Android Studio.
 */
public class ApiModule {
    public static final int CURRENT = 99;

    private java.lang.String mFolderName;

    private int mApi;

    private org.gradle.api.tasks.SourceSet mSourceSet;

    private android.support.build.ApiModule mPrev;

    private java.lang.String mParentModuleName;

    private java.util.List<java.lang.String> mParentModuleDependencies;

    private java.util.List<java.lang.String> mResourceFolders = new java.util.ArrayList<>();

    private java.util.List<java.lang.String> mAssetFolders = new java.util.ArrayList<>();

    private java.util.List<java.lang.String> mJavaResourceFolders = new java.util.ArrayList<>();

    public ApiModule(java.lang.String folderName, int api) {
        mFolderName = folderName;
        mApi = api;
    }

    public android.support.build.ApiModule resources(java.lang.String... resourceFolders) {
        java.util.Collections.addAll(mResourceFolders, resourceFolders);
        return this;
    }

    public android.support.build.ApiModule assets(java.lang.String... assetFolders) {
        java.util.Collections.addAll(mAssetFolders, assetFolders);
        return this;
    }

    public android.support.build.ApiModule javaResources(java.lang.String... javaResourceFolders) {
        java.util.Collections.addAll(mJavaResourceFolders, javaResourceFolders);
        return this;
    }

    public java.util.List<java.lang.String> getResourceFolders() {
        return mResourceFolders;
    }

    public java.util.List<java.lang.String> getAssetFolders() {
        return mAssetFolders;
    }

    public java.util.List<java.lang.String> getJavaResourceFolders() {
        return mJavaResourceFolders;
    }

    public void setResourceFolders(java.util.List<java.lang.String> resourceFolders) {
        mResourceFolders = resourceFolders;
    }

    public java.lang.String getParentModuleName() {
        return mParentModuleName;
    }

    public void setParentModuleName(java.lang.String parentModuleName) {
        mParentModuleName = parentModuleName;
    }

    public java.lang.String getFolderName() {
        return mFolderName;
    }

    public int getApi() {
        return mApi;
    }

    public java.lang.Object getApiForSourceSet() {
        return mApi == android.support.build.ApiModule.CURRENT ? "current" : mApi;
    }

    public org.gradle.api.tasks.SourceSet getSourceSet() {
        return mSourceSet;
    }

    public void setSourceSet(org.gradle.api.tasks.SourceSet sourceSet) {
        mSourceSet = sourceSet;
    }

    public android.support.build.ApiModule getPrev() {
        return mPrev;
    }

    public void setPrev(android.support.build.ApiModule prev) {
        mPrev = prev;
    }

    public java.lang.String getModuleName() {
        return ((":" + mParentModuleName) + "-") + mFolderName;
    }

    public java.util.List<java.lang.String> getParentModuleDependencies() {
        return mParentModuleDependencies;
    }

    public void setParentModuleDependencies(java.util.List<java.lang.String> parentModuleDependencies) {
        mParentModuleDependencies = parentModuleDependencies;
    }
}

