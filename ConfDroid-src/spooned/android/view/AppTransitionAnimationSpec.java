package android.view;


/**
 * Holds information about how the next app transition animation should be executed.
 *
 * This class is intended to be used with IWindowManager.overridePendingAppTransition* methods when
 * simple arguments are not enough to describe the animation.
 *
 * @unknown 
 */
public class AppTransitionAnimationSpec implements android.os.Parcelable {
    public final int taskId;

    public final android.graphics.GraphicBuffer buffer;

    public final android.graphics.Rect rect;

    @android.annotation.UnsupportedAppUsage
    public AppTransitionAnimationSpec(int taskId, android.graphics.GraphicBuffer buffer, android.graphics.Rect rect) {
        this.taskId = taskId;
        this.rect = rect;
        this.buffer = buffer;
    }

    public AppTransitionAnimationSpec(android.os.Parcel in) {
        taskId = in.readInt();
        rect = in.readParcelable(null);
        buffer = in.readParcelable(null);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(taskId);
        /* flags */
        dest.writeParcelable(rect, 0);
        dest.writeParcelable(buffer, 0);
    }

    @android.annotation.NonNull
    public static final android.os.Parcelable.Creator<android.view.AppTransitionAnimationSpec> CREATOR = new android.os.Parcelable.Creator<android.view.AppTransitionAnimationSpec>() {
        public android.view.AppTransitionAnimationSpec createFromParcel(android.os.Parcel in) {
            return new android.view.AppTransitionAnimationSpec(in);
        }

        public android.view.AppTransitionAnimationSpec[] newArray(int size) {
            return new android.view.AppTransitionAnimationSpec[size];
        }
    };

    @java.lang.Override
    public java.lang.String toString() {
        return ((((("{taskId: " + taskId) + ", buffer: ") + buffer) + ", rect: ") + rect) + "}";
    }
}

