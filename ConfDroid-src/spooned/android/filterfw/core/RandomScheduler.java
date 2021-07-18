/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.filterfw.core;


/**
 *
 *
 * @unknown 
 */
public class RandomScheduler extends android.filterfw.core.Scheduler {
    private java.util.Random mRand = new java.util.Random();

    public RandomScheduler(android.filterfw.core.FilterGraph graph) {
        super(graph);
    }

    @java.lang.Override
    public void reset() {
    }

    @java.lang.Override
    public android.filterfw.core.Filter scheduleNextNode() {
        java.util.Vector<android.filterfw.core.Filter> candidates = new java.util.Vector<android.filterfw.core.Filter>();
        for (android.filterfw.core.Filter filter : getGraph().getFilters()) {
            if (filter.canProcess())
                candidates.add(filter);

        }
        if (candidates.size() > 0) {
            int r = mRand.nextInt(candidates.size());
            return candidates.elementAt(r);
        }
        return null;
    }
}

