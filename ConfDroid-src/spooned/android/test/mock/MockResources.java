/**
 * Copyright (C) 2008 The Android Open Source Project
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
package android.test.mock;


/**
 * A mock {@link android.content.res.Resources} class. All methods are non-functional and throw
 * {@link java.lang.UnsupportedOperationException}. Override it to provide the operations that you
 * need.
 *
 * @deprecated Use a mocking framework like <a href="https://github.com/mockito/mockito">Mockito</a>.
New tests should be written using the
<a href="{@docRoot }tools/testing-support-library/index.html">Android Testing Support Library</a>.
 */
@java.lang.Deprecated
public class MockResources extends android.content.res.Resources {
    public MockResources() {
        super(new android.content.res.AssetManager(), null, null);
    }

    @java.lang.Override
    public void updateConfiguration(android.content.res.Configuration config, android.util.DisplayMetrics metrics) {
        // this method is called from the constructor, so we just do nothing
    }

    @java.lang.Override
    public java.lang.CharSequence getText(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.CharSequence getQuantityText(int id, int quantity) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getString(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getString(int id, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getQuantityString(int id, int quantity, java.lang.Object... formatArgs) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getQuantityString(int id, int quantity) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.CharSequence getText(int id, java.lang.CharSequence def) {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.CharSequence[] getTextArray(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String[] getStringArray(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public int[] getIntArray(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.TypedArray obtainTypedArray(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public float getDimension(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public int getDimensionPixelOffset(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public int getDimensionPixelSize(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.graphics.Movie getMovie(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public int getColor(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.ColorStateList getColorStateList(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public int getInteger(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getLayout(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getAnimation(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.XmlResourceParser getXml(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.io.InputStream openRawResource(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.AssetFileDescriptor openRawResourceFd(int id) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public void getValue(int id, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public void getValue(java.lang.String name, android.util.TypedValue outValue, boolean resolveRefs) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.TypedArray obtainAttributes(android.util.AttributeSet set, int[] attrs) {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.util.DisplayMetrics getDisplayMetrics() {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public android.content.res.Configuration getConfiguration() {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public int getIdentifier(java.lang.String name, java.lang.String defType, java.lang.String defPackage) {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getResourceName(int resid) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getResourcePackageName(int resid) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getResourceTypeName(int resid) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }

    @java.lang.Override
    public java.lang.String getResourceEntryName(int resid) throws android.content.res.Resources.NotFoundException {
        throw new java.lang.UnsupportedOperationException("mock object, not implemented");
    }
}

