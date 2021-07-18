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
package android.webkit;


/**
 * Represents a plugin (Java equivalent of the PluginPackageAndroid
 * C++ class in libs/WebKitLib/WebKit/WebCore/plugins/android/)
 *
 * @unknown 
 * @deprecated This interface was intended to be used by Gears. Since Gears was
deprecated, so is this class.
 */
@java.lang.Deprecated
public class Plugin {
    /* @hide
    @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    public interface PreferencesClickHandler {
        /* @hide
        @deprecated This interface was intended to be used by Gears. Since Gears was
        deprecated, so is this class.
         */
        public void handleClickEvent(android.content.Context context);
    }

    private java.lang.String mName;

    private java.lang.String mPath;

    private java.lang.String mFileName;

    private java.lang.String mDescription;

    private android.webkit.Plugin.PreferencesClickHandler mHandler;

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public Plugin(java.lang.String name, java.lang.String path, java.lang.String fileName, java.lang.String description) {
        mName = name;
        mPath = path;
        mFileName = fileName;
        mDescription = description;
        mHandler = new android.webkit.Plugin.DefaultClickHandler();
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public java.lang.String toString() {
        return mName;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public java.lang.String getName() {
        return mName;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public java.lang.String getPath() {
        return mPath;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public java.lang.String getFileName() {
        return mFileName;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public java.lang.String getDescription() {
        return mDescription;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public void setName(java.lang.String name) {
        mName = name;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public void setPath(java.lang.String path) {
        mPath = path;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public void setFileName(java.lang.String fileName) {
        mFileName = fileName;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public void setDescription(java.lang.String description) {
        mDescription = description;
    }

    /**
     *
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public void setClickHandler(android.webkit.Plugin.PreferencesClickHandler handler) {
        mHandler = handler;
    }

    /**
     * Invokes the click handler for this plugin.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    public void dispatchClickEvent(android.content.Context context) {
        if (mHandler != null) {
            mHandler.handleClickEvent(context);
        }
    }

    /**
     * Default click handler. The plugins should implement their own.
     *
     * @unknown 
     * @deprecated This interface was intended to be used by Gears. Since Gears was
    deprecated, so is this class.
     */
    @java.lang.Deprecated
    private class DefaultClickHandler implements android.content.DialogInterface.OnClickListener , android.webkit.Plugin.PreferencesClickHandler {
        private android.app.AlertDialog mDialog;

        @java.lang.Deprecated
        public void handleClickEvent(android.content.Context context) {
            // Show a simple popup dialog containing the description
            // string of the plugin.
            if (mDialog == null) {
                mDialog = new android.app.AlertDialog.Builder(context).setTitle(mName).setMessage(mDescription).setPositiveButton(R.string.ok, this).setCancelable(false).show();
            }
        }

        /**
         *
         *
         * @unknown 
         * @deprecated This interface was intended to be used by Gears. Since Gears was
        deprecated, so is this class.
         */
        @java.lang.Deprecated
        public void onClick(android.content.DialogInterface dialog, int which) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}

