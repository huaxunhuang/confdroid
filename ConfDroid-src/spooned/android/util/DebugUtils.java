/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.util;


/**
 * <p>Various utilities for debugging and logging.</p>
 */
public class DebugUtils {
    /**
     *
     *
     * @unknown 
     */
    public DebugUtils() {
    }

    /**
     * <p>Filters objects against the <code>ANDROID_OBJECT_FILTER</code>
     * environment variable. This environment variable can filter objects
     * based on their class name and attribute values.</p>
     *
     * <p>Here is the syntax for <code>ANDROID_OBJECT_FILTER</code>:</p>
     *
     * <p><code>ClassName@attribute1=value1@attribute2=value2...</code></p>
     *
     * <p>Examples:</p>
     * <ul>
     * <li>Select TextView instances: <code>TextView</code></li>
     * <li>Select TextView instances of text "Loading" and bottom offset of 22:
     * <code>TextView@text=Loading.*@bottom=22</code></li>
     * </ul>
     *
     * <p>The class name and the values are regular expressions.</p>
     *
     * <p>This class is useful for debugging and logging purpose:</p>
     * <pre>
     * if (DEBUG) {
     *   if (DebugUtils.isObjectSelected(childView) && LOGV_ENABLED) {
     *     Log.v(TAG, "Object " + childView + " logged!");
     *   }
     * }
     * </pre>
     *
     * <p><strong>NOTE</strong>: This method is very expensive as it relies
     * heavily on regular expressions and reflection. Calls to this method
     * should always be stripped out of the release binaries and avoided
     * as much as possible in debug mode.</p>
     *
     * @param object
     * 		any object to match against the ANDROID_OBJECT_FILTER
     * 		environement variable
     * @return true if object is selected by the ANDROID_OBJECT_FILTER
    environment variable, false otherwise
     */
    public static boolean isObjectSelected(java.lang.Object object) {
        boolean match = false;
        java.lang.String s = java.lang.System.getenv("ANDROID_OBJECT_FILTER");
        if ((s != null) && (s.length() > 0)) {
            java.lang.String[] selectors = s.split("@");
            // first selector == class name
            if (object.getClass().getSimpleName().matches(selectors[0])) {
                // check potential attributes
                for (int i = 1; i < selectors.length; i++) {
                    java.lang.String[] pair = selectors[i].split("=");
                    java.lang.Class<?> klass = object.getClass();
                    try {
                        java.lang.reflect.Method declaredMethod = null;
                        java.lang.Class<?> parent = klass;
                        do {
                            declaredMethod = parent.getDeclaredMethod(("get" + pair[0].substring(0, 1).toUpperCase(java.util.Locale.ROOT)) + pair[0].substring(1), ((java.lang.Class[]) (null)));
                        } while (((parent = klass.getSuperclass()) != null) && (declaredMethod == null) );
                        if (declaredMethod != null) {
                            java.lang.Object value = declaredMethod.invoke(object, ((java.lang.Object[]) (null)));
                            match |= (value != null ? value.toString() : "null").matches(pair[1]);
                        }
                    } catch (java.lang.NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (java.lang.IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (java.lang.reflect.InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return match;
    }

    /**
     *
     *
     * @unknown 
     */
    public static void buildShortClassTag(java.lang.Object cls, java.lang.StringBuilder out) {
        if (cls == null) {
            out.append("null");
        } else {
            java.lang.String simpleName = cls.getClass().getSimpleName();
            if ((simpleName == null) || simpleName.isEmpty()) {
                simpleName = cls.getClass().getName();
                int end = simpleName.lastIndexOf('.');
                if (end > 0) {
                    simpleName = simpleName.substring(end + 1);
                }
            }
            out.append(simpleName);
            out.append('{');
            out.append(java.lang.Integer.toHexString(java.lang.System.identityHashCode(cls)));
        }
    }

    /**
     *
     *
     * @unknown 
     */
    public static void printSizeValue(java.io.PrintWriter pw, long number) {
        float result = number;
        java.lang.String suffix = "";
        if (result > 900) {
            suffix = "KB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "MB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "GB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "TB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "PB";
            result = result / 1024;
        }
        java.lang.String value;
        if (result < 1) {
            value = java.lang.String.format("%.2f", result);
        } else
            if (result < 10) {
                value = java.lang.String.format("%.1f", result);
            } else
                if (result < 100) {
                    value = java.lang.String.format("%.0f", result);
                } else {
                    value = java.lang.String.format("%.0f", result);
                }


        pw.print(value);
        pw.print(suffix);
    }

    /**
     *
     *
     * @unknown 
     */
    public static java.lang.String sizeValueToString(long number, java.lang.StringBuilder outBuilder) {
        if (outBuilder == null) {
            outBuilder = new java.lang.StringBuilder(32);
        }
        float result = number;
        java.lang.String suffix = "";
        if (result > 900) {
            suffix = "KB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "MB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "GB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "TB";
            result = result / 1024;
        }
        if (result > 900) {
            suffix = "PB";
            result = result / 1024;
        }
        java.lang.String value;
        if (result < 1) {
            value = java.lang.String.format("%.2f", result);
        } else
            if (result < 10) {
                value = java.lang.String.format("%.1f", result);
            } else
                if (result < 100) {
                    value = java.lang.String.format("%.0f", result);
                } else {
                    value = java.lang.String.format("%.0f", result);
                }


        outBuilder.append(value);
        outBuilder.append(suffix);
        return outBuilder.toString();
    }

    /**
     * Use prefixed constants (static final values) on given class to turn value
     * into human-readable string.
     *
     * @unknown 
     */
    public static java.lang.String valueToString(java.lang.Class<?> clazz, java.lang.String prefix, int value) {
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            final int modifiers = field.getModifiers();
            if (((java.lang.reflect.Modifier.isStatic(modifiers) && java.lang.reflect.Modifier.isFinal(modifiers)) && field.getType().equals(int.class)) && field.getName().startsWith(prefix)) {
                try {
                    if (value == field.getInt(null)) {
                        return field.getName().substring(prefix.length());
                    }
                } catch (java.lang.IllegalAccessException ignored) {
                }
            }
        }
        return java.lang.Integer.toString(value);
    }

    /**
     * Use prefixed constants (static final values) on given class to turn flags
     * into human-readable string.
     *
     * @unknown 
     */
    public static java.lang.String flagsToString(java.lang.Class<?> clazz, java.lang.String prefix, int flags) {
        final java.lang.StringBuilder res = new java.lang.StringBuilder();
        for (java.lang.reflect.Field field : clazz.getDeclaredFields()) {
            final int modifiers = field.getModifiers();
            if (((java.lang.reflect.Modifier.isStatic(modifiers) && java.lang.reflect.Modifier.isFinal(modifiers)) && field.getType().equals(int.class)) && field.getName().startsWith(prefix)) {
                try {
                    final int value = field.getInt(null);
                    if ((flags & value) != 0) {
                        flags &= ~value;
                        res.append(field.getName().substring(prefix.length())).append('|');
                    }
                } catch (java.lang.IllegalAccessException ignored) {
                }
            }
        }
        if ((flags != 0) || (res.length() == 0)) {
            res.append(java.lang.Integer.toHexString(flags));
        } else {
            res.deleteCharAt(res.length() - 1);
        }
        return res.toString();
    }
}

