/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.support.v7.app;


/**
 * This class is responsible for manually inflating our tinted widgets which are used on devices
 * running {@link android.os.Build.VERSION_CODES#KITKAT KITKAT} or below. As such, this class
 * should only be used when running on those devices.
 * <p>This class two main responsibilities: the first is to 'inject' our tinted views in place of
 * the framework versions in layout inflation; the second is backport the {@code android:theme}
 * functionality for any inflated widgets. This include theme inheritance from it's parent.
 */
class AppCompatViewInflater {
    private static final java.lang.Class<?>[] sConstructorSignature = new java.lang.Class[]{ android.content.Context.class, android.util.AttributeSet.class };

    private static final int[] sOnClickAttrs = new int[]{ android.R.attr.onClick };

    private static final java.lang.String[] sClassPrefixList = new java.lang.String[]{ "android.widget.", "android.view.", "android.webkit." };

    private static final java.lang.String LOG_TAG = "AppCompatViewInflater";

    private static final java.util.Map<java.lang.String, java.lang.reflect.Constructor<? extends android.view.View>> sConstructorMap = new android.support.v4.util.ArrayMap<>();

    private final java.lang.Object[] mConstructorArgs = new java.lang.Object[2];

    public final android.view.View createView(android.view.View parent, final java.lang.String name, @android.support.annotation.NonNull
    android.content.Context context, @android.support.annotation.NonNull
    android.util.AttributeSet attrs, boolean inheritContext, boolean readAndroidTheme, boolean readAppTheme, boolean wrapContext) {
        final android.content.Context originalContext = context;
        // We can emulate Lollipop's android:theme attribute propagating down the view hierarchy
        // by using the parent's context
        if (inheritContext && (parent != null)) {
            context = parent.getContext();
        }
        if (readAndroidTheme || readAppTheme) {
            // We then apply the theme on the context, if specified
            context = android.support.v7.app.AppCompatViewInflater.themifyContext(context, attrs, readAndroidTheme, readAppTheme);
        }
        if (wrapContext) {
            context = android.support.v7.widget.TintContextWrapper.wrap(context);
        }
        android.view.View view = null;
        // We need to 'inject' our tint aware Views in place of the standard framework versions
        switch (name) {
            case "TextView" :
                view = new android.support.v7.widget.AppCompatTextView(context, attrs);
                break;
            case "ImageView" :
                view = new android.support.v7.widget.AppCompatImageView(context, attrs);
                break;
            case "Button" :
                view = new android.support.v7.widget.AppCompatButton(context, attrs);
                break;
            case "EditText" :
                view = new android.support.v7.widget.AppCompatEditText(context, attrs);
                break;
            case "Spinner" :
                view = new android.support.v7.widget.AppCompatSpinner(context, attrs);
                break;
            case "ImageButton" :
                view = new android.support.v7.widget.AppCompatImageButton(context, attrs);
                break;
            case "CheckBox" :
                view = new android.support.v7.widget.AppCompatCheckBox(context, attrs);
                break;
            case "RadioButton" :
                view = new android.support.v7.widget.AppCompatRadioButton(context, attrs);
                break;
            case "CheckedTextView" :
                view = new android.support.v7.widget.AppCompatCheckedTextView(context, attrs);
                break;
            case "AutoCompleteTextView" :
                view = new android.support.v7.widget.AppCompatAutoCompleteTextView(context, attrs);
                break;
            case "MultiAutoCompleteTextView" :
                view = new android.support.v7.widget.AppCompatMultiAutoCompleteTextView(context, attrs);
                break;
            case "RatingBar" :
                view = new android.support.v7.widget.AppCompatRatingBar(context, attrs);
                break;
            case "SeekBar" :
                view = new android.support.v7.widget.AppCompatSeekBar(context, attrs);
                break;
        }
        if ((view == null) && (originalContext != context)) {
            // If the original context does not equal our themed context, then we need to manually
            // inflate it using the name so that android:theme takes effect.
            view = createViewFromTag(context, name, attrs);
        }
        if (view != null) {
            // If we have created a view, check it's android:onClick
            checkOnClickListener(view, attrs);
        }
        return view;
    }

    private android.view.View createViewFromTag(android.content.Context context, java.lang.String name, android.util.AttributeSet attrs) {
        if (name.equals("view")) {
            name = attrs.getAttributeValue(null, "class");
        }
        try {
            mConstructorArgs[0] = context;
            mConstructorArgs[1] = attrs;
            if ((-1) == name.indexOf('.')) {
                for (int i = 0; i < android.support.v7.app.AppCompatViewInflater.sClassPrefixList.length; i++) {
                    final android.view.View view = createView(context, name, android.support.v7.app.AppCompatViewInflater.sClassPrefixList[i]);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            } else {
                return createView(context, name, null);
            }
        } catch (java.lang.Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        } finally {
            // Don't retain references on context.
            mConstructorArgs[0] = null;
            mConstructorArgs[1] = null;
        }
    }

    /**
     * android:onClick doesn't handle views with a ContextWrapper context. This method
     * backports new framework functionality to traverse the Context wrappers to find a
     * suitable target.
     */
    private void checkOnClickListener(android.view.View view, android.util.AttributeSet attrs) {
        final android.content.Context context = view.getContext();
        if ((!(context instanceof android.content.ContextWrapper)) || ((android.os.Build.VERSION.SDK_INT >= 15) && (!android.support.v4.view.ViewCompat.hasOnClickListeners(view)))) {
            // Skip our compat functionality if: the Context isn't a ContextWrapper, or
            // the view doesn't have an OnClickListener (we can only rely on this on API 15+ so
            // always use our compat code on older devices)
            return;
        }
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, android.support.v7.app.AppCompatViewInflater.sOnClickAttrs);
        final java.lang.String handlerName = a.getString(0);
        if (handlerName != null) {
            view.setOnClickListener(new android.support.v7.app.AppCompatViewInflater.DeclaredOnClickListener(view, handlerName));
        }
        a.recycle();
    }

    private android.view.View createView(android.content.Context context, java.lang.String name, java.lang.String prefix) throws android.view.InflateException, java.lang.ClassNotFoundException {
        java.lang.reflect.Constructor<? extends android.view.View> constructor = android.support.v7.app.AppCompatViewInflater.sConstructorMap.get(name);
        try {
            if (constructor == null) {
                // Class not found in the cache, see if it's real, and try to add it
                java.lang.Class<? extends android.view.View> clazz = context.getClassLoader().loadClass(prefix != null ? prefix + name : name).asSubclass(android.view.View.class);
                constructor = clazz.getConstructor(android.support.v7.app.AppCompatViewInflater.sConstructorSignature);
                android.support.v7.app.AppCompatViewInflater.sConstructorMap.put(name, constructor);
            }
            constructor.setAccessible(true);
            return constructor.newInstance(mConstructorArgs);
        } catch (java.lang.Exception e) {
            // We do not want to catch these, lets return null and let the actual LayoutInflater
            // try
            return null;
        }
    }

    /**
     * Allows us to emulate the {@code android:theme} attribute for devices before L.
     */
    private static android.content.Context themifyContext(android.content.Context context, android.util.AttributeSet attrs, boolean useAndroidTheme, boolean useAppTheme) {
        final android.content.res.TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.View, 0, 0);
        int themeId = 0;
        if (useAndroidTheme) {
            // First try reading android:theme if enabled
            themeId = a.getResourceId(R.styleable.View_android_theme, 0);
        }
        if (useAppTheme && (themeId == 0)) {
            // ...if that didn't work, try reading app:theme (for legacy reasons) if enabled
            themeId = a.getResourceId(R.styleable.View_theme, 0);
            if (themeId != 0) {
                android.util.Log.i(android.support.v7.app.AppCompatViewInflater.LOG_TAG, "app:theme is now deprecated. " + "Please move to using android:theme instead.");
            }
        }
        a.recycle();
        if ((themeId != 0) && ((!(context instanceof android.support.v7.view.ContextThemeWrapper)) || (((android.support.v7.view.ContextThemeWrapper) (context)).getThemeResId() != themeId))) {
            // If the context isn't a ContextThemeWrapper, or it is but does not have
            // the same theme as we need, wrap it in a new wrapper
            context = new android.support.v7.view.ContextThemeWrapper(context, themeId);
        }
        return context;
    }

    /**
     * An implementation of OnClickListener that attempts to lazily load a
     * named click handling method from a parent or ancestor context.
     */
    private static class DeclaredOnClickListener implements android.view.View.OnClickListener {
        private final android.view.View mHostView;

        private final java.lang.String mMethodName;

        private java.lang.reflect.Method mResolvedMethod;

        private android.content.Context mResolvedContext;

        public DeclaredOnClickListener(@android.support.annotation.NonNull
        android.view.View hostView, @android.support.annotation.NonNull
        java.lang.String methodName) {
            mHostView = hostView;
            mMethodName = methodName;
        }

        @java.lang.Override
        public void onClick(@android.support.annotation.NonNull
        android.view.View v) {
            if (mResolvedMethod == null) {
                resolveMethod(mHostView.getContext(), mMethodName);
            }
            try {
                mResolvedMethod.invoke(mResolvedContext, v);
            } catch (java.lang.IllegalAccessException e) {
                throw new java.lang.IllegalStateException("Could not execute non-public method for android:onClick", e);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw new java.lang.IllegalStateException("Could not execute method for android:onClick", e);
            }
        }

        @android.support.annotation.NonNull
        private void resolveMethod(@android.support.annotation.Nullable
        android.content.Context context, @android.support.annotation.NonNull
        java.lang.String name) {
            while (context != null) {
                try {
                    if (!context.isRestricted()) {
                        final java.lang.reflect.Method method = context.getClass().getMethod(mMethodName, android.view.View.class);
                        if (method != null) {
                            mResolvedMethod = method;
                            mResolvedContext = context;
                            return;
                        }
                    }
                } catch (java.lang.NoSuchMethodException e) {
                    // Failed to find method, keep searching up the hierarchy.
                }
                if (context instanceof android.content.ContextWrapper) {
                    context = ((android.content.ContextWrapper) (context)).getBaseContext();
                } else {
                    // Can't search up the hierarchy, null out and fail.
                    context = null;
                }
            } 
            final int id = mHostView.getId();
            final java.lang.String idText = (id == android.view.View.NO_ID) ? "" : (" with id '" + mHostView.getContext().getResources().getResourceEntryName(id)) + "'";
            throw new java.lang.IllegalStateException((((("Could not find method " + mMethodName) + "(View) in a parent or ancestor Context for android:onClick ") + "attribute defined on view ") + mHostView.getClass()) + idText);
        }
    }
}

