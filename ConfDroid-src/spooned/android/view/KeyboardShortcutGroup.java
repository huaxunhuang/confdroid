/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.view;


/**
 * A group of {@link KeyboardShortcutInfo}.
 */
public final class KeyboardShortcutGroup implements android.os.Parcelable {
    private final java.lang.CharSequence mLabel;

    private final java.util.List<android.view.KeyboardShortcutInfo> mItems;

    // The system group looks different UI wise.
    private boolean mSystemGroup;

    /**
     *
     *
     * @param label
     * 		The title to be used for this group, or null if there is none.
     * @param items
     * 		The set of items to be included.
     */
    public KeyboardShortcutGroup(@android.annotation.Nullable
    java.lang.CharSequence label, @android.annotation.NonNull
    java.util.List<android.view.KeyboardShortcutInfo> items) {
        mLabel = label;
        mItems = new java.util.ArrayList(com.android.internal.util.Preconditions.checkNotNull(items));
    }

    /**
     *
     *
     * @param label
     * 		The title to be used for this group, or null if there is none.
     */
    public KeyboardShortcutGroup(@android.annotation.Nullable
    java.lang.CharSequence label) {
        this(label, java.util.Collections.<android.view.KeyboardShortcutInfo>emptyList());
    }

    /**
     *
     *
     * @param label
     * 		The title to be used for this group, or null if there is none.
     * @param items
     * 		The set of items to be included.
     * @param isSystemGroup
     * 		Set this to {@code true} if this is s system group.
     * @unknown 
     */
    @android.annotation.TestApi
    public KeyboardShortcutGroup(@android.annotation.Nullable
    java.lang.CharSequence label, @android.annotation.NonNull
    java.util.List<android.view.KeyboardShortcutInfo> items, boolean isSystemGroup) {
        mLabel = label;
        mItems = new java.util.ArrayList(com.android.internal.util.Preconditions.checkNotNull(items));
        mSystemGroup = isSystemGroup;
    }

    /**
     *
     *
     * @param label
     * 		The title to be used for this group, or null if there is none.
     * @param isSystemGroup
     * 		Set this to {@code true} if this is s system group.
     * @unknown 
     */
    @android.annotation.TestApi
    public KeyboardShortcutGroup(@android.annotation.Nullable
    java.lang.CharSequence label, boolean isSystemGroup) {
        this(label, java.util.Collections.<android.view.KeyboardShortcutInfo>emptyList(), isSystemGroup);
    }

    private KeyboardShortcutGroup(android.os.Parcel source) {
        mItems = new java.util.ArrayList<>();
        mLabel = source.readCharSequence();
        source.readTypedList(mItems, android.view.KeyboardShortcutInfo.CREATOR);
        mSystemGroup = source.readInt() == 1;
    }

    /**
     * Returns the label to be used to describe this group.
     */
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    /**
     * Returns the list of items included in this group.
     */
    public java.util.List<android.view.KeyboardShortcutInfo> getItems() {
        return mItems;
    }

    /**
     *
     *
     * @unknown *
     */
    @android.annotation.TestApi
    public boolean isSystemGroup() {
        return mSystemGroup;
    }

    /**
     * Adds an item to the existing list.
     *
     * @param item
     * 		The item to be added.
     */
    public void addItem(android.view.KeyboardShortcutInfo item) {
        mItems.add(item);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeCharSequence(mLabel);
        dest.writeTypedList(mItems);
        dest.writeInt(mSystemGroup ? 1 : 0);
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.KeyboardShortcutGroup> CREATOR = new android.view.Creator<android.view.KeyboardShortcutGroup>() {
        public android.view.KeyboardShortcutGroup createFromParcel(android.os.Parcel source) {
            return new android.view.KeyboardShortcutGroup(source);
        }

        public android.view.KeyboardShortcutGroup[] newArray(int size) {
            return new android.view.KeyboardShortcutGroup[size];
        }
    };
}

