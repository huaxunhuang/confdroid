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
package android.telecom;


/**
 * The unique identifier for a {@link PhoneAccount}. A {@code PhoneAccountHandle} is made of two
 * parts:
 * <ul>
 *  <li>The component name of the associated connection service.</li>
 *  <li>A string identifier that is unique across {@code PhoneAccountHandle}s with the same
 *      component name.</li>
 * </ul>
 *
 * Note: This Class requires a non-null {@link ComponentName} and {@link UserHandle} to operate
 * properly. Passing in invalid parameters will generate a log warning.
 *
 * See {@link PhoneAccount}, {@link TelecomManager}.
 */
public final class PhoneAccountHandle implements android.os.Parcelable {
    private final android.content.ComponentName mComponentName;

    private final java.lang.String mId;

    private final android.os.UserHandle mUserHandle;

    public PhoneAccountHandle(@android.annotation.NonNull
    android.content.ComponentName componentName, @android.annotation.NonNull
    java.lang.String id) {
        this(componentName, id, android.os.Process.myUserHandle());
    }

    public PhoneAccountHandle(@android.annotation.NonNull
    android.content.ComponentName componentName, @android.annotation.NonNull
    java.lang.String id, @android.annotation.NonNull
    android.os.UserHandle userHandle) {
        checkParameters(componentName, userHandle);
        mComponentName = componentName;
        mId = id;
        mUserHandle = userHandle;
    }

    /**
     * The {@code ComponentName} of the connection service which is responsible for making phone
     * calls using this {@code PhoneAccountHandle}.
     *
     * @return A suitable {@code ComponentName}.
     */
    public android.content.ComponentName getComponentName() {
        return mComponentName;
    }

    /**
     * A string that uniquely distinguishes this particular {@code PhoneAccountHandle} from all the
     * others supported by the connection service that created it.
     * <p>
     * A connection service must select identifiers that are stable for the lifetime of
     * their users' relationship with their service, across many Android devices. For example, a
     * good set of identifiers might be the email addresses with which with users registered for
     * their accounts with a particular service. Depending on how a service chooses to operate,
     * a bad set of identifiers might be an increasing series of integers
     * ({@code 0}, {@code 1}, {@code 2}, ...) that are generated locally on each phone and could
     * collide with values generated on other phones or after a data wipe of a given phone.
     *
     * Important: A non-unique identifier could cause non-deterministic call-log backup/restore
     * behavior.
     *
     * @return A service-specific unique identifier for this {@code PhoneAccountHandle}.
     */
    public java.lang.String getId() {
        return mId;
    }

    /**
     *
     *
     * @return the {@link UserHandle} to use when connecting to this PhoneAccount.
     */
    public android.os.UserHandle getUserHandle() {
        return mUserHandle;
    }

    @java.lang.Override
    public int hashCode() {
        return java.util.Objects.hash(mComponentName, mId, mUserHandle);
    }

    @java.lang.Override
    public java.lang.String toString() {
        // Note: Log.pii called for mId as it can contain personally identifying phone account
        // information such as SIP account IDs.
        return new java.lang.StringBuilder().append(mComponentName).append(", ").append(android.telecom.Log.pii(mId)).append(", ").append(mUserHandle).toString();
    }

    @java.lang.Override
    public boolean equals(java.lang.Object other) {
        return ((((other != null) && (other instanceof android.telecom.PhoneAccountHandle)) && java.util.Objects.equals(((android.telecom.PhoneAccountHandle) (other)).getComponentName(), getComponentName())) && java.util.Objects.equals(((android.telecom.PhoneAccountHandle) (other)).getId(), getId())) && java.util.Objects.equals(((android.telecom.PhoneAccountHandle) (other)).getUserHandle(), getUserHandle());
    }

    // 
    // Parcelable implementation.
    // 
    @java.lang.Override
    public int describeContents() {
        return 0;
    }

    @java.lang.Override
    public void writeToParcel(android.os.Parcel out, int flags) {
        mComponentName.writeToParcel(out, flags);
        out.writeString(mId);
        mUserHandle.writeToParcel(out, flags);
    }

    private void checkParameters(android.content.ComponentName componentName, android.os.UserHandle userHandle) {
        if (componentName == null) {
            android.util.Log.w("PhoneAccountHandle", new java.lang.Exception("PhoneAccountHandle has " + "been created with null ComponentName!"));
        }
        if (userHandle == null) {
            android.util.Log.w("PhoneAccountHandle", new java.lang.Exception("PhoneAccountHandle has " + "been created with null UserHandle!"));
        }
    }

    public static final android.os.Parcelable.Creator<android.telecom.PhoneAccountHandle> CREATOR = new android.os.Parcelable.Creator<android.telecom.PhoneAccountHandle>() {
        @java.lang.Override
        public android.telecom.PhoneAccountHandle createFromParcel(android.os.Parcel in) {
            return new android.telecom.PhoneAccountHandle(in);
        }

        @java.lang.Override
        public android.telecom.PhoneAccountHandle[] newArray(int size) {
            return new android.telecom.PhoneAccountHandle[size];
        }
    };

    private PhoneAccountHandle(android.os.Parcel in) {
        this(android.content.ComponentName.CREATOR.createFromParcel(in), in.readString(), android.os.UserHandle.CREATOR.createFromParcel(in));
    }
}

