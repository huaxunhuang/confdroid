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
 * An AudioPatch describes a connection between audio sources and audio sinks.
 * An audio source can be an output mix (playback AudioBus) or an input device (microphone).
 * An audio sink can be an output device (speaker) or an input mix (capture AudioBus).
 * An AudioPatch is created by AudioManager.createAudioPatch() and released by
 * AudioManager.releaseAudioPatch()
 * It contains the list of source and sink AudioPortConfig showing audio port configurations
 * being connected.
 *
 * @unknown 
 */
public class AudioPatch {
    private final android.media.AudioHandle mHandle;

    private final android.media.AudioPortConfig[] mSources;

    private final android.media.AudioPortConfig[] mSinks;

    AudioPatch(android.media.AudioHandle patchHandle, android.media.AudioPortConfig[] sources, android.media.AudioPortConfig[] sinks) {
        mHandle = patchHandle;
        mSources = sources;
        mSinks = sinks;
    }

    /**
     * Retrieve the list of sources of this audio patch.
     */
    public android.media.AudioPortConfig[] sources() {
        return mSources;
    }

    /**
     * Retreive the list of sinks of this audio patch.
     */
    public android.media.AudioPortConfig[] sinks() {
        return mSinks;
    }

    /**
     * Get the system unique patch ID.
     */
    public int id() {
        return mHandle.id();
    }

    @java.lang.Override
    public java.lang.String toString() {
        java.lang.StringBuilder s = new java.lang.StringBuilder();
        s.append("mHandle: ");
        s.append(mHandle.toString());
        s.append(" mSources: {");
        for (android.media.AudioPortConfig source : mSources) {
            s.append(source.toString());
            s.append(", ");
        }
        s.append("} mSinks: {");
        for (android.media.AudioPortConfig sink : mSinks) {
            s.append(sink.toString());
            s.append(", ");
        }
        s.append("}");
        return s.toString();
    }
}

