/**
 * Copyright (c) 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package android.service.notification;


/**
 * The current condition of an {@link android.app.AutomaticZenRule}, provided by the
 * {@link ConditionProviderService} that owns the rule. Used to tell the system to enter Do Not
 * Disturb mode and request that the system exit Do Not Disturb mode.
 */
public final class Condition implements android.os.Parcelable {
    @android.annotation.SystemApi
    public static final java.lang.String SCHEME = "condition";

    /**
     *
     *
     * @unknown 
     */
    @android.annotation.IntDef({ android.service.notification.Condition.STATE_FALSE, android.service.notification.Condition.STATE_TRUE, android.service.notification.Condition.STATE_TRUE, android.service.notification.Condition.STATE_ERROR })
    @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.SOURCE)
    public @interface State {}

    /**
     * Indicates that Do Not Disturb should be turned off. Note that all Conditions from all
     * {@link ConditionProviderService} providers must be off for Do Not Disturb to be turned off on
     * the device.
     */
    public static final int STATE_FALSE = 0;

    /**
     * Indicates that Do Not Disturb should be turned on.
     */
    public static final int STATE_TRUE = 1;

    @android.annotation.SystemApi
    public static final int STATE_UNKNOWN = 2;

    @android.annotation.SystemApi
    public static final int STATE_ERROR = 3;

    @android.annotation.SystemApi
    public static final int FLAG_RELEVANT_NOW = 1 << 0;

    @android.annotation.SystemApi
    public static final int FLAG_RELEVANT_ALWAYS = 1 << 1;

    /**
     * The URI representing the rule being updated.
     * See {@link android.app.AutomaticZenRule#getConditionId()}.
     */
    public final android.net.Uri id;

    /**
     * A summary of what the rule encoded in {@link #id} means when it is enabled. User visible
     * if the state of the condition is {@link #STATE_TRUE}.
     */
    public final java.lang.String summary;

    @android.annotation.SystemApi
    public final java.lang.String line1;

    @android.annotation.SystemApi
    public final java.lang.String line2;

    /**
     * The state of this condition. {@link #STATE_TRUE} will enable Do Not Disturb mode.
     * {@link #STATE_FALSE} will turn Do Not Disturb off for this rule. Note that Do Not Disturb
     * might still be enabled globally if other conditions are in a {@link #STATE_TRUE} state.
     */
    @android.service.notification.Condition.State
    public final int state;

    @android.annotation.SystemApi
    public final int flags;

    @android.annotation.SystemApi
    public final int icon;

    /**
     * An object representing the current state of a {@link android.app.AutomaticZenRule}.
     *
     * @param id
     * 		the {@link android.app.AutomaticZenRule#getConditionId()} of the zen rule
     * @param summary
     * 		a user visible description of the rule state.
     */
    public Condition(android.net.Uri id, java.lang.String summary, int state) {
        this(id, summary, "", "", -1, state, android.service.notification.Condition.FLAG_RELEVANT_ALWAYS);
    }

    @android.annotation.SystemApi
    public Condition(android.net.Uri id, java.lang.String summary, java.lang.String line1, java.lang.String line2, int icon, int state, int flags) {
        if (id == null)
            throw new java.lang.IllegalArgumentException("id is required");

        if (summary == null)
            throw new java.lang.IllegalArgumentException("summary is required");

        if (!android.service.notification.Condition.isValidState(state))
            throw new java.lang.IllegalArgumentException("state is invalid: " + state);

        this.id = id;
        this.summary = summary;
        this.line1 = line1;
        this.line2 = line2;
        this.icon = icon;
        this.state = state;
        this.flags = flags;
    }

    public Condition(android.os.Parcel source) {
        this(((android.net.Uri) (source.readParcelable(android.service.notification.Condition.class.getClassLoader()))), source.readString(), source.readString(), source.readString(), source.readInt(), source.readInt(), source.readInt());
    }

    private static boolean isValidState(int state) {
        return (state >= android.service.notification.Condition.STATE_FALSE) && (state <= android.service.notification.Condition.STATE_ERROR);
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeParcelable(id, 0);
        dest.writeString(summary);
        dest.writeString(line1);
        dest.writeString(line2);
        dest.writeInt(icon);
        dest.writeInt(state);
        dest.writeInt(this.flags);
    }

    @java.lang.Override
    public java.lang.String toString() {
        return new java.lang.StringBuilder(android.service.notification.Condition.class.getSimpleName()).append('[').append("id=").append(id).append(",summary=").append(summary).append(",line1=").append(line1).append(",line2=").append(line2).append(",icon=").append(icon).append(",state=").append(android.service.notification.Condition.stateToString(state)).append(",flags=").append(flags).append(']').toString();
    }

    @android.annotation.SystemApi
    public static java.lang.String stateToString(int state) {
        if (state == android.service.notification.Condition.STATE_FALSE)
            return "STATE_FALSE";

        if (state == android.service.notification.Condition.STATE_TRUE)
            return "STATE_TRUE";

        if (state == android.service.notification.Condition.STATE_UNKNOWN)
            return "STATE_UNKNOWN";

        if (state == android.service.notification.Condition.STATE_ERROR)
            return "STATE_ERROR";

        throw new java.lang.IllegalArgumentException("state is invalid: " + state);
    }

    @android.annotation.SystemApi
    public static java.lang.String relevanceToString(int flags) {
        final boolean now = (flags & android.service.notification.Condition.FLAG_RELEVANT_NOW) != 0;
        final boolean always = (flags & android.service.notification.Condition.FLAG_RELEVANT_ALWAYS) != 0;
        if ((!now) && (!always))
            return "NONE";

        if (now && always)
            return "NOW, ALWAYS";

        return now ? "NOW" : "ALWAYS";
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if (!(o instanceof android.service.notification.Condition))
            return false;

        if (o == this)
            return true;

        final android.service.notification.Condition other = ((android.service.notification.Condition) (o));
        return (((((java.util.Objects.equals(other.id, id) && java.util.Objects.equals(other.summary, summary)) && java.util.Objects.equals(other.line1, line1)) && java.util.Objects.equals(other.line2, line2)) && (other.icon == icon)) && (other.state == state)) && (other.flags == flags);
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(id, summary, line1, line2, icon, state, flags);
    }

    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @android.annotation.SystemApi
    public android.service.notification.Condition copy() {
        final android.os.Parcel parcel = android.os.Parcel.obtain();
        try {
            writeToParcel(parcel, 0);
            parcel.setDataPosition(0);
            return new android.service.notification.Condition(parcel);
        } finally {
            parcel.recycle();
        }
    }

    @android.annotation.SystemApi
    public static android.net.Uri.Builder newId(android.content.Context context) {
        return new android.net.Uri.Builder().scheme(android.service.notification.Condition.SCHEME).authority(context.getPackageName());
    }

    @android.annotation.SystemApi
    public static boolean isValidId(android.net.Uri id, java.lang.String pkg) {
        return ((id != null) && android.service.notification.Condition.SCHEME.equals(id.getScheme())) && pkg.equals(id.getAuthority());
    }

    public static final android.os.Parcelable.Creator<android.service.notification.Condition> CREATOR = new android.os.Parcelable.Creator<android.service.notification.Condition>() {
        @java.lang.Override
        public android.service.notification.Condition createFromParcel(android.os.Parcel source) {
            return new android.service.notification.Condition(source);
        }

        @java.lang.Override
        public android.service.notification.Condition[] newArray(int size) {
            return new android.service.notification.Condition[size];
        }
    };
}

