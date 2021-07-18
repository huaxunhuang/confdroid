/**
 * Copyright (C) 2013 The Android Open Source Project
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
public class WebVttRenderer extends android.media.SubtitleController.Renderer {
    private final android.content.Context mContext;

    private android.media.WebVttRenderingWidget mRenderingWidget;

    public WebVttRenderer(android.content.Context context) {
        mContext = context;
    }

    @java.lang.Override
    public boolean supports(android.media.MediaFormat format) {
        if (format.containsKey(android.media.MediaFormat.KEY_MIME)) {
            return format.getString(android.media.MediaFormat.KEY_MIME).equals("text/vtt");
        }
        return false;
    }

    @java.lang.Override
    public android.media.SubtitleTrack createTrack(android.media.MediaFormat format) {
        if (mRenderingWidget == null) {
            mRenderingWidget = new android.media.WebVttRenderingWidget(mContext);
        }
        return new android.media.WebVttTrack(mRenderingWidget, format);
    }
}

