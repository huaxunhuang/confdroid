/**
 * Copyright (C) 2015 The Android Open Source Project
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
package android.service.quicksettings;


/**
 * A Tile holds the state of a tile that will be displayed
 * in Quick Settings.
 *
 * A tile in Quick Settings exists as an icon with an accompanied label.
 * It also may have content description for accessibility usability.
 * The style and layout of the tile may change to match a given
 * device.
 */
public final class Tile implements android.os.Parcelable {
    private static final java.lang.String TAG = "Tile";

    /**
     * An unavailable state indicates that for some reason this tile is not currently
     * available to the user for some reason, and will have no click action.  The tile's
     * icon will be tinted differently to reflect this state.
     */
    public static final int STATE_UNAVAILABLE = 0;

    /**
     * This represents a tile that is currently in a disabled state but is still interactable.
     *
     * A disabled state indicates that the tile is not currently active (e.g. wifi disconnected or
     * bluetooth disabled), but is still interactable by the user to modify this state.  Tiles
     * that have boolean states should use this to represent one of their states.  The tile's
     * icon will be tinted differently to reflect this state, but still be distinct from unavailable.
     */
    public static final int STATE_INACTIVE = 1;

    /**
     * This represents a tile that is currently active. (e.g. wifi is connected, bluetooth is on,
     * cast is casting).  This is the default state.
     */
    public static final int STATE_ACTIVE = 2;

    private android.os.IBinder mToken;

    private android.graphics.drawable.Icon mIcon;

    private java.lang.CharSequence mLabel;

    private java.lang.CharSequence mContentDescription;

    // Default to active until clients of the new API can update.
    private int mState = android.service.quicksettings.Tile.STATE_ACTIVE;

    private android.service.quicksettings.IQSService mService;

    /**
     *
     *
     * @unknown 
     */
    public Tile(android.os.Parcel source) {
        readFromParcel(source);
    }

    /**
     *
     *
     * @unknown 
     */
    public Tile() {
    }

    /**
     *
     *
     * @unknown 
     */
    public void setService(android.service.quicksettings.IQSService service, android.os.IBinder stub) {
        mService = service;
        mToken = stub;
    }

    /**
     * The current state of the tile.
     *
     * @see #STATE_UNAVAILABLE
     * @see #STATE_INACTIVE
     * @see #STATE_ACTIVE
     */
    public int getState() {
        return mState;
    }

    /**
     * Sets the current state for the tile.
     *
     * Does not take effect until {@link #updateTile()} is called.
     *
     * @param state
     * 		One of {@link #STATE_UNAVAILABLE}, {@link #STATE_INACTIVE},
     * 		{@link #STATE_ACTIVE}
     */
    public void setState(int state) {
        mState = state;
    }

    /**
     * Gets the current icon for the tile.
     */
    public android.graphics.drawable.Icon getIcon() {
        return mIcon;
    }

    /**
     * Sets the current icon for the tile.
     *
     * This icon is expected to be white on alpha, and may be
     * tinted by the system to match it's theme.
     *
     * Does not take effect until {@link #updateTile()} is called.
     *
     * @param icon
     * 		New icon to show.
     */
    public void setIcon(android.graphics.drawable.Icon icon) {
        this.mIcon = icon;
    }

    /**
     * Gets the current label for the tile.
     */
    public java.lang.CharSequence getLabel() {
        return mLabel;
    }

    /**
     * Sets the current label for the tile.
     *
     * Does not take effect until {@link #updateTile()} is called.
     *
     * @param label
     * 		New label to show.
     */
    public void setLabel(java.lang.CharSequence label) {
        this.mLabel = label;
    }

    /**
     * Gets the current content description for the tile.
     */
    public java.lang.CharSequence getContentDescription() {
        return mContentDescription;
    }

    /**
     * Sets the current content description for the tile.
     *
     * Does not take effect until {@link #updateTile()} is called.
     *
     * @param contentDescription
     * 		New content description to use.
     */
    public void setContentDescription(java.lang.CharSequence contentDescription) {
        this.mContentDescription = contentDescription;
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    /**
     * Pushes the state of the Tile to Quick Settings to be displayed.
     */
    public void updateTile() {
        try {
            mService.updateQsTile(this, mToken);
        } catch (android.os.RemoteException e) {
            android.util.Log.e(android.service.quicksettings.Tile.TAG, "Couldn't update tile");
        }
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        if (mIcon != null) {
            dest.writeByte(((byte) (1)));
            mIcon.writeToParcel(dest, flags);
        } else {
            dest.writeByte(((byte) (0)));
        }
        dest.writeInt(mState);
        android.text.TextUtils.writeToParcel(mLabel, dest, flags);
        android.text.TextUtils.writeToParcel(mContentDescription, dest, flags);
    }

    private void readFromParcel(android.os.Parcel source) {
        if (source.readByte() != 0) {
            mIcon = android.graphics.drawable.Icon.CREATOR.createFromParcel(source);
        } else {
            mIcon = null;
        }
        mState = source.readInt();
        mLabel = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
        mContentDescription = android.text.TextUtils.CHAR_SEQUENCE_CREATOR.createFromParcel(source);
    }

    public static final android.os.Parcelable.Creator<android.service.quicksettings.Tile> CREATOR = new android.os.Parcelable.Creator<android.service.quicksettings.Tile>() {
        @java.lang.Override
        public android.service.quicksettings.Tile createFromParcel(android.os.Parcel source) {
            return new android.service.quicksettings.Tile(source);
        }

        @java.lang.Override
        public android.service.quicksettings.Tile[] newArray(int size) {
            return new android.service.quicksettings.Tile[size];
        }
    };
}

