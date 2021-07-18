package android.app.backup;


/**
 * Provides the interface through which a {@link BackupAgent} writes entire files
 * to a full backup data set, via its {@link BackupAgent#onFullBackup(FullBackupDataOutput)}
 * method.
 */
public class FullBackupDataOutput {
    // Currently a name-scoping shim around BackupDataOutput
    private final android.app.backup.BackupDataOutput mData;

    private long mSize;

    /**
     *
     *
     * @unknown - used only in measure operation
     */
    public FullBackupDataOutput() {
        mData = null;
        mSize = 0;
    }

    /**
     *
     *
     * @unknown 
     */
    public FullBackupDataOutput(android.os.ParcelFileDescriptor fd) {
        mData = new android.app.backup.BackupDataOutput(fd.getFileDescriptor());
    }

    /**
     *
     *
     * @unknown 
     */
    public android.app.backup.BackupDataOutput getData() {
        return mData;
    }

    /**
     *
     *
     * @unknown - used for measurement pass
     */
    public void addSize(long size) {
        if (size > 0) {
            mSize += size;
        }
    }

    /**
     *
     *
     * @unknown - used for measurement pass
     */
    public long getSize() {
        return mSize;
    }
}

