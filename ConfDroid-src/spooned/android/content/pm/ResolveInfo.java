/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.content.pm;


/**
 * Information that is returned from resolving an intent
 * against an IntentFilter. This partially corresponds to
 * information collected from the AndroidManifest.xml's
 * &lt;intent&gt; tags.
 */
public class ResolveInfo implements android.os.Parcelable {
    private static final java.lang.String TAG = "ResolveInfo";

    /**
     * The activity or broadcast receiver that corresponds to this resolution
     * match, if this resolution is for an activity or broadcast receiver.
     * Exactly one of {@link #activityInfo}, {@link #serviceInfo}, or
     * {@link #providerInfo} will be non-null.
     */
    public android.content.pm.ActivityInfo activityInfo;

    /**
     * The service that corresponds to this resolution match, if this resolution
     * is for a service. Exactly one of {@link #activityInfo},
     * {@link #serviceInfo}, or {@link #providerInfo} will be non-null.
     */
    public android.content.pm.ServiceInfo serviceInfo;

    /**
     * The provider that corresponds to this resolution match, if this
     * resolution is for a provider. Exactly one of {@link #activityInfo},
     * {@link #serviceInfo}, or {@link #providerInfo} will be non-null.
     */
    public android.content.pm.ProviderInfo providerInfo;

    /**
     * An auxiliary response that may modify the resolved information. This is
     * only set under certain circumstances; such as when resolving instant apps
     * or components defined in un-installed splits.
     *
     * @unknown 
     */
    public android.content.pm.AuxiliaryResolveInfo auxiliaryInfo;

    /**
     * Whether or not an instant app is available for the resolved intent.
     */
    public boolean isInstantAppAvailable;

    /**
     *
     *
     * @unknown 
     */
    @java.lang.Deprecated
    public boolean instantAppAvailable;

    /**
     * The IntentFilter that was matched for this ResolveInfo.
     */
    public android.content.IntentFilter filter;

    /**
     * The declared priority of this match.  Comes from the "priority"
     * attribute or, if not set, defaults to 0.  Higher values are a higher
     * priority.
     */
    public int priority;

    /**
     * Order of result according to the user's preference.  If the user
     * has not set a preference for this result, the value is 0; higher
     * values are a higher priority.
     */
    public int preferredOrder;

    /**
     * The system's evaluation of how well the activity matches the
     * IntentFilter.  This is a match constant, a combination of
     * {@link IntentFilter#MATCH_CATEGORY_MASK IntentFilter.MATCH_CATEGORY_MASK}
     * and {@link IntentFilter#MATCH_ADJUSTMENT_MASK IntentFiler.MATCH_ADJUSTMENT_MASK}.
     */
    public int match;

    /**
     * Only set when returned by
     * {@link PackageManager#queryIntentActivityOptions}, this tells you
     * which of the given specific intents this result came from.  0 is the
     * first in the list, < 0 means it came from the generic Intent query.
     */
    public int specificIndex = -1;

    /**
     * This filter has specified the Intent.CATEGORY_DEFAULT, meaning it
     * would like to be considered a default action that the user can
     * perform on this data.
     */
    public boolean isDefault;

    /**
     * A string resource identifier (in the package's resources) of this
     * match's label.  From the "label" attribute or, if not set, 0.
     */
    public int labelRes;

    /**
     * The actual string retrieve from <var>labelRes</var> or null if none
     * was provided.
     */
    public java.lang.CharSequence nonLocalizedLabel;

    /**
     * A drawable resource identifier (in the package's resources) of this
     * match's icon.  From the "icon" attribute or, if not set, 0. It is
     * set only if the icon can be obtained by resource id alone.
     */
    public int icon;

    /**
     * Optional -- if non-null, the {@link #labelRes} and {@link #icon}
     * resources will be loaded from this package, rather than the one
     * containing the resolved component.
     */
    public java.lang.String resolvePackageName;

    /**
     * If not equal to UserHandle.USER_CURRENT, then the intent will be forwarded to this user.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public int targetUserId;

    /**
     * Set to true if the icon cannot be obtained by resource ids alone.
     * It is set to true for ResolveInfos from the managed profile: They need to
     * have their icon badged, so it cannot be obtained by resource ids alone.
     *
     * @unknown 
     */
    public boolean noResourceId;

    /**
     * Same as {@link #icon} but it will always correspond to "icon" attribute
     * regardless of {@link #noResourceId} value.
     *
     * @unknown 
     */
    public int iconResourceId;

    /**
     *
     *
     * @unknown Target comes from system process?
     */
    @android.annotation.UnsupportedAppUsage
    public boolean system;

    /**
     * Will be set to {@code true} if the {@link IntentFilter} responsible for intent
     * resolution is classified as a "browser".
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public boolean handleAllWebDataURI;

    /**
     * {@hide }
     */
    @android.annotation.UnsupportedAppUsage
    public android.content.pm.ComponentInfo getComponentInfo() {
        if (activityInfo != null)
            return activityInfo;

        if (serviceInfo != null)
            return serviceInfo;

        if (providerInfo != null)
            return providerInfo;

        throw new java.lang.IllegalStateException("Missing ComponentInfo!");
    }

    /**
     * Retrieve the current textual label associated with this resolution.  This
     * will call back on the given PackageManager to load the label from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the label can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a CharSequence containing the resolutions's label.  If the
    item does not have a label, its name is returned.
     */
    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) {
        if (nonLocalizedLabel != null) {
            return nonLocalizedLabel;
        }
        java.lang.CharSequence label;
        if ((resolvePackageName != null) && (labelRes != 0)) {
            label = pm.getText(resolvePackageName, labelRes, null);
            if (label != null) {
                return label.toString().trim();
            }
        }
        android.content.pm.ComponentInfo ci = getComponentInfo();
        android.content.pm.ApplicationInfo ai = ci.applicationInfo;
        if (labelRes != 0) {
            label = pm.getText(ci.packageName, labelRes, ai);
            if (label != null) {
                return label.toString().trim();
            }
        }
        java.lang.CharSequence data = ci.loadLabel(pm);
        // Make the data safe
        if (data != null)
            data = data.toString().trim();

        return data;
    }

    /**
     *
     *
     * @return The resource that would be used when loading
    the label for this resolve info.
     * @unknown 
     */
    public int resolveLabelResId() {
        if (labelRes != 0) {
            return labelRes;
        }
        final android.content.pm.ComponentInfo componentInfo = getComponentInfo();
        if (componentInfo.labelRes != 0) {
            return componentInfo.labelRes;
        }
        return componentInfo.applicationInfo.labelRes;
    }

    /**
     *
     *
     * @return The resource that would be used when loading
    the icon for this resolve info.
     * @unknown 
     */
    public int resolveIconResId() {
        if (icon != 0) {
            return icon;
        }
        final android.content.pm.ComponentInfo componentInfo = getComponentInfo();
        if (componentInfo.icon != 0) {
            return componentInfo.icon;
        }
        return componentInfo.applicationInfo.icon;
    }

    /**
     * Retrieve the current graphical icon associated with this resolution.  This
     * will call back on the given PackageManager to load the icon from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the icon can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the resolution's icon.  If the
    item does not have an icon, the default activity icon is returned.
     */
    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        android.graphics.drawable.Drawable dr = null;
        if ((resolvePackageName != null) && (iconResourceId != 0)) {
            dr = pm.getDrawable(resolvePackageName, iconResourceId, null);
        }
        android.content.pm.ComponentInfo ci = getComponentInfo();
        if ((dr == null) && (iconResourceId != 0)) {
            android.content.pm.ApplicationInfo ai = ci.applicationInfo;
            dr = pm.getDrawable(ci.packageName, iconResourceId, ai);
        }
        if (dr != null) {
            return pm.getUserBadgedIcon(dr, new android.os.UserHandle(pm.getUserId()));
        }
        return ci.loadIcon(pm);
    }

    /**
     * Return the icon resource identifier to use for this match.  If the
     * match defines an icon, that is used; else if the activity defines
     * an icon, that is used; else, the application icon is used.
     * This function does not check noResourceId flag.
     *
     * @return The icon associated with this match.
     */
    final int getIconResourceInternal() {
        if (iconResourceId != 0)
            return iconResourceId;

        final android.content.pm.ComponentInfo ci = getComponentInfo();
        if (ci != null) {
            return ci.getIconResource();
        }
        return 0;
    }

    /**
     * Return the icon resource identifier to use for this match.  If the
     * match defines an icon, that is used; else if the activity defines
     * an icon, that is used; else, the application icon is used.
     *
     * @return The icon associated with this match.
     */
    public final int getIconResource() {
        if (noResourceId)
            return 0;

        return getIconResourceInternal();
    }

    public void dump(android.util.Printer pw, java.lang.String prefix) {
        dump(pw, prefix, android.content.pm.PackageItemInfo.DUMP_FLAG_ALL);
    }

    /**
     *
     *
     * @unknown 
     */
    public void dump(android.util.Printer pw, java.lang.String prefix, int dumpFlags) {
        if (filter != null) {
            pw.println(prefix + "Filter:");
            filter.dump(pw, prefix + "  ");
        }
        pw.println((((((((((prefix + "priority=") + priority) + " preferredOrder=") + preferredOrder) + " match=0x") + java.lang.Integer.toHexString(match)) + " specificIndex=") + specificIndex) + " isDefault=") + isDefault);
        if (resolvePackageName != null) {
            pw.println((prefix + "resolvePackageName=") + resolvePackageName);
        }
        if (((labelRes != 0) || (nonLocalizedLabel != null)) || (icon != 0)) {
            pw.println((((((prefix + "labelRes=0x") + java.lang.Integer.toHexString(labelRes)) + " nonLocalizedLabel=") + nonLocalizedLabel) + " icon=0x") + java.lang.Integer.toHexString(icon));
        }
        if (activityInfo != null) {
            pw.println(prefix + "ActivityInfo:");
            activityInfo.dump(pw, prefix + "  ", dumpFlags);
        } else
            if (serviceInfo != null) {
                pw.println(prefix + "ServiceInfo:");
                serviceInfo.dump(pw, prefix + "  ", dumpFlags);
            } else
                if (providerInfo != null) {
                    pw.println(prefix + "ProviderInfo:");
                    providerInfo.dump(pw, prefix + "  ", dumpFlags);
                }


    }

    public ResolveInfo() {
        targetUserId = android.os.UserHandle.USER_CURRENT;
    }

    public ResolveInfo(android.content.pm.ResolveInfo orig) {
        activityInfo = orig.activityInfo;
        serviceInfo = orig.serviceInfo;
        providerInfo = orig.providerInfo;
        filter = orig.filter;
        priority = orig.priority;
        preferredOrder = orig.preferredOrder;
        match = orig.match;
        specificIndex = orig.specificIndex;
        labelRes = orig.labelRes;
        nonLocalizedLabel = orig.nonLocalizedLabel;
        icon = orig.icon;
        resolvePackageName = orig.resolvePackageName;
        noResourceId = orig.noResourceId;
        iconResourceId = orig.iconResourceId;
        system = orig.system;
        targetUserId = orig.targetUserId;
        handleAllWebDataURI = orig.handleAllWebDataURI;
        isInstantAppAvailable = orig.isInstantAppAvailable;
        instantAppAvailable = isInstantAppAvailable;
    }

    public java.lang.String toString() {
        final android.content.pm.ComponentInfo ci = getComponentInfo();
        java.lang.StringBuilder sb = new java.lang.StringBuilder(128);
        sb.append("ResolveInfo{");
        sb.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(this)));
        sb.append(' ');
        android.content.ComponentName.appendShortString(sb, ci.packageName, ci.name);
        if (priority != 0) {
            sb.append(" p=");
            sb.append(priority);
        }
        if (preferredOrder != 0) {
            sb.append(" o=");
            sb.append(preferredOrder);
        }
        sb.append(" m=0x");
        sb.append(java.lang.Integer.toHexString(match));
        if (targetUserId != android.os.UserHandle.USER_CURRENT) {
            sb.append(" targetUserId=");
            sb.append(targetUserId);
        }
        sb.append('}');
        return sb.toString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        if (activityInfo != null) {
            dest.writeInt(1);
            activityInfo.writeToParcel(dest, parcelableFlags);
        } else
            if (serviceInfo != null) {
                dest.writeInt(2);
                serviceInfo.writeToParcel(dest, parcelableFlags);
            } else
                if (providerInfo != null) {
                    dest.writeInt(3);
                    providerInfo.writeToParcel(dest, parcelableFlags);
                } else {
                    dest.writeInt(0);
                }


        if (filter != null) {
            dest.writeInt(1);
            filter.writeToParcel(dest, parcelableFlags);
        } else {
            dest.writeInt(0);
        }
        dest.writeInt(priority);
        dest.writeInt(preferredOrder);
        dest.writeInt(match);
        dest.writeInt(specificIndex);
        dest.writeInt(labelRes);
        android.text.TextUtils.writeToParcel(nonLocalizedLabel, dest, parcelableFlags);
        dest.writeInt(icon);
        dest.writeString(resolvePackageName);
        dest.writeInt(targetUserId);
        dest.writeInt(system ? 1 : 0);
        dest.writeInt(noResourceId ? 1 : 0);
        dest.writeInt(iconResourceId);
        dest.writeInt(handleAllWebDataURI ? 1 : 0);
        dest.writeInt(isInstantAppAvailable ? 1 : 0);
    }

    @android.annotation.NonNull
    public static final android.content.pm.Creator<android.content.pm.ResolveInfo> CREATOR = new android.content.pm.Creator<android.content.pm.ResolveInfo>() {
        public android.content.pm.ResolveInfo createFromParcel(android.os.Parcel source) {
            return new android.content.pm.ResolveInfo(source);
        }

        public android.content.pm.ResolveInfo[] newArray(int size) {
            return new android.content.pm.ResolveInfo[size];
        }
    };

    private ResolveInfo(android.os.Parcel source) {
        activityInfo = null;
        serviceInfo = null;
        providerInfo = null;
        switch (source.readInt()) {
            case 1 :
                activityInfo = this.CREATOR.createFromParcel(source);
                break;
            case 2 :
                serviceInfo = android.content.pm.ServiceInfo.CREATOR.createFromParcel(source);
                break;
            case 3 :
                providerInfo = this.CREATOR.createFromParcel(source);
                break;
            default :
                android.util.Slog.w(android.content.pm.ResolveInfo.TAG, "Missing ComponentInfo!");
                break;
        }
        if (source.readInt() != 0) {
            filter = this.CREATOR.createFromParcel(source);
        }
        priority = source.readInt();
        preferredOrder = source.readInt();
        match = source.readInt();
        specificIndex = source.readInt();
        labelRes = source.readInt();
        nonLocalizedLabel = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        icon = source.readInt();
        resolvePackageName = source.readString();
        targetUserId = source.readInt();
        system = source.readInt() != 0;
        noResourceId = source.readInt() != 0;
        iconResourceId = source.readInt();
        handleAllWebDataURI = source.readInt() != 0;
        instantAppAvailable = isInstantAppAvailable = source.readInt() != 0;
    }

    public static class DisplayNameComparator implements java.util.Comparator<android.content.pm.ResolveInfo> {
        public DisplayNameComparator(android.content.pm.PackageManager pm) {
            mPM = pm;
            mCollator.setStrength(java.text.Collator.PRIMARY);
        }

        public final int compare(android.content.pm.ResolveInfo a, android.content.pm.ResolveInfo b) {
            // We want to put the one targeted to another user at the end of the dialog.
            if (a.targetUserId != android.os.UserHandle.USER_CURRENT) {
                return 1;
            }
            if (b.targetUserId != android.os.UserHandle.USER_CURRENT) {
                return -1;
            }
            java.lang.CharSequence sa = a.loadLabel(mPM);
            if (sa == null)
                sa = a.activityInfo.name;

            java.lang.CharSequence sb = b.loadLabel(mPM);
            if (sb == null)
                sb = b.activityInfo.name;

            return mCollator.compare(sa.toString(), sb.toString());
        }

        private final java.text.Collator mCollator = java.text.Collator.getInstance();

        private android.content.pm.PackageManager mPM;
    }
}

