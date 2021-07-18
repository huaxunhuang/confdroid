/**
 * Copyright (C) 2006 The Android Open Source Project
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


public class InflateTest extends android.test.AndroidTestCase implements android.test.PerformanceTestCase {
    private android.view.LayoutInflater mInflater;

    private android.content.res.Resources mResources;

    private android.view.View mView;

    @java.lang.Override
    protected void setUp() throws java.lang.Exception {
        setUp();
        mInflater = android.view.LayoutInflater.from(mContext);
        mResources = mContext.getResources();
        // to try to make things consistent, before doing timing
        // do an initial instantiation of the layout and then clear
        // out the layout cache.
        // mInflater.inflate(mResId, null, null);
        // mResources.flushLayoutCache();
    }

    public int startPerformance(android.test.PerformanceTestCase.Intermediates intermediates) {
        return 0;
    }

    public boolean isPerformanceOnly() {
        return false;
    }

    public void inflateTest(int resourceId) {
        mView = mInflater.inflate(resourceId, null);
        mResources.flushLayoutCache();
    }

    public void inflateCachedTest(int resourceId) {
        // Make sure this layout is in the cache.
        mInflater.inflate(resourceId, null);
        mInflater.inflate(resourceId, null);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout1() throws java.lang.Exception {
        inflateTest(R.layout.layout_one);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout2() throws java.lang.Exception {
        inflateTest(R.layout.layout_two);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout3() throws java.lang.Exception {
        inflateTest(R.layout.layout_three);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout4() throws java.lang.Exception {
        inflateTest(R.layout.layout_four);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout5() throws java.lang.Exception {
        inflateTest(R.layout.layout_five);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout6() throws java.lang.Exception {
        inflateTest(R.layout.layout_six);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCachedLayout1() throws java.lang.Exception {
        inflateCachedTest(R.layout.layout_one);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCachedLayout2() throws java.lang.Exception {
        inflateCachedTest(R.layout.layout_two);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCachedLayout3() throws java.lang.Exception {
        inflateCachedTest(R.layout.layout_three);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCachedLayout4() throws java.lang.Exception {
        inflateCachedTest(R.layout.layout_four);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCachedLayout5() throws java.lang.Exception {
        inflateCachedTest(R.layout.layout_five);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testCachedLayout6() throws java.lang.Exception {
        inflateCachedTest(R.layout.layout_six);
    }

    // public void testLayoutTag() throws Exception {
    // public void setUp
    // (Context
    // context){
    // setUp(context, R.layout.layout_tag);
    // }
    // public void run
    // ()
    // {
    // super.run();
    // if (!"MyTag".equals(mView.getTag())) {
    // throw new RuntimeException("Incorrect tag: " + mView.getTag());
    // }
    // }
    // }
    public static class ViewOne extends android.view.View {
        public ViewOne(android.content.Context context) {
            super(context);
        }

        public ViewOne(android.content.Context context, android.util.AttributeSet attrs) {
            super(context, attrs);
        }
    }
}

