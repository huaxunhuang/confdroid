/**
 * Copyright (C) 2018 The Android Open Source Project
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
 * limitations under the License
 */
package android.view;


/**
 * Describes an activity to be animated as part of a remote animation.
 *
 * @unknown 
 */
public class RemoteAnimationTarget implements android.os.Parcelable {
    /**
     * The app is in the set of opening apps of this transition.
     */
    public static final int MODE_OPENING = 0;

    /**
     * The app is in the set of closing apps of this transition.
     */
    public static final int MODE_CLOSING = 1;

    /**
     * The app is in the set of resizing apps (eg. mode change) of this transition.
     */
    public static final int MODE_CHANGING = 2;

    @android.annotation.IntDef(prefix = { "MODE_" }, value = { android.view.RemoteAnimationTarget.MODE_OPENING, android.view.RemoteAnimationTarget.MODE_CLOSING, android.view.RemoteAnimationTarget.MODE_CHANGING })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface Mode {}

    /**
     * The {@link Mode} to describe whether this app is opening or closing.
     */
    @android.annotation.UnsupportedAppUsage
    @android.view.RemoteAnimationTarget.Mode
    public final int mode;

    /**
     * The id of the task this app belongs to.
     */
    @android.annotation.UnsupportedAppUsage
    public final int taskId;

    /**
     * The {@link SurfaceControl} object to actually control the transform of the app.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.view.SurfaceControl leash;

    /**
     * The {@link SurfaceControl} for the starting state of a target if this transition is
     * MODE_CHANGING, {@code null)} otherwise. This is relative to the app window.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.view.SurfaceControl startLeash;

    /**
     * Whether the app is translucent and may reveal apps behind.
     */
    @android.annotation.UnsupportedAppUsage
    public final boolean isTranslucent;

    /**
     * The clip rect window manager applies when clipping the app's main surface in screen space
     * coordinates. This is just a hint to the animation runner: If running a clip-rect animation,
     * anything that extends beyond these bounds will not have any effect. This implies that any
     * clip-rect animation should likely stop at these bounds.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.graphics.Rect clipRect;

    /**
     * The insets of the main app window.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.graphics.Rect contentInsets;

    /**
     * The index of the element in the tree in prefix order. This should be used for z-layering
     * to preserve original z-layer order in the hierarchy tree assuming no "boosting" needs to
     * happen.
     */
    @android.annotation.UnsupportedAppUsage
    public final int prefixOrderIndex;

    /**
     * The source position of the app, in screen spaces coordinates. If the position of the leash
     * is modified from the controlling app, any animation transform needs to be offset by this
     * amount.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.graphics.Point position;

    /**
     * The bounds of the source container the app lives in, in screen space coordinates. If the crop
     * of the leash is modified from the controlling app, it needs to take the source container
     * bounds into account when calculating the crop.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.graphics.Rect sourceContainerBounds;

    /**
     * The starting bounds of the source container in screen space coordinates. This is {@code null}
     * if the animation target isn't MODE_CHANGING. Since this is the starting bounds, it's size
     * should be equivalent to the size of the starting thumbnail. Note that sourceContainerBounds
     * is the end bounds of a change transition.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.graphics.Rect startBounds;

    /**
     * The window configuration for the target.
     */
    @android.annotation.UnsupportedAppUsage
    public final android.app.WindowConfiguration windowConfiguration;

    /**
     * Whether the task is not presented in Recents UI.
     */
    @android.annotation.UnsupportedAppUsage
    public boolean isNotInRecents;

    public RemoteAnimationTarget(int taskId, int mode, android.view.SurfaceControl leash, boolean isTranslucent, android.graphics.Rect clipRect, android.graphics.Rect contentInsets, int prefixOrderIndex, android.graphics.Point position, android.graphics.Rect sourceContainerBounds, android.app.WindowConfiguration windowConfig, boolean isNotInRecents, android.view.SurfaceControl startLeash, android.graphics.Rect startBounds) {
        this.mode = mode;
        this.taskId = taskId;
        this.leash = leash;
        this.isTranslucent = isTranslucent;
        this.clipRect = new android.graphics.Rect(clipRect);
        this.contentInsets = new android.graphics.Rect(contentInsets);
        this.prefixOrderIndex = prefixOrderIndex;
        this.position = new android.graphics.Point(position);
        this.sourceContainerBounds = new android.graphics.Rect(sourceContainerBounds);
        this.windowConfiguration = windowConfig;
        this.isNotInRecents = isNotInRecents;
        this.startLeash = startLeash;
        this.startBounds = (startBounds == null) ? null : new android.graphics.Rect(startBounds);
    }

    public RemoteAnimationTarget(android.os.Parcel in) {
        taskId = in.readInt();
        mode = in.readInt();
        leash = in.readParcelable(null);
        isTranslucent = in.readBoolean();
        clipRect = in.readParcelable(null);
        contentInsets = in.readParcelable(null);
        prefixOrderIndex = in.readInt();
        position = in.readParcelable(null);
        sourceContainerBounds = in.readParcelable(null);
        windowConfiguration = in.readParcelable(null);
        isNotInRecents = in.readBoolean();
        startLeash = in.readParcelable(null);
        startBounds = in.readParcelable(null);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeInt(taskId);
        dest.writeInt(mode);
        /* flags */
        dest.writeParcelable(leash, 0);
        dest.writeBoolean(isTranslucent);
        /* flags */
        dest.writeParcelable(clipRect, 0);
        /* flags */
        dest.writeParcelable(contentInsets, 0);
        dest.writeInt(prefixOrderIndex);
        /* flags */
        dest.writeParcelable(position, 0);
        /* flags */
        dest.writeParcelable(sourceContainerBounds, 0);
        /* flags */
        dest.writeParcelable(windowConfiguration, 0);
        dest.writeBoolean(isNotInRecents);
        /* flags */
        dest.writeParcelable(startLeash, 0);
        /* flags */
        dest.writeParcelable(startBounds, 0);
    }

    public void dump(java.io.PrintWriter pw, java.lang.String prefix) {
        pw.print(prefix);
        pw.print("mode=");
        pw.print(mode);
        pw.print(" taskId=");
        pw.print(taskId);
        pw.print(" isTranslucent=");
        pw.print(isTranslucent);
        pw.print(" clipRect=");
        clipRect.printShortString(pw);
        pw.print(" contentInsets=");
        contentInsets.printShortString(pw);
        pw.print(" prefixOrderIndex=");
        pw.print(prefixOrderIndex);
        pw.print(" position=");
        position.printShortString(pw);
        pw.print(" sourceContainerBounds=");
        sourceContainerBounds.printShortString(pw);
        pw.println();
        pw.print(prefix);
        pw.print("windowConfiguration=");
        pw.println(windowConfiguration);
        pw.print(prefix);
        pw.print("leash=");
        pw.println(leash);
    }

    public void writeToProto(android.util.proto.ProtoOutputStream proto, long fieldId) {
        final long token = proto.start(fieldId);
        proto.write(android.view.RemoteAnimationTargetProto.TASK_ID, taskId);
        proto.write(android.view.RemoteAnimationTargetProto.MODE, mode);
        leash.writeToProto(proto, android.view.RemoteAnimationTargetProto.LEASH);
        proto.write(android.view.RemoteAnimationTargetProto.IS_TRANSLUCENT, isTranslucent);
        clipRect.writeToProto(proto, android.view.RemoteAnimationTargetProto.CLIP_RECT);
        contentInsets.writeToProto(proto, android.view.RemoteAnimationTargetProto.CONTENT_INSETS);
        proto.write(android.view.RemoteAnimationTargetProto.PREFIX_ORDER_INDEX, prefixOrderIndex);
        position.writeToProto(proto, android.view.RemoteAnimationTargetProto.POSITION);
        sourceContainerBounds.writeToProto(proto, android.view.RemoteAnimationTargetProto.SOURCE_CONTAINER_BOUNDS);
        windowConfiguration.writeToProto(proto, android.view.RemoteAnimationTargetProto.WINDOW_CONFIGURATION);
        if (startLeash != null) {
            startLeash.writeToProto(proto, android.view.RemoteAnimationTargetProto.START_LEASH);
        }
        if (startBounds != null) {
            startBounds.writeToProto(proto, android.view.RemoteAnimationTargetProto.START_BOUNDS);
        }
        proto.end(token);
    }

    @android.annotation.NonNull
    public static final android.view.Creator<android.view.RemoteAnimationTarget> CREATOR = new android.view.Creator<android.view.RemoteAnimationTarget>() {
        public android.view.RemoteAnimationTarget createFromParcel(android.os.Parcel in) {
            return new android.view.RemoteAnimationTarget(in);
        }

        public android.view.RemoteAnimationTarget[] newArray(int size) {
            return new android.view.RemoteAnimationTarget[size];
        }
    };
}

