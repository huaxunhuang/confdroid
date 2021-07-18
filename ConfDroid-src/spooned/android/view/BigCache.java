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
package android.view;


/**
 * This activity contains two Views, one as big as the screen, one much larger. The large one
 * should not be able to activate its drawing cache.
 */
public class BigCache extends android.app.Activity {
    @java.lang.Override
    protected void onCreate(android.os.Bundle icicle) {
        super.onCreate(icicle);
        final android.widget.LinearLayout testBed = new android.widget.LinearLayout(this);
        testBed.setOrientation(android.widget.LinearLayout.VERTICAL);
        testBed.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
        final int cacheSize = android.view.ViewConfiguration.getMaximumDrawingCacheSize();
        final android.view.Display display = getWindowManager().getDefaultDisplay();
        final int screenWidth = display.getWidth();
        final int screenHeight = display.getHeight();
        final android.view.View tiny = new android.view.View(this);
        tiny.setId(R.id.a);
        tiny.setBackgroundColor(0xffff0000);
        tiny.setLayoutParams(new android.widget.LinearLayout.LayoutParams(screenWidth, screenHeight));
        final android.view.View large = new android.view.View(this);
        large.setId(R.id.b);
        large.setBackgroundColor(0xff00ff00);
        // Compute the height of the view assuming a cache size based on ARGB8888
        final int height = (2 * (cacheSize / 2)) / screenWidth;
        large.setLayoutParams(new android.widget.LinearLayout.LayoutParams(screenWidth, height));
        final android.widget.ScrollView scroller = new android.widget.ScrollView(this);
        scroller.setLayoutParams(new android.view.ViewGroup.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT));
        testBed.addView(tiny);
        testBed.addView(large);
        scroller.addView(testBed);
        setContentView(scroller);
    }
}

