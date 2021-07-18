/**
 * Copyright (C) 2014 The Android Open Source Project
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
package android.view;


/**
 * This class represents information about a window from the
 * window manager to another part of the system.
 *
 * @unknown 
 */
public class WindowInfo implements android.os.Parcelable {
    private static final int MAX_POOL_SIZE = 10;

    private static final android.util.Pools.SynchronizedPool<android.view.WindowInfo> sPool = new android.util.Pools.SynchronizedPool<android.view.WindowInfo>(MAX_POOL_SIZE);

    public int type;

    public int layer;

    public android.os.IBinder token;

    public android.os.IBinder parentToken;

    public android.os.IBinder activityToken;

    public boolean focused;

    public final android.graphics.Rect boundsInScreen = new android.graphics.Rect();

    public java.util.List<android.os.IBinder> childTokens;

    public java.lang.CharSequence title;

    public long accessibilityIdOfAnchor = android.view.accessibility.AccessibilityNodeInfo.UNDEFINED_NODE_ID;

    public boolean inPictureInPicture;

    public boolean hasFlagWatchOutsideTouch;

    private WindowInfo() {
        /* do nothing - hide constructor */
    }

    public static android.view.WindowInfo obtain() {
        android.view.WindowInfo window = sPool.acquire();
        if (window == null) {
            window = new android.view.WindowInfo();
        }
        return window;
    }

    public static android.view.WindowInfo obtain(android.view.WindowInfo other) {
        android.view.WindowInfo window = android.view.WindowInfo.obtain();
        window.type = other.type;
        window.layer = other.layer;
        window.token = other.token;
        window.parentToken = other.parentToken;
        window.activityToken = other.activityToken;
        window.focused = other.focused;
        window.boundsInScreen.set(other.boundsInScreen);
        window.title = other.title;
        window.accessibilityIdOfAnchor = other.accessibilityIdOfAnchor;
        window.inPictureInPicture = other.inPictureInPicture;
        window.hasFlagWatchOutsideTouch = other.hasFlagWatchOutsideTouch;
        if ((other.childTokens != null) && (!other.childTokens.isEmpty())) {
            if (window.childTokens == null) {
                window.childTokens = new java.util.ArrayList<android.os.IBinder>(other.childTokens);
            } else {
                window.childTokens.addAll(other.childTokens);
            }
        }
        return window;
    }

    public void recycle() {
        clear();
        sPool.release(this);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel parcel, int flags) {
        parcel.writeInt(type);
        parcel.writeInt(layer);
        parcel.writeStrongBinder(token);
        parcel.writeStrongBinder(parentToken);
        parcel.writeStrongBinder(activityToken);
        parcel.writeInt(focused ? 1 : 0);
        boundsInScreen.writeToParcel(parcel, flags);
        parcel.writeCharSequence(title);
        parcel.writeLong(accessibilityIdOfAnchor);
        parcel.writeInt(inPictureInPicture ? 1 : 0);
        parcel.writeInt(hasFlagWatchOutsideTouch ? 1 : 0);
        if ((childTokens != null) && (!childTokens.isEmpty())) {
            parcel.writeInt(1);
            parcel.writeBinderList(childTokens);
        } else {
            parcel.writeInt(0);
        }
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder builder = new java.lang.StringBuilder();
        builder.append("WindowInfo[");
        builder.append("title=").append(title);
        builder.append(", type=").append(type);
        builder.append(", layer=").append(layer);
        builder.append(", token=").append(token);
        builder.append(", bounds=").append(boundsInScreen);
        builder.append(", parent=").append(parentToken);
        builder.append(", focused=").append(focused);
        builder.append(", children=").append(childTokens);
        builder.append(", accessibility anchor=").append(accessibilityIdOfAnchor);
        builder.append(", pictureInPicture=").append(inPictureInPicture);
        builder.append(", watchOutsideTouch=").append(hasFlagWatchOutsideTouch);
        builder.append(']');
        return builder.toString();
    }

    private void initFromParcel(android.os.Parcel parcel) {
        type = parcel.readInt();
        layer = parcel.readInt();
        token = parcel.readStrongBinder();
        parentToken = parcel.readStrongBinder();
        activityToken = parcel.readStrongBinder();
        focused = parcel.readInt() == 1;
        boundsInScreen.readFromParcel(parcel);
        title = parcel.readCharSequence();
        accessibilityIdOfAnchor = parcel.readLong();
        inPictureInPicture = parcel.readInt() == 1;
        hasFlagWatchOutsideTouch = parcel.readInt() == 1;
        final boolean hasChildren = parcel.readInt() == 1;
        if (hasChildren) {
            if (childTokens == null) {
                childTokens = new java.util.ArrayList<android.os.IBinder>();
            }
            parcel.readBinderList(childTokens);
        }
    }

    private void clear() {
        type = 0;
        layer = 0;
        token = null;
        parentToken = null;
        activityToken = null;
        focused = false;
        boundsInScreen.setEmpty();
        if (childTokens != null) {
            childTokens.clear();
        }
        inPictureInPicture = false;
        hasFlagWatchOutsideTouch = false;
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.WindowInfo> CREATOR = new android.view.Creator<android.view.WindowInfo>() {
        @java.lang.Override
        public android.view.WindowInfo createFromParcel(android.os.Parcel parcel) {
            android.view.WindowInfo window = android.view.WindowInfo.obtain();
            window.initFromParcel(parcel);
            return window;
        }

        @java.lang.Override
        public android.view.WindowInfo[] newArray(int size) {
            return new android.view.WindowInfo[size];
        }
    };
}

