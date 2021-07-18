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


abstract class Aggregator {
    protected android.bordeaux.services.AggregatorManager mAggregatorManager;

    public void setManager(android.bordeaux.services.AggregatorManager m) {
        mAggregatorManager = m;
    }

    public abstract java.lang.String[] getListOfFeatures();

    public abstract java.util.Map<java.lang.String, java.lang.String> getFeatureValue(java.lang.String featureName);
}

