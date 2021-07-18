/**
 * Copyright (C) 2020 The Android Open Source Project
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
package android.content.pm.parsing.component;


/**
 *
 *
 * @unknown *
 */
public final class ParsedIntentInfo extends android.content.IntentFilter {
    public static final android.content.pm.parsing.component.ParsedIntentInfo.Parceler PARCELER = new android.content.pm.parsing.component.ParsedIntentInfo.Parceler();

    public static class Parceler implements com.android.internal.util.Parcelling<android.content.pm.parsing.component.ParsedIntentInfo> {
        @java.lang.Override
        public void parcel(android.content.pm.parsing.component.ParsedIntentInfo item, android.os.Parcel dest, int parcelFlags) {
            item.writeIntentInfoToParcel(dest, parcelFlags);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedIntentInfo unparcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedIntentInfo(source);
        }
    }

    public static class ListParceler implements com.android.internal.util.Parcelling<java.util.List<android.content.pm.parsing.component.ParsedIntentInfo>> {
        /**
         * <p>
         * Implementation note: The serialized form for the intent list also contains the name
         * of the concrete class that's stored in the list, and assumes that every element of the
         * list is of the same type. This is very similar to the original parcelable mechanism.
         * We cannot use that directly because IntentInfo extends IntentFilter, which is parcelable
         * and is public API. It also declares Parcelable related methods as final which means
         * we can't extend them. The approach of using composition instead of inheritance leads to
         * a large set of cascading changes in the PackageManagerService, which seem undesirable.
         *
         * <p>
         * <b>WARNING: </b> The list of objects returned by this function might need to be fixed up
         * to make sure their owner fields are consistent. See {@code fixupOwner}.
         */
        @java.lang.Override
        public void parcel(java.util.List<android.content.pm.parsing.component.ParsedIntentInfo> item, android.os.Parcel dest, int parcelFlags) {
            if (item == null) {
                dest.writeInt(-1);
                return;
            }
            final int size = item.size();
            dest.writeInt(size);
            for (int index = 0; index < size; index++) {
                android.content.pm.parsing.component.ParsedIntentInfo.PARCELER.parcel(item.get(index), dest, parcelFlags);
            }
        }

        @java.lang.Override
        public java.util.List<android.content.pm.parsing.component.ParsedIntentInfo> unparcel(android.os.Parcel source) {
            int size = source.readInt();
            if (size == (-1)) {
                return null;
            }
            if (size == 0) {
                return new java.util.ArrayList<>(0);
            }
            final java.util.ArrayList<android.content.pm.parsing.component.ParsedIntentInfo> intentsList = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                intentsList.add(android.content.pm.parsing.component.ParsedIntentInfo.PARCELER.unparcel(source));
            }
            return intentsList;
        }
    }

    public static class StringPairListParceler implements com.android.internal.util.Parcelling<java.util.List<android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo>>> {
        @java.lang.Override
        public void parcel(java.util.List<android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo>> item, android.os.Parcel dest, int parcelFlags) {
            if (item == null) {
                dest.writeInt(-1);
                return;
            }
            final int size = item.size();
            dest.writeInt(size);
            for (int index = 0; index < size; index++) {
                android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo> pair = item.get(index);
                dest.writeString(pair.first);
                android.content.pm.parsing.component.ParsedIntentInfo.PARCELER.parcel(pair.second, dest, parcelFlags);
            }
        }

        @java.lang.Override
        public java.util.List<android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo>> unparcel(android.os.Parcel source) {
            int size = source.readInt();
            if (size == (-1)) {
                return null;
            }
            if (size == 0) {
                return new java.util.ArrayList<>(0);
            }
            final java.util.List<android.util.Pair<java.lang.String, android.content.pm.parsing.component.ParsedIntentInfo>> list = new java.util.ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                list.add(android.util.Pair.create(source.readString(), android.content.pm.parsing.component.ParsedIntentInfo.PARCELER.unparcel(source)));
            }
            return list;
        }
    }

    boolean hasDefault;

    int labelRes;

    @android.annotation.Nullable
    java.lang.CharSequence nonLocalizedLabel;

    int icon;

    public ParsedIntentInfo() {
    }

    public ParsedIntentInfo(android.os.Parcel in) {
        super(in);
        hasDefault = in.readBoolean();
        labelRes = in.readInt();
        nonLocalizedLabel = in.readCharSequence();
        icon = in.readInt();
    }

    public void writeIntentInfoToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeBoolean(hasDefault);
        dest.writeInt(labelRes);
        dest.writeCharSequence(nonLocalizedLabel);
        dest.writeInt(icon);
    }

    public java.lang.String toString() {
        return ("ProviderIntentInfo{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + '}';
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedIntentInfo> CREATOR = new android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedIntentInfo>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedIntentInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedIntentInfo(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedIntentInfo[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedIntentInfo[size];
        }
    };

    public boolean isHasDefault() {
        return hasDefault;
    }

    public int getLabelRes() {
        return labelRes;
    }

    @android.annotation.Nullable
    public java.lang.CharSequence getNonLocalizedLabel() {
        return nonLocalizedLabel;
    }

    public int getIcon() {
        return icon;
    }
}

