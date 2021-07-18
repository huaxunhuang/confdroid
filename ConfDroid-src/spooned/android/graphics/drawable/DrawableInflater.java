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
 * Instantiates a drawable XML file into its corresponding
 * {@link android.graphics.drawable.Drawable} objects.
 * <p>
 * For performance reasons, inflation relies heavily on pre-processing of
 * XML files that is done at build time. Therefore, it is not currently possible
 * to use this inflater with an XmlPullParser over a plain XML file at runtime;
 * it only works with an XmlPullParser returned from a compiled resource (R.
 * <em>something</em> file.)
 *
 * @unknown Pending API finalization.
 */
public final class DrawableInflater {
    private static final java.util.HashMap<java.lang.String, java.lang.reflect.Constructor<? extends android.graphics.drawable.Drawable>> CONSTRUCTOR_MAP = new java.util.HashMap<>();

    private final android.content.res.Resources mRes;

    @android.annotation.UnsupportedAppUsage
    private final java.lang.ClassLoader mClassLoader;

    /**
     * Loads the drawable resource with the specified identifier.
     *
     * @param context
     * 		the context in which the drawable should be loaded
     * @param id
     * 		the identifier of the drawable resource
     * @return a drawable, or {@code null} if the drawable failed to load
     */
    @android.annotation.Nullable
    public static android.graphics.drawable.Drawable loadDrawable(@android.annotation.NonNull
    android.content.Context context, @android.annotation.DrawableRes
    int id) {
        return android.graphics.drawable.DrawableInflater.loadDrawable(context.getResources(), context.getTheme(), id);
    }

    /**
     * Loads the drawable resource with the specified identifier.
     *
     * @param resources
     * 		the resources from which the drawable should be loaded
     * @param theme
     * 		the theme against which the drawable should be inflated
     * @param id
     * 		the identifier of the drawable resource
     * @return a drawable, or {@code null} if the drawable failed to load
     */
    @android.annotation.Nullable
    public static android.graphics.drawable.Drawable loadDrawable(@android.annotation.NonNull
    android.content.res.Resources resources, @android.annotation.Nullable
    android.content.res.Resources.Theme theme, @android.annotation.DrawableRes
    int id) {
        return resources.getDrawable(id, theme);
    }

    /**
     * Constructs a new drawable inflater using the specified resources and
     * class loader.
     *
     * @param res
     * 		the resources used to resolve resource identifiers
     * @param classLoader
     * 		the class loader used to load custom drawables
     * @unknown 
     */
    public DrawableInflater(@android.annotation.NonNull
    android.content.res.Resources res, @android.annotation.NonNull
    java.lang.ClassLoader classLoader) {
        mRes = res;
        mClassLoader = classLoader;
    }

    /**
     * Inflates a drawable from inside an XML document using an optional
     * {@link Theme}.
     * <p>
     * This method should be called on a parser positioned at a tag in an XML
     * document defining a drawable resource. It will attempt to create a
     * Drawable from the tag at the current position.
     *
     * @param name
     * 		the name of the tag at the current position
     * @param parser
     * 		an XML parser positioned at the drawable tag
     * @param attrs
     * 		an attribute set that wraps the parser
     * @param theme
     * 		the theme against which the drawable should be inflated, or
     * 		{@code null} to not inflate against a theme
     * @return a drawable
     * @throws XmlPullParserException
     * 		
     * @throws IOException
     * 		
     */
    @android.annotation.NonNull
    public android.graphics.drawable.Drawable inflateFromXml(@android.annotation.NonNull
    java.lang.String name, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        return inflateFromXmlForDensity(name, parser, attrs, 0, theme);
    }

    /**
     * Version of {@link #inflateFromXml(String, XmlPullParser, AttributeSet, Theme)} that accepts
     * an override density.
     */
    @android.annotation.NonNull
    android.graphics.drawable.Drawable inflateFromXmlForDensity(@android.annotation.NonNull
    java.lang.String name, @android.annotation.NonNull
    org.xmlpull.v1.XmlPullParser parser, @android.annotation.NonNull
    android.util.AttributeSet attrs, int density, @android.annotation.Nullable
    android.content.res.Resources.Theme theme) throws java.io.IOException, org.xmlpull.v1.XmlPullParserException {
        // Inner classes must be referenced as Outer$Inner, but XML tag names
        // can't contain $, so the <drawable> tag allows developers to specify
        // the class in an attribute. We'll still run it through inflateFromTag
        // to stay consistent with how LayoutInflater works.
        if (name.equals("drawable")) {
            name = attrs.getAttributeValue(null, "class");
            if (name == null) {
                throw new android.view.InflateException("<drawable> tag must specify class attribute");
            }
        }
        android.graphics.drawable.Drawable drawable = inflateFromTag(name);
        if (drawable == null) {
            drawable = inflateFromClass(name);
        }
        drawable.setSrcDensityOverride(density);
        drawable.inflate(mRes, parser, attrs, theme);
        return drawable;
    }

    @android.annotation.NonNull
    @java.lang.SuppressWarnings("deprecation")
    private android.graphics.drawable.Drawable inflateFromTag(@android.annotation.NonNull
    java.lang.String name) {
        switch (name) {
            case "selector" :
                return new android.graphics.drawable.StateListDrawable();
            case "animated-selector" :
                return new android.graphics.drawable.AnimatedStateListDrawable();
            case "level-list" :
                return new android.graphics.drawable.LevelListDrawable();
            case "layer-list" :
                return new android.graphics.drawable.LayerDrawable();
            case "transition" :
                return new android.graphics.drawable.TransitionDrawable();
            case "ripple" :
                return new android.graphics.drawable.RippleDrawable();
            case "adaptive-icon" :
                return new android.graphics.drawable.AdaptiveIconDrawable();
            case "color" :
                return new android.graphics.drawable.ColorDrawable();
            case "shape" :
                return new android.graphics.drawable.GradientDrawable();
            case "vector" :
                return new android.graphics.drawable.VectorDrawable();
            case "animated-vector" :
                return new android.graphics.drawable.AnimatedVectorDrawable();
            case "scale" :
                return new android.graphics.drawable.ScaleDrawable();
            case "clip" :
                return new android.graphics.drawable.ClipDrawable();
            case "rotate" :
                return new android.graphics.drawable.RotateDrawable();
            case "animated-rotate" :
                return new android.graphics.drawable.AnimatedRotateDrawable();
            case "animation-list" :
                return new android.graphics.drawable.AnimationDrawable();
            case "inset" :
                return new android.graphics.drawable.InsetDrawable();
            case "bitmap" :
                return new android.graphics.drawable.BitmapDrawable();
            case "nine-patch" :
                return new android.graphics.drawable.NinePatchDrawable();
            case "animated-image" :
                return new android.graphics.drawable.AnimatedImageDrawable();
            default :
                return null;
        }
    }

    @android.annotation.NonNull
    private android.graphics.drawable.Drawable inflateFromClass(@android.annotation.NonNull
    java.lang.String className) {
        try {
            java.lang.reflect.Constructor<? extends android.graphics.drawable.Drawable> constructor;
            synchronized(android.graphics.drawable.DrawableInflater.CONSTRUCTOR_MAP) {
                constructor = android.graphics.drawable.DrawableInflater.CONSTRUCTOR_MAP.get(className);
                if (constructor == null) {
                    final java.lang.Class<? extends android.graphics.drawable.Drawable> clazz = mClassLoader.loadClass(className).asSubclass(android.graphics.drawable.Drawable.class);
                    constructor = clazz.getConstructor();
                    android.graphics.drawable.DrawableInflater.CONSTRUCTOR_MAP.put(className, constructor);
                }
            }
            return constructor.newInstance();
        } catch (java.lang.NoSuchMethodException e) {
            final android.view.InflateException ie = new android.view.InflateException("Error inflating class " + className);
            ie.initCause(e);
            throw ie;
        } catch (java.lang.ClassCastException e) {
            // If loaded class is not a Drawable subclass.
            final android.view.InflateException ie = new android.view.InflateException("Class is not a Drawable " + className);
            ie.initCause(e);
            throw ie;
        } catch (java.lang.ClassNotFoundException e) {
            // If loadClass fails, we should propagate the exception.
            final android.view.InflateException ie = new android.view.InflateException("Class not found " + className);
            ie.initCause(e);
            throw ie;
        } catch (java.lang.Exception e) {
            final android.view.InflateException ie = new android.view.InflateException("Error inflating class " + className);
            ie.initCause(e);
            throw ie;
        }
    }
}

