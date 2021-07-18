/**
 * * Copyright 2015, The Android Open Source Project
 * *
 * * Licensed under the Apache License, Version 2.0 (the "License");
 * * you may not use this file except in compliance with the License.
 * * You may obtain a copy of the License at
 * *
 * *     http://www.apache.org/licenses/LICENSE-2.0
 * *
 * * Unless required by applicable law or agreed to in writing, software
 * * distributed under the License is distributed on an "AS IS" BASIS,
 * * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * * See the License for the specific language governing permissions and
 * * limitations under the License.
 */
package android.view;


/**
 * {@link DragAndDropPermissions} controls the access permissions for the content URIs associated
 * with a {@link DragEvent}.
 * <p>
 * Permission are granted when this object is created by {@link android.app.Activity#requestDragAndDropPermissions(DragEvent)
 * Activity.requestDragAndDropPermissions}.
 * Which permissions are granted is defined by the set of flags passed to {@link View#startDragAndDrop(android.content.ClipData, View.DragShadowBuilder, Object, int)
 * View.startDragAndDrop} by the app that started the drag operation.
 * </p>
 * <p>
 * The life cycle of the permissions is bound to the activity used to call {@link android.app.Activity#requestDragAndDropPermissions(DragEvent) requestDragAndDropPermissions}. The
 * permissions are revoked when this activity is destroyed, or when {@link #release()} is called,
 * whichever occurs first.
 * </p>
 * <p>
 * If you anticipate that your application will receive a large number of drops (e.g. document
 * editor), you should try to call {@link #release()} on the obtained permissions as soon as they
 * are no longer required. Permissions can be added to your activity's
 * {@link Activity#onSaveInstanceState} bundle and later retrieved in order to manually release
 * the permissions once they are no longer needed.
 * </p>
 */
public final class DragAndDropPermissions implements android.os.Parcelable {
    private final com.android.internal.view.IDragAndDropPermissions mDragAndDropPermissions;

    private android.os.IBinder mTransientToken;

    /**
     * Create a new {@link DragAndDropPermissions} object to control the access permissions for
     * content URIs associated with {@link DragEvent}.
     *
     * @param dragEvent
     * 		Drag event
     * @return {@link DragAndDropPermissions} object or null if there are no content URIs associated
    with the {@link DragEvent}.
     * @unknown 
     */
    public static android.view.DragAndDropPermissions obtain(android.view.DragEvent dragEvent) {
        if (dragEvent.getDragAndDropPermissions() == null) {
            return null;
        }
        return new android.view.DragAndDropPermissions(dragEvent.getDragAndDropPermissions());
    }

    /**
     *
     *
     * @unknown 
     */
    private DragAndDropPermissions(com.android.internal.view.IDragAndDropPermissions dragAndDropPermissions) {
        mDragAndDropPermissions = dragAndDropPermissions;
    }

    /**
     * Take the permissions and bind their lifetime to the activity.
     *
     * @param activityToken
     * 		Binder pointing to an Activity instance to bind the lifetime to.
     * @return True if permissions are successfully taken.
     * @unknown 
     */
    public boolean take(android.os.IBinder activityToken) {
        try {
            mDragAndDropPermissions.take(activityToken);
        } catch (android.os.RemoteException e) {
            return false;
        }
        return true;
    }

    /**
     * Take the permissions. Must call {@link #release} explicitly.
     *
     * @return True if permissions are successfully taken.
     * @unknown 
     */
    public boolean takeTransient() {
        try {
            mTransientToken = new android.os.Binder();
            mDragAndDropPermissions.takeTransient(mTransientToken);
        } catch (android.os.RemoteException e) {
            return false;
        }
        return true;
    }

    /**
     * Revoke permissions explicitly.
     */
    public void release() {
        try {
            mDragAndDropPermissions.release();
            mTransientToken = null;
        } catch (android.os.RemoteException e) {
        }
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.DragAndDropPermissions> CREATOR = new android.os.Parcelable.Creator<android.view.DragAndDropPermissions>() {
        @java.lang.Override
        public android.view.DragAndDropPermissions createFromParcel(android.os.Parcel source) {
            return new android.view.DragAndDropPermissions(source);
        }

        @java.lang.Override
        public android.view.DragAndDropPermissions[] newArray(int size) {
            return new android.view.DragAndDropPermissions[size];
        }
    };

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel destination, int flags) {
        destination.writeStrongInterface(mDragAndDropPermissions);
        destination.writeStrongBinder(mTransientToken);
    }

    private DragAndDropPermissions(android.os.Parcel in) {
        mDragAndDropPermissions = IDragAndDropPermissions.Stub.asInterface(in.readStrongBinder());
        mTransientToken = in.readStrongBinder();
    }
}

