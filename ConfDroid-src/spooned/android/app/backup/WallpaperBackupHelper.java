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
package android.app.backup;


/**
 * Helper for backing up / restoring wallpapers.  Basically an AbsoluteFileBackupHelper,
 * but with logic for deciding what to do with restored wallpaper images.
 *
 * @unknown 
 */
public class WallpaperBackupHelper extends android.app.backup.FileBackupHelperBase implements android.app.backup.BackupHelper {
    private static final java.lang.String TAG = "WallpaperBackupHelper";

    private static final boolean DEBUG = false;

    // If 'true', then apply an acceptable-size heuristic at restore time, dropping back
    // to the factory default wallpaper if the restored one differs "too much" from the
    // device's preferred wallpaper image dimensions.
    private static final boolean REJECT_OUTSIZED_RESTORE = false;

    // When outsized restore rejection is enabled, this is the maximum ratio between the
    // source and target image heights that will be permitted.  The ratio is checked both
    // ways (i.e. >= MAX, or <= 1/MAX) to validate restores from both largeer-than-target
    // and smaller-than-target sources.
    private static final double MAX_HEIGHT_RATIO = 1.35;

    // The height ratio check when applying larger images on smaller screens is separate;
    // in current policy we accept any such restore regardless of the relative dimensions.
    private static final double MIN_HEIGHT_RATIO = 0;

    // This path must match what the WallpaperManagerService uses
    // TODO: Will need to change if backing up non-primary user's wallpaper
    // http://b/22388012
    public static final java.lang.String WALLPAPER_IMAGE = new java.io.File(android.os.Environment.getUserSystemDirectory(android.os.UserHandle.USER_SYSTEM), "wallpaper").getAbsolutePath();

    public static final java.lang.String WALLPAPER_ORIG_IMAGE = new java.io.File(android.os.Environment.getUserSystemDirectory(android.os.UserHandle.USER_SYSTEM), "wallpaper_orig").getAbsolutePath();

    public static final java.lang.String WALLPAPER_INFO = new java.io.File(android.os.Environment.getUserSystemDirectory(android.os.UserHandle.USER_SYSTEM), "wallpaper_info.xml").getAbsolutePath();

    // Use old keys to keep legacy data compatibility and avoid writing two wallpapers
    public static final java.lang.String WALLPAPER_IMAGE_KEY = "/data/data/com.android.settings/files/wallpaper";

    public static final java.lang.String WALLPAPER_INFO_KEY = "/data/system/wallpaper_info.xml";

    // Stage file - should be adjacent to the WALLPAPER_IMAGE location.  The wallpapers
    // will be saved to this file from the restore stream, then renamed to the proper
    // location if it's deemed suitable.
    // TODO: Will need to change if backing up non-primary user's wallpaper
    // http://b/22388012
    private static final java.lang.String STAGE_FILE = new java.io.File(android.os.Environment.getUserSystemDirectory(android.os.UserHandle.USER_SYSTEM), "wallpaper-tmp").getAbsolutePath();

    android.content.Context mContext;

    java.lang.String[] mFiles;

    java.lang.String[] mKeys;

    double mDesiredMinWidth;

    double mDesiredMinHeight;

    /**
     * Construct a helper for backing up / restoring the files at the given absolute locations
     * within the file system.
     *
     * @param context
     * 		
     * @param files
     * 		
     */
    public WallpaperBackupHelper(android.content.Context context, java.lang.String[] files, java.lang.String[] keys) {
        super(context);
        mContext = context;
        mFiles = files;
        mKeys = keys;
        final android.view.WindowManager wm = ((android.view.WindowManager) (context.getSystemService(android.content.Context.WINDOW_SERVICE)));
        final android.app.WallpaperManager wpm = ((android.app.WallpaperManager) (context.getSystemService(android.content.Context.WALLPAPER_SERVICE)));
        final android.view.Display d = wm.getDefaultDisplay();
        final android.graphics.Point size = new android.graphics.Point();
        d.getSize(size);
        mDesiredMinWidth = java.lang.Math.min(size.x, size.y);
        mDesiredMinHeight = ((double) (wpm.getDesiredMinimumHeight()));
        if (mDesiredMinHeight <= 0) {
            mDesiredMinHeight = size.y;
        }
        if (android.app.backup.WallpaperBackupHelper.DEBUG) {
            android.util.Slog.d(android.app.backup.WallpaperBackupHelper.TAG, (("dmW=" + mDesiredMinWidth) + " dmH=") + mDesiredMinHeight);
        }
    }

    /**
     * Based on oldState, determine which of the files from the application's data directory
     * need to be backed up, write them to the data stream, and fill in newState with the
     * state as it exists now.
     */
    @java.lang.Override
    public void performBackup(android.os.ParcelFileDescriptor oldState, android.app.backup.BackupDataOutput data, android.os.ParcelFileDescriptor newState) {
        android.app.backup.FileBackupHelperBase.performBackup_checked(oldState, data, newState, mFiles, mKeys);
    }

    /**
     * Restore one absolute file entity from the restore stream.  If we're restoring the
     * magic wallpaper file, take specific action to determine whether it is suitable for
     * the current device.
     */
    @java.lang.Override
    public void restoreEntity(android.app.backup.BackupDataInputStream data) {
        final java.lang.String key = data.getKey();
        if (isKeyInList(key, mKeys)) {
            if (key.equals(android.app.backup.WallpaperBackupHelper.WALLPAPER_IMAGE_KEY)) {
                // restore the file to the stage for inspection
                java.io.File f = new java.io.File(android.app.backup.WallpaperBackupHelper.STAGE_FILE);
                if (writeFile(f, data)) {
                    // Preflight the restored image's dimensions without loading it
                    android.graphics.BitmapFactory.Options options = new android.graphics.BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    android.graphics.BitmapFactory.decodeFile(android.app.backup.WallpaperBackupHelper.STAGE_FILE, options);
                    if (android.app.backup.WallpaperBackupHelper.DEBUG)
                        android.util.Slog.d(android.app.backup.WallpaperBackupHelper.TAG, (("Restoring wallpaper image w=" + options.outWidth) + " h=") + options.outHeight);

                    if (android.app.backup.WallpaperBackupHelper.REJECT_OUTSIZED_RESTORE) {
                        // We accept any wallpaper that is at least as wide as our preference
                        // (i.e. wide enough to fill the screen), and is within a comfortable
                        // factor of the target height, to avoid significant clipping/scaling/
                        // letterboxing.  At this point we know that mDesiredMinWidth is the
                        // smallest dimension, regardless of current orientation, so we can
                        // safely require that the candidate's width and height both exceed
                        // that hard minimum.
                        final double heightRatio = mDesiredMinHeight / options.outHeight;
                        if ((((options.outWidth < mDesiredMinWidth) || (options.outHeight < mDesiredMinWidth)) || (heightRatio >= android.app.backup.WallpaperBackupHelper.MAX_HEIGHT_RATIO)) || (heightRatio <= android.app.backup.WallpaperBackupHelper.MIN_HEIGHT_RATIO)) {
                            // Not wide enough for the screen, or too short/tall to be a good fit
                            // for the height of the screen, broken image file, or the system's
                            // desires for wallpaper size are in a bad state.  Probably one of the
                            // first two.
                            android.util.Slog.i(android.app.backup.WallpaperBackupHelper.TAG, ((((((("Restored image dimensions (w=" + options.outWidth) + ", h=") + options.outHeight) + ") too far off target (tw=") + mDesiredMinWidth) + ", th=") + mDesiredMinHeight) + "); falling back to default wallpaper.");
                            f.delete();
                            return;
                        }
                    }
                    // We passed the acceptable-dimensions test (if any), so we're going to
                    // use the restored image.  That comes last, when we are done restoring
                    // both the pixels and the metadata.
                }
            } else
                if (key.equals(android.app.backup.WallpaperBackupHelper.WALLPAPER_INFO_KEY)) {
                    // XML file containing wallpaper info
                    java.io.File f = new java.io.File(android.app.backup.WallpaperBackupHelper.WALLPAPER_INFO);
                    writeFile(f, data);
                }

        }
    }

    /**
     * Hook for the agent to call this helper upon completion of the restore.  We do this
     * upon completion so that we know both the imagery and the wallpaper info have
     * been emplaced without requiring either or relying on ordering.
     */
    public void onRestoreFinished() {
        final java.io.File f = new java.io.File(android.app.backup.WallpaperBackupHelper.STAGE_FILE);
        if (f.exists()) {
            // TODO: spin a service to copy the restored image to sd/usb storage,
            // since it does not exist anywhere other than the private wallpaper
            // file.
            android.util.Slog.d(android.app.backup.WallpaperBackupHelper.TAG, "Applying restored wallpaper image.");
            f.renameTo(new java.io.File(android.app.backup.WallpaperBackupHelper.WALLPAPER_ORIG_IMAGE));
        }
    }
}

