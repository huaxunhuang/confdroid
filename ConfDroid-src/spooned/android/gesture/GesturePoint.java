/**
 * Copyright (C) 2008-2009 The Android Open Source Project
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


/**
 * A timed point of a gesture stroke. Multiple points form a stroke.
 */
public class GesturePoint {
    public final float x;

    public final float y;

    public final long timestamp;

    public GesturePoint(float x, float y, long t) {
        this.x = x;
        this.y = y;
        timestamp = t;
    }

    static android.gesture.GesturePoint deserialize(java.io.DataInputStream in) throws java.io.IOException {
        // Read X and Y
        final float x = in.readFloat();
        final float y = in.readFloat();
        // Read timestamp
        final long timeStamp = in.readLong();
        return new android.gesture.GesturePoint(x, y, timeStamp);
    }

    @java.lang.Override
    public java.lang.Object clone() {
        return new android.gesture.GesturePoint(x, y, timestamp);
    }
}

