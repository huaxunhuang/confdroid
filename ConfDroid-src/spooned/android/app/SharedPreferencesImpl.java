/**
 * Copyright (C) 2010 The Android Open Source Project
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
package android.app;


final class SharedPreferencesImpl implements android.content.SharedPreferences {
    private static final java.lang.String TAG = "SharedPreferencesImpl";

    private static final boolean DEBUG = false;

    // Lock ordering rules:
    // - acquire SharedPreferencesImpl.this before EditorImpl.this
    // - acquire mWritingToDiskLock before EditorImpl.this
    private final java.io.File mFile;

    private final java.io.File mBackupFile;

    private final int mMode;

    private java.util.Map<java.lang.String, java.lang.Object> mMap;// guarded by 'this'


    private int mDiskWritesInFlight = 0;// guarded by 'this'


    private boolean mLoaded = false;// guarded by 'this'


    private long mStatTimestamp;// guarded by 'this'


    private long mStatSize;// guarded by 'this'


    private final java.lang.Object mWritingToDiskLock = new java.lang.Object();

    private static final java.lang.Object mContent = new java.lang.Object();

    private final java.util.WeakHashMap<android.content.SharedPreferences.OnSharedPreferenceChangeListener, java.lang.Object> mListeners = new java.util.WeakHashMap<android.content.SharedPreferences.OnSharedPreferenceChangeListener, java.lang.Object>();

    SharedPreferencesImpl(java.io.File file, int mode) {
        mFile = file;
        mBackupFile = android.app.SharedPreferencesImpl.makeBackupFile(file);
        mMode = mode;
        mLoaded = false;
        mMap = null;
        startLoadFromDisk();
    }

    private void startLoadFromDisk() {
        synchronized(this) {
            mLoaded = false;
        }
        new java.lang.Thread("SharedPreferencesImpl-load") {
            public void run() {
                loadFromDisk();
            }
        }.start();
    }

    private void loadFromDisk() {
        synchronized(this) {
            if (mLoaded) {
                return;
            }
            if (mBackupFile.exists()) {
                mFile.delete();
                mBackupFile.renameTo(mFile);
            }
        }
        // Debugging
        if (mFile.exists() && (!mFile.canRead())) {
            android.util.Log.w(android.app.SharedPreferencesImpl.TAG, ("Attempt to read preferences file " + mFile) + " without permission");
        }
        java.util.Map map = null;
        android.system.StructStat stat = null;
        try {
            stat = android.system.Os.stat(mFile.getPath());
            if (mFile.canRead()) {
                java.io.BufferedInputStream str = null;
                try {
                    str = new java.io.BufferedInputStream(new java.io.FileInputStream(mFile), 16 * 1024);
                    map = com.android.internal.util.XmlUtils.readMapXml(str);
                } catch (org.xmlpull.v1.XmlPullParserException | java.io.IOException e) {
                    android.util.Log.w(android.app.SharedPreferencesImpl.TAG, "getSharedPreferences", e);
                } finally {
                    libcore.io.IoUtils.closeQuietly(str);
                }
            }
        } catch (android.system.ErrnoException e) {
            /* ignore */
        }
        synchronized(this) {
            mLoaded = true;
            if (map != null) {
                mMap = map;
                mStatTimestamp = stat.st_mtime;
                mStatSize = stat.st_size;
            } else {
                mMap = new java.util.HashMap<>();
            }
            notifyAll();
        }
    }

    static java.io.File makeBackupFile(java.io.File prefsFile) {
        return new java.io.File(prefsFile.getPath() + ".bak");
    }

    void startReloadIfChangedUnexpectedly() {
        synchronized(this) {
            // TODO: wait for any pending writes to disk?
            if (!hasFileChangedUnexpectedly()) {
                return;
            }
            startLoadFromDisk();
        }
    }

    // Has the file changed out from under us?  i.e. writes that
    // we didn't instigate.
    private boolean hasFileChangedUnexpectedly() {
        synchronized(this) {
            if (mDiskWritesInFlight > 0) {
                // If we know we caused it, it's not unexpected.
                if (android.app.SharedPreferencesImpl.DEBUG)
                    android.util.Log.d(android.app.SharedPreferencesImpl.TAG, "disk write in flight, not unexpected.");

                return false;
            }
        }
        final android.system.StructStat stat;
        try {
            /* Metadata operations don't usually count as a block guard
            violation, but we explicitly want this one.
             */
            dalvik.system.BlockGuard.getThreadPolicy().onReadFromDisk();
            stat = android.system.Os.stat(mFile.getPath());
        } catch (android.system.ErrnoException e) {
            return true;
        }
        synchronized(this) {
            return (mStatTimestamp != stat.st_mtime) || (mStatSize != stat.st_size);
        }
    }

    public void registerOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
        synchronized(this) {
            mListeners.put(listener, android.app.SharedPreferencesImpl.mContent);
        }
    }

    public void unregisterOnSharedPreferenceChangeListener(android.content.SharedPreferences.OnSharedPreferenceChangeListener listener) {
        synchronized(this) {
            mListeners.remove(listener);
        }
    }

    private void awaitLoadedLocked() {
        if (!mLoaded) {
            // Raise an explicit StrictMode onReadFromDisk for this
            // thread, since the real read will be in a different
            // thread and otherwise ignored by StrictMode.
            dalvik.system.BlockGuard.getThreadPolicy().onReadFromDisk();
        }
        while (!mLoaded) {
            try {
                wait();
            } catch (java.lang.InterruptedException unused) {
            }
        } 
    }

    public java.util.Map<java.lang.String, ?> getAll() {
        synchronized(this) {
            awaitLoadedLocked();
            // noinspection unchecked
            return new java.util.HashMap<java.lang.String, java.lang.Object>(mMap);
        }
    }

    @android.annotation.Nullable
    public java.lang.String getString(java.lang.String key, @android.annotation.Nullable
    java.lang.String defValue) {
        synchronized(this) {
            awaitLoadedLocked();
            java.lang.String v = ((java.lang.String) (mMap.get(key)));
            return v != null ? v : defValue;
        }
    }

    @android.annotation.Nullable
    public java.util.Set<java.lang.String> getStringSet(java.lang.String key, @android.annotation.Nullable
    java.util.Set<java.lang.String> defValues) {
        synchronized(this) {
            awaitLoadedLocked();
            java.util.Set<java.lang.String> v = ((java.util.Set<java.lang.String>) (mMap.get(key)));
            return v != null ? v : defValues;
        }
    }

    public int getInt(java.lang.String key, int defValue) {
        synchronized(this) {
            awaitLoadedLocked();
            java.lang.Integer v = ((java.lang.Integer) (mMap.get(key)));
            return v != null ? v : defValue;
        }
    }

    public long getLong(java.lang.String key, long defValue) {
        synchronized(this) {
            awaitLoadedLocked();
            java.lang.Long v = ((java.lang.Long) (mMap.get(key)));
            return v != null ? v : defValue;
        }
    }

    public float getFloat(java.lang.String key, float defValue) {
        synchronized(this) {
            awaitLoadedLocked();
            java.lang.Float v = ((java.lang.Float) (mMap.get(key)));
            return v != null ? v : defValue;
        }
    }

    public boolean getBoolean(java.lang.String key, boolean defValue) {
        synchronized(this) {
            awaitLoadedLocked();
            java.lang.Boolean v = ((java.lang.Boolean) (mMap.get(key)));
            return v != null ? v : defValue;
        }
    }

    public boolean contains(java.lang.String key) {
        synchronized(this) {
            awaitLoadedLocked();
            return mMap.containsKey(key);
        }
    }

    public android.content.SharedPreferences.Editor edit() {
        // TODO: remove the need to call awaitLoadedLocked() when
        // requesting an editor.  will require some work on the
        // Editor, but then we should be able to do:
        // 
        // context.getSharedPreferences(..).edit().putString(..).apply()
        // 
        // ... all without blocking.
        synchronized(this) {
            awaitLoadedLocked();
        }
        return new android.app.SharedPreferencesImpl.EditorImpl();
    }

    // Return value from EditorImpl#commitToMemory()
    private static class MemoryCommitResult {
        public boolean changesMade;// any keys different?


        public java.util.List<java.lang.String> keysModified;// may be null


        public java.util.Set<android.content.SharedPreferences.OnSharedPreferenceChangeListener> listeners;// may be null


        public java.util.Map<?, ?> mapToWriteToDisk;

        public final java.util.concurrent.CountDownLatch writtenToDiskLatch = new java.util.concurrent.CountDownLatch(1);

        public volatile boolean writeToDiskResult = false;

        public void setDiskWriteResult(boolean result) {
            writeToDiskResult = result;
            writtenToDiskLatch.countDown();
        }
    }

    public final class EditorImpl implements android.content.SharedPreferences.Editor {
        private final java.util.Map<java.lang.String, java.lang.Object> mModified = com.google.android.collect.Maps.newHashMap();

        private boolean mClear = false;

        public android.content.SharedPreferences.Editor putString(java.lang.String key, @android.annotation.Nullable
        java.lang.String value) {
            synchronized(this) {
                mModified.put(key, value);
                return this;
            }
        }

        public android.content.SharedPreferences.Editor putStringSet(java.lang.String key, @android.annotation.Nullable
        java.util.Set<java.lang.String> values) {
            synchronized(this) {
                mModified.put(key, values == null ? null : new java.util.HashSet<java.lang.String>(values));
                return this;
            }
        }

        public android.content.SharedPreferences.Editor putInt(java.lang.String key, int value) {
            synchronized(this) {
                mModified.put(key, value);
                return this;
            }
        }

        public android.content.SharedPreferences.Editor putLong(java.lang.String key, long value) {
            synchronized(this) {
                mModified.put(key, value);
                return this;
            }
        }

        public android.content.SharedPreferences.Editor putFloat(java.lang.String key, float value) {
            synchronized(this) {
                mModified.put(key, value);
                return this;
            }
        }

        public android.content.SharedPreferences.Editor putBoolean(java.lang.String key, boolean value) {
            synchronized(this) {
                mModified.put(key, value);
                return this;
            }
        }

        public android.content.SharedPreferences.Editor remove(java.lang.String key) {
            synchronized(this) {
                mModified.put(key, this);
                return this;
            }
        }

        public android.content.SharedPreferences.Editor clear() {
            synchronized(this) {
                mClear = true;
                return this;
            }
        }

        public void apply() {
            final android.app.SharedPreferencesImpl.MemoryCommitResult mcr = commitToMemory();
            final java.lang.Runnable awaitCommit = new java.lang.Runnable() {
                public void run() {
                    try {
                        mcr.writtenToDiskLatch.await();
                    } catch (java.lang.InterruptedException ignored) {
                    }
                }
            };
            android.app.QueuedWork.add(awaitCommit);
            java.lang.Runnable postWriteRunnable = new java.lang.Runnable() {
                public void run() {
                    awaitCommit.run();
                    android.app.QueuedWork.remove(awaitCommit);
                }
            };
            android.app.SharedPreferencesImpl.this.enqueueDiskWrite(mcr, postWriteRunnable);
            // Okay to notify the listeners before it's hit disk
            // because the listeners should always get the same
            // SharedPreferences instance back, which has the
            // changes reflected in memory.
            notifyListeners(mcr);
        }

        // Returns true if any changes were made
        private android.app.SharedPreferencesImpl.MemoryCommitResult commitToMemory() {
            android.app.SharedPreferencesImpl.MemoryCommitResult mcr = new android.app.SharedPreferencesImpl.MemoryCommitResult();
            synchronized(android.app.SharedPreferencesImpl.this) {
                // We optimistically don't make a deep copy until
                // a memory commit comes in when we're already
                // writing to disk.
                if (mDiskWritesInFlight > 0) {
                    // We can't modify our mMap as a currently
                    // in-flight write owns it.  Clone it before
                    // modifying it.
                    // noinspection unchecked
                    mMap = new java.util.HashMap<java.lang.String, java.lang.Object>(mMap);
                }
                mcr.mapToWriteToDisk = mMap;
                mDiskWritesInFlight++;
                boolean hasListeners = mListeners.size() > 0;
                if (hasListeners) {
                    mcr.keysModified = new java.util.ArrayList<java.lang.String>();
                    mcr.listeners = new java.util.HashSet<android.content.SharedPreferences.OnSharedPreferenceChangeListener>(mListeners.keySet());
                }
                synchronized(this) {
                    if (mClear) {
                        if (!mMap.isEmpty()) {
                            mcr.changesMade = true;
                            mMap.clear();
                        }
                        mClear = false;
                    }
                    for (java.util.Map.Entry<java.lang.String, java.lang.Object> e : mModified.entrySet()) {
                        java.lang.String k = e.getKey();
                        java.lang.Object v = e.getValue();
                        // "this" is the magic value for a removal mutation. In addition,
                        // setting a value to "null" for a given key is specified to be
                        // equivalent to calling remove on that key.
                        if ((v == this) || (v == null)) {
                            if (!mMap.containsKey(k)) {
                                continue;
                            }
                            mMap.remove(k);
                        } else {
                            if (mMap.containsKey(k)) {
                                java.lang.Object existingValue = mMap.get(k);
                                if ((existingValue != null) && existingValue.equals(v)) {
                                    continue;
                                }
                            }
                            mMap.put(k, v);
                        }
                        mcr.changesMade = true;
                        if (hasListeners) {
                            mcr.keysModified.add(k);
                        }
                    }
                    mModified.clear();
                }
            }
            return mcr;
        }

        public boolean commit() {
            android.app.SharedPreferencesImpl.MemoryCommitResult mcr = commitToMemory();
            /* sync write on this thread okay */
            android.app.SharedPreferencesImpl.this.enqueueDiskWrite(mcr, null);
            try {
                mcr.writtenToDiskLatch.await();
            } catch (java.lang.InterruptedException e) {
                return false;
            }
            notifyListeners(mcr);
            return mcr.writeToDiskResult;
        }

        private void notifyListeners(final android.app.SharedPreferencesImpl.MemoryCommitResult mcr) {
            if (((mcr.listeners == null) || (mcr.keysModified == null)) || (mcr.keysModified.size() == 0)) {
                return;
            }
            if (android.os.Looper.myLooper() == android.os.Looper.getMainLooper()) {
                for (int i = mcr.keysModified.size() - 1; i >= 0; i--) {
                    final java.lang.String key = mcr.keysModified.get(i);
                    for (android.content.SharedPreferences.OnSharedPreferenceChangeListener listener : mcr.listeners) {
                        if (listener != null) {
                            listener.onSharedPreferenceChanged(android.app.SharedPreferencesImpl.this, key);
                        }
                    }
                }
            } else {
                // Run this function on the main thread.
                android.app.ActivityThread.sMainThreadHandler.post(new java.lang.Runnable() {
                    public void run() {
                        notifyListeners(mcr);
                    }
                });
            }
        }
    }

    /**
     * Enqueue an already-committed-to-memory result to be written
     * to disk.
     *
     * They will be written to disk one-at-a-time in the order
     * that they're enqueued.
     *
     * @param postWriteRunnable
     * 		if non-null, we're being called
     * 		from apply() and this is the runnable to run after
     * 		the write proceeds.  if null (from a regular commit()),
     * 		then we're allowed to do this disk write on the main
     * 		thread (which in addition to reducing allocations and
     * 		creating a background thread, this has the advantage that
     * 		we catch them in userdebug StrictMode reports to convert
     * 		them where possible to apply() ...)
     */
    private void enqueueDiskWrite(final android.app.SharedPreferencesImpl.MemoryCommitResult mcr, final java.lang.Runnable postWriteRunnable) {
        final java.lang.Runnable writeToDiskRunnable = new java.lang.Runnable() {
            public void run() {
                synchronized(mWritingToDiskLock) {
                    writeToFile(mcr);
                }
                synchronized(android.app.SharedPreferencesImpl.this) {
                    mDiskWritesInFlight--;
                }
                if (postWriteRunnable != null) {
                    postWriteRunnable.run();
                }
            }
        };
        final boolean isFromSyncCommit = postWriteRunnable == null;
        // Typical #commit() path with fewer allocations, doing a write on
        // the current thread.
        if (isFromSyncCommit) {
            boolean wasEmpty = false;
            synchronized(this) {
                wasEmpty = mDiskWritesInFlight == 1;
            }
            if (wasEmpty) {
                writeToDiskRunnable.run();
                return;
            }
        }
        android.app.QueuedWork.singleThreadExecutor().execute(writeToDiskRunnable);
    }

    private static java.io.FileOutputStream createFileOutputStream(java.io.File file) {
        java.io.FileOutputStream str = null;
        try {
            str = new java.io.FileOutputStream(file);
        } catch (java.io.FileNotFoundException e) {
            java.io.File parent = file.getParentFile();
            if (!parent.mkdir()) {
                android.util.Log.e(android.app.SharedPreferencesImpl.TAG, "Couldn't create directory for SharedPreferences file " + file);
                return null;
            }
            android.os.FileUtils.setPermissions(parent.getPath(), (android.os.FileUtils.S_IRWXU | android.os.FileUtils.S_IRWXG) | android.os.FileUtils.S_IXOTH, -1, -1);
            try {
                str = new java.io.FileOutputStream(file);
            } catch (java.io.FileNotFoundException e2) {
                android.util.Log.e(android.app.SharedPreferencesImpl.TAG, "Couldn't create SharedPreferences file " + file, e2);
            }
        }
        return str;
    }

    // Note: must hold mWritingToDiskLock
    private void writeToFile(android.app.SharedPreferencesImpl.MemoryCommitResult mcr) {
        // Rename the current file so it may be used as a backup during the next read
        if (mFile.exists()) {
            if (!mcr.changesMade) {
                // If the file already exists, but no changes were
                // made to the underlying map, it's wasteful to
                // re-write the file.  Return as if we wrote it
                // out.
                mcr.setDiskWriteResult(true);
                return;
            }
            if (!mBackupFile.exists()) {
                if (!mFile.renameTo(mBackupFile)) {
                    android.util.Log.e(android.app.SharedPreferencesImpl.TAG, (("Couldn't rename file " + mFile) + " to backup file ") + mBackupFile);
                    mcr.setDiskWriteResult(false);
                    return;
                }
            } else {
                mFile.delete();
            }
        }
        // Attempt to write the file, delete the backup and return true as atomically as
        // possible.  If any exception occurs, delete the new file; next time we will restore
        // from the backup.
        try {
            java.io.FileOutputStream str = android.app.SharedPreferencesImpl.createFileOutputStream(mFile);
            if (str == null) {
                mcr.setDiskWriteResult(false);
                return;
            }
            com.android.internal.util.XmlUtils.writeMapXml(mcr.mapToWriteToDisk, str);
            android.os.FileUtils.sync(str);
            str.close();
            android.app.ContextImpl.setFilePermissionsFromMode(mFile.getPath(), mMode, 0);
            try {
                final android.system.StructStat stat = android.system.Os.stat(mFile.getPath());
                synchronized(this) {
                    mStatTimestamp = stat.st_mtime;
                    mStatSize = stat.st_size;
                }
            } catch (android.system.ErrnoException e) {
                // Do nothing
            }
            // Writing was successful, delete the backup file if there is one.
            mBackupFile.delete();
            mcr.setDiskWriteResult(true);
            return;
        } catch (org.xmlpull.v1.XmlPullParserException e) {
            android.util.Log.w(android.app.SharedPreferencesImpl.TAG, "writeToFile: Got exception:", e);
        } catch (java.io.IOException e) {
            android.util.Log.w(android.app.SharedPreferencesImpl.TAG, "writeToFile: Got exception:", e);
        }
        // Clean up an unsuccessfully written file
        if (mFile.exists()) {
            if (!mFile.delete()) {
                android.util.Log.e(android.app.SharedPreferencesImpl.TAG, "Couldn't clean up partially-written file " + mFile);
            }
        }
        mcr.setDiskWriteResult(false);
    }
}

