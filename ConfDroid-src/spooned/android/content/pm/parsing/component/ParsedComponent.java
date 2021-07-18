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
public abstract class ParsedComponent implements android.os.Parcelable {
    private static android.content.pm.parsing.component.ParsedIntentInfo.ListParceler sForIntentInfos = Parcelling.Cache.getOrCreate(android.content.pm.parsing.component.ParsedIntentInfo.ListParceler.class);

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String name;

    int icon;

    int labelRes;

    @android.annotation.Nullable
    java.lang.CharSequence nonLocalizedLabel;

    int logo;

    int banner;

    int descriptionRes;

    // TODO(b/135203078): Replace flags with individual booleans, scoped by subclass
    int flags;

    @android.annotation.NonNull
    @com.android.internal.util.DataClass.ParcelWith(com.android.internal.util.Parcelling.BuiltIn.ForInternedString.class)
    private java.lang.String packageName;

    @android.annotation.Nullable
    @com.android.internal.util.DataClass.PluralOf("intent")
    @com.android.internal.util.DataClass.ParcelWith(android.content.pm.parsing.component.ParsedIntentInfo.ListParceler.class)
    private java.util.List<android.content.pm.parsing.component.ParsedIntentInfo> intents;

    private android.content.ComponentName componentName;

    @android.annotation.Nullable
    protected android.os.Bundle metaData;

    ParsedComponent() {
    }

    @java.lang.SuppressWarnings("IncompleteCopyConstructor")
    public ParsedComponent(android.content.pm.parsing.component.ParsedComponent other) {
        this.metaData = other.metaData;
        this.name = other.name;
        this.icon = other.getIcon();
        this.labelRes = other.getLabelRes();
        this.nonLocalizedLabel = other.getNonLocalizedLabel();
        this.logo = other.getLogo();
        this.banner = other.getBanner();
        this.descriptionRes = other.getDescriptionRes();
        this.flags = other.getFlags();
        this.setPackageName(other.packageName);
        this.intents = new java.util.ArrayList<>(other.getIntents());
    }

    public void addIntent(android.content.pm.parsing.component.ParsedIntentInfo intent) {
        this.intents = com.android.internal.util.CollectionUtils.add(this.intents, intent);
    }

    @android.annotation.NonNull
    public java.util.List<android.content.pm.parsing.component.ParsedIntentInfo> getIntents() {
        return intents != null ? intents : java.util.Collections.emptyList();
    }

    public android.content.pm.parsing.component.ParsedComponent setName(java.lang.String name) {
        this.name = android.text.TextUtils.safeIntern(name);
        return this;
    }

    @android.annotation.CallSuper
    public void setPackageName(@android.annotation.NonNull
    java.lang.String packageName) {
        this.packageName = android.text.TextUtils.safeIntern(packageName);
        // noinspection ConstantConditions
        this.componentName = null;
        // Note: this method does not edit name (which can point to a class), because this package
        // name change is not changing the package in code, but the identifier used by the system.
    }

    @android.annotation.NonNull
    public android.content.ComponentName getComponentName() {
        if (componentName == null) {
            componentName = new android.content.ComponentName(getPackageName(), getName());
        }
        return componentName;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeInt(this.getIcon());
        dest.writeInt(this.getLabelRes());
        dest.writeCharSequence(this.getNonLocalizedLabel());
        dest.writeInt(this.getLogo());
        dest.writeInt(this.getBanner());
        dest.writeInt(this.getDescriptionRes());
        dest.writeInt(this.getFlags());
        android.content.pm.parsing.ParsingPackageImpl.sForInternedString.parcel(this.packageName, dest, flags);
        android.content.pm.parsing.component.ParsedComponent.sForIntentInfos.parcel(this.getIntents(), dest, flags);
        dest.writeBundle(this.metaData);
    }

    protected ParsedComponent(android.os.Parcel in) {
        // We use the boot classloader for all classes that we load.
        final java.lang.ClassLoader boot = java.lang.Object.class.getClassLoader();
        // noinspection ConstantConditions
        this.name = in.readString();
        this.icon = in.readInt();
        this.labelRes = in.readInt();
        this.nonLocalizedLabel = in.readCharSequence();
        this.logo = in.readInt();
        this.banner = in.readInt();
        this.descriptionRes = in.readInt();
        this.flags = in.readInt();
        // noinspection ConstantConditions
        this.packageName = android.content.pm.parsing.ParsingPackageImpl.sForInternedString.unparcel(in);
        this.intents = android.content.pm.parsing.component.ParsedComponent.sForIntentInfos.unparcel(in);
        this.metaData = in.readBundle(boot);
    }

    @android.annotation.NonNull
    public java.lang.String getName() {
        return name;
    }

    public int getIcon() {
        return icon;
    }

    public int getLabelRes() {
        return labelRes;
    }

    @android.annotation.Nullable
    public java.lang.CharSequence getNonLocalizedLabel() {
        return nonLocalizedLabel;
    }

    public int getLogo() {
        return logo;
    }

    public int getBanner() {
        return banner;
    }

    public int getDescriptionRes() {
        return descriptionRes;
    }

    public int getFlags() {
        return flags;
    }

    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return packageName;
    }

    @android.annotation.Nullable
    public android.os.Bundle getMetaData() {
        return metaData;
    }
}

