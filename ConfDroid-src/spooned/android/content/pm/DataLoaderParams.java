/**
 * Copyright (C) 2019 The Android Open Source Project
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
package android.content.pm;


/**
 * This class represents the parameters used to configure a Data Loader.
 *
 * WARNING: This is a system API to aid internal development.
 * Use at your own risk. It will change or be removed without warning.
 *
 * @unknown 
 */
@android.annotation.SystemApi
public class DataLoaderParams {
    @android.annotation.NonNull
    private final android.content.pm.DataLoaderParamsParcel mData;

    /**
     * Creates and populates set of Data Loader parameters for Streaming installation.
     *
     * @param componentName
     * 		Data Loader component supporting Streaming installation.
     * @param arguments
     * 		free form installation arguments
     */
    @android.annotation.NonNull
    public static final android.content.pm.DataLoaderParams forStreaming(@android.annotation.NonNull
    android.content.ComponentName componentName, @android.annotation.NonNull
    java.lang.String arguments) {
        return new android.content.pm.DataLoaderParams(DataLoaderType.STREAMING, componentName, arguments);
    }

    /**
     * Creates and populates set of Data Loader parameters for Incremental installation.
     *
     * @param componentName
     * 		Data Loader component supporting Incremental installation.
     * @param arguments
     * 		free form installation arguments
     */
    @android.annotation.NonNull
    public static final android.content.pm.DataLoaderParams forIncremental(@android.annotation.NonNull
    android.content.ComponentName componentName, @android.annotation.NonNull
    java.lang.String arguments) {
        return new android.content.pm.DataLoaderParams(DataLoaderType.INCREMENTAL, componentName, arguments);
    }

    /**
     *
     *
     * @unknown 
     */
    public DataLoaderParams(@android.annotation.NonNull
    @android.content.pm.DataLoaderType
    int type, @android.annotation.NonNull
    android.content.ComponentName componentName, @android.annotation.NonNull
    java.lang.String arguments) {
        android.content.pm.DataLoaderParamsParcel data = new android.content.pm.DataLoaderParamsParcel();
        data.type = type;
        data.packageName = componentName.getPackageName();
        data.className = componentName.getClassName();
        data.arguments = arguments;
        mData = data;
    }

    /**
     *
     *
     * @unknown 
     */
    DataLoaderParams(@android.annotation.NonNull
    android.content.pm.DataLoaderParamsParcel data) {
        mData = data;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.NonNull
    public final android.content.pm.DataLoaderParamsParcel getData() {
        return mData;
    }

    /**
     *
     *
     * @return data loader type
     */
    @android.annotation.NonNull
    @android.content.pm.DataLoaderType
    public final int getType() {
        return mData.type;
    }

    /**
     *
     *
     * @return data loader's component name
     */
    @android.annotation.NonNull
    public final android.content.ComponentName getComponentName() {
        return new android.content.ComponentName(mData.packageName, mData.className);
    }

    /**
     *
     *
     * @return data loader's arguments
     */
    @android.annotation.NonNull
    public final java.lang.String getArguments() {
        return mData.arguments;
    }
}

