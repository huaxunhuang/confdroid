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
package android.graphics.drawable;


/**
 * An umbrella container for several serializable graphics representations, including Bitmaps,
 * compressed bitmap images (e.g. JPG or PNG), and drawable resources (including vectors).
 *
 * <a href="https://developer.android.com/training/displaying-bitmaps/index.html">Much ink</a>
 * has been spilled on the best way to load images, and many clients may have different needs when
 * it comes to threading and fetching. This class is therefore focused on encapsulation rather than
 * behavior.
 */
public final class Icon implements android.os.Parcelable {
    private static final java.lang.String TAG = "Icon";

    /**
     * An icon that was created using {@link Icon#createWithBitmap(Bitmap)}.
     *
     * @see #getType
     */
    public static final int TYPE_BITMAP = 1;

    /**
     * An icon that was created using {@link Icon#createWithResource}.
     *
     * @see #getType
     */
    public static final int TYPE_RESOURCE = 2;

    /**
     * An icon that was created using {@link Icon#createWithData(byte[], int, int)}.
     *
     * @see #getType
     */
    public static final int TYPE_DATA = 3;

    /**
     * An icon that was created using {@link Icon#createWithContentUri}
     * or {@link Icon#createWithFilePath(String)}.
     *
     * @see #getType
     */
    public static final int TYPE_URI = 4;

    /**
     * An icon that was created using {@link Icon#createWithAdaptiveBitmap}.
     *
     * @see #getType
     */
    public static final int TYPE_ADAPTIVE_BITMAP = 5;

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.graphics.drawable.Icon.TYPE_BITMAP, android.graphics.drawable.Icon.TYPE_RESOURCE, android.graphics.drawable.Icon.TYPE_DATA, android.graphics.drawable.Icon.TYPE_URI, android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP })
    public @interface IconType {}

    private static final int VERSION_STREAM_SERIALIZER = 1;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private final int mType;

    private android.content.res.ColorStateList mTintList;

    static final android.graphics.BlendMode DEFAULT_BLEND_MODE = android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE;// SRC_IN


    private android.graphics.BlendMode mBlendMode = android.graphics.drawable.Drawable.DEFAULT_BLEND_MODE;

    // To avoid adding unnecessary overhead, we have a few basic objects that get repurposed
    // based on the value of mType.
    // TYPE_BITMAP: Bitmap
    // TYPE_RESOURCE: Resources
    // TYPE_DATA: DataBytes
    private java.lang.Object mObj1;

    // TYPE_RESOURCE: package name
    // TYPE_URI: uri string
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private java.lang.String mString1;

    // TYPE_RESOURCE: resId
    // TYPE_DATA: data length
    private int mInt1;

    // TYPE_DATA: data offset
    private int mInt2;

    /**
     * Gets the type of the icon provided.
     * <p>
     * Note that new types may be added later, so callers should guard against other
     * types being returned.
     */
    @android.graphics.drawable.Icon.IconType
    public int getType() {
        return mType;
    }

    /**
     *
     *
     * @return The {@link android.graphics.Bitmap} held by this {@link #TYPE_BITMAP} Icon.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public android.graphics.Bitmap getBitmap() {
        if ((mType != android.graphics.drawable.Icon.TYPE_BITMAP) && (mType != android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP)) {
            throw new java.lang.IllegalStateException("called getBitmap() on " + this);
        }
        return ((android.graphics.Bitmap) (mObj1));
    }

    private void setBitmap(android.graphics.Bitmap b) {
        mObj1 = b;
    }

    /**
     *
     *
     * @return The length of the compressed bitmap byte array held by this {@link #TYPE_DATA} Icon.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public int getDataLength() {
        if (mType != android.graphics.drawable.Icon.TYPE_DATA) {
            throw new java.lang.IllegalStateException("called getDataLength() on " + this);
        }
        synchronized(this) {
            return mInt1;
        }
    }

    /**
     *
     *
     * @return The offset into the byte array held by this {@link #TYPE_DATA} Icon at which
    valid compressed bitmap data is found.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public int getDataOffset() {
        if (mType != android.graphics.drawable.Icon.TYPE_DATA) {
            throw new java.lang.IllegalStateException("called getDataOffset() on " + this);
        }
        synchronized(this) {
            return mInt2;
        }
    }

    /**
     *
     *
     * @return The byte array held by this {@link #TYPE_DATA} Icon ctonaining compressed
    bitmap data.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public byte[] getDataBytes() {
        if (mType != android.graphics.drawable.Icon.TYPE_DATA) {
            throw new java.lang.IllegalStateException("called getDataBytes() on " + this);
        }
        synchronized(this) {
            return ((byte[]) (mObj1));
        }
    }

    /**
     *
     *
     * @return The {@link android.content.res.Resources} for this {@link #TYPE_RESOURCE} Icon.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public android.content.res.Resources getResources() {
        if (mType != android.graphics.drawable.Icon.TYPE_RESOURCE) {
            throw new java.lang.IllegalStateException("called getResources() on " + this);
        }
        return ((android.content.res.Resources) (mObj1));
    }

    /**
     * Gets the package used to create this icon.
     * <p>
     * Only valid for icons of type {@link #TYPE_RESOURCE}.
     * Note: This package may not be available if referenced in the future, and it is
     * up to the caller to ensure safety if this package is re-used and/or persisted.
     */
    @android.annotation.NonNull
    public java.lang.String getResPackage() {
        if (mType != android.graphics.drawable.Icon.TYPE_RESOURCE) {
            throw new java.lang.IllegalStateException("called getResPackage() on " + this);
        }
        return mString1;
    }

    /**
     * Gets the resource used to create this icon.
     * <p>
     * Only valid for icons of type {@link #TYPE_RESOURCE}.
     * Note: This resource may not be available if the application changes at all, and it is
     * up to the caller to ensure safety if this resource is re-used and/or persisted.
     */
    @android.annotation.IdRes
    public int getResId() {
        if (mType != android.graphics.drawable.Icon.TYPE_RESOURCE) {
            throw new java.lang.IllegalStateException("called getResId() on " + this);
        }
        return mInt1;
    }

    /**
     *
     *
     * @return The URI (as a String) for this {@link #TYPE_URI} Icon.
     * @unknown 
     */
    public java.lang.String getUriString() {
        if (mType != android.graphics.drawable.Icon.TYPE_URI) {
            throw new java.lang.IllegalStateException("called getUriString() on " + this);
        }
        return mString1;
    }

    /**
     * Gets the uri used to create this icon.
     * <p>
     * Only valid for icons of type {@link #TYPE_URI}.
     * Note: This uri may not be available in the future, and it is
     * up to the caller to ensure safety if this uri is re-used and/or persisted.
     */
    @android.annotation.NonNull
    public android.net.Uri getUri() {
        return android.net.Uri.parse(getUriString());
    }

    private static final java.lang.String typeToString(int x) {
        switch (x) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
                return "BITMAP";
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                return "BITMAP_MASKABLE";
            case android.graphics.drawable.Icon.TYPE_DATA :
                return "DATA";
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
                return "RESOURCE";
            case android.graphics.drawable.Icon.TYPE_URI :
                return "URI";
            default :
                return "UNKNOWN";
        }
    }

    /**
     * Invokes {@link #loadDrawable(Context)} on the given {@link android.os.Handler Handler}
     * and then sends <code>andThen</code> to the same Handler when finished.
     *
     * @param context
     * 		{@link android.content.Context Context} in which to load the drawable; see
     * 		{@link #loadDrawable(Context)}
     * @param andThen
     * 		{@link android.os.Message} to send to its target once the drawable
     * 		is available. The {@link android.os.Message#obj obj}
     * 		property is populated with the Drawable.
     */
    public void loadDrawableAsync(android.content.Context context, android.os.Message andThen) {
        if (andThen.getTarget() == null) {
            throw new java.lang.IllegalArgumentException("callback message must have a target handler");
        }
        new android.graphics.drawable.Icon.LoadDrawableTask(context, andThen).runAsync();
    }

    /**
     * Invokes {@link #loadDrawable(Context)} on a background thread and notifies the <code>
     * {@link OnDrawableLoadedListener#onDrawableLoaded listener} </code> on the {@code handler}
     * when finished.
     *
     * @param context
     * 		{@link Context Context} in which to load the drawable; see
     * 		{@link #loadDrawable(Context)}
     * @param listener
     * 		to be {@link OnDrawableLoadedListener#onDrawableLoaded notified} when
     * 		{@link #loadDrawable(Context)} finished
     * @param handler
     * 		{@link Handler} on which to notify the {@code listener}
     */
    public void loadDrawableAsync(android.content.Context context, final android.graphics.drawable.Icon.OnDrawableLoadedListener listener, android.os.Handler handler) {
        new android.graphics.drawable.Icon.LoadDrawableTask(context, handler, listener).runAsync();
    }

    /**
     * Returns a Drawable that can be used to draw the image inside this Icon, constructing it
     * if necessary. Depending on the type of image, this may not be something you want to do on
     * the UI thread, so consider using
     * {@link #loadDrawableAsync(Context, Message) loadDrawableAsync} instead.
     *
     * @param context
     * 		{@link android.content.Context Context} in which to load the drawable; used
     * 		to access {@link android.content.res.Resources Resources}, for example.
     * @return A fresh instance of a drawable for this image, yours to keep.
     */
    public android.graphics.drawable.Drawable loadDrawable(android.content.Context context) {
        final android.graphics.drawable.Drawable result = loadDrawableInner(context);
        if ((result != null) && ((mTintList != null) || (mBlendMode != android.graphics.drawable.Icon.DEFAULT_BLEND_MODE))) {
            result.mutate();
            result.setTintList(mTintList);
            result.setTintBlendMode(mBlendMode);
        }
        return result;
    }

    /**
     * Do the heavy lifting of loading the drawable, but stop short of applying any tint.
     */
    private android.graphics.drawable.Drawable loadDrawableInner(android.content.Context context) {
        switch (mType) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
                return new android.graphics.drawable.BitmapDrawable(context.getResources(), getBitmap());
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                return new android.graphics.drawable.AdaptiveIconDrawable(null, new android.graphics.drawable.BitmapDrawable(context.getResources(), getBitmap()));
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
                if (getResources() == null) {
                    // figure out where to load resources from
                    java.lang.String resPackage = getResPackage();
                    if (android.text.TextUtils.isEmpty(resPackage)) {
                        // if none is specified, try the given context
                        resPackage = context.getPackageName();
                    }
                    if ("android".equals(resPackage)) {
                        mObj1 = android.content.res.Resources.getSystem();
                    } else {
                        final android.content.pm.PackageManager pm = context.getPackageManager();
                        try {
                            android.content.pm.ApplicationInfo ai = pm.getApplicationInfo(resPackage, android.content.pm.PackageManager.MATCH_UNINSTALLED_PACKAGES);
                            if (ai != null) {
                                mObj1 = pm.getResourcesForApplication(ai);
                            } else {
                                break;
                            }
                        } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                            android.util.Log.e(android.graphics.drawable.Icon.TAG, java.lang.String.format("Unable to find pkg=%s for icon %s", resPackage, this), e);
                            break;
                        }
                    }
                }
                try {
                    return getResources().getDrawable(getResId(), context.getTheme());
                } catch (java.lang.RuntimeException e) {
                    android.util.Log.e(android.graphics.drawable.Icon.TAG, java.lang.String.format("Unable to load resource 0x%08x from pkg=%s", getResId(), getResPackage()), e);
                }
                break;
            case android.graphics.drawable.Icon.TYPE_DATA :
                return new android.graphics.drawable.BitmapDrawable(context.getResources(), android.graphics.BitmapFactory.decodeByteArray(getDataBytes(), getDataOffset(), getDataLength()));
            case android.graphics.drawable.Icon.TYPE_URI :
                final android.net.Uri uri = getUri();
                final java.lang.String scheme = uri.getScheme();
                java.io.InputStream is = null;
                if (android.content.ContentResolver.SCHEME_CONTENT.equals(scheme) || android.content.ContentResolver.SCHEME_FILE.equals(scheme)) {
                    try {
                        is = context.getContentResolver().openInputStream(uri);
                    } catch (java.lang.Exception e) {
                        android.util.Log.w(android.graphics.drawable.Icon.TAG, "Unable to load image from URI: " + uri, e);
                    }
                } else {
                    try {
                        is = new java.io.FileInputStream(new java.io.File(mString1));
                    } catch (java.io.FileNotFoundException e) {
                        android.util.Log.w(android.graphics.drawable.Icon.TAG, "Unable to load image from path: " + uri, e);
                    }
                }
                if (is != null) {
                    return new android.graphics.drawable.BitmapDrawable(context.getResources(), android.graphics.BitmapFactory.decodeStream(is));
                }
                break;
        }
        return null;
    }

    /**
     * Load the requested resources under the given userId, if the system allows it,
     * before actually loading the drawable.
     *
     * @unknown 
     */
    public android.graphics.drawable.Drawable loadDrawableAsUser(android.content.Context context, int userId) {
        if (mType == android.graphics.drawable.Icon.TYPE_RESOURCE) {
            java.lang.String resPackage = getResPackage();
            if (android.text.TextUtils.isEmpty(resPackage)) {
                resPackage = context.getPackageName();
            }
            if ((getResources() == null) && (!getResPackage().equals("android"))) {
                final android.content.pm.PackageManager pm = context.getPackageManager();
                try {
                    // assign getResources() as the correct user
                    mObj1 = pm.getResourcesForApplicationAsUser(resPackage, userId);
                } catch (android.content.pm.PackageManager.NameNotFoundException e) {
                    android.util.Log.e(android.graphics.drawable.Icon.TAG, java.lang.String.format("Unable to find pkg=%s user=%d", getResPackage(), userId), e);
                }
            }
        }
        return loadDrawable(context);
    }

    /**
     *
     *
     * @unknown 
     */
    public static final int MIN_ASHMEM_ICON_SIZE = 128 * (1 << 10);

    /**
     * Puts the memory used by this instance into Ashmem memory, if possible.
     *
     * @unknown 
     */
    public void convertToAshmem() {
        if ((((mType == android.graphics.drawable.Icon.TYPE_BITMAP) || (mType == android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP)) && getBitmap().isMutable()) && (getBitmap().getAllocationByteCount() >= android.graphics.drawable.Icon.MIN_ASHMEM_ICON_SIZE)) {
            setBitmap(getBitmap().createAshmemBitmap());
        }
    }

    /**
     * Writes a serialized version of an Icon to the specified stream.
     *
     * @param stream
     * 		The stream on which to serialize the Icon.
     * @unknown 
     */
    public void writeToStream(java.io.OutputStream stream) throws java.io.IOException {
        java.io.DataOutputStream dataStream = new java.io.DataOutputStream(stream);
        dataStream.writeInt(android.graphics.drawable.Icon.VERSION_STREAM_SERIALIZER);
        dataStream.writeByte(mType);
        switch (mType) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                getBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, dataStream);
                break;
            case android.graphics.drawable.Icon.TYPE_DATA :
                dataStream.writeInt(getDataLength());
                dataStream.write(getDataBytes(), getDataOffset(), getDataLength());
                break;
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
                dataStream.writeUTF(getResPackage());
                dataStream.writeInt(getResId());
                break;
            case android.graphics.drawable.Icon.TYPE_URI :
                dataStream.writeUTF(getUriString());
                break;
        }
    }

    private Icon(int mType) {
        this.mType = mType;
    }

    /**
     * Create an Icon from the specified stream.
     *
     * @param stream
     * 		The input stream from which to reconstruct the Icon.
     * @unknown 
     */
    public static android.graphics.drawable.Icon createFromStream(java.io.InputStream stream) throws java.io.IOException {
        java.io.DataInputStream inputStream = new java.io.DataInputStream(stream);
        final int version = inputStream.readInt();
        if (version >= android.graphics.drawable.Icon.VERSION_STREAM_SERIALIZER) {
            final int type = inputStream.readByte();
            switch (type) {
                case android.graphics.drawable.Icon.TYPE_BITMAP :
                    return android.graphics.drawable.Icon.createWithBitmap(android.graphics.BitmapFactory.decodeStream(inputStream));
                case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                    return android.graphics.drawable.Icon.createWithAdaptiveBitmap(android.graphics.BitmapFactory.decodeStream(inputStream));
                case android.graphics.drawable.Icon.TYPE_DATA :
                    final int length = inputStream.readInt();
                    final byte[] data = new byte[length];
                    /* offset */
                    inputStream.read(data, 0, length);
                    return /* offset */
                    android.graphics.drawable.Icon.createWithData(data, 0, length);
                case android.graphics.drawable.Icon.TYPE_RESOURCE :
                    final java.lang.String packageName = inputStream.readUTF();
                    final int resId = inputStream.readInt();
                    return android.graphics.drawable.Icon.createWithResource(packageName, resId);
                case android.graphics.drawable.Icon.TYPE_URI :
                    final java.lang.String uriOrPath = inputStream.readUTF();
                    return android.graphics.drawable.Icon.createWithContentUri(uriOrPath);
            }
        }
        return null;
    }

    /**
     * Compares if this icon is constructed from the same resources as another icon.
     * Note that this is an inexpensive operation and doesn't do deep Bitmap equality comparisons.
     *
     * @param otherIcon
     * 		the other icon
     * @return whether this icon is the same as the another one
     * @unknown 
     */
    public boolean sameAs(android.graphics.drawable.Icon otherIcon) {
        if (otherIcon == this) {
            return true;
        }
        if (mType != otherIcon.getType()) {
            return false;
        }
        switch (mType) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                return getBitmap() == otherIcon.getBitmap();
            case android.graphics.drawable.Icon.TYPE_DATA :
                return ((getDataLength() == otherIcon.getDataLength()) && (getDataOffset() == otherIcon.getDataOffset())) && java.util.Arrays.equals(getDataBytes(), otherIcon.getDataBytes());
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
                return (getResId() == otherIcon.getResId()) && java.util.Objects.equals(getResPackage(), otherIcon.getResPackage());
            case android.graphics.drawable.Icon.TYPE_URI :
                return java.util.Objects.equals(getUriString(), otherIcon.getUriString());
        }
        return false;
    }

    /**
     * Create an Icon pointing to a drawable resource.
     *
     * @param context
     * 		The context for the application whose resources should be used to resolve the
     * 		given resource ID.
     * @param resId
     * 		ID of the drawable resource
     */
    public static android.graphics.drawable.Icon createWithResource(android.content.Context context, @android.annotation.DrawableRes
    int resId) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("Context must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_RESOURCE);
        rep.mInt1 = resId;
        rep.mString1 = context.getPackageName();
        return rep;
    }

    /**
     * Version of createWithResource that takes Resources. Do not use.
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static android.graphics.drawable.Icon createWithResource(android.content.res.Resources res, @android.annotation.DrawableRes
    int resId) {
        if (res == null) {
            throw new java.lang.IllegalArgumentException("Resource must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_RESOURCE);
        rep.mInt1 = resId;
        rep.mString1 = res.getResourcePackageName(resId);
        return rep;
    }

    /**
     * Create an Icon pointing to a drawable resource.
     *
     * @param resPackage
     * 		Name of the package containing the resource in question
     * @param resId
     * 		ID of the drawable resource
     */
    public static android.graphics.drawable.Icon createWithResource(java.lang.String resPackage, @android.annotation.DrawableRes
    int resId) {
        if (resPackage == null) {
            throw new java.lang.IllegalArgumentException("Resource package name must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_RESOURCE);
        rep.mInt1 = resId;
        rep.mString1 = resPackage;
        return rep;
    }

    /**
     * Create an Icon pointing to a bitmap in memory.
     *
     * @param bits
     * 		A valid {@link android.graphics.Bitmap} object
     */
    public static android.graphics.drawable.Icon createWithBitmap(android.graphics.Bitmap bits) {
        if (bits == null) {
            throw new java.lang.IllegalArgumentException("Bitmap must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_BITMAP);
        rep.setBitmap(bits);
        return rep;
    }

    /**
     * Create an Icon pointing to a bitmap in memory that follows the icon design guideline defined
     * by {@link AdaptiveIconDrawable}.
     *
     * @param bits
     * 		A valid {@link android.graphics.Bitmap} object
     */
    public static android.graphics.drawable.Icon createWithAdaptiveBitmap(android.graphics.Bitmap bits) {
        if (bits == null) {
            throw new java.lang.IllegalArgumentException("Bitmap must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP);
        rep.setBitmap(bits);
        return rep;
    }

    /**
     * Create an Icon pointing to a compressed bitmap stored in a byte array.
     *
     * @param data
     * 		Byte array storing compressed bitmap data of a type that
     * 		{@link android.graphics.BitmapFactory}
     * 		can decode (see {@link android.graphics.Bitmap.CompressFormat}).
     * @param offset
     * 		Offset into <code>data</code> at which the bitmap data starts
     * @param length
     * 		Length of the bitmap data
     */
    public static android.graphics.drawable.Icon createWithData(byte[] data, int offset, int length) {
        if (data == null) {
            throw new java.lang.IllegalArgumentException("Data must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_DATA);
        rep.mObj1 = data;
        rep.mInt1 = length;
        rep.mInt2 = offset;
        return rep;
    }

    /**
     * Create an Icon pointing to an image file specified by URI.
     *
     * @param uri
     * 		A uri referring to local content:// or file:// image data.
     */
    public static android.graphics.drawable.Icon createWithContentUri(java.lang.String uri) {
        if (uri == null) {
            throw new java.lang.IllegalArgumentException("Uri must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_URI);
        rep.mString1 = uri;
        return rep;
    }

    /**
     * Create an Icon pointing to an image file specified by URI.
     *
     * @param uri
     * 		A uri referring to local content:// or file:// image data.
     */
    public static android.graphics.drawable.Icon createWithContentUri(android.net.Uri uri) {
        if (uri == null) {
            throw new java.lang.IllegalArgumentException("Uri must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_URI);
        rep.mString1 = uri.toString();
        return rep;
    }

    /**
     * Store a color to use whenever this Icon is drawn.
     *
     * @param tint
     * 		a color, as in {@link Drawable#setTint(int)}
     * @return this same object, for use in chained construction
     */
    public android.graphics.drawable.Icon setTint(@android.annotation.ColorInt
    int tint) {
        return setTintList(android.content.res.ColorStateList.valueOf(tint));
    }

    /**
     * Store a color to use whenever this Icon is drawn.
     *
     * @param tintList
     * 		as in {@link Drawable#setTintList(ColorStateList)}, null to remove tint
     * @return this same object, for use in chained construction
     */
    public android.graphics.drawable.Icon setTintList(android.content.res.ColorStateList tintList) {
        mTintList = tintList;
        return this;
    }

    /**
     * Store a blending mode to use whenever this Icon is drawn.
     *
     * @param mode
     * 		a blending mode, as in {@link Drawable#setTintMode(PorterDuff.Mode)}, may be null
     * @return this same object, for use in chained construction
     */
    @android.annotation.NonNull
    public android.graphics.drawable.Icon setTintMode(@android.annotation.NonNull
    android.graphics.PorterDuff.Mode mode) {
        mBlendMode = android.graphics.BlendMode.fromValue(mode.nativeInt);
        return this;
    }

    /**
     * Store a blending mode to use whenever this Icon is drawn.
     *
     * @param mode
     * 		a blending mode, as in {@link Drawable#setTintMode(PorterDuff.Mode)}, may be null
     * @return this same object, for use in chained construction
     */
    @android.annotation.NonNull
    public android.graphics.drawable.Icon setTintBlendMode(@android.annotation.NonNull
    android.graphics.BlendMode mode) {
        mBlendMode = mode;
        return this;
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public boolean hasTint() {
        return (mTintList != null) || (mBlendMode != android.graphics.drawable.Icon.DEFAULT_BLEND_MODE);
    }

    /**
     * Create an Icon pointing to an image file specified by path.
     *
     * @param path
     * 		A path to a file that contains compressed bitmap data of
     * 		a type that {@link android.graphics.BitmapFactory} can decode.
     */
    public static android.graphics.drawable.Icon createWithFilePath(java.lang.String path) {
        if (path == null) {
            throw new java.lang.IllegalArgumentException("Path must not be null.");
        }
        final android.graphics.drawable.Icon rep = new android.graphics.drawable.Icon(android.graphics.drawable.Icon.TYPE_URI);
        rep.mString1 = path;
        return rep;
    }

    @java.lang.Override
    public java.lang.String toString() {
        final java.lang.StringBuilder sb = new java.lang.StringBuilder("Icon(typ=").append(android.graphics.drawable.Icon.typeToString(mType));
        switch (mType) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                sb.append(" size=").append(getBitmap().getWidth()).append("x").append(getBitmap().getHeight());
                break;
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
                sb.append(" pkg=").append(getResPackage()).append(" id=").append(java.lang.String.format("0x%08x", getResId()));
                break;
            case android.graphics.drawable.Icon.TYPE_DATA :
                sb.append(" len=").append(getDataLength());
                if (getDataOffset() != 0) {
                    sb.append(" off=").append(getDataOffset());
                }
                break;
            case android.graphics.drawable.Icon.TYPE_URI :
                sb.append(" uri=").append(getUriString());
                break;
        }
        if (mTintList != null) {
            sb.append(" tint=");
            java.lang.String sep = "";
            for (int c : mTintList.getColors()) {
                sb.append(java.lang.String.format("%s0x%08x", sep, c));
                sep = "|";
            }
        }
        if (mBlendMode != android.graphics.drawable.Icon.DEFAULT_BLEND_MODE)
            sb.append(" mode=").append(mBlendMode);

        sb.append(")");
        return sb.toString();
    }

    /**
     * Parcelable interface
     */
    public int describeContents() {
        return ((mType == android.graphics.drawable.Icon.TYPE_BITMAP) || (mType == android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP)) || (mType == android.graphics.drawable.Icon.TYPE_DATA) ? android.os.Parcelable.CONTENTS_FILE_DESCRIPTOR : 0;
    }

    // ===== Parcelable interface ======
    private Icon(android.os.Parcel in) {
        this(in.readInt());
        switch (mType) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                final android.graphics.Bitmap bits = this.CREATOR.createFromParcel(in);
                mObj1 = bits;
                break;
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
                final java.lang.String pkg = in.readString();
                final int resId = in.readInt();
                mString1 = pkg;
                mInt1 = resId;
                break;
            case android.graphics.drawable.Icon.TYPE_DATA :
                final int len = in.readInt();
                final byte[] a = in.readBlob();
                if (len != a.length) {
                    throw new java.lang.RuntimeException(((("internal unparceling error: blob length (" + a.length) + ") != expected length (") + len) + ")");
                }
                mInt1 = len;
                mObj1 = a;
                break;
            case android.graphics.drawable.Icon.TYPE_URI :
                final java.lang.String uri = in.readString();
                mString1 = uri;
                break;
            default :
                throw new java.lang.RuntimeException((("invalid " + this.getClass().getSimpleName()) + " type in parcel: ") + mType);
        }
        if (in.readInt() == 1) {
            mTintList = this.CREATOR.createFromParcel(in);
        }
        mBlendMode = android.graphics.BlendMode.fromValue(in.readInt());
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(mType);
        switch (mType) {
            case android.graphics.drawable.Icon.TYPE_BITMAP :
            case android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP :
                final android.graphics.Bitmap bits = getBitmap();
                getBitmap().writeToParcel(dest, flags);
                break;
            case android.graphics.drawable.Icon.TYPE_RESOURCE :
                dest.writeString(getResPackage());
                dest.writeInt(getResId());
                break;
            case android.graphics.drawable.Icon.TYPE_DATA :
                dest.writeInt(getDataLength());
                dest.writeBlob(getDataBytes(), getDataOffset(), getDataLength());
                break;
            case android.graphics.drawable.Icon.TYPE_URI :
                dest.writeString(getUriString());
                break;
        }
        if (mTintList == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(1);
            mTintList.writeToParcel(dest, flags);
        }
        dest.writeInt(android.graphics.BlendMode.toValue(mBlendMode));
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.graphics.drawable.Icon> CREATOR = new android.os.Parcelable.Creator<android.graphics.drawable.Icon>() {
        public android.graphics.drawable.Icon createFromParcel(android.os.Parcel in) {
            return new android.graphics.drawable.Icon(in);
        }

        public android.graphics.drawable.Icon[] newArray(int size) {
            return new android.graphics.drawable.Icon[size];
        }
    };

    /**
     * Scale down a bitmap to a given max width and max height. The scaling will be done in a uniform way
     *
     * @param bitmap
     * 		the bitmap to scale down
     * @param maxWidth
     * 		the maximum width allowed
     * @param maxHeight
     * 		the maximum height allowed
     * @return the scaled bitmap if necessary or the original bitmap if no scaling was needed
     * @unknown 
     */
    public static android.graphics.Bitmap scaleDownIfNecessary(android.graphics.Bitmap bitmap, int maxWidth, int maxHeight) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        if ((bitmapWidth > maxWidth) || (bitmapHeight > maxHeight)) {
            float scale = java.lang.Math.min(((float) (maxWidth)) / bitmapWidth, ((float) (maxHeight)) / bitmapHeight);
            bitmap = /* filter */
            android.graphics.Bitmap.createScaledBitmap(bitmap, java.lang.Math.max(1, ((int) (scale * bitmapWidth))), java.lang.Math.max(1, ((int) (scale * bitmapHeight))), true);
        }
        return bitmap;
    }

    /**
     * Scale down this icon to a given max width and max height.
     * The scaling will be done in a uniform way and currently only bitmaps are supported.
     *
     * @param maxWidth
     * 		the maximum width allowed
     * @param maxHeight
     * 		the maximum height allowed
     * @unknown 
     */
    public void scaleDownIfNecessary(int maxWidth, int maxHeight) {
        if ((mType != android.graphics.drawable.Icon.TYPE_BITMAP) && (mType != android.graphics.drawable.Icon.TYPE_ADAPTIVE_BITMAP)) {
            return;
        }
        android.graphics.Bitmap bitmap = getBitmap();
        setBitmap(android.graphics.drawable.Icon.scaleDownIfNecessary(bitmap, maxWidth, maxHeight));
    }

    /**
     * Implement this interface to receive a callback when
     * {@link #loadDrawableAsync(Context, OnDrawableLoadedListener, Handler) loadDrawableAsync}
     * is finished and your Drawable is ready.
     */
    public interface OnDrawableLoadedListener {
        void onDrawableLoaded(android.graphics.drawable.Drawable d);
    }

    /**
     * Wrapper around loadDrawable that does its work on a pooled thread and then
     * fires back the given (targeted) Message.
     */
    private class LoadDrawableTask implements java.lang.Runnable {
        final android.content.Context mContext;

        final android.os.Message mMessage;

        public LoadDrawableTask(android.content.Context context, final android.os.Handler handler, final android.graphics.drawable.Icon.OnDrawableLoadedListener listener) {
            mContext = context;
            mMessage = android.os.Message.obtain(handler, new java.lang.Runnable() {
                @java.lang.Override
                public void run() {
                    listener.onDrawableLoaded(((android.graphics.drawable.Drawable) (mMessage.obj)));
                }
            });
        }

        public LoadDrawableTask(android.content.Context context, android.os.Message message) {
            mContext = context;
            mMessage = message;
        }

        @java.lang.Override
        public void run() {
            mMessage.obj = loadDrawable(mContext);
            mMessage.sendToTarget();
        }

        public void runAsync() {
            AsyncTask.THREAD_POOL_EXECUTOR.execute(this);
        }
    }
}

