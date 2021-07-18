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
package android.text.style;


public class ImageSpan extends android.text.style.DynamicDrawableSpan {
    private android.graphics.drawable.Drawable mDrawable;

    private android.net.Uri mContentUri;

    private int mResourceId;

    private android.content.Context mContext;

    private java.lang.String mSource;

    /**
     *
     *
     * @deprecated Use {@link #ImageSpan(Context, Bitmap)} instead.
     */
    @java.lang.Deprecated
    public ImageSpan(android.graphics.Bitmap b) {
        this(null, b, android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    /**
     *
     *
     * @deprecated Use {@link #ImageSpan(Context, Bitmap, int) instead.
     */
    @java.lang.Deprecated
    public ImageSpan(android.graphics.Bitmap b, int verticalAlignment) {
        this(null, b, verticalAlignment);
    }

    public ImageSpan(android.content.Context context, android.graphics.Bitmap b) {
        this(context, b, android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    /**
     *
     *
     * @param verticalAlignment
     * 		one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     * 		{@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public ImageSpan(android.content.Context context, android.graphics.Bitmap b, int verticalAlignment) {
        super(verticalAlignment);
        mContext = context;
        mDrawable = (context != null) ? new android.graphics.drawable.BitmapDrawable(context.getResources(), b) : new android.graphics.drawable.BitmapDrawable(b);
        int width = mDrawable.getIntrinsicWidth();
        int height = mDrawable.getIntrinsicHeight();
        mDrawable.setBounds(0, 0, width > 0 ? width : 0, height > 0 ? height : 0);
    }

    public ImageSpan(android.graphics.drawable.Drawable d) {
        this(d, android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    /**
     *
     *
     * @param verticalAlignment
     * 		one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     * 		{@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public ImageSpan(android.graphics.drawable.Drawable d, int verticalAlignment) {
        super(verticalAlignment);
        mDrawable = d;
    }

    public ImageSpan(android.graphics.drawable.Drawable d, java.lang.String source) {
        this(d, source, android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    /**
     *
     *
     * @param verticalAlignment
     * 		one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     * 		{@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public ImageSpan(android.graphics.drawable.Drawable d, java.lang.String source, int verticalAlignment) {
        super(verticalAlignment);
        mDrawable = d;
        mSource = source;
    }

    public ImageSpan(android.content.Context context, android.net.Uri uri) {
        this(context, uri, android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    /**
     *
     *
     * @param verticalAlignment
     * 		one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     * 		{@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public ImageSpan(android.content.Context context, android.net.Uri uri, int verticalAlignment) {
        super(verticalAlignment);
        mContext = context;
        mContentUri = uri;
        mSource = uri.toString();
    }

    public ImageSpan(android.content.Context context, @android.annotation.DrawableRes
    int resourceId) {
        this(context, resourceId, android.text.style.DynamicDrawableSpan.ALIGN_BOTTOM);
    }

    /**
     *
     *
     * @param verticalAlignment
     * 		one of {@link DynamicDrawableSpan#ALIGN_BOTTOM} or
     * 		{@link DynamicDrawableSpan#ALIGN_BASELINE}.
     */
    public ImageSpan(android.content.Context context, @android.annotation.DrawableRes
    int resourceId, int verticalAlignment) {
        super(verticalAlignment);
        mContext = context;
        mResourceId = resourceId;
    }

    @java.lang.Override
    public android.graphics.drawable.Drawable getDrawable() {
        android.graphics.drawable.Drawable drawable = null;
        if (mDrawable != null) {
            drawable = mDrawable;
        } else
            if (mContentUri != null) {
                android.graphics.Bitmap bitmap = null;
                try {
                    java.io.InputStream is = mContext.getContentResolver().openInputStream(mContentUri);
                    bitmap = android.graphics.BitmapFactory.decodeStream(is);
                    drawable = new android.graphics.drawable.BitmapDrawable(mContext.getResources(), bitmap);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                    is.close();
                } catch (java.lang.Exception e) {
                    android.util.Log.e("sms", "Failed to loaded content " + mContentUri, e);
                }
            } else {
                try {
                    drawable = mContext.getDrawable(mResourceId);
                    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                } catch (java.lang.Exception e) {
                    android.util.Log.e("sms", "Unable to find resource: " + mResourceId);
                }
            }

        return drawable;
    }

    /**
     * Returns the source string that was saved during construction.
     */
    public java.lang.String getSource() {
        return mSource;
    }
}

