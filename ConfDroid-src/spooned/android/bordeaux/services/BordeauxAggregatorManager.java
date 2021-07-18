/**
 * Copyright (C) 2012 The Android Open Source Project
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
package android.bordeaux.services;


/**
 * AggregatorManger for Learning framework.
 */
public class BordeauxAggregatorManager {
    static final java.lang.String TAG = "BordeauxAggregatorManager";

    static final java.lang.String AggregatorManager_NOTAVAILABLE = "AggregatorManager not Available";

    private android.content.Context mContext;

    private android.bordeaux.services.IAggregatorManager mAggregatorManager;

    public boolean retrieveAggregatorManager() {
        if (mAggregatorManager == null) {
            mAggregatorManager = android.bordeaux.services.BordeauxManagerService.getAggregatorManager(mContext);
            if (mAggregatorManager == null) {
                android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
                return false;
            }
        }
        return true;
    }

    public BordeauxAggregatorManager(android.content.Context context) {
        mContext = context;
        mAggregatorManager = android.bordeaux.services.BordeauxManagerService.getAggregatorManager(mContext);
    }

    public java.util.Map<java.lang.String, java.lang.String> GetData(final java.lang.String dataName) {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return getMap(mAggregatorManager.getData(dataName));
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Exception in Getting " + dataName);
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    public java.util.List<java.lang.String> getLocationClusters() {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return mAggregatorManager.getLocationClusters();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Error getting location clusters");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    public java.util.List<java.lang.String> getTimeOfDayValues() {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return mAggregatorManager.getTimeOfDayValues();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Error getting time of day values");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    public java.util.List<java.lang.String> getDayOfWeekValues() {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return mAggregatorManager.getDayOfWeekValues();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Error getting day of week values");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    public boolean setFakeLocation(final java.lang.String name) {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return mAggregatorManager.setFakeLocation(name);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Error setting fake location:" + name);
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    public boolean setFakeTimeOfDay(final java.lang.String time_of_day) {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return mAggregatorManager.setFakeTimeOfDay(time_of_day);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Error setting fake time of day:" + time_of_day);
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    public boolean setFakeDayOfWeek(final java.lang.String day_of_week) {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return mAggregatorManager.setFakeDayOfWeek(day_of_week);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Error setting fake day of week:" + day_of_week);
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    public boolean getFakeMode() {
        if (!retrieveAggregatorManager())
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);

        try {
            return mAggregatorManager.getFakeMode();
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.bordeaux.services.BordeauxAggregatorManager.TAG, "Error getting fake mode");
            throw new java.lang.RuntimeException(android.bordeaux.services.BordeauxAggregatorManager.AggregatorManager_NOTAVAILABLE);
        }
    }

    private java.util.Map<java.lang.String, java.lang.String> getMap(final java.util.List<android.bordeaux.services.StringString> sample) {
        java.util.HashMap<java.lang.String, java.lang.String> map = new java.util.HashMap<java.lang.String, java.lang.String>();
        for (int i = 0; i < sample.size(); i++) {
            map.put(sample.get(i).key, sample.get(i).value);
        }
        return ((java.util.Map) (map));
    }
}

