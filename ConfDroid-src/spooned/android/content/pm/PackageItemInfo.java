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
 * Base class containing information common to all package items held by
 * the package manager.  This provides a very common basic set of attributes:
 * a label, icon, and meta-data.  This class is not intended
 * to be used by itself; it is simply here to share common definitions
 * between all items returned by the package manager.  As such, it does not
 * itself implement Parcelable, but does provide convenience methods to assist
 * in the implementation of Parcelable in subclasses.
 */
public class PackageItemInfo {
    /**
     * The maximum length of a safe label, in characters
     */
    private static final int MAX_SAFE_LABEL_LENGTH = 50000;

    /**
     *
     *
     * @unknown 
     */
    public static final float DEFAULT_MAX_LABEL_SIZE_PX = 500.0F;

    /**
     * Remove {@link Character#isWhitespace(int) whitespace} and non-breaking spaces from the edges
     * of the label.
     *
     * @see #loadSafeLabel(PackageManager, float, int)
     * @deprecated Use {@link TextUtils#SAFE_STRING_FLAG_TRIM} instead
     * @unknown 
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.SystemApi
    public static final int SAFE_LABEL_FLAG_TRIM = android.text.TextUtils.SAFE_STRING_FLAG_TRIM;

    /**
     * Force entire string into single line of text (no newlines). Cannot be set at the same time as
     * {@link #SAFE_LABEL_FLAG_FIRST_LINE}.
     *
     * @see #loadSafeLabel(PackageManager, float, int)
     * @deprecated Use {@link TextUtils#SAFE_STRING_FLAG_SINGLE_LINE} instead
     * @unknown 
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.SystemApi
    public static final int SAFE_LABEL_FLAG_SINGLE_LINE = android.text.TextUtils.SAFE_STRING_FLAG_SINGLE_LINE;

    /**
     * Return only first line of text (truncate at first newline). Cannot be set at the same time as
     * {@link #SAFE_LABEL_FLAG_SINGLE_LINE}.
     *
     * @see #loadSafeLabel(PackageManager, float, int)
     * @deprecated Use {@link TextUtils#SAFE_STRING_FLAG_FIRST_LINE} instead
     * @unknown 
     * @unknown 
     */
    @java.lang.Deprecated
    @android.annotation.SystemApi
    public static final int SAFE_LABEL_FLAG_FIRST_LINE = android.text.TextUtils.SAFE_STRING_FLAG_FIRST_LINE;

    private static volatile boolean sForceSafeLabels = false;

    /**
     * Always use {@link #loadSafeLabel safe labels} when calling {@link #loadLabel}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    public static void forceSafeLabels() {
        android.content.pm.PackageItemInfo.sForceSafeLabels = true;
    }

    /**
     * Public name of this item. From the "android:name" attribute.
     */
    public java.lang.String name;

    /**
     * Name of the package that this item is in.
     */
    public java.lang.String packageName;

    /**
     * A string resource identifier (in the package's resources) of this
     * component's label.  From the "label" attribute or, if not set, 0.
     */
    public int labelRes;

    /**
     * The string provided in the AndroidManifest file, if any.  You
     * probably don't want to use this.  You probably want
     * {@link PackageManager#getApplicationLabel}
     */
    public java.lang.CharSequence nonLocalizedLabel;

    /**
     * A drawable resource identifier (in the package's resources) of this
     * component's icon.  From the "icon" attribute or, if not set, 0.
     */
    public int icon;

    /**
     * A drawable resource identifier (in the package's resources) of this
     * component's banner.  From the "banner" attribute or, if not set, 0.
     */
    public int banner;

    /**
     * A drawable resource identifier (in the package's resources) of this
     * component's logo. Logos may be larger/wider than icons and are
     * displayed by certain UI elements in place of a name or name/icon
     * combination. From the "logo" attribute or, if not set, 0.
     */
    public int logo;

    /**
     * Additional meta-data associated with this component.  This field
     * will only be filled in if you set the
     * {@link PackageManager#GET_META_DATA} flag when requesting the info.
     */
    public android.os.Bundle metaData;

    /**
     * If different of UserHandle.USER_NULL, The icon of this item will represent that user.
     *
     * @unknown 
     */
    public int showUserIcon;

    public PackageItemInfo() {
        showUserIcon = android.os.UserHandle.USER_NULL;
    }

    public PackageItemInfo(android.content.pm.PackageItemInfo orig) {
        name = orig.name;
        if (name != null)
            name = name.trim();

        packageName = orig.packageName;
        labelRes = orig.labelRes;
        nonLocalizedLabel = orig.nonLocalizedLabel;
        if (nonLocalizedLabel != null)
            nonLocalizedLabel = nonLocalizedLabel.toString().trim();

        icon = orig.icon;
        banner = orig.banner;
        logo = orig.logo;
        metaData = orig.metaData;
        showUserIcon = orig.showUserIcon;
    }

    /**
     * Retrieve the current textual label associated with this item.  This
     * will call back on the given PackageManager to load the label from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the label can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a CharSequence containing the item's label.  If the
    item does not have a label, its name is returned.
     */
    @android.annotation.NonNull
    public java.lang.CharSequence loadLabel(@android.annotation.NonNull
    android.content.pm.PackageManager pm) {
        if (android.content.pm.PackageItemInfo.sForceSafeLabels) {
            return loadSafeLabel(pm, android.content.pm.PackageItemInfo.DEFAULT_MAX_LABEL_SIZE_PX, android.text.TextUtils.SAFE_STRING_FLAG_TRIM | android.text.TextUtils.SAFE_STRING_FLAG_FIRST_LINE);
        } else {
            return loadUnsafeLabel(pm);
        }
    }

    /**
     * {@hide }
     */
    public java.lang.CharSequence loadUnsafeLabel(android.content.pm.PackageManager pm) {
        if (nonLocalizedLabel != null) {
            return nonLocalizedLabel;
        }
        if (labelRes != 0) {
            java.lang.CharSequence label = pm.getText(packageName, labelRes, getApplicationInfo());
            if (label != null) {
                return label.toString().trim();
            }
        }
        if (name != null) {
            return name;
        }
        return packageName;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated use loadSafeLabel(PackageManager, float, int) instead
     */
    @android.annotation.SystemApi
    @java.lang.Deprecated
    @android.annotation.NonNull
    public java.lang.CharSequence loadSafeLabel(@android.annotation.NonNull
    android.content.pm.PackageManager pm) {
        return loadSafeLabel(pm, android.content.pm.PackageItemInfo.DEFAULT_MAX_LABEL_SIZE_PX, android.text.TextUtils.SAFE_STRING_FLAG_TRIM | android.text.TextUtils.SAFE_STRING_FLAG_FIRST_LINE);
    }

    /**
     * Calls {@link TextUtils#makeSafeForPresentation} for the label of this item.
     *
     * <p>For parameters see {@link TextUtils#makeSafeForPresentation}.
     *
     * @unknown 
     */
    @android.annotation.SystemApi
    @android.annotation.NonNull
    public java.lang.CharSequence loadSafeLabel(@android.annotation.NonNull
    android.content.pm.PackageManager pm, @android.annotation.FloatRange(from = 0)
    float ellipsizeDip, @android.text.TextUtils.SafeStringFlags
    int flags) {
        com.android.internal.util.Preconditions.checkNotNull(pm);
        return android.text.TextUtils.makeSafeForPresentation(loadUnsafeLabel(pm).toString(), android.content.pm.PackageItemInfo.MAX_SAFE_LABEL_LENGTH, ellipsizeDip, flags);
    }

    /**
     * Retrieve the current graphical icon associated with this item.  This
     * will call back on the given PackageManager to load the icon from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the icon can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the item's icon.  If the
    item does not have an icon, the item's default icon is returned
    such as the default activity icon.
     */
    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        return pm.loadItemIcon(this, getApplicationInfo());
    }

    /**
     * Retrieve the current graphical icon associated with this item without
     * the addition of a work badge if applicable.
     * This will call back on the given PackageManager to load the icon from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the icon can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the item's icon.  If the
    item does not have an icon, the item's default icon is returned
    such as the default activity icon.
     */
    public android.graphics.drawable.Drawable loadUnbadgedIcon(android.content.pm.PackageManager pm) {
        return pm.loadUnbadgedItemIcon(this, getApplicationInfo());
    }

    /**
     * Retrieve the current graphical banner associated with this item.  This
     * will call back on the given PackageManager to load the banner from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the banner can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the item's banner.  If the item
    does not have a banner, this method will return null.
     */
    public android.graphics.drawable.Drawable loadBanner(android.content.pm.PackageManager pm) {
        if (banner != 0) {
            android.graphics.drawable.Drawable dr = pm.getDrawable(packageName, banner, getApplicationInfo());
            if (dr != null) {
                return dr;
            }
        }
        return loadDefaultBanner(pm);
    }

    /**
     * Retrieve the default graphical icon associated with this item.
     *
     * @param pm
     * 		A PackageManager from which the icon can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the item's default icon
    such as the default activity icon.
     * @unknown 
     */
    public android.graphics.drawable.Drawable loadDefaultIcon(android.content.pm.PackageManager pm) {
        return pm.getDefaultActivityIcon();
    }

    /**
     * Retrieve the default graphical banner associated with this item.
     *
     * @param pm
     * 		A PackageManager from which the banner can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the item's default banner
    or null if no default logo is available.
     * @unknown 
     */
    protected android.graphics.drawable.Drawable loadDefaultBanner(android.content.pm.PackageManager pm) {
        return null;
    }

    /**
     * Retrieve the current graphical logo associated with this item. This
     * will call back on the given PackageManager to load the logo from
     * the application.
     *
     * @param pm
     * 		A PackageManager from which the logo can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the item's logo. If the item
    does not have a logo, this method will return null.
     */
    public android.graphics.drawable.Drawable loadLogo(android.content.pm.PackageManager pm) {
        if (logo != 0) {
            android.graphics.drawable.Drawable d = pm.getDrawable(packageName, logo, getApplicationInfo());
            if (d != null) {
                return d;
            }
        }
        return loadDefaultLogo(pm);
    }

    /**
     * Retrieve the default graphical logo associated with this item.
     *
     * @param pm
     * 		A PackageManager from which the logo can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @return Returns a Drawable containing the item's default logo
    or null if no default logo is available.
     * @unknown 
     */
    protected android.graphics.drawable.Drawable loadDefaultLogo(android.content.pm.PackageManager pm) {
        return null;
    }

    /**
     * Load an XML resource attached to the meta-data of this item.  This will
     * retrieved the name meta-data entry, and if defined call back on the
     * given PackageManager to load its XML file from the application.
     *
     * @param pm
     * 		A PackageManager from which the XML can be loaded; usually
     * 		the PackageManager from which you originally retrieved this item.
     * @param name
     * 		Name of the meta-date you would like to load.
     * @return Returns an XmlPullParser you can use to parse the XML file
    assigned as the given meta-data.  If the meta-data name is not defined
    or the XML resource could not be found, null is returned.
     */
    public android.content.res.XmlResourceParser loadXmlMetaData(android.content.pm.PackageManager pm, java.lang.String name) {
        if (metaData != null) {
            int resid = metaData.getInt(name);
            if (resid != 0) {
                return pm.getXml(packageName, resid, getApplicationInfo());
            }
        }
        return null;
    }

    /**
     *
     *
     * @unknown Flag for dumping: include all details.
     */
    public static final int DUMP_FLAG_DETAILS = 1 << 0;

    /**
     *
     *
     * @unknown Flag for dumping: include nested ApplicationInfo.
     */
    public static final int DUMP_FLAG_APPLICATION = 1 << 1;

    /**
     *
     *
     * @unknown Flag for dumping: all flags to dump everything.
     */
    public static final int DUMP_FLAG_ALL = android.content.pm.PackageItemInfo.DUMP_FLAG_DETAILS | android.content.pm.PackageItemInfo.DUMP_FLAG_APPLICATION;

    protected void dumpFront(android.util.Printer pw, java.lang.String prefix) {
        if (name != null) {
            pw.println((prefix + "name=") + name);
        }
        pw.println((prefix + "packageName=") + packageName);
        if ((((labelRes != 0) || (nonLocalizedLabel != null)) || (icon != 0)) || (banner != 0)) {
            pw.println((((((((prefix + "labelRes=0x") + java.lang.Integer.toHexString(labelRes)) + " nonLocalizedLabel=") + nonLocalizedLabel) + " icon=0x") + java.lang.Integer.toHexString(icon)) + " banner=0x") + java.lang.Integer.toHexString(banner));
        }
    }

    protected void dumpBack(android.util.Printer pw, java.lang.String prefix) {
        // no back here
    }

    public void writeToParcel(android.os.Parcel dest, int parcelableFlags) {
        dest.writeString(name);
        dest.writeString(packageName);
        dest.writeInt(labelRes);
        android.text.TextUtils.writeToParcel(nonLocalizedLabel, dest, parcelableFlags);
        dest.writeInt(icon);
        dest.writeInt(logo);
        dest.writeBundle(metaData);
        dest.writeInt(banner);
        dest.writeInt(showUserIcon);
    }

    /**
     *
     *
     * @unknown 
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId, int dumpFlags) {
        long token = proto.start(fieldId);
        if (name != null) {
            proto.write(PackageItemInfoProto.NAME, name);
        }
        proto.write(PackageItemInfoProto.PACKAGE_NAME, packageName);
        proto.write(PackageItemInfoProto.LABEL_RES, labelRes);
        if (nonLocalizedLabel != null) {
            proto.write(PackageItemInfoProto.NON_LOCALIZED_LABEL, nonLocalizedLabel.toString());
        }
        proto.write(PackageItemInfoProto.ICON, icon);
        proto.write(PackageItemInfoProto.BANNER, banner);
        proto.end(token);
    }

    protected PackageItemInfo(android.os.Parcel source) {
        name = source.readString();
        packageName = source.readString();
        labelRes = source.readInt();
        nonLocalizedLabel = TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        icon = source.readInt();
        logo = source.readInt();
        metaData = source.readBundle();
        banner = source.readInt();
        showUserIcon = source.readInt();
    }

    /**
     * Get the ApplicationInfo for the application to which this item belongs,
     * if available, otherwise returns null.
     *
     * @return Returns the ApplicationInfo of this item, or null if not known.
     * @unknown 
     */
    protected android.content.pm.ApplicationInfo getApplicationInfo() {
        return null;
    }

    public static class DisplayNameComparator implements java.util.Comparator<android.content.pm.PackageItemInfo> {
        public DisplayNameComparator(android.content.pm.PackageManager pm) {
            mPM = pm;
        }

        public final int compare(android.content.pm.PackageItemInfo aa, android.content.pm.PackageItemInfo ab) {
            java.lang.CharSequence sa = aa.loadLabel(mPM);
            if (sa == null)
                sa = aa.name;

            java.lang.CharSequence sb = ab.loadLabel(mPM);
            if (sb == null)
                sb = ab.name;

            return sCollator.compare(sa.toString(), sb.toString());
        }

        private final java.text.Collator sCollator = java.text.Collator.getInstance();

        private android.content.pm.PackageManager mPM;
    }
}

