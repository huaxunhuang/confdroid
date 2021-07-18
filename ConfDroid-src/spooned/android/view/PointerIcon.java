/**
 * Copyright (C) 2011 The Android Open Source Project
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
package android.view;


/**
 * Represents an icon that can be used as a mouse pointer.
 * <p>
 * Pointer icons can be provided either by the system using system types,
 * or by applications using bitmaps or application resources.
 * </p>
 */
public final class PointerIcon implements android.os.Parcelable {
    private static final java.lang.String TAG = "PointerIcon";

    /**
     * {@hide } Type constant: Custom icon with a user-supplied bitmap.
     */
    public static final int TYPE_CUSTOM = -1;

    /**
     * Type constant: Null icon.  It has no bitmap.
     */
    public static final int TYPE_NULL = 0;

    /**
     * Type constant: no icons are specified. If all views uses this, then falls back
     * to the default type, but this is helpful to distinguish a view explicitly want
     * to have the default icon.
     *
     * @unknown 
     */
    public static final int TYPE_NOT_SPECIFIED = 1;

    /**
     * Type constant: Arrow icon.  (Default mouse pointer)
     */
    public static final int TYPE_ARROW = 1000;

    /**
     * {@hide } Type constant: Spot hover icon for touchpads.
     */
    public static final int TYPE_SPOT_HOVER = 2000;

    /**
     * {@hide } Type constant: Spot touch icon for touchpads.
     */
    public static final int TYPE_SPOT_TOUCH = 2001;

    /**
     * {@hide } Type constant: Spot anchor icon for touchpads.
     */
    public static final int TYPE_SPOT_ANCHOR = 2002;

    // Type constants for additional predefined icons for mice.
    /**
     * Type constant: context-menu.
     */
    public static final int TYPE_CONTEXT_MENU = 1001;

    /**
     * Type constant: hand.
     */
    public static final int TYPE_HAND = 1002;

    /**
     * Type constant: help.
     */
    public static final int TYPE_HELP = 1003;

    /**
     * Type constant: wait.
     */
    public static final int TYPE_WAIT = 1004;

    /**
     * Type constant: cell.
     */
    public static final int TYPE_CELL = 1006;

    /**
     * Type constant: crosshair.
     */
    public static final int TYPE_CROSSHAIR = 1007;

    /**
     * Type constant: text.
     */
    public static final int TYPE_TEXT = 1008;

    /**
     * Type constant: vertical-text.
     */
    public static final int TYPE_VERTICAL_TEXT = 1009;

    /**
     * Type constant: alias (indicating an alias of/shortcut to something is
     * to be created.
     */
    public static final int TYPE_ALIAS = 1010;

    /**
     * Type constant: copy.
     */
    public static final int TYPE_COPY = 1011;

    /**
     * Type constant: no-drop.
     */
    public static final int TYPE_NO_DROP = 1012;

    /**
     * Type constant: all-scroll.
     */
    public static final int TYPE_ALL_SCROLL = 1013;

    /**
     * Type constant: horizontal double arrow mainly for resizing.
     */
    public static final int TYPE_HORIZONTAL_DOUBLE_ARROW = 1014;

    /**
     * Type constant: vertical double arrow mainly for resizing.
     */
    public static final int TYPE_VERTICAL_DOUBLE_ARROW = 1015;

    /**
     * Type constant: diagonal double arrow -- top-right to bottom-left.
     */
    public static final int TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW = 1016;

    /**
     * Type constant: diagonal double arrow -- top-left to bottom-right.
     */
    public static final int TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW = 1017;

    /**
     * Type constant: zoom-in.
     */
    public static final int TYPE_ZOOM_IN = 1018;

    /**
     * Type constant: zoom-out.
     */
    public static final int TYPE_ZOOM_OUT = 1019;

    /**
     * Type constant: grab.
     */
    public static final int TYPE_GRAB = 1020;

    /**
     * Type constant: grabbing.
     */
    public static final int TYPE_GRABBING = 1021;

    // OEM private types should be defined starting at this range to avoid
    // conflicts with any system types that may be defined in the future.
    private static final int TYPE_OEM_FIRST = 10000;

    /**
     * The default pointer icon.
     */
    public static final int TYPE_DEFAULT = android.view.PointerIcon.TYPE_ARROW;

    private static final android.view.PointerIcon gNullIcon = new android.view.PointerIcon(android.view.PointerIcon.TYPE_NULL);

    private static final android.util.SparseArray<android.util.SparseArray<android.view.PointerIcon>> gSystemIconsByDisplay = new android.util.SparseArray<android.util.SparseArray<android.view.PointerIcon>>();

    private static boolean sUseLargeIcons = false;

    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    private final int mType;

    private int mSystemIconResourceId;

    @android.annotation.UnsupportedAppUsage
    private android.graphics.Bitmap mBitmap;

    @android.annotation.UnsupportedAppUsage
    private float mHotSpotX;

    @android.annotation.UnsupportedAppUsage
    private float mHotSpotY;

    // The bitmaps for the additional frame of animated pointer icon. Note that the first frame
    // will be stored in mBitmap.
    @android.annotation.UnsupportedAppUsage
    private android.graphics.Bitmap[] mBitmapFrames;

    @android.annotation.UnsupportedAppUsage
    private int mDurationPerFrame;

    /**
     * Listener for displays lifecycle.
     *
     * @unknown 
     */
    private static DisplayManager.DisplayListener sDisplayListener;

    private PointerIcon(int type) {
        mType = type;
    }

    /**
     * Gets a special pointer icon that has no bitmap.
     *
     * @return The null pointer icon.
     * @see #TYPE_NULL
     * @unknown 
     */
    public static android.view.PointerIcon getNullIcon() {
        return android.view.PointerIcon.gNullIcon;
    }

    /**
     * Gets the default pointer icon.
     *
     * @param context
     * 		The context.
     * @return The default pointer icon.
     * @throws IllegalArgumentException
     * 		if context is null.
     * @unknown 
     */
    public static android.view.PointerIcon getDefaultIcon(@android.annotation.NonNull
    android.content.Context context) {
        return android.view.PointerIcon.getSystemIcon(context, android.view.PointerIcon.TYPE_DEFAULT);
    }

    /**
     * Gets a system pointer icon for the given type.
     * If typeis not recognized, returns the default pointer icon.
     *
     * @param context
     * 		The context.
     * @param type
     * 		The pointer icon type.
     * @return The pointer icon.
     * @throws IllegalArgumentException
     * 		if context is null.
     */
    public static android.view.PointerIcon getSystemIcon(@android.annotation.NonNull
    android.content.Context context, int type) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context must not be null");
        }
        if (type == android.view.PointerIcon.TYPE_NULL) {
            return android.view.PointerIcon.gNullIcon;
        }
        if (android.view.PointerIcon.sDisplayListener == null) {
            android.view.PointerIcon.registerDisplayListener(context);
        }
        final int displayId = context.getDisplayId();
        android.util.SparseArray<android.view.PointerIcon> systemIcons = android.view.PointerIcon.gSystemIconsByDisplay.get(displayId);
        if (systemIcons == null) {
            systemIcons = new android.util.SparseArray();
            android.view.PointerIcon.gSystemIconsByDisplay.put(displayId, systemIcons);
        }
        android.view.PointerIcon icon = systemIcons.get(type);
        // Reload if not in the same display.
        if (icon != null) {
            return icon;
        }
        int typeIndex = android.view.PointerIcon.getSystemIconTypeIndex(type);
        if (typeIndex == 0) {
            typeIndex = android.view.PointerIcon.getSystemIconTypeIndex(android.view.PointerIcon.TYPE_DEFAULT);
        }
        int defStyle = (android.view.PointerIcon.sUseLargeIcons) ? com.android.internal.R.style.LargePointer : com.android.internal.R.style.Pointer;
        android.content.res.TypedArray a = context.obtainStyledAttributes(null, com.android.internal.R.styleable.Pointer, 0, defStyle);
        int resourceId = a.getResourceId(typeIndex, -1);
        a.recycle();
        if (resourceId == (-1)) {
            android.util.Log.w(android.view.PointerIcon.TAG, "Missing theme resources for pointer icon type " + type);
            return type == android.view.PointerIcon.TYPE_DEFAULT ? android.view.PointerIcon.gNullIcon : android.view.PointerIcon.getSystemIcon(context, android.view.PointerIcon.TYPE_DEFAULT);
        }
        icon = new android.view.PointerIcon(type);
        if ((resourceId & 0xff000000) == 0x1000000) {
            icon.mSystemIconResourceId = resourceId;
        } else {
            icon.loadResource(context, context.getResources(), resourceId);
        }
        systemIcons.append(type, icon);
        return icon;
    }

    /**
     * Updates wheter accessibility large icons are used or not.
     *
     * @unknown 
     */
    public static void setUseLargeIcons(boolean use) {
        android.view.PointerIcon.sUseLargeIcons = use;
        android.view.PointerIcon.gSystemIconsByDisplay.clear();
    }

    /**
     * Creates a custom pointer icon from the given bitmap and hotspot information.
     *
     * @param bitmap
     * 		The bitmap for the icon.
     * @param hotSpotX
     * 		The X offset of the pointer icon hotspot in the bitmap.
     * 		Must be within the [0, bitmap.getWidth()) range.
     * @param hotSpotY
     * 		The Y offset of the pointer icon hotspot in the bitmap.
     * 		Must be within the [0, bitmap.getHeight()) range.
     * @return A pointer icon for this bitmap.
     * @throws IllegalArgumentException
     * 		if bitmap is null, or if the x/y hotspot
     * 		parameters are invalid.
     */
    public static android.view.PointerIcon create(@android.annotation.NonNull
    android.graphics.Bitmap bitmap, float hotSpotX, float hotSpotY) {
        if (bitmap == null) {
            throw new java.lang.IllegalArgumentException("bitmap must not be null");
        }
        android.view.PointerIcon.validateHotSpot(bitmap, hotSpotX, hotSpotY);
        android.view.PointerIcon icon = new android.view.PointerIcon(android.view.PointerIcon.TYPE_CUSTOM);
        icon.mBitmap = bitmap;
        icon.mHotSpotX = hotSpotX;
        icon.mHotSpotY = hotSpotY;
        return icon;
    }

    /**
     * Loads a custom pointer icon from an XML resource.
     * <p>
     * The XML resource should have the following form:
     * <code>
     * &lt;?xml version="1.0" encoding="utf-8"?&gt;
     * &lt;pointer-icon xmlns:android="http://schemas.android.com/apk/res/android"
     *   android:bitmap="@drawable/my_pointer_bitmap"
     *   android:hotSpotX="24"
     *   android:hotSpotY="24" /&gt;
     * </code>
     * </p>
     *
     * @param resources
     * 		The resources object.
     * @param resourceId
     * 		The resource id.
     * @return The pointer icon.
     * @throws IllegalArgumentException
     * 		if resources is null.
     * @throws Resources.NotFoundException
     * 		if the resource was not found or the drawable
     * 		linked in the resource was not found.
     */
    public static android.view.PointerIcon load(@android.annotation.NonNull
    android.content.res.Resources resources, @android.annotation.XmlRes
    int resourceId) {
        if (resources == null) {
            throw new java.lang.IllegalArgumentException("resources must not be null");
        }
        android.view.PointerIcon icon = new android.view.PointerIcon(android.view.PointerIcon.TYPE_CUSTOM);
        icon.loadResource(null, resources, resourceId);
        return icon;
    }

    /**
     * Loads the bitmap and hotspot information for a pointer icon, if it is not already loaded.
     * Returns a pointer icon (not necessarily the same instance) with the information filled in.
     *
     * @param context
     * 		The context.
     * @return The loaded pointer icon.
     * @throws IllegalArgumentException
     * 		if context is null.
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage(maxTargetSdk = Build.VERSION_CODES.P, trackingBug = 115609023)
    public android.view.PointerIcon load(@android.annotation.NonNull
    android.content.Context context) {
        if (context == null) {
            throw new java.lang.IllegalArgumentException("context must not be null");
        }
        if ((mSystemIconResourceId == 0) || (mBitmap != null)) {
            return this;
        }
        android.view.PointerIcon result = new android.view.PointerIcon(mType);
        result.mSystemIconResourceId = mSystemIconResourceId;
        result.loadResource(context, context.getResources(), mSystemIconResourceId);
        return result;
    }

    /**
     *
     *
     * @unknown 
     */
    public int getType() {
        return mType;
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.PointerIcon> CREATOR = new android.os.Parcelable.Creator<android.view.PointerIcon>() {
        public android.view.PointerIcon createFromParcel(android.os.Parcel in) {
            int type = in.readInt();
            if (type == android.view.PointerIcon.TYPE_NULL) {
                return getNullIcon();
            }
            int systemIconResourceId = in.readInt();
            if (systemIconResourceId != 0) {
                android.view.PointerIcon icon = new android.view.PointerIcon(type);
                icon.mSystemIconResourceId = systemIconResourceId;
                return icon;
            }
            android.graphics.Bitmap bitmap = Bitmap.CREATOR.createFromParcel(in);
            float hotSpotX = in.readFloat();
            float hotSpotY = in.readFloat();
            return android.view.PointerIcon.create(bitmap, hotSpotX, hotSpotY);
        }

        public android.view.PointerIcon[] newArray(int size) {
            return new android.view.PointerIcon[size];
        }
    };

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        out.writeInt(mType);
        if (mType != android.view.PointerIcon.TYPE_NULL) {
            out.writeInt(mSystemIconResourceId);
            if (mSystemIconResourceId == 0) {
                mBitmap.writeToParcel(out, flags);
                out.writeFloat(mHotSpotX);
                out.writeFloat(mHotSpotY);
            }
        }
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        if (this == other) {
            return true;
        }
        if ((other == null) || (!(other instanceof android.view.PointerIcon))) {
            return false;
        }
        android.view.PointerIcon otherIcon = ((android.view.PointerIcon) (other));
        if ((mType != otherIcon.mType) || (mSystemIconResourceId != otherIcon.mSystemIconResourceId)) {
            return false;
        }
        if ((mSystemIconResourceId == 0) && (((mBitmap != otherIcon.mBitmap) || (mHotSpotX != otherIcon.mHotSpotX)) || (mHotSpotY != otherIcon.mHotSpotY))) {
            return false;
        }
        return true;
    }

    /**
     * Get the Bitmap from the Drawable.
     *
     *  If the Bitmap needed to be scaled up to account for density, BitmapDrawable
     *  handles this at draw time. But this class doesn't actually draw the Bitmap;
     *  it is just a holder for native code to access its SkBitmap. So this needs to
     *  get a version that is scaled to account for density.
     */
    private android.graphics.Bitmap getBitmapFromDrawable(android.graphics.drawable.BitmapDrawable bitmapDrawable) {
        android.graphics.Bitmap bitmap = bitmapDrawable.getBitmap();
        final int scaledWidth = bitmapDrawable.getIntrinsicWidth();
        final int scaledHeight = bitmapDrawable.getIntrinsicHeight();
        if ((scaledWidth == bitmap.getWidth()) && (scaledHeight == bitmap.getHeight())) {
            return bitmap;
        }
        android.graphics.Rect src = new android.graphics.Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        android.graphics.RectF dst = new android.graphics.RectF(0, 0, scaledWidth, scaledHeight);
        android.graphics.Bitmap scaled = android.graphics.Bitmap.createBitmap(scaledWidth, scaledHeight, bitmap.getConfig());
        android.graphics.Canvas canvas = new android.graphics.Canvas(scaled);
        android.graphics.Paint paint = new android.graphics.Paint();
        paint.setFilterBitmap(true);
        canvas.drawBitmap(bitmap, src, dst, paint);
        return scaled;
    }

    private void loadResource(android.content.Context context, android.content.res.Resources resources, @android.annotation.XmlRes
    int resourceId) {
        final android.content.res.XmlResourceParser parser = resources.getXml(resourceId);
        final int bitmapRes;
        final float hotSpotX;
        final float hotSpotY;
        try {
            com.android.internal.util.XmlUtils.beginDocument(parser, "pointer-icon");
            final android.content.res.TypedArray a = resources.obtainAttributes(parser, com.android.internal.R.styleable.PointerIcon);
            bitmapRes = a.getResourceId(com.android.internal.R.styleable.PointerIcon_bitmap, 0);
            hotSpotX = a.getDimension(com.android.internal.R.styleable.PointerIcon_hotSpotX, 0);
            hotSpotY = a.getDimension(com.android.internal.R.styleable.PointerIcon_hotSpotY, 0);
            a.recycle();
        } catch (java.lang.Exception ex) {
            throw new java.lang.IllegalArgumentException("Exception parsing pointer icon resource.", ex);
        } finally {
            parser.close();
        }
        if (bitmapRes == 0) {
            throw new java.lang.IllegalArgumentException("<pointer-icon> is missing bitmap attribute.");
        }
        android.graphics.drawable.Drawable drawable;
        if (context == null) {
            drawable = resources.getDrawable(bitmapRes);
        } else {
            drawable = context.getDrawable(bitmapRes);
        }
        if (drawable instanceof android.graphics.drawable.AnimationDrawable) {
            // Extract animation frame bitmaps.
            final android.graphics.drawable.AnimationDrawable animationDrawable = ((android.graphics.drawable.AnimationDrawable) (drawable));
            final int frames = animationDrawable.getNumberOfFrames();
            drawable = animationDrawable.getFrame(0);
            if (frames == 1) {
                android.util.Log.w(android.view.PointerIcon.TAG, "Animation icon with single frame -- simply treating the first " + "frame as a normal bitmap icon.");
            } else {
                // Assumes they have the exact duration.
                mDurationPerFrame = animationDrawable.getDuration(0);
                mBitmapFrames = new android.graphics.Bitmap[frames - 1];
                final int width = drawable.getIntrinsicWidth();
                final int height = drawable.getIntrinsicHeight();
                for (int i = 1; i < frames; ++i) {
                    android.graphics.drawable.Drawable drawableFrame = animationDrawable.getFrame(i);
                    if (!(drawableFrame instanceof android.graphics.drawable.BitmapDrawable)) {
                        throw new java.lang.IllegalArgumentException("Frame of an animated pointer icon " + "must refer to a bitmap drawable.");
                    }
                    if ((drawableFrame.getIntrinsicWidth() != width) || (drawableFrame.getIntrinsicHeight() != height)) {
                        throw new java.lang.IllegalArgumentException(((("The bitmap size of " + i) + "-th frame ") + "is different. All frames should have the exact same size and ") + "share the same hotspot.");
                    }
                    android.graphics.drawable.BitmapDrawable bitmapDrawableFrame = ((android.graphics.drawable.BitmapDrawable) (drawableFrame));
                    mBitmapFrames[i - 1] = getBitmapFromDrawable(bitmapDrawableFrame);
                }
            }
        }
        if (!(drawable instanceof android.graphics.drawable.BitmapDrawable)) {
            throw new java.lang.IllegalArgumentException("<pointer-icon> bitmap attribute must " + "refer to a bitmap drawable.");
        }
        android.graphics.drawable.BitmapDrawable bitmapDrawable = ((android.graphics.drawable.BitmapDrawable) (drawable));
        final android.graphics.Bitmap bitmap = getBitmapFromDrawable(bitmapDrawable);
        android.view.PointerIcon.validateHotSpot(bitmap, hotSpotX, hotSpotY);
        // Set the properties now that we have successfully loaded the icon.
        mBitmap = bitmap;
        mHotSpotX = hotSpotX;
        mHotSpotY = hotSpotY;
    }

    private static void validateHotSpot(android.graphics.Bitmap bitmap, float hotSpotX, float hotSpotY) {
        if ((hotSpotX < 0) || (hotSpotX >= bitmap.getWidth())) {
            throw new java.lang.IllegalArgumentException("x hotspot lies outside of the bitmap area");
        }
        if ((hotSpotY < 0) || (hotSpotY >= bitmap.getHeight())) {
            throw new java.lang.IllegalArgumentException("y hotspot lies outside of the bitmap area");
        }
    }

    private static int getSystemIconTypeIndex(int type) {
        switch (type) {
            case android.view.PointerIcon.TYPE_ARROW :
                return com.android.internal.R.styleable.Pointer_pointerIconArrow;
            case android.view.PointerIcon.TYPE_SPOT_HOVER :
                return com.android.internal.R.styleable.Pointer_pointerIconSpotHover;
            case android.view.PointerIcon.TYPE_SPOT_TOUCH :
                return com.android.internal.R.styleable.Pointer_pointerIconSpotTouch;
            case android.view.PointerIcon.TYPE_SPOT_ANCHOR :
                return com.android.internal.R.styleable.Pointer_pointerIconSpotAnchor;
            case android.view.PointerIcon.TYPE_HAND :
                return com.android.internal.R.styleable.Pointer_pointerIconHand;
            case android.view.PointerIcon.TYPE_CONTEXT_MENU :
                return com.android.internal.R.styleable.Pointer_pointerIconContextMenu;
            case android.view.PointerIcon.TYPE_HELP :
                return com.android.internal.R.styleable.Pointer_pointerIconHelp;
            case android.view.PointerIcon.TYPE_WAIT :
                return com.android.internal.R.styleable.Pointer_pointerIconWait;
            case android.view.PointerIcon.TYPE_CELL :
                return com.android.internal.R.styleable.Pointer_pointerIconCell;
            case android.view.PointerIcon.TYPE_CROSSHAIR :
                return com.android.internal.R.styleable.Pointer_pointerIconCrosshair;
            case android.view.PointerIcon.TYPE_TEXT :
                return com.android.internal.R.styleable.Pointer_pointerIconText;
            case android.view.PointerIcon.TYPE_VERTICAL_TEXT :
                return com.android.internal.R.styleable.Pointer_pointerIconVerticalText;
            case android.view.PointerIcon.TYPE_ALIAS :
                return com.android.internal.R.styleable.Pointer_pointerIconAlias;
            case android.view.PointerIcon.TYPE_COPY :
                return com.android.internal.R.styleable.Pointer_pointerIconCopy;
            case android.view.PointerIcon.TYPE_ALL_SCROLL :
                return com.android.internal.R.styleable.Pointer_pointerIconAllScroll;
            case android.view.PointerIcon.TYPE_NO_DROP :
                return com.android.internal.R.styleable.Pointer_pointerIconNodrop;
            case android.view.PointerIcon.TYPE_HORIZONTAL_DOUBLE_ARROW :
                return com.android.internal.R.styleable.Pointer_pointerIconHorizontalDoubleArrow;
            case android.view.PointerIcon.TYPE_VERTICAL_DOUBLE_ARROW :
                return com.android.internal.R.styleable.Pointer_pointerIconVerticalDoubleArrow;
            case android.view.PointerIcon.TYPE_TOP_RIGHT_DIAGONAL_DOUBLE_ARROW :
                return com.android.internal.R.styleable.Pointer_pointerIconTopRightDiagonalDoubleArrow;
            case android.view.PointerIcon.TYPE_TOP_LEFT_DIAGONAL_DOUBLE_ARROW :
                return com.android.internal.R.styleable.Pointer_pointerIconTopLeftDiagonalDoubleArrow;
            case android.view.PointerIcon.TYPE_ZOOM_IN :
                return com.android.internal.R.styleable.Pointer_pointerIconZoomIn;
            case android.view.PointerIcon.TYPE_ZOOM_OUT :
                return com.android.internal.R.styleable.Pointer_pointerIconZoomOut;
            case android.view.PointerIcon.TYPE_GRAB :
                return com.android.internal.R.styleable.Pointer_pointerIconGrab;
            case android.view.PointerIcon.TYPE_GRABBING :
                return com.android.internal.R.styleable.Pointer_pointerIconGrabbing;
            default :
                return 0;
        }
    }

    /**
     * Manage system icon cache handled by display lifecycle.
     *
     * @param context
     * 		The context.
     */
    private static void registerDisplayListener(@android.annotation.NonNull
    android.content.Context context) {
        android.view.PointerIcon.sDisplayListener = new android.hardware.display.DisplayManager.DisplayListener() {
            @java.lang.Override
            public void onDisplayAdded(int displayId) {
            }

            @java.lang.Override
            public void onDisplayRemoved(int displayId) {
                android.view.PointerIcon.gSystemIconsByDisplay.remove(displayId);
            }

            @java.lang.Override
            public void onDisplayChanged(int displayId) {
                android.view.PointerIcon.gSystemIconsByDisplay.remove(displayId);
            }
        };
        android.hardware.display.DisplayManager displayManager = context.getSystemService(android.hardware.display.DisplayManager.class);
        /* handler */
        displayManager.registerDisplayListener(android.view.PointerIcon.sDisplayListener, null);
    }
}

