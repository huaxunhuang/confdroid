/**
 * Copyright (C) 2009 The Android Open Source Project
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
package android.gesture;


public final class GestureLibraries {
    private GestureLibraries() {
    }

    public static android.gesture.GestureLibrary fromFile(java.lang.String path) {
        return android.gesture.GestureLibraries.fromFile(new java.io.File(path));
    }

    public static android.gesture.GestureLibrary fromFile(java.io.File path) {
        return new android.gesture.GestureLibraries.FileGestureLibrary(path);
    }

    public static android.gesture.GestureLibrary fromPrivateFile(android.content.Context context, java.lang.String name) {
        return android.gesture.GestureLibraries.fromFile(context.getFileStreamPath(name));
    }

    public static android.gesture.GestureLibrary fromRawResource(android.content.Context context, @android.annotation.RawRes
    int resourceId) {
        return new android.gesture.GestureLibraries.ResourceGestureLibrary(context, resourceId);
    }

    private static class FileGestureLibrary extends android.gesture.GestureLibrary {
        private final java.io.File mPath;

        public FileGestureLibrary(java.io.File path) {
            mPath = path;
        }

        @java.lang.Override
        public boolean isReadOnly() {
            return !mPath.canWrite();
        }

        public boolean save() {
            if (!mStore.hasChanged())
                return true;

            final java.io.File file = mPath;
            final java.io.File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    return false;
                }
            }
            boolean result = false;
            try {
                // noinspection ResultOfMethodCallIgnored
                file.createNewFile();
                mStore.save(new java.io.FileOutputStream(file), true);
                result = true;
            } catch (java.io.FileNotFoundException e) {
                android.util.Log.d(android.gesture.GestureConstants.LOG_TAG, "Could not save the gesture library in " + mPath, e);
            } catch (java.io.IOException e) {
                android.util.Log.d(android.gesture.GestureConstants.LOG_TAG, "Could not save the gesture library in " + mPath, e);
            }
            return result;
        }

        public boolean load() {
            boolean result = false;
            final java.io.File file = mPath;
            if (file.exists() && file.canRead()) {
                try {
                    mStore.load(new java.io.FileInputStream(file), true);
                    result = true;
                } catch (java.io.FileNotFoundException e) {
                    android.util.Log.d(android.gesture.GestureConstants.LOG_TAG, "Could not load the gesture library from " + mPath, e);
                } catch (java.io.IOException e) {
                    android.util.Log.d(android.gesture.GestureConstants.LOG_TAG, "Could not load the gesture library from " + mPath, e);
                }
            }
            return result;
        }
    }

    private static class ResourceGestureLibrary extends android.gesture.GestureLibrary {
        private final java.lang.ref.WeakReference<android.content.Context> mContext;

        private final int mResourceId;

        public ResourceGestureLibrary(android.content.Context context, int resourceId) {
            mContext = new java.lang.ref.WeakReference<android.content.Context>(context);
            mResourceId = resourceId;
        }

        @java.lang.Override
        public boolean isReadOnly() {
            return true;
        }

        public boolean save() {
            return false;
        }

        public boolean load() {
            boolean result = false;
            final android.content.Context context = mContext.get();
            if (context != null) {
                final java.io.InputStream in = context.getResources().openRawResource(mResourceId);
                try {
                    mStore.load(in, true);
                    result = true;
                } catch (java.io.IOException e) {
                    android.util.Log.d(android.gesture.GestureConstants.LOG_TAG, "Could not load the gesture library from raw resource " + context.getResources().getResourceName(mResourceId), e);
                }
            }
            return result;
        }
    }
}

