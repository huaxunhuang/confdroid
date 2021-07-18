/**
 * Copyright (C) 2018 The Android Open Source Project
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
package android.view;


/**
 * Defines which animation types should be overridden by which remote animation.
 *
 * @unknown 
 */
public class RemoteAnimationDefinition implements android.os.Parcelable {
    private final android.util.SparseArray<android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry> mTransitionAnimationMap;

    @android.annotation.UnsupportedAppUsage
    public RemoteAnimationDefinition() {
        mTransitionAnimationMap = new android.util.SparseArray();
    }

    /**
     * Registers a remote animation for a specific transition.
     *
     * @param transition
     * 		The transition type. Must be one of WindowManager.TRANSIT_* values.
     * @param activityTypeFilter
     * 		The remote animation only runs if an activity with type of this
     * 		parameter is involved in the transition.
     * @param adapter
     * 		The adapter that described how to run the remote animation.
     */
    @android.annotation.UnsupportedAppUsage
    public void addRemoteAnimation(@android.view.WindowManager.TransitionType
    int transition, @android.app.WindowConfiguration.ActivityType
    int activityTypeFilter, android.view.RemoteAnimationAdapter adapter) {
        mTransitionAnimationMap.put(transition, new android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry(adapter, activityTypeFilter));
    }

    /**
     * Registers a remote animation for a specific transition without defining an activity type
     * filter.
     *
     * @param transition
     * 		The transition type. Must be one of WindowManager.TRANSIT_* values.
     * @param adapter
     * 		The adapter that described how to run the remote animation.
     */
    @android.annotation.UnsupportedAppUsage
    public void addRemoteAnimation(@android.view.WindowManager.TransitionType
    int transition, android.view.RemoteAnimationAdapter adapter) {
        addRemoteAnimation(transition, android.app.WindowConfiguration.ACTIVITY_TYPE_UNDEFINED, adapter);
    }

    /**
     * Checks whether a remote animation for specific transition is defined.
     *
     * @param transition
     * 		The transition type. Must be one of WindowManager.TRANSIT_* values.
     * @param activityTypes
     * 		The set of activity types of activities that are involved in the
     * 		transition. Will be used for filtering.
     * @return Whether this definition has defined a remote animation for the specified transition.
     */
    public boolean hasTransition(@android.view.WindowManager.TransitionType
    int transition, android.util.ArraySet<java.lang.Integer> activityTypes) {
        return getAdapter(transition, activityTypes) != null;
    }

    /**
     * Retrieves the remote animation for a specific transition.
     *
     * @param transition
     * 		The transition type. Must be one of WindowManager.TRANSIT_* values.
     * @param activityTypes
     * 		The set of activity types of activities that are involved in the
     * 		transition. Will be used for filtering.
     * @return The remote animation adapter for the specified transition.
     */
    @android.annotation.Nullable
    public android.view.RemoteAnimationAdapter getAdapter(@android.view.WindowManager.TransitionType
    int transition, android.util.ArraySet<java.lang.Integer> activityTypes) {
        final android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry entry = mTransitionAnimationMap.get(transition);
        if (entry == null) {
            return null;
        }
        if ((entry.activityTypeFilter == android.app.WindowConfiguration.ACTIVITY_TYPE_UNDEFINED) || activityTypes.contains(entry.activityTypeFilter)) {
            return entry.adapter;
        } else {
            return null;
        }
    }

    public RemoteAnimationDefinition(android.os.Parcel in) {
        final int size = in.readInt();
        mTransitionAnimationMap = new android.util.SparseArray(size);
        for (int i = 0; i < size; i++) {
            final int transition = in.readInt();
            final android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry entry = in.readTypedObject(android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry.CREATOR);
            mTransitionAnimationMap.put(transition, entry);
        }
    }

    /**
     * To be called by system_server to keep track which pid is running the remote animations inside
     * this definition.
     */
    public void setCallingPid(int pid) {
        for (int i = mTransitionAnimationMap.size() - 1; i >= 0; i--) {
            setCallingPid(pid);
        }
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        final int size = mTransitionAnimationMap.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            dest.writeInt(mTransitionAnimationMap.keyAt(i));
            dest.writeTypedObject(mTransitionAnimationMap.valueAt(i), flags);
        }
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.RemoteAnimationDefinition> CREATOR = new android.view.Creator<android.view.RemoteAnimationDefinition>() {
        public android.view.RemoteAnimationDefinition createFromParcel(android.os.Parcel in) {
            return new android.view.RemoteAnimationDefinition(in);
        }

        public android.view.RemoteAnimationDefinition[] newArray(int size) {
            return new android.view.RemoteAnimationDefinition[size];
        }
    };

    private static class RemoteAnimationAdapterEntry implements android.os.Parcelable {
        final android.view.RemoteAnimationAdapter adapter;

        /**
         * Only run the transition if one of the activities matches the filter.
         * {@link WindowConfiguration.ACTIVITY_TYPE_UNDEFINED} means no filter
         */
        @android.app.WindowConfiguration.ActivityType
        final int activityTypeFilter;

        RemoteAnimationAdapterEntry(android.view.RemoteAnimationAdapter adapter, int activityTypeFilter) {
            this.adapter = adapter;
            this.activityTypeFilter = activityTypeFilter;
        }

        private RemoteAnimationAdapterEntry(android.os.Parcel in) {
            adapter = in.readParcelable(android.view.RemoteAnimationAdapter.class.getClassLoader());
            activityTypeFilter = in.readInt();
        }

        @java.lang.Override
        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeParcelable(adapter, flags);
            dest.writeInt(activityTypeFilter);
        }

        @java.lang.Override
        public int describeContents() {
            return 0;
        }

        @android.annotation.NonNull
        private static final android.view.Creator<android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry> CREATOR = new android.view.Creator<android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry>() {
            @java.lang.Override
            public android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry createFromParcel(android.os.Parcel in) {
                return new android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry(in);
            }

            @java.lang.Override
            public android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry[] newArray(int size) {
                return new android.view.RemoteAnimationDefinition.RemoteAnimationAdapterEntry[size];
            }
        };
    }
}

