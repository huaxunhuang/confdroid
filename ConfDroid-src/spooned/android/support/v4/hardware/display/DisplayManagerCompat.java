/**
 * Copyright (C) 2013 The Android Open Source Project
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
package android.support.v4.hardware.display;


/**
 * Helper for accessing features in {@link android.hardware.display.DisplayManager}
 * introduced after API level 4 in a backwards compatible fashion.
 */
public abstract class DisplayManagerCompat {
    private static final java.util.WeakHashMap<android.content.Context, android.support.v4.hardware.display.DisplayManagerCompat> sInstances = new java.util.WeakHashMap<android.content.Context, android.support.v4.hardware.display.DisplayManagerCompat>();

    /**
     * Display category: Presentation displays.
     * <p>
     * This category can be used to identify secondary displays that are suitable for
     * use as presentation displays.
     * </p>
     *
     * @see android.app.Presentation for information about presenting content
    on secondary displays.
     * @see #getDisplays(String)
     */
    public static final java.lang.String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";

    DisplayManagerCompat() {
    }

    /**
     * Gets an instance of the display manager given the context.
     */
    public static android.support.v4.hardware.display.DisplayManagerCompat getInstance(android.content.Context context) {
        synchronized(android.support.v4.hardware.display.DisplayManagerCompat.sInstances) {
            android.support.v4.hardware.display.DisplayManagerCompat instance = android.support.v4.hardware.display.DisplayManagerCompat.sInstances.get(context);
            if (instance == null) {
                final int version = android.os.Build.VERSION.SDK_INT;
                if (version >= 17) {
                    instance = new android.support.v4.hardware.display.DisplayManagerCompat.JellybeanMr1Impl(context);
                } else {
                    instance = new android.support.v4.hardware.display.DisplayManagerCompat.LegacyImpl(context);
                }
                android.support.v4.hardware.display.DisplayManagerCompat.sInstances.put(context, instance);
            }
            return instance;
        }
    }

    /**
     * Gets information about a logical display.
     *
     * The display metrics may be adjusted to provide compatibility
     * for legacy applications.
     *
     * @param displayId
     * 		The logical display id.
     * @return The display object, or null if there is no valid display with the given id.
     */
    public abstract android.view.Display getDisplay(int displayId);

    /**
     * Gets all currently valid logical displays.
     *
     * @return An array containing all displays.
     */
    public abstract android.view.Display[] getDisplays();

    /**
     * Gets all currently valid logical displays of the specified category.
     * <p>
     * When there are multiple displays in a category the returned displays are sorted
     * of preference.  For example, if the requested category is
     * {@link #DISPLAY_CATEGORY_PRESENTATION} and there are multiple presentation displays
     * then the displays are sorted so that the first display in the returned array
     * is the most preferred presentation display.  The application may simply
     * use the first display or allow the user to choose.
     * </p>
     *
     * @param category
     * 		The requested display category or null to return all displays.
     * @return An array containing all displays sorted by order of preference.
     * @see #DISPLAY_CATEGORY_PRESENTATION
     */
    public abstract android.view.Display[] getDisplays(java.lang.String category);

    private static class LegacyImpl extends android.support.v4.hardware.display.DisplayManagerCompat {
        private final android.view.WindowManager mWindowManager;

        public LegacyImpl(android.content.Context context) {
            mWindowManager = ((android.view.WindowManager) (context.getSystemService(android.content.Context.WINDOW_SERVICE)));
        }

        @java.lang.Override
        public android.view.Display getDisplay(int displayId) {
            android.view.Display display = mWindowManager.getDefaultDisplay();
            if (display.getDisplayId() == displayId) {
                return display;
            }
            return null;
        }

        @java.lang.Override
        public android.view.Display[] getDisplays() {
            return new android.view.Display[]{ mWindowManager.getDefaultDisplay() };
        }

        @java.lang.Override
        public android.view.Display[] getDisplays(java.lang.String category) {
            return category == null ? getDisplays() : new android.view.Display[0];
        }
    }

    private static class JellybeanMr1Impl extends android.support.v4.hardware.display.DisplayManagerCompat {
        private final java.lang.Object mDisplayManagerObj;

        public JellybeanMr1Impl(android.content.Context context) {
            mDisplayManagerObj = android.support.v4.hardware.display.DisplayManagerJellybeanMr1.getDisplayManager(context);
        }

        @java.lang.Override
        public android.view.Display getDisplay(int displayId) {
            return android.support.v4.hardware.display.DisplayManagerJellybeanMr1.getDisplay(mDisplayManagerObj, displayId);
        }

        @java.lang.Override
        public android.view.Display[] getDisplays() {
            return android.support.v4.hardware.display.DisplayManagerJellybeanMr1.getDisplays(mDisplayManagerObj);
        }

        @java.lang.Override
        public android.view.Display[] getDisplays(java.lang.String category) {
            return android.support.v4.hardware.display.DisplayManagerJellybeanMr1.getDisplays(mDisplayManagerObj, category);
        }
    }
}

