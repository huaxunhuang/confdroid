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
public class ParsedMainComponent extends android.content.pm.parsing.component.ParsedComponent {
    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String processName;

    boolean directBootAware;

    boolean enabled = true;

    boolean exported;

    int order;

    @android.annotation.Nullable
    java.lang.String splitName;

    public ParsedMainComponent() {
    }

    public ParsedMainComponent(android.content.pm.parsing.component.ParsedMainComponent other) {
        super(other);
        this.processName = other.processName;
        this.directBootAware = other.directBootAware;
        this.enabled = other.enabled;
        this.exported = other.exported;
        this.order = other.order;
        this.splitName = other.splitName;
    }

    public android.content.pm.parsing.component.ParsedMainComponent setProcessName(java.lang.String processName) {
        this.processName = android.text.TextUtils.safeIntern(processName);
        return this;
    }

    public android.content.pm.parsing.component.ParsedMainComponent setEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    /**
     * A main component's name is a class name. This makes code slightly more readable.
     */
    public java.lang.String getClassName() {
        return getName();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.processName, dest, flags);
        dest.writeBoolean(this.directBootAware);
        dest.writeBoolean(this.enabled);
        dest.writeBoolean(this.exported);
        dest.writeInt(this.order);
        dest.writeString(this.splitName);
    }

    protected ParsedMainComponent(android.os.Parcel in) {
        super(in);
        this.processName = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.directBootAware = in.readBoolean();
        this.enabled = in.readBoolean();
        this.exported = in.readBoolean();
        this.order = in.readInt();
        this.splitName = in.readString();
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedMainComponent> CREATOR = new android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedMainComponent>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedMainComponent createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedMainComponent(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedMainComponent[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedMainComponent[size];
        }
    };

    @android.annotation.Nullable
    public java.lang.String getProcessName() {
        return processName;
    }

    public boolean isDirectBootAware() {
        return directBootAware;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isExported() {
        return exported;
    }

    public int getOrder() {
        return order;
    }

    @android.annotation.Nullable
    public java.lang.String getSplitName() {
        return splitName;
    }

    public android.content.pm.parsing.component.ParsedMainComponent setDirectBootAware(boolean value) {
        directBootAware = value;
        return this;
    }

    public android.content.pm.parsing.component.ParsedMainComponent setExported(boolean value) {
        exported = value;
        return this;
    }

    public android.content.pm.parsing.component.ParsedMainComponent setSplitName(@android.annotation.Nullable
    java.lang.String value) {
        splitName = value;
        return this;
    }
}

