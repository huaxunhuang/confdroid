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
package android.media;


/**
 * The AudioHandle is used by the audio framework implementation to
 * uniquely identify a particular component of the routing topology
 * (AudioPort or AudioPatch)
 * It is not visible or used at the API.
 */
class AudioHandle {
    private final int mId;

    AudioHandle(int id) {
        mId = id;
    }

    int id() {
        return mId;
    }

    @java.lang.Override
    public boolean equals(java.lang.Object o) {
        if ((o == null) || (!(o instanceof android.media.AudioHandle))) {
            return false;
        }
        android.media.AudioHandle ah = ((android.media.AudioHandle) (o));
        return mId == ah.id();
    }

    @java.lang.Override
    public int hashCode() {
        return mId;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return java.lang.Integer.toString(mId);
    }
}

