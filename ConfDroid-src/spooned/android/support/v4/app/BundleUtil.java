package android.support.v4.app;


class BundleUtil {
    /**
     * Get an array of Bundle objects from a parcelable array field in a bundle.
     * Update the bundle to have a typed array so fetches in the future don't need
     * to do an array copy.
     */
    public static android.os.Bundle[] getBundleArrayFromBundle(android.os.Bundle bundle, java.lang.String key) {
        android.os.Parcelable[] array = bundle.getParcelableArray(key);
        if ((array instanceof android.os.Bundle[]) || (array == null)) {
            return ((android.os.Bundle[]) (array));
        }
        android.os.Bundle[] typedArray = java.util.Arrays.copyOf(array, array.length, android.os.Bundle[].class);
        bundle.putParcelableArray(key, typedArray);
        return typedArray;
    }
}

