/**
 * Copyright (C) 2008-2012 The Android Open Source Project
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
package android.renderscript;


/**
 *
 *
 * @unknown 
 * @deprecated in API 16
FileA3D allows users to load RenderScript objects from files
or resources stored on disk. It could be used to load items
such as 3D geometry data converted to a RenderScript format from
content creation tools. Currently only meshes are supported
in FileA3D.

When successfully loaded, FileA3D will contain a list of
index entries for all the objects stored inside it.
 */
public class FileA3D extends android.renderscript.BaseObj {
    /**
     *
     *
     * @deprecated in API 16
    Specifies what renderscript object type is contained within
    the FileA3D IndexEntry
     */
    public enum EntryType {

        /**
         *
         *
         * @deprecated in API 16
        Unknown or or invalid object, nothing will be loaded
         */
        UNKNOWN(0),
        /**
         *
         *
         * @deprecated in API 16
        RenderScript Mesh object
         */
        MESH(1);
        int mID;

        EntryType(int id) {
            mID = id;
        }

        static android.renderscript.FileA3D.EntryType toEntryType(int intID) {
            return android.renderscript.FileA3D.EntryType.values()[intID];
        }
    }

    /**
     *
     *
     * @deprecated in API 16
    IndexEntry contains information about one of the RenderScript
    objects inside the file's index. It could be used to query the
    object's type and also name and load the object itself if
    necessary.
     */
    public static class IndexEntry {
        android.renderscript.RenderScript mRS;

        int mIndex;

        long mID;

        java.lang.String mName;

        android.renderscript.FileA3D.EntryType mEntryType;

        android.renderscript.BaseObj mLoadedObj;

        /**
         *
         *
         * @deprecated in API 16
        Returns the name of a renderscript object the index entry
        describes
         * @return name of a renderscript object the index entry
        describes
         */
        public java.lang.String getName() {
            return mName;
        }

        /**
         *
         *
         * @deprecated in API 16
        Returns the type of a renderscript object the index entry
        describes
         * @return type of a renderscript object the index entry
        describes
         */
        public android.renderscript.FileA3D.EntryType getEntryType() {
            return mEntryType;
        }

        /**
         *
         *
         * @deprecated in API 16
        Used to load the object described by the index entry
         * @return base renderscript object described by the entry
         */
        public android.renderscript.BaseObj getObject() {
            mRS.validate();
            android.renderscript.BaseObj obj = android.renderscript.FileA3D.IndexEntry.internalCreate(mRS, this);
            return obj;
        }

        /**
         *
         *
         * @deprecated in API 16
        Used to load the mesh described by the index entry, object
        described by the index entry must be a renderscript mesh
         * @return renderscript mesh object described by the entry
         */
        public android.renderscript.Mesh getMesh() {
            return ((android.renderscript.Mesh) (getObject()));
        }

        static synchronized android.renderscript.BaseObj internalCreate(android.renderscript.RenderScript rs, android.renderscript.FileA3D.IndexEntry entry) {
            if (entry.mLoadedObj != null) {
                return entry.mLoadedObj;
            }
            // to be purged on cleanup
            if (entry.mEntryType == android.renderscript.FileA3D.EntryType.UNKNOWN) {
                return null;
            }
            long objectID = rs.nFileA3DGetEntryByIndex(entry.mID, entry.mIndex);
            if (objectID == 0) {
                return null;
            }
            switch (entry.mEntryType) {
                case MESH :
                    entry.mLoadedObj = new android.renderscript.Mesh(objectID, rs);
                    break;
                default :
                    throw new android.renderscript.RSRuntimeException("Unrecognized object type in file.");
            }
            entry.mLoadedObj.updateFromNative();
            return entry.mLoadedObj;
        }

        IndexEntry(android.renderscript.RenderScript rs, int index, long id, java.lang.String name, android.renderscript.FileA3D.EntryType type) {
            mRS = rs;
            mIndex = index;
            mID = id;
            mName = name;
            mEntryType = type;
            mLoadedObj = null;
        }
    }

    android.renderscript.FileA3D.IndexEntry[] mFileEntries;

    java.io.InputStream mInputStream;

    FileA3D(long id, android.renderscript.RenderScript rs, java.io.InputStream stream) {
        super(id, rs);
        mInputStream = stream;
        guard.open("destroy");
    }

    private void initEntries() {
        int numFileEntries = mRS.nFileA3DGetNumIndexEntries(getID(mRS));
        if (numFileEntries <= 0) {
            return;
        }
        mFileEntries = new android.renderscript.FileA3D.IndexEntry[numFileEntries];
        int[] ids = new int[numFileEntries];
        java.lang.String[] names = new java.lang.String[numFileEntries];
        mRS.nFileA3DGetIndexEntries(getID(mRS), numFileEntries, ids, names);
        for (int i = 0; i < numFileEntries; i++) {
            mFileEntries[i] = new android.renderscript.FileA3D.IndexEntry(mRS, i, getID(mRS), names[i], android.renderscript.FileA3D.EntryType.toEntryType(ids[i]));
        }
    }

    /**
     *
     *
     * @deprecated in API 16
    Returns the number of objects stored inside the a3d file
     * @return the number of objects stored inside the a3d file
     */
    public int getIndexEntryCount() {
        if (mFileEntries == null) {
            return 0;
        }
        return mFileEntries.length;
    }

    /**
     *
     *
     * @deprecated in API 16
    Returns an index entry from the list of all objects inside
    FileA3D
     * @param index
     * 		number of the entry from the list to return
     * @return entry in the a3d file described by the index
     */
    public android.renderscript.FileA3D.IndexEntry getIndexEntry(int index) {
        if (((getIndexEntryCount() == 0) || (index < 0)) || (index >= mFileEntries.length)) {
            return null;
        }
        return mFileEntries[index];
    }

    /**
     *
     *
     * @deprecated in API 16
    Creates a FileA3D object from an asset stored on disk
     * @param rs
     * 		Context to which the object will belong.
     * @param mgr
     * 		asset manager used to load asset
     * @param path
     * 		location of the file to load
     * @return a3d file containing renderscript objects
     */
    public static android.renderscript.FileA3D createFromAsset(android.renderscript.RenderScript rs, android.content.res.AssetManager mgr, java.lang.String path) {
        rs.validate();
        long fileId = rs.nFileA3DCreateFromAsset(mgr, path);
        if (fileId == 0) {
            throw new android.renderscript.RSRuntimeException("Unable to create a3d file from asset " + path);
        }
        android.renderscript.FileA3D fa3d = new android.renderscript.FileA3D(fileId, rs, null);
        fa3d.initEntries();
        return fa3d;
    }

    /**
     *
     *
     * @deprecated in API 16
    Creates a FileA3D object from a file stored on disk
     * @param rs
     * 		Context to which the object will belong.
     * @param path
     * 		location of the file to load
     * @return a3d file containing renderscript objects
     */
    public static android.renderscript.FileA3D createFromFile(android.renderscript.RenderScript rs, java.lang.String path) {
        long fileId = rs.nFileA3DCreateFromFile(path);
        if (fileId == 0) {
            throw new android.renderscript.RSRuntimeException("Unable to create a3d file from " + path);
        }
        android.renderscript.FileA3D fa3d = new android.renderscript.FileA3D(fileId, rs, null);
        fa3d.initEntries();
        return fa3d;
    }

    /**
     *
     *
     * @deprecated in API 16
    Creates a FileA3D object from a file stored on disk
     * @param rs
     * 		Context to which the object will belong.
     * @param path
     * 		location of the file to load
     * @return a3d file containing renderscript objects
     */
    public static android.renderscript.FileA3D createFromFile(android.renderscript.RenderScript rs, java.io.File path) {
        return android.renderscript.FileA3D.createFromFile(rs, path.getAbsolutePath());
    }

    /**
     *
     *
     * @deprecated in API 16
    Creates a FileA3D object from an application resource
     * @param rs
     * 		Context to which the object will belong.
     * @param res
     * 		resource manager used for loading
     * @param id
     * 		resource to create FileA3D from
     * @return a3d file containing renderscript objects
     */
    public static android.renderscript.FileA3D createFromResource(android.renderscript.RenderScript rs, android.content.res.Resources res, int id) {
        rs.validate();
        java.io.InputStream is = null;
        try {
            is = res.openRawResource(id);
        } catch (java.lang.Exception e) {
            throw new android.renderscript.RSRuntimeException("Unable to open resource " + id);
        }
        long fileId = 0;
        if (is instanceof android.content.res.AssetManager.AssetInputStream) {
            long asset = ((android.content.res.AssetManager.AssetInputStream) (is)).getNativeAsset();
            fileId = rs.nFileA3DCreateFromAssetStream(asset);
        } else {
            throw new android.renderscript.RSRuntimeException("Unsupported asset stream");
        }
        if (fileId == 0) {
            throw new android.renderscript.RSRuntimeException("Unable to create a3d file from resource " + id);
        }
        android.renderscript.FileA3D fa3d = new android.renderscript.FileA3D(fileId, rs, is);
        fa3d.initEntries();
        return fa3d;
    }
}

