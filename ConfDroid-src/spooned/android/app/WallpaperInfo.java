/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.app;


/**
 * This class is used to specify meta information of a wallpaper service.
 */
public final class WallpaperInfo implements android.os.Parcelable {
    static final java.lang.String TAG = "WallpaperInfo";

    /**
     * The Service that implements this wallpaper component.
     */
    final android.content.pm.ResolveInfo mService;

    /**
     * The wallpaper setting activity's name, to
     * launch the setting activity of this wallpaper.
     */
    final java.lang.String mSettingsActivityName;

    /**
     * Resource identifier for this wallpaper's thumbnail image.
     */
    final int mThumbnailResource;

    /**
     * Resource identifier for a string indicating the author of the wallpaper.
     */
    final int mAuthorResource;

    /**
     * Resource identifier for a string containing a short description of the wallpaper.
     */
    final int mDescriptionResource;

    final int mContextUriResource;

    final int mContextDescriptionResource;

    final boolean mShowMetadataInPreview;

    /**
     * Constructor.
     *
     * @param context
     * 		The Context in which we are parsing the wallpaper.
     * @param service
     * 		The ResolveInfo returned from the package manager about
     * 		this wallpaper's component.
     */
    public WallpaperInfo(android.content.Context context, android.content.pm.ResolveInfo service) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mService = service;
        android.content.pm.ServiceInfo si = service.serviceInfo;
        android.content.pm.PackageManager pm = context.getPackageManager();
        java.lang.String settingsActivityComponent = null;
        int thumbnailRes = -1;
        int authorRes = -1;
        int descriptionRes = -1;
        int contextUriRes = -1;
        int contextDescriptionRes = -1;
        boolean showMetadataInPreview = false;
        android.content.res.XmlResourceParser parser = null;
        try {
            parser = si.loadXmlMetaData(pm, android.service.wallpaper.WallpaperService.SERVICE_META_DATA);
            if (parser == null) {
                throw new org.xmlpull.v1.XmlPullParserException(("No " + android.service.wallpaper.WallpaperService.SERVICE_META_DATA) + " meta-data");
            }
            android.content.res.Resources res = pm.getResourcesForApplication(si.applicationInfo);
            android.util.AttributeSet attrs = android.util.Xml.asAttributeSet(parser);
            int type;
            while (((type = parser.next()) != org.xmlpull.v1.XmlPullParser.END_DOCUMENT) && (type != org.xmlpull.v1.XmlPullParser.START_TAG)) {
            } 
            java.lang.String nodeName = parser.getName();
            if (!"wallpaper".equals(nodeName)) {
                throw new org.xmlpull.v1.XmlPullParserException("Meta-data does not start with wallpaper tag");
            }
            android.content.res.TypedArray sa = res.obtainAttributes(attrs, com.android.internal.R.styleable.Wallpaper);
            settingsActivityComponent = sa.getString(com.android.internal.R.styleable.Wallpaper_settingsActivity);
            thumbnailRes = sa.getResourceId(com.android.internal.R.styleable.Wallpaper_thumbnail, -1);
            authorRes = sa.getResourceId(com.android.internal.R.styleable.Wallpaper_author, -1);
            descriptionRes = sa.getResourceId(com.android.internal.R.styleable.Wallpaper_description, -1);
            contextUriRes = sa.getResourceId(com.android.internal.R.styleable.Wallpaper_contextUri, -1);
            contextDescriptionRes = sa.getResourceId(com.android.internal.R.styleable.Wallpaper_contextDescription, -1);
            showMetadataInPreview = sa.getBoolean(com.android.internal.R.styleable.Wallpaper_showMetadataInPreview, false);
            sa.recycle();
        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
            throw new org.xmlpull.v1.XmlPullParserException("Unable to create context for: " + si.packageName);
        } finally {
            if (parser != null)
                parser.close();

        }
        mSettingsActivityName = settingsActivityComponent;
        mThumbnailResource = thumbnailRes;
        mAuthorResource = authorRes;
        mDescriptionResource = descriptionRes;
        mContextUriResource = contextUriRes;
        mContextDescriptionResource = contextDescriptionRes;
        mShowMetadataInPreview = showMetadataInPreview;
    }

    WallpaperInfo(android.os.Parcel source) {
        mSettingsActivityName = source.readString();
        mThumbnailResource = source.readInt();
        mAuthorResource = source.readInt();
        mDescriptionResource = source.readInt();
        mContextUriResource = source.readInt();
        mContextDescriptionResource = source.readInt();
        mShowMetadataInPreview = source.readInt() != 0;
        mService = android.content.pm.ResolveInfo.CREATOR.createFromParcel(source);
    }

    /**
     * Return the .apk package that implements this wallpaper.
     */
    public java.lang.String getPackageName() {
        return mService.serviceInfo.packageName;
    }

    /**
     * Return the class name of the service component that implements
     * this wallpaper.
     */
    public java.lang.String getServiceName() {
        return mService.serviceInfo.name;
    }

    /**
     * Return the raw information about the Service implementing this
     * wallpaper.  Do not modify the returned object.
     */
    public android.content.pm.ServiceInfo getServiceInfo() {
        return mService.serviceInfo;
    }

    /**
     * Return the component of the service that implements this wallpaper.
     */
    public android.content.ComponentName getComponent() {
        return new android.content.ComponentName(mService.serviceInfo.packageName, mService.serviceInfo.name);
    }

    /**
     * Load the user-displayed label for this wallpaper.
     *
     * @param pm
     * 		Supply a PackageManager used to load the wallpaper's
     * 		resources.
     */
    public java.lang.CharSequence loadLabel(android.content.pm.PackageManager pm) {
        return mService.loadLabel(pm);
    }

    /**
     * Load the user-displayed icon for this wallpaper.
     *
     * @param pm
     * 		Supply a PackageManager used to load the wallpaper's
     * 		resources.
     */
    public android.graphics.drawable.Drawable loadIcon(android.content.pm.PackageManager pm) {
        return mService.loadIcon(pm);
    }

    /**
     * Load the thumbnail image for this wallpaper.
     *
     * @param pm
     * 		Supply a PackageManager used to load the wallpaper's
     * 		resources.
     */
    public android.graphics.drawable.Drawable loadThumbnail(android.content.pm.PackageManager pm) {
        if (mThumbnailResource < 0)
            return null;

        return pm.getDrawable(mService.serviceInfo.packageName, mThumbnailResource, mService.serviceInfo.applicationInfo);
    }

    /**
     * Return a string indicating the author(s) of this wallpaper.
     */
    public java.lang.CharSequence loadAuthor(android.content.pm.PackageManager pm) throws android.content.res.Resources.NotFoundException {
        if (mAuthorResource <= 0)
            throw new android.content.res.Resources.NotFoundException();

        java.lang.String packageName = mService.resolvePackageName;
        android.content.pm.ApplicationInfo applicationInfo = null;
        if (packageName == null) {
            packageName = mService.serviceInfo.packageName;
            applicationInfo = mService.serviceInfo.applicationInfo;
        }
        return pm.getText(packageName, mAuthorResource, applicationInfo);
    }

    /**
     * Return a brief summary of this wallpaper's behavior.
     */
    public java.lang.CharSequence loadDescription(android.content.pm.PackageManager pm) throws android.content.res.Resources.NotFoundException {
        java.lang.String packageName = mService.resolvePackageName;
        android.content.pm.ApplicationInfo applicationInfo = null;
        if (packageName == null) {
            packageName = mService.serviceInfo.packageName;
            applicationInfo = mService.serviceInfo.applicationInfo;
        }
        if (mService.serviceInfo.descriptionRes != 0) {
            return pm.getText(packageName, mService.serviceInfo.descriptionRes, applicationInfo);
        }
        if (mDescriptionResource <= 0)
            throw new android.content.res.Resources.NotFoundException();

        return pm.getText(packageName, mDescriptionResource, mService.serviceInfo.applicationInfo);
    }

    /**
     * Returns an URI that specifies a link for further context about this wallpaper.
     *
     * @param pm
     * 		An instance of {@link PackageManager} to retrieve the URI.
     * @return The URI.
     */
    public android.net.Uri loadContextUri(android.content.pm.PackageManager pm) throws android.content.res.Resources.NotFoundException {
        if (mContextUriResource <= 0)
            throw new android.content.res.Resources.NotFoundException();

        java.lang.String packageName = mService.resolvePackageName;
        android.content.pm.ApplicationInfo applicationInfo = null;
        if (packageName == null) {
            packageName = mService.serviceInfo.packageName;
            applicationInfo = mService.serviceInfo.applicationInfo;
        }
        java.lang.String contextUriString = pm.getText(packageName, mContextUriResource, applicationInfo).toString();
        if (contextUriString == null) {
            return null;
        }
        return android.net.Uri.parse(contextUriString);
    }

    /**
     * Retrieves a title of the URI that specifies a link for further context about this wallpaper.
     *
     * @param pm
     * 		An instance of {@link PackageManager} to retrieve the title.
     * @return The title.
     */
    public java.lang.CharSequence loadContextDescription(android.content.pm.PackageManager pm) throws android.content.res.Resources.NotFoundException {
        if (mContextDescriptionResource <= 0)
            throw new android.content.res.Resources.NotFoundException();

        java.lang.String packageName = mService.resolvePackageName;
        android.content.pm.ApplicationInfo applicationInfo = null;
        if (packageName == null) {
            packageName = mService.serviceInfo.packageName;
            applicationInfo = mService.serviceInfo.applicationInfo;
        }
        return pm.getText(packageName, mContextDescriptionResource, applicationInfo).toString();
    }

    /**
     * Queries whether any metadata should be shown when previewing the wallpaper. If this value is
     * set to true, any component that shows a preview of this live wallpaper should also show
     * accompanying information like {@link #loadLabel},
     * {@link #loadDescription}, {@link #loadAuthor} and
     * {@link #loadContextDescription(PackageManager)}, so the user gets to know further information
     * about this wallpaper.
     *
     * @return Whether any metadata should be shown when previewing the wallpaper.
     */
    public boolean getShowMetadataInPreview() {
        return mShowMetadataInPreview;
    }

    /**
     * Return the class name of an activity that provides a settings UI for
     * the wallpaper.  You can launch this activity be starting it with
     * an {@link android.content.Intent} whose action is MAIN and with an
     * explicit {@link android.content.ComponentName}
     * composed of {@link #getPackageName} and the class name returned here.
     *
     * <p>A null will be returned if there is no settings activity associated
     * with the wallpaper.
     */
    public java.lang.String getSettingsActivity() {
        return mSettingsActivityName;
    }

    public void dump(android.util.Printer pw, java.lang.String prefix) {
        pw.println(prefix + "Service:");
        mService.dump(pw, prefix + "  ");
        pw.println((prefix + "mSettingsActivityName=") + mSettingsActivityName);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("WallpaperInfo{" + mService.serviceInfo.name) + ", settings: ") + mSettingsActivityName) + "}";
    }

    /**
     * Used to package this object into a {@link Parcel}.
     *
     * @param dest
     * 		The {@link Parcel} to be written.
     * @param flags
     * 		The flags used for parceling.
     */
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(mSettingsActivityName);
        dest.writeInt(mThumbnailResource);
        dest.writeInt(mAuthorResource);
        dest.writeInt(mDescriptionResource);
        dest.writeInt(mContextUriResource);
        dest.writeInt(mContextDescriptionResource);
        dest.writeInt(mShowMetadataInPreview ? 1 : 0);
        mService.writeToParcel(dest, flags);
    }

    /**
     * Used to make this class parcelable.
     */
    public static final android.os.Parcelable.Creator<android.app.WallpaperInfo> CREATOR = new android.os.Parcelable.Creator<android.app.WallpaperInfo>() {
        public android.app.WallpaperInfo createFromParcel(android.os.Parcel source) {
            return new android.app.WallpaperInfo(source);
        }

        public android.app.WallpaperInfo[] newArray(int size) {
            return new android.app.WallpaperInfo[size];
        }
    };

    public int describeContents() {
        return 0;
    }
}

