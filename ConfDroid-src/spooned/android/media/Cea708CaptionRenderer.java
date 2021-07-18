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
package android.media;


/**
 *
 *
 * @unknown 
 */
public class Cea708CaptionRenderer extends android.media.SubtitleController.Renderer {
    private final android.content.Context mContext;

    private android.media.Cea708CCWidget mCCWidget;

    public Cea708CaptionRenderer(android.content.Context context) {
        mContext = context;
    }

    @java.lang.Override
    public boolean supports(android.media.MediaFormat format) {
        if (format.containsKey(android.media.MediaFormat.KEY_MIME)) {
            java.lang.String mimeType = format.getString(android.media.MediaFormat.KEY_MIME);
            return android.media.MediaPlayer.MEDIA_MIMETYPE_TEXT_CEA_708.equals(mimeType);
        }
        return false;
    }

    @java.lang.Override
    public android.media.SubtitleTrack createTrack(android.media.MediaFormat format) {
        java.lang.String mimeType = format.getString(android.media.MediaFormat.KEY_MIME);
        if (android.media.MediaPlayer.MEDIA_MIMETYPE_TEXT_CEA_708.equals(mimeType)) {
            if (mCCWidget == null) {
                mCCWidget = new android.media.Cea708CCWidget(mContext);
            }
            return new android.media.Cea708CaptionTrack(mCCWidget, format);
        }
        throw new java.lang.RuntimeException("No matching format: " + format.toString());
    }
}

