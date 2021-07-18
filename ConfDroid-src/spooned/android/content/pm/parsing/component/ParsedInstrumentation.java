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
public class ParsedInstrumentation extends android.content.pm.parsing.component.ParsedComponent {
    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String targetPackage;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String targetProcesses;

    boolean handleProfiling;

    boolean functionalTest;

    public ParsedInstrumentation() {
    }

    public void setTargetPackage(@android.annotation.Nullable
    java.lang.String targetPackage) {
        this.targetPackage = android.text.TextUtils.safeIntern(targetPackage);
    }

    public void setTargetProcesses(@android.annotation.Nullable
    java.lang.String targetProcesses) {
        this.targetProcesses = android.text.TextUtils.safeIntern(targetProcesses);
    }

    public java.lang.String toString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("Instrumentation{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        android.content.ComponentName.appendShortString(sb, getPackageName(), getName());
        sb.append('}');
        return sb.toString();
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.targetPackage, dest, flags);
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.targetProcesses, dest, flags);
        dest.writeBoolean(this.handleProfiling);
        dest.writeBoolean(this.functionalTest);
    }

    protected ParsedInstrumentation(android.os.Parcel in) {
        super(in);
        this.targetPackage = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.targetProcesses = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.handleProfiling = in.readByte() != 0;
        this.functionalTest = in.readByte() != 0;
    }

    public static final android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedInstrumentation> CREATOR = new android.os.Parcelable.Creator<android.content.pm.parsing.component.ParsedInstrumentation>() {
        @java.lang.Override
        public android.content.pm.parsing.component.ParsedInstrumentation createFromParcel(android.os.Parcel source) {
            return new android.content.pm.parsing.component.ParsedInstrumentation(source);
        }

        @java.lang.Override
        public android.content.pm.parsing.component.ParsedInstrumentation[] newArray(int size) {
            return new android.content.pm.parsing.component.ParsedInstrumentation[size];
        }
    };

    @android.annotation.Nullable
    public java.lang.String getTargetPackage() {
        return targetPackage;
    }

    @android.annotation.Nullable
    public java.lang.String getTargetProcesses() {
        return targetProcesses;
    }

    public boolean isHandleProfiling() {
        return handleProfiling;
    }

    public boolean isFunctionalTest() {
        return functionalTest;
    }
}

