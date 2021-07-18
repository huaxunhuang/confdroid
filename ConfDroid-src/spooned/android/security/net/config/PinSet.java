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
package android.security.net.config;


/**
 *
 *
 * @unknown 
 */
public final class PinSet {
    public static final android.security.net.config.PinSet EMPTY_PINSET = new android.security.net.config.PinSet(java.util.Collections.<android.security.net.config.Pin>emptySet(), java.lang.Long.MAX_VALUE);

    public final long expirationTime;

    public final java.util.Set<android.security.net.config.Pin> pins;

    public PinSet(java.util.Set<android.security.net.config.Pin> pins, long expirationTime) {
        if (pins == null) {
            throw new java.lang.NullPointerException("pins must not be null");
        }
        this.pins = pins;
        this.expirationTime = expirationTime;
    }

    java.util.Set<java.lang.String> getPinAlgorithms() {
        // TODO: Cache this.
        java.util.Set<java.lang.String> algorithms = new android.util.ArraySet<java.lang.String>();
        for (android.security.net.config.Pin pin : pins) {
            algorithms.add(pin.digestAlgorithm);
        }
        return algorithms;
    }
}

