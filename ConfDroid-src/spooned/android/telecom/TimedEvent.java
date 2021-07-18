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
 * limitations under the License
 */
package android.telecom;


/**
 *
 *
 * @unknown 
 */
public abstract class TimedEvent<T> {
    public abstract long getTime();

    public abstract T getKey();

    public static <T> java.util.Map<T, java.lang.Double> averageTimings(java.util.Collection<? extends android.telecom.TimedEvent<T>> events) {
        java.util.HashMap<T, java.lang.Integer> counts = new java.util.HashMap<>();
        java.util.HashMap<T, java.lang.Double> result = new java.util.HashMap<>();
        for (android.telecom.TimedEvent<T> entry : events) {
            if (counts.containsKey(entry.getKey())) {
                counts.put(entry.getKey(), counts.get(entry.getKey()) + 1);
                result.put(entry.getKey(), result.get(entry.getKey()) + entry.getTime());
            } else {
                counts.put(entry.getKey(), 1);
                result.put(entry.getKey(), ((double) (entry.getTime())));
            }
        }
        for (java.util.Map.Entry<T, java.lang.Double> entry : result.entrySet()) {
            result.put(entry.getKey(), entry.getValue() / counts.get(entry.getKey()));
        }
        return result;
    }
}

