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
package android.content;


/**
 * Identifier for a specific application component
 * ({@link android.app.Activity}, {@link android.app.Service},
 * {@link android.content.BroadcastReceiver}, or
 * {@link android.content.ContentProvider}) that is available.  Two
 * pieces of information, encapsulated here, are required to identify
 * a component: the package (a String) it exists in, and the class (a String)
 * name inside of that package.
 */
public final class ComponentName implements android.os.Parcelable , java.lang.Cloneable , java.lang.Comparable<android.content.ComponentName> {
    private final java.lang.String mPackage;

    private final java.lang.String mClass;

    /**
     * Create a new component identifier where the class name may be specified
     * as either absolute or relative to the containing package.
     *
     * <p>Relative package names begin with a <code>'.'</code> character. For a package
     * <code>"com.example"</code> and class name <code>".app.MyActivity"</code> this method
     * will return a ComponentName with the package <code>"com.example"</code>and class name
     * <code>"com.example.app.MyActivity"</code>. Fully qualified class names are also
     * permitted.</p>
     *
     * @param pkg
     * 		the name of the package the component exists in
     * @param cls
     * 		the name of the class inside of <var>pkg</var> that implements
     * 		the component
     * @return the new ComponentName
     */
    @android.annotation.NonNull
    public static android.content.ComponentName createRelative(@android.annotation.NonNull
    java.lang.String pkg, @android.annotation.NonNull
    java.lang.String cls) {
        if (android.text.TextUtils.isEmpty(cls)) {
            throw new java.lang.IllegalArgumentException("class name cannot be empty");
        }
        final java.lang.String fullName;
        if (cls.charAt(0) == '.') {
            // Relative to the package. Prepend the package name.
            fullName = pkg + cls;
        } else {
            // Fully qualified package name.
            fullName = cls;
        }
        return new android.content.ComponentName(pkg, fullName);
    }

    /**
     * Create a new component identifier where the class name may be specified
     * as either absolute or relative to the containing package.
     *
     * <p>Relative package names begin with a <code>'.'</code> character. For a package
     * <code>"com.example"</code> and class name <code>".app.MyActivity"</code> this method
     * will return a ComponentName with the package <code>"com.example"</code>and class name
     * <code>"com.example.app.MyActivity"</code>. Fully qualified class names are also
     * permitted.</p>
     *
     * @param pkg
     * 		a Context for the package implementing the component
     * @param cls
     * 		the name of the class inside of <var>pkg</var> that implements
     * 		the component
     * @return the new ComponentName
     */
    @android.annotation.NonNull
    public static android.content.ComponentName createRelative(@android.annotation.NonNull
    android.content.Context pkg, @android.annotation.NonNull
    java.lang.String cls) {
        return android.content.ComponentName.createRelative(pkg.getPackageName(), cls);
    }

    /**
     * Create a new component identifier.
     *
     * @param pkg
     * 		The name of the package that the component exists in.  Can
     * 		not be null.
     * @param cls
     * 		The name of the class inside of <var>pkg</var> that
     * 		implements the component.  Can not be null.
     */
    public ComponentName(@android.annotation.NonNull
    java.lang.String pkg, @android.annotation.NonNull
    java.lang.String cls) {
        if (pkg == null)
            throw new java.lang.NullPointerException("package name is null");

        if (cls == null)
            throw new java.lang.NullPointerException("class name is null");

        mPackage = pkg;
        mClass = cls;
    }

    /**
     * Create a new component identifier from a Context and class name.
     *
     * @param pkg
     * 		A Context for the package implementing the component,
     * 		from which the actual package name will be retrieved.
     * @param cls
     * 		The name of the class inside of <var>pkg</var> that
     * 		implements the component.
     */
    public ComponentName(@android.annotation.NonNull
    android.content.Context pkg, @android.annotation.NonNull
    java.lang.String cls) {
        if (cls == null)
            throw new java.lang.NullPointerException("class name is null");

        mPackage = pkg.getPackageName();
        mClass = cls;
    }

    /**
     * Create a new component identifier from a Context and Class object.
     *
     * @param pkg
     * 		A Context for the package implementing the component, from
     * 		which the actual package name will be retrieved.
     * @param cls
     * 		The Class object of the desired component, from which the
     * 		actual class name will be retrieved.
     */
    public ComponentName(@android.annotation.NonNull
    android.content.Context pkg, @android.annotation.NonNull
    java.lang.Class<?> cls) {
        mPackage = pkg.getPackageName();
        mClass = cls.getName();
    }

    public android.content.ComponentName clone() {
        return new android.content.ComponentName(mPackage, mClass);
    }

    /**
     * Return the package name of this component.
     */
    @android.annotation.NonNull
    public java.lang.String getPackageName() {
        return mPackage;
    }

    /**
     * Return the class name of this component.
     */
    @android.annotation.NonNull
    public java.lang.String getClassName() {
        return mClass;
    }

    /**
     * Return the class name, either fully qualified or in a shortened form
     * (with a leading '.') if it is a suffix of the package.
     */
    public java.lang.String getShortClassName() {
        if (mClass.startsWith(mPackage)) {
            int PN = mPackage.length();
            int CN = mClass.length();
            if ((CN > PN) && (mClass.charAt(PN) == '.')) {
                return mClass.substring(PN, CN);
            }
        }
        return mClass;
    }

    private static void appendShortClassName(java.lang.StringBuilder sb, java.lang.String packageName, java.lang.String className) {
        if (className.startsWith(packageName)) {
            int PN = packageName.length();
            int CN = className.length();
            if ((CN > PN) && (className.charAt(PN) == '.')) {
                sb.append(className, PN, CN);
                return;
            }
        }
        sb.append(className);
    }

    private static void printShortClassName(java.io.PrintWriter pw, java.lang.String packageName, java.lang.String className) {
        if (className.startsWith(packageName)) {
            int PN = packageName.length();
            int CN = className.length();
            if ((CN > PN) && (className.charAt(PN) == '.')) {
                pw.write(className, PN, CN - PN);
                return;
            }
        }
        pw.print(className);
    }

    /**
     * Helper to get {@link #flattenToShortString()} in a {@link ComponentName} reference that can
     * be {@code null}.
     *
     * @unknown 
     */
    @android.annotation.Nullable
    public static java.lang.String flattenToShortString(@android.annotation.Nullable
    android.content.ComponentName componentName) {
        return componentName == null ? null : componentName.flattenToShortString();
    }

    /**
     * Return a String that unambiguously describes both the package and
     * class names contained in the ComponentName.  You can later recover
     * the ComponentName from this string through
     * {@link #unflattenFromString(String)}.
     *
     * @return Returns a new String holding the package and class names.  This
    is represented as the package name, concatenated with a '/' and then the
    class name.
     * @see #unflattenFromString(String)
     */
    @android.annotation.NonNull
    public java.lang.String flattenToString() {
        return (mPackage + "/") + mClass;
    }

    /**
     * The same as {@link #flattenToString()}, but abbreviates the class
     * name if it is a suffix of the package.  The result can still be used
     * with {@link #unflattenFromString(String)}.
     *
     * @return Returns a new String holding the package and class names.  This
    is represented as the package name, concatenated with a '/' and then the
    class name.
     * @see #unflattenFromString(String)
     */
    @android.annotation.NonNull
    public java.lang.String flattenToShortString() {
        java.lang.StringBuilder sb = new java.lang.StringBuilder(mPackage.length() + mClass.length());
        android.content.ComponentName.appendShortString(sb, mPackage, mClass);
        return sb.toString();
    }

    /**
     *
     *
     * @unknown 
     */
    public void appendShortString(java.lang.StringBuilder sb) {
        android.content.ComponentName.appendShortString(sb, mPackage, mClass);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void appendShortString(java.lang.StringBuilder sb, java.lang.String packageName, java.lang.String className) {
        sb.append(packageName).append('/');
        android.content.ComponentName.appendShortClassName(sb, packageName, className);
    }

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.UnsupportedAppUsage
    public static void printShortString(java.io.PrintWriter pw, java.lang.String packageName, java.lang.String className) {
        pw.print(packageName);
        pw.print('/');
        android.content.ComponentName.printShortClassName(pw, packageName, className);
    }

    /**
     * Recover a ComponentName from a String that was previously created with
     * {@link #flattenToString()}.  It splits the string at the first '/',
     * taking the part before as the package name and the part after as the
     * class name.  As a special convenience (to use, for example, when
     * parsing component names on the command line), if the '/' is immediately
     * followed by a '.' then the final class name will be the concatenation
     * of the package name with the string following the '/'.  Thus
     * "com.foo/.Blah" becomes package="com.foo" class="com.foo.Blah".
     *
     * @param str
     * 		The String that was returned by flattenToString().
     * @return Returns a new ComponentName containing the package and class
    names that were encoded in <var>str</var>
     * @see #flattenToString()
     */
    @android.annotation.Nullable
    public static android.content.ComponentName unflattenFromString(@android.annotation.NonNull
    java.lang.String str) {
        int sep = str.indexOf('/');
        if ((sep < 0) || ((sep + 1) >= str.length())) {
            return null;
        }
        java.lang.String pkg = str.substring(0, sep);
        java.lang.String cls = str.substring(sep + 1);
        if ((cls.length() > 0) && (cls.charAt(0) == '.')) {
            cls = pkg + cls;
        }
        return new android.content.ComponentName(pkg, cls);
    }

    /**
     * Return string representation of this class without the class's name
     * as a prefix.
     */
    public java.lang.String toShortString() {
        return ((("{" + mPackage) + "/") + mClass) + "}";
    }

    @java.lang.Override
    public java.lang.String toString() {
        return ((("ComponentInfo{" + mPackage) + "/") + mClass) + "}";
    }

    /**
     * Put this here so that individual services don't have to reimplement this. @hide
     */
    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        final long token = proto.start(fieldId);
        proto.write(ComponentNameProto.PACKAGE_NAME, mPackage);
        proto.write(ComponentNameProto.CLASS_NAME, mClass);
        proto.end(token);
    }

    @java.lang.Override
    public boolean equals(java.lang.Object obj) {
        try {
            if (obj != null) {
                android.content.ComponentName other = ((android.content.ComponentName) (obj));
                // Note: no null checks, because mPackage and mClass can
                // never be null.
                return mPackage.equals(other.mPackage) && mClass.equals(other.mClass);
            }
        } catch (java.lang.ClassCastException e) {
        }
        return false;
    }

    @java.lang.Override
    public int hashCode() {
        return mPackage.hashCode() + mClass.hashCode();
    }

    public int compareTo(android.content.ComponentName that) {
        int v;
        v = this.mPackage.compareTo(that.mPackage);
        if (v != 0) {
            return v;
        }
        return this.mClass.compareTo(that.mClass);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(android.os.Parcel out, int flags) {
        // WARNING: If you modify this function, also update
        // frameworks/base/libs/services/src/content/ComponentName.cpp.
        out.writeString(mPackage);
        out.writeString(mClass);
    }

    /**
     * Write a ComponentName to a Parcel, handling null pointers.  Must be
     * read with {@link #readFromParcel(Parcel)}.
     *
     * @param c
     * 		The ComponentName to be written.
     * @param out
     * 		The Parcel in which the ComponentName will be placed.
     * @see #readFromParcel(Parcel)
     */
    public static void writeToParcel(android.content.ComponentName c, android.os.Parcel out) {
        if (c != null) {
            c.writeToParcel(out, 0);
        } else {
            out.writeString(null);
        }
    }

    /**
     * Read a ComponentName from a Parcel that was previously written
     * with {@link #writeToParcel(ComponentName, Parcel)}, returning either
     * a null or new object as appropriate.
     *
     * @param in
     * 		The Parcel from which to read the ComponentName
     * @return Returns a new ComponentName matching the previously written
    object, or null if a null had been written.
     * @see #writeToParcel(ComponentName, Parcel)
     */
    public static android.content.ComponentName readFromParcel(android.os.Parcel in) {
        java.lang.String pkg = in.readString();
        return pkg != null ? new android.content.ComponentName(pkg, in) : null;
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.content.ComponentName> CREATOR = new android.os.Parcelable.Creator<android.content.ComponentName>() {
        public android.content.ComponentName createFromParcel(android.os.Parcel in) {
            return new android.content.ComponentName(in);
        }

        public android.content.ComponentName[] newArray(int size) {
            return new android.content.ComponentName[size];
        }
    };

    /**
     * Instantiate a new ComponentName from the data in a Parcel that was
     * previously written with {@link #writeToParcel(Parcel, int)}.  Note that you
     * must not use this with data written by
     * {@link #writeToParcel(ComponentName, Parcel)} since it is not possible
     * to handle a null ComponentObject here.
     *
     * @param in
     * 		The Parcel containing the previously written ComponentName,
     * 		positioned at the location in the buffer where it was written.
     */
    public ComponentName(android.os.Parcel in) {
        mPackage = in.readString();
        if (mPackage == null)
            throw new java.lang.NullPointerException("package name is null");

        mClass = in.readString();
        if (mClass == null)
            throw new java.lang.NullPointerException("class name is null");

    }

    private ComponentName(java.lang.String pkg, android.os.Parcel in) {
        mPackage = pkg;
        mClass = in.readString();
    }

    /**
     * Interface for classes associated with a component name.
     *
     * @unknown 
     */
    @java.lang.FunctionalInterface
    public interface WithComponentName {
        /**
         * Return the associated component name.
         */
        android.content.ComponentName getComponentName();
    }
}

