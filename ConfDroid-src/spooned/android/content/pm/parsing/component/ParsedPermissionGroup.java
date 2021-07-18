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
 * @unknown 
 */
public class ParsedPermissionGroup extends android.content.pm.parsing.component.ParsedComponent {
    int requestDetailResourceId;

    int backgroundRequestResourceId;

    int backgroundRequestDetailResourceId;

    int requestRes;

    int priority;

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public java.lang.String toString() {
        return ((("PermissionGroup{" + java.lang.Integer.toHexString(java.lang.System.identityHashCode(this))) + " ") + getName()) + "}";
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.requestDetailResourceId);
        dest.writeInt(this.backgroundRequestResourceId);
        dest.writeInt(this.backgroundRequestDetailResourceId);
        dest.writeInt(this.requestRes);
        dest.writeInt(this.priority);
    }

    public ParsedPermissionGroup() {
    }

    protected ParsedPermissionGroup(android.os.Parcel in) {
        super(in);
        this.requestDetailResourceId = in.readInt();
        this.backgroundRequestResourceId = in.readInt();
        this.backgroundRequestDetailResourceId = in.readInt();
        this.requestRes = in.readInt();
        this.priority = in.readInt();
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedPermissionGroup> CREATOR = new android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedPermissionGroup>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedPermissionGroup createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedPermissionGroup(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedPermissionGroup[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedPermissionGroup[size];
        }
    };

    public int getRequestDetailResourceId() {
        return requestDetailResourceId;
    }

    public int getBackgroundRequestResourceId() {
        return backgroundRequestResourceId;
    }

    public int getBackgroundRequestDetailResourceId() {
        return backgroundRequestDetailResourceId;
    }

    public int getRequestRes() {
        return requestRes;
    }

    public int getPriority() {
        return priority;
    }
}

