/**
 * Copyright (C) 2007 The Android Open Source Project
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
package android.widget.scroll;


/**
 * A screen with some scenarios that exercise {@link ScrollView}'s implementation
 * of {@link android.view.ViewGroup#requestChildRectangleOnScreen}:
 * <li>Scrolling to something off screen (from top and from bottom)
 * <li>Scrolling to bring something that is larger than the screen on screen
 *  (from top and from bottom).
 */
public class RequestRectangleVisible extends android.app.Activity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.scroll_to_rectangle);
        final android.graphics.Rect rect = new android.graphics.Rect();
        final android.view.View childToMakeVisible = findViewById(R.id.childToMakeVisible);
        final android.widget.TextView topBlob = ((android.widget.TextView) (findViewById(R.id.topBlob)));
        final android.widget.TextView bottomBlob = ((android.widget.TextView) (findViewById(R.id.bottomBlob)));
        // estimate to get blobs larger than screen
        int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        int numLinesForScreen = screenHeight / 18;
        for (int i = 0; i < numLinesForScreen; i++) {
            topBlob.append(i + " another line in the blob\n");
            bottomBlob.append(i + " another line in the blob\n");
        }
        findViewById(R.id.scrollToRectFromTop).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                rect.set(0, 0, childToMakeVisible.getLeft(), childToMakeVisible.getHeight());
                childToMakeVisible.requestRectangleOnScreen(rect, true);
            }
        });
        findViewById(R.id.scrollToRectFromTop2).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                rect.set(0, 0, topBlob.getWidth(), topBlob.getHeight());
                topBlob.requestRectangleOnScreen(rect, true);
            }
        });
        findViewById(R.id.scrollToRectFromBottom).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                rect.set(0, 0, childToMakeVisible.getLeft(), childToMakeVisible.getHeight());
                childToMakeVisible.requestRectangleOnScreen(rect, true);
            }
        });
        findViewById(R.id.scrollToRectFromBottom2).setOnClickListener(new android.view.View.OnClickListener() {
            public void onClick(android.view.View v) {
                rect.set(0, 0, bottomBlob.getWidth(), bottomBlob.getHeight());
                bottomBlob.requestRectangleOnScreen(rect, true);
            }
        });
    }
}

