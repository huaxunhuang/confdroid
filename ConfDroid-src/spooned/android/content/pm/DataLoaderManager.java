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
 * Data loader manager takes care of data loaders of different packages. It provides methods to
 * initialize a data loader binder service (binding and creating it), to return a binder of the data
 * loader binder service and to destroy a data loader binder service.
 *
 * @see com.android.server.pm.DataLoaderManagerService
 * @unknown 
 */
public class DataLoaderManager {
    private static final java.lang.String TAG = "DataLoaderManager";

    private final android.content.pm.IDataLoaderManager mService;

    public DataLoaderManager(android.content.pm.IDataLoaderManager service) {
        mService = service;
    }

    /**
     * Finds a data loader binder service and binds to it. This requires PackageManager.
     *
     * @param dataLoaderId
     * 		ID for the new data loader binder service.
     * @param params
     * 		DataLoaderParamsParcel object that contains data loader params, including
     * 		its package name, class name, and additional parameters.
     * @param listener
     * 		Callback for the data loader service to report status back to the
     * 		caller.
     * @return false if 1) target ID collides with a data loader that is already bound to data
    loader manager; 2) package name is not specified; 3) fails to find data loader package;
    or 4) fails to bind to the specified data loader service, otherwise return true.
     */
    public boolean bindToDataLoader(int dataLoaderId, @android.annotation.NonNull
    android.content.pm.DataLoaderParamsParcel params, @android.annotation.NonNull
    android.content.pm.IDataLoaderStatusListener listener) {
        try {
            return mService.bindToDataLoader(dataLoaderId, params, listener);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Returns a binder interface of the data loader binder service, given its ID.
     */
    @android.annotation.Nullable
    public android.content.pm.IDataLoader getDataLoader(int dataLoaderId) {
        try {
            return mService.getDataLoader(dataLoaderId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }

    /**
     * Unbinds from a data loader binder service, specified by its ID.
     * DataLoader will receive destroy notification.
     */
    @android.annotation.Nullable
    public void unbindFromDataLoader(int dataLoaderId) {
        try {
            mService.unbindFromDataLoader(dataLoaderId);
        } catch (android.os.RemoteException e) {
            throw e.rethrowFromSystemServer();
        }
    }
}

