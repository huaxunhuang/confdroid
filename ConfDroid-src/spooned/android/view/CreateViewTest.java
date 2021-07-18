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


public class CreateViewTest extends android.test.AndroidTestCase implements android.test.PerformanceTestCase {
    public boolean isPerformanceOnly() {
        return false;
    }

    public int startPerformance(android.test.PerformanceTestCase.Intermediates intermediates) {
        return 0;
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout1() throws java.lang.Exception {
        new android.view.CreateViewTest.ViewOne(mContext);
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout2() throws java.lang.Exception {
        android.widget.LinearLayout vert = new android.widget.LinearLayout(mContext);
        vert.addView(new android.view.CreateViewTest.ViewOne(mContext), new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout3() throws java.lang.Exception {
        android.widget.LinearLayout vert = new android.widget.LinearLayout(mContext);
        android.view.CreateViewTest.ViewOne one = new android.view.CreateViewTest.ViewOne(mContext);
        vert.addView(one, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0));
        android.view.CreateViewTest.ViewOne two = new android.view.CreateViewTest.ViewOne(mContext);
        vert.addView(two, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0));
        android.view.CreateViewTest.ViewOne three = new android.view.CreateViewTest.ViewOne(mContext);
        vert.addView(three, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0));
        android.view.CreateViewTest.ViewOne four = new android.view.CreateViewTest.ViewOne(mContext);
        vert.addView(four, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0));
        android.view.CreateViewTest.ViewOne five = new android.view.CreateViewTest.ViewOne(mContext);
        vert.addView(five, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0));
        android.view.CreateViewTest.ViewOne six = new android.view.CreateViewTest.ViewOne(mContext);
        vert.addView(six, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT, 0));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout4() throws java.lang.Exception {
        android.widget.TextView text = new android.widget.TextView(mContext);
        text.setText("S");
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout5() throws java.lang.Exception {
        android.widget.TextView text = new android.widget.TextView(mContext);
        text.setText("S");
        android.widget.LinearLayout vert = new android.widget.LinearLayout(mContext);
        vert.addView(text, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    }

    @android.test.suitebuilder.annotation.SmallTest
    public void testLayout6() throws java.lang.Exception {
        android.widget.LinearLayout vert = new android.widget.LinearLayout(mContext);
        android.widget.TextView one = new android.widget.TextView(mContext);
        one.setText("S");
        vert.addView(one, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        android.widget.TextView two = new android.widget.TextView(mContext);
        two.setText("M");
        vert.addView(two, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        android.widget.TextView three = new android.widget.TextView(mContext);
        three.setText("T");
        vert.addView(three, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        android.widget.TextView four = new android.widget.TextView(mContext);
        four.setText("W");
        vert.addView(four, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        android.widget.TextView five = new android.widget.TextView(mContext);
        five.setText("H");
        vert.addView(five, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
        android.widget.TextView six = new android.widget.TextView(mContext);
        six.setText("F");
        vert.addView(six, new android.widget.LinearLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0));
    }

    public static class ViewOne extends android.view.View {
        public ViewOne(android.content.Context context) {
            super(context);
        }
    }
}

