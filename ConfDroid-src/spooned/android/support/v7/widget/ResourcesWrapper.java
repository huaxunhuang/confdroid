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
package android.support.v7.widget;


/**
 * This extends Resources but delegates the calls to another Resources object. This enables
 * any customization done by some subclass of Resources to be also picked up.
 */
class ResourcesWrapper extends android.content.res.Resources {
    private final android.content.res.Resources mResources;

    public ResourcesWrapper(android.content.res.Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        mResources = resources;
    }

    @java.lang.Override
    public java.lang.CharSequence getText(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getText(id);
    }

    @java.lang.Override
    public java.lang.CharSequence getQuantityText(int id, int quantity) throws android.content.res.Resources.NotFoundException {
        return mResources.getQuantityText(id, quantity);
    }

    @java.lang.Override
    public java.lang.String getString(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getString(id);
    }

    @java.lang.Override
    public java.lang.String getString(int id, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        return mResources.getString(id, formatArgs);
    }

    @java.lang.Override
    public java.lang.String getQuantityString(int id, int quantity, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        return mResources.getQuantityString(id, quantity, formatArgs);
    }

    @java.lang.Override
    public java.lang.String getQuantityString(int id, int quantity) throws android.content.res.Resources.NotFoundException {
        return mResources.getQuantityString(id, quantity);
    }

    @java.lang.Override
    public java.lang.CharSequence getText(int id, java.lang.CharSequence def) {
        return mResources.getText(id, def);
    }

    @java.lang.Override
    public java.lang.CharSequence[] getTextArray(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getTextArray(id);
    }

    @java.lang.Override
    public java.lang.String[] getStringArray(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getStringArray(id);
    }

    @java.lang.Override
    public int[] getIntArray(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getIntArray(id);
    }

    @java.lang.Override
    public android.content.res.TypedArray obtainTypedArray(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.obtainTypedArray(id);
    }

    @java.lang.Override
    public float getDimension(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getDimension(id);
    }

    @java.lang.Override
    public int getDimensionPixelOffset(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getDimensionPixelOffset(id);
    }

    @java.lang.Override
    public int getDimensionPixelSize(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getDimensionPixelSize(id);
    }

    @java.lang.Override
    public float getFraction(int id, int base, int pbase) {
        return mResources.getFraction(id, base, pbase);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getDrawable(id);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(int id, android.content.res.Resources.Theme theme) throws android.content.res.Resources.NotFoundException {
        return mResources.getDrawable(id, theme);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawableForDensity(int id, int density) throws android.content.res.Resources.NotFoundException {
        return mResources.getDrawableForDensity(id, density);
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawableForDensity(int id, int density, android.content.res.Resources.Theme theme) {
        return mResources.getDrawableForDensity(id, density, theme);
    }

    @java.lang.Override
    public android.graphics.Movie getMovie(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getMovie(id);
    }

    @java.lang.Override
    public int getColor(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getColor(id);
    }

    @java.lang.Override
    public android.content.res.ColorStateList getColorStateList(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getColorStateList(id);
    }

    @java.lang.Override
    public boolean getBoolean(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getBoolean(id);
    }

    @java.lang.Override
    public int getInteger(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getInteger(id);
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getLayout(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getLayout(id);
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getAnimation(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getAnimation(id);
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getXml(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.getXml(id);
    }

    @java.lang.Override
    public java.io.InputStream openRawResource(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.openRawResource(id);
    }

    @java.lang.Override
    public java.io.InputStream openRawResource(int id, android.util.TypedValue value) throws android.content.res.Resources.NotFoundException {
        return mResources.openRawResource(id, value);
    }

    @java.lang.Override
    public android.content.res.AssetFileDescriptor openRawResourceFd(int id) throws android.content.res.Resources.NotFoundException {
        return mResources.openRawResourceFd(id);
    }

    @java.lang.Override
    public void getValue(int id, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        mResources.getValue(id, outValue, resolveRefs);
    }

    @java.lang.Override
    public void getValueForDensity(int id, int density, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        mResources.getValueForDensity(id, density, outValue, resolveRefs);
    }

    @java.lang.Override
    public void getValue(java.lang.String name, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        mResources.getValue(name, outValue, resolveRefs);
    }

    @java.lang.Override
    public android.content.res.TypedArray obtainAttributes(android.util.AttributeSet set, int[] attrs) {
        return mResources.obtainAttributes(set, attrs);
    }

    @java.lang.Override
    public void updateConfiguration(android.content.res.Configuration config, android.util.DisplayMetrics metrics) {
        super.updateConfiguration(config, metrics);
        if (mResources != null) {
            // called from super's constructor. So, need to check.
            mResources.updateConfiguration(config, metrics);
        }
    }

    @java.lang.Override
    public android.util.DisplayMetrics getDisplayMetrics() {
        return mResources.getDisplayMetrics();
    }

    @java.lang.Override
    public android.content.res.Configuration getConfiguration() {
        return mResources.getConfiguration();
    }

    @java.lang.Override
    public int getIdentifier(java.lang.String name, java.lang.String defType, java.lang.String defPackage) {
        return mResources.getIdentifier(name, defType, defPackage);
    }

    @java.lang.Override
    public java.lang.String getResourceName(int resid) throws android.content.res.Resources.NotFoundException {
        return mResources.getResourceName(resid);
    }

    @java.lang.Override
    public java.lang.String getResourcePackageName(int resid) throws android.content.res.Resources.NotFoundException {
        return mResources.getResourcePackageName(resid);
    }

    @java.lang.Override
    public java.lang.String getResourceTypeName(int resid) throws android.content.res.Resources.NotFoundException {
        return mResources.getResourceTypeName(resid);
    }

    @java.lang.Override
    public java.lang.String getResourceEntryName(int resid) throws android.content.res.Resources.NotFoundException {
        return mResources.getResourceEntryName(resid);
    }

    @java.lang.Override
    public void parseBundleExtras(android.content.res.XmlResourceParser parser, android.os.Bundle outBundle) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        mResources.parseBundleExtras(parser, outBundle);
    }

    @java.lang.Override
    public void parseBundleExtra(java.lang.String tagName, android.util.AttributeSet attrs, android.os.Bundle outBundle) throws org.xmlpull.v1.XmlPullParserException {
        mResources.parseBundleExtra(tagName, attrs, outBundle);
    }
}

