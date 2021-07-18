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
package android.system;


/**
 * Corresponds to C's {@code struct group_source_req}.
 *
 * @unknown 
 */
public final class StructGroupSourceReq {
    public final int gsr_interface;

    public final java.net.InetAddress gsr_group;

    public final java.net.InetAddress gsr_source;

    public StructGroupSourceReq(int gsr_interface, java.net.InetAddress gsr_group, java.net.InetAddress gsr_source) {
        this.gsr_interface = gsr_interface;
        this.gsr_group = gsr_group;
        this.gsr_source = gsr_source;
    }

    @java.lang.Override
    public java.lang.String toString() {
        return libcore.util.Objects.toString(this);
    }
}

