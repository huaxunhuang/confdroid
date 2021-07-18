/**
 * Copyright (C) 2006 The Android Open Source Project
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
package android.widget;


/**
 * {@link ViewSwitcher} that switches between two ImageViews when a new
 * image is set on it. The views added to an ImageSwitcher must all be
 * {@link ImageView ImageViews}.
 */
public class ImageSwitcher extends android.widget.ViewSwitcher {
    /**
     * Creates a new empty ImageSwitcher.
     *
     * @param context
     * 		the application's environment
     */
    public ImageSwitcher(android.content.Context context) {
        super(context);
    }

    /**
     * Creates a new empty ImageSwitcher for the given context and with the
     * specified set attributes.
     *
     * @param context
     * 		the application environment
     * @param attrs
     * 		a collection of attributes
     */
    public ImageSwitcher(android.content.Context context, android.util.AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets a new image on the ImageSwitcher with the given resource id.
     * This will set that image resource on the next ImageView in the switcher and will
     * then switch to that view.
     *
     * @param resid
     * 		a Drawable resource id
     * @see ImageView#setImageResource(int)
     */
    public void setImageResource(@android.annotation.DrawableRes
    int resid) {
        android.widget.ImageView image = ((android.widget.ImageView) (this.getNextView()));
        image.setImageResource(resid);
        showNext();
    }

    /**
     * Sets a new image on the ImageSwitcher with the given Uri.
     * This will set that image on the next ImageView in the switcher and will
     * then switch to that view.
     *
     * @param uri
     * 		the Uri of an image
     * @see ImageView#setImageURI(Uri)
     */
    public void setImageURI(android.net.Uri uri) {
        android.widget.ImageView image = ((android.widget.ImageView) (this.getNextView()));
        image.setImageURI(uri);
        showNext();
    }

    /**
     * Sets a new drawable on the ImageSwitcher.
     * This will set that drawable on the next ImageView in the switcher and will
     * then switch to that view.
     *
     * @param drawable
     * 		the drawable to be set or {@code null} to clear the content
     * @see ImageView#setImageDrawable(Drawable)
     */
    public void setImageDrawable(android.graphics.drawable.Drawable drawable) {
        android.widget.ImageView image = ((android.widget.ImageView) (this.getNextView()));
        image.setImageDrawable(drawable);
        showNext();
    }

    @java.lang.Override
    public java.lang.CharSequence getAccessibilityClassName() {
        return android.widget.ImageSwitcher.class.getName();
    }
}

