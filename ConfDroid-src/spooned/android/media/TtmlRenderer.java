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
 *
 *
 * @unknown 
 */
public class TtmlRenderer extends android.media.SubtitleController.Renderer {
    private final android.content.Context mContext;

    private static final java.lang.String MEDIA_MIMETYPE_TEXT_TTML = "application/ttml+xml";

    private android.media.TtmlRenderingWidget mRenderingWidget;

    public TtmlRenderer(android.content.Context context) {
        mContext = context;
    }

    @java.lang.Override
    public boolean supports(android.media.MediaFormat format) {
        if (format.containsKey(android.media.MediaFormat.KEY_MIME)) {
            return format.getString(android.media.MediaFormat.KEY_MIME).equals(android.media.TtmlRenderer.MEDIA_MIMETYPE_TEXT_TTML);
        }
        return false;
    }

    @java.lang.Override
    public android.media.SubtitleTrack createTrack(android.media.MediaFormat format) {
        if (mRenderingWidget == null) {
            mRenderingWidget = new android.media.TtmlRenderingWidget(mContext);
        }
        return new android.media.TtmlTrack(mRenderingWidget, format);
    }
}

