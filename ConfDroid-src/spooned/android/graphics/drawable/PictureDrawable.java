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
package android.graphics.drawable;


/**
 * Drawable subclass that wraps a Picture, allowing the picture to be used
 * wherever a Drawable is supported.
 */
public class PictureDrawable extends android.graphics.drawable.Drawable {
    private android.graphics.Picture mPicture;

    /**
     * Construct a new drawable referencing the specified picture. The picture
     * may be null.
     *
     * @param picture
     * 		The picture to associate with the drawable. May be null.
     */
    public PictureDrawable(android.graphics.Picture picture) {
        mPicture = picture;
    }

    /**
     * Return the picture associated with the drawable. May be null.
     *
     * @return the picture associated with the drawable, or null.
     */
    public android.graphics.Picture getPicture() {
        return mPicture;
    }

    /**
     * Associate a picture with this drawable. The picture may be null.
     *
     * @param picture
     * 		The picture to associate with the drawable. May be null.
     */
    public void setPicture(android.graphics.Picture picture) {
        mPicture = picture;
    }

    @java.lang.Override
    public void draw(android.graphics.Canvas canvas) {
        if (mPicture != null) {
            android.graphics.Rect bounds = getBounds();
            canvas.save();
            canvas.clipRect(bounds);
            canvas.translate(bounds.left, bounds.top);
            canvas.drawPicture(mPicture);
            canvas.restore();
        }
    }

    @java.lang.Override
    public int getIntrinsicWidth() {
        return mPicture != null ? mPicture.getWidth() : -1;
    }

    @java.lang.Override
    public int getIntrinsicHeight() {
        return mPicture != null ? mPicture.getHeight() : -1;
    }

    @java.lang.Override
    public int getOpacity() {
        // not sure, so be safe
        return android.graphics.PixelFormat.TRANSLUCENT;
    }

    @java.lang.Override
    public void setColorFilter(android.graphics.ColorFilter colorFilter) {
    }

    @java.lang.Override
    public void setAlpha(int alpha) {
    }
}

